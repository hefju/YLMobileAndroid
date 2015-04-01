package TaskClass;

import java.util.List;

/**
 * Created by Administrator on 2015/4/1.
 */
public class YLATM {
    public int  Id;
    public String ServerReturn ;//服务器返回的成功与否，成功1，其他异常是e.toString().
    public String TaskID ;
    public String SiteID ;  //网点ID
    public String SiteName ;//网点名
    public String SiteType ;//网点类型ATM还是网点

    public String TradeBegin ;//交易开始时间
    public String TradeEnd ;//交易结束时间
    public String ATMCount ;//ATM数目
}
