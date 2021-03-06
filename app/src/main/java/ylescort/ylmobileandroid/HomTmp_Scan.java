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
import TaskClass.GatherPrint;
import TaskClass.Site;
import TaskClass.TasksManager;
import TaskClass.User;
import TaskClass.YLATM;
import TaskClass.YLTask;
import YLAdapter.YLValutboxitemAdapter;
import YLDataService.AnalysisBoxList;
import YLDataService.WebServerTmpValutInorOut;
import YLPrinter.YLPrint;
import YLSystemDate.YLEditData;
import YLSystemDate.YLMediaPlayer;
import YLSystemDate.YLRecord;
import YLSystemDate.YLSysTime;
import YLSystemDate.YLSystem;

public class HomTmp_Scan extends YLBaseScanActivity implements View.OnClickListener {

    private TextView HomTmp_Scan_tv_title;
    private TextView Homtmp_Scan_tv_basename;
    private ListView HomTmp_Scan_listview;
    private Button HomTmp_Scan_btn_scan;
    private Button HomTmp_Scan_btn_upload;
    private Button HomTmp_Scan_btn_refresh;
    private Button HomTmp_Scan_btn_apply;

    private TasksManager tasksManager = null;//任务管理类
    private YLTask ylTask;//当前选中的任务
    private List<Box> AllBoxList;
    private List<Box> CarBoxList;
    private String chioce;
    private int TimeID;//1是出库，2是入库。
    private int TaskTimeID;
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
        try {
            InitLayout();
            InitData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void InitLayout() {
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
    protected void InitData() throws Exception {
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
                for (Box box : ylTask.getLstCarBox()) {
                    CarBoxList.add(box);
                }
            }
        }

        DisPlayBoxlistAdapter(AllBoxList);
    }

    @Override
    public void YLPutdatatoList(String recivedata) {
        if (getTimeID() ==1){
            AddBoxToListfortimeid1(recivedata);
        }else{
            AddBoxToListfortimeid2(recivedata);
        }
    }

    private void DisPlayBoxlistAdapter(List<Box> boxList){
//        if (boxList != null){
            ylValutboxitemAdapter = new YLValutboxitemAdapter(getApplicationContext()
                    ,boxList,R.layout.vault_in_detail_boxitem);
            HomTmp_Scan_listview.setAdapter(ylValutboxitemAdapter);
//        }
    }

    private void AddBoxToListfortimeid1(String recivedata) {
        if (recivedata.length() !=10)return;
        boolean boxstate = true;
        if (AllBoxList.size()==0) return;
        for (int i = 0; i < AllBoxList.size(); i++) {

            Box box = new Box();
            box = AllBoxList.get(i);

            if (box.getBoxID().equals(recivedata)){
                if (box.getValutcheck() != null){
                    if (box.getValutcheck().equals("对")){
                        break;
                    }else {
                        box.setValutcheck("对");
                        box.setBaseValutIn(getChioce());
                        box.setBoxCount("1");
                        box.setActionTime(YLSysTime.GetStrCurrentTime());
                        box.setTradeAction("收");
                        box.setTimeID(getTimeID()+"");
                        box.setTaskTimeID(TaskTimeID);
                        CarBoxList.add(box);
                        boxstate = false;
                        ylMediaPlayer.SuccessOrFailMidia("success",getApplicationContext());
                    }

                }
            }
//            if (AllBoxList.get(i).getBoxID().equals(recivedata) ){
//                AllBoxList.get(i).setValutcheck("对");
//                AllBoxList.get(i).setBaseValutIn(getChioce());
//                AllBoxList.get(i).setBoxCount("1");
//                AllBoxList.get(i).setOrderNumber(OrderNumber);
//                boxstate = false;
//                CarBoxList.add(AllBoxList.get(i));
//                ylMediaPlayer.SuccessOrFailMidia("success",getApplicationContext());
//            }
        }
        if (boxstate){
            ylMediaPlayer.SuccessOrFailMidia("fail",getApplicationContext());
        }
        GatherAllboxlist(AllBoxList,"out");
        ylValutboxitemAdapter.notifyDataSetChanged();
    }

