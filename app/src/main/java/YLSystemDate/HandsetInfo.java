package YLSystemDate;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;

import YLDataService.BaseBoxDBSer;
import ylescort.ylmobileandroid.YLlauncher;

/**
 * Created by Administrator on 2016-11-01.
 */

public class HandsetInfo {
    private Context context;

    public HandsetInfo(Context context) {
        this.context = context;
    }

    public String getVersionName() throws Exception {
        // 获取packagemanager的实例
//        PackageManager packageManager = getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
//        PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(), 0);
        return "2.2.0";
    }

    public String HandSetSIM() throws Exception{
        String srvName = context.TELEPHONY_SERVICE;
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(srvName);
        return telephonyManager.getSimSerialNumber();
    }

    //获取手持机卡码
    public String HandSetIMEI() throws Exception{
        String srvName = Context.TELEPHONY_SERVICE;
        TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(srvName);
        return telephonyManager.getDeviceId();
    }


    //获取手持机SN码及上次缓存时间
    public String Preferencedata  ( String typestr, String data) throws Exception{
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(typestr, data);
    }


    //获取手持机MAC码
    public String WifiMAC() throws Exception {
        //在wifi未开启状态下，仍然可以获取MAC地址，但是IP地址必须在已连接状态下否则为0
        String macAddress = "";
        WifiManager wifiMgr = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = (null == wifiMgr ? null : wifiMgr.getConnectionInfo());
        if (null != info) {
            macAddress = info.getMacAddress();
        }
        return  macAddress;
    }

    //获取当前款箱数据
    public String YLBoxCount() throws Exception{
        int count= (new BaseBoxDBSer(context)).BaseBoxCount();
        return count+"";
    }

    public String getupdateinfo(){

        try {
            String cacheLastUpdate = Preferencedata("CacheLastUpdate", "1900-01-01 01:01:01");
            String handsetSN = Preferencedata("HandsetName", "9999");
            String VersionName = getVersionName();
            String HandSetMAC = WifiMAC();
            String HandSetIMEI = HandSetIMEI();
            String YLBoxCount = YLBoxCount();
            String SIMIMEI = HandSetSIM();
            YLHandSetBaseData.setYLVersion(VersionName);
            YLHandSetBaseData.setHandSetIMEI(HandSetIMEI);
            YLHandSetBaseData.setHandSetMAC(HandSetMAC);
            YLHandSetBaseData.setCacheDatetime(cacheLastUpdate);
            YLHandSetBaseData.setYLBoxCount(YLBoxCount);
            YLHandSetBaseData.setSIMIMEI(SIMIMEI);
            YLHandSetBaseData.setHandSetSN(handsetSN);

            String hseinfo = YLHandSetBaseData.getHandSetSN()
                    +"-"+YLHandSetBaseData.getHandSetIMEI()
                    +"-"+YLHandSetBaseData.getSIMIMEI()
                    +"-"+YLHandSetBaseData.getYLVersion();
            YLSystem.setHandsetIMEI(hseinfo);

            return "版本号："+YLHandSetBaseData.getYLVersion()+"\r\n"+"手持机："+YLHandSetBaseData.getHandSetSN();

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


}
