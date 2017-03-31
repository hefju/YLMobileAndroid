package YLSystemDate;

import android.content.Context;
import android.os.SystemClock;
import android.util.Log;

import com.android.rfid.DevSettings;

import java.io.DataOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import TaskClass.User;
import ylescort.ylmobileandroid.ShellInterface;

/**
 * Created by Administrator on 2015/4/20.
 */
public class YLSysTime {

    private Context context;

    public YLSysTime() {
    }

    public YLSysTime(Context context) {
        this.context = context;
    }

    private static Date Servertime;

    public static Date getServertime()throws Exception{
        return  new Date();
    }

    public static void setServertime(Date servertime) {
        Servertime = servertime;
    }

    public static String TimeToStr(Date date)throws Exception{
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        return sDateFormat.format(date);
    }

    public static String DateToStr(Date date)throws Exception{
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        return sDateFormat.format(date);
    }

    public static Date StrToDate(String str )throws Exception{
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        return sDateFormat.parse(str);
    }

    public static Date StrToTime(String str )throws Exception{
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        return sDateFormat.parse(str);
    }

    public static String GetStrCurrentTime(){
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        return sDateFormat.format(new java.util.Date());
    }

    public static String GetStrCurrentShortTime(){
        SimpleDateFormat sDateFormat = new SimpleDateFormat("HH:mm:ss", Locale.CHINA);
        return sDateFormat.format(new java.util.Date());
    }

    public static Date StrToMin(String str )throws Exception{
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        return sDateFormat.parse(str);
    }

    public static String GetStrCurrentMin(){
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        return sDateFormat.format(new java.util.Date());
    }

    public static Date GetDateCurrentTime()throws Exception{
        return new Date();
    }

    public static Date GetDateCurrentDate()throws Exception{
        return StrToDate(GetStrCurrentTime());
    }

    public static Date GetDateCurrentMin()throws Exception{
        return  StrToMin(GetStrCurrentTime());
    }

    public static String GetSercurTime() throws Exception {
        return TimeToStr(Servertime);
    }

    public static Calendar AddDateString(Calendar calendar,int Daycount){
        calendar.add(calendar.DATE,Daycount);
        return calendar;
    }

    public static  String IntToStrDate(int year, int monthOfYear, int dayOfMonth){
        return year+"-"+String.format("%02d",(monthOfYear + 1))+"-"
                +String.format("%02d",(dayOfMonth));
    }

    private void CheckLocateTime(String Servertime){
        try {
            Date SerDate = StrToMin(Servertime);
            Calendar SerCal = Calendar.getInstance();
            SerCal.setTime(SerDate);

            Date Loca = GetDateCurrentMin();
            Calendar locaCal = Calendar.getInstance();
            locaCal.setTime(Loca);

            int com = SerCal.compareTo(locaCal);
            if (com !=0){
                SerDate = StrToTime(Servertime);
                SerCal.setTime(SerDate);
                SetTime(SerCal);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Sertime(final String Servertime) {
        Thread thread  = new Thread(new Runnable() {
            @Override
            public void run() {
                CheckLocateTime(Servertime);
            }
        });
        thread.start();
    }

    /**
     * root后通过日历控件修改时间
     * @param calendar 日期控件
     */

    private void SetTime(Calendar calendar) {
        try {
            if (ShellInterface.isSuAvailable()) {
                ShellInterface.runCommand("chmod 666 /dev/alarm");
                SystemClock.setCurrentTimeMillis(calendar.getTimeInMillis());
                ShellInterface.runCommand("chmod 664 /dev/alarm");
                Log.e(YLSystem.getKimTag(), "时间修改成功");
            }
            if (context != null)
            {
                DevSettings d = new DevSettings(context);
                d.setCurrentTime(calendar.getTimeInMillis());
                Log.e(YLSystem.getKimTag(), "时间修改成功");
            }
        } catch (Exception e) {
            Log.e(YLSystem.getKimTag(), "修改失败");
            e.printStackTrace();
        }
    }

    /**
     * root 后通过修改系统配置文件修改时间
     * @param time string 格式时间
     */
    public void SetDate(String time) {
        try {
            Process process = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(process.getOutputStream());
            os.writeBytes("date -s 20120419.024012; \n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
