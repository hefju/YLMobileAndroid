package ylescort.ylmobileandroid;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import TaskClass.Box;
import TaskClass.TasksManager;
import TaskClass.YLTask;
import YLAdapter.YLBoxEdiAdapter;
import YLSystemDate.YLEditData;
import YLSystemDate.YLSystem;

/**
 * Created by Administrator on 2015/5/25.
 */
public class YLBoxEidter extends ActionBarActivity implements View.OnClickListener {

    private RadioButton boxedi_rbtn_get;
    private RadioButton boxedi_rbtn_give;
    private RadioButton boxedi_rbtn_empty;
    private RadioButton boxedi_rbtn_full;
    private RadioButton boxedi_rbtn_moneyboxs;
    private RadioButton boxedi_rbtn_cardbox;
    private RadioButton boxedi_rbtn_Voucher;
    private RadioButton boxedi_rbtn_Voucherbag;

    private Spinner boxedi_sp_tasktype;
    private Spinner boxedi_sp_TimeID;


    private ListView boxedi_listview;

    private List<Box> boxSiteListAll;
    private List<Box> boxEditListEdit;
    private List<Box> boxEditListAll;

    private List<Box> boxnosave;

    private int listpostion;
    private String currSiteID;
    private String boxscanstate;
    private String currTimeID;

    private YLBoxEdiAdapter ylBoxEdiAdapter;

    private TasksManager tasksManager = null;
    private YLTask ylTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ylbox_edit);
        InitView();
        InitData();
    }

    private void InitData() {
        tasksManager= YLSystem.getTasksManager();
        ylTask=tasksManager.CurrentTask;
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
//        YLEditData.ylboxnosave = boxEditListAll;
//        Log.e(YLSystem.getKimTag(),YLEditData.ylboxnosave.toString()+"初始化");
        LoadBoxData(boxEditListAll);
        ///增加
        if (boxEditListAll.size()> 0){
            Box box = boxEditListAll.get(0);
            PutDataToView(box);
        }
    }

    private void InitView() {
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


        boxedi_rbtn_get.setOnClickListener(this);

        ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(this, R.array.tasktype
                , android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        boxedi_sp_tasktype.setAdapter(arrayAdapter);
        boxedi_sp_tasktype.setPrompt("交接类型");
        YLBoxEidter.this.setTitle("款箱编辑: " + YLSystem.getUser().getName());

        boxedi_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListView listView = (ListView) parent;
                Box box = (Box) listView.getItemAtPosition(position);
                listpostion = position;
                PutDataToView(box);
            }
        });

        boxedi_sp_tasktype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (boxEditListEdit.size() < 1) return;
                Box box = boxEditListEdit.get(listpostion);
                box.setBoxTaskType(parent.getItemAtPosition(position).toString());
                boxEditListEdit.set(listpostion, box);
                ylBoxEdiAdapter.notifyDataSetInvalidated();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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

    private void LoadBoxData(List<Box> boxList){
        if (boxList ==null)return;
        ylBoxEdiAdapter = new YLBoxEdiAdapter(this, boxList,R.layout.activity_boxedititem);
        ylBoxEdiAdapter.setSelectItem(listpostion);
        boxedi_listview.setAdapter(ylBoxEdiAdapter);
        TallyBox(boxList);
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
        boxedi_rbtn_get.setText("收箱"+getbox);
        boxedi_rbtn_give.setText("送箱"+givebox);
        boxedi_rbtn_full.setText("实箱"+fullbox);
        boxedi_rbtn_empty.setText("空箱"+emptybox);
        boxedi_rbtn_moneyboxs.setText("款箱"+moneybox);
        boxedi_rbtn_cardbox.setText("卡箱"+cardbox);
        boxedi_rbtn_Voucher.setText("凭证箱"+voucher);
        boxedi_rbtn_Voucherbag.setText("凭证袋"+voucherbag);

    }

    private void PutDataToView(Box box) {
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.boxedi_rbtn_moneyboxs:
                boxedi_rbtn_cardbox.setChecked(false);
                boxedi_rbtn_Voucher.setChecked(false);
                boxedi_rbtn_Voucherbag.setChecked(false);
                break;
            case R.id.boxedi_rbtn_cardbox:
                boxedi_rbtn_moneyboxs.setChecked(false);
                boxedi_rbtn_Voucher.setChecked(false);
                boxedi_rbtn_Voucherbag.setChecked(false);
                break;
            case R.id.boxedi_rbtn_Voucher:
                boxedi_rbtn_cardbox.setChecked(false);
                boxedi_rbtn_moneyboxs.setChecked(false);
                boxedi_rbtn_Voucherbag.setChecked(false);
                break;
            case R.id.boxedi_rbtn_Voucherbag:
                boxedi_rbtn_cardbox.setChecked(false);
                boxedi_rbtn_Voucher.setChecked(false);
                boxedi_rbtn_moneyboxs.setChecked(false);
                break;
        }
        if (boxEditListEdit == null || boxEditListEdit.size() ==0)return;
        Box box = boxEditListEdit.get(listpostion);
        box.setTradeAction(GetBoxStuat("g"));
        box.setBoxStatus(GetBoxStuat("f"));
        box.setBoxType(GetBoxStuat("s"));
        boxEditListEdit.set(listpostion, box);
//        boxnosave = YLEditData.getYlboxnosave();
        Log.e(YLSystem.getKimTag(), boxnosave + "编辑中");
        ylBoxEdiAdapter.notifyDataSetInvalidated();
        //LoadBoxData(boxEditListEdit);
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

}
