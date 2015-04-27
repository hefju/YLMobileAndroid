package ylescort.ylmobileandroid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import TaskClass.Box;
import TaskClass.YLTask;
import YLAdapter.YLBoxEdiAdapter;
import YLDataService.YLBoxScanCheck;
import YLSystemDate.YLEditData;
import YLSystemDate.YLMediaPlayer;


public class vault_in_detail extends ActionBarActivity {

    private TextView vault_in_detail_tv_taskname;
    private ListView vault_in_detail_listview;
    private TextView vault_in_detail_tv_tolly;
    private TextView vault_in_detail_tv_check;
    private Button vault_in_detail_btn_scan1d;
    private Button vault_in_detail_btn_enter;

    private YLTask ylTask;
    private Scan1DRecive vaultindetailscan1DRecive;
    private YLMediaPlayer ylMediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vault_in_detail);
        InitView();
        InitDate();
        InitReciveScan1D();
    }

    private void InitDate() {
        ylTask = YLEditData.getYlTask();
        String title ="任务:"+ ylTask.getLine()+"   执行人:"+ ylTask.getTaskManager();
        vault_in_detail_tv_taskname.setText(title);
        ylMediaPlayer = new YLMediaPlayer();
    }

    private void InitView(){
        vault_in_detail_tv_taskname = (TextView) findViewById(R.id.vault_in_detail_tv_taskname);
        vault_in_detail_listview = (ListView) findViewById(R.id.vault_in_detail_listview);
        vault_in_detail_tv_tolly = (TextView) findViewById(R.id.vault_in_detail_tv_tolly);
        vault_in_detail_tv_check = (TextView) findViewById(R.id.vault_in_detail_tv_check);
        vault_in_detail_btn_scan1d = (Button) findViewById(R.id.vault_in_detail_btn_scan1d);
        vault_in_detail_btn_enter = (Button) findViewById(R.id.vault_in_detail_btn_enter);
    }

    private void InitReciveScan1D() {
        vaultindetailscan1DRecive = new Scan1DRecive();
        IntentFilter filter = new IntentFilter();
        filter.addAction("ylescort.ylmobileandroid.vault_in_detail");
        registerReceiver(vaultindetailscan1DRecive,filter);
        Intent start = new Intent(vault_in_detail.this,Scan1DService.class);
        vault_in_detail.this.startService(start);
    }

    private class Scan1DRecive extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String recivedata = intent.getStringExtra("result");
            if (recivedata != null){
                Box box= YLBoxScanCheck.CheckBox(recivedata, getApplicationContext());
                GetBoxToListView(box);
                }
        }
    }

    private void GetBoxToListView(Box box) {
        if (!box.getBoxName().equals("illegalbox") ||!box.getBoxName().equals("无数据")){
            try {

                ylMediaPlayer.SuccessOrFailMidia("success",getApplicationContext());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void DisPlayBoxlistAdapter(List<Box> boxList){
        if (boxList != null && boxList.size()>0){

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode){
            case 131:Scan1DCmd();
                break;
            case 132:ConfirmData();
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void ConfirmData() {

    }

    private void Scan1DCmd() {
        String activity = "ylescort.ylmobileandroid.vault_in_detail";
        Intent ac = new Intent();
        ac.setAction("ylescort.ylmobileandroid.Scan1DService");
        ac.putExtra("activity", activity);
        sendBroadcast(ac);
        Intent sendToservice = new Intent(vault_in_detail.this, Scan1DService.class); // 用于发送指令
        sendToservice.putExtra("cmd", "scan");
        this.startService(sendToservice); // 发送指令
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_vault_in_detail, menu);
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
