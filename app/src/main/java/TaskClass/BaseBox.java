package TaskClass;

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
}
