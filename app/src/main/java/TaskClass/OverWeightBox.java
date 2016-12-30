package TaskClass;

/**
 * Created by Administrator on 2016-12-28.
 */

public class OverWeightBox {

    private String ServerReturn;//服务器返回的成功与否，成功1，其他异常是e.tostring().
    private String BoxID;//钱箱ID
    private String BoxName;//钱箱名
    private String BoxType;//钱箱类型   //款箱, 凭证, 卡箱
    private String ActionTime;//钱箱操作时间
    private String BoxWeight;//款箱重量

    public String getServerReturn() {
        return ServerReturn;
    }

    public void setServerReturn(String serverReturn) {
        ServerReturn = serverReturn;
    }

    public String getBoxID() {
        return BoxID;
    }

    public void setBoxID(String boxID) {
        BoxID = boxID;
    }

    public String getBoxName() {
        return BoxName;
    }

    public void setBoxName(String boxName) {
        BoxName = boxName;
    }

    public String getBoxType() {
        return BoxType;
    }

    public void setBoxType(String boxType) {
        BoxType = boxType;
    }

    public String getActionTime() {
        return ActionTime;
    }

    public void setActionTime(String actionTime) {
        ActionTime = actionTime;
    }

    public String getBoxWeight() {
        return BoxWeight;
    }

    public void setBoxWeight(String boxWeight) {
        BoxWeight = boxWeight;
    }

    @Override
    public String toString() {
        return "OverWeightBox{" +
                "ServerReturn='" + ServerReturn + '\'' +
                ", BoxID='" + BoxID + '\'' +
                ", BoxName='" + BoxName + '\'' +
                ", BoxType='" + BoxType + '\'' +
                ", ActionTime='" + ActionTime + '\'' +
                ", BoxWeight='" + BoxWeight + '\'' +
                '}';
    }
}
