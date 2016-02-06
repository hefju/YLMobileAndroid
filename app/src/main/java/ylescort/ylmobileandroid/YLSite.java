package ylescort.ylmobileandroid;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import TaskClass.Box;
import TaskClass.Site;
import TaskClass.TasksManager;
import TaskClass.User;
import TaskClass.YLTask;
import YLDataService.WebServerYLSite;
import YLDataService.WebService;
import YLSystemDate.YLEditData;
import YLSystemDate.YLSysTime;
import YLSystemDate.YLSystem;
import YLAdapter.YLSiteAdapter;


public class YLSite extends ActionBarActivity {

    private TextView ylsite_tv_title;
    private ListView listView;
    private TasksManager tasksManager = null;//任务管理类
    private YLTask ylTask;//当前选中的任务
    private List<Site> siteList;
    private YLSiteAdapter ylSiteAdapter;

    private Button Site_apply;
    private Button Site_check;
    private Button Site_tmp;
    private Button Site_cartocar;
    private ProgressDialog progressDialog ;

    private WebService webService;

    private boolean checkcardownload;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ylsite);
        try {
            progressDialog =  new ProgressDialog(YLSite.this);
            tasksManager = YLSystem.getTasksManager();//获取任务管理类
            ylTask = tasksManager.CurrentTask;//当前选中的任务
            checkcardownload = false;

            webService = new  WebService();

            if (!ylTask.getTaskState().equals("有更新")) {
                ylTask.setTaskState("进行中");
            }

            siteList = new ArrayList<>();

            ylsite_tv_title = (TextView) findViewById(R.id.ylsite_tv_title);
            ylsite_tv_title.setText(ylTask.getLine());

            listView = (ListView) findViewById(R.id.ylsite_lv_MainView);
            Site_apply = (Button) findViewById(R.id.Site_apply);
            Site_check = (Button) findViewById(R.id.Site_check);
            Site_tmp = (Button) findViewById(R.id.Site_tmp);
            Site_cartocar = (Button) findViewById(R.id.Site_cartocar);


            Site_apply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShowApplyAcivity();
                }
            });

            Site_check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShowCheckAcivity();
                }
            });

            Site_tmp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ShowtmpActivity();
                }
            });

            Site_cartocar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {ShowcartocarActivity();}
            });


            DisplayTaskSite(ylTask.lstSite);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (checkcardownload) {
                        OpenBoxAct((ListView) parent, position);
                    }else{
                        Toast.makeText(getApplication(),"请点击右上角车内款箱按钮重新加载数据",Toast.LENGTH_SHORT).show();
                    }
                }
            });

            //增加载入自动更新0330kim
            if (!ylTask.getTaskState().equals("有更新")) {
                Toast.makeText(getApplicationContext(), "已经最新", Toast.LENGTH_SHORT).show();
            }else {
                GetSite();
            }
            GetCarBoxlist();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void ShowcartocarActivity() {
        Intent intent = new Intent();
        intent.setClass(this, YLCarToCarTask.class);
        startActivity(intent);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    private void ShowtmpActivity() {
        Intent intent = new Intent();
        intent.setClass(this, HomTmp_Scan.class);
        startActivity(intent);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    private void GetCarBoxlist() throws Exception {
        boolean getcarboxs = true;
            for (Site site : ylTask.getLstSite()) {
                if (site.getStatus() == null)continue;
                if (site.getStatus().equals("已完成")) {
                    getcarboxs = false;
                    checkcardownload = true;
                }
            }


//        Log.e(YLSystem.getKimTag(), getcarboxs + "车内款箱更新标识");
        if (getcarboxs) {
            CarBoxListAsy carBoxListAsy = new CarBoxListAsy();
            carBoxListAsy.execute(ylTask.getTaskID());
        }
        YLSite.this.setTitle("车内款箱数: " + ylTask.getLstCarBox().size());
    }

    private void GetSite() {
        WebServerYLSite webServerYLSite = new WebServerYLSite();
        List<Site> lstSite = null;
        try {
            User user = new User();
            user = YLSystem.getUser();
            user.setTaskDate(ylTask.getTaskID());
            lstSite = webServerYLSite.GetYLTaskSite(YLSystem.getUser(), getApplicationContext());
            tasksManager.MergeSite(lstSite);//同步本地的网点
            tasksManager.CurrentTask.setTaskState("进行中");
            DisplayTaskSite(ylTask.lstSite); //显示网点列表
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void ShowCheckAcivity() {
        Intent intent = new Intent();
        intent.setClass(this, LocalCheck.class);
        startActivity(intent);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    private void OpenBoxAct(ListView parent, int position) {


        ListView listView1 = (ListView) parent;
        Site site = (Site) listView1.getItemAtPosition(position);

        try {
            Sertime(webService.GetServerTime(getApplicationContext(), "SiteID:"+site.getSiteID()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        Intent intent = new Intent();
        intent.setClass(this, HomYLBoxScan.class);//新款箱扫描
        YLEditData.setCurrentYLSite(site);
        startActivity(intent);
        tasksManager.SaveTask(YLSite.this);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    private void Sertime(final String CurrentTime) {
        YLSysTime ylSysTime = new YLSysTime();
        ylSysTime.Sertime(CurrentTime);
    }

    public void YLSite_UpDate(View view) {
        dialog();
    }

    private void ShowApplyAcivity() {
        Intent intent = new Intent();
        intent.setClass(this, HandovermanCheck.class);
        Bundle bundle = new Bundle();
        bundle.putString("taskid", ylTask.getTaskID());
        bundle.putString("taskName", ylTask.getLine());
        intent.putExtras(bundle);
        startActivity(intent);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    private String UpLoadService() throws Exception {
        UploadAsycnTask uploadAsycnTask = new UploadAsycnTask();
        uploadAsycnTask.execute();
        return uploadAsycnTask.get();
    }

    private class UploadAsycnTask extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... params) {
            String url = YLSystem.GetBaseUrl(getApplicationContext()) + "UpLoad";
            Log.e(YLSystem.getKimTag(), url);
            HttpPost post = new HttpPost(url);
            Gson gson = new Gson();
            JSONObject p = new JSONObject();
            try {
                YLTask t1 = ylTask;
                t1.lstSite = ylTask.lstSite;
                t1.lstBox = ylTask.lstBox;
                p.put("STask", gson.toJson(t1));//整个任务=====================自定义。。。。。
                p.put("empid", YLSystem.getUser().EmpID);//人员id=====================自定义。。。。。
                p.put("deviceID", YLSystem.getUser().DeviceID);//手持机号=====================自定义。。。。。
                p.put("ISWIFI", YLSystem.getUser().ISWIFI);//是否用WIFI=====================自定义。。。。。
                post.setEntity(new StringEntity(p.toString(), "UTF-8"));
                post.setHeader(HTTP.CONTENT_TYPE, "text/json");
                HttpClient client = new DefaultHttpClient();
                HttpResponse response = client.execute(post);
                Log.e(YLSystem.getKimTag(), "箱数：" + t1.getLstBox().size() + "网点数：" + t1.getLstSite().size());
                if (response.getStatusLine().getStatusCode() == 200) {
                    String content = EntityUtils.toString(response.getEntity());
                    Log.e(YLSystem.getKimTag(), t1.lstBox.size() + "boxlist");
                    return gson.fromJson(content, new TypeToken<String>() {
                    }.getType());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private void DisplayTaskSite(List<Site> siteList) {
        if (siteList == null || siteList.size() < 1) return;
        ylSiteAdapter = new YLSiteAdapter(this, siteList, R.layout.activity_ylsiteitem);
        listView.setAdapter(ylSiteAdapter);

    }


    protected void dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(YLSite.this);
        builder.setMessage("确认上传吗?");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                try {
                    Date LoactTime = YLSysTime.GetDateCurrentTime();
                    Date UpdateTime = YLSysTime.StrToTime(ylTask.getServerReturn());
                    long longtime = LoactTime.getTime() - UpdateTime.getTime();
                    if (longtime <= 15000) {
                        Toast.makeText(getApplicationContext(), "请不要频繁上传数据", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    String serret = UpLoadService();
                    if (serret.equals("1")) {
                        ylTask.setTaskState("已上传");
                        ylTask.setServerReturn(YLSysTime.GetStrCurrentTime());
                        tasksManager.SaveTask(getApplicationContext());
                        UpDataDialog();
//                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "未上传成功,请重新上传", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "未上传成功,请连接网络重新上传", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void UpDataDialog() {
        new AlertDialog.Builder(YLSite.this).setTitle("提示")
                .setMessage("任务已上传，谢谢！")
                .setPositiveButton("确定", null).show();
    }


    private class CarBoxListAsy extends AsyncTask<String,Integer,List<Box>>{

        @Override
        protected void onPreExecute() {
            progressDialog.setCancelable(false);
            progressDialog.setMessage("正下载出库款箱数据，请稍候。");
            progressDialog.setMax(100);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.show();
        }

        @Override
        protected List<Box> doInBackground(String... strings) {
            String url = YLSystem.GetBaseUrl(getApplicationContext()) + "StoreGetBoxByTaskIDOut";
            HttpPost post = new HttpPost(url);
            Gson gson = new Gson();
            JSONObject p = new JSONObject();
            InputStream inputStream = null;
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            try {
                p.put("TaskID", strings[0]);
                p.put("deviceID", YLSystem.getHandsetIMEI());
                p.put("empid", YLSystem.getUser().getEmpID());
                post.setEntity(new StringEntity(p.toString(), "UTF-8"));
                post.setHeader(HTTP.CONTENT_TYPE, "text/json");
                HttpClient client = new DefaultHttpClient();
                HttpResponse response = client.execute(post);
                if (response.getStatusLine().getStatusCode() == 200) {
                    inputStream = response.getEntity().getContent();
                    long filelength = response.getEntity().getContentLength();
                    int len = 0;
                    byte[] data = new byte[1024];
                    int totallength = 0;
                    int value = 0;
                    while ((len = inputStream.read(data)) != -1) {
                        totallength += len;
                        value = (int) ((totallength / (float) filelength) * 100);
                        publishProgress(value);
                        outputStream.write(data, 0, len);
                    }
                    byte[] result = outputStream.toByteArray();
                    String content = new String(result, "UTF-8");
                    List<Box> boxList = gson.fromJson(content, new TypeToken<List<Box>>() {
                    }.getType());
                    Log.e(YLSystem.getKimTag(), boxList.get(0).toString() + "在车数量");
                    if (boxList.get(0).getBoxID() == null) {
                        boxList.clear();
                    }
                    ylTask.setLstCarBox(boxList);

                    tasksManager.SaveTask(YLSite.this);
                    checkcardownload = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressDialog.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(List<Box> boxes) {
            progressDialog.setProgress(0);
            YLSite.this.setTitle("车内款箱数: " + ylTask.getLstCarBox().size());
            progressDialog.dismiss();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4) {
            finish();
            overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ylsite, menu);
        if (!ylTask.getTaskState().equals("有更新")) {
            menu.removeItem(0);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.btnUpdateSite_ylsite) {
            try {
                GetCarBoxlist();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        if (ylTask.lstSite != null) {
            try {
                YLSite.this.setTitle("车内款箱数: " + ylTask.lstCarBox.size());
                ylSiteAdapter.notifyDataSetInvalidated();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        super.onResume();
    }
}
