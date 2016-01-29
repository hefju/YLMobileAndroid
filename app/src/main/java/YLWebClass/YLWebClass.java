package YLWebClass;

import java.util.List;
import TaskClass.BaseBox;
import TaskClass.BaseClient;
import TaskClass.BaseEmp;
import TaskClass.BaseSite;
import TaskClass.Box;
import TaskClass.Site;
import TaskClass.User;
import TaskClass.YLATM;
import TaskClass.YLTask;

/**
 * Created by Administrator on 2016-01-27.
 */
public class YLWebClass {
    public static BaseBox baseBox;
    public static List<BaseBox> baseBoxList;

    public static BaseClient baseClient;
    public static List<BaseClient> baseClientList;

    public static BaseEmp baseEmp;
    public static List<BaseEmp> baseEmpList;

    public static BaseSite baseSite;
    public static List<BaseSite> baseSiteList;

    public static Box ylbox;
    public static List<Box> ylboxList;

    public static Site ylsite;
    public static List<Site> ylsiteList;

    public static User yluser;
    public static List<User> yluserList;

    public static YLATM ylatm;
    public static List<YLATM> ylatmList;

    public static YLTask myylTask;
    public static List<YLTask> myylTaskList;


    public static List<YLTask> getMyylTaskList() {
        return myylTaskList;
    }

    public static void setMyylTaskList(List<YLTask> myylTaskList) {
        YLWebClass.myylTaskList = myylTaskList;
    }

    public static YLTask getMyylTask() {
        return myylTask;
    }

    public static void setMyylTask(YLTask myylTask) {
        YLWebClass.myylTask = myylTask;
    }

    public static BaseBox getBaseBox() {
        return baseBox;
    }

    public static void setBaseBox(BaseBox baseBox) {
        YLWebClass.baseBox = baseBox;
    }

    public static List<BaseBox> getBaseBoxList() {
        return baseBoxList;
    }

    public static void setBaseBoxList(List<BaseBox> baseBoxList) {
        YLWebClass.baseBoxList = baseBoxList;
    }

    public static BaseClient getBaseClient() {
        return baseClient;
    }

    public static void setBaseClient(BaseClient baseClient) {
        YLWebClass.baseClient = baseClient;
    }

    public static List<BaseClient> getBaseClientList() {
        return baseClientList;
    }

    public static void setBaseClientList(List<BaseClient> baseClientList) {
        YLWebClass.baseClientList = baseClientList;
    }

    public static BaseEmp getBaseEmp() {
        return baseEmp;
    }

    public static void setBaseEmp(BaseEmp baseEmp) {
        YLWebClass.baseEmp = baseEmp;
    }

    public static List<BaseEmp> getBaseEmpList() {
        return baseEmpList;
    }

    public static void setBaseEmpList(List<BaseEmp> baseEmpList) {
        YLWebClass.baseEmpList = baseEmpList;
    }

    public static BaseSite getBaseSite() {
        return baseSite;
    }

    public static void setBaseSite(BaseSite baseSite) {
        YLWebClass.baseSite = baseSite;
    }

    public static List<BaseSite> getBaseSiteList() {
        return baseSiteList;
    }

    public static void setBaseSiteList(List<BaseSite> baseSiteList) {
        YLWebClass.baseSiteList = baseSiteList;
    }

    public static Box getYlbox() {
        return ylbox;
    }

    public static void setYlbox(Box ylbox) {
        YLWebClass.ylbox = ylbox;
    }

    public static List<Box> getYlboxList() {
        return ylboxList;
    }

    public static void setYlboxList(List<Box> ylboxList) {
        YLWebClass.ylboxList = ylboxList;
    }

    public static Site getYlsite() {
        return ylsite;
    }

    public static void setYlsite(Site ylsite) {
        YLWebClass.ylsite = ylsite;
    }

    public static List<Site> getYlsiteList() {
        return ylsiteList;
    }

    public static void setYlsiteList(List<Site> ylsiteList) {
        YLWebClass.ylsiteList = ylsiteList;
    }

    public static User getYluser() {
        return yluser;
    }

    public static void setYluser(User yluser) {
        YLWebClass.yluser = yluser;
    }

    public static List<User> getYluserList() {
        return yluserList;
    }

    public static void setYluserList(List<User> yluserList) {
        YLWebClass.yluserList = yluserList;
    }

    public static YLATM getYlatm() {
        return ylatm;
    }

    public static void setYlatm(YLATM ylatm) {
        YLWebClass.ylatm = ylatm;
    }

    public static List<YLATM> getYlatmList() {
        return ylatmList;
    }

    public static void setYlatmList(List<YLATM> ylatmList) {
        YLWebClass.ylatmList = ylatmList;
    }
}
