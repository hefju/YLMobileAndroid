package ylescort.ylmobileandroid;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import TaskClass.Box;
import TaskClass.Site;
import TaskClass.User;
import TaskClass.YLTask;
import YLAdapter.YLValutboxitemAdapter;
import YLDataService.AnalysisBoxList;
import YLDataService.WebServerValutInorOut;
import YLDataService.WebService;
import YLDataService.YLBoxScanCheck;
import YLSystemDate.YLEditData;
import YLSystemDate.YLMediaPlayer;
import YLSystemDate.YLSysTime;
import YLSystemDate.YLSystem;


public class vault_out_detail extends YLBaseScanActivity implements View.OnClickListener {

    private TextView vault_out_detail_tv_taskname;
    private TextView vault_out_detail_tv_boxstaut;
    private TextView vault_out_detail_tv_type;
    private TextView vault_out_detail_tv_boxinfo;
    private LinearLayout vault_out_detail_ll_boxinfo;

    private Button vault_out_detail_btn_readcard;
    private ListView vault_out_detail_lv;
    private Button vault_out_detail_btn_scan1d;
    private Button vault_out_detail_btn_scanuhf;
    private Button vault_out_detail_btn_enter;
    private CheckBox vault_out_detail_cb_tips;


    private List<Box> vaulteroutboxlist ;
    private YLMediaPlayer ylMediaPlayer;
    private List<Box> AllboxList;
    private List<Site> siteList;

    private AnalysisBoxList analysisBoxList;
    private WebServerValutInorOut webServerValutInorOut;
    private YLValutboxitemAdapter ylValutboxitemAdapter;
    private boolean Scanflag;

