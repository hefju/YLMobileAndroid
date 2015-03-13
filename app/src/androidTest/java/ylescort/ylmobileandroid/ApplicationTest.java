package ylescort.ylmobileandroid;

import android.app.Application;
import android.content.Context;
import android.test.ApplicationTestCase;
import android.util.Log;

import com.android.hdhe.nfc.NFCcmdManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import TaskClass.TasksManager;
import TaskClass.User;
import TaskClass.YLTask;
import YLDataService.EmpDBSer;
import YLDataService.TaskDBSer;
import YLDataService.TasksManagerDBSer;
import YLDataService.WebService;
import YLDataService.YLSQLHelper;
import YLSystem.YLSystem;
import YLWebService.YLWebService;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    private Context applicationContext;

    public ApplicationTest() { super(Application.class); }

    private static final String TAG = "YLtest";

    public void testCreateDB() throws Exception{
        YLSQLHelper ylsqlHelper = new YLSQLHelper(getContext());
        ylsqlHelper.getWritableDatabase();
    }


    public void testEmpIns() throws Exception {

        User user = new User();
        user.setEmpNO("200097");
        user.setName("kim");
        user.setPass("200097");
        EmpDBSer empDBSer = new EmpDBSer(getContext());
        empDBSer.InsEmp(user);
    }

    public void testWebContent() throws Exception {

        WebService webService = new WebService();
        User user = new User();
        user.setEmpNO("600241");
        user.setPass( YLSystem.md5("600241"));
        String mather = "Login1";
         String webcontent =  webService.UserWebContent(mather,user);
        Log.d(TAG,webcontent);
    }

    public void testTaskWebContent() throws Exception {
        Gson gson = new Gson();
        WebService webService = new WebService();
        User user = new User();
        user.EmpNO="600241";
        user.Name="杨磊";
        user.Pass= YLSystem.md5("600241");
        user.DeviceID="NH008";
        user.ISWIFI="1";
        user.EmpID="2703";
        user.TaskDate= "2014-08-07";
        String mather = "GetTask1";
        String webcontent =  webService.TaskWebContent(mather,user);
        Log.d(TAG,webcontent);

        List<YLTask> ylTaskList = new ArrayList<YLTask>();

        ylTaskList= gson.fromJson(webcontent, new TypeToken<List<YLTask>>() {
        }.getType());
        Log.d(TAG,ylTaskList.toString());

    }

    public void testInsTask(){
        YLTask ylTask = new YLTask();
        ylTask.setLine("123");
        ylTask.setHandset("2115");
        TaskDBSer taskDBSer = new TaskDBSer(getContext());
        taskDBSer.InsterTask(ylTask);
    }

    private NFCcmdManager manager ;
    private byte[] uid ;

    public void testHF(){

        manager = NFCcmdManager.getNFCcmdManager(12, 115200, 0);
        manager.readerPowerOn();

    }
    private TasksManager tasksManager = null;//任务管理类

    public void testDeltask() {
        tasksManager = new TasksManager();
        tasksManager.TaskDate = "2015-03-12";
        TasksManagerDBSer tasksManagerDBSer = new TasksManagerDBSer(getApplicationContext());
        tasksManagerDBSer.DeleteTasksManager(tasksManager);
    }


    public Context getApplicationContext() {
        return applicationContext;
    }
}
