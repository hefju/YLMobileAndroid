package YLDataService;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import TaskClass.BaseClient;

/**
 * Created by rush on 2015/2/11.
 */
public class BaseClientDBSer {
    private YLSQLHelper ylsqlHelper;

    public BaseClientDBSer(Context context){this.ylsqlHelper = new YLSQLHelper(context);}

    //根据条件查找BaseClient
    public List<BaseClient> GetBaseClients(String where){
        List<BaseClient> lstBaseClient=new ArrayList<>();
        SQLiteDatabase sdb =ylsqlHelper.getReadableDatabase();

        Cursor cursor = sdb.rawQuery("select * from BaseClient " + where, null);
        while(cursor.moveToNext()){
            int Id = cursor.getInt(cursor.getColumnIndex("Id"));
            String ServerReturn = cursor.getString(cursor.getColumnIndex("ServerReturn"));
            String ClientID = cursor.getString(cursor.getColumnIndex("ClientID"));
            String ClientName = cursor.getString(cursor.getColumnIndex("ClientName"));
            String ClientType = cursor.getString(cursor.getColumnIndex("ClientType"));
            String HFNo = cursor.getString(cursor.getColumnIndex("HFNo"));

            BaseClient b=new BaseClient();
            b.Id=Id;
            b.ServerReturn=ServerReturn;
            b.ClientID=ClientID;
            b.ClientName=ClientName;
            b.ClientType=ClientType;
            b.HFNo=HFNo;

            lstBaseClient.add(b);
        }
        sdb.close(); //关闭数据库
        return lstBaseClient;
    }

    //批量插入BaseClient
    public void InsertBaseClient(List<BaseClient> lst) {
        SQLiteDatabase sdb = ylsqlHelper.getWritableDatabase();
        sdb.beginTransaction();
        try {
            for (BaseClient x : lst) {
                sdb.execSQL("INSERT INTO BaseClient (ServerReturn, ClientID, ClientName, ClientType,HFNo)\n" +
                                "VALUES     (?,?,?,?,?)",
                        new Object[]{x.ServerReturn,x.ClientID,x.ClientName,x.ClientType,x.HFNo });

            }
        }
        finally {
            sdb.setTransactionSuccessful();
            sdb.endTransaction();
            sdb.close(); //关闭数据库
        }

    }

    //批量更新BaseClient
    public void UpdateBaseClient(List<BaseClient> lst) {
        SQLiteDatabase sdb = ylsqlHelper.getWritableDatabase();
        sdb.beginTransaction();
        try {
            for (BaseClient x : lst) {
                sdb.execSQL("UPDATE BaseClient SET ServerReturn =?, ClientID =?, ClientName =?," +
                                " ClientType =?,HFNo=? where Id=?",
                        new Object[]{x.ServerReturn,x.ClientID,x.ClientName,x.ClientType,x.HFNo, x.Id});

            }
        }
        finally {
            sdb.setTransactionSuccessful();
            sdb.endTransaction();
            sdb.close(); //关闭数据库
        }

    }
    public void UpdateBaseClientByEmpID(List<BaseClient> lst) {
        SQLiteDatabase sdb = ylsqlHelper.getWritableDatabase();
        sdb.beginTransaction();
        try {
            for (BaseClient x : lst) {
                sdb.execSQL("UPDATE BaseClient SET ServerReturn =?, ClientID =?, ClientName =?," +
                                " ClientType =?,HFNo=? where ClientID=?",
                        new Object[]{x.ServerReturn,x.ClientID,x.ClientName,x.ClientType,x.HFNo, x.ClientID});

            }
        }
        finally {
            sdb.setTransactionSuccessful();
            sdb.endTransaction();
            sdb.close(); //关闭数据库
        }

    }

    public void DeleteBaseClient(List<BaseClient> lst) {
        SQLiteDatabase sdb = ylsqlHelper.getWritableDatabase();
        sdb.beginTransaction();
        try {
            for (BaseClient x : lst) {
                sdb.execSQL("DELETE FROM BaseClient where Id=?", new Object[]{x.Id});

            }
        }
        finally {
            sdb.setTransactionSuccessful();
            sdb.endTransaction();
            sdb.close(); //关闭数据库
        }

    }
    public void DeleteBaseClientByEmpID(List<BaseClient> lst) {
        SQLiteDatabase sdb = ylsqlHelper.getWritableDatabase();
        sdb.beginTransaction();
        try {
            for (BaseClient x : lst) {
                sdb.execSQL("DELETE FROM BaseClient where ClientID=?", new Object[]{x.ClientID});

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
            sdb.execSQL("DELETE FROM BaseClient ");
        }
        finally {
            sdb.setTransactionSuccessful();
            sdb.endTransaction();
            sdb.close(); //关闭数据库
        }
    }

}
