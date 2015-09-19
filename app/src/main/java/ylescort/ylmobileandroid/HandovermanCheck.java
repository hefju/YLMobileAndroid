package ylescort.ylmobileandroid;

import android.app.AlertDialog;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import TaskClass.Box;
import TaskClass.TasksManager;
import TaskClass.User;
import TaskClass.YLTask;
import YLAdapter.YLBoxEdiAdapter;
import YLDataService.AnalysisBoxList;
import YLDataService.WebService;
import YLSystemDate.YLSystem;
import ylescort.ylmobileandroid.R;


public class HandovermanCheck extends ActionBarActivity implements View.OnClickListener {

    private TextView handoverman_tv_Title;
    private ListView handoverman_listview;
    private TextView handoverman_tv_monenybox;
    private TextView handoverman_tv_cardbox;
    private TextView handoverman_tv_voucher;
    private TextView handoverman_tv_voucherbag;
    private TextView handoverman_tv_getbox;
    private TextView handoverman_tv_givebox;
    private TextView handoverman_tv_fullbox;
    private TextView handoverman_tv_emptybox;
    private Button handoverman_btn_cancel;
    private Button handoverman_btn_enter;
    private Button handoverman_btn_carbox;

    private TasksManager tasksManager = null;//任务管理类
    private YLTask ylTask;//当前选中的任务

    private List<Box> carlist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handoverman_check);
        try {
            InitView();
            InitData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void InitView() {

        Bundle bundle = this.getIntent().getExtras();
        String taskid = bundle.getString("taskid");
        String taskName = bundle.getString("taskName");

        handoverman_tv_Title = (TextView) findViewById(R.id.handoverman_tv_Title);
        handoverman_tv_monenybox = (TextView) findViewById(R.id.handoverman_tv_monenybox);
        handoverman_tv_cardbox = (TextView) findViewById(R.id.handoverman_tv_cardbox);
        handoverman_tv_voucher = (TextView) findViewById(R.id.handoverman_tv_voucher);
        handoverman_tv_voucherbag = (TextView) findViewById(R.id.handoverman_tv_voucherbag);
        handoverman_tv_getbox = (TextView) findViewById(R.id.handoverman_tv_getbox);
        handoverman_tv_givebox = (TextView) findViewById(R.id.handoverman_tv_givebox);
        handoverman_tv_fullbox = (TextView) findViewById(R.id.handoverman_tv_fullbox);
        handoverman_tv_emptybox = (TextView) findViewById(R.id.handoverman_tv_emptybox);
        handoverman_listview = (ListView) findViewById(R.id.handoverman_listview);
        handoverman_btn_cancel = (Button) findViewById(R.id.handoverman_btn_cancel);
        handoverman_btn_enter = (Button) findViewById(R.id.handoverman_btn_enter);
        handoverman_btn_carbox = (Button) findViewById(R.id.handoverman_btn_carbox);

        handoverman_tv_Title.setText(taskName);handoverman_tv_Title.setTag(taskid);

        handoverman_btn_enter.setOnClickListener(this);
        handoverman_btn_cancel.setOnClickListener(this);
        handoverman_btn_carbox.setOnClickListener(this);
    }


    private void InitData() throws Exception{

        tasksManager= YLSystem.getTasksManager();//获取任务管理类
        ylTask=tasksManager.CurrentTask;//当前选中的任务
        carlist = new ArrayList<>();
        if (ylTask.getLstCarBox() != null){
            carlist = ylTask.getLstCarBox();
        }

        List<Box> boxList = new ArrayList<>();
        WebService webService = new WebService();
        boxList =  webService.GetVaultInBoxList(handoverman_tv_Title.getTag().toString(),
                YLSystem.getUser().getDeviceID(),YLSystem.getUser().getEmpID(),getApplicationContext());
        Log.e(YLSystem.getKimTag(), boxList.toString());
        for (int i = 0;i<boxList.size();i++){
            boxList.get(i).setTradeAction("收");
        }
        BoxCheckAdapter(boxList);
        Analysis(boxList);
    }

    private void Analysis(List<Box> boxList) {
        if (boxList.size()==1& boxList.get(0).getServerReturn().equals("没有入库箱。"))return;
        AnalysisBoxList analysisBoxList = new AnalysisBoxList();
        List<String> stringList =  analysisBoxList.AnsysisBoxList(boxList);
        handoverman_tv_monenybox.setText("款箱:" + stringList.get(0));
        handoverman_tv_cardbox.setText("卡箱:"+stringList.get(1));
        handoverman_tv_voucher.setText("凭证箱:"+stringList.get(2));
        handoverman_tv_voucherbag.setText("凭证袋:"+stringList.get(3));
        handoverman_tv_getbox.setText("收箱:"+stringList.get(4));
        handoverman_tv_givebox.setText("送箱:" + stringList.get(5));
        handoverman_tv_fullbox.setText("实箱:" + stringList.get(6));
        handoverman_tv_emptybox.setText("空箱:" + stringList.get(7));
    }

    private void BoxCheckAdapter(List<Box> boxList){
        if (boxList == null)return;
        
        YLBoxEdiAdapter ylBoxEdiAdapter = new YLBoxEdiAdapter(getApplicationContext(),
                boxList,R.layout.activity_boxedititem);
        handoverman_listview.setAdapter(ylBoxEdiAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_handoverman_check, menu);
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
            case R.id.handoverman_btn_enter:
                try {
                    ComfirmStoreIn();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.handoverman_btn_cancel:
                finish();
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                break;
            case R.id.handoverman_btn_carbox:
                BoxCheckAdapter(carlist);
                break;
        }
    }

    private void ComfirmStoreIn()throws Exception{
        WebService webService = new WebService();
        User user = new User();
        user = YLSystem.getUser();
        user.setTaskDate(handoverman_tv_Title.getTag().toString());
        String SerRetrun =  webService.ComfirmStoreIn(user, getApplicationContext());
        Log.e(YLSystem.getKimTag(),SerRetrun+"");
        if (SerRetrun.equals("1")){
            Toast.makeText(getApplicationContext(),"已申请成功！",Toast.LENGTH_SHORT).show();
            finish();
            overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
        }else {
            Toast.makeText(getApplicationContext(),"未能申请成功，请连接有效网络再申请",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4){
            finish();
            overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
        }
        return super.onKeyDown(keyCode, event);
    }
}
