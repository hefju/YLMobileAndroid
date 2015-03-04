package ylescort.ylmobileandroid;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import TaskClass.ArriveTime;
import TaskClass.Box;
import TaskClass.Site;
import TaskClass.TasksManager;
import TaskClass.User;
import TaskClass.YLTask;
import YLDataService.SiteDBSer;
import YLDataService.WebService;
import YLSystem.YLSystem;
import adapter.YLSiteAdapter;


public class YLSite extends ActionBarActivity {

    private TextView ylsite_tv_title;
    private ListView listView;
    private TasksManager tasksManager = null;//任务管理类
    private YLTask ylTask;//当前选中的任务
    android.os.Handler mHandler; //消息处理

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ylsite);

        tasksManager= YLSystem.getTasksManager();//获取任务管理类
        ylTask=tasksManager.CurrentTask;//当前选中的任务
        if(!ylTask.getTaskState().equals("有更新")){
            ylTask.setTaskState("进行中");
        }

        ylsite_tv_title=(TextView)findViewById(R.id.ylsite_tv_title);
        ylsite_tv_title.setText(ylTask.getLine());

        listView = (ListView)findViewById(R.id.ylsite_lv_MainView);
        DisplayTaskSite(ylTask.lstSite);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick (AdapterView<?> parent, View view, int position, long id) {
                ListView listView1 = (ListView) parent;
                Site site = (Site) listView1.getItemAtPosition(position);
                Toast.makeText(YLSite.this, site.getSiteName(), Toast.LENGTH_SHORT).show();


                String time="19:10";

                ArriveTime arriveTime = new ArriveTime();
                arriveTime.setEmpID(YLSystem.getUser().getEmpID());
                arriveTime.setATime(time);
                arriveTime.setTimeID("1");
                arriveTime.setTradeBegin(time + "");
                arriveTime.setTradeEnd(time + "");
                arriveTime.setTradeState("1");
                arriveTime.setSiteID(site.getSiteID());
                List<ArriveTime> arriveTimeList = new ArrayList<ArriveTime>();
                arriveTimeList.add(arriveTime);
                site.setLstArriveTime(arriveTimeList);
                ylTask.lstSite.add(site);

                Intent intent = new Intent();
                intent.setClass(YLSite.this, box.class);
                Bundle bundle = new Bundle();
                bundle.putString("siteid",site.getSiteID());
                bundle.putString("sitename",site.getSiteName());
                intent.putExtras(bundle);
                startActivity(intent);//我调用时Scan1DService会报错.
            }
        });


        //用于回传数据更新UI
        mHandler = new android.os.Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        break;
                    case 20: //获取GetTaskList成功
                        Log.d("jutest", "从服务器获取GetTaskStie成功");
                        List<Site> lstSite  = (List<Site>) msg.obj;
                        tasksManager.MergeSite(lstSite);//同步本地的网点
                        tasksManager.CurrentTask.setTaskState("进行中");
                        DisplayTaskSite(ylTask.lstSite); //显示网点列表
                        tasksManager.SaveTask(YLSite.this);
                        //Task_btn_refresh.setEnabled(true);//可以再次点击刷新了 //
                        break;
                    case 21://获取GetTaskStie失败, 服务器返回值不等于1, 获取数据失败
                        String content = (String) msg.obj;
                        Toast.makeText(getApplicationContext(), content, Toast.LENGTH_LONG).show();
                        //Task_btn_refresh.setEnabled(true);//可以再次点击刷新了
                        break;
                    case 100:
                        break;
                    default:
                        break;
                }
                super.handleMessage(msg);
            }
        };
    }

    ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();

    public void YLSite_UpDate(View view){
        singleThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    //============================测试数据开始添加
                    //添加lstArriveTime类
                    ArriveTime A1 = new ArriveTime();
                    A1.EmpID="2703" ;//登陆的人员ID，记录操作人员ID.
                    A1.ATime ="2014-08-08 08:55:00";//到达网点的时间
                    A1.TimeID="1" ;//到达时间ID
                    A1.TradeBegin="2014-08-08 09:00:00" ;//交易开始时间
                    A1.TradeEnd ="2014-08-08 09:01:00";//交易结束时间
                    A1.TradeState ="1";//这次到达完成交易了么？1为完成，0为未完成
                    //添加box类

                    Box B1 = new Box();
                    B1.SiteID="1759";//收，送
                    B1.BoxID="2";//箱ID
                    B1.BoxName="3";//箱名
                    B1.ActionTime="2014-08-08 09:00:00";//交接时间
                    B1.BoxStatus="实";//实，空
                    B1.BoxType="款箱";//款箱，卡箱，凭证
                    B1.TradeAction="收";//收，送
                    B1.TimeID="1" ;//到达时间ID
                    B1.BoxOrder= "1";
                    //添加site类
                    Site SI1 = new Site();
                    SI1.TaskID="6112" ;
                    SI1.SiteID ="1759";    //网点ID
                    SI1.SiteName="佛山中行星晖园支行" ;//网点名
                    SI1.SiteManager ="**";//网点负责人
                    SI1.SiteManagerPhone ="**";//网点负责人电话
                    SI1.SiteType="网点" ;//网点类型ATM还是网点
                    SI1.Status ="已交接";//交接状态: 未交接, 交接中, 已交接
                    SI1.ATMCount="0" ;//ATM数目
                    List<ArriveTime> LA = new ArrayList<ArriveTime>() ;
                    LA.add(A1);
                    SI1.lstArriveTime=LA;
                    //添加Task类
                    YLTask t1 = new YLTask();
                    t1.ServerVersion = ylTask.getServerVersion();
                    t1.TaskVersion=ylTask.getTaskVersion();
                    t1. TaskID=ylTask.getTaskID();
                    t1. TaskType=ylTask.getTaskType();
                    t1. Handset=ylTask.getHandset();
                    t1. TaskDate=ylTask.getTaskDate();
                    t1. Line=ylTask.getLine();
                    t1. TaskManager=ylTask.getTaskManager();
                    t1. TaskATMBeginTime=ylTask.getTaskATMBeginTime();
                    t1. TaskATMEndTime=ylTask.getTaskATMEndTime();
                    t1. TaskManagerNo=ylTask.getTaskManagerNo();
                    t1. ServerReturn=ylTask.getServerReturn();
//                    List<Site> LS = new ArrayList<Site>() ;
//                    LS.add(SI1);
//                    List<Box> LB= new ArrayList<Box>() ;
//                    LB.add(B1);
//                    t1.lstSite = LS;
//                    t1.lstBox= LB;

                    t1.lstSite=ylTask.lstSite;
                    t1.lstBox=ylTask.lstBox;

                    //添加数值到User类
                    User s1 = new User();
                    s1.EmpID="2703";
                    s1.EmpNO = "600241";
                    s1.Name = "杨磊";
                    s1.Pass = "8c4d6ed1b2688b2373bcac4137fab1e6";
                    s1.DeviceID = "NH026";
                    s1.ISWIFI = "1";
                    //以上是测试数据可以修改============================================================================


                    String url = "http://58.252.75.149:8055/YLMobileServiceAndroid.svc/UpLoad";//网址
                    HttpPost post = new HttpPost(url);
                    UpDataToService(t1, YLSystem.getUser(), post);

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

    private void DisplayTaskSite( List<Site> siteList) {
//        SiteDBSer siteDBSer = new SiteDBSer(getApplicationContext());
//        List<Site> siteList = siteDBSer.GetSites("WHERE TaskID = '"+taskid+"'");
       // List<Site> siteList =ylTask.lstSite;
        if (siteList==null||siteList.size()<1)return;
        YLSiteAdapter ylSiteAdapter = new YLSiteAdapter(this,siteList,R.layout.activity_ylsiteitem);
        listView.setAdapter(ylSiteAdapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ylsite, menu);
        if(ylTask.getTaskState()!="有更新"){
            menu.removeItem(0);//为什么不生效?
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.btnUpdateSite_ylsite) {
            if(ylTask.getTaskState()!="有更新") {
                Toast.makeText(getApplicationContext(),"已经是最新.",Toast.LENGTH_SHORT).show();
                return true;
            }
            WebService.GetTaskSite(getApplicationContext(), mHandler,ylTask.getTaskID());
            Toast.makeText(getApplicationContext(), "正在获取...", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