    private YLTask ylTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vault_out_detail);
        try{
            InitLayout();
            InitData();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void DeleteBoxinList(final int position) {
        Box box = AllboxList.get(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(vault_out_detail.this);
        builder.setMessage("确认删除:\r\n"+box.getBoxName()+"?");
        builder.setTitle("提示");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AllboxList.remove(position);
                Log.e(YLSystem.getKimTag(), AllboxList.toString());
                if (AllboxList.size() == 0) {
                    AllboxList = new ArrayList<Box>();
                }
                ylValutboxitemAdapter.notifyDataSetChanged();
                ShowBoxList();
                dialog.dismiss();
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


    @Override
    protected void InitLayout() {
        vault_out_detail_tv_taskname = (TextView)findViewById(R.id.vault_out_detail_tv_taskname);
        vault_out_detail_tv_boxstaut = (TextView)findViewById(R.id.vault_out_detail_tv_boxstaut);
        vault_out_detail_tv_type = (TextView)findViewById(R.id.vault_out_detail_tv_type);
        vault_out_detail_tv_boxinfo = (TextView)findViewById(R.id.vault_out_detail_tv_boxinfo);
        vault_out_detail_ll_boxinfo = (LinearLayout)findViewById(R.id.vault_out_detail_ll_boxinfo);

        vault_out_detail_btn_readcard = (Button)findViewById(R.id.vault_out_detail_btn_readcard);
        vault_out_detail_lv = (ListView)findViewById(R.id.vault_out_detail_lv);
        vault_out_detail_btn_scan1d = (Button)findViewById(R.id.vault_out_detail_btn_scan1d);
        vault_out_detail_btn_scanuhf = (Button)findViewById(R.id.vault_out_detail_btn_scanuhf);
        vault_out_detail_btn_enter = (Button)findViewById(R.id.vault_out_detail_btn_enter);
        vault_out_detail_cb_tips = (CheckBox) findViewById(R.id.vault_out_detail_cb_tips);

        vault_out_detail_btn_readcard.setOnClickListener(this);
        vault_out_detail_btn_scan1d.setOnClickListener(this);
        vault_out_detail_btn_scanuhf.setOnClickListener(this);
        vault_out_detail_btn_enter.setOnClickListener(this);
        vault_out_detail_cb_tips.setOnClickListener(this);

        vault_out_detail_btn_scan1d.setBackgroundColor(-13388315);
//        vault_out_detail_btn_scanuhf.setBackgroundColor(-13388315);

        vault_out_detail_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListView listView = (ListView) parent;
                Box box = (Box) listView.getItemAtPosition(position);
                //不能修改空实12-10
//                YLBoxchangeType("setbox", position);
                DeleteBoxinList(position);

            }
        });

        vault_out_detail.this.setTitle("出库明细--" + YLSystem.getUser().getName());
    }

    @Override
    protected void InitData() throws Exception {

        vaulteroutboxlist = new ArrayList<>();
        AllboxList = new ArrayList<>();
        ylMediaPlayer = new YLMediaPlayer(getApplicationContext());
        ylTask = YLEditData.getYlTask();
        vault_out_detail_tv_taskname.setText(ylTask.getLine());
        analysisBoxList = new AnalysisBoxList();
        webServerValutInorOut = new WebServerValutInorOut();
        Scanflag = false;

        User user = YLSystem.getUser();
        user.setTaskDate(ylTask.getTaskID());

        AllboxList= webServerValutInorOut.StoreGetBoxByTaskIDOut(user,getApplicationContext());

        if (AllboxList.get(0).getServerReturn().contains("没有出库箱")){
            AllboxList.clear();
        }

        siteList = ylTask.getLstSite();
        if (siteList == null){
            siteList = new ArrayList<>();
        }

        DisPlayBoxlistAdapter(AllboxList);
        Log.e(YLSystem.getKimTag(), AllboxList.size() + " 库内款箱数");
        ShowBoxList();
    }

    @Override
    public void YLPutdatatoList(String recivedata) {
        ScanBoxInListView(recivedata,"1D");
    }


    private void ShowBoxList() {

//        if (AllboxList.size() > 0) {
            List<String> stringList = analysisBoxList.AnsysisBoxListForKeeper2(AllboxList);
            String boxtype = "实款箱：" + stringList.get(0) + "  实卡箱：" + stringList.get(2) + "\r\n实凭证箱：" + stringList.get(4) +
                    " 实凭证袋：" + stringList.get(6);
            String boxstaut = "空款箱：" + stringList.get(1) + "  空卡箱：" + stringList.get(3) + "\r\n空凭证箱：" + stringList.get(5) +
                    " 空凭证袋：" + stringList.get(7);
            Log.e(YLSystem.getKimTag(), stringList.toString() + "上传统计");
            String total = ylTask.getLine() + "\r\n" + "实时扫描数： " + AllboxList.size() + "   ";

            vault_out_detail_tv_taskname.setText(total);
            vault_out_detail_tv_boxstaut.setText(boxtype);
            vault_out_detail_tv_type.setText(boxstaut);
//        }
    }

    private void YLBoxScan1D() {
        if (Scanflag)return;
        if (vault_out_detail_btn_scan1d.getText().equals("扫描")){
            vault_out_detail_btn_scan1d.setBackgroundColor(-30720);
            Scan1DCmd(2);
            vault_out_detail_btn_scan1d.setText("停止");
        }else {
            vault_out_detail_btn_scan1d.setBackgroundColor(-13388315);
            Scan1DCmd(0);
            vault_out_detail_btn_scan1d.setText("扫描");
        }
    }

    private void YLBoxchangeType(final String type, final int position ) {
        new AlertDialog.Builder(this).setTitle("请选择类型").setIcon(android.R.drawable.ic_dialog_info)
                .setSingleChoiceItems(R.array.ylboxfullorempty, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Box box = new Box();
                        box = AllboxList.get(position);
                        switch (which) {
                            case 0:
                                box.setBoxStatus("实");
                                box.setValutcheck("核");
                                AllboxList.set(position, box);
                                break;
                            case 1:
                                box.setBoxStatus("空");
                                box.setValutcheck("核");
                                AllboxList.set(position, box);
                                break;
                            case 2:
                                AllboxList.remove(position);
                                Log.e(YLSystem.getKimTag(), AllboxList.toString());
                                if (AllboxList.size() == 0) {
                                    AllboxList = new ArrayList<Box>();
                                }
                                break;
                        }
//                        DisPlayBoxlistAdapter(AllboxList);
                        ylValutboxitemAdapter.notifyDataSetChanged();
                        ShowBoxList();
                        dialog.dismiss();
                    }
                }).show();
    }

    private void YLBoxEnter() throws Exception {

        AlertDialog.Builder builder = new AlertDialog.Builder(vault_out_detail.this);
        builder.setMessage("是否确认上传数据");
        builder.setTitle("提示");
        builder.setPositiveButton("上传", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    ylTask.setLstBox(AllboxList);
                    YLEditData.setYlTask(ylTask);
                    WebService webService = new WebService();
                    String serreturn =
                            webService.PostVaultInBoxList(YLSystem.getUser(), getApplicationContext());
                    if (serreturn.equals("1")){
                        ylTask.setTaskState("已上传");
                        dialog.dismiss();
//                        vault_out_detail.this.finish();
                        User user = YLSystem.getUser();
                        user.setTaskDate(ylTask.getTaskID());
                        WebServerValutInorOut webServerValutInorOut = new WebServerValutInorOut();
                        AllboxList= webServerValutInorOut.StoreGetBoxByTaskIDOut(user,getApplicationContext());
                        Log.e(YLSystem.getKimTag(),AllboxList.size()+"上传数量");
                        DisPlayBoxlistAdapter(AllboxList);
                        ShowBoxList();
                    }else if (serreturn.equals("2")){
                        Toast.makeText(getApplicationContext(),"该线路已上传，如需覆盖请联系信息技术部",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getApplicationContext(),"未上传数据，请检查网络后再上传",Toast.LENGTH_SHORT).show();
                    }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_vault_out_detail, menu);
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
            case R.id.vault_out_detail_btn_scan1d:
                YLBoxScan1D();
                break;
            case R.id.vault_out_detail_btn_scanuhf:
                ShowBoxInfo();
                break;
            case R.id.vault_out_detail_btn_enter:
                try {
                    if (AllboxList.size()==0)return;
                    YLBoxEnter();
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            case R.id.vault_out_detail_btn_readcard:
//                ReadHFCard();
                if (AllboxList.size()==0)return;
                DeleteBoxinList(AllboxList.size()-1);
                break;
            case R.id.vault_out_detail_cb_tips:
                break;
        }
    }

    private void ShowBoxInfo() {
        if (vault_out_detail_ll_boxinfo.getVisibility() == View.GONE){
            vault_out_detail_ll_boxinfo.setVisibility(View.VISIBLE);
            vault_out_detail_btn_scanuhf.setText("隐藏");
        }else  {
            vault_out_detail_ll_boxinfo.setVisibility(View.GONE);
            vault_out_detail_btn_scanuhf.setText("显示");
        }

    }

    @Override
    public void HandSetHotKey(int keyCode) {

        switch (keyCode) {
            case 131:
                YLBoxScan1D();
                break;
            case 132:
                break;
            case 133:
                YLBoxScan1D();
                break;
            case 134:
                YLBoxScan1D();
                break;
            case 4:
                finish();
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                break;
        }

        super.HandSetHotKey(keyCode);
    }

    private void ScanBoxInListView(String recivedata, String form) {
        if (recivedata.length() !=10)return;
        if (Scanflag)return;
        boolean boxcheck = true;
        if (AllboxList.size() ==0){
            AllboxList.clear();
            boxcheck = true;
        }
        else {
            for (int i = AllboxList.size()-1; i>=0;i--){
                Box listbox = AllboxList.get(i);
                if (listbox.getBoxID().equals(recivedata)){
                    if (form.equals("1D")){
                        vault_out_detail_tv_boxinfo.setText(listbox.getBoxName());
                        ylMediaPlayer.SuccessOrFail(true);
                    }
                    boxcheck = false;
                    break;
                }
            }
        }
        if (boxcheck) {
            Log.e(YLSystem.getKimTag(), AllboxList.size() + "插入数据");
            Box box = YLBoxScanCheck.CheckBoxbyUHF(recivedata, getApplicationContext());
            if (box.getBoxName().equals("无数据"))return;
            box.setActionTime(YLSysTime.GetStrCurrentTime());
            box.setTradeAction("出");
            box.setBoxCount("1");
            box.setServerReturn("1");
            box.setTimeID("1");

            ChcekTaskSite(box);

//            Log.e(YLSystem.getKimTag(), box.toString());
//            vault_out_detail_tv_boxinfo.setText(box.getBoxName());
//            AllboxList.add(box);
//            DisPlayBoxlistAdapter(AllboxList);
//            ylMediaPlayer.SuccessOrFailMidia("success", getApplicationContext());
        }

        ShowBoxList();
    }

    private void ChcekTaskSite(final Box box) {
        boolean line = true;
        for (Site site : siteList) {
            if (site.getSiteID() == box.getSiteID()){
                line = false;
            }
        }
        if (vault_out_detail_cb_tips.isChecked() & line){
            YLBoxScan1D();
            Scanflag = true;
            ylMediaPlayer.SuccessOrFail(false);
            final AlertDialog.Builder builder = new AlertDialog.Builder(vault_out_detail.this);
            builder.setMessage("该款箱不属于任务网点，是否继续添加此线路？");
            builder.setTitle("提示");
            builder.setCancelable(false);
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Scanflag = false;
                    dialogInterface.dismiss();
                }
            });
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    vault_out_detail_tv_boxinfo.setText(box.getBoxName());
                    AllboxList.add(box);
                    DisPlayBoxlistAdapter(AllboxList);
                    ylMediaPlayer.SuccessOrFail(true);
                    Scanflag = false;
                }
            });
            builder.show();

        }else {
            vault_out_detail_tv_boxinfo.setText(box.getBoxName());
            AllboxList.add(box);
            DisPlayBoxlistAdapter(AllboxList);
            ylMediaPlayer.SuccessOrFail(true);
        }
    }

    private Box CheckBox(String recivedata ){
        boolean getboxtof = false;
        Box getbox = new Box();

        for (int i = 0; i < AllboxList.size();i++){
            Box box = AllboxList.get(i);
            if (box.getBoxID().equals(recivedata)){
                getbox = box;
                getbox.setValutcheck("对");
                getboxtof = true;
                break;
            }
        }
        if (!getboxtof){
            getbox= YLBoxScanCheck.CheckBox(recivedata, getApplicationContext());
            getbox.setValutcheck("多");
        }
        return getbox;
    }


    private void AddYLBoxtoListView(Box box) {
        try {
            if (box.getBoxName().equals("illegalbox") || box.getBoxName().equals("无数据")) {
                ylMediaPlayer.SuccessOrFail(false);
                return;
            }

            boolean boxcheck = true;

            Log.e(YLSystem.getKimTag(),AllboxList.toString());
            if (AllboxList.size() ==0){
                AllboxList.clear();
                boxcheck = true;
            }else if (AllboxList.size()==1&AllboxList.get(0).getServerReturn().contains("没有出库箱")){
                AllboxList.clear();
                boxcheck = true;
            }else {
                int listcount = AllboxList.size() - 1;
                for (int i = 0; i < AllboxList.size(); i++) {
                    Box scanbox = AllboxList.get(listcount - i);
                    if (scanbox.getBoxID().equals(box.getBoxID())) {
                        ylMediaPlayer.SuccessOrFail(true);
                        boxcheck = false;
                        break;
                    }
                }
            }

            if (boxcheck) {
//                Log.e(YLSystem.getKimTag(), AllboxList.size() + "插入数据");
//                box.setBoxType(vault_out_detail_tv_type.getTag().toString());
//                box.setBoxStatus(vault_out_detail_tv_boxstaut.getTag().toString());
                box.setActionTime(YLSysTime.GetStrCurrentTime());
                box.setTradeAction("出");
                box.setBoxCount("1");
                box.setServerReturn("1");
                box.setTimeID("1");
//                Log.e(YLSystem.getKimTag(), box.toString());
                AllboxList.add(box);
                DisPlayBoxlistAdapter(AllboxList);
                ylMediaPlayer.SuccessOrFail(true);

            }
            ShowBoxList();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void DisPlayBoxlistAdapter(List<Box> boxList){
        if (boxList != null ){
            ylValutboxitemAdapter =
                    new YLValutboxitemAdapter(getApplicationContext(),boxList,R.layout.vault_in_detail_boxitem);
            vault_out_detail_lv.setAdapter(ylValutboxitemAdapter);
            scrollMyListViewToBottom();
        }
    }

    private void scrollMyListViewToBottom() {
        vault_out_detail_lv.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                vault_out_detail_lv.setSelection(vaulteroutboxlist.size() - 1);
            }
        });
    }

}
