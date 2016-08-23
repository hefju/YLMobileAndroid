package YLPrinter;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.util.Log;

import java.util.List;
import java.util.Set;

import TaskClass.Box;
import TaskClass.GatherPrint;
import YLSystemDate.YLSystem;
import zpSDK.zpSDK.zpSDK;

/**
 * Created by Administrator on 2016-03-07.
 */
public class YLPrint {
    public BluetoothAdapter bluetoothAdapter;
    public BluetoothDevice bluetoothDevice;
    private Boolean LoadPrinter = false ;
    private double rowshight;
    private double rowsplus;
    private double columnswide;

    public boolean InitBluetooth (){
        if (!LoadPrinter) {
            boolean OpenPrinter = false;
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            Set<BluetoothDevice> bluetoothDeviceSet = bluetoothAdapter.getBondedDevices();
            for (BluetoothDevice device : bluetoothDeviceSet) {
                Log.e(YLSystem.getKimTag(), "discover:" + device.getName());
                bluetoothDevice = device;
                OpenPrinter = true;
            }

            if (OpenPrinter) {
                LoadPrinter = zpSDK.zp_open(bluetoothAdapter, bluetoothDevice);
                Log.e(YLSystem.getKimTag(), "OpenPrinter" +LoadPrinter);
                return  LoadPrinter;
            }
        }
        return LoadPrinter;
    }

    public boolean OperBlueDevice(){
        return zpSDK.zp_open(bluetoothAdapter, bluetoothDevice);
    }

    private double NextRow(){
        rowshight= rowshight+2;
        return  rowsplus * rowshight;
    }

    private double NextRow2(){
        rowshight= rowshight+1;
        return  rowsplus * rowshight;
    }

    private  double linerow(){
        return  rowsplus * rowshight;
    }

