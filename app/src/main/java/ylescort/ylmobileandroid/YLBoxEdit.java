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
import java.util.Collections;
import java.util.List;

import TaskClass.Box;
import TaskClass.TasksManager;
import TaskClass.BoxCombyTime;
import TaskClass.YLTask;
import YLDataService.AnalysisBoxList;
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
    private List<Box> boxeditnosave;//初始款箱列表
    private List<Box> yleditcarboxs;
    private AnalysisBoxList analysisBoxList;

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
        editflag = true;
        initlayout();
        boxedi_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               ListView listView = (ListView)parent;
                Box box = (Box)listView.getItemAtPosition(position);
                listpostion = position;
                if (boxscanstate.equals("到达")){
                    if (box.getTradeAction().equals("送")){
                        boxedi_sp_tasktype.setEnabled(false);
                        boxedi_rbtn_full.setEnabled(false);
                        boxedi_rbtn_empty.setEnabled(false);
                    }else {
                        boxedi_sp_tasktype.setEnabled(false);
                        boxedi_rbtn_full.setEnabled(false);
                        boxedi_rbtn_empty.setEnabled(false);
                    }
                }else {
                    if (box.getTradeAction().equals("送")){
                        boxedi_sp_tasktype.setEnabled(false);
                        boxedi_rbtn_full.setEnabled(false);
                        boxedi_rbtn_empty.setEnabled(false);
                    }else {
                        boxedi_sp_tasktype.setEnabled(false);
//                        boxedi_rbtn_full.setEnabled(false);
//                        boxedi_rbtn_empty.setEnabled(false);
                    }
                }

                EditBox(box);
                ylBoxEdiAdapter.setSelectItem(position);
                ylBoxEdiAdapter.notifyDataSetChanged();
            }
        });

        boxedi_sp_tasktype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (boxEditListEdit.size()<1)return;
                Box box = boxEditListEdit.get(listpostion);

                Box editbox = new Box();
                editbox.setTradeAction(GetBoxStuat("g"));
                editbox.setBoxStatus(GetBoxStuat("f"));
                editbox.setBoxType(GetBoxStuat("s"));
                editbox.setSiteID(box.getSiteID());
                editbox.setBoxID(box.getBoxID());
                editbox.setBoxName(box.getBoxName());
                editbox.setNextOutTime(box.getNextOutTime());
                editbox.setActionTime(box.getActionTime());
                editbox.setTimeID(box.getTimeID());
                editbox.setBoxCount(box.getBoxCount());
                editbox.setBoxOrder(box.getBoxOrder());
                editbox.setRemark(box.getRemark());
                editbox.setTaskTimeID(box.getTaskTimeID());
                editbox.setBoxToT(box.getBoxToT());
                editbox.setBoxTaskType(box.getBoxTaskType());

                editbox.setBoxTaskType(parent.getItemAtPosition(position).toString());
