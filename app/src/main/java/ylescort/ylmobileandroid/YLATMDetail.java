package ylescort.ylmobileandroid;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.hdhe.nfc.NFCcmdManager;

import java.text.SimpleDateFormat;
import java.util.Locale;

import TaskClass.YLATM;
import YLSystemDate.YLEditData;


public class YLATMDetail extends ActionBarActivity {


    private TextView ylsite_tv_sitename;//网点名称
    private Button ylatmdetail_btn_start;//开始加钞
    private TextView ylatmdetail_tv_start;//开始时间
    private Button ylatmdetail_btn_end;//结束加钞
    private TextView ylatmdetail_tv_end;//结束时间
    private TextView ylatmdetail_tv_count;//ATM数量
    private Button ylatmdetail_btn_del1;//减一
    private EditText ylatmdetail_et_atmcount;//输入ATM数量
    private Button ylatmdetail_btn_add1;//加一
    private Button ylatmdetail_btn_enter;//确定
    private Button ylatmdetail_btn_cancel;//取消

    private NFCcmdManager HFmanager ;

    private String readtimeflag;

    private String OperStuat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ylatmdetail);
        InitHFreader();
        InitLayout();
        GetInterString();
    }

    private void InitHFreader() {
        try{
            HFmanager = NFCcmdManager.getNFCcmdManager(13, 115200, 0);
            HFmanager.readerPowerOn();
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "HF初始化失败", Toast.LENGTH_SHORT).show();
        }
    }

    private void GetInterString() {
        Bundle bundle = this.getIntent().getExtras();
        String Name = bundle.getString("EdiOrIns");
        if (Name.equals("Edi")){
            OperStuat ="--编辑";
            ylatmdetail_et_atmcount.setText(YLEditData.getYlatm().ATMCount);
            ylatmdetail_tv_start.setText(YLEditData.getYlatm().getTradeBegin());
            ylatmdetail_tv_end.setText(YLEditData.getYlatm().getTradeEnd());

        }else {
            OperStuat = "--新增";
        }
        ylsite_tv_sitename.setText(YLEditData.getYlatm().getSiteName()+OperStuat);
    }

    private void InitLayout() {
        ylsite_tv_sitename = (TextView)findViewById(R.id.ylsite_tv_sitename);
        ylatmdetail_btn_start = (Button)findViewById(R.id.ylatmdetail_btn_start);
        ylatmdetail_tv_start = (TextView)findViewById(R.id.ylatmdetail_tv_start);
        ylatmdetail_btn_end = (Button)findViewById(R.id.ylatmdetail_btn_end);
        ylatmdetail_tv_end = (TextView)findViewById(R.id.ylatmdetail_tv_end);
        ylatmdetail_tv_count = (TextView)findViewById(R.id.ylatmdetail_tv_count);
        ylatmdetail_btn_del1 = (Button)findViewById(R.id.ylatmdetail_btn_del1);
        ylatmdetail_et_atmcount = (EditText)findViewById(R.id.ylatmdetail_et_atmcount);
        ylatmdetail_btn_add1 = (Button)findViewById(R.id.ylatmdetail_btn_add1);
        ylatmdetail_btn_enter = (Button)findViewById(R.id.ylatmdetail_btn_enter);
        ylatmdetail_btn_cancel = (Button)findViewById(R.id.ylatmdetail_btn_cancel);

        ylatmdetail_btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readtimeflag = "start";
                ReadStartTimeDialog();
            }
        });

        ylatmdetail_btn_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readtimeflag = "end";
                ReadStartTimeDialog();
            }
        });

        ylatmdetail_btn_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ylatmEnter();
            }
        });

        ylatmdetail_btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ylatmCancel();
            }
        });

        ylatmdetail_btn_del1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int atmcount = Integer.parseInt(ylatmdetail_et_atmcount.getText().toString());
                if (atmcount == 0 ) return;
                atmcount --;
                ylatmdetail_et_atmcount.setText(atmcount+"");
            }
        });
        ylatmdetail_btn_add1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int atmcount = Integer.parseInt(ylatmdetail_et_atmcount.getText().toString());
                atmcount++;
                ylatmdetail_et_atmcount.setText(atmcount+"");
            }
        });
    }

    private void ylatmCancel() {
        this.finish();
    }

    private void ylatmEnter() {
        if (OperStuat.equals("--编辑")){
            YLATM ylatm;
            ylatm = YLEditData.getYlatm();
            ylatm.setATMCount(ylatmdetail_et_atmcount.getText()+"");
            ylatm.setTradeBegin(ylatmdetail_tv_start.getText()+"");
            ylatm.setTradeEnd(ylatmdetail_tv_end.getText()+"");
            ylatm.setTradeState("已完成");
            for (int i = 0; i <YLEditData.getYlatmList().size();i++){
                if (YLEditData.getYlatmList().get(i).getSiteID().equals(ylatm.getSiteID())&&
                        YLEditData.getYlatmList().get(i).getTimeID().equals(ylatm.getTimeID())){
                    YLEditData.ylatmList.set(i,ylatm);
                }
            }
        }else {
            YLATM ylatm;
            ylatm = YLEditData.getYlatm();
            ylatm.setATMCount(ylatmdetail_et_atmcount.getText()+"");
            ylatm.setTradeBegin(ylatmdetail_tv_start.getText()+"");
            ylatm.setTradeEnd(ylatmdetail_tv_end.getText()+"");
            ylatm.setTradeState("已完成");
            YLEditData.ylatmList.add(ylatm);
        }
        this.finish();
    }

    private void GetTimeByHF(String startorend) {
        byte[] uid ;
        if (HFmanager.readerPowerOff()){
            HFmanager.readerPowerOn();
        }HFmanager.readerPowerOff();
        HFmanager.init_14443A();
        uid = HFmanager.inventory_14443A();
        if (uid != null){
            if (startorend.equals("start")){
                ylatmdetail_tv_start.setText(GetCurrDateTime("date"));
            }else {
                ylatmdetail_tv_end.setText(GetCurrDateTime("date"));
            }
        }
        HFmanager.readerPowerOff();
    }

    private String GetCurrDateTime(String dort){
        String datetimeformat;
        if (dort.equals("date")){
            datetimeformat = "yyyy-MM-dd HH:mm:ss";
        }else {
            datetimeformat = "HH:mm";
        }
        SimpleDateFormat sDateFormat = new SimpleDateFormat(datetimeformat, Locale.CHINA);
        return sDateFormat.format(new java.util.Date());
    }

    private void ReadStartTimeDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(YLATMDetail.this);
        builder.setMessage("是否确认?");
        builder.setTitle("提示");
        builder.setPositiveButton("确认",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                GetTimeByHF(readtimeflag);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ylatmdetail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
