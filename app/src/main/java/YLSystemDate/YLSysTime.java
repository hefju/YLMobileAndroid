package YLSystemDate;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Administrator on 2015/4/20.
 */
public class YLSysTime {

    private static Date Servertime;

    public static Date getServertime() {
        return Servertime;
    }

    public static void setServertime(Date servertime) {
        Servertime = servertime;
    }

    private String DateToStr(Date date)throws Exception{
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        return sDateFormat.format(date);
    }

    private Date StrToDate(String str )throws Exception{
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        return sDateFormat.parse(str);
    }

}
