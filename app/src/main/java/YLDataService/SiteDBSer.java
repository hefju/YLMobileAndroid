package YLDataService;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import TaskClass.Site;

/**
 * Created by asus on 2015/1/31.
 */
public class SiteDBSer {
    private YLSQLHelper ylsqlHelper;

    public SiteDBSer(Context context){this.ylsqlHelper = new YLSQLHelper(context);}

    public void InsertSite(Site x) {
        SQLiteDatabase sdb = ylsqlHelper.getWritableDatabase();
        sdb.beginTransaction();
        try {
            sdb.execSQL("INSERT INTO Site(ServerReturn, TaskID, SiteID, SiteName, SiteManager, " +
                    "SiteManagerPhone, SiteType, Status, ATMCount) VALUES (?,?,?,?,?,?,?,?,?)"
                    ,new Object[]{x.ServerReturn,x.TaskID,x.SiteID,x.SiteName,x.SiteManager,
                    x.SiteManagerPhone,x.SiteType,x.Status,x.ATMCount} );
        }
        finally {
            sdb.endTransaction();
            sdb.close();
        }
    }
    public void UpdateSite(Site x) {
        SQLiteDatabase sdb = ylsqlHelper.getWritableDatabase();
        sdb.beginTransaction();
        try {
            sdb.execSQL("UPDATE  Site SET ServerReturn =?, TaskID =?, SiteID =?, SiteName =?, " +
                        "SiteManager =?, SiteManagerPhone =?, SiteType =?, Status =?, ATMCount =?" +
                            " where id=?",new Object[]{x.ServerReturn,x.TaskID,x.SiteID,x.SiteName,
                    x.SiteManager,x.SiteManagerPhone,x.SiteType,x.Status,x.ATMCount,x.Id} );
        }
        finally {
            sdb.endTransaction();
            sdb.close();
        }
    }
    public void DeleteSite(Site x) {
        SQLiteDatabase sdb = ylsqlHelper.getWritableDatabase();
        sdb.beginTransaction();
        try {
            sdb.execSQL("delete from Site where id=?",new Object[]{x.Id} );
        }
        finally {
            sdb.endTransaction();
            sdb.close();
        }
    }

    //根据条件查找box
    public List<Site> GetSites(String where){
        List<Site> lstSite=new ArrayList<>();
        SQLiteDatabase sdb =ylsqlHelper.getReadableDatabase();

        Cursor cursor = sdb.rawQuery("select * from Site " + where, null);
        while(cursor.moveToNext()){
            int Id = cursor.getInt(cursor.getColumnIndex("Id"));
            String ServerReturn = cursor.getString(cursor.getColumnIndex("ServerReturn"));
            String TaskID = cursor.getString(cursor.getColumnIndex("TaskID"));
            String SiteID = cursor.getString(cursor.getColumnIndex("SiteID"));
            String SiteName = cursor.getString(cursor.getColumnIndex("SiteName"));
            String SiteManager = cursor.getString(cursor.getColumnIndex("SiteManager"));
            String SiteManagerPhone = cursor.getString(cursor.getColumnIndex("SiteManagerPhone"));
            String SiteType = cursor.getString(cursor.getColumnIndex("SiteType"));
            String Status = cursor.getString(cursor.getColumnIndex("Status"));
            String ATMCount = cursor.getString(cursor.getColumnIndex("ATMCount"));

            Site s=new Site();
            s.Id=Id;
            s.ServerReturn=ServerReturn;
            s.TaskID=TaskID;
            s.SiteID=SiteID;
            s.SiteName=SiteName;
            s.SiteManager=SiteManager;
            s.SiteManagerPhone=SiteManagerPhone;
            s.SiteType=SiteType;
            s.Status=Status;
            s.ATMCount=ATMCount;

            lstSite.add(s);
        }
        sdb.close(); //关闭数据库
        return lstSite;
    }
    public void InsertSite(List<Site> lst) {
        SQLiteDatabase sdb = ylsqlHelper.getWritableDatabase();
        sdb.beginTransaction();
        try {
            for (Site x : lst) {
                sdb.execSQL("INSERT INTO Site(ServerReturn, TaskID, SiteID, SiteName, SiteManager, " +
                        "SiteManagerPhone, SiteType, Status, ATMCount) VALUES (?,?,?,?,?,?,?,?,?)"
                        , new Object[]{x.ServerReturn, x.TaskID, x.SiteID, x.SiteName, x.SiteManager,
                        x.SiteManagerPhone, x.SiteType, x.Status, x.ATMCount});
            }
        }
        finally {
            sdb.endTransaction();
            sdb.close();
        }

    } public void UpdateSite(List<Site> lst) {
        SQLiteDatabase sdb = ylsqlHelper.getWritableDatabase();
        sdb.beginTransaction();
        try {
            for (Site x : lst) {
                sdb.execSQL("UPDATE  Site SET ServerReturn =?, TaskID =?, SiteID =?, SiteName =?, " +
                        "SiteManager =?, SiteManagerPhone =?, SiteType =?, Status =?, ATMCount =?" +
                        " where id=?", new Object[]{x.ServerReturn, x.TaskID, x.SiteID, x.SiteName,
                        x.SiteManager, x.SiteManagerPhone, x.SiteType, x.Status, x.ATMCount, x.Id});
            }
        }
        finally {
            sdb.endTransaction();
            sdb.close();
        }
    }
        public void DeleteSite(List<Site> lst) {
            SQLiteDatabase sdb = ylsqlHelper.getWritableDatabase();
            sdb.beginTransaction();
            try {
                for (Site x : lst) {
                    sdb.execSQL("delete from Site where id=?", new Object[]{x.Id});
                }
            }
            finally {
                sdb.endTransaction();
                sdb.close();
            }
        }


}
