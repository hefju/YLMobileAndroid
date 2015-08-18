package ylescort.ylmobileandroid;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import TaskClass.Box;
import TaskClass.Site;
import TaskClass.TasksManager;
import TaskClass.YLTask;
import YLAdapter.YLSiteAdapter;
import YLDataService.AnalysisBoxList;
import YLSystemDate.YLSystem;

public class LocalCheck extends ActionBarActivity {

    private TextView  localcheck_tv_line;
    private TextView  localcheck_tv_title;
    private TextView  localcheck_tv_moneybox;
    private TextView  localcheck_tv_cardbox;
    private TextView  localcheck_tv_voucherbox;
    private TextView  localcheck_tv_voucherbag;
    private TextView  localcheck_tv_getbox;
    private TextView  localcheck_tv_givebox;
    private TextView  localcheck_tv_fullbox;
    private TextView  localcheck_tv_emptybox;
    private Button localcheck_btn_allbox;

    private ListView localcheck_listview;

    private TasksManager tasksManager = null;//任务管理类
    private YLTask ylTask;//当前选中的任务
    private List<Box> boxList;

    private YLSiteAdapter ylSiteAdapter;
    private AnalysisBoxList analysisBoxList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_check);
        try {
            InitView();
            InitData();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void InitView() {
        localcheck_tv_line = (TextView)findViewById(R.id.localcheck_tv_line);
        localcheck_tv_title = (TextView)findViewById(R.id.localcheck_tv_title);
        localcheck_tv_moneybox = (TextView)findViewById(R.id.localcheck_tv_moneybox);
        localcheck_tv_cardbox = (TextView)findViewById(R.id.localcheck_tv_cardbox);
        localcheck_tv_voucherbox = (TextView)findViewById(R.id.localcheck_tv_voucherbox);
        localcheck_tv_voucherbag = (TextView)findViewById(R.id.localcheck_tv_voucherbag);
        localcheck_tv_getbox = (TextView)findViewById(R.id.localcheck_tv_getbox);
        localcheck_tv_givebox = (TextView)findViewById(R.id.localcheck_tv_givebox);
        localcheck_tv_fullbox = (TextView)findViewById(R.id.localcheck_tv_fullbox);
        localcheck_tv_emptybox = (TextView)findViewById(R.id.localcheck_tv_emptybox);
        localcheck_listview = (ListView)findViewById(R.id.localcheck_listview);

        localcheck_btn_allbox = (Button)findViewById(R.id.localcheck_btn_allbox);

        localcheck_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListView listView = (ListView) parent;
                List<Box> analysisbox = new ArrayList<Box>();
                Site site =(Site) listView.getItemAtPosition(position);
                if (boxList == null)return;
                for (int i = 0 ; i < boxList.size();i++){
                    Box box = new Box();
                    box = boxList.get(i);
                    if (box.getSiteID().equals(site.getSiteID())){
                        analysisbox.add(box);
                    }
                }
                List<String> stringList = analysisBoxList.AnsysisBoxList(analysisbox);
                localcheck_tv_moneybox.setText("款箱:"+stringList.get(0));
                localcheck_tv_cardbox.setText("卡箱:"+stringList.get(1));
                localcheck_tv_voucherbox.setText("凭证箱:"+stringList.get(2));
                localcheck_tv_voucherbag.setText("凭证袋:"+stringList.get(3));
                localcheck_tv_getbox.setText("收箱:"+stringList.get(4));
                localcheck_tv_givebox.setText("送箱:"+stringList.get(5));
                localcheck_tv_fullbox.setText("实箱:"+stringList.get(6));
                localcheck_tv_emptybox.setText("空箱:"+stringList.get(7));
                localcheck_tv_title.setText(site.getSiteName());
            }
        });

        localcheck_btn_allbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> stringList = analysisBoxList.AnsysisBoxList(boxList);
                localcheck_tv_moneybox.setText("款箱:"+stringList.get(0));
                localcheck_tv_cardbox.setText("卡箱:"+stringList.get(1));
                localcheck_tv_voucherbox.setText("凭证箱:"+stringList.get(2));
                localcheck_tv_voucherbag.setText("凭证袋:"+stringList.get(3));
                localcheck_tv_getbox.setText("收箱:"+stringList.get(4));
                localcheck_tv_givebox.setText("送箱:"+stringList.get(5));
                localcheck_tv_fullbox.setText("实箱:"+stringList.get(6));
                localcheck_tv_emptybox.setText("空箱:"+stringList.get(7));
                localcheck_tv_title.setText("合计数据：");
            }
        });
    }

    private void InitData()throws Exception {
        tasksManager= YLSystem.getTasksManager();//获取任务管理类
        ylTask=tasksManager.CurrentTask;//当前选中的任务
        HandOverManAdapter(ylTask.getLstSite());
        boxList = new ArrayList<Box>();
        boxList = ylTask.getLstBox();
        analysisBoxList = new AnalysisBoxList();
        localcheck_tv_line.setText(ylTask.getLine());
//        List<String> stringList = analysisBoxList.AnsysisBoxList(boxList);
//
//        String anaylsisbox ="总计:收箱:"+stringList.get(4)+"送箱:"+stringList.get(5)+"实箱:"+stringList.get(6)+
//                "空箱:"+stringList.get(7)+"\n\r"+"款箱:"+stringList.get(0)+"卡箱:"+stringList.get(1)+"凭证箱:"+stringList.get(2)+"凭证袋:"+
//                stringList.get(3);
//        localcheck_tv_allbox.setText(anaylsisbox);
//        localcheck_tv_title.setText("未选择");

//        localcheck_tv_cardbox.setText("未选择");
    }

    private void HandOverManAdapter(List<Site> siteList){
        if (siteList == null)return;
        ylSiteAdapter =  new YLSiteAdapter(this,siteList,R.layout.activity_ylsiteitem);
        localcheck_listview.setAdapter(ylSiteAdapter);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4){
            finish();
            overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_local_check, menu);
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