//                box.setBoxTaskType(parent.getItemAtPosition(position).toString());
                boxEditListEdit.set(listpostion, editbox);
                //LoadBoxData(boxEditListEdit);
                ylBoxEdiAdapter.notifyDataSetChanged();
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

        List<Integer> list =  analysisBoxList.AnsysisBoxList(boxList);

        boxedi_rbtn_get.setText("收箱:"+list.get(4));
        boxedi_rbtn_give.setText("送箱:"+list.get(5));
        boxedi_rbtn_full.setText("实箱:" + list.get(6));
        boxedi_rbtn_empty.setText("空箱:" + list.get(7));
        boxedi_rbtn_moneyboxs.setText("款箱:" + list.get(0));
        boxedi_rbtn_cardbox.setText("卡箱:" + list.get(1));
        boxedi_rbtn_Voucher.setText("凭证箱:" + list.get(2));
        boxedi_rbtn_Voucherbag.setText("凭证袋:" + list.get(3));

    }

    private void GetandSetBoxtolist() {
        if (boxEditListEdit == null || boxEditListEdit.size() ==0)return;

//        Log.e(YLSystem.getKimTag(),yleditcarboxs.toString()+"frist");

        Box box = boxEditListEdit.get(listpostion);
        Box editbox = new Box();
        editbox.setTradeAction(GetBoxStuat("g"));
        editbox.setBoxStatus(GetBoxStuat("f"));
        editbox.setBoxType(GetBoxStuat("s"));
        editbox.setSiteID(box.getSiteID());
        editbox.setBoxID(box.getBoxID());
        editbox.setBoxName(box.getBoxName());
        editbox.setNextOutTime(box.getNextOutTime());
        editbox.setActionTime(box.getActionTime());
        editbox.setTimeID(box.getTimeID());
        editbox.setBoxCount(box.getBoxCount());
        editbox.setBoxOrder(box.getBoxOrder());
        editbox.setRemark(box.getRemark());
        editbox.setTaskTimeID(box.getTaskTimeID());
        editbox.setBoxToT(box.getBoxToT());
        editbox.setBoxTaskType(box.getBoxTaskType());
        boxEditListEdit.set(listpostion, editbox);

        if (editbox.getTradeAction().equals("收")){
            for (int i = 0; i < yleditcarboxs.size(); i++) {
                if (yleditcarboxs.get(i).getBoxID().equals(editbox.getBoxID())){
                    yleditcarboxs.get(i).setBoxStatus(editbox.getBoxStatus());
                    yleditcarboxs.get(i).setBoxType(editbox.getBoxType());
                    yleditcarboxs.get(i).setBoxTaskType(editbox.getBoxTaskType());
                }
            }
        }

        ylBoxEdiAdapter.notifyDataSetChanged();
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

            case "早送晚收":boxedi_sp_tasktype.setSelection(0);
                break;
            case "上下介":boxedi_sp_tasktype.setSelection(1);
                break;
            case "同行调拨":boxedi_sp_tasktype.setSelection(2);
                break;
            case "跨行调拨":boxedi_sp_tasktype.setSelection(3);
                break;
            case "企业收送款":boxedi_sp_tasktype.setSelection(4);
                break;
            case "寄库箱":boxedi_sp_tasktype.setSelection(5);
                break;
        }

        if (box.getBoxName().contains("粤龙临")){
            boxedi_rbtn_moneyboxs.setEnabled(true);
            boxedi_rbtn_cardbox.setEnabled(true);
            boxedi_rbtn_Voucher.setEnabled(true);
            boxedi_rbtn_Voucherbag.setEnabled(true);
        }else {
            boxedi_rbtn_moneyboxs.setEnabled(false);
            boxedi_rbtn_cardbox.setEnabled(false);
            boxedi_rbtn_Voucher.setEnabled(false);
            boxedi_rbtn_Voucherbag.setEnabled(false);
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


        ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(this, R.array.TaskType
                , android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        boxedi_sp_tasktype.setAdapter(arrayAdapter);
        boxedi_sp_tasktype.setPrompt("交接类型");

        InitBoxData();
        YLBoxEdit.this.setTitle("款箱编辑: " + YLSystem.getUser().getName());
    }

    @Override
    public void onClick(View v) {
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
            case R.id.boxedi_btn_black://返回按钮
                YLSystem.setEdiboxList(boxEditListEdit);

                finish();
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                break;
            case R.id.boxedi_btn_del:
                if (boxEditListEdit.size()!=0){
//                    if (boxEditListEdit.get(listpostion).getTradeAction().equals("收")){
//                        Box box = boxEditListEdit.get(listpostion);
//
//                        for (int i = 0; i < yleditcarboxs.size(); i++) {
//                            if (yleditcarboxs.get(i).getActionTime().equals(box.getActionTime())){
//                                yleditcarboxs.remove(i);
//                                break;
//                            }
//                        }
//                    }else {
//                        Box box = boxEditListEdit.get(listpostion);
//                        box.setTradeAction("收");
//                        if (box.getId() != 1){
//                        yleditcarboxs.add(box);}
//                    }
//                    boxEditListEdit.remove(listpostion);
//                    ylBoxEdiAdapter.setSelectItem(0);
//                    listpostion = 0;
//                    Log.e(YLSystem.getKimTag(), yleditcarboxs.size() + "编辑在库数量");
//                    YLEditData.setYleditcarbox(yleditcarboxs);
//                    TallyBox(boxEditListEdit);
//                    ylBoxEdiAdapter.notifyDataSetChanged();

                    Box box = boxEditListEdit.get(listpostion);
                    Log.e(YLSystem.getKimTag(),"编辑删除"+box.toString());
                    if (box.getTradeAction().equals("收")){
                        for (int i = 0; i < yleditcarboxs.size(); i++) {
                            Box carbox = yleditcarboxs.get(i);
                            if (carbox.getBoxID().equals(box.getBoxID())){
                                yleditcarboxs.remove(i);
                                break;
                            }
                        }
                    }else {
                        box.setTradeAction("收");
                        if (box.getId() != 1) {
                            yleditcarboxs.add(box);
                        }
                    }
                    boxEditListEdit.remove(listpostion);
                    ylBoxEdiAdapter.setSelectItem(0);
                    listpostion = 0;
                    Log.e(YLSystem.getKimTag(), yleditcarboxs.size() + "编辑在库数量");
                    YLEditData.setYleditcarbox(yleditcarboxs);
                    TallyBox(boxEditListEdit);
                    ylBoxEdiAdapter.notifyDataSetChanged();
                }
                break;
        }
        editflag = false;
    }

    private void InitBoxData() {

        tasksManager= YLSystem.getTasksManager();//获取任务管理类
        ylTask=tasksManager.CurrentTask;//当前选中的任务
        analysisBoxList = new AnalysisBoxList();

        boxEditListAll = new ArrayList<>();
        boxEditListEdit = new ArrayList<>();
        boxSiteListAll = new ArrayList<>();
        yleditcarboxs = new ArrayList<>();
        boxeditnosave = new ArrayList<>();

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
                        boxeditnosave.add(box);
                    }
                }
                boxedi_rbtn_get.setEnabled(false);
                boxedi_rbtn_give.setEnabled(false);
                boxedi_rbtn_full.setEnabled(false);
                boxedi_rbtn_empty.setEnabled(false);
                boxedi_rbtn_moneyboxs.setEnabled(false);
                boxedi_rbtn_cardbox.setEnabled(false);
                boxedi_rbtn_Voucher.setEnabled(false);
                boxedi_rbtn_Voucherbag.setEnabled(false);
                boxedi_btn_del.setEnabled(false);
                boxedi_sp_tasktype.setEnabled(false);
            }
        }else{
            boxedi_sp_tasktype.setEnabled(false);
            boxedi_rbtn_get.setEnabled(false);
            boxedi_rbtn_give.setEnabled(false);
            boxEditListAll = YLSystem.getEdiboxList();
        }


        if (YLEditData.getYleditcarbox() !=null){
            if (YLEditData.getYleditcarbox().size()>0){
                for (Box box : YLEditData.getYleditcarbox()) {
                    yleditcarboxs.add(box);
                }
            }
        }
