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
import TaskClass.FingerPrint;
import YLDataService.ATMBoxDBSer;
import YLDataService.BaseClientDBSer;
import YLDataService.FingerPrintDBSer;
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

    //region   2018.12.15指纹测试
    String fpTag="unit_test";
    public void testInsFingerPrint(){
        FingerPrintDBSer dbSer=new FingerPrintDBSer(getContext());
        FingerPrint fingerPrint=new FingerPrint();
        fingerPrint.setEmpNum("600300");
        fingerPrint.setFinger("fp600300");
       Integer count=  dbSer.InsFingerPrint(fingerPrint);
        if(count!=1)
            Log.d(fpTag,"600300员工的指纹插入成功");
        else
            Log.e(fpTag,"600300员工的指纹插入失败");
    }
    public void testUpdateFingerPrint(){
        FingerPrintDBSer dbSer=new FingerPrintDBSer(getContext());
        FingerPrint fingerPrint=new FingerPrint();
        fingerPrint.setEmpNum("600034");
        fingerPrint.setFinger("fp600034update");
        Integer count=  dbSer.UpdateFingerPrint(fingerPrint);
        if(count!=1)
            Log.e(fpTag,"600034员工的指纹更新失败");
        else
            Log.d(fpTag,"600034员工的指纹更新成功");
    }

    public  void  testFingerPrint_Exists() {
        FingerPrintDBSer dbSer=new FingerPrintDBSer(getContext());
        FingerPrint fingerPrint=new FingerPrint();
        fingerPrint.setEmpNum("600300");
        fingerPrint.setFinger("fp600300");
        boolean exists=  dbSer.Exists(fingerPrint);

        if(!exists)
            Log.d(fpTag,"存在600300员工的指纹");
        else
            Log.e(fpTag,"不存在600300员工的指纹");
    }

    //获取所有的指纹,逐个打印出来,
    public  void  testGetAllFingerPrint() {
        FingerPrintDBSer dbSer=new FingerPrintDBSer(getContext());
        List<FingerPrint> list=dbSer.GetAllFingerPrint();
        for (int i=0;i<list.size();i++){
            FingerPrint f=list.get(i);
            Log.d("unit_test",f.toString());
        }
        if(list.size()<1)
            Log.e(fpTag,"指纹数据记录只有0条");
            else
            Log.d(fpTag,"指纹数据记录大于1条");
    }

    public void testGetFingerPrint(){
        FingerPrintDBSer dbSer=new FingerPrintDBSer(getContext());
        FingerPrint fingerPrint=new FingerPrint();
        fingerPrint.setEmpNum("600030");

        FingerPrint entity=dbSer.GetFingerPrint(fingerPrint);
        if(entity==null){
            Log.e(fpTag,"读取600030员工指纹失败.");
        }else{
            Log.d(fpTag,"读取单个员工成功:"+entity.toString());
        }

    }





    //小心,这是删除所有数据!!!!!!!
    public  void  testDeleteAllFingerPrint(){
        FingerPrintDBSer dbSer=new FingerPrintDBSer(getContext());
        int count=dbSer.DeleteAllFingerPrint();

        if(count<1)
            Log.e(fpTag,"指纹数据删除失败.");
        else
            Log.d(fpTag,"指纹数据删除成功.");
    }


    //end


}
