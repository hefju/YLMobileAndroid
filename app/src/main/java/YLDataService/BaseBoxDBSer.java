package YLDataService;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import TaskClass.BaseBox;
import TaskClass.Box;

/**
 * Created by rush on 2015/2/11.
 */
public class BaseBoxDBSer {
    private YLSQLHelper ylsqlHelper;

    public BaseBoxDBSer(Context context){this.ylsqlHelper = new YLSQLHelper(context);}

    //根据条件查找box
    public List<BaseBox> GetBaseBoxs(String where){
        List<BaseBox> lstBaseBox=new ArrayList<>();
        SQLiteDatabase sdb =ylsqlHelper.getReadableDatabase();

        Cursor cursor = sdb.rawQuery("select * from BaseBox " + where, null);
        while(cursor.moveToNext()){
            int Id = cursor.getInt(cursor.getColumnIndex("Id"));
            String ServerReturn = cursor.getString(cursor.getColumnIndex("ServerReturn"));
            String BoxID = cursor.getString(cursor.getColumnIndex("BoxID"));
            String BoxName = cursor.getString(cursor.getColumnIndex("BoxName"));
            String BoxUHFNo = cursor.getString(cursor.getColumnIndex("BoxUHFNo"));
            String BoxBCNo = cursor.getString(cursor.getColumnIndex("BoxBCNo"));
            String BoxType = cursor.getString(cursor.getColumnIndex("BoxType"));
            String ClientID = cursor.getString(cursor.getColumnIndex("ClientID"));
            String SiteID = cursor.getString(cursor.getColumnIndex("SiteID"));

            BaseBox b=new BaseBox();
            b.Id=Id;
            b.ServerReturn=ServerReturn;
            b.BoxID=BoxID;
            b.BoxName=BoxName;
            b.BoxUHFNo=BoxUHFNo;
            b.BoxBCNo=BoxBCNo;
            b.BoxType=BoxType;
            b.ClientID=ClientID;
            b.SiteID=SiteID;

            lstBaseBox.add(b);
        }
        sdb.close(); //关闭数据库
        return lstBaseBox;
    }

    //批量插入box
    public void InsertBox(List<BaseBox> lst) {
        SQLiteDatabase sdb = ylsqlHelper.getWritableDatabase();
        sdb.beginTransaction();
        try {
            for (BaseBox x : lst) {
                sdb.execSQL("INSERT INTO BaseBox (ServerReturn, BoxID, BoxName, BoxUHFNo, BoxBCNo," +
                                " BoxType, ClientID, SiteID) VALUES     (?,?,?,?,?,?,?,?)",
                        new Object[]{x.ServerReturn,x.BoxID,x.BoxName,x.BoxUHFNo,x.BoxBCNo,x.BoxType,
                                x.ClientID,x.SiteID });
                sdb.setTransactionSuccessful();
            }
        }
        finally {
            sdb.endTransaction();
            sdb.close(); //关闭数据库
        }

    }

    //批量更新box
    public void UpdateBox(List<BaseBox> lst) {
        SQLiteDatabase sdb = ylsqlHelper.getWritableDatabase();
        sdb.beginTransaction();
        try {
            for (BaseBox x : lst) {
                sdb.execSQL("UPDATE BaseBox SET ServerReturn =?, BoxID =?, BoxName =?, BoxUHFNo =?," +
                                " BoxBCNo =?, BoxType =?, ClientID =?, SiteID =? where Id=?",
                        new Object[]{x.ServerReturn,x.BoxID,x.BoxName,x.BoxUHFNo,x.BoxBCNo,x.BoxType,
                                x.ClientID,x.SiteID, x.Id});
                sdb.setTransactionSuccessful();
            }
        }
        finally {
            sdb.endTransaction();
            sdb.close(); //关闭数据库
        }

    }

    public void DeleteBox(List<BaseBox> lst) {
        SQLiteDatabase sdb = ylsqlHelper.getWritableDatabase();
        sdb.beginTransaction();
        try {
            for (BaseBox x : lst) {
                sdb.execSQL("DELETE FROM BaseBox where Id=?", new Object[]{x.Id});
                sdb.setTransactionSuccessful();
            }
        }
        finally {
            sdb.endTransaction();
            sdb.close(); //关闭数据库
        }

    }
}