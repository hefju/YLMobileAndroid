package ylescort.ylmobileandroid;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.hdhe.nfc.NFCcmdManager;
import com.android.hdhe.uhf.reader.Tools;

import java.util.List;

import TaskClass.BaseEmp;
import TaskClass.User;
import TaskClass.YLTask;
import YLAdapter.YLValuttaskitemAdapter;
import YLDataService.BaseEmpDBSer;
import YLDataService.WebService;
import YLSystemDate.YLEditData;
import YLSystemDate.YLMediaPlayer;
import YLSystemDate.YLSysTime;
import YLSystemDate.YLSystem;


public class vault_in_operate extends ActionBarActivity {

    private Button vault_in_operate_btn_readcard;
    private EditText vault_in_operate_et_empno;
    private Button vault_in_operate_btn_search;

    ListView vault_in_operate_lv;
    private NFCcmdManager manager ;
    private YLMediaPlayer player;
    private YLValuttaskitemAdapter ylValuttaskitemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vault_in_operate);
        vault_in_operate.this.setTitle("入库操作员: " + YLSystem.getUser().getName());
        InitHFreader();
        InitData();
        InitView();
//        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void InitData() {
        player = new YLMediaPlayer();
    }

    private void InitHFreader() {
        try{
            manager = NFCcmdManager.getNFCcmdManager(YLSystem.getHFport(), 115200, 0);
            manager.readerPowerOn();
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "HF初始化失败", Toast.LENGTH_SHORT).show();
        }
    }

    private void InitView() {

        vault_in_operate_et_empno = (EditText)findViewById(R.id.vault_in_operate_et_empno);
        vault_in_operate_btn_readcard = (Button)findViewById(R.id.vault_in_operate_btn_readcard);
        vault_in_operate_btn_search = (Button)findViewById(R.id.vault_in_operate_btn_search);

        vault_in_operate_lv = (ListView)findViewById(R.id.vault_in_operate_lv);

        vault_in_operate_btn_readcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {GetHanderovermanTask();}
                catch (Exception e) { e.printStackTrace(); }
            }
        });

        vault_in_operate_btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                String et = vault_in_operate_et_empno.getText().toString();
                if (et.length()>0){
                    GetHanderovermanbyEMPno(et);
                }}catch (Exception e){
                    e.printStackTrace();
                }
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });

        vault_in_operate_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListView listView = (ListView)parent;
                YLTask ylTask = (YLTask) listView.getItemAtPosition(position);
                YLEditData.setYlTask(ylTask);
                Intent intent = new Intent();
                intent.setClass(vault_in_operate.this,vault_in_detail.class);
                startActivity(intent);
            }
        });
    }

    private void GetHanderovermanTask() throws Exception{
        manager.init_14443A();
        manager.readerPowerOn();
        byte[] uid = manager.inventory_14443A();
        if(uid != null){
            if (!YLSystem.getNetWorkState().equals("2")){
                User user = new User();
                String EmpHF = Tools.Bytes2HexString(uid,uid.length);
                BaseEmpDBSer baseEmpDBSer = new BaseEmpDBSer(getApplicationContext());
                List<BaseEmp> baseEmpList = baseEmpDBSer.GetBaseEmps("where EmpHFNo ='"+EmpHF+"'" );

                if (baseEmpList.size()>0){
                    BaseEmp baseEmp = baseEmpList.get(0);
                    user.setEmpNO(baseEmp.EmpNo);
                    user.setEmpHFNo(baseEmp.EmpHFNo);
                    String pickdate = YLSysTime.DateToStr(YLEditData.getDatePick());
                    user.setTaskDate(pickdate);
                    user.setDeviceID(YLSystem.getHandsetIMEI());
                    user.setEmpID(YLSystem.getUser().getEmpID());
                    WebService webService = new WebService();
                    List<YLTask> ylTaskList = webService.GetHandovermanTask(user,getApplicationContext());
                    DisplayTaskList(ylTaskList);
                    player.SuccessOrFailMidia("success",getApplicationContext());
                }else {
                    player.SuccessOrFailMidia("fail",getApplicationContext());
                    Toast.makeText(getApplicationContext(),"本地数据未有资料,请更新缓存",Toast.LENGTH_SHORT).show();
                }
            }
        }else {
            player.SuccessOrFailMidia("fail",getApplicationContext());
            Toast.makeText(getApplicationContext(), "未找到卡", Toast.LENGTH_SHORT).show();
        }
        manager.readerPowerOff();
    }

    private void GetHanderovermanbyEMPno(String EMPNO)throws Exception{
        if (!YLSystem.getNetWorkState().equals("2")){
            User user = new User();
            BaseEmpDBSer baseEmpDBSer = new BaseEmpDBSer(getApplicationContext());
            List<BaseEmp> baseEmpList = baseEmpDBSer.GetBaseEmps("where EmpNo ='"+EMPNO+"'" );
            if (baseEmpList.size()>0){
                BaseEmp baseEmp = baseEmpList.get(0);
                user.setEmpNO(baseEmp.EmpNo);
                user.setEmpHFNo(baseEmp.EmpHFNo);
                String pickdate = YLSysTime.DateToStr(YLEditData.getDatePick());
                user.setTaskDate(pickdate);
                user.setDeviceID(YLSystem.getHandsetIMEI());
                user.setEmpID(YLSystem.getUser().getEmpID());
                WebService webService = new WebService();
                List<YLTask> ylTaskList = webService.GetHandovermanTask(user,getApplicationContext());
                DisplayTaskList(ylTaskList);
                player.SuccessOrFailMidia("success",getApplicationContext());
            }else {
                player.SuccessOrFailMidia("fail",getApplicationContext());
                Toast.makeText(getApplicationContext(),"本地数据未有资料,请更新缓存",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void DisplayTaskList(List<YLTask> ylTaskList){
        if (ylTaskList == null || ylTaskList.size()<1)return;
        ylValuttaskitemAdapter =
                new YLValuttaskitemAdapter(this,ylTaskList,R.layout.vault_in_operate_taskitem);
        vault_in_operate_lv.setAdapter(ylValuttaskitemAdapter);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode== 131 || keyCode == 132){
            try {GetHanderovermanTask();}
            catch (Exception e) {e.printStackTrace();}
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_vault_in_operate, menu);
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
    protected void onPostResume() {
        if (ylValuttaskitemAdapter!=null){
            ylValuttaskitemAdapter.notifyDataSetChanged();
        }

        super.onPostResume();
    }
}
