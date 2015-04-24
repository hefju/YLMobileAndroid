package ylescort.ylmobileandroid;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import YLSystemDate.YLEditData;
import YLSystemDate.YLSysTime;
import YLSystemDate.YLSystem;

/**
 * Created by Administrator on 2015/4/20.
 */
public class SerTimeService extends Service{
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        try {
            StartSerTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onCreate();
    }

    private void StartSerTime() throws Exception {
        if (YLSysTime.getServertime() == null){
            if (YLSystem.getUser().getServerReturn() == null)return;
            YLSysTime.setServertime(YLSysTime.StrToTime(YLSystem.getUser().getTime()));
        }
        Date systime = YLSysTime.getServertime();
        final Calendar calendar = Calendar.getInstance(Locale.CHINA);
        calendar.setTime(systime);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
               calendar.add(Calendar.MILLISECOND,1000);
                YLSysTime.setServertime(calendar.getTime());
                Log.d("kim",calendar.getTime().toString());
            }
        },0,1000);
    }
}
