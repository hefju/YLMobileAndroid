package ylescort.ylmobileandroid;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import TaskClass.ArriveTime;
import TaskClass.Site;
import TaskClass.YLTask;
import YLDataService.SiteDBSer;
import adapter.YLSiteAdapter;


public class YLSite extends ActionBarActivity {

    private ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ylsite);

        Bundle bundle = this.getIntent().getExtras();
        String taskid = bundle.getString("taskid");
        listView = (ListView)findViewById(R.id.ylsite_lv_MainView);
        LoadLocalData(taskid);





        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick (AdapterView<?> parent, View view, int position, long id) {

                ListView listView1 = (ListView)parent;
               // YLTask ylTask = (YLTask)listView1.getItemAtPosition(position);
               // Toast.makeText(YLSite.this, siteList.getTaskID().toString(), Toast.LENGTH_SHORT).show();


                Intent intent = new Intent();
                intent.setClass(YLSite.this,box.class);
            /*  Bundle bundle = new Bundle();
                bundle.putString("taskid",ylTask.getTaskID().toString());
                intent.putExtras(bundle);*/
                startActivity(intent);

            }
        });

    }

    private void LoadLocalData(String taskid) {
        SiteDBSer siteDBSer = new SiteDBSer(getApplicationContext());
        //List<Site> siteList = siteDBSer.GetSites("WHERE TaskID = '"+taskid+"'");

        List<Site> siteList=new ArrayList<Site>();

        //============================测试数据开始添加===========================================
        //添加lstArriveTime类
        ArriveTime A1 = new ArriveTime();
        A1.EmpID="2703" ;//登陆的人员ID，记录操作人员ID.
        A1.ATime ="2014-08-08 08:55:00";//到达网点的时间
        A1.TimeID="1" ;//到达时间ID
        A1.TradeBegin="2014-08-08 09:00:00" ;//交易开始时间
        A1.TradeEnd ="2014-08-08 09:01:00";//交易结束时间
        A1.TradeState ="1";//这次到达完成交易了么？1为完成，0为未完成

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

        siteList.add(SI1);
        //============================以上测试数据===========================================

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
