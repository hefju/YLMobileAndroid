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
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import TaskClass.Site;
import TaskClass.TasksManager;
import TaskClass.User;
import TaskClass.YLTask;
import YLDataService.SiteDBSer;
import YLDataService.TaskDBSer;
import YLDataService.WebService;
import YLSystem.YLSystem;
import adapter.YLTaskAdapter;


public class Task extends ActionBarActivity {

    private TextView textView;
    private ListView listView; //显示任务列表
    private android.os.Handler handler = null;
    private TasksManager tasksManager = null;//任务管理类
    private CalendarView calendarView;//日期选择控件
    private TextView txt_Date_Task;   //显示当前选中的日期
    private Button Task_btn_refresh;  //从网上下载任务数据
    private LinearLayout pnlDownMenu_Task;
    private Button btnCancel_Task;  //从网上下载任务数据
    private Button btnDownload_Task;  //从网上下载任务数据
    private DatePicker yltask_datepicker;//日期控件

    android.os.Handler mHandler; //消息处理


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        Task.this.setTitle("任务管理: "+YLSystem.getUser().getName());
        tasksManager=YLSystem.getTasksManager();//获取任务管理类
        yltask_datepicker = (DatePicker)findViewById(R.id.yltask_datepicker);
//        txt_Date_Task=(TextView)findViewById(R.id.txt_Date_Task);//显示当前日期
//        calendarView=(CalendarView)findViewById(R.id.calendarViewTask);
//        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
//            @Override
//            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
//                month=month+1;
//                txt_Date_Task.setText( "日期:"+year+"年"+month+"月"+dayOfMonth+"日");
//                //txt_Date_Task.setTag(year+"-"+month+"-"+dayOfMonth);
////                Log.d("jutest","月:"+ String.valueOf(month));
////                if(dayOfMonth==28){
////                    Log.d("jutest","onSelectedDayChange:28");
////                    calendarView.setVisibility(View.GONE);
////                    pnlDownMenu_Task.setVisibility(View.VISIBLE);
////                }
//            }
//        });
//        Calendar cal = Calendar.getInstance();
//        long now=cal.getTimeInMillis();
//        cal.set(2000,1,1);
//        long tmpTime=cal.getTimeInMillis();
//        calendarView.setDate(tmpTime);
//        calendarView.setDate(now);

//        pnlDownMenu_Task=(LinearLayout)findViewById(R.id.pnlDownMenu_Task);//操作菜单
//        btnCancel_Task=(Button)findViewById(R.id.btnCancel_Task);//关闭操作菜单
//        btnDownload_Task=(Button)findViewById(R.id.btnDownload_Task);//下载任务
//        btnCancel_Task.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                pnlDownMenu_Task.setVisibility(View.GONE);
//                calendarView.setVisibility(View.VISIBLE);
//            }
//        });
//        btnDownload_Task.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d("jutest","btnDownload_Task:click");
//            }
//        });

        //刷新按钮
        Task_btn_refresh =(Button)findViewById(R.id.Task_btn_refresh);//刷新任务列表
        Task_btn_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              String time= GetCalendarViewTime();//用户选中的日期
              tasksManager.Loading(getApplicationContext(),time);//加载本地数据
              DisplayTaskList(tasksManager.lstLatestTask);//显示本地任务列表

