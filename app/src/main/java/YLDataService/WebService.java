package YLDataService;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
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
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import TaskClass.BaseBox;
import TaskClass.BaseClient;
import TaskClass.BaseEmp;
import TaskClass.BaseSite;
import TaskClass.Site;
import TaskClass.User;
import TaskClass.YLTask;
import YLSystem.YLSystem;

/**
 * Created by Administrator on 2015/1/28.
 * http://58.252.75.149:8055/YLMobileServiceAndroid.svc/Login
 * http://58.252.75.149:8055/YLMobileServiceAndroid.svc/GetTask
 * http://58.252.75.149:8055/YLMobileServiceAndroid.svc/GetTaskStie
 * http://58.252.75.149:8055/YLMobileServiceAndroid.svc/GetBaseEmp
 * http://58.252.75.149:8055/YLMobileServiceAndroid.svc/GetBaseClient
 * http://58.252.75.149:8055/YLMobileServiceAndroid.svc/GetBaseSite
 * http://58.252.75.149:8055/YLMobileServiceAndroid.svc/GetBaseBox
 */
public class WebService {
    public static String webserviceaddress = "http://58.252.75.149:8055/YLMobileServiceAndroid.svc/";

    ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();

    android.os.Handler mh = new android.os.Handler() {   //以Handler为桥梁将结果传入UI
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    public final String UserWebContent(String webapi, User user) throws JSONException, IOException {

        webserviceaddress += webapi;
        HttpPost post = new HttpPost(webserviceaddress);
        Gson gson = new Gson();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("user", gson.toJson(user));
        post.setEntity(new StringEntity(jsonObject.toString(), "UTF-8"));//将参数设置入POST请求
        post.setHeader(HTTP.CONTENT_TYPE, "text/json");//设置为json格式。
        HttpClient client = new DefaultHttpClient();
        HttpResponse response = client.execute(post);
        if (response.getStatusLine().getStatusCode() == 200) {
            String content = EntityUtils.toString(response.getEntity());    //得到返回字符串
            User getjsonuser = gson.fromJson(content, new TypeToken<User>() {
            }.getType());
            return getjsonuser.getServerReturn();
        } else {
            return null;
        }
    }

    public final String getBaseEmp(String webapi, User user) throws JSONException, IOException {

        webserviceaddress += webapi;
        HttpPost post = new HttpPost(webserviceaddress);
        Gson gson = new Gson();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("user", gson.toJson(user));
        post.setEntity(new StringEntity(jsonObject.toString(), "UTF-8"));//将参数设置入POST请求
        post.setHeader(HTTP.CONTENT_TYPE, "text/json");//设置为json格式。
        HttpClient client = new DefaultHttpClient();
        HttpResponse response = client.execute(post);
        if (response.getStatusLine().getStatusCode() == 200) {
            String content = EntityUtils.toString(response.getEntity());    //得到返回字符串
            User getjsonuser = gson.fromJson(content, new TypeToken<User>() {
            }.getType());
            return getjsonuser.getServerReturn();
        } else {
            return null;
        }
    }

    public final static String TaskWebContent(String webapi, User user) throws Exception {

        webserviceaddress += webapi;
        HttpPost post = new HttpPost(webserviceaddress);
        Gson gson = new Gson();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("user", gson.toJson(user));
        post.setEntity(new StringEntity(jsonObject.toString(), "UTF-8"));//将参数设置入POST请求
        post.setHeader(HTTP.CONTENT_TYPE, "text/json");//设置为json格式。
        HttpClient client = new DefaultHttpClient();
        HttpResponse response = client.execute(post);
        if (response.getStatusLine().getStatusCode() == 200) {
            String content = EntityUtils.toString(response.getEntity());    //得到返回字符串
            return content;
        } else {
            return null;
        }
    }

    private String webcontent = null;

    public String gettask() throws Exception {
        final String content = null;
        singleThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    String url = "http://58.252.75.149:8055/YLMobileServiceAndroid.svc/GetTask1";
                    HttpPost post = new HttpPost(url);
                    //添加数值到User类
                    User user = new User();
                    user.EmpNO = "600241";
                    user.Name = "杨磊";
                    user.Pass = YLSystem.md5("600241");
                    user.DeviceID = "NH008";
                    user.ISWIFI = "1";
                    user.EmpID = "2703";
                    user.TaskDate = "2014-08-07";
                    Gson gson = new Gson();
                    //设置POST请求中的参数
                    JSONObject p = new JSONObject();
                    p.put("user", gson.toJson(user));//将User类转换成Json传到服务器。
                    post.setEntity(new StringEntity(p.toString(), "UTF-8"));//将参数设置入POST请求
                    post.setHeader(HTTP.CONTENT_TYPE, "text/json");//设置为json格式。
                    HttpClient client = new DefaultHttpClient();
                    HttpResponse response = client.execute(post);
                    if (response.getStatusLine().getStatusCode() == 200) {
                        webcontent = EntityUtils.toString(response.getEntity());
                        if (content.equals("1")) {
                            mh.sendEmptyMessage(0);
                        } else {

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
        return webcontent;
    }

    public String getServerVer() {
        //todo 从服务器获取软件的最新版本
        webcontent = "1.02";
        return webcontent;
    }

    public static  void CacheData(final Context ctx) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        String timeLastUpdate = prefs.getString("CacheLastUpdate", "ALL");
        final String[] servertime = {""};

        final Integer[] finish = {0};
        Handler  mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {

                String content ;
                switch (msg.what) {
                    case 20:
                        break;
                    case 21:
                        content = (String) msg.obj;
                        finish[0]++;
                        break;
                    case 22:
                        content = (String) msg.obj;
                        servertime[0] =content;
                        break;
                    case 100:
//                        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//                        String date = sDateFormat.format(new java.util.Date());
                        String date = servertime[0];
                        //测试不开
                        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(ctx);
                        SharedPreferences.Editor edit = settings.edit();
                        edit.putString("CacheLastUpdate", date);//YLSystem.getUser().getTime()
                        edit.apply();
                        break;
                }


                super.handleMessage(msg);
            }
        };
        GetBaseEmp( ctx, mHandler,  timeLastUpdate) ;
        GetBaseClient( ctx, mHandler,  timeLastUpdate) ;
        GetBaseSite( ctx, mHandler,  timeLastUpdate) ;
        GetBaseBox( ctx, mHandler,  timeLastUpdate) ;
        int count=0;
        while (count<10){
            try {
                Thread.sleep(1000*5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (  finish[0] ==4  ){
                Message msg = mHandler.obtainMessage(100);
                mHandler.sendMessage(msg);
                break;
            }
        }

    }
    public static void GetBaseEmp(final Context ctx, final Handler mHandler,  final String timeLastUpdate) {
        new Thread() {
            public void run() {
                String url =LocalSetting.webserviceaddress+ "GetBaseEmp";//网址
                HttpPost post = new HttpPost(url);
//                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
//                String timeLastUpdate = prefs.getString("CacheLastUpdate", "ALL");

                List<BaseEmp> ListBaseEmp = new ArrayList<BaseEmp>();
                //添加数值到User类
                Gson gson = new Gson();
                //设置POST请求中的参数-------返回EmpID员工ID，ServerReturn 服务器错误，1为没错误，Time 服务器时间。
                try {
                    JSONObject p = new JSONObject();
                    p.put("DeviceID", YLSystem.GetDeviceID(ctx)); //"NH008");//
                    p.put("ISWIFI", YLSystem.isWifiActive(ctx));//"1");//
                    p.put("datetime", timeLastUpdate);
                    post.setEntity(new StringEntity(p.toString(), "UTF-8"));//将参数设置入POST请求
                    post.setHeader(HTTP.CONTENT_TYPE, "text/json");//设置为json格式。

                    HttpClient client = new DefaultHttpClient();
                    HttpResponse response = null;
                    response = client.execute(post);

                    if (response.getStatusLine().getStatusCode() == 200) {
                        String content = EntityUtils.toString(response.getEntity());
                        ListBaseEmp = gson.fromJson(content, new TypeToken<List<BaseEmp>>() {
                        }.getType());
                        Log.d("jutest", "GetBaseEmp:" + ListBaseEmp.size() + " 时间:" + timeLastUpdate);//打印到logcat

                        Message msg =null;
                        if(mHandler!=null) {
                            msg = mHandler.obtainMessage(20);
                            msg.obj = "BaseEmp下载完毕";
                            mHandler.sendMessage(msg);
                        }

                        (new BaseEmp()).CacheBaseEmp(ctx, ListBaseEmp);//保存到数据库
                        if(mHandler!=null) {
                            msg = mHandler.obtainMessage(21);
                            msg.obj = "BaseEmp更新完毕";
                            mHandler.sendMessage(msg);

                            if(ListBaseEmp.size()>0) {
                                msg = mHandler.obtainMessage(22);
                                msg.obj = ListBaseEmp.get(0).ServerTime;
                                mHandler.sendMessage(msg);
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public static void GetBaseClient(final Context ctx, final Handler mHandler,  final String timeLastUpdate) {
        new Thread() {
            public void run() {
                String url =LocalSetting.webserviceaddress+ "GetBaseClient";//网址
                HttpPost post = new HttpPost(url);
//                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
//                String timeLastUpdate = prefs.getString("CacheLastUpdate", "ALL");

                List<BaseClient> ListBase = new ArrayList<BaseClient>();
                //添加数值到User类
                Gson gson = new Gson();
                //设置POST请求中的参数-------返回EmpID员工ID，ServerReturn 服务器错误，1为没错误，Time 服务器时间。
                try {
                    JSONObject p = new JSONObject();
                    p.put("DeviceID", YLSystem.GetDeviceID(ctx)); //"NH008");//
                    p.put("ISWIFI", YLSystem.isWifiActive(ctx));//"1");//
                    p.put("datetime", timeLastUpdate);
                    post.setEntity(new StringEntity(p.toString(), "UTF-8"));//将参数设置入POST请求
                    post.setHeader(HTTP.CONTENT_TYPE, "text/json");//设置为json格式。

                    HttpClient client = new DefaultHttpClient();
                    HttpResponse response = null;
                    response = client.execute(post);

                    if (response.getStatusLine().getStatusCode() == 200) {
                        String content = EntityUtils.toString(response.getEntity());
                        ListBase = gson.fromJson(content, new TypeToken<List<BaseClient>>() {
                        }.getType());
                        Log.d("jutest", "GetBaseClient:" + ListBase.size());//打印到logcat

                        Message msg =null;
                        if(mHandler!=null) {
                            msg = mHandler.obtainMessage(20);
                            msg.obj = "BaseClient下载完毕";
                            mHandler.sendMessage(msg);
                        }

                        (new BaseClient()).CacheBaseClient(ctx, ListBase);//保存到数据库
                        if(mHandler!=null) {
                            msg = mHandler.obtainMessage(21);
                            msg.obj ="BaseClient更新完毕";
                            mHandler.sendMessage(msg);

                            if(ListBase.size()>0) {
                                msg = mHandler.obtainMessage(22);
                                msg.obj = ListBase.get(0).ServerTime;
                                mHandler.sendMessage(msg);
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public static void GetBaseSite(final Context ctx, final Handler mHandler,  final String timeLastUpdate) {
        new Thread() {
            public void run() {
                String url =LocalSetting.webserviceaddress+ "GetBaseSite";//网址
                HttpPost post = new HttpPost(url);
//                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
//                String timeLastUpdate = prefs.getString("CacheLastUpdate", "ALL");

                List<BaseSite> ListBase = new ArrayList<BaseSite>();
                //添加数值到User类
                Gson gson = new Gson();
                //设置POST请求中的参数-------返回EmpID员工ID，ServerReturn 服务器错误，1为没错误，Time 服务器时间。
                try {
                    JSONObject p = new JSONObject();
                    p.put("DeviceID", YLSystem.GetDeviceID(ctx)); //"NH008");//
                    p.put("ISWIFI", YLSystem.isWifiActive(ctx));//"1");//
                    p.put("datetime", timeLastUpdate);
                    post.setEntity(new StringEntity(p.toString(), "UTF-8"));//将参数设置入POST请求
                    post.setHeader(HTTP.CONTENT_TYPE, "text/json");//设置为json格式。

                    HttpClient client = new DefaultHttpClient();
                    HttpResponse response = null;
                    response = client.execute(post);

                    if (response.getStatusLine().getStatusCode() == 200) {
                        String content = EntityUtils.toString(response.getEntity());
                        ListBase = gson.fromJson(content, new TypeToken<List<BaseSite>>() {
                        }.getType());
                        Log.d("jutest", "GetBaseSite:" + ListBase.size());//打印到logcat

                        Message msg =null;
                        if(mHandler!=null) {
                            msg = mHandler.obtainMessage(20);
                            msg.obj = "BaseSite下载完毕";
                            mHandler.sendMessage(msg);
                        }

                        (new BaseSite()).CacheBaseSite(ctx, ListBase);//保存到数据库
                        if(mHandler!=null) {
                            msg = mHandler.obtainMessage(21);
                            msg.obj = "BaseSite更新完毕";
                            mHandler.sendMessage(msg);

                            if(ListBase.size()>0) {
                                msg = mHandler.obtainMessage(22);
                                msg.obj = ListBase.get(0).ServerTime;
                                mHandler.sendMessage(msg);
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public static void GetBaseBox(final Context ctx, final Handler mHandler,  final String timeLastUpdate) {
        new Thread() {
            public void run() {
                String url =LocalSetting.webserviceaddress+ "GetBaseBox";//网址
                HttpPost post = new HttpPost(url);
//                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
//                String timeLastUpdate = prefs.getString("CacheLastUpdate", "ALL");

                List<BaseBox> ListBase = new ArrayList<BaseBox>();
                //添加数值到User类
                Gson gson = new Gson();
                //设置POST请求中的参数-------返回EmpID员工ID，ServerReturn 服务器错误，1为没错误，Time 服务器时间。
                try {
                    JSONObject p = new JSONObject();
                    p.put("DeviceID", YLSystem.GetDeviceID(ctx)); //"NH008");//
                    p.put("ISWIFI", YLSystem.isWifiActive(ctx));//"1");//
                    p.put("datetime", timeLastUpdate);
                    post.setEntity(new StringEntity(p.toString(), "UTF-8"));//将参数设置入POST请求
                    post.setHeader(HTTP.CONTENT_TYPE, "text/json");//设置为json格式。

                    HttpClient client = new DefaultHttpClient();
                    HttpResponse response = null;
                    response = client.execute(post);

                    if (response.getStatusLine().getStatusCode() == 200) {
                        String content = EntityUtils.toString(response.getEntity());
                        ListBase = gson.fromJson(content, new TypeToken<List<BaseBox>>() {
                        }.getType());
                        Log.d("jutest", "GetBaseBox:" + ListBase.size());//打印到logcat

                        Message msg =null;
                        if(mHandler!=null) {
                            msg = mHandler.obtainMessage(20);
                            msg.obj = "BaseBox下载完毕";
                            mHandler.sendMessage(msg);
                        }

                        (new BaseBox()).CacheBaseBox(ctx, ListBase);//保存到数据库
                        if(mHandler!=null) {
                            msg = mHandler.obtainMessage(21);
                            msg.obj = "BaseBox更新完毕";
                            mHandler.sendMessage(msg);

                            if(ListBase.size()>0) {
                                msg = mHandler.obtainMessage(22);
                                msg.obj = ListBase.get(0).ServerTime;
                                mHandler.sendMessage(msg);
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }


    public static void GetTaskList(final Context ctx, final Handler mHandler) {
        new Thread() {
            public void run() {
                try {
                    String url = "http://58.252.75.149:8055/YLMobileServiceAndroid.svc/GetTask1";
                    HttpPost post = new HttpPost(url);

                    User user=YLSystem.getUser();
                    //测试数据
                    user.DeviceID = "NH008";
                    user.TaskDate = "2014-08-07";

                    Gson gson = new Gson();
                    //设置POST请求中的参数
                    JSONObject p = new JSONObject();
                    p.put("user", gson.toJson(user));//将User类转换成Json传到服务器。
                    post.setEntity(new StringEntity(p.toString(), "UTF-8"));//将参数设置入POST请求
                    post.setHeader(HTTP.CONTENT_TYPE, "text/json");//设置为json格式。
                    HttpClient client = new DefaultHttpClient();
                    HttpResponse response = client.execute(post);
                    if (response.getStatusLine().getStatusCode() == 200) {
                        String content = EntityUtils.toString(response.getEntity());

                        List<YLTask> lstYLTask = gson.fromJson(content, new TypeToken<List<YLTask>>() {
                        }.getType());
                        String result = lstYLTask.get(0).ServerReturn;
                        if (result.equals("1")) {
                            Log.d("jutest", "GetTaskList:" + lstYLTask.size());
                            Message msg = mHandler.obtainMessage(20);
                            msg.obj = lstYLTask;
                            mHandler.sendMessage(msg);
                        } else {
                            Message msg = mHandler.obtainMessage(21);
                            msg.obj = result;
                            mHandler.sendMessage(msg);
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
        }. start();
    }


    public static void GetTaskSite(final Context ctx, final Handler mHandler, final String taskid) {
        new Thread() {
            public void run() {
                try {
                    String url =  "http://58.252.75.149:8055/YLMobileServiceAndroid.svc/GetTaskStie";
                    HttpPost post = new HttpPost(url);

                    User user=YLSystem.getUser();
                    //测试数据
                    user.DeviceID = "NH008";
                    user.TaskDate = "2014-08-07";

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
                        String content = EntityUtils.toString(response.getEntity());

                        List<Site> lstSite  = gson.fromJson(content, new TypeToken<List<Site>>() {
                        }.getType());
                        String result = lstSite.get(0).ServerReturn;
                        if (result.equals("1")) {
                            Log.d("jutest", "GetTaskSite:" + lstSite.size());
                            Message msg = mHandler.obtainMessage(20);
                            msg.obj = lstSite;
                            mHandler.sendMessage(msg);
                        } else {
                            Message msg = mHandler.obtainMessage(21);
                            msg.obj = result;
                            mHandler.sendMessage(msg);
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
        }. start();
    }



}
