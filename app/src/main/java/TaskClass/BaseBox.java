package TaskClass;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import YLDataService.BaseBoxDBSer;
import YLDataService.BaseEmpDBSer;

/**
 * Created by Administrator on 2015/2/11 0011.
 */
public class BaseBox //箱类
{
    public int  Id;
    public String ServerReturn ;//服务器返回的成功与否，成功1，其他异常是e.toString().
    public String BoxID ;//箱ID
    public String BoxName ;//箱名称
    public String BoxUHFNo ;//箱UHF卡号
    public String BoxBCNo ;//箱条形码号
    public String BoxType ;//箱类型
    public String ClientID ;//客户ID
    public String SiteID ;//网点ID
    public String Mark ;//标记添加=1、修改=2、删除=3
    public String ServerTime;//服务器时间。


    public String getBoxID() {
        return BoxID;
    }

    public void setBoxID(String boxID) {
        BoxID = boxID;
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

    public String getBoxName() {
        return BoxName;
    }

    public void setBoxName(String boxName) {
        BoxName = boxName;
    }

    public String getBoxUHFNo() {
        return BoxUHFNo;
    }

    public void setBoxUHFNo(String boxUHFNo) {
        BoxUHFNo = boxUHFNo;
    }

    public String getBoxBCNo() {
        return BoxBCNo;
    }

    public void setBoxBCNo(String boxBCNo) {
        BoxBCNo = boxBCNo;
    }

    public String getBoxType() {
        return BoxType;
    }

    public void setBoxType(String boxType) {
        BoxType = boxType;
    }

    public String getClientID() {
        return ClientID;
    }

    public void setClientID(String clientID) {
        ClientID = clientID;
    }

    public String getSiteID() {
        return SiteID;
    }

    public void setSiteID(String siteID) {
        SiteID = siteID;
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
        return "BaseBox{" +
                "Id=" + Id +
                ", ServerReturn='" + ServerReturn + '\'' +
                ", BoxID='" + BoxID + '\'' +
                ", BoxName='" + BoxName + '\'' +
                ", BoxUHFNo='" + BoxUHFNo + '\'' +
                ", BoxBCNo='" + BoxBCNo + '\'' +
                ", BoxType='" + BoxType + '\'' +
                ", ClientID='" + ClientID + '\'' +
                ", SiteID='" + SiteID + '\'' +
                ", Mark='" + Mark + '\'' +
                ", ServerTime='" + ServerTime + '\'' +
                '}';
    }

    public void CacheBaseBox(Context ctx,List<BaseBox> lst){
        ArrayList<BaseBox> lstAdd=new ArrayList<>();
        ArrayList<BaseBox> lstUpdate=new ArrayList<>();
        ArrayList<BaseBox> lstDel=new ArrayList<>();
        for (BaseBox x : lst){
            if(x.Mark==null)
                continue;
            switch (x.Mark) {
                case "1":
                    lstAdd.add(x);
                    break;
                case "2":
                    lstUpdate.add(x);
                    break;
                case "3":
                    lstDel.add(x);
                    break;
            }
        }
        BaseBoxDBSer dbSer = new BaseBoxDBSer(ctx);
        if(lstDel.size()>0)
        dbSer.DeleteBaseEmpByBoxID(lstDel);
        if(lstUpdate.size()>0)
        dbSer.UpdateBaseEmpByBoxID(lstUpdate);//update 不能根据ID来update而是根据EmpID来update的
        if(lstAdd.size()>0)
        dbSer.InsertBox(lstAdd);
    }
}