              if(YLSystem.isNetConnected(Task.this)){
                  User user=YLSystem.getUser();
                  user.setTaskDate(time);
                  Task_btn_refresh.setEnabled(false);//禁止刷新按钮
                  WebService.GetTaskList(getApplicationContext(),mHandler);
                  Toast.makeText(getApplicationContext(), "正在获取...", Toast.LENGTH_SHORT).show();
              }else{
                Toast.makeText(getApplicationContext(), "没有网络.", Toast.LENGTH_LONG).show();
              }
            }
        });
        /*
        Bundle bundle = this.getIntent().getExtras();
        String Name = bundle.getString("AName");
        textView = (TextView)findViewById(R.id.TaskTital);
        textView.setText(Name);
        */
        listView = (ListView)findViewById(R.id.Task_lv_mlistview);
        //LocaData();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
             @Override
             public void onItemClick (AdapterView<?> parent, View view, int position, long id) {

                 ListView listView1 = (ListView)parent;
                 YLTask ylTask = (YLTask)listView1.getItemAtPosition(position);
                 String tasktype =  ylTask.getTaskType();

//                 Toast.makeText(Task.this,ylTask.getTaskID().toString(),Toast.LENGTH_SHORT).show();

                tasksManager.SetCurrentTask(ylTask.getTaskID());//设置选中的任务

                 Intent intent = new Intent();
                 if (tasktype.equals("ATM清机")){
                     intent.setClass(Task.this,YLATMList.class);
                 }else {
                     intent.setClass(Task.this,YLSite.class);
                 }

//                 Bundle bundle = new Bundle();
//                 bundle.putString("taskid",ylTask.getTaskID().toString());
//                 intent.putExtras(bundle);
                 startActivity(intent);

             }
         });


        //用于回传数据更新UI
        mHandler = new android.os.Handler(){
            @Override
            public void handleMessage(Message msg) {

                //String content = (String) msg.obj;
                switch (msg.what) {
                    case 1:
                        break;
                    case 20: //获取GetTaskList成功
                        Log.d("jutest","从服务器获取GetTaskList成功");
                        List<YLTask> lstYLTask  = (List<YLTask>) msg.obj;
                        UpdateLocalTaskList(lstYLTask);
//                        tasksManager.Merge(GetCalendarViewTime(),lstYLTask);
//                        FillData(lstYLTask);
                        DisplayTaskList(tasksManager.lstLatestTask);
                        Task_btn_refresh.setEnabled(true);//可以再次点击刷新了
                        break;
                    case 21://获取GetTaskList失败, 服务器返回值不等于1, 获取数据失败
                        String content = (String) msg.obj;
                        Toast.makeText(getApplicationContext(),content,Toast.LENGTH_LONG).show();
                        Task_btn_refresh.setEnabled(true);//可以再次点击刷新了
                        break;
                    case 100:
                        break;
                    default:
                        break;
                }
                super.handleMessage(msg);
            }
        };
    }

    //显示本地任务列表
    private void DisplayTaskList( List<YLTask> lstYLTask) {
        if(lstYLTask==null || lstYLTask.size()<1)return;
        YLTaskAdapter ylTaskAdapter =  new YLTaskAdapter(this,lstYLTask,R.layout.activity_taskitem);
        listView.setAdapter(ylTaskAdapter);
    }

    //下载的任务列表跟本地的任务列表比较, 整理出最终的任务列表
    private void UpdateLocalTaskList(List<YLTask> lstYLTask) {
        //测试的代码.
        for(YLTask x:lstYLTask){
            x.setTaskDate(GetCalendarViewTime());
        }
        //测试代码结束
        //Log.d("jutest",)
        List<YLTask> lstlocal=tasksManager.lstLatestTask;
        if(lstlocal==null) {
            for (YLTask l:lstYLTask){
                l.setTaskState("有更新");
            }
            tasksManager.lstLatestTask = (ArrayList) lstYLTask;
        }
        else{
            for(YLTask remote: lstYLTask) {
                YLTask local=GetRemoteYLTask(remote.getTaskID(),lstlocal);
                if(local!=null){
                    int v0=Integer.parseInt(local.getServerVersion());
                    int v1=Integer.parseInt(remote.getServerVersion());
                    if(v1>v0){
                        local.setTaskState("有更新");
                       // local设置状态
                    }
                }else{
                    remote.setTaskState("有更新");
                    lstlocal.add(remote);
                }
            }

            //删除客户端有,服务器端没有的任务.  如果不要"已删除"状态, 这个方法就不是这样写了.
            int max=lstlocal.size();
            for (int i=max-1;i>=0;i--){
                YLTask l=lstlocal.get(i);
                if(GetRemoteYLTask(l.getTaskID(),lstYLTask)==null){
                    lstlocal.remove(i);
                }
            }
//            for (YLTask l:lstlocal){
//                if(GetRemoteYLTask(l.getTaskID(),lstYLTask)==null)
//                    l.setTaskState("已删除");
//            }
        }
    }

    private YLTask GetRemoteYLTask(String taskID, List<YLTask> lstYLTask) {
        for (YLTask x:lstYLTask){
            if (x.getTaskID().equals(taskID)){
                return x;
            }
        }
        return null;
    }

//    private boolean FillDataFromLocal(String time) {
//        TaskDBSer dbSer=new TaskDBSer(getApplicationContext());
//        List<YLTask> lstYLTask =dbSer.FindTaskIDByDate(time);
//        tasksManager.Merge(time,lstYLTask);
//        return lstYLTask.size()>0;
//    }


    //将任务列表显示到界面上
    private void FillData(List<YLTask> lstYLTask) {
        YLTaskAdapter ylTaskAdapter =  new YLTaskAdapter(this,lstYLTask,R.layout.activity_taskitem);
        listView.setAdapter(ylTaskAdapter);
    }

    private  String GetCalendarViewTime(){
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        int year         = yltask_datepicker.getYear();
        int monthOfYear  = yltask_datepicker.getMonth();
        int dayOfMonth   = yltask_datepicker.getDayOfMonth();
        calendar.set(year,monthOfYear,dayOfMonth);
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");
        String time=format.format(calendar.getTime());
        return  time;
    }

