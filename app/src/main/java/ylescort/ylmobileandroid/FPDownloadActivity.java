package ylescort.ylmobileandroid;

import android.content.Context;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import ScanFP.YLFPHelper;
import TaskClass.FingerPrint;
import TaskClass.User;
import TaskClass.YLTask;
import YLDataService.EmpDBSer;
import YLDataService.FingerPrintDBSer;
import YLDataService.WebService;
import YLWebService.YLWebService;

//登录前请先下载指纹,因为指纹库不会全部下载到本地
public class FPDownloadActivity extends ActionBarActivity implements View.OnClickListener{

    TextView txtInfo; //提示信息
    EditText txtEmpNum; //员工编号
    Button btnDownload;//下载按钮
    YLFPHelper ylfpHelper=new YLFPHelper();
    android.os.Handler mHandler; //消息处理

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fpdownload);
        InitLayout();

    }

    private void InitLayout() {
        txtEmpNum = (EditText) findViewById(R.id.txtEmpNum);
        txtInfo = (TextView) findViewById(R.id.txtInfo);

        btnDownload = (Button) findViewById(R.id.btnDownload);
        btnDownload.setOnClickListener(this);
        asyncMessage();
    }

    private void asyncMessage() {
        //用于回传数据更新UI
        mHandler = new android.os.Handler(){
            @Override
            public void handleMessage(Message msg) {

                //String content = (String) msg.obj;
                switch (msg.what) {
                    case 1:
                        break;
                    case 20: //获取GetTaskList成功
                        txtInfo.setText(txtEmpNum.getText().toString()+" 指纹下载成功");
                        txtEmpNum.setText("");
                        txtEmpNum.setFocusable(true);
//                        List<YLTask> lstYLTask  = (List<YLTask>) msg.obj;
//                        UpdateLocalTaskList(lstYLTask);
////                        tasksManager.Merge(GetCalendarViewTime(),lstYLTask);
////                        FillData(lstYLTask);
//                        DisplayTaskList(tasksManager.lstLatestTask);
//                        Task_btn_refresh.setEnabled(true);//可以再次点击刷新了
                        break;
                    case 21://获取GetTaskList失败, 服务器返回值不等于1, 获取数据失败
                        txtInfo.setText("指纹下载失败");
//                        String content = (String) msg.obj;
//                        Toast.makeText(getApplicationContext(),content,Toast.LENGTH_LONG).show();
//                        Task_btn_refresh.setEnabled(true);//可以再次点击刷新了
                        break;
                    case 100:
                        break;
                    default:
                        break;
                }
                super.handleMessage(msg);
            }
        };
    }

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
        if(empNum==null||empNum.equals("")){
            txtInfo.setText("请输入员工编号");
            return;
        }

        //检查本地数据库是否存在指纹
        FingerPrintDBSer fingerPrintDBSer=new  FingerPrintDBSer(context);
        FingerPrint fingerPrint=new FingerPrint();
        fingerPrint.setEmpNum(empNum);
        boolean isExists= fingerPrintDBSer.Exists(fingerPrint);
        if(isExists){
            txtInfo.setText("员工指纹已经存在");
            return;
        }

        //从网络上下载指纹
      //  WebService.GetTaskList(getApplicationContext(), mHandler);
        EmpDBSer empDBSer=new EmpDBSer(context);
        User user= empDBSer.GetUserByEmpId(empNum);
        if(user==null){
            txtInfo.setText("员工不存在");
            return;
        }
        String empid=user.getEmpID();
        YLWebService ylWebService = new YLWebService();
        List<String> list = ylWebService.GetEmpFingerPrints(context, empid, "unitest", "1");
        if(list.size()>0){
            txtInfo.setText("指纹下载成功.");
            return;
        }
    }
}
