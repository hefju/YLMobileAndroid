package TaskClass;

import java.util.Comparator;
import java.util.Date;

import YLSystemDate.YLSysTime;

/**
 * Created by Administrator on 2016-12-29.
 */

public class BoxCombyTimedesc implements Comparator<Box> {
    @Override
    public int compare(Box lhs, Box rhs) {

        try {
            Date t1 = YLSysTime.StrToTime(lhs.getActionTime());
            Date t2 = YLSysTime.StrToTime(rhs.getActionTime());
            return  t2.compareTo(t1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }
}
