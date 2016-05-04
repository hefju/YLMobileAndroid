package YLFileOperate;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.PushbackInputStream;
import java.util.Calendar;

import YLSystemDate.YLSystem;

/**
 * Created by Administrator on 2016-04-20.
 */
public class YLLoghandle {

    private String path;
    private YLtxtOperate yLtxtOperate;
    private String YLLogName;

    public YLLoghandle(Context context) {
        yLtxtOperate = new YLtxtOperate(context);
        path = yLtxtOperate.SDPATH;
    }



    public void CreatAndCleanYLLog() throws Exception {
        File creatfile = GetYLLogName(0);
        if (!creatfile.exists()) {
            yLtxtOperate.createSDFile(creatfile.getName());
        }
        YLLogName = creatfile.getName();
        File DeleteFile = GetYLLogName(7);
        if (DeleteFile.exists()) {
            yLtxtOperate.deleteSDFile(DeleteFile.getName());
        }
    }


    private File GetYLLogName(int addday) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, addday);
        int Month = calendar.get(Calendar.MONTH) + 1;
        int Day = calendar.get(Calendar.DAY_OF_MONTH);
        String FileName = String.format("%02d", Month) + "" + String.format("%02d", Day) + ".txt";
        return new File(yLtxtOperate.SDPATH + "//" + FileName);
    }

    public File GetYLLogName(int year,int month,int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year,month,day);
        int Month = calendar.get(Calendar.MONTH);
        int Day = calendar.get(Calendar.DAY_OF_MONTH);
        String FileName = String.format("%02d", Month) + "" + String.format("%02d", Day) + ".txt";
        return new File(yLtxtOperate.SDPATH + "//" + FileName);
    }



    public void AppendWrite(String write) throws Exception {
        CreatAndCleanYLLog();
        yLtxtOperate.AppendWrite(path + "//" + YLLogName, write + ",");
    }

    public String ReadTxt(String logname) throws Exception {
        CreatAndCleanYLLog();
        String str = yLtxtOperate.readSDFile(path + "//" + logname);
        Log.e(YLSystem.getKimTag(), "查看:"+str);
        return  str;
    }

    public void Delete() throws Exception {
        CreatAndCleanYLLog();
        yLtxtOperate.deleteSDFile(YLLogName);
        CreatAndCleanYLLog();
    }


}
