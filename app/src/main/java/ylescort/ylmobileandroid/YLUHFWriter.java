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
import android.widget.TextView;

import com.android.hdhe.uhf.reader.Tools;
import com.android.hdhe.uhf.reader.UhfReader;

import java.util.List;

import TaskClass.Box;
import YLDataService.YLBoxScanCheck;
import YLSystemDate.YLMediaPlayer;
import YLSystemDate.YLSystem;


public class YLUHFWriter extends ActionBarActivity implements View.OnClickListener {

    private TextView yluhfwriter_tv_boxname;
    private TextView yluhfwriter_tv_boxid;
    private TextView yluhfwriter_tv_uhfno;
    private Button yluhfwriter_btn_1dread;
    private Button yluhfwriter_btn_uhfread;
    private Button yluhfwriter_btn_uhfwrite;
    private ScanRecive scan1dRecive;
    private UhfReader reader;
    private YLMediaPlayer ylMediaPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yluhfwriter);
        InitView();
        InitData();
        InitRecive();
        InitUHF();
    }

    private void InitData() {
        ylMediaPlayer = new YLMediaPlayer();
    }

    private void InitUHF() {
        reader = UhfReader.getInstance();
        reader.setOutputPower(22);
    }

    private void InitView() {

        yluhfwriter_tv_boxname = (TextView)findViewById(R.id.yluhfwriter_tv_boxname);
        yluhfwriter_tv_boxid = (TextView)findViewById(R.id.yluhfwriter_tv_boxid);
        yluhfwriter_tv_uhfno = (TextView)findViewById(R.id.yluhfwriter_tv_uhfno);
        yluhfwriter_btn_1dread = (Button)findViewById(R.id.yluhfwriter_btn_1dread);
        yluhfwriter_btn_uhfread = (Button)findViewById(R.id.yluhfwriter_btn_uhfread);
        yluhfwriter_btn_uhfwrite = (Button)findViewById(R.id.yluhfwriter_btn_uhfwrite);

        yluhfwriter_btn_1dread.setOnClickListener(this);
        yluhfwriter_btn_uhfread.setOnClickListener(this);
        yluhfwriter_btn_uhfwrite.setOnClickListener(this);
    }

    private void InitRecive() {
        scan1dRecive = new ScanRecive();
        IntentFilter filter = new IntentFilter();
        filter.addAction("ylescort.ylmobileandroid.YLUHFWriter");
        registerReceiver(scan1dRecive, filter);
        Intent start = new Intent(YLUHFWriter.this,Scan1DService.class);
        YLUHFWriter.this.startService(start);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.yluhfwriter_btn_1dread:Scan1DCmd("scan");
                break;
            case R.id.yluhfwriter_btn_uhfread:ScanUHF();
                break;
            case R.id.yluhfwriter_btn_uhfwrite:WriterUHF();
                break;
        }
    }

    private void WriterUHF() {
        String Passowrd = "00000000";
        byte[]password =Tools.HexString2Bytes(Passowrd);
        int memBank = 1;
        int StartAddr = 2;
        String boxid = yluhfwriter_tv_boxid.getText().toString();
        byte[] data = Tools.HexString2Bytes(boxid);
        boolean write =  reader.writeTo6C(password,memBank,StartAddr,data.length,data);
        if (write){
            ylMediaPlayer.SuccessOrFailMidia("success",getApplicationContext());
        }else {
            ylMediaPlayer.SuccessOrFailMidia("fail",getApplicationContext());
        }
    }

    private void ScanUHF() {
        List<byte[]> epcList;
        epcList = reader.inventoryRealTime();
        if (epcList != null && !epcList.isEmpty()){
            for(byte[] epc:epcList){
                String epcStr = Tools.Bytes2HexString(epc, epc.length).substring(0,10);
                yluhfwriter_tv_uhfno.setText(epcStr);
                ylMediaPlayer.SuccessOrFailMidia("success",getApplicationContext());
            }
        }
        epcList = null ;
    }


    private class ScanRecive extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String recivedata = intent.getStringExtra("result");
            if (recivedata !=null){
                Box box= YLBoxScanCheck.CheckBoxbyUHF(recivedata, getApplicationContext());
                if (box.getBoxName().equals("无数据"))return;
                yluhfwriter_tv_boxname.setText(box.getBoxName());
                yluhfwriter_tv_boxid.setText(box.getBoxID());
                ylMediaPlayer.SuccessOrFailMidia("success",getApplicationContext());
            }
        }
    }

    private void Scan1DCmd(String cmd) {
        String activity = "ylescort.ylmobileandroid.YLUHFWriter";
        Intent ac = new Intent();
        ac.setAction("ylescort.ylmobileandroid.Scan1DService");
        ac.putExtra("activity", activity);
        sendBroadcast(ac);
        Intent sendToservice = new Intent(YLUHFWriter.this, Scan1DService.class); // 用于发送指令
        sendToservice.putExtra("cmd", cmd);
        this.startService(sendToservice); // 发送指令
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode){
            case 131:Scan1DCmd("scan");
                break;
            case 132:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_yluhfwriter, menu);
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
