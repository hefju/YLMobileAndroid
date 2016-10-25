package ylescort.ylmobileandroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.TextView;
import java.util.Timer;
import java.util.TimerTask;
import YLDataService.BaseBoxDBSer;
import YLFileOperate.YLLoghandle;
import YLSystemDate.YLHandSetBaseData;
import YLSystemDate.YLRecord;
import YLSystemDate.YLSystem;

public class YLlauncher extends ActionBarActivity {


    private TextView YLlauncher_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yllauncher);

        InitLayout();

        //初始化行为记录
        YLLoghandle ylLoghandle = new YLLoghandle(getApplicationContext());
        YLRecord.setYlloghandle(ylLoghandle);
        //开始界面转跳
        StartLoginActvity();

        //初始化手持机数据
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                InitData();
            }
        });
        thread.start();
        StartNetServer();
    }

    private void StartLoginActvity() {
        final Intent it = new Intent(this, Login.class); //你要转向的Activity
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                startActivity(it); //执行
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                finish();
            }
        };
        timer.schedule(task, 500); //1秒后
    }

    private void InitData() {
        try {
            String cacheLastUpdate = Preferencedata("CacheLastUpdate", "1900-01-01 01:01:01");
            String handsetSN = Preferencedata("HandsetName", "9999");
            String VersionName = getVersionName();
            String HandSetMAC = WifiMAC();
            String HandSetIMEI = HandSetIMEI();
            String YLBoxCount = YLBoxCount();
            String SIMIMEI = HandSetSIM();
            YLHandSetBaseData.setYLVersion(VersionName);
            YLHandSetBaseData.setHandSetIMEI(HandSetIMEI);
            YLHandSetBaseData.setHandSetMAC(HandSetMAC);
            YLHandSetBaseData.setCacheDatetime(cacheLastUpdate);
            YLHandSetBaseData.setYLBoxCount(YLBoxCount);
            YLHandSetBaseData.setSIMIMEI(SIMIMEI);
            YLHandSetBaseData.setHandSetSN(handsetSN);
            Log.e(YLSystem.getKimTag(),"初始化成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void InitLayout() {
        YLlauncher_tv = (TextView) findViewById(R.id.YLlauncher_tv);
    }


    //获取软件版本号
    private String getVersionName() throws Exception {
        // 获取packagemanager的实例
//        PackageManager packageManager = getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
//        PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(), 0);
        return "2.0.9";
    }

    //获取手持机SIM卡码
    private String HandSetSIM() throws Exception{
        String srvName = Context.TELEPHONY_SERVICE;
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(srvName);
        return telephonyManager.getSimSerialNumber();
    }

    //获取手持机卡码
    private String HandSetIMEI() throws Exception{
        String srvName = Context.TELEPHONY_SERVICE;
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(srvName);
        return telephonyManager.getDeviceId();
    }


    //获取手持机SN码及上次缓存时间
    private String Preferencedata  ( String typestr, String data) throws Exception{
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        return prefs.getString(typestr, data);
    }


    //获取手持机MAC码
    private String WifiMAC() throws Exception {
        //在wifi未开启状态下，仍然可以获取MAC地址，但是IP地址必须在已连接状态下否则为0
        String macAddress = "";
        WifiManager wifiMgr = (WifiManager)getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = (null == wifiMgr ? null : wifiMgr.getConnectionInfo());
        if (null != info) {
            macAddress = info.getMacAddress();
        }
        return  macAddress;
    }

    //获取当前款箱数据
    private String YLBoxCount() throws Exception{
        int count= (new BaseBoxDBSer(YLlauncher.this)).BaseBoxCount();
        return count+"";
    }

    private void StartNetServer(){
        /**
         增加对网络接入判断
         */
        Intent i = new Intent(getApplicationContext(), YLNetWorkStateService.class);
        startService(i);

    }

}
