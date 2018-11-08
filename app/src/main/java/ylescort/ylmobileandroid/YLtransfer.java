package ylescort.ylmobileandroid;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.view.ActionMode;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

import TaskClass.ArriveTime;
import TaskClass.Box;
import TaskClass.Site;
import TaskClass.TasksManager;
import TaskClass.YLTask;
import YLDataOperate.YLCarBoxOperate;
import YLDataOperate.YLtransferDataOperate;
import YLDataService.YLBoxScanCheck;
import YLPrinter.YLPrint;
import YLSystemDate.YLEditData;
import YLSystemDate.YLMediaPlayer;
import YLSystemDate.YLRecord;
import YLSystemDate.YLSysTime;
import YLSystemDate.YLSystem;

public class YLtransfer extends YLBaseScanActivity implements View.OnClickListener {

    //界面控件
    private TextView yltransfer_tv_title;
    private TextView yltransfer_tv_boxname;
    private TextView yltransfer_tv_boxtot;
    private TextView yltransfer_tv_boxaction;
    private TextView yltransfer_tv_boxtype;
    private TextView yltransfer_tv_boxstaut;
    private TextView yltransfer_tv_tasktype;

    private TextView yltransfer_tv_moneybox;
    private TextView yltransfer_tv_cardbox;
    private TextView yltransfer_tv_Voucher;
    private TextView yltransfer_tv_Voucherbag;

    private Spinner yltransfer_sp_tasktype;

    private RadioButton yltransfer_rbtn_get;
    private RadioButton yltransfer_rbtn_give;
    private RadioButton yltransfer_rbtn_full;
    private RadioButton yltransfer_rbtn_empty;

    private Button yltransfer_btn_date;
    private Button yltransfer_btn_scan;
    private Button yltransfer_btn_nonelable;
    private Button yltransfer_btn_ent;
    private Button yltransfer_btn_boxtype;
    private Button yltransfer_btn_print;

    private CheckBox yltransfer_cb_scan;
    private CheckBox yltransfer_cb_complie;
    private CheckBox yltransfer_cb_date;
    private CheckBox yltransfer_cb_ToT;
    private CheckBox yltransfer_cb_ylclearing;

    private YLMediaPlayer ylMediaPlayer;
    private Box ChooseBox; //当前选择款箱状态，收箱用
    private String Transfertype; //交接类型
    private TasksManager tasksManager = null;//任务管理类
    private YLTask ylTask;//当前选中的任务
    private YLCarBoxOperate ycdo;//车内箱操作类
    private YLtransferDataOperate ytdo;//交接箱操作类
    private int TaskTimeID;//任务顺序ID
    private String PickDate;//寄库箱日期
    private String TodayStrDate;//今天日期
    private ArriveTime arriveTime;//网点交接时间
    private YLBoxScanCheck ylBoxScanCheck;//数据库查找款箱

    private List<Site> siteList;//网点列表
    private Calendar calendar;//日期控件

