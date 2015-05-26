package ylescort.ylmobileandroid;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import TaskClass.Box;
import TaskClass.TasksManager;
import TaskClass.YLTask;
import YLSystemDate.YLEditData;
import YLSystemDate.YLSystem;
import YLAdapter.YLBoxEdiAdapter;


public class YLBoxEdit extends ActionBarActivity implements View.OnClickListener {

    private RadioButton boxedi_rbtn_get;
    private RadioButton boxedi_rbtn_give;
    private RadioButton boxedi_rbtn_empty;
    private RadioButton boxedi_rbtn_full;
    private RadioButton boxedi_rbtn_moneyboxs;
    private RadioButton boxedi_rbtn_cardbox;
    private RadioButton boxedi_rbtn_Voucher;
    private RadioButton boxedi_rbtn_Voucherbag;

    private Button boxedi_btn_ent;
    private Button boxedi_btn_black;
    private Button boxedi_btn_del;

    private Spinner boxedi_sp_tasktype;
    private Spinner boxedi_sp_TimeID;

    private ListView boxedi_listview;

    private List<Box> boxSiteListAll;//所有网点款箱数据
    private List<Box> boxEditListEdit;//在编辑的数据
    private List<Box> boxEditListAll;//网点所有款箱数据

    public YLEditData ylEditData;

    private int listpostion;
    private String currSiteID;
    private String boxscanstate;
    private String currTimeID;

    private boolean editflag;

    private YLBoxEdiAdapter ylBoxEdiAdapter;

