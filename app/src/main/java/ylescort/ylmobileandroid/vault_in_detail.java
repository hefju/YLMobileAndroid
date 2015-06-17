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
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import TaskClass.Box;
import TaskClass.YLTask;
import YLAdapter.YLValutboxitemAdapter;
import YLDataService.WebService;
import YLDataService.YLBoxScanCheck;
import YLSystemDate.YLEditData;
import YLSystemDate.YLMediaPlayer;
import YLSystemDate.YLSysTime;
import YLSystemDate.YLSystem;


public class vault_in_detail extends ActionBarActivity implements View.OnClickListener {

    private TextView vault_in_detail_tv_taskname;
    private ListView vault_in_detail_listview;
    private TextView vault_in_detail_tv_tolly;
    private TextView vault_in_detail_tv_check;
    private Button vault_in_detail_btn_scan1d;
    private Button vault_in_detail_btn_enter;

    private RadioButton vault_in_detail_rbtn_allbox;
    private RadioButton vault_in_detail_rbtn_lackbox;
    private RadioButton vault_in_detail_rbtn_morebox;

    private YLTask ylTask;
    private Scan1DRecive vaultindetailscan1DRecive;
    private YLMediaPlayer ylMediaPlayer;

    private List<Box> displayboxlist;
    private List<Box> Allboxlist;

    private YLValutboxitemAdapter ylValutboxitemAdapter;

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

        displayboxlist = new ArrayList<Box>();
        Allboxlist = new ArrayList<Box>();
        ylMediaPlayer = new YLMediaPlayer();

