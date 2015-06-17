package ylescort.ylmobileandroid;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;

import java.util.List;

import TaskClass.Box;
import TaskClass.TasksManager;
import TaskClass.User;
import TaskClass.YLTask;
import YLDataService.AnalysisBoxList;
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
        List<Box> boxList = webService.GetVaultInBoxList("91610", "1", "3166", getContext());
        Log.e(TAG,boxList.toString());
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
        String get = webService.PostVaultInBoxList(user, getContext());
        Log.e(TAG,get+"");
    }

    public void testAllBox()throws Exception{
        WebService webService = new WebService();
        User user = new User();
        user.setISWIFI("南海基地");
        user.setDeviceID("1");
        user.setEmpID("3361");
        List<Box> list = webService.GetAllBox(user, getContext());

        AnalysisBoxList analysisBoxList = new AnalysisBoxList();
//        List<Integer> integerList =
//        analysisBoxList.AnsysisBoxList(list);
//        Log.e(TAG,integerList.toString()+"");
    }

    public void testGetTaskList() throws Exception{
        WebService webService = new WebService();
        User user = new User();
        user.setTaskDate("2015-05-29");
        user.setEmpID("3727");
        user.setDeviceID("123");
        user.setName("001");
        List<YLTask> ylTaskList = webService.GetVaultOutTask(user,getContext());
        Log.e(TAG,ylTaskList.toString());
    }

    public void testComfirmvalutin()throws Exception{
        WebService webService = new WebService();
        User user = new User();
        user.setTaskDate("123");
        user.setEmpID("123");
        user.setDeviceID("123");
        user.setISWIFI("123");
        String Serreturn =  webService.ComfirmStoreIn(user, getContext());
        Log.e(TAG,Serreturn+"");
    }

    public void testVaultComfirmTask()throws Exception{
        WebService webService = new WebService();
        User user = new User();
        user.setTaskDate("2015-06-16");
        user.setEmpID("3361");
        user.setDeviceID("123");
        user.setISWIFI("1");
        List<YLTask> ylTaskList= webService.StoreInGetBaseAllTask(user, getContext());
        Log.e(TAG, ylTaskList.toString());
    }

}
