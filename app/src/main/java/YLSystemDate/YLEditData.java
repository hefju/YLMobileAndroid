package YLSystemDate;

import java.util.Date;
import java.util.List;

import TaskClass.Box;
import TaskClass.Site;
import TaskClass.YLATM;
import TaskClass.YLTask;

/**
 * Created by Administrator on 2015/4/3.
 */
public class YLEditData {
    public static YLATM ylatm;
    public static List<YLATM> ylatmList;
    public static List<Box> ylboxnosave;

    public static Date DatePick;//当前选择的日期
    public static YLTask ylTask;
    public static List<Box> ylcarbox;//车内款箱空实状态
    public static Site CurrentYLSite;//当前选择的网点

    public static Site getCurrentYLSite() {
        return CurrentYLSite;
    }

    public static void setCurrentYLSite(Site currentYLSite) {
        CurrentYLSite = currentYLSite;
    }

    public static List<Box> getYlcarbox() {
        return ylcarbox;
    }

    public static void setYlcarbox(List<Box> ylcarbox) {
        YLEditData.ylcarbox = ylcarbox;
    }

    public static List<Box> getYlboxnosave() {
        return ylboxnosave;
    }

    public static void setYlboxnosave(List<Box> ylboxnosave) {
        YLEditData.ylboxnosave = ylboxnosave;
    }

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
