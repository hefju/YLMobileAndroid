package ylescort.ylmobileandroid;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;

import com.android.hdhe.nfc.NFCcmdManager;
//import com.android.hdhe.uhf.reader.SerialPort;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import TaskClass.BaseEmp;
import TaskClass.Box;
import TaskClass.TasksManager;
import TaskClass.User;
import TaskClass.YLTask;
import YLDataService.BaseEmpDBSer;
import YLDataService.EmpDBSer;
import YLDataService.TaskDBSer;
import YLDataService.TasksManagerDBSer;
import YLDataService.WebService;
import YLDataService.YLBoxScanCheck;
import YLDataService.YLSQLHelper;
import YLSystemDate.YLSysTime;
import YLSystemDate.YLSystem;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {

    public ApplicationTest() { super(Application.class); }

    private static final String TAG = "kim";

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
        user.setPass( YLSystem.SetMD5("600241"));
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
        user.Pass= YLSystem.SetMD5("600241");
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
        TasksManagerDBSer tasksManagerDBSer = new TasksManagerDBSer(getContext());
        tasksManagerDBSer.DeleteTasksManagerbydate("2016-05-20");
    }

    public void testgetbaseemp() throws Exception {
        BaseEmpDBSer baseEmpDBSer = new BaseEmpDBSer(getContext());
        String userNo = "123";
        List<BaseEmp> baseEmpList = baseEmpDBSer.GetBaseEmps("where EmpID ='"+userNo+"'" );
        if (baseEmpList.toString().equals("[]")){
            Log.e(TAG,"123");
        }else {
            Log.e(TAG,baseEmpList.toString());
        }
    }

    public void testbasebox()throws Exception{
        String number = "0\t\n";
        Box box= YLBoxScanCheck.CheckBox(number,getContext());
        Log.e(TAG,box.toString());
    }

    public void testAnyTaskgetuser()throws Exception{
        String url = YLSystem.GetBaseUrl(getContext())+"Login1";
        User user = new User();
        user.setEmpNO("520037");
        user.setPass(YLSystem.SetMD5("520037"));
        WebService webService = new WebService();
        User userfromweb = webService.LogicBypassword(user, url);
        Log.e(TAG,userfromweb.toString());
    }

    public void testAnyTaskVaultTask()throws Exception{
        YLSysTime.setServertime(new Date());
        Log.e(TAG,YLSysTime.getServertime().toString());

    }

    public void testColors()throws Exception{
        //int b =  getResources().getColor(R.color.dodgerblue);//得到配置文件里的颜色
    }

    public void testUHFPort()throws Exception{
//        SerialPort serialPort = new SerialPort(15,115200,0);
//        if (serialPort != null){
//            Log.e(YLSystem.getKimTag(),"1");
//        }else {
//            Log.e(YLSystem.getKimTag(),"0");
//        }
    }


}
