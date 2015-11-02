package ylescort.ylmobileandroid;

import android.app.Application;
import android.os.SystemClock;
import android.test.ApplicationTestCase;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;

import TaskClass.YLTask;
import YLSystemDate.YLSysTime;
import YLSystemDate.YLSystem;

/**
 * Created by Administrator on 2015/4/29.
 */
public class TimeTest extends ApplicationTestCase<Application> {

    public TimeTest() { super(Application.class); }

    private static final String TAG = "kim";

    public void testLocaleDate()throws Exception{
       // YLSysTime.setServertime(new Date());
        String settime = YLSysTime.TimeToStr(YLSysTime.getServertime());
        Log.e(TAG,settime);
    }

     public void testStrDate()throws Exception{
         String strdate = YLSysTime.GetStrCurrentTime();
         Log.e(TAG,strdate);
     }

    public void testInttoString()throws Exception{
        int i = 0,j = 2;
        String string ="0"+ i+j;
        Log.e(TAG,string);
    }

    public void  testDateCompare()throws Exception{
//        YLTask ylTask = new YLTask();
//        String LocalTime = "2015-05-01 12:12:16";
//        String UpLoadTime = ylTask.getServerReturn();
//        if (UpLoadTime == null){
//            Log.e(YLSystem.getKimTag(),UpLoadTime+" UpLoadTime");
//        }else {
//            Date DateLoca = YLSysTime.StrToTime(LocalTime);
//            Date DateUpLoad = YLSysTime.StrToTime(UpLoadTime);
//
//            Integer compare = DateLoca.compareTo(DateUpLoad);
//            long timeLong = DateLoca.getTime() - DateUpLoad.getTime();
//
//
//            Log.e(YLSystem.getKimTag(),timeLong+" compare");
//        }

        String LocalTime = "2015-05-02 12:13:16";
        String UpLoadTime = "2015-05-02 12:13:15";

        try {
            Date datelocal = YLSysTime.StrToTime(LocalTime);
            Date dateUpload = YLSysTime.StrToTime(UpLoadTime);

            int compet = datelocal.compareTo(dateUpload);
            Log.e(YLSystem.getKimTag(),compet+"对比");

        }catch (Exception e){
            e.printStackTrace();
        }



//        Log.e(YLSystem.getKimTag(),DateUpLoad+"DateUpLoad");

    }

    public void testedittime(){
        Calendar c = Calendar.getInstance();
        c.set(2010, 1, 1, 12, 00, 00);
        SystemClock.setCurrentTimeMillis(c.getTimeInMillis());
    }

    public void testStrTranCanr()throws Exception{
        String Time="2015-08-18 18:07:14";
        Calendar calendar = Calendar.getInstance();
        Date date = YLSysTime.StrToTime(Time);
        calendar.set(date.getYear(),date.getMonth(),date.getDate(),date.getHours(),date.getMinutes(),date.getSeconds());
        Log.e(TAG, calendar.getTime().toString());
    }

    public void testCurrentDate()throws Exception{
        Log.e(YLSystem.getKimTag(), YLSysTime.GetDateCurrentDate().toString()) ;
    }

}
