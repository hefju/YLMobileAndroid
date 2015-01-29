package ylescort.ylmobileandroid;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;


public class Task extends ActionBarActivity {

    private TextView textView;
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        /*
        Bundle bundle = this.getIntent().getExtras();
        String Name = bundle.getString("AName");
        textView = (TextView)findViewById(R.id.TaskTital);
        textView.setText(Name);
        */
        listView = (ListView)findViewById(R.id.Task_lv_mlistview);
        try {
            LoadData();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
         listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
             @Override
             public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                 /*获取列表项目数据
                 ListView lView = (ListView)parent;
                 HashMap<String,String> map=(HashMap<String,String>)lView.getItemAtPosition(position);
                 String title=map.get("任务名称");
                 Toast.makeText(Task.this,title,Toast.LENGTH_SHORT).show();
                 */

                 Intent intent = new Intent();
                 intent.setClass(Task.this,box.class);
                 Bundle bundle = new Bundle();
                 bundle.putString("AName","Kim");
                 intent.putExtras(bundle);
                 startActivity(intent);

             }
         });

    }




    public void LoadData() throws ClassNotFoundException {

       // listView = (ListView)findViewById(R.id.Task_listView);

        //生成动态数组，加入数据
        ArrayList<HashMap<String, Object>> listItem = new ArrayList<>();

        for(int i=0;i<10;i++)
        {
            HashMap<String, Object> map = new HashMap<>();
            map.put("任务名称", "Name "+i);
            map.put("任务类型", "Stype2 "+i);
            map.put("任务状态", "stateLoadData");
            listItem.add(map);
        }
        //生成适配器的Item和动态数组对应的元素
        SimpleAdapter listItemAdapter = new SimpleAdapter(this,listItem,//数据源
                R.layout.activity_task,//ListItem的XML实现
                //动态数组与ImageItem对应的子项
                new String[] {"任务名称","任务类型", "任务状态"},
                //ImageItem的XML文件里面的一个ImageView,两个TextView ID
                new int[] {R.id.Task_taskname,R.id.Task_taskstype,R.id.Task_taskstaut}
        );

        //添加并且显示
        listView.setAdapter(listItemAdapter);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_task_, menu);
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
