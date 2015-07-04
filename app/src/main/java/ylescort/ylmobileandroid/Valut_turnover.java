package ylescort.ylmobileandroid;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import TaskClass.Box;
import TaskClass.User;
import TaskClass.YLTask;
import YLAdapter.YLBoxEdiAdapter;
import YLAdapter.YLValutboxitemAdapter;
import YLDataService.AnalysisBoxList;
import YLDataService.WebServerValutturnover;
import YLDataService.YLBoxScanCheck;
import YLSystemDate.YLEditData;
import YLSystemDate.YLSysTime;
import YLSystemDate.YLSystem;

public class Valut_turnover extends ActionBarActivity implements View.OnClickListener {

    private ListView vault_turnover_listview;
    private TextView vault_turnover_tv_title;
    private Button vault_turnover_btn_vaultin;
    private Button vault_turnover_btn_vaultout;
    private Button vault_turnover_btn_scan;
    private Button vault_turnover_btn_upload;

    private YLTask vaultinylTask;
    private YLTask vaultoutylTask;

    private String PickDate;
    private List<Box> valutoutboxList;
    private List<Box> valutinboxList;
    private List<Box> DisplayboxList;

    private WebServerValutturnover webServerValutturnover;
    private Scan1DRecive vaultindetailscan1DRecive;

    private YLValutboxitemAdapter ylBoxEdiAdapter;

    private String InBaseName;
    private String OutBaseName;
    private String BoxOper;

    private User user;

