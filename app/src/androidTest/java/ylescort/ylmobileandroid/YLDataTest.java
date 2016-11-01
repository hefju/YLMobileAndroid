package ylescort.ylmobileandroid;

import android.app.Application;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.ApplicationTestCase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import TaskClass.BaseBox;
import TaskClass.BaseClient;
import TaskClass.BaseClient_HF;
import TaskClass.BaseEmp;
import TaskClass.BaseSite;
import TaskClass.Box;
import TaskClass.BoxCombyOrder;
import TaskClass.GatherPrint;
import TaskClass.TasksManager;
import TaskClass.User;
import TaskClass.YLTask;
import YLDataService.AnalysisBoxList;
import YLDataService.BaseBoxDBSer;
import YLDataService.BaseEmpDBSer;
import YLDataService.BaseSiteDBSer;
import YLDataService.BoxDBSer;
import YLDataService.TasksManagerDBSer;
import YLDataService.WebServerValutturnover;
import YLDataService.YLBoxScanCheck;
import YLDataService.YLSQLHelper;
import YLDataService.YLSiteInfo;
import YLSystemDate.YLEditData;
import YLSystemDate.YLSysTime;
import YLSystemDate.YLSystem;

/**
 * Created by Administrator on 2015/5/11.
 */
public class YLDataTest extends ApplicationTestCase<Application> {
    public YLDataTest() {super(Application.class);}

    private static final String TAG = "kim";

    public void testSetData() throws  Exception{

//        HashSet<Box> boxSet = new HashSet<Box>();

        Set<Box> boxSet = new HashSet<>();

        Box box1 = new Box();
        box1.setId(123);
        box1.setBoxName("测试1");
        box1.setBoxID("123");

        Box box2 = new Box();
        box2.setBoxOrder("1");
        box2.setBoxName("测试1");
        box2.setBoxID("123");

        Box box3 = new Box();
        box3.setBoxName("测试1");
        box3.setBoxID("123");


        boolean t1 =  boxSet.add(box1);
        boolean t2 = boxSet.add(box2);
        boolean t3 = boxSet.add(box3);



        for (Box box : boxSet) {
            if (box.getId() == 123){
                box.setBoxName("测试3");
            }
        }

//        for (Box box : boxSet) {
//            Log.e(YLSystem.getKimTag(),box.getBoxName());
//
//        }
        Log.e(YLSystem.getKimTag(),t1+""+t2+t3+"");
    }


    public void testDeletTask() throws  Exception{
        TasksManagerDBSer tasksManagerDBSer = new TasksManagerDBSer(getContext());
        tasksManagerDBSer.DeleteTasksManagerbydate("2016-10-19");
    }

    public void testUpdateSiteHF() throws Exception{
        BaseSiteDBSer dbSer = new BaseSiteDBSer(getContext());
        BaseSite b = new BaseSite();

        b = dbSer.GetBasesingleSite("where SiteID =1861 ");//顺德农商行南海支行
        b.setSiteHFNo("AEF45C65,7B62D225");

        BaseSite a = new BaseSite();
        a = dbSer.GetBasesingleSite("where SiteID =250 ");//佛山建行千灯湖支行
        a.setSiteHFNo("A0EAD8A6,002DE3A6");

        List<BaseSite> baseSites = new ArrayList<>();
        baseSites.add(b);
        baseSites.add(a);
        dbSer.UpdateBaseSite(baseSites);

    }


    public void testBaseEmp() throws Exception{
        BaseEmpDBSer b = new BaseEmpDBSer(getContext());
        List<BaseEmp> baseEmps = b.GetBaseEmps("where EmpNo =520037");
        for (BaseEmp emp : baseEmps) {
            Log.e(TAG,emp.toString());
        }
    }

    public void testHFSite() throws Exception{
       YLSiteInfo ylSiteInfo = new YLSiteInfo(getContext());
        boolean t = ylSiteInfo.CheckSiteHF("157","4EF87B64");
        Log.e(YLSystem.getKimTag(),"返回:"+t);
    }


    public void testSiteNewData() throws Exception{
        YLSQLHelper ylsqlHelper = new YLSQLHelper(getContext());
        SQLiteDatabase sdb =ylsqlHelper.getReadableDatabase();
        Cursor cursor = sdb.rawQuery("select * from BaseSite where Id = '1861'",null);
        String sitehf = "";
        while (cursor.moveToNext()){
//            int id = cursor.getInt(cursor.getColumnIndex("Id"));
              sitehf = cursor.getString(cursor.getColumnIndex("SiteHFNo"));
        }

        Log.e(YLSystem.getKimTag(),"fanhui"+sitehf);
    }

