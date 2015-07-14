package ylescort.ylmobileandroid;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import TaskClass.BaseBox;
import TaskClass.BaseClient_HF;
import TaskClass.BaseEmp;
import TaskClass.Box;
import YLDataService.BaseBoxDBSer;
import YLDataService.BaseEmpDBSer;
import YLDataService.BoxDBSer;
import YLDataService.YLBoxScanCheck;
import YLSystemDate.YLSystem;

/**
 * Created by Administrator on 2015/5/11.
 */
public class YLDataTest extends ApplicationTestCase<Application> {
    public YLDataTest() {super(Application.class);}

    private static final String TAG = "kim";

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
        Box box= YLBoxScanCheck.CheckBox("0114106238", getContext());
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
        String newStr = str.substring(0,10);
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

}
