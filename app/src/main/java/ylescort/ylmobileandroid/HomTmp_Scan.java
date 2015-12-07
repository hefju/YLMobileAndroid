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
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import TaskClass.Box;
import TaskClass.Site;
import TaskClass.TasksManager;
import TaskClass.User;
import TaskClass.YLATM;
import TaskClass.YLTask;
import YLAdapter.YLValutboxitemAdapter;
import YLDataService.WebServerTmpValutInorOut;
import YLSystemDate.YLEditData;
import YLSystemDate.YLMediaPlayer;
import YLSystemDate.YLSysTime;
import YLSystemDate.YLSystem;

public class HomTmp_Scan extends ActionBarActivity implements View.OnClickListener {

    private TextView HomTmp_Scan_tv_title;
    private TextView Homtmp_Scan_tv_basename;
    private ListView HomTmp_Scan_listview;
    private Button HomTmp_Scan_btn_scan;
    private Button HomTmp_Scan_btn_upload;
    private Button HomTmp_Scan_btn_refresh;
    private Button HomTmp_Scan_btn_apply;

    private TasksManager tasksManager = null;//任务管理类
    private YLTask ylTask;//当前选中的任务

    private Scan1DRecive Scan1DRecive;

    private List<Box> AllBoxList;
    private List<Box> CarBoxList;
    private String chioce;
    private int TimeID;//1是出库，2是入库。
    private YLValutboxitemAdapter ylValutboxitemAdapter;
    private WebServerTmpValutInorOut webServerTmpValutInorOut;
    private YLMediaPlayer ylMediaPlayer;


    private int colorbule,colorred,colorpurple,colordefaul;

    public String getChioce() {
        if (chioce == null){
            chioce = "南海基地";
        }
        return chioce;
    }

    public void setChioce(String chioce) {
        this.chioce = chioce;
    }

    public int getTimeID() {
        return TimeID;
    }

