package ylescort.ylmobileandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import TaskClass.User;
import TaskClass.YLTask;
import YLAdapter.YLTaskAdapter;
import YLDataService.WebServerTmpValutInorOut;
import YLSystemDate.YLEditData;
import YLSystemDate.YLSysTime;
import YLSystemDate.YLSystem;

public class vault_tmp_inorout extends ActionBarActivity implements View.OnClickListener {

    private ListView vault_tmp_listview;
    private Button vault_tmp_btn_in;
    private Button vault_tmp_btn_out;
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
        vault_tmp_btn_in.setOnClickListener(this);
        vault_tmp_btn_out.setOnClickListener(this);
        vault_tmp_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ListView listView = (ListView)adapterView;
                YLTask ylTask = (YLTask)listView.getItemAtPosition(i);
//                String tasktype =  ylTask.getTaskID();
                YLEditData.setYlTask(ylTask);

                if (ylTask.getTaskVersion().equals("")){
                    Toast.makeText(getApplicationContext(),"获取任务不成功，请重新下载任务。",Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent();
                intent.setClass(vault_tmp_inorout.this,vault_tmp_scan.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    private void DisplayTaskList( List<YLTask> lstYLTask) {
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
        LoadTmpTask("1");
    }


    private void LoadTmpTask(String timeid){
        try {
            String gettime = YLSysTime.DateToStr(YLEditData.getDatePick());
            User user = new User();
            user.setTaskDate(gettime);
            user.setEmpID(YLSystem.getUser().getEmpID());
            user.setTime(timeid);
            ylTasks = webServerTmpValutInorOut.GetTmpTaskList(user);
//            ylTaskAdapter.notifyDataSetChanged();
            YLEditData.setTimeID(timeid);
            DisplayTaskList(ylTasks);


        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private void LoadVaulttmpInTask() {
        LoadTmpTask("2");
    }
}
