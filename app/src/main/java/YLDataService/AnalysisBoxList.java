package YLDataService;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import TaskClass.Box;
import YLSystemDate.YLSystem;

/**
 * Created by Administrator on 2015-06-04.
 */
public class AnalysisBoxList {

    public List<String> AnsysisBoxList(List<Box> boxList){
        int moneybox = 0;
        int cardbox = 0;
        int voucherbox = 0;
        int voucherbag = 0;
        int getbox = 0;
        int givebox = 0;
        int fullbox = 0;
        int emptybox = 0;

        for (Box box:boxList){
            switch (box.getBoxType()){
                case"款箱":moneybox++;
                    break;
                case"卡箱":cardbox++;
                    break;
                case"凭证箱":voucherbox++;
                    break;
                case"凭证袋":voucherbag++;
                    break;
            }
//            if (box.getTradeAction().equals("收")){
//                getbox++;
//            }else {givebox++;}
            if (box.getBoxStatus().equals("实")){
                fullbox++;
            }else {emptybox++;}
        }

        List<String> stringList = new ArrayList<>();
        stringList.add(moneybox+"");
        stringList.add(cardbox+"");
        stringList.add(voucherbox+"");
        stringList.add(voucherbag+"");
        stringList.add(getbox+"");
        stringList.add(givebox+"");
        stringList.add(fullbox+"");
        stringList.add(emptybox+"");
        return stringList;
    }

}