    private void AddBoxToListfortimeid2(String recivedata) {
        if (recivedata.length() !=10)return;
        for (Box box : AllBoxList) {
            if (box.getBoxID().equals(recivedata)){
                ylMediaPlayer.SuccessOrFailMidia("fail", getApplicationContext());
                return;
            }
        }
        for (int i = 0; i < CarBoxList.size(); i++) {
//            Box box = new Box();
//            box = CarBoxList.get(i);
            String boxid = CarBoxList.get(i).getBoxID();
            if (boxid.equals(recivedata)){
                Box carbox = CarBoxList.get(i);
                Box box = new Box();
                box.setBoxID(recivedata);
                box.setBoxName(carbox.getBoxName());
                box.setBoxStatus(carbox.getBoxStatus());
                box.setBoxTaskType(carbox.getBoxTaskType());
                box.setBoxToT(carbox.getBoxToT());
                box.setBoxType(carbox.getBoxType());
                box.setRemark(carbox.getRemark());
                box.setNextOutTime(carbox.getNextOutTime());
                box.setSiteID(carbox.getSiteID());
                box.setActionTime(YLSysTime.GetStrCurrentTime());
                box.setTimeID(getTimeID() + "");
                box.setBoxCount("1");
                box.setTradeAction("送");
                box.setBaseValutIn(getChioce());
                box.setTaskTimeID(TaskTimeID);
                AllBoxList.add(box);
                CarBoxList.remove(i);
                ylMediaPlayer.SuccessOrFailMidia("success", getApplicationContext());
                Log.e(YLSystem.getKimTag(),box.toString()+"临时入库款箱");
                break;
            }
        }
        GatherAllboxlist(AllBoxList,"in");
        ylValutboxitemAdapter.notifyDataSetChanged();
    }

    private void GatherAllboxlist(List<Box> boxes,String inroout){
        if (inroout.equals("in")){
            String sql = "扫描数量: "+boxes.size();
            HomTmp_Scan_tv_title.setText(sql);
        }else if (inroout.equals("out")){
            int count = 0;
            for (Box box : boxes) {
                if (box.getValutcheck() != null){
                    if (box.getValutcheck().equals("对")){
                        count ++;
                    }
                }
            }
            String sql = "扫描数量:"+count;
            HomTmp_Scan_tv_title.setText(sql);
        }

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
                    String serreturn = webServerTmpValutInorOut.UpLoadBoxTmp();
                    if (serreturn.equals("1")) {
                        ShowPrintDailog();
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

    private void ShowPrintDailog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(HomTmp_Scan.this);
        builder.setTitle("是否打印临时出入库？");
        final String[] multiChoiceItems = {"入库汇总数","入库清单表"};
        final boolean[] defaultSelectedStatus = {true,false};
        builder.setMultiChoiceItems(multiChoiceItems, defaultSelectedStatus, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                defaultSelectedStatus[i] = b;
            }
        });
        builder.setPositiveButton("打印", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (defaultSelectedStatus[0]){
                    PrintGather();
                    YLRecord.WriteRecord("申请","打印入库汇总");
                }
                if (defaultSelectedStatus[1]){
                    PrintDetail();
                    YLRecord.WriteRecord("申请","打印入库明细");
                }
                reinitlayout();
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                reinitlayout();
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }

    private void reinitlayout(){
        ylTask.setLstCarBox(CarBoxList);
        tasksManager.SaveTask(getApplicationContext());
        HomTmp_Scan.this.setTitle("未确认申请操作");
//                        HomTmp_Scan_btn_refresh.setEnabled(false);
        HomTmp_Scan_btn_scan.setEnabled(false);
        HomTmp_Scan_btn_upload.setEnabled(false);
        HomTmp_Scan_btn_refresh.setBackgroundColor(colordefaul);
        AllBoxList.clear();
        ylValutboxitemAdapter.notifyDataSetChanged();
    }

