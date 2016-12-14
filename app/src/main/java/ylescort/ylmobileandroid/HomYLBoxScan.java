package ylescort.ylmobileandroid;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import TaskClass.ArriveTime;
import TaskClass.Box;
import TaskClass.Site;
import TaskClass.TasksManager;
import TaskClass.YLTask;
import YLDataService.WebServerYLSite;
import YLDataService.YLBoxScanCheck;
import YLSystemDate.YLEditData;
import YLSystemDate.YLMediaPlayer;
import YLSystemDate.YLRecord;
import YLSystemDate.YLSysTime;
import YLSystemDate.YLSystem;


public class HomYLBoxScan extends ActionBarActivity implements View.OnClickListener {

    private TextView homylboxscan_tv_title;
    private TextView homylboxscan_tv_boxname;
    private TextView homylboxscan_tv_boxcount;
    private TextView homylboxscan_tv_boxaction;
    private TextView homylboxscan_tv_boxtype;
    private TextView homylboxscan_tv_boxstaut;
    private TextView homylboxscan_tv_tasktype;

    private TextView homylboxscan_tv_moneybox;
    private TextView homylboxscan_tv_cardbox;
    private TextView homylboxscan_tv_Voucher;
    private TextView homylboxscan_tv_Voucherbag;

    private Spinner homylboxscan_sp_tasktype;

    private RadioButton homylboxscan_rbtn_get;
    private RadioButton homylboxscan_rbtn_give;
    private RadioButton homylboxscan_rbtn_full;
    private RadioButton homylboxscan_rbtn_empty;

    private Button homylboxscan_btn_date;
    private Button homylboxscan_btn_scan;
    private Button homylboxscan_btn_nonelable;
    private Button homylboxscan_btn_ent;
    private Button homylboxscan_btn_boxtype;

    private CheckBox homylboxscan_cb_scan;
    private CheckBox homylboxscan_cb_complie;
    private CheckBox homylboxscan_cb_date;
    private CheckBox homylboxscan_cb_ToT;

    private List<Box> AllBoxList;
    private List<Box> CarBoxList;
    private List<Box> CarBoxListnosave;

    private Scan1DRecive HomBoxscan1DRecive;

    private ArriveTime arriveTime;
    private TasksManager tasksManager = null;//任务管理类
    private YLTask ylTask;//当前选中的任务
    private String box_sp_text ;
    private String YLScanMessage;
    private String PickDate;
    private String TodayDate;

