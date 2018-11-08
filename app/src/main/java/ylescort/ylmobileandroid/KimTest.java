package ylescort.ylmobileandroid;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.android.hdhe.nfc.NFCcmdManager;
//import com.android.hdhe.uhf.reader.Tools;
import com.example.nfc.util.Tools;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import TaskClass.BaseATMBox;
import TaskClass.BaseATMMachine;
import TaskClass.BaseBox;
import TaskClass.BaseClient;
import TaskClass.BaseEmp;
import TaskClass.BaseSite;
import TaskClass.Box;
import TaskClass.GatherPrint;
import TaskClass.User;
import YLDataService.ATMBoxDBSer;
import YLDataService.ATMMachineDBSer;
import YLDataService.BaseBoxDBSer;
import YLDataService.BaseClientDBSer;
import YLDataService.BaseEmpDBSer;
import YLDataService.BaseSiteDBSer;
import YLDataService.TasksManagerDBSer;
import YLDataService.WebServerBaseData;
import YLDataService.YLBoxScanCheck;
import YLDataService.YLSiteInfo;
import YLFileOperate.DBMove;
import YLFileOperate.YLLoghandle;
import YLPrinter.YLPrint;
import YLSystemDate.YLMediaPlayer;
import YLSystemDate.YLSysTime;
import YLSystemDate.YLSystem;

public class KimTest extends YLBaseActivity implements View.OnClickListener {

    private Button kim_test1;
    private Button kim_test2;
    private Button kim_copydb;
    private Button kim_vibrate;
    private Button kim_uhftest;
    private Button kim_uhfwrite;
    private Button kim_uploadOR;
    private Button kim_cleardata;

    private Scan1DRecive ScanTest;
    private NFCcmdManager manager ;
    private ScanUHFRecive scanUHFRecive;
    private ProgressDialog progressDialog;
    private BaseEmpDBSer baseEmpDBSer;
    private BaseSiteDBSer baseSiteDBSer;
    private BaseClientDBSer baseClientDBSer;
    private BaseBoxDBSer baseBoxDBSer;
    private ATMBoxDBSer atmBoxDBSer;
    private ATMMachineDBSer atmMachineDBSer;
    private int count = 0;
    private  YLPrint ylPrint;
    private YLMediaPlayer ylMediaPlayer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kim_test);

//        PushBoxList();
//        FragmentManager manager = getFragmentManager();
//        FragmentTransaction transaction = manager.beginTransaction();
//        YLBoxEditFragment ylBoxEditFragment = new YLBoxEditFragment();
//        transaction.replace(R.id.kim_test_ll_listview,ylBoxEditFragment);
//        transaction.commit();
        kim_test1 = (Button) findViewById(R.id.kim_test1);
        kim_test2 = (Button) findViewById(R.id.kim_test2);
        kim_copydb = (Button) findViewById(R.id.kim_copydb);
        kim_vibrate = (Button)findViewById(R.id.kim_vibrate);
        kim_uhftest = (Button)findViewById(R.id.kim_uhftest);
        kim_uhfwrite = (Button)findViewById(R.id.kim_uhfwrite);
        kim_uploadOR = (Button)findViewById(R.id.kim_uploadOR);
        kim_cleardata = (Button)findViewById(R.id.kim_cleardata);
        kim_test1.setOnClickListener(this);
        kim_test2.setOnClickListener(this);
        kim_copydb.setOnClickListener(this);
        kim_vibrate.setOnClickListener(this);
        kim_uhftest.setOnClickListener(this);
        kim_uhfwrite.setOnClickListener(this);
        kim_uploadOR.setOnClickListener(this);
        kim_cleardata.setOnClickListener(this);

        InitReciveScan1D();

//        InitReciveUHF();

