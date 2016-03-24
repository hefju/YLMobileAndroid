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
    public static YLATM ylatmNetPoint;
    public static List<YLATM> ylatmList;
    public static List<Box> yleditcarbox;

    public static Date DatePick;//当前选择的日期
    public static YLTask ylTask;
    public static List<Box> ylcarbox;//车内款箱空实状态
    public static Site CurrentYLSite;//当前选择的网点
    public static String TimeID; //出入库标识1为出库，2为入库；

    public static YLTask CarToCarylTask;
    public static int CarToCargetorgive;

    public static Site PrintSite;//网点打印

    public static Site getPrintSite() {
        return PrintSite;
    }

    public static void setPrintSite(Site printSite) {
        PrintSite = printSite;
    }

    public static int getCarToCargetorgive() {
        return CarToCargetorgive;
    }

    public static void setCarToCargetorgive(int carToCargetorgive) {
        CarToCargetorgive = carToCargetorgive;
    }

    public static YLTask getCarToCarylTask() {
        return CarToCarylTask;
    }

    public static void setCarToCarylTask(YLTask carToCarylTask) {
        CarToCarylTask = carToCarylTask;
    }

    public static String getTimeID() {
        return TimeID;
    }

    public static void setTimeID(String timeID) {
        TimeID = timeID;
    }

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

    public static List<Box> getYleditcarbox() {
        return yleditcarbox;
    }

    public static void setYleditcarbox(List<Box> yleditcarbox) {
        YLEditData.yleditcarbox = yleditcarbox;
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

    public static YLATM getYlatmNetPoint() {
        return ylatmNetPoint;
    }

    public static void setYlatmNetPoint(YLATM ylatmNetPoint) {
        YLEditData.ylatmNetPoint = ylatmNetPoint;
    }
}
