package TaskClass;


import java.util.Comparator;
import java.util.Date;

import YLSystemDate.YLSysTime;

/**
 * Created by Administrator on 2015-11-02.
 */
public class BoxCombyTime implements Comparator<Box> {
    @Override
    public int compare(Box box, Box t1) {
        try {
            Date box1date = YLSysTime.StrToTime(box.getActionTime());
            Date box2date = YLSysTime.StrToTime(t1.getActionTime());
            return box1date.compareTo(box2date);
        }catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }
}
