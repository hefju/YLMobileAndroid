package YLDataService;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;

import TaskClass.Box;
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
        getYLTaskSiteAsyTask.execute(url,user.getTaskDate(),YLSystem.getHandsetIMEI(),user.getEmpID(),user.getISWIFI());
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

    /**
     * 获取款箱数量
     * @param context context
     * @param TaskID 任务ID
     * @param EMPID 员工ID
     * @return 款箱数量
     * @throws Exception
     */
    public int GetTaskBoxCount(Context context,String TaskID,String EMPID) throws  Exception{
        String url = YLSystem.GetBaseUrl(context)+"StoreGetBoxByTaskIDOutCount";
        GetTaskBoxCountAsy getTaskBoxCountAsy = new GetTaskBoxCountAsy();
        getTaskBoxCountAsy.execute(url,TaskID,EMPID);
        return getTaskBoxCountAsy.get();
    }

    private class GetTaskBoxCountAsy extends  AsyncTask<String,Integer,Integer>{
        @Override
        protected Integer doInBackground(String... strings) {
            String url = strings[0];
            HttpPost post = new HttpPost(url);
            Gson gson = new Gson();
            JSONObject p = new JSONObject();
            try {
                p.put("TaskID",strings[1]);
                p.put("deviceID",YLSystem.getHandsetIMEI());
                p.put("empid",strings[2]);
                post.setEntity(new StringEntity(p.toString(),"UTF-8"));
                post.setHeader(HTTP.CONTENT_TYPE,"text/json");
                HttpClient client = new DefaultHttpClient();
                HttpResponse response = client.execute(post);
                if (response.getStatusLine().getStatusCode() == 200){
                    String content = EntityUtils.toString(response.getEntity());
                    return gson.fromJson(content, Integer.class);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public String GetCarBoxOutID(Context context,String TaskID,String EMPID) throws  Exception{
        String url = YLSystem.GetBaseUrl(context)+"StoreGetBoxByTaskIDOutID";
        GetCarBoxOutIDAsy getCarBoxOutIDAsyAsy = new GetCarBoxOutIDAsy();
        getCarBoxOutIDAsyAsy.execute(url,TaskID,EMPID);
        return getCarBoxOutIDAsyAsy.get();
    }

    private class GetCarBoxOutIDAsy extends  AsyncTask<String,Integer,String>{
        @Override
        protected String doInBackground(String... strings) {
            String url = strings[0];
            HttpPost post = new HttpPost(url);
            Gson gson = new Gson();
            JSONObject p = new JSONObject();
            HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, 1000);
            HttpConnectionParams.setSoTimeout(httpParams, 1000);
            try {
                p.put("TaskID",strings[1]);
                p.put("deviceID",YLSystem.getHandsetIMEI());
                p.put("empid",strings[2]);
                post.setEntity(new StringEntity(p.toString(),"UTF-8"));
                post.setHeader(HTTP.CONTENT_TYPE,"text/json");
                HttpClient client = new DefaultHttpClient(httpParams);
                HttpResponse response = client.execute(post);
                if (response.getStatusLine().getStatusCode() == 200){
                    String content = EntityUtils.toString(response.getEntity());
                    return gson.fromJson(content, String.class);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "0";
            }
            return "0";
        }
    }




    public List<Box> GetCarBoxlist(Context context,String taskid)throws Exception {
        String url = YLSystem.GetBaseUrl(context) + "StoreGetBoxByTaskIDOut";
        CarBoxListAsy carBoxListAsy = new CarBoxListAsy();
        carBoxListAsy.execute(url, taskid);
        return carBoxListAsy.get();
    }

    private class CarBoxListAsy extends AsyncTask<String,Integer,List<Box>>{

        @Override
        protected List<Box> doInBackground(String... strings) {

            HttpPost post = new HttpPost(strings[0]);
            Gson gson = new Gson();
            JSONObject p = new JSONObject();
            try {
                p.put("TaskID", strings[1]);
                p.put("deviceID", YLSystem.getHandsetIMEI());
                p.put("empid", YLSystem.getUser().getEmpID());
                post.setEntity(new StringEntity(p.toString(), "UTF-8"));
                post.setHeader(HTTP.CONTENT_TYPE, "text/json");
                HttpClient client = new DefaultHttpClient();
                HttpResponse response = client.execute(post);
                if (response.getStatusLine().getStatusCode() == 200) {
                    String content = EntityUtils.toString(response.getEntity());
                    return gson.fromJson(content, new TypeToken<List<Box>>() {
                    }.getType());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }


    ///连接数据库失败：999
    ///已经上传成功：1
    ///没有上传过：0
    public String UploadState(Context context,String taskid )throws  Exception{
        String url = YLSystem.GetBaseUrl(context) + "CheckSuccessUpLoad";
        UploadStateAsy uploadStateAsy = new UploadStateAsy();
        uploadStateAsy.execute(url,taskid);
        return uploadStateAsy.get();
    }

    private class UploadStateAsy extends AsyncTask<String,Integer,String>{

        @Override
        protected String doInBackground(String... strings) {

            HttpPost post = new HttpPost(strings[0]);
            Gson gson = new Gson();
            JSONObject p = new JSONObject();
            HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, 1000);
            HttpConnectionParams.setSoTimeout(httpParams, 1000);
            try {
                p.put("TaskID", strings[1]);
                p.put("deviceID", "23");
                p.put("empid", "123");
                post.setEntity(new StringEntity(p.toString(),"UTF-8"));
                post.setHeader(HTTP.CONTENT_TYPE,"text/json");
                HttpClient client = new DefaultHttpClient(httpParams);
                HttpResponse response = client.execute(post);
                if (response.getStatusLine().getStatusCode() == 200){
                    String content = EntityUtils.toString(response.getEntity());
                    return gson.fromJson(content, String.class);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}
