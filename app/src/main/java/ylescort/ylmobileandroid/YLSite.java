package ylescort.ylmobileandroid;

import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import TaskClass.Site;
import TaskClass.TasksManager;
import TaskClass.YLTask;
import YLDataService.SiteDBSer;
import YLDataService.WebService;
import YLSystem.YLSystem;
import adapter.YLSiteAdapter;


public class YLSite extends ActionBarActivity {

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


//        Bundle bundle = this.getIntent().getExtras();
//        String taskid = bundle.getString("taskid");
        listView = (ListView)findViewById(R.id.ylsite_lv_MainView);
        LoadLocalData();

        //在按钮上调用下面的代码获取数据
//        WebService.GetTaskSite(getApplicationContext(), mHandler,ylTask.getTaskID());
//        Toast.makeText(getApplicationContext(), "正在获取...", Toast.LENGTH_SHORT).show();

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
                        //在这里处理获取到的网点
                        //UpdateLocalTaskList(lstYLTask); //同步本地的网点
                        //DisplayTaskSite(tasksManager.lstLatestTask); //显示网点列表
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

    private void LoadLocalData() {
//        SiteDBSer siteDBSer = new SiteDBSer(getApplicationContext());
//        List<Site> siteList = siteDBSer.GetSites("WHERE TaskID = '"+taskid+"'");
        List<Site> siteList =ylTask.lstSite;
        YLSiteAdapter ylSiteAdapter = new YLSiteAdapter(this,siteList,R.layout.activity_ylsiteitem);
        listView.setAdapter(ylSiteAdapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ylsite, menu);
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
}
