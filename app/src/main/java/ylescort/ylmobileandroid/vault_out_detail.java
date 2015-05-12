package ylescort.ylmobileandroid;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import TaskClass.Box;
import YLAdapter.YLValutboxitemAdapter;
import YLDataService.YLBoxScanCheck;
import YLSystemDate.YLMediaPlayer;
import YLSystemDate.YLSystem;


public class vault_out_detail extends ActionBarActivity implements View.OnClickListener {

    private TextView vault_out_detail_tv_taskname;
    private TextView vault_out_detail_tv_boxstaut;
    private TextView vault_out_detail_tv_type;

    private Button vault_out_detail_btn_readcard;
    private ListView vault_out_detail_lv;
    private Button vault_out_detail_btn_scan1d;
    private Button vault_out_detail_btn_boxtype;
    private Button vault_out_detail_btn_enter;

    private Scan1DRecive YLBoxscan1DRecive;  //广播接收者

    private List<Box> vaulteroutboxlist ;
    private YLMediaPlayer ylMediaPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vault_out_detail);
        InitView();
        InitData();
        InitReciveScan1D();
    }

    private void InitView() {
        vault_out_detail_tv_taskname = (TextView)findViewById(R.id.vault_out_detail_tv_taskname);
        vault_out_detail_tv_boxstaut = (TextView)findViewById(R.id.vault_out_detail_tv_boxstaut);
        vault_out_detail_tv_type = (TextView)findViewById(R.id.vault_out_detail_tv_type);
        vault_out_detail_btn_readcard = (Button)findViewById(R.id.vault_out_detail_btn_readcard);
        vault_out_detail_lv = (ListView)findViewById(R.id.vault_out_detail_lv);
        vault_out_detail_btn_scan1d = (Button)findViewById(R.id.vault_out_detail_btn_scan1d);
        vault_out_detail_btn_boxtype = (Button)findViewById(R.id.vault_out_detail_btn_boxtype);
        vault_out_detail_btn_enter = (Button)findViewById(R.id.vault_out_detail_btn_enter);

        vault_out_detail_btn_readcard.setOnClickListener(this);
        vault_out_detail_btn_scan1d.setOnClickListener(this);
        vault_out_detail_btn_boxtype.setOnClickListener(this);
        vault_out_detail_btn_enter.setOnClickListener(this);

        vault_out_detail_btn_scan1d.setEnabled(false);
        vault_out_detail_btn_enter.setEnabled(false);

        vault_out_detail_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListView listView = (ListView) parent;
                //Box box = (Box) listView.getItemAtPosition(position);
                YLBoxchangeType("setbox",position);

            }
        });
    }

    private void InitData() {
        vaulteroutboxlist = new ArrayList<>();
        ylMediaPlayer = new YLMediaPlayer();
    }

    private void InitReciveScan1D() {
        YLBoxscan1DRecive = new Scan1DRecive();
        IntentFilter filter = new IntentFilter();
        filter.addAction("ylescort.ylmobileandroid.vault_out_detail");
        registerReceiver(YLBoxscan1DRecive,filter);
        Intent start = new Intent(vault_out_detail.this,Scan1DService.class);
        vault_out_detail.this.startService(start);
    }

    private void YLBoxScan1D() {
        if (vault_out_detail_btn_scan1d.getText().equals("扫描")){
             Scan1DCmd("toscan100ms");
            vault_out_detail_btn_scan1d.setText("停止");
        }else {
            Scan1DCmd("stopscan");
            vault_out_detail_btn_scan1d.setText("扫描");
        }

    }

    private void YLBoxchangeType(final String type, final int position ) {
        new AlertDialog.Builder(this).setTitle("请选择类型").setIcon(android.R.drawable.ic_dialog_info)
                .setSingleChoiceItems(R.array.ylboxvalut, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Box box = new Box();
                        switch (which) {
                            case 0:
                                box.setBoxType("款箱");
                                box.setBoxStatus("实");
                                break;
                            case 1:
                                box.setBoxType("卡箱");
                                box.setBoxStatus("实");
                                break;
                            case 2:
                                box.setBoxType("凭证");
                                box.setBoxStatus("实");
                                break;
                            case 3:
                                box.setBoxType("款箱");
                                box.setBoxStatus("空");
                                break;
                            case 4:
                                box.setBoxType("卡箱");
                                box.setBoxStatus("空");
                                break;
                            case 5:
                                box.setBoxType("凭证");
                                box.setBoxStatus("空");
                                break;
                            case 6:
                                box.setBoxType("删除");
                                box.setBoxStatus("删除");
                                break;
                        }
                        if (type.equals("settype")) {
                            if (box.getBoxType().equals("删除")) {
                                dialog.dismiss();
                                return;
                            }
                            vault_out_detail_tv_boxstaut.setText(box.getBoxStatus());
                            vault_out_detail_tv_type.setText(box.getBoxType());
                            vault_out_detail_btn_scan1d.setEnabled(true);
                            vault_out_detail_btn_enter.setEnabled(true);
                        } else {
                            if (box.getBoxType().equals("删除")) {
                                vaulteroutboxlist.remove(position);
                            } else {
                                Box listbox = vaulteroutboxlist.get(position);
                                box.setBoxName(listbox.getBoxName());
                                box.setBoxID(listbox.getBoxID());
                                vaulteroutboxlist.set(position, box);
                            }
                        }
                        DisPlayBoxlistAdapter(vaulteroutboxlist);
                        dialog.dismiss();
                    }
                }).show();
    }

    private void YLBoxEnter() {

    }

    private void ReadHFCard(){

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_vault_out_detail, menu);
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
            case R.id.vault_out_detail_btn_scan1d:
                YLBoxScan1D();
                break;
            case R.id.vault_out_detail_btn_boxtype:
                YLBoxchangeType("settype", 0);
                break;
            case R.id.vault_out_detail_btn_enter:
                YLBoxEnter();
                break;
            case R.id.vault_out_detail_btn_readcard:
                ReadHFCard();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode){
            case 131:YLBoxScan1D();
                break;
            case 132:YLBoxScan1D();
                break;
            case 133:YLBoxScan1D();
                break;
            case 134:YLBoxScan1D();
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    private class Scan1DRecive extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String recivedata = intent.getStringExtra("result");
            if (recivedata != null){
                Box box = YLBoxScanCheck.CheckBox(recivedata,getApplicationContext());
                AddYLBoxtoListView(box);
            }
        }
    }

    private void AddYLBoxtoListView(Box box) {
        if (!box.getBoxName().equals("illegalbox") ||!box.getBoxName().equals("无数据")){
            try{
                boolean boxcheck = true;
                int listcount = vaulteroutboxlist.size()-1;
                for (int i = 0;i<vaulteroutboxlist.size();i++){
                    Box scanbox = vaulteroutboxlist.get(listcount-i);
                    if (scanbox.getBoxID().equals(box.getBoxID())){
                        ylMediaPlayer.SuccessOrFailMidia("fail",getApplicationContext());
                        boxcheck = false;
                        break;
                    }
                }

                if (boxcheck){
                    box.setBoxType(vault_out_detail_tv_type.getText().toString());
                    box.setBoxStatus(vault_out_detail_tv_boxstaut.getText().toString());
                    vaulteroutboxlist.add(box);
                    DisPlayBoxlistAdapter(vaulteroutboxlist);
                    ylMediaPlayer.SuccessOrFailMidia("success", getApplicationContext());
                    scrollMyListViewToBottom();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void DisPlayBoxlistAdapter(List<Box> boxList){
        if (boxList != null ){
            YLValutboxitemAdapter ylValutboxitemAdapter =
                    new YLValutboxitemAdapter(getApplicationContext(),boxList,R.layout.vault_in_detail_boxitem);
            vault_out_detail_lv.setAdapter(ylValutboxitemAdapter);
        }
    }

    private void scrollMyListViewToBottom() {
        vault_out_detail_lv.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                vault_out_detail_lv.setSelection(vaulteroutboxlist.size() - 1);
            }
        });
    }


    private void Scan1DCmd(String cmd) {
        if (vault_out_detail_tv_boxstaut.getText().equals("状态")& !cmd.equals("stopscan")){
            Toast.makeText(getApplicationContext(),"未设置状态",Toast.LENGTH_SHORT).show();
            return;}

        String activity = "ylescort.ylmobileandroid.vault_out_detail";
        Intent ac = new Intent();
        ac.setAction("ylescort.ylmobileandroid.Scan1DService");
        ac.putExtra("activity", activity);
        sendBroadcast(ac);
        Intent sendToservice = new Intent(vault_out_detail.this, Scan1DService.class); // 用于发送指令
        sendToservice.putExtra("cmd", cmd);
        this.startService(sendToservice); // 发送指令
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(YLBoxscan1DRecive);
        Scan1DCmd("stopscan");
        super.onDestroy();
    }
}
