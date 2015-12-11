package YLDataService;

import android.content.Context;
import android.util.Log;

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
        if (replaceboxnumber.length() < 10 || replaceboxnumber.length() > 10){
            box.setBoxName("无数据");
            box.setBoxID("0");
            return box;
        }
        BaseBoxDBSer baseBoxDBSer = new BaseBoxDBSer(context);
        baseBox = baseBoxDBSer.GetBoxByBCNo(replaceboxnumber);
        if (baseBox.BoxName.equals("无数据")){
            box.setBoxName("无数据");
            box.setBoxID("0");
            return box;
        }else{
            box.setBoxName(baseBox.BoxName);
            box.setBoxID(replaceboxnumber);
            if (baseBox.BoxType.equals("普通箱")||baseBox.BoxType.equals("")){
                box.setBoxType("款箱");
            }else {
                box.setBoxType(baseBox.BoxType);
            }
            return box;
        }
    }

    public static Box CheckBoxbyUHF(String boxnumber,Context context){
        Box box = new Box();
        String replaceboxnumber =replaceBlank(boxnumber);
        if (replaceboxnumber.length() != 10){
            box.setBoxName("无数据");
            box.setBoxID("0");
            return box;
        }
        BoxDBSer boxDBSer = new BoxDBSer(context);
        box = boxDBSer.GetBoxs2(replaceboxnumber);
        if (box.getBoxType() == null||box.getBoxType().equals("普通箱") || box.getBoxType().equals("") ){
            box.setBoxType("款箱");
        }
        return box;
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
