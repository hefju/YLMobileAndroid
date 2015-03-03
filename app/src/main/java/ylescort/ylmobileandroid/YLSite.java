package ylescort.ylmobileandroid;

import android.content.Intent;
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

import java.util.List;

import TaskClass.Box;
import TaskClass.Site;
import TaskClass.TasksManager;
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
