package YLDataService;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
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

import TaskClass.BaseATMBox;
import TaskClass.BaseATMMachine;
import TaskClass.BaseBox;
import TaskClass.BaseClient;
import TaskClass.BaseEmp;
import TaskClass.BaseSite;
import TaskClass.Box;
import YLSystemDate.YLSysTime;
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
    private ATMBoxDBSer atmBoxDBSer;
    private ATMMachineDBSer atmMachineDBSer;

    public void GetBaseData(Context context,String updatetime)throws Exception{
//        progressDialog = new ProgressDialog(context);
        baseEmpDBSer = new BaseEmpDBSer(context);
        baseSiteDBSer = new BaseSiteDBSer(context);
        baseClientDBSer = new BaseClientDBSer(context);
        baseBoxDBSer = new BaseBoxDBSer(context);
        atmBoxDBSer = new ATMBoxDBSer(context);
        atmMachineDBSer = new ATMMachineDBSer(context);

        Log.e(YLSystem.getKimTag(),"开始");
        String DeviceID = YLSystem.getHandsetIMEI();
        String isWifi =YLSystem.getNetWorkState();
        String empurl = YLSystem.GetBaseUrl(context)+"GetBaseEmp";
        String Clienturl = YLSystem.GetBaseUrl(context)+"GetBaseClient";
        String Siteurl = YLSystem.GetBaseUrl(context)+"GetBaseSite";
        String boxurl = YLSystem.GetBaseUrl(context)+"GetBaseBox";
        String atmbox = YLSystem.GetBaseUrl(context)+"GetBaseATMBox";
        String atmmachine = YLSystem.GetBaseUrl(context)+"GetBaseATMMachine";

        AnysTaskGetBaseData anysTaskGetBaseData = new AnysTaskGetBaseData();
        anysTaskGetBaseData.execute(DeviceID,isWifi,empurl,Clienturl,Siteurl,boxurl,updatetime,atmbox,atmmachine);
        String servertime =  anysTaskGetBaseData.get();
        if (!servertime.equals("")){
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("CacheLastUpdate", servertime);
            editor.apply();
        }

    }

    private class AnysTaskGetBaseData extends AsyncTask<String,Integer,String>{
        @Override
        protected String doInBackground(String... params) {
            String servertime = "";
            try {
                String url = params[2];
                HttpPost post = new HttpPost(url);
                Gson gson = new Gson();
                JSONObject p = new JSONObject();
                p.put("DeviceID", params[0]);
                p.put("ISWIFI", params[1]);
                p.put("datetime", params[6]);
                post.setEntity(new StringEntity(p.toString(), "UTF-8"));
                post.setHeader(HTTP.CONTENT_TYPE, "text/json");
                HttpClient client = new DefaultHttpClient();
                HttpResponse response = client.execute(post);
                if (response.getStatusLine().getStatusCode() == 200) {
                    String content = EntityUtils.toString(response.getEntity());
                    List<BaseEmp> emps =  gson.fromJson(content, new TypeToken<List<BaseEmp>>() {
                    }.getType());
                    if (emps.size()>0 & emps.get(0).getEmpID() !=null){
                        baseEmpDBSer.CacheBaseEmp(emps);
                        servertime = emps.get(0).ServerTime;
                        Log.e(YLSystem.getKimTag(), emps.size()+"人员数据");
                    }
                }

                post = new HttpPost(params[3]);
                p.put("DeviceID", params[0]);
                p.put("ISWIFI", params[1]);
                p.put("datetime", params[6]);
                post.setEntity(new StringEntity(p.toString(), "UTF-8"));
                post.setHeader(HTTP.CONTENT_TYPE, "text/json");
                client = new DefaultHttpClient();
                response = client.execute(post);
                if (response.getStatusLine().getStatusCode() == 200){
                    String content = EntityUtils.toString(response.getEntity());
                    List<BaseClient> baseClients =  gson.fromJson(content, new TypeToken<List<BaseClient>>() {
                    }.getType());
                    if (baseClients.size()>0 & baseClients.get(0).getClientID() != null){
                        baseClientDBSer.CacheBaseClient(baseClients);
                        if (servertime.equals("")){
                            servertime = baseClients.get(0).ServerTime;
                        }
                        Log.e(YLSystem.getKimTag(), baseClients.size() + "客户数据");
                    }
                }

                post = new HttpPost(params[4]);
                p.put("DeviceID", params[0]);
                p.put("ISWIFI", params[1]);
                p.put("datetime", params[6]);
                post.setEntity(new StringEntity(p.toString(), "UTF-8"));
                post.setHeader(HTTP.CONTENT_TYPE, "text/json");
                client = new DefaultHttpClient();
                response = client.execute(post);
                if (response.getStatusLine().getStatusCode() == 200){
                    String content = EntityUtils.toString(response.getEntity());
                    List<BaseSite> siteList =  gson.fromJson(content, new TypeToken<List<BaseSite>>() {
                    }.getType());
                    if ( siteList.size() >0 & siteList.get(0).getSiteID() != null){
                        baseSiteDBSer.CacheBaseSite(siteList);
                        if (servertime.equals("")){
                            servertime = siteList.get(0).ServerTime;
                        }
                        Log.e(YLSystem.getKimTag(),siteList.size()+"网点数据");
                    }
                }

                post = new HttpPost(params[5]);
                p.put("DeviceID", params[0]);
                p.put("ISWIFI", params[1]);
                p.put("datetime", params[6]);
                post.setEntity(new StringEntity(p.toString(), "UTF-8"));
                post.setHeader(HTTP.CONTENT_TYPE, "text/json");
                client = new DefaultHttpClient();
                response = client.execute(post);
                if (response.getStatusLine().getStatusCode() == 200){
                    String content = EntityUtils.toString(response.getEntity());
                    List<BaseBox> baseBoxes =  gson.fromJson(content, new TypeToken<List<BaseBox>>() {
                    }.getType());
                    if (baseBoxes.size() > 0 & baseBoxes.get(0).getBoxID() != null) {
                        baseBoxDBSer.CacheBaseBox(baseBoxes);
                        if (servertime.equals("")){
                            servertime = baseBoxes.get(0).ServerTime;
                        }
                        Log.e(YLSystem.getKimTag(), baseBoxes.size() + "款箱数据");
                    }
                }

                post = new HttpPost(params[7]);
                p.put("DeviceID", params[0]);
                p.put("ISWIFI", params[1]);
                p.put("datetime", params[6]);
                post.setEntity(new StringEntity(p.toString(), "UTF-8"));
                post.setHeader(HTTP.CONTENT_TYPE, "text/json");
                client = new DefaultHttpClient();
                response = client.execute(post);
                if (response.getStatusLine().getStatusCode() == 200){
                    String content = EntityUtils.toString(response.getEntity());
                    List<BaseATMBox> baseabmBoxes =  gson.fromJson(content, new TypeToken<List<BaseATMBox>>() {
                    }.getType());
                    if (baseabmBoxes.size() > 0 & baseabmBoxes.get(0).getATMBoxID() != null) {
                        atmBoxDBSer.CacheBaseATMBox(baseabmBoxes);
                        if (servertime.equals("")){
                            servertime = baseabmBoxes.get(0).ServerTime;
                        }
                        Log.e(YLSystem.getKimTag(), baseabmBoxes.size() + "ATM款箱数据");
                    }
                }

                post = new HttpPost(params[8]);
                p.put("DeviceID", params[0]);
                p.put("ISWIFI", params[1]);
                p.put("datetime", params[6]);
                post.setEntity(new StringEntity(p.toString(), "UTF-8"));
                post.setHeader(HTTP.CONTENT_TYPE, "text/json");
                client = new DefaultHttpClient();
                response = client.execute(post);
                if (response.getStatusLine().getStatusCode() == 200){
                    String content = EntityUtils.toString(response.getEntity());
                    List<BaseATMMachine> baseATMMachines =  gson.fromJson(content, new TypeToken<List<BaseATMMachine>>() {
                    }.getType());
                    if (baseATMMachines.size() > 0 & baseATMMachines.get(0).getMachineID() != null) {
                        atmMachineDBSer.CacheBaseATMMachine(baseATMMachines);
                        if (servertime.equals("")){
                            servertime = baseATMMachines.get(0).getServerTime();
                        }
                        Log.e(YLSystem.getKimTag(), baseATMMachines.size() + "ATM点数据");
                    }
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
            return servertime;
        }


        @Override
        protected void onPostExecute(String s) {
//            progressDialog.dismiss();
            super.onPostExecute(s);
        }
    }

    public void CacheData(final Context context ) {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                    String timeLastUpdate = prefs.getString("CacheLastUpdate", "ALL");
                    Log.e(YLSystem.getKimTag(), timeLastUpdate + "缓存时间");
                    if (!timeLastUpdate.equals("ALL")) {
                        WebServerBaseData webServerBaseData = new WebServerBaseData();
                        webServerBaseData.GetBaseData(context, timeLastUpdate);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

}
