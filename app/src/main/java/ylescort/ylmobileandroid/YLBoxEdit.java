package ylescort.ylmobileandroid;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import TaskClass.Box;
import TaskClass.TasksManager;
import TaskClass.YLTask;
import YLSystem.YLSystem;
import adapter.YLBoxAdapter;
import adapter.YLBoxEdiAdapter;


public class YLBoxEdit extends ActionBarActivity {


//    private Switch boxedi_sw_gog;
//    private Switch boxedi_sw_eof;
//    private RadioGroup boxedi_radioGroup;
//    private RadioButton boxedi_rb_money;
//    private RadioButton boxedi_rb_card;
//    private RadioButton boxedi_rb_Voucher;

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
    private ListView boxedi_listview;
    private List<Box> boxList;
    private List<Box> boxListEdi;
    private int listpostion;
    private String currSiteID;

    private TasksManager tasksManager = null;//任务管理类
    private YLTask ylTask;//当前选中的任务

    private YLBoxEdiAdapter ylBoxEdiAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ylbox_edit);
        init();
        boxedi_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               ListView listView = (ListView)parent;
                Box box = (Box)listView.getItemAtPosition(position);
                listpostion = position;
                EditBox(box);
                ReLoadData();
            }
        });

        RadioClick();

        boxedi_sp_tasktype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (boxList.size()<1)return;
                Box box = boxList.get(listpostion);
                box.setBoxTaskType(parent.getItemAtPosition(position).toString());
                boxList.set(listpostion,box);
                ReLoadData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void RadioClick() {

        boxedi_rbtn_get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Box box = boxList.get(listpostion);
                box.setTradeAction(GetBoxStuat("g"));
                boxList.set(listpostion,box);
                //ReLoadData();
                ylBoxEdiAdapter.notifyDataSetChanged();
            }
        });

        boxedi_rbtn_give.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Box box = boxList.get(listpostion);
                box.setTradeAction(GetBoxStuat("g"));
                boxList.set(listpostion,box);
                //ReLoadData();
                ylBoxEdiAdapter.notifyDataSetChanged();
            }
        });

        boxedi_rbtn_full.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Box box = boxList.get(listpostion);
                box.setBoxStatus(GetBoxStuat("f"));
                boxList.set(listpostion,box);
                //ReLoadData();
                ylBoxEdiAdapter.notifyDataSetChanged();
            }
        });

        boxedi_rbtn_empty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Box box = boxList.get(listpostion);
                box.setBoxStatus(GetBoxStuat("f"));
                boxList.set(listpostion,box);
                //ReLoadData();
                ylBoxEdiAdapter.notifyDataSetChanged();
            }
        });

        boxedi_rbtn_moneyboxs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Box box = boxList.get(listpostion);
                box.setBoxType(GetBoxStuat("s"));
                boxList.set(listpostion,box);
                //ReLoadData();
                ylBoxEdiAdapter.notifyDataSetChanged();
            }
        });

        boxedi_rbtn_cardbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Box box = boxList.get(listpostion);
                box.setBoxType(GetBoxStuat("s"));
                boxList.set(listpostion,box);
                //ReLoadData();
                ylBoxEdiAdapter.notifyDataSetChanged();
            }
        });

        boxedi_rbtn_Voucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Box box = boxList.get(listpostion);
                box.setBoxType(GetBoxStuat("s"));
                boxList.set(listpostion,box);
                //ReLoadData();
                ylBoxEdiAdapter.notifyDataSetChanged();
            }
        });



