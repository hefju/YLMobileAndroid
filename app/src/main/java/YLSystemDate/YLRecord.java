package YLSystemDate;

import android.content.Context;
import android.util.Log;

import java.io.File;

import YLFileOperate.YLLoghandle;

/**
 * Created by Administrator on 2016-04-26.
 */
public class YLRecord {

    public static YLLoghandle Ylloghandle ;

    public static YLLoghandle getYlloghandle() {
        return Ylloghandle;
    }

    public static void setYlloghandle(YLLoghandle ylloghandle) {
        Ylloghandle = ylloghandle;
    }


    public static  void WriteRecord(Class c,String s){
        try {
//            2016-04-26 16:32:50
            String strdate = YLSysTime.GetStrCurrentTime().substring(11);
            String str = strdate+"-"+ c.getSimpleName()+"-"+s;
            Ylloghandle.AppendWrite(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static  void WriteRecord(String activityname,String s){
        try {
            String strdate = YLSysTime.GetStrCurrentTime().substring(11);
            String str = strdate+"-"+ activityname+"-"+s;
            Ylloghandle.AppendWrite(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
