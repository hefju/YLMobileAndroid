package ylescort.ylmobileandroid;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import TaskClass.Box;
import YLDataService.YLBoxScanCheck;

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
}