package ylescort.ylmobileandroid;

import android.content.BroadcastReceiver;
import android.content.Context;
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
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;


public class box extends ActionBarActivity {

    private ListView listView;
    private String cmd = "scan";
    private List<Map<String, Object>> mlist;
    //用于保存扫描次数
    private FileOutputStream fos;

    private MyBroadcast myBroad;  //广播接收者

    private String activity = "ylescort.ylmobileandroid.box";
    //    ylescort.ylmobileandroid.Scan1DService
    public String TAG = "MainActivity";  //Debug
    private MediaPlayer mPlayer;  //媒体播放者，用于播放提示音

    private boolean keyDownFlag = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_box);
        listView = (ListView) findViewById(R.id.boxlistview);
        try {
            LoadData();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        init();
    }

    public void LoadData() throws ClassNotFoundException {

        // listView = (ListView)findViewById(R.id.Task_listView);

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
                mlist = new ArrayList<Map<String,Object>>();
                //对数据进行排序
                //list_goods = sortAndadd(list_goods, receivedata);

               String sql[] = receivedata.split("\\\\");
               receivedata = sql[0];

                Toast.makeText(getApplicationContext(), receivedata, Toast.LENGTH_LONG).show();
                // String  allcount = list_goods.get(0).getCount()+"";
                //写到固定的文件中
//                try {
//                    fos.write(allcount.getBytes());
//                } catch (IOException e1) {
//                    // TODO Auto-generated catch block
//                    e1.printStackTrace();
//                }
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
