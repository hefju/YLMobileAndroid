package ScanFP;

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
    private FingerHelper mFingerHelper;
    //比较指纹,BufferA是指纹数组,BufferB是用户按的指纹
    public int MatchFP(List<String> BufferA, String BufferB) throws Exception {

        if (BufferB.length() == 0) {
            throw new Exception("未获取指纹");
        }
        if (BufferA.size() == 0) {
            throw new Exception("未录入指纹");
        }
        byte[] FPB = Tools.HexString2Bytes(BufferB);
        mFingerHelper.downChar2Buffer(mFingerHelper.CHAR_BUFFER_B, FPB, 512);
        List<Integer> integerList = new ArrayList<>();
        for (String s : BufferA) {

            byte[] FPA = Tools.HexString2Bytes(s);
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
