package YLDataOperate;

import android.content.Context;
import android.support.v4.app.NavUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import TaskClass.Box;
import TaskClass.Site;
import TaskClass.YLTask;
import YLDataService.WebServerYLSite;
import YLSystemDate.YLSysTime;
import YLSystemDate.YLSystem;

/**
 * Created by Administrator on 2016-07-06.
 */
public class YLCarBoxOperate {

    public static List<Box> YLCurrectCarBoxList;

    public static List<Box> YLEditeCarBoxList;

    public static List<Box> getYLCurrectCarBoxList() {
        if (YLCurrectCarBoxList == null){
            return new ArrayList<>();
        }else {
            return YLCurrectCarBoxList;
        }
    }

    public static void setYLCurrectCarBoxList(List<Box> YLCurrectCarBoxes) {

        List<Box> boxes = new ArrayList<>();

        if (YLCurrectCarBoxes != null) {
            Log.e(YLSystem.getKimTag(),YLCurrectCarBoxes.size()+"初始数量");
            for (Box box : YLCurrectCarBoxes) {
                boxes.add(box);
            }
        }else{
            YLCurrectCarBoxes = new ArrayList<>();
        }
        YLCarBoxOperate.YLCurrectCarBoxList = YLCurrectCarBoxes;
        YLCarBoxOperate.YLEditeCarBoxList = boxes;
    }

    public static List<Box> getYLEditeCarBoxList() {
        if (YLEditeCarBoxList == null){
            return new ArrayList<>();
        }else {
            return YLEditeCarBoxList;
        }
    }

//    public static void setYLEditeCarBoxList(List<Box> YLEditeCarBoxList) {
//
//        List<Box> boxes = new ArrayList<>();
//        for (Box box : YLEditeCarBoxList) {
//            boxes.add(box);
//        }
//        YLCarBoxOperate.YLEditeCarBoxList = boxes;
//    }


    public void AddBoxOnEditeCarBox(Box box){
        YLEditeCarBoxList.add(box);
    }

    public void DeleteBoxOnEditCarbox(int index){
        YLEditeCarBoxList.remove(index);
    }

    public boolean CheckCarboxbybox( Box box){
        for (Box checkbox : YLEditeCarBoxList) {
            if (checkbox.getBoxID().equals(box.getBoxID())){
                return true;
            }
        }
        return  false;
    }

    public boolean CheckCarboxbystr( String boxid){
        for (Box checkbox : YLEditeCarBoxList) {
            if (checkbox.getBoxID().equals(boxid)){
                return  true;
            }
        }
        return  false;
    }

    public Box RemoveCarBox(String boxid){
        for (int i = 0; i < YLEditeCarBoxList.size(); i++) {
            if (YLEditeCarBoxList.get(i).getBoxID().equals(boxid)){
                Box carbox = new Box(YLEditeCarBoxList.get(i));
                carbox.setTradeAction("送");
                carbox.setActionTime(YLSysTime.GetStrCurrentTime());
                YLEditeCarBoxList.remove(i);
                return  carbox;
            }
        }

        Box box = new Box();
        box.setBoxID("0");
        return box;
    }



    public void ReplaceEditCarbox(int index, Box box){
        YLEditeCarBoxList.set(index,box);
    }

    public boolean LoadCarboxlist(YLTask ylTask){
        if (ylTask.getLstSite() != null ){
            for (Site site : ylTask.getLstSite()) {
                if (site.getStatus().equals("已完成") ||site.getStatus().equals("已打印") ){
                    return false;
                }
            }

            if (ylTask.lstCarBox.size() >0){
                return false;
            }
        }
        return true;
    }

    //获取车内箱出库ID
    public String GetCarBoxOutID (Context context,String taskid){
        try {
            WebServerYLSite webServerYLSite = new WebServerYLSite();
            return webServerYLSite.GetCarBoxOutID2(context,taskid);
        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        }
//        return "0";
    }

    //获取车内款箱数并更新手持机
    public void UpdateCarbox(Context context,String taskid){
        try {
            WebServerYLSite webServerYLSite = new WebServerYLSite();
            List<Box> boxes =  webServerYLSite.GetCarBoxlist(context,taskid);
            Log.e(YLSystem.getKimTag(),boxes.toString());
            if (boxes.get(0).getServerReturn().contains("没有")){
                setYLCurrectCarBoxList(boxes);
            }else {
                boxes = new ArrayList<>();
                setYLCurrectCarBoxList(boxes);
            }
        } catch (Exception e) {
            List<Box> boxes = new ArrayList<>();
            setYLCurrectCarBoxList(boxes);
            e.printStackTrace();
        }
    }


}
