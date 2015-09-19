package ylescort.ylmobileandroid;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Message;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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

import TaskClass.Box;
import TaskClass.Site;
import TaskClass.TasksManager;
import TaskClass.User;
import TaskClass.YLTask;
import YLDataService.WebServerValutInorOut;
import YLDataService.WebServerYLSite;
import YLDataService.WebService;
import YLSystemDate.YLEditData;
import YLSystemDate.YLSysTime;
import YLSystemDate.YLSystem;
import YLAdapter.YLSiteAdapter;


public class YLSite extends ActionBarActivity {

    private TextView ylsite_tv_title;
    private ListView listView;
    private TasksManager tasksManager = null;//任务管理类
    private YLTask ylTask;//当前选中的任务
    android.os.Handler mHandler; //消息处理
    private List<Site> siteList;
    private YLSiteAdapter ylSiteAdapter;

    private Button Site_apply;
    private Button Site_check;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ylsite);
        try {
            YLSite.this.setTitle("任务网点: " + YLSystem.getUser().getName());
            tasksManager = YLSystem.getTasksManager();//获取任务管理类
            ylTask = tasksManager.CurrentTask;//当前选中的任务

            if (!ylTask.getTaskState().equals("有更新")) {
                ylTask.setTaskState("进行中");
            }

            siteList = new ArrayList<>();

            ylsite_tv_title = (TextView) findViewById(R.id.ylsite_tv_title);
            ylsite_tv_title.setText(ylTask.getLine());

            listView = (ListView) findViewById(R.id.ylsite_lv_MainView);
            Site_apply = (Button) findViewById(R.id.Site_apply);
            Site_check = (Button) findViewById(R.id.Site_check);


            Site_apply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShowApplyAcivity();
                }
            });

            Site_check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShowCheckAcivity();
                }
            });


            DisplayTaskSite(ylTask.lstSite);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    OpenBoxAct((ListView) parent, position);
                }
            });


            //用于回传数据更新UI
            mHandler = new android.os.Handler() {
                @Override
                public void handleMessage(Message msg) {
                    switch (msg.what) {
                        case 1:
                            break;
                        case 20: //获取GetTaskList成功
                            Log.d("jutest", "从服务器获取GetTaskStie成功");
                            List<Site> lstSite = (List<Site>) msg.obj;
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

            //增加载入自动更新0330kim
            if (!ylTask.getTaskState().equals("有更新")) {
                Toast.makeText(getApplicationContext(), "已经最新.", Toast.LENGTH_SHORT).show();
                return;
            }
//        WebService.GetTaskSite(getApplicationContext(), mHandler,ylTask.getTaskID());
//        Toast.makeText(getApplicationContext(), "正在获取...", Toast.LENGTH_SHORT).show();
            GetSite();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void GetCarBoxlist() throws Exception {
        if (ylTask.getLstCarBox() == null || ylTask.getLstCarBox().size() == 0
                || ylTask.getLstCarBox().get(0).getBoxID() == null) {
            WebServerValutInorOut webServerValutInorOut = new WebServerValutInorOut();
            User user = new User();
            user.setTaskDate(ylTask.getTaskID());
            user.setDeviceID(YLSystem.getHandsetIMEI());
            user.setEmpID(YLSystem.getUser().getEmpID());
            List<Box> boxList = webServerValutInorOut.StoreGetBoxByTaskIDOut(user, getApplicationContext());
            if (boxList.get(0).getBoxID() != null) {
                ylTask.setLstCarBox(boxList);
            } else {
                List<Box> boxes = new ArrayList<Box>();
                boxes.clear();
                ylTask.setLstCarBox(boxes);
            }
        }
        Log.e(YLSystem.getKimTag(), ylTask.lstCarBox.size() + "在车数量");
    }

    private void GetSite() {
        WebServerYLSite webServerYLSite = new WebServerYLSite();
        List<Site> lstSite = null;
        try {
            User user = new User();
            user = YLSystem.getUser();
            user.setTaskDate(ylTask.getTaskID());
            lstSite = webServerYLSite.GetYLTaskSite(YLSystem.getUser(), getApplicationContext());
            tasksManager.MergeSite(lstSite);//同步本地的网点
            tasksManager.CurrentTask.setTaskState("进行中");
            DisplayTaskSite(ylTask.lstSite); //显示网点列表
            boolean getcarboxs = true;
            for (Site site : lstSite) {
                if (site.getStatus().equals("已完成")){
                    getcarboxs = false;
                }
            }
            if (getcarboxs){
                GetCarBoxlist();
            }
            tasksManager.SaveTask(YLSite.this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void ShowCheckAcivity() {
        Intent intent = new Intent();
        intent.setClass(this, LocalCheck.class);
        startActivity(intent);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    private void OpenBoxAct(ListView parent, int position) {

        ListView listView1 = (ListView) parent;
        Site site = (Site) listView1.getItemAtPosition(position);

        Intent intent = new Intent();

        intent.setClass(this, HomYLBoxScan.class);//新款箱扫描

        YLEditData.setCurrentYLSite(site);
//        Bundle bundle = new Bundle();
//        bundle.putString("siteid",site.getSiteID());
//        bundle.putString("sitename",site.getSiteName());
//        intent.putExtras(bundle);
        startActivity(intent);//我调用时Scan1DService会报错.
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    public void YLSite_UpDate(View view) {
        dialog();
    }

    private void ShowApplyAcivity() {
        Intent intent = new Intent();
        intent.setClass(this, HandovermanCheck.class);
        Bundle bundle = new Bundle();
        bundle.putString("taskid", ylTask.getTaskID());
        bundle.putString("taskName", ylTask.getLine());
        intent.putExtras(bundle);
        startActivity(intent);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    private String UpLoadService() throws Exception {
        UploadAsycnTask uploadAsycnTask = new UploadAsycnTask();
        uploadAsycnTask.execute();
        return uploadAsycnTask.get();
    }

    private class UploadAsycnTask extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... params) {
            String url = YLSystem.GetBaseUrl(getApplicationContext()) + "UpLoad";
            Log.e(YLSystem.getKimTag(), url);
            HttpPost post = new HttpPost(url);
            Gson gson = new Gson();
            JSONObject p = new JSONObject();
            try {
                YLTask t1 = ylTask;
                t1.lstSite = ylTask.lstSite;
                t1.lstBox = ylTask.lstBox;
                p.put("STask", gson.toJson(t1));//整个任务=====================自定义。。。。。
                p.put("empid", YLSystem.getUser().EmpID);//人员id=====================自定义。。。。。
                p.put("deviceID", YLSystem.getUser().DeviceID);//手持机号=====================自定义。。。。。
                p.put("ISWIFI", YLSystem.getUser().ISWIFI);//是否用WIFI=====================自定义。。。。。
                post.setEntity(new StringEntity(p.toString(), "UTF-8"));
                post.setHeader(HTTP.CONTENT_TYPE, "text/json");
                HttpClient client = new DefaultHttpClient();
                HttpResponse response = client.execute(post);
                Log.e(YLSystem.getKimTag(), "箱数：" + t1.getLstBox().size() + "网点数：" + t1.getLstSite().size());
                if (response.getStatusLine().getStatusCode() == 200) {
                    String content = EntityUtils.toString(response.getEntity());
                    Log.e(YLSystem.getKimTag(), t1.lstBox.size() + "boxlist");
                    return gson.fromJson(content, new TypeToken<String>() {
                    }.getType());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private void UpDataToService(YLTask t1, User s1, HttpPost post) throws JSONException, IOException {
        //添加数值到User类
        Gson gson = new Gson();
        //设置POST请求中的参数-------返回EmpID员工ID，ServerReturn 服务器错误，1为没错误，Time 服务器时间。
        JSONObject p = new JSONObject();
        p.put("STask", gson.toJson(t1));//整个任务=====================自定义。。。。。
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

    private void DisplayTaskSite(List<Site> siteList) {
//        SiteDBSer siteDBSer = new SiteDBSer(getApplicationContext());
//        List<Site> siteList = siteDBSer.GetSites("WHERE TaskID = '"+taskid+"'");
        // List<Site> siteList =ylTask.lstSite;
        if (siteList == null || siteList.size() < 1) return;
        ylSiteAdapter = new YLSiteAdapter(this, siteList, R.layout.activity_ylsiteitem);
        listView.setAdapter(ylSiteAdapter);

    }


    protected void dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(YLSite.this);
        builder.setMessage("确认上传吗?");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                try {
                    Date LoactTime = YLSysTime.GetDateCurrentTime();
                    Date UpdateTime = YLSysTime.StrToTime(ylTask.getServerReturn());
                    long longtime = LoactTime.getTime() - UpdateTime.getTime();
                    if (longtime <= 15000) {
                        Toast.makeText(getApplicationContext(), "请不要频繁上传数据", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    String serret = UpLoadService();
                    if (serret.equals("1")) {
                        ylTask.setTaskState("已上传");
                        ylTask.setServerReturn(YLSysTime.GetStrCurrentTime());
                        tasksManager.SaveTask(getApplicationContext());
                        UpDataDialog();
//                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "未上传成功,请重新上传", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "未上传成功,请连接网络重新上传", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
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

    private void UpDataDialog() {
        new AlertDialog.Builder(YLSite.this).setTitle("提示")
                .setMessage("任务已上传，谢谢！")
                .setPositiveButton("确定", null).show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4) {
            finish();
            overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ylsite, menu);
        if (!ylTask.getTaskState().equals("有更新")) {
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
            if (ylTask.getTaskState() != "有更新") {
                Toast.makeText(getApplicationContext(), "已经是最新.", Toast.LENGTH_SHORT).show();
                return true;
            }
//            WebService.GetTaskSite(getApplicationContext(), mHandler,ylTask.getTaskID());
            GetSite();
            Toast.makeText(getApplicationContext(), "正在获取...", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        if (ylTask.lstSite != null) {
            ylSiteAdapter.notifyDataSetInvalidated();
        }
        super.onResume();
    }
}
