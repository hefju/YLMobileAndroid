package ylescort.ylmobileandroid;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;
import YLSystemDate.YLSysTime;

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

}
