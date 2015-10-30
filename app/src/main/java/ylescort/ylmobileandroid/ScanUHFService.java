package ylescort.ylmobileandroid;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import com.android.hdhe.uhf.reader.SerialPort;
import com.android.hdhe.uhf.reader.Tools;
import com.android.hdhe.uhf.reader.UhfReader;

import java.util.List;

import YLSystemDate.YLSystem;

/**
 * Created by Administrator on 2015-07-10.
 */
public class ScanUHFService extends Service {

    private boolean runFlag = true;
    private boolean startFlag = false;
    private UhfReader reader ; //超高频读写器

    private String TAG = "kim";
    public String activity =null;
    private MyReceiver myReceive;
    private Intent serviceIntent;
    private SerialPort UHFSerialPort;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        InitUHF();
        super.onCreate();
    }

    private void InitUHF() {
        try {
            reader = UhfReader.getInstance();
//            UHFSerialPort = new SerialPort(12,115200,0);
//            reader.powerOn();
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences
                    (this);
            String UHFpower = prefs.getString("UHFPower", "");
            if(UHFpower.equals("")){
                UHFpower = "22";}
            Log.e(YLSystem.getKimTag(),UHFpower+"获取功率");
            Integer uhf= Integer.parseInt(UHFpower);
            if (reader != null) {
                Thread.sleep(100);
                reader.setOutputPower(uhf);
                Thread thread = new InventoryThread();
                thread.start();
                Log.e(YLSystem.getKimTag(), "uhf初始化成功");
            } else {
                Log.e(YLSystem.getKimTag(), "uhf初始化不成功");
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        myReceive = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("ylescort.ylmobileandroid.ScanUHFService");
        registerReceiver(myReceive, filter);

        serviceIntent = new Intent();

    }

    private class InventoryThread extends Thread{
        private List<byte[]> epcList;
        @Override
        public void run() {
            super.run();
            while(runFlag){
                if(startFlag){
//					reader.stopInventoryMulti()
                    epcList = reader.inventoryRealTime(); //实时盘存
                    if(epcList != null && !epcList.isEmpty()){
                        //播放提示音
//                        Util.play(1, 0);
                        for(byte[] epc:epcList){
//                            String epcStr = Tools.Bytes2HexString(epc, epc.length).replaceAll("^(0+)", "");
                            String epcStr = Tools.Bytes2HexString(epc, epc.length).substring(0, 10);
//                            Intent serviceIntent = new Intent();
                            serviceIntent.setAction(activity);
//                            String newStr = epcStr.replaceAll("^(0+)", "");
                            serviceIntent.putExtra("result", "UHF"+epcStr);
                            sendBroadcast(serviceIntent);
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
//                            Log.e(TAG,epcStr+"read");
                        }
                    }
                    epcList = null ;
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String cmd_arr = "";
        try{
            cmd_arr = intent.getStringExtra("cmd");
        }catch (Exception e){
            e.printStackTrace();
            return 0 ;
        }
        if (cmd_arr !=null){
            Log.e(YLSystem.getKimTag(),cmd_arr);
            switch (cmd_arr) {
                case "scan":
                    if (startFlag) {
                        startFlag = false;
                    } else {
                        startFlag = true;
                    }
                    break;
                case "stopscan":
                    startFlag = false;
//                    reader.powerOff();
                    break;
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String ac = intent.getStringExtra("activity");
            if(ac!=null)
                Log.e(YLSystem.getKimTag(), ac);
            activity = ac; // 获取activity
            if (intent.getBooleanExtra("stopflag", false))
//                stopSelf(); // 收到停止服务信号
                runFlag = false;
            Log.e(YLSystem.getKimTag(), intent.getBooleanExtra("stopflag", false)
                    + "");
        }
    }

    @Override
    public void onDestroy() {
        runFlag = false;
        unregisterReceiver(myReceive);
        Log.e(YLSystem.getKimTag(),"stopserver");
        super.onDestroy();
    }
}