    public boolean PrintGather(GatherPrint gatherPrint,int type) throws  Exception{
        rowshight = 4;rowsplus = 2;columnswide = 20;

//        if (!LoadPrinter) return false ;

        Boolean creatpage = zpSDK.zp_page_create(80,130);

        Log.e(YLSystem.getKimTag(), "creat" + creatpage.toString());

        zpSDK.zp_draw_text(10, rowshight, "佛山市粤龙保安押运有限公司");

        String title = "";
        String Address = "";
        String NetPoint = "";
        String handovertime = "";
        String carorline = "";
        switch (type){
            case 1:
                title = "押运交接单";
                Address = "单位名称:" + gatherPrint.getClintName();
                NetPoint =  "网点名称:" + gatherPrint.getSiteName();
                handovertime = "打印时间:"+gatherPrint.getTradeTime();
                carorline = "押运车牌："+gatherPrint.getCarNumber();
                break;
            case 2:
                title = "金库入库汇总";
                Address = gatherPrint.getClintName();
                NetPoint =gatherPrint.getSiteName();
                handovertime = gatherPrint.getTradeTime();
                carorline = gatherPrint.getCarNumber();
                break;
        }

        zpSDK.zp_draw_text(25, NextRow2(), title);

        zpSDK.zp_draw_text(40, NextRow(), gatherPrint.getTaskNumber());

        zpSDK.zp_draw_text_ex(3, NextRow(),
                Address, "宋体", 3, 0, true, false, false);
        zpSDK.zp_draw_text(3, NextRow(), NetPoint);

        zpSDK.zp_draw_text(3, NextRow(), handovertime);

        zpSDK.zp_draw_text(3, NextRow(), carorline);

        //送箱表格坐标
        double formx1 = 0;
        double formx2 = columnswide;
        double formx3 = NextColumns();
        double formx4 = NextColumns();
        double formx5 = NextColumns();
        double formx6 = NextColumns();
        double formx7 = NextColumns();
        double formx8 = NextColumns();
        double formx9 = NextColumns();
        double formx10 = NextColumns();

        double formy1=NextRow2();
        double formy2=NextRow();
        double formy3=NextRow();
        double formy4=NextRow();
        double formy5=NextRow();

        //送箱行线描绘
        zpSDK.zp_draw_line(formx1, formy1, formx10, formy1, 2);
        zpSDK.zp_draw_line(formx2, formy2, formx10, formy2, 2);
        zpSDK.zp_draw_line(formx1, formy3, formx10, formy3, 2);
        zpSDK.zp_draw_line(formx1, formy4, formx10, formy4, 2);
        zpSDK.zp_draw_line(formx1, formy5, formx10, formy5, 2);
        //送箱列线描绘
        zpSDK.zp_draw_line(formx1, formy1, formx1, formy5, 2);
        zpSDK.zp_draw_line(formx2, formy1, formx2, formy5, 3);
        zpSDK.zp_draw_line(formx3, formy2, formx3, formy4, 2);
        zpSDK.zp_draw_line(formx4, formy1, formx4, formy4, 3);
        zpSDK.zp_draw_line(formx5, formy2, formx5, formy4, 2);
        zpSDK.zp_draw_line(formx6, formy1, formx6, formy4, 3);
        zpSDK.zp_draw_line(formx7, formy2, formx7, formy4, 2);
        zpSDK.zp_draw_line(formx8, formy1, formx8, formy4, 3);
        zpSDK.zp_draw_line(formx9, formy2, formx9, formy4, 2);
        zpSDK.zp_draw_line(formx10, formy1, formx10, formy5,3);

        //款箱箱文字坐标
        double[] TextBoxtypeX = {23.5,36.5,48,60.5};
        double[] TextBoxtypeY = {35,58};

        double[] TextBoxstautX = {21,27.5,34,40.5,47,53.5,60,67};
        double[] TextBoxstautY = {39,62};

        double[] TextTitleX = {8,2};
        double[] TextTitleY = {38,43.5,47,49,52,52,60,66,70,74,78,82,86,90,94};

        zpSDK.zp_draw_text(TextTitleX[0], TextTitleY[0], "送箱");
        zpSDK.zp_draw_text(TextTitleX[0], TextTitleY[1], "小计");
        zpSDK.zp_draw_text(TextTitleX[1]+2, TextTitleY[2], "总数合计");

        zpSDK.zp_draw_text(TextBoxtypeX[0], TextBoxtypeY[0], "款箱");
        zpSDK.zp_draw_text(TextBoxstautX[0], TextBoxstautY[0], "实");
        zpSDK.zp_draw_text(TextBoxstautX[1], TextBoxstautY[0], "空");

        zpSDK.zp_draw_text(TextBoxtypeX[1], TextBoxtypeY[0], "卡箱");
        zpSDK.zp_draw_text(TextBoxstautX[2], TextBoxstautY[0], "实");
        zpSDK.zp_draw_text(TextBoxstautX[3], TextBoxstautY[0], "空");

        zpSDK.zp_draw_text(TextBoxtypeX[2], TextBoxtypeY[0], "凭证箱");
        zpSDK.zp_draw_text(TextBoxstautX[4], TextBoxstautY[0], "实");
        zpSDK.zp_draw_text(TextBoxstautX[5], TextBoxstautY[0], "空");

        zpSDK.zp_draw_text(TextBoxtypeX[3], TextBoxtypeY[0], "凭证袋");
        zpSDK.zp_draw_text(TextBoxstautX[6], TextBoxstautY[0], "实");
        zpSDK.zp_draw_text(TextBoxstautX[7], TextBoxstautY[0], "空");

        //收箱表格
        rowshight = 55;
        double formy6 = 55;
        double formy7 = formy6+4;
        double formy8 = formy7+4;
        double formy9 = formy8+4;
        double formy10 = formy9+4;
        double formy11 = formy10+4;
        double formy12 = formy11+4;
        double formy13 = formy12+4;
        double formy14 = formy13+4;
        double formy15 = formy14+4;
        double formy16 = formy15+4;


        zpSDK.zp_draw_line(formx1, formy6, formx1, formy16, 2);
        zpSDK.zp_draw_line(formx2, formy6, formx2, formy16, 3);
        zpSDK.zp_draw_line(formx3, formy7, formx3, formy15, 2);
        zpSDK.zp_draw_line(formx4, formy6, formx4, formy16, 3);
        zpSDK.zp_draw_line(formx5, formy7, formx5, formy15, 2);
        zpSDK.zp_draw_line(formx6, formy6, formx6, formy16, 3);
        zpSDK.zp_draw_line(formx7, formy7, formx7, formy15, 2);
        zpSDK.zp_draw_line(formx8, formy6, formx8, formy16, 3);
        zpSDK.zp_draw_line(formx9, formy7, formx9, formy15, 2);
        zpSDK.zp_draw_line(formx10, formy6, formx10, formy16,3);

        zpSDK.zp_draw_line(formx1, formy6, formx10, formy6, 2);
        zpSDK.zp_draw_line(formx2, formy7, formx10, formy7, 2);
        zpSDK.zp_draw_line(formx1, formy8, formx10, formy8, 2);
        zpSDK.zp_draw_line(formx1, formy9, formx10, formy9, 2);
        zpSDK.zp_draw_line(formx1, formy10, formx10, formy10, 2);
        zpSDK.zp_draw_line(formx1, formy11, formx10, formy11, 2);
        zpSDK.zp_draw_line(formx1, formy12, formx10, formy12, 2);
        zpSDK.zp_draw_line(formx1, formy13, formx10, formy13, 2);
        zpSDK.zp_draw_line(formx1, formy14, formx10, formy14, 4);
        zpSDK.zp_draw_line(formx1, formy15, formx10, formy15, 2);
        zpSDK.zp_draw_line(formx1, formy16, formx10, formy16, 2);


        zpSDK.zp_draw_text(TextTitleX[1],TextTitleY[5],  "收箱明细：");
        zpSDK.zp_draw_text(TextTitleX[1],TextTitleY[6],  "交接类型");
        zpSDK.zp_draw_text(TextTitleX[1],TextTitleY[7],  "早送晚收");
        zpSDK.zp_draw_text(TextTitleX[1],TextTitleY[8],  "上下介");
        zpSDK.zp_draw_text(TextTitleX[1],TextTitleY[9],  "寄库箱");
        zpSDK.zp_draw_text(TextTitleX[1],TextTitleY[10],  "同行调拨");
        zpSDK.zp_draw_text(TextTitleX[1],TextTitleY[11],  "跨行调拨");
        zpSDK.zp_draw_text(TextTitleX[1],TextTitleY[12],  "企业收送款");
        zpSDK.zp_draw_text(TextTitleX[0],TextTitleY[13],  "小计");
        zpSDK.zp_draw_text(TextTitleX[0],TextTitleY[14],  "合计");


        zpSDK.zp_draw_text(TextBoxtypeX[0], TextBoxtypeY[1], "款箱");
        zpSDK.zp_draw_text(TextBoxstautX[0], TextBoxstautY[1], "实");
        zpSDK.zp_draw_text(TextBoxstautX[1], TextBoxstautY[1], "空");

        zpSDK.zp_draw_text(TextBoxtypeX[1], TextBoxtypeY[1], "卡箱");
        zpSDK.zp_draw_text(TextBoxstautX[2], TextBoxstautY[1], "实");
        zpSDK.zp_draw_text(TextBoxstautX[3], TextBoxstautY[1], "空");

        zpSDK.zp_draw_text(TextBoxtypeX[2], TextBoxtypeY[1], "凭证箱");
        zpSDK.zp_draw_text(TextBoxstautX[4], TextBoxstautY[1], "实");
        zpSDK.zp_draw_text(TextBoxstautX[5], TextBoxstautY[1], "空");

        zpSDK.zp_draw_text(TextBoxtypeX[3], TextBoxtypeY[1], "凭证袋");
        zpSDK.zp_draw_text(TextBoxstautX[6], TextBoxstautY[1], "实");
        zpSDK.zp_draw_text(TextBoxstautX[7], TextBoxstautY[1], "空");


        //送箱数据
        zpSDK.zp_draw_text(TextBoxstautX[0], TextTitleY[1], gatherPrint.getGivemoneyfull());
        zpSDK.zp_draw_text(TextBoxstautX[1], TextTitleY[1], gatherPrint.getGivemoneyempty());
        zpSDK.zp_draw_text(TextBoxstautX[2], TextTitleY[1], gatherPrint.getGivecardfull());
        zpSDK.zp_draw_text(TextBoxstautX[3], TextTitleY[1], gatherPrint.getGivecardempty());
        zpSDK.zp_draw_text(TextBoxstautX[4], TextTitleY[1], gatherPrint.getGivevoucherfull());
        zpSDK.zp_draw_text(TextBoxstautX[5], TextTitleY[1],gatherPrint.getGivevoucherempty());
        zpSDK.zp_draw_text(TextBoxstautX[6], TextTitleY[1],gatherPrint.getGivevoucherbagfull());
        zpSDK.zp_draw_text(TextBoxstautX[7], TextTitleY[1],gatherPrint.getGivevoucherbagempty());
        zpSDK.zp_draw_text(TextBoxstautX[3]+2, TextTitleY[2],gatherPrint.getGiveTotal());

        //收箱数据
        //早送晚收
        zpSDK.zp_draw_text(TextBoxstautX[0],TextTitleY[7], gatherPrint.getZswsmoneyfull());
        zpSDK.zp_draw_text(TextBoxstautX[1],TextTitleY[7], gatherPrint.getZswsmoneyempty());
        zpSDK.zp_draw_text(TextBoxstautX[2],TextTitleY[7], gatherPrint.getZswscardfull());
        zpSDK.zp_draw_text(TextBoxstautX[3],TextTitleY[7], gatherPrint.getZswscardempty());
        zpSDK.zp_draw_text(TextBoxstautX[4],TextTitleY[7], gatherPrint.getZswsvoucherfull());
        zpSDK.zp_draw_text(TextBoxstautX[5],TextTitleY[7],gatherPrint.getZswsvoucherempty());
        zpSDK.zp_draw_text(TextBoxstautX[6],TextTitleY[7],gatherPrint.getZswsvoucherbagfull());
        zpSDK.zp_draw_text(TextBoxstautX[7],TextTitleY[7],gatherPrint.getZswsvoucherbagempty());

        //上下介
        zpSDK.zp_draw_text(TextBoxstautX[0], TextTitleY[8], gatherPrint.getSxjmoneyfull());
        zpSDK.zp_draw_text(TextBoxstautX[1], TextTitleY[8], gatherPrint.getSxjmoneyempty());
        zpSDK.zp_draw_text(TextBoxstautX[2], TextTitleY[8], gatherPrint.getSxjcardfull());
        zpSDK.zp_draw_text(TextBoxstautX[3], TextTitleY[8], gatherPrint.getSxjcardempty());
        zpSDK.zp_draw_text(TextBoxstautX[4], TextTitleY[8], gatherPrint.getSxjvoucherfull());
        zpSDK.zp_draw_text(TextBoxstautX[5], TextTitleY[8],gatherPrint.getSxjvoucherempty());
        zpSDK.zp_draw_text(TextBoxstautX[6], TextTitleY[8],gatherPrint.getSxjvoucherbagfull());
        zpSDK.zp_draw_text(TextBoxstautX[7], TextTitleY[8],gatherPrint.getSxjvoucherbagempty());

        //寄库箱
        zpSDK.zp_draw_text(TextBoxstautX[0], TextTitleY[9], gatherPrint.getJkxmoneyfull());
        zpSDK.zp_draw_text(TextBoxstautX[1], TextTitleY[9], gatherPrint.getJkxmoneyempty());
        zpSDK.zp_draw_text(TextBoxstautX[2], TextTitleY[9], gatherPrint.getJkxcardfull());
        zpSDK.zp_draw_text(TextBoxstautX[3], TextTitleY[9], gatherPrint.getJkxcardempty());
        zpSDK.zp_draw_text(TextBoxstautX[4], TextTitleY[9], gatherPrint.getJkxvoucherfull());
        zpSDK.zp_draw_text(TextBoxstautX[5], TextTitleY[9],gatherPrint.getJkxvoucherempty());
        zpSDK.zp_draw_text(TextBoxstautX[6], TextTitleY[9],gatherPrint.getJkxvoucherbagfull());
        zpSDK.zp_draw_text(TextBoxstautX[7], TextTitleY[9],gatherPrint.getJkxvoucherbagempty());
        //同行调拨
        zpSDK.zp_draw_text(TextBoxstautX[0], TextTitleY[10], gatherPrint.getThdbmoneyfull());
        zpSDK.zp_draw_text(TextBoxstautX[1], TextTitleY[10], gatherPrint.getThdbmoneyempty());
        zpSDK.zp_draw_text(TextBoxstautX[2], TextTitleY[10], gatherPrint.getThdbcardfull());
        zpSDK.zp_draw_text(TextBoxstautX[3], TextTitleY[10], gatherPrint.getThdbcardempty());
        zpSDK.zp_draw_text(TextBoxstautX[4], TextTitleY[10], gatherPrint.getThdbvoucherfull());
        zpSDK.zp_draw_text(TextBoxstautX[5], TextTitleY[10],gatherPrint.getThdbvoucherempty());
        zpSDK.zp_draw_text(TextBoxstautX[6], TextTitleY[10],gatherPrint.getThdbvoucherbagfull());
        zpSDK.zp_draw_text(TextBoxstautX[7], TextTitleY[10],gatherPrint.getThdbvoucherbagempty());
        //跨行调拨
        zpSDK.zp_draw_text(TextBoxstautX[0], TextTitleY[11], gatherPrint.getKhdbmoneyfull());
        zpSDK.zp_draw_text(TextBoxstautX[1], TextTitleY[11], gatherPrint.getKhdbmoneyempty());
        zpSDK.zp_draw_text(TextBoxstautX[2], TextTitleY[11], gatherPrint.getKhdbcardfull());
        zpSDK.zp_draw_text(TextBoxstautX[3], TextTitleY[11], gatherPrint.getKhdbcardempty());
        zpSDK.zp_draw_text(TextBoxstautX[4], TextTitleY[11], gatherPrint.getKhdbvoucherfull());
        zpSDK.zp_draw_text(TextBoxstautX[5], TextTitleY[11],gatherPrint.getKhdbvoucherempty());
        zpSDK.zp_draw_text(TextBoxstautX[6], TextTitleY[11],gatherPrint.getKhdbvoucherbagfull());
        zpSDK.zp_draw_text(TextBoxstautX[7], TextTitleY[11],gatherPrint.getKhdbvoucherbagempty());
        //企业收送款
        zpSDK.zp_draw_text(TextBoxstautX[0], TextTitleY[12], gatherPrint.getQysskmoneyfull());
        zpSDK.zp_draw_text(TextBoxstautX[1], TextTitleY[12], gatherPrint.getQysskmoneyempty());
        zpSDK.zp_draw_text(TextBoxstautX[2], TextTitleY[12], gatherPrint.getQysskcardfull());
        zpSDK.zp_draw_text(TextBoxstautX[3], TextTitleY[12], gatherPrint.getQysskcardempty());
        zpSDK.zp_draw_text(TextBoxstautX[4], TextTitleY[12], gatherPrint.getQysskvoucherfull());
        zpSDK.zp_draw_text(TextBoxstautX[5], TextTitleY[12],gatherPrint.getQysskvoucherempty());
        zpSDK.zp_draw_text(TextBoxstautX[6], TextTitleY[12],gatherPrint.getQysskvoucherbagfull());
        zpSDK.zp_draw_text(TextBoxstautX[7], TextTitleY[12],gatherPrint.getQysskvoucherbagempty());
        //小计
        zpSDK.zp_draw_text(TextBoxstautX[0], TextTitleY[13], gatherPrint.getGetTotalmoneyfull());
        zpSDK.zp_draw_text(TextBoxstautX[1], TextTitleY[13], gatherPrint.getGetTotalmoneyempty());
        zpSDK.zp_draw_text(TextBoxstautX[2], TextTitleY[13], gatherPrint.getGetTotalcardfull());
        zpSDK.zp_draw_text(TextBoxstautX[3], TextTitleY[13], gatherPrint.getGetTotalcardempty());
        zpSDK.zp_draw_text(TextBoxstautX[4], TextTitleY[13], gatherPrint.getGetTotalvoucherfull());
        zpSDK.zp_draw_text(TextBoxstautX[5], TextTitleY[13],gatherPrint.getGetTotalvoucherempty());
        zpSDK.zp_draw_text(TextBoxstautX[6], TextTitleY[13],gatherPrint.getGetTotalvoucherbagfull());
        zpSDK.zp_draw_text(TextBoxstautX[7], TextTitleY[13],gatherPrint.getGetTotalvoucherbagempty());
        //合计
        zpSDK.zp_draw_text(TextBoxstautX[0]+4, TextTitleY[14], gatherPrint.getGetTotalmoney());
        zpSDK.zp_draw_text(TextBoxstautX[2]+4, TextTitleY[14],gatherPrint.getGetTotalcard());
        zpSDK.zp_draw_text(TextBoxstautX[4]+4, TextTitleY[14],gatherPrint.getGetTotalvoucher());
        zpSDK.zp_draw_text(TextBoxstautX[6]+4, TextTitleY[14],gatherPrint.getGetTotalvoucherbag());

        zpSDK.zp_draw_text(3, 100, "押运业务员："+gatherPrint.getHomName() +"  签名：");
        zpSDK.zp_draw_text(3, 110, "网点交接人（签章）1：                   2：");
        Boolean printpage =  zpSDK.zp_page_print(false);
        Log.e("kim", "print" + printpage.toString());

        zpSDK.zp_page_free();

        return printpage;
    }

