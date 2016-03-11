package YLPrinter;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.util.Log;
import java.util.Set;
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

    public boolean InitBluetooth (){
        if (!LoadPrinter) {
            boolean OpenPrinter = false;
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            Set<BluetoothDevice> bluetoothDeviceSet = bluetoothAdapter.getBondedDevices();
            for (BluetoothDevice device : bluetoothDeviceSet) {
                bluetoothDevice = device;
                OpenPrinter = true;
            }

            if (OpenPrinter) {
                LoadPrinter = zpSDK.zp_open(bluetoothAdapter, bluetoothDevice);
                return  LoadPrinter;
            }
        }
        return LoadPrinter;
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

    public boolean PrintForm(GatherPrint gatherPrint){

        if (!InitBluetooth()) return false ;

        Boolean creatpage = zpSDK.zp_page_create(80,110);

        Log.e(YLSystem.getKimTag(), "creat" + creatpage.toString());

        zpSDK.zp_draw_text(10, NextRow(), "佛山市粤龙保安押运有限公司");

        zpSDK.zp_draw_text(25, NextRow(), "押运交接单");

        zpSDK.zp_draw_text_ex(3, NextRow(),
                "单位名称:"+gatherPrint.getClintName(), "宋体", 3, 0, true, false, false);
        zpSDK.zp_draw_text(3, NextRow(), "网点名称:"+gatherPrint.getSiteName());

        zpSDK.zp_draw_text(3, NextRow(), "交接时间:"+gatherPrint.getTradeTime());

        zpSDK.zp_draw_text(3, NextRow(), "押运车牌："+gatherPrint.getCarNumber());

        double Tx1=0;
        double Tx2=20;
        double Tx3=26.5;
        double Tx4=33;
        double Tx5=39.5;
        double Tx6=46;
        double Tx7=52.5;
        double Tx8=59;
        double Tx9=65.5;
        double Tx10=72;

        double Ty1=NextRow();
        double Ty2=NextRow();
        double Ty3=NextRow();
        double Ty4=NextRow();

        zpSDK.zp_draw_line(Tx1, Ty1, Tx10, Ty1, 2);
        zpSDK.zp_draw_line(Tx2, Ty2, Tx10, Ty2, 2);
        zpSDK.zp_draw_line(Tx1, Ty3, Tx10, Ty3, 2);
        zpSDK.zp_draw_line(Tx1, Ty4, Tx10, Ty4, 2);

        zpSDK.zp_draw_line(Tx1, Ty1, Tx1, Ty4, 2);
        zpSDK.zp_draw_line(Tx2, Ty1, Tx2, Ty4, 2);
        zpSDK.zp_draw_line(Tx3, Ty2, Tx3, Ty4, 2);
        zpSDK.zp_draw_line(Tx4, Ty1, Tx4, Ty4, 2);
        zpSDK.zp_draw_line(Tx5, Ty2, Tx5, Ty4, 2);
        zpSDK.zp_draw_line(Tx6, Ty1, Tx6, Ty4, 2);
        zpSDK.zp_draw_line(Tx7, Ty2, Tx7, Ty4, 2);
        zpSDK.zp_draw_line(Tx8, Ty1, Tx8, Ty4, 2);
        zpSDK.zp_draw_line(Tx9, Ty2, Tx9, Ty4, 2);
        zpSDK.zp_draw_line(Tx10, Ty1, Tx10, Ty4, 2);


        rowshight = 22;

        double Ty5=NextRow();
        double Ty6=NextRow();
        double Ty7=NextRow();
        double Ty8=NextRow();
        double Ty9=NextRow();
        double Ty10=NextRow();
        double Ty11=NextRow();
        double Ty12=NextRow();
        double Ty13=NextRow();
        double Ty14=NextRow();
        double Ty15=NextRow();


        zpSDK.zp_draw_line(Tx1, Ty5, Tx10, Ty5, 2);
        zpSDK.zp_draw_line(Tx2, Ty6, Tx10, Ty6, 2);
        zpSDK.zp_draw_line(Tx1, Ty7, Tx10, Ty7, 2);
        zpSDK.zp_draw_line(Tx1, Ty8, Tx10, Ty8, 2);
        zpSDK.zp_draw_line(Tx1, Ty9, Tx10, Ty9, 2);
        zpSDK.zp_draw_line(Tx1, Ty10, Tx10, Ty10, 2);
        zpSDK.zp_draw_line(Tx1, Ty11, Tx10, Ty11, 2);
        zpSDK.zp_draw_line(Tx1, Ty12, Tx10, Ty12, 2);
        zpSDK.zp_draw_line(Tx1, Ty13, Tx10, Ty13, 2);
        zpSDK.zp_draw_line(Tx1, Ty14, Tx10, Ty14, 2);
        zpSDK.zp_draw_line(Tx1, Ty15, Tx10, Ty15, 2);

        zpSDK.zp_draw_line(Tx1, Ty5, Tx1, Ty15, 2);
        zpSDK.zp_draw_line(Tx2, Ty5, Tx2, Ty15, 2);
        zpSDK.zp_draw_line(Tx3, Ty6, Tx3, Ty15, 2);
        zpSDK.zp_draw_line(Tx4, Ty5, Tx4, Ty15, 2);
        zpSDK.zp_draw_line(Tx5, Ty6, Tx5, Ty15, 2);
        zpSDK.zp_draw_line(Tx6, Ty5, Tx6, Ty15, 2);
        zpSDK.zp_draw_line(Tx7, Ty6, Tx7, Ty15, 2);
        zpSDK.zp_draw_line(Tx8, Ty5, Tx8, Ty15, 2);
        zpSDK.zp_draw_line(Tx9, Ty6, Tx9, Ty15, 2);
        zpSDK.zp_draw_line(Tx10, Ty5, Tx10, Ty15, 2);


        double Fx1=8;
        double Fx2=21;
        double Fx3=23.5;
        double Fx4=27.5;
        double Fx5=34;
        double Fx6=36.5;
        double Fx7=40.5;
        double Fx8=47;
        double Fx9=48;
        double Fx10=53.5;
        double Fx11=60;
        double Fx12=60.5;
        double Fx13=67;

        double Fy1=33;
        double Fy2=36;
        double Fy3=37;
        double Fy4=41.5;

        double Fy5=51.5;
        double Fy6=53;
        double Fy7=55.5;
        double Fy8=59;
        double rowshight = 4;
        double Fy9=Fy8+rowshight;
        double Fy10=Fy9+rowshight;
        double Fy11=Fy10+rowshight;
        double Fy12=Fy11+rowshight;
        double Fy13=Fy12+rowshight;
        double Fy14=Fy13+rowshight;
        double Fy15=Fy14+rowshight;


        zpSDK.zp_draw_text(Fx1, Fy2, "送箱");
        zpSDK.zp_draw_text(Fx1, Fy4, "小计");

        zpSDK.zp_draw_text(Fx2, Fy3, "实");
        zpSDK.zp_draw_text(Fx3, Fy1, "款箱");
        zpSDK.zp_draw_text(Fx4, Fy3, "空");

        zpSDK.zp_draw_text(Fx5, Fy3, "实");
        zpSDK.zp_draw_text(Fx6, Fy1, "卡箱");
        zpSDK.zp_draw_text(Fx7, Fy3, "空");

        zpSDK.zp_draw_text(Fx8, Fy3, "实");
        zpSDK.zp_draw_text(Fx9, Fy1, "凭证箱");
        zpSDK.zp_draw_text(Fx10, Fy3, "空");

        zpSDK.zp_draw_text(Fx11, Fy3, "实");
        zpSDK.zp_draw_text(Fx12, Fy1, "凭证袋");
        zpSDK.zp_draw_text(Fx13, Fy3, "空");

        Fx1 = 2;
        zpSDK.zp_draw_text(Fx1, 47.5, "收箱明细：");
        zpSDK.zp_draw_text(Fx1, Fy6, "交接类型");
        zpSDK.zp_draw_text(Fx1, Fy8, "早送晚收");
        zpSDK.zp_draw_text(Fx1, Fy9, "上下介");
        zpSDK.zp_draw_text(Fx1, Fy10, "寄库箱");
        zpSDK.zp_draw_text(Fx1, Fy11, "同行调拨");
        zpSDK.zp_draw_text(Fx1, Fy12, "跨行调拨");
        zpSDK.zp_draw_text(Fx1, Fy13, "企业收送款");
        zpSDK.zp_draw_text(Fx1, Fy14, "小计");
        zpSDK.zp_draw_text(Fx1, Fy15, "合计");

        zpSDK.zp_draw_text(Fx2, Fy7, "实");
        zpSDK.zp_draw_text(Fx3, Fy5, "款箱");
        zpSDK.zp_draw_text(Fx4, Fy7, "空");

        zpSDK.zp_draw_text(Fx5, Fy7, "实");
        zpSDK.zp_draw_text(Fx6, Fy5, "卡箱");
        zpSDK.zp_draw_text(Fx7, Fy7, "空");

        zpSDK.zp_draw_text(Fx8, Fy7, "实");
        zpSDK.zp_draw_text(Fx9, Fy5, "凭证箱");
        zpSDK.zp_draw_text(Fx10, Fy7, "空");

        zpSDK.zp_draw_text(Fx11, Fy7, "实");
        zpSDK.zp_draw_text(Fx12, Fy5, "凭证袋");
        zpSDK.zp_draw_text(Fx13, Fy7, "空");

        //送箱数据
        zpSDK.zp_draw_text(Fx2, Fy4, gatherPrint.getGivemoneyfull());
        zpSDK.zp_draw_text(Fx4, Fy4, gatherPrint.getGivemoneyempty());
        zpSDK.zp_draw_text(Fx5, Fy4, gatherPrint.getGivecardfull());
        zpSDK.zp_draw_text(Fx7, Fy4, gatherPrint.getGivecardempty());
        zpSDK.zp_draw_text(Fx8, Fy4, gatherPrint.getGivevoucherfull());
        zpSDK.zp_draw_text(Fx10, Fy4,gatherPrint.getGivevoucherempty());
        zpSDK.zp_draw_text(Fx11, Fy4,gatherPrint.getGiveoucherbagfull());
        zpSDK.zp_draw_text(Fx13, Fy4,gatherPrint.getGiveoucherbagempty());
        zpSDK.zp_draw_text(Fx2, Fy5,gatherPrint.getGiveTotal());

        //收箱数据
        //早送晚收
        zpSDK.zp_draw_text(Fx2, Fy8, gatherPrint.getZswsmoneyfull());
        zpSDK.zp_draw_text(Fx4, Fy8, gatherPrint.getZswsmoneyempty());
        zpSDK.zp_draw_text(Fx5, Fy8, gatherPrint.getZswscardfull());
        zpSDK.zp_draw_text(Fx7, Fy8, gatherPrint.getZswscardempty());
        zpSDK.zp_draw_text(Fx8, Fy8, gatherPrint.getZswsvoucherfull());
        zpSDK.zp_draw_text(Fx10, Fy8,gatherPrint.getZswsvoucherempty());
        zpSDK.zp_draw_text(Fx11, Fy8,gatherPrint.getZswsvoucherfull());
        zpSDK.zp_draw_text(Fx13, Fy8,gatherPrint.getZswsvoucherbagempty());

        //上下介
        zpSDK.zp_draw_text(Fx2, Fy9, gatherPrint.getSxjmoneyfull());
        zpSDK.zp_draw_text(Fx4, Fy9, gatherPrint.getSxjmoneyempty());
        zpSDK.zp_draw_text(Fx5, Fy9, gatherPrint.getSxjcardfull());
        zpSDK.zp_draw_text(Fx7, Fy9, gatherPrint.getSxjcardempty());
        zpSDK.zp_draw_text(Fx8, Fy9, gatherPrint.getSxjvoucherfull());
        zpSDK.zp_draw_text(Fx10, Fy9,gatherPrint.getSxjvoucherempty());
        zpSDK.zp_draw_text(Fx11, Fy9,gatherPrint.getSxjvoucherbagfull());
        zpSDK.zp_draw_text(Fx13, Fy9,gatherPrint.getSxjvoucherbagempty());

        //寄库箱
        zpSDK.zp_draw_text(Fx2, Fy10, gatherPrint.getJkxmoneyfull());
        zpSDK.zp_draw_text(Fx4, Fy10, gatherPrint.getJkxmoneyempty());
        zpSDK.zp_draw_text(Fx5, Fy10, gatherPrint.getJkxcardfull());
        zpSDK.zp_draw_text(Fx7, Fy10, gatherPrint.getJkxcardempty());
        zpSDK.zp_draw_text(Fx8, Fy10, gatherPrint.getJkxvoucherfull());
        zpSDK.zp_draw_text(Fx10, Fy10,gatherPrint.getJkxvoucherempty());
        zpSDK.zp_draw_text(Fx11, Fy10,gatherPrint.getJkxvoucherbagfull());
        zpSDK.zp_draw_text(Fx13, Fy10,gatherPrint.getJkxvoucherbagempty());
        //同行调拨
        zpSDK.zp_draw_text(Fx2, Fy11, gatherPrint.getThdbmoneyfull());
        zpSDK.zp_draw_text(Fx4, Fy11, gatherPrint.getThdbmoneyempty());
        zpSDK.zp_draw_text(Fx5, Fy11, gatherPrint.getThdbcardfull());
        zpSDK.zp_draw_text(Fx7, Fy11, gatherPrint.getThdbcardempty());
        zpSDK.zp_draw_text(Fx8, Fy11, gatherPrint.getThdbvoucherfull());
        zpSDK.zp_draw_text(Fx10, Fy11,gatherPrint.getThdbvoucherempty());
        zpSDK.zp_draw_text(Fx11, Fy11,gatherPrint.getThdbvoucherbagfull());
        zpSDK.zp_draw_text(Fx13, Fy11,gatherPrint.getThdbvoucherbagempty());
        //跨行调拨
        zpSDK.zp_draw_text(Fx2, Fy12, gatherPrint.getKhdbmoneyfull());
        zpSDK.zp_draw_text(Fx4, Fy12, gatherPrint.getKhdbmoneyempty());
        zpSDK.zp_draw_text(Fx5, Fy12, gatherPrint.getKhdbcardfull());
        zpSDK.zp_draw_text(Fx7, Fy12, gatherPrint.getKhdbcardempty());
        zpSDK.zp_draw_text(Fx8, Fy12, gatherPrint.getKhdbvoucherfull());
        zpSDK.zp_draw_text(Fx10, Fy12,gatherPrint.getKhdbvoucherempty());
        zpSDK.zp_draw_text(Fx11, Fy12,gatherPrint.getKhdbvoucherbagfull());
        zpSDK.zp_draw_text(Fx13, Fy12,gatherPrint.getKhdbvoucherbagempty());
        //企业收送款
        zpSDK.zp_draw_text(Fx2, Fy13, gatherPrint.getQysskmoneyfull());
        zpSDK.zp_draw_text(Fx4, Fy13, gatherPrint.getQysskmoneyempty());
        zpSDK.zp_draw_text(Fx5, Fy13, gatherPrint.getQysskcardfull());
        zpSDK.zp_draw_text(Fx7, Fy13, gatherPrint.getQysskcardempty());
        zpSDK.zp_draw_text(Fx8, Fy13, gatherPrint.getQysskvoucherfull());
        zpSDK.zp_draw_text(Fx10, Fy13,gatherPrint.getQysskvoucherempty());
        zpSDK.zp_draw_text(Fx11, Fy13,gatherPrint.getQysskvoucherbagfull());
        zpSDK.zp_draw_text(Fx13, Fy13,gatherPrint.getQysskvoucherbagempty());
        //小计
        zpSDK.zp_draw_text(Fx2, Fy14, gatherPrint.getGetTotalmoneyfull());
        zpSDK.zp_draw_text(Fx4, Fy14, gatherPrint.getGetTotalmoneyempty());
        zpSDK.zp_draw_text(Fx5, Fy14, gatherPrint.getGetTotalcardfull());
        zpSDK.zp_draw_text(Fx7, Fy14, gatherPrint.getGetTotalcardempty());
        zpSDK.zp_draw_text(Fx8, Fy14, gatherPrint.getGetTotalvoucherfull());
        zpSDK.zp_draw_text(Fx10, Fy14,gatherPrint.getGetTotalvoucherempty());
        zpSDK.zp_draw_text(Fx11, Fy14,gatherPrint.getGetTotalvoucherbagfull());
        zpSDK.zp_draw_text(Fx13, Fy14,gatherPrint.getGetTotalvoucherbagempty());
        //合计
        zpSDK.zp_draw_text(Fx3, Fy15, gatherPrint.getGetTotalmoney());
        zpSDK.zp_draw_text(Fx6, Fy15,gatherPrint.getGetTotalcard());
        zpSDK.zp_draw_text(Fx9, Fy15,gatherPrint.getGetTotalvoucher());
        zpSDK.zp_draw_text(Fx12, Fy15,gatherPrint.getGetTotalvoucherbag());

        zpSDK.zp_draw_text(3, 93, "押运业务员"+gatherPrint.getHomName());
        zpSDK.zp_draw_text(3, 100, "网点交接人（签章）1：           2：");
        Boolean printpage =  zpSDK.zp_page_print(false);
        Log.e("kim", "print" + printpage.toString());

        zpSDK.zp_page_free();

        return false;
    }
}
