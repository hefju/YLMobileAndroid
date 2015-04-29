package ylescort.ylmobileandroid;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;

import java.util.List;

import TaskClass.User;
import TaskClass.YLTask;
import YLDataService.WebService;
import YLSystemDate.YLSystem;

/**
 * Created by Administrator on 2015/4/29.
 */
public class WebServerTest extends ApplicationTestCase<Application> {
    public WebServerTest() { super(Application.class); }

    private static final String TAG = "kim";
    public void testValutinTask()throws Exception{
        WebService webService = new WebService();
        String url = YLSystem.GetBaseUrl(getContext())+"StoreInGetTask";
        User user = new User();
        user.setEmpHFNo("002DE3A6");
        user.setEmpNO("520037");
        user.setTaskDate("2015-04-27");
        user.setDeviceID("123");
        user.setEmpID("3166");
        List<YLTask> ylTaskList = webService.GetHandovermanTask(user, url);
        Log.e(TAG,ylTaskList.toString());
    }
}
