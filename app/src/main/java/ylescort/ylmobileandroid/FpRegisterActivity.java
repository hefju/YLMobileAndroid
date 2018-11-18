package ylescort.ylmobileandroid;

import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.za.finger.FingerHelper;
import com.za.finger.IUsbConnState;

public class FpRegisterActivity extends ActionBarActivity implements View.OnClickListener  {
private  String TAG="FpRegisterActivity";
    EditText etEmpNum;//录入员工编号
    TextView txtTitle,txtUser1,txtUser2,txtMessage;//标题,用户1,用户2,提示信息
    Button btnFpOpen,btnFpRegister,btnFpClose;//启动指纹,注册指纹,关闭指纹
    protected Handler mHandler=null ;  //消息同步
    protected final int MSG_HF = 1101 ;
    protected final int MSG_FP = 1102 ;

    protected  int fpCharBuffer=1;//指纹A 指纹B
    protected int templateNum = 0 ;
    protected long startTime = 0L ; //指纹等待时间开始
    protected long endTime = 0L ;   //指纹等待时间结束, 计算超时

    private int statues = 0 ;//指纹功能状态, 是获取特征码还是图像,还是登录
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fp_register);
        mFingerHelper = new FingerHelper(this, usbConnstate);
        InitUi();
    }

    private void InitUi() {
        txtTitle= (TextView) findViewById(R.id.txtTitle);
        etEmpNum= (EditText) findViewById(R.id.etEmpNum);
        txtUser1= (TextView) findViewById(R.id.txtUser1);
        txtUser2= (TextView) findViewById(R.id.txtUser2);
        txtMessage= (TextView) findViewById(R.id.txtMessage);
        btnFpOpen= (Button) findViewById(R.id.btnFpOpen);
        btnFpOpen.setOnClickListener(this);
        btnFpRegister= (Button) findViewById(R.id.btnFpRegister);
        btnFpRegister.setOnClickListener(this);
        btnFpRegister.setEnabled(false);
        btnFpClose= (Button) findViewById(R.id.btnFpClose);
        btnFpClose.setOnClickListener(this);
        //btnFpClose.setEnabled(false);

        SetNormolMessage("请点击开启指纹");
        etEmpNum.setText("600013");//测试用

        mHandler = new Handler() {
            public void handleMessage(android.os.Message msg) {
             if(msg.what== MSG_FP){
                    String fpmsg = msg.getData().getString("fpmsg");
                    // Util.play(1, 0 );
                   // txtcontent.setText(fpmsg);
                 SetErrorMessage(fpmsg);
                }
            };
        };
    }

    //界面上的信息提示
    private void SetNormolMessage(String msg){
        txtMessage.setText(msg);
        txtMessage.setTextColor(Color.BLACK);
    }
    private void SetErrorMessage(String msg){
        txtMessage.setText(msg);
        txtMessage.setTextColor(Color.RED);
    }

    protected  boolean IsFpInit=false; //fp初始化了没有
    protected FingerHelper mFingerHelper ;  //option finger
    protected boolean FpInit() {

        mFingerHelper = new FingerHelper(this, usbConnstate);
        mFingerHelper.init();

        long  startTime = System.currentTimeMillis() ;
        long endTime = startTime ;
        //run match finger char task
        mHandler.postDelayed(searchTask, 0);
        IsFpInit=true;

        return  true;
    }

    private void sendMSG_fp(String content, int msgCode) {
        //Log.e("jutest", "cardUid = " + cardUid);
        Bundle bundle = new Bundle();
        bundle.putString("fpmsg", content);
        Message msg = new Message() ;
        msg.setData(bundle);
        msg.what = msgCode ;
        mHandler.sendMessage(msg);
    }

    /**
     * search finger in flash database
     */
    protected Runnable searchTask = new Runnable() {
        @Override
        public void run() {
            String temp = "";
            long timeCount = 0L;
            endTime = System.currentTimeMillis();
            timeCount = endTime - startTime;
            //search finger time 10s
            if (timeCount > 10000) {
                Log.i("jutest", "get_finger_img_time_out");
                sendMSG_fp("get_finger_img_time_out", MSG_FP);
                return;
            }
            try {
                int statues = mFingerHelper.getImage();
                //find finger
                if (statues == mFingerHelper.PS_OK) {
                    //gen char to bufferA
                    statues = mFingerHelper.genChar(mFingerHelper.CHAR_BUFFER_A);
                    if (statues == mFingerHelper.PS_OK) {
                        int[] iMaddr = {0, 0};
                        //is exist flash database,database size = 512
                        statues = mFingerHelper.search(mFingerHelper.CHAR_BUFFER_A, 0, 512, iMaddr);
                        if (statues == mFingerHelper.PS_OK) {
                            Log.i("jutest", "finger_is_found");
                            sendMSG_fp("finger_is_found", MSG_FP);
                        } else {
                            Log.i("jutest", "no_found_finger_in_flash");
                            sendMSG_fp("no_found_finger_in_flash", MSG_FP);
                        }

                    }
                } else if (statues == mFingerHelper.PS_NO_FINGER) {
                    Log.i("jutest", "searching_finger");
                    sendMSG_fp("searching_finger", MSG_FP);
                    mHandler.postDelayed(searchTask, 100);
                } else if (statues == mFingerHelper.PS_GET_IMG_ERR) {
                    Log.i("jutest", "get_img_error");
                    sendMSG_fp("get_img_error", MSG_FP);
                    return;
                } else {
                    Log.i("jutest", "dev_error"); //temp = res.getString(R.string.dev_error);
                    sendMSG_fp("dev_error", MSG_FP);
                    return;
                }
            }
            catch (Exception e){
                SetErrorMessage(e.getMessage());
            }
        }
    } ;
    //IUsbConnState is to receive usb finger connect state
    private IUsbConnState usbConnstate = new IUsbConnState() {
        @Override
        public void onUsbConnected() {
            //Loger.e(tag, "onUsbConnected()");
            //connect finger device
            int statues =  mFingerHelper.connectFingerDev() ;
            if (statues == mFingerHelper.CONNECT_OK) {
                Log.i("jutest","conn_dev_success");
            }else{
                Log.i("jutest","conn_dev_fail");
            }
            //setAllBtnEnable(true, btnOpen, false);
        }

        @Override
        public void onUsbPermissionDenied() {
            Log.i("jutest","usb_perssion_deny");
        }

        @Override
        public void onDeviceNotFound() {
            Log.i("jutest","dev_not_found");
        }
    } ;
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnFpOpen:
                Log.e(TAG,"btnFpOpen.onClick");
                //开启指纹
               // FpInit();

                mFingerHelper.init() ;
                btnFpOpen.setEnabled(false);
              //  btnFpClose.setEnabled(true);
                btnFpRegister.setEnabled(true);
