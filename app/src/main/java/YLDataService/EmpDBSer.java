package YLDataService;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import TaskClass.User;

/**
 * Created by Administrator on 2015/1/28.
 */
public class EmpDBSer {

    private YLSQLHelper ylsqlHelper;

    public EmpDBSer(Context context){this.ylsqlHelper = new YLSQLHelper(context);}

    public void InsEmp(User user){
        SQLiteDatabase sdb = ylsqlHelper.getWritableDatabase();
        sdb.beginTransaction();
        try {
           sdb.execSQL("insert into T_employee (EmpID,EmpNO,Pass,Name,DeviceID,ISWIFI,Time,ServerReturn) values(?,?,?,?,?,?,?,?)"
                   ,new Object[]{user.getEmpID(),user.getEmpID(),user.getEmpNO(),user.getPass(),user.getName(),user.getDeviceID(),
           user.getISWIFI(),user.getTime(),user.getServerReturn()} );
        }
        finally {
            sdb.endTransaction();
        }
    }

}
