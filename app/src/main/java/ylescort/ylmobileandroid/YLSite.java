package ylescort.ylmobileandroid;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import TaskClass.Site;
import TaskClass.TasksManager;
import TaskClass.User;
import TaskClass.YLTask;
import YLDataService.WebService;
import YLSystem.YLSystem;
import adapter.YLSiteAdapter;


public class YLSite extends ActionBarActivity {

    private TextView ylsite_tv_title;
    private ListView listView;
    private TasksManager tasksManager = null;//任务管理类
    private YLTask ylTask;//当前选中的任务
    android.os.Handler mHandler; //消息处理
    private List<Site> siteList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ylsite);
        YLSite.this.setTitle("任务网点: "+YLSystem.getUser().getName());
        tasksManager= YLSystem.getTasksManager();//获取任务管理类
        ylTask=tasksManager.CurrentTask;//当前选中的任务
        if(!ylTask.getTaskState().equals("有更新")){
            ylTask.setTaskState("进行中");
        }
        siteList = new ArrayList<>();

        ylsite_tv_title=(TextView)findViewById(R.id.ylsite_tv_title);
        ylsite_tv_title.setText(ylTask.getLine());

        listView = (ListView)findViewById(R.id.ylsite_lv_MainView);
        DisplayTaskSite(ylTask.lstSite);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick (AdapterView<?> parent, View view, int position, long id) {
                OpenBoxAct((ListView) parent, position);
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

    private void OpenBoxAct(ListView parent, int position) {

        ListView listView1 = (ListView) parent;
        Site site = (Site) listView1.getItemAtPosition(position);
        //Toast.makeText(this, site.getSiteName(), Toast.LENGTH_SHORT).show();

//        String time="19:10";
//        ArriveTime arriveTime = new ArriveTime();
//        arriveTime.setEmpID(YLSystem.getUser().getEmpID());
//        arriveTime.setATime(time);
//        arriveTime.setTimeID("1");
//        arriveTime.setTradeBegin(time + "");
//        arriveTime.setTradeEnd(time + "");
//        arriveTime.setTradeState("1");
//        arriveTime.setSiteID(site.getSiteID());
//        List<ArriveTime> arriveTimeList = new ArrayList<ArriveTime>();
//        arriveTimeList.add(arriveTime);
//        site.setLstArriveTime(arriveTimeList);
//        siteList.add(site);

        Intent intent = new Intent();
        intent.setClass(this, YLBoxScan.class);
        Bundle bundle = new Bundle();
        bundle.putString("siteid",site.getSiteID());
        bundle.putString("sitename",site.getSiteName());
        intent.putExtras(bundle);
        startActivity(intent);//我调用时Scan1DService会报错.
    }

    ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();

    public void YLSite_UpDate(View view){

        //ylTask.lstSite = siteList;
        dialog();
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


    protected void dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(YLSite.this);
        builder.setMessage("确认上传吗?");
        builder.setTitle("提示");
        builder.setPositiveButton("确认",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                singleThreadExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            YLTask t1 = ylTask;
                            t1.lstSite=ylTask.lstSite;
                            t1.lstBox=ylTask.lstBox;
                            String url = "http://58.252.75.149:8055/YLMobileServiceAndroid.svc/UpLoad";//网址
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
    @Override
    protected void onResume() {
        DisplayTaskSite(ylTask.lstSite);
        super.onResume();
    }

}
