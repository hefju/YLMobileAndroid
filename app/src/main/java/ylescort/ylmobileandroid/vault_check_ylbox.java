package ylescort.ylmobileandroid;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import TaskClass.Box;
import TaskClass.User;
import TaskClass.YLTask;
import YLAdapter.YLVaultcheckboxAdapter;
import YLDataService.WebService;
import YLDataService.YLBoxScanCheck;
import YLSystemDate.YLEditData;
import YLSystemDate.YLMediaPlayer;
import YLSystemDate.YLSystem;


public class vault_check_ylbox extends ActionBarActivity implements View.OnClickListener {

    private ListView vault_check_lv;
    private Button vault_check_btn_scan;
    private Button vault_check_btn_conFirm;
    private Button vault_check_btn_basedep;
    private TextView vault_check_tv_statistics;

    private Scan1DRecive scan1DRecive;
    private List<Box> boxList;

    private YLMediaPlayer ylMediaPlayer;

    private List<Box> Allboxlist;

    private YLVaultcheckboxAdapter ylVaultcheckboxAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vault_check_ylbox);
        try {
            InitView();
            InitData();
            InitReciveScan1D();
        }catch (Exception e){
            vault_check_btn_basedep.setEnabled(false);
            e.printStackTrace();
        }

    }

    private void InitData() throws  Exception{
        boxList = new ArrayList<>();
        Allboxlist = new ArrayList<>();
//        WebService webService = new WebService();
//        User user = new User();
//        user = YLSystem.getUser();
//        user.setISWIFI(vault_check_btn_basedep.getText().toString());
//        Allboxlist = webService.GetAllBox(user, getApplicationContext());
//        Log.e(YLSystem.getKimTag(), Allboxlist.size() + "");
//        if (Allboxlist.size()<2){
//            new AlertDialog.Builder(vault_check_ylbox.this).setTitle("提示")
//                    .setMessage("未从服务器获取数据\r\n请检查网络连接良好后再开启")
//                    .setPositiveButton("确定", null).show();
//            vault_check_btn_basedep.setEnabled(false);
//        }
        ylMediaPlayer = new YLMediaPlayer();
    }


    private void InitView() {
        vault_check_lv = (ListView)findViewById(R.id.vault_check_lv);
        vault_check_btn_scan = (Button)findViewById(R.id.vault_check_btn_scan);
        vault_check_btn_conFirm = (Button)findViewById(R.id.vault_check_btn_conFirm);
        vault_check_btn_basedep = (Button)findViewById(R.id.vault_check_btn_basedep);
        vault_check_tv_statistics = (TextView)findViewById(R.id.vault_check_tv_statistics);

        vault_check_btn_scan.setOnClickListener(this);
        vault_check_btn_conFirm.setOnClickListener(this);
        vault_check_btn_basedep.setOnClickListener(this);

        vault_check_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        vault_check_btn_scan.setEnabled(false);
        vault_check_btn_conFirm.setEnabled(false);

        vault_check_tv_statistics.setText("总计: 0 个");
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
            case R.id.vault_check_btn_scan:
                if (vault_check_btn_scan.getText().equals("扫描")){
                vault_check_btn_scan.setText("停止");
                Scan1DCmd("toscan100ms");
            }else {
                vault_check_btn_scan.setText("扫描");
                Scan1DCmd("stopscan");
            }
                break;
            case R.id.vault_check_btn_conFirm:ConFirm();
                break;
            case R.id.vault_check_btn_basedep:GetBaseDepartment();
                break;
        }
    }

    private void GetBaseDepartment() {
        new AlertDialog.Builder(this).setTitle("请选择基地").setIcon(android.R.drawable.ic_dialog_info)
                .setSingleChoiceItems(R.array.basedepartment, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:vault_check_btn_basedep.setText("南海基地");
                                break;
                            case 1:vault_check_btn_basedep.setText("大良基地");
                                break;
                            case 2:vault_check_btn_basedep.setText("乐从基地");
                                break;
                            case 3:vault_check_btn_basedep.setText("三水基地");
                                break;
                        }
                        vault_check_btn_scan.setEnabled(true);
                        vault_check_btn_conFirm.setEnabled(true);
                        dialog.dismiss();
                    }
                }).show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode){
            case 131: if (vault_check_btn_scan.getText().equals("扫描")){
                vault_check_btn_scan.setText("停止");
               Scan1DCmd("toscan100ms");
            }else {
                vault_check_btn_scan.setText("扫描");
                Scan1DCmd("stopscan");
            }
                break;
            case 132:ConFirm();
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void ConFirm(){
        AlertDialog.Builder builder = new AlertDialog.Builder(vault_check_ylbox.this);
        builder.setMessage("是否确认上传数据");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    WebService webService = new WebService();
                    User user = new User();
                    user = YLSystem.getUser();
                    user.setISWIFI("0");
                    YLTask ylTask = new YLTask();
                    ylTask.setTaskATMBeginTime(vault_check_btn_basedep.getText().toString());//盘库基地
                    ylTask.setTaskATMEndTime(boxList.size()+"");//盘库数量
                    ylTask.setLstBox(boxList);
                    //ylTask.setTaskState(YLEditData.getDatePick().toString());
                    YLEditData.setYlTask(ylTask);
                    webService.PostCheckVaultboxlist(YLSystem.getUser(), getApplicationContext());
                    vault_check_ylbox.this.finish();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }


    private void Scan1DCmd( String cmd ) {
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
                Box box= CheckBox(recivedata);
                GetBoxToListView(box);}
        }
    }

    private Box CheckBox(String recivedata ){
        boolean getboxtof = false;
        Box getbox = new Box();
        if (Allboxlist.size()>1){
        for (int i = 0; i < Allboxlist.size();i++){
            Box box = Allboxlist.get(i);
            if (box.getBoxID().equals(recivedata)){
               getbox = box;
                getboxtof = true;
                break;
            }
        }
        }else {
            getboxtof = true;
        }
        if (getboxtof){
            getbox= YLBoxScanCheck.CheckBox(recivedata, getApplicationContext());
        }
        return getbox;
    }

    private void GetBoxToListView(Box box) {
        try {
            if (box.getBoxName().equals("illegalbox") || box.getBoxName().equals("无数据")) {
                ylMediaPlayer.SuccessOrFailMidia("faile", getApplicationContext());
                return;
            }
            boolean boxcheck = true;
            int boxcount = boxList.size() - 1;
            for (int i = 0; i < boxList.size(); i++) {
                if (box.getBoxName().equals(boxList.get(boxcount - i).getBoxName())) {
                    boxcheck = false;
                    ylMediaPlayer.SuccessOrFailMidia("faile", getApplicationContext());
                    break;
                }
            }
            if (boxcheck) {
                boxList.add(box);
                vault_check_tv_statistics.setText("总计:" + boxList.size() + "个");
                DisplayYLBox(boxList);
                ylMediaPlayer.SuccessOrFailMidia("success", getApplicationContext());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void DisplayYLBox(List<Box> boxList) {
        if (boxList.size()< 1)return;
        if (boxList.size() == 1){
             ylVaultcheckboxAdapter =
                    new YLVaultcheckboxAdapter(this,boxList,R.layout.vault_check_ylboxitem);
            vault_check_lv.setAdapter(ylVaultcheckboxAdapter);
        }else {
            ylVaultcheckboxAdapter.notifyDataSetChanged();
        }
        scrollMyListViewToBottom();
    }


    private void scrollMyListViewToBottom() {
        vault_check_lv.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                vault_check_lv.setSelection(boxList.size() - 1);
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (scan1DRecive != null) {
            Scan1DCmd("stopscan");
            unregisterReceiver(scan1DRecive);
        }
        super.onDestroy();
    }
}
