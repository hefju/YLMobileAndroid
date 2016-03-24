package YLDataService;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import TaskClass.Box;
import TaskClass.GatherPrint;
import YLSystemDate.YLSystem;
import ylescort.ylmobileandroid.R;

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
            if (box.getValutcheck().equals("多")
                    ||box.getValutcheck().equals("核")
                    ||box.getValutcheck().equals(""))continue;
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

    public GatherPrint AnsysisBoxListForPrint(List<Box> boxList) {

        int givemoneyfull = 0;
        int givemoneyempty = 0;
        int givecardfull = 0;
        int givecardempty = 0;
        int givevoucherfull = 0;
        int givevoucherempty = 0;
        int givevoucherbagfull = 0;
        int givevoucherbagempty = 0;

        int zswsmoneyfull = 0;
        int zswsmoneyempty = 0;
        int zswscardfull = 0;
        int zswscardempty = 0;
        int zswsvoucherfull = 0;
        int zswsvoucherempty = 0;
        int zswsvoucherbagfull = 0;
        int zswsvoucherbagempty = 0;

        int sxjmoneyfull = 0;
        int sxjmoneyempty = 0;
        int sxjcardfull = 0;
        int sxjcardempty = 0;
        int sxjvoucherfull = 0;
        int sxjvoucherempty = 0;
        int sxjvoucherbagfull = 0;
        int sxjvoucherbagempty = 0;

        int jkxmoneyfull = 0;
        int jkxmoneyempty = 0;
        int jkxcardfull = 0;
        int jkxcardempty = 0;
        int jkxvoucherfull = 0;
        int jkxvoucherempty = 0;
        int jkxvoucherbagfull = 0;
        int jkxvoucherbagempty = 0;

        int thdbmoneyfull = 0;
        int thdbmoneyempty = 0;
        int thdbcardfull = 0;
        int thdbcardempty = 0;
        int thdbvoucherfull = 0;
        int thdbvoucherempty = 0;
        int thdbvoucherbagfull = 0;
        int thdbvoucherbagempty = 0;

        int khdbmoneyfull = 0;
        int khdbmoneyempty = 0;
        int khdbcardfull = 0;
        int khdbcardempty = 0;
        int khdbvoucherfull = 0;
        int khdbvoucherempty = 0;
        int khdbvoucherbagfull = 0;
        int khdbvoucherbagempty = 0;

        int qysskmoneyfull = 0;
        int qysskmoneyempty = 0;
        int qysskcardfull = 0;
        int qysskcardempty = 0;
        int qysskvoucherfull = 0;
        int qysskvoucherempty = 0;
        int qysskvoucherbagfull = 0;
        int qysskvoucherbagempty = 0;

        int giveTotal= 0;

        int getTotalmoneyfull= 0;
        int getTotalcardfull= 0;
        int getTotalvoucherfull= 0;
        int getTotalvoucherbagfull= 0;
        int getTotalmoneyempty= 0;
        int getTotalcardempty= 0;
        int getTotalvoucherempty= 0;
        int getTotalvoucherbagempty= 0;

        int getTotalmoney= 0;
        int getTotalcard= 0;
        int getTotalvoucher= 0;
        int getTotalvoucherbag= 0;

        for (Box box : boxList) {
            if (box.getTradeAction().equals("送")) {
                switch (box.getBoxType()) {
                    case "款箱":
                        if (box.getBoxStatus().equals("实")) {
                            givemoneyfull++;
                        } else {
                            givemoneyempty++;
                        }
                        break;
                    case "卡箱":
                        if (box.getBoxStatus().equals("实")) {
                            givecardfull++;
                        } else {
                            givecardempty++;
                        }
                        break;
                    case "凭证箱":
                        if (box.getBoxStatus().equals("实")) {
                            givevoucherfull++;
                        } else {
                            givevoucherempty++;
                        }
                        break;
                    case "凭证袋":
                        if (box.getBoxStatus().equals("实")) {
                            givevoucherbagfull++;
                        } else {
                            givevoucherempty++;
                        }
                        break;
                }
            } else {
                switch (box.getBoxTaskType()) {
                    case "早送晚收":
                        switch (box.getBoxType()) {
                            case "款箱":
                                if (box.getBoxStatus().equals("实")) {
                                    zswsmoneyfull++;
                                } else {
                                    zswsmoneyempty++;
                                }
                                break;
                            case "卡箱":
                                if (box.getBoxStatus().equals("实")) {
                                    zswscardfull++;
                                } else {
                                    zswscardempty++;
                                }
                                break;
                            case "凭证箱":
                                if (box.getBoxStatus().equals("实")) {
                                    zswsvoucherfull++;
                                } else {
                                    zswsvoucherempty++;
                                }
                                break;
                            case "凭证袋":
                                if (box.getBoxStatus().equals("实")) {
                                    zswsvoucherbagfull++;
                                } else {
                                    zswsvoucherbagempty++;
                                }
                                break;
                        }
                        break;
                    case "上下介":
                        switch (box.getBoxType()) {
                            case "款箱":
                                if (box.getBoxStatus().equals("实")) {
                                    sxjmoneyfull++;
                                } else {
                                    sxjmoneyempty++;
                                }
                                break;
                            case "卡箱":
                                if (box.getBoxStatus().equals("实")) {
                                    sxjcardfull++;
                                } else {
                                    sxjcardempty++;
                                }
                                break;
                            case "凭证箱":
                                if (box.getBoxStatus().equals("实")) {
                                    sxjvoucherfull++;
                                } else {
                                    sxjvoucherempty++;
                                }
                                break;
                            case "凭证袋":
                                if (box.getBoxStatus().equals("实")) {
                                    sxjvoucherbagfull++;
                                } else {
                                    sxjvoucherbagempty++;
                                }
                                break;
                        }
                        break;
                    case "寄库箱":
                        switch (box.getBoxType()) {
                            case "款箱":
                                if (box.getBoxStatus().equals("实")) {
                                    jkxmoneyfull++;
                                } else {
                                    jkxmoneyempty++;
                                }
                                break;
                            case "卡箱":
                                if (box.getBoxStatus().equals("实")) {
                                    jkxcardfull++;
                                } else {
                                    jkxcardempty++;
                                }
                                break;
                            case "凭证箱":
                                if (box.getBoxStatus().equals("实")) {
                                    jkxvoucherfull++;
                                } else {
                                    jkxvoucherempty++;
                                }
                                break;
                            case "凭证袋":
                                if (box.getBoxStatus().equals("实")) {
                                    jkxvoucherbagfull++;
                                } else {
                                    jkxvoucherbagempty++;
                                }
                                break;
                        }
                        break;
                    case "同行调拨":
                        switch (box.getBoxType()) {
                            case "款箱":
                                if (box.getBoxStatus().equals("实")) {
                                    thdbmoneyfull++;
                                } else {
                                    thdbmoneyempty++;
                                }
                                break;
                            case "卡箱":
                                if (box.getBoxStatus().equals("实")) {
                                    thdbcardfull++;
                                } else {
                                    thdbcardempty++;
                                }
                                break;
                            case "凭证箱":
                                if (box.getBoxStatus().equals("实")) {
                                    thdbvoucherfull++;
                                } else {
                                    thdbvoucherempty++;
                                }
                                break;
                            case "凭证袋":
                                if (box.getBoxStatus().equals("实")) {
                                    thdbvoucherbagfull++;
                                } else {
                                    thdbcardempty++;
                                }
                                break;
                        }
                        break;
                    case "跨行调拨":
                        switch (box.getBoxType()) {
                            case "款箱":
                                if (box.getBoxStatus().equals("实")) {
                                    khdbmoneyfull++;
                                } else {
                                    khdbmoneyempty++;
                                }
                                break;
                            case "卡箱":
                                if (box.getBoxStatus().equals("实")) {
                                    khdbcardfull++;
                                } else {
                                    khdbcardempty++;
                                }
                                break;
                            case "凭证箱":
                                if (box.getBoxStatus().equals("实")) {
                                    khdbvoucherfull++;
                                } else {
                                    khdbvoucherempty++;
                                }
                                break;
                            case "凭证袋":
                                if (box.getBoxStatus().equals("实")) {
                                    khdbvoucherbagfull++;
                                } else {
                                    khdbvoucherbagempty++;
                                }
                                break;
                        }
                        break;
                    case "企业收送款":
                        switch (box.getBoxType()) {
                            case "款箱":
                                if (box.getBoxStatus().equals("实")) {
                                    qysskmoneyfull++;
                                } else {
                                    qysskmoneyempty++;
                                }
                                break;
                            case "卡箱":
                                if (box.getBoxStatus().equals("实")) {
                                    qysskcardfull++;
                                } else {
                                    qysskcardempty++;
                                }
                                break;
                            case "凭证箱":
                                if (box.getBoxStatus().equals("实")) {
                                    qysskvoucherfull++;
                                } else {
                                    qysskvoucherempty++;
                                }
                                break;
                            case "凭证袋":
                                if (box.getBoxStatus().equals("实")) {
                                    qysskvoucherbagfull++;
                                } else {
                                    qysskvoucherbagempty++;
                                }
                                break;
                        }
                        break;
                }
            }

        }
        //送总箱数
        giveTotal = givemoneyfull+givemoneyempty+givecardempty+givecardfull+givevoucherempty+
                givevoucherfull+givevoucherbagfull+givevoucherbagempty;
        //收实款箱数
        getTotalmoneyfull = zswsmoneyfull+sxjmoneyfull+jkxmoneyfull+thdbmoneyfull+khdbmoneyfull+
                qysskmoneyfull;
        //收空款箱数
        getTotalmoneyempty = zswsmoneyempty+sxjmoneyempty+jkxmoneyempty+thdbmoneyempty+
                khdbmoneyempty+ qysskmoneyempty;
        //收实卡箱
        getTotalcardfull =  zswscardfull+sxjcardfull+jkxcardfull+thdbcardfull+
                khdbcardfull+ qysskcardfull;
        //收空卡箱
        getTotalcardempty = zswscardempty+sxjcardempty+jkxcardempty+thdbcardempty+
                khdbcardempty+ qysskcardempty;
        //收实凭证箱
        getTotalvoucherfull = zswsvoucherfull+sxjvoucherfull+jkxvoucherfull+thdbvoucherfull+khdbvoucherfull+
                qysskvoucherfull;
        //收空凭证箱
        getTotalvoucherempty = zswsvoucherempty+sxjvoucherempty+jkxvoucherempty+thdbvoucherempty+khdbvoucherempty+
                qysskvoucherempty;
        //收实凭证袋
        getTotalvoucherbagfull = zswsvoucherbagfull+sxjvoucherbagfull+jkxvoucherbagfull+thdbvoucherbagfull+khdbvoucherbagfull+
                qysskvoucherbagfull;
        //收空凭证袋
        getTotalvoucherbagempty = zswsvoucherbagempty+sxjvoucherbagempty+jkxvoucherbagempty+thdbvoucherbagempty+
                khdbvoucherbagempty+ qysskvoucherbagempty;
        //收总款箱
        getTotalmoney = getTotalmoneyfull+getTotalmoneyempty;
        //收总卡箱
        getTotalcard = getTotalcardfull+getTotalcardempty;
        //收总凭证箱
        getTotalvoucher = getTotalvoucherfull+getTotalvoucherempty;
        //收总凭证袋
        getTotalvoucherbag = getTotalvoucherbagfull+getTotalvoucherbagempty;


        GatherPrint gatherPrint;
        gatherPrint = new GatherPrint(

                IntToStr(givemoneyfull),
                IntToStr(givemoneyempty),
                IntToStr(givecardfull),
                IntToStr(givecardempty),
                IntToStr(givevoucherfull),
                IntToStr(givevoucherempty),
                IntToStr(givevoucherbagfull),
                IntToStr(givevoucherbagempty),

                IntToStr(zswsmoneyfull),
                IntToStr(zswsmoneyempty),
                IntToStr(zswscardfull),
                IntToStr(zswscardempty),
                IntToStr(zswsvoucherfull),
                IntToStr(zswsvoucherempty),
                IntToStr(zswsvoucherbagfull),
                IntToStr(zswsvoucherbagempty),

                IntToStr(sxjmoneyfull),
                IntToStr(sxjmoneyempty),
                IntToStr(sxjcardfull),
                IntToStr(sxjcardempty),
                IntToStr(sxjvoucherfull),
                IntToStr(sxjvoucherempty),
                IntToStr(sxjvoucherbagfull),
                IntToStr(sxjvoucherbagempty),

                IntToStr(jkxmoneyfull),
                IntToStr(jkxmoneyempty),
                IntToStr(jkxcardfull),
                IntToStr(jkxcardempty),
                IntToStr(jkxvoucherfull),
                IntToStr(jkxvoucherempty),
                IntToStr(jkxvoucherbagfull),
                IntToStr(jkxvoucherbagempty),

                IntToStr(thdbmoneyfull),
                IntToStr(thdbmoneyempty),
                IntToStr(thdbcardfull),
                IntToStr(thdbcardempty),
                IntToStr(thdbvoucherfull),
                IntToStr(thdbvoucherempty),
                IntToStr(thdbvoucherbagfull),
                IntToStr(thdbvoucherbagempty),

                IntToStr(khdbmoneyfull),
                IntToStr(khdbmoneyempty),
                IntToStr(khdbcardfull),
                IntToStr(khdbcardempty),
                IntToStr(khdbvoucherfull),
                IntToStr(khdbvoucherempty),
                IntToStr(khdbvoucherbagfull),
                IntToStr(khdbvoucherbagempty),

                IntToStr(qysskmoneyfull),
                IntToStr(qysskmoneyempty),
                IntToStr(qysskcardfull),
                IntToStr(qysskcardempty),
                IntToStr(qysskvoucherfull),
                IntToStr(qysskvoucherempty),
                IntToStr(qysskvoucherbagfull),
                IntToStr(qysskvoucherbagempty),

                giveTotal+"",

                IntToStr(getTotalmoneyfull),
                IntToStr(getTotalcardfull),
                IntToStr(getTotalvoucherfull),
                IntToStr(getTotalvoucherbagfull),
                IntToStr(getTotalmoneyempty),
                IntToStr(getTotalcardempty),
                IntToStr(getTotalvoucherempty),
                IntToStr(getTotalvoucherbagempty),

                getTotalmoney+"",
                getTotalcard+"",
                getTotalvoucher+"",
                getTotalvoucherbag+"");

        return gatherPrint;
    }

    private String IntToStr(int value){
        if (value == 0){
            return "";
        }
        return  value+"";
    }

}