    public void testSetListadd() throws Exception {
        Set<String> set = new HashSet<String>(new ArrayList<String>());
        for (int i = 0; i < 10;i++){
            Log.e(TAG,i%3+"");
            if (i%3 ==0){
                set.add("1");
            }else if (i%3 ==1){
                set.add("2");
            }else if (i%3 ==2){
                set.add("3");
            }
        }
        Log.e(TAG,set.toString());
    }

    public void testForData()throws Exception{
        ArrayList<String> namelist = new ArrayList<>();
        ArrayList<String> newnamelist = new ArrayList<>();
        for (int i = 0; i < 10 ;i++){
            String name = "0123456789";
            namelist.add(name.substring(i));
        }
        Log.e(TAG,namelist.toString());

//        for (int i = 0;i <namelist.size();i++){
//            int gg =  namelist.size()-i-1;
//            String get = namelist.get(gg);
//            newnamelist.add(get+"N");
//        }

        for (int i = namelist.size()-1;i>=0;i--){
            String get = namelist.get(i);
            newnamelist.add(get);
        }

        Log.e(TAG,newnamelist.toString());
    }

    public void testBoxDataServer()throws Exception{
        Box box= YLBoxScanCheck.CheckBox("geafe", getContext());
        Log.e(TAG,box.toString());
    }

    public void testColorInt()throws Exception{
        int color = R.color.orange;
        Log.e(TAG,color+"");
    }

    public void testListBoxSet() throws Exception{

        List<Box> fristList = new ArrayList<>();
//        List<Box> SecondList = new ArrayList<>();

        for (int i = 0 ; i <5;i++){
            Box box = new Box();
            box.setBoxID(i+1+"");
            fristList.add(box);
        }
//        for (int i = 0 ;i <fristList.size();i++){
//            Box box = fristList.get(i);
//            SecondList.add(box);
//        }

        List<Box> SecondList = new ArrayList<>(fristList);

//        SecondList = fristList;
        Box box = new Box();
        box.setBoxID("6");
        fristList.set(0,box);
//        fristList.clear();


        Log.e(TAG, fristList.toString() + "fristList");
        Log.e(TAG,SecondList.toString()+"SecondList");
    }

    public void testGetEmpHFNO() throws Exception{
        BaseEmpDBSer baseEmpDBSer = new BaseEmpDBSer(getContext());
        List<BaseEmp> baseEmps = baseEmpDBSer.GetBaseEmps("where EmpNo = '2000' ");
        Log.e(TAG,baseEmps.toString());
    }

    public void testStringFormat()throws Exception{
        String teststr = "123";
        String str = String.format("%05d", 3);
        Log.e(YLSystem.getKimTag(),str);
    }

    public void testreplacestr()throws Exception{
        String str = "1234567890123456789";
//        String newStr = str.replaceAll("^(0+)", "");
        String newStr = str.substring(0, 10);
        Log.e(YLSystem.getKimTag(),newStr);
    }

    public void testSelcetBox() throws Exception{
        String boxid = "0114108547";
        Box box = new Box();
//        BaseBoxDBSer baseBoxDBSer = new BaseBoxDBSer(getContext());
        BoxDBSer boxDBSer = new BoxDBSer(getContext());
        box =  boxDBSer.GetBoxs2(boxid);
        Log.e(YLSystem.getKimTag(),box.toString());
    }

    public void testListGroup()throws Exception{
        List<Box> boxList = new ArrayList<>();
        List<Box> orderboxlist = new ArrayList<>();

        for (int i = 0;i<6;i++){
            Box box = new Box();
            box.setBoxName(i+1+"");
            box.setBoxOrder("顺序：2");
            boxList.add(box);
        }

        for (int i = 0;i<5;i++){
            Box box = new Box();
            box.setBoxName(i+1+"");
            box.setBoxOrder("顺序：1");
            boxList.add(box);
        }

        for (int i = 0;i<7;i++){
            Box box = new Box();
            box.setBoxName(i+1+"");
            box.setBoxOrder("顺序：3");
            boxList.add(box);
        }

        int count =0;
        String order = boxList.get(0).getBoxOrder();

        for (int i = 0; i < boxList.size(); i++) {
            if (boxList.get(i).getBoxOrder().equals(order)) {
                count++;
            } else {
                Box box = new Box();
                box.setBoxName(order);
                box.setBoxOrder(count + "");
                count = 1;
                order = boxList.get(i).getBoxOrder();
                orderboxlist.add(box);
            }
            if (i == boxList.size()-1){
                Box box = new Box();
                box.setBoxName(order);
                box.setBoxOrder(count + "");
                order = boxList.get(i).getBoxOrder();
                orderboxlist.add(box);
            }
        }


        Log.e(YLSystem.getKimTag(),orderboxlist.toString());

    }


