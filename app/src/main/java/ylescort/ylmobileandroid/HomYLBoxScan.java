package ylescort.ylmobileandroid;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
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
import java.util.Date;
import java.util.List;

import TaskClass.ArriveTime;
import TaskClass.Box;
import TaskClass.Site;
import TaskClass.TasksManager;
import TaskClass.YLTask;
import YLDataService.YLBoxScanCheck;
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
    private RadioButton homylboxscan_rbtn_moneyboxs;
    private RadioButton homylboxscan_rbtn_cardbox;
    private RadioButton homylboxscan_rbtn_Voucher;
    private RadioButton homylboxscan_rbtn_Voucherbag;

    private Button homylboxscan_btn_date;
    private Button homylboxscan_btn_scan;
    private Button homylboxscan_btn_nonelable;
    private Button homylboxscan_btn_ent;
    private Button homylboxscan_btn_boxtype;

    private CheckBox homylboxscan_cb_scan;
    private CheckBox homylboxscan_cb_complie;
    private CheckBox homylboxscan_cb_date;

    private List<Box> AllBoxList;

    private Scan1DRecive HomBoxscan1DRecive;

    private ArriveTime arriveTime;
    private TasksManager tasksManager = null;//任务管理类
    private YLTask ylTask;//当前选中的任务
    private String box_sp_text ;
    private String YLScanMessage;
    private String PickDate;
    private Calendar calendar;
    private int TaskTimeID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hom_ylbox_scan);

        HomYLBoxScan.this.setTitle("款箱操作: " + YLSystem.getUser().getName());
        tasksManager= YLSystem.getTasksManager();//获取任务管理类
        ylTask=tasksManager.CurrentTask;//当前选中的任务
        try {
            InitView();
            InitData();
            InitReciveScan1D();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void InitData() {

        Bundle bundle = this.getIntent().getExtras();
        String SiteName = bundle.getString("sitename");
        String SiteID = bundle.getString("siteid");
        homylboxscan_tv_title.setText(SiteName);
        homylboxscan_tv_title.setTag(SiteID);

        ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(this,R.array.tasktype
                ,android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        homylboxscan_sp_tasktype.setAdapter(arrayAdapter);
        homylboxscan_sp_tasktype.setPrompt("交接类型");
        homylboxscan_sp_tasktype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                box_sp_text = parent.getItemAtPosition(position).toString();
                switch (box_sp_text){
                    case "早送":
                        homylboxscan_rbtn_give.setChecked(true);
                        homylboxscan_rbtn_give.setEnabled(true);
                        homylboxscan_rbtn_get.setEnabled(false);
                        homylboxscan_btn_date.setEnabled(false);
                        homylboxscan_cb_date.setEnabled(false);
                        break;
                    case "晚收":
                        homylboxscan_rbtn_get.setChecked(true);
                        homylboxscan_rbtn_get.setEnabled(true);
                        homylboxscan_rbtn_give.setEnabled(false);
                        homylboxscan_btn_date.setEnabled(false);
                        homylboxscan_cb_date.setEnabled(false);
                        break;
                    case "寄库箱":
                        homylboxscan_btn_date.setEnabled(true);
                        homylboxscan_cb_date.setEnabled(true);
                        break;
                    default:
                        homylboxscan_rbtn_give.setEnabled(true);
                        homylboxscan_rbtn_get.setEnabled(true);
                        homylboxscan_btn_date.setEnabled(false);
                        homylboxscan_cb_date.setEnabled(false);
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        if (ylTask.getId() == null){
            TaskTimeID = 1;
        }else {
            TaskTimeID = ylTask.getId();
        }
        Log.e(YLSystem.getKimTag(),TaskTimeID+"TaskTimeID");

        /**
         * 根据任务匹配多选项
         */
        String tasktype = ylTask.getTaskType();
        if (tasktype.equals("早送")){
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
        homylboxscan_tv_Voucher.setText("凭证箱:"+0);
        homylboxscan_tv_Voucherbag.setText("凭证袋:"+0);

        calendar =YLSysTime.AddDateString(Calendar.getInstance(),1) ;

        int year = calendar.get(Calendar.YEAR);
        int Month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);

        String Date = year+"-"+String.format("%02d",(Month + 1))+"-"
                +String.format("%02d",(day));

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

        homylboxscan_btn_date.setOnClickListener(this);
        homylboxscan_btn_scan.setOnClickListener(this);
        homylboxscan_btn_nonelable.setOnClickListener(this);
        homylboxscan_btn_ent.setOnClickListener(this);
        homylboxscan_btn_boxtype.setOnClickListener(this);
        homylboxscan_cb_scan.setOnClickListener(this);
        homylboxscan_cb_complie.setOnClickListener(this);
        homylboxscan_cb_date.setOnClickListener(this);

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

        if (!homylboxscan_rbtn_empty.isChecked() & !homylboxscan_rbtn_full.isChecked()) {
            YLScanMessage = "空实未选";
            return true;
        }
        if (!homylboxscan_rbtn_get.isChecked() & !homylboxscan_rbtn_give.isChecked()) {
            YLScanMessage = "收送未选";
            return true;
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
            Intent intent = new Intent();
            intent.setClass(this, YLBoxEdit.class);
            String box_btn_ent_text = homylboxscan_btn_ent.getText().toString();
            Bundle bundle = new Bundle();
            bundle.putString("siteid",homylboxscan_tv_title.getTag().toString());
            bundle.putString("box_btn_ent_text",box_btn_ent_text);
            intent.putExtras(bundle);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class Scan1DRecive extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String recivedata = intent.getStringExtra("result");
            if (recivedata != null) {
//                if (!box_sp_text.contains("企业") &AllBoxList.size()>0){
////                    for (Box box :AllBoxList) {
////                        if (box.getBoxID().equals(YLBoxScanCheck.replaceBlank(recivedata))) {
////                            YLBoxMediaPlay("fail");
////                            return;
////                        }
////                    }
                BoxIDtoBox(recivedata);
            }
        }//注册广播
    }

    private void BoxIDtoBox(String recivedata) {
        Log.e(YLSystem.getKimTag(), recivedata + "接收数据1D");
        for (int i = AllBoxList.size() - 1; i >= 0; i--) {
            if (AllBoxList.get(i).getBoxID().equals(recivedata)
                    & AllBoxList.get(i).getTradeAction().equals(getboxatcion())) {
                YLBoxMediaPlay("fail");
                return;
            }
        }
        try {
            Box box = YLBoxScanCheck.CheckBox(recivedata, getApplicationContext());
            if (box.getBoxName().equals("无数据")) {
                YLBoxMediaPlay("fail");
                return;
            }
            if (box.getBoxName().contains("粤龙临") || box.getBoxType().equals("无")) {
                if (!checkboxsype().equals("款箱类")) {
                    box.setBoxType(checkboxsype());
                } else {
                    Toast.makeText(getApplicationContext(), "标签箱类型未选", Toast.LENGTH_SHORT).show();
                    YLBoxMediaPlay("fail");
                    return;
                }
            }
            PutBoxToList(box, "1", "ordin");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getboxatcion(){
        if (homylboxscan_rbtn_get.isChecked()){
            return "收";
        }else {
            return "送";
        }
    }

    private String checkboxsype(){
        return homylboxscan_btn_boxtype.getText().toString();
    }

    private void PutBoxToList(Box box,String count,String type) {

        String tradeaction ="";
        String tradestaut = "";
        String tradetype = "";
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
            if (homylboxscan_cb_date.isChecked()){
                box.setNextOutTime("2099-12-31");
            }else {
                box.setNextOutTime(PickDate);
            }
            if (homylboxscan_cb_complie.isChecked()){
                box.setRemark("1");
            }else {
                box.setRemark("");
            }
            box.setTaskTimeID(TaskTimeID);
            box.setActionTime(YLSysTime.GetStrCurrentTime());
            Log.e(YLSystem.getKimTag(), box.toString() + "插入款箱");
            AllBoxList.add(box);
            YLBoxMediaPlay("success");
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
            case R.id.homylboxscan_btn_date:GetDatePick();
                break;
            case R.id.homylboxscan_btn_scan:ScanYLBox();
                break;
            case R.id.homylboxscan_btn_nonelable:NoLableIns();
                break;
            case R.id.homylboxscan_btn_ent:ArriveAndFinish();
                break;
            case R.id.homylboxscan_btn_boxtype:ShowBoxtype();
                break;
            case R.id.homylboxscan_cb_scan:
                break;
            case R.id.homylboxscan_cb_complie:
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
                          case 2:homylboxscan_btn_boxtype.setText("凭证箱");
                              break;
                          case 3:homylboxscan_btn_boxtype.setText("凭证袋");
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

        new AlertDialog.Builder(this).setTitle("数量:")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setView(et)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        String input = et.getText().toString().replaceAll("^(0+)", "");
//                        String input = et.getText().toString();
//                        BoxIDtoBox(input);

                        if (input.equals("")|| input.equals("0")) {
                            Toast.makeText(getApplicationContext(), "不能为空", Toast.LENGTH_SHORT).show();
                        } else {
                            int intinput = Integer.parseInt(input);
                            Box box = new Box();
                            box.setBoxID("无标签");
                            box.setBoxName("无标签");
                            PutBoxToList(box,intinput+"","tmp");
                        }


                    }
                }).setNegativeButton("取消", null).show();
    }

    private void ArriveAndFinish() {
        if (homylboxscan_btn_ent.getText().equals("到达")){
            AlertDialog.Builder builder = new AlertDialog.Builder(HomYLBoxScan.this);
            builder.setMessage("确认到达吗?");
            builder.setTitle("提示");
            builder.setPositiveButton("确认",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    homylboxscan_btn_ent.setText("完成交接");
                    homylboxscan_btn_scan.setEnabled(true);
                    homylboxscan_btn_nonelable.setEnabled(true);
                    AddArriveTime();
                    AllBoxList = new ArrayList<Box>();
                    YLSystem.setEdiboxList(AllBoxList);
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("取消",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    homylboxscan_btn_ent.setText("到达");
                    dialog.dismiss();

                }
            });
            builder.create().show();
        }else {
            AlertDialog.Builder builder = new AlertDialog.Builder(HomYLBoxScan.this);
            builder.setMessage("确认完成交接吗?");
            builder.setTitle("提示");
            builder.setPositiveButton("确认",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(AllBoxList.size()< 1){
                        dialog.dismiss();
                        HomYLBoxScan.this.finish();
                        return;
                    }
                    for (int i = 0 ;i<ylTask.lstSite.size();i++){
                        if (ylTask.lstSite.get(i).getSiteID().equals(homylboxscan_tv_title.getTag().toString())){
                            ylTask.lstSite.get(i).setStatus("已完成");
                        }
                    }
                    if (ylTask.lstBox== null){
                        ylTask.lstBox = new ArrayList<>();
                    }
                    AllBoxList= YLSystem.getEdiboxList();
                    for (int i = 0 ;i < AllBoxList.size();i++){
                        Box box = new Box();
                        box = AllBoxList.get(i);
                        ylTask.lstBox.add(box);
                    }
                    try {
                        arriveTime.setTradeEnd(YLSysTime.GetStrCurrentTime());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    arriveTime.setTradeState("1");

                    for (int i = 0 ; i < ylTask.lstSite.size();i++){
                        if (ylTask.lstSite.get(i).getSiteID().equals(homylboxscan_tv_title.getTag().toString())){
                            Site site = ylTask.lstSite.get(i);
                            List<ArriveTime> getArrTiemList =  site.getLstArriveTime();
                            if (getArrTiemList == null){
                                getArrTiemList = new ArrayList<>();
                                getArrTiemList.add(arriveTime);
                            }else {
                                getArrTiemList.add(arriveTime);
                            }
                            site.setLstArriveTime(getArrTiemList);
                            ylTask.lstSite.set(i,site);
                        }
                    }
                    ylTask.setId(TaskTimeID+1);
                    tasksManager.SaveTask(getApplicationContext());
                    AllBoxList.clear();
                    YLSystem.setEdiboxList(AllBoxList);
                    Log.e(YLSystem.getKimTag(),ylTask.getLstBox().size()+"款箱数量");
                    dialog.dismiss();
                    HomYLBoxScan.this.finish();
                }

            });

            builder.setNegativeButton("取消",new DialogInterface.OnClickListener() {
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
                Scan1DCmd("toscan100ms");
            }else {
                homylboxscan_btn_scan.setText("扫描/F1");
                Scan1DCmd("stopscan");
            }
        }else {
            Scan1DCmd("scan");
        }

    }


    private void TallyBox(List<Box> boxList) {
        if (boxList == null)return;
        int emptybox = 0;
        int fullbox = 0;
        int getbox = 0;
        int givebox = 0;
        int moneybox = 0;
        int cardbox = 0;
        int voucher =0;
        int voucherbag = 0;
        for (Box box :boxList){
            if (box.getTradeAction().equals("收")){
                getbox +=Integer.parseInt(box.getBoxCount()) ;}
            else{
                givebox+=Integer.parseInt(box.getBoxCount());
            }
            if (box.getBoxStatus().equals("空")){
                emptybox+=Integer.parseInt(box.getBoxCount());
            }else {
                fullbox+=Integer.parseInt(box.getBoxCount());
            }
            switch (box.getBoxType()){
                case "款箱":moneybox+=Integer.parseInt(box.getBoxCount());
                    break;
                case "卡箱":cardbox+=Integer.parseInt(box.getBoxCount());
                    break;
                case "凭证箱":voucher+=Integer.parseInt(box.getBoxCount());
                    break;
                case "凭证袋":voucherbag+=Integer.parseInt(box.getBoxCount());
                    break;
            }
        }
        homylboxscan_rbtn_get.setText("收箱:"+getbox);
        homylboxscan_rbtn_give.setText("送箱:"+givebox);
        homylboxscan_rbtn_full.setText("实箱:"+fullbox);
        homylboxscan_rbtn_empty.setText("空箱:"+emptybox);
        homylboxscan_tv_moneybox.setText("款箱:"+moneybox);
        homylboxscan_tv_cardbox.setText("卡箱:"+cardbox);
        homylboxscan_tv_Voucher.setText("凭证箱:"+voucher);
        homylboxscan_tv_Voucherbag.setText("凭证袋:"+voucherbag);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.e(YLSystem.getKimTag(),keyCode+"");
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
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(HomYLBoxScan.this);
        builder.setMessage("未完成确认离开吗?");
        builder.setTitle("提示");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                dialog.dismiss();
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
    protected void onPostResume() {
        if (homylboxscan_btn_ent.getText().equals("完成交接")){
            AllBoxList = YLSystem.getEdiboxList();
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
        }
        Scan1DCmd("stopscan");
        super.onDestroy();
    }

    private void YLBoxMediaPlay(String mediavoice)  {
        try {
            MediaPlayer mPlayer = new MediaPlayer();
            if (mediavoice.equals("success")){
                mPlayer = MediaPlayer.create(HomYLBoxScan.this, R.raw.msg);
                if(mPlayer.isPlaying()){
                    return;
                }
            }else {
                mPlayer.setDataSource("/system/media/audio/notifications/Proxima.ogg");  //选用系统声音文件
                mPlayer.prepare();
            }
            mPlayer.start();
            Thread.sleep(300);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