    public void setTimeID(int timeID) {
        TimeID = timeID;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hom_tmp__scan);
        InitView();
        InitData();
        InitScan();
    }

    private void InitScan() {
        Scan1DRecive = new Scan1DRecive();
        IntentFilter filter = new IntentFilter();
        filter.addAction("ylescort.ylmobileandroid.HomTmp_Scan");
        registerReceiver(Scan1DRecive, filter);
        Intent start = new Intent(HomTmp_Scan.this,Scan1DService.class);
        HomTmp_Scan.this.startService(start);
    }

    private void InitData() {
        colorbule = getResources().getColor(R.color.androidblued);
        colorred = getResources().getColor(R.color.androidredl);
        colorpurple = getResources().getColor(R.color.androidpurplel);
        colordefaul = getResources().getColor(R.color.grey);
        HomTmp_Scan_tv_title.setText("扫描数量：0");

        ylMediaPlayer = new YLMediaPlayer();

        webServerTmpValutInorOut = new WebServerTmpValutInorOut(getApplicationContext());

        tasksManager = YLSystem.getTasksManager();//获取任务管理类
        ylTask = tasksManager.CurrentTask;//当前选中的任务
        AllBoxList = new ArrayList<>();
        CarBoxList = new ArrayList<>();
        if (ylTask.getLstCarBox() != null){
            if (ylTask.getLstCarBox().size()>0){
                for (Box box : ylTask.getLstBox()) {
                    CarBoxList.add(box);
                }
            }
        }

        DisPlayBoxlistAdapter(AllBoxList);
    }

    private void DisPlayBoxlistAdapter(List<Box> boxList){
        if (boxList != null){
            ylValutboxitemAdapter = new YLValutboxitemAdapter(getApplicationContext()
                    ,boxList,R.layout.vault_in_detail_boxitem);
            HomTmp_Scan_listview.setAdapter(ylValutboxitemAdapter);
        }
    }

    private class Scan1DRecive extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String recivedata = intent.getStringExtra("result");
            if (recivedata != null) {
                if (getTimeID() ==1){
                    AddBoxToListfortimeid1(recivedata);
                }else{
                    AddBoxToListfortimeid2(recivedata);
                }

            }
        }
    }

    private void AddBoxToListfortimeid1(String recivedata) {
        if (recivedata.length() !=10)return;
        for (Box box : AllBoxList) {
            if (box.getBoxID().equals(recivedata))return;
        }

    }

    private void AddBoxToListfortimeid2(String recivedata) {
        if (recivedata.length() !=10)return;
        for (Box box : AllBoxList) {
            if (box.getBoxID().equals(recivedata)){
                Log.e(YLSystem.getKimTag(),recivedata+"接收数据1");
                ylMediaPlayer.SuccessOrFailMidia("fail", getApplicationContext());
                return;
            }
        }
        for (int i = 0; i < CarBoxList.size(); i++) {
            Box box = CarBoxList.get(i);
            if (box.getBoxID().equals(recivedata)){
                box.setActionTime(YLSysTime.GetStrCurrentTime());
                box.setTimeID(getTimeID() + "");
                box.setBoxCount("1");
                box.setTradeAction("送");
                box.setBaseValutIn(getChioce());
                Log.e(YLSystem.getKimTag(), recivedata + "接收数据2");
                AllBoxList.add(box);
                CarBoxList.remove(i);
                ylMediaPlayer.SuccessOrFailMidia("success", getApplicationContext());
                break;
            }
        }
        ylValutboxitemAdapter.notifyDataSetChanged();
    }

    private void Scan1DCmd(String cmd) {
        String activity = "ylescort.ylmobileandroid.HomTmp_Scan";
        Intent ac = new Intent();
        ac.setAction("ylescort.ylmobileandroid.Scan1DService");
        ac.putExtra("activity", activity);
        sendBroadcast(ac);
        Intent sendToservice = new Intent(HomTmp_Scan.this, Scan1DService.class); // 用于发送指令
        sendToservice.putExtra("cmd", cmd);
        this.startService(sendToservice); // 发送指令
    }


    private void InitView() {
        HomTmp_Scan_tv_title = (TextView) findViewById(R.id.HomTmp_Scan_tv_title);
        Homtmp_Scan_tv_basename = (TextView)findViewById(R.id.Homtmp_Scan_tv_basename);
        HomTmp_Scan_listview = (ListView) findViewById(R.id.HomTmp_Scan_listview);
        HomTmp_Scan_btn_scan = (Button) findViewById(R.id.HomTmp_Scan_btn_scan);
        HomTmp_Scan_btn_upload = (Button) findViewById(R.id.HomTmp_Scan_btn_upload);
        HomTmp_Scan_btn_refresh = (Button) findViewById(R.id.HomTmp_Scan_btn_refresh);
        HomTmp_Scan_btn_apply = (Button) findViewById(R.id.HomTmp_Scan_btn_apply);

        HomTmp_Scan_btn_scan.setOnClickListener(this);
        HomTmp_Scan_btn_upload.setOnClickListener(this);
        HomTmp_Scan_btn_refresh.setOnClickListener(this);
        HomTmp_Scan_btn_apply.setOnClickListener(this);

        HomTmp_Scan.this.setTitle("未确认申请操作");

    }

    @Override
    public void onClick(View view) {
        try {
            switch (view.getId()) {
                case R.id.HomTmp_Scan_btn_scan:
                    Scan1D();
                    break;
                case R.id.HomTmp_Scan_btn_upload:
                    UpData();
                    break;
                case R.id.HomTmp_Scan_btn_refresh:
                    Getvaulttmpoutbox();
                    break;
                case R.id.HomTmp_Scan_btn_apply:
                    ShowApplyDialog();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void UpData() throws Exception {
        AlertDialog.Builder builder = new AlertDialog.Builder(HomTmp_Scan.this);
        builder.setMessage("确认上传吗?");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try {
                    YLTask task = new YLTask();
                    task.setTaskDate(ylTask.getTaskDate());
                    task.setLine(ylTask.getLine());
                    task.setTaskID(ylTask.getTaskID());
                    task.setTaskType(ylTask.getTaskType());
                    task.setTaskManager(ylTask.getTaskManager());
                    task.setTaskManagerNo(ylTask.getTaskManagerNo());
                    task.setLstBox(AllBoxList);
                    YLEditData.setYlTask(task);
                    String serreturn = webServerTmpValutInorOut.UpLoadBoxTmp(YLSystem.getUser().getEmpID());
                    if (serreturn.equals("1")) {
                        HomTmp_Scan.this.setTitle("未确认申请操作");
                        HomTmp_Scan_btn_refresh.setEnabled(false);
                        HomTmp_Scan_btn_scan.setEnabled(false);
                        HomTmp_Scan_btn_upload.setEnabled(false);
                        HomTmp_Scan_btn_refresh.setBackgroundColor(colordefaul);
                        AllBoxList.clear();
                        ylValutboxitemAdapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }

    private void Getvaulttmpoutbox() {
        List<Box> boxList = new ArrayList<>();

    }

    private void ShowApplyDialog(){
        setChioce(null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示信息");
        builder.setSingleChoiceItems(R.array.basedepartment, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                setChioce(MatchBase(i));
            }
        });
        builder.setNegativeButton("入库", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try{
                    if (YLSystem.getBaseName().equals(getChioce())){
                        return;
                    }
                    User user = new User();
                    user.setTaskDate(ylTask.getTaskID());
                    user.setEmpID(YLSystem.getUser().getEmpID());
                    user.setDeviceID(YLSystem.getHandsetIMEI());
                    user.setTime("2");
                    setTimeID(2);
                    user.setName(getChioce());
                    String returnstring =
                            webServerTmpValutInorOut.ComfirmValuttmpinorout(user);
                    if (returnstring.equals("0")){
                        Toast.makeText(getApplicationContext(),"未申请成功",Toast.LENGTH_SHORT).show();
                    }else{
                        HomTmp_Scan_btn_scan.setEnabled(true);
                        HomTmp_Scan_btn_upload.setEnabled(true);
                        HomTmp_Scan_btn_refresh.setEnabled(false);
                        if (getChioce() == null) {
                            HomTmp_Scan.this.setTitle("申请入库：南海基地");
                        }else {
                            HomTmp_Scan.this.setTitle("申请入库：" + getChioce());
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        builder.setNeutralButton("出库", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try {
                    User user = new User();
                    user.setTaskDate(ylTask.getTaskID());
                    user.setEmpID(YLSystem.getUser().getEmpID());
                    user.setDeviceID(YLSystem.getHandsetIMEI());
                    user.setTime("1");
                    setTimeID(1);
                    user.setName(getChioce());
                    String returnstring =
                            webServerTmpValutInorOut.ComfirmValuttmpinorout(user);
                    if (returnstring.equals("0")){
                        Toast.makeText(getApplicationContext(),"未申请成功",Toast.LENGTH_SHORT).show();
                    }else{
                        if (getChioce() == null) {
                            HomTmp_Scan.this.setTitle("申请出库：南海基地");
                        } else {
                            HomTmp_Scan.this.setTitle("申请出库：" + getChioce());
                        }
                        HomTmp_Scan_btn_refresh.setEnabled(true);
                        HomTmp_Scan_btn_scan.setEnabled(true);
                        HomTmp_Scan_btn_upload.setEnabled(true);
                        HomTmp_Scan_btn_refresh.setBackgroundColor(colorpurple);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }

    private String MatchBase(int choice){
        String Choose = "未选择";
        switch (choice){
            case 0:Choose = "南海基地";
                break;
            case 1:Choose = "大良基地";
                break;
            case 2:Choose = "乐从基地";
                break;
            case 3:Choose = "三水基地";
                break;
        }
        if (Choose.equals(YLSystem.getBaseName())){
            Choose = "未选择";
            Toast.makeText(getApplicationContext(),"不能选择本基地",Toast.LENGTH_SHORT).show();
        }
        return Choose;
    }

    private void Scan1D() {
        if (!HomTmp_Scan_btn_scan.isEnabled())return;
        if (HomTmp_Scan_btn_scan.getText().equals("扫描"))
        {
            Scan1DCmd("toscan100ms");
            HomTmp_Scan_btn_scan.setBackgroundColor(colorred);
            HomTmp_Scan_btn_scan.setText("停止");
        }else{
            Scan1DCmd("stopscan");
            HomTmp_Scan_btn_scan.setBackgroundColor(colorbule);
            HomTmp_Scan_btn_scan.setText("扫描");
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode){
            case 131:Scan1D();
                break;
            case 4:LeaveActivity();
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void LeaveActivity() {
        AlertDialog.Builder builder = new AlertDialog.Builder(HomTmp_Scan.this);
        builder.setMessage("确认离开吗?");
        builder.setTitle("提示");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                dialog.dismiss();
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
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

    @Override
    protected void onDestroy() {
        if (Scan1DRecive != null){
            unregisterReceiver(Scan1DRecive);
        }
        Scan1DCmd("stopscan");
        super.onDestroy();
    }
}
