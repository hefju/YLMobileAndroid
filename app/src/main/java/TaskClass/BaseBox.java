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

    public void CacheBaseBox(Context ctx,List<BaseBox> lst){
        ArrayList<BaseBox> lstAdd=new ArrayList<>();
        ArrayList<BaseBox> lstUpdate=new ArrayList<>();
        ArrayList<BaseBox> lstDel=new ArrayList<>();
        for (BaseBox x : lst){
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
        BaseBoxDBSer dbSer = new BaseBoxDBSer(ctx);
        if(lstDel.size()>0)
        dbSer.DeleteBaseEmpByBoxID(lstDel);
        if(lstUpdate.size()>0)
        dbSer.UpdateBaseEmpByBoxID(lstUpdate);//update 不能根据ID来update而是根据EmpID来update的
        if(lstAdd.size()>0)
        dbSer.InsertBox(lstAdd);
    }
}
