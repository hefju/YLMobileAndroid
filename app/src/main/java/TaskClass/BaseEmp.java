package TaskClass;

import android.content.Context;
import android.text.style.ForegroundColorSpan;

import java.util.ArrayList;
import java.util.List;

import YLDataService.BaseEmpDBSer;

/**
 * Created by Administrator on 2015/2/11 0011.
 */

public class BaseEmp//员工类
{
    public int  Id;
    public String ServerReturn ;//服务器返回的成功与否，成功1，其他异常是e.toString().
    public String EmpID ;//员工ID
    public String EmpName ;//员工名称
    public String EmpNo ;//员工号
    public String EmpHFNo ;//员工卡号
    public String EmpWorkState ;//员工岗位
    public String EmpJJNo ;//交接证号
    public String Mark ;//标记添加=1、修改=2、删除=3
    public String ServerTime;//服务器时间。

    public String getEmpID() {
        return EmpID;
    }

    public void setEmpID(String empID) {
        EmpID = empID;
    }

    public void CacheBaseEmp(Context ctx,List<BaseEmp> lst){
        ArrayList<BaseEmp> lstAdd=new ArrayList<>();
        ArrayList<BaseEmp> lstUpdate=new ArrayList<>();
        ArrayList<BaseEmp> lstDel=new ArrayList<>();
        for (BaseEmp x : lst){
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
        BaseEmpDBSer dbSer = new BaseEmpDBSer(ctx);
        if(lstDel.size()>0)
        dbSer.DeleteBaseEmpByEmpID(lstDel);
        if(lstUpdate.size()>0)
        dbSer.UpdateBaseEmpByEmpID(lstUpdate);//update 不能根据ID来update而是根据EmpID来update的
        if(lstAdd.size()>0)
        dbSer.InsertBaseEmp(lstAdd);
    }

    @Override
    public String toString() {
        return "BaseEmp{" +
                "Id=" + Id +
                ", ServerReturn='" + ServerReturn + '\'' +
                ", EmpID='" + EmpID + '\'' +
                ", EmpName='" + EmpName + '\'' +
                ", EmpNo='" + EmpNo + '\'' +
                ", EmpHFNo='" + EmpHFNo + '\'' +
                ", EmpWorkState='" + EmpWorkState + '\'' +
                ", EmpJJNo='" + EmpJJNo + '\'' +
                ", Mark='" + Mark + '\'' +
                ", ServerTime='" + ServerTime + '\'' +
                '}';
    }
}
