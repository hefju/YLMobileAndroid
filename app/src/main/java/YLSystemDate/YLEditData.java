package YLSystemDate;

import java.util.Date;
import java.util.List;

import TaskClass.YLATM;
import TaskClass.YLTask;

/**
 * Created by Administrator on 2015/4/3.
 */
public class YLEditData {
    public static YLATM ylatm;
    public static List<YLATM> ylatmList;

    public static Date DatePick;
    public static YLTask ylTask;

    public static YLTask getYlTask() {
        return ylTask;
    }

    public static void setYlTask(YLTask ylTask) {
        YLEditData.ylTask = ylTask;
    }

    public static Date getDatePick() {
        return DatePick;
    }

    public static void setDatePick(Date datePick) {
        DatePick = datePick;
    }

    public static List<YLATM> getYlatmList() {
        return ylatmList;
    }

    public static void setYlatmList(List<YLATM> ylatmList) {
        YLEditData.ylatmList = ylatmList;
    }

    public static YLATM getYlatm() {
        return ylatm;
    }

    public static void setYlatm(YLATM ylatm) {
        YLEditData.ylatm = ylatm;
    }
}
