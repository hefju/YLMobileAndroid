package ylescort.ylmobileandroid;

import android.app.AlertDialog;
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
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import TaskClass.ArriveTime;
import TaskClass.BaseBox;
import TaskClass.Box;
import TaskClass.Site;
import TaskClass.TasksManager;
import TaskClass.YLTask;
import YLDataService.BaseBoxDBSer;
import YLSystemDate.YLSystem;
import adapter.YLBoxAdapter;


public class YLBoxScan extends ActionBarActivity {

    /**
     * 款箱扫描控件
     */
    private TextView box_tv_titel;
    private ListView YLScanBoxlistView;
    private Switch box_swh_singleormore;//单多
    private Spinner box_sp_stype;//交接类型
    private RadioButton box_rbtn_empty;//空箱
    private TextView box_tv_empty;//空箱统计
    private RadioButton box_rbtn_full;//实箱
    private TextView box_tv_full;//实箱统计
    private RadioButton box_rbtn_get;//收箱
    private TextView box_tv_get;//收箱统计
    private RadioButton box_rbtn_give;//送箱
    private TextView box_tv_give;//送箱统计
    private TextView box_tv_total;//操作统计
    private RadioButton  box_rbtn_moneyboxs;//款箱
    private RadioButton  box_rbtn_cardbox;//卡箱
    private RadioButton  box_rbtn_Voucher;//凭证
    private TextView box_tv_moneyboxs;//款箱统计
    private TextView box_tv_cardbox;//卡箱统计
    private TextView box_tv_voucher;//凭证统计
    private Button box_btn_ent;//确认
    private Button box_btn_scan;//扫描
    private Button box_btn_nonelable;//无标签
    /**
     * 红外扫描注册广播
     */
    private String cmd = "scan";
    private FileOutputStream fos;
    private MyBroadcast myBroad;  //广播接收者
    //private String activity = "ylescort.ylmobileandroid.box";
    public String TAG = "MainActivity";  //Debug
    private MediaPlayer mPlayer;  //媒体播放者，用于播放提示音
    /**
     * 热键广播
     */
    private boolean keyDownFlag = false;
    private FunkeyListener funkeyReceive; //功能键广播接收者
    /**
     * 款箱处理数据
     */
    //private List<Box> yltaskboxList;//内存内款数据
    private List<Box> ScanboxList;//扫描数据
    private ArriveTime arriveTime;
    private TasksManager tasksManager = null;//任务管理类
    private YLTask ylTask;//当前选中的任务
    private BaseBoxDBSer baseBoxDBSer;
    private BaseBox baseBox;
    private String radiobutton = "";
    private String box_sp_text ;


