package YLDataService;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import TaskClass.BaseClient;
import TaskClass.BaseClient_HF;

/**
 * Created by rush on 2015-04-13.
 */
public class BaseClient_HF_DBSer {
    private YLSQLHelper ylsqlHelper;

    public BaseClient_HF_DBSer(Context context){this.ylsqlHelper = new YLSQLHelper(context);}

    //根据条件查找BaseClient_HF
    public List<BaseClient_HF> GetBaseClient_HFs(String where){
        List<BaseClient_HF> lstBaseClient_HF=new ArrayList<>();
        SQLiteDatabase sdb =ylsqlHelper.getReadableDatabase();

        Cursor cursor = sdb.rawQuery("select * from BaseClient_HF " + where, null);
        while(cursor.moveToNext()){
            int Id = cursor.getInt(cursor.getColumnIndex("Id"));
            String ServerReturn = cursor.getString(cursor.getColumnIndex("ServerReturn"));
            String ClientID = cursor.getString(cursor.getColumnIndex("ClientID"));
            String HFNo = cursor.getString(cursor.getColumnIndex("HFNo"));
            String Mark = cursor.getString(cursor.getColumnIndex("Mark"));
            String ServerTime = cursor.getString(cursor.getColumnIndex("ServerTime"));


            BaseClient_HF b=new BaseClient_HF();
            b.Id=Id;
            b.ServerReturn=ServerReturn;
            b.ClientID=ClientID;
            b.HFNo=HFNo;
            b.Mark=Mark;
            b.ServerTime=ServerTime;

            lstBaseClient_HF.add(b);
        }
        sdb.close(); //关闭数据库
        return lstBaseClient_HF;
    }

    //批量插入BaseClient_HF
    public void InsertBaseClient_HF(List<BaseClient_HF> lst) {
        SQLiteDatabase sdb = ylsqlHelper.getWritableDatabase();
        sdb.beginTransaction();
        try {
            for (BaseClient_HF x : lst) {
                sdb.execSQL("INSERT INTO BaseClient_HF (ServerReturn, ClientID, HFNo, Mark,ServerTime)\n" +
                                "VALUES     (?,?,?,?,?)",
                        new Object[]{x.ServerReturn,x.ClientID,x.HFNo,x.Mark,x.ServerTime });

            }
        }
        finally {
            sdb.setTransactionSuccessful();
            sdb.endTransaction();
            sdb.close(); //关闭数据库
        }

    }

    //批量更新BaseClient_HF
    public void UpdateBaseClient_HF(List<BaseClient_HF> lst) {
        SQLiteDatabase sdb = ylsqlHelper.getWritableDatabase();
        sdb.beginTransaction();
        try {
            for (BaseClient_HF x : lst) {
                sdb.execSQL("UPDATE BaseClient_HF SET ServerReturn =?, ClientID =?, HFNo =?," +
                                " Mark =?,ServerTime=? where Id=?",
                        new Object[]{x.ServerReturn,x.ClientID,x.HFNo,x.Mark, x.ServerTime,x.Id});

            }
        }
        finally {
            sdb.setTransactionSuccessful();
            sdb.endTransaction();
            sdb.close(); //关闭数据库
        }

    }
    public void UpdateBaseClient_HFByHFNo(List<BaseClient_HF> lst) {
        SQLiteDatabase sdb = ylsqlHelper.getWritableDatabase();
        sdb.beginTransaction();
        try {
            for (BaseClient_HF x : lst) {
                sdb.execSQL("UPDATE BaseClient_HF SET ServerReturn =?, ClientID =?, " +
                                " Mark =?,ServerTime=? where HFNo=?",
                        new Object[]{x.ServerReturn,x.ClientID,x.Mark, x.ServerTime,x.HFNo});

            }
        }
        finally {
            sdb.setTransactionSuccessful();
            sdb.endTransaction();
            sdb.close(); //关闭数据库
        }

    }

    public void DeleteBaseClient_HF(List<BaseClient_HF> lst) {
        SQLiteDatabase sdb = ylsqlHelper.getWritableDatabase();
        sdb.beginTransaction();
        try {
            for (BaseClient_HF x : lst) {
                sdb.execSQL("DELETE FROM BaseClient_HF where Id=?", new Object[]{x.Id});

            }
        }
        finally {
            sdb.setTransactionSuccessful();
            sdb.endTransaction();
            sdb.close(); //关闭数据库
        }

    }
    public void DeleteBaseClient_HFByHFNo(List<BaseClient_HF> lst) {
        SQLiteDatabase sdb = ylsqlHelper.getWritableDatabase();
        sdb.beginTransaction();
        try {
            for (BaseClient_HF x : lst) {
                sdb.execSQL("DELETE FROM BaseClient_HF where HFNo=?", new Object[]{x.HFNo});

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
            sdb.execSQL("DELETE FROM BaseClient_HF ");
        }
        finally {
            sdb.setTransactionSuccessful();
            sdb.endTransaction();
            sdb.close(); //关闭数据库
        }
    }
}