    private double NextColumns() {
        columnswide = columnswide+6.5;
        return columnswide;
    }

    public boolean PrintDetail(List<Box> boxList,int type, GatherPrint gatherPrint) throws  Exception{

        rowshight = 4;rowsplus = 2;

        int count = 1;
        for (Box box : boxList) {
            if (box.getNextOutTime() != null ){
                if (box.getNextOutTime().length() > 0){
                    count = count +2;
                }else if (box.getBoxName().length() >10){
                    count = count +2;
                }else {
                    count++;
                }
            }else {
                count++;
            }
        }

        double pagehigh = count*4+80;
        if (pagehigh< 130)pagehigh = 130;
        Log.e(YLSystem.getKimTag(), "页面高度" + pagehigh);
        Boolean creatpage = zpSDK.zp_page_create(80,pagehigh);

        Log.e(YLSystem.getKimTag(), "creat" + creatpage.toString());

        zpSDK.zp_draw_text(10, rowshight, "佛山市粤龙保安押运有限公司");
        String title = "";
        String Address = "";
        String NetPoint = "";
        String handovertime = "";
        String carorline = "";
        switch (type){
            case 1:
                title = "押运交接单";
                Address = "单位名称:" + gatherPrint.getClintName();
                NetPoint =  "网点名称:" + gatherPrint.getSiteName();
                handovertime = "打印时间:"+gatherPrint.getTradeTime();
                carorline = "押运车牌："+gatherPrint.getCarNumber();
                break;
            case 2:
                title = "金库入库汇总";
                Address = gatherPrint.getClintName();
                NetPoint =gatherPrint.getSiteName();
                handovertime = gatherPrint.getTradeTime();
                carorline = gatherPrint.getCarNumber();
                break;
        }

        zpSDK.zp_draw_text(25, NextRow2(), title);

        zpSDK.zp_draw_text(40, NextRow(), gatherPrint.getTaskNumber());

        zpSDK.zp_draw_text_ex(3, NextRow(),
                Address, "宋体", 3, 0, true, false, false);
        zpSDK.zp_draw_text(3, NextRow(), NetPoint);

        zpSDK.zp_draw_text(3, NextRow(), handovertime);

        zpSDK.zp_draw_text(3, NextRow(), carorline);


//        zpSDK.zp_draw_text(25, NextRow2(),title );
//
//        zpSDK.zp_draw_text_ex(50, NextRow2(), "NO."+Number, "宋体", 3, 0, true, false, false);

        zpSDK.zp_draw_text(2, NextRow(), "收送箱类型清单：");

        rowshight = 40;

        zpSDK.zp_draw_text(1, rowshight, "序号");
        zpSDK.zp_draw_text(10, rowshight, "箱号");
        zpSDK.zp_draw_text(23, rowshight, "收/送");
        zpSDK.zp_draw_text(33, rowshight, "空/实");
        zpSDK.zp_draw_text(41, rowshight, "款箱类型");
        zpSDK.zp_draw_text(53, rowshight, "交接类型");
        zpSDK.zp_draw_text(66, rowshight, "备注");


        int order = 1;
        for (Box box : boxList) {
            rowshight=rowshight+4;
            if (box.getNextOutTime() != null){
                if (box.getNextOutTime().length() > 0 ){
//                    Log.e(YLSystem.getKimTag(),"出库状态为有出库日期"+box.getBoxName());
                    zpSDK.zp_draw_text(2, rowshight, order + "");
                    zpSDK.zp_draw_text(6, rowshight, box.getBoxName());
                    zpSDK.zp_draw_text(45, rowshight,"出库："+box.getNextOutTime());
                    rowshight=rowshight+4;
                    zpSDK.zp_draw_text(35, rowshight, box.getTradeAction());
                    zpSDK.zp_draw_text(40, rowshight, box.getBoxStatus());
                    zpSDK.zp_draw_text(45, rowshight, box.getBoxType());
                    zpSDK.zp_draw_text(55, rowshight, box.getBoxTaskType());
                }else if (box.getBoxName().length() >10) {
//                    Log.e(YLSystem.getKimTag(),"款箱名称过长"+box.getBoxName());
                    zpSDK.zp_draw_text(2, rowshight, order + "");
                    zpSDK.zp_draw_text(6, rowshight, box.getBoxName());
                    rowshight=rowshight+4;
                    zpSDK.zp_draw_text(35, rowshight, box.getTradeAction());
                    zpSDK.zp_draw_text(40, rowshight, box.getBoxStatus());
                    zpSDK.zp_draw_text(45, rowshight, box.getBoxType());
                    zpSDK.zp_draw_text(55, rowshight, box.getBoxTaskType());
                    zpSDK.zp_draw_text(55, rowshight, "");
                }else {
//                    Log.e(YLSystem.getKimTag(),"出库状态不为null"+box.getBoxName());
                    zpSDK.zp_draw_text(2, rowshight, order + "");
                    zpSDK.zp_draw_text(6, rowshight, box.getBoxName());
                    zpSDK.zp_draw_text(35, rowshight, box.getTradeAction());
                    zpSDK.zp_draw_text(40, rowshight, box.getBoxStatus());
                    zpSDK.zp_draw_text(45, rowshight, box.getBoxType());
                    zpSDK.zp_draw_text(55, rowshight, box.getBoxTaskType());
                    zpSDK.zp_draw_text(55, rowshight, "");
                }

            }else {
//                Log.e(YLSystem.getKimTag(),"出库状态为null"+box.getBoxName());
                zpSDK.zp_draw_text(2, rowshight, order + "");
                zpSDK.zp_draw_text(6, rowshight, box.getBoxName());
                zpSDK.zp_draw_text(35, rowshight, box.getTradeAction());
                zpSDK.zp_draw_text(40, rowshight, box.getBoxStatus());
                zpSDK.zp_draw_text(45, rowshight, box.getBoxType());
                zpSDK.zp_draw_text(55, rowshight, box.getBoxTaskType());
                zpSDK.zp_draw_text(55, rowshight, "");
            }
            order++;
        }

        zpSDK.zp_draw_text(3, rowshight16(), "押运业务员:"+gatherPrint.getHomName() +"  签名：");
        zpSDK.zp_draw_text(3, rowshight8(), "网点交接人（签章）1：                   2：");


        if (rowshight > 130){
            rowshight16();
        }else {
            rowshight = 120;
        }

        zpSDK.zp_draw_text(20, rowshight, "第一页，共一页");

        Boolean printpage =  zpSDK.zp_page_print(false);
        Log.e("kim", "print" +rowshight+ printpage.toString());

        zpSDK.zp_page_free();

        return printpage;
    }

    private double rowshight4(){
        rowshight=rowshight+4;
        return rowshight;
    }

    private double rowshight16(){
        rowshight=rowshight+16;
        return rowshight;
    }

    private double rowshight8(){
        rowshight=rowshight+8;
        return rowshight;
    }

}
