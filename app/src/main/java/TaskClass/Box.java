package TaskClass;

/**
 * Created by Administrator on 2015/1/19.
 */
public class Box {
    public int  Id;
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
    public String BoxCount;//箱数量
    public String BoxOrder;//箱序号

    public String getBoxCount() {
        return BoxCount;
    }

    public void setBoxCount(String boxCount) {
        BoxCount = boxCount;
    }

    public String getBoxOrder() {
        return BoxOrder;
    }

    public void setBoxOrder(String boxOrder) {
        BoxOrder = boxOrder;
    }

    public String getTimeID() {
        return TimeID;
    }

    public void setTimeID(String timeID) {
        TimeID = timeID;
    }

    public String getActionTime() {
        return ActionTime;
    }

    public void setActionTime(String actionTime) {
        ActionTime = actionTime;
    }

    public String getNextOutTime() {
        return NextOutTime;
    }

    public void setNextOutTime(String nextOutTime) {
        NextOutTime = nextOutTime;
    }

    public String getBoxType() {
        return BoxType;
    }

    public void setBoxType(String boxType) {
        BoxType = boxType;
    }

    public String getBoxStatus() {
        return BoxStatus;
    }

    public void setBoxStatus(String boxStatus) {
        BoxStatus = boxStatus;
    }

    public String getTradeAction() {
        return TradeAction;
    }

    public void setTradeAction(String tradeAction) {
        TradeAction = tradeAction;
    }

    public String getBoxName() {
        return BoxName;
    }

    public void setBoxName(String boxName) {
        BoxName = boxName;
    }

    public String getBoxID() {
        return BoxID;
    }

    public void setBoxID(String boxID) {
        BoxID = boxID;
    }

    public String getSiteID() {
        return SiteID;
    }

    public void setSiteID(String siteID) {
        SiteID = siteID;
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
