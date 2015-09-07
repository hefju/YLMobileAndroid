package ylescort.ylmobileandroid;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.hardware.input.InputManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.hdhe.nfc.NFCcmdManager;
import com.example.nfc.util.Tools;

import java.io.DataOutputStream;
import java.util.Calendar;
import java.util.List;

import TaskClass.BaseEmp;
import TaskClass.User;
import YLDataService.BaseEmpDBSer;
import YLDataService.WebService;
import YLSystemDate.YLMediaPlayer;
import YLSystemDate.YLSysTime;
import YLSystemDate.YLSystem;
import YLWebService.UpdateManager;

/**
 * Created by Administrator on 2015/4/14.
 */
public class LoginActivity extends ActionBarActivity implements View.OnClickListener {

    private EditText Log_ET_Name;
    private EditText Log_ET_PassWord;
    private TextView log_tv_vision;
    private TextView log_tv_hsimei;
    private Switch logic_sw_address;
    private Button Log_BN_HF;
    private NFCcmdManager manager ;
    private boolean buttonflag;
    private YLMediaPlayer ylMediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        InitLayout();
        InitHFreader();
        InitData();
    }

    private void InitData()  {
        try {
            String oper ="";
            if (YLSystem.getHFport() == 13){
                oper = "业务员端";
            }else{
                oper = "库管员端";
            }
            LoginActivity.this.setTitle("粤龙保安押运--" + oper);
            int b =  getResources().getColor(R.color.androidbluel);//得到配置文件里的颜色
            String ylvision = getVersionName();
            log_tv_vision.setTextColor(b);
            Log.e(YLSystem.getKimTag(), b + "");
            log_tv_vision.setText("版本号:"+ylvision);
        } catch (Exception e) {
            e.printStackTrace();
        }
        buttonflag = false;

        ylMediaPlayer = new YLMediaPlayer();

        /**
         * 获取手机IMEI码
         */
        String srvName = Context.TELEPHONY_SERVICE;
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(srvName);
        String IMEI = telephonyManager.getDeviceId();
        String SIM = telephonyManager.getSimSerialNumber();
        YLSystem.setHandsetIMEI(IMEI+"-"+SIM);

        log_tv_hsimei.setText("机器码:" + IMEI + "\r\n" + "SIM卡：" + SIM);

        //正式服务测试服务正式为checked为false
        logic_sw_address.setChecked(true);

        if (logic_sw_address.isChecked()){
            YLSystem.setSerAdress("0");
        }else {
            YLSystem.setSerAdress("1");
        }
    }

    private String getVersionName() throws Exception{
        // 获取packagemanager的实例
        PackageManager packageManager = getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(),0);
        return packInfo.versionName;
    }

    private void InitHFreader() {
        try{
            manager = NFCcmdManager.getNFCcmdManager(YLSystem.getHFport(), 115200, 0);
            manager.readerPowerOn();
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "HF初始化失败", Toast.LENGTH_SHORT).show();
        }
    }

    private void InitLayout() {
        /**
         增加对网络接入判断
         */
        Intent i=new Intent(getApplicationContext(),YLNetWorkStateService.class);
        startService(i);

        Log_ET_Name = (EditText)findViewById(R.id.Log_ET_Name);
        Log_ET_PassWord = (EditText)findViewById(R.id.Log_ET_PassWord);
        log_tv_vision = (TextView) findViewById(R.id.log_tv_vision);
        log_tv_hsimei = (TextView) findViewById(R.id.log_tv_hsimei);
        Log_BN_HF = (Button) findViewById(R.id.Log_BN_HF);
        logic_sw_address = (Switch)findViewById(R.id.logic_sw_address);
        Button Log_BN_Ent = (Button) findViewById(R.id.Log_BN_Ent);
        Button btnTest1 = (Button) findViewById(R.id.btnTest1);
        Button btnTest2 = (Button) findViewById(R.id.btnTest2);
        Log_BN_HF.setOnClickListener(this);
        Log_BN_Ent.setOnClickListener(this);
        btnTest1.setOnClickListener(this);
        btnTest2.setOnClickListener(this);
        logic_sw_address.setOnClickListener(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode){
            case 131:
                try {
                    LoginByPassword();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 132:
                try {
                    LoginByHF();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent();
            intent.setClass(getApplicationContext(), setup.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.Log_BN_HF:
                try {
                    LoginByHF();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.Log_BN_Ent:
                try {
                    LoginByPassword();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btnTest1:UpDataAPK();
                break;
            case R.id.btnTest2:
//                Intent intent = new Intent();
//                intent.setClass(LoginActivity.this, KimTest.class);
//                startActivity(intent);

                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.ylboxstautandtasktype,
                        (ViewGroup) findViewById(R.id.dialog));

                new AlertDialog.Builder(this).setTitle("请选择款箱交接状态").setView(layout)
                        .setPositiveButton("确定", null)
                        .setNegativeButton("取消", null).show();


                break;
            case R.id.logic_sw_address:
                if (logic_sw_address.isChecked()){
                    YLSystem.setSerAdress("0");
                }else {
                    YLSystem.setSerAdress("1");
                }
                break;
        }
    }

    private void LoginByPassword() throws Exception{
        if (buttonflag){
            buttonflag= true;
            return;
        }
        if (!YLSystem.getNetWorkState().equals("2")){
            WebService.CacheData(getApplicationContext());
            String url = YLSystem.GetBaseUrl(getApplicationContext())+"Login1";
            User user = new User();
            user.setEmpNO(Log_ET_Name.getText().toString());
            user.setPass(YLSystem.SetMD5(Log_ET_PassWord.getText().toString()));
            WebService webService = new WebService();
            User userfromweb = webService.LogicBypassword(user, url);
            Log.e(YLSystem.getKimTag(), userfromweb.toString());
            YLSysTime ylSysTime = new YLSysTime();
            ylSysTime.CheckLocateTime(userfromweb.getTime());
            YLSystem.setBaseName(userfromweb.getTaskDate());
            GetEmpByServer(userfromweb);
        }else {
            String userNo = Log_ET_Name.getText().toString();
            BaseEmpDBSer baseEmpDBSer = new BaseEmpDBSer(getApplicationContext());
            List<BaseEmp> baseEmpList = baseEmpDBSer.GetBaseEmps("where EmpNo ='"+userNo+"'" );
            FindEmpByLocal(baseEmpList);
        }
    }

    private void LoginByHF() throws Exception {
        if (buttonflag){
            buttonflag= true;
            return;}
        Log_BN_HF.setEnabled(false);
        manager.init_14443A();
        byte[] uid = manager.inventory_14443A();
        if(uid != null){
            if (!YLSystem.getNetWorkState().equals("2")){
                WebService.CacheData(getApplicationContext());
                String url = YLSystem.GetBaseUrl(getApplicationContext())+"LoginByHF";
                User user = new User();
                user.setEmpNO(Tools.Bytes2HexString(uid, uid.length));
                WebService webService = new WebService();
                User userfromweb = webService.LogicByHF(user,url);
                Log.e(YLSystem.getKimTag(), userfromweb.toString());
                YLSysTime ylSysTime = new YLSysTime();
                ylSysTime.CheckLocateTime(userfromweb.getTime());
                YLSystem.setBaseName(userfromweb.getTaskDate());
                if (userfromweb.getServerReturn().equals("没有此人或密码错误。")){
//                    YLMediaPlay("faile");
                    ylMediaPlayer.SuccessOrFailMidia("faile",getApplicationContext());
                    buttonflag= false;
                    Log_BN_HF.setEnabled(true);
                }else {
                    GetEmpByServer(userfromweb);
                }

            }else {
                String userNo = Tools.Bytes2HexString(uid, uid.length);
                BaseEmpDBSer baseEmpDBSer = new BaseEmpDBSer(getApplicationContext());
                List<BaseEmp> baseEmpList = baseEmpDBSer.GetBaseEmps("where EmpHFNo ='"+userNo+"'" );
                FindEmpByLocal(baseEmpList);
            }
        }else {
            Toast.makeText(getApplicationContext(), "未找到卡", Toast.LENGTH_SHORT).show();
            buttonflag= false;
            Log_BN_HF.setEnabled(true);
        }
    }

    private void GetEmpByServer(User userfromweb) throws Exception {
        if (userfromweb.getServerReturn().equals("1")){

            Log_BN_HF.setEnabled(true);
            userfromweb.setISWIFI(YLSystem.getNetWorkState());
            userfromweb.setDeviceID(YLSystem.getHandsetIMEI());
            YLSystem.setUser(userfromweb);
//            if (YLSysTime.getServertime() == null){
//                Intent TimeSerintent = new Intent(getApplicationContext(),SerTimeService.class);
//                startService(TimeSerintent);
//            }
            Intent intent = new Intent();
            String EmpWorkState = GetEmpPost(userfromweb.getEmpID());
            if (EmpWorkState.equals("金库主管")||EmpWorkState.equals("库管员")
                    ||EmpWorkState.equals("部门经理")){
                intent.setClass(LoginActivity.this, VaultInOrOut.class);
            }else {
                intent.setClass(LoginActivity.this, Task.class);
            }
            startActivity(intent);
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            ylMediaPlayer.SuccessOrFailMidia("success", getApplicationContext());
            Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_SHORT).show();
        }else {
            ylMediaPlayer.SuccessOrFailMidia("faile", getApplicationContext());
            buttonflag= false;
            Log_BN_HF.setEnabled(true);
            Toast.makeText(getApplicationContext(),"登录失败",Toast.LENGTH_SHORT).show();
        }
    }

    private void FindEmpByLocal(List<BaseEmp> baseEmpList) {
        if (baseEmpList.size()>0){
            BaseEmp baseEmp = baseEmpList.get(0);
            User user = new User();
            user.setEmpNO(baseEmp.EmpNo);
            user.setEmpID(baseEmp.EmpID);
            user.setPass("");
            user.setName(baseEmp.EmpName);
            user.setISWIFI("0");
            user.setTaskDate("");
            YLSystem.setUser(user);
            Intent intent = new Intent();
            if (baseEmp.EmpWorkState.equals("金库主管")
                    ||baseEmp.EmpWorkState.equals("库管员")
                    ||baseEmp.EmpName.equals("吴艳")){
                intent.setClass(LoginActivity.this, VaultInOrOut.class);
            }else {
            intent.setClass(LoginActivity.this, Task.class);}
            startActivity(intent);
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            buttonflag= false;
            Log_BN_HF.setEnabled(true);
        }else {
            buttonflag= false;
            Log_BN_HF.setEnabled(true);
            Toast.makeText(getApplicationContext(), "无此人员信息", Toast.LENGTH_SHORT).show();
        }
    }

    private void UpDataAPK() {
        if (!YLSystem.getNetWorkState().equals("1"))return;
        final EditText editText = new EditText(this);
        editText.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
        InputMethodManager inputManager = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(editText,0);
        new AlertDialog.Builder(this).setTitle("请输入升级密码:")
                .setIcon(android.R.drawable.ic_dialog_info).setView(editText)
                .setPositiveButton("升级", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (editText.getText().toString().equals("9")) {
                            UpdateManager um = new UpdateManager(LoginActivity.this);
                            um.check();
                        }
                    }
                }).setNegativeButton("取消", null).show();
    }

    private String GetEmpPost(String EmpID){
        BaseEmpDBSer baseEmpDBSer = new BaseEmpDBSer(getApplicationContext());
        List<BaseEmp> baseEmpList = baseEmpDBSer.GetBaseEmps("where EmpID='"+EmpID+"'");
        if (!baseEmpList.toString().equals("[]")){
            return  baseEmpList.get(0).EmpWorkState;
        }else {
            return "none";
        }
    }

    @Override
    protected void onStop() {
        manager.readerPowerOff();
        super.onStop();
    }

    @Override
    protected void onPostResume() {
        manager.readerPowerOn();
        super.onPostResume();
    }
}
