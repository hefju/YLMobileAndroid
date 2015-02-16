package ylescort.ylmobileandroid.util;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.AndroidCharacter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import TaskClass.BaseEmp;
import TaskClass.User;
import TaskClass.YLTask;
import YLDataService.BaseEmpDBSer;
import YLDataService.EmpDBSer;
import adapter.YLTaskAdapter;
import ylescort.ylmobileandroid.R;

public class CacheDataView extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cache_data_view);

        ListView lvCache=(ListView)findViewById(R.id.lvCache);

        ArrayAdapter<String> adapter = new ArrayAdapter(this,
                android.R.layout.simple_expandable_list_item_1,
                getData());

        lvCache.setAdapter(adapter);
    }

    private   List<String> getData() {
        BaseEmpDBSer dbSer=new BaseEmpDBSer(this);
        List<BaseEmp> lstUser =dbSer.GetBaseEmps(" where id>0");
        Log.d("jutest","BaseEmp count:"+String.valueOf(lstUser.size()));

        List<String> lstItem=new ArrayList<>();
        for (BaseEmp x : lstUser ){
            lstItem.add(x.EmpName+"_"+x.EmpNo);
        }
        return  lstItem;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cache_data_view, menu);
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
