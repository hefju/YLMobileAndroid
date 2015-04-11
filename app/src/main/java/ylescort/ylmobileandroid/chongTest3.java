package ylescort.ylmobileandroid;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import TaskClass.ArriveTime;
import TaskClass.BaseBox;
import TaskClass.BaseClient;
import TaskClass.BaseEmp;
import TaskClass.BaseSite;
import TaskClass.Box;
import TaskClass.Site;
import TaskClass.User;
import TaskClass.Vision;
import TaskClass.YLTask;
import YLSystemDate.YLSystem;


public class chongTest3 extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chong_test3);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chong_test3, menu);
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

    ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
    private List<String> listViewdata = new ArrayList<String>();
    Handler mh = new Handler() {   //以Handler为桥梁将结果传入UI
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            // ListView listView = (ListView) findViewById(R.id.listView);
            //  listView.setAdapter(new ArrayAdapter<String>(CHONG_TEST.this, android.R.layout.simple_expandable_list_item_1, listViewdata));
            Toast.makeText(getApplicationContext(), "储存在每个方法的内存里。调试里能看到。", Toast.LENGTH_LONG).show();
        }
    };

    //获取全部员工
    public void chongtest_emp(View view) throws ClassNotFoundException {

        singleThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    //添加数值到User类
                    User s1 = new User();
                    s1.EmpNO = "200097";
                    s1.Name = "徐竞航";
                    s1.Pass = "8c4d6ed1b2688b2373bcac4137fab1e6";
                    s1.DeviceID = "NH008";
                    s1.ISWIFI = "1";
                    //以上是测试数据可以修改============================================================================

                    String url = YLSystem.GetBaseUrl(getApplicationContext())+"GetBaseEmp";
                    HttpPost post = new HttpPost(url);
                    //添加数值到User类
                    Gson gson = new Gson();
                    //设置POST请求中的参数-------返回EmpID员工ID，ServerReturn 服务器错误，1为没错误，Time 服务器时间。
                    JSONObject p = new JSONObject();
                    p.put("DeviceID", s1.DeviceID);//手持机号=====================自定义。。。。。
                    p.put("ISWIFI", s1.ISWIFI);//是否用WIFI=====================自定义。。。。。
                    p.put("datetime", "2010-01-01 00:00:00");//ALL下载全部，写入时间为下载包含时间以内的所有记录。按秒计算，如：2015-02-02 12:23:34=====================自定义。。。。。
                    post.setEntity(new StringEntity(p.toString(), "UTF-8"));//将参数设置入POST请求
                    post.setHeader(HTTP.CONTENT_TYPE, "text/json");//设置为json格式。
                    HttpClient client = new DefaultHttpClient();
                    HttpResponse response = client.execute(post);
                    if (response.getStatusLine().getStatusCode() == 200) {
                        String content = EntityUtils.toString(response.getEntity());    //得到返回字符串

                        //存储在ListBaseEmp里=============================================================================================
                        List<BaseEmp> ListTemp=new ArrayList<BaseEmp>();
                        ListTemp = gson.fromJson(content, new TypeToken<List<BaseEmp>>() {
                        }.getType());
                        //=================================================================================================================


                        listViewdata.clear();
                        for (BaseEmp classtemp : ListTemp) {
                            //返回的ServerReturn值==1的时候就代表成功。不是1的时候代表失败。===========================================
                            if (classtemp.ServerReturn.equals("1")) {
                                listViewdata.add(classtemp.EmpName);
                            }
                        }
                        Log.d("WCF", "ok");//打印到logcat

                        mh.sendEmptyMessage(0);


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
                    e.printStackTrace();
                }
            }
        });

    }
    //获取全部客户
    public void chongtest_client(View view) throws ClassNotFoundException {

        singleThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    //添加数值到User类
                    User s1 = new User();
                    s1.EmpNO = "200097";
                    s1.Name = "徐竞航";
                    s1.Pass = "8c4d6ed1b2688b2373bcac4137fab1e6";
                    s1.DeviceID = "NH008";
                    s1.ISWIFI = "1";
                    //以上是测试数据可以修改============================================================================

                    String url = YLSystem.GetBaseUrl(getApplicationContext())+"GetBaseClient";

                    HttpPost post = new HttpPost(url);
                    //添加数值到User类
                    Gson gson = new Gson();
                    //设置POST请求中的参数-------返回EmpID员工ID，ServerReturn 服务器错误，1为没错误，Time 服务器时间。
                    JSONObject p = new JSONObject();
                    p.put("DeviceID", s1.DeviceID);//手持机号=====================自定义。。。。。
                    p.put("ISWIFI", s1.ISWIFI);//是否用WIFI=====================自定义。。。。。
                    p.put("datetime", "ALL");//ALL下载全部，写入时间为下载包含时间以内的所有记录。按秒计算，如：2015-02-02 12:23:34=====================自定义。。。。。
                    post.setEntity(new StringEntity(p.toString(), "UTF-8"));//将参数设置入POST请求
                    post.setHeader(HTTP.CONTENT_TYPE, "text/json");//设置为json格式。
                    HttpClient client = new DefaultHttpClient();
                    HttpResponse response = client.execute(post);
                    if (response.getStatusLine().getStatusCode() == 200) {
                        String content = EntityUtils.toString(response.getEntity());    //得到返回字符串

                        //存储在ListBaseEmp里=============================================================================================
                        List<BaseClient> ListTemp=new ArrayList<BaseClient>();
                        ListTemp = gson.fromJson(content, new TypeToken<List<BaseClient>>() {
                        }.getType());
                        //=================================================================================================================


                        listViewdata.clear();
                        for (BaseClient classtemp : ListTemp) {
                            //返回的ServerReturn值==1的时候就代表成功。不是1的时候代表失败。===========================================
                            if (classtemp.ServerReturn.equals("1")) {
                                listViewdata.add(classtemp.ClientName);
                            }
                        }
                        Log.d("WCF",  "ok");//打印到logcat

                        mh.sendEmptyMessage(0);


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
                    e.printStackTrace();
                }
            }
        });

    }
    //获取全部网点
    public void chongtest_site(View view) throws ClassNotFoundException {

        singleThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    //添加数值到User类
                    User s1 = new User();
                    s1.EmpNO = "200097";
                    s1.Name = "徐竞航";
                    s1.Pass = "8c4d6ed1b2688b2373bcac4137fab1e6";
                    s1.DeviceID = "NH008";
                    s1.ISWIFI = "1";
                    //以上是测试数据可以修改============================================================================

                    String url = YLSystem.GetBaseUrl(getApplicationContext())+"GetBaseSite";
                    HttpPost post = new HttpPost(url);
                    //添加数值到User类
                    Gson gson = new Gson();
                    //设置POST请求中的参数-------返回EmpID员工ID，ServerReturn 服务器错误，1为没错误，Time 服务器时间。
                    JSONObject p = new JSONObject();
                    p.put("DeviceID", s1.DeviceID);//手持机号=====================自定义。。。。。
                    p.put("ISWIFI", s1.ISWIFI);//是否用WIFI=====================自定义。。。。。
                    p.put("datetime", "ALL");//ALL下载全部，写入时间为下载包含时间以内的所有记录。按秒计算，如：2015-02-02 12:23:34=====================自定义。。。。。
                    post.setEntity(new StringEntity(p.toString(), "UTF-8"));//将参数设置入POST请求
                    post.setHeader(HTTP.CONTENT_TYPE, "text/json");//设置为json格式。
                    HttpClient client = new DefaultHttpClient();
                    HttpResponse response = client.execute(post);
                    if (response.getStatusLine().getStatusCode() == 200) {
                        String content = EntityUtils.toString(response.getEntity());    //得到返回字符串

                        //存储在ListBaseEmp里=============================================================================================
                        List<BaseSite> ListTemp=new ArrayList<BaseSite>();
                        ListTemp = gson.fromJson(content, new TypeToken<List<BaseSite>>() {
                        }.getType());
                        //=================================================================================================================


                        listViewdata.clear();
                        for (BaseSite classtemp : ListTemp) {
                            //返回的ServerReturn值==1的时候就代表成功。不是1的时候代表失败。===========================================
                            if (classtemp.ServerReturn.equals("1"))
                            {
                                listViewdata.add(classtemp.SiteName);
                            }
                        }
                        Log.d("WCF",  "ok");//打印到logcat

                        mh.sendEmptyMessage(0);


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
                    e.printStackTrace();
                }
            }
        });

    }
    //获取全部箱
    public void chongtest_box(View view) throws ClassNotFoundException {

        singleThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    //添加数值到User类
                    User s1 = new User();
                    s1.EmpNO = "200097";
                    s1.Name = "徐竞航";
                    s1.Pass = "8c4d6ed1b2688b2373bcac4137fab1e6";
                    s1.DeviceID = "NH008";
                    s1.ISWIFI = "1";
                    //以上是测试数据可以修改============================================================================

                    String url = YLSystem.GetBaseUrl(getApplicationContext())+"GetBaseBox";
                    HttpPost post = new HttpPost(url);
                    //添加数值到User类
                    Gson gson = new Gson();
                    //设置POST请求中的参数-------返回EmpID员工ID，ServerReturn 服务器错误，1为没错误，Time 服务器时间。
                    JSONObject p = new JSONObject();
                    p.put("DeviceID", s1.DeviceID);//手持机号=====================自定义。。。。。
                    p.put("ISWIFI", s1.ISWIFI);//是否用WIFI=====================自定义。。。。。
                    p.put("datetime", "ALL");//ALL下载全部，写入时间为下载包含时间以内的所有记录。按秒计算，如：2015-02-02 12:23:34=====================自定义。。。。。
                    post.setEntity(new StringEntity(p.toString(), "UTF-8"));//将参数设置入POST请求
                    post.setHeader(HTTP.CONTENT_TYPE, "text/json");//设置为json格式。
                    HttpClient client = new DefaultHttpClient();
                    HttpResponse response = client.execute(post);
                    if (response.getStatusLine().getStatusCode() == 200) {
                        String content = EntityUtils.toString(response.getEntity());    //得到返回字符串

                        //存储在ListBaseEmp里=============================================================================================
                        List<BaseBox> ListTemp=new ArrayList<BaseBox>();
                        ListTemp = gson.fromJson(content, new TypeToken<List<BaseBox>>() {
                        }.getType());
                        //=================================================================================================================


                        listViewdata.clear();
                        for (BaseBox classtemp : ListTemp) {
                            //返回的ServerReturn值==1的时候就代表成功。不是1的时候代表失败。===========================================
                            if (classtemp.ServerReturn.equals("1")) {
                                listViewdata.add(classtemp.BoxName);
                            }
                        }
                        Log.d("WCF",  "ok");//打印到logcat

                        mh.sendEmptyMessage(0);


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
                    e.printStackTrace();
                }
            }
        });

    }

    //上传任务。。
    public void upload_Task(View view) throws ClassNotFoundException {

        singleThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    //============================测试数据开始添加
                    //添加lstArriveTime类
                    ArriveTime A1 = new ArriveTime();
                    A1.EmpID="2703" ;//登陆的人员ID，记录操作人员ID.
                    A1.ATime ="2014-08-08 08:55:00";//到达网点的时间
                    A1.TimeID="1" ;//到达时间ID
                    A1.TradeBegin="2014-08-08 09:00:00" ;//交易开始时间
                    A1.TradeEnd ="2014-08-08 09:01:00";//交易结束时间
                    A1.TradeState ="1";//这次到达完成交易了么？1为完成，0为未完成
                    //添加box类
                    Box B1 = new Box();
                    B1.SiteID="1759";//收，送
                    B1.BoxID="2";//箱ID
                    B1.BoxName="3";//箱名
                    B1.ActionTime="2014-08-08 09:00:00";//交接时间
                    B1.BoxStatus="实";//实，空
                    B1.BoxType="款箱";//款箱，卡箱，凭证
                    B1.TradeAction="收";//收，送
                    B1.TimeID="1" ;//到达时间ID
                    B1.BoxCount="20";//20个无标签箱，默认读卡的是1个。
                    //添加site类
                    Site SI1 = new Site();
                    SI1.TaskID="6112" ;
                    SI1.SiteID ="1759";    //网点ID
                    SI1.SiteName="佛山中行星晖园支行" ;//网点名
                    SI1.SiteManager ="**";//网点负责人
                    SI1.SiteManagerPhone ="**";//网点负责人电话
                    SI1.SiteType="网点" ;//网点类型ATM还是网点
                    SI1.Status ="已交接";//交接状态: 未交接, 交接中, 已交接
                    SI1.ATMCount="0" ;//ATM数目
                    List<ArriveTime> LA = new ArrayList<ArriveTime>() ;
                    LA.add(A1);
                    SI1.lstArriveTime=LA;
                    //添加Task类
                    YLTask t1 = new YLTask();
                    t1.ServerVersion = "1";
                    t1.TaskVersion="1";
                    t1. TaskID="6112";
                    t1. TaskType="早送";
                    t1. Handset="NH008";
                    t1. TaskDate="2014-08-08";
                    t1. Line="南海26线早送";
                    t1. TaskManager="杨磊";
                    t1. TaskATMBeginTime=null;
                    t1. TaskATMEndTime=null;
                    t1. TaskManagerNo="600241";
                    t1. ServerReturn="1";
                    List<Site> LS = new ArrayList<Site>() ;
                    LS.add(SI1);
                    List<Box> LB= new ArrayList<Box>() ;
                    LB.add(B1);
                    t1.lstSite=LS;
                    t1.lstBox=LB;
                    //添加数值到User类
                    User s1 = new User();
                    s1.EmpID="2703";
                    s1.EmpNO = "600241";
                    s1.Name = "杨磊";
                    s1.Pass = "8c4d6ed1b2688b2373bcac4137fab1e6";
                    s1.DeviceID = "NH026";
                    s1.ISWIFI = "1";
                    //以上是测试数据可以修改============================================================================

                    String url = YLSystem.GetBaseUrl(getApplicationContext())+"UpLoad";
                    HttpPost post = new HttpPost(url);
                    //添加数值到User类
                    Gson gson = new Gson();
                    //设置POST请求中的参数-------返回EmpID员工ID，ServerReturn 服务器错误，1为没错误，Time 服务器时间。
                    JSONObject p = new JSONObject();
                    p.put("STask",gson.toJson(t1));//整个任务=====================自定义。。。。。
                    p.put("empid", s1.EmpID);//人员id=====================自定义。。。。。
                    p.put("deviceID", s1.DeviceID);//手持机号=====================自定义。。。。。
                    p.put("ISWIFI", s1.ISWIFI);//是否用WIFI=====================自定义。。。。。

                    post.setEntity(new StringEntity(p.toString(), "UTF-8"));//将参数设置入POST请求
                    post.setHeader(HTTP.CONTENT_TYPE, "text/json");//设置为json格式。
                    HttpClient client = new DefaultHttpClient();
                    HttpResponse response = client.execute(post);
                    if (response.getStatusLine().getStatusCode() == 200) {
                        String content = EntityUtils.toString(response.getEntity());    //得到返回字符串


                        Log.d("WCF", content);//打印到logcat

                        mh.sendEmptyMessage(0);


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
                    e.printStackTrace();
                }
            }
        });

    }

    //读取box 名
    public void getbox(View view) throws ClassNotFoundException
    {
    /*    List<Box> lstbox=new ArrayList<Box>();
        BoxDBSer a =new BoxDBSer(getApplicationContext());
        lstbox=a.GetBoxs("");*/
        listViewdata.add("1");
        listViewdata.add("2");
        listViewdata.add("3");
        listViewdata.add("4");
        listViewdata.add("5");

        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(new ArrayAdapter<String>(chongTest3.this, android.R.layout.simple_expandable_list_item_1, listViewdata));

    }
    public void getvision(View view) throws ClassNotFoundException{
        singleThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    //添加数值到User类
                    User s1 = new User();
                    s1.EmpNO = "200097";
                    s1.Name = "徐竞航";
                    s1.Pass = "8c4d6ed1b2688b2373bcac4137fab1e6";
                    s1.DeviceID = "NH008";
                    s1.ISWIFI = "1";
                    //以上是测试数据可以修改============================================================================


                    String url = YLSystem.GetBaseUrl(getApplicationContext())+"GetVision";
                    HttpPost post = new HttpPost(url);
                    //添加数值到User类
                    Gson gson = new Gson();
                    //设置POST请求中的参数-------返回EmpID员工ID，ServerReturn 服务器错误，1为没错误，Time 服务器时间。
                    JSONObject p = new JSONObject();

                    p.put("DeviceID", s1.DeviceID);//手持机号=====================自定义。。。。。
                    p.put("ISWIFI", s1.ISWIFI);//是否用WIFI=====================自定义。。。。。

                    post.setEntity(new StringEntity(p.toString(), "UTF-8"));//将参数设置入POST请求
                    post.setHeader(HTTP.CONTENT_TYPE, "text/json");//设置为json格式。
                    HttpClient client = new DefaultHttpClient();
                    HttpResponse response = client.execute(post);
                    if (response.getStatusLine().getStatusCode() == 200) {
                        String content = EntityUtils.toString(response.getEntity());    //得到返回字符串
                        Vision vision = gson.fromJson(content, new TypeToken<Vision>() {
                        }.getType());
                        Log.d("WCF",  "ok");//打印到logcat

                        mh.sendEmptyMessage(0);


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
                    e.printStackTrace();
                }
            }
        });

    }
}
