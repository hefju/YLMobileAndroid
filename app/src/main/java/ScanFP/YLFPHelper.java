package ScanFP;

import android.content.Context;

import com.za.finger.FingerHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import TaskClass.FingerPrint;
import YLDataService.FingerPrintDBSer;
import cn.pda.serialport.Tools;

/**
 * Created by Administrator on 2018/12/15.
 * 指纹管理工具.用于比较,插入,更新,读取指纹
 */

public class YLFPHelper {
    public void setmFingerHelper(FingerHelper mFingerHelper) {
        this.mFingerHelper = mFingerHelper;
    }

    private FingerHelper mFingerHelper;

    //比较指纹,BufferA是指纹数组,BufferB是用户按的指纹
    private int MatchFP(List<FingerPrint> BufferA, String BufferB)  {

        if (BufferB.length() == 0) {
            return -2;//未获取指纹  throw new Exception("未获取指纹");
        }
        if (BufferA.size() == 0) {
            return -3;//未录入指纹   throw new Exception("未录入指纹");
        }
        byte[] FPB = Tools.HexString2Bytes(BufferB);
        mFingerHelper.downChar2Buffer(mFingerHelper.CHAR_BUFFER_B, FPB, 512);
        List<Integer> integerList = new ArrayList<>();
        for (FingerPrint fp : BufferA) {

            byte[] FPA = Tools.HexString2Bytes(fp.getFinger());
            mFingerHelper.downChar2Buffer(mFingerHelper.CHAR_BUFFER_A, FPA, 512);
            int[] iScore = {0, 0};
            mFingerHelper.match(iScore);
            //myfpInfo.FPMessage("匹配完成");
            integerList.add(iScore[0]);
        }
        Integer matchIndex = -1, maxScore = 0;
        for (int j = 0; j < integerList.size(); j++) {
            int i = integerList.get(j);
            if (i > 60) {
                maxScore = i;
                matchIndex = j;
            }
        }
        return matchIndex;//返回匹配下标,如果是-1表示没有匹配
//            int Max = Collections.max(integerList);
    }

    //通过指纹查找员工编号,如果没有就返回"",
    public String FindEmpByFP(String BufferB, Context context){
        String empnum="";
        //把指纹全部读取出来,看看有没有性能问题
        FingerPrintDBSer dbSer=new FingerPrintDBSer(context);
        List<FingerPrint> list=dbSer.GetAllFingerPrint();
        int index=MatchFP(list,BufferB);//-1表示没有找到指纹, -2表示未获取带用户按的指纹, -3表示指纹库里面没有指纹数据
        if(index>-1){
            empnum= list.get(index).getEmpNum();
        }
        return empnum;
    }

    //读取所有的指纹数据,这里还需要完善,怎样使用?
    public void LoadFpFromDb(FingerPrintDBSer fpDb){
        List<FingerPrint> list= fpDb.GetAllFingerPrint();
    }

    //根据员工编号保存指纹
    public int SaveFp(String EmpNum,String BufferA,FingerPrintDBSer fpDb){

        FingerPrint fingerPrint=new FingerPrint();
        fingerPrint.setEmpNum(EmpNum);
        fingerPrint.setFinger(BufferA);
        if(fpDb.Exists(fingerPrint)){//检查数据库是否存在员工编号
            return fpDb.UpdateFingerPrint(fingerPrint);//存在就更新
        }else{
            return fpDb.InsFingerPrint(fingerPrint);//不存在就插入
        }
    }
}
