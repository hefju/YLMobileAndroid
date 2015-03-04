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



    public String getSiteID() {
        return SiteID;
    }

    public void setSiteID(String siteID) {
        SiteID = siteID;
    }

    public String getTradeState() {
        return TradeState;
    }

    public void setTradeState(String tradeState) {
        TradeState = tradeState;
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

    public String getTimeID() {
        return TimeID;
    }

    public void setTimeID(String timeID) {
        TimeID = timeID;
    }

    public String getATime() {
        return ATime;
    }

    public void setATime(String ATime) {
        this.ATime = ATime;
    }

    public String getEmpID() {
        return EmpID;
    }

    public void setEmpID(String empID) {
        EmpID = empID;
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
