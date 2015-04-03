package YLWebService;

import android.util.Log;

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
import java.util.Map;

import TaskClass.Site;
import TaskClass.YLTask;
import TaskClass.User;
import YLSystemDate.YLSystem;

/**
 * Created by asus on 2015/1/29.
 * 用于访问远程服务
 */
public class YLWebService {
  private static Map map = new HashMap();
    public YLWebService()
    {
        map.put("Login",   "http://58.252.75.149:8055/YLMobileServiceAndroid.svc/Login");
        map.put("TaskList","http://58.252.75.149:8055/YLMobileServiceAndroid.svc/GetTask");
        map.put("SiteList","http://58.252.75.149:8055/YLMobileServiceAndroid.svc/GetTaskStie");
    }

    //获取网点列表
    public List<Site> GetSiteList(String taskID){
        try {
            String url =map.get("SiteList").toString();
            HttpPost post = new HttpPost(url);

            //添加数值到User类
            User s1 =YLSystem.getUser();

            Gson gson = new Gson();
            //设置POST请求中的参数
            JSONObject p = new JSONObject();
            p.put("taskID", taskID);//任务ID。
            p.put("deviceID", s1.DeviceID);//手持机号
            p.put("empid", s1.EmpID);//员工ID。
            p.put("ISWIFI", s1.ISWIFI);//是否用WIFI

            post.setEntity(new StringEntity(p.toString(), "UTF-8"));//将参数设置入POST请求
            post.setHeader(HTTP.CONTENT_TYPE, "text/json");//设置为json格式。
            HttpClient client = new DefaultHttpClient();
            HttpResponse response = client.execute(post);
            if (response.getStatusLine().getStatusCode() == 200) {
                String content = EntityUtils.toString(response.getEntity());    //得到返回字符串
                Log.d("WCF", content);//打印到logcat

                List<Site> siteList = gson.fromJson(content, new TypeToken<List<Site>>() {
                }.getType());
            return siteList;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    //获取任务列表
    public List<YLTask>  DownloadTaskList(){
        try {
            String url = map.get("TaskList").toString();
            HttpPost post = new HttpPost(url);
            //添加数值到User类
            User s1 =YLSystem.getUser();

            Gson gson = new Gson();
            //设置POST请求中的参数---------------返回List<YLTask>关于此员工对应的任务。
            JSONObject p = new JSONObject();
            p.put("empid", s1.EmpID);//员工ID。
            p.put("deviceID", s1.DeviceID);//手持机号
            p.put("ISWIFI", s1.ISWIFI);//是否用WIFI

            post.setEntity(new StringEntity(p.toString(), "UTF-8"));//将参数设置入POST请求
            post.setHeader(HTTP.CONTENT_TYPE, "text/json");//设置为json格式。
            HttpClient client = new DefaultHttpClient();
            HttpResponse response = client.execute(post);
            if (response.getStatusLine().getStatusCode() == 200) {
                String content = EntityUtils.toString(response.getEntity());    //得到返回字符串
                Log.d("WCF", content);//打印到logcat

                List<YLTask> taskList=new ArrayList<YLTask> ();
                taskList = gson.fromJson(content, new TypeToken<List<YLTask>>() {
                }.getType());

                return taskList;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    //用户登录
    public boolean Login(String EmpNO,String UserName,String pwd,String DeviceID, String ISWIFI)
    {
        try {
           // String url = "http://192.168.200.137:8055/YLMobileServiceAndroid.svc/Login";//网址
            String url=map.get("Login").toString();
            HttpPost post = new HttpPost(url);

            //添加数值到User类
            User s1 = new User();
            s1.EmpNO =EmpNO;// "200097";
            s1.Name = UserName;//"徐竞航";
            s1.Pass = pwd;//"8c4d6ed1b2688b2373bcac4137fab1e6";
            s1.DeviceID =DeviceID;// "NH008";
            s1.ISWIFI =ISWIFI;// "1";

            //设置POST请求中的参数-------返回EmpID员工ID，ServerReturn 服务器错误，1为没错误，Time 服务器时间。
            JSONObject p = new JSONObject();
            p.put("Lno", s1.EmpNO);//员工卡号。
            p.put("Lpass", s1.Pass);//密码
            p.put("DeviceID", s1.DeviceID);//手持机号
            p.put("ISWIFI", s1.ISWIFI);//是否用WIFI
            post.setEntity(new StringEntity(p.toString(), "UTF-8"));//将参数设置入POST请求
            post.setHeader(HTTP.CONTENT_TYPE, "text/json");//设置为json格式。
            HttpClient client = new DefaultHttpClient();
            HttpResponse response = client.execute(post);
            if (response.getStatusLine().getStatusCode() == 200) {
                String content = EntityUtils.toString(response.getEntity());    //得到返回字符串
                Log.d("WCF", content);//打印到logcat

                //显示到列表里。
                Gson gson = new Gson();
                User s3=new User();
                s3 = gson.fromJson(content, new TypeToken<User>() {
                }.getType());

                if(s3.ServerReturn.equals("1")) {
                    YLSystem.setUser(s3);//登录成功后设置用户
                    return true;
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }


}
