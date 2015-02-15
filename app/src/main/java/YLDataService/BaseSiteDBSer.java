package YLDataService;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import TaskClass.BaseSite;

/**
 * Created by rush on 2015/2/11.
 */
public class BaseSiteDBSer {
    private YLSQLHelper ylsqlHelper;

    public BaseSiteDBSer(Context context){this.ylsqlHelper = new YLSQLHelper(context);}

    //根据条件查找BaseSite
    public List<BaseSite> GetBaseSites(String where){
        List<BaseSite> lstBaseSite=new ArrayList<>();
        SQLiteDatabase sdb =ylsqlHelper.getReadableDatabase();

        Cursor cursor = sdb.rawQuery("select * from BaseSite " + where, null);
        while(cursor.moveToNext()){
            int Id = cursor.getInt(cursor.getColumnIndex("Id"));
            String ServerReturn = cursor.getString(cursor.getColumnIndex("ServerReturn"));
            String SiteID = cursor.getString(cursor.getColumnIndex("SiteID"));
            String SiteName = cursor.getString(cursor.getColumnIndex("SiteName"));
            String SiteType = cursor.getString(cursor.getColumnIndex("SiteType"));
            String ClientID = cursor.getString(cursor.getColumnIndex("ClientID"));

            BaseSite b=new BaseSite();
            b.Id=Id;
            b.ServerReturn=ServerReturn;
            b.SiteID=SiteID;
            b.SiteName=SiteName;
            b.SiteType=SiteType;
            b.ClientID=ClientID;

            lstBaseSite.add(b);
        }
        sdb.close(); //关闭数据库
        return lstBaseSite;
    }

    //批量插入BaseSite
    public void InsertBaseSite(List<BaseSite> lst) {
        SQLiteDatabase sdb = ylsqlHelper.getWritableDatabase();
        sdb.beginTransaction();
        try {
            for (BaseSite x : lst) {
                sdb.execSQL("INSERT INTO BaseSite ( ServerReturn, SiteID, SiteName, SiteType, ClientID)\n" +
                                "VALUES     (?,?,?,?,?)",
                        new Object[]{x.ServerReturn,x.SiteID,x.SiteName,x.SiteType,x.ClientID,
                        });

            }
        }
        finally {
            sdb.setTransactionSuccessful();
            sdb.endTransaction();
            sdb.close(); //关闭数据库
        }

    }

    //批量更新BaseSite
    public void UpdateBaseSite(List<BaseSite> lst) {
        SQLiteDatabase sdb = ylsqlHelper.getWritableDatabase();
        sdb.beginTransaction();
        try {
            for (BaseSite x : lst) {
                sdb.execSQL("UPDATE    BaseSite SET ServerReturn =?, SiteID =?, SiteName =?, SiteType =?, ClientID =? where Id=?",
                        new Object[]{x.ServerReturn,x.SiteID,x.SiteName,x.SiteType,x.ClientID, x.Id});

            }
        }
        finally {
            sdb.setTransactionSuccessful();
            sdb.endTransaction();
            sdb.close(); //关闭数据库
        }

    }
    public void UpdateBaseSiteByEmpID(List<BaseSite> lst) {
        SQLiteDatabase sdb = ylsqlHelper.getWritableDatabase();
        sdb.beginTransaction();
        try {
            for (BaseSite x : lst) {
                sdb.execSQL("UPDATE BaseSite SET ServerReturn =?, SiteID =?, SiteName =?, SiteType =?, " +
                                "ClientID =? where SiteID=?",
                        new Object[]{x.ServerReturn,x.SiteID,x.SiteName,x.SiteType,x.ClientID, x.SiteID});

            }
        }
        finally {
            sdb.setTransactionSuccessful();
            sdb.endTransaction();
            sdb.close(); //关闭数据库
        }

    }

    public void DeleteBaseSite(List<BaseSite> lst) {
        SQLiteDatabase sdb = ylsqlHelper.getWritableDatabase();
        sdb.beginTransaction();
        try {
            for (BaseSite x : lst) {
                sdb.execSQL("DELETE FROM BaseSite where Id=?", new Object[]{x.Id});

            }
        }
        finally {
            sdb.setTransactionSuccessful();
            sdb.endTransaction();
            sdb.close(); //关闭数据库
        }

    }

    public void DeleteBaseSiteByEmpID(List<BaseSite> lst) {
        SQLiteDatabase sdb = ylsqlHelper.getWritableDatabase();
        sdb.beginTransaction();
        try {
            for (BaseSite x : lst) {
                sdb.execSQL("DELETE FROM BaseSite where SiteID=?", new Object[]{x.SiteID});

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
            sdb.execSQL("DELETE FROM BaseSite ");
        }
        finally {
            sdb.setTransactionSuccessful();
            sdb.endTransaction();
            sdb.close(); //关闭数据库
        }
    }
}
