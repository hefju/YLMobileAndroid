package YLDataService;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import TaskClass.BaseATMMachine;
import YLSystemDate.YLSystem;

/**
 * Created by Administrator on 2016-11-18.
 */

public class ATMMachineDBSer {
    private YLSQLHelper ylsqlHelper;
    private Context context;

    public ATMMachineDBSer (Context context){
        this.ylsqlHelper = new YLSQLHelper(context);
        this.context = context;
    }



    public void DeleteAll() {
        SQLiteDatabase sdb = ylsqlHelper.getWritableDatabase();
        sdb.beginTransaction();
        try {
            sdb.execSQL("DELETE FROM BaseATMMachine ");
        }
        finally {
            sdb.setTransactionSuccessful();
            sdb.endTransaction();
            sdb.close(); //关闭数据库
        }
    }

    public void Ins(List<BaseATMMachine> lst){
        SQLiteDatabase sdb = ylsqlHelper.getWritableDatabase();
        try{
            String sql = "INSERT INTO BaseATMMachine (ServerReturn, MachineID, SiteID, MachineName, MachineType," +
                    " MachineNo, MachineHFNo, MachineCode, Mark, ServerTime) "+
                    " VALUES     (?,?,?,?,?,?,?,?,?,?)";
            SQLiteStatement sqLiteStatement = sdb.compileStatement(sql);
            sdb.beginTransaction();
            for (BaseATMMachine x : lst){
                sqLiteStatement.bindString(1,x.getServerReturn());
                sqLiteStatement.bindString(2,x.getMachineID());
                sqLiteStatement.bindString(3,x.getSiteID());
                sqLiteStatement.bindString(4,x.getMachineName());
                sqLiteStatement.bindString(5,x.getMachineType());
                sqLiteStatement.bindString(6,x.getMachineNo());
                sqLiteStatement.bindString(7, x.getMachineHFNo());
                sqLiteStatement.bindString(8, x.getMachineCode());
                sqLiteStatement.bindString(9, x.getMark());
                sqLiteStatement.bindString(10, x.getServerTime());
                sqLiteStatement.executeInsert();
                sqLiteStatement.clearBindings();
            }
            sdb.setTransactionSuccessful();

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            sdb.endTransaction();
            sdb.close();
        }
    }

    public void upda(List<BaseATMMachine> lst){
        SQLiteDatabase sdb = ylsqlHelper.getWritableDatabase();
        sdb.beginTransaction();
        try {
            for (BaseATMMachine x : lst) {
                sdb.execSQL("UPDATE BaseATMMachine SET  ServerReturn =?, SiteID =?, MachineName =?," +
                                " MachineType =?, MachineNo =?, MachineHFNo =?, MachineCode =?,"+
                                "  Mark =?,ServerTime =?  where MachineID=?",
                        new Object[]{x.getServerReturn(),x.getSiteID(),x.getMachineName(),x.getMachineType(),
                                x.getMachineNo(),x.getMachineHFNo(),x.getMachineCode()
                                ,x.getMark(),x.getServerTime(),x.getMachineID()});
            }
        }
        finally {
            sdb.setTransactionSuccessful();
            sdb.endTransaction();
            sdb.close(); //关闭数据库
        }
    }

    public void del(List<BaseATMMachine> lst){
        SQLiteDatabase sdb = ylsqlHelper.getWritableDatabase();
        sdb.beginTransaction();
        try {
            for (BaseATMMachine x : lst) {
                sdb.execSQL("DELETE FROM BaseATMMachine where MachineID=?", new Object[]{x.getMachineID()});
            }
        }
        finally {
            sdb.setTransactionSuccessful();
            sdb.endTransaction();
            sdb.close(); //关闭数据库
        }
    }


    private BaseATMMachine FindATMMachine (String atmboxcode){
        SQLiteDatabase sdb = ylsqlHelper.getReadableDatabase();
        Cursor c = sdb.rawQuery("select * from BaseATMMachine where MachineCode = ?",new String[]{atmboxcode});
//        ATMBox atmBox = new ATMBox();
        BaseATMMachine b = new BaseATMMachine();
        if (c.getCount() >0){
            while (c.moveToNext()){
                b.setServerReturn(c.getString(c.getColumnIndex("ServerReturn")));
                b.setMachineID(c.getString(c.getColumnIndex("MachineID")));
                b.setSiteID(c.getString(c.getColumnIndex("SiteID")));
                b.setMachineName(c.getString(c.getColumnIndex("MachineName")));
                b.setMachineType(c.getString(c.getColumnIndex("MachineType")));
                b.setMachineNo(c.getString(c.getColumnIndex("MachineNo")));
                b.setMachineCode(c.getString(c.getColumnIndex("MachineCode")));
                b.setMark(c.getString(c.getColumnIndex("Mark")));
                b.setServerTime(c.getString(c.getColumnIndex("ServerTime")));
                b.setMachineHFNo(c.getString(c.getColumnIndex("MachineHFNo")));

            }
        }else{
            b.setMachineID("0");
            b.setMachineName("海盾押运");
            b.setSiteID("0");
        }
        sdb.close();
        return b;
    }

    public BaseATMMachine GetATMMachineinfo (String boxcode){
        boxcode = YLBoxScanCheck.replaceBlank(boxcode);
        BaseATMMachine box = new BaseATMMachine();
//        if (boxcode.length() !=10){
//            box.setMachineID("0");
//            box.setMachineName("海盾押运");
//            box.setSiteID("海盾钞箱");
//        }else {
//            BaseClientDBSer b = new BaseClientDBSer(context);
//            box = FindATMMachine(boxcode);
//            box.setClientName(b.GetClientName(box.getClientID()));
//            box.setUseClientName(b.GetClientName(box.getUseClientID()));
//        }
        Log.e(YLSystem.getKimTag(),box.toString());
        return box;
    }


    public void CacheBaseATMMachine( List<BaseATMMachine> list){
        List<BaseATMMachine> add = new ArrayList<>();
        List<BaseATMMachine> update = new ArrayList<>();
        List<BaseATMMachine> del = new ArrayList<>();

        for (BaseATMMachine box : list) {
            if (box.getMark() == null) continue;
            switch (box.getMark()){
                case "1":add.add(box);
                    break;
                case "2":update.add(box);
                    break;
                case "3":del.add(box);
                    break;
            }
        }
        if (add.size()>0){
            Ins(add);
        }
        if (update.size()>0){
            upda(update);
        }
        if (del.size()>0){
            del(del);
        }
    }


}
