package ylescort.ylmobileandroid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.hdhe.nfc.NFCcmdManager;
import com.android.hdhe.uhf.reader.Tools;

import java.io.FileOutputStream;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import TaskClass.Box;
import TaskClass.TasksManager;
import TaskClass.YLTask;
import YLSystem.YLSystem;
import ylescort.ylmobileandroid.R;

public class YLATMList extends ActionBarActivity {

    private Button ATMlist_btn_Start;//开始按钮
    private Button ATMlist_btn_End;//结束按钮
    private Button ATMlist_btn_Addatm;//添加按钮
    private Button ATMlist_btn_UpDateatm;//上传按钮

    private TextView ATMlist_tv_title;//标题

    private ListView ATMlist_listview;//列表

    private List<YLSite> ylSiteList;//网点列表

    private TasksManager tasksManager = null;//任务管理类
    private YLTask ylTask;//当前选中的任务
    private Scan1DRecive ATMscan1DRecive;  //广播接收者
    private FileOutputStream fos;

    private NFCcmdManager HFmanager ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ylatmlist);
        InitLayout();
        InitData();
        InitHFreader();
        InitReciveScan1D();
    }

    private void InitHFreader() {
        try{
            HFmanager = NFCcmdManager.getNFCcmdManager(13, 115200, 0);
            HFmanager.readerPowerOn();
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"HF初始化失败",Toast.LENGTH_SHORT).show();
        }
    }

    private void InitReciveScan1D() {
        ATMscan1DRecive = new Scan1DRecive();
        IntentFilter filter = new IntentFilter();
        filter.addAction("ylescort.ylmobileandroid.YLATMList");
        registerReceiver(ATMscan1DRecive,filter);
        Intent start = new Intent(YLATMList.this,Scan1DService.class);
        YLATMList.this.startService(start);
    }

    private class Scan1DRecive extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String recivedata = intent.getStringExtra("result");
            if (recivedata != null){
                Toast.makeText(getApplicationContext(),replaceBlank(recivedata),Toast.LENGTH_SHORT).show();
            }
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

    private void SendScan1Dcmd(){
        String activity = "ylescort.ylmobileandroid.YLATMList";
        Intent ac = new Intent();
        ac.setAction("ylescort.ylmobileandroid.Scan1DService");
        ac.putExtra("activity", activity);
        sendBroadcast(ac);
        Intent sendToservice = new Intent(YLATMList.this, Scan1DService.class); // 用于发送指令

        sendToservice.putExtra("cmd", "scan");

        this.startService(sendToservice); // 发送指令
    }

    public void ATMScan1D(View view){
        SendScan1Dcmd();
    }

    public void ATMListHFreaderonClick(View view) {
        ATMListHFreader();
    }

    private void ATMListHFreader() {
        byte[] uid ;
        HFmanager.init_14443A();
        uid = HFmanager.inventory_14443A();
        if (uid != null){
            Toast.makeText(getApplicationContext(),
                    Tools.Bytes2HexString(uid, uid.length), Toast.LENGTH_SHORT).show();
        }
    }

    private void InitLayout() {
        ATMlist_btn_Start = (Button) findViewById(R.id.ATMlist_btn_Start);
        ATMlist_btn_End = (Button) findViewById(R.id.ATMlist_btn_End);
        ATMlist_btn_Addatm = (Button) findViewById(R.id.ATMlist_btn_Addatm);
        ATMlist_btn_UpDateatm = (Button) findViewById(R.id.ATMlist_btn_UpDateatm);

        ATMlist_tv_title = (TextView) findViewById(R.id.ATMlist_tv_title);
        ATMlist_listview = (ListView) findViewById(R.id.ATMlist_listview);
    }

    private void InitData() {
        tasksManager= YLSystem.getTasksManager();//获取任务管理类
        ylTask=tasksManager.CurrentTask;//当前选中的任务
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 131){
            SendScan1Dcmd();}
        else if (keyCode == 133){
            ATMListHFreader();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ylatmlist, menu);
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

    @Override
    protected void onDestroy() {
        unregisterReceiver(ATMscan1DRecive);
        Intent stopService = new Intent();
        stopService.setAction("ylescort.ylmobileandroid.Scan1DService");
        stopService.putExtra("stopflag", true);
        sendBroadcast(stopService);
        super.onDestroy();
    }
}
