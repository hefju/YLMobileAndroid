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

/**
 * Created by Administrator on 2015-10-24.
 */
public class WebServerTmpValutInorOut {

    private Context WebServercontext;
    public WebServerTmpValutInorOut(Context context){
        this.WebServercontext = context;
    }

    /// 业务员申请临时出入库
    /// </summary>
    /// <param name="TaskID">任务ID：33211</param>
    /// <param name="empid">业务员id：3280</param>
    /// <param name="deviceID">手持机硬件号:861189013980320</param>
    /// <param name="ISWIFI">是否使用wifi：1或者0</param>
    /// <param name="TimeID">申请出库：1或者 申请入库：2</param>
    /// <param name="BaseName">申请对应出入库的基地。。。</param>
    /// <returns>"1";为正确，其他为出错。</returns>
    public String ComfirmValuttmpinorout( User user)throws Exception{
        String url =YLSystem.GetBaseUrl(WebServercontext)+"ComfirmStoreInOutTemp";
        ComfirmValutTmpInOrOut comfirmValutTmpInOrOut = new ComfirmValutTmpInOrOut();
        comfirmValutTmpInOrOut.execute(url,user.getTaskDate(), user.getEmpID(),YLSystem.getHandsetIMEI(),
                YLSystem.getNetWorkState(),user.getTime(),user.getName());
        return comfirmValutTmpInOrOut.get();
    }

    private class ComfirmValutTmpInOrOut extends AsyncTask<String,Integer,String>{

        @Override
        protected String doInBackground(String... strings) {

            String url = strings[0];
            HttpPost post = new HttpPost(url);
            Gson gson = new Gson();
            JSONObject p = new JSONObject();
            try {
                p.put("TaskID",strings[1]);
                p.put("empid",strings[2]);
                p.put("deviceID",strings[3]);
                p.put("ISWIFI",strings[4]);
                p.put("TimeID",strings[5]);
                p.put("BaseName",strings[6]);
                post.setEntity(new StringEntity(p.toString(),"UTF-8"));
                post.setHeader(HTTP.CONTENT_TYPE,"text/json");
                HttpClient client = new DefaultHttpClient();
                HttpResponse response = client.execute(post);
                if (response.getStatusLine().getStatusCode() == 200){
                    String content = EntityUtils.toString(response.getEntity());
                    Log.e(YLSystem.getKimTag(),content+"确认申请出入库");
                    return gson.fromJson(content, String.class);

                }
            } catch (Exception e) {
                e.printStackTrace();
                return "0";
            }
            return null;
        }
    }


    /// 获取本基地业务员已申请临时入库的任务
    /// </summary>
    /// <param name="DataTime">日期：2015-06-30</param>
    /// <param name="deviceID">手持机硬件号:861189013980320 </param>
    /// <param name="empid">库管员id：3280</param>
    /// <param name="TimeID">出库：1或者 入库：2</param>
    /// <returns>
    ///YLTask.ServerReturn "1";为正确，其他为出错
    ///YLTask.ServerVersion;任务版本号
    ///YLTask.TaskID 任务ID
    ///YLTask.TaskType 任务类型
    ///YLTask.Handset ;任务手持机号
    ///YLTask.TaskDate ;任务日期
    ///YLTask.Line ;任务名
    ///YLTask.TaskManagerNo ;任务执行人员工号
    ///YLTask.TaskManager ;任务执行人
    /// </returns>

    public List<YLTask> GetTmpTaskList(User user)throws Exception{
        GetTmpTaskList getTmpTaskList = new GetTmpTaskList();
        getTmpTaskList.execute(user.getTaskDate(),YLSystem.getHandsetIMEI()
                ,user.getEmpID(),user.getTime());
        return getTmpTaskList.get();
    }

    private class GetTmpTaskList extends AsyncTask<String,Integer,List<YLTask>>{
        @Override
        protected List<YLTask> doInBackground(String... strings) {

            String url = YLSystem.GetBaseUrl(WebServercontext)+"StoreInOutGetBaseAllTaskTemp";
            HttpPost post = new HttpPost(url);
            Gson gson = new Gson();
            JSONObject p = new JSONObject();
            try {
                p.put("DataTime",strings[0]);
                p.put("deviceID",strings[1]);
                p.put("empid",strings[2]);
                p.put("TimeID",strings[3]);
                post.setEntity(new StringEntity(p.toString(),"UTF-8"));
                post.setHeader(HTTP.CONTENT_TYPE,"text/json");
                HttpClient client = new DefaultHttpClient();
                HttpResponse response = client.execute(post);
                if (response.getStatusLine().getStatusCode() == 200){
                    String content = EntityUtils.toString(response.getEntity());
                    Log.e(YLSystem.getKimTag(),content+"临时出入库任务");
                    return gson.fromJson(content, new TypeToken<List<YLTask>>() {}.getType());

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public String UpLoadBoxTmp(String empid) throws  Exception{
        UpLoadBoxTmp upLoadBoxTmp = new UpLoadBoxTmp();
        upLoadBoxTmp.execute(empid, YLSystem.getHandsetIMEI(), YLSystem.getNetWorkState());
        return upLoadBoxTmp.get();
    }

    private class UpLoadBoxTmp extends AsyncTask<String,Integer,String>{
        @Override
        protected String doInBackground(String... strings) {
            String url = YLSystem.GetBaseUrl(WebServercontext)+"UpLoadBoxTemp";
            HttpPost post = new HttpPost(url);
            Gson gson = new Gson();
            JSONObject p = new JSONObject();
            try {
                p.put("STask", gson.toJson(YLEditData.getYlTask()));
                p.put("empid",strings[0]);
                p.put("deviceID",strings[1]);
                p.put("ISWIFI",strings[2]);
                post.setEntity(new StringEntity(p.toString(),"UTF-8"));
                post.setHeader(HTTP.CONTENT_TYPE,"text/json");
                HttpClient client = new DefaultHttpClient();
                HttpResponse response = client.execute(post);
                if (response.getStatusLine().getStatusCode() == 200){
                    String content = EntityUtils.toString(response.getEntity());
                    return gson.fromJson(content,  String.class);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}
