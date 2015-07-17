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
import TaskClass.User;
import TaskClass.YLTask;
import YLAdapter.YLValutboxitemAdapter;
import YLDataService.AnalysisBoxList;
import YLDataService.WebServerValutInorOut;
import YLDataService.WebService;
import YLDataService.YLBoxScanCheck;
import YLSystemDate.YLEditData;
import YLSystemDate.YLMediaPlayer;
import YLSystemDate.YLSysTime;
import YLSystemDate.YLSystem;


public class vault_out_detail extends ActionBarActivity implements View.OnClickListener {

    private TextView vault_out_detail_tv_taskname;
    private TextView vault_out_detail_tv_boxstaut;
    private TextView vault_out_detail_tv_type;

    private Button vault_out_detail_btn_readcard;
    private ListView vault_out_detail_lv;
    private Button vault_out_detail_btn_scan1d;
    private Button vault_out_detail_btn_scanuhf;
    private Button vault_out_detail_btn_enter;

    private Scan1DRecive YLBoxscan1DRecive;  //广播接收者
    private Scan1DRecive YLBoxscanUHFRecive;

    private List<Box> vaulteroutboxlist ;
    private YLMediaPlayer ylMediaPlayer;
    private List<Box> AllboxList;

    private AnalysisBoxList analysisBoxList;
    private WebServerValutInorOut webServerValutInorOut;
    private YLValutboxitemAdapter ylValutboxitemAdapter;

