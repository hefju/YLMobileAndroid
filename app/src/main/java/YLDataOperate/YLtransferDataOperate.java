package YLDataOperate;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import TaskClass.ArriveTime;
import TaskClass.Box;
import TaskClass.BoxCombyTime;
import TaskClass.Site;
import TaskClass.YLTask;
import YLDataService.YLBoxScanCheck;
import YLSystemDate.YLSysTime;
import YLSystemDate.YLSystem;

/**
 * Created by Administrator on 2016-07-06.
 */
public class YLtransferDataOperate {

    public static List<Box> Transferingboxes;//交接中款箱

//    public static List<Box> AllTransferboxes;//全交接款箱列表

    public static List<Box> Transferedboxes;//已交接完成款箱列表

    public static Site ChooseSite;

    public static String SitetimeID;

    public static int SiteTaskTimeID;

    public static Site getChooseSite() {
        return ChooseSite;
    }

    public static void setChooseSite(Site chooseSite) {
        ChooseSite = chooseSite;
    }

    public static String getSitetimeID() {
        return SitetimeID;
    }

    public static void setSitetimeID(String sitetimeID) {
        SitetimeID = sitetimeID;
    }

    public static int getSiteTaskTimeID() {
        return SiteTaskTimeID;
    }

    public static void setSiteTaskTimeID(int siteTaskTimeID) {
        SiteTaskTimeID = siteTaskTimeID;
    }

    public static List<Box> getTransferedboxes() {
        if (Transferedboxes == null){
            Transferedboxes = new ArrayList<>();
        }
        return Transferedboxes;
    }

    public static void setTransferedboxes(List<Box> transferedboxes) {
        Transferedboxes = transferedboxes;
    }

    public static List<Box> getTransferingboxes() {
        if (Transferingboxes == null) {
            return new ArrayList<>();
        } else {
            return Transferingboxes;
        }
    }


//    public static List<Box> getAllTransferboxes() {
//        if (AllTransferboxes == null){
//            AllTransferboxes = new ArrayList<>();
//        }
//        return AllTransferboxes;
//    }

//    public static void setAllTransferboxes(List<Box> allTransferboxes) {
//        if (allTransferboxes != null){
//            if (allTransferboxes.size()>0){
//                for (Box box : allTransferboxes) {
//                    AllTransferboxes.add(box);
//                }
//            }
//        }
//    }

    //初始化交接数据
    public void InitBoxes(){
//        if (AllTransferboxes != null){
//            Transferedboxes.clear();
//            for (Box transferedbox : AllTransferboxes) {
//                Transferedboxes.add(transferedbox);
//            }
//        }else {
//            AllTransferboxes = new ArrayList<>();
//            Transferedboxes = new ArrayList<>();
//        }

        if (Transferedboxes == null){
            Transferedboxes = new ArrayList<>();
            Transferingboxes = new ArrayList<>();
        }else {
            Transferingboxes = new ArrayList<>();
        }

    }

    //返回交接前数据
    public void BackupBoxes(){
//        AllTransferboxes.clear();
//        if (Transferedboxes != null){
//            for (Box transferedbox : Transferedboxes) {
//                AllTransferboxes.add(transferedbox);
//            }
//        }else {
//            AllTransferboxes = new ArrayList<>();
//            Transferedboxes = new ArrayList<>();
//        }
    }

    public static void setTransferingboxes(List<Box> transferingboxes) {

        if (transferingboxes != null) {
            YLtransferDataOperate.Transferingboxes = transferingboxes;
        }
        else{
            YLtransferDataOperate.Transferingboxes = new ArrayList<>();
        }
    }

    public Box ChecklastBoxstatus (List<Box> boxList, Box box){

        BoxCombyTime ylBoxComparator = new BoxCombyTime();
        Collections.sort(boxList,ylBoxComparator);

        int index = 0;
        for (int i = 0; i < boxList.size(); i++) {
            if (boxList.get(i).getBoxID().equals(box.getBoxID())){
                index = i;
            }
        }

        if (index == 0){
            Box rebox = new Box();
            rebox.setBoxID("0");
            rebox.setBoxName("无数据");
            return rebox;
        }else {
            return  boxList.get(index);
        }
    }

