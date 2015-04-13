package TaskClass;

/**
 * Created by rush on 2015-04-13.
 */
public class BaseClient_HF {
    public int  Id;
    public String ServerReturn ;//服务器返回的成功与否，成功1，其他异常是e.toString().
    public String ClientID ;//客户ID
    public String HFNo;//HFNo  IC卡的号码。
    public String Mark ;//标记添加=1、修改=2、删除=3
    public String ServerTime;//服务器时间。

}