//                if(!IsFpInit) {
//                    boolean success = FpInit();
//                    if(!success){
//                        SetErrorMessage("指纹初始化失败.");
//                    }
//                }

                break;
            case R.id.btnFpRegister:

                try {
                    //注册指纹
                    FpRegister();
                }catch (Exception e){
                    SetErrorMessage(e.getMessage());
                }
            case R.id.btnFpClose:
                //关闭指纹
                try {
                    mFingerHelper.close();
                }catch (Exception e){
                    SetErrorMessage(e.getMessage());
                }
                btnFpOpen.setEnabled(true);
               // btnFpClose.setEnabled(false);
                btnFpRegister.setEnabled(false);
            default:
                break;
        }
    }

    private void FpRegister() {

        startTime = System.currentTimeMillis() ;
        endTime = startTime ;
        //run match finger char task
        mHandler.postDelayed(searchTask, 0);

//        startTime = System.currentTimeMillis() ;
//        endTime = startTime ;
//        fpCharBuffer = mFingerHelper.CHAR_BUFFER_A ;
//        mHandler.postDelayed(enrollTask, 0);


//        startTime = System.currentTimeMillis() ;
//        //endTime = startTime ;
//
//        fpCharBuffer = mFingerHelper.CHAR_BUFFER_A ;
//        //run match finger char task
//        mHandler.postDelayed(enrollTask, 0);

//        Toast.makeText(FpRegisterActivity.this,"注册指纹", Toast.LENGTH_SHORT).show();
    }

    /**
     * enroll finger char to flash database
     */
    private Runnable enrollTask  = new Runnable() {
        @Override
        public void run() {
            String temp = ""  ;
            long timeCount = 0L ;
            endTime = System.currentTimeMillis() ;
            timeCount = endTime - startTime ;
            //search finger time 10s
            if (timeCount > 10000) {
                SetErrorMessage( "获取指纹超时1");
                return ;
            }
            try {
                statues = mFingerHelper.getImage();
                //find finger
                if (statues == mFingerHelper.PS_OK) {
                    //first finger
                    if (fpCharBuffer == mFingerHelper.CHAR_BUFFER_A) {
                        //gen char to bufferA
                        statues = mFingerHelper.genChar(fpCharBuffer);
                        if (statues == mFingerHelper.PS_OK) {
                            int[] iMaddr = {0, 0};
                            //is exist flash database,database size = 512
                            statues = mFingerHelper.search(mFingerHelper.CHAR_BUFFER_A, 0, 512, iMaddr);
                            if (statues == mFingerHelper.PS_OK) {
                                //temp = res.getString(R.string.already_exist_flash) + " , User id index["+ iMaddr[0] +"]";
                                SetErrorMessage("指纹已经存在");//already_exist_flash
                                return;
                            }
                            SetNormolMessage("请重新录入指纹");//gen_finger_buffer_a_press_again
                            //fpCharBuffer = mFingerHelper.CHAR_BUFFER_B ;
                            mHandler.postDelayed(enrollTask, 2000);
                        }
                    }
//                else if (fpCharBuffer == mFingerHelper.CHAR_BUFFER_B) { //second finger
//                    //gen char to bufferB
//                    statues = mFingerHelper.genChar(fpCharBuffer);
//                    if (statues == mFingerHelper.PS_OK) {
//                        temp = res.getString(R.string.gen_char) + " \r\n";
//                        editTips.setText(temp);
//                        //merge BUFFER_A with BUFFER_B , gen template to MODULE_BUFFER
//                        mFingerHelper.regTemplate() ;
//                        int[] iMbNum = {0, 0} ;
//                        mFingerHelper.getTemplateNum(iMbNum);
//                        templateNum = iMbNum[0];
//                        if (templateNum >= 512) {
//                            temp = res.getString(R.string.flash_database_full) + " \r\n";
//                            editTips.setText(temp);
//                            setAllBtnEnable(true, btnOpen, false);
//                            return ;
//                        }
//                        //store template to flash database
//                        statues =  mFingerHelper.storeTemplate(mFingerHelper.MODEL_BUFFER, templateNum);
//                        if (statues == mFingerHelper.PS_OK) {
//                            temp = res.getString(R.string.enroll_success) + ", User id index["+templateNum +"] \r\n";
//                            editTips.setText(temp);
//                        }else{
//                            temp = res.getString(R.string.enroll_fail) + ",statues= " + statues +" \r\n";
//                            editTips.setText(temp);
//                        }
//                    }
//                    setAllBtnEnable(true, btnOpen, false);
//                }

                } else if (statues == mFingerHelper.PS_NO_FINGER) {
                    // temp = res.getString(R.string.searching_finger) + " ,time:" +((10000-(endTime - startTime)))/1000 +"s";
                    SetNormolMessage("等待按指纹");//searching_finger
                    mHandler.postDelayed(enrollTask, 100);
                } else if (statues == mFingerHelper.PS_GET_IMG_ERR) {
                    //temp = res.getString(R.string.get_img_error);
                    SetErrorMessage("获取指纹失败");//searching_finger
                    return;
                } else {
                    //temp = res.getString(R.string.dev_error);
                    SetErrorMessage("指纹连接失败");//searching_finger
                    return;
                }
            }
            catch (Exception e){
                SetErrorMessage( e.getMessage());
            }
        }
    } ;
    @Override
    protected void onDestroy() {
        mFingerHelper.close();//关闭指纹
        super.onDestroy();
    }
}
