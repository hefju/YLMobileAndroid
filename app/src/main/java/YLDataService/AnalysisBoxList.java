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

    public List<Integer> AnsysisBoxList(List<Box> boxList){
        int moneybox = 0;
        int cardbox = 0;
        int voucherbox = 0;
        int voucherbag = 0;
        int getbox = 0;
        int givebox = 0;
        int fullbox = 0;
        int emptybox = 0;


        for (Box box:boxList){
            int count =Integer.parseInt(box.getBoxCount()) ;
            try {
                switch (box.getBoxType()){
                    case"款箱":moneybox+=count;
                        break;
                    case"卡箱":cardbox+=count;
                        break;
                    case"凭证箱":voucherbox +=count;
                        break;
                    case"凭证袋":voucherbag +=count;
                        break;
                }
            }catch (Exception e){
                moneybox += 0;
                cardbox += 0;
                voucherbox += 0 ;
                voucherbag += 0;
            }
            try {
                if (box.getTradeAction().equals("收")) {
                    getbox += count;
                } else {
                    givebox += count;
                }
            } catch (Exception e) {
                getbox +=0;
                givebox +=0;
            }

            try {
                if (box.getBoxStatus().equals("实")) {
                    fullbox += count;
                } else {
                    emptybox += count;
                }
            }catch (Exception e){
                fullbox += 0;
                emptybox +=0;
            }

        }

        List<Integer> stringList = new ArrayList<>();
        stringList.add(moneybox);
        stringList.add(cardbox);
        stringList.add(voucherbox);
        stringList.add(voucherbag);
        stringList.add(getbox);
        stringList.add(givebox);
        stringList.add(fullbox);
        stringList.add(emptybox);
        return stringList;
    }

    public List<String>AnsysisBoxListForKeeper(List<Box> boxList){
        int fullmoneybox = 0;
        int emptymoneybox = 0;
        int fullcardbox = 0;
        int emptycardbox = 0;
        int fullvoucherbox = 0;
        int emptyvoucherbox = 0;
        int fullvoucherbag = 0;
        int emptyvoucherbag = 0;

        for (Box box : boxList) {
            if (box.getBoxStatus() == null)continue;
            switch (box.getBoxStatus()){
                case "实":
                   switch (box.getBoxType()){
                       case "款箱":fullmoneybox++;
                           break;
                       case "卡箱":fullcardbox++;
                           break;
                       case "凭证箱":fullvoucherbox++;
                           break;
                       case "凭证袋":fullvoucherbag++;
                           break;
                   }
                    break;
                case "空":
                    switch (box.getBoxType()){
                        case "款箱":emptymoneybox++;
                            break;
                        case "卡箱":emptycardbox++;
                            break;
                        case "凭证箱":emptyvoucherbox++;
                            break;
                        case "凭证袋":emptyvoucherbag++;
                            break;
                    }
                    break;
            }
        }

        List<String> stringList = new ArrayList<>();
        stringList.add(fullmoneybox+"");
        stringList.add(emptymoneybox+"");
        stringList.add(fullcardbox+"");
        stringList.add(emptycardbox+"");
        stringList.add(fullvoucherbox+"");
        stringList.add(emptyvoucherbox+"");
        stringList.add(fullvoucherbag+"");
        stringList.add(emptyvoucherbag+"");
        return stringList;
    }

}
