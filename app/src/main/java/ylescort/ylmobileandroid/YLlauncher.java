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
import YLSystemDate.HandsetInfo;
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
            HandsetInfo handsetInfo = new HandsetInfo(getApplicationContext());
            String cacheLastUpdate =handsetInfo.Preferencedata("CacheLastUpdate", "1900-01-01 01:01:01");
            String handsetSN =handsetInfo. Preferencedata("HandsetName", "9999");
            String VersionName =handsetInfo. getVersionName();
            String HandSetMAC = handsetInfo.WifiMAC();
            String HandSetIMEI =handsetInfo. HandSetIMEI();
            String YLBoxCount =handsetInfo.YLBoxCount();
            String SIMIMEI =handsetInfo. HandSetSIM();
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

    private void StartNetServer(){
        /**
         增加对网络接入判断
         */
        Intent i = new Intent(getApplicationContext(), YLNetWorkStateService.class);
        startService(i);

    }

}
