package YLDataService;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import TaskClass.User;
import TaskClass.YLTask;

/**
 * Created by Administrator on 2015/1/28.
 */
public class WebService {
    private String webserviceaddress = "http://192.168.200.137:8055/YLMobileServiceAndroid.svc/";

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


    public final String TaskWebContent(String webapi,User user ) throws JSONException, IOException {

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




}
