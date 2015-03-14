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
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import TaskClass.ArriveTime;
import TaskClass.BaseBox;
import TaskClass.Box;
import TaskClass.Site;
import TaskClass.TasksManager;
import TaskClass.YLTask;
import YLDataService.BaseBoxDBSer;
import YLSystem.YLSystem;
import adapter.YLBoxAdapter;


public class box extends ActionBarActivity {

    private TextView box_tv_titel;
    private ListView listView;
    private String cmd = "scan";
    private FileOutputStream fos;
    private MyBroadcast myBroad;  //广播接收者
    private String activity = "ylescort.ylmobileandroid.box";
    public String TAG = "MainActivity";  //Debug
    private MediaPlayer mPlayer;  //媒体播放者，用于播放提示音
    private boolean keyDownFlag = false;

//    private Switch box_swh_TradeAction; //收送
//    private Switch box_swh_Status; //空实
    private Switch box_swh_singleormore;//单多
//    private Switch box_swh_BoxTaskType;//普通中调

    private Spinner box_sp_stype;//交接类型

//    private RadioButton box_rbtn_general;//普通箱
//    private TextView box_tv_general;//普通箱统计
//    private RadioButton box_rbtn_transfer;//中调箱
//    private TextView box_tv_transfer;//中调箱统计
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

    private List<Box> yltaskboxList;//内存内款数据
    private List<Box> ScanboxList;//扫描数据
    private ArriveTime arriveTime;


    private TasksManager tasksManager = null;//任务管理类
    private YLTask ylTask;//当前选中的任务

    private BaseBoxDBSer baseBoxDBSer;
    private BaseBox baseBox;

    private String radiobutton = "";

    private FunkeyListener funkeyReceive; //功能键广播接收者

    private String box_sp_text ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_box);
        box.this.setTitle("款箱操作: "+YLSystem.getUser().getName());
        tasksManager= YLSystem.getTasksManager();//获取任务管理类
        ylTask=tasksManager.CurrentTask;//当前选中的任务

        try {
            LoadData();
            init();
            KeyBroad();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void KeyBroad() {

        funkeyReceive  = new FunkeyListener();
        //代码注册功能键广播接收者
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.FUN_KEY");
        registerReceiver(funkeyReceive, filter);
    }

    public void LoadData() throws ClassNotFoundException {

        baseBoxDBSer = new BaseBoxDBSer(getApplicationContext());
        baseBox = new BaseBox();

        listView = (ListView) findViewById(R.id.boxlistview);
        box_tv_titel = (TextView)findViewById(R.id.box_tv_title);
        box_swh_singleormore = (Switch)findViewById(R.id.box_swh_singleormore);

        box_sp_stype = (Spinner)findViewById(R.id.box_sp_stype);

//        box_rbtn_general = (RadioButton)findViewById(R.id.box_rbtn_general);
//        box_tv_general = (TextView)findViewById(R.id.box_tv_general);
//        box_rbtn_transfer = (RadioButton)findViewById(R.id.box_rbtn_transfer);
//        box_tv_transfer = (TextView)findViewById(R.id.box_tv_transfer);
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

//        String[] m={"早送","晚收","区内中调","跨区中调","库内区内中调","库内跨区中调","夜间周转","人行"};
//
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,m);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//        box_sp_stype.setAdapter(adapter);
//        box_sp_stype.setPrompt("交接类型");

        ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(this,R.array.tasktype
                ,android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        box_sp_stype.setAdapter(arrayAdapter);
        box_sp_stype.setPrompt("交接类型");
       box_sp_stype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//               Toast.makeText(getApplicationContext(),
//                       parent.getItemAtPosition(position).toString(),Toast.LENGTH_SHORT).show();
               box_sp_text = parent.getItemAtPosition(position).toString();
           }

           @Override
           public void onNothingSelected(AdapterView<?> parent) {

           }
       });

        String tasktype = ylTask.getTaskType();
        if (tasktype.equals("早送")){
            box_sp_stype.setSelection(0);
        }else {
            box_sp_stype.setSelection(1);
        }

        Bundle bundle = this.getIntent().getExtras();
        String SiteName = bundle.getString("sitename");
        String SiteID = bundle.getString("siteid");
        box_tv_titel.setText(SiteName);
        box_tv_titel.setTag(SiteID);

        DisPlayBoxListView(SiteID);

    }

    private void DisPlayBoxListView(String siteID) {
        yltaskboxList =new ArrayList<>();
        if (ylTask.lstBox != null && ylTask.lstBox.size()>0){
            for (int i = 0 ; i < ylTask.lstBox.size();i++){
                if (ylTask.lstBox.get(i).getSiteID().equals(siteID)){
                    Box box = new Box();
                    box = ylTask.lstBox.get(i);
                    yltaskboxList.add(box);
                }
            }
        }
        adapterbox(yltaskboxList);
        TallyBox(yltaskboxList);//统计数据
    }

    private void adapterbox(List<Box> adapterboxlist){
        if (adapterboxlist !=null){
            YLBoxAdapter ylBoxAdapter = new YLBoxAdapter(this, adapterboxlist, R.layout.activity_boxlist);
            listView.setAdapter(ylBoxAdapter);
        }
    }

    private void init(){
       Log.e(TAG, "on start");
        myBroad = new MyBroadcast();
        IntentFilter filter = new IntentFilter();
        filter.addAction("ylescort.ylmobileandroid.box");
        registerReceiver(myBroad, filter);
        Log.e(TAG, "register Receiver");
        //启动服务
        Intent start = new Intent(box.this, Scan1DService.class);
        box.this.startService(start);

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
                fos = box.this.openFileOutput("count.txt", Context.MODE_PRIVATE);
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
            mPlayer = MediaPlayer.create(box.this, R.raw.msg);
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
        //mPlayer.start();
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

//        if (!box_rbtn_general.isChecked()&!box_rbtn_transfer.isChecked()){
//            radiobutton = "类型未选";
//            return true;}
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
                            //Toast.makeText(getApplicationContext(), input, Toast.LENGTH_SHORT).show();
                        }
                    }
                }).setNegativeButton("取消", null).show();

    }

    public void boxlistent(View view){
        String Entbtn =  box_btn_ent.getText().toString();
        if (Entbtn.equals("确定")){
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
            this.finish();
        }else{
            dialog();
        }
    }

    protected void dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(box.this);
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
                adapterbox(ScanboxList);

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
/*
扫描数据录入
 */

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
        listView.setAdapter(ylBoxAdapter);

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
        listView.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                listView.setSelection(ScanboxList.size() - 1);
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
        //cmd = "scan";
        Intent sendToservice = new Intent(box.this, Scan1DService.class); // 用于发送指令
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
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    private class FunkeyListener extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            boolean defaultdown=false;
            int keycode = intent.getIntExtra("keycode", 0);
            boolean keydown = intent.getBooleanExtra("keydown", defaultdown);
            Log.i("ServiceDemo", "receiver:keycode="+keycode+"keydown="+keydown);

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
                sendCmd();
            }

            if(keycode == 132 && keydown){
//	        	Toast.makeText(getApplicationContext(), "这是F2按键", 0).show();
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

            Bundle bundle = new Bundle();
            bundle.putString("siteid",box_tv_titel.getTag().toString());
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
        unregisterReceiver(funkeyReceive);
        super.onDestroy();
    }

    @Override
    protected void onPostResume() {
        //DisPlayBoxListView(box_tv_titel.getTag().toString());
        //adapterbox(ylTask.getLstBox());
        adapterbox(YLSystem.getEdiboxList());
        super.onPostResume();
    }
}