    private TasksManager tasksManager = null;//任务管理类
    private YLTask ylTask;//当前选中的任务


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ylbox_edit);
//        boxnosave = new ArrayList<>();
        editflag = true;
        initlayout();
        boxedi_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               ListView listView = (ListView)parent;
                Box box = (Box)listView.getItemAtPosition(position);
                listpostion = position;
                EditBox(box);
                ylBoxEdiAdapter.setSelectItem(position);
                ylBoxEdiAdapter.notifyDataSetInvalidated();
            }
        });

        boxedi_sp_tasktype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (boxEditListEdit.size()<1)return;
                Box box = boxEditListEdit.get(listpostion);
                box.setBoxTaskType(parent.getItemAtPosition(position).toString());
                boxEditListEdit.set(listpostion, box);
                //LoadBoxData(boxEditListEdit);
                ylBoxEdiAdapter.notifyDataSetInvalidated();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        GetTimeIDtoSp();

        //TimeID 变更
        boxedi_sp_TimeID.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //if (boxEditListAll.size()<1)return;
                String TimeID = parent.getItemAtPosition(position).toString();
                currTimeID = TimeID;
                ListGroup(TimeID);
                LoadBoxData(boxEditListEdit);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void ListGroup(String timeID) {

        if (timeID.equals("全部")|| boxEditListEdit.size() == 0){
           for (int i = 0 ;i <boxEditListAll.size();i++){
               boxEditListEdit.add(boxEditListAll.get(i));
           }
            boxEditListAll.clear();
        }else {
            for (int i = 0; i < boxEditListEdit.size();i++){
                boxEditListAll.add(boxEditListEdit.get(i));
            }
            boxEditListEdit.clear();
            for (int i = 0 ; i < boxEditListAll.size();i++){
                if (boxEditListAll.get(i).getTimeID().equals(timeID)){
                    boxEditListEdit.add(boxEditListAll.get(i));
                }
            }
            for (int i = 0 ; i < boxEditListAll.size();i++){
                if (boxEditListAll.get(i).getTimeID().equals(timeID)){
                    boxEditListAll.remove(i);
                    --i;
                }
            }
        }
    }

    //将TimeID
    private void GetTimeIDtoSp() {
       if (boxEditListAll.size()==0){
           return;
       }
        ArrayList<String> TimeIDlist = new ArrayList<>();
        String Timeid = "全部";
       TimeIDlist.add(Timeid);
        for (int i = 0 ; i< boxEditListAll.size();i++){
            if (Timeid.equals(boxEditListAll.get(i).getTimeID())){
                Timeid =  boxEditListAll.get(i).getTimeID();
            }else {
                Timeid =  boxEditListAll.get(i).getTimeID();
                TimeIDlist.add(Timeid);
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this,android.R.layout.simple_spinner_item,TimeIDlist);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        boxedi_sp_TimeID.setAdapter(adapter);
        boxedi_sp_TimeID.setPrompt("到达顺序");
    }

    private void TallyBox(List<Box> boxList) {
        if (boxList == null)return;
        int emptybox = 0;
        int fullbox = 0;
        int getbox = 0;
        int givebox = 0;
        int moneybox = 0;
        int cardbox = 0;
        int voucher =0;
        int voucherbag =0;
        for (Box box :boxList){
            if (box.getTradeAction().equals("收")){
                getbox +=Integer.parseInt(box.getBoxCount()) ;}
            else{
                givebox+=Integer.parseInt(box.getBoxCount());
            }
            if (box.getBoxStatus().equals("空")){
                emptybox+=Integer.parseInt(box.getBoxCount());
            }else {
                fullbox+=Integer.parseInt(box.getBoxCount());
            }
            if (box.getBoxType().equals("款箱")){
                moneybox+=Integer.parseInt(box.getBoxCount());
            }else if (box.getBoxType().equals("卡箱")){
                cardbox+=Integer.parseInt(box.getBoxCount());
            }else if (box.getBoxType().equals("凭证箱")){
                voucher+=Integer.parseInt(box.getBoxCount());
            }else if (box.getBoxType().equals("凭证袋")){
                voucherbag +=Integer.parseInt(box.getBoxCount());
            }
        }

        boxedi_rbtn_get.setText("收箱-"+getbox);
        boxedi_rbtn_give.setText("送箱-"+givebox);
        boxedi_rbtn_full.setText("实箱-" + fullbox);
        boxedi_rbtn_empty.setText("空箱-" + emptybox);
        boxedi_rbtn_moneyboxs.setText("款箱-" + moneybox);
        boxedi_rbtn_cardbox.setText("卡箱-" + cardbox);
        boxedi_rbtn_Voucher.setText("凭证箱-" + voucher);
        boxedi_rbtn_Voucherbag.setText("凭证袋-" + voucherbag);

    }

    private void GetandSetBoxtolist() {
        if (boxEditListEdit == null || boxEditListEdit.size() ==0)return;
        Box box = boxEditListEdit.get(listpostion);
        box.setTradeAction(GetBoxStuat("g"));
        box.setBoxStatus(GetBoxStuat("f"));
        box.setBoxType(GetBoxStuat("s"));
        boxEditListEdit.set(listpostion, box);
        ylBoxEdiAdapter.notifyDataSetInvalidated();
        TallyBox(boxEditListEdit);

    }

    private String GetBoxStuat(String getboxstuat ){
        String boxstuat = "";
        if (getboxstuat.equals("g")){
            if (boxedi_rbtn_give .isChecked()){
                boxstuat = "送";
            }else {
                boxstuat = "收";
            }}
        else if (getboxstuat.equals("f")){
            if (boxedi_rbtn_full.isChecked()){
                boxstuat ="实";
            }else {
                boxstuat ="空";
            }}
        else if (getboxstuat.equals("s")){
            if (boxedi_rbtn_moneyboxs.isChecked()){
                boxstuat ="款箱";
            }else if (boxedi_rbtn_cardbox.isChecked()){
                boxstuat ="卡箱";
            }else if (boxedi_rbtn_Voucher.isChecked()){
                boxstuat ="凭证箱";
            }else if (boxedi_rbtn_Voucherbag.isChecked()){
                boxstuat ="凭证袋";
            }}
        return boxstuat;
    }

    private void EditBox(Box box) {
        String boxtradaction = box.getTradeAction();
        String boxstatus = box.getBoxStatus();
        String boxtype = box.getBoxType();
        String boxtasktype = box.getBoxTaskType();

        if (boxtradaction.equals("收")){
            boxedi_rbtn_get.setChecked(true);
        }else {
            boxedi_rbtn_give.setChecked(true);
        }
        if (boxstatus.equals("空")){
            boxedi_rbtn_empty.setChecked(true);
        }else {
            boxedi_rbtn_full.setChecked(true);
        }

        switch (boxtype){
            case "款箱":
                boxedi_rbtn_moneyboxs.setChecked(true);
                boxedi_rbtn_cardbox.setChecked(false);
                boxedi_rbtn_Voucher.setChecked(false);
                boxedi_rbtn_Voucherbag.setChecked(false);
                break;
            case "卡箱":
                boxedi_rbtn_moneyboxs.setChecked(false);
                boxedi_rbtn_cardbox.setChecked(true);
                boxedi_rbtn_Voucher.setChecked(false);
                boxedi_rbtn_Voucherbag.setChecked(false);
                break;
            case "凭证箱":
                boxedi_rbtn_moneyboxs.setChecked(false);
                boxedi_rbtn_cardbox.setChecked(false);
                boxedi_rbtn_Voucher.setChecked(true);
                boxedi_rbtn_Voucherbag.setChecked(false);
                break;
            case "凭证袋":
                boxedi_rbtn_moneyboxs.setChecked(false);
                boxedi_rbtn_cardbox.setChecked(false);
                boxedi_rbtn_Voucher.setChecked(false);
                boxedi_rbtn_Voucherbag.setChecked(true);
                break;
        }
        switch (boxtasktype){
            case "早送":boxedi_sp_tasktype.setSelection(0);
                break;
            case "晚收":boxedi_sp_tasktype.setSelection(1);
                break;
            case "日间区内中调":boxedi_sp_tasktype.setSelection(2);
                break;
            case "日间跨区中调":boxedi_sp_tasktype.setSelection(3);
                break;
            case "库内区内中调":boxedi_sp_tasktype.setSelection(4);
                break;
            case "库内跨区中调":boxedi_sp_tasktype.setSelection(5);
                break;
            case "夜间基地周转":boxedi_sp_tasktype.setSelection(6);
                break;
            case "佛山人行":boxedi_sp_tasktype.setSelection(7);
                break;
            case "寄库":boxedi_sp_tasktype.setSelection(8);
                break;
            case "企业上门收款":boxedi_sp_tasktype.setSelection(9);
                break;
        }
    }

    private void initlayout() {

        boxedi_sp_tasktype = (Spinner) findViewById(R.id.boxedi_sp_tasktype);
        boxedi_sp_TimeID = (Spinner) findViewById(R.id.boxedi_sp_TimeID);
        boxedi_listview = (ListView) findViewById(R.id.boxedi_listview);

        boxedi_rbtn_get = (RadioButton) findViewById(R.id.boxedi_rbtn_get);
        boxedi_rbtn_give = (RadioButton) findViewById(R.id.boxedi_rbtn_give);
        boxedi_rbtn_empty = (RadioButton) findViewById(R.id.boxedi_rbtn_empty);
        boxedi_rbtn_full = (RadioButton) findViewById(R.id.boxedi_rbtn_full);
        boxedi_rbtn_moneyboxs = (RadioButton) findViewById(R.id.boxedi_rbtn_moneyboxs);
        boxedi_rbtn_cardbox = (RadioButton) findViewById(R.id.boxedi_rbtn_cardbox);
        boxedi_rbtn_Voucher = (RadioButton) findViewById(R.id.boxedi_rbtn_Voucher);
        boxedi_rbtn_Voucherbag = (RadioButton) findViewById(R.id.boxedi_rbtn_Voucherbag);

        boxedi_btn_ent = (Button)findViewById(R.id.boxedi_btn_ent);
        boxedi_btn_black = (Button)findViewById(R.id.boxedi_btn_black);
        boxedi_btn_del = (Button)findViewById(R.id.boxedi_btn_del);

        boxedi_rbtn_get.setOnClickListener(this);
        boxedi_rbtn_give.setOnClickListener(this);
        boxedi_rbtn_empty.setOnClickListener(this);
        boxedi_rbtn_full.setOnClickListener(this);
        boxedi_rbtn_moneyboxs.setOnClickListener(this);
        boxedi_rbtn_cardbox.setOnClickListener(this);
        boxedi_rbtn_Voucher.setOnClickListener(this);
        boxedi_rbtn_Voucherbag.setOnClickListener(this);

        boxedi_btn_ent.setOnClickListener(this);
        boxedi_btn_black.setOnClickListener(this);
        boxedi_btn_del.setOnClickListener(this);


        ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(this, R.array.tasktype
                , android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        boxedi_sp_tasktype.setAdapter(arrayAdapter);
        boxedi_sp_tasktype.setPrompt("交接类型");

        InitBoxData();
        YLBoxEdit.this.setTitle("款箱编辑: " + YLSystem.getUser().getName());
    }

    @Override
    public void onClick(View v) {
        if (editflag){
            ylEditData.setYlboxnosave(boxEditListEdit);
            Log.e(YLSystem.getKimTag(), ylEditData.getYlboxnosave().toString() + "onclick");
        }

        switch (v.getId()){
            case R.id.boxedi_rbtn_moneyboxs:
                boxedi_rbtn_cardbox.setChecked(false);
                boxedi_rbtn_Voucher.setChecked(false);
                boxedi_rbtn_Voucherbag.setChecked(false);
                GetandSetBoxtolist();
                break;
            case R.id.boxedi_rbtn_cardbox:
                boxedi_rbtn_moneyboxs.setChecked(false);
                boxedi_rbtn_Voucher.setChecked(false);
                boxedi_rbtn_Voucherbag.setChecked(false);
                GetandSetBoxtolist();
                break;
            case R.id.boxedi_rbtn_Voucher:
                boxedi_rbtn_cardbox.setChecked(false);
                boxedi_rbtn_moneyboxs.setChecked(false);
                boxedi_rbtn_Voucherbag.setChecked(false);
                GetandSetBoxtolist();
                break;
            case R.id.boxedi_rbtn_Voucherbag:
                boxedi_rbtn_cardbox.setChecked(false);
                boxedi_rbtn_Voucher.setChecked(false);
                boxedi_rbtn_moneyboxs.setChecked(false);
                GetandSetBoxtolist();
                break;
            case R.id.boxedi_rbtn_get:GetandSetBoxtolist();
                break;
            case R.id.boxedi_rbtn_give:GetandSetBoxtolist();
                break;
            case R.id.boxedi_rbtn_empty:GetandSetBoxtolist();
                break;
            case R.id.boxedi_rbtn_full:GetandSetBoxtolist();
                break;
            case R.id.boxedi_btn_ent:SaveBoxlistData();
                break;
            case R.id.boxedi_btn_black:
                YLSystem.setEdiboxList(ylEditData.getYlboxnosave());
                Log.e(YLSystem.getKimTag(), ylEditData.getYlboxnosave().toString() + "nosave");
                YLBoxEdit.this.finish();
                break;
            case R.id.boxedi_btn_del:
                if (boxEditListEdit.size()!=0){
                    boxEditListEdit.remove(listpostion);
                    ylBoxEdiAdapter.notifyDataSetInvalidated();
                }
                break;
        }
        editflag = false;
    }

    private void InitBoxData() {

        tasksManager= YLSystem.getTasksManager();//获取任务管理类
        ylTask=tasksManager.CurrentTask;//当前选中的任务

        boxEditListAll = new ArrayList<>();
        boxEditListEdit = new ArrayList<>();
        boxSiteListAll = new ArrayList<>();

        Bundle bundle = this.getIntent().getExtras();
        currSiteID = bundle.getString("siteid");
        boxscanstate = bundle.getString("box_btn_ent_text");
        if (boxscanstate.equals("到达")){
            if (ylTask.getLstBox() != null && ylTask.getLstBox().size() != 0){
                boxSiteListAll = ylTask.getLstBox();
                for (int i = 0 ;i < ylTask.getLstBox().size();i++){
                    Box box = ylTask.getLstBox().get(i);
                    if (box.getSiteID().equals(currSiteID)){
                        boxEditListAll.add(box);
                    }
                }
            }
        }else{
            boxEditListAll = YLSystem.getEdiboxList();
        }

        ylEditData.setYlboxnosave(boxEditListAll);
        Log.e(YLSystem.getKimTag(), ylEditData.getYlboxnosave().toString() + "初始化");

        LoadBoxData(boxEditListAll);
        ///增加
        if (boxEditListAll.size()> 0){
            Box box = boxEditListAll.get(0);
            EditBox(box);
        }
    }


    private void LoadBoxData(List<Box> boxList){
        if (boxList ==null)return;
        ylBoxEdiAdapter = new YLBoxEdiAdapter(this, boxList,R.layout.activity_boxedititem);
        ylBoxEdiAdapter.setSelectItem(listpostion);
        boxedi_listview.setAdapter(ylBoxEdiAdapter);
        TallyBox(boxList);
    }

    protected void SaveBoxlistData() {
        AlertDialog.Builder builder = new AlertDialog.Builder(YLBoxEdit.this);
        builder.setMessage("确认保存吗?");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                ListGroup("全部");
                if (boxscanstate.equals("完成交接")) {
                    YLSystem.setEdiboxList(boxEditListEdit);
                } else {
                    //删除所属网点款箱
                    for (int i = 0; i < boxSiteListAll.size(); i++) {
                        if (boxSiteListAll.get(i).getSiteID().equals(currSiteID)) {
                            boxSiteListAll.remove(i);
                            --i;
                        }
                    }
                    for (int i = 0; i < boxEditListEdit.size(); i++) {
                        boxSiteListAll.add(boxEditListEdit.get(i));
                    }
                }
                dialog.dismiss();
                YLBoxEdit.this.finish();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
        Log.e(YLSystem.getKimTag(), ylEditData.getYlboxnosave().toString() + "保存");
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.e("aaaaaaaaaaaaaaaaaaaaaa", keyCode + "");
        if( keyCode == KeyEvent.KEYCODE_HOME){

            return super.onKeyDown(keyCode, event);
        }
        if(keyCode  == KeyEvent.KEYCODE_BACK){
            NoSaveBoxListDialog();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void NoSaveBoxListDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(YLBoxEdit.this);
        builder.setMessage("是否保存后退出?");
        builder.setTitle("提示");
        builder.setPositiveButton("确认",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ListGroup("全部");
                if (boxscanstate.equals("完成交接")){
                    YLSystem.setEdiboxList(boxEditListEdit);
                }else {
                    //删除所属网点款箱
                    for (int i = 0;i < boxSiteListAll.size();i++){
                        if (boxSiteListAll.get(i).getSiteID().equals(currSiteID)){
                            boxSiteListAll.remove(i);
                            --i;
                        }
                    }
                    for (int i = 0;i < boxEditListEdit.size();i++){
                        boxSiteListAll.add(boxEditListEdit.get(i));
                    }
                }
                dialog.dismiss();
                YLBoxEdit.this.finish();
            }
        });
        builder.setNegativeButton("取消",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                YLSystem.setEdiboxList(boxEditListEdit);
                YLBoxEdit.this.finish();
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ylbox_edit, menu);
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
