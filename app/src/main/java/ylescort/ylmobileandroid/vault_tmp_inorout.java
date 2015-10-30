package ylescort.ylmobileandroid;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import TaskClass.User;
import TaskClass.YLTask;
import YLAdapter.YLTaskAdapter;
import YLDataService.WebServerTmpValutInorOut;
import YLSystemDate.YLSystem;

public class vault_tmp_inorout extends ActionBarActivity implements View.OnClickListener {

    private ListView vault_tmp_listview;
    private Button vault_tmp_btn_in;
    private Button vault_tmp_btn_out;
    private DatePicker vault_tmp_dp;
    private WebServerTmpValutInorOut webServerTmpValutInorOut;
    private YLTaskAdapter ylTaskAdapter;

    private List<YLTask> ylTasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vault_tmp_inorout);
        InitView();
        InitData();
    }

    private void InitData() {
        webServerTmpValutInorOut = new WebServerTmpValutInorOut(getApplicationContext());
        ylTasks = new ArrayList<>();
        DisplayTaskList(ylTasks);
    }

    private void InitView() {
        vault_tmp_listview = (ListView) findViewById(R.id.vault_tmp_listview);
        vault_tmp_btn_in = (Button)findViewById(R.id.vault_tmp_btn_in);
        vault_tmp_btn_out = (Button) findViewById(R.id.vault_tmp_btn_out);
        vault_tmp_dp = (DatePicker) findViewById(R.id.vault_tmp_dp);
        vault_tmp_btn_in.setOnClickListener(this);
        vault_tmp_btn_out.setOnClickListener(this);
        vault_tmp_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
    }

    private void DisplayTaskList( List<YLTask> lstYLTask) {
        if(lstYLTask==null){
            lstYLTask = new ArrayList<>();
        }
        ylTaskAdapter =  new YLTaskAdapter(this,lstYLTask,R.layout.activity_taskitem);
        vault_tmp_listview.setAdapter(ylTaskAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.vault_tmp_btn_in:
                LoadVaulttmpInTask();
                break;
            case R.id.vault_tmp_btn_out:
                LoadVaulttmpOutTask();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case 4:
                finish();
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void LoadVaulttmpOutTask() {
        LoadTmpTask("2");
    }


    private void LoadTmpTask(String timeid){
        try {
            User user = new User();
            user.setTaskDate(GetCalendarViewTime());
            user.setEmpID(YLSystem.getUser().getEmpID());
            user.setTime(timeid);
            ylTasks = webServerTmpValutInorOut.GetTmpTaskList(user);
            ylTaskAdapter.notifyDataSetChanged();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private  String GetCalendarViewTime(){
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        int year         = vault_tmp_dp.getYear();
        int monthOfYear  = vault_tmp_dp.getMonth();
        int dayOfMonth   = vault_tmp_dp.getDayOfMonth();
        calendar.set(year,monthOfYear,dayOfMonth);
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd",Locale.CHINA);
        String time=format.format(calendar.getTime());
        return  time;
    }

    private void LoadVaulttmpInTask() {
        LoadTmpTask("1");
    }
}