        ylTask = YLEditData.getYlTask();
        String title ="任务:"+ ylTask.getLine()+"\r\n执行人:"+ ylTask.getTaskManager();
        vault_in_detail_tv_taskname.setText(title);
        WebService webService = new WebService();
        displayboxlist = webService.GetVaultInBoxList(ylTask.getTaskID(),YLSystem.getHandsetIMEI(),
                YLSystem.getUser().getEmpID(),getApplicationContext());
        if (displayboxlist.get(0).getServerReturn().equals("1")){
            for (int i = 0;i<displayboxlist.size();i++){
                Box box = new Box();
                box = displayboxlist.get(i);
                Allboxlist.add(box);
            }
        }
        DisPlayBoxlistAdapter(displayboxlist);
        StatisticalBoxList(displayboxlist);
    }

    private void InitView(){
        vault_in_detail_tv_taskname = (TextView) findViewById(R.id.vault_in_detail_tv_taskname);
        vault_in_detail_listview = (ListView) findViewById(R.id.vault_in_detail_listview);
        vault_in_detail_tv_tolly = (TextView) findViewById(R.id.vault_in_detail_tv_tolly);
        vault_in_detail_tv_check = (TextView) findViewById(R.id.vault_in_detail_tv_check);
        vault_in_detail_btn_scan1d = (Button) findViewById(R.id.vault_in_detail_btn_scan1d);
        vault_in_detail_btn_enter = (Button) findViewById(R.id.vault_in_detail_btn_enter);

        vault_in_detail_rbtn_allbox = (RadioButton)findViewById(R.id.vault_in_detail_rbtn_allbox);
        vault_in_detail_rbtn_lackbox = (RadioButton)findViewById(R.id.vault_in_detail_rbtn_lackbox);
        vault_in_detail_rbtn_morebox = (RadioButton)findViewById(R.id.vault_in_detail_rbtn_morebox);

        vault_in_detail_btn_scan1d.setOnClickListener(this);
        vault_in_detail_btn_enter.setOnClickListener(this);
        vault_in_detail_rbtn_allbox.setOnClickListener(this);
        vault_in_detail_rbtn_lackbox.setOnClickListener(this);
        vault_in_detail_rbtn_morebox.setOnClickListener(this);

        vault_in_detail_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (displayboxlist.size() < 1) return;
                Box box = displayboxlist.get(position);
                if (box.getValutcheck() == null) return;
                if (box.getValutcheck().equals("多")) {
                    ShowMultChoice(position);
                }
            }
        });


    }

    private void ShowMultChoice(final int position) {
        new AlertDialog.Builder(this).setTitle("请选择类型").setIcon
                (android.R.drawable.ic_dialog_info).setSingleChoiceItems(R.array.ylboxvalut, 0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Box box = displayboxlist.get(position);
                        switch (which) {
                            case 0:
                                box.setBoxStatus("实");
                                box.setBoxType("款箱");
                                break;
                            case 1:
                                box.setBoxStatus("实");
                                box.setBoxType("卡箱");
                                break;
                            case 2:
                                box.setBoxStatus("实");
                                box.setBoxType("凭证箱");
                                break;
                            case 3:
                                box.setBoxStatus("实");
                                box.setBoxType("凭证袋");
                                break;
                            case 4:
                                box.setBoxStatus("空");
                                box.setBoxType("款箱");
                                break;
                            case 5:
                                box.setBoxStatus("空");
                                box.setBoxType("卡箱");
                                break;
                            case 6:
                                box.setBoxStatus("空");
                                box.setBoxType("凭证箱");
                                break;
                            case 7:
                                box.setBoxStatus("空");
                                box.setBoxType("凭证袋");
                                break;
                            case 8:
                                Box removebox = displayboxlist.get(position);
                                for (int i = 0; i < Allboxlist.size(); i++) {
                                    Box alllistboxbox = new Box();
                                    alllistboxbox = Allboxlist.get(i);
                                    if (removebox.getBoxID().equals(alllistboxbox.getBoxID())){
                                        Allboxlist.remove(i);
                                        break;
                                    }
                                }
                                displayboxlist.remove(position);
                                break;
                        }
                        if (displayboxlist.size() == position + 1) {
                            displayboxlist.set(position, box);
//                            DisPlayBoxlistAdapter(displayboxlist);
                            ylValutboxitemAdapter.notifyDataSetChanged();
                            vault_in_detail_listview.setSelection(position + 1);
                        } else {
//                            DisPlayBoxlistAdapter(displayboxlist);
                            ylValutboxitemAdapter.notifyDataSetChanged();
                            vault_in_detail_listview.setSelection(position);
                        }
                        dialog.dismiss();
                    }
                }).show();
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
            case R.id.vault_in_detail_btn_scan1d:
                String cmd = "";
                if (vault_in_detail_btn_scan1d.getText().equals("扫描/F1")){
                    cmd = "toscan100ms";
                    vault_in_detail_btn_scan1d.setText("停止/F1");
                }else {
                    cmd = "stopscan";
                    vault_in_detail_btn_scan1d.setText("扫描/F1");
                }
                Scan1DCmd(cmd);
                break;
            case R.id.vault_in_detail_btn_enter:ConfirmData();
                break;
            case R.id.vault_in_detail_rbtn_allbox:FilterBoxdisplay();
                break;
            case R.id.vault_in_detail_rbtn_lackbox:FilterBoxdisplay();
                break;
            case R.id.vault_in_detail_rbtn_morebox:FilterBoxdisplay();
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
        try {
            if (box.getBoxName().equals("illegalbox") || box.getBoxName().equals("无数据")) {
                ylMediaPlayer.SuccessOrFailMidia("fail", getApplicationContext());
                return;
            }
//            if (Allboxlist.size() < 1) return;
            boolean boxcheck = true;
            int position = 0;
            for (int i = 0; i < Allboxlist.size(); i++) {
                Box hombox = Allboxlist.get(i);
                if (hombox.getBoxID().equals(box.getBoxID())) {
                    if (hombox.getValutcheck() == null) {
                        hombox.setValutcheck("对");
                        hombox.setTradeAction("入");
                        hombox.setActionTime(YLSysTime.GetStrCurrentTime());
                        Allboxlist.set(i, hombox);
                        boxcheck = false;
                        position = i;
                        break;
                    } else if (hombox.getValutcheck().equals("多")
                            || hombox.getValutcheck().equals("对")) {
                        ylMediaPlayer.SuccessOrFailMidia("fail", getApplicationContext());
                        return;
                    }
                }
            }
            if (boxcheck) {
                box.setValutcheck("多");
                box.setBoxCount("1");
                box.setBoxStatus("无");
                box.setBoxType("无");
                box.setTradeAction("入");
                box.setActionTime(YLSysTime.GetStrCurrentTime());
                box.setBoxTaskType(ylTask.getTaskType());
                Allboxlist.add(box);
            }
            FilterBoxdisplay();
//            ylValutboxitemAdapter.notifyDataSetInvalidated();
//            DisPlayBoxlistAdapter(displayboxlist);
            if (position == 0) {
                position = displayboxlist.size();
            }
            vault_in_detail_listview.setSelection(position);
            ylMediaPlayer.SuccessOrFailMidia("success", getApplicationContext());
            StatisticalBoxList(Allboxlist);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void StatisticalBoxList(List<Box> boxList){
        int homstr = 0,vaulter= 0,correct = 0 ;
        if (boxList == null || boxList.size() < 1)return;
        for (Box box :boxList){
            if (box.getValutcheck() == null){
                homstr +=1;
            }else if (box.getValutcheck().equals("对")){
                vaulter +=1;
                correct +=1;
                homstr +=1;
            }else if (box.getValutcheck().equals("多")){
                vaulter +=1;
            }
        }

        int lackbox = homstr - correct;
        String hom = "业务员上传:"+homstr+"个";
        String vault = "库管员扫描:"+vaulter+"个, 符合:"+correct+"个 缺少:"+lackbox+"个";
        vault_in_detail_tv_tolly.setText(hom);
        vault_in_detail_tv_check.setText(vault);
    }

    private void DisPlayBoxlistAdapter(List<Box> boxList){
        if (boxList != null && boxList.size()>0){
//            FilterBoxdisplay();
//            boxList = Displayboxlist;
            ylValutboxitemAdapter = new YLValutboxitemAdapter(getApplicationContext()
                    ,boxList,R.layout.vault_in_detail_boxitem);
            vault_in_detail_listview.setAdapter(ylValutboxitemAdapter);
        }
    }

    private void FilterBoxdisplay(){
        displayboxlist.clear();
        //全部
        if (vault_in_detail_rbtn_allbox.isChecked()){
            for (int i = 0; i<Allboxlist.size();i++){
                Box box = new Box();
                box = Allboxlist.get(i);
                displayboxlist.add(box);
            }
        }

        //缺少
        if (vault_in_detail_rbtn_lackbox.isChecked()){
            for (int i = 0 ;i < Allboxlist.size();i++){
                Box box = new Box();
                box = Allboxlist.get(i);
                if (box.getValutcheck() == null){
                    displayboxlist.add(box);
                }
            }
        }

        //多箱
        if (vault_in_detail_rbtn_morebox.isChecked()){
            for (int i = 0; i<Allboxlist.size();i++){
                Box box = new Box();
                box = Allboxlist.get(i);
                if (box.getValutcheck()!=null && box.getValutcheck().equals("多")){
                    displayboxlist.add(box);
                }
            }
        }
        ylValutboxitemAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode){
            case 131:
                String cmd = "";
                if (vault_in_detail_btn_scan1d.getText().equals("扫描/F1")){
                    cmd = "toscan100ms";
                    vault_in_detail_btn_scan1d.setText("停止/F1");
                }else {
                    cmd = "stopscan";
                    vault_in_detail_btn_scan1d.setText("扫描/F1");
                }
                Scan1DCmd(cmd);
                break;
            case 132:ConfirmData();
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void ConfirmData() {
       AlertDialog.Builder builder = new AlertDialog.Builder(vault_in_detail.this);
        builder.setMessage("是否确认上传数据");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (Allboxlist.size() < 1) return;
                boolean boxcheck = false;
                for (Box box : Allboxlist) {
                    if (box.getValutcheck() != null) {
                        if (box.getBoxType().equals("无")) {
                            boxcheck = true;
                            break;
                        }
                    }
                }
                if (boxcheck) {
                    new AlertDialog.Builder(vault_in_detail.this).setTitle("提示")
                            .setMessage("有款箱未状态设置\r\n请完成设置")
                            .setPositiveButton("确定", null).show();
                } else {
                    try {
                        ylTask.setLstBox(Allboxlist);
                        YLEditData.setYlTask(ylTask);
                        Log.e(YLSystem.getKimTag(), Allboxlist.toString());
                        WebService webService = new WebService();
                        String returstr = webService.PostVaultInBoxList(YLSystem.getUser(), getApplicationContext());
                        if (returstr.contains("0")){
                            Log.e(YLSystem.getKimTag(),"0");
                        }else if (returstr.contains("1")){
                            Log.e(YLSystem.getKimTag(),"1");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    ylTask.setTaskState("已上传");
                    Scan1DCmd("stopscan");
                    vault_in_detail.this.finish();
                    dialog.dismiss();
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

    private void Scan1DCmd(String cmd) {
        String activity = "ylescort.ylmobileandroid.vault_in_detail";
        Intent ac = new Intent();
        ac.setAction("ylescort.ylmobileandroid.Scan1DService");
        ac.putExtra("activity", activity);
        sendBroadcast(ac);
        Intent sendToservice = new Intent(vault_in_detail.this, Scan1DService.class); // 用于发送指令
        sendToservice.putExtra("cmd", cmd);
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
        if (vaultindetailscan1DRecive != null){
            unregisterReceiver(vaultindetailscan1DRecive);
        }
        Scan1DCmd("stopscan");
        super.onDestroy();
    }
}
