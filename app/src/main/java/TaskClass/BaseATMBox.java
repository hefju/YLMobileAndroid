package TaskClass;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import YLDataService.ATMBoxDBSer;

/**
 * Created by Administrator on 2016-11-07.
 */

public class BaseATMBox {
    public String ATMBoxID ;//款箱ID
    public String ClientID;//客户ID
    public String UseClientID;//使用客户ID
    public String BoxCode;//条形码
    public String BoxName;//钞盒名称
    public String BoxBrand;//钞盒品牌
    public String Boxtype;//钞盒类型
    public String Boxvalue;//面值
    public String Passageway;//通道
    public String ServerReturn;
    public String Mark ;
    public String ServerTime ;

    public String getATMBoxID() {
        return ATMBoxID;
    }

    public void setATMBoxID(String ATMBoxID) {
        this.ATMBoxID = ATMBoxID;
    }

    public String getClientID() {
        return ClientID;
    }

    public void setClientID(String clientID) {
        ClientID = clientID;
    }

    public String getUseClientID() {
        return UseClientID;
    }

    public void setUseClientID(String useClientID) {
        UseClientID = useClientID;
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

    public String getServerReturn() {
        return ServerReturn;
    }

    public void setServerReturn(String serverReturn) {
        ServerReturn = serverReturn;
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
