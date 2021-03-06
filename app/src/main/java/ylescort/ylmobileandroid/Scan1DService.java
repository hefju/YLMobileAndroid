package ylescort.ylmobileandroid;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import com.example.scandemo.SerialPort;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2015/2/10.
 */
public class Scan1DService extends Service {

    private SerialPort mSerialPort;
    private InputStream mInputStream;
    private OutputStream mOutputStream;
    private MyReceiver myReceive;
    private ReadThread mReadThread;
    private Timer sendData;
    private Timer scan100ms;
    public String activity =null; //"ylescort.ylmobileandroid.box";
    public String data;
    public StringBuffer data_buffer = new StringBuffer();  //接收数据缓冲
    private boolean run = true;  //线程标识
    private boolean run_scan100ms = false;  //每100ms扫描标识
    private Timer timeout = null;
    public String TAG = "ScanService";  //Debug

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        //初始化
        init();
    }

    private void init(){
        Log.e("service on create", "service on create");
        try {
            mSerialPort = new SerialPort(0, 9600, 0);// scaner
        } catch (Exception e) {
            e.printStackTrace();
        }
        mSerialPort.scaner_poweron();
        Log.e(TAG, "scan power on");
        mOutputStream = mSerialPort.getOutputStream();
        mInputStream = mSerialPort.getInputStream();

        myReceive = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("ylescort.ylmobileandroid.Scan1DService");
        registerReceiver(myReceive, filter);
        // 注册Broadcast Receiver，用于关闭Service

		sendData = new Timer();
        scan100ms = new Timer();
		/* Create a receiving thread */
        mReadThread = new ReadThread();
        mReadThread.start(); // 开启读线程
        Log.e(TAG, "start thread");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "start command");
        String cmd_arr = "";
        try{
            cmd_arr = intent.getStringExtra("cmd");
        }catch (Exception e){
            e.printStackTrace();
            return 0 ;
        }

//		Log.e(TAG, cmd_arr);
        if (cmd_arr == null)
            return 0; // 没收到命令直接返回
        Log.e("CMD", cmd_arr);

        if("scan".equals(cmd_arr)){
            if(timeout != null){
                timeout.cancel();
            }
            scan100ms.cancel();   //取消Timer任务
            run_scan100ms = false;
            if(mSerialPort.scaner_trig_stat()){
                mSerialPort.scaner_trigoff();
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            mSerialPort.scaner_trigon();  //触发扫描

            if(timeout != null){
                timeout.cancel();
                timeout = null;
                return 0;
            }
            timeout = new Timer();
            timeout.schedule(new TimerTask() {

                @Override
                public void run() {
                    mSerialPort.scaner_trigoff(); //设置5s超时
                    timeout = null;
                }
            }, 5000);
            Log.e(TAG, "start scan");
        }else if("toscan100ms".equals(cmd_arr)){
            if(run_scan100ms) return 0;
            run_scan100ms = true;
            scan100ms.cancel();
            scan100ms = new Timer();
            scan100ms.schedule(new TimerTask() {
                @Override
                public void run() {
                    if(mSerialPort.scaner_trig_stat()){//判断是否正在开红外
                        mSerialPort.scaner_trigoff();  //关红外读头
                        try{
                            Thread.sleep(50);
                        }catch (InterruptedException e)
                        {
                            e.printStackTrace();
                        }
                    }
                    mSerialPort.scaner_trigon();  //触发读卡
                }
            }, 0, 500);
        }else if ("stopscan".equals(cmd_arr)){
//            if(timeout != null){
//                timeout.cancel();
//            }
            scan100ms.cancel();   //取消Timer任务
            run_scan100ms = false;
            mSerialPort.scaner_trigoff();  //关红外读头
        }
        return 0;
    }


    @Override
    public void onDestroy() {
        if (mReadThread != null)
            run = false; 					// 关闭线程
        scan100ms.cancel();
        mSerialPort.scaner_poweroff(); 		// 关闭电源
        mSerialPort.close(0); 				// 关闭串口
        unregisterReceiver(myReceive); 		// 卸载注册
        super.onDestroy();
    }

    /**
     *  读线程 ,读取设备返回的信息，将其回传给发送请求的activity
     * @author Jimmy Pang
     *
     */
    private class ReadThread extends Thread {
        @Override
        public void run() {
            super.run();
            while (run) {
                int size;
                try {
                    byte[] buffer = new byte[512];
                    if (mInputStream == null)
                        return;
                    size = mInputStream.read(buffer);
                    if (size > 0) {

//						data = Tools.Bytes2HexString(buffer, size);
                        data = new String(buffer, 0, size);
                        data_buffer.append(data);
                        Log.e(TAG, size +"********"+data);
                        data = null;
//						 Log.e("DeviceService data", data);
                        //activity="ylescort.ylmobileandroid.box";
                        if(data_buffer != null && data_buffer.length() != 0 && activity != null){
                            Log.e("ScanService data", data_buffer.toString());
                            Intent serviceIntent = new Intent();
                            serviceIntent.setAction(activity);
                            serviceIntent.putExtra("result", replaceBlank(data_buffer.toString()));
                            data_buffer.setLength(0);  //清空缓存数据
                            Log.e(TAG, "result");
                            sendBroadcast(serviceIntent);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }

    private static   String replaceBlank(String str) {
        String dest = "";
        if (str!=null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }


    /**
     *  广播接受者
     * @author Jimmy Pang
     *
     */
    private class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String ac = intent.getStringExtra("activity");
            if(ac!=null)
                Log.e("receive activity", ac);
            activity = ac; // 获取activity
            if (intent.getBooleanExtra("stopflag", false))
                stopSelf(); // 收到停止服务信号
            Log.e("stop service", intent.getBooleanExtra("stopflag", false)
                    + "");

        }

    }



}
