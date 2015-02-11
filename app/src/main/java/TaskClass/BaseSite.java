package TaskClass;

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
}