    private void PrintDetail() {
        try {
            YLPrint ylPrint = new YLPrint();
            ylPrint.InitBluetooth();
            List<Box> boxes = AllBoxList;

            AnalysisBoxList analysisBoxList = new AnalysisBoxList();
            GatherPrint gatherPrint = analysisBoxList.AnsysisBoxListForPrint(boxes);
            gatherPrint.setClintName("所属基地:"+ YLSystem.getBaseName());
            gatherPrint.setTradeTime("任务日期:"+ylTask.getTaskDate());
            if (TimeID == 1){
                gatherPrint.setSiteName("临时出库："+getChioce());
            }else {
                gatherPrint.setSiteName("临时入库："+getChioce());
            }
            gatherPrint.setCarNumber("");
            gatherPrint.setTaskNumber("NO."+ylTask.getTaskID());
            gatherPrint.setHomName(YLSystem.getUser().getEmpNO()+"-"+YLSystem.getUser().getName());
            gatherPrint.setTaskLine("任务线路:"+ylTask.getLine());

            ylPrint.PrintDetail2(boxes,2,gatherPrint);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void PrintGather() {
        try {
            YLPrint ylPrint = new YLPrint();
            ylPrint.InitBluetooth();
            List<Box> boxes = AllBoxList;
            AnalysisBoxList analysisBoxList = new AnalysisBoxList();
            GatherPrint gatherPrint = analysisBoxList.AnsysisBoxListForPrint(boxes);
            if (TimeID == 1){
                gatherPrint.setSiteName("临时出库："+getChioce());
            }else {
                gatherPrint.setSiteName("临时入库："+getChioce());
            }
            gatherPrint.setClintName("所属基地:"+ YLSystem.getBaseName());
            gatherPrint.setTradeTime("任务日期:"+ylTask.getTaskDate());
            gatherPrint.setCarNumber("");
            gatherPrint.setTaskNumber("NO."+ylTask.getTaskID());
            gatherPrint.setHomName(YLSystem.getUser().getEmpNO()+"-"+YLSystem.getUser().getName());
            gatherPrint.setTaskLine(ylTask.getLine());
            ylPrint.PrintGather(gatherPrint,2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void Getvaulttmpoutbox() {
        try {
            setTimeID(1);
            TaskTimeID = 1;
            AllBoxList.clear();
            AllBoxList = webServerTmpValutInorOut.GetTmpBoxList(ylTask.getTaskID(), "1", "999", "1");
            if (AllBoxList.size()>0  & AllBoxList.get(0).getBoxID() != null){
                if (!AllBoxList.get(0).getServerReturn().equals("0")){
                    setChioce(AllBoxList.get(0).getBaseValutIn());
                }

                MyLog(AllBoxList.get(0).toString());
                Log.e(YLSystem.getKimTag(),"出库列表"+AllBoxList.size());
                DisPlayBoxlistAdapter(AllBoxList);
                HomTmp_Scan.this.setTitle("出库箱扫描");
                HomTmp_Scan_btn_scan.setEnabled(true);
                HomTmp_Scan_btn_upload.setEnabled(true);
            }else {
                YLMessagebox("未有出库箱");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
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
                try {
                    if (YLSystem.getBaseName().equals(getChioce())) {
                        return;
                    }
                    User user = new User();
                    user.setTaskDate(ylTask.getTaskID());
                    user.setEmpID(YLSystem.getUser().getEmpID());
                    user.setDeviceID(YLSystem.getHandsetIMEI());
                    user.setTime("2");
                    setTimeID(2);
                    TaskTimeID = 2;
                    user.setName(getChioce());
                    String returnstring =
                            webServerTmpValutInorOut.ComfirmValuttmpinorout(user);
                    Log.e(YLSystem.getKimTag(),"出入库返回TaskTimeID"+returnstring);
                    if (returnstring.equals("0")) {
                        Toast.makeText(getApplicationContext(), "未申请成功", Toast.LENGTH_SHORT).show();
                    } else {
                        HomTmp_Scan_btn_scan.setEnabled(true);
                        HomTmp_Scan_btn_upload.setEnabled(true);
//                        HomTmp_Scan_btn_refresh.setEnabled(false);
                        TaskTimeID = Integer.parseInt(returnstring) ;
                        if (getChioce() == null) {
                            HomTmp_Scan.this.setTitle("申请入库：南海基地");
                        } else {
                            HomTmp_Scan.this.setTitle("申请入库：" + getChioce());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
//        builder.setNeutralButton("出库", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                try {
//                    User user = new User();
//                    user.setTaskDate(ylTask.getTaskID());
//                    user.setEmpID(YLSystem.getUser().getEmpID());
//                    user.setDeviceID(YLSystem.getHandsetIMEI());
//                    user.setTime("1");
//                    setTimeID(1);
//                    user.setName(getChioce());
//                    String returnstring =
//                            webServerTmpValutInorOut.ComfirmValuttmpinorout(user);
//                    if (returnstring.equals("0")){
//                        Toast.makeText(getApplicationContext(),"未申请成功",Toast.LENGTH_SHORT).show();
//                    }else{
//                        if (getChioce() == null) {
//                            HomTmp_Scan.this.setTitle("申请出库：南海基地");
//                        } else {
//                            HomTmp_Scan.this.setTitle("申请出库：" + getChioce());
//                        }
//                        OrderNumber = Integer.parseInt(returnstring);
//                        HomTmp_Scan_btn_refresh.setEnabled(true);
//                        HomTmp_Scan_btn_scan.setEnabled(true);
//                        HomTmp_Scan_btn_upload.setEnabled(true);
//                        HomTmp_Scan_btn_refresh.setBackgroundColor(colorpurple);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
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
            Scan1DCmd(2);
            HomTmp_Scan_btn_scan.setBackgroundColor(colorred);
            HomTmp_Scan_btn_scan.setText("停止");
        }else{
            Scan1DCmd(0);
            HomTmp_Scan_btn_scan.setBackgroundColor(colorbule);
            HomTmp_Scan_btn_scan.setText("扫描");
        }
    }

    @Override
    public void HandSetHotKey(int keyCode) {
        switch (keyCode){
            case 131:Scan1D();
                break;
            case 4:LeaveActivity();
                break;
        }
        super.HandSetHotKey(keyCode);
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
}
