package YLSystem;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import TaskClass.User;

/**
 * Created by asus on 2015/1/29.
 */
public class YLSystem {

    private static User user;
    //登录后的用户
    public static User getUser() {
        return user;
    }

    //设置用户
    public static void setUser(User user) {
        YLSystem.user = user;
    }




    public String GetDeviceID()
    {
        return "NH008";//todo 等待添加方法从本机数据库取数
    }

    public String GetISWIFI(Context context)
    {
        return isWifiActive(context);
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
                        return "1";
                      //  return true;
                    }
                }
            }
        }
        return "0";// return false;
    }
}
