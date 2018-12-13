package ylescort.ylmobileandroid;

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

import com.za.finger.FingerHelper;
import com.za.finger.IUsbConnState;

import ScanFP.ScanThread;
import cn.pda.serialport.Tools;
import ylescort.ylmobileandroid.R;

public class FpRegisterActivity2 extends ActionBarActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fp_register2);
        InitLayout();
        InitData();
    }

    private String Tag = "kim";
    private ImageView img_fp;
    private EditText edit_tips;
    private Button btn_open,btn_enroll,btn_close;

    //scan

    private ScanThread scanThread;
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
        img_fp = (ImageView) findViewById(R.id.img_fp);
        edit_tips = (EditText) findViewById(R.id.edit_tips);
        btn_open = (Button) findViewById(R.id.btn_open);
        btn_close = (Button) findViewById(R.id.btn_close);
        btn_enroll = (Button) findViewById(R.id.btn_enroll);

        btn_open.setOnClickListener(this);
        btn_enroll.setOnClickListener(this);
        btn_close.setOnClickListener(this);
    }

    private void InitData() {
        //scan
        try {
            scanThread = new ScanThread(mHandler);
            scanThread.start();
        }catch (Exception e){
            Log.e(Tag,"扫描打开失败");
        }

        //fp
        mFingerHelper = new FingerHelper(this,usbConnState);
        res = this.getResources();
        defaultBm = BitmapFactory.decodeResource(getResources(),R.drawable.fingerprint);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_open: OpenDevice();
                break;
            case R.id.btn_enroll: EnrollFp();
                break;
            case R.id.btn_close: CloseDevice();
                break;

        }
    }

    private void OpenDevice() {
        mFingerHelper.init();
    }

    private void EnrollFp() {
        img_fp.setImageBitmap(defaultBm);
        startTime = System.currentTimeMillis() ;
        endTime = startTime ;

        fpCharBuffer = mFingerHelper.CHAR_BUFFER_A ;
        //run match finger char task
        mHandler.postDelayed(enrollTask, 0);
    }

    private void CloseDevice() {
        mFingerHelper.close();
        this.finish();
    }

    @Override
    protected void onDestroy() {
        if (mFingerHelper !=null){
            mFingerHelper.close();
        }
        super.onDestroy();
    }

    private Runnable getFPImageTask = new Runnable() {
        @Override
        public void run() {
            String temp = "";
            long timeCount = 0L;
            endTime = System.currentTimeMillis();
            timeCount = endTime - startTime;
            if (timeCount > 10000){
                temp = "获取图像超时";
                edit_tips.setText(temp);
                return;
            }
            statues = mFingerHelper.getImage();
            if (statues == mFingerHelper.PS_OK){
                int[] recvLen = {0,0};
                byte[] imageByte = new byte[IMAGE_SIZE];
                mFingerHelper.uploadImage(imageByte,recvLen);
                mFingerHelper.imageData2BMP(imageByte,tempImgPath);
                temp = "获取图像成功";
                edit_tips.setText(temp);
                Bitmap bm = BitmapFactory.decodeFile(tempImgPath,null);
                img_fp.setImageBitmap(bm);
            }else if(statues == mFingerHelper.PS_NO_FINGER){
                temp = "查找指纹,请按指纹"+ " ,time:" +((10000-(endTime - startTime)))/1000 +"s";
                edit_tips.setText(temp);
                fpHandler.postDelayed(getFPImageTask,100);
            }else if (statues == mFingerHelper.PS_GET_IMG_ERR){
                temp = "获取图像出错";
                edit_tips.setText(temp);
                fpHandler.postDelayed(getFPImageTask,100);
                return;
            }else{
                temp = "设备故障";
                edit_tips.setText(temp);
                return;
            }
        }
    };

    /**
     * get finger char
     */
    private Runnable getCharTask = new Runnable() {
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
                    int[] iCharLen = {0, 0} ;
                    byte[] charBytes = new byte[512];
                    //upload char
                    statues = mFingerHelper.upCharFromBufferID(mFingerHelper.CHAR_BUFFER_A, charBytes, iCharLen);
                    if (statues == mFingerHelper.PS_OK) {
                        //upload success
                        temp = res.getString(R.string.get_finger_char_success) +":\r\n " + Tools.Bytes2HexString(charBytes, 512);
                        edit_tips.setText(temp);

                    }
                }else{
                    //char is bad quickly
                    temp = res.getString(R.string.finger_char_is_bad_try_again);
                    edit_tips.setText(temp);

                    return ;
                }
            } else if (statues == mFingerHelper.PS_NO_FINGER) {
                temp = res.getString(R.string.searching_finger) + " ,time:" +((10000-(endTime - startTime)))/1000 +"s";
                edit_tips.setText(temp);
                fpHandler.postDelayed(getCharTask, 100);
            } else if (statues == mFingerHelper.PS_GET_IMG_ERR) {
                temp = res.getString(R.string.get_img_error);
                edit_tips.setText(temp);
                fpHandler.postDelayed(getCharTask, 100);
                return ;
            }else{
                temp = res.getString(R.string.dev_error);
                edit_tips.setText(temp);

                return ;
            }
        }
    } ;

    /**
     * match two finger char, if match score > 60 is the same finger
     */
    private Runnable matchFingerTask = new Runnable() {
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
                //first finger
                if (fpCharBuffer == mFingerHelper.CHAR_BUFFER_A) {
                    //gen char to bufferA
                    statues = mFingerHelper.genChar(fpCharBuffer);
                    if (statues == mFingerHelper.PS_OK) {
                        temp = res.getString(R.string.gen_finger_buffer_a_press_again);
                        edit_tips.setText(temp);
                        fpCharBuffer = mFingerHelper.CHAR_BUFFER_B ;
                        fpHandler.postDelayed(matchFingerTask, 2000);
                    }
                } else if (fpCharBuffer == mFingerHelper.CHAR_BUFFER_B) { //second finger
                    //gen char to bufferB
                    statues = mFingerHelper.genChar(fpCharBuffer);
                    if (statues == mFingerHelper.PS_OK) {
                        temp = res.getString(R.string.gen_finger_buffer_b_success) + " \r\n";
                        edit_tips.setText(temp);
                        //match buffer_a with buffer_b
                        int[] iScore = {0, 0} ;
                        mFingerHelper.match(iScore);
                        temp = res.getString(R.string.match_a_b_success_score) + " = " + iScore[0] ;
                        edit_tips.append(temp);

                    }
                }

            } else if (statues == mFingerHelper.PS_NO_FINGER) {
                temp = res.getString(R.string.searching_finger) + " ,time:" +((10000-(endTime - startTime)))/1000 +"s";
                edit_tips.setText(temp);
                fpHandler.postDelayed(matchFingerTask, 100);
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
    }  ;


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
                temp = res.getString(R.string.get_finger_img_time_out);
                edit_tips.setText(temp);

                return ;
            }
            statues = mFingerHelper.getImage() ;
            //find finger
            if (statues == mFingerHelper.PS_OK) {
                //first finger
                if (fpCharBuffer == mFingerHelper.CHAR_BUFFER_A) {
                    //gen char to bufferA
                    statues = mFingerHelper.genChar(fpCharBuffer);
                    if (statues == mFingerHelper.PS_OK) {
                        int[] iMaddr = {0, 0} ;
                        //is exist flash database,database size = 512
                        statues = mFingerHelper.search(mFingerHelper.CHAR_BUFFER_A, 0, 512, iMaddr);
                        if (statues == mFingerHelper.PS_OK) {
                            temp = res.getString(R.string.already_exist_flash) + " , User id index["+ iMaddr[0] +"]";
                            edit_tips.setText(temp);

                            return ;
                        }
                        temp = res.getString(R.string.gen_finger_buffer_a_press_again);
                        edit_tips.setText(temp);
                        fpCharBuffer = mFingerHelper.CHAR_BUFFER_B ;
                        fpHandler.postDelayed(enrollTask, 2000);
                    }
                } else if (fpCharBuffer == mFingerHelper.CHAR_BUFFER_B) { //second finger
                    //gen char to bufferB
                    statues = mFingerHelper.genChar(fpCharBuffer);
                    if (statues == mFingerHelper.PS_OK) {
                        temp = res.getString(R.string.gen_char) + " \r\n";
                        edit_tips.setText(temp);
                        //merge BUFFER_A with BUFFER_B , gen template to MODULE_BUFFER
                        mFingerHelper.regTemplate() ;
                        int[] iMbNum = {0, 0} ;
                        mFingerHelper.getTemplateNum(iMbNum);
                        templateNum = iMbNum[0];
                        if (templateNum >= 512) {
                            temp = res.getString(R.string.flash_database_full) + " \r\n";
                            edit_tips.setText(temp);

                            return ;
                        }
                        //store template to flash database
                        statues =  mFingerHelper.storeTemplate(mFingerHelper.MODEL_BUFFER, templateNum);
                        if (statues == mFingerHelper.PS_OK) {
                            temp = res.getString(R.string.enroll_success) + ", User id index["+templateNum +"] \r\n";
                            edit_tips.setText(temp);
                        }else{
                            temp = res.getString(R.string.enroll_fail) + ",statues= " + statues +" \r\n";
                            edit_tips.setText(temp);
                        }

                    }

                }

            } else if (statues == mFingerHelper.PS_NO_FINGER) {
                temp = res.getString(R.string.searching_finger) + " ,time:" +((10000-(endTime - startTime)))/1000 +"s";
                edit_tips.setText(temp);
                fpHandler.postDelayed(enrollTask, 100);
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

    /**
     * search finger in flash database
     */
    private Runnable searchTask = new Runnable() {
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
                    //is exist flash database,database size = 512
                    statues = mFingerHelper.search(mFingerHelper.CHAR_BUFFER_A, 0, 512, iMaddr);
                    if (statues == mFingerHelper.PS_OK) {
                        temp = res.getString(R.string.finger_is_found) +" , User id index["+ iMaddr[0] +"]";
                        edit_tips.setText(temp);

                    }else{
                        temp = res.getString(R.string.no_found_finger_in_flash);
                        edit_tips.setText(temp);
                    }

                }
            } else if (statues == mFingerHelper.PS_NO_FINGER) {
                temp = res.getString(R.string.searching_finger) + " ,time:" +((10000-(endTime - startTime)))/1000 +"s";
                edit_tips.setText(temp);
                fpHandler.postDelayed(searchTask, 100);
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
}
