package YLWebClass;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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


    //Http请求
    public static byte[] get(String url) throws Exception {
        URL url1 = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) url1.openConnection();

        InputStream inputStream = conn.getInputStream();
        byte[] buffer = new byte[10 * 1024];
        //缓冲区大小
        int len = -1;
        //读取长度 //用于保存网络读取的数据的输出流
        ByteArrayOutputStream baos = new ByteArrayOutputStream(0);
        //获取网络资源总长度
        long sumLength = conn.getContentLength();
        //当前读取的长度
        long curLength = 0;
        //判断打开链接是否成功
        if(conn.getResponseCode() == 200) {
            while((len = inputStream.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
                //打印读取的进度: 当前已读取的长度/网络资源的总长度
                curLength += len;
                //累加本次读取的资源大小
                int progress = (int) (curLength*100 / sumLength);
                Log.d("wang", "已下载：" + progress +"%");
            }
            return baos.toByteArray();
        }
        return null;
    }
}