    private YLTask ylTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vault_out_detail);
        try{
            InitView();
            InitData();
            InitReciveScan1D();
//            InitReciveScanUHF();
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    private void InitReciveScanUHF() {
        YLBoxscanUHFRecive = new Scan1DRecive();
        IntentFilter filter = new IntentFilter();
        filter.addAction("ylescort.ylmobileandroid.vault_in_detail");
        registerReceiver(YLBoxscanUHFRecive, filter);
        Intent start = new Intent(vault_out_detail.this,ScanUHFService.class);
        vault_out_detail.this.startService(start);
    }

    private void InitView() {

        vault_out_detail_tv_taskname = (TextView)findViewById(R.id.vault_out_detail_tv_taskname);
        vault_out_detail_tv_boxstaut = (TextView)findViewById(R.id.vault_out_detail_tv_boxstaut);
        vault_out_detail_tv_type = (TextView)findViewById(R.id.vault_out_detail_tv_type);
        vault_out_detail_btn_readcard = (Button)findViewById(R.id.vault_out_detail_btn_readcard);
        vault_out_detail_lv = (ListView)findViewById(R.id.vault_out_detail_lv);
        vault_out_detail_btn_scan1d = (Button)findViewById(R.id.vault_out_detail_btn_scan1d);
        vault_out_detail_btn_scanuhf = (Button)findViewById(R.id.vault_out_detail_btn_scanuhf);
        vault_out_detail_btn_enter = (Button)findViewById(R.id.vault_out_detail_btn_enter);

        vault_out_detail_btn_readcard.setOnClickListener(this);
        vault_out_detail_btn_scan1d.setOnClickListener(this);
        vault_out_detail_btn_scanuhf.setOnClickListener(this);
        vault_out_detail_btn_enter.setOnClickListener(this);

        vault_out_detail_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListView listView = (ListView) parent;
                Box box = (Box) listView.getItemAtPosition(position);
                YLBoxchangeType("setbox", position);

            }
        });

        vault_out_detail.this.setTitle("出库明细--" + YLSystem.getUser().getName());
    }

    private void InitData()throws  Exception {
        vaulteroutboxlist = new ArrayList<>();
        AllboxList = new ArrayList<>();
        ylMediaPlayer = new YLMediaPlayer();
        ylTask = YLEditData.getYlTask();
        vault_out_detail_tv_taskname.setText(ylTask.getLine());
        analysisBoxList = new AnalysisBoxList();
        webServerValutInorOut = new WebServerValutInorOut();

        User user = YLSystem.getUser();
        user.setTaskDate(ylTask.getTaskID());

        AllboxList= webServerValutInorOut.StoreGetBoxByTaskIDOut(user,getApplicationContext());
        DisPlayBoxlistAdapter(AllboxList);
        Log.e(YLSystem.getKimTag(), AllboxList.size() + " 库内款箱数");
        ShowBoxList();
    }

    private void ShowBoxList() {
        if (AllboxList.size()==1)return;
        List<String> stringList = analysisBoxList.AnsysisBoxList(AllboxList);
        String boxtype = "款箱："+stringList.get(0)+"  卡箱:"+stringList.get(1)+"  凭证箱:"+stringList.get(2)+
                "  凭证袋:"+stringList.get(3);
        String boxstaut = "实箱："+stringList.get(6)+"  空箱："+stringList.get(7);

        String total = "实时扫描数： "+AllboxList.size()+"   ";

        vault_out_detail_tv_taskname.setText(total);
        vault_out_detail_tv_boxstaut.setText(boxstaut);
        vault_out_detail_tv_type.setText(boxtype);
    }

    private void InitReciveScan1D() {
        YLBoxscan1DRecive = new Scan1DRecive();
        IntentFilter filter = new IntentFilter();
        filter.addAction("ylescort.ylmobileandroid.vault_out_detail");
        registerReceiver(YLBoxscan1DRecive, filter);
        Intent start = new Intent(vault_out_detail.this,Scan1DService.class);
        vault_out_detail.this.startService(start);
    }

    private void YLBoxScan1D() {
        if (vault_out_detail_btn_scan1d.getText().equals("扫描/F1")){
            Scan1DCmd("toscan100ms");
            vault_out_detail_btn_scan1d.setText("停止/F1");
        }else {
            Scan1DCmd("stopscan");
            vault_out_detail_btn_scan1d.setText("扫描/F1");
        }
    }

    private void YLBoxchangeType(final String type, final int position ) {
        new AlertDialog.Builder(this).setTitle("请选择类型").setIcon(android.R.drawable.ic_dialog_info)
                .setSingleChoiceItems(R.array.ylboxfullorempty, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Box box = new Box();
                        box = AllboxList.get(position);
                        switch (which) {
                            case 0:
                                box.setBoxStatus("实");
                                box.setValutcheck("核");
                                AllboxList.set(position, box);
                                break;
                            case 1:
                                box.setBoxStatus("空");
                                box.setValutcheck("核");
                                AllboxList.set(position, box);
                                break;
                            case 2:
                                AllboxList.remove(position);
                                Log.e(YLSystem.getKimTag(),AllboxList.toString());
                                if (AllboxList.size()==0) {
                                    AllboxList = new ArrayList<Box>();
                                }
                                break;
                        }
//                        DisPlayBoxlistAdapter(AllboxList);
                        ylValutboxitemAdapter.notifyDataSetChanged();
                        ShowBoxList();
                        dialog.dismiss();
                    }
                }).show();



    }

    private void YLBoxEnter() throws Exception {

        AlertDialog.Builder builder = new AlertDialog.Builder(vault_out_detail.this);
        builder.setMessage("是否确认上传数据");
        builder.setTitle("提示");
        builder.setPositiveButton("上传", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    ylTask.setLstBox(AllboxList);
                    YLEditData.setYlTask(ylTask);
                    WebService webService = new WebService();
                    String serreturn =
                            webService.PostVaultInBoxList(YLSystem.getUser(), getApplicationContext());
                    if (serreturn.equals("1")){
                        ylTask.setTaskState("已上传");
                        dialog.dismiss();
//                        vault_out_detail.this.finish();
                        User user = YLSystem.getUser();
                        user.setTaskDate(ylTask.getTaskID());
                        WebServerValutInorOut webServerValutInorOut = new WebServerValutInorOut();
                        AllboxList= webServerValutInorOut.StoreGetBoxByTaskIDOut(user,getApplicationContext());
                        DisPlayBoxlistAdapter(AllboxList);
                        ShowBoxList();
                    }else{
                        Toast.makeText(getApplicationContext(),"未上传数据，请检查网络后再上传",Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
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
            case R.id.vault_out_detail_btn_scanuhf:
                ScanUHF("scan");
                break;
            case R.id.vault_out_detail_btn_enter:
                try {
                    YLBoxEnter();
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            case R.id.vault_out_detail_btn_readcard:
                ReadHFCard();
                break;
        }
    }

    private void ScanUHF(String action) {
        String activity = "ylescort.ylmobileandroid.vault_out_detail";
        Intent ac = new Intent();
        ac.setAction("ylescort.ylmobileandroid.ScanUHFService");
        ac.putExtra("activity", activity);
        sendBroadcast(ac);
        Intent sendToservice = new Intent(vault_out_detail.this, ScanUHFService.class); // 用于发送指令
        sendToservice.putExtra("cmd", action);
        this.startService(sendToservice); // 发送指令
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode){
            case 131:YLBoxScan1D();
                break;
            case 132:
                try {
                    YLBoxEnter();
                }catch (Exception e){
                    e.printStackTrace();
                }
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
//                recivedata = YLBoxScanCheck.replaceBlank(recivedata);
//                Box box =CheckBox(recivedata);
                AddYLBoxtoListView(box);
//                recivedata = YLBoxScanCheck.replaceBlank(recivedata);
//                ScanBoxInListView(recivedata);
            }
        }
    }

    private void ScanBoxInListView(String recivedata) {

    }

    private Box CheckBox(String recivedata ){
        boolean getboxtof = false;
        Box getbox = new Box();

        for (int i = 0; i < AllboxList.size();i++){
            Box box = AllboxList.get(i);
            if (box.getBoxID().equals(recivedata)){
                getbox = box;
                getbox.setValutcheck("对");
                getboxtof = true;
                break;
            }
        }
        if (!getboxtof){
            getbox= YLBoxScanCheck.CheckBox(recivedata, getApplicationContext());
            getbox.setValutcheck("多");
        }
        return getbox;
    }


    private void AddYLBoxtoListView(Box box) {
        try {
            if (box.getBoxName().equals("illegalbox") || box.getBoxName().equals("无数据")) {
                ylMediaPlayer.SuccessOrFailMidia("fail", getApplicationContext());
                return;
            }

            boolean boxcheck = true;

            Log.e(YLSystem.getKimTag(),AllboxList.toString());
            if (AllboxList.size() ==0){
                AllboxList.clear();
                boxcheck = true;
            }else if (AllboxList.size()==1&AllboxList.get(0).getServerReturn().contains("没有出库箱")){
                AllboxList.clear();
                boxcheck = true;
            }else {
                int listcount = AllboxList.size() - 1;
                for (int i = 0; i < AllboxList.size(); i++) {
                    Box scanbox = AllboxList.get(listcount - i);
                    if (scanbox.getBoxID().equals(box.getBoxID())) {
                        ylMediaPlayer.SuccessOrFailMidia("success ", getApplicationContext());
                        boxcheck = false;
                        break;
                    }
                }
            }

            if (boxcheck) {
                Log.e(YLSystem.getKimTag(), AllboxList.size() + "插入数据");
//                box.setBoxType(vault_out_detail_tv_type.getTag().toString());
//                box.setBoxStatus(vault_out_detail_tv_boxstaut.getTag().toString());
                box.setActionTime(YLSysTime.GetStrCurrentTime());
                box.setTradeAction("出");
                box.setBoxCount("1");
                box.setServerReturn("1");
                box.setTimeID("1");
                Log.e(YLSystem.getKimTag(), box.toString());
                AllboxList.add(box);
                DisPlayBoxlistAdapter(AllboxList);
                ylMediaPlayer.SuccessOrFailMidia("success", getApplicationContext());

            }
            ShowBoxList();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void DisPlayBoxlistAdapter(List<Box> boxList){
        if (boxList != null ){
            ylValutboxitemAdapter =
                    new YLValutboxitemAdapter(getApplicationContext(),boxList,R.layout.vault_in_detail_boxitem);
            vault_out_detail_lv.setAdapter(ylValutboxitemAdapter);
            scrollMyListViewToBottom();
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
//        if (vault_out_detail_tv_boxstaut.getText().equals("状态")& !cmd.equals("stopscan")){
//            Toast.makeText(getApplicationContext(),"未设置状态",Toast.LENGTH_SHORT).show();
//            return;}

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
        if (YLBoxscan1DRecive != null){
        unregisterReceiver(YLBoxscan1DRecive);}
        Scan1DCmd("stopscan");
        super.onDestroy();
    }
}