    public int GetTaskTimeIDbyyltask(YLTask task){
        if (task.getId() == null|| task.getId() ==0){
            return 1 ;
        }else {
            return task.getId() ;
        }
    }

    public ArriveTime AddArriveTime(YLTask ylTask,String SiteID,int TaskTimeID ) {

        List<ArriveTime> arriveTimeList = new ArrayList<>();
        ArriveTime arriveTime = new ArriveTime();
        try {
            for (int i = 0; i < ylTask.lstSite.size(); i++) {
                if (ylTask.lstSite.get(i).getSiteID().equals(SiteID)) {
                    Site site = ylTask.lstSite.get(i);
                    arriveTimeList = site.getLstArriveTime();

                    int TimeID = 0;
                    if (arriveTimeList == null) {
                        TimeID = 1;
                    } else {
                        TimeID = arriveTimeList.size() + 1;
                    }
                    arriveTime.setServerReturn("1");
                    arriveTime.setEmpID(YLSystem.getUser().getEmpID());
                    arriveTime.setATime(YLSysTime.GetStrCurrentTime());
                    arriveTime.setTradeBegin(YLSysTime.GetStrCurrentTime());
                    arriveTime.setTimeID(TimeID + "");
                    arriveTime.setTaskTimeID(TaskTimeID);
                    arriveTime.setSiteID(SiteID);
                    arriveTime.setPrintCount(0);
                    arriveTime.setPrintStatus("未打印");
                    arriveTime.setClientHFNO("");
                    arriveTime.setTradeState("1");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arriveTime;
    }

    public ArriveTime GetArrivttimebyTaskTimeID(YLTask ylTask,String SiteID, int TaskTimeID){
//        for (Site site : ylTask.lstSite) {
//            site.get
//        }
        return null;
    }

    //送箱：检查当前交接款箱是否有重复
    public boolean Repeatbox(String boxid){

        boolean checkgive = false;
        for (Box box : Transferingboxes) {
            if (box.getBoxID().equals(boxid) & box.getTradeAction().equals("送")){
                checkgive = true;
            }
        }
        return  checkgive;
    }

    //收箱:检查款箱最后状态为收
    public boolean Repeatgetbox(String boxid){
        String checkbox = "";
        BoxCombyTime ylBoxComparator = new BoxCombyTime();
        Collections.sort(Transferedboxes,ylBoxComparator);
        Collections.sort(Transferingboxes,ylBoxComparator);

        for (Box box : Transferedboxes) {
            if (box.getBoxID().equals(boxid)){
                checkbox =box.getTradeAction();
            }
        }
        for (Box box : Transferingboxes) {
            if (box.getBoxID().equals(boxid)){
                checkbox =box.getTradeAction();
            }
        }
        return checkbox.equals("收");
    }

    public boolean CheckBoxName(Box box, String btnname){
        return box.getBoxName().startsWith("粤龙临") &btnname.equals("款箱类");
    }

    public String TranferBoxcount(int taskTimeID){
//        int count = 0;
//        for (Box allTransferbox : AllTransferboxes) {
//            if (allTransferbox.getTaskTimeID() == taskTimeID){
//                count ++;
//            }
//        }

        return Transferingboxes.size()+1+"";
    }

    public Box BoxofNoCarbox(Context context,String boxid,String SiteID,String TimeID,int TaskTimeID,
                             String TaskType,String boxToT,String boxstatus){
        Box box = YLBoxScanCheck.CheckBoxbyUHF(boxid,context);
        box.setBoxStatus(boxstatus);
        box.setSiteID(SiteID);
        box.setTimeID(TimeID);
        box.setBoxCount("1");
        box.setBoxOrder(TranferBoxcount(TaskTimeID));
        box.setBoxTaskType(TaskType);
        box.setTaskTimeID(TaskTimeID);
        box.setActionTime(YLSysTime.GetStrCurrentTime());
        box.setTradeAction("送");
        box.setBoxToT(boxToT);
        box.setId(1);
        return  box;
    }

    public List<String> ShowBoxListGather(){

        int emptybox = 0;
        int fullbox = 0;
        int getbox = 0;
        int givebox = 0;
        int moneybox = 0;
        int cardbox = 0;
        int voucher = 0;
        int voucherbag = 0;

        try {
            for (Box box : Transferingboxes) {
//               if (box.getTaskTimeID() == tasktimeid){
                   if (box.getTradeAction().equals("收")){
                       getbox++;
                   }else{
                       givebox++;
                   }
                   if (box.getBoxStatus().equals("空")){
                       emptybox++;
                   }else {
                       fullbox++;
                   }
                   switch (box.getBoxType()){
                       case "款箱":moneybox++;
                           break;
                       case "卡箱":cardbox++;
                           break;
                       case "凭证箱":voucher++;
                           break;
                       case "凭证袋":voucherbag++;
                           break;
                   }
//               }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<String> strings = new ArrayList<>();
        strings.add("收箱:" + getbox);
        strings.add("送箱:" + givebox);
        strings.add("实箱:" + fullbox);
        strings.add("空箱:" + emptybox);
        strings.add("款箱:" + moneybox);
        strings.add("卡箱:" + cardbox);
        strings.add("凭证箱:\r\n     " + voucher);
        strings.add( "凭证袋:\r\n     " + voucherbag);
        return strings;
    }

    public YLTask AcieveData(String Sietid,ArriveTime arriveTime,YLTask ylTask,int TaskTimeID){
        try {
            ylTask.lstBox = YLtransferDataOperate.getTransferedboxes();
            for (int j = 0; j < ylTask.lstSite.size(); j++) {
                if (ylTask.lstSite.get(j).getSiteID().equals(Sietid)) {
                    ylTask.lstSite.get(j).setStatus("已完成");
                }
            }
            arriveTime.setTradeEnd(YLSysTime.GetStrCurrentTime());
            arriveTime.setTradeState("1");

            for (int j = 0; j < ylTask.lstSite.size(); j++) {
                if (ylTask.lstSite.get(j).getSiteID().equals(Sietid)) {
                    Site site = ylTask.lstSite.get(j);
                    List<ArriveTime> getArrTiemList = site.getLstArriveTime();
                    if (getArrTiemList == null) {
                        getArrTiemList = new ArrayList<>();
                        getArrTiemList.add(arriveTime);
                    } else {
                        getArrTiemList.add(arriveTime);
                    }
                    site.setLstArriveTime(getArrTiemList);
                    ylTask.lstSite.set(j, site);
                }
            }
            for (Box box : Transferingboxes) {
                ylTask.lstBox.add(box);
            }

            if (ylTask.lstarrivetime == null){
                ylTask.lstarrivetime = new ArrayList<>();
            }
            ylTask.lstarrivetime.add(arriveTime);

            ylTask.setId(TaskTimeID + 1);
            ylTask.setLstCarBox(YLCarBoxOperate.getYLEditeCarBoxList());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ylTask;
    }

    public List<String> SpTimeID(String Siteid){
        Set<String> set = new HashSet<String>(new ArrayList<String>());

        for (Box box : Transferedboxes) {
            if (box.getSiteID().equals(Siteid)){
                set.add(box.getTimeID());
            }
        }
        List<String> list = new ArrayList<>();
        for (String s : set) {
            list.add(s);
        }
        if (list.size() == 0){
            list.add("1");
        }

        return list;
    }

    public List<Box> EditerBoxDisplay(Site Site,String timeid){

        List<Box> boxes = new ArrayList<>();
        for (Box box : Transferedboxes) {
            if (box.getSiteID().equals(Site.getSiteID())
                    & box.getTimeID().equals(timeid)){
                boxes.add(box);
            }
        }
        return  boxes;
    }

}