    public void testhandset()throws Exception{
        List<String> boxList = new ArrayList<>();
        List<Box> orderboxlist = new ArrayList<>();

        boxList.add("1234");
        boxList.add("123");
        boxList.add("14");
        boxList.add("1234");

        boxList = removeDeuplicate(boxList);

        Log.e(YLSystem.getKimTag(),boxList.toString());
    }

    public List removeDeuplicate(List arlList)
    {
        HashSet h=new HashSet(arlList);
        arlList.clear();
        arlList.addAll(h);
        List list=new ArrayList();
        list=arlList;
        return list;
    }

    public void testFindClass(){
        List<Box> boxList = new ArrayList<>();
        for (int i = 0;i<3;i++){
            Box box = new Box();
            box.setBoxID(i + "");
            box.setBoxName("测试" + i);
            box.setBoxOrder(i + "");
            boxList.add(box);
            if (i==0){
                Log.e(YLSystem.getKimTag(), box.hashCode()+"listhashcode");
            }
        }
        Log.e(YLSystem.getKimTag(), boxList.toString());
        Box box = new Box();
        box.setBoxID("0");
        box.setBoxName("测试0");
        box.setBoxOrder("0");
        Log.e(YLSystem.getKimTag(), box.hashCode()+"hashcode");
        if (boxList.contains(box)){
            Log.e(YLSystem.getKimTag(),"1");
        }else {
            Log.e(YLSystem.getKimTag(),"0");
        }
    }

    public void testDeleteYLTask()throws Exception{
        TasksManager tasksManager = new TasksManager();
        tasksManager.setTaskDate("2015-11-07");
        TasksManagerDBSer tasksManagerDBSer = new TasksManagerDBSer(getContext());
        tasksManagerDBSer.DeleteTasksManager(tasksManager);
    }

    public void testDeleteBaseBox()throws Exception{

        Box box = new Box();

        box.setBoxID("0114101923");

        BoxDBSer boxDBSer = new BoxDBSer(getContext());

        boxDBSer.DeleteBox(box);

    }

    public void testSurstr() throws Exception {
        String  CountName = "第100次";
        CountName = CountName.substring(1,CountName.length()-1);
//        CountName = CountName.substring(CountName.length()-1);
        Log.e(TAG, CountName);
    }

    public void testComBoxStr() throws Exception {
        List<Box> list = new ArrayList<>();
        String[] strs = new String[6];
        strs[0] = "6";
        strs[1] = "2";
        strs[2] = "3";
        strs[3] = "1";
        strs[4] = "4";
        strs[5] = "5";

        for (int i = 0 ;i<6;i++){
            Box box = new Box();
            box.setBoxOrder(strs[i]);
            list.add(box);
        }

        for (Box box : list) {
            Log.e(TAG,"未排序前"+box.getBoxOrder());
        }

        BoxCombyOrder boxCombyOrder = new BoxCombyOrder();

        Collections.sort(list, boxCombyOrder);

        for (Box box : list) {
            Log.e(TAG,"排序后"+box.getBoxOrder());
        }


    }

    public void testPrintGather()throws Exception{

        List<Box> list = new ArrayList<>();
        for (int i = 0 ; i < 3;i++){
            Box box = YLBoxScanCheck.CheckBox("011507062"+i, getContext());
            box.setBoxStatus("实");
            box.setTradeAction("送");
            box.setBoxTaskType("早送晚收");
            list.add(box);

        }
        for (int i = 0 ; i < 2;i++){
            Box box = YLBoxScanCheck.CheckBox("011507052"+i, getContext());
            box.setBoxStatus("空");
            box.setTradeAction("收");
            box.setBoxTaskType("上下介");
            list.add(box);
        }
        for (int i = 0 ; i < 4;i++){
            Box box = YLBoxScanCheck.CheckBox("011507042"+i, getContext());
            box.setBoxStatus("实");
            box.setTradeAction("收");
            box.setBoxTaskType("寄库箱");
            list.add(box);
        }
        for (int i = 0 ; i < 1;i++){
            Box box = YLBoxScanCheck.CheckBox("011507032"+i, getContext());
            box.setBoxStatus("空");
            box.setTradeAction("收");
            box.setBoxTaskType("跨行调拨");
            list.add(box);
        }

        AnalysisBoxList analysisBoxList = new AnalysisBoxList();
        GatherPrint print = analysisBoxList.AnsysisBoxListForPrint(list);
        print.setSiteName("123");
        Log.e(TAG, print.getJkxmoneyempty() + "返回");

    }

    public void testGetClinet()throws Exception{
        YLSiteInfo ylSiteInfo = new YLSiteInfo(getContext());
        String baseClients =  ylSiteInfo.GetClientbySiteID("999");
        Log.e(TAG,baseClients);
        baseClients = ylSiteInfo.GetClientbySiteID("114");
        Log.e(TAG,baseClients);
    }

}
