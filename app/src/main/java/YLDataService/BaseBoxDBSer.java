package YLDataService;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

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

    public BaseBox GetBoxByBCNo(String BCNo){

        SQLiteDatabase sdb = ylsqlHelper.getReadableDatabase();
        Cursor cursor = sdb.rawQuery("select * from BaseBox where BoxBCNo=?", new String[]{BCNo});
        BaseBox b = new BaseBox();
        if (cursor.getCount() < 1) {
            b.BoxBCNo = BCNo;
            b.BoxName = "无数据";
            b.BoxType = "款箱";
            sdb.close(); //关闭数据库
            return b;
        } else {

            while (cursor.moveToNext()) {
                int Id = cursor.getInt(cursor.getColumnIndex("Id"));
                String ServerReturn = cursor.getString(cursor.getColumnIndex("ServerReturn"));
                String BoxID = cursor.getString(cursor.getColumnIndex("BoxID"));
                String BoxName = cursor.getString(cursor.getColumnIndex("BoxName"));
                String BoxUHFNo = cursor.getString(cursor.getColumnIndex("BoxUHFNo"));
                String BoxBCNo = cursor.getString(cursor.getColumnIndex("BoxBCNo"));
                String BoxType = cursor.getString(cursor.getColumnIndex("BoxType"));
                String ClientID = cursor.getString(cursor.getColumnIndex("ClientID"));
                String SiteID = cursor.getString(cursor.getColumnIndex("SiteID"));

                b.Id = Id;
                b.ServerReturn = ServerReturn;
                b.BoxID = BoxID;
                b.BoxName = BoxName;
                b.BoxUHFNo = BoxUHFNo;
                b.BoxBCNo = BoxBCNo;
                b.BoxType = BoxType;
                b.ClientID = ClientID;
                b.SiteID = SiteID;

                sdb.close(); //关闭数据库
                return b;
            }
        }
        sdb.close(); //关闭数据库
        return null;
    }

    //批量插入box
    public void InsertBox(List<BaseBox> lst) {
        SQLiteDatabase sdb = ylsqlHelper.getWritableDatabase();
        sdb.beginTransaction();
        try {
            for (BaseBox x : lst) {
                sdb.execSQL("INSERT INTO BaseBox (ServerReturn, BoxID, BoxName, BoxUHFNo, BoxBCNo," +
                                " BoxType, ClientID, SiteID) VALUES     (?,?,?,?,?,?,?,?)",
                        new Object[]{x.ServerReturn, x.BoxID, x.BoxName, x.BoxUHFNo, x.BoxBCNo, x.BoxType,
                                x.ClientID, x.SiteID});
            }
        }
        finally {
            sdb.setTransactionSuccessful();
            sdb.endTransaction();
            sdb.close(); //关闭数据库
        }
    }

    public void InsertBox2(List<BaseBox> lst) {
        SQLiteDatabase sdb = ylsqlHelper.getWritableDatabase();
        try{
            String sql = "INSERT INTO BaseBox (ServerReturn, BoxID, BoxName, BoxUHFNo, BoxBCNo," +
                    " BoxType, ClientID, SiteID) VALUES     (?,?,?,?,?,?,?,?)";
            SQLiteStatement sqLiteStatement = sdb.compileStatement(sql);
            sdb.beginTransaction();
            for (BaseBox x : lst){
                sqLiteStatement.bindString(1,x.ServerReturn);
                sqLiteStatement.bindString(2,x.BoxID);
                sqLiteStatement.bindString(3,x.BoxName);
                sqLiteStatement.bindString(4,x.BoxUHFNo);
                sqLiteStatement.bindString(5,x.BoxBCNo);
                sqLiteStatement.bindString(6,x.BoxType);
                sqLiteStatement.bindString(7,x.ClientID);
                sqLiteStatement.bindString(8, x.SiteID);
                long result = sqLiteStatement.executeInsert();
            }
            sdb.setTransactionSuccessful();

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            sdb.endTransaction();
            sdb.close();
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
                        new Object[]{x.ServerReturn, x.BoxID, x.BoxName, x.BoxUHFNo, x.BoxBCNo, x.BoxType,
                                x.ClientID, x.SiteID, x.Id});
            }
        }
        finally {
            sdb.setTransactionSuccessful();
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
            }
        }
        finally {
            sdb.setTransactionSuccessful();
            sdb.endTransaction();
            sdb.close(); //关闭数据库
        }

    }

    public void DeleteBaseEmpByBoxID(List<BaseBox> lst) {
        SQLiteDatabase sdb = ylsqlHelper.getWritableDatabase();
        sdb.beginTransaction();
        try {
            for (BaseBox x : lst) {
                sdb.execSQL("DELETE FROM BaseBox where BoxID=?", new Object[]{x.BoxID});
            }
        }
        finally {
            sdb.setTransactionSuccessful();
            sdb.endTransaction();
            sdb.close(); //关闭数据库
        }
    }

    public void UpdateBaseEmpByBoxID(List<BaseBox> lst) {
        SQLiteDatabase sdb = ylsqlHelper.getWritableDatabase();
        sdb.beginTransaction();
        try {
            for (BaseBox x : lst) {
                sdb.execSQL("UPDATE BaseBox SET ServerReturn =?, BoxID =?, BoxName =?, BoxUHFNo =?," +
                                " BoxBCNo =?, BoxType =?, ClientID =?, SiteID =? where BoxID=?",
                        new Object[]{x.ServerReturn,x.BoxID,x.BoxName,x.BoxUHFNo,x.BoxBCNo,x.BoxType,
                                x.ClientID,x.SiteID, x.BoxID});
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
            sdb.execSQL("DELETE FROM BaseBox ");
        }
        finally {
            sdb.setTransactionSuccessful();
            sdb.endTransaction();
            sdb.close(); //关闭数据库
        }
    }

    public int BaseBoxCount(){
        SQLiteDatabase sdb = ylsqlHelper.getReadableDatabase();
        int Count =0;
        sdb.beginTransaction();
        try {
            Cursor cursor = sdb.rawQuery("select count(*) as count from BaseBox ",null);
            while (cursor.moveToNext()){
                Count = cursor.getInt(cursor.getColumnIndex("count"));
            }
        }finally {
            sdb.setTransactionSuccessful();
            sdb.endTransaction();
            sdb.close();
        }
        return  Count;
    }

    public void CacheBaseBox(List<BaseBox> lst){
        ArrayList<BaseBox> lstAdd=new ArrayList<>();
        ArrayList<BaseBox> lstUpdate=new ArrayList<>();
        ArrayList<BaseBox> lstDel=new ArrayList<>();
        for (BaseBox x : lst){
            if(x.Mark==null)
                continue;
            if(x.Mark.equals("1")){
                lstAdd.add(x);
            }else if(x.Mark.equals("2")){
                lstUpdate.add(x);
            }else if(x.Mark.equals("3")){
                lstDel.add(x);
            }
        }
        if(lstDel.size()>0)
            DeleteBaseEmpByBoxID(lstDel);
        if(lstUpdate.size()>0)
            UpdateBaseEmpByBoxID(lstUpdate);//update 不能根据ID来update而是根据EmpID来update的
        if(lstAdd.size()>0)
            InsertBox(lstAdd);
    }

}
