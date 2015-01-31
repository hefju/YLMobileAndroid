package YLDataService;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

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
        }
    }
}
