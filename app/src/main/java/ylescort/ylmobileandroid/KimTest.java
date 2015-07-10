package ylescort.ylmobileandroid;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.hdhe.nfc.NFCcmdManager;
import com.android.hdhe.uhf.reader.Tools;
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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import TaskClass.BaseBox;
import TaskClass.BaseClient;
import TaskClass.BaseEmp;
import TaskClass.BaseSite;
import TaskClass.Box;
import TaskClass.User;
import YLDataService.BaseBoxDBSer;
import YLDataService.BaseClientDBSer;
import YLDataService.BaseEmpDBSer;
import YLDataService.BaseSiteDBSer;
import YLDataService.WebServerBaseData;
import YLDataService.WebService;
import YLFileOperate.DBMove;
import YLFragment.YLBoxEditFragment;
import YLSystemDate.YLSysTime;
import YLSystemDate.YLSystem;


public class KimTest extends ActionBarActivity implements View.OnClickListener {

    private Button kim_test1;
    private Button kim_test2;
    private Button kim_copydb;
    private Button kim_vibrate;

    private Scan1DRecive ScanTest;
    private NFCcmdManager manager ;
    private ProgressDialog progressDialog;
    private BaseEmpDBSer baseEmpDBSer;
    private BaseSiteDBSer baseSiteDBSer;
    private BaseClientDBSer baseClientDBSer;
    private BaseBoxDBSer baseBoxDBSer;


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
        kim_test1.setOnClickListener(this);
        kim_test2.setOnClickListener(this);
        kim_copydb.setOnClickListener(this);
        kim_vibrate.setOnClickListener(this);

        InitReciveScan1D();

        InitHFreader();
    }

    private class Scan1DRecive extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            String recivedata = intent.getStringExtra("result");
            Log.e(YLSystem.getKimTag(),recivedata);
            if (recivedata != null){
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
            case R.id.kim_test2:TestHF();
                break;
            case R.id.kim_copydb:CopyDB();
                break;
            case R.id.kim_vibrate:
                try {
                    AnysTaskCacheData();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
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

        String svcName = Context.NOTIFICATION_SERVICE;
        NotificationManager notificationManager = (NotificationManager)getSystemService(svcName);
        Notification.Builder builder =
                new Notification.Builder(KimTest.this);
        builder.setSmallIcon(R.drawable.ic_launcher).setTicker("")
                .setWhen(System.currentTimeMillis())
                .setDefaults(Notification.DEFAULT_SOUND|
                Notification.DEFAULT_VIBRATE).setLights(-13210, 0, 1);
        Notification notification = builder.getNotification();
        notificationManager.notify(1,notification);


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
        if (!YLSystem.getNetWorkState().equals("1")){
            Toast.makeText(getApplicationContext(),"请在WIFI连接情况下更新缓存",Toast.LENGTH_SHORT).show();
            return;
        }

//清除缓存时间
        try {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("CacheLastUpdate", YLSysTime.GetStrCurrentTime());
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
        GetBaseData();
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
            webServerBaseData.GetBaseData(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void CopyDB() {
        DBMove dbMove = new DBMove();
        String OldFile ="/data/data/ylescort.ylmobileandroid/databases/YLDB.db";
        String newFile = "/storage/sdcard0/YLDB.db";
        dbMove.CopySdcardFile(OldFile, newFile);
    }

    private void TestHF() {
        manager.init_14443A();
        manager.readerPowerOn();
        byte[] uid = manager.inventory_14443A();
        if(uid != null){
            String EmpHF = Tools.Bytes2HexString(uid, uid.length);
            Toast.makeText(getApplicationContext(), EmpHF, Toast.LENGTH_SHORT).show();
            manager.readerPowerOff();
        }
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

    public void GetBaseData()throws Exception{
        progressDialog = new ProgressDialog(getApplicationContext());
        baseEmpDBSer = new BaseEmpDBSer(getApplicationContext());
        baseSiteDBSer = new BaseSiteDBSer(getApplicationContext());
        baseClientDBSer = new BaseClientDBSer(getApplicationContext());
        baseBoxDBSer = new BaseBoxDBSer(getApplicationContext());
        Log.e(YLSystem.getKimTag(),"开始");
        String DeviceID = YLSystem.getHandsetIMEI();
        String isWifi =YLSystem.getNetWorkState();
        String empurl = YLSystem.GetBaseUrl(getApplicationContext())+"GetBaseEmp";
        String Clienturl = YLSystem.GetBaseUrl(getApplicationContext())+"GetBaseClient";
        String Siteurl = YLSystem.GetBaseUrl(getApplicationContext())+"GetBaseSite";
        String boxurl = YLSystem.GetBaseUrl(getApplicationContext())+"GetBaseBox";
        AnysTaskGetBaseData anysTaskGetBaseData = new AnysTaskGetBaseData();
        anysTaskGetBaseData.execute(DeviceID, isWifi, empurl, Clienturl, Siteurl, boxurl);
//        anysTaskGetBaseData.get();
    }

    private class AnysTaskGetBaseData extends AsyncTask<String,Integer,String>{

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
                    List<BaseEmp> emps =  gson.fromJson(content, new TypeToken<List<BaseEmp>>() {
                    }.getType());
                    baseEmpDBSer.InsertBaseEmp(emps);

                    Log.e(YLSystem.getKimTag(),emps.size()+"人员数据");
                }

                post = new HttpPost(params[3]);
                p.put("DeviceID", params[0]);
                p.put("ISWIFI", params[1]);
                p.put("datetime", "ALL");
                post.setEntity(new StringEntity(p.toString(), "UTF-8"));
                post.setHeader(HTTP.CONTENT_TYPE, "/json");
                client = new DefaultHttpClient();
                response = client.execute(post);
                if (response.getStatusLine().getStatusCode() == 200){
                    String content = EntityUtils.toString(response.getEntity());
                    List<BaseClient> baseClients =  gson.fromJson(content, new TypeToken<List<BaseClient>>() {
                    }.getType());
                    baseClientDBSer.InsertBaseClient(baseClients);
                    Log.e(YLSystem.getKimTag(),baseClients.size()+"客户数据");
                }

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
                    Log.e(YLSystem.getKimTag(),siteList.size()+"网点数据");
                }

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
                    List<BaseBox> baseBoxes =  gson.fromJson(content, new TypeToken<List<BaseBox>>() {
                    }.getType());
                    baseBoxDBSer.InsertBox2(baseBoxes);
                    Log.e(YLSystem.getKimTag(),baseBoxes.size()+"插入款箱数据");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            super.onPostExecute(s);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressDialog.setProgress(values[0]);
            super.onProgressUpdate(values);
        }
    }

    @Override
    protected void onDestroy() {
        if (ScanTest !=null){
            unregisterReceiver(ScanTest);
        }
        super.onDestroy();
    }
}
