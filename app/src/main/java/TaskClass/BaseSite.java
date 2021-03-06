package TaskClass;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import YLDataService.BaseSiteDBSer;

/**
 * Created by Administrator on 2015/2/11 0011.
 */
public class BaseSite //网点类
{
    public int  Id;
    public String ServerReturn;//服务器返回的成功与否，成功1，其他异常是e.toString().
    public String SiteID;//网点ID
    public String SiteName;//网点名称
    public String SiteType;//网点类型
    public String ClientID;//客户ID
    public String SiteBCNo;//条形码
    public String Mark ;//标记添加=1、修改=2、删除=3
    public String ServerTime;//服务器时间。
    public String SiteHFNo;//网点交接卡号


    public String getSiteHFNo() {
        return SiteHFNo;
    }

    public void setSiteHFNo(String siteHFNo) {
        SiteHFNo = siteHFNo;
    }

    public String getSiteID() {
        return SiteID;
    }

    public void setSiteID(String siteID) {
        SiteID = siteID;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getServerReturn() {
        return ServerReturn;
    }

    public void setServerReturn(String serverReturn) {
        ServerReturn = serverReturn;
    }

    public String getSiteName() {
        return SiteName;
    }

    public void setSiteName(String siteName) {
        SiteName = siteName;
    }

    public String getSiteType() {
        return SiteType;
    }

    public void setSiteType(String siteType) {
        SiteType = siteType;
    }

    public String getClientID() {
        return ClientID;
    }

    public void setClientID(String clientID) {
        ClientID = clientID;
    }

    public String getSiteBCNo() {
        return SiteBCNo;
    }

    public void setSiteBCNo(String siteBCNo) {
        SiteBCNo = siteBCNo;
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

    @Override
    public String toString() {
        return "BaseSite{" +
                "Id=" + Id +
                ", ServerReturn='" + ServerReturn + '\'' +
                ", SiteID='" + SiteID + '\'' +
                ", SiteName='" + SiteName + '\'' +
                ", SiteType='" + SiteType + '\'' +
                ", ClientID='" + ClientID + '\'' +
                ", SiteBCNo='" + SiteBCNo + '\'' +
                ", Mark='" + Mark + '\'' +
                ", ServerTime='" + ServerTime + '\'' +
                ", SiteHFNo='" + SiteHFNo + '\'' +
                '}';
    }

    public void CacheBaseSite(Context ctx, List<BaseSite> lst){
        ArrayList<BaseSite> lstAdd=new ArrayList<>();
        ArrayList<BaseSite> lstUpdate=new ArrayList<>();
        ArrayList<BaseSite> lstDel=new ArrayList<>();
        for (BaseSite x : lst){
            if(x.Mark==null)
                continue;
            if(x.Mark.equals("1")){
                lstAdd.add(x);
            }else if(x.Mark.equals("2")){
                lstUpdate.add(x);
            }else if(x.Mark.equals("3")){
                lstDel.add(x);
            }
        }
        BaseSiteDBSer dbSer = new BaseSiteDBSer(ctx);
        if(lstDel.size()>0)
        dbSer.DeleteBaseSiteByEmpID(lstDel);
        if(lstUpdate.size()>0)
        dbSer.UpdateBaseSiteByEmpID(lstUpdate);//update 不能根据ID来update而是根据EmpID来update的
        if(lstAdd.size()>0)
        dbSer.InsertBaseSite(lstAdd);
    }
}
