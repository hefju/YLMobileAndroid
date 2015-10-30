package ylescort.ylmobileandroid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import TaskClass.Box;
import YLAdapter.YLValutboxitemAdapter;

public class vault_tmp_scan extends ActionBarActivity implements View.OnClickListener {

    private TextView vaulttmp_scan_tv_title;
    private ListView vaulttmp_scan_listview;
    private Button vaulttmp_scan_btn_scan;
    private Button vaulttmp_scan_btn_upload;

    private Scan1DRecive Scan1DRecive;

    private YLValutboxitemAdapter ylValutboxitemAdapter;
    private int colorbule,colorred;

    private List<Box> AllBoxList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vault_tmp_scan);
        InitView();
        InitData();
        InitScan();
    }

    private void InitData() {
        colorbule = getResources().getColor(R.color.androidblued);
        colorred = getResources().getColor(R.color.androidredl);
        AllBoxList = new ArrayList<>();
        DisPlayBoxlistAdapter(AllBoxList);
    }

    private void InitScan() {
        Scan1DRecive = new Scan1DRecive();
        IntentFilter filter = new IntentFilter();
        filter.addAction("ylescort.ylmobileandroid.vault_tmp_scan");
        registerReceiver(Scan1DRecive, filter);
        Intent start = new Intent(vault_tmp_scan.this,Scan1DService.class);
        vault_tmp_scan.this.startService(start);
    }

    private void DisPlayBoxlistAdapter(List<Box> boxList){
        if (boxList != null && boxList.size()>0){
            ylValutboxitemAdapter = new YLValutboxitemAdapter(getApplicationContext()
                    ,boxList,R.layout.vault_in_detail_boxitem);
            vaulttmp_scan_listview.setAdapter(ylValutboxitemAdapter);
        }
    }


    private class Scan1DRecive extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String recivedata = intent.getStringExtra("result");
            if (recivedata != null) {
            }
        }
    }

    private void Scan1DCmd(String cmd) {
        String activity = "ylescort.ylmobileandroid.vault_tmp_scan";
        Intent ac = new Intent();
        ac.setAction("ylescort.ylmobileandroid.Scan1DService");
        ac.putExtra("activity", activity);
        sendBroadcast(ac);
        Intent sendToservice = new Intent(vault_tmp_scan.this, Scan1DService.class); // 用于发送指令
        sendToservice.putExtra("cmd", cmd);
        this.startService(sendToservice); // 发送指令
    }

    private void InitView() {
        vaulttmp_scan_tv_title = (TextView) findViewById(R.id.vaulttmp_scan_tv_title);
        vaulttmp_scan_listview = (ListView) findViewById(R.id.vaulttmp_scan_listview);
        vaulttmp_scan_btn_scan = (Button) findViewById(R.id.vaulttmp_scan_btn_scan);
        vaulttmp_scan_btn_upload = (Button) findViewById(R.id.vaulttmp_scan_btn_upload);

        vaulttmp_scan_btn_scan.setOnClickListener(this);
        vaulttmp_scan_btn_upload.setOnClickListener(this);

        vaulttmp_scan_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.vaulttmp_scan_btn_scan:
                Scan1D();
                break;
            case R.id.vaulttmp_scan_btn_upload:
                break;
        }
    }

    private void Scan1D() {
        if (vaulttmp_scan_btn_scan.getText().equals("扫描")) {
            Scan1DCmd("toscan100ms");
            vaulttmp_scan_btn_scan.setBackgroundColor(colorred);
            vaulttmp_scan_btn_scan.setText("停止");
        }else{
            Scan1DCmd("stopscan");
            vaulttmp_scan_btn_scan.setBackgroundColor(colorbule);
            vaulttmp_scan_btn_scan.setText("扫描");
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode){
            case 131:
                Scan1D();
                break;
            case 132:
                break;
            case 4:
                finish();
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        if (Scan1DRecive != null){
            unregisterReceiver(Scan1DRecive);
        }
        Scan1DCmd("stopscan");
        super.onDestroy();
    }

}
