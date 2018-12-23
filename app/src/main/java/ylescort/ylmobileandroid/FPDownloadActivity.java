package ylescort.ylmobileandroid;

import android.content.Context;
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

import java.util.List;

import ScanFP.YLFPHelper;
import TaskClass.BaseEmp;
import TaskClass.FingerPrint;
import TaskClass.User;
import TaskClass.YLTask;
import YLDataService.BaseEmpDBSer;
import YLDataService.EmpDBSer;
import YLDataService.FingerPrintDBSer;
import YLDataService.WebService;
import YLWebService.YLWebService;

//登录前请先下载指纹,因为指纹库不会全部下载到本地
public class FPDownloadActivity extends ActionBarActivity implements View.OnClickListener{
    private  String TAG="FPDownloadActivity";
    TextView txtInfo; //提示信息
    EditText txtEmpNum; //员工编号
    Button btnDownload;//下载按钮
    YLFPHelper ylfpHelper=new YLFPHelper();
    android.os.Handler mHandler; //消息处理
    private String ipEmpNum="";//用户输入的员工编号

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fpdownload);
        InitLayout();
    }

    private void InitLayout() {
        txtEmpNum = (EditText) findViewById(R.id.txtEmpNum);
        txtInfo = (TextView) findViewById(R.id.txtInfo);
        txtInfo.setText("");

        btnDownload = (Button) findViewById(R.id.btnDownload);
        btnDownload.setOnClickListener(this);
        //asyncMessage();
    }

//UI多线程写法参考: https://blog.csdn.net/mad1989/article/details/25964495
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 0: //这是例子
                    Bundle data = msg.getData();
                    String val = data.getString("value");
                    //Log.i("mylog", "请求结果为-->" + val);
                    break;
                case 20: //获取GetTaskList成功
                    txtInfo.setText(txtEmpNum.getText().toString()+" 指纹下载成功");
                    txtEmpNum.setText("");
                    txtEmpNum.setFocusable(true);
                    btnDownload.setEnabled(true);
                    break;
                case 21://获取GetTaskList失败, 服务器返回值不等于1, 获取数据失败
                    txtInfo.setText("指纹下载失败");
//                        String content = (String) msg.obj;
//                        Toast.makeText(getApplicationContext(),content,Toast.LENGTH_LONG).show();
//                        Task_btn_refresh.setEnabled(true);//可以再次点击刷新了
                    btnDownload.setEnabled(true);
                    break;
                default:
                    break;
            }
        }
    };




    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnDownload: DownLoadFP();
                break;
            default:
                break;
        }
    }
    private Context getActivityContext() {
        return FPDownloadActivity.this;
    }


    //下载指纹
    private void DownLoadFP() {
        Context context=getActivityContext();
        String empNum=txtEmpNum.getText().toString();//员工编号
        ipEmpNum=empNum;//放到外部变量,其他线程使用
        if(empNum==null||empNum.equals("")){
            txtInfo.setText("请输入员工编号");
            return;
        }

        //检查本地数据库是否存在指纹
        FingerPrintDBSer fingerPrintDBSer=new  FingerPrintDBSer(context);
        FingerPrint fingerPrint=new FingerPrint();
        fingerPrint.setEmpNum(empNum);
        fingerPrint.setFingerType("0");//手指类型用0,1,2,3, 不用文字了
        boolean isExists= fingerPrintDBSer.Exists(fingerPrint);
        if(isExists){
            txtInfo.setText("员工指纹已经存在");
            return;
        }


        //从网络上下载指纹,获取员工ID,获取指纹
        // 开启一个子线程，进行网络操作，等待有返回结果，使用handler通知UI
        new Thread(networkTask).start();
        btnDownload.setEnabled(false);
    }

    Runnable networkTask = new Runnable() {

        @Override
        public void run() {
            Context context=getActivityContext();
            // 在这里进行 http request.网络请求相关操作
            BaseEmpDBSer baseEmpDBSer=new BaseEmpDBSer(context);
            BaseEmp user=baseEmpDBSer.GetUserByEmpNo(ipEmpNum);
            if(user==null){
                txtInfo.setText("员工不存在或者没有员工资料");
                return;
            }
            String empid=user.getEmpID();
            YLWebService ylWebService = new YLWebService();
            List<String> list = ylWebService.GetEmpFingerPrints(getActivityContext(), empid, "unitest", "1");
            //ipEmpNum

            YLFPHelper ylfpHelper=new YLFPHelper();//指纹管理类
            FingerPrintDBSer fingerPrintDBSer=new  FingerPrintDBSer(context);//保存到数据库

            for (int i=0;i<list.size();i++){
                Log.e(TAG,list.get(i));
                FingerPrint fingerPrint=new FingerPrint();//设置好插入的各种参数
                fingerPrint.setEmpNum(ipEmpNum);
                fingerPrint.setFingerType(String.valueOf(i));
                fingerPrint.setFinger(list.get(i));
                ylfpHelper.SaveFp(fingerPrint,fingerPrintDBSer);

            }

            Message msg = new Message();
            Bundle data = new Bundle();
            data.putInt("size", list.size());
            msg.setData(data);

            if(list.size()>0){
                msg.what=20;
            }else {
                msg.what=21;
            }

            handler.sendMessage(msg);
        }
    };

}
