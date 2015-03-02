package ylescort.ylmobileandroid;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import TaskClass.YLTask;


public class YLBoxEdit extends ActionBarActivity {


    private Switch boxedi_sw_gog;
    private Switch boxedi_sw_eof;

    private RadioButton boxedi_rb_money;
    private RadioButton boxedi_rb_card;
    private RadioButton boxedi_rb_Voucher;

    private ListView boxedi_listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ylbox_edit);
        init();
        boxedi_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String,String> map=(HashMap<String,String>)boxedi_listview.getItemAtPosition(position);
                String box = map.get("编码");
                Toast.makeText(getApplicationContext(),box,Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void init() {
        boxedi_sw_gog = (Switch)findViewById(R.id.boxedi_sw_gog);
        boxedi_sw_eof = (Switch)findViewById(R.id.boxedi_sw_eof);
        boxedi_rb_money = (RadioButton)findViewById(R.id.boxedi_rb_money);
        boxedi_rb_card = (RadioButton)findViewById(R.id.boxedi_rb_card);
        boxedi_rb_Voucher = (RadioButton)findViewById(R.id.boxedi_rb_Voucher);
        boxedi_listview = (ListView)findViewById(R.id.boxedi_listview);
        GetLocaData();
    }

    private void GetLocaData() {

        //生成动态数组，加入数据
        ArrayList<HashMap<String, Object>> listItem = new ArrayList<>();

        for (int i = 0 ;i <=10;i++){
            HashMap<String, Object> map = new HashMap<>();
            map.put("序号",i+1);
            map.put("编码","709394"+i);
            map.put("收/送","收");
            map.put("空/实","实");
            map.put("类型","款箱");
            map.put("数量",i+2);
            listItem.add(map);
        }

        //生成适配器的Item和动态数组对应的元素
        SimpleAdapter listItemAdapter = new SimpleAdapter(this,listItem,//数据源
                R.layout.activity_boxlist,
                //动态数组与ImageItem对应的子项
                new String[] {"序号","编码", "收/送", "空/实", "类型","数量"},
                new int[] {R.id.boxlv_tv_order,R.id.boxlv_tv_Boxid,R.id.boxlv_tv_TradeAction,
                R.id.boxlv_tv_Status,R.id.boxlv_tv_type,R.id.boxlv_tv_count}
        );

        //添加并且显示
        boxedi_listview.setAdapter(listItemAdapter);
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
