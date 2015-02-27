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

import TaskClass.YLTask;


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

    private Switch box_swh_getorgive; //收送
    private Switch box_swh_emplyorfull; //空实
    private Switch box_swh_singleormore;//单多

    private RadioButton  box_rbtn_moneyboxs;//款箱
    private RadioButton  box_rbtn_cardbox;//卡箱
    private RadioButton  box_rbtn_Voucher;//凭证
    private ArrayList<HashMap<String, Object>> listItem ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_box);

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

        box_swh_getorgive = (Switch)findViewById(R.id.box_swh_getorgive);
        box_swh_emplyorfull = (Switch)findViewById(R.id.box_swh_emplyorfull);
        box_swh_singleormore = (Switch)findViewById(R.id.box_swh_singleormore);

        box_rbtn_moneyboxs = (RadioButton)findViewById(R.id.box_rbtn_moneyboxs);
        box_rbtn_cardbox = (RadioButton)findViewById(R.id.box_rbtn_cardbox);
        box_rbtn_Voucher = (RadioButton)findViewById(R.id.box_rbtn_Voucher);

        listItem =  new ArrayList<>();
        // listView = (ListView)findViewById(R.id.Task_listView);
/*
        //生成动态数组，加入数据
        ArrayList<HashMap<String, Object>> listItem = new ArrayList<>();

        for(int i=0;i<10;i++)
        {
            HashMap<String, Object> map = new HashMap<>();
            map.put("任务名称", "stateLoad "+i);
            map.put("任务类型", "Stype "+i);
            map.put("任务状态", "state");
            listItem.add(map);
        }
        //生成适配器的Item和动态数组对应的元素
        SimpleAdapter listItemAdapter = new SimpleAdapter(this,listItem,//数据源
                R.layout.activity_taskitem,//ListItem的XML实现
                //动态数组与ImageItem对应的子项
                new String[] {"任务名称","任务类型", "任务状态"},
                //ImageItem的XML文件里面的一个ImageView,两个TextView ID
                new int[] {R.id.Task_taskname,R.id.Task_taskstype,R.id.Task_taskstaut}
        );

        //添加并且显示
        listView.setAdapter(listItemAdapter);
        */

        Bundle bundle = this.getIntent().getExtras();
        String SiteName = bundle.getString("SiteName");
        box_tv_titel.setText(SiteName);


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
                //mlist = new ArrayList<Map<String,Object>>();
                //Toast.makeText(getApplicationContext(), receivedata, Toast.LENGTH_LONG).show();
                PutDatatoListView(receivedata);

                try {
                    fos.close();
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                int count = 1;
//                for(Goods goods:list_goods ){
//                    Map<String, Object> map = new HashMap<String, Object>();
//                    map.put("barcodeID", count);
//                    map.put("barcode", goods.getBarcode());
//                    map.put("count", goods.getCount());
//                    count++;
//                    mlist.add(map);
            }

//                mListview.setAdapter(new SimpleAdapter(MainActivity.this,
//                        mlist, R.layout.listview_item, new String[] {
//                        "barcodeID","barcode", "count" }, new int[] {
//                        R.id.barcodeID_item,R.id.barcode_item, R.id.count_item }));
//                if(mPlayer != null){
//                    if(mPlayer.isPlaying())
//                        return;
//                }
//                //媒体播放
//                mPlayer = new MediaPlayer();
//                try {
//                    mPlayer.setDataSource("/system/media/audio/ui/VideoRecord.ogg");  //选用系统声音文件
//                    mPlayer.prepare();
//                } catch (IllegalArgumentException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                } catch (SecurityException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                } catch (IllegalStateException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//                mPlayer.start();
////				Selection.setSelection(receive_data.getEditableText(), 0);  //让光标保持在最前面
//            }
        }

    }

    public void ScanOnClick (View view ) throws ClassNotFoundException{
        sendCmd();   //发送指令到服务
    }

    public void NoLableIns(View view){
        /*
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
                            Toast.makeText(getApplicationContext(), input, Toast.LENGTH_SHORT).show();
                        }
                    }
                }).setNegativeButton("取消", null).show();
                */
        Toast.makeText(getApplicationContext(), GetBoxStuat("s"), Toast.LENGTH_SHORT).show();
    }


    private void InsNumFromScan(String Number){
        String boxstaut = GetBoxStuat("g");


    }


    private void PutDatatoListView(String boxnumber){

        //生成动态数组，加入数据
        //ArrayList<HashMap<String, Object>> listItem = new ArrayList<>();

//        for(int i=0;i<10;i++)
//        {
//            HashMap<String, Object> map = new HashMap<>();
//            map.put("任务名称", "stateLoad "+i);
//            map.put("任务类型", "Stype "+i);
//            map.put("任务状态", "state");
//            listItem.add(map);
//        }

        if (CheckBoxNumber(boxnumber)){return;}

        int count = listItem.size();

        HashMap<String, Object> map = new HashMap<>();
        map.put("序号",count+1);
        map.put("箱编号",boxnumber);
        map.put("收/送",GetBoxStuat("g"));
        map.put("空/实",GetBoxStuat("f"));
        map.put("箱类型",GetBoxStuat("s"));
        listItem.add(map);

//        ArrayList<HashMap<String, Object>> newlistItem = new ArrayList<>();
//
//            for (int i = listItem.size(); i >= 0; i--) {
//                Map  listitemmap = listItem.get(i);
//                HashMap<String, Object> newmap = new HashMap<>();
//                newmap.put("序号",listitemmap.get("序号"));
//                newmap.put("箱编号",listitemmap.get("箱编号"));
//                newmap.put("收/送",listitemmap.get("收/送"));
//                newmap.put("空/实",listitemmap.get("空/实"));
//                newmap.put("箱类型",listitemmap.get("箱类型"));
//
//                newlistItem.add(newmap);
//            }


        //生成适配器的Item和动态数组对应的元素
        SimpleAdapter listItemAdapter = new SimpleAdapter(this,listItem,//数据源
                R.layout.activity_boxlist,//ListItem的XML实现
                //动态数组与ImageItem对应的子项
                new String[] {"序号","箱编号", "收/送","空/实","箱类型"},
                //ImageItem的XML文件里面的一个ImageView,两个TextView ID
                new int[] {R.id.boxlv_tv_order,R.id.boxlv_tv_Number,R.id.boxlv_tv_getorgive,
                        R.id.boxlv_tv_emporfull,R.id.boxlv_tv_staut}
        );

        //添加并且显示
        listView.setAdapter(listItemAdapter);

    }

    private boolean CheckBoxNumber(String boxnumber) {

        boolean checkthebox = false;
        for (int i = 0 ;i<listItem.size();i++){
            Map lvNumber = listItem.get(i);
            if (lvNumber.get("箱编号").equals(boxnumber)){
                checkthebox = true;
                break;
            }
            else {checkthebox = false;}
        }
       return checkthebox;
    }


    private String GetBoxStuat(String getboxstuat ){
        String boxstuat = "";
        if (getboxstuat.equals("g")){
        if (box_swh_getorgive.isChecked()){
            boxstuat = "送";
        }else {
            boxstuat = "收";
        }}
        else if (getboxstuat.equals("f")){
        if (box_swh_emplyorfull.isChecked()){
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
