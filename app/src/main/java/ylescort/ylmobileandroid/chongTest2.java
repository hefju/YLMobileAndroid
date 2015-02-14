package ylescort.ylmobileandroid;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

import TaskClass.BaseBox;
import TaskClass.BaseClient;
import TaskClass.BaseEmp;
import TaskClass.BaseSite;
import TaskClass.User;


public class chongTest2 extends ActionBarActivity {
    private List<String> listViewdata ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chong_test2);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chong_test2, menu);
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

                    String url = "http://58.252.75.149:8055/YLMobileServiceAndroid.svc/GetBaseEmp";//网址
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

                    String url = "http://58.252.75.149:8055/YLMobileServiceAndroid.svc/GetBaseClient";//网址
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

                    String url = "http://58.252.75.149:8055/YLMobileServiceAndroid.svc/GetBaseSite";//网址
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

                    String url = "http://58.252.75.149:8055/YLMobileServiceAndroid.svc/GetBaseBox";//网址
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

}
