package ylescort.ylmobileandroid;

import android.app.Application;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.ApplicationTestCase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import TaskClass.ATMBox;
import TaskClass.BaseATMBox;
import YLDataService.ATMBoxDBSer;
import YLDataService.BaseClientDBSer;
import YLDataService.YLSQLHelper;
import YLSystemDate.YLSystem;

/**
 * Created by Administrator on 2016-11-08.
 */

public class SQLDatatest extends ApplicationTestCase<Application> {

    public SQLDatatest() { super(Application.class); }

    public  void testSelClient(){
        BaseClientDBSer b = new BaseClientDBSer(getContext());
        String str = b.GetClientName("321213");
        Log.e(YLSystem.getKimTag(),str);
    }

    public void testseldata(){
        ATMBoxDBSer a = new ATMBoxDBSer(getContext());
        ATMBox ab = new ATMBox(a.GetATMboxinfo("0115041135"));
        Log.e(YLSystem.getKimTag(),ab.toString());
    }

    public void testInsData(){
        List<BaseATMBox> bl = new ArrayList<>();
        BaseATMBox b = new BaseATMBox();
        ATMBoxDBSer a = new ATMBoxDBSer(getContext());
        b.setATMBoxID("1");
        b.setClientID("1");
        b.setUseClientID("1");
        b.setBoxCode("0115041135");
        b.setBoxBrand("1");
        b.setBoxName("测试一");
        b.setBoxtype("1");
        b.setBoxvalue("100");
        b.setPassageway("1");
        b.setServerReturn("1");
        b.setMark("1");
        b.setServerTime("1");
        bl.add(b);
        a.Ins(bl);

    }

    public void testDropTable(){
        YLSQLHelper ylsqlHelper = new YLSQLHelper(getContext());
        SQLiteDatabase sdb = ylsqlHelper.getWritableDatabase();
        sdb.execSQL("drop table BaseATMBox");
    }

    public void testdeletetaskmanager(){

        YLSQLHelper y = new YLSQLHelper(getContext());
        SQLiteDatabase sdb = y.getWritableDatabase();
        String date = "2016-12-01";
        String sql = "delete from TasksManager where TaskDate <'"+date+"'";

        sdb.execSQL(sql);

    }

    public void testCounttaskmanager(){

        YLSQLHelper y = new YLSQLHelper(getContext());
        SQLiteDatabase sdb = y.getWritableDatabase();
        int Count = 0;
        Cursor cursor = sdb.rawQuery("SELECT count(*) as count  FROM TasksManager WHERE   TaskDate > '2016-12-01' ",null);
        while (cursor.moveToNext()){
            Count = cursor.getInt(cursor.getColumnIndex("count"));
        }
        Log.e(YLSystem.getKimTag(),Count+"数量");

    }


}
