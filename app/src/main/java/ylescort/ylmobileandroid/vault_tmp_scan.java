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
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import TaskClass.Box;
import TaskClass.YLTask;
import YLAdapter.YLValutboxitemAdapter;
import YLDataService.WebServerTmpValutInorOut;
import YLDataService.YLBoxScanCheck;
import YLSystemDate.YLEditData;
import YLSystemDate.YLMediaPlayer;
import YLSystemDate.YLSysTime;
import YLSystemDate.YLSystem;

public class vault_tmp_scan extends YLBaseScanActivity implements View.OnClickListener {

    private TextView vaulttmp_scan_tv_title;
    private ListView vaulttmp_scan_listview;
    private Button vaulttmp_scan_btn_scan;
    private Button vaulttmp_scan_btn_upload;

    private Scan1DRecive Scan1DRecive;

    private YLValutboxitemAdapter ylValutboxitemAdapter;
    private int colorbule,colorred;
    private String TimeID;
    private int  TaskTimeID;
    private YLTask ylTask;
    private WebServerTmpValutInorOut webServerTmpValutInorOut;

    private List<Box> AllBoxList;
    private YLMediaPlayer ylMediaPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vault_tmp_scan);
        try {
            InitLayout();
            InitData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void InitLayout() {
        vaulttmp_scan_tv_title = (TextView) findViewById(R.id.vaulttmp_scan_tv_title);
        vaulttmp_scan_listview = (ListView) findViewById(R.id.vaulttmp_scan_listview);
        vaulttmp_scan_btn_scan = (Button) findViewById(R.id.vaulttmp_scan_btn_scan);
        vaulttmp_scan_btn_upload = (Button) findViewById(R.id.vaulttmp_scan_btn_upload);

        vaulttmp_scan_btn_scan.setOnClickListener(this);
        vaulttmp_scan_btn_upload.setOnClickListener(this);

        vaulttmp_scan_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                ListView listView = (ListView) adapterView;
//                Box box = (Box) listView.getItemAtPosition(i);
//                ShowDelete(box,i);
//                ylValutboxitemAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void InitData() throws Exception {
        webServerTmpValutInorOut = new WebServerTmpValutInorOut(getApplicationContext());
        colorbule = getResources().getColor(R.color.androidblued);
        colorred = getResources().getColor(R.color.androidredl);
        AllBoxList = new ArrayList<>();
        AllBoxList.clear();
        ylMediaPlayer = new YLMediaPlayer();
        try {
            TimeID = YLEditData.getTimeID();
            ylTask = YLEditData.getYlTask();
            TaskTimeID =Integer.parseInt(ylTask.getTaskVersion());
            if (TimeID.equals("2")) {
                AllBoxList = webServerTmpValutInorOut.GetTmpBoxList
                        (ylTask.getTaskID(), TimeID,YLSystem.getBaseName(),ylTask.getTaskVersion());
                Log.e(YLSystem.getKimTag(),AllBoxList.toString());
                this.setTitle("临时入库扫描");
            }else{
                this.setTitle("临时出库扫描");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        DisPlayBoxlistAdapter(AllBoxList);
    }

    @Override
    public void YLPutdatatoList(String recivedata) {
        if (TimeID.equals("2")){
            AddBoxToAllboxTimeID2(recivedata);
        }else {
            AddBoxToAllboxTimeID1(recivedata);
        }
    }


    private void DisPlayBoxlistAdapter(List<Box> boxList){
        ylValutboxitemAdapter = new YLValutboxitemAdapter(getApplicationContext()
                , boxList, R.layout.vault_in_detail_boxitem);
        vaulttmp_scan_listview.setAdapter(ylValutboxitemAdapter);
    }


    private void AddBoxToAllboxTimeID1(String recivedata) {
        if (recivedata.length() != 10) return;
        boolean addbox = true;
        try {
            for (int i = 0; i < AllBoxList.size(); i++) {
                if (AllBoxList.get(i).getBoxID().equals(recivedata)) {
                    addbox = false;
                }
            }
            if (addbox) {
                Box box = YLBoxScanCheck.CheckBoxbyUHF(recivedata, getApplicationContext());
                if (box.getBoxID().equals("0")) return;
                box.setTimeID(TimeID);
                box.setBaseValutIn(YLSystem.getBaseName());
                box.setTradeAction("出");
                box.setActionTime(YLSysTime.GetStrCurrentTime());
                box.setTaskTimeID(TaskTimeID);
                ylMediaPlayer.SuccessOrFailMidia("success", getApplicationContext());
                AllBoxList.add(box);
            }
            GatherAllBoxList(AllBoxList,"out");
            ylValutboxitemAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void AddBoxToAllboxTimeID2(String recivedata) {
        if (recivedata.length() != 10)return;
        boolean addbox =true  ;

        for (int i = 0; i < AllBoxList.size(); i++) {
            Box box = AllBoxList.get(i);
            if (box.getBoxID().equals(recivedata)){
                box.setValutcheck("对");
                box.setTradeAction("入");
                box.setActionTime(YLSysTime.GetStrCurrentTime());
                box.setBaseValutIn(YLSystem.getBaseName());
                box.setTimeID(TimeID);
                box.setTaskTimeID(TaskTimeID);
                addbox = false;
                ylMediaPlayer.SuccessOrFailMidia("success", getApplicationContext());
            }
        }
        if (addbox){
            ylMediaPlayer.SuccessOrFailMidia("fail", getApplicationContext());
        }
        GatherAllBoxList(AllBoxList,"in");
        ylValutboxitemAdapter.notifyDataSetChanged();

    }

    private void GatherAllBoxList(List<Box> boxes,String inorout){
        if (inorout.equals("in")){
            int count = 0;
            for (Box box : boxes) {
                if (box.getValutcheck() != null){
                    count ++;
                }
            }
            String  sql = "扫描数量："+count;
            vaulttmp_scan_tv_title.setText(sql);

        }else if (inorout.equals("out")){
            String sql = "扫描数量："+boxes.size();
            vaulttmp_scan_tv_title.setText(sql);
        }
    }


    private void ShowDelete(Box box, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(vault_tmp_scan.this);
        builder.setMessage("确认删除"+box.getBoxName()+"?");
        builder.setTitle("提示");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AllBoxList.remove(position);
            }
        });
        builder.create().show();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.vaulttmp_scan_btn_scan:
                Scan1D();
                break;
            case R.id.vaulttmp_scan_btn_upload:
                UpLoad();
                break;
        }
    }

    private void UpLoad() {
        AlertDialog.Builder builder = new AlertDialog.Builder(vault_tmp_scan.this);
        builder.setMessage("确认上传吗?");
        builder.setTitle("提示");
        builder.setPositiveButton("上传", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try {
                    YLTask task = new YLTask();
                    task.setTaskDate(ylTask.getTaskDate());
                    task.setLine(ylTask.getLine());
                    task.setTaskID(ylTask.getTaskID());
                    task.setTaskType(ylTask.getTaskType());
                    task.setTaskManager(ylTask.getTaskManager());
                    task.setTaskManagerNo(ylTask.getTaskManagerNo());
                    task.setLstBox(AllBoxList);
                    YLEditData.setYlTask(task);
                    String upload = webServerTmpValutInorOut.UploadTmpValut();
                    if (upload.equals("1")) {
                        AllBoxList.clear();
                        ylValutboxitemAdapter.notifyDataSetChanged();
                        vaulttmp_scan_tv_title.setText("");
                        Toast.makeText(getApplicationContext(), "上传成功", Toast.LENGTH_SHORT).show();
                    }
                    dialogInterface.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.create().show();

    }

    private void Scan1D() {
        if (vaulttmp_scan_btn_scan.getText().equals("扫描/F1")) {
            Scan1DCmd(2);
            vaulttmp_scan_btn_scan.setBackgroundColor(colorred);
            vaulttmp_scan_btn_scan.setText("停止/F1");
        }else{
            Scan1DCmd(0);
            vaulttmp_scan_btn_scan.setBackgroundColor(colorbule);
            vaulttmp_scan_btn_scan.setText("扫描/F1");
        }
    }

    @Override
    public void HandSetHotKey(int keyCode) {
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
        super.HandSetHotKey(keyCode);
    }
}
