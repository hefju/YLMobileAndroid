package TaskClass;

import java.util.List;

/**
 * Created by Administrator on 2015/1/19.
 */
public class Site {
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

}