    private MediaPlayer mPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_valut_turnover);
        try {
            InitView();
            InitData();
            InitReciveScan1D();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void InitReciveScan1D() {
        vaultindetailscan1DRecive = new Scan1DRecive();
        IntentFilter filter = new IntentFilter();
        filter.addAction("ylescort.ylmobileandroid.Valut_turnover");
        registerReceiver(vaultindetailscan1DRecive, filter);
        Intent start = new Intent(Valut_turnover.this,Scan1DService.class);
        Valut_turnover.this.startService(start);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.vault_turnover_btn_vaultin:VaultIn();
                break;
            case R.id.vault_turnover_btn_vaultout:VaultOut();
                break;
            case R.id.vault_turnover_btn_scan:Scan1DCmd("toscan100ms");
                break;
            case R.id.vault_turnover_btn_upload:UpLoadDialog();
                break;
        }
    }


    private void UpLoadDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(Valut_turnover.this);
        builder.setMessage("确认上传吗?");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                UploadData();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void UploadData() {
        try {
            vaultoutylTask.setTaskDate(PickDate);
            vaultoutylTask.setLstBox(DisplayboxList);
            YLEditData.setYlTask(vaultoutylTask);
            webServerValutturnover.Valutturnoverupload(YLSystem.getUser(), getApplicationContext());
            DisplayboxList.clear();
            ylBoxEdiAdapter.notifyDataSetChanged();
            vault_turnover_btn_vaultout.setEnabled(true);
            vault_turnover_btn_vaultin.setEnabled(true);
            Valut_turnover.this.setTitle("未设置出入库操作");
            BoxOper = "0";
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void VaultOut() {
        UploadData();
        new AlertDialog.Builder(this).setTitle("请选择基地").setIcon(android.R.drawable.ic_dialog_info)
                .setSingleChoiceItems(R.array.basedepartment, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String choicbase = "";
                        switch (which) {
                            case 0:
                                choicbase = "南海基地";
                                break;
                            case 1:
                                choicbase = "大良基地";
                                break;
                            case 2:
                                choicbase = "乐从基地";
                                break;
                            case 3:
                                choicbase = "三水基地";
                                break;
                        }
                        if (OutBaseName.equals(choicbase)) {
                            Toast.makeText(getApplicationContext(), "不能选择本基地"
                                    , Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            return;
                        }
//                        if (InBaseName.equals("")) {
//                            InBaseName = choicbase;
//                        } else  {
//                            Toast.makeText(getApplicationContext(), "请上传后再更换基地"
//                                    , Toast.LENGTH_SHORT).show();
//                            dialog.dismiss();
//                            return;
//                        }
                        InBaseName = choicbase;
                        Valut_turnover.this.setTitle("出库目标基地：" + InBaseName);
                        vault_turnover_btn_vaultout.setEnabled(false);
                        vault_turnover_btn_vaultin.setEnabled(true);
                        try {
                            valutoutboxList = webServerValutturnover.VaultTrunoverOutBoxList(OutBaseName, InBaseName,
                                    YLSystem.getUser().getDeviceID(), YLSystem.getUser().getEmpID()
                                    , PickDate, getApplicationContext());
                            if (!valutoutboxList.get(0).getServerReturn().contains("没有")) {
                                DisplayboxList.addAll(valutoutboxList);
                                vault_turnover_tv_title.setText("出库总数: " + valutoutboxList.size());
                            } else {
                                vault_turnover_tv_title.setText("出库总数:0");
                            }
                            BoxOper = "out";

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        dialog.dismiss();
                    }
                }).show();
        ylBoxEdiAdapter.notifyDataSetChanged();
    }

    private void VaultIn() {
        try {
            UploadData();
            Valut_turnover.this.setTitle("当前操作：入库");
            BoxOper = "in";
            valutinboxList = webServerValutturnover.ValutInBoxList(user,getApplicationContext());
            if (valutinboxList.get(0).getBoxID() != null){
                for (int i = 0;i<valutinboxList.size();i++){
                    DisplayboxList.add(valutinboxList.get(i));
                }
            }
            AnalyBoxes(DisplayboxList,"in");
            ylBoxEdiAdapter.notifyDataSetChanged();
            vault_turnover_btn_vaultout.setEnabled(true);
            vault_turnover_btn_vaultin.setEnabled(false);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void AnalyBoxes(List<Box> displayboxList, String inorout) {
        if (inorout.equals("in")){
            String intext ="入库列表数为："+ displayboxList.size();
            int correctbox = 0;
            for (int i = 0;i <displayboxList.size();i++){
                Box box = displayboxList.get(i);
                if (box.getValutcheck().equals("对")){
                    correctbox++;
                }
            }
            String correctstr = "库管员扫描正确扫描:"+correctbox;
            vault_turnover_tv_title.setText(intext+"\r\n"+correctstr);
        }else if (inorout.equals("out")){
            String outstr = "出库总数："+displayboxList.size();
            vault_turnover_tv_title.setText(outstr);
        }
    }

    private class Scan1DRecive extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String recivedata = intent.getStringExtra("result");
            if (recivedata != null){
                Box box= YLBoxScanCheck.CheckBox(recivedata, getApplicationContext());
                PutBoxToBoxList(box);
            }
        }
    }

    private void PutBoxToBoxList(Box box) {
        try {
            if (box.getBoxName().equals("illegalbox") || box.getBoxName().equals("无数据")) return;
            boolean checkbox = true;
            if (BoxOper.equals("out")) {
                for (int i = 0; i < DisplayboxList.size(); i++) {
                    if (box.getBoxID().equals(DisplayboxList.get(i).getBoxID())) {
                        checkbox = false;
                        break;
                    }
                }
                if (checkbox) {
                    if (BoxOper.equals("out")) {
                        box.setValutcheck(InBaseName);
                        box.setBaseValutOut(OutBaseName);
                        box.setBaseValutIn(InBaseName);
                        box.setTradeAction("出");
                        box.setTimeID("1");
                        box.setActionTime(YLSysTime.GetStrCurrentTime());
                        DisplayboxList.add(box);
                        YLBoxMediaPlay("success");
                        AnalyBoxes(DisplayboxList, "out");
                        vault_turnover_listview.setSelection(DisplayboxList.size() - 1);
                    }
                }
            } else {
                for (int i = 0; i < DisplayboxList.size(); i++) {
                    Box boxin = new Box();
                    boxin = DisplayboxList.get(i);
                    if (boxin.getBoxID().equals(box.getBoxID())) {
                        boxin.setValutcheck("对");
                        boxin.setTradeAction("入");
                        boxin.setBaseValutOut(boxin.getBaseValutOut());
                        boxin.setTimeID("2");
                        boxin.setActionTime(YLSysTime.GetStrCurrentTime());
                        DisplayboxList.set(i, boxin);
                        YLBoxMediaPlay("success");
                        vault_turnover_listview.setSelection(i);
                        AnalyBoxes(DisplayboxList, "in");
                    }
                }
            }
            ylBoxEdiAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void InitData() throws Exception {
        mPlayer = new MediaPlayer();
        vaultinylTask = new YLTask();
        vaultoutylTask = new YLTask();
        DisplayboxList = new ArrayList<>();
        OutBaseName = YLSystem.getBaseName();
        PickDate = YLSysTime.DateToStr(YLEditData.getDatePick());
        webServerValutturnover = new WebServerValutturnover();
        InBaseName = "";
        user = new User();
        user = YLSystem.getUser();
        user.setServerReturn(OutBaseName);
        user.setTaskDate(PickDate);
        DisplayBoxListAdapter(DisplayboxList);

    }

    private void DisplayBoxListAdapter(List<Box> boxList){
        if (boxList ==null)return;
        ylBoxEdiAdapter = new YLValutboxitemAdapter(this, boxList,R.layout.vault_in_detail_boxitem);
        vault_turnover_listview.setAdapter(ylBoxEdiAdapter);
    }

    private void InitView() {

        vault_turnover_listview = (ListView) findViewById(R.id.vault_turnover_listview);
        vault_turnover_btn_vaultin = (Button) findViewById(R.id.vault_turnover_btn_vaultin);
        vault_turnover_btn_vaultout = (Button) findViewById(R.id.vault_turnover_btn_vaultout);
        vault_turnover_btn_scan = (Button) findViewById(R.id.vault_turnover_btn_scan);
        vault_turnover_btn_upload = (Button) findViewById(R.id.vault_turnover_btn_upload);
        vault_turnover_tv_title = (TextView)findViewById(R.id.vault_turnover_tv_title);

        vault_turnover_btn_vaultin.setOnClickListener(this);
        vault_turnover_btn_vaultout.setOnClickListener(this);
        vault_turnover_btn_scan.setOnClickListener(this);
        vault_turnover_btn_upload.setOnClickListener(this);

        Valut_turnover.this.setTitle("未设置出入库操作");
        vault_turnover_tv_title.setText("未设置出入库操作");
        BoxOper = "0";

        vault_turnover_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                ListView listView = (ListView) parent;
                DeleteBox(position);
            }
        });
    }

    private void DeleteBox(final int postion){
        if (BoxOper.equals("in"))return;
        AlertDialog.Builder builder = new AlertDialog.Builder(Valut_turnover.this);
        builder.setMessage("确认删除吗?");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DisplayboxList.remove(postion);
                ylBoxEdiAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    @Override
    protected void onDestroy() {
        if (vaultindetailscan1DRecive != null){
            unregisterReceiver(vaultindetailscan1DRecive);
        }
        Scan1DCmd("stopscan");
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode){
            case 131:Scan1DCmd("toscan100ms");
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_valut_turnover, menu);
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

    private void Scan1DCmd(String cmd) {

        if (BoxOper.equals("0"))return;

        if (!cmd.equals("stopscan")){
            String sacnbtntext = vault_turnover_btn_scan.getText().toString();
            if (sacnbtntext.equals("扫描/F1")){
                cmd = "toscan100ms";
                vault_turnover_btn_scan.setText("停止/F1");
            }else {
                cmd = "stopscan";
                vault_turnover_btn_scan.setText("扫描/F1");
            }
        }

        String activity = "ylescort.ylmobileandroid.Valut_turnover";
        Intent ac = new Intent();
        ac.setAction("ylescort.ylmobileandroid.Scan1DService");
        ac.putExtra("activity", activity);
        sendBroadcast(ac);
        Intent sendToservice = new Intent(Valut_turnover.this, Scan1DService.class); // 用于发送指令
        sendToservice.putExtra("cmd", cmd);
        this.startService(sendToservice); // 发送指令
    }

    private void YLBoxMediaPlay (String mediavoic)throws Exception{
        if (mediavoic.equals("success")) {
            mPlayer = MediaPlayer.create(Valut_turnover.this, R.raw.msg);
            if (mPlayer.isPlaying()) {
                return;
            }
        } else {
            try {
                mPlayer.setDataSource("/system/media/audio/notifications/Proxima.ogg");  //选用系统声音文件
                mPlayer.prepare();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mPlayer.start();
    }

}
