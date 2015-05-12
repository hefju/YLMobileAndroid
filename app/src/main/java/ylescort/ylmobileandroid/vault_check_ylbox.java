package ylescort.ylmobileandroid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import TaskClass.Box;
import YLDataService.YLBoxScanCheck;
import YLSystemDate.YLSystem;


public class vault_check_ylbox extends ActionBarActivity implements View.OnClickListener {

    private ListView vault_check_lv;
    private Button vault_check_btn_scan;
    private Button vault_check_btn_conFirm;

    private Scan1DRecive scan1DRecive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vault_check_ylbox);
        InitView();
        InitReciveScan1D();
    }


    private void InitView() {
        vault_check_lv = (ListView)findViewById(R.id.vault_check_lv);
        vault_check_btn_scan = (Button)findViewById(R.id.vault_check_btn_scan);
        vault_check_btn_conFirm = (Button)findViewById(R.id.vault_check_btn_conFirm);

        vault_check_btn_scan.setOnClickListener(this);
        vault_check_btn_conFirm.setOnClickListener(this);
        vault_check_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    private void InitReciveScan1D() {
        scan1DRecive = new Scan1DRecive();
        IntentFilter filter = new IntentFilter();
        filter.addAction("ylescort.ylmobileandroid.vault_check_ylbox");
        registerReceiver(scan1DRecive, filter);
        Intent start = new Intent(vault_check_ylbox.this,Scan1DService.class);
        vault_check_ylbox.this.startService(start);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_vault_check_ylbox, menu);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.vault_check_btn_scan:Scan1DCmd();
                break;
            case R.id.vault_check_btn_conFirm:ConFirm();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode){
            case 131:Scan1DCmd();
                break;
            case 132:ConFirm();
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void ConFirm() {

    }


    private void Scan1DCmd() {

        String cmd ;
        if (vault_check_btn_scan.getText().equals("扫描")){
            vault_check_btn_scan.setText("停止");
            cmd = "toscan100ms";
        }else {
            vault_check_btn_scan.setText("扫描");
            cmd = "stopscan";
        }

        String activity = "ylescort.ylmobileandroid.vault_check_ylbox";
        Intent ac = new Intent();
        ac.setAction("ylescort.ylmobileandroid.Scan1DService");
        ac.putExtra("activity", activity);
        sendBroadcast(ac);
        Intent sendToservice = new Intent(vault_check_ylbox.this, Scan1DService.class);
        sendToservice.putExtra("cmd", cmd);
        this.startService(sendToservice);
    }

    private class Scan1DRecive extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String recivedata = intent.getStringExtra("result");
            if (recivedata != null){
                Box box= YLBoxScanCheck.CheckBox(recivedata, getApplicationContext());
                GetBoxToListView(box);}
        }
    }

    private void GetBoxToListView(Box box) {
        Log.e(YLSystem.getKimTag(),box.BoxName);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(scan1DRecive);
        super.onDestroy();
    }
}
