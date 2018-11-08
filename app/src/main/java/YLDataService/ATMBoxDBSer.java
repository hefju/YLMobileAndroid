package YLDataService;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.net.PortUnreachableException;
import java.util.ArrayList;
import java.util.List;
import TaskClass.ATMBox;
import TaskClass.BaseATMBox;
import YLSystemDate.YLSystem;


/**
 * Created by Administrator on 2016-11-07.
 */

public class ATMBoxDBSer {

    private YLSQLHelper ylsqlHelper;
    private Context context;

    public ATMBoxDBSer (Context context){
        this.ylsqlHelper = new YLSQLHelper(context);
        this.context = context;
    }

    public void DeleteAll() {
        SQLiteDatabase sdb = ylsqlHelper.getWritableDatabase();
        sdb.beginTransaction();
        try {
            sdb.execSQL("DELETE FROM BaseATMBox ");
        }
        finally {
            sdb.setTransactionSuccessful();
            sdb.endTransaction();
            sdb.close(); //关闭数据库
        }
    }

    public void Ins(List<BaseATMBox> lst){
        SQLiteDatabase sdb = ylsqlHelper.getWritableDatabase();
        try{
            String sql = "INSERT INTO BaseATMBox (ATMBoxID, ClientID, UseClientID, BoxCode, BoxName," +
                    " BoxBrand, Boxtype, Boxvalue, Passageway, ServerReturn, Mark,ServerTime) "+
                    " VALUES     (?,?,?,?,?,?,?,?,?,?,?,?)";
            SQLiteStatement sqLiteStatement = sdb.compileStatement(sql);
            sdb.beginTransaction();
            for (BaseATMBox x : lst){
                sqLiteStatement.bindString(1,x.getATMBoxID());
                sqLiteStatement.bindString(2,x.getClientID());
                sqLiteStatement.bindString(3,x.getUseClientID());
                sqLiteStatement.bindString(4,x.getBoxCode());
                sqLiteStatement.bindString(5,x.getBoxName());
                sqLiteStatement.bindString(6,x.getBoxBrand());
                sqLiteStatement.bindString(7, x.getBoxtype());
                sqLiteStatement.bindString(8, x.getBoxvalue());
                sqLiteStatement.bindString(9, x.getPassageway());
                sqLiteStatement.bindString(10, x.getServerReturn());
                sqLiteStatement.bindString(11, x.getMark());
                sqLiteStatement.bindString(12, x.getServerTime());
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

    public void upda(List<BaseATMBox> lst){
        SQLiteDatabase sdb = ylsqlHelper.getWritableDatabase();
        sdb.beginTransaction();
        try {
            for (BaseATMBox x : lst) {
                sdb.execSQL("UPDATE BaseATMBox SET  ClientID =?, UseClientID =?, BoxCode =?," +
                                " BoxName =?, BoxBrand =?, Boxtype =?, Boxvalue =?,"+
                        "  Passageway =?,ServerReturn =? ,Mark =?,ServerTime =? where ATMBoxID=?",
                        new Object[]{x.getClientID(),x.getUseClientID(),x.getBoxCode(),x.getBoxName(),
                                x.getBoxBrand(),x.getBoxtype(),x.getBoxvalue()
                                ,x.getPassageway(),x.getServerReturn(),x.getMark(),
                                x.getServerTime(),x.getATMBoxID()});
            }
        }
        finally {
            sdb.setTransactionSuccessful();
            sdb.endTransaction();
            sdb.close(); //关闭数据库
        }
    }

    public void del(List<BaseATMBox> lst){
        SQLiteDatabase sdb = ylsqlHelper.getWritableDatabase();
        sdb.beginTransaction();
        try {
            for (BaseATMBox x : lst) {
                sdb.execSQL("DELETE FROM BaseATMBox where ATMBoxID=?", new Object[]{x.getATMBoxID()});
            }
        }
        finally {
            sdb.setTransactionSuccessful();
            sdb.endTransaction();
            sdb.close(); //关闭数据库
        }
    }

    public int ATMboxcount(){
        SQLiteDatabase sdb = ylsqlHelper.getReadableDatabase();
        int Count =0;
        sdb.beginTransaction();
        try {
            Cursor cursor = sdb.rawQuery("select count(*) as count from BaseATMBox ",null);
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

    private ATMBox FindATMbox (String atmboxcode){
        SQLiteDatabase sdb = ylsqlHelper.getReadableDatabase();
        Cursor c = sdb.rawQuery("select * from BaseATMBox where BoxCode = ?",new String[]{atmboxcode});
        ATMBox atmBox = new ATMBox();
        if (c.getCount() >0){
            while (c.moveToNext()){
                atmBox.setClientID(c.getString(c.getColumnIndex("ClientID")));
                atmBox.setUseClientID(c.getString(c.getColumnIndex("UseClientID")));
                atmBox.setBoxCode(atmboxcode);
                atmBox.setBoxName(c.getString(c.getColumnIndex("BoxName")));
                atmBox.setBoxBrand(c.getString(c.getColumnIndex("BoxBrand")));
                atmBox.setBoxtype(c.getString(c.getColumnIndex("Boxtype")));
                atmBox.setBoxvalue(c.getString(c.getColumnIndex("Boxvalue")));
                atmBox.setPassageway(c.getString(c.getColumnIndex("Passageway")));
            }
        }else{
            atmBox.setClientID("0");
            atmBox.setClientName("海盾押运");
            atmBox.setBoxName("海盾钞箱");
        }
        sdb.close();
        return atmBox;
    }

    public ATMBox GetATMboxinfo (String boxcode){
         boxcode = YLBoxScanCheck.replaceBlank(boxcode);
        ATMBox box = new ATMBox();
        if (boxcode.length() !=10){
            box.setClientID("0");
            box.setClientName("海盾押运");
            box.setBoxName("海盾钞箱");
        }else {
            BaseClientDBSer b = new BaseClientDBSer(context);
            box = FindATMbox(boxcode);
            box.setClientName(b.GetClientName(box.getClientID()));
            box.setUseClientName(b.GetClientName(box.getUseClientID()));
        }
        Log.e(YLSystem.getKimTag(),box.toString());
        return box;
    }


    public void CacheBaseATMBox(List<BaseATMBox> list){
        List<BaseATMBox> add = new ArrayList<>();
        List<BaseATMBox> update = new ArrayList<>();
        List<BaseATMBox> del = new ArrayList<>();

        for (BaseATMBox box : list) {
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
