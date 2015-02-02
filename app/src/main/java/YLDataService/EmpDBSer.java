package YLDataService;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

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
           sdb.execSQL("insert into Employee (EmpID,EmpNO,Pass,Name,DeviceID,ISWIFI,Time,ServerReturn) values(?,?,?,?,?,?,?,?)"
                   ,new Object[]{user.getEmpID(),user.getEmpNO(),user.getPass(),user.getName(),user.getDeviceID(),
           user.getISWIFI(),user.getTime(),user.getServerReturn()} );
            sdb.setTransactionSuccessful();
        }
        finally {
            sdb.endTransaction();
            sdb.close();
        }
    }

    public void UpdateUser(User x) {
        List<User> lst=new ArrayList<>();
        lst.add(x);
        UpdateUser(lst);
    }
    public void DeleteUser(User x) {
        List<User> lst=new ArrayList<>();
        lst.add(x);
        DeleteUser(lst);
    }

    //通过EmpID查找用户
    public User GetUser(String empID) {
        SQLiteDatabase sdb =ylsqlHelper.getReadableDatabase();
        Cursor cursor = sdb.rawQuery("select * from Employee where EmpID=?" + empID, null);

        User u=new User();
        while(cursor.moveToNext()){
            String EmpID = cursor.getString(cursor.getColumnIndex("EmpID"));
            String EmpNO = cursor.getString(cursor.getColumnIndex("EmpNO"));
            String Pass = cursor.getString(cursor.getColumnIndex("Pass"));
            String Name = cursor.getString(cursor.getColumnIndex("Name"));
            String DeviceID = cursor.getString(cursor.getColumnIndex("DeviceID"));
            String ISWIFI = cursor.getString(cursor.getColumnIndex("ISWIFI"));
            String Time = cursor.getString(cursor.getColumnIndex("Time"));
            String ServerReturn = cursor.getString(cursor.getColumnIndex("ServerReturn"));
            String TaskDate = cursor.getString(cursor.getColumnIndex("TaskDate"));

            u.EmpID=EmpID;
            u.EmpNO=EmpNO;
            u.Pass=Pass;
            u.Name=Name;
            u.DeviceID=DeviceID;
            u.ISWIFI=ISWIFI;
            u.Time=Time;
            u.ServerReturn=ServerReturn;
            u.TaskDate=TaskDate;
            break;
        }
        sdb.close(); //关闭数据库
        return u;
    }

    public void InsertEmp(List<User> lst){
        SQLiteDatabase sdb = ylsqlHelper.getWritableDatabase();
        sdb.beginTransaction();
        try {
            for(User x: lst) {
                sdb.execSQL("insert into Employee (EmpID,EmpNO,Pass,Name,DeviceID,ISWIFI,Time," +
                        "ServerReturn,TaskDate) values(?,?,?,?,?,?,?,?,?)"
                        , new Object[]{x.getEmpID(), x.getEmpNO(), x.getPass(), x.getName(), x.getDeviceID(),
                        x.getISWIFI(), x.getTime(), x.getServerReturn(),x.getTaskDate()});
                sdb.setTransactionSuccessful();
            }
        }
        finally {
            sdb.endTransaction();
            sdb.close();
        }
    }

    public void UpdateUser(List<User> lst) {
        SQLiteDatabase sdb = ylsqlHelper.getWritableDatabase();
        sdb.beginTransaction();
        try {
            for(User x: lst) {
                sdb.execSQL("UPDATE T_employee SET EmpNO =?, Pass =?, Name =?, DeviceID =?, " +
                        "ISWIFI =?, Time =?, ServerReturn =?, TaskDate =? where EmpID=?"
                        , new Object[]{ x.getEmpNO(), x.getPass(), x.getName(), x.getDeviceID(),
                        x.getISWIFI(), x.getTime(), x.getServerReturn(),x.getTaskDate(),x.getEmpID()});
            }
        }
        finally {
            sdb.endTransaction();
            sdb.close();
        }
    }

    public void DeleteUser(List<User> lst) {
        SQLiteDatabase sdb = ylsqlHelper.getWritableDatabase();
        sdb.beginTransaction();
        try {
            for(User x: lst) {
                sdb.execSQL("delete from T_employee where EmpID=?", new Object[]{x.getEmpID()});
            }
        }
        finally {
            sdb.endTransaction();
            sdb.close();
        }
    }


    public void Insemptest(){
        SQLiteDatabase sdb = ylsqlHelper.getWritableDatabase();
        sdb.beginTransaction();
        try {
            sdb.execSQL("insert into Employee (EmpID,EmpNO,Pass,Name,DeviceID,ISWIFI,Time,ServerReturn) values(1,1,1,1,1,1,1,1)");
            sdb.setTransactionSuccessful();
        }catch (Exception e){
            e.printStackTrace();
        }
        finally{
            //结束事务
            sdb.endTransaction();
        }
    }

}
