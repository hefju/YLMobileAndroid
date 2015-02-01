package ylescort.ylmobileandroid;

import android.content.Intent;
import android.database.Cursor;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import TaskClass.User;
import TaskClass.YLTask;
import YLDataService.WebService;
import YLSystem.YLSystem;


public class Task extends ActionBarActivity {

    private TextView textView;
    private ListView listView;
    private android.os.Handler handler = null;
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
           // LoadData();

        } catch (Exception e) {
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



    ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();

    android.os.Handler mh = new android.os.Handler() {   //以Handler为桥梁将结果传入UI
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };


    protected   void GetTaskData() throws Exception{

        Gson gson = new Gson();
        WebService webService = new WebService();
        User user = new User();
        user.EmpNO="600241";
        user.Name="杨磊";
        user.Pass= YLSystem.md5("600241");
        user.DeviceID="NH008";
        user.ISWIFI="1";
        user.EmpID="2703";
        user.TaskDate= "2014-08-07";
        String mather = "GetTask1";
        String webcontent = null;

        webcontent =  asyncTask(user,mather);

        List<YLTask> ylTaskList = new ArrayList<YLTask>();

        ylTaskList= gson.fromJson(webcontent, new TypeToken<List<YLTask>>() {
        }.getType());
        Log.d("YLtest",ylTaskList.toString());
    }



    private String asyncTask(final User user, final String mather) {

        final android.os.Handler handler = new android.os.Handler(){

            public void handleMessage(Message message)
            {
                try {
                    WebService.TaskWebContent(mather,user);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    handler.sendMessage(handler.obtainMessage());
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();
        return "";
    }


    public void YLtask(View view) throws ClassNotFoundException {
        /*
        Intent intent = new Intent();
        intent.setClass(Login.this, Task.class);
        Bundle bundle = new Bundle();
        bundle.putString("AName","Kim");
        intent.putExtras(bundle);
        startActivity(intent);
        */
        /*
        singleThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    String url =  "http://58.252.75.149:8055/YLMobileServiceAndroid.svc/GetTask1";
                    HttpPost post = new HttpPost(url);
                    //添加数值到User类
                    User user = new User();
                    user.EmpNO="600241";
                    user.Name="杨磊";
                    user.Pass= YLSystem.md5("600241");
                    user.DeviceID="NH008";
                    user.ISWIFI="1";
                    user.EmpID="2703";
                    user.TaskDate= "2014-08-07";
                    Gson gson = new Gson();
                    //设置POST请求中的参数
                    JSONObject p = new JSONObject();
                    p.put("user", gson.toJson(user));//将User类转换成Json传到服务器。
                    post.setEntity(new StringEntity(p.toString(), "UTF-8"));//将参数设置入POST请求
                    post.setHeader(HTTP.CONTENT_TYPE, "text/json");//设置为json格式。
                    HttpClient client = new DefaultHttpClient();
                    HttpResponse response = client.execute(post);
                    Log.d("WCF","fhe");  //得到返回字符串
                    if (response.getStatusLine().getStatusCode() == 200) {
                        String content = EntityUtils.toString(response.getEntity());
                        Log.d("WCF",content);  //得到返回字符串


                        if (content.equals("1")){
                            mh.sendEmptyMessage(0);
                        }
                        else {

                            mh.sendEmptyMessage(0);
                        }

                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
*/
        WebService webService = new WebService();
        try {
            String webconte =  webService.gettask();
            Log.d("Tasktest",webconte);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void LoadData() throws ClassNotFoundException {
         listView = (ListView)findViewById(R.id.Task_lv_mlistview);

        //生成动态数组，加入数据
        ArrayList<HashMap<String, Object>> listItem = new ArrayList<>();

        for(int i=0;i<5;i++)
        {
            HashMap<String, Object> map = new HashMap<>();
            map.put("任务名称", "stateLoad "+i);
            map.put("任务类型", "Stype "+i);
            map.put("任务状态", "state");
            listItem.add(map);
        }
        //生成适配器的Item和动态数组对应的元素
        SimpleAdapter listItemAdapter = new SimpleAdapter(this,listItem,//数据源
                R.layout.activity_taskitem,//ListItem的XML实现
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