//        boxedi_sw_gog.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //收/送
//                Box box = boxList.get(listpostion);
//                box.setTradeAction(GetBoxStuat("g"));
//                boxList.set(listpostion,box);
//                ReLoadData();
//            }
//        });
//
//        boxedi_sw_eof.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //空/实
//                Box box = boxList.get(listpostion);
//                box.setBoxStatus(GetBoxStuat("f"));
//                boxList.set(listpostion,box);
//                ReLoadData();
//            }
//        });
//
//        boxedi_radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                //箱类型
//                Box box = boxList.get(listpostion);
//                box.setBoxType(GetBoxStuat("s"));
//                boxList.set(listpostion, box);
//                ReLoadData();
//            }
//        });
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
        if (boxList.size()!=0){
            boxList.remove(listpostion);ReLoadData();
        }
    }

    private void EditBox(Box box) {

        String boxtradaction = box.getTradeAction();
        String boxstatus = box.getBoxStatus();
        String boxtype = box.getBoxType();
        String boxtasktype = box.getBoxTaskType();

        if (boxtradaction.equals("收")){
            //boxedi_sw_gog.setChecked(false);
            boxedi_rbtn_get.setChecked(true);
        }else {
            //boxedi_sw_gog.setChecked(true);
            boxedi_rbtn_give.setChecked(true);
        }
        if (boxstatus.equals("空")){
            //boxedi_sw_eof.setChecked(false);
            boxedi_rbtn_empty.setChecked(true);
        }else {
            //boxedi_sw_eof.setChecked(true);
            boxedi_rbtn_full.setChecked(true);
        }
        if (boxtype.equals("款箱")){
            //boxedi_rb_money.setChecked(true);
            boxedi_rbtn_moneyboxs.setChecked(true);
        }else if (boxtype.equals("卡箱")){
            //boxedi_rb_card.setChecked(true);
            boxedi_rbtn_cardbox.setChecked(true);
        }else if (boxtype.equals("凭证")){
            //boxedi_rb_Voucher.setChecked(true);
            boxedi_rbtn_Voucher.setChecked(true);
        }
        if (boxtasktype.equals("早送")){
            boxedi_sp_tasktype.setSelection(0);
        }else if (boxtasktype.equals("晚收")){
            boxedi_sp_tasktype.setSelection(1);
        }else if (boxtasktype.equals("区内中调")){
            boxedi_sp_tasktype.setSelection(2);
        }else if (boxtasktype.equals("跨区中调")){
            boxedi_sp_tasktype.setSelection(3);
        }else if (boxtasktype.equals("库内区内中调")){
            boxedi_sp_tasktype.setSelection(4);
        }else if (boxtasktype.equals("库内跨区中调")){
            boxedi_sp_tasktype.setSelection(5);
        }else if (boxtasktype.equals("夜间周转")){
            boxedi_sp_tasktype.setSelection(6);
        }else if (boxtasktype.equals("人行")){
            boxedi_sp_tasktype.setSelection(7);
        }
    }

    private void init() {

        boxedi_sp_tasktype = (Spinner)findViewById(R.id.boxedi_sp_tasktype);
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




//        boxedi_sw_gog = (Switch)findViewById(R.id.boxedi_sw_gog);
//        boxedi_sw_eof = (Switch)findViewById(R.id.boxedi_sw_eof);
//        boxedi_rb_money = (RadioButton)findViewById(R.id.boxedi_rb_money);
//        boxedi_rb_card = (RadioButton)findViewById(R.id.boxedi_rb_card);
//        boxedi_rb_Voucher = (RadioButton)findViewById(R.id.boxedi_rb_Voucher);
//        boxedi_radioGroup= (RadioGroup)findViewById(R.id.boxedi_radioGroup);

        boxListEdi = new ArrayList<>();
        boxList = new ArrayList<>();
        tasksManager= YLSystem.getTasksManager();//获取任务管理类
        ylTask=tasksManager.CurrentTask;//当前选中的任务
        //boxListEdi =ylTask.getLstBox();
        boxListEdi =YLSystem.getEdiboxList();

        ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(this,R.array.tasktype
                ,android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        boxedi_sp_tasktype.setAdapter(arrayAdapter);
        boxedi_sp_tasktype.setPrompt("交接类型");

        Bundle bundle = this.getIntent().getExtras();
        currSiteID = bundle.getString("siteid");
        String boxscanstate = bundle.getString("box_btn_ent_text");
        if (boxscanstate.equals("到达")){
            boxListEdi = ylTask.getLstBox();
        }

        if (boxListEdi != null){
            for (int i = 0 ;i<boxListEdi.size();i++){
                if (boxListEdi.get(i).getSiteID().equals(currSiteID)){
                    boxList.add(boxListEdi.get(i));
                }
            }
        }
        YLBoxEdit.this.setTitle("款箱编辑: "+YLSystem.getUser().getName());
        ReLoadData();
    }

    private void ReLoadData(){
        if (boxList==null){
            return;
        }
//        YLBoxAdapter ylBoxAdapter = new YLBoxAdapter(this,boxList,R.layout.activity_boxlist);
//        boxedi_listview.setAdapter(ylBoxAdapter);
//        boxedi_listview.setSelection(listpostion);

        ylBoxEdiAdapter = new YLBoxEdiAdapter(this,boxList,R.layout.activity_boxedititem);
        //ylBoxEdiAdapter.setSelectItem(listpostion);
        boxedi_listview.setAdapter(ylBoxEdiAdapter);
        boxedi_listview.setSelection(listpostion);
    }

    public void boxedi_ent(View view){
        dialog();
    }

    protected void dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(YLBoxEdit.this);
        builder.setMessage("确认保存吗?");
        builder.setTitle("提示");
        builder.setPositiveButton("确认",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                for (int i = 0;i < boxListEdi.size();i++){
                    if (boxListEdi.get(i).getSiteID().equals(currSiteID)){
                        boxListEdi.remove(i);
                        --i;
                    }
                }

                for (int i = 0 ;i <boxList.size();i++){
                    Box box = new Box();
                    box = boxList.get(i);
                    boxListEdi.add(box);
                }
                //ylTask.setLstBox(boxListEdi);
                YLSystem.setEdiboxList(boxListEdi);
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
