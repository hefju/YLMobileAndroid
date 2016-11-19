package TaskClass;

/**
 * Created by Administrator on 2016-11-07.
 */

public class ATMBox {
    public String ClientID;//客户ID
    public String ClientName;//客户名称
    public String UseClientID;//使用客户ID
    public String UseClientName;//使用客户名称
    public String BoxCode;//条形码
    public String BoxName;//钞盒名称
    public String BoxBrand;//钞盒品牌
    public String Boxtype;//钞盒类型
    public String Boxvalue;//面值
    public String Passageway;//通道
    public String TimeID;//出入库操作:1代表出库 2代表入库
    public String Actiontime;//操作时间
    public String SiteID;//网点ID；
    public String SiteHF;//网点HF码；

    public String getClientID() {
        return ClientID;
    }

    public void setClientID(String clientID) {
        ClientID = clientID;
    }

    public String getClientName() {
        return ClientName;
    }

    public void setClientName(String clientName) {
        ClientName = clientName;
    }

    public String getUseClientID() {
        return UseClientID;
    }

    public void setUseClientID(String useClientID) {
        UseClientID = useClientID;
    }

    public String getUseClientName() {
        return UseClientName;
    }

    public void setUseClientName(String useClientName) {
        UseClientName = useClientName;
    }

    public String getBoxCode() {
        return BoxCode;
    }

    public void setBoxCode(String boxCode) {
        BoxCode = boxCode;
    }

    public String getBoxName() {
        return BoxName;
    }

    public void setBoxName(String boxName) {
        BoxName = boxName;
    }

    public String getBoxBrand() {
        return BoxBrand;
    }

    public void setBoxBrand(String boxBrand) {
        BoxBrand = boxBrand;
    }

    public String getBoxtype() {
        return Boxtype;
    }

    public void setBoxtype(String boxtype) {
        Boxtype = boxtype;
    }

    public String getBoxvalue() {
        return Boxvalue;
    }

    public void setBoxvalue(String boxvalue) {
        Boxvalue = boxvalue;
    }

    public String getPassageway() {
        return Passageway;
    }

    public void setPassageway(String passageway) {
        Passageway = passageway;
    }

    public String getTimeID() {
        return TimeID;
    }

    public void setTimeID(String timeID) {
        TimeID = timeID;
    }

    public String getActiontime() {
        return Actiontime;
    }

    public void setActiontime(String actiontime) {
        Actiontime = actiontime;
    }

    public String getSiteID() {
        return SiteID;
    }

    public void setSiteID(String siteID) {
        SiteID = siteID;
    }

    public String getSiteHF() {
        return SiteHF;
    }

    public void setSiteHF(String siteHF) {
        SiteHF = siteHF;
    }

    public ATMBox(){}

    public ATMBox(ATMBox atmBox) {
        ClientID =atmBox.getClientID();
        ClientName = atmBox.getClientName();
        UseClientID = atmBox.getUseClientID();
        UseClientName = atmBox.getUseClientName();
        BoxCode = atmBox.getBoxCode();
        BoxName = atmBox.getBoxName();
        BoxBrand = atmBox.getBoxBrand();
        Boxtype = atmBox.getBoxtype();
        Boxvalue = atmBox.getBoxvalue();
        Passageway = atmBox.getPassageway();
        TimeID = atmBox.getTimeID();
        Actiontime = atmBox.getActiontime();
        SiteID = atmBox.getSiteID();
        SiteHF = atmBox.getSiteHF();
    }

    public ATMBox(String clientID, String clientName, String useClientID,
                  String useClientName, String boxCode, String boxName,
                  String boxBrand, String boxtype, String boxvalue,
                  String passageway, String timeID, String actiontime,
                  String siteID, String siteHF) {
        ClientID = clientID;
        ClientName = clientName;
        UseClientID = useClientID;
        UseClientName = useClientName;
        BoxCode = boxCode;
        BoxName = boxName;
        BoxBrand = boxBrand;
        Boxtype = boxtype;
        Boxvalue = boxvalue;
        Passageway = passageway;
        TimeID = timeID;
        Actiontime = actiontime;
        SiteID = siteID;
        SiteHF = siteHF;
    }

    @Override
    public String toString() {
        return "ATMBox{" +
                "ClientID='" + ClientID + '\'' +
                ", ClientName='" + ClientName + '\'' +
                ", UseClientID='" + UseClientID + '\'' +
                ", UseClientName='" + UseClientName + '\'' +
                ", BoxCode='" + BoxCode + '\'' +
                ", BoxName='" + BoxName + '\'' +
                ", BoxBrand='" + BoxBrand + '\'' +
                ", Boxtype='" + Boxtype + '\'' +
                ", Boxvalue='" + Boxvalue + '\'' +
                ", Passageway='" + Passageway + '\'' +
                ", TimeID='" + TimeID + '\'' +
                ", Actiontime='" + Actiontime + '\'' +
                ", SiteID='" + SiteID + '\'' +
                ", SiteHF='" + SiteHF + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ATMBox)) return false;

        ATMBox atmBox = (ATMBox) o;

        return getBoxCode().equals(atmBox.getBoxCode());

    }

    @Override
    public int hashCode() {
        return getBoxCode().hashCode();
    }
}