//    private List<YLTask> GetTaskList() {
//        if(tasksManager==null)
//        {
//            String taskdate= txt_Date_Task.getTag().toString();
//            tasksManager=new TasksManager();
//            if(tasksManager.GetTaskListFromLoacl(taskdate,getApplicationContext())==false) {
//                tasksManager.DownTaskList();
//            }
//        }
//        List<YLTask> ylTaskList=null;
//        return ylTaskList;
//    }

    private void LocaData() {
        //从本地读取数据, 如果没有, 就从网上下载数据
        TaskDBSer  taskDBSer = new TaskDBSer(getApplicationContext());
        List<YLTask> ylTaskList = taskDBSer.SelTaskbydatetolist("2014-08-07");
        YLTaskAdapter ylTaskAdapter =  new YLTaskAdapter(this,ylTaskList,R.layout.activity_taskitem);
        listView.setAdapter(ylTaskAdapter);
    }


    ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();

    android.os.Handler mh = new android.os.Handler() {   //以Handler为桥梁将结果传入UI
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    public void YLtask(View view) throws ClassNotFoundException {
        /*
        Intent intent = new Intent();
        intent.setClass(Login.this, Task.class);
        Bundle bundle = new Bundle();
        bundle.putString("AName","Kim");
        intent.putExtras(bundle);
        startActivity(intent);
        */

        final User user = YLSystem.getUser();

//        final User user = new User();
//        user.EmpNO="600241";
//        user.Name="杨磊";
//        user.Pass= YLSystem.md5("600241");
//        user.DeviceID="NH008";
//        user.ISWIFI="1";
//        user.EmpID="2703";
//        user.TaskDate= "2014.08.07";


        //获取任务
        GetTask(user);

        TaskDBSer  taskDBSer = new TaskDBSer(getApplicationContext());
        final List<String> taskid = taskDBSer.SelTaskID2(user.getTaskDate());
        //获取网点
        for (String singtaskid :taskid){
            GetSite(user, singtaskid);
        }

       // List<YLTask> ylTaskList = taskDBSer.SelTaskbydatetolist("2014-08-07");
        //GetYLBoxLayoutControl(ylTaskList);
    }

    private void GetSite(final User user, final String taskid) {
        singleThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    String url =  "http://58.252.75.149:8055/YLMobileServiceAndroid.svc/GetTaskStie";
                    HttpPost post = new HttpPost(url);
                    //添加数值到User类
                    Gson gson = new Gson();
                    //设置POST请求中的参数
                    JSONObject p = new JSONObject();
                    p.put("taskID",taskid);
                    p.put("deviceID",user.getDeviceID());
                    p.put("empid",user.getEmpID());
                    p.put("ISWIFI",user.getISWIFI());

                    post.setEntity(new StringEntity(p.toString(), "UTF-8"));//将参数设置入POST请求
                    post.setHeader(HTTP.CONTENT_TYPE, "text/json");//设置为json格式。
                    HttpClient client = new DefaultHttpClient();
                    HttpResponse response = client.execute(post);
                    if (response.getStatusLine().getStatusCode() == 200) {
                        String webcontent = EntityUtils.toString(response.getEntity());
                        List<Site> siteList = new ArrayList<Site>();
                        siteList = gson.fromJson(webcontent,new TypeToken<List<Site>>(){}.getType());

                        for (Site site:siteList){
                            if (site.getServerReturn().equals("此任务没有网点。")){
                                continue;
                            }
                            SiteDBSer siteDBSer = new SiteDBSer(getApplicationContext());
                            siteDBSer.InsertSite2(site);
                            Log.d("WCF", site.getSiteName());
                        }
                        if (webcontent.equals("1")){
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
    }

    private void GetTask(final User user) {
        singleThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    String url =  "http://58.252.75.149:8055/YLMobileServiceAndroid.svc/GetTask1";
                    HttpPost post = new HttpPost(url);
                    //添加数值到User类

                    Gson gson = new Gson();
                    //设置POST请求中的参数
                    JSONObject p = new JSONObject();
                    p.put("user", gson.toJson(user));//将User类转换成Json传到服务器。
                    post.setEntity(new StringEntity(p.toString(), "UTF-8"));//将参数设置入POST请求
                    post.setHeader(HTTP.CONTENT_TYPE, "text/json");//设置为json格式。
                    HttpClient client = new DefaultHttpClient();
                    HttpResponse response = client.execute(post);
                    if (response.getStatusLine().getStatusCode() == 200) {
                        String webcontent = EntityUtils.toString(response.getEntity());
                        List<YLTask> ylTaskList = new ArrayList<YLTask>();
                        ylTaskList = gson.fromJson(webcontent,new TypeToken<List<YLTask>>(){}.getType());

                        for (YLTask ylTask:ylTaskList){
                            if (ylTask.getServerReturn().equals("没有任务。")){
                                continue;
                            }
                            TaskDBSer taskDBSer = new TaskDBSer(getApplicationContext());
                            taskDBSer.InsertYLTask2(ylTask);
                            Log.d("WCF", ylTask.getLine());
                        }
                        if (webcontent.equals("1")){
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
    }

    public void LoadData(List<YLTask> ylTaskList) throws ClassNotFoundException {
         listView = (ListView)findViewById(R.id.Task_lv_mlistview);

        //生成动态数组，加入数据
        ArrayList<HashMap<String, Object>> listItem = new ArrayList<>();

        for (YLTask ylTask:ylTaskList){
            HashMap<String, Object> map = new HashMap<>();
            map.put("任务名称",ylTask.getLine());
            map.put("任务类型",ylTask.getTaskType());
            map.put("任务状态","未完成");
            map.put("taskid",ylTask.getId());
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

    @Override
    protected void onResume() {
        DisplayTaskList(tasksManager.lstLatestTask);//显示本地任务列表
        super.onResume();
    }

}
