package ylescort.ylmobileandroid;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.reflect.TypeToken;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;
import TaskClass.BaseEmp;
import YLDataService.BaseBoxDBSer;
import YLDataService.BaseEmpDBSer;
import YLDataService.TasksManagerDBSer;
import YLDataService.WebServerBaseData;
import YLSystemDate.HandsetInfo;
import YLSystemDate.YLHandSetBaseData;
import YLSystemDate.YLMediaPlayer;
import YLSystemDate.YLRecord;
import YLSystemDate.YLSysTime;
import YLSystemDate.YLSystem;
import TaskClass.User;
import YLWebService.UpdateManager;


public class Login extends YLBaseActivity implements View.OnClickListener {


    private EditText Log_ET_Name;
    private EditText Log_ET_PassWord;
    private TextView log_tv_vision;
    private TextView log_tv_hsimei;
    private Switch logic_sw_address;

    private YLMediaPlayer ylMediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        InitLayout();
        InitData();
    }

    @Override
    public void HandSetHotKey(int arg) {
        MyLog(arg + "HOTKEY");
        try {
            switch (arg) {
                case 131:
                    LoginByPassword();
                    YLRecord.WriteRecord("登录界面", "密码按键" + Log_ET_Name.getText());
                    break;
                case 132:
                    LoginByHF();
                    YLRecord.WriteRecord("登录界面", "HF按键");
                    break;
                case 21:
                    LoginByPassword();
                    break;
                case 22:
                    LoginByHF();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void InitLayout() {
        Log_ET_Name = (EditText) findViewById(R.id.Log_ET_Name);
        Log_ET_PassWord = (EditText) findViewById(R.id.Log_ET_PassWord);
        log_tv_vision = (TextView) findViewById(R.id.log_tv_vision);
        log_tv_hsimei = (TextView) findViewById(R.id.log_tv_hsimei);
        Button Log_BN_HF = (Button) findViewById(R.id.Log_BN_HF);
        logic_sw_address = (Switch) findViewById(R.id.logic_sw_address);
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
    protected void InitData() {

        ylMediaPlayer = new YLMediaPlayer(getApplicationContext());

        //正式服务测试服务
        //正式checked为false
        //测试checked为true
//        logic_sw_address.setVisibility(View.VISIBLE);

//        logic_sw_address.setChecked(true);

        if (logic_sw_address.isChecked()) {
            YLSystem.setSerAdress("0");
        } else {
            YLSystem.setSerAdress("1");
        }
        String string = "版本号："+YLHandSetBaseData.getYLVersion()+"\r\n"
                +"手持机："+YLHandSetBaseData.getHandSetSN();
        String count ="款箱数量:    "+ YLBoxcount();
        log_tv_vision.setText(string);
        log_tv_hsimei.setText(count);

        YLSystem.setHandsetIMEI(
                        YLHandSetBaseData.getHandSetSN()
                + "-" + YLHandSetBaseData.getHandSetIMEI()
                + "-" + YLHandSetBaseData.getSIMIMEI()
                + "-" +YLHandSetBaseData.getYLVersion());

    }

    @Override
    public void onClick(View view) {
        try {
            switch (view.getId()) {
                case R.id.Log_BN_HF:

                    LoginByHF();

//                    String user = "500008";
//                    Log_ET_Name.setText(user);
//                    Log_ET_PassWord.setText(user);
//                    LoginByPassword();

                    YLRecord.WriteRecord("登录界面","HF登录");
                    break;
                case R.id.Log_BN_Ent:

//                    String user2 = "520037";
//                    Log_ET_Name.setText(user2);
//                    Log_ET_PassWord.setText(user2);

                    LoginByPassword();
                    YLRecord.WriteRecord("登录界面","帐号登录"+Log_ET_Name.getText());
                    break;
                case R.id.btnTest1:

                    UpDataAPK();

//                    DeleteTaskbyDate();

                    YLRecord.WriteRecord("登录界面","升级");
                    break;
                case R.id.btnTest2:
                    Intent intent = new Intent();
                    intent.setClass(Login.this, KimTest.class);
                    startActivity(intent);
                    YLRecord.WriteRecord("登录界面","进入测试界面");
                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                    break;
                case R.id.logic_sw_address:
                    if (logic_sw_address.isChecked()) {
                        YLSystem.setSerAdress("0");
                    } else {
                        YLSystem.setSerAdress("1");
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //测试删除日期任务
    private void DeleteTaskbyDate() {
        TasksManagerDBSer tasksManagerDBSer = new TasksManagerDBSer(getApplicationContext());
        tasksManagerDBSer.DeleteTasksManagerbydate("2016-08-12");
    }


    //版本更新
    private void UpDataAPK() {
        if (!YLSystem.getNetWorkState().equals("1")) return;
        final EditText editText = new EditText(this);
        editText.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
        InputMethodManager inputManager = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(editText, 0);
        new AlertDialog.Builder(this).setTitle("请输入升级密码:")
                .setIcon(android.R.drawable.ic_dialog_info).setView(editText)
                .setPositiveButton("升级", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (editText.getText().toString().equals("9")) {
                            UpdateManager um = new UpdateManager(Login.this);
                            um.check();
                        }
                    }
                }).setNegativeButton("取消", null).show();
    }

    private int YLBoxcount(){
        return  (new BaseBoxDBSer(Login.this)).BaseBoxCount();
    }

    //HF登陆
    private void LoginByHF() {
        String hfcode = HFReadUID();
        if (hfcode.equals(""))return;
        try {
            if (YLSystem.getNetWorkState() == null ||
                    YLSystem.getNetWorkState().equals("2")){
                BaseEmpDBSer baseEmpDBSer = new BaseEmpDBSer(getApplicationContext());
                List<BaseEmp> baseEmpList = baseEmpDBSer.GetBaseEmps("where EmpHFNo ='" + hfcode + "'");
                FindEmpByLocal(baseEmpList);
            }else{
                JSONObject jsonObject = new JSONObject();
                User user = new User();
                user.setEmpNO(hfcode);
                user.setDeviceID(YLSystem.getHandsetIMEI());
                user.setISWIFI(YLSystem.getNetWorkState());
                jsonObject.put("user",gson.toJson(user));
                String url = YLSystem.GetBaseUrl(getApplicationContext())+"LoginByHF";

                YLWebDataAsyTaskForeground yf = new YLWebDataAsyTaskForeground(jsonObject,
                        url,2) {
                    @Override
                    protected void onPostExecute(String s) {
                        YLProgressDialog.dismiss();
                        if (s.equals(""))return;
                        User yluser = new User();
                        yluser = gson.fromJson(s,new TypeToken<User>(){}.getType());
                        if (yluser.getServerReturn().equals("没有此人或密码错误。")) {
                            ylMediaPlayer.SuccessOrFail(false);
                            Toast.makeText(getApplicationContext(),"登录失败",Toast.LENGTH_SHORT).show();
                        }else {
                            Sertime(yluser);
                            GetEmpByServer(yluser);
                        }
                    }
                };
                yf.execute();
                yf.doInBackground();
                CacheData();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void GetEmpByServer(User yluser) {
        MyLog(yluser.toString());
        if (yluser.getServerReturn().equals("1")) {
            yluser.setISWIFI(YLSystem.getNetWorkState());
            yluser.setDeviceID(YLSystem.getHandsetIMEI());
            YLSystem.setUser(yluser);
            YLSystem.setBaseName(yluser.getTaskDate());
            Intent intent = new Intent();
            String EmpWorkState = GetEmpPost(yluser.getEmpID());
            if (EmpWorkState.equals("金库主管") || EmpWorkState.equals("库管员")
                    || EmpWorkState.equals("部门经理")) {
                intent.setClass(Login.this, VaultInOrOut.class);
            } else {
                intent.setClass(Login.this, Task.class);
            }
            startActivity(intent);
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            ylMediaPlayer.SuccessOrFail(true);
            YLRecord.WriteRecord("登录界面","联网登录："+ yluser.getEmpID());
            Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_SHORT).show();
        } else {
            ylMediaPlayer.SuccessOrFail(false);
            Toast.makeText(getApplicationContext(), "登录失败", Toast.LENGTH_SHORT).show();
        }

    }

    private String GetEmpPost(String empID) {
        BaseEmpDBSer baseEmpDBSer = new BaseEmpDBSer(getApplicationContext());
        List<BaseEmp> baseEmpList = baseEmpDBSer.GetBaseEmps("where EmpID='" + empID + "'");
        if (!baseEmpList.toString().equals("[]")) {
            return baseEmpList.get(0).EmpWorkState;
        } else {
            return "none";
        }
    }

    private void FindEmpByLocal(List<BaseEmp> baseEmpList) {
        if (baseEmpList.size() > 0) {
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
                    || baseEmp.EmpWorkState.equals("库管员")
                    || baseEmp.EmpName.equals("吴艳")) {
                intent.setClass(Login.this, VaultInOrOut.class);
            } else {
                intent.setClass(Login.this, Task.class);
            }
            YLRecord.WriteRecord("登录界面","离线登录："+ user.getEmpID());
            startActivity(intent);
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        } else {
            Toast.makeText(getApplicationContext(), "无此人员信息", Toast.LENGTH_SHORT).show();
        }
    }

    //缓存数据
    private void CacheData()  {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String cache = YLHandSetBaseData.getCacheDatetime();
                    if (!cache.equals("ALL")){
                        WebServerBaseData webServerBaseData = new WebServerBaseData();
                        webServerBaseData.GetBaseData(getApplicationContext(), cache);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    //密码登陆
    private void LoginByPassword()throws Exception {
//        CacheData();
        if (Log_ET_Name.getText().toString().equals("")|| Log_ET_PassWord.getText().toString().equals(""))return;
        if (YLSystem.getNetWorkState() == null
                || YLSystem.getNetWorkState().equals("2")){
            BaseEmpDBSer baseEmpDBSer=new BaseEmpDBSer(getApplicationContext());
            List<BaseEmp> baseEmpList = baseEmpDBSer.GetBaseEmps("where EmpNo ='" + Log_ET_Name.getText().toString() + "'");
            FindEmpByLocal(baseEmpList);
        }else{
            JSONObject jsonObject = new JSONObject();
            User user = new User();
            user.setEmpNO(Log_ET_Name.getText().toString());
            user.setPass(YLSystem.SetMD5(Log_ET_PassWord.getText().toString()));
            user.setDeviceID(YLSystem.getHandsetIMEI());
            user.setISWIFI(YLSystem.getNetWorkState());
            jsonObject.put("user",gson.toJson(user));
            String url = YLSystem.GetBaseUrl(getApplicationContext())+"Login1";

            YLWebDataAsyTaskForeground y = new YLWebDataAsyTaskForeground(jsonObject,url,2) {
                @Override
                protected void onPostExecute(String s) {
                    YLProgressDialog.dismiss();
                    if (s.equals(""))return;
                    User yluser = new User();
                    yluser = gson.fromJson(s,new TypeToken<User>(){}.getType());
                    if (yluser.getServerReturn().equals("没有此人或密码错误。")) {
                        ylMediaPlayer.SuccessOrFail(false);
                        Toast.makeText(getApplicationContext(),"登录失败",Toast.LENGTH_SHORT).show();
                    }else {
                        Sertime(yluser);
                        GetEmpByServer(yluser);
                    }
                }
            };
            y.execute();
            y.doInBackground();
            CacheData();
        }
    }

    private void Sertime(final User user) {
        YLSysTime ylSysTime = new YLSysTime(getApplicationContext());
        ylSysTime.Sertime(user.getTime());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent();
            intent.setClass(getApplicationContext(), setup.class);
            startActivity(intent);
            YLRecord.WriteRecord("登录界面","页面设置");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostResume() {
        if (YLHandSetBaseData.getHandSetSN() == null){
            try {
                HandsetInfo h = new HandsetInfo(getApplicationContext());
                log_tv_vision.setText(h.getupdateinfo());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        super.onPostResume();
    }
}
