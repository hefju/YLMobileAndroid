package YLSystemDate;

/**
 * Created by Administrator on 2016-05-24.
 */
public class YLHandSetBaseData {

    private static String YLVersion ;//版本号
    private static String HandSetIMEI;//手持机IMEI码
    private static String HandSetMAC;//手持机MAC地址
    private static String CacheDatetime;//手持机上次缓存时间
    private static String SIMIMEI;//SIM卡IMEI码
    private static String YLBoxCount;//当前手持机款箱数量
    private static String HandSetSN;//手持机SN编码


    public static String getYLVersion() {
        return YLVersion;
    }

    public static void setYLVersion(String YLVersion) {
        YLHandSetBaseData.YLVersion = YLVersion;
    }

    public static String getHandSetIMEI() {
        return HandSetIMEI;
    }

    public static void setHandSetIMEI(String handSetIMEI) {
        HandSetIMEI = handSetIMEI;
    }

    public static String getHandSetMAC() {
        return HandSetMAC;
    }

    public static void setHandSetMAC(String handSetMAC) {
        HandSetMAC = handSetMAC;
    }

    public static String getCacheDatetime() {
        return CacheDatetime;
    }

    public static void setCacheDatetime(String cacheDatetime) {
        CacheDatetime = cacheDatetime;
    }

    public static String getSIMIMEI() {
        return SIMIMEI;
    }

    public static void setSIMIMEI(String SIMIMEI) {
        YLHandSetBaseData.SIMIMEI = SIMIMEI;
    }

    public static String getYLBoxCount() {
        return YLBoxCount;
    }

    public static void setYLBoxCount(String YLBoxCount) {
        YLHandSetBaseData.YLBoxCount = YLBoxCount;
    }

    public static String getHandSetSN() {
        return HandSetSN;
    }

    public static void setHandSetSN(String handSetSN) {
        HandSetSN = handSetSN;
    }
}