    private boolean GiveNetPointCheck;//送箱网点匹配标识
    private boolean GetNetPointCheck;//收箱匹配标识
    private boolean Scanflag;//扫描标识
    private YLPrint ylPrint;//打印类

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yltransfer2);
        InitLayout();
        InitData();
    }

    @Override
    protected void InitLayout() {

        yltransfer_tv_title = (TextView) findViewById(R.id.yltransfer_tv_title);
        yltransfer_tv_boxname = (TextView) findViewById(R.id.yltransfer_tv_boxname);
        yltransfer_tv_boxtot = (TextView) findViewById(R.id.yltransfer_tv_boxtot);
        yltransfer_tv_boxaction = (TextView) findViewById(R.id.yltransfer_tv_boxaction);
        yltransfer_tv_boxtype = (TextView) findViewById(R.id.yltransfer_tv_boxtype);
        yltransfer_tv_boxstaut = (TextView) findViewById(R.id.yltransfer_tv_boxstaut);
        yltransfer_tv_tasktype = (TextView) findViewById(R.id.yltransfer_tv_tasktype);

        yltransfer_tv_moneybox = (TextView) findViewById(R.id.yltransfer_tv_moneybox);
        yltransfer_tv_cardbox = (TextView) findViewById(R.id.yltransfer_tv_cardbox);
        yltransfer_tv_Voucher = (TextView) findViewById(R.id.yltransfer_tv_Voucher);
        yltransfer_tv_Voucherbag = (TextView) findViewById(R.id.yltransfer_tv_Voucherbag);

        yltransfer_sp_tasktype = (Spinner) findViewById(R.id.yltransfer_sp_tasktype);

        yltransfer_rbtn_get = (RadioButton) findViewById(R.id.yltransfer_rbtn_get);
        yltransfer_rbtn_give = (RadioButton) findViewById(R.id.yltransfer_rbtn_give);
        yltransfer_rbtn_full = (RadioButton) findViewById(R.id.yltransfer_rbtn_full);
        yltransfer_rbtn_empty = (RadioButton) findViewById(R.id.yltransfer_rbtn_empty);

        yltransfer_btn_date = (Button) findViewById(R.id.yltransfer_btn_date);
        yltransfer_btn_scan = (Button) findViewById(R.id.yltransfer_btn_scan);
        yltransfer_btn_nonelable = (Button) findViewById(R.id.yltransfer_btn_nonelable);
        yltransfer_btn_ent = (Button) findViewById(R.id.yltransfer_btn_ent);
        yltransfer_btn_boxtype = (Button) findViewById(R.id.yltransfer_btn_boxtype);
        yltransfer_btn_print = (Button) findViewById(R.id.yltransfer_btn_print);

        yltransfer_cb_scan = (CheckBox) findViewById(R.id.yltransfer_cb_scan);
        yltransfer_cb_complie = (CheckBox) findViewById(R.id.yltransfer_cb_complie);
        yltransfer_cb_date = (CheckBox) findViewById(R.id.yltransfer_cb_date);
        yltransfer_cb_ToT = (CheckBox) findViewById(R.id.yltransfer_cb_ToT);
        yltransfer_cb_ylclearing = (CheckBox) findViewById(R.id.yltransfer_cb_ylclearing);

        yltransfer_btn_date.setOnClickListener(this);
        yltransfer_btn_scan.setOnClickListener(this);
        yltransfer_btn_nonelable.setOnClickListener(this);
        yltransfer_btn_ent.setOnClickListener(this);
        yltransfer_btn_boxtype.setOnClickListener(this);
        yltransfer_btn_print.setOnClickListener(this);
        yltransfer_cb_scan.setOnClickListener(this);
        yltransfer_cb_complie.setOnClickListener(this);
        yltransfer_cb_date.setOnClickListener(this);
        yltransfer_cb_ylclearing.setOnClickListener(this);

        yltransfer_rbtn_get.setOnClickListener(this);
        yltransfer_rbtn_give.setOnClickListener(this);
        yltransfer_rbtn_full.setOnClickListener(this);
        yltransfer_rbtn_empty.setOnClickListener(this);
        yltransfer_cb_ToT.setOnClickListener(this);

        yltransfer_btn_scan.setEnabled(false);
        yltransfer_btn_nonelable.setEnabled(false);
        yltransfer_btn_print.setEnabled(false);

        TallyBox();
    }

    @Override
    protected void InitData() {

        //创建新类
        ylMediaPlayer = new YLMediaPlayer(getApplicationContext());
        ChooseBox = new Box();
        ylBoxScanCheck = new YLBoxScanCheck(getApplicationContext());

        ycdo = new YLCarBoxOperate();
        ytdo = new YLtransferDataOperate();
        ylPrint = new YLPrint();

        GiveNetPointCheck = false;
        GetNetPointCheck = false;
        Scanflag = false;

        tasksManager = YLSystem.getTasksManager();//获取任务管理类
        ylTask = tasksManager.CurrentTask;//当前选中的任务
        //加载车内款箱、交接款箱操作类
        YLCarBoxOperate.setYLCurrectCarBoxList(ylTask.getLstCarBox());

        YLtransferDataOperate.setTransferedboxes(ylTask.getLstBox());

        siteList = ylTask.getLstSite();

        ytdo.InitBoxes();


        //标题加载网点名称
        yltransfer_tv_title.setText(YLEditData.getCurrentYLSite().getSiteName());
        yltransfer_tv_title.setTag(YLEditData.getCurrentYLSite().getSiteID());
        ChooseBox.setSiteID(YLEditData.getCurrentYLSite().getSiteID());
        YLtransferDataOperate.setChooseSite(YLEditData.getCurrentYLSite());

        //根据任务匹配交接类型
        String tasktype = ylTask.getTaskType();
        if (tasktype.equals("早送") || tasktype.equals("晚收")) {
            yltransfer_sp_tasktype.setSelection(0);
            ChooseBox.setBoxTaskType("早送晚收");
        } else {
            yltransfer_sp_tasktype.setSelection(1);
            ChooseBox.setBoxTaskType("上下介");
        }

        //加载日期至日期控件
        calendar = YLSysTime.AddDateString(Calendar.getInstance(), 0);
        int year = calendar.get(Calendar.YEAR);
        int Month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);

        TodayStrDate = YLSysTime.IntToStrDate(year, Month, day);
        PickDate = TodayStrDate;
        yltransfer_btn_date.setText(TodayStrDate);

        //加载交接类型
        ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(this, R.array.TaskType,
                android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yltransfer_sp_tasktype.setAdapter(arrayAdapter);
        yltransfer_sp_tasktype.setPrompt("交接类型");
        yltransfer_sp_tasktype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Transfertype = adapterView.getItemAtPosition(i).toString();
                switch (Transfertype) {
                    case "早送晚收":
                        yltransfer_cb_ToT.setChecked(false);
                        yltransfer_cb_ToT.setEnabled(false);
                        yltransfer_btn_date.setEnabled(true);
                        yltransfer_cb_date.setEnabled(false);
                        yltransfer_cb_date.setChecked(false);
                        yltransfer_cb_ylclearing.setEnabled(false);
                        yltransfer_cb_ylclearing.setChecked(false);
                        yltransfer_btn_date.setText(TodayStrDate);
                        ChooseBox.setNextOutTime("");
                        ChooseBox.setBoxToT("0");
                        yltransfer_cb_ylclearing.setChecked(false);
                        ChooseBox.setYlclearing("0");

                        break;
                    case "寄库箱":
                        yltransfer_cb_ToT.setChecked(false);
                        yltransfer_cb_ToT.setEnabled(false);
                        yltransfer_btn_date.setEnabled(true);
                        yltransfer_cb_date.setEnabled(true);
                        yltransfer_cb_ylclearing.setEnabled(false);
                        yltransfer_cb_ylclearing.setChecked(false);
                        ChooseBox.setBoxToT("0");
                        ChooseBox.setYlclearing("0");
                        yltransfer_cb_ylclearing.setChecked(false);
                        ChooseBox.setYlclearing("0");
                        break;
                    case "上下介":
                        yltransfer_cb_ToT.setEnabled(true);
                        yltransfer_btn_date.setEnabled(false);
                        yltransfer_cb_date.setEnabled(false);
                        yltransfer_cb_date.setChecked(false);
                        yltransfer_cb_ylclearing.setEnabled(true);
                        yltransfer_cb_ylclearing.setChecked(false);
                        ChooseBox.setNextOutTime("");
                        ChooseBox.setYlclearing("0");
                        break;
                    case "跨行调拨":
                        yltransfer_cb_ToT.setEnabled(true);
                        yltransfer_btn_date.setEnabled(false);
                        yltransfer_cb_date.setEnabled(false);
                        yltransfer_cb_date.setChecked(false);
                        yltransfer_cb_ylclearing.setEnabled(true);
                        yltransfer_cb_ylclearing.setChecked(false);
                        ChooseBox.setNextOutTime("");
                        ChooseBox.setYlclearing("0");
                        break;
                    default:
                        yltransfer_cb_ToT.setEnabled(true);
                        yltransfer_btn_date.setEnabled(false);
                        yltransfer_cb_date.setEnabled(false);
                        yltransfer_cb_date.setChecked(false);
                        yltransfer_cb_ylclearing.setEnabled(false);
                        yltransfer_cb_ylclearing.setChecked(false);
                        ChooseBox.setNextOutTime("");
                        ChooseBox.setYlclearing("0");
                        yltransfer_cb_ylclearing.setChecked(false);
                        ChooseBox.setYlclearing("0");
                        break;
                }
                YLRecord.WriteRecord("网点交接", "选择交接类型：" + Transfertype);
                ChooseBox.setBoxTaskType(Transfertype);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ChooseBox.setYlclearing("0");
        ChooseBox.setBoxToT("0");
    }

    @Override
    public void YLPutdatatoList(String recivedata) {
        try {
            if (yltransfer_rbtn_get.isChecked()) {
                GetBox(recivedata);
            } else {
                GiveBox(recivedata);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //送箱
    private void GiveBox(String recivedata) {
        //当前扫描是否重复
        if (ytdo.Repeatbox(recivedata)) {
            ylMediaPlayer.SuccessOrFail(false);
            return;
        }
        //寻找车内箱
        if (ycdo.CheckCarboxbystr(recivedata)) {
            //是否提示网点匹配
            if (GiveNetPointCheck) {
                Box givebox = ylBoxScanCheck.GetBoxbyBCNO(recivedata);
                //匹配
                if (yltransfer_tv_title.getTag().toString().equals(givebox.getSiteID())) {
                    YLRecord.WriteRecord("网点交接", "匹配送箱");
                    RemoveandaddCarbox(recivedata);
                } else {
                    //不匹配
                    StopScan();
                    YLRecord.WriteRecord("网点交接", "不匹配送箱");
                    ShowNetPointmatch(recivedata);
                    Scanflag = true;
                }
            } else {
                //不提示网点匹配
                YLRecord.WriteRecord("网点交接", "不提示送箱");
                RemoveandaddCarbox(recivedata);

            }
        } else {
            //无车内箱弹框选择
            Box givebox = ylBoxScanCheck.GetBoxbyBCNO(recivedata);
            if (givebox.getBoxID().equals("0")) {
                ylMediaPlayer.SuccessOrFail(false);
            } else {
                StopScan();
                YLRecord.WriteRecord("网点交接", "无车内箱");
                GivebyNoCarBoxDailog(recivedata);
                Scanflag = true;
            }
        }
    }

    //收箱
    private void GetBox(String recivedata) {
        //检查收箱是否可以交接、检查车内箱是否可以交接
        if (ytdo.Repeatgetbox(recivedata) || ycdo.CheckCarboxbystr(recivedata)) {
            ylMediaPlayer.SuccessOrFail(false);
            return;
        }
        //检查是否为临时标签
        Box box = ylBoxScanCheck.GetBoxbyBCNO(recivedata);
        if (box.getBoxID().equals("0")) {
            ylMediaPlayer.SuccessOrFail(false);
            return;
        }
        if (ytdo.CheckBoxName(box, ChooseBox.getBoxType())) {
            StopScan();
            YLRecord.WriteRecord("网点交接", "临时标签未选类型");
            YLMessagebox("临时标签款箱类型未选");
        } else {
            if (box.getBoxName().startsWith("海盾临")) {
                YLRecord.WriteRecord("网点交接", "添加临时标签");
                GetBoxDetail(box, true);
            } else {
                if (GetNetPointCheck) {//如果选择收箱提示
                    boolean boxsitecheck = false;
                    for (Site site : siteList) {
                        if (box.getSiteID().equals(site.getSiteID())) {
                            boxsitecheck = true;
                        }
                    }
                    if (boxsitecheck) {
                        YLRecord.WriteRecord("网点交接", "符合收箱网点列表");
                        GetBoxDetail(box, false);
                    } else {
                        StopScan();
                        YLRecord.WriteRecord("网点交接", "不符合收箱网点列表");
                        ShowDifferentSite(box);
                        Scanflag = true;
                    }
                } else {
                    YLRecord.WriteRecord("网点交接", "网点非提示收箱");
                    GetBoxDetail(box, false);
                }
            }
        }
    }

    private void ShowDifferentSite(final Box box) {
        if (Scanflag) return;
        AlertDialog.Builder builder = new AlertDialog.Builder(YLtransfer.this);
        builder.setMessage("所收款箱未在该线路网点，是否确认收箱？");
        builder.setTitle("提示");
        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                YLRecord.WriteRecord("网点交接", "收箱提示确认");
                GetBoxDetail(box, false);
//                GetNetPointCheck = false;
                Scanflag = false;
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                YLRecord.WriteRecord("网点交接", "收箱提示取消");
//                GetNetPointCheck = false;
                Scanflag = false;
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }

    private void GetBoxDetail(Box box, boolean tempbox) {
        if (ChooseBox.getTradeAction() == null || ChooseBox.getBoxStatus() == null) {
            YLMessagebox("未设置收送空实，请重新点击");
            return;
        }

        box.setTradeAction(ChooseBox.getTradeAction());
        box.setBoxStatus(ChooseBox.getBoxStatus());
        box.setBoxToT(ChooseBox.getBoxToT());
        if (tempbox) {
            box.setBoxType(ChooseBox.getBoxType());
        }
        box.setBoxCount("1");
        box.setSiteID(ChooseBox.getSiteID());
        box.setBoxTaskType(ChooseBox.getBoxTaskType());
        box.setRemark(ChooseBox.getRemark());
        box.setBoxOrder(ChooseBox.getBoxOrder());
        box.setTimeID(ChooseBox.getTimeID());

        String nexttime = "";
        switch (ChooseBox.getBoxTaskType()) {
            case "早送晚收":
                if (PickDate.equals(TodayStrDate)) {
                    nexttime = "";
                } else {
                    nexttime = PickDate;
                }
                break;
            case "寄库箱":
                if (yltransfer_cb_date.isChecked() ||
                        PickDate.equals(TodayStrDate)) {
                    nexttime = "2099-12-31";
                } else {
                    nexttime = PickDate;
                }
                break;
            default:
                nexttime = "";
                break;
        }
        box.setNextOutTime(nexttime);
        box.setTaskTimeID(ChooseBox.getTaskTimeID());
        box.setActionTime(YLSysTime.GetStrCurrentTime());
        box.setYlclearing(ChooseBox.getYlclearing());

//        YLtransferDataOperate.Transferingboxes.add(box);
        YLtransferDataOperate.AddboxtoList(box);
        YLCarBoxOperate.YLEditeCarBoxList.add(box);
        YLRecord.WriteRecord("网点交接", "收箱:" + box.getBoxName() + box.getBoxStatus() + box.getBoxTaskType());
        ylMediaPlayer.SuccessOrFail(true);
        yltransfer_cb_complie.setChecked(false);

        ShowBoxDaitel(box);
    }

    private void ShowNetPointmatch(final String recivedata) {
        if (Scanflag) return;
        AlertDialog.Builder builder = new AlertDialog.Builder(YLtransfer.this);
        builder.setMessage("所送款箱归属网点与该网点名称不同，是否确认送箱？");
        builder.setTitle("提示");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                YLRecord.WriteRecord("网点交接", "送箱提示确认");
                RemoveandaddCarbox(recivedata);
                Scanflag = false;
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                YLRecord.WriteRecord("网点交接", "送箱提示取消");
                Scanflag = false;
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }

    private void RemoveandaddCarbox(String recivedata) {
        Box removecarbox = ycdo.RemoveCarBox(recivedata);
        if (removecarbox.getBoxID().equals("0")) {
            ylMediaPlayer.SuccessOrFail(false);
        } else {
            removecarbox.setTaskTimeID(ChooseBox.getTaskTimeID());
            removecarbox.setTimeID(ChooseBox.getTimeID());
            removecarbox.setTradeAction("送");
            removecarbox.setSiteID(ChooseBox.getSiteID());
            removecarbox.setActionTime(YLSysTime.GetStrCurrentTime());
            removecarbox.setId(0);
//            YLtransferDataOperate.Transferingboxes.add(removecarbox);
            YLtransferDataOperate.AddboxtoList(removecarbox);
            ShowBoxDaitel(removecarbox);
            YLRecord.WriteRecord("网点交接", "送箱：" + removecarbox.getBoxName() +
                    removecarbox.getBoxStatus() + removecarbox.getBoxTaskType());
            ylMediaPlayer.SuccessOrFail(true);
        }
        ShowBoxDaitel(removecarbox);
    }

    //无车内款箱送箱弹框
    private void GivebyNoCarBoxDailog(final String boxid) {
        if (Scanflag) return;
        AlertDialog.Builder builder = new AlertDialog.Builder(YLtransfer.this);
        builder.setIcon(android.R.drawable.ic_input_get);
        builder.setTitle("请选择送箱状态");
        builder.setCancelable(false);
        View view = LayoutInflater.from(YLtransfer.this).inflate(R.layout.ylboxstautandtasktype, null);
        builder.setView(view);
        final RadioButton boxstaut_full = (RadioButton) view.findViewById(R.id.boxstaut_full);
        final CheckBox boxstaut_ToT = (CheckBox) view.findViewById(R.id.boxstaut_ToT);
        final RadioButton boxstaut_upordown = (RadioButton) view.findViewById(R.id.boxstaut_upordown);
        final RadioButton boxstaut_samebankdispatch = (RadioButton) view.findViewById(R.id.boxstaut_samebankdispatch);
        final RadioButton boxstaut_differentbankdispatch = (RadioButton) view.findViewById(R.id.boxstaut_differentbankdispatch);
        final RadioButton boxstaut_compelygog = (RadioButton) view.findViewById(R.id.boxstaut_compelygog);
        final RadioButton boxstaut_gog = (RadioButton) view.findViewById(R.id.boxstaut_gog);
        final RadioButton boxstaut_invaultbox = (RadioButton) view.findViewById(R.id.boxstaut_invaultbox);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try {
                    String boxstatus = "";
                    if (boxstaut_full.isChecked()) {
                        boxstatus = "实";
                    } else {
                        boxstatus = "空";
                    }
                    String boxtot = "";
                    if (boxstaut_ToT.isChecked()) {
                        boxtot = "1";
                    } else {
                        boxtot = "0";
                    }
                    String tasktype = "";
                    if (boxstaut_upordown.isChecked()) {
                        tasktype = "上下介";
                    }
                    if (boxstaut_samebankdispatch.isChecked()) {
                        tasktype = "同行调拨";
                    }
                    if (boxstaut_differentbankdispatch.isChecked()) {
                        tasktype = "跨行调拨";
                    }
                    if (boxstaut_compelygog.isChecked()) {
                        tasktype = "企业收送款";
                    }
                    if (boxstaut_gog.isChecked()) {
                        tasktype = "早送晚收";
                    }
                    if (boxstaut_invaultbox.isChecked()) {
                        tasktype = "寄库箱";
                    }
                    Box box = ytdo.BoxofNoCarbox(getApplicationContext(), boxid, ChooseBox.getSiteID(),
                            ChooseBox.getTimeID(), TaskTimeID, tasktype, boxtot, boxstatus);
                    MyLog(box.toString());
//                    YLtransferDataOperate.Transferingboxes.add(box);
                    YLtransferDataOperate.AddboxtoList(box);
                    ShowBoxDaitel(box);
                    TallyBox();
                    ylMediaPlayer.SuccessOrFail(true);
                    Scanflag = false;
                    YLRecord.WriteRecord("扫描", "送箱非车内箱：" + box.getBoxName() + box.getBoxStatus()
                            + box.getBoxTaskType());
                    dialogInterface.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Scanflag = false;
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }

    private void ShowBoxDaitel(Box box) {
        yltransfer_tv_boxname.setText(box.getBoxName());
        yltransfer_tv_boxaction.setText(box.getTradeAction());
        yltransfer_tv_boxtype.setText(box.getBoxType());

        if (box.getBoxToT() == null || box.getBoxToT().equals("0")) {
            yltransfer_tv_boxtot.setText("否");
        } else {
            yltransfer_tv_boxtot.setText("是");
        }
        yltransfer_tv_boxstaut.setText(box.getBoxStatus());
        yltransfer_tv_tasktype.setText(box.getBoxTaskType());
        TallyBox();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.yltransfer_btn_date:
                GetDatePick();
                YLRecord.WriteRecord("网点交接", "获取寄库箱日期" + PickDate);
                if (yltransfer_cb_date.isChecked()) {
                    ChooseBox.setNextOutTime("2099-12-31");
                } else {
                    ChooseBox.setNextOutTime(PickDate);
                }
//                MyLog("出库日期"+ChooseBox.getNextOutTime());
                break;
            case R.id.yltransfer_btn_scan:
                YLRecord.WriteRecord("网点交接", "款箱扫描");
                ScanYLBox();
                break;
            case R.id.yltransfer_btn_nonelable:
                YLRecord.WriteRecord("网点交接", "手输单号");
                NoLableBox();
                break;
            case R.id.yltransfer_btn_ent:
                ArriveAndAchieve();
                break;
            case R.id.yltransfer_btn_boxtype:
                ShowBoxtype();
//                ChooseBox.setBoxType(yltransfer_btn_boxtype.getText().toString());
                YLRecord.WriteRecord("网点交接", "临时箱选择" + yltransfer_btn_boxtype.getText().toString());
                break;
            case R.id.yltransfer_rbtn_get:
                ChooseBox.setTradeAction("收");
                YLRecord.WriteRecord("网点交接", "设置收箱");
                break;
            case R.id.yltransfer_rbtn_give:
                ChooseBox.setTradeAction("送");
                YLRecord.WriteRecord("网点交接", "设置送箱");
                break;
            case R.id.yltransfer_rbtn_full:
                ChooseBox.setBoxStatus("实");
                YLRecord.WriteRecord("网点交接", "设置实箱");
                break;
            case R.id.yltransfer_rbtn_empty:
                ChooseBox.setBoxStatus("空");
                YLRecord.WriteRecord("网点交接", "设置空箱");
                break;
            case R.id.yltransfer_cb_complie:
                if (yltransfer_cb_complie.isChecked()) {
                    ChooseBox.setRemark("1");
                    YLRecord.WriteRecord("网点交接", "设置补打");
                } else {
                    ChooseBox.setRemark("0");
                    YLRecord.WriteRecord("网点交接", "设置不补打");
                }
                break;
            case R.id.yltransfer_cb_date:
                if (yltransfer_cb_date.isChecked()) {
                    ChooseBox.setNextOutTime("2099-12-31");
                    YLRecord.WriteRecord("网点交接", "设置长期寄库");
                } else {
                    ChooseBox.setNextOutTime(PickDate);
                    YLRecord.WriteRecord("网点交接", "设置寄库日期：" + PickDate);
                }
                break;
            case R.id.yltransfer_cb_ToT:
                if (yltransfer_cb_ToT.isChecked()) {
                    ChooseBox.setBoxToT("1");
                    YLRecord.WriteRecord("网点交接", "设置即日收送");
                } else {
                    ChooseBox.setBoxToT("0");
                    YLRecord.WriteRecord("网点交接", "设置非即日收送");
                }
                break;
            case R.id.yltransfer_cb_scan://连续扫描
                if (!yltransfer_cb_scan.isChecked()) {
                    Scan1DCmd(0);
                    yltransfer_btn_scan.setText("扫描");
                }
                break;
            case R.id.yltransfer_btn_print:
                Print();
                break;
            case R.id.yltransfer_cb_ylclearing:

                if (yltransfer_cb_ylclearing.isChecked()) {
                    YLRecord.WriteRecord("网点交接", "设置海盾清分");
                    ChooseBox.setYlclearing("1");
                } else {
                    YLRecord.WriteRecord("网点交接", "设置非海盾清分");
                    ChooseBox.setYlclearing("0");
                }
                break;
        }
    }

    private void NoLableBox() {
        if (yltransfer_rbtn_get.isChecked()) {
            if (!yltransfer_rbtn_full.isChecked()
                    & !yltransfer_rbtn_empty.isChecked()) {
                YLMessagebox("收箱前请选择空实状态");
                return;
            }
        }

        yltransfer_cb_complie.setChecked(true);
        ChooseBox.setRemark("1");
        final EditText et = new EditText(this);
        et.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
        new AlertDialog.Builder(this).setTitle("条码:")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setView(et)
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String input = et.getText().toString();
                        if (input.length() != 10 || input.equals("")) {
                            Toast.makeText(getApplicationContext(), "输入标签有误！", Toast.LENGTH_SHORT).show();
                        } else {
                            if (yltransfer_rbtn_get.isChecked()) {
                                GetBox(input);
                            } else {
                                GiveBox(input);
                            }
                        }
                    }
                }).setNegativeButton("取消", null).show();

    }

    private void Print() {
        AlertDialog.Builder builder = new AlertDialog.Builder(YLtransfer.this);
        builder.setTitle("请选择打印类别？");
        final String[] multiChoiceItems = {"打印汇总", "打印明细"};
        final boolean[] defaultSelectedStatus = {false, false};
        builder.setMultiChoiceItems(multiChoiceItems, defaultSelectedStatus, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                defaultSelectedStatus[i] = b;
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (defaultSelectedStatus[0]) {
                    PrintGather();
                }
                if (defaultSelectedStatus[1]) {
                    PrintDetail();
                }
                dialogInterface.dismiss();
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

    private void PrintDetail() {

    }

    private void PrintGather() {

    }

    //到达完成交接
    private void ArriveAndAchieve() {
        switch (yltransfer_btn_ent.getText().toString()) {
            case "到达":
                ArriveNetPoint();
                break;
            case "完成":
                AchieveTask();
                break;
            case "返回":
                YLtransfer.this.finish();
                break;
        }
    }

    //任务完成汇总数据
    private void AchieveTask() {
        AlertDialog.Builder builder = new AlertDialog.Builder(YLtransfer.this);
        builder.setMessage("确认完成交接?");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //完成交接
                ylTask = ytdo.AcieveData(ChooseBox.getSiteID(), arriveTime, ylTask, TaskTimeID);
                tasksManager.SaveTask(getApplicationContext());
//                MyLog("车内款箱数量："+ylTask.getLstCarBox().size());
                yltransfer_btn_print.setEnabled(true);
                yltransfer_btn_ent.setText("返回");
                Scanflag = true;
                YLtransferDataOperate.setSiteTaskTimeID(0);
                YLRecord.WriteRecord("网点交接", "完成交接 TaskTimeID:" + TaskTimeID);
                YLtransfer.this.finish();
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //取消交接
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }

    //到达初始化数据
    private void ArriveNetPoint() {
        AlertDialog.Builder builder = new AlertDialog.Builder(YLtransfer.this);
        builder.setTitle("是否到达网点？");
        final String[] multiChoiceItems = {"送箱网点交接提示", "收箱线路网点提示"};
        final boolean[] defaultSelectedStatus = {true, true};
        builder.setMultiChoiceItems(multiChoiceItems, defaultSelectedStatus, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                defaultSelectedStatus[i] = b;
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //是否需要网点匹配提示
                GiveNetPointCheck = defaultSelectedStatus[0];
                GetNetPointCheck = defaultSelectedStatus[1];

                //载入任务顺序ID
                TaskTimeID = ytdo.GetTaskTimeIDbyyltask(ylTask);

                ChooseBox.setTaskTimeID(TaskTimeID);
                ChooseBox.setBoxType("款箱类");
                ChooseBox.setRemark("0");

                //判断是否需要加载车内款箱数
                boolean loadcarbox = ycdo.LoadCarboxlist(ylTask);
                if (loadcarbox) {
                    YLRecord.WriteRecord("网点交接", "加载车内款箱数");
                    LoadandupdateCarbox();
                }
                yltransfer_btn_ent.setText("完成");
                yltransfer_btn_scan.setEnabled(true);
                yltransfer_btn_nonelable.setEnabled(true);

                String siteid = yltransfer_tv_title.getTag().toString();
                arriveTime = ytdo.AddArriveTime(ylTask, siteid, TaskTimeID);
                ChooseBox.setSiteID(siteid);
                ChooseBox.setTimeID(arriveTime.getTimeID());
                YLtransferDataOperate.setSitetimeID(arriveTime.getTimeID());
                YLRecord.WriteRecord("网点交接", "到达网点:" + yltransfer_tv_title.getText() +
                        "TaskTimeID" + TaskTimeID + "送箱提示" + GiveNetPointCheck + "收箱提示:" + GetNetPointCheck);
                dialogInterface.dismiss();
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

    //获取车内款箱数据
    private void LoadandupdateCarbox() {
        //先下载出入ID，根据之前下载的车内款箱数对比
        //出入库款箱修改界面修改后有bug不能获取最新列表ID
        try {
            String yltaskid = ylTask.getTaskID();
            String boxoutid = ycdo.GetCarBoxOutID(getApplicationContext(), yltaskid);
            int count = YLCarBoxOperate.getYLCurrectCarBoxList().size();
            if (count > 0) {
                String localboxoutid = YLCarBoxOperate.getYLCurrectCarBoxList().get(0).getServerReturn();
                if (!localboxoutid.equals(boxoutid)) {
                    ycdo.UpdateCarbox(getApplicationContext(), yltaskid);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //获取寄库箱出库日期
    private void GetDatePick() {
        int year = calendar.get(Calendar.YEAR);
        int Month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);

//        DatePickerDialog datePickerDialog = new DatePickerDialog(YLtransfer.this,
//                new DatePickerDialog.OnDateSetListener() {
//                    @Override
//                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                        PickDate = YLSysTime.IntToStrDate(year, monthOfYear, dayOfMonth);
//                        yltransfer_btn_date.setText(PickDate);
//                        if (TodayStrDate.equals(PickDate)) {
//                            ChooseBox.setNextOutTime("");
//                        } else {
//                            ChooseBox.setNextOutTime(PickDate);
//                        }
//                    }
//                }, year, Month, day);

//        datePickerDialog.getDatePicker().setCalendarViewShown(false);

        DatePickerDialog dpd = new DatePickerDialog(this, AlertDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                PickDate = YLSysTime.IntToStrDate(year, monthOfYear, dayOfMonth);
                yltransfer_btn_date.setText(PickDate);
                if (TodayStrDate.equals(PickDate)) {
                    ChooseBox.setNextOutTime("");
                } else {
                    ChooseBox.setNextOutTime(PickDate);
                }
            }
        },year,Month,day);


        dpd.show();
    }

    //统计交接款箱数量
    private void TallyBox() {
        try {
            List<String> s = ytdo.ShowBoxListGather();
            yltransfer_rbtn_get.setText(s.get(0));
            yltransfer_rbtn_give.setText(s.get(1));
            yltransfer_rbtn_full.setText(s.get(2));
            yltransfer_rbtn_empty.setText(s.get(3));
            yltransfer_tv_moneybox.setText(s.get(4));
            yltransfer_tv_cardbox.setText(s.get(5));
            yltransfer_tv_Voucher.setText(s.get(6));
            yltransfer_tv_Voucherbag.setText(s.get(7));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //选定款箱类型
    private void ShowBoxtype() {
        new AlertDialog.Builder(this).setTitle("款箱类型").setIcon(android.R.drawable.ic_dialog_info)
                .setSingleChoiceItems(R.array.ylboxtype, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case 0:
                                yltransfer_btn_boxtype.setText("款箱");
                                ChooseBox.setBoxType("款箱");
                                break;
                            case 1:
                                yltransfer_btn_boxtype.setText("卡箱");
                                ChooseBox.setBoxType("卡箱");
                                break;
                            case 2:
                                yltransfer_btn_boxtype.setText("凭证箱");
                                ChooseBox.setBoxType("凭证箱");
                                break;
                            case 3:
                                yltransfer_btn_boxtype.setText("凭证袋");
                                ChooseBox.setBoxType("凭证袋");
                                break;
                        }
                        YLRecord.WriteRecord("网点交接", "选择款箱类型：" + ChooseBox.getBoxType());
                        dialogInterface.dismiss();
                    }

                }).show();
    }

    private void StopScan() {
        Scan1DCmd(0);
        yltransfer_btn_scan.setText("扫描");
        ylMediaPlayer.SuccessOrFail(false);
    }

    @Override
    public void HandSetHotKey(int keyCode) {
        switch (keyCode) {
            case 131:
                ScanYLBox();
                break;
            case 132:
                ScanYLBox();
                break;
            case 133:
                ScanYLBox();
                break;
            case 134:
                ScanYLBox();
                break;
            case 4:
                LeaveAction();
                break;
        }
        super.HandSetHotKey(keyCode);
    }

    //款箱扫描
    private void ScanYLBox() {
        if (Scanflag) return;
//        if (GiveNetPointCheck)return;
        if (CheckRadioButton().trim().length() > 0) {
            YLMessagebox(CheckRadioButton());
            return;
        }

        if (yltransfer_cb_scan.isChecked()) {
            if (yltransfer_btn_scan.getText().equals("扫描")) {
                yltransfer_btn_scan.setText("停止");
                YLRecord.WriteRecord("网点交接", "多次扫描");
                Scan1DCmd(2);
            } else {
                yltransfer_btn_scan.setText("扫描");
                YLRecord.WriteRecord("网点交接", "提示扫描");
                Scan1DCmd(0);
            }
        } else {
            YLRecord.WriteRecord("网点交接", "单次扫描");
            Scan1DCmd(1);
        }
    }

    private String CheckRadioButton() {
        String str = "";
        if (yltransfer_btn_ent.getText().equals("到达")) {
            str = "请先到达再扫描交接";
            YLRecord.WriteRecord("网点交接", "未到达扫描");
            return str;
        }

        if (!yltransfer_rbtn_get.isChecked() & !yltransfer_rbtn_give.isChecked()) {
            str = "收送类型未选择";
            YLRecord.WriteRecord("网点交接", "未选择收送扫描");
            return str;
        }
        if (yltransfer_rbtn_get.isChecked()) {
            if (!yltransfer_rbtn_full.isChecked() & !yltransfer_rbtn_empty.isChecked()) {
                str = "空实状态未选择";
                YLRecord.WriteRecord("网点交接", "未选择空实扫描");
            }
        }
        return str;
    }

    //返回按钮
    private void LeaveAction() {
        if (yltransfer_btn_ent.getText().equals("到达")) {
            YLRecord.WriteRecord("网点交接", "未按到达离开网点");
            finish();
            overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(YLtransfer.this);
        builder.setMessage("未完成确认离开吗?");
        builder.setTitle("提示");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                ylTask.setLstCarBox(CarBoxListnosave);
                YLRecord.WriteRecord("网点交接", "未完成离开");
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
    protected void onStop() {
        YLRecord.WriteRecord("网点交接", "离开网点交接");
        super.onStop();
    }

    @Override
    protected void onPostResume() {
        MyLog(YLtransferDataOperate.Transferingboxes.size() + "返回数量");
        TallyBox();
        CleanShow();
        super.onPostResume();
    }

    private void CleanShow() {
        yltransfer_tv_boxname.setText("");
        yltransfer_tv_boxtot.setText("");
        yltransfer_tv_boxaction.setText("");
        yltransfer_tv_boxtype.setText("");
        yltransfer_tv_boxstaut.setText("");
        yltransfer_tv_tasktype.setText("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_hom_ylbox_scan, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            if (yltransfer_btn_ent.getText().toString().equals("到达")) {
                YLRecord.WriteRecord("网点交接", "非交接状态款箱编辑");
                YLtransferDataOperate.setSiteTaskTimeID(0);
            } else {
                YLRecord.WriteRecord("网点交接", "交接状态款箱编辑");
                YLtransferDataOperate.setSiteTaskTimeID(TaskTimeID);
            }

            YLstartActivity(YLtransferedi.class);

        } else if (id == R.id.action_settings2) {
            ShowRefreshDialog();
        }

        return super.onOptionsItemSelected(item);
    }

    private void ShowRefreshDialog() {
        if (!yltransfer_btn_ent.getText().equals("完成")) return;
        AlertDialog.Builder builder = new AlertDialog.Builder(YLtransfer.this);
        builder.setMessage("是否刷新到达时间?");
        builder.setTitle("提示");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                YLRecord.WriteRecord("网点交接", "刷新到达时间");
                arriveTime.setATime(YLSysTime.GetStrCurrentTime());
                dialogInterface.dismiss();
            }
        });
        builder.create().show();

    }

}
