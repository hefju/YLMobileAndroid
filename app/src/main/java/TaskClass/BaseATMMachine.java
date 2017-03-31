package TaskClass;

/**
 * Created by Administrator on 2016-11-18.
 */

public class BaseATMMachine {

    private String ServerReturn;//服务器返回的成功与否，成功1
    private String MachineID ;//机器ID
    private String SiteID ;//网点ID
    private String MachineName ;//机器名字
    private String MachineType ;//机型
    private String MachineNo ;//机器码
    private String MachineHFNo ;//机器HF卡
    private String MachineCode ;//机器条形码
    private String Mark  ;//标记添加=1、修改=2、删除=3
    private String ServerTime  ;//服务器时间

    public String getServerReturn() {
        return ServerReturn;
    }

    public void setServerReturn(String serverReturn) {
        ServerReturn = serverReturn;
    }

    public String getMachineID() {
        return MachineID;
    }

    public void setMachineID(String machineID) {
        MachineID = machineID;
    }

    public String getSiteID() {
        return SiteID;
    }

    public void setSiteID(String siteID) {
        SiteID = siteID;
    }

    public String getMachineName() {
        return MachineName;
    }

    public void setMachineName(String machineName) {
        MachineName = machineName;
    }

    public String getMachineType() {
        return MachineType;
    }

    public void setMachineType(String machineType) {
        MachineType = machineType;
    }

    public String getMachineNo() {
        return MachineNo;
    }

    public void setMachineNo(String machineNo) {
        MachineNo = machineNo;
    }

    public String getMachineHFNo() {
        return MachineHFNo;
    }

    public void setMachineHFNo(String machineHFNo) {
        MachineHFNo = machineHFNo;
    }

    public String getMachineCode() {
        return MachineCode;
    }

    public void setMachineCode(String machineCode) {
        MachineCode = machineCode;
    }

    public String getMark() {
        return Mark;
    }

    public void setMark(String mark) {
        Mark = mark;
    }

    public String getServerTime() {
        return ServerTime;
    }

    public void setServerTime(String serverTime) {
        ServerTime = serverTime;
    }



}
