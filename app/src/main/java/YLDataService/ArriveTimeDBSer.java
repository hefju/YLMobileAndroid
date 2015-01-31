package YLDataService;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import TaskClass.ArriveTime;

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
        }
    }
}
