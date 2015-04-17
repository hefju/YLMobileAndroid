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
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import TaskClass.Box;
import TaskClass.TasksManager;
import TaskClass.YLTask;
import YLSystemDate.YLSystem;
import adapter.YLBoxEdiAdapter;


public class YLBoxEdit extends ActionBarActivity {

    private RadioButton boxedi_rbtn_get;
    private RadioButton boxedi_rbtn_give;
    private RadioButton boxedi_rbtn_empty;
    private RadioButton boxedi_rbtn_full;
    private RadioButton boxedi_rbtn_moneyboxs;
    private RadioButton boxedi_rbtn_cardbox;
    private RadioButton boxedi_rbtn_Voucher;

    private TextView boxedi_tv_get;
    private TextView boxedi_tv_give;
    private TextView boxedi_tv_empty;
    private TextView boxedi_tv_moneyboxs;
    private TextView boxedi_tv_full;
    private TextView boxedi_tv_cardbox;
    private TextView boxedi_tv_voucher;

    private Spinner boxedi_sp_tasktype;
    private Spinner boxedi_sp_TimeID;


    private ListView boxedi_listview;

    private List<Box> boxSiteListAll;//所有网点款箱数据
    private List<Box> boxEditListEdit;//在编辑的数据
    private List<Box> boxEditListAll;//网点所有款箱数据
    private int listpostion;
    private String currSiteID;
    private String boxscanstate;
    private String currTimeID;


    private TasksManager tasksManager = null;//任务管理类
    private YLTask ylTask;//当前选中的任务

    //private YLBoxEdiAdapter ylBoxEdiAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ylbox_edit);
        initlayout();
        boxedi_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               ListView listView = (ListView)parent;
                Box box = (Box)listView.getItemAtPosition(position);
                listpostion = position;
                EditBox(box);
                LoadBoxData(boxEditListEdit);
            }
        });

        //RadioClick();

        boxedi_sp_tasktype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (boxEditListEdit.size()<1)return;
                Box box = boxEditListEdit.get(listpostion);
                box.setBoxTaskType(parent.getItemAtPosition(position).toString());
                boxEditListEdit.set(listpostion, box);
                LoadBoxData(boxEditListEdit);
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
        String until = "个";
        int emptybox = 0;
        int fullbox = 0;
        int getbox = 0;
        int givebox = 0;
        int moneybox = 0;
        int cardbox = 0;
        int voucher =0;
        int total;
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
            }else {
                voucher+=Integer.parseInt(box.getBoxCount());
            }
        }
        //total = moneybox+cardbox+voucher;
        boxedi_tv_empty.setText(emptybox+until);
        boxedi_tv_full.setText(fullbox+until);
        boxedi_tv_get.setText(getbox+until);
        boxedi_tv_give.setText(givebox+until);
        boxedi_tv_moneyboxs.setText(moneybox+"");
        boxedi_tv_cardbox.setText(cardbox+"");
        boxedi_tv_voucher.setText(voucher+"");
    }

    public void RadioClick(View view) throws ClassNotFoundException {
        if (boxEditListEdit == null || boxEditListEdit.size() ==0)return;
        Box box = boxEditListEdit.get(listpostion);
        box.setTradeAction(GetBoxStuat("g"));
        box.setBoxStatus(GetBoxStuat("f"));
        box.setBoxType(GetBoxStuat("s"));
        LoadBoxData(boxEditListEdit);

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
                boxstuat ="凭证";
            }}
        return boxstuat;
    }

    public void boxedi_del(View view){
        if (boxEditListEdit.size()!=0){
            boxEditListEdit.remove(listpostion);
            LoadBoxData(boxEditListEdit);//ReLoadData();
        }else {

        }
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
            case "款箱":boxedi_rbtn_moneyboxs.setChecked(true);
                break;
            case "卡箱":boxedi_rbtn_cardbox.setChecked(true);
                break;
            case "凭证":boxedi_rbtn_Voucher.setChecked(true);
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

        boxedi_sp_tasktype = (Spinner)findViewById(R.id.boxedi_sp_tasktype);
        boxedi_sp_TimeID = (Spinner)findViewById(R.id.boxedi_sp_TimeID);
        boxedi_listview = (ListView)findViewById(R.id.boxedi_listview);

         boxedi_rbtn_get = (RadioButton)findViewById(R.id.boxedi_rbtn_get);
         boxedi_rbtn_give = (RadioButton)findViewById(R.id.boxedi_rbtn_give);
         boxedi_rbtn_empty = (RadioButton)findViewById(R.id.boxedi_rbtn_empty);
         boxedi_rbtn_full = (RadioButton)findViewById(R.id.boxedi_rbtn_full);
         boxedi_rbtn_moneyboxs = (RadioButton)findViewById(R.id.boxedi_rbtn_moneyboxs);
         boxedi_rbtn_cardbox = (RadioButton)findViewById(R.id.boxedi_rbtn_cardbox);
         boxedi_rbtn_Voucher = (RadioButton)findViewById(R.id.boxedi_rbtn_Voucher);

         boxedi_tv_get = (TextView)findViewById(R.id.boxedi_tv_get);
         boxedi_tv_give = (TextView)findViewById(R.id.boxedi_tv_give);
         boxedi_tv_empty = (TextView)findViewById(R.id.boxedi_tv_empty);
         boxedi_tv_moneyboxs = (TextView)findViewById(R.id.boxedi_tv_moneyboxs);
         boxedi_tv_full = (TextView)findViewById(R.id.boxedi_tv_full);
         boxedi_tv_cardbox = (TextView)findViewById(R.id.boxedi_tv_cardbox);
         boxedi_tv_voucher = (TextView)findViewById(R.id.boxedi_tv_voucher);

        ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(this,R.array.tasktype
                ,android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        boxedi_sp_tasktype.setAdapter(arrayAdapter);
        boxedi_sp_tasktype.setPrompt("交接类型");

        InitBoxData();
        YLBoxEdit.this.setTitle("款箱编辑: "+YLSystem.getUser().getName());
        //ReLoadData();
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
        LoadBoxData(boxEditListAll);
    }


    private void LoadBoxData(List<Box> boxList){
        if (boxList ==null)return;
        YLBoxEdiAdapter ylBoxEdiAdapter = new YLBoxEdiAdapter(this, boxList,R.layout.activity_boxedititem);
        ylBoxEdiAdapter.setSelectItem(listpostion);
        boxedi_listview.setAdapter(ylBoxEdiAdapter);
        boxedi_listview.setSelection(listpostion);
        TallyBox(boxList);
    }

    public void boxedi_ent(View view){
        SaveBoxlistData();
    }

    protected void SaveBoxlistData() {
        AlertDialog.Builder builder = new AlertDialog.Builder(YLBoxEdit.this);
        builder.setMessage("确认保存吗?");
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
                dialog.dismiss();
            }
        });
        builder.create().show();
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
