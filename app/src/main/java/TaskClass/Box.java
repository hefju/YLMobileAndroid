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

    public Box(){}

    public Box(Box box) {
        Id = box.getId();
        ServerReturn = box.getServerReturn();
        SiteID = box.getSiteID();
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
                '}';
    }
}
