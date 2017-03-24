package ylescort.ylmobileandroid;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import TaskClass.ATMBox;
import YLDataService.ATMBoxDBSer;
import YLSystemDate.YLMediaPlayer;

public class ATMBoxCheck extends YLBaseScanActivity implements View.OnClickListener {

    private TextView atmcheck_clientname;
    private TextView atmcheck_useclient;
    private TextView atmcheck_name;
    private TextView atmcheck_brand;
    private TextView atmcheck_type;
    private TextView atmcheck_value;
    private TextView atmcheck_passageway;
    private TextView atmcheck_code;
    private TextView atmcheck_tv_info;

    private Button atmcheck_btn_scan;
    private Button atmcheck_btn_clean;
    private Button atmcheck_btn_black;

    private ATMBoxDBSer atmBoxDBSer;
    private YLMediaPlayer ylMediaPlayer;

    private HashSet<ATMBox> atmBoxList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atmbox_check);
        InitLayout();
        InitData();
    }

    public void HandSetHotKey(int arg) {
        switch (arg){
            case 131: Scan1DCmd(1);
                break;
            case 132: Scan1DCmd(1);
                break;
        }
    }

    @Override
    protected void InitLayout() {
        atmcheck_clientname = (TextView) findViewById(R.id.atmcheck_clientname);
        atmcheck_useclient = (TextView) findViewById(R.id.atmcheck_useclient);
        atmcheck_name = (TextView) findViewById(R.id.atmcheck_name);
        atmcheck_brand = (TextView) findViewById(R.id.atmcheck_brand);
        atmcheck_type = (TextView) findViewById(R.id.atmcheck_type);
        atmcheck_value = (TextView) findViewById(R.id.atmcheck_value);
        atmcheck_passageway = (TextView) findViewById(R.id.atmcheck_passageway);
        atmcheck_code = (TextView) findViewById(R.id.atmcheck_code);
        atmcheck_tv_info = (TextView) findViewById(R.id.atmcheck_tv_info);
        atmcheck_btn_scan = (Button) findViewById(R.id.atmcheck_btn_scan);
        atmcheck_btn_clean = (Button) findViewById(R.id.atmcheck_btn_clean);
        atmcheck_btn_black = (Button) findViewById(R.id.atmcheck_btn_black);

        atmcheck_btn_scan.setOnClickListener(this);
        atmcheck_btn_clean.setOnClickListener(this);
        atmcheck_btn_black.setOnClickListener(this);

    }

    @Override
    protected void InitData() {
        atmBoxDBSer = new ATMBoxDBSer(getApplicationContext());
        ylMediaPlayer = new YLMediaPlayer(getApplicationContext());
        atmBoxList= new HashSet<>();
    }

    @Override
    public void YLPutdatatoList(String recivedata) {
        ATMBox atmBox = new ATMBox(atmBoxDBSer.GetATMboxinfo(recivedata));
        if (atmBox.getClientID().equals("0")) {
            ylMediaPlayer.SuccessOrFail(false);
        } else {
            ShowATMBoxInfo(atmBox);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.atmcheck_btn_scan :
                Scan1DCmd(1);
                break;
            case R.id.atmcheck_btn_clean :
                CleanDetail();
                break;
            case R.id.atmcheck_btn_black :
                this.finish();
                break;
        }
    }

    private void CleanDetail() {
        atmcheck_clientname.setText("");
        atmcheck_useclient.setText("");
        atmcheck_code.setText("");
        atmcheck_name.setText("");
        atmcheck_brand.setText("");
        atmcheck_value.setText("");
        atmcheck_passageway.setText("");
        atmcheck_type.setText("");
    }

    private void ShowATMBoxInfo(ATMBox atmBox) {
        atmcheck_clientname.setText(atmBox.getClientName());
        atmcheck_useclient.setText(atmBox.getUseClientName());
        atmcheck_code.setText(atmBox.getBoxCode());
        atmcheck_name.setText(atmBox.getBoxName());
        atmcheck_brand.setText(atmBox.getBoxBrand());
        atmcheck_value.setText(atmBox.getBoxvalue());
        atmcheck_passageway.setText(atmBox.getPassageway());
        atmcheck_type.setText(atmBox.getBoxtype());

        atmBoxList.add(atmBox);

        String scan ="已盘点 "+ atmBoxList.size()+" 个钞盒";

        atmcheck_tv_info.setText(scan);

        ylMediaPlayer.SuccessOrFail(true);
    }
}
