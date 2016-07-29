package ylescort.ylmobileandroid;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
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
import YLDataService.WebServerBaseData;
import YLDataService.WebServerYLSite;
import YLDataService.WebService;
import YLSystemDate.YLEditData;
import YLSystemDate.YLRecord;
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

    private YLSitePrintAdapter ylSitePrintAdapterl;

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
                    YLRecord.WriteRecord("网点","进入核对");
                }
            });

            Site_check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    YLRecord.WriteRecord("网点","进入汇总");
                    ShowCheckAcivity();
                }
            });

            Site_tmp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    YLRecord.WriteRecord("网点","临时出入库");
                    ShowtmpActivity();
                }
            });

            Site_cartocar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    YLRecord.WriteRecord("网点","车组交接");
                    ShowcartocarActivity();
                }
            });


            DisplayTaskSite(ylTask.lstSite);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (checkcardownload || YLSystem.getNetWorkState().equals("2")) {
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
        YLRecord.WriteRecord("网点","进入车组交接"+ylTask.getTaskID());
        startActivity(intent);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    private void ShowtmpActivity() {
        Intent intent = new Intent();
        intent.setClass(this, HomTmp_Scan.class);
        YLRecord.WriteRecord("网点","进入临时出入库"+ylTask.getTaskID());
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
            YLRecord.WriteRecord("网点","获取车内款箱");
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
        intent.setClass(this, HomYLBoxScan.class);//款箱扫描
//        intent.setClass(this, YLtransfer.class);//新款箱扫描
        YLRecord.WriteRecord("网点","进入网点扫描："+site.getSiteName());
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
        try {
            WebServerYLSite w = new WebServerYLSite();
            String s =  w.UploadState(getApplicationContext(),ylTask.getTaskID());
            Log.e(YLSystem.getKimTag(),s+"上传返回");
            if (s != null){
                if (s.equals("1")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(YLSite.this);
                    builder.setMessage("已上传任务，再次上传会覆盖此前数据，是否确认上传?");
                    builder.setTitle("提示");
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            dialog();
                        }
                    });
                    builder.show();
                }
                else {
                    dialog();
                }
            }else {
                dialog();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void ShowApplyAcivity() {
        Intent intent = new Intent();
        intent.setClass(this, HandovermanCheck.class);
        Bundle bundle = new Bundle();
        bundle.putString("taskid", ylTask.getTaskID());
        bundle.putString("taskName", ylTask.getLine());
        intent.putExtras(bundle);
        YLRecord.WriteRecord("网点","进入统计界面"+ylTask.getTaskID());
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
        ylSitePrintAdapterl = new YLSitePrintAdapter
                (this, siteList, R.layout.activity_ylsiteprintitem);
        listView.setAdapter(ylSitePrintAdapterl);

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
                        YLRecord.WriteRecord("网点","上传成功"+ylTask.getTaskID());
                        UpDataDialog();
//                        finish();
                    } else {
                        YLRecord.WriteRecord("网点","上传失败"+ylTask.getTaskID());
                        Toast.makeText(getApplicationContext(), "未上传成功,请重新上传", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    YLRecord.WriteRecord("网点","网络原因上传失败"+ylTask.getTaskID());
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
                    YLRecord.WriteRecord("网点","更新车内款箱数："+ boxList.size());
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
            YLRecord.WriteRecord("网点","网点返回");
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

            try {
            WebServerBaseData webServerBaseData = new WebServerBaseData();
            webServerBaseData.CacheData(getApplicationContext());
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
                ylSitePrintAdapterl.notifyDataSetInvalidated();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        super.onResume();
    }

    public class YLSitePrintAdapter extends BaseAdapter {
        private List<Site> siteList;
        private int resource;
        private LayoutInflater inflater;
        public YLSitePrintAdapter(Context context,List<Site> siteList,int resource){
            this.siteList = siteList;
            this.resource = resource;
            inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        }


        @Override
        public int getCount() {
            return siteList.size();
        }

        @Override
        public Object getItem(int position) {
            return siteList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            Button sitePrint = null;
            TextView sitename = null;
            TextView sitestate = null;

            if (convertView == null) {
                convertView = inflater.inflate(resource, null);
                sitePrint = (Button) convertView.findViewById(R.id.ylsiteprint_print);
                sitename = (TextView) convertView.findViewById(R.id.ylsiteprint_sitename);
                sitestate = (TextView) convertView.findViewById(R.id.ylsiteprint_state);
                ViewCache viewCache = new ViewCache();
                viewCache.YLsitePrintview = sitePrint;
                viewCache.YLsiteNameview = sitename;
                viewCache.YLsitestateview = sitestate;
                convertView.setTag(viewCache);
            } else {
                ViewCache viewCache = (ViewCache) convertView.getTag();
                sitePrint = viewCache.YLsitePrintview;
                sitename = viewCache.YLsiteNameview;
                sitestate = viewCache.YLsitestateview;
            }

            final Site site = siteList.get(position);
            sitePrint.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (site.getStatus().equals("未交接"))return;
                    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter
                            .getDefaultAdapter();
                    if (!mBluetoothAdapter.isEnabled()) {
                        YLRecord.WriteRecord("网点","蓝牙未打开");
                        Intent mIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(mIntent, 1);
                    }else {

                        YLEditData.setPrintSite(site);
                        Intent intent = new Intent();
                        intent.setClass(YLSite.this, YLPrintActivity.class);
                        startActivity(intent);
                        YLRecord.WriteRecord("网点","进入打印"+site.getSiteName());
                        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                    }


                }
            });
            sitename.setText(site.getSiteName());
            sitestate.setText(site.getStatus());

            if (sitestate.getText().equals("已完成")){
                convertView.setBackgroundColor(getResources().getColor(R.color.androidyellowl));
            }else {
                convertView.setBackgroundColor(Color.TRANSPARENT);
            }
            return convertView;
        }


        private final class ViewCache{
            public Button YLsitePrintview;
            public TextView YLsiteNameview;
            public TextView YLsitestateview;
        }
    }


}
