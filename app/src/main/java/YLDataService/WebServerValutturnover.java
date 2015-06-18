package YLDataService;

import android.os.AsyncTask;

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

import java.util.List;

import TaskClass.Box;
import TaskClass.YLTask;

/**
 * Created by Administrator on 2015-06-17.
 */

/**
 * 获取所在基地款箱列表
 */
public class WebServerValutturnover {

    public List<Box> ValutOutBoxList()throws Exception{
        ValutOutBoxListAsyTask valutOutBoxListAsyTask = new ValutOutBoxListAsyTask();
        valutOutBoxListAsyTask.execute();
        return valutOutBoxListAsyTask.get();
    }

    private class ValutOutBoxListAsyTask extends AsyncTask<String,Integer,List<Box>>{
        @Override
        protected List<Box> doInBackground(String... params) {
            String url = params[0];
            HttpPost post = new HttpPost(url);
            Gson gson = new Gson();
            JSONObject p = new JSONObject();
            try {
                p.put("DataTime",params[1]);
                p.put("deviceID",params[2]);
                p.put("empid",params[3]);
                post.setEntity(new StringEntity(p.toString(),"UTF-8"));
                post.setHeader(HTTP.CONTENT_TYPE,"text/json");
                HttpClient client = new DefaultHttpClient();
                HttpResponse response = client.execute(post);
                if (response.getStatusLine().getStatusCode() == 200){
                    String content = EntityUtils.toString(response.getEntity());
                    return gson.fromJson(content, new TypeToken<List<Box>>() {}.getType());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public List<Box> ValutInBoxList()throws Exception{
        ValutInBoxListAsyTask valutInBoxListAsyTask = new ValutInBoxListAsyTask();
        valutInBoxListAsyTask.execute();
        return valutInBoxListAsyTask.get();
    }

    private class ValutInBoxListAsyTask extends AsyncTask<String,Integer,List<Box>>{
        @Override
        protected List<Box> doInBackground(String... params) {
            String url = params[0];
            HttpPost post = new HttpPost(url);
            Gson gson = new Gson();
            JSONObject p = new JSONObject();
            try {
                p.put("DataTime",params[1]);
                p.put("deviceID",params[2]);
                p.put("empid",params[3]);
                post.setEntity(new StringEntity(p.toString(),"UTF-8"));
                post.setHeader(HTTP.CONTENT_TYPE,"text/json");
                HttpClient client = new DefaultHttpClient();
                HttpResponse response = client.execute(post);
                if (response.getStatusLine().getStatusCode() == 200){
                    String content = EntityUtils.toString(response.getEntity());
                    return gson.fromJson(content, new TypeToken<List<Box>>() {}.getType());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public String Valutturnoverupload()throws Exception{
        ValutturnoveruploadAsyTask valutturnoveruploadAsyTask = new ValutturnoveruploadAsyTask();
        valutturnoveruploadAsyTask.execute();
        valutturnoveruploadAsyTask.get();
        return null;
    }

    private class ValutturnoveruploadAsyTask extends AsyncTask<String,Integer,String>{
        @Override
        protected String doInBackground(String... params) {
            String url = params[0];
            HttpPost post = new HttpPost(url);
            Gson gson = new Gson();
            JSONObject p = new JSONObject();
            try {
                p.put("DataTime",params[1]);
                p.put("deviceID",params[2]);
                p.put("empid",params[3]);
                post.setEntity(new StringEntity(p.toString(),"UTF-8"));
                post.setHeader(HTTP.CONTENT_TYPE,"text/json");
                HttpClient client = new DefaultHttpClient();
                HttpResponse response = client.execute(post);
                if (response.getStatusLine().getStatusCode() == 200){
                    String content = EntityUtils.toString(response.getEntity());
                    return gson.fromJson(content, new TypeToken<List<Box>>() {}.getType());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
