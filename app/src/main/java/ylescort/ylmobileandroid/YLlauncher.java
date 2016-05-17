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

import YLDataService.WebServerBaseData;
import YLSystemDate.YLSystem;

public class YLlauncher extends ActionBarActivity {


    private TextView YLlauncher_tv;
    private String HandsetSN;
    private String HandsetMAC;
    private String VisionName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yllauncher);

        InitLayout();
        InitData();
        StartLoginActvity();

    }

    private void StartLoginActvity() {
        final Intent it = new Intent(this, LoginActivity.class); //你要转向的Activity
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                startActivity(it); //执行
                finish();
            }
        };
        timer.schedule(task, 1000 * 5); //10秒后
    }

    private void InitData() {


    }


    private void InitLayout() {
        YLlauncher_tv = (TextView) findViewById(R.id.YLlauncher_tv);
    }



    private String getVersionName() throws Exception {
        // 获取packagemanager的实例
        PackageManager packageManager = getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(), 0);
        return packInfo.versionName;
    }

    //获取手持机SIM卡码
    private String HandSetSIM() throws Exception{
        String srvName = Context.TELEPHONY_SERVICE;
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(srvName);
        return telephonyManager.getSimSerialNumber();
    }

    //获取手持机SN码
    private String Preferencedata  ( String typestr, String data) throws Exception{
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        return prefs.getString(typestr, data);
    }


    //获取手持机MAC码
    private String WifiMAC() throws Exception {
        //在wifi未开启状态下，仍然可以获取MAC地址，但是IP地址必须在已连接状态下否则为0
        String macAddress = null;
        WifiManager wifiMgr = (WifiManager)getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = (null == wifiMgr ? null : wifiMgr.getConnectionInfo());
        if (null != info) {
            macAddress = info.getMacAddress();
        }
        return  macAddress;
    }

    private void StartNetServer(){
        /**
         增加对网络接入判断
         */
        Intent i = new Intent(getApplicationContext(), YLNetWorkStateService.class);
        startService(i);

    }

}
