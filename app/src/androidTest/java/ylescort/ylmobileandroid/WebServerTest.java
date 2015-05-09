package ylescort.ylmobileandroid;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;

import java.util.List;

import TaskClass.Box;
import TaskClass.TasksManager;
import TaskClass.User;
import TaskClass.YLTask;
import YLDataService.WebService;
import YLSystemDate.YLEditData;
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
        user.setTaskDate("2015-05-04");
        user.setDeviceID("123");
        user.setEmpID("3166");
        List<YLTask> ylTaskList = webService.GetHandovermanTask(user, getContext());
        Log.e(TAG,ylTaskList.toString());
    }

    public void testValutinBox()throws Exception{
        WebService webService = new WebService();
        List<Box> boxList = webService.GetVaultInBoxList("91610x", "1", "3166", getContext());
        Log.e(TAG,boxList.toString()+"");
    }

    public void testValultinBox()throws Exception{
        WebService webService = new WebService();
        User user = new User();
        List<YLTask> ylTaskList= webService.GetHandovermanTask(user, getContext());
        Log.e(TAG,ylTaskList.toString());
    }

    public void testValultoutTask()throws Exception{
        WebService webService = new WebService();
        User user = new User();
        user.setEmpHFNo("");
        user.setEmpNO("");
        user.setTaskDate("2015-05-04");
        user.setDeviceID("231");
        user.setEmpID("3361");
        user.setName("033");//借用user类传入线路编号
        List<YLTask> ylTaskList = webService.GetVaultOutTask(user, getContext());
        Log.e(TAG,ylTaskList.toString());
    }

    public void testUploadvaultinbox()throws Exception{
        YLTask ylTask = new YLTask();
        YLEditData.setYlTask(ylTask);
        WebService webService = new WebService();
        User user = new User();
        user.setEmpID("3361");
        user.setDeviceID("123");
        String get = webService.PostVaultInBoxList(user,getContext());
        Log.e(TAG,get+"");
    }
}
