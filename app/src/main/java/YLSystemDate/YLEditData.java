package YLSystemDate;

import java.util.List;

import TaskClass.YLATM;

/**
 * Created by Administrator on 2015/4/3.
 */
public class YLEditData {
    public static YLATM ylatm;
    public static List<YLATM> ylatmList;

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