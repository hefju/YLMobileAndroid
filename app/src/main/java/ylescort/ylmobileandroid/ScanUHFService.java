package ylescort.ylmobileandroid;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

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

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        InitUHF();
    }

    private void InitUHF() {
        try {
            reader = UhfReader.getInstance();
            if (reader != null) {
                Thread.sleep(100);
                reader.setOutputPower(26);
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
                            String epcStr = Tools.Bytes2HexString(epc, epc.length);
//                            Intent serviceIntent = new Intent();
                            serviceIntent.setAction(activity);
//                            String newStr = epcStr.replaceAll("^(0+)", "");
                            serviceIntent.putExtra("result", epcStr);
                            sendBroadcast(serviceIntent);
                            Log.e(TAG,epcStr);
                        }
                    }
                    epcList = null ;
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String cmd_arr = intent.getStringExtra("cmd");
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
                    break;
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            String ac = intent.getStringExtra("activity");
            if(ac!=null)
                Log.e(YLSystem.getKimTag(), ac);
            activity = ac; // 获取activity
            if (intent.getBooleanExtra("stopflag", false))
                stopSelf(); // 收到停止服务信号
            Log.e(YLSystem.getKimTag(), intent.getBooleanExtra("stopflag", false)
                    + "");
        }
    }

    @Override
    public void onDestroy() {
        runFlag = false;
        unregisterReceiver(myReceive);
        super.onDestroy();
    }
}
