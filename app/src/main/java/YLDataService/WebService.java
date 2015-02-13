package YLDataService;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import TaskClass.BaseEmp;
import TaskClass.User;
import TaskClass.YLTask;
import YLSystem.YLSystem;

/**
 * Created by Administrator on 2015/1/28.
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

    public final String UserWebContent(String webapi,User user ) throws JSONException, IOException {

         webserviceaddress +=webapi;
        HttpPost post = new HttpPost(webserviceaddress);
        Gson gson = new Gson();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("user",gson.toJson(user));
        post.setEntity(new StringEntity(jsonObject.toString(), "UTF-8"));//将参数设置入POST请求
        post.setHeader(HTTP.CONTENT_TYPE, "text/json");//设置为json格式。
        HttpClient client = new DefaultHttpClient();
        HttpResponse response = client.execute(post);
        if (response.getStatusLine().getStatusCode() == 200) {
            String content = EntityUtils.toString(response.getEntity());    //得到返回字符串
            User getjsonuser = gson.fromJson(content, new TypeToken<User>() {
            }.getType());
            return getjsonuser.getServerReturn();
        }
        else {
            return null;
        }
    }
    public final String getBaseEmp(String webapi,User user ) throws JSONException, IOException {

        webserviceaddress +=webapi;
        HttpPost post = new HttpPost(webserviceaddress);
        Gson gson = new Gson();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("user",gson.toJson(user));
        post.setEntity(new StringEntity(jsonObject.toString(), "UTF-8"));//将参数设置入POST请求
        post.setHeader(HTTP.CONTENT_TYPE, "text/json");//设置为json格式。
        HttpClient client = new DefaultHttpClient();
        HttpResponse response = client.execute(post);
        if (response.getStatusLine().getStatusCode() == 200) {
            String content = EntityUtils.toString(response.getEntity());    //得到返回字符串
            User getjsonuser = gson.fromJson(content, new TypeToken<User>() {
            }.getType());
            return getjsonuser.getServerReturn();
        }
        else {
            return null;
        }
    }

    public final static   String TaskWebContent(String webapi,User user ) throws Exception {

        webserviceaddress +=webapi;
        HttpPost post = new HttpPost(webserviceaddress);
        Gson gson = new Gson();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("user",gson.toJson(user));
        post.setEntity(new StringEntity(jsonObject.toString(), "UTF-8"));//将参数设置入POST请求
        post.setHeader(HTTP.CONTENT_TYPE, "text/json");//设置为json格式。
        HttpClient client = new DefaultHttpClient();
        HttpResponse response = client.execute(post);
        if (response.getStatusLine().getStatusCode() == 200) {
            String content = EntityUtils.toString(response.getEntity());    //得到返回字符串
            return content;
        }
        else {
            return null;
        }
    }

    private  String webcontent= null;

    public String gettask() throws Exception{
        final String content = null;
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
                    if (response.getStatusLine().getStatusCode() == 200) {
                        webcontent = EntityUtils.toString(response.getEntity());
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
        }); return webcontent;
    }

    public String getServerVer () {
        //todo 从服务器获取软件的最新版本
        webcontent="1.02";
        return webcontent;
    }

    public static  void  GetBaseEmp(final Context ctx,final Handler mHandler) {
        new Thread() {
            public void run() {
                String url = "http://58.252.75.149:8055/YLMobileServiceAndroid.svc/GetBaseEmp";//网址
                HttpPost post = new HttpPost(url);

                List<BaseEmp> ListBaseEmp = new ArrayList<BaseEmp>();
                //添加数值到User类
                Gson gson = new Gson();
                //设置POST请求中的参数-------返回EmpID员工ID，ServerReturn 服务器错误，1为没错误，Time 服务器时间。
                try {
                    JSONObject p = new JSONObject();
                    p.put("DeviceID", YLSystem.GetDeviceID(ctx)); //"NH008");//
                    p.put("ISWIFI", YLSystem.isWifiActive(ctx));//"1");//
                    post.setEntity(new StringEntity(p.toString(), "UTF-8"));//将参数设置入POST请求
                    post.setHeader(HTTP.CONTENT_TYPE, "text/json");//设置为json格式。

                    HttpClient client = new DefaultHttpClient();
                    HttpResponse response = null;
                    response = client.execute(post);

                    if (response.getStatusLine().getStatusCode() == 200) {
                        String content = EntityUtils.toString(response.getEntity());
                        ListBaseEmp = gson.fromJson(content, new TypeToken<List<BaseEmp>>() {}.getType());
                        Log.d("jutest", "GetBaseEmp"+ListBaseEmp.size());//打印到logcat

                        (new BaseEmp()).CacheBaseEmp(ctx,ListBaseEmp);//保存到数据库
                        //UPDATE是一个自己定义的整数，代表了消息ID
                        Message msg = mHandler.obtainMessage(2);
                        msg.obj=ListBaseEmp;
                        mHandler.sendMessage(msg);
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



}
