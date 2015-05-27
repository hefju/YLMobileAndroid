package ylescort.ylmobileandroid;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;

import java.util.ArrayList;
import java.util.List;

import TaskClass.TasksManager;
import TaskClass.User;
import TaskClass.YLTask;
import YLAdapter.YLValuttaskitemAdapter;
import YLDataService.WebService;
import YLSystemDate.YLEditData;
import YLSystemDate.YLSysTime;
import YLSystemDate.YLSystem;


public class vault_out_operate extends ActionBarActivity implements View.OnClickListener {

    private EditText vault_out_operate_ET_taskname;
    private Button   vault_out_operate_btn_search;
    private Button   vault_out_operate_btn_readcard;
    private ListView vault_out_operate_lv_tasklist;
    private NumberPicker vault_out_operate_numberpicktens;
    private NumberPicker vault_out_operate_numberpickunits;
    private TasksManager tasksManager ;
    private YLValuttaskitemAdapter ylValuttaskitemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vault_out_operate);
        InitView();

    }

    private void InitView() {
        vault_out_operate.this.setTitle("出入库操作员: "+ YLSystem.getUser().getName());
        vault_out_operate_ET_taskname = (EditText) findViewById(R.id.vault_out_operate_ET_taskname);
        vault_out_operate_btn_search = (Button) findViewById(R.id.vault_out_operate_btn_search);
        vault_out_operate_btn_readcard = (Button) findViewById(R.id.vault_out_operate_btn_readcard);
        vault_out_operate_lv_tasklist = (ListView) findViewById(R.id.vault_out_operate_lv_tasklist);
        vault_out_operate_numberpicktens = (NumberPicker)findViewById(R.id.vault_out_operate_numberpicktens);
        vault_out_operate_numberpickunits = (NumberPicker)findViewById(R.id.vault_out_operate_numberpickunits);

        vault_out_operate_btn_search.setOnClickListener(this);
        vault_out_operate_btn_readcard.setOnClickListener(this);

        vault_out_operate_numberpicktens.setMinValue(0);
        vault_out_operate_numberpicktens.setMaxValue(9);
        vault_out_operate_numberpickunits.setMinValue(0);
        vault_out_operate_numberpickunits.setMaxValue(9);

        vault_out_operate_lv_tasklist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListView listView = (ListView)parent;
                YLTask ylTask = (YLTask)listView.getItemAtPosition(position);
                //tasksManager.SetCurrentTask(ylTask.getTaskID());
                YLEditData.setYlTask(ylTask);
                Intent intent = new Intent();
                intent.setClass(vault_out_operate.this,vault_out_detail.class);
                startActivity(intent);
            }
        });
    }


    private void GetTaskbyReadCard() {

    }

    private void SearchYLTask() {
        try {
            int tens = vault_out_operate_numberpicktens.getValue();
            int units = vault_out_operate_numberpickunits.getValue();
            String Line = "0" + tens + units;
            String taskdate = YLSysTime.DateToStr(YLEditData.getDatePick());
            User user = new User();
            user.setEmpHFNo("");
            user.setEmpNO("");
            user.setTaskDate(taskdate);
            user.setDeviceID("");
            user.setEmpID(YLSystem.getUser().getEmpID());
            user.setName(Line);//借用user类传入线路编号
            WebService webService = new WebService();
            List<YLTask> ylTaskList = webService.GetVaultOutTask(user, getApplication());
            Log.e(YLSystem.getKimTag(),ylTaskList.toString());
            if (ylTaskList.size()==1 && ylTaskList.get(0).getServerReturn().equals("没有出库任务。")){
                ShowNoTaskDialog();
                ylTaskList = new ArrayList<>();
            }
            DisplayYLTaskAdapter(ylTaskList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void ShowNoTaskDialog() {
        new AlertDialog.Builder(vault_out_operate.this).setTitle("提示")
                .setMessage("该日期未查询到该路线任务\r\n请重新选择")
                .setPositiveButton("确定", null).show();
    }

    private void DisplayYLTaskAdapter(List<YLTask> ylTaskList) {
        if (ylTaskList == null || ylTaskList.size()<1)return;
        if (ylTaskList.size()== 1){
            ylValuttaskitemAdapter =
                    new YLValuttaskitemAdapter(this,ylTaskList,R.layout.vault_in_operate_taskitem);
            vault_out_operate_lv_tasklist.setAdapter(ylValuttaskitemAdapter);
        }else {
            ylValuttaskitemAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case 131:
                SearchYLTask();
                break;
            case 132:
                GetTaskbyReadCard();
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_vault_out_operate, menu);
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
            case R.id.vault_out_operate_btn_search:SearchYLTask();
                break;
            case R.id.vault_out_operate_btn_readcard:GetTaskbyReadCard();
                break;
        }
    }

    @Override
    protected void onPostResume() {
        if (ylValuttaskitemAdapter != null){
            ylValuttaskitemAdapter.notifyDataSetChanged();
        }
        super.onPostResume();
    }
}
