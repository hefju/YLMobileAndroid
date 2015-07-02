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
import YLSystemDate.YLSystem;

/**
 * Created by Administrator on 2015-06-29.
 */
public class WebServerValutInorOut {

    /**
     * 根据任务获取入库箱数
     * @param TaskID 任务ID
     * @param deviceID 手持机IMEI
     * @param EmpID 库管员EmpID
     * @param context context
     * @return list<Box>
     * @throws Exception
     */

    public List<Box> GetVaultInBoxList(String TaskID,String deviceID,String EmpID,Context context)throws Exception{
        String url = YLSystem.GetBaseUrl(context)+"StoreGetBoxByTaskID";
        GetVaultInBoxListAsycnTask getVaultInBoxListAsycnTask = new GetVaultInBoxListAsycnTask();
        getVaultInBoxListAsycnTask.execute(url,TaskID,deviceID,EmpID);
        return getVaultInBoxListAsycnTask.get();
    }

    private class GetVaultInBoxListAsycnTask extends AsyncTask<String,Integer,List<Box>> {
        @Override
        protected List<Box> doInBackground(String... params) {
            String url = params[0];
            HttpPost post = new HttpPost(url);
            Gson gson = new Gson();
            JSONObject p = new JSONObject();
            try {
                p.put("TaskID",params[1]);
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
     /// <summary>
     /// 库管员上传的出库箱表--。。。
     /// </summary>
     /// <param name="TaskID"></param>
     /// <param name="deviceID"></param>
     /// <param name="empid"></param>
     /// <returns></returns>
     */
    public List<Box> StoreGetBoxByTaskIDOut(User user,Context context) throws Exception{
        String url = YLSystem.GetBaseUrl(context)+"StoreGetBoxByTaskIDOut";
        GetVaultkeeperoutBox getVaultkeeperoutBox = new GetVaultkeeperoutBox();
        getVaultkeeperoutBox.execute(url,user.getTaskDate(),user.getDeviceID(),user.getEmpID());
        return getVaultkeeperoutBox.get();
    }

    private class GetVaultkeeperoutBox extends AsyncTask<String,Integer,List<Box>>{

        @Override
        protected List<Box> doInBackground(String... params) {
            String url = params[0];
            HttpPost post = new HttpPost(url);
            Gson gson = new Gson();
            JSONObject p = new JSONObject();
            try {
                p.put("TaskID",params[1]);
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
