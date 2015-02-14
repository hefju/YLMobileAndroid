package TaskClass;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import YLDataService.BaseClientDBSer;

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


    public void CacheBaseClient(Context ctx,List<BaseClient> lst){
        ArrayList<BaseClient> lstAdd=new ArrayList<>();
        ArrayList<BaseClient> lstUpdate=new ArrayList<>();
        ArrayList<BaseClient> lstDel=new ArrayList<>();
        for (BaseClient x : lst){
            if(x.Mark.equals("1")){
                lstAdd.add(x);
            }else if(x.Mark.equals("2")){
                lstUpdate.add(x);
            }else if(x.Mark.equals("3")){
                lstDel.add(x);
            }
        }
        BaseClientDBSer dbSer = new BaseClientDBSer(ctx);
        dbSer.DeleteBaseClientByEmpID(lstDel);
        dbSer.UpdateBaseClientByEmpID(lstUpdate);//update 不能根据ID来update而是根据EmpID来update的
        dbSer.InsertBaseClient(lstAdd);
    }
}

