package TaskClass;

/**
 * Created by Administrator on 2015/2/11 0011.
 */

public class BaseClient //客户类
{
    public int  Id;
    public String ServerReturn ;//服务器返回的成功与否，成功1，其他异常是e.toString().
    public String ClientID ;//客户ID
    public String ClientName ;//客户名称
    public String ClientType ;//客户类型
    public String Mark ;//标记添加=1、修改=2、删除=3
    public String ServerTime;//服务器时间。
}