//        InitHFreader();

        InitData();

    }

    @Override
    protected void InitLayout() {

    }

    protected void InitData() {
        int count= (new BaseBoxDBSer(KimTest.this)).BaseBoxCount();
        kim_vibrate.setText(count+" 个款箱");
        ylMediaPlayer = new YLMediaPlayer(getApplicationContext());
        this.setTitle("测试");
    }

    private class Scan1DRecive extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            String recivedata = intent.getStringExtra("result");
            Log.e(YLSystem.getKimTag(),recivedata);
            if (recivedata != null){
//                count++;-从  +++++++++
//                kim_uhftest.setText("读取:"+count+"次数");
                Toast.makeText(getApplicationContext(),recivedata,Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class ScanUHFRecive extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            String recivedata = intent.getStringExtra("result");
            Log.e(YLSystem.getKimTag(),recivedata);
            if (recivedata != null){
//                count++;
//                kim_uhftest.setText("读取:"+count+"次数");
                Toast.makeText(getApplicationContext(),recivedata,Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void InitHFreader() {
        try{
            manager = NFCcmdManager.getNFCcmdManager(13, 115200, 0);
            manager.readerPowerOn();
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "HF初始化失败", Toast.LENGTH_SHORT).show();
        }
    }

    private void InitReciveScan1D() {
        ScanTest = new Scan1DRecive();
        IntentFilter filter = new IntentFilter();
        filter.addAction("ylescort.ylmobileandroid.KimTest");
        registerReceiver(ScanTest, filter);
        Intent start = new Intent(KimTest.this,Scan1DService.class);
        KimTest.this.startService(start);
    }

    private void InitReciveUHF(){
        scanUHFRecive = new ScanUHFRecive();
        IntentFilter filter = new IntentFilter();
        filter.addAction("ylescort.ylmobileandroid.KimTest");
        registerReceiver(ScanTest, filter);
        Intent start = new Intent(KimTest.this,ScanUHFService.class);
        KimTest.this.startService(start);
    }

    private void Scan1DCmd (){
        String activity = "ylescort.ylmobileandroid.KimTest";
        Intent ac = new Intent();
        ac.setAction("ylescort.ylmobileandroid.Scan1DService");
        ac.putExtra("activity", activity);
        sendBroadcast(ac);
        Intent sendToservice = new Intent(KimTest.this, Scan1DService.class); // 用于发送指令
        sendToservice.putExtra("cmd", "scan");
        this.startService(sendToservice); // 发送指令
    }

    private void ScanUHF(String action){
//        count = 0;
//        String activity = "ylescort.ylmobileandroid.KimTest";
//        Intent ac = new Intent();
//        ac.setAction("ylescort.ylmobileandroid.ScanUHFService");
//        ac.putExtra("activity", activity);
//        sendBroadcast(ac);
//        Intent sendToservice = new Intent(KimTest.this, ScanUHFService.class); // 用于发送指令
//        sendToservice.putExtra("cmd", action);
//        this.startService(sendToservice); // 发送指令
    }

    public void testentext(){
        Toast.makeText(getApplicationContext(),"测试基类",Toast.LENGTH_SHORT).show();
    }

    private void NolableDialog() {

//        final EditText et = new EditText(this);
//        et.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
//        et.requestFocus();
//        InputMethodManager inputManager = (InputMethodManager) et.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//        //imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
//        inputManager.showSoftInput(et,0);
//        new AlertDialog.Builder(this).setTitle("请输入").setIcon
//                (android.R.drawable.ic_dialog_info).setView(et).setPositiveButton("确定", null)
//                .setNegativeButton("取消", null).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_kim_test, menu);
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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.kim_test1:Scan1DCmd();
                break;
            case R.id.kim_test2:
                TestHF();
//                AnysTaskinsterData();

                break;
            case R.id.kim_copydb:CopyDB();
                break;
            case R.id.kim_vibrate:

                final EditText editText = new EditText(this);
                editText.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
                InputMethodManager inputManager = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(editText, 0);
                new AlertDialog.Builder(this).setTitle("请输入升级密码:")
                        .setIcon(android.R.drawable.ic_dialog_info).setView(editText)
                        .setPositiveButton("缓存", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (editText.getText().toString().equals("9")) {
                                    try {
                                        AnysTaskCacheData();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }).setNegativeButton("取消", null).show();
                break;
            case R.id.kim_uhftest:
//                ScanUHF("scan");
//                UHFWriter();
                activationroot();
                break;
            case R.id.kim_uhfwrite:
                PrintTest();
                break;
            case R.id.kim_uploadOR:
                ChoiceDate();
                break;
            case R.id.kim_cleardata:
//                ClearData();
                UHFWriter();
        }
    }

    private void PrintTest() {
        ylPrint = new YLPrint();
        ylPrint.InitBluetooth();
        GatherPrint g = new GatherPrint(
                "1","2","3","4","5","6","7","8","9","10",
                "11","12","13","14","15","16","17","18","19","20",
                "21","22","23","24","25","26","27","28","29","30",
                "31","32","33","34","35","36","37","38","39","40",
                "41","42","43","44","45","46","47","48","49","50",
                "51","52","53","54","55","56","57","58","59","60",
                "61","62","63","64","65","66","67","68","69");

        g.setSiteName("测试网点");
        g.setClintName("测试客户");
        g.setTradeTime("测试时间");
        g.setCarNumber("测试车辆");
        g.setTaskNumber("测试交接号" );
        g.setHomName("测试人员");
        g.setTaskLine("测试线路");

        try {
            ylPrint.PrintGather(g,1);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private List<Box> LoadtestBox (){
        List<Box> list = new ArrayList<>();
        Box box = YLBoxScanCheck.CheckBox("0115081902", getApplicationContext());
        for (int i = 0 ; i < 3;i++){
            box.setBoxStatus("实");
            box.setTradeAction("送");
            box.setBoxTaskType("早送晚收");
            box.setNextOutTime("");
            list.add(box);

        }
//        Box box1 = YLBoxScanCheck.CheckBox("0114103543", getApplicationContext());
//        for (int i = 0 ; i < 6;i++){
//            box1.setBoxStatus("空");
//            box1.setTradeAction("收");
//            box1.setBoxTaskType("上下介");
//            box1.setNextOutTime("");
//            list.add(box1);
//        }
//        Box box2 = YLBoxScanCheck.CheckBox("0116012280", getApplicationContext());
//        for (int i = 0 ; i < 6;i++){
//            box2.setBoxStatus("实");
//            box2.setTradeAction("收");
//            box2.setBoxTaskType("寄库箱");
//            box2.setNextOutTime("");
//            list.add(box2);
//        }
//
//        Box box3 = YLBoxScanCheck.CheckBox("0114103419", getApplicationContext());
//        for (int i = 0 ; i < 3;i++){
//            box3.setBoxStatus("空");
//            box3.setTradeAction("收");
//            box3.setBoxTaskType("跨行调拨");
//            box3.setNextOutTime("");
//            list.add(box3);
//        }

        return list;
    }


    private void ClearData() {
        final EditText editText = new EditText(this);
        editText.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
        InputMethodManager inputManager = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(editText, 0);
        new AlertDialog.Builder(this).setTitle("请输入删除密码:")
                .setIcon(android.R.drawable.ic_dialog_info).setView(editText)
                .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (editText.getText().toString().equals("9")) {
                            try {
                                TasksManagerDBSer tasksManagerDBSer = new TasksManagerDBSer(getApplicationContext());
                                String datestr = YLSysTime.DateToStr(YLSysTime.GetDateCurrentDate());
                                tasksManagerDBSer.DeleteTasksManagerbydate(datestr);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).setNegativeButton("取消", null).show();
    }

    private void ChoiceDate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int Month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(KimTest.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        try {
                            YLLoghandle ylLoghandle = new YLLoghandle(getApplicationContext());
                            File file = ylLoghandle.GetYLLogName(year, monthOfYear+1, dayOfMonth);
                            if (file.exists()) {
                                String str = ylLoghandle.ReadTxt(file.getName());
                                UploadORAsyncTask u = new UploadORAsyncTask();
                                String url =YLSystem.GetBaseUrl(getApplicationContext())+"UploadOperationRecord";
                                String datename =year+"-"+ file.getName().substring(0,2)+"-"+ file.getName().substring(2,4);
                                Log.e(YLSystem.getKimTag(),"文件名"+datename);
                                u.execute(url,str,datename,YLSystem.getHandsetIMEI());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }, year, Month, day);
        datePickerDialog.show();
    }

    private void activationroot() {

        final EditText editText = new EditText(this);
        editText.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
        InputMethodManager inputManager = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(editText, 0);
        new AlertDialog.Builder(this).setTitle("请输入升级密码:")
                .setIcon(android.R.drawable.ic_dialog_info).setView(editText)
                .setPositiveButton("激活ROOT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (editText.getText().toString().equals("9")) {
                            try {
                                YLSysTime ylSysTime = new YLSysTime(getApplicationContext());
                                ylSysTime.Sertime("2016-01-01 11:11:11");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).setNegativeButton("取消", null).show();
    }

    private void UHFWriter() {
        Intent intent = new Intent();
        intent.setClass(KimTest.this,ATMBoxCheck.class);
        startActivity(intent);
    }

    private void vibrate() {
//        String vibratorService = Context.VIBRATOR_SERVICE;
//        Vibrator vibrator = (Vibrator)getSystemService(vibratorService);
//        long[] pattern = {1000,2000,4000,8000,16000};
//        vibrator.vibrate(pattern,0);
//        vibrator.vibrate(1000);
//        NotificationManager manager = (NotificationManager) this
//                .getSystemService(Context.NOTIFICATION_SERVICE);
//        Notification notification = new Notification();
//        notification.ledARGB= Color.RED;
//        notification.ledOffMS= 0;
//        notification.ledOnMS=1;
//        notification.flags = notification.flags|Notification.FLAG_SHOW_LIGHTS;
//        manager.notify(1,notification);

//        String svcName = Context.NOTIFICATION_SERVICE;
//        NotificationManager notificationManager = (NotificationManager)getSystemService(svcName);
//        Notification.Builder builder =
//                new Notification.Builder(KimTest.this);
//        builder.setSmallIcon(R.drawable.ic_launcher).setTicker("")
//                .setWhen(System.currentTimeMillis())
//                .setDefaults(Notification.DEFAULT_SOUND|
//                Notification.DEFAULT_VIBRATE).setLights(-13210, 0, 1);
//        Notification notification = builder.getNotification();
//        notificationManager.notify(1,notification);


    }

    private void notificactionLed() {
        NotificationManager manager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification();
        notification.icon = R.drawable.ic_launcher;
        notification.tickerText = "发送灯通知";

        /**
         * To turn the LED off, pass 0 in the alpha channel for colorARGB or 0 for both ledOnMS and ledOffMS.
         To turn the LED on, pass 1 for ledOnMS and 0 for ledOffMS.
         To flash the LED, pass the number of milliseconds that it should be on and off to ledOnMS and ledOffMS.
         */
        notification.defaults = Notification.DEFAULT_LIGHTS;
        notification.ledARGB = 0xffffffff;//控制led灯的颜色

        //灯闪烁时需要设置下面两个变量
        notification.ledOnMS = 300;
        notification.ledOffMS = 300;

        notification.flags = Notification.FLAG_SHOW_LIGHTS;

        Intent intent = new Intent(this, KimTest.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                intent, PendingIntent.FLAG_ONE_SHOT);

        notification.setLatestEventInfo(this, "灯测试", "led灯测试", pendingIntent);
        manager.notify(1, notification);
    }

    private void showactivity(){
        Intent intent = new Intent();
        intent.setClass(KimTest.this, HomYLBoxScan.class);
        startActivity(intent);
    }

    private void AnysTaskCacheData() throws Exception{
//        if (!YLSystem.getNetWorkState().equals("1")){
//            Toast.makeText(getApplicationContext(),"请在WIFI连接情况下更新缓存",Toast.LENGTH_SHORT).show();
//            return;
//        }
        try {
            //清除数据库内容
            (new BaseBoxDBSer(KimTest.this)).DeleteAll();
            (new BaseClientDBSer(KimTest.this)).DeleteAll();
            (new BaseEmpDBSer(KimTest.this)).DeleteAll();
            (new BaseSiteDBSer(KimTest.this)).DeleteAll();
            (new ATMBoxDBSer(KimTest.this)).DeleteAll();
            (new ATMMachineDBSer(KimTest.this)).DeleteAll();
        }
        catch (Exception e){
            e.printStackTrace();
            return;
        }
        GetBaseData();
    }

    public void GetBaseData()throws Exception{
        progressDialog = new ProgressDialog(getApplicationContext());
        baseEmpDBSer = new BaseEmpDBSer(getApplicationContext());
        baseSiteDBSer = new BaseSiteDBSer(getApplicationContext());
        baseClientDBSer = new BaseClientDBSer(getApplicationContext());
        baseBoxDBSer = new BaseBoxDBSer(getApplicationContext());
        atmBoxDBSer = new ATMBoxDBSer(getApplicationContext());
        atmMachineDBSer = new ATMMachineDBSer(getApplicationContext());

        String DeviceID = YLSystem.getHandsetIMEI();
        String isWifi =YLSystem.getNetWorkState();
        String empurl = YLSystem.GetBaseUrl(getApplicationContext())+"GetBaseEmp";
        String Clienturl = YLSystem.GetBaseUrl(getApplicationContext())+"GetBaseClient";
        String Siteurl = YLSystem.GetBaseUrl(getApplicationContext())+"GetBaseSite";
        String boxurl = YLSystem.GetBaseUrl(getApplicationContext())+"GetBaseBox";
        String atmbox = YLSystem.GetBaseUrl(getApplicationContext())+"GetBaseATMBox";
        String atmmachine = YLSystem.GetBaseUrl(getApplicationContext())+"GetBaseATMMachine";
        Log.e(YLSystem.getKimTag(),DeviceID+"开始");
        AnysTaskGetBaseData anysTaskGetBaseData = new AnysTaskGetBaseData();
        anysTaskGetBaseData.execute(DeviceID, isWifi, empurl, Clienturl, Siteurl, boxurl,atmbox,atmmachine);
//        anysTaskGetBaseData.get();
    }

    private void AnysTaskinsterData(){
        InsertEmp();
        InsertSite();
    }

    private void InsertEmp() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.e(YLSystem.getKimTag(),"员工开始");
                    (new BaseEmpDBSer(KimTest.this)).DeleteAll();
                    String url = YLSystem.GetBaseUrl(getApplicationContext())+"GetBaseEmp";
                    HttpPost post = new HttpPost(url);
                    Gson gson = new Gson();
                    JSONObject p = new JSONObject();
                    p.put("DeviceID", YLSystem.getHandsetIMEI());
                    p.put("ISWIFI", YLSystem.getNetWorkState());
                    p.put("datetime", "ALL");
                    post.setEntity(new StringEntity(p.toString(), "UTF-8"));
                    post.setHeader(HTTP.CONTENT_TYPE, "text/json");
                    HttpClient client = new DefaultHttpClient();
                    HttpResponse response = client.execute(post);
                    if (response.getStatusLine().getStatusCode() == 200) {
                        String content = EntityUtils.toString(response.getEntity());
                        List<BaseEmp> emps = gson.fromJson (content, new TypeToken<List<BaseEmp>>() {
                        }.getType());
                        baseEmpDBSer.InsertBaseEmp(emps);
                        Log.e(YLSystem.getKimTag(),emps.size()+"员工数据");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
    }

    private void InsertSite(){
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.e(YLSystem.getKimTag(),"网点开始");
                    (new BaseEmpDBSer(KimTest.this)).DeleteAll();
                    String url = YLSystem.GetBaseUrl(getApplicationContext())+"GetBaseSite";
                    HttpPost post = new HttpPost(url);
                    Gson gson = new Gson();
                    JSONObject p = new JSONObject();
                    p.put("DeviceID", YLSystem.getHandsetIMEI());
                    p.put("ISWIFI", YLSystem.getNetWorkState());
                    p.put("datetime", "ALL");
                    post.setEntity(new StringEntity(p.toString(), "UTF-8"));
                    post.setHeader(HTTP.CONTENT_TYPE, "text/json");
                    HttpClient client = new DefaultHttpClient();
                    HttpResponse response = client.execute(post);
                    if (response.getStatusLine().getStatusCode() == 200) {
                        String content = EntityUtils.toString(response.getEntity());
                        List<BaseSite> bases = gson.fromJson (content, new TypeToken<List<BaseSite>>() {
                        }.getType());
                        baseSiteDBSer.InsertBaseSite(bases);
                        Log.e(YLSystem.getKimTag(),bases.size()+"网点数据");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
    }


    private class AnysTaskGetBaseData extends AsyncTask<String,Integer,String>{

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(KimTest.this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("正在更新中");
            progressDialog.setMax(6);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            publishProgress(0);
            String sertime = "";
            try {
                String url = params[2];
                HttpPost post = new HttpPost(url);
                Gson gson = new Gson();
                JSONObject p = new JSONObject();
                p.put("DeviceID", params[0]);
                p.put("ISWIFI", params[1]);
                p.put("datetime", "ALL");
                post.setEntity(new StringEntity(p.toString(), "UTF-8"));
                post.setHeader(HTTP.CONTENT_TYPE, "text/json");
                HttpClient client = new DefaultHttpClient();
                HttpResponse response = client.execute(post);
                if (response.getStatusLine().getStatusCode() == 200) {
                    String content = EntityUtils.toString(response.getEntity());
                    List<BaseEmp> emps = gson.fromJson (content, new TypeToken<List<BaseEmp>>() {
                    }.getType());
                    baseEmpDBSer.InsertBaseEmp(emps);
                    sertime = emps.get(0).ServerTime;
                    Log.e(YLSystem.getKimTag(),emps.size()+"员工数量");
                }
                publishProgress(1);
                post = new HttpPost(params[6]);
                p.put("DeviceID", params[0]);
                p.put("ISWIFI", params[1]);
                p.put("datetime", "ALL");
                post.setEntity(new StringEntity(p.toString(), "UTF-8"));
                post.setHeader(HTTP.CONTENT_TYPE, "text/json");
                client = new DefaultHttpClient();
                response = client.execute(post);
                if (response.getStatusLine().getStatusCode() == 200){
                    String content = EntityUtils.toString(response.getEntity());
                    List<BaseATMBox> baseATMBoxes = gson.fromJson(content, new TypeToken<List<BaseATMBox>>() {
                    }.getType());
                    Log.e(YLSystem.getKimTag(), baseATMBoxes.size() + "ATM钞箱数量");
                    atmBoxDBSer.Ins(baseATMBoxes);
                    sertime = baseATMBoxes.get(0).ServerTime;
                }

                publishProgress(2);

                post = new HttpPost(params[7]);
                p.put("DeviceID", params[0]);
                p.put("ISWIFI", params[1]);
                p.put("datetime", "ALL");
                post.setEntity(new StringEntity(p.toString(), "UTF-8"));
                post.setHeader(HTTP.CONTENT_TYPE, "text/json");
                client = new DefaultHttpClient();
                response = client.execute(post);
                if (response.getStatusLine().getStatusCode() == 200){
                    String content = EntityUtils.toString(response.getEntity());
                    List<BaseATMMachine> baseATMMachines =
                            gson.fromJson(content, new TypeToken<List<BaseATMMachine>>() {}.getType());
                    Log.e(YLSystem.getKimTag(), baseATMMachines.size() + "ATM机器数量");
                    atmMachineDBSer.Ins(baseATMMachines);
                    sertime = baseATMMachines.get(0).getServerTime();
                }
                publishProgress(3);

                post = new HttpPost(params[3]);
                p.put("DeviceID", params[0]);
                p.put("ISWIFI", params[1]);
                p.put("datetime", "ALL");
                post.setEntity(new StringEntity(p.toString(), "UTF-8"));
                post.setHeader(HTTP.CONTENT_TYPE, "text/json");
                client = new DefaultHttpClient();
                response = client.execute(post);
                if (response.getStatusLine().getStatusCode() == 200){
                    String content = EntityUtils.toString(response.getEntity());
                    List<BaseClient> baseClients =  gson.fromJson(content, new TypeToken<List<BaseClient>>() {
                    }.getType());
                    baseClientDBSer.InsertBaseClient(baseClients);
                    sertime = baseClients.get(0).ServerTime;
                    Log.e(YLSystem.getKimTag(),baseClients.size()+"客户数量");
                }
                publishProgress(4);
                post = new HttpPost(params[4]);
                p.put("DeviceID", params[0]);
                p.put("ISWIFI", params[1]);
                p.put("datetime", "ALL");
                post.setEntity(new StringEntity(p.toString(), "UTF-8"));
                post.setHeader(HTTP.CONTENT_TYPE, "text/json");
                client = new DefaultHttpClient();
                response = client.execute(post);
                if (response.getStatusLine().getStatusCode() == 200){
                    String content = EntityUtils.toString(response.getEntity());

                    List<BaseSite> siteList =  gson.fromJson(content, new TypeToken<List<BaseSite>>() {
                    }.getType());

                    baseSiteDBSer.InsertBaseSite(siteList);
                    sertime = siteList.get(0).ServerTime;
                    Log.e(YLSystem.getKimTag(),siteList.size()+"网点数量");
                }
                publishProgress(5);
                post = new HttpPost(params[5]);
                p.put("DeviceID", params[0]);
                p.put("ISWIFI", params[1]);
                p.put("datetime", "ALL");
                post.setEntity(new StringEntity(p.toString(), "UTF-8"));
                post.setHeader(HTTP.CONTENT_TYPE, "text/json");
                client = new DefaultHttpClient();
                response = client.execute(post);
                if (response.getStatusLine().getStatusCode() == 200){
                    String content = EntityUtils.toString(response.getEntity());

                    List<BaseBox> baseBoxes = gson.fromJson(content, new TypeToken<List<BaseBox>>() {
                    }.getType());
                    baseBoxDBSer.InsertBox2(baseBoxes);
                    sertime = baseBoxes.get(0).ServerTime;
                    Log.e(YLSystem.getKimTag(), baseBoxes.size() + "款箱数量");
                    InitData();
                }
                publishProgress(6);

                return  sertime;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return sertime;
        }

        @Override
        protected void onPostExecute(String s) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("CacheLastUpdate", s);
            editor.apply();
            progressDialog.dismiss();
            super.onPostExecute(s);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressDialog.setProgress(values[0]);
            super.onProgressUpdate(values);
        }
    }

    private class Ansycache extends AsyncTask<String,Integer,String>{

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(KimTest.this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("正在更新中");
//            progressDialog.setMax(100);
//            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.show();

            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            CacheData();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            super.onPostExecute(s);
        }
    }

    private void CacheData(){
        if (!YLSystem.getNetWorkState().equals("1")){
            Toast.makeText(getApplicationContext(),"请在WIFI连接情况下更新缓存",Toast.LENGTH_SHORT).show();
            return;
        }

//清除缓存时间
        try {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("CacheLastUpdate", "ALL");
            editor.apply();
        }
        catch (Exception e){
            e.printStackTrace();
            return;
        }
        try {
            //清除数据库内容
            (new BaseBoxDBSer(KimTest.this)).DeleteAll();
            (new BaseClientDBSer(KimTest.this)).DeleteAll();
            (new BaseEmpDBSer(KimTest.this)).DeleteAll();
            (new BaseSiteDBSer(KimTest.this)).DeleteAll();
        }
        catch (Exception e){
            e.printStackTrace();
            return;
        }

        WebServerBaseData webServerBaseData = new WebServerBaseData();
        try {
            webServerBaseData.GetBaseData(getApplicationContext(),"ALL");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void CopyDB() {
        DBMove dbMove = new DBMove();
        String OldFile ="/data/data/ylescort.ylmobileandroid/databases/YLDB.db";
        String newFile =  "/storage/emulated/0/YLLOG/YLDB.db";   //旧机型 "/storage/sdcard0/YLDB.db";
        String outputfolder =  "/storage/emulated/0/YLLOG/";  //日志输出文件夹
        dbMove.CopySdcardFile(OldFile, newFile,outputfolder);
    }

    private void TestHF() {
//        manager.init_14443A();
//        manager.readerPowerOn();
//        byte[] uid = manager.inventory_14443A();
//        if(uid != null){
//            String EmpHF = Tools.Bytes2HexString(uid, uid.length);
//            Toast.makeText(getApplicationContext(), EmpHF, Toast.LENGTH_SHORT).show();
//            manager.readerPowerOff();
//        }
        String hfcode = HFReadUID();

        if (hfcode.length() >0){
            YLSiteInfo ylSiteInfo = new YLSiteInfo(getApplicationContext());
            String sitename = ylSiteInfo.GetBaseSiteName(hfcode);
            if (sitename.equals("未录入网点")){
                ylMediaPlayer.SuccessOrFail(false);
                YLMessagebox(hfcode);
            }else {
                ylMediaPlayer.SuccessOrFail(true);
                YLMessagebox(sitename);
            }

        }else {
            YLMessagebox("不能读卡，请重新读取");
            ylMediaPlayer.SuccessOrFail(false);
        }

//        YLMessagebox(hfcode);
    }

    private void PushBoxList() {

        List<Box> boxList = new ArrayList<>();
        for (int i = 0 ; i < 10;i++){
            Box box = new Box();
            box.setBoxName("测试箱名"+i);
            box.setBoxID("测试箱编号" + i);
            box.setBoxType("实箱");
            boxList.add(box);
        }
        YLSystem.setEdiboxList(boxList);
    }

    private void GetDataFromServer() {

        kim_test2.setText("11");
        kim_test2.setEnabled(false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    GetUserFormServer getUserFormServer = new GetUserFormServer();
                    getUserFormServer.execute("");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    public class GetUserFormServer extends AsyncTask<String,Integer, User>{

        @Override
        protected User doInBackground(String... params) {
            String url = YLSystem.GetBaseUrl(getApplicationContext())+"Login1";
            HttpPost post = new HttpPost(url);
            User user = new User();
            user.setEmpNO("710161");
            user.setPass(YLSystem.SetMD5("710161"));
            Gson gson = new Gson();
            JSONObject p = new JSONObject();
            try {
                p.put("user",gson.toJson(user));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                post.setEntity(new StringEntity(p.toString(),"UTF-8"));
                post.setHeader(HTTP.CONTENT_TYPE,"text/json");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            HttpClient client = new DefaultHttpClient();
            try {
                HttpResponse response = client.execute(post);
                if (response.getStatusLine().getStatusCode() == 200){
                    String content = EntityUtils.toString(response.getEntity());
                    User getjsonuser = new User();
                    getjsonuser =  gson.fromJson(content, new TypeToken<User>() {
                    }.getType());
                    return getjsonuser;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(User user) {

            if (user.getServerReturn().equals("1")){
                kim_test2.setText("22");
                kim_test2.setEnabled(true);
            }else {
                kim_test2.setText("33");
                kim_test2.setEnabled(true);
            }
            super.onPostExecute(user);
        }
    }

    public class UploadORAsyncTask extends AsyncTask<String,Integer,String>{

        @Override
        protected String doInBackground(String... strings) {
            String url = strings[0];
            HttpPost post = new HttpPost(url);
            Gson gson = new Gson();
            JSONObject p = new JSONObject();
            try {
                p.put("ORStr",strings[1]);
                p.put("Date",strings[2]);
                p.put("deviceID",strings[3]);
                post.setEntity(new StringEntity(p.toString(),"UTF-8"));
                post.setHeader(HTTP.CONTENT_TYPE,"text/json");
                HttpClient client = new DefaultHttpClient();
                HttpResponse response = client.execute(post);

                if (response.getStatusLine().getStatusCode() == 200){
                    String content = EntityUtils.toString(response.getEntity());
                    Log.e(YLSystem.getKimTag(),content+"上传返回");
                    return gson.fromJson(content, String.class);

                }
            } catch (Exception e) {
                e.printStackTrace();
                return "0";
            }
            return null;
        }
    }

    @Override
    protected void onDestroy() {
        if (ScanTest !=null){
            unregisterReceiver(ScanTest);
        }
//        ScanUHF("stopflag");
//        stopService(new Intent(this, vault_in_detail.class));
        super.onDestroy();
    }

}
