package YLDataService;

import android.content.Context;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import TaskClass.BaseBox;
import TaskClass.Box;

/**
 * Created by Administrator on 2015/4/27.
 */
public class YLBoxScanCheck {

    private static BaseBox baseBox;

    public static   Box CheckBox(String boxnumber ,Context context){
        Box box = new Box();
        String replaceboxnumber =replaceBlank(boxnumber);
        if (replaceboxnumber.length() < 10){
            box.setBoxName("illegalbox");
            return box;
        }
        BaseBoxDBSer baseBoxDBSer = new BaseBoxDBSer(context);
        baseBox = baseBoxDBSer.GetBoxByBCNo(replaceboxnumber);
        if (baseBox.BoxName.equals("无数据")){
            box.setBoxName("无数据");
            return box;
        }else{
            box.setBoxName(baseBox.BoxName);
            box.setBoxID(replaceboxnumber);
            return box;
        }
    }

    public static   String replaceBlank(String str) {
        String dest = "";
        if (str!=null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }
}