    private YLBoxAdapter ylBoxAdapter;
    private int listpostion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_box);
        YLBoxScan.this.setTitle("款箱操作: "+YLSystem.getUser().getName());
        tasksManager= YLSystem.getTasksManager();//获取任务管理类
        ylTask=tasksManager.CurrentTask;//当前选中的任务

        try {
            GetYLBoxLayoutControl();//获取界面控件
            LoadYLBoxBaseData();    //获取初始数据
            YLBoxScaninit();        //初始化红外扫描
            KeyBroad();             //初始化热键
//            GetScreen();//备用
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void GetScreen() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
        registerReceiver(mBatlnfoReceiver,intentFilter);
    }

    private BroadcastReceiver mBatlnfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Intent.ACTION_SCREEN_OFF.equals(action)){
                box_btn_scan.setText("停止");
                sendCmd();}
        }
    };

    public void GetYLBoxLayoutControl() throws ClassNotFoundException {

        YLScanBoxlistView = (ListView) findViewById(R.id.boxlistview);
        box_tv_titel = (TextView)findViewById(R.id.box_tv_title);
        box_swh_singleormore = (Switch)findViewById(R.id.box_swh_singleormore);

        box_sp_stype = (Spinner)findViewById(R.id.box_sp_stype);

        box_rbtn_empty = (RadioButton)findViewById(R.id.box_rbtn_empty);
        box_tv_empty = (TextView)findViewById(R.id.box_tv_empty);
        box_rbtn_full = (RadioButton)findViewById(R.id.box_rbtn_full);
        box_tv_full = (TextView)findViewById(R.id.box_tv_full);
        box_rbtn_get = (RadioButton)findViewById(R.id.box_rbtn_get);
        box_tv_get = (TextView)findViewById(R.id.box_tv_get);
        box_rbtn_give = (RadioButton)findViewById(R.id.box_rbtn_give);
        box_tv_give = (TextView)findViewById(R.id.box_tv_give);

        box_tv_total = (TextView)findViewById(R.id.box_tv_total);

        box_rbtn_moneyboxs = (RadioButton)findViewById(R.id.box_rbtn_moneyboxs);
        box_rbtn_cardbox = (RadioButton)findViewById(R.id.box_rbtn_cardbox);
        box_rbtn_Voucher = (RadioButton)findViewById(R.id.box_rbtn_Voucher);

        box_tv_moneyboxs = (TextView)findViewById(R.id.box_tv_moneyboxs);
        box_tv_cardbox = (TextView)findViewById(R.id.box_tv_cardbox);
        box_tv_voucher = (TextView)findViewById(R.id.box_tv_voucher);

        box_btn_ent = (Button)findViewById(R.id.box_btn_ent);
        box_btn_scan = (Button)findViewById(R.id.box_btn_scan);
        box_btn_nonelable = (Button)findViewById(R.id.box_btn_nonelable);

        box_btn_scan.setEnabled(false);
        box_btn_nonelable.setEnabled(false);

        YLScanBoxlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListView listView = (ListView) parent;
                listpostion= position;
                Box box = (Box) listView.getItemAtPosition(position);
                PutBoxTolayout(box);
            }
        });

