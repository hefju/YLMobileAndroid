package YLDataService;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import TaskClass.BaseEmp;

/**
 * Created by rush on 2015/2/11.
 */
public class BaseEmpDBSer {
    private YLSQLHelper ylsqlHelper;

    public BaseEmpDBSer(Context context){this.ylsqlHelper = new YLSQLHelper(context);}
    //根据条件查找BaseEmp
    public List<BaseEmp> GetBaseEmps(String where){
        List<BaseEmp> lstBaseEmp=new ArrayList<>();
        SQLiteDatabase sdb =ylsqlHelper.getReadableDatabase();

        Cursor cursor = sdb.rawQuery("select * from BaseEmp " + where, null);
        while(cursor.moveToNext()){
            int Id = cursor.getInt(cursor.getColumnIndex("Id"));
            String ServerReturn = cursor.getString(cursor.getColumnIndex("ServerReturn"));
            String EmpID = cursor.getString(cursor.getColumnIndex("EmpID"));
            String EmpName = cursor.getString(cursor.getColumnIndex("EmpName"));
            String EmpNo = cursor.getString(cursor.getColumnIndex("EmpNo"));
            String EmpHFNo = cursor.getString(cursor.getColumnIndex("EmpHFNo"));
            String EmpWorkState = cursor.getString(cursor.getColumnIndex("EmpWorkState"));
            String EmpJJNo = cursor.getString(cursor.getColumnIndex("EmpJJNo"));


            BaseEmp b=new BaseEmp();
            b.Id=Id;
            b.ServerReturn=ServerReturn;
            b.EmpID=EmpID;
            b.EmpName=EmpName;
            b.EmpNo=EmpNo;
            b.EmpHFNo=EmpHFNo;
            b.EmpWorkState=EmpWorkState;
            b.EmpJJNo=EmpJJNo;

            lstBaseEmp.add(b);
        }
        sdb.close(); //关闭数据库
        return lstBaseEmp;
    }

    //批量插入BaseEmp
    public void InsertBaseEmp(List<BaseEmp> lst) {
        SQLiteDatabase sdb = ylsqlHelper.getWritableDatabase();
        sdb.beginTransaction();
        try {
            for (BaseEmp x : lst) {
                sdb.execSQL("INSERT INTO BaseEmp (ServerReturn, EmpID, EmpName, EmpNo, EmpHFNo, " +
                                "EmpWorkState, EmpJJNo) VALUES     (?,?,?,?,?,?,?)",
                        new Object[]{x.ServerReturn,x.EmpID,x.EmpName,x.EmpNo,x.EmpHFNo,x.EmpWorkState,x.EmpJJNo,
                        });

            }
        }
        finally {
            sdb.setTransactionSuccessful();
            sdb.endTransaction();
            sdb.close(); //关闭数据库
        }

    }

    //批量更新BaseEmp
       public void UpdateBaseEmp(List<BaseEmp> lst) {
        SQLiteDatabase sdb = ylsqlHelper.getWritableDatabase();
        sdb.beginTransaction();
        try {
            for (BaseEmp x : lst) {
                sdb.execSQL("UPDATE BaseEmp SET ServerReturn =?, EmpID =?, EmpName =?, EmpNo =?," +
                                " EmpHFNo =?, EmpWorkState =?, EmpJJNo =? where Id=?",
                        new Object[]{x.ServerReturn,x.EmpID,x.EmpName,x.EmpNo,x.EmpHFNo,x.EmpWorkState,x.EmpJJNo, x.Id});

            }
        }
        finally {
            sdb.setTransactionSuccessful();
            sdb.endTransaction();
            sdb.close(); //关闭数据库
        }

    }

    public void UpdateBaseEmpByEmpID(List<BaseEmp> lst) {
        SQLiteDatabase sdb = ylsqlHelper.getWritableDatabase();
        sdb.beginTransaction();
        try {
            for (BaseEmp x : lst) {
                sdb.execSQL("UPDATE BaseEmp SET ServerReturn =?, EmpID =?, EmpName =?, EmpNo =?," +
                                " EmpHFNo =?, EmpWorkState =?, EmpJJNo =? where EmpID=?",
                        new Object[]{x.ServerReturn,x.EmpID,x.EmpName,x.EmpNo,x.EmpHFNo,x.EmpWorkState,x.EmpJJNo, x.EmpID});

            }
        }
        finally {
            sdb.setTransactionSuccessful();
            sdb.endTransaction();
            sdb.close(); //关闭数据库
        }

    }


    public void DeleteBaseEmp(List<BaseEmp> lst) {
        SQLiteDatabase sdb = ylsqlHelper.getWritableDatabase();
        sdb.beginTransaction();
        try {
            for (BaseEmp x : lst) {
                sdb.execSQL("DELETE FROM BaseEmp where Id=?", new Object[]{x.Id});

            }
        }
        finally {
            sdb.setTransactionSuccessful();
            sdb.endTransaction();
            sdb.close(); //关闭数据库
        }

    }
    public void DeleteBaseEmpByEmpID(List<BaseEmp> lst) {
        SQLiteDatabase sdb = ylsqlHelper.getWritableDatabase();
        sdb.beginTransaction();
        try {
            for (BaseEmp x : lst) {
                sdb.execSQL("DELETE FROM BaseEmp where EmpID=?", new Object[]{x.EmpID});
            }
        }
        finally {
            sdb.setTransactionSuccessful();
            sdb.endTransaction();
            sdb.close(); //关闭数据库
        }

    }

    public void DeleteAll() {
        SQLiteDatabase sdb = ylsqlHelper.getWritableDatabase();
        sdb.beginTransaction();
        try {
            sdb.execSQL("DELETE FROM BaseEmp ");
        }
        finally {
            sdb.setTransactionSuccessful();
            sdb.endTransaction();
            sdb.close(); //关闭数据库
        }
    }
}
