package TaskClass;

/**
 * Created by asus on 2015/1/29.
 */
public class ArriveTime {
    public int  Id;
    public String ServerReturn ;//服务器返回的成功与否，成功1，其他异常是e.toString().
    public String EmpID ;//登陆的人员ID，记录操作人员ID.
    public String ATime ;//到达网点的时间
    public String TimeID ;//到达时间ID
    public String TradeBegin ;//交易开始时间
    public String TradeEnd ;//交易结束时间
    public String TradeState ;//这次到达完成交易了么？1为完成，0为未完成
    public String SiteID ;    //网点ID
}
