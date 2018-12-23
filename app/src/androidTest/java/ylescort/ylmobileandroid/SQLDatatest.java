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
import TaskClass.BaseEmp;
import TaskClass.FingerPrint;
import TaskClass.User;
import YLDataService.ATMBoxDBSer;
import YLDataService.BaseClientDBSer;
import YLDataService.BaseEmpDBSer;
import YLDataService.EmpDBSer;
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
    String juTag="unit_test";
    //插入指纹
    public void testInsFingerPrint(){
        FingerPrintDBSer dbSer=new FingerPrintDBSer(getContext());
        FingerPrint fingerPrint=new FingerPrint();
        fingerPrint.setEmpNum("600300");
        fingerPrint.setFingerType("1");
        fingerPrint.setFinger("fp600300");
       int count=  dbSer.InsFingerPrint(fingerPrint);
        if(count>0)
            Log.e(juTag,"600300员工的指纹插入成功");
        else
            Log.e(juTag,"600300员工的指纹插入失败");
    }
    //指纹是否存在
    public  void  testFingerPrint_Exists() {
        FingerPrintDBSer dbSer=new FingerPrintDBSer(getContext());
        FingerPrint fingerPrint=new FingerPrint();
        fingerPrint.setEmpNum("600300");
        fingerPrint.setFinger("fp600300");
        boolean exists=  dbSer.Exists(fingerPrint);

        if(!exists)
            Log.e(juTag,"存在600300员工的指纹");
        else
            Log.e(juTag,"不存在600300员工的指纹");
    }
    //更新指纹
    public void testUpdateFingerPrint(){
        FingerPrintDBSer dbSer=new FingerPrintDBSer(getContext());
        FingerPrint fingerPrint=new FingerPrint();
        fingerPrint.setEmpNum("600034");
        fingerPrint.setFinger("fp600034update");
        Integer count=  dbSer.UpdateFingerPrint(fingerPrint);
        if(count!=1)
            Log.e(juTag,"600034员工的指纹更新失败");
        else
            Log.d(juTag,"600034员工的指纹更新成功");
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
            Log.e(juTag,"指纹数据记录只有0条");
            else
            Log.e(juTag,"指纹数据记录大于1条");
    }

    //获取单个运功的指纹
    public void testGetFingerPrint(){
        FingerPrintDBSer dbSer=new FingerPrintDBSer(getContext());
        FingerPrint fingerPrint=new FingerPrint();
        fingerPrint.setEmpNum("620142");

        List<FingerPrint> lstFP=dbSer.GetFingerPrint(fingerPrint);
        if(lstFP==null||lstFP.size()<1){
            Log.e(juTag,"读取620142员工指纹失败.");
        }else{
            Log.e(juTag,"读取单个员工成功, 数量为:"+ String.valueOf(lstFP.size()));
        }

    }

    //小心,这是删除所有数据!!!!!!!
    public  void  testDeleteAllFingerPrint(){
        FingerPrintDBSer dbSer=new FingerPrintDBSer(getContext());
        int count=dbSer.DeleteAllFingerPrint();

        if(count<1)
            Log.e(juTag,"指纹数据删除失败.");
        else
            Log.e(juTag,"指纹数据删除成功.");
    }
    public  void testDropFingerPrint(){
        FingerPrintDBSer dbSer=new FingerPrintDBSer(getContext());
        int count=dbSer.DropFingerPrint();

        if(count<1)
            Log.e(juTag,"指纹数据表drop失败.");
        else
            Log.e(juTag,"指纹数据表drop成功.");
    }
    //end

    //region 员工数据表测试 2018.12.23
    //返回所有员工
    public void testGetAllUser(){
        BaseEmpDBSer baseEmpDBSer=new BaseEmpDBSer(getContext());
        List<BaseEmp> lstUser=baseEmpDBSer.GetAllUser();
//        for (int i=0;i<lstUser.size();i++){
//            Log.e(juTag,lstUser.get(i).toString());
//        }
        if(lstUser.size()==0){
            Log.e(juTag,"错误:baseEmpDBSer.GetAllUser() 无数据.");
        }else{
            Log.e(juTag,"成功:baseEmpDBSer.GetAllUser() 数量:"+String.valueOf(lstUser.size()));
        }
    }
    //根据empid返回员工
    public void testGetUserByEmpId(){
        String empid="3638";
        BaseEmpDBSer baseEmpDBSer=new BaseEmpDBSer(getContext());
        BaseEmp user=baseEmpDBSer.GetUserByEmpId(empid);
        Log.e(juTag,user.toString());
    }

    //根据empNo返回员工
    public void testGetUserByEmpNo(){
        String empid="620124";
        BaseEmpDBSer baseEmpDBSer=new BaseEmpDBSer(getContext());
        BaseEmp user=baseEmpDBSer.GetUserByEmpNo(empid);
        Log.e(juTag,user.toString());
    }
    //endregion

}
