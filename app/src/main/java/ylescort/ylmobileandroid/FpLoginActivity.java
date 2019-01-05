package ylescort.ylmobileandroid;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.za.finger.FingerHelper;
import com.za.finger.IUsbConnState;

import java.util.List;

import ScanFP.ScanThread;
import ScanFP.YLFPHelper;
import TaskClass.BaseEmp;
import TaskClass.User;
import YLDataService.BaseEmpDBSer;
import YLSystemDate.YLSystem;
import cn.pda.serialport.Tools;

public class FpLoginActivity extends ActionBarActivity  implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fp_login);
        InitLayout();
        InitData();
    }
    private String Tag = "kim";
    private ImageView img_fp;
    private EditText edit_tips;
    private Button btn_open,btn_get_img,btn_get_char,btn_match_fp,btn_search,btn_enroll,btn_empty,btn_close,btn_scan;
    private TextView txtUser1,txtUser2;
    private String EmpNumA="";//员工编号A
    private String EmpNumB="";//员工编号B
    YLFPHelper ylfpHelper=new YLFPHelper();

    private Context getActivityContext() {
        return FpLoginActivity.this;
    }
    //scan
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == ScanThread.SCAN){
                String data = msg.getData().getString("data");
                edit_tips.setText("ScanData:"+data);
            }
            super.handleMessage(msg);
        }
    };

    //fp
    private FingerHelper mFingerHelper;
    private int statues = 0;
    private long startTime = 0L;
    private long endTime = 0L;
    private Handler fpHandler = new Handler();
    private final  int IMAGE_SIZE = 256*288;
    private String tempImgPath = "/mnt/sdcard/temp.bmp";
    private Bitmap defaultBm;
    private  int fpCharBuffer =0;
    private  int templateNum = 0;
    private String tips = "";
    private Resources res;

    private IUsbConnState usbConnState = new IUsbConnState() {
        @Override
        public void onUsbConnected() {
            Log.e(Tag,"onUsbConnected()");
            statues = mFingerHelper.connectFingerDev();
            if (statues == mFingerHelper.CONNECT_OK){
                tips = "指纹设备打开成功.";//"connect usb finger device success";
                edit_tips.setText(tips);
            }else{
                tips = "指纹设备打开失败.";//"connect usb finger device fail";
                edit_tips.setText(tips+"statues ="+statues);
            }
        }

        @Override
        public void onUsbPermissionDenied() {
            Log.e(Tag,"onUsbPermissionDenied()");
            tips = "usb 权限拒绝";
            edit_tips.setText(tips);
        }

        @Override
        public void onDeviceNotFound() {
            tips = "未找到设备";
            edit_tips.setText(tips);
        }
    };

    private void InitLayout() {
//        img_fp = (ImageView) findViewById(R.id.img_fp);
        edit_tips = (EditText) findViewById(R.id.edit_tips);
        txtUser1 = (TextView) findViewById(R.id.txtUser1);
        txtUser2 = (TextView) findViewById(R.id.txtUser2);
        btn_open = (Button) findViewById(R.id.btn_open);
        btn_search = (Button) findViewById(R.id.btn_search);
        btn_close = (Button) findViewById(R.id.btn_close);

        btn_open.setOnClickListener(this);

        btn_search.setOnClickListener(this);
        btn_close.setOnClickListener(this);

    }

    private void InitData() {
        //fp
        mFingerHelper = new FingerHelper(this,usbConnState);
       res = this.getResources();
//        defaultBm = BitmapFactory.decodeResource(getResources(),R.drawable.fingerprint);
        ylfpHelper.setmFingerHelper(mFingerHelper);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_open: OpenDevice();
                break;

            case R.id.btn_search: SearchFp();
                break;

            case R.id.btn_close: CloseDevice();
                break;
        }
    }

    private void OpenDevice() {
        mFingerHelper.init();
    }

    private void SearchFp() {
        startTime = System.currentTimeMillis() ;
        endTime = startTime ;
        //run match finger char task
        mHandler.postDelayed(CompareTask, 0);//CompareTask searchTask
    }

    private void CloseDevice() {
        mFingerHelper.close();
       // UnLockUI();//测试
    }
    @Override
    protected void onDestroy() {
        if (mFingerHelper !=null){
            mFingerHelper.close();
        }
        super.onDestroy();
    }

    /**
     * 对比指纹,采用字符串的方式,
     */
    private Runnable CompareTask = new Runnable() {
        @Override
        public void run() {
            String temp = ""  ;
            long timeCount = 0L ;
            endTime = System.currentTimeMillis() ;
            timeCount = endTime - startTime ;
            //search finger time 10s
            if (timeCount > 10000) {
                temp = res.getString(R.string.get_finger_img_time_out);
                edit_tips.setText(temp);

                return ;
            }
            statues = mFingerHelper.getImage() ;
            //find finger
            if (statues == mFingerHelper.PS_OK) {
                //gen char to bufferA
                statues = mFingerHelper.genChar(mFingerHelper.CHAR_BUFFER_A);
                if (statues == mFingerHelper.PS_OK) {
                    int[] iMaddr = {0, 0} ;
                    byte[] charBytes = new byte[512];
                    //is exist flash database,database size = 512
                   // statues = mFingerHelper.search(mFingerHelper.CHAR_BUFFER_A, 0, 512, iMaddr);
                    statues = mFingerHelper.upCharFromBufferID(mFingerHelper.CHAR_BUFFER_A, charBytes, iMaddr);
                    if (statues == mFingerHelper.PS_OK) {
                        String fpInput= Tools.Bytes2HexString(charBytes, 512);


                        String empNum= ylfpHelper.FindEmpByFP(fpInput,getActivityContext());
                        if(empNum.equals("")){
                            temp = "通过指纹没有找到用户,请重试.";
                            edit_tips.setText(temp);
                        }else{
                            if(EmpNumA.equals("")) {
                                EmpNumA = empNum;
                                SetLoginFlag(EmpNumA,txtUser1);
                                temp = "请第二个员工验证指纹";
                                edit_tips.setText(temp);
                            }
                            else if(EmpNumB.equals("")) {
                                EmpNumB = empNum;
                                SetLoginFlag(EmpNumB,txtUser2);
                                UnLockUI();//解锁界面,跳转到下一个界面,暂时不知道要转到哪里,可以跳转到多个目标界面才行
                            }
                        }

                    }else{
                        temp = res.getString(R.string.finger_char_is_bad_try_again);
                        edit_tips.setText(temp);
                    }

                }
            } else if (statues == mFingerHelper.PS_NO_FINGER) {
                temp = res.getString(R.string.searching_finger) + " ,time:" +((10000-(endTime - startTime)))/1000 +"s";
                edit_tips.setText(temp);
                fpHandler.postDelayed(CompareTask, 100);
            } else if (statues == mFingerHelper.PS_GET_IMG_ERR) {
                temp = res.getString(R.string.get_img_error);
                edit_tips.setText(temp);

                return ;
            }else{
                temp = res.getString(R.string.dev_error);
                edit_tips.setText(temp);

                return ;
            }
        }


    } ;

    private void UnLockUI() {
        try {
//            if(EmpNumA.equals(""))   //测试
//                EmpNumA="620142";
            BaseEmpDBSer baseEmpDBSer=new BaseEmpDBSer(getActivityContext());
            BaseEmp baseUser=baseEmpDBSer.GetUserByEmpNo(EmpNumA);

            User user = new User();
            user.setEmpNO(baseUser.EmpNo);
            user.setEmpID(baseUser.EmpID);
            user.setPass("");
            user.setName(baseUser.EmpName);
            user.setISWIFI("0");
            user.setTaskDate("");
            YLSystem.setUser(user);

            //如果要打开任务界面Task.class, 必须先设置用户
            Intent intent = new Intent();
            intent.setClass(FpLoginActivity.this, Task.class);
            startActivity(intent);
        }
        catch (Exception e){
            Log.e(Tag,e.getMessage());
        }
    }

    private void SetLoginFlag(String empNumAB, TextView txtUser12) {
        txtUser12.setText(empNumAB+" 已验证");
    }
}
