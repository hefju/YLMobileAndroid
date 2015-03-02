package ylescort.ylmobileandroid;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioButton;
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

    private RadioButton boxedi_rb_money;
    private RadioButton boxedi_rb_card;
    private RadioButton boxedi_rb_Voucher;

    private ListView boxedi_listview;
    private List<Box> boxList;

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
                EditBox(box);
            }
        });
    }

    private void EditBox(Box box) {


    }

    private void init() {
        boxedi_sw_gog = (Switch)findViewById(R.id.boxedi_sw_gog);
        boxedi_sw_eof = (Switch)findViewById(R.id.boxedi_sw_eof);
        boxedi_rb_money = (RadioButton)findViewById(R.id.boxedi_rb_money);
        boxedi_rb_card = (RadioButton)findViewById(R.id.boxedi_rb_card);
        boxedi_rb_Voucher = (RadioButton)findViewById(R.id.boxedi_rb_Voucher);
        boxedi_listview = (ListView)findViewById(R.id.boxedi_listview);
        boxList =  new ArrayList<>();
        GetLocaData();
    }

    private void GetLocaData() {

        for (int i = 0 ;i<=9;i++){
            Box box = new Box();
            box.setBoxorder(i+"");
            box.setBoxID(709394+i+"");
            box.setTradeAction("送");
            box.setBoxStatus("空");
            box.setBoxType("款箱");
            box.setBoxcount(1+"");
            boxList.add(box);
        }
        YLBoxAdapter ylBoxAdapter = new YLBoxAdapter(this,boxList,R.layout.activity_boxlist);
        boxedi_listview.setAdapter(ylBoxAdapter);
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
