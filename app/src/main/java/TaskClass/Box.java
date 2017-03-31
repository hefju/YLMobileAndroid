package TaskClass;

import java.util.Objects;

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
    public String BoxTaskType;//箱任务类型---中调或普通
    public String BoxOrder;//箱序号
    public String Valutcheck;//金库检查--多/符合/缺
    public String BaseValutOut;//出库基地
    public String BaseValutIn;//入库基地
    public String Remark;// 补打标签标记
    public int TaskTimeID;// 任务顺序
    public String BoxToT;//是否次日送
    public String Ylclearing;//是否上介清分

    public Box(){}

    public Box(Box box) {
        Id = box.getId();
        ServerReturn = box.getServerReturn();
        SiteID =  box.getSiteID();
        BoxID = box.getBoxID();
        BoxName = box.getBoxName();
        TradeAction = box.getTradeAction();
        BoxStatus = box.getBoxStatus();
        BoxType = box.getBoxType();
        NextOutTime = box.getNextOutTime();
        ActionTime = box.getActionTime();
        TimeID = box.getTimeID();
        BoxCount = box.getBoxCount();
        BoxTaskType = box.getBoxTaskType();
        BoxOrder = box.getBoxOrder();
        Valutcheck = box.getValutcheck();
        BaseValutOut = box.getBaseValutOut();
        BaseValutIn = box.getBaseValutIn();
        Remark = box.getRemark();
        TaskTimeID = box.getTaskTimeID();
        BoxToT = box.getBoxToT();
        Ylclearing = box.getYlclearing();
    }

    public String getYlclearing() {
        return Ylclearing;
    }

    public void setYlclearing(String ylclearing) {
        Ylclearing = ylclearing;
    }

    public String getBoxToT() {
        return BoxToT;
    }

    public void setBoxToT(String boxToT) {
        BoxToT = boxToT;
    }

    public int getTaskTimeID() {
        return TaskTimeID;
    }

    public void setTaskTimeID(int taskTimeID) {
        TaskTimeID = taskTimeID;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        this.Remark = remark;
    }

    public String getBaseValutIn() {
        return BaseValutIn;
    }

    public void setBaseValutIn(String baseValutIn) {
        BaseValutIn = baseValutIn;
    }

    public String getBaseValutOut() {
        return BaseValutOut;
    }

    public void setBaseValutOut(String baseValutOut) {
        BaseValutOut = baseValutOut;
    }

    public String getValutcheck() {
        return Valutcheck;
    }

    public void setValutcheck(String valutcheck) {
        Valutcheck = valutcheck;
    }

    public String getBoxTaskType() {
        return BoxTaskType;
    }

    public void setBoxTaskType(String boxTaskType) {
        BoxTaskType = boxTaskType;
    }

    public String getTimeID() {
        return TimeID;
    }

    public void setTimeID(String timeID) {
        TimeID = timeID;
    }

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

    @Override
    public String toString() {
        return "Box{" +
                "Id=" + Id +
                ", ServerReturn='" + ServerReturn + '\'' +
                ", SiteID='" + SiteID + '\'' +
                ", BoxID='" + BoxID + '\'' +
                ", BoxName='" + BoxName + '\'' +
                ", TradeAction='" + TradeAction + '\'' +
                ", BoxStatus='" + BoxStatus + '\'' +
                ", BoxType='" + BoxType + '\'' +
                ", NextOutTime='" + NextOutTime + '\'' +
                ", ActionTime='" + ActionTime + '\'' +
                ", TimeID='" + TimeID + '\'' +
                ", BoxCount='" + BoxCount + '\'' +
                ", BoxTaskType='" + BoxTaskType + '\'' +
                ", BoxOrder='" + BoxOrder + '\'' +
                ", Valutcheck='" + Valutcheck + '\'' +
                ", BaseValutOut='" + BaseValutOut + '\'' +
                ", BaseValutIn='" + BaseValutIn + '\'' +
                ", Remark='" + Remark + '\'' +
                ", TaskTimeID=" + TaskTimeID +
                ", BoxToT='" + BoxToT + '\'' +
                ", Ylclearing='" + Ylclearing + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Box)) return false;

        Box box = (Box) o;

        if (getTaskTimeID() != box.getTaskTimeID()) return false;
        if (getServerReturn() != null ? !getServerReturn().equals(box.getServerReturn()) : box.getServerReturn() != null)
            return false;
        if (getSiteID() != null ? !getSiteID().equals(box.getSiteID()) : box.getSiteID() != null)
            return false;
        if (getBoxID() != null ? !getBoxID().equals(box.getBoxID()) : box.getBoxID() != null)
            return false;
        if (getBoxName() != null ? !getBoxName().equals(box.getBoxName()) : box.getBoxName() != null)
            return false;
        if (getTradeAction() != null ? !getTradeAction().equals(box.getTradeAction()) : box.getTradeAction() != null)
            return false;
        if (getBoxStatus() != null ? !getBoxStatus().equals(box.getBoxStatus()) : box.getBoxStatus() != null)
            return false;
        if (getBoxType() != null ? !getBoxType().equals(box.getBoxType()) : box.getBoxType() != null)
            return false;
        if (getNextOutTime() != null ? !getNextOutTime().equals(box.getNextOutTime()) : box.getNextOutTime() != null)
            return false;
        if (getActionTime() != null ? !getActionTime().equals(box.getActionTime()) : box.getActionTime() != null)
            return false;
        if (getTimeID() != null ? !getTimeID().equals(box.getTimeID()) : box.getTimeID() != null)
            return false;
        if (getBoxCount() != null ? !getBoxCount().equals(box.getBoxCount()) : box.getBoxCount() != null)
            return false;
        if (getBoxTaskType() != null ? !getBoxTaskType().equals(box.getBoxTaskType()) : box.getBoxTaskType() != null)
            return false;
        if (getValutcheck() != null ? !getValutcheck().equals(box.getValutcheck()) : box.getValutcheck() != null)
            return false;
        if (getBaseValutOut() != null ? !getBaseValutOut().equals(box.getBaseValutOut()) : box.getBaseValutOut() != null)
            return false;
        if (getBaseValutIn() != null ? !getBaseValutIn().equals(box.getBaseValutIn()) : box.getBaseValutIn() != null)
            return false;
        if (getRemark() != null ? !getRemark().equals(box.getRemark()) : box.getRemark() != null)
            return false;
        if (getBoxToT() != null ? !getBoxToT().equals(box.getBoxToT()) : box.getBoxToT() != null)
            return false;
        return getYlclearing() != null ? getYlclearing().equals(box.getYlclearing()) : box.getYlclearing() == null;

    }

    @Override
    public int hashCode() {
        int result = getServerReturn() != null ? getServerReturn().hashCode() : 0;
        result = 31 * result + (getSiteID() != null ? getSiteID().hashCode() : 0);
        result = 31 * result + (getBoxID() != null ? getBoxID().hashCode() : 0);
        result = 31 * result + (getBoxName() != null ? getBoxName().hashCode() : 0);
        result = 31 * result + (getTradeAction() != null ? getTradeAction().hashCode() : 0);
        result = 31 * result + (getBoxStatus() != null ? getBoxStatus().hashCode() : 0);
        result = 31 * result + (getBoxType() != null ? getBoxType().hashCode() : 0);
        result = 31 * result + (getNextOutTime() != null ? getNextOutTime().hashCode() : 0);
        result = 31 * result + (getActionTime() != null ? getActionTime().hashCode() : 0);
        result = 31 * result + (getTimeID() != null ? getTimeID().hashCode() : 0);
        result = 31 * result + (getBoxCount() != null ? getBoxCount().hashCode() : 0);
        result = 31 * result + (getBoxTaskType() != null ? getBoxTaskType().hashCode() : 0);
        result = 31 * result + (getValutcheck() != null ? getValutcheck().hashCode() : 0);
        result = 31 * result + (getBaseValutOut() != null ? getBaseValutOut().hashCode() : 0);
        result = 31 * result + (getBaseValutIn() != null ? getBaseValutIn().hashCode() : 0);
        result = 31 * result + (getRemark() != null ? getRemark().hashCode() : 0);
        result = 31 * result + getTaskTimeID();
        result = 31 * result + (getBoxToT() != null ? getBoxToT().hashCode() : 0);
        result = 31 * result + (getYlclearing() != null ? getYlclearing().hashCode() : 0);
        return result;
    }
}
