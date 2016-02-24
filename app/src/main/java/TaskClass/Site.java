package TaskClass;

import java.util.List;

/**
 * Created by Administrator on 2015/1/19.
 */
public class Site {
    public int  Id;
    public String ServerReturn ;//服务器返回的成功与否，成功1，其他异常是e.toString().
    public String TaskID ;
    public String SiteID ;    //网点ID
    public String SiteName ;//网点名
    public String SiteManager ;//网点负责人
    public String SiteManagerPhone ;//网点负责人电话
    public String SiteType ;//网点类型ATM还是网点
    public String Status ;//交接状态: 未交接, 交接中, 已交接
    public String ATMCount ;//ATM数目
    public List<ArriveTime> lstArriveTime;
    public String SiteRank;//网点级别

    public Site(){}


    public String getSiteRank() {
        return SiteRank;
    }

    public void setSiteRank(String siteRank) {
        SiteRank = siteRank;
    }

    public List<ArriveTime> getLstArriveTime() {
        return lstArriveTime;
    }

    public void setLstArriveTime(List<ArriveTime> lstArriveTime) {
        this.lstArriveTime = lstArriveTime;
    }

    public String getATMCount() {
        return ATMCount;
    }

    public void setATMCount(String ATMCount) {
        this.ATMCount = ATMCount;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getSiteType() {
        return SiteType;
    }

    public void setSiteType(String siteType) {
        SiteType = siteType;
    }

    public String getSiteManagerPhone() {
        return SiteManagerPhone;
    }

    public void setSiteManagerPhone(String siteManagerPhone) {
        SiteManagerPhone = siteManagerPhone;
    }

    public String getSiteManager() {
        return SiteManager;
    }

    public void setSiteManager(String siteManager) {
        SiteManager = siteManager;
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

    @Override
    public String toString() {
        return "Site{" +
                "ServerReturn='" + ServerReturn + '\'' +
                ", TaskID='" + TaskID + '\'' +
                ", SiteID='" + SiteID + '\'' +
                ", SiteName='" + SiteName + '\'' +
                ", SiteManager='" + SiteManager + '\'' +
                ", ATMCount='" + ATMCount + '\'' +
                ", lstArriveTime=" + lstArriveTime +
                ", SiteRank='" + SiteRank + '\'' +
                '}';
    }
}
