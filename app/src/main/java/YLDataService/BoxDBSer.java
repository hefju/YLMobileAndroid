package YLDataService;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

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
            sdb.setTransactionSuccessful();
            sdb.endTransaction();
            sdb.close(); //关闭数据库
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
            sdb.setTransactionSuccessful();
            sdb.endTransaction();
            sdb.close(); //关闭数据库
        }

    }
    public void DeleteBox(Box x) {
        SQLiteDatabase sdb = ylsqlHelper.getWritableDatabase();
        sdb.beginTransaction();
        try {
            sdb.execSQL("DELETE FROM Box where Id=?", new Object[]{x.Id} );

        }
        finally {
            sdb.setTransactionSuccessful();
            sdb.endTransaction();
            sdb.close(); //关闭数据库
        }

    }


    //根据条件查找box
    public List<Box> GetBoxs(String where){
        List<Box> lstBox=new ArrayList<>();
        SQLiteDatabase sdb =ylsqlHelper.getReadableDatabase();

        Cursor cursor = sdb.rawQuery("select * from Box " + where, null);
        while(cursor.moveToNext()){
            int Id = cursor.getInt(cursor.getColumnIndex("Id"));
            String ServerReturn = cursor.getString(cursor.getColumnIndex("ServerReturn"));
            String SiteID = cursor.getString(cursor.getColumnIndex("SiteID"));
            String BoxID = cursor.getString(cursor.getColumnIndex("BoxID"));
            String BoxName = cursor.getString(cursor.getColumnIndex("BoxName"));
            String TradeAction = cursor.getString(cursor.getColumnIndex("TradeAction"));
            String BoxStatus = cursor.getString(cursor.getColumnIndex("BoxStatus"));
            String BoxType = cursor.getString(cursor.getColumnIndex("BoxType"));
            String NextOutTime = cursor.getString(cursor.getColumnIndex("NextOutTime"));
            String ActionTime = cursor.getString(cursor.getColumnIndex("ActionTime"));
            Integer TimeID = cursor.getInt(cursor.getColumnIndex("TimeID"));

            Box b=new Box();
            b.Id=Id;
            b.ServerReturn=ServerReturn;
            b.SiteID=SiteID;
            b.BoxID=BoxID;
            b.BoxName=BoxName;
            b.TradeAction=TradeAction;
            b.BoxStatus=BoxStatus;
            b.BoxType=BoxType;
            b.NextOutTime=NextOutTime;
            b.ActionTime=ActionTime;
            b.TimeID=TimeID;
            lstBox.add(b);
        }
        sdb.close(); //关闭数据库
        return lstBox;
    }

    //批量插入box
    public void InsertBox(List<Box> lst) {
        SQLiteDatabase sdb = ylsqlHelper.getWritableDatabase();
        sdb.beginTransaction();
        try {
            for (Box x : lst) {
                sdb.execSQL("INSERT INTO Box(ServerReturn, SiteID, BoxID, BoxName, TradeAction, BoxStatus," +
                                " BoxType, NextOutTime, ActionTime, TimeID) VALUES   (?,?,?,?,?,?,?,?,?,?,)",
                        new Object[]{x.ServerReturn, x.SiteID, x.BoxID, x.BoxName,
                                x.TradeAction, x.BoxStatus, x.BoxType, x.NextOutTime, x.ActionTime, x.TimeID});

            }
        }
        finally {
            sdb.setTransactionSuccessful();
            sdb.endTransaction();
            sdb.close(); //关闭数据库
        }

    }

    //批量更新box
    public void UpdateBox(List<Box> lst) {
        SQLiteDatabase sdb = ylsqlHelper.getWritableDatabase();
        sdb.beginTransaction();
        try {
            for (Box x : lst) {
                sdb.execSQL("UPDATE Box SET ServerReturn =?, SiteID =?, BoxID =?, BoxName =?, TradeAction =?," +
                                " BoxStatus =?, BoxType =?, NextOutTime =?, ActionTime =?, TimeID =? where Id=?",
                        new Object[]{x.ServerReturn, x.SiteID, x.BoxID, x.BoxName, x.TradeAction, x.BoxStatus,
                                x.BoxType, x.NextOutTime, x.ActionTime, x.TimeID, x.Id});

            }
        }
        finally {
            sdb.setTransactionSuccessful();
            sdb.endTransaction();
            sdb.close(); //关闭数据库
        }

    }

    public void DeleteBox(List<Box> lst) {
        SQLiteDatabase sdb = ylsqlHelper.getWritableDatabase();
        sdb.beginTransaction();
        try {
            for (Box x : lst) {
                sdb.execSQL("DELETE FROM Box where Id=?", new Object[]{x.Id});

            }
        }
        finally {
            sdb.setTransactionSuccessful();
            sdb.endTransaction();
            sdb.close(); //关闭数据库
        }

    }
}
