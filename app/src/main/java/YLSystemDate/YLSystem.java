package YLSystemDate;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import TaskClass.Box;
import TaskClass.TasksManager;
import TaskClass.User;
import YLDataService.LocalSetting;
import YLDataService.WebService;

/**
 * Created by asus on 2015/1/29.
 */
public class YLSystem {

    //region 变量定义
    private static TasksManager tasksManager;//任务管理类
    private static User user; //登录到系统的用户
    private static String appversion;//软件版本号
    private static List<Box> ediboxList;//编辑中的箱数组
    private static String NetWorkState;//网络状态
    private static String handsetIMEI;//手持机IMEI
    private static String kimTag;



    public static String getKimTag() {
        return "kim";
    }

    public static String getHandsetIMEI() {
        return handsetIMEI;
    }

    public static void setHandsetIMEI(String handsetIMEI) {
        YLSystem.handsetIMEI = handsetIMEI;
    }

    public static String getNetWorkState() {
        return NetWorkState;
    }

    public static void setNetWorkState(String netWorkState) {
        NetWorkState = netWorkState;
    }

    public static List<Box> getEdiboxList() {
        return ediboxList;
    }

    public static void setEdiboxList(List<Box> ediboxList) {
        YLSystem.ediboxList = ediboxList;
    }

    //endregion

    //<editor-fold desc="geter , seter">
    //登录后的用户
    public static User getUser() {
        return user;
    }

    //设置用户
    public static void setUser(User user) {
        YLSystem.user = user;
    }

    public static TasksManager getTasksManager() {
        if(tasksManager==null)
            tasksManager=new TasksManager();
        return tasksManager;
    }

    public static void setTasksManager(TasksManager tasksManager) {
        YLSystem.tasksManager = tasksManager;
    }
    //</editor-fold>

    public  void InitYLSystem(){
        //1.设置系统的版本号.
        //2.设置登录用户
        //3.设置TasksManager
        //4.设置手持机ID
    }

    public static String getVerName(Context context) {
        String verName = context.getResources().getText(ylescort.ylmobileandroid.R.string.app_versionName).toString();
        return verName;
    }

    public static boolean CheckUpdate(Context ctx) {
        WebService ws = new WebService();
        String serverVer = ws.getServerVer();
        String localVer = YLSystem.getVerName(ctx);
        if (Double.parseDouble(serverVer) > Double.parseDouble(localVer)) {
           return true;
        } else {
           return false;
        }
    }

    public static String GetDeviceID(Context ctx)
    {
        ContextWrapper wrapper=new ContextWrapper(ctx);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        String content =  prefs.getString("HandsetName", "未设置");

        return content;//"NH008";
    }

    public String GetISWIFI(Context context)
    {
        return isWifiActive(context);
    }

    public static String GetBaseUrl(Context context)
    {
        isWifiActive(context);
        return LocalSetting.webserviceaddress;
    }


    public static String isWifiActive(Context icontext){
       Context context = icontext.getApplicationContext();
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
       NetworkInfo[] info;
        if (connectivity != null) {
            info = connectivity.getAllNetworkInfo();
           if (info != null) {
               for (int i = 0; i < info.length; i++) {
                   if (info[i].getTypeName().equals("WIFI") && info[i].isConnected()) {
                       //用wifi的时候自动改用内网访问。
                       LocalSetting.webserviceaddress=LocalSetting.webserviceaddresstempLocal;
                       return "1";
                   }
               }
            }
        }
         //没有用wifi的时候自动转换成外网访问。。。
         LocalSetting.webserviceaddress=LocalSetting.webserviceaddresstempWeb;
         return "0";
        // return "1";
    }

    public static String SetMD5(String string) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

    public static boolean isNetConnected(Activity activity) {
        ConnectivityManager cm = (ConnectivityManager)activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo[] infos = cm.getAllNetworkInfo();
            if (infos != null) {
                for (NetworkInfo ni : infos) {
                    if (ni.isConnected()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
