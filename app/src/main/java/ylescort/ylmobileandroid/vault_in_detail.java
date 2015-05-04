package ylescort.ylmobileandroid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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

import java.util.ArrayList;
import java.util.List;

import TaskClass.Box;
import TaskClass.YLTask;
import YLAdapter.YLBoxEdiAdapter;
import YLAdapter.YLValutboxitemAdapter;
import YLDataService.WebService;
import YLDataService.YLBoxScanCheck;
import YLSystemDate.YLEditData;
import YLSystemDate.YLMediaPlayer;
import YLSystemDate.YLSystem;


public class vault_in_detail extends ActionBarActivity implements View.OnClickListener {

    private TextView vault_in_detail_tv_taskname;
    private ListView vault_in_detail_listview;
    private TextView vault_in_detail_tv_tolly;
    private TextView vault_in_detail_tv_check;
    private Button vault_in_detail_btn_scan1d;
    private Button vault_in_detail_btn_enter;

    private YLTask ylTask;
    private Scan1DRecive vaultindetailscan1DRecive;
    private YLMediaPlayer ylMediaPlayer;

    private List<Box> homlistbox;
    private List<Box> Scanlistbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vault_in_detail);
        try {
            InitView();
            InitDate();
            InitReciveScan1D();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void InitDate()throws Exception{
        ylTask = YLEditData.getYlTask();
        String title ="任务:"+ ylTask.getLine()+"   执行人:"+ ylTask.getTaskManager();
        vault_in_detail_tv_taskname.setText(title);
        ylMediaPlayer = new YLMediaPlayer();
        WebService webService = new WebService();
        homlistbox = webService.GetVaultInBoxList(ylTask.getTaskID(),YLSystem.getHandsetIMEI(),
                YLSystem.getUser().getEmpID(),getApplicationContext());
        DisPlayBoxlistAdapter(homlistbox);
        StatisticalBoxList(homlistbox);
    }

    private void InitView(){
        vault_in_detail_tv_taskname = (TextView) findViewById(R.id.vault_in_detail_tv_taskname);
        vault_in_detail_listview = (ListView) findViewById(R.id.vault_in_detail_listview);
        vault_in_detail_tv_tolly = (TextView) findViewById(R.id.vault_in_detail_tv_tolly);
        vault_in_detail_tv_check = (TextView) findViewById(R.id.vault_in_detail_tv_check);
        vault_in_detail_btn_scan1d = (Button) findViewById(R.id.vault_in_detail_btn_scan1d);
        vault_in_detail_btn_enter = (Button) findViewById(R.id.vault_in_detail_btn_enter);

        vault_in_detail_btn_scan1d.setOnClickListener(this);
        vault_in_detail_btn_enter.setOnClickListener(this);
    }

    private void InitReciveScan1D() {
        vaultindetailscan1DRecive = new Scan1DRecive();
        IntentFilter filter = new IntentFilter();
        filter.addAction("ylescort.ylmobileandroid.vault_in_detail");
        registerReceiver(vaultindetailscan1DRecive,filter);
        Intent start = new Intent(vault_in_detail.this,Scan1DService.class);
        vault_in_detail.this.startService(start);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.vault_in_detail_btn_scan1d:Scan1DCmd();
                break;
            case R.id.vault_in_detail_btn_enter:ConfirmData();
                break;
        }
    }

    private class Scan1DRecive extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String recivedata = intent.getStringExtra("result");
            if (recivedata != null){
                Box box= YLBoxScanCheck.CheckBox(recivedata, getApplicationContext());
                GetBoxToListView(box);}
        }
    }

    private void GetBoxToListView(Box box) {
        if (!box.getBoxName().equals("illegalbox") ||!box.getBoxName().equals("无数据")){
            try {
                if (homlistbox.size()<1)return;
                boolean boxcheck = true;
                for (int i = 0 ; i <homlistbox.size();i++){
                    Box hombox = homlistbox.get(i);
                    if (hombox.getBoxID().equals(box.getBoxID())){
                        if (hombox.getValutcheck()==null){
                        hombox.setValutcheck("√");
                        homlistbox.set(i,hombox);
                        boxcheck = false;
                        break;}
                        else if (hombox.getValutcheck().equals("多")
                                ||hombox.getValutcheck().equals("√") ){
                            ylMediaPlayer.SuccessOrFailMidia("fail", getApplicationContext());
                            return;
                        }
                    }
                }
                if (boxcheck){
                    box.setValutcheck("多");
                    box.setBoxCount("1");
                    homlistbox.add(box);
                }
                DisPlayBoxlistAdapter(homlistbox);
                ylMediaPlayer.SuccessOrFailMidia("success", getApplicationContext());
                StatisticalBoxList(homlistbox);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void StatisticalBoxList(List<Box> boxList){

        if (boxList == null || boxList.size() < 1)return;
        int homstr = 0,vaulter= 0;
        for (Box box :boxList){
            if (box.getValutcheck() == null){
                homstr +=1;
            }else if (box.getValutcheck().equals("√")){
                vaulter +=1;
                homstr +=1;
            }else if (box.getValutcheck().equals("多")){
                vaulter +=1;
            }
        }
        vault_in_detail_tv_tolly.setText("业务员上传数量:"+homstr+"个");
        vault_in_detail_tv_check.setText("库管员扫描数量"+vaulter+"个");
    }

    private void DisPlayBoxlistAdapter(List<Box> boxList){
        if (boxList != null && boxList.size()>0){
            YLValutboxitemAdapter ylValutboxitemAdapter =
                    new YLValutboxitemAdapter(getApplicationContext(),boxList,R.layout.vault_in_detail_boxitem);
            vault_in_detail_listview.setAdapter(ylValutboxitemAdapter);
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

    @Override
    protected void onDestroy() {
        unregisterReceiver(vaultindetailscan1DRecive);
        super.onDestroy();
    }
}
