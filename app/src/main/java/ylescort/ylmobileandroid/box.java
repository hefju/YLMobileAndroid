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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.SortedMap;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import TaskClass.Box;
import TaskClass.Site;
import TaskClass.TasksManager;
import TaskClass.User;
import TaskClass.YLTask;
import YLSystem.YLSystem;
import adapter.YLBoxAdapter;


public class box extends ActionBarActivity {

    private TextView box_tv_titel;
    private ListView listView;
    private String cmd = "scan";
    private List<Map<String, Object>> mlist;
    //用于保存扫描次数
    private FileOutputStream fos;
    private MyBroadcast myBroad;  //广播接收者
    private String activity = "ylescort.ylmobileandroid.box";
    public String TAG = "MainActivity";  //Debug
    private MediaPlayer mPlayer;  //媒体播放者，用于播放提示音
    private boolean keyDownFlag = false;

    private Switch box_swh_TradeAction; //收送
    private Switch box_swh_Status; //空实
    private Switch box_swh_singleormore;//单多

    private RadioButton  box_rbtn_moneyboxs;//款箱
    private RadioButton  box_rbtn_cardbox;//卡箱
    private RadioButton  box_rbtn_Voucher;//凭证

    private Button box_btn_ent;//确认

    private List<Box> boxList;

    private TasksManager tasksManager = null;//任务管理类
    private YLTask ylTask;//当前选中的任务

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_box);
        tasksManager= YLSystem.getTasksManager();//获取任务管理类
        ylTask=tasksManager.CurrentTask;//当前选中的任务

        try {
            LoadData();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        init();
    }

    public void LoadData() throws ClassNotFoundException {

        listView = (ListView) findViewById(R.id.boxlistview);
        box_tv_titel = (TextView)findViewById(R.id.box_tv_title);

        box_swh_TradeAction = (Switch)findViewById(R.id.box_swh_TradeAction);
        box_swh_Status = (Switch)findViewById(R.id.box_swh_Status);
        box_swh_singleormore = (Switch)findViewById(R.id.box_swh_singleormore);

        box_rbtn_moneyboxs = (RadioButton)findViewById(R.id.box_rbtn_moneyboxs);
        box_rbtn_cardbox = (RadioButton)findViewById(R.id.box_rbtn_cardbox);
        box_rbtn_Voucher = (RadioButton)findViewById(R.id.box_rbtn_Voucher);

        box_btn_ent = (Button)findViewById(R.id.box_btn_ent);

        boxList =new ArrayList<>();

        Bundle bundle = this.getIntent().getExtras();
        String SiteName = bundle.getString("sitename");
        String SiteID = bundle.getString("siteid");
        box_tv_titel.setText(SiteName);
        box_tv_titel.setTag(SiteID);

        if (ylTask.lstBox != null && !ylTask.lstBox.isEmpty()){
            for (int i = 0 ; i < ylTask.lstBox.size();i++){
                if (ylTask.lstBox.get(i).getSiteID().equals(SiteID)){
                    Box box = new Box();
                    box = ylTask.lstBox.get(i);
                    boxList.add(box);
                }
            }
        }
        if (boxList.size()!=0){
            YLBoxAdapter ylBoxAdapter = new YLBoxAdapter(this,boxList,R.layout.activity_boxlist);
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
                //媒体播放
                mPlayer = new MediaPlayer();
                try {
                    mPlayer.setDataSource("/system/media/audio/ui/VideoRecord.ogg");  //选用系统声音文件
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
                //mPlayer.start();
				//Selection.setSelection(receive_data.getEditableText(), 0);  //让光标保持在最前面
            }


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

    public void ScanOnClick (View view ) throws ClassNotFoundException{
        sendCmd();   //发送指令到服务
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
                            Toast.makeText(getApplicationContext(), input, Toast.LENGTH_SHORT).show();
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
            for (int i = 0 ;i < boxList.size();i++){
                Box box = new Box();
                box = boxList.get(i);
                ylTask.lstBox.add(box);
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

    private void PutDatatoListView(String boxnumber,String boxcount){

        if (CheckBoxNumber(boxnumber)){return;}
        String time="19:10";
        Box box = new Box();
        int count= boxList.size();
        box.setSiteID(box_tv_titel.getTag().toString());
        box.setBoxOrder(count + 1 + "");
        box.setBoxID(boxnumber);
        box.setTradeAction(GetBoxStuat("g"));
        box.setBoxStatus(GetBoxStuat("f"));
        box.setBoxType(GetBoxStuat("s"));
        box.setBoxCount(boxcount);
        box.setTimeID(1);
        box.setActionTime(time+"");
        boxList.add(box);
        YLBoxAdapter ylBoxAdapter = new YLBoxAdapter(this,boxList,R.layout.activity_boxlist);
        listView.setAdapter(ylBoxAdapter);
        scrollMyListViewToBottom();
    }

    private void scrollMyListViewToBottom() {
        listView.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                listView.setSelection(boxList.size() - 1);
            }
        });
    }

    private boolean CheckBoxNumber(String boxnumber) {

        boolean checkthebox = false;
        for (int i = 0 ; i<boxList.size();i++){
            String boxid = boxList.get(i).BoxID;
            if (boxid.equals(boxnumber) &!boxid.equals("无标签")){
                checkthebox = true;
                break;
            }else {
                checkthebox = false;
            }
        }
       return checkthebox;
    }

    private String GetBoxStuat(String getboxstuat ){
        String boxstuat = "";
        if (getboxstuat.equals("g")){
        if (box_swh_TradeAction.isChecked()){
            boxstuat = "送";
        }else {
            boxstuat = "收";
        }}
        else if (getboxstuat.equals("f")){
        if (box_swh_Status.isChecked()){
            boxstuat ="实";
        }else {
            boxstuat ="空";
        }}
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
        // 给服务发送广播，内容为com.example.scandemo.MainActivity
        Intent ac = new Intent();
        ac.setAction("ylescort.ylmobileandroid.Scan1DService");
        ac.putExtra("activity", activity);
        sendBroadcast(ac);
        Log.e(TAG, "send broadcast");

        if (box_swh_singleormore.isChecked()){
            cmd = "toscan100ms";
        }else {
            cmd = "scan";
        }
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
        super.onDestroy();
    }
}
