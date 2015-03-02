package ylescort.ylmobileandroid;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import TaskClass.Box;
import adapter.YLBoxAdapter;


public class YLBoxEdit extends ActionBarActivity {


    private Switch boxedi_sw_gog;
    private Switch boxedi_sw_eof;

    private RadioGroup boxedi_radioGroup;
    private RadioButton boxedi_rb_money;
    private RadioButton boxedi_rb_card;
    private RadioButton boxedi_rb_Voucher;

    private ListView boxedi_listview;
    private List<Box> boxList;
    private int listpostion;

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
            }
        });
        boxedi_sw_gog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //收/送
                Box box = boxList.get(listpostion);
                box.setTradeAction(GetBoxStuat("g"));
                boxList.set(listpostion,box);
                ReLoadData();
            }
        });
        boxedi_sw_eof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //空/实
                Box box = boxList.get(listpostion);
                box.setBoxStatus(GetBoxStuat("f"));
                boxList.set(listpostion,box);
                ReLoadData();
            }
        });

        boxedi_radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //箱类型
                Box box = boxList.get(listpostion);
                box.setBoxType(GetBoxStuat("s"));
                boxList.set(listpostion,box);
                ReLoadData();
            }
        });

    }

    private String GetBoxStuat(String getboxstuat ){
        String boxstuat = "";
        if (getboxstuat.equals("g")){
            if (boxedi_sw_gog.isChecked()){
                boxstuat = "送";
            }else {
                boxstuat = "收";
            }}
        else if (getboxstuat.equals("f")){
            if (boxedi_sw_eof.isChecked()){
                boxstuat ="实";
            }else {
                boxstuat ="空";
            }}
        else if (getboxstuat.equals("s")){
            if (boxedi_rb_money.isChecked()){
                boxstuat ="款箱";
            }else if (boxedi_rb_card.isChecked()){
                boxstuat ="卡箱";
            }else if (boxedi_rb_Voucher.isChecked()){
                boxstuat ="凭证";
            }}

        return boxstuat;
    }

    public void boxedi_del(View view){
        boxList.remove(listpostion);ReLoadData();
    }

    private void EditBox(Box box) {

        String boxtradaction = box.getTradeAction();
        String boxstatus = box.getBoxStatus();
        String boxtype = box.getBoxType();
        if (boxtradaction.equals("收")){
            boxedi_sw_gog.setChecked(false);
        }else {
            boxedi_sw_gog.setChecked(true);
        }
        if (boxstatus.equals("空")){
            boxedi_sw_eof.setChecked(false);
        }else {
            boxedi_sw_eof.setChecked(true);
        }
        if (boxtype.equals("款箱")){
            boxedi_rb_money.setChecked(true);
        }else if (boxtype.equals("卡箱")){
            boxedi_rb_card.setChecked(true);
        }else if (boxtype.equals("凭证")){
            boxedi_rb_Voucher.setChecked(true);
        }
    }

    private void init() {
        boxedi_sw_gog = (Switch)findViewById(R.id.boxedi_sw_gog);
        boxedi_sw_eof = (Switch)findViewById(R.id.boxedi_sw_eof);
        boxedi_rb_money = (RadioButton)findViewById(R.id.boxedi_rb_money);
        boxedi_rb_card = (RadioButton)findViewById(R.id.boxedi_rb_card);
        boxedi_rb_Voucher = (RadioButton)findViewById(R.id.boxedi_rb_Voucher);
        boxedi_listview = (ListView)findViewById(R.id.boxedi_listview);
        boxedi_radioGroup= (RadioGroup)findViewById(R.id.boxedi_radioGroup);
        boxList =  new ArrayList<>();
        GetLocaData();
    }

    private void GetLocaData() {

        for (int i = 0 ;i<=9;i++){
            Box box = new Box();
            box.setBoxOrder(i+1+"");
            box.setBoxID(709394+i+"");
            box.setTradeAction("送");
            box.setBoxStatus("空");
            box.setBoxType("款箱");
            box.setBoxCount(1+"");
            boxList.add(box);
        }
        YLBoxAdapter ylBoxAdapter = new YLBoxAdapter(this,boxList,R.layout.activity_boxlist);
        boxedi_listview.setAdapter(ylBoxAdapter);
    }

    private void ReLoadData(){
        YLBoxAdapter ylBoxAdapter = new YLBoxAdapter(this,boxList,R.layout.activity_boxlist);
        boxedi_listview.setAdapter(ylBoxAdapter);
        boxedi_listview.setSelection(listpostion);
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