//        box_sp_stype.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (ScanboxList.size()<1)return;
//                Box box = ScanboxList.get(listpostion);
//                box.setBoxTaskType(parent.getItemAtPosition(position).toString());
//                ScanboxList.set(listpostion,box);
//                BoxScanAdapter(ScanboxList);
//                ylBoxAdapter.notifyDataSetChanged();
//            }
//        });

        //YLBoxScanlayoutOnClick();

    } //获取界面控件

    private String GetBoxScanStuat(String getboxstuat ){
        String boxstuat = "";
        if (getboxstuat.equals("g")){
            if (box_rbtn_give .isChecked()){
                boxstuat = "送";
            }else {
                boxstuat = "收";
            }}
        else if (getboxstuat.equals("f")){
            if (box_rbtn_full.isChecked()){
                boxstuat ="实";
            }else {
                boxstuat ="空";
            }}
        else if (getboxstuat.equals("s")){
            if (box_rbtn_moneyboxs.isChecked()){
                boxstuat ="款箱";
            }else if (box_rbtn_cardbox.isChecked()){
                boxstuat ="卡箱";
            }else if (box_rbtn_Voucher.isChecked()){
                boxstuat ="凭证";
            }}
        return boxstuat;
    }

    //界面控件点击事件
    public void YLBoxScanlayoutOnClick(View view ) throws ClassNotFoundException {

      if(ScanboxList == null || ScanboxList.size() ==0)return;
        Box box = ScanboxList.get(listpostion);
        box.setTradeAction(GetBoxScanStuat("g"));
        box.setBoxStatus(GetBoxScanStuat("f"));
        box.setBoxType(GetBoxScanStuat("s"));
        box.setBoxTaskType(box_sp_stype.getSelectedItem().toString());
        ScanboxList.set(listpostion,box);
        BoxScanAdapter(ScanboxList);

        //ylBoxAdapter.notifyDataSetChanged();

//        box_rbtn_get.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Box box = ScanboxList.get(listpostion);
//                box.setTradeAction("收");
//                ScanboxList.set(listpostion,box);
//                ylBoxAdapter.notifyDataSetChanged();
//            }
//        });
//
//        box_rbtn_give.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Box box = ScanboxList.get(listpostion);
//                box.setTradeAction("送");
//                ScanboxList.set(listpostion,box);
//                ylBoxAdapter.notifyDataSetChanged();
//            }
//        });
//
//        box_rbtn_full.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Box box = ScanboxList.get(listpostion);
//                box.setBoxStatus("实");
//                ScanboxList.set(listpostion,box);
//                ylBoxAdapter.notifyDataSetChanged();
//            }
//        });
//
//        box_rbtn_empty.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Box box = ScanboxList.get(listpostion);
//                box.setBoxStatus("空");
//                ScanboxList.set(listpostion,box);
//                ylBoxAdapter.notifyDataSetChanged();
//            }
//        });
//
//        box_rbtn_moneyboxs.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Box box = ScanboxList.get(listpostion);
//                box.setBoxType("款箱");
//                ScanboxList.set(listpostion,box);
//                ylBoxAdapter.notifyDataSetChanged();
//            }
//        });
//
//        box_rbtn_cardbox.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Box box = ScanboxList.get(listpostion);
//                box.setBoxType("卡箱");
//                ScanboxList.set(listpostion,box);
//                ylBoxAdapter.notifyDataSetChanged();
//            }
//        });
//
//        box_rbtn_Voucher.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Box box = ScanboxList.get(listpostion);
//                box.setBoxType("凭证");
//                ScanboxList.set(listpostion,box);
//                ylBoxAdapter.notifyDataSetChanged();
//            }
//        });
    }

    //款箱属性数据加载至插件
    private void PutBoxTolayout(Box box) {
        if (box.getTradeAction().equals("收")){
            box_rbtn_get.setChecked(true);
        }else {
            box_rbtn_give.setChecked(true);
        }
        if (box.getBoxStatus().equals("实")){
            box_rbtn_full.setChecked(true);
        }else {
            box_rbtn_empty.setChecked(true);
        }

        switch (box.getBoxType()){
            case "款箱":box_rbtn_moneyboxs.setChecked(true);
                break;
            case "卡箱":box_rbtn_cardbox.setChecked(true);
                break;
            case "凭证":box_rbtn_Voucher.setChecked(true);
                break;
        }
        switch (box.getBoxTaskType()){
            case "早送":box_sp_stype.setSelection(0);
                break;
            case "晚收":box_sp_stype.setSelection(1);
                break;
            case "区内中调":box_sp_stype.setSelection(2);
                break;
            case "跨区中调":box_sp_stype.setSelection(3);
                break;
            case "库内区内中调":box_sp_stype.setSelection(4);
                break;
            case "库内跨区中调":box_sp_stype.setSelection(5);
                break;
            case "夜间周转":box_sp_stype.setSelection(6);
                break;
            case "人行":box_sp_stype.setSelection(7);
                break;
        }
    }

    private void LoadYLBoxBaseData() {

        /**
         * 数据库基础数据
         */
        baseBoxDBSer = new BaseBoxDBSer(getApplicationContext());
        baseBox = new BaseBox();

        /**
         * 初始化多选项数据
         */
        ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(this,R.array.tasktype
                ,android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        box_sp_stype.setAdapter(arrayAdapter);
        box_sp_stype.setPrompt("交接类型");
        box_sp_stype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                box_sp_text = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /**
         * 根据任务匹配多选项
         */
        String tasktype = ylTask.getTaskType();
        if (tasktype.equals("早送")){
            box_sp_stype.setSelection(0);
        }else {
            box_sp_stype.setSelection(1);
        }

        /**
         * 获取网点传入数据并赋值标题
         */
        Bundle bundle = this.getIntent().getExtras();
        String SiteName = bundle.getString("sitename");
        String SiteID = bundle.getString("siteid");
        box_tv_titel.setText(SiteName);
        box_tv_titel.setTag(SiteID);

        /**
         * 根据网点读取数据
         */
        //DisPlayBoxListView(SiteID);

    } //获取初始数据

    private void YLBoxScaninit(){
        Log.e(TAG, "on start");
        myBroad = new MyBroadcast();
        IntentFilter filter = new IntentFilter();
        filter.addAction("ylescort.ylmobileandroid.box");
        registerReceiver(myBroad, filter);
        Log.e(TAG, "register Receiver");
        //启动服务
        Intent start = new Intent(YLBoxScan.this, Scan1DService.class);
        YLBoxScan.this.startService(start);

    } //初始化红外扫描

    private void KeyBroad() {

        funkeyReceive  = new FunkeyListener();
        //代码注册功能键广播接收者
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.FUN_KEY");
        registerReceiver(funkeyReceive, filter);
    } //初始化热键

    private void DisPlayBoxListView(String siteID) {
//        List<Box> yltaskboxList =new ArrayList<>();
//        if (ylTask.lstBox != null && ylTask.lstBox.size()>0){
//            for (int i = 0 ; i < ylTask.lstBox.size();i++){
//                if (ylTask.lstBox.get(i).getSiteID().equals(siteID)){
////                    Box box = new Box();
////                    box = ylTask.lstBox.get(i);
//                    yltaskboxList.add(ylTask.lstBox.get(i));
//                }
//            }
//        }
//        BoxScanAdapter(yltaskboxList);
//        TallyBox(yltaskboxList);//统计数据
    }

    private void BoxScanAdapter(List<Box> adapterboxlist){
        if (adapterboxlist !=null){
            ylBoxAdapter = new YLBoxAdapter(this, adapterboxlist, R.layout.activity_boxlist);
            YLScanBoxlistView.setAdapter(ylBoxAdapter);
            YLScanBoxlistView.setSelection(listpostion);
        }
    }

    /**
     *  广播接收者,接收服务发送过来的数据，并更新UI
     * @author Administrator
     *
     */

    private class MyBroadcast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                fos = YLBoxScan.this.openFileOutput("count.txt", Context.MODE_PRIVATE);
            } catch (FileNotFoundException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            String receivedata = intent.getStringExtra("result"); // 服务返回的数据
            if (receivedata != null) {
                Log.e(TAG  + "  receivedata", receivedata);
                receivedata = replaceBlank(receivedata);
                PutDatatoListView(receivedata,"1");
                try {
                    fos.close();
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
				//Selection.setSelection(receive_data.getEditableText(), 0);  //让光标保持在最前面
            }
    }

    private void YLBoxMediaPlay(String mediavoice) {
         mPlayer = new MediaPlayer();

        if (mediavoice.equals("success")){
            mPlayer = MediaPlayer.create(YLBoxScan.this, R.raw.msg);
            if(mPlayer.isPlaying()){
                return;
            }
        }else {
        try {
            mPlayer.setDataSource("/system/media/audio/notifications/Proxima.ogg");  //选用系统声音文件
            mPlayer.prepare();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        }
        mPlayer.start();
    }

    public  String replaceBlank(String str) {
        String dest = "";
        if (str!=null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    public void ScanOnClick (View view ) throws ClassNotFoundException {
//        if(CheckRadioButton()){
//            Toast.makeText(getApplicationContext(),radiobutton,Toast.LENGTH_SHORT).show();
//            return;}
        sendCmd();
    }

    private boolean CheckRadioButton() {

        if (!box_rbtn_empty.isChecked()&!box_rbtn_full.isChecked()){
            radiobutton = "空实未选";
            return true;}
        if (!box_rbtn_get.isChecked()&!box_rbtn_give.isChecked()){
            radiobutton = "收送未选";}
        if (!box_rbtn_moneyboxs.isChecked()&!box_rbtn_cardbox.isChecked()&!box_rbtn_Voucher.isChecked()){
            radiobutton = "箱类未选";
            return true;}
        if (!box_btn_scan.isEnabled()){
            radiobutton= "未到达不能扫描";
            return true;
        }
        return false;
    }

    public void NoLableIns(View view){

        final EditText et = new EditText(this);
        et.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
        new AlertDialog.Builder(this).setTitle("数量:")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setView(et)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String input = et.getText().toString();
                        if (input.equals("")) {
                            Toast.makeText(getApplicationContext(), "不能为空", Toast.LENGTH_SHORT).show();
                        } else {
                            PutDatatoListView("无标签",input);
                        }
                    }
                }).setNegativeButton("取消", null).show();

    }

    public void boxlistent(View view){
        String Entbtn =  box_btn_ent.getText().toString();
        if (Entbtn.equals("确定")){
            AchieveDialog();
        }else{
            ArrEnterDialog();
        }
    }

    protected void ArrEnterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(YLBoxScan.this);
        builder.setMessage("确认到达吗?");
        builder.setTitle("提示");
        builder.setPositiveButton("确认",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                box_btn_ent.setText("确定");
                box_btn_scan.setEnabled(true);
                box_btn_nonelable.setEnabled(true);
                AddArriveTime();

                ScanboxList = new ArrayList<Box>();
                BoxScanAdapter(ScanboxList);

                YLSystem.setEdiboxList(ScanboxList);

                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                box_btn_ent.setText("到达");

                dialog.dismiss();

            }
        });
        builder.create().show();
    }

    protected void AchieveDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(YLBoxScan.this);
        builder.setMessage("确认完成吗?");
        builder.setTitle("提示");
        builder.setPositiveButton("确认",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for (int i = 0 ;i<ylTask.lstSite.size();i++){
                    if (ylTask.lstSite.get(i).getSiteID().equals(box_tv_titel.getTag().toString())){
                        ylTask.lstSite.get(i).setStatus("已完成");
                    }
                }
                if (ylTask.lstBox== null){
                    ylTask.lstBox = new ArrayList<>();
                }
                for (int i = 0 ;i < ScanboxList.size();i++){
                    Box box = new Box();
                    box = ScanboxList.get(i);
                    ylTask.lstBox.add(box);
                }
                YLSystem.setEdiboxList(new ArrayList<Box>());

                arriveTime.setTradeEnd(GetCurrTime());
                arriveTime.setTradeState("1");

                for (int i = 0 ; i < ylTask.lstSite.size();i++){
                    if (ylTask.lstSite.get(i).getSiteID().equals(box_tv_titel.getTag().toString())){
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
                dialog.dismiss();
                YLBoxScan.this.finish();
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

    protected void LeaveDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(YLBoxScan.this);
        builder.setMessage("未完成确认退出吗?");
        builder.setTitle("提示");
        builder.setPositiveButton("确认",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                ScanboxList.clear();
                YLSystem.setEdiboxList(ScanboxList);
                YLBoxScan.this.finish();
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

    private String GetCurrTime(){
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sDateFormat.format(new java.util.Date());
        return date;
    }

    private void AddArriveTime() {
        List<ArriveTime> arriveTimeList = new ArrayList<>();
        for (int i = 0 ; i < ylTask.lstSite.size();i++){
             if (ylTask.lstSite.get(i).getSiteID().equals(box_tv_titel.getTag().toString())){
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
                arriveTime.setATime(GetCurrTime());
                arriveTime.setTimeID(TimeID+"");
                arriveTime.setTradeBegin(GetCurrTime());
                arriveTime.setSiteID(box_tv_titel.getTag().toString());
            }
        }
    }

    private void PutDatatoListView(String boxnumber,String boxcount){
//        baseBox =  baseBoxDBSer.GetBoxByBCNo(boxnumber);
        if (!boxnumber.equals("无标签")){
        if (CheckBoxNumber(boxnumber)){
            //媒体播放
            YLBoxMediaPlay("failed");
            return;}}else{
            baseBox.BoxName = "无标签";}
        Box box = new Box();
        int count= ScanboxList.size();
        box.setSiteID(box_tv_titel.getTag().toString());
        box.setBoxOrder(count + 1 + "");
        box.setBoxID(boxnumber);
        box.setBoxName(baseBox.BoxName);
        box.setTradeAction(GetBoxStuat("g"));
        box.setBoxStatus(GetBoxStuat("f"));
        box.setBoxType(GetBoxStuat("s"));
        box.setBoxTaskType(box_sp_text);
        box.setBoxCount(boxcount);
        box.setTimeID(arriveTime.getTimeID());
        box.setActionTime(GetCurrTime());
        ScanboxList.add(box);
        YLBoxAdapter ylBoxAdapter = new YLBoxAdapter(this, ScanboxList,R.layout.activity_boxlist);
        YLScanBoxlistView.setAdapter(ylBoxAdapter);

        YLBoxMediaPlay("success");

        TallyBox(ScanboxList);//统计数据

        YLSystem.setEdiboxList(ScanboxList);

        scrollMyListViewToBottom();
    }

    private void TallyBox(List<Box> boxList) {
        String until = "个";
        int general = 0;
        int transfer = 0;
        int emptybox = 0;
        int fullbox = 0;
        int getbox = 0;
        int givebox = 0;
        int moneybox = 0;
        int cardbox = 0;
        int voucher =0;
        int total = 0;
        for (Box box :boxList){
         if (box.getTradeAction().equals("收")){
             getbox +=Integer.parseInt(box.getBoxCount()) ;}else{
             givebox+=Integer.parseInt(box.getBoxCount());
         }
            if (box.getBoxStatus().equals("空")){
                emptybox+=Integer.parseInt(box.getBoxCount());
            }else {
                fullbox+=Integer.parseInt(box.getBoxCount());
            }
            if (box.getBoxTaskType().equals("普")){
                general+=Integer.parseInt(box.getBoxCount());
            }else {
                transfer+=Integer.parseInt(box.getBoxCount());
            }
            if (box.getBoxType().equals("款箱")){
                moneybox+=Integer.parseInt(box.getBoxCount());
            }else if (box.getBoxType().equals("卡箱")){
                cardbox+=Integer.parseInt(box.getBoxCount());
            }else {
                voucher+=Integer.parseInt(box.getBoxCount());
            }
        }
        total = moneybox+cardbox+voucher;
//        box_tv_general.setText(general+until);
//        box_tv_transfer.setText(transfer+until);
        box_tv_empty.setText(emptybox+until);
        box_tv_full.setText(fullbox+until);
        box_tv_get.setText(getbox+until);
        box_tv_give.setText(givebox+until);
        box_tv_moneyboxs.setText(moneybox+"");
        box_tv_cardbox.setText(cardbox+"");
        box_tv_voucher.setText(voucher+"");
        box_tv_total.setText("总数:"+total+until);
    }

    private void scrollMyListViewToBottom() {
        YLScanBoxlistView.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                YLScanBoxlistView.setSelection(ScanboxList.size() - 1);
            }
        });
    }

    private boolean CheckBoxNumber(String boxnumber) {

        boolean checkthebox = false;
        baseBox =  baseBoxDBSer.GetBoxByBCNo(boxnumber);
        if (baseBox.BoxBCNo.length()!=10 ||baseBox.BoxName.equals("无数据")){
            checkthebox = true;
            return checkthebox;}
        for (int i = 0 ; i <ScanboxList.size();i++){
            String boxid = ScanboxList.get(i).BoxID;
            if (boxid.equals(boxnumber)){
                checkthebox = true;
                return checkthebox;
            }
        }
        return checkthebox;
    }

    private String GetBoxStuat(String getboxstuat ){
        String boxstuat = "";
        if (getboxstuat.equals("g")){
        if (box_rbtn_give.isChecked()){
            boxstuat = "送";
        }else {
            boxstuat = "收";
        }}
        else if (getboxstuat.equals("f")){
        if (box_rbtn_full.isChecked()){
            boxstuat ="实";
        }else {
            boxstuat ="空";
        }}
//        else if (getboxstuat.equals("t")){
//            if (box_rbtn_transfer.isChecked()){
//                boxstuat ="中";
//            }else{
//                boxstuat ="普";
//            }
//        }
        else if (getboxstuat.equals("s")){
        if (box_rbtn_cardbox.isChecked()){
            boxstuat ="卡箱";
        }else if (box_rbtn_moneyboxs.isChecked()){
            boxstuat ="款箱";
        }else if (box_rbtn_Voucher.isChecked()){
            boxstuat ="凭证";
        }}

        return boxstuat;
    }

    /**
     * 发送指令
     */

    private void sendCmd() {
        // 给服务发送广播，内容为ylescort.ylmobileandroid.box

        if(CheckRadioButton()){
            Toast.makeText(getApplicationContext(),radiobutton,Toast.LENGTH_SHORT).show();
            return;}

        String scan = box_btn_scan.getText().toString();
        String activity = "ylescort.ylmobileandroid.box";
        Intent ac = new Intent();
        ac.setAction("ylescort.ylmobileandroid.Scan1DService");
        ac.putExtra("activity", activity);
        sendBroadcast(ac);
        Log.e(TAG, "send broadcast");

        if (box_swh_singleormore.isChecked()&scan.equals("扫描")){
            cmd = "toscan100ms";
            box_btn_scan.setText("停止");
        }else if(scan.equals("扫描")) {
            cmd = "scan";
        }else if (scan.equals("停止")){
            cmd = "stopscan";
            box_btn_scan.setText("扫描");
        }
        Intent sendToservice = new Intent(YLBoxScan.this, Scan1DService.class); // 用于发送指令
        sendToservice.putExtra("cmd", cmd);
        this.startService(sendToservice); // 发送指令
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.e("aaaaaaaaaaaaaaaaaaaaaa", keyCode+"");
        if( keyCode == KeyEvent.KEYCODE_HOME){
            if(!keyDownFlag){
                Log.e(TAG, "send cmd by HOME button************************************");
                sendCmd();   //发送指令到服务
                keyDownFlag = true;
            }
            return super.onKeyDown(keyCode, event);
        }
        if(keyCode  == KeyEvent.KEYCODE_BACK){
            Log.e(TAG, "KEY CODE BACK");
            if (box_btn_ent.getText().equals("确定")){
                LeaveDialog();
            }else{
                finish();
            }


        }
        return super.onKeyDown(keyCode, event);
    }

    private class FunkeyListener extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            boolean defaultdown=false;
            int keycode = intent.getIntExtra("keycode", 0);
            boolean keydown = intent.getBooleanExtra("keydown", defaultdown);
            //Log.i("ServiceDemo", "receiver:keycode="+keycode+"keydown="+keydown);

            //左侧下按键
            if(keycode == 133 && keydown){
                sendCmd();
            }
            //右侧按键
            if(keycode == 134 && keydown){
                sendCmd();
            }

            if(keycode == 131 && keydown){
//	        	Toast.makeText(getApplicationContext(), "这是F1按键", 0).show();
                box_rbtn_empty.setChecked(true);
                sendCmd();
            }

            if(keycode == 132 && keydown){
//	        	Toast.makeText(getApplicationContext(), "这是F2按键", 0).show();
                box_rbtn_full.setChecked(true);
                sendCmd();
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_box, menu);
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

            String box_btn_ent_text = box_btn_ent.getText().toString();
            Bundle bundle = new Bundle();
            bundle.putString("siteid",box_tv_titel.getTag().toString());
            bundle.putString("box_btn_ent_text",box_btn_ent_text);
            intent.putExtras(bundle);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(myBroad);
        Intent stopService = new Intent();
        stopService.setAction("ylescort.ylmobileandroid.Scan1DService");
        stopService.putExtra("stopflag", true);
        sendBroadcast(stopService);  //给服务发送广播,令服务停止
        Log.e(TAG, "send stop");
        super.onDestroy();
    }

    @Override
    protected void onPostResume() {
        //DisPlayBoxListView(box_tv_titel.getTag().toString());
        //BoxScanAdapter(ylTask.getLstBox());

        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.FUN_KEY");
        registerReceiver(funkeyReceive, filter);

        BoxScanAdapter(YLSystem.getEdiboxList());
        super.onPostResume();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(funkeyReceive);
        super.onStop();
    }
}