package ylescort.ylmobileandroid;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import TaskClass.Box;
import TaskClass.TasksManager;
import TaskClass.YLTask;
import YLAdapter.YLTaskAdapter;
import YLSystemDate.YLEditData;
import YLSystemDate.YLSystem;

public class YLCarToCarTask extends YLBaseActivity implements View.OnClickListener {


    private TextView ylcartocartask_tv_title;
    private ListView ylcartocartask_lv;
    private Button ylcartocartask_btn_give;
    private Button ylcartocartask_btn_get;

    private List<YLTask> ylTaskList;
    private YLTaskAdapter ylTaskAdapter;
    private TasksManager tasksManager;
    private YLTask ylTask;
    private int getorgive;//1送2收
    private boolean clickfalg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ylcar_to_car_task);
        InitLayout();
        InitData();
    }

    @Override
    protected void InitLayout() {
        ylcartocartask_tv_title = (TextView) findViewById(R.id.ylcartocartask_tv_title);
        ylcartocartask_lv = (ListView) findViewById(R.id.ylcartocartask_lv);
        ylcartocartask_btn_give = (Button)findViewById(R.id.ylcartocartask_btn_give);
        ylcartocartask_btn_get = (Button)findViewById(R.id.ylcartocartask_btn_get);

        ylcartocartask_btn_give.setOnClickListener(this);
        ylcartocartask_btn_get.setOnClickListener(this);
        ylcartocartask_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ListView listView = (ListView) adapterView;
                YLTask lvylTask = (YLTask) listView.getItemAtPosition(i);

                YLEditData.setCarToCarylTask(lvylTask);
                YLEditData.setCarToCargetorgive(getorgive);

                YLEditData.setYlcarbox(ylTask.getLstCarBox());

                YLstartActivity(YLCarToCarScan.class);
            }
        });
    }

    @Override
    protected void InitData() {
        tasksManager = YLSystem.getTasksManager();//获取任务管理类
        ylTask = tasksManager.CurrentTask;//当前选中的任务
        YLEditData.setYlTask(ylTask);
        clickfalg = true;

        ylTaskList = new ArrayList<>();
        DisplayYLTaskList();

    }

    private void DisplayYLTaskList()  {
        try {
            ylTaskAdapter =  new YLTaskAdapter(this,ylTaskList,R.layout.activity_taskitem);
            ylcartocartask_lv.setAdapter(ylTaskAdapter);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ylcartocartask_btn_give:
                if (clickfalg){
                    clickfalg = false;
                    GetCarToCarTask("1");
                    ylcartocartask_tv_title.setText("送箱任务列表");
                    getorgive = 1;
                }

                break;
            case R.id.ylcartocartask_btn_get:

                if (clickfalg) {
                    clickfalg = false;
                    GetCarToCarTask("2");
                    ylcartocartask_tv_title.setText("接箱任务列表");
                    getorgive = 2;
                }
                break;
        }
    }

    private void GetCarToCarTask(String getorgive) {

        try {
            JSONObject jsonObject = new JSONObject();

            jsonObject.put("TaskID",YLEditData.getYlTask().getTaskID());
            jsonObject.put("TimeID",getorgive);
            jsonObject.put("empid",YLSystem.getUser().getEmpID());
            jsonObject.put("deviceID",YLSystem.getHandsetIMEI());
            jsonObject.put("ISWIFI",YLSystem.getNetWorkState());
            String url = YLSystem.GetBaseUrl(getApplicationContext())+"GetTaskCarToCar";

//            jsonObject.put("DeviceID", YLSystem.getHandsetIMEI());
//            jsonObject.put("ISWIFI", YLSystem.getNetWorkState());
//            jsonObject.put("datetime", "ALL");
//            String url = YLSystem.GetBaseUrl(getApplicationContext())+"GetBaseBox";

            YLWebDataAsyTaskForeground  yatf = new YLWebDataAsyTaskForeground(jsonObject,url) {
                @Override
                protected void onPostExecute(String s) {
                    YLProgressDialog.dismiss();
                    ylTaskList = gson.fromJson(s,new TypeToken<List<YLTask>>(){}.getType());
                    if (ylTaskList == null){
                        ylTaskList = new ArrayList<>();
                        YLMessagebox("网络出错，请重新下载");
                    }else {
                        if(ylTaskList.get(0).getServerReturn().equals("0")){
                            ylTaskList = new ArrayList<>();
                            YLMessagebox("未有任务，请联系调度室");
                        }
                    }
                    MyLog(ylTaskList.toString());
                    DisplayYLTaskList();
                    clickfalg = true;
//                    List<Box> boxes = gson.fromJson(s,new TypeToken<List<Box>>(){}.getType());
//                    if (boxes  == null){
//                        ylcartocartask_tv_title.setText("下载失败");
//                    }else {
//                        ylcartocartask_tv_title.setText(boxes.size()+"下载数量");
//                    }

                }
            };

            yatf.execute();
            yatf.doInBackground();

        }catch (Exception e){
            YLProgressDialog.dismiss();
            e.printStackTrace();
        }

    }

    @Override
    protected void onPostResume() {

        super.onPostResume();
    }
}
