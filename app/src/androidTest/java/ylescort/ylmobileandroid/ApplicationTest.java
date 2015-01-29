package ylescort.ylmobileandroid;

import android.app.Application;
import android.test.ApplicationTestCase;

import TaskClass.User;
import YLDataService.EmpDBSer;
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


    public void EmpIns() throws Exception {

        User user = new User();
        user.setEmpNO("200097");
        user.setName("kim");
        user.setPass("200097");
        EmpDBSer empDBSer = new EmpDBSer(this.getContext());
        empDBSer.InsEmp(user);
    }


}
