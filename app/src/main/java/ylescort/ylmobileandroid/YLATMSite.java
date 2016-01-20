package ylescort.ylmobileandroid;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.hdhe.nfc.NFCcmdManager;
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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import TaskClass.BaseSite;
import TaskClass.Site;
import TaskClass.TasksManager;
import TaskClass.User;
import TaskClass.YLATM;
import TaskClass.YLTask;
import YLAdapter.YLATMSiteAdapter;
import YLDataService.BaseSiteDBSer;
import YLDataService.WebService;
import YLSystemDate.YLEditData;
import YLSystemDate.YLSysTime;
import YLSystemDate.YLSystem;


public class YLATMSite extends ActionBarActivity implements View.OnClickListener {

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

    private NFCcmdManager HFmanager ;

    private BaseSiteDBSer baseSiteDBSer;
    private YLATMSiteAdapter ylatmSiteAdapter;
    private WebService webService;

    private String NetPoint;//银行网点

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ylatmlist);
        InitLayout();
        InitData();
        InitReciveScan1D();
    }

    private void InitReciveScan1D() {
        ATMscan1DRecive = new Scan1DRecive();
        IntentFilter filter = new IntentFilter();
        filter.addAction("ylescort.ylmobileandroid.YLATMSite");
        registerReceiver(ATMscan1DRecive, filter);
        Intent start = new Intent(YLATMSite.this,Scan1DService.class);
        YLATMSite.this.startService(start);
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
        if (YLEditData.getYlatmList() == null ){
            ylatmList = new ArrayList<>();
            YLEditData.setYlatmList(ylatmList);}
        List<BaseSite> baseSiteList = baseSiteDBSer.GetBaseSites("where SiteBCNo ="+replaceBlank(recivedata));
        if (baseSiteList.size()>0){
            try {
                Sertime(webService.GetServerTime(getApplicationContext(),"ATM ALL"));
            }catch (Exception e){
                e.printStackTrace();
            }
            BaseSite baseSite = baseSiteList.get(0);
            YLATM ylatm = new YLATM();
            ylatm.setId(YLEditData.getYlatmList().size()+1);
            ylatm.setServerReturn("1");
            ylatm.setTaskID(ylTask.getTaskID());
            ylatm.setSiteID(baseSite.SiteID);
            ylatm.setSiteName(baseSite.SiteName);
            ylatm.setSiteType(baseSite.SiteType);
            ylatm.setTradeState("未交接");
            ylatm.setTimeID(GetTimeID(ylatm.getSiteID()));
            ylatm.setEmpID(YLSystem.getUser().getEmpID());
            YLEditData.setYlatm(ylatm);
            Intent intent = new Intent();
            intent.setClass(YLATMSite.this,YLATMDetail.class);
            Bundle bundle = new Bundle();
            bundle.putString("EdiOrIns","Ins");
            intent.putExtras(bundle);
            startActivity(intent);
        }else {
            Toast.makeText(getApplicationContext(), "网点码无效", Toast.LENGTH_SHORT).show();
        }
    }

    private void Sertime(final String CurrentTime) {
        YLSysTime ylSysTime = new YLSysTime();
        ylSysTime.Sertime(CurrentTime);
    }

    private Integer GetTimeID(String siteID) {
        int SiteTimeID = 1;
        for (int i = 0 ; i< YLEditData.getYlatmList().size();i++){
            YLATM ylatm  = YLEditData.getYlatmList().get(i);
            if (ylatm.getSiteID().equals(siteID)){
                SiteTimeID ++;}
        }
        return SiteTimeID;
    }

    private void DisplayATMSite(List<YLATM> ylatmList) {
        if (ylatmList != null ){
            ylatmSiteAdapter = new YLATMSiteAdapter(this,ylatmList,R.layout.activity_atmsiteitem);
            ATMlist_listview.setAdapter(ylatmSiteAdapter);}
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
        String activity = "ylescort.ylmobileandroid.YLATMSite";
        Intent ac = new Intent();
        ac.setAction("ylescort.ylmobileandroid.Scan1DService");
        ac.putExtra("activity", activity);
        sendBroadcast(ac);
        Intent sendToservice = new Intent(YLATMSite.this, Scan1DService.class); // 用于发送指令
        sendToservice.putExtra("cmd", "scan");
        this.startService(sendToservice); // 发送指令
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
                try {
                    ListView listView = (ListView) parent;
                    YLATM ylatm = (YLATM) listView.getItemAtPosition(position);
                    YLEditData.setYlatm(ylatm);
                    Sertime(webService.GetServerTime(getApplicationContext(),"ATMSiteID:"+ylatm.getSiteID()));
                    Intent intent = new Intent();
                    intent.setClass(YLATMSite.this, YLATMDetail.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("EdiOrIns", "Edi");
                    intent.putExtras(bundle);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        ATMlist_btn_Start.setOnClickListener(this);
        ATMlist_btn_End.setOnClickListener(this);
        ATMlist_btn_Addatm.setOnClickListener(this);
        ATMlist_btn_UpDateatm.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ATMlist_btn_Start:
                TaskStart();
                break;
            case R.id.ATMlist_btn_End:
                TaskEnd();
                break;
            case R.id.ATMlist_btn_Addatm:
                SendScan1Dcmd();
                break;
            case R.id.ATMlist_btn_UpDateatm:
                UpDataDialog();
                break;

        }
    }

    private void InitData() {
        tasksManager= YLSystem.getTasksManager();//获取任务管理类
        ylTask=tasksManager.CurrentTask;//当前选中的任务
        ATMlist_tv_title.setText(ylTask.getLine());
        webService = new WebService();
        NetPoint = "";

        if(ylTask.getTaskState().equals("有更新")){
            ATMlist_tv_title.setText("获取任务中");
            String url = YLSystem.GetBaseUrl(getApplicationContext()) + "GetTaskStie";
            GetATMSite2 getATMSite2 = new GetATMSite2();
            getATMSite2.execute(url);
        }else {
            ylTask.setTaskState("进行中");
            ylatmList =ylTask.getLstATM();
            YLEditData.setYlatmList(ylTask.getLstATM());
            if (ylTask.getTaskATMBeginTime() !=null){
                ATMlist_tv_starttime.setText(ylTask.getTaskATMBeginTime());
            }
            if (ylTask.getTaskATMEndTime() != null){
                ATMlist_tv_endtime.setText(ylTask.getTaskATMEndTime());
            }

            if (ylatmList.size()>0){
                for (YLATM ylatm : ylatmList) {
                    if (ylatm.getSiteType().equals("网点")){
                        NetPoint = ylatm.getSiteName();
                        YLATMSite.this.setTitle(NetPoint);
                    }
                }
            }

            DisplayATMSite(ylatmList);
        }

        if (ylTask.getTaskATMBeginTime() == null){
            ATMlist_btn_End.setEnabled(false);
            ATMlist_btn_Addatm.setEnabled(false);
            ATMlist_btn_UpDateatm.setEnabled(false);
            ATMlist_listview.setEnabled(false);
        }else {
            ATMlist_btn_End.setEnabled(true);
            ATMlist_btn_Addatm.setEnabled(true);
            ATMlist_btn_UpDateatm.setEnabled(true);
            ATMlist_listview.setEnabled(true);
        }

        baseSiteDBSer = new BaseSiteDBSer(getApplicationContext());

    }

    ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();

    public class GetATMSite2 extends AsyncTask<String,Integer,List<Site>> {
        @Override
        protected List<Site> doInBackground(String... params) {

            try {
                HttpPost post = new HttpPost(params[0]);
                User user = YLSystem.getUser();
                //添加数值到User类
                Gson gson = new Gson();
                //设置POST请求中的参数
                JSONObject p = new JSONObject();

                p.put("taskID", ylTask.getTaskID());
                p.put("deviceID", user.getDeviceID());
                p.put("empid", user.getEmpID());
                p.put("ISWIFI", user.getISWIFI());
                post.setEntity(new StringEntity(p.toString(), "UTF-8"));//将参数设置入POST请求
                post.setHeader(HTTP.CONTENT_TYPE, "text/json");//设置为json格式。
                HttpClient client = new DefaultHttpClient();
                HttpResponse response = null;
                response = client.execute(post);
                if (response != null) {
                    if (response.getStatusLine().getStatusCode() == 200) {
                        String content = null;
                        content = EntityUtils.toString(response.getEntity());
                        List<Site> lstSite = gson.fromJson(content, new TypeToken<List<Site>>() {
                        }.getType());
                        String result = lstSite.get(0).ServerReturn;
                        if (result.equals("1")) {
                            return lstSite;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Site> sites) {
            ylTask.setLstSite(sites);

            List<YLATM> ylatms = new ArrayList<YLATM>();
            if (ylTask.getLstSite() !=null){
                for (int i = 0; i <ylTask.getLstSite().size();i++){
                    Site site = ylTask.getLstSite().get(i);
                    YLATM ylatm = new YLATM();
                    ylatm.setId(i+1);
                    ylatm.setServerReturn("1");
                    ylatm.setTaskID(ylTask.getTaskID());
                    ylatm.setSiteID(site.getSiteID());
                    ylatm.setSiteName(site.getSiteName());
                    ylatm.setTradeState("未交接");
                    ylatm.setSiteType(site.getSiteType());
                    if (site.getSiteType().equals("网点")){
                        NetPoint = site.getSiteName();
                        YLATMSite.this.setTitle(NetPoint);
                    }
                    ylatm.setTradeBegin("");
                    ylatm.setTradeEnd("");
                    ylatm.setATMCount("0");
                    ylatm.setTimeID(1);
                    ylatm.setEmpID(YLSystem.getUser().getEmpID());
                    ylatms.add(ylatm);
                }
            }
            YLEditData.setYlatmList(ylatms);
            DisplayATMSite(YLEditData.getYlatmList());
            ATMlist_tv_title.setText(ylTask.getLine());
            super.onPostExecute(sites);
        }
    }

    private void UpDataDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(YLATMSite.this);
        builder.setMessage("是否确认上传?");
        builder.setTitle("提示");
        builder.setPositiveButton("确认",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                UpATMTask();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void UpATMTask() {
        singleThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    YLTask t1 = ylTask;
                    List<Site> ylSites = new ArrayList<Site>();
                    t1.lstSite = ylSites;
                    t1.lstBox = ylTask.lstBox;
                    t1.lstATM = YLEditData.getYlatmList();
                    String url = YLSystem.GetBaseUrl(getApplicationContext()) + "UpLoad";
                    HttpPost post = new HttpPost(url);
                    UpDataToService(t1, YLSystem.getUser(), post);
                    ylTask.setTaskState("已上传");
                    tasksManager.SaveTask(getApplicationContext());
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void UpDataToService(YLTask t1, User s1, HttpPost post) throws Exception{
        Gson gson = new Gson();
        JSONObject p = new JSONObject();
        p.put("STask",gson.toJson(t1));
        p.put("empid", s1.EmpID);
        p.put("deviceID", s1.DeviceID);
        p.put("ISWIFI", s1.ISWIFI);
        post.setEntity(new StringEntity(p.toString(), "UTF-8"));
        post.setHeader(HTTP.CONTENT_TYPE, "text/json");
        HttpClient client = new DefaultHttpClient();
        HttpResponse response = client.execute(post);
        if (response.getStatusLine().getStatusCode() == 200) {
            String content = EntityUtils.toString(response.getEntity());
            Log.e(YLSystem.getKimTag(),"ATM服务返回"+content);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode){
            case 131:SendScan1Dcmd();
                break;
            case 132:UpDataDialog();
                break;
            case 133:TaskStart();
                break;
            case 134:TaskEnd();
                break;
            case 4:
                ylTask.setTaskState("进行中");
                ylTask.setLstATM(YLEditData.getYlatmList());
                tasksManager.SaveTask(getApplicationContext());
                finish();
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void TaskStart() {
        AlertDialog.Builder builder = new AlertDialog.Builder(YLATMSite.this);
        builder.setMessage("是否确认开始?");
        builder.setTitle("提示");
        builder.setPositiveButton("确认",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (ylTask.getTaskATMBeginTime() == null){
                    ylTask.setTaskATMBeginTime(YLSysTime.GetStrCurrentTime());
                    ATMlist_tv_starttime.setText(YLSysTime.GetStrCurrentTime());
                }else {
                    ATMlist_tv_starttime.setText(ylTask.getTaskATMBeginTime());
                }
                ATMlist_btn_End.setEnabled(true);
                ATMlist_btn_Addatm.setEnabled(true);
                ATMlist_btn_UpDateatm.setEnabled(true);
                ATMlist_listview.setEnabled(true);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });
        builder.create().show();

    }

    private void TaskEnd() {

        AlertDialog.Builder builder = new AlertDialog.Builder(YLATMSite.this);
        builder.setMessage("是否确认结束?");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                if (ylTask.getTaskATMEndTime() == null){
                ylTask.setTaskATMEndTime(YLSysTime.GetStrCurrentTime());
                ATMlist_tv_endtime.setText(YLSysTime.GetStrCurrentTime());
//                }
//                else {
//                    ATMlist_tv_endtime.setText(ylTask.getTaskATMEndTime());
//                }
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    @Override
    protected void onPostResume() {
        if (ylatmSiteAdapter != null){
            ylatmList = YLEditData.getYlatmList();
            Log.e(YLSystem.getKimTag(),ylatmList.toString());
            ylatmSiteAdapter.notifyDataSetChanged();
        }
        super.onPostResume();
    }
}
