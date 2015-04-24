package ylescort.ylmobileandroid;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.hdhe.nfc.NFCcmdManager;
import com.android.hdhe.uhf.reader.Tools;

import java.util.List;

import TaskClass.User;
import TaskClass.YLTask;
import YLAdapter.YLValuttaskitemAdapter;
import YLDataService.WebService;
import YLSystemDate.YLSystem;


public class vault_in_operate extends ActionBarActivity {

    Button vault_in_operate_btn_readcard;
    ListView vault_in_operate_lv;
    private NFCcmdManager manager ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vault_in_operate);
        vault_in_operate.this.setTitle("入库操作员: "+YLSystem.getUser().getName());
        InitHFreader();
        InitView();

    }

    private void InitHFreader() {
        try{
            manager = NFCcmdManager.getNFCcmdManager(13, 115200, 0);
            manager.readerPowerOn();
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "HF初始化失败", Toast.LENGTH_SHORT).show();
        }
    }

    private void InitView() {
        vault_in_operate_btn_readcard = (Button)findViewById(R.id.vault_in_operate_btn_readcard);
        vault_in_operate_lv = (ListView)findViewById(R.id.vault_in_operate_lv);
        vault_in_operate_btn_readcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetHanderovermanTask();
            }
        });
    }

    private void GetHanderovermanTask(){
        manager.init_14443A();
        byte[] uid = manager.inventory_14443A();
        if(uid != null){
            if (!YLSystem.getNetWorkState().equals("2")){
                String url = YLSystem.GetBaseUrl(getApplicationContext())+"GetTask1";
                User user = new User();
                user.setEmpNO(Tools.Bytes2HexString(uid,uid.length));
                /*
                * 将员工HF卡查找数据库获取新User;
                * */
                WebService webService = new WebService();
                try {
                    List<YLTask> ylTaskList = webService.GetHandovermanTask(user,url);
                    //显示任务列表
                    Log.e("yltask",ylTaskList.toString());
                    DisplayTaskList(ylTaskList);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void DisplayTaskList(List<YLTask> ylTaskList){
        if (ylTaskList == null || ylTaskList.size()<1)return;
        YLValuttaskitemAdapter ylValuttaskitemAdapter =
                new YLValuttaskitemAdapter(this,ylTaskList,R.layout.vault_in_operate_taskitem);
        vault_in_operate_lv.setAdapter(ylValuttaskitemAdapter);
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
}