    private Calendar calendar;
    private int TaskTimeID;
    private YLMediaPlayer ylMediaPlayer;
    private Box CurrentBox;//当前选择状态下的box
    private boolean ShowDailog;
    private boolean ShowNotice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hom_ylbox_scan);
        try {

            tasksManager = YLSystem.getTasksManager();//获取任务管理类
            ylTask = tasksManager.CurrentTask;//当前选中的任务
            CarBoxList = new ArrayList<Box>();
            CarBoxListnosave = new ArrayList<Box>();
            if (ylTask.getLstCarBox() != null){
                if (ylTask.getLstCarBox().size() > 0){
                    for (Box box : ylTask.getLstCarBox()) {
                        CarBoxList.add(box);
                        CarBoxListnosave.add(box);
                    }
                }
            }

            HomYLBoxScan.this.setTitle("款箱操作: " + YLSystem.getUser().getName());
            InitView();
            InitData();
            InitReciveScan1D();
            GetScreen();          //屏幕关闭时事件
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void GetScreen() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
        registerReceiver(mBatlnfoReceiver, intentFilter);
    }

    private BroadcastReceiver mBatlnfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Intent.ACTION_SCREEN_OFF.equals(action)){
                homylboxscan_btn_scan.setText("扫描/F1");
                Scan1DCmd("stopscan");
                YLEditData.setYleditcarbox(CarBoxList);
            }
        }
    };

    private void InitData() {
        CurrentBox = new Box();
        CurrentBox.setBoxCount("1");
        ylMediaPlayer = new YLMediaPlayer();
        ShowDailog = true;
        ShowNotice = true;
        homylboxscan_tv_title.setText(YLEditData.getCurrentYLSite().getSiteName());
        homylboxscan_tv_title.setTag(YLEditData.getCurrentYLSite().getSiteID());

        ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(this,R.array.TaskType
                ,android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        homylboxscan_sp_tasktype.setAdapter(arrayAdapter);
        homylboxscan_sp_tasktype.setPrompt("交接类型");
        homylboxscan_sp_tasktype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                box_sp_text = parent.getItemAtPosition(position).toString();
                switch (box_sp_text) {
                    case "早送晚收":
                        homylboxscan_cb_ToT.setChecked(false);
                        homylboxscan_cb_ToT.setEnabled(false);
                        homylboxscan_btn_date.setEnabled(true);
                        homylboxscan_cb_date.setEnabled(false);
                        homylboxscan_cb_date.setChecked(false);
//                        CurrentBox.setNextOutTime("");
                        CurrentBox.setBoxToT("0");
                        break;
                    case "寄库箱":
                        homylboxscan_cb_ToT.setChecked(false);
                        homylboxscan_cb_ToT.setEnabled(false);
                        homylboxscan_btn_date.setEnabled(true);
                        homylboxscan_cb_date.setEnabled(true);
                        CurrentBox.setBoxToT("0");
                        break;
                    default:
                        homylboxscan_cb_ToT.setEnabled(true);
                        homylboxscan_btn_date.setEnabled(false);
                        homylboxscan_cb_date.setEnabled(false);
                        homylboxscan_cb_date.setChecked(false);
                        CurrentBox.setNextOutTime("");
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        if (ylTask.getId() == null|| ylTask.getId() ==0){
            TaskTimeID = 1;
        }else {
            TaskTimeID = ylTask.getId();
        }
        Log.e(YLSystem.getKimTag(),TaskTimeID+"OrderNumber");

        /**
         * 根据任务匹配多选项
         */
        String tasktype = ylTask.getTaskType();
        if (tasktype.equals("早送") ||tasktype.equals("晚收")){
            homylboxscan_sp_tasktype.setSelection(0);
        }else {
            homylboxscan_sp_tasktype.setSelection(1);
        }

        homylboxscan_rbtn_get.setText("收箱:" + 0);
        homylboxscan_rbtn_give.setText("送箱:" + 0);
        homylboxscan_rbtn_full.setText("实箱:" + 0);
        homylboxscan_rbtn_empty.setText("空箱:" + 0);
        homylboxscan_tv_moneybox.setText("款箱:"+0);
        homylboxscan_tv_cardbox.setText("卡箱:"+0);
        homylboxscan_tv_Voucher.setText("凭证箱:\r\n     "+0);
        homylboxscan_tv_Voucherbag.setText("凭证袋:\r\n     "+0);

        calendar =YLSysTime.AddDateString(Calendar.getInstance(),1) ;

        int year = calendar.get(Calendar.YEAR);
        int Month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);

        String Date = year+"-"+String.format("%02d",(Month + 1))+"-"
                +String.format("%02d",(day));
        TodayDate = Date;
        PickDate = Date;
        homylboxscan_btn_date.setText(Date);

    }//初始化数据

    private void InitReciveScan1D() {
        HomBoxscan1DRecive = new Scan1DRecive();
        IntentFilter filter = new IntentFilter();
        filter.addAction("ylescort.ylmobileandroid.HomYLBoxScan");
        registerReceiver(HomBoxscan1DRecive, filter);
        Intent start = new Intent(HomYLBoxScan.this,Scan1DService.class);
        HomYLBoxScan.this.startService(start);
    }//初始化广播

    private void InitView() {

        homylboxscan_tv_title = (TextView) findViewById(R.id.homylboxscan_tv_title);
        homylboxscan_tv_boxname = (TextView) findViewById(R.id.homylboxscan_tv_boxname);
        homylboxscan_tv_boxcount = (TextView) findViewById(R.id.homylboxscan_tv_boxcount);
        homylboxscan_tv_boxaction = (TextView) findViewById(R.id.homylboxscan_tv_boxaction);
        homylboxscan_tv_boxtype = (TextView) findViewById(R.id.homylboxscan_tv_boxtype);
        homylboxscan_tv_boxstaut = (TextView) findViewById(R.id.homylboxscan_tv_boxstaut);
        homylboxscan_tv_tasktype = (TextView) findViewById(R.id.homylboxscan_tv_tasktype);

        homylboxscan_tv_moneybox = (TextView) findViewById(R.id.homylboxscan_tv_moneybox);
        homylboxscan_tv_cardbox = (TextView) findViewById(R.id.homylboxscan_tv_cardbox);
        homylboxscan_tv_Voucher = (TextView) findViewById(R.id.homylboxscan_tv_Voucher);
        homylboxscan_tv_Voucherbag = (TextView) findViewById(R.id.homylboxscan_tv_Voucherbag);

        homylboxscan_sp_tasktype = (Spinner) findViewById(R.id.homylboxscan_sp_tasktype);

        homylboxscan_rbtn_get = (RadioButton) findViewById(R.id.homylboxscan_rbtn_get);
        homylboxscan_rbtn_give = (RadioButton) findViewById(R.id.homylboxscan_rbtn_give);
        homylboxscan_rbtn_full = (RadioButton) findViewById(R.id.homylboxscan_rbtn_full);
        homylboxscan_rbtn_empty = (RadioButton) findViewById(R.id.homylboxscan_rbtn_empty);

        homylboxscan_btn_date = (Button) findViewById(R.id.homylboxscan_btn_date);
        homylboxscan_btn_scan = (Button) findViewById(R.id.homylboxscan_btn_scan);
        homylboxscan_btn_nonelable = (Button) findViewById(R.id.homylboxscan_btn_nonelable);
        homylboxscan_btn_ent = (Button) findViewById(R.id.homylboxscan_btn_ent);
        homylboxscan_btn_boxtype = (Button) findViewById(R.id.homylboxscan_btn_boxtype);

        homylboxscan_cb_scan = (CheckBox)findViewById(R.id.homylboxscan_cb_scan);
        homylboxscan_cb_complie = (CheckBox)findViewById(R.id.homylboxscan_cb_complie);
        homylboxscan_cb_date  = (CheckBox)findViewById(R.id.homylboxscan_cb_date);
        homylboxscan_cb_ToT = (CheckBox)findViewById(R.id.homylboxscan_cb_ToT);

        homylboxscan_btn_date.setOnClickListener(this);
        homylboxscan_btn_scan.setOnClickListener(this);
        homylboxscan_btn_nonelable.setOnClickListener(this);
        homylboxscan_btn_ent.setOnClickListener(this);
        homylboxscan_btn_boxtype.setOnClickListener(this);
        homylboxscan_cb_scan.setOnClickListener(this);
        homylboxscan_cb_complie.setOnClickListener(this);
        homylboxscan_cb_date.setOnClickListener(this);

        homylboxscan_rbtn_get.setOnClickListener(this);
        homylboxscan_rbtn_give.setOnClickListener(this);
        homylboxscan_rbtn_full.setOnClickListener(this);
        homylboxscan_rbtn_empty.setOnClickListener(this);
        homylboxscan_cb_ToT.setOnClickListener(this);

        homylboxscan_btn_scan.setEnabled(false);
        homylboxscan_btn_nonelable.setEnabled(false);

    }//初始化界面


    private void Scan1DCmd(String cmd) {
        String activity = "ylescort.ylmobileandroid.HomYLBoxScan";
        Intent ac = new Intent();
        ac.setAction("ylescort.ylmobileandroid.Scan1DService");
        ac.putExtra("activity", activity);
        sendBroadcast(ac);
        Intent sendToservice = new Intent(HomYLBoxScan.this, Scan1DService.class);
        sendToservice.putExtra("cmd", cmd);
        this.startService(sendToservice);
    }//发送扫描指令


    private boolean CheckRadioButton(String type) {

        if (!homylboxscan_rbtn_get.isChecked() & !homylboxscan_rbtn_give.isChecked()) {
            YLScanMessage = "收送未选";
            return true;
        }
        if (homylboxscan_rbtn_get.isChecked()) {
            if (!homylboxscan_rbtn_empty.isChecked() & !homylboxscan_rbtn_full.isChecked()) {
                YLScanMessage = "空实未选";
                return true;
            }
        }
        if (type.equals("need")){
            if (homylboxscan_btn_boxtype.getText().equals("款箱类")){
                YLScanMessage = "箱类未选";
                return true;
            }
        }

        if (!homylboxscan_btn_scan.isEnabled()) {
            YLScanMessage = "未到达不能扫描";
            return true;
        }
        return false;
    }//检测扫箱类型


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_hom_ylbox_scan, menu);
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
            YLEditData.setYleditcarbox(CarBoxList);
            Intent intent = new Intent();
            intent.setClass(this, YLBoxEdit.class);
            String box_btn_ent_text = homylboxscan_btn_ent.getText().toString();
            Bundle bundle = new Bundle();
            bundle.putString("siteid",homylboxscan_tv_title.getTag().toString());
            bundle.putString("box_btn_ent_text", box_btn_ent_text);
            intent.putExtras(bundle);
            startActivity(intent);
            Scan1DCmd("stopscan");
            YLRecord.WriteRecord("扫描","进入款箱编辑");
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            return true;
        }else if (id == R.id.action_settings2){
            ShowRefreshDialog();
        }

        return super.onOptionsItemSelected(item);
    }

    private void ShowRefreshDialog() {
        if (!homylboxscan_btn_ent.getText().equals("完成交接"))return;
        AlertDialog.Builder builder = new AlertDialog.Builder(HomYLBoxScan.this);
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
                arriveTime.setATime(YLSysTime.GetStrCurrentTime());
                dialogInterface.dismiss();
            }
        });
        builder.create().show();

    }

    private class Scan1DRecive extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String recivedata = intent.getStringExtra("result");
            if (recivedata != null) {
                if (recivedata.length() != 10)return;
                BoxIDtoBox(recivedata);
            }
        }//注册广播
    }

    private void BoxIDtoBox(final String recivedata) {

        for (int i = AllBoxList.size() - 1; i >= 0; i--) {
            if (AllBoxList.get(i).getBoxID().equals(recivedata)
                    & AllBoxList.get(i).getTradeAction().equals(getboxatcion())) {
                ylMediaPlayer.SuccessOrFailMidia("fail", getApplicationContext());
                return;
            }
        }
        final Box box = YLBoxScanCheck.CheckBoxbyUHF(recivedata, getApplicationContext());
        YLRecord.WriteRecord("扫描","录入箱ID:"+recivedata);
        if (box.getBoxName().equals("无数据")) {
            ylMediaPlayer.SuccessOrFailMidia("fail", getApplicationContext());
            return;
        }
        if (homylboxscan_rbtn_get.isChecked()) {
            try {
                if (box.getBoxName().contains("粤龙临") || box.getBoxType().equals("无")) {
                    if (!checkboxsype().equals("款箱类")) {
                        YLRecord.WriteRecord("扫描","收箱录入箱状态:"+box.getBoxName()+box.getBoxType());
                        box.setBoxType(checkboxsype());
                    } else {
                        Toast.makeText(getApplicationContext(), "标签箱类型未选", Toast.LENGTH_SHORT).show();
                        ylMediaPlayer.SuccessOrFailMidia("fail", getApplicationContext());
                        return;
                    }
                }
                PutBoxToList(box, "1", "ordin");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            boolean checkcarbox = true;
            if (CarBoxList.size() ==0){
                ylMediaPlayer.SuccessOrFailMidia("fail", getApplicationContext());
                homylboxscan_btn_scan.setText("扫描/F1");
                Scan1DCmd("stopscan");
                checkcarbox = false;
                if (ShowDailog) {
                    ShowBoxStautdailog(box);
                }
            }

            try {
                for (int i = 0; i < CarBoxList.size(); i++) {
                    final Box givebox = CarBoxList.get(i);

                    if (givebox.getBoxID().equals(recivedata)) {
                        Log.e(YLSystem.getKimTag(),"车内款箱："+ givebox.toString());
//                       if (!box.getSiteID().equals(CurrentBox.getSiteID()) &givebox.getBoxTaskType().equals("早送晚收")
//                               &  !homylboxscan_tv_title.getTag().toString().equals(3099+"")
//                               &  !homylboxscan_tv_title.getTag().toString().equals(3925+"")
//                               &  !homylboxscan_tv_title.getTag().toString().equals(3926+"") )
                        if (ShowNotice) {
                           ShowDailog = false;
                           ylMediaPlayer.SuccessOrFail(false);
                           AlertDialog.Builder builder = new AlertDialog.Builder(HomYLBoxScan.this);
                           builder.setMessage("所送款箱归属与该网点名称不同，是否确认送箱？");
                           builder.setTitle("提示");
                           builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                       @Override
                                       public void onClick(DialogInterface dialogInterface, int i) {
                                           YLRecord.WriteRecord("扫描","送箱非相同网点:"
                                                   +box.getBoxName()+box.getBoxType()+box.getBoxTaskType());
                                           Box setbox = new Box();
                                           setbox.setBoxID(recivedata);
                                           setbox.setSiteID(CurrentBox.getSiteID());
                                           setbox.setBoxStatus(givebox.getBoxStatus());
                                           setbox.setBoxType(givebox.getBoxType());
                                           setbox.setBoxTaskType(givebox.getBoxTaskType());
                                           setbox.setBoxName(givebox.getBoxName());
                                           setbox.setTradeAction("送");
                                           setbox.setActionTime(YLSysTime.GetStrCurrentTime());
                                           setbox.setTimeID(CurrentBox.getTimeID());
                                           setbox.setBoxCount("1");
                                           setbox.setBoxOrder(AllBoxList.size() + 1 + "");
                                           setbox.setTaskTimeID(TaskTimeID);
                                           setbox.setBoxToT(givebox.getBoxToT());
                                           setbox.setNextOutTime(givebox.getNextOutTime());
                                           homylboxscan_tv_boxname.setText(givebox.getBoxName());
                                           homylboxscan_tv_boxaction.setText("送");
                                           homylboxscan_tv_boxtype.setText(givebox.getBoxType());
                                           homylboxscan_tv_boxcount.setText("1");
                                           homylboxscan_tv_boxstaut.setText(givebox.getBoxStatus());
                                           homylboxscan_tv_tasktype.setText(givebox.getBoxTaskType());
                                           AllBoxList.add(setbox);
                                           TallyBox(AllBoxList);
                                           ylMediaPlayer.SuccessOrFailMidia("success", getApplicationContext());
                                       }
                                   }

                           );

                           builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                               @Override
                               public void onClick(DialogInterface dialogInterface, int i) {
                                   CarBoxList.add(givebox);
                               }
                           });
                           builder.show();
                           checkcarbox = false;

                       }else {
                            Box setbox = new Box();
                            setbox.setBoxID(recivedata);
                            setbox.setSiteID(CurrentBox.getSiteID());
                            setbox.setBoxStatus(givebox.getBoxStatus());
                            setbox.setBoxType(givebox.getBoxType());
                            setbox.setBoxTaskType(givebox.getBoxTaskType());
                            setbox.setBoxName(givebox.getBoxName());
                            setbox.setTradeAction("送");
                            setbox.setActionTime(YLSysTime.GetStrCurrentTime());
                            setbox.setTimeID(CurrentBox.getTimeID());
                            setbox.setBoxCount("1");
                            setbox.setBoxOrder(AllBoxList.size() + 1 + "");
                            setbox.setTaskTimeID(TaskTimeID);
                            setbox.setBoxToT(givebox.getBoxToT());
                            setbox.setNextOutTime(givebox.getNextOutTime());
                            homylboxscan_tv_boxname.setText(givebox.getBoxName());
                            homylboxscan_tv_boxaction.setText("送");
                            homylboxscan_tv_boxtype.setText(givebox.getBoxType());
                            homylboxscan_tv_boxcount.setText("1");
                            homylboxscan_tv_boxstaut.setText(givebox.getBoxStatus());
                            homylboxscan_tv_tasktype.setText(givebox.getBoxTaskType());
                            AllBoxList.add(setbox);
                            TallyBox(AllBoxList);
                            CarBoxList.remove(i);
                            YLRecord.WriteRecord("扫描", "送箱相同网点:"
                                    + setbox.getBoxName() + setbox.getBoxType() + setbox.getBoxTaskType());
                            ylMediaPlayer.SuccessOrFailMidia("success", getApplicationContext());
                            checkcarbox = false;
                            ShowDailog = true;
                            break;
                       }
                        CarBoxList.remove(i);
                        Log.e(YLSystem.getKimTag(), CarBoxList.size() + "在车数量");
                        ShowDailog = true;
                        break;
                    }
                }
                GiveBoxforNoincarbox(box, checkcarbox);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void GiveBoxforNoincarbox(Box box, boolean checkcarbox) {
        if (checkcarbox) {
            ylMediaPlayer.SuccessOrFailMidia("fail", getApplicationContext());
            homylboxscan_btn_scan.setText("扫描/F1");
            Scan1DCmd("stopscan");
            if (ShowDailog) {
                ShowBoxStautdailog(box);
            }
        }
    }

    private String getboxatcion(){
        if (homylboxscan_rbtn_get.isChecked()){
            return "收";
        }else {
            return "送";
        }
    }

    public String checkboxsype(){
        return homylboxscan_btn_boxtype.getText().toString();
    }

    private void PutBoxToList(Box box,String count,String type) {

        String tradeaction ="";
        String tradestaut = "";
        String tradetype = "";
        String tradeToT = "";
        homylboxscan_tv_boxname.setText(box.getBoxName());
        homylboxscan_tv_boxcount.setText(count);
        box.setBoxCount(count);
        if (homylboxscan_rbtn_get.isChecked()){
            tradeaction= "收";
        }else {
            tradeaction = "送";
        }
        box.setTradeAction(tradeaction);
        homylboxscan_tv_boxaction.setText(tradeaction);
        if (homylboxscan_rbtn_empty.isChecked()){
            tradestaut = "空";
        }else {
            tradestaut = "实";
        }

        if (homylboxscan_cb_ToT.isChecked()){
            tradeToT = "1";
        }else {
            tradeToT = "0";
        }

        box.setBoxToT(tradeToT);

        homylboxscan_tv_boxstaut.setText(tradestaut);

        box.setBoxStatus(tradestaut);

        if (type.equals("tmp")){
            tradetype = homylboxscan_btn_boxtype.getText().toString();
            box.setBoxType(tradetype);
            homylboxscan_tv_boxtype.setText(tradetype);
        }
        else {
            homylboxscan_tv_boxtype.setText(box.getBoxType());
        }
        try {
            homylboxscan_tv_tasktype.setText(box_sp_text);
            box.setBoxTaskType(box_sp_text);
            box.setSiteID(homylboxscan_tv_title.getTag().toString());
            box.setBoxOrder(AllBoxList.size() + 1 + "");
            box.setTimeID(arriveTime.getTimeID());

            switch (box_sp_text){
                case "寄库箱":
                    if (homylboxscan_cb_date.isChecked()) {
                        box.setNextOutTime("2099-12-31");
                    } else {
                        box.setNextOutTime(PickDate);
                    }
                    break;
                case "早送晚收":
                    if (!PickDate.equals(TodayDate)) {
                        box.setNextOutTime(PickDate);
                    }else {
                        box.setNextOutTime("");
                    }
                    break;
                default:box.setNextOutTime("");
                    break;
            }

            if (homylboxscan_cb_complie.isChecked()){
                box.setRemark("1");
            }else {
                box.setRemark("0");
            }
            box.setTaskTimeID(TaskTimeID);
            box.setActionTime(YLSysTime.GetStrCurrentTime());
            YLRecord.WriteRecord("扫描","收箱:"+box.getBoxName()+box.getBoxType()+box.getBoxTaskType());
            AllBoxList.add(box);
            CarBoxList.add(box);
            ylMediaPlayer.SuccessOrFailMidia("success",getApplicationContext());
            YLSystem.setEdiboxList(AllBoxList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        homylboxscan_cb_complie.setChecked(false);
        Log.e(YLSystem.getKimTag(), AllBoxList.size() + "款箱数量");
        TallyBox(AllBoxList);
    }//扫箱数据加载界面

    private void AddArriveTime() {
        List<ArriveTime> arriveTimeList = new ArrayList<>();
        for (int i = 0 ; i < ylTask.lstSite.size();i++){
            if (ylTask.lstSite.get(i).getSiteID().equals(homylboxscan_tv_title.getTag().toString())){
                Site site = ylTask.lstSite.get(i);
                arriveTimeList =  site.getLstArriveTime();

                int TimeID = 0;
                if (arriveTimeList == null){
                    TimeID = 1;
                }else {
                    TimeID = arriveTimeList.size()+1;
                }
                arriveTime = new ArriveTime();
                arriveTime.setServerReturn("1");
                arriveTime.setEmpID(YLSystem.getUser().getEmpID());
                try {
                    arriveTime.setATime(YLSysTime.GetStrCurrentTime());
                    arriveTime.setTradeBegin(YLSysTime.GetStrCurrentTime());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                arriveTime.setTimeID(TimeID + "");
                arriveTime.setSiteID(homylboxscan_tv_title.getTag().toString());
            }
        }
    }//添加时间ID

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.homylboxscan_btn_date:

                GetDatePick();
                YLRecord.WriteRecord("扫描","获取寄库箱日期"+PickDate);
                if (homylboxscan_cb_date.isChecked()) {
                    CurrentBox.setNextOutTime("2099-12-31");
                } else {
                    CurrentBox.setNextOutTime(PickDate);
                }
                break;
            case R.id.homylboxscan_btn_scan:
                YLRecord.WriteRecord("扫描","款箱扫描");
                ScanYLBox();
                break;
            case R.id.homylboxscan_btn_nonelable:
                YLRecord.WriteRecord("扫描","手输单号");
                NoLableIns();
                break;
            case R.id.homylboxscan_btn_ent:
                ArriveAndFinish();
                break;
            case R.id.homylboxscan_btn_boxtype:
                ShowBoxtype();
                CurrentBox.setBoxType(homylboxscan_btn_boxtype.getText().toString());
                YLRecord.WriteRecord("扫描","临时箱选择"+homylboxscan_btn_boxtype.getText().toString());
                break;
            case R.id.homylboxscan_rbtn_get:
                CurrentBox.setTradeAction("收");
                YLRecord.WriteRecord("扫描","设置收箱");
                break;
            case R.id.homylboxscan_rbtn_give:
                CurrentBox.setTradeAction("送");
                YLRecord.WriteRecord("扫描","设置送箱");
                break;
            case R.id.homylboxscan_rbtn_full:
                CurrentBox.setBoxStatus("实");
                YLRecord.WriteRecord("扫描","设置实箱");
                break;
            case R.id.homylboxscan_rbtn_empty:
                CurrentBox.setBoxStatus("空");
                YLRecord.WriteRecord("扫描","设置空箱");
                break;
            case R.id.homylboxscan_cb_complie:
                    if (homylboxscan_cb_complie.isChecked()){
                        CurrentBox.setRemark("1");
                        YLRecord.WriteRecord("扫描","设置补打");
                    }else {
                        CurrentBox.setRemark("0");
                        YLRecord.WriteRecord("扫描","设置不补打");
                    }
                break;
            case R.id.homylboxscan_cb_date:
                if (homylboxscan_cb_date.isChecked()) {
                    CurrentBox.setNextOutTime("2099-12-31");
                    YLRecord.WriteRecord("扫描","设置长期寄库");
                } else {
                    CurrentBox.setNextOutTime(PickDate);
                    YLRecord.WriteRecord("扫描","设置寄库日期："+PickDate);
                }
                break;
            case R.id.homylboxscan_cb_ToT:
                if (homylboxscan_cb_ToT.isChecked()){
                    CurrentBox.setBoxToT("1");
                    YLRecord.WriteRecord("扫描","设置即日收送");
                }else {
                    CurrentBox.setBoxToT("0");
                    YLRecord.WriteRecord("扫描","设置非即日收送");
                }
                break;
            case R.id.homylboxscan_cb_scan:
                if (!homylboxscan_cb_scan.isChecked())
                {
                    Scan1DCmd("stopscan");
                    homylboxscan_btn_scan.setText("扫描/F1");
                }
                break;
        }
    }//按键事件

    private void ShowBoxtype() {
      new AlertDialog.Builder(this).setTitle("款箱类型").setIcon(android.R.drawable.ic_dialog_info)
              .setSingleChoiceItems(R.array.ylboxtype, 0, new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialogInterface, int i) {
                      switch (i) {
                          case 0:
                              homylboxscan_btn_boxtype.setText("款箱");
                              break;
                          case 1:
                              homylboxscan_btn_boxtype.setText("卡箱");
                              break;
                          case 2:
                              homylboxscan_btn_boxtype.setText("凭证箱");
                              break;
                          case 3:
                              homylboxscan_btn_boxtype.setText("凭证袋");
                              break;
                      }
                      dialogInterface.dismiss();
                  }

              }).show();
    }

    private void GetDatePick() {
        int year = calendar.get(Calendar.YEAR);
        int Month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(HomYLBoxScan.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        PickDate = year+"-"+String.format("%02d",(monthOfYear + 1))+"-"
                                +String.format("%02d",(dayOfMonth));
                        homylboxscan_btn_date.setText(PickDate);
                        CurrentBox.setNextOutTime(PickDate);
                        Log.e(YLSystem.getKimTag(),PickDate+"");
                    }
                },year,Month,day);
        datePickerDialog.show();
    }

    private void NoLableIns() {
        if (CheckRadioButton("need")) {
            Toast.makeText(getApplicationContext(), YLScanMessage, Toast.LENGTH_SHORT).show();
            return;
        }

        homylboxscan_cb_complie.setChecked(true);
        final EditText et = new EditText(this);
        et.setInputType(EditorInfo.TYPE_CLASS_NUMBER);

        new AlertDialog.Builder(this).setTitle("条码数据:")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setView(et)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
//                        String input = et.getText().toString().replaceAll("^(0+)", "");
                        String input = et.getText().toString();
                        if (input.length() !=10||input.equals("")) {
                            Toast.makeText(getApplicationContext(), "输入标签有误！", Toast.LENGTH_SHORT).show();
                        } else {
//                            int intinput = Integer.parseInt(input);
//                            Box box = new Box();
//                            box.setBoxID("无标签");
//                            box.setBoxName("无标签");
//                            PutBoxToList(box,intinput+"","tmp");
                            BoxIDtoBox(input);

                        }
                    }
                }).setNegativeButton("取消", null).show();
    }

    private void ArriveAndFinish() {
        if (homylboxscan_btn_ent.getText().equals("到达")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(HomYLBoxScan.this);
            builder.setTitle("确认到达吗？");
            final String[] multiChoiceItems = {"网点交接提示"};
            final boolean[] defaultSelectedStatus = {true};
            builder.setMultiChoiceItems(multiChoiceItems, defaultSelectedStatus, new DialogInterface.OnMultiChoiceClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                    defaultSelectedStatus[i] = b;
                }
            });

            builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    YLRecord.WriteRecord("扫描","到达");
                    try {
                        ShowNotice = defaultSelectedStatus[0];
                        Log.e(YLSystem.getKimTag(),ShowNotice+"提示");
                        boolean sitecheck = true;
                        for (Site site : ylTask.getLstSite()) {
                            if (site.getStatus().equals("已完成")) {
                                sitecheck = false;
                                break;
                            }
                        }
                        if (sitecheck) {
                            WebServerYLSite webServerYLSite = new WebServerYLSite();
                            String BoxOutID = webServerYLSite.GetCarBoxOutID
                                    (getApplicationContext(), ylTask.getTaskID(), YLSystem.getUser().getEmpID());
                            Log.e(YLSystem.getKimTag(), "库管出箱ID：" + BoxOutID + "手持机箱数：" + CarBoxList.size());
                            YLRecord.WriteRecord("扫描","库管出箱ID：" + BoxOutID + "手持机车内箱数：" + CarBoxList.size());
                            if (!BoxOutID.equals("0")) {
                                if (CarBoxList.size() > 0) {
                                    if (!CarBoxList.get(0).getServerReturn().equals(BoxOutID)) {
                                        Log.e(YLSystem.getKimTag(), "更新出库数据");
                                        YLRecord.WriteRecord("扫描","更新出库数据");
                                        List<Box> newboxList = webServerYLSite.GetCarBoxlist
                                                (getApplicationContext(), ylTask.getTaskID());
                                        CarBoxList.clear();
                                        CarBoxListnosave.clear();
                                        CarBoxList.addAll(newboxList);
                                        CarBoxListnosave.addAll(newboxList);
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    homylboxscan_btn_ent.setText("完成交接");
                    homylboxscan_btn_scan.setEnabled(true);
                    homylboxscan_btn_nonelable.setEnabled(true);
                    AddArriveTime();
                    AllBoxList = new ArrayList<Box>();
                    YLSystem.setEdiboxList(AllBoxList);

                    CurrentBox.setSiteID(homylboxscan_tv_title.getTag().toString());
                    CurrentBox.setTimeID(arriveTime.getTimeID());

                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    YLRecord.WriteRecord("扫描","取消到达");
                    homylboxscan_btn_ent.setText("到达");
                    dialog.dismiss();

                }
            });
            builder.create().show();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(HomYLBoxScan.this);
            builder.setMessage("确认完成交接吗?");
            builder.setTitle("提示");
            builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    YLRecord.WriteRecord("扫描","完成交接");
                    if (AllBoxList.size() < 1) {
                        dialog.dismiss();
                        HomYLBoxScan.this.finish();
                        return;
                    }
                    for (int i = 0; i < ylTask.lstSite.size(); i++) {
                        if (ylTask.lstSite.get(i).getSiteID().equals(homylboxscan_tv_title.getTag().toString())) {
                            ylTask.lstSite.get(i).setStatus("已完成");
                        }
                    }
                    if (ylTask.lstBox == null) {
                        ylTask.lstBox = new ArrayList<>();
                    }
                    Set<Box> boxSet = new HashSet<Box>(new ArrayList<Box>());
                    AllBoxList = YLSystem.getEdiboxList();
                    for (Box box : AllBoxList) {
                        boxSet.add(box);
                    }
                    ylTask.lstBox.addAll(boxSet);
                    try {
                        arriveTime.setTradeEnd(YLSysTime.GetStrCurrentTime());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    arriveTime.setTradeState("1");

                    for (int i = 0; i < ylTask.lstSite.size(); i++) {
                        if (ylTask.lstSite.get(i).getSiteID().equals(homylboxscan_tv_title.getTag().toString())) {
                            Site site = ylTask.lstSite.get(i);
                            List<ArriveTime> getArrTiemList = site.getLstArriveTime();
                            if (getArrTiemList == null) {
                                getArrTiemList = new ArrayList<>();
                                getArrTiemList.add(arriveTime);
                            } else {
                                getArrTiemList.add(arriveTime);
                            }
                            site.setLstArriveTime(getArrTiemList);
                            ylTask.lstSite.set(i, site);
                        }
                    }
                    ylTask.setId(TaskTimeID + 1);
                    ylTask.setLstCarBox(CarBoxList);
                    tasksManager.SaveTask(getApplicationContext());
                    AllBoxList.clear();
                    YLSystem.setEdiboxList(AllBoxList);
                    dialog.dismiss();
                    HomYLBoxScan.this.finish();
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

    private void ScanYLBox() {
        if (CheckRadioButton("noneed")){
            Toast.makeText(getApplicationContext(), YLScanMessage, Toast.LENGTH_SHORT).show();
            return;
        }

        if (homylboxscan_cb_scan.isChecked()){
            if (homylboxscan_btn_scan.getText().equals("扫描/F1")){
                homylboxscan_btn_scan.setText("停止/F1");
                ShowDailog = true;
                Scan1DCmd("toscan100ms");
            }else {
                homylboxscan_btn_scan.setText("扫描/F1");
                Scan1DCmd("stopscan");
            }
        }else {
            ShowDailog = true;
            Scan1DCmd("scan");
        }
    }


    private void TallyBox(List<Box> boxList) {
        try {
            if (boxList == null) return;
            int emptybox = 0;
            int fullbox = 0;
            int getbox = 0;
            int givebox = 0;
            int moneybox = 0;
            int cardbox = 0;
            int voucher = 0;
            int voucherbag = 0;
            for (Box box : boxList) {
                if (box.getTradeAction().equals("收")) {
                    getbox += Integer.parseInt(box.getBoxCount());
                } else {
                    givebox += Integer.parseInt(box.getBoxCount());
                }
                if (box.getBoxStatus().equals("空")) {
                    emptybox += Integer.parseInt(box.getBoxCount());
                } else {
                    fullbox += Integer.parseInt(box.getBoxCount());
                }
                switch (box.getBoxType()) {
                    case "款箱":
                        moneybox += Integer.parseInt(box.getBoxCount());
                        break;
                    case "卡箱":
                        cardbox += Integer.parseInt(box.getBoxCount());
                        break;
                    case "凭证箱":
                        voucher += Integer.parseInt(box.getBoxCount());
                        break;
                    case "凭证袋":
                        voucherbag += Integer.parseInt(box.getBoxCount());
                        break;
                }
            }
            homylboxscan_rbtn_get.setText("收箱:" + getbox);
            homylboxscan_rbtn_give.setText("送箱:" + givebox);
            homylboxscan_rbtn_full.setText("实箱:" + fullbox);
            homylboxscan_rbtn_empty.setText("空箱:" + emptybox);
            homylboxscan_tv_moneybox.setText("款箱:" + moneybox);
            homylboxscan_tv_cardbox.setText("卡箱:" + cardbox);
            homylboxscan_tv_Voucher.setText("凭证箱:\r\n     " + voucher);
            homylboxscan_tv_Voucherbag.setText("凭证袋:\r\n     " + voucherbag);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private void ShowBoxStautdailog(final Box givebox){
        YLRecord.WriteRecord("扫描","送箱录入未有车内箱");
        AlertDialog.Builder builder = new AlertDialog.Builder(HomYLBoxScan.this);
        builder.setIcon(android.R.drawable.ic_input_get);
        builder.setTitle("请选择送箱状态");
        View view = LayoutInflater.from(HomYLBoxScan.this).inflate(R.layout.ylboxstautandtasktype, null);
        builder.setView(view);
        ShowDailog = false;
        final  RadioButton boxstaut_empty = (RadioButton) view.findViewById(R.id.boxstaut_empty);
        final  RadioButton boxstaut_full = (RadioButton) view.findViewById(R.id.boxstaut_full);
        final  CheckBox boxstaut_ToT = (CheckBox) view.findViewById(R.id.boxstaut_ToT);
        final RadioButton boxstaut_upordown = (RadioButton) view.findViewById(R.id.boxstaut_upordown);
        final RadioButton boxstaut_samebankdispatch = (RadioButton) view.findViewById(R.id.boxstaut_samebankdispatch);
        final RadioButton boxstaut_differentbankdispatch = (RadioButton) view.findViewById(R.id.boxstaut_differentbankdispatch);
        final RadioButton boxstaut_compelygog = (RadioButton) view.findViewById(R.id.boxstaut_compelygog);
        final RadioButton boxstaut_gog = (RadioButton) view.findViewById(R.id.boxstaut_gog);
        final RadioButton boxstaut_invaultbox = (RadioButton) view.findViewById(R.id.boxstaut_invaultbox);

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    Box box = new Box();
                    if (boxstaut_full.isChecked()) {
                        box.setBoxStatus("实");
                    } else {
                        box.setBoxStatus("空");
                    }
                    if (boxstaut_ToT.isChecked()) {
                        box.setBoxToT("1");
                    } else {
                        box.setBoxToT("0");
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
                    box.setBoxID(givebox.getBoxID());
                    box.setBoxName(givebox.getBoxName());
                    box.setBoxType(givebox.getBoxType());
                    box.setSiteID(CurrentBox.getSiteID());
                    box.setTimeID(CurrentBox.getTimeID());
                    box.setBoxCount("1");
                    box.setBoxOrder(AllBoxList.size() + 1 + "");
                    box.setBoxTaskType(tasktype);
                    box.setTaskTimeID(CurrentBox.getTaskTimeID());
                    box.setActionTime(YLSysTime.GetStrCurrentTime());
                    box.setTradeAction("送");
                    box.setId(1);
                    homylboxscan_tv_boxname.setText(givebox.getBoxName());
                    homylboxscan_tv_boxaction.setText("送");
                    homylboxscan_tv_boxtype.setText(givebox.getBoxType());
                    homylboxscan_tv_boxcount.setText("1");
                    homylboxscan_tv_boxstaut.setText(box.getBoxStatus());
                    homylboxscan_tv_tasktype.setText(tasktype);
                    Log.e(YLSystem.getKimTag(), "添加额外款箱：" + box.toString());
                    AllBoxList.add(box);
                    TallyBox(AllBoxList);
                    ylMediaPlayer.SuccessOrFailMidia("success", getApplicationContext());
                    ShowDailog = true;
                    YLRecord.WriteRecord("扫描","送箱选非车内箱："+box.getBoxName()+box.getBoxStatus());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ShowDailog = true;
            }
        });
        builder.show();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode){
            case 131:ScanYLBox();
                break;
            case 132:ScanYLBox();
                break;
            case 133:ScanYLBox();
                break;
            case 134:ScanYLBox();
                break;
            case 4:LeaveAction();
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void LeaveAction() {
        if (homylboxscan_btn_ent.getText().equals("到达")){
            finish();
            overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(HomYLBoxScan.this);
        builder.setMessage("未完成确认离开吗?");
        builder.setTitle("提示");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ylTask.setLstCarBox(CarBoxListnosave);
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
        YLEditData.setYleditcarbox(CarBoxList);
        YLSystem.setEdiboxList(AllBoxList);
        Scan1DCmd("stopscan");
        homylboxscan_btn_scan.setText("扫描/F1");
        super.onStop();
    }

    @Override
    protected void onPostResume() {
        if (homylboxscan_btn_ent.getText().equals("完成交接")){
            AllBoxList = YLSystem.getEdiboxList();
            CarBoxList = YLEditData.getYleditcarbox();
            Log.e(YLSystem.getKimTag(),CarBoxList.size()+"重载返回数据");
            TallyBox(AllBoxList);
            InitTextView();
        }
        super.onPostResume();
    }

    private void InitTextView() {
        homylboxscan_tv_boxname.setText("");
        homylboxscan_tv_boxcount.setText("");
        homylboxscan_tv_boxaction.setText("");
        homylboxscan_tv_boxtype.setText("");
        homylboxscan_tv_boxstaut.setText("");
        homylboxscan_tv_tasktype.setText("");
    }

    @Override
    protected void onDestroy() {
        if (HomBoxscan1DRecive != null){
            unregisterReceiver(HomBoxscan1DRecive);
            unregisterReceiver(mBatlnfoReceiver);
        }
        Scan1DCmd("stopscan");
        super.onDestroy();
    }

}
