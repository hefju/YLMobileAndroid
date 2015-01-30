package ylescort.ylmobileandroid;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;

import TaskClass.User;
import YLDataService.EmpDBSer;
import YLDataService.WebService;
import YLDataService.YLSQLHelper;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() { super(Application.class); }

    private static final String TAG = "PersonServiceTest";

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
        user.setEmpNO("200097");
        user.setPass("200097");
        String mather = "Login1";
         String webcontent =  webService.UserWebContent(mather,user);
        Log.d(TAG,webcontent);
    }


}
