package YLSystemDate;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Administrator on 2015/4/20.
 */
public class YLSysTime {

    private static Date Servertime;

    public static Date getServertime()throws Exception{
        Date date = new Date();
        if (Servertime== null||Servertime.compareTo(date)==0){
            Log.e("kim",Servertime+"1");
            Servertime = GetDateCurrentTime();
            Log.e("kim",Servertime+"2");
        }
        return Servertime;
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

    public static Date GetDateCurrentTime()throws Exception{
        return new Date();
    }

    public static String GetSercurTime() throws Exception {
        return TimeToStr(Servertime);
    }

}
