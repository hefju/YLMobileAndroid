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
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.List;

import TaskClass.BaseBox;
import TaskClass.BaseClient;
import TaskClass.BaseEmp;
import TaskClass.BaseSite;
import TaskClass.Box;
import YLSystemDate.YLSystem;

/**
 * Created by Administrator on 2015-07-09.
 */
public class WebServerBaseData {

    public ProgressDialog progressDialog;
    private BaseEmpDBSer baseEmpDBSer;
    private BaseSiteDBSer baseSiteDBSer;
    private BaseClientDBSer baseClientDBSer;
    private BaseBoxDBSer baseBoxDBSer;

    public void GetBaseData(Context context)throws Exception{
        progressDialog = new ProgressDialog(context);
        baseEmpDBSer = new BaseEmpDBSer(context);
        baseSiteDBSer = new BaseSiteDBSer(context);
        baseClientDBSer = new BaseClientDBSer(context);
        baseBoxDBSer = new BaseBoxDBSer(context);
        Log.e(YLSystem.getKimTag(),"开始");
        String DeviceID = YLSystem.getHandsetIMEI();
        String isWifi =YLSystem.getNetWorkState();
        String empurl = YLSystem.GetBaseUrl(context)+"GetBaseEmp";
        String Clienturl = YLSystem.GetBaseUrl(context)+"GetBaseClient";
        String Siteurl = YLSystem.GetBaseUrl(context)+"GetBaseSite";
        String boxurl = YLSystem.GetBaseUrl(context)+"GetBaseBox";
        AnysTaskGetBaseData anysTaskGetBaseData = new AnysTaskGetBaseData();
        anysTaskGetBaseData.execute(DeviceID,isWifi,empurl,Clienturl,Siteurl,boxurl);
        anysTaskGetBaseData.get();
    }

    private class AnysTaskGetBaseData extends AsyncTask<String,Integer,String>{
        @Override
        protected String doInBackground(String... params) {

            try {
                String url = params[2];
                HttpPost post = new HttpPost(url);
                Gson gson = new Gson();
                JSONObject p = new JSONObject();
                p.put("DeviceID", params[0]);
                p.put("ISWIFI", params[1]);
                p.put("datetime", "ALL");
                post.setEntity(new StringEntity(p.toString(), "UTF-8"));
                post.setHeader(HTTP.CONTENT_TYPE, "text/json");
                HttpClient client = new DefaultHttpClient();
                HttpResponse response = client.execute(post);
                if (response.getStatusLine().getStatusCode() == 200) {
                    String content = EntityUtils.toString(response.getEntity());
                    List<BaseEmp> emps =  gson.fromJson(content, new TypeToken<List<BaseEmp>>() {
                    }.getType());

                    baseEmpDBSer.InsertBaseEmp(emps);

                    Log.e(YLSystem.getKimTag(),emps.size()+"人员数据");
                }

                post = new HttpPost(params[3]);
                p.put("DeviceID", params[0]);
                p.put("ISWIFI", params[1]);
                p.put("datetime", "ALL");
                post.setEntity(new StringEntity(p.toString(), "UTF-8"));
                post.setHeader(HTTP.CONTENT_TYPE, "text/json");
                client = new DefaultHttpClient();
                response = client.execute(post);
                if (response.getStatusLine().getStatusCode() == 200){
                    String content = EntityUtils.toString(response.getEntity());
                    List<BaseClient> baseClients =  gson.fromJson(content, new TypeToken<List<BaseClient>>() {
                    }.getType());
                    baseClientDBSer.InsertBaseClient(baseClients);
                    Log.e(YLSystem.getKimTag(),baseClients.size()+"客户数据");
                }

                post = new HttpPost(params[4]);
                p.put("DeviceID", params[0]);
                p.put("ISWIFI", params[1]);
                p.put("datetime", "ALL");
                post.setEntity(new StringEntity(p.toString(), "UTF-8"));
                post.setHeader(HTTP.CONTENT_TYPE, "text/json");
                client = new DefaultHttpClient();
                response = client.execute(post);
                if (response.getStatusLine().getStatusCode() == 200){
                    String content = EntityUtils.toString(response.getEntity());
                    List<BaseSite> siteList =  gson.fromJson(content, new TypeToken<List<BaseSite>>() {
                    }.getType());
                    baseSiteDBSer.InsertBaseSite(siteList);
                    Log.e(YLSystem.getKimTag(),siteList.size()+"网点数据");
                }

                post = new HttpPost(params[5]);
                p.put("DeviceID", params[0]);
                p.put("ISWIFI", params[1]);
                p.put("datetime", "ALL");
                post.setEntity(new StringEntity(p.toString(), "UTF-8"));
                post.setHeader(HTTP.CONTENT_TYPE, "text/json");
                client = new DefaultHttpClient();
                response = client.execute(post);
                if (response.getStatusLine().getStatusCode() == 200){
                    String content = EntityUtils.toString(response.getEntity());
                    List<BaseBox> baseBoxes =  gson.fromJson(content, new TypeToken<List<BaseBox>>() {
                    }.getType());
                    baseBoxDBSer.InsertBox(baseBoxes);
                    Log.e(YLSystem.getKimTag(),baseBoxes.size()+"款箱数据");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            super.onPostExecute(s);
        }
    }


}
