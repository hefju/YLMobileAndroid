package ylescort.ylmobileandroid;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.List;

import TaskClass.Site;
import TaskClass.TasksManager;
import TaskClass.YLTask;
import YLDataService.SiteDBSer;
import YLSystem.YLSystem;
import adapter.YLSiteAdapter;


public class YLSite extends ActionBarActivity {

    private ListView listView;
    private TasksManager tasksManager = null;//任务管理类
    private YLTask ylTask;//当前选中的任务

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
