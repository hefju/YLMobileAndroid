package YLDataService;

import android.content.Context;
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

import TaskClass.Site;
import TaskClass.User;
import YLSystemDate.YLSystem;

/**
 * Created by Administrator on 2015-06-27.
 */
public class WebServerYLSite {

    public List<Site> GetYLTaskSite (User user,Context context)throws Exception{
        String url = YLSystem.GetBaseUrl(context)+"GetTaskStie";
        GetYLTaskSiteAsyTask getYLTaskSiteAsyTask = new GetYLTaskSiteAsyTask();
        getYLTaskSiteAsyTask.execute(url,user.getTaskDate(),user.getDeviceID(),user.getEmpID(),user.getISWIFI());
        return getYLTaskSiteAsyTask.get();
    }

    private class GetYLTaskSiteAsyTask extends AsyncTask<String,Integer,List<Site>>{
        @Override
        protected List<Site> doInBackground(String... params) {
            String url = params[0];
            HttpPost post = new HttpPost(url);
            Gson gson = new Gson();
            JSONObject p = new JSONObject();
            try {
                p.put("taskID",params[1]);
                p.put("deviceID",params[2]);
                p.put("empid",params[3]);
                p.put("ISWIFI",params[4]);
                post.setEntity(new StringEntity(p.toString(),"UTF-8"));
                post.setHeader(HTTP.CONTENT_TYPE,"text/json");
                HttpClient client = new DefaultHttpClient();
                HttpResponse response = client.execute(post);
                if (response.getStatusLine().getStatusCode() == 200){
                    String content = EntityUtils.toString(response.getEntity());
                    return gson.fromJson(content, new TypeToken<List<Site>>() {}.getType());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
