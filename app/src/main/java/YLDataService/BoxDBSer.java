package YLDataService;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import TaskClass.Box;

/**
 * Created by asus on 2015/1/31.
 */
public class BoxDBSer {
    private YLSQLHelper ylsqlHelper;

    public BoxDBSer(Context context){this.ylsqlHelper = new YLSQLHelper(context);}

    public void InsertBox(Box x) {
        SQLiteDatabase sdb = ylsqlHelper.getWritableDatabase();
        sdb.beginTransaction();
        try {
            sdb.execSQL("INSERT INTO Box(ServerReturn, SiteID, BoxID, BoxName, TradeAction, BoxStatus," +
                    " BoxType, NextOutTime, ActionTime, TimeID) VALUES   (?,?,?,?,?,?,?,?,?,?,)",
                    new Object[]{x.ServerReturn,x.SiteID,x.BoxID,x.BoxName,
                    x.TradeAction,x.BoxStatus,x.BoxType,x.NextOutTime,x.ActionTime,x.TimeID} );
        }
        finally {
            sdb.endTransaction();
        }

    }
    public void UpdateBox(Box x) {
        SQLiteDatabase sdb = ylsqlHelper.getWritableDatabase();
        sdb.beginTransaction();
        try {
            sdb.execSQL("UPDATE Box SET ServerReturn =?, SiteID =?, BoxID =?, BoxName =?, TradeAction =?," +
                            " BoxStatus =?, BoxType =?, NextOutTime =?, ActionTime =?, TimeID =? where Id=?",
                    new Object[]{x.ServerReturn,x.SiteID,x.BoxID,x.BoxName,x.TradeAction,x.BoxStatus,
                            x.BoxType,x.NextOutTime,x.ActionTime,x.TimeID,x.Id} );
        }
        finally {
            sdb.endTransaction();
        }

    }
    public void DeleteBox(Box x) {
        SQLiteDatabase sdb = ylsqlHelper.getWritableDatabase();
        sdb.beginTransaction();
        try {
            sdb.execSQL("DELETE FROM Box where Id=?", new Object[]{x.Id} );
        }
        finally {
            sdb.endTransaction();
        }

    }
}
