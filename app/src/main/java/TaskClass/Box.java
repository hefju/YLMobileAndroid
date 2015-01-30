package TaskClass;

/**
 * Created by Administrator on 2015/1/19.
 */
public class Box {
    public String ServerReturn ;//服务器返回的成功与否，成功1，其他异常是e.toString().
    public String SiteID ;
    public String BoxID ;    //钱箱ID
    public String BoxName ;//钱箱名
    public String TradeAction ;//钱箱状态   //送箱还是收箱
    public String BoxStatus ;//钱箱状态   //实箱, 空箱
    public String BoxType ;//钱箱类型   //款箱, 凭证, 卡箱
    public String NextOutTime ;//下次出库时间  预定出库: [2]天后出库
    public String ActionTime ;//钱箱操作时间
    public String TimeID ;//到达时间ID


}
