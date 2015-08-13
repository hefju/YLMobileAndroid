package YLDataService;

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
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.List;

import TaskClass.Box;
import TaskClass.User;
import TaskClass.YLTask;
import YLSystemDate.YLEditData;
import YLSystemDate.YLSystem;
import ylescort.ylmobileandroid.YLBoxScan;

/**
 * Created by Administrator on 2015-06-17.
 */

/**
 * 获取所在基地款箱列表
 */
public class WebServerValutturnover {


    /**
     * 获取本基地在库款箱列表
     * @param user 用户类型
     * @param context context
     * @return 款箱列表
     * @throws Exception
     */
    public List<Box> ValutOutBoxList(User user,Context context)throws Exception{
        String url = YLSystem.GetBaseUrl(context)+"StoreGetNowInBoxlList";
        ValutOutBoxListAsyTask valutOutBoxListAsyTask = new ValutOutBoxListAsyTask();
        valutOutBoxListAsyTask.execute(url,user.getTaskDate(),YLSystem.getHandsetIMEI(),user.getEmpID());
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
                p.put("BaseName",params[1]);
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

    /**
     * 获取本日需要入库目标基地列表
     *  <param name="BaseName">基地名称：南海基地</param>
     *  <param name="deviceID">手持机号：</param>
     *  <param name="empid">金库库管员ID：3361</param>
     *  <param name="TaskDate">任务日期：2015-06-24</param>
     * @return 款箱列表
     * @throws Exception
     */

    public List<Box> ValutInBoxList(User user,Context context)throws Exception{
        ValutInBoxListAsyTask valutInBoxListAsyTask = new ValutInBoxListAsyTask();
        String url = YLSystem.GetBaseUrl(context)+"StoreTurnGetAllOutList";
        valutInBoxListAsyTask.execute(url,user.getServerReturn()
                ,YLSystem.getHandsetIMEI(),user.getEmpID(),user.getTaskDate());
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
                p.put("BaseName",params[1]);
                p.put("deviceID",params[2]);
                p.put("empid",params[3]);
                p.put("TaskDate",params[4]);
                post.setEntity(new StringEntity(p.toString(),"UTF-8"));
                post.setHeader(HTTP.CONTENT_TYPE,"text/json");
                HttpClient client = new DefaultHttpClient();
                HttpResponse response = client.execute(post);
                Log.e(YLSystem.getKimTag(), response.getStatusLine().getStatusCode()+"入库服务返回");
                if (response.getStatusLine().getStatusCode() == 200){
                    String content = EntityUtils.toString(response.getEntity());
                    return gson.fromJson(content, new TypeToken<List<Box>>() {}.getType());
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return null;
        }
    }

    /**
     * 上传库内周转出入库记录
     * <param name="STask">任务类包含box类</param>
     * <param name="empid">库管员ID 3361</param>
     * <param name="deviceID">手持机号 NHJ01</param>
     * @return 返回1为成功，0为失败。
     * @throws Exception
     */
    public String Valutturnoverupload(User user,Context context)throws Exception{
        String url = YLSystem.GetBaseUrl(context)+"StoreUploadBoxTurn";
        ValutturnoveruploadAsyTask valutturnoveruploadAsyTask = new ValutturnoveruploadAsyTask();
        valutturnoveruploadAsyTask.execute(url, user.getEmpID(), YLSystem.getHandsetIMEI());
        return valutturnoveruploadAsyTask.get();
    }

    private class ValutturnoveruploadAsyTask extends AsyncTask<String,Integer,String>{
        @Override
        protected String doInBackground(String... params) {
            String url = params[0];
            HttpPost post = new HttpPost(url);
            Gson gson = new Gson();
            JSONObject p = new JSONObject();
            try {
                p.put("STask",gson.toJson(YLEditData.getYlTask()));
                p.put("empid",params[1]);
                p.put("deviceID",params[2]);
                post.setEntity(new StringEntity(p.toString(),"UTF-8"));
                post.setHeader(HTTP.CONTENT_TYPE,"text/json");
                HttpClient client = new DefaultHttpClient();
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

    /**
     /// 获取库内周转出库表，出库到指定基地的箱列表，如果上传了就获取上传的列表。状态是出，目标基地是BaseName
     /// </summary>
     /// <param name="SourceBase">源基地：南海基地</param>
     /// <param name="TargetBase">目标基地：乐从基地</param>
     /// <param name="deviceID">手持机号：</param>
     /// <param name="empid">金库库管员ID：3361</param>
     /// <param name="TaskDate1">任务日期：2015-06-24</param>
     */

    public List<Box> VaultTrunoverOutBoxList
    (String SourceBase, String TargetBase, String deviceID
            , String empid, String TaskDate,Context context)throws Exception{
        Vaulttrunoverout vaulttrunoverout  = new Vaulttrunoverout();
        String url = YLSystem.GetBaseUrl(context)+"StoreTurnGetBoxListOut";
        vaulttrunoverout.execute(url,SourceBase,TargetBase,YLSystem.getHandsetIMEI(),empid,TaskDate);
        return vaulttrunoverout.get();
    }

    private class Vaulttrunoverout extends AsyncTask<String,Integer,List<Box>>{
        @Override
        protected List<Box> doInBackground(String... params) {
            String url = params[0];
            HttpPost post = new HttpPost(url);
            Gson gson = new Gson();
            JSONObject p = new JSONObject();
            try {
                p.put("SourceBase",params[1]);
                p.put("TargetBase",params[2]);
                p.put("deviceID",params[3]);
                p.put("empid",params[4]);
                p.put("TaskDate",params[5]);
                post.setEntity(new StringEntity(p.toString(),"UTF-8"));
                post.setHeader(HTTP.CONTENT_TYPE,"text/json");
                HttpClient client = new DefaultHttpClient();
                HttpResponse response = client.execute(post);
                Log.e(YLSystem.getKimTag(),"服务返回："+response.getStatusLine().getStatusCode());
                if (response.getStatusLine().getStatusCode() == 200){
                    String content = EntityUtils.toString(response.getEntity());
                    return gson.fromJson(content, new TypeToken<List<Box>>() {}.getType());
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return null;
        }
    }

    public String UpLoadPrintlalbe(Context context, User user)throws Exception{
        UpLoadPrintlalbeAsyncTask upLoadPrintlalbeAsyncTask = new UpLoadPrintlalbeAsyncTask();
        String url = YLSystem.GetBaseUrl(context)+"StoreUploadCountBadBoxRecord";
        upLoadPrintlalbeAsyncTask.execute(url,user.getEmpID(),YLSystem.getHandsetIMEI());
        return upLoadPrintlalbeAsyncTask.get();
    }


    private class UpLoadPrintlalbeAsyncTask extends AsyncTask<String,Integer,String>{
        @Override
        protected String doInBackground(String... params) {
            String url = params[0];
            HttpPost post = new HttpPost(url);
            Gson gson = new Gson();
            JSONObject p = new JSONObject();
            try {
                p.put("STask",gson.toJson(YLEditData.getYlTask()));
                Log.e(YLSystem.getKimTag(),YLEditData.getYlTask().toString());
                p.put("empid",params[1]);
                p.put("deviceID",params[2]);
                post.setEntity(new StringEntity(p.toString(),"UTF-8"));
                post.setHeader(HTTP.CONTENT_TYPE,"text/json");
                HttpClient client = new DefaultHttpClient();
                HttpResponse response = client.execute(post);
                if (response.getStatusLine().getStatusCode() == 200){
                    String content = EntityUtils.toString(response.getEntity());
                    return gson.fromJson(content, String.class);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return null;
        }
    }

}
