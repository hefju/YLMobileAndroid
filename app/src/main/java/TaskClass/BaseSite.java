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
    public String Mark ;//标记添加=1、修改=2、删除=3
    public String ServerTime;//服务器时间。

    public void CacheBaseSite(Context ctx,List<BaseSite> lst){
        ArrayList<BaseSite> lstAdd=new ArrayList<>();
        ArrayList<BaseSite> lstUpdate=new ArrayList<>();
        ArrayList<BaseSite> lstDel=new ArrayList<>();
        for (BaseSite x : lst){
            if(x.Mark.equals("1")){
                lstAdd.add(x);
            }else if(x.Mark.equals("2")){
                lstUpdate.add(x);
            }else if(x.Mark.equals("3")){
                lstDel.add(x);
            }
        }
        BaseSiteDBSer dbSer = new BaseSiteDBSer(ctx);
        dbSer.DeleteBaseSiteByEmpID(lstDel);
        dbSer.UpdateBaseSiteByEmpID(lstUpdate);//update 不能根据ID来update而是根据EmpID来update的
        dbSer.InsertBaseSite(lstAdd);
    }
}
