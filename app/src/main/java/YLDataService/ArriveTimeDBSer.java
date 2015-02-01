package YLDataService;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import TaskClass.ArriveTime;
import TaskClass.Site;

/**
 * Created by asus on 2015/1/31.
 */
public class ArriveTimeDBSer {
    private YLSQLHelper ylsqlHelper;

    public ArriveTimeDBSer(Context context){this.ylsqlHelper = new YLSQLHelper(context);}
    public void InsertArriveTime(ArriveTime x) {
        SQLiteDatabase sdb = ylsqlHelper.getWritableDatabase();
        sdb.beginTransaction();
        try {
            sdb.execSQL("INSERT INTO ArriveTime(ServerReturn, EmpID, ATime, TimeID, TradeBegin," +
                            " TradeEnd, TradeState) VALUES   (?,?,?,?,?,?,?,)",
                    new Object[]{x.ServerReturn,x.EmpID,x.ATime,x.TimeID,x.TradeBegin,x.TradeEnd,x.TradeState} );
        }
        finally {
            sdb.endTransaction();
            sdb.close();
        }

    }
    public void UpdateArriveTime(ArriveTime x) {
        SQLiteDatabase sdb = ylsqlHelper.getWritableDatabase();
        sdb.beginTransaction();
        try {
            sdb.execSQL("UPDATE  ArriveTime SET ServerReturn =?, EmpID =?, ATime =?, TimeID =?," +
                            " TradeBegin =?, TradeEnd =?, TradeState =? where Id=?",
                    new Object[]{x.ServerReturn,x.EmpID,x.ATime,x.TimeID,x.TradeBegin,x.TradeEnd,
                            x.TradeState,x.Id} );
        }
        finally {
            sdb.endTransaction();
            sdb.close();
        }
    }
    public void DeleteArriveTime(ArriveTime x) {
        SQLiteDatabase sdb = ylsqlHelper.getWritableDatabase();
        sdb.beginTransaction();
        try {
            sdb.execSQL("DELETE FROM ArriveTime where Id=? ",
                    new Object[]{x.Id} );
        }
        finally {
            sdb.endTransaction();
            sdb.close();
        }
    }

    public List<ArriveTime> GetArriveTime(String where){
        List<ArriveTime> lstArriveTime=new ArrayList<>();
        SQLiteDatabase sdb =ylsqlHelper.getReadableDatabase();

        Cursor cursor = sdb.rawQuery("select * from ArriveTime " + where, null);
        while(cursor.moveToNext()){
            int Id = cursor.getInt(cursor.getColumnIndex("Id"));
            String ServerReturn = cursor.getString(cursor.getColumnIndex("ServerReturn"));
            String EmpID = cursor.getString(cursor.getColumnIndex("EmpID"));
            String ATime = cursor.getString(cursor.getColumnIndex("ATime"));
            String TimeID = cursor.getString(cursor.getColumnIndex("TimeID"));
            String TradeBegin = cursor.getString(cursor.getColumnIndex("TradeBegin"));
            String TradeEnd = cursor.getString(cursor.getColumnIndex("TradeEnd"));
            String TradeState = cursor.getString(cursor.getColumnIndex("TradeState"));

            ArriveTime a=new ArriveTime();
            a.Id=Id;
            a.ServerReturn=ServerReturn;
            a.EmpID=EmpID;
            a.ATime=ATime;
            a.TimeID=TimeID;
            a.TradeBegin=TradeBegin;
            a.TradeEnd=TradeEnd;
            a.TradeState=TradeState;

            lstArriveTime.add(a);
        }
        sdb.close(); //关闭数据库
        return lstArriveTime;
    }

    public void InsertArriveTime(List<ArriveTime> lst) {
        SQLiteDatabase sdb = ylsqlHelper.getWritableDatabase();
        sdb.beginTransaction();
        try {
            for (ArriveTime x : lst) {
                sdb.execSQL("INSERT INTO ArriveTime(ServerReturn, EmpID, ATime, TimeID, TradeBegin," +
                                " TradeEnd, TradeState) VALUES   (?,?,?,?,?,?,?,)",
                        new Object[]{x.ServerReturn, x.EmpID, x.ATime, x.TimeID, x.TradeBegin, x.TradeEnd, x.TradeState});
            }
        }
        finally {
            sdb.endTransaction();
            sdb.close();
        }

    }
    public void UpdateArriveTime(List<ArriveTime> lst) {
        SQLiteDatabase sdb = ylsqlHelper.getWritableDatabase();
        sdb.beginTransaction();
        try {
            for (ArriveTime x : lst) {
                sdb.execSQL("UPDATE  ArriveTime SET ServerReturn =?, EmpID =?, ATime =?, TimeID =?," +
                                " TradeBegin =?, TradeEnd =?, TradeState =? where Id=?",
                        new Object[]{x.ServerReturn, x.EmpID, x.ATime, x.TimeID, x.TradeBegin, x.TradeEnd,
                                x.TradeState, x.Id});
            }
        }
        finally {
            sdb.endTransaction();
            sdb.close();
        }
    }
    public void DeleteArriveTime(List<ArriveTime> lst) {
        SQLiteDatabase sdb = ylsqlHelper.getWritableDatabase();
        sdb.beginTransaction();
        try {
            for (ArriveTime x : lst) {
                sdb.execSQL("DELETE FROM ArriveTime where Id=? ", new Object[]{x.Id});
            }
        }
        finally {
            sdb.endTransaction();
            sdb.close();
        }
    }

}