//        YLEditData.setYleditcarbox(yleditcarboxs);


        LoadBoxData(boxEditListAll);
        ///增加
        if (boxEditListAll.size()> 0){
            Box box = boxEditListAll.get(0);
            ylBoxEdiAdapter.setSelectItem(0);
            EditBox(box);
        }
    }


    private void LoadBoxData(List<Box> boxList){
        if (boxList ==null)return;
        ylBoxEdiAdapter = new YLBoxEdiAdapter(this, boxList,R.layout.activity_boxedititem);
        ylBoxEdiAdapter.setSelectItem(listpostion);
        boxedi_listview.setAdapter(ylBoxEdiAdapter);
        if (boxList.size()>0){
            BoxCombyTime ylBoxComparator = new BoxCombyTime();
            Collections.sort(boxList,ylBoxComparator);
            if (boxList.get(0).getTradeAction() != null & boxList.get(0).getTradeAction().equals("送")){
                boxedi_rbtn_empty.setEnabled(false);
                boxedi_rbtn_full.setEnabled(false);
            }
        }

        TallyBox(boxList);
    }

    protected void SaveBoxlistData() {
        AlertDialog.Builder builder = new AlertDialog.Builder(YLBoxEdit.this);
        builder.setMessage("确认保存吗?");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                SaveEditData();
                dialog.dismiss();
                YLBoxEdit.this.finish();
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
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

    private void SaveEditData() {
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
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SaveEditData();
                dialog.dismiss();
                YLBoxEdit.this.finish();
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                YLSystem.setEdiboxList(boxEditListEdit);
                YLBoxEdit.this.finish();
                dialog.dismiss();
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
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
