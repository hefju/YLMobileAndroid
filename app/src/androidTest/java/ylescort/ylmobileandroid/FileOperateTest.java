package ylescort.ylmobileandroid;

import android.app.Application;
import android.system.ErrnoException;
import android.test.ApplicationTestCase;
import android.util.Log;

import java.io.File;

import YLFileOperate.YLLoghandle;
import YLFileOperate.YLtxtOperate;
import YLSystemDate.YLRecord;
import YLSystemDate.YLSystem;

/**
 * Created by Administrator on 2016-04-20.
 */
public class FileOperateTest extends ApplicationTestCase<Application> {
    public FileOperateTest() { super(Application.class); }

    public void  testCreatTxt() throws Exception{
        YLtxtOperate yLtxtOperate = new YLtxtOperate(getContext());
        yLtxtOperate.createSDFile("0413.txt");
    }

    public void  testCreatTxt2() throws Exception{
        YLtxtOperate yLtxtOperate = new YLtxtOperate(getContext());
//        yLtxtOperate.createSDFile2("0413.txt");
    }

    public void testDeletetxt() throws Exception{
        YLtxtOperate yLtxtOperate = new YLtxtOperate(getContext());
        yLtxtOperate.deleteSDFile("0426.txt");
    }

    public void testappendwrite() throws Exception{
        YLtxtOperate yLtxtOperate = new YLtxtOperate(getContext());
        String fileName = yLtxtOperate.SDPATH + "/" + "yllog.txt";
        for (int i =0 ;i<3;i++) {
            yLtxtOperate.AppendWrite(fileName, "测试" + i + "\n");
        }
    }

    public  void testread() throws  Exception {
        YLtxtOperate yLtxtOperate = new YLtxtOperate(getContext());
        String fileName = yLtxtOperate.SDPATH + "/" + "yllog.txt";
        String fileread =String.format( fileName,"UTF-8") ;
       String rfile =  yLtxtOperate.readSDFile(fileread);
        Log.e(YLSystem.getKimTag(),rfile);
    }

    public void testdelete() throws Exception{
        YLLoghandle ylLog = new YLLoghandle(getContext());
        ylLog.Delete();

    }

    public void testWrite() throws Exception{
        YLLoghandle ylLog = new YLLoghandle(getContext());
        for (int i = 0; i< 10;i++){
            ylLog.AppendWrite("1346"+i);
        }

    }

    public void testRead() throws Exception{
        YLLoghandle ylLog = new YLLoghandle(getContext());
        ylLog.ReadTxt(ylLog.GetYLLogName(2016,4,27 ).getName());
    }

    public void teststatOperate() throws Exception{

        YLLoghandle ylLoghandle = new YLLoghandle(getContext());
        YLRecord.setYlloghandle(ylLoghandle);
        for (int i = 0; i< 1000;i++){
            YLRecord.WriteRecord(this.getClass(),"测试"+i);
        }
    }

    public void testgetFileName()throws Exception{
        YLLoghandle ylLoghandle = new YLLoghandle(getContext());
        File file = ylLoghandle.GetYLLogName(2016,4,27);
        if (file.exists()){
            Log.e(YLSystem.getKimTag(),file.getName());
        }else {
            Log.e(YLSystem.getKimTag(),"未有文件");
        }
    }



}
