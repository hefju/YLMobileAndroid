package ylescort.ylmobileandroid;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.hdhe.nfc.NFCcmdManager;
import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import TaskClass.Site;
import TaskClass.TasksManager;
import TaskClass.User;
import TaskClass.YLATM;
import TaskClass.YLTask;
import YLSystemDate.YLEditData;
import YLSystemDate.YLSystem;
import adapter.YLATMSiteAdapter;
import adapter.YLSiteAdapter;

public class YLATMList extends ActionBarActivity {

    private Button ATMlist_btn_Start;//开始按钮
    private Button ATMlist_btn_End;//结束按钮
    private Button ATMlist_btn_Addatm;//添加按钮
    private Button ATMlist_btn_UpDateatm;//上传按钮
    private ListView ATMlist_listview;//列表

    private TextView ATMlist_tv_title;//标题
    private TextView ATMlist_tv_starttime;//任务开始
    private TextView ATMlist_tv_endtime;//任务结束

    private List<YLATM> ylatmList;

    private TasksManager tasksManager = null;//任务管理类
    private YLTask ylTask;//当前选中的任务
    private Scan1DRecive ATMscan1DRecive;  //广播接收者
    private FileOutputStream fos;

    private NFCcmdManager HFmanager ;

    private String Dialogtype;
    private boolean Dialogflag;

