package ylescort.ylmobileandroid;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.hdhe.nfc.NFCcmdManager;
import com.example.nfc.util.Tools;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import TaskClass.BaseEmp;
import TaskClass.TasksManager;
import TaskClass.User;
import YLDataService.BaseEmpDBSer;
import YLDataService.WebService;
import YLSystemDate.YLSystem;
import YLWebService.UpdateManager;

/**
 * Created by Administrator on 2015/4/14.
 */
public class LoginActivity extends ActionBarActivity implements View.OnClickListener {

    private EditText Log_ET_Name;
    private EditText Log_ET_PassWord;
    private TextView log_tv_vision;
    private Button Log_BN_HF;
    private NFCcmdManager manager ;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        InitLayout();
        InitHFreader();
        InitData();
    }

    private void InitData() {
        try {
            int b =  getResources().getColor(R.color.dodgerblue);//得到配置文件里的颜色
            String ylvision = getVersionName();
            log_tv_vision.setTextColor(b);
            log_tv_vision.setText("版本号:"+ylvision);
        } catch (Exception e) {
            e.printStackTrace();
        }

        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle("提示");
        progressDialog.setMessage("系统登陆中");
        progressDialog.setIcon(R.drawable.ylescort);
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.setButton("返回",new CanButton());
    }

    private String getVersionName() throws Exception{
        // 获取packagemanager的实例
        PackageManager packageManager = getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(),0);
        String version = packInfo.versionName;
        return version;
    }

    private void InitHFreader() {
        try{
            manager = NFCcmdManager.getNFCcmdManager(13, 115200, 0);
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
        Log_BN_HF = (Button) findViewById(R.id.Log_BN_HF);
        Button Log_BN_Ent = (Button) findViewById(R.id.Log_BN_Ent);
        Button btnTest1 = (Button) findViewById(R.id.btnTest1);
        Button btnTest2 = (Button) findViewById(R.id.btnTest2);
        Log_BN_HF.setOnClickListener(this);
        Log_BN_Ent.setOnClickListener(this);
        btnTest1.setOnClickListener(this);
        btnTest2.setOnClickListener(this);
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
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, KimTest.class);
                startActivity(intent);
                break;
        }
    }

    private void LoginByPassword() throws Exception{
        if (!YLSystem.getNetWorkState().equals("2")){
            String url = YLSystem.GetBaseUrl(getApplicationContext())+"Login1";
            User user = new User();
            user.setEmpNO(Log_ET_Name.getText().toString());
            user.setPass(YLSystem.md5(Log_ET_PassWord.getText().toString()));
            WebService webService = new WebService();
            User userfromweb = webService.LogicBypassword(user, url);
            if (userfromweb.getServerReturn().equals("1")){
                //更新缓存
                WebService.CacheData(getApplicationContext());
                userfromweb.setISWIFI(YLSystem.getNetWorkState());
                YLSystem.setUser(userfromweb);
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, Task.class);
                startActivity(intent);
                YLMediaPlay("success");
                Toast.makeText(getApplicationContext(),"登录成功",Toast.LENGTH_SHORT).show();
            }else {
                YLMediaPlay("faile");
                Toast.makeText(getApplicationContext(),"登录失败",Toast.LENGTH_SHORT).show();
            }
        }else {
            String userNo = Log_ET_Name.getText().toString();
            BaseEmpDBSer baseEmpDBSer = new BaseEmpDBSer(getApplicationContext());
            List<BaseEmp> baseEmpList = baseEmpDBSer.GetBaseEmps("where EmpNo ='"+userNo+"'" );
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
                intent.setClass(LoginActivity.this, Task.class);
                startActivity(intent);

            }else {
                Log_BN_HF.setEnabled(true);
                Toast.makeText(getApplicationContext(), "无此人员信息", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void LoginByHF() throws Exception {
        if (!Log_BN_HF.isEnabled()){
            return;
        }
        Log_BN_HF.setEnabled(false);
        manager.init_14443A();
        byte[] uid = manager.inventory_14443A();
        if(uid != null){
            if (!YLSystem.getNetWorkState().equals("2")){
                String url = YLSystem.GetBaseUrl(getApplicationContext())+"LoginByHF";
                User user = new User();
                user.setEmpNO(Tools.Bytes2HexString(uid, uid.length));
                WebService webService = new WebService();
                User userfromweb = webService.LogicByHF(user,url);
                if (userfromweb.getServerReturn().equals("1")){
                    //更新缓存
                    WebService.CacheData(getApplicationContext());
                    Log_BN_HF.setEnabled(true);
                    userfromweb.setISWIFI(YLSystem.getNetWorkState());
                    YLSystem.setUser(userfromweb);
                    Intent intent = new Intent();
                    intent.setClass(LoginActivity.this, Task.class);
                    startActivity(intent);
                    YLMediaPlay("success");
                    Toast.makeText(getApplicationContext(),"登录成功",Toast.LENGTH_SHORT).show();
                }else {
                    YLMediaPlay("faile");
                    Log_BN_HF.setEnabled(true);
                    Toast.makeText(getApplicationContext(),"登录失败",Toast.LENGTH_SHORT).show();
                }
            }else {
                String userNo = Tools.Bytes2HexString(uid, uid.length);
                BaseEmpDBSer baseEmpDBSer = new BaseEmpDBSer(getApplicationContext());
                List<BaseEmp> baseEmpList = baseEmpDBSer.GetBaseEmps("where EmpHFNo ='"+userNo+"'" );
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
                    intent.setClass(LoginActivity.this, Task.class);
                    startActivity(intent);

                }else {
                    Log_BN_HF.setEnabled(true);
                    Toast.makeText(getApplicationContext(), "无此人员信息", Toast.LENGTH_SHORT).show();
                }
            }
        }else {
            Toast.makeText(getApplicationContext(), "未寻到卡", Toast.LENGTH_SHORT).show();
            Log_BN_HF.setEnabled(true);
        }
    }

    private void UpDataAPK() {
        if (!YLSystem.getNetWorkState().equals("1"))return;
        UpdateManager um=new UpdateManager(LoginActivity.this);
        um.check();
    }

    private void YLMediaPlay(String mediavoice) throws Exception{
        MediaPlayer mPlayer = new MediaPlayer();

        if (mediavoice.equals("success")){
            mPlayer = MediaPlayer.create(LoginActivity.this, R.raw.msg);
            if(mPlayer.isPlaying()){
                return;
            }
        }else {
            mPlayer.setDataSource("/system/media/audio/notifications/Proxima.ogg");  //选用系统声音文件
            mPlayer.prepare();
        }
        mPlayer.start();
    }

    private class CanButton implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            progressDialog.dismiss();
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
