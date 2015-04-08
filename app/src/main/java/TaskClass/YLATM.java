package TaskClass;

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
    public Integer TimeID ;//到达次数
    public String EmpID;

    public String getEmpID() {
        return EmpID;
    }

    public void setEmpID(String empID) {
        EmpID = empID;
    }

    public Integer getTimeID() {
        return TimeID;
    }

    public void setTimeID(Integer timeID) {
        TimeID = timeID;
    }

    public String getATMCount() {
        return ATMCount;
    }

    public void setATMCount(String ATMCount) {
        this.ATMCount = ATMCount;
    }

    public String getTradeEnd() {
        return TradeEnd;
    }

    public void setTradeEnd(String tradeEnd) {
        TradeEnd = tradeEnd;
    }

    public String getTradeBegin() {
        return TradeBegin;
    }

    public void setTradeBegin(String tradeBegin) {
        TradeBegin = tradeBegin;
    }

    public String getSiteType() {
        return SiteType;
    }

    public void setSiteType(String siteType) {
        SiteType = siteType;
    }

    public String getSiteName() {
        return SiteName;
    }

    public void setSiteName(String siteName) {
        SiteName = siteName;
    }

    public String getSiteID() {
        return SiteID;
    }

    public void setSiteID(String siteID) {
        SiteID = siteID;
    }

    public String getTaskID() {
        return TaskID;
    }

    public void setTaskID(String taskID) {
        TaskID = taskID;
    }

    public String getServerReturn() {
        return ServerReturn;
    }

    public void setServerReturn(String serverReturn) {
        ServerReturn = serverReturn;
    }

    public int getId() {

        return Id;
    }

    public void setId(int id) {
        Id = id;
    }
}