    private FunkeyListener funkeyReceive; //功能键广播接收者



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ylatmlist);
        InitLayout();
        InitData();
        InitHFreader();
        InitReciveScan1D();
        KeyBroad();
    }

    private void InitHFreader() {
        try{
            HFmanager = NFCcmdManager.getNFCcmdManager(13, 115200, 0);
            HFmanager.readerPowerOn();
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"HF初始化失败",Toast.LENGTH_SHORT).show();
        }
    }

    private void InitReciveScan1D() {
        ATMscan1DRecive = new Scan1DRecive();
        IntentFilter filter = new IntentFilter();
        filter.addAction("ylescort.ylmobileandroid.YLATMList");
        registerReceiver(ATMscan1DRecive,filter);
        Intent start = new Intent(YLATMList.this,Scan1DService.class);
        YLATMList.this.startService(start);
    }

    private class Scan1DRecive extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String recivedata = intent.getStringExtra("result");
            if (recivedata != null){
                GetATMsite(recivedata);}
        }
    }

    private void GetATMsite(String recivedata) {
        if (ylatmList == null){
            ylatmList = new ArrayList<>();
        }

        YLATM ylatm = new YLATM();
        ylatm.setId(1);
        ylatm.setServerReturn("1");
        ylatm.setTaskID("1");
        ylatm.setSiteID("1");
        ylatm.setSiteName("思密达");
        ylatm.setSiteType("未交接");
        ylatm.setTradeBegin("");
        ylatm.setTradeEnd("");
        ylatm.setATMCount("1");
        ylatm.setTimeID(0);
        ylatmList.add(ylatm);
        DisplayATMSite(ylatmList);
    }

    private void DisplayATMSite(List<YLATM> ylatmList) {
        if (ylatmList == null || ylatmList.size() < 1)return;
        YLATMSiteAdapter ylatmSiteAdapter = new YLATMSiteAdapter(this,ylatmList,R.layout.activity_atmsiteitem);
        ATMlist_listview.setAdapter(ylatmSiteAdapter);
    }

    public  String replaceBlank(String str) {
        String dest = "";
        if (str!=null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    private void SendScan1Dcmd(){
        String activity = "ylescort.ylmobileandroid.YLATMList";
        Intent ac = new Intent();
        ac.setAction("ylescort.ylmobileandroid.Scan1DService");
        ac.putExtra("activity", activity);
        sendBroadcast(ac);
        Intent sendToservice = new Intent(YLATMList.this, Scan1DService.class); // 用于发送指令

        sendToservice.putExtra("cmd", "scan");

        this.startService(sendToservice); // 发送指令
    }

    public void ATMScan1D(View view){
        Dialogtype = "ATMScan1D";
        SendScan1Dcmd();
    }

    public void ATMListHFTaskstartClick(View view) {
        Dialogtype = "ATMListHFTaskstartClick";
        ATMListHFreader("taskstart");
    }

    public void ATMListHFTaskendClick(View view) {
        Dialogtype = "ATMListHFTaskendClick";
        ATMListHFreader("taskend");
    }

    private void ATMListHFreader(String TaskStartEnd) {
        byte[] uid ;
        HFmanager.init_14443A();
        uid = HFmanager.inventory_14443A();
        if (uid != null){
            String taskstarttime;
            String taskendttime;
            if (TaskStartEnd.equals("taskstart")){
                if (ylTask.getTaskATMBeginTime() == null){
                ylTask.setTaskATMBeginTime(GetCurrDateTime("date"));}
            }else {
                if (ylTask.getTaskATMEndTime() == null){
                ylTask.setTaskATMEndTime(GetCurrDateTime("date"));}
            }
            taskstarttime = "任务开始:"+ylTask.getTaskATMBeginTime();
            taskendttime = "任务结束:"+ylTask.getTaskATMEndTime();

            if (taskstarttime.equals("任务开始:null")){
                taskstarttime = "任务开始:";
            }
            if (taskendttime.equals("任务结束:null")){
                taskendttime = "任务结束:";
            }
            ATMlist_tv_starttime.setText(taskstarttime);
            ATMlist_tv_endtime.setText(taskendttime);
        }
    }

    private void InitLayout() {
        ATMlist_btn_Start = (Button) findViewById(R.id.ATMlist_btn_Start);
        ATMlist_btn_End = (Button) findViewById(R.id.ATMlist_btn_End);
        ATMlist_btn_Addatm = (Button) findViewById(R.id.ATMlist_btn_Addatm);
        ATMlist_btn_UpDateatm = (Button) findViewById(R.id.ATMlist_btn_UpDateatm);

        ATMlist_tv_title = (TextView) findViewById(R.id.ATMlist_tv_title);
        ATMlist_tv_starttime = (TextView) findViewById(R.id.ATMlist_tv_starttime);
        ATMlist_tv_endtime = (TextView) findViewById(R.id.ATMlist_tv_endtime);
        ATMlist_listview = (ListView) findViewById(R.id.ATMlist_listview);

        ATMlist_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListView listView = (ListView)parent;
                YLATM ylatm = (YLATM)listView.getItemAtPosition(position);
                YLEditData.setYlatm(ylatm);
                Intent intent = new Intent();
                intent.setClass(YLATMList.this,YLATMDetail.class);
                startActivity(intent);
            }
        });
    }

    private void InitData() {
        tasksManager= YLSystem.getTasksManager();//获取任务管理类
        ylTask=tasksManager.CurrentTask;//当前选中的任务
        ATMlist_tv_title.setText(ylTask.getLine());
    }

    private String GetCurrDateTime(String dort){
        String datetimeformat;
        if (dort.equals("date")){
            datetimeformat = "yyyy-MM-dd HH:mm:ss";
        }else {
            datetimeformat = "HH:mm";
        }
        SimpleDateFormat sDateFormat = new SimpleDateFormat(datetimeformat, Locale.CHINA);
        return sDateFormat.format(new java.util.Date());
    }

    public void ATMListUpData(View view){
       Dialogtype = "ATMListUpData";
       UpDataDialog();
    }

    ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();

    private void UpDataDialog() {
        Dialogflag = true;
        AlertDialog.Builder builder = new AlertDialog.Builder(YLATMList.this);
        builder.setMessage("是否确认?");
        builder.setTitle("提示");
        builder.setPositiveButton("确认",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ButtonMethod();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void ButtonMethod() {
        switch (Dialogtype){
            case "ATMScan1D":SendScan1Dcmd();
                break;
            case "ATMListHFTaskstartClick":ATMListHFreader("taskstart");
                break;
            case "ATMListHFTaskendClick":ATMListHFreader("taskend");
                break;
            case "ATMListUpData":UpDataDialog();
                break;
        }
        Dialogflag = false;
    }

    private void UpATMTask() {
        singleThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    YLTask t1 = ylTask;
                    t1.lstSite=ylTask.lstSite;
                    t1.lstBox=ylTask.lstBox;
                    //String url = "http://58.252.75.149:8055/YLMobileServiceAndroid.svc/UpLoad";//网址
                    String url = YLSystem.GetBaseUrl(getApplicationContext())+"UpLoad";
                    HttpPost post = new HttpPost(url);
                    UpDataToService(t1, YLSystem.getUser(), post);

                    ylTask.setTaskState("已上传");

                    tasksManager.SaveTask(getApplicationContext());
                    finish();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void UpDataToService(YLTask t1, User s1, HttpPost post) throws JSONException, IOException {
        //添加数值到User类
        Gson gson = new Gson();
        //设置POST请求中的参数-------返回EmpID员工ID，ServerReturn 服务器错误，1为没错误，Time 服务器时间。
        JSONObject p = new JSONObject();
        p.put("STask",gson.toJson(t1));//整个任务=====================自定义。。。。。
        p.put("empid", s1.EmpID);//人员id=====================自定义。。。。。
        p.put("deviceID", s1.DeviceID);//手持机号=====================自定义。。。。。
        p.put("ISWIFI", s1.ISWIFI);//是否用WIFI=====================自定义。。。。。

        post.setEntity(new StringEntity(p.toString(), "UTF-8"));//将参数设置入POST请求
        post.setHeader(HTTP.CONTENT_TYPE, "text/json");//设置为json格式。
        HttpClient client = new DefaultHttpClient();
        HttpResponse response = client.execute(post);
        if (response.getStatusLine().getStatusCode() == 200) {
            String content = EntityUtils.toString(response.getEntity());    //得到返回字符串
            Log.d("WCF", content);//打印到logcat
        }
    }

    private class FunkeyListener extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            boolean defaultdown=false;
            int keycode = intent.getIntExtra("keycode", 0);
            boolean keydown = intent.getBooleanExtra("keydown", defaultdown);

            //左侧下按键
            if(keycode == 133 && keydown){
                Log.d("hotkey", "133");

            }
            //右侧按键
            if(keycode == 134 && keydown){
                Log.d("hotkey", "134");

            }

            if(keycode == 131 && keydown){
//	        	Toast.makeText(getApplicationContext(), "这是F1按键", 0).show();
                Log.d("hotkey", "131");
                GetATMsite("");
            }

            if(keycode == 132 && keydown){
//	        	Toast.makeText(getApplicationContext(), "这是F2按键", 0).show();
                Log.d("hotkey", "132");

            }

        }

    }

    private void KeyBroad() {

        funkeyReceive  = new FunkeyListener();
        //代码注册功能键广播接收者
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.FUN_KEY");
        registerReceiver(funkeyReceive, filter);
    } //初始化热键

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ylatmlist, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(ATMscan1DRecive);
        Intent stopService = new Intent();
        stopService.setAction("ylescort.ylmobileandroid.Scan1DService");
        stopService.putExtra("stopflag", true);
        sendBroadcast(stopService);
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(funkeyReceive);
        super.onStop();
    }

    @Override
    protected void onPostResume() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.FUN_KEY");
        registerReceiver(funkeyReceive, filter);
        super.onPostResume();
    }
}
