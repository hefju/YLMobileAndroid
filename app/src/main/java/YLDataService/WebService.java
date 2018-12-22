package YLDataService;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import org.apache.http.HttpConnection;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import TaskClass.BaseBox;
import TaskClass.BaseClient;
import TaskClass.BaseEmp;
import TaskClass.BaseSite;
import TaskClass.Box;
import TaskClass.Site;
import TaskClass.User;
import TaskClass.YLTask;
import YLSystemDate.YLEditData;
import YLSystemDate.YLSystem;

/**
 * Created by Administrator on 2015/1/28.
 */
public class WebService {
    public static String webserviceaddress = "";

    ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();

    android.os.Handler mh = new android.os.Handler() {   //以Handler为桥梁将结果传入UI
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    public final String UserWebContent(String webapi, User user) throws JSONException, IOException {

        webserviceaddress += webapi;
        HttpPost post = new HttpPost(webserviceaddress);
        Gson gson = new Gson();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("user", gson.toJson(user));
        post.setEntity(new StringEntity(jsonObject.toString(), "UTF-8"));//将参数设置入POST请求
        post.setHeader(HTTP.CONTENT_TYPE, "text/json");//设置为json格式。
        HttpClient client = new DefaultHttpClient();
        HttpResponse response = client.execute(post);
        if (response.getStatusLine().getStatusCode() == 200) {
            String content = EntityUtils.toString(response.getEntity());    //得到返回字符串
            User getjsonuser = gson.fromJson(content, new TypeToken<User>() {
            }.getType());
            return getjsonuser.getServerReturn();
        } else {
            return null;
        }
    }

    public final static String TaskWebContent(String webapi, User user) throws Exception {

        webserviceaddress += webapi;
        HttpPost post = new HttpPost(webserviceaddress);
        Gson gson = new Gson();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("user", gson.toJson(user));
        post.setEntity(new StringEntity(jsonObject.toString(), "UTF-8"));//将参数设置入POST请求
        post.setHeader(HTTP.CONTENT_TYPE, "text/json");//设置为json格式。
        HttpClient client = new DefaultHttpClient();
        HttpResponse response = client.execute(post);
        if (response.getStatusLine().getStatusCode() == 200) {
            String content = EntityUtils.toString(response.getEntity());    //得到返回字符串
            return content;
        } else {
            return null;
        }
    }

    private String webcontent = null;

    public String getServerVer() {
        webcontent = "1.02";
        return webcontent;
    }

    private  static  void  SetLastUpdateTime(final Context ctx,String date)
    {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor edit = settings.edit();
        edit.putString("CacheLastUpdate", date);//YLSystem.getUser().getTime()
        edit.apply();
    }
    public static  void CacheData(final Context ctx) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        String timeLastUpdate = prefs.getString("CacheLastUpdate", "ALL");
        final String[] servertime = {""};

        final Integer[] finish = {0};
        Handler  mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {

                String content ;
                switch (msg.what) {
                    case 20:
                        break;
                    case 21:
                        content = (String) msg.obj;
                        finish[0]++;
                        break;
                    case 22:
//                        content = (String) msg.obj;
//                        servertime[0] =content;
//                        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//                        String date = sDateFormat.format(new java.util.Date());
//                        //测试不开
//                        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(ctx);
//                        SharedPreferences.Editor edit = settings.edit();
//                        edit.putString("CacheLastUpdate", date);//YLSystem.getUser().getTime()
//                        edit.apply();
                        break;
                    case 100:
////                        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
////                        String date = sDateFormat.format(new java.util.Date());
//                        String date = servertime[0];
//                        //测试不开
//                        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(ctx);
//                        SharedPreferences.Editor edit = settings.edit();
//                        edit.putString("CacheLastUpdate", date);//YLSystem.getUser().getTime()
//                        edit.apply();
                        break;
                }


                super.handleMessage(msg);
            }
        };
        GetBaseEmp( ctx, mHandler,  timeLastUpdate) ;
        GetBaseClient( ctx, mHandler,  timeLastUpdate) ;
        GetBaseSite( ctx, mHandler,  timeLastUpdate) ;
        GetBaseBox( ctx, mHandler,  timeLastUpdate) ;
//        int count=0;
//        while (count<10){
//            try {
//                Thread.sleep(1000*5);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            if (  finish[0] ==4  ){
//                Message msg = mHandler.obtainMessage(100);
//                mHandler.sendMessage(msg);
//                break;
//            }
//        }

    }
    public static void GetBaseEmp(final Context ctx, final Handler mHandler,  final String timeLastUpdate) {
        new Thread() {
            public void run() {
                String url =YLSystem.GetBaseUrl(ctx)+ "GetBaseEmp";//网址
                HttpPost post = new HttpPost(url);
//                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
//                String timeLastUpdate = prefs.getString("CacheLastUpdate", "ALL");

                List<BaseEmp> ListBaseEmp = new ArrayList<BaseEmp>();
                //添加数值到User类
                Gson gson = new Gson();
                //设置POST请求中的参数-------返回EmpID员工ID，ServerReturn 服务器错误，1为没错误，Time 服务器时间。
                try {
                    JSONObject p = new JSONObject();
                    p.put("DeviceID", YLSystem.getHandsetIMEI()); //"NH008");//
                    p.put("ISWIFI", YLSystem.isWifiActive(ctx));//"1");//
                    p.put("datetime", timeLastUpdate);
                    post.setEntity(new StringEntity(p.toString(), "UTF-8"));//将参数设置入POST请求
                    post.setHeader(HTTP.CONTENT_TYPE, "text/json");//设置为json格式。

                    HttpClient client = new DefaultHttpClient();
                    HttpResponse response = null;
                    response = client.execute(post);

                    if (response.getStatusLine().getStatusCode() == 200) {
                        String content = EntityUtils.toString(response.getEntity());
                        ListBaseEmp = gson.fromJson(content, new TypeToken<List<BaseEmp>>() {
                        }.getType());
                        Log.d("jutest", "GetBaseEmp:" + ListBaseEmp.size() + " timeLastUpdate:" + timeLastUpdate);//打印到logcat

                        Message msg =null;
                        if(mHandler!=null) {
                            msg = mHandler.obtainMessage(20);
                            msg.obj = "BaseEmp下载完毕";
                            mHandler.sendMessage(msg);
                        }

                        (new BaseEmp()).CacheBaseEmp(ctx, ListBaseEmp);//保存到数据库
                        if(mHandler!=null) {
                            msg = mHandler.obtainMessage(21);
                            msg.obj = "BaseEmp更新完毕";
                            mHandler.sendMessage(msg);

                            if(ListBaseEmp.size()>0) {
//                                msg = mHandler.obtainMessage(22);
//                                msg.obj = ListBaseEmp.get(0).ServerTime;
//                                mHandler.sendMessage(msg);
                                if(ListBaseEmp.get(0).ServerReturn.equals("1")) {
                                    Thread.sleep(2000);
                                    SetLastUpdateTime(ctx, ListBaseEmp.get(0).ServerTime);
                                    Log.d("jutest", "GetBaseEmp:时间:" + ListBaseEmp.get(0).ServerTime);
                                }
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public static void GetBaseClient(final Context ctx, final Handler mHandler,  final String timeLastUpdate) {
        new Thread() {
            public void run() {
                String url =YLSystem.GetBaseUrl(ctx)+ "GetBaseClient";//网址
                HttpPost post = new HttpPost(url);
//                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
//                String timeLastUpdate = prefs.getString("CacheLastUpdate", "ALL");

                List<BaseClient> ListBase = new ArrayList<BaseClient>();
                //添加数值到User类
                Gson gson = new Gson();
                //设置POST请求中的参数-------返回EmpID员工ID，ServerReturn 服务器错误，1为没错误，Time 服务器时间。
                try {
                    JSONObject p = new JSONObject();
                    p.put("DeviceID", YLSystem.getHandsetIMEI()); //"NH008");//
                    p.put("ISWIFI", YLSystem.isWifiActive(ctx));//"1");//
                    p.put("datetime", timeLastUpdate);
                    post.setEntity(new StringEntity(p.toString(), "UTF-8"));//将参数设置入POST请求
                    post.setHeader(HTTP.CONTENT_TYPE, "text/json");//设置为json格式。

                    HttpClient client = new DefaultHttpClient();
                    HttpResponse response = null;
                    response = client.execute(post);

                    if (response.getStatusLine().getStatusCode() == 200) {
                        String content = EntityUtils.toString(response.getEntity());
                        ListBase = gson.fromJson(content, new TypeToken<List<BaseClient>>() {
                        }.getType());
                        Log.d("jutest", "GetBaseClient:" + ListBase.size());//打印到logcat

                        Message msg =null;
                        if(mHandler!=null) {
                            msg = mHandler.obtainMessage(20);
                            msg.obj = "BaseClient下载完毕";
                            mHandler.sendMessage(msg);
                        }

                        (new BaseClient()).CacheBaseClient(ctx, ListBase);//保存到数据库
                        if(mHandler!=null) {
                            msg = mHandler.obtainMessage(21);
                            msg.obj ="BaseClient更新完毕";
                            mHandler.sendMessage(msg);

                            if(ListBase.size()>0) {
//                                msg = mHandler.obtainMessage(22);
//                                msg.obj = ListBase.get(0).ServerTime;
//                                mHandler.sendMessage(msg);
                                if (ListBase.get(0).ServerReturn.equals("1")) {
                                    Thread.sleep(2000);
                                    SetLastUpdateTime(ctx, ListBase.get(0).ServerTime);
                                    Log.d("jutest", "GetBaseClient:时间:" + ListBase.get(0).ServerTime);
                                }
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public static void GetBaseSite(final Context ctx, final Handler mHandler,  final String timeLastUpdate) {
        new Thread() {
            public void run() {
                String url =YLSystem.GetBaseUrl(ctx)+ "GetBaseSite";//网址
                HttpPost post = new HttpPost(url);
//                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
//                String timeLastUpdate = prefs.getString("CacheLastUpdate", "ALL");

                List<BaseSite> ListBase = new ArrayList<BaseSite>();
                //添加数值到User类
                Gson gson = new Gson();
                //设置POST请求中的参数-------返回EmpID员工ID，ServerReturn 服务器错误，1为没错误，Time 服务器时间。
                try {
                    JSONObject p = new JSONObject();
                    p.put("DeviceID", YLSystem.getHandsetIMEI()); //"NH008");//
                    p.put("ISWIFI", YLSystem.isWifiActive(ctx));//"1");//
                    p.put("datetime", timeLastUpdate);
                    post.setEntity(new StringEntity(p.toString(), "UTF-8"));//将参数设置入POST请求
                    post.setHeader(HTTP.CONTENT_TYPE, "text/json");//设置为json格式。

                    HttpClient client = new DefaultHttpClient();
                    HttpResponse response = null;
                    response = client.execute(post);

                    if (response.getStatusLine().getStatusCode() == 200) {
                        String content = EntityUtils.toString(response.getEntity());
                        ListBase = gson.fromJson(content, new TypeToken<List<BaseSite>>() {
                        }.getType());
                        Log.d("jutest", "GetBaseSite:" + ListBase.size());//打印到logcat

                        Message msg =null;
                        if(mHandler!=null) {
                            msg = mHandler.obtainMessage(20);
                            msg.obj = "BaseSite下载完毕";
                            mHandler.sendMessage(msg);
                        }

                        (new BaseSite()).CacheBaseSite(ctx, ListBase);//保存到数据库
                        if(mHandler!=null) {
                            msg = mHandler.obtainMessage(21);
                            msg.obj = "BaseSite更新完毕";
                            mHandler.sendMessage(msg);

                            if(ListBase.size()>0) {
//                                msg = mHandler.obtainMessage(22);
//                                msg.obj = ListBase.get(0).ServerTime;
//                                mHandler.sendMessage(msg);
                                if (ListBase.get(0).ServerReturn.equals("1")) {
                                    Thread.sleep(2000);
                                    SetLastUpdateTime(ctx, ListBase.get(0).ServerTime);
                                    Log.d("jutest", "GetBaseSite时间:" + ListBase.get(0).ServerTime);
                                }
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public static void GetBaseBox(final Context ctx, final Handler mHandler,  final String timeLastUpdate) {
        new Thread() {
            public void run() {
                String url =YLSystem.GetBaseUrl(ctx)+ "GetBaseBox";//网址
                HttpPost post = new HttpPost(url);
//                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
//                String timeLastUpdate = prefs.getString("CacheLastUpdate", "ALL");

                List<BaseBox> ListBase = new ArrayList<BaseBox>();
                //添加数值到User类
                Gson gson = new Gson();
                //设置POST请求中的参数-------返回EmpID员工ID，ServerReturn 服务器错误，1为没错误，Time 服务器时间。
                try {
                    JSONObject p = new JSONObject();
                    p.put("DeviceID", YLSystem.getHandsetIMEI()); //"NH008");//
                    p.put("ISWIFI", YLSystem.isWifiActive(ctx));//"1");//
                    p.put("datetime", timeLastUpdate);
                    post.setEntity(new StringEntity(p.toString(), "UTF-8"));//将参数设置入POST请求
                    post.setHeader(HTTP.CONTENT_TYPE, "text/json");//设置为json格式。

                    HttpClient client = new DefaultHttpClient();
                    HttpResponse response = null;
                    response = client.execute(post);

                    if (response.getStatusLine().getStatusCode() == 200) {
                        String content = EntityUtils.toString(response.getEntity());
                        ListBase = gson.fromJson(content, new TypeToken<List<BaseBox>>() {
                        }.getType());
                        Log.d("jutest", "GetBaseBox:" + ListBase.size());//打印到logcat

                        Message msg =null;
                        if(mHandler!=null) {
                            msg = mHandler.obtainMessage(20);
                            msg.obj = "BaseBox下载完毕";
                            mHandler.sendMessage(msg);
                        }

                        (new BaseBox()).CacheBaseBox(ctx, ListBase);//保存到数据库
                        if(mHandler!=null) {
                            msg = mHandler.obtainMessage(21);
                            msg.obj = "BaseBox更新完毕";
                            mHandler.sendMessage(msg);

                            if(ListBase.size()>0) {
//                                msg = mHandler.obtainMessage(22);
//                                msg.obj = ListBase.get(0).ServerTime;
//                                mHandler.sendMessage(msg);
                                if (ListBase.get(0).ServerReturn.equals("1")) {
                                    Thread.sleep(2000);
                                    SetLastUpdateTime(ctx, ListBase.get(0).ServerTime);
                                    Log.d("jutest", "GetBaseBox:时间:" + ListBase.get(0).ServerTime);//打印到logcat
                                }
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public static void GetTaskList(final Context ctx, final Handler mHandler) {
        new Thread() {
            public void run() {
                try {
                    String url = YLSystem.GetBaseUrl(ctx)+"GetTask1";
                    HttpPost post = new HttpPost(url);

                    User user=YLSystem.getUser();
                    //测试数据
                    user.DeviceID = YLSystem.getHandsetIMEI();
                    //user.TaskDate = "2015-03-12";

                    Gson gson = new Gson();
                    //设置POST请求中的参数
                    JSONObject p = new JSONObject();
                    p.put("user", gson.toJson(user));//将User类转换成Json传到服务器。
                    post.setEntity(new StringEntity(p.toString(), "UTF-8"));//将参数设置入POST请求
                    post.setHeader(HTTP.CONTENT_TYPE, "text/json");//设置为json格式。
                    HttpClient client = new DefaultHttpClient();
                    HttpResponse response = client.execute(post);
                    if (response.getStatusLine().getStatusCode() == 200) {
                        String content = EntityUtils.toString(response.getEntity());

                        List<YLTask> lstYLTask = gson.fromJson(content, new TypeToken<List<YLTask>>() {
                        }.getType());
                        String result = lstYLTask.get(0).ServerReturn;
                        if (result.equals("1")) {
                            Message msg = mHandler.obtainMessage(20);
                            msg.obj = lstYLTask;
                            mHandler.sendMessage(msg);
                        } else {
                            Message msg = mHandler.obtainMessage(21);
                            msg.obj = result;
                            mHandler.sendMessage(msg);
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }. start();
    }

    /**
     * 根据HF登陆界面
     * @param loguser HF编号
     * @param url webServer API
     * @return User
     * @throws Exception
     */
    public User LogicByHF(User loguser,String url) throws Exception {
        GetUserFormServerbyHF getUserFormServer = new GetUserFormServerbyHF();
        getUserFormServer.execute(url, loguser.getEmpNO());
        return getUserFormServer.get();
    }

    private class GetUserFormServerbyHF extends AsyncTask<String,Integer,User>{
        @Override
        protected User doInBackground(String... params) {
            String url = params[0];
            HttpPost post = new HttpPost(url);
            User user = new User();
            user.setEmpNO(params[1]);
            user.setDeviceID(YLSystem.getHandsetIMEI());
            user.setISWIFI(YLSystem.getNetWorkState());
            Gson gson = new Gson();
            JSONObject p = new JSONObject();
            HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, 3000);
            HttpConnectionParams.setSoTimeout(httpParams, 3000);
            try {
                p.put("user", gson.toJson(user));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                post.setEntity(new StringEntity(p.toString(),"UTF-8"));
                post.setHeader(HTTP.CONTENT_TYPE,"text/json");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            HttpClient client = new DefaultHttpClient(httpParams);
            try {
                HttpResponse response = client.execute(post);
                Log.e(YLSystem.getKimTag(), response.getStatusLine().getStatusCode()+"");
                if (response.getStatusLine().getStatusCode() == 200){
                    String content = EntityUtils.toString(response.getEntity());
                    User getjsonuser = new User();
                    getjsonuser =  gson.fromJson(content, new TypeToken<User>() {
                    }.getType());
                    return getjsonuser;
                }else {
                    User returnuser = new User();
                    returnuser.setName("illuser");
                    return returnuser;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    /**
     * 根据用户名及密码登陆
     * @param loguser 用户编号,用户密码
     * @param url webServer API
     * @return User
     * @throws Exception
     */

    public User LogicBypassword(User loguser,String url)throws Exception{
        GetUserFormServerbypassword getUserFormServerbypassword = new GetUserFormServerbypassword();
        getUserFormServerbypassword.execute(url,loguser.getEmpNO(),loguser.getPass());
        return getUserFormServerbypassword.get();
    }

    private class GetUserFormServerbypassword extends AsyncTask<String,Integer,User>{
        @Override
        protected User doInBackground(String... params) {
            String url = params[0];
            HttpPost post = new HttpPost(url);
            User user = new User();
            user.setEmpNO(params[1]);
            user.setPass(params[2]);
            user.setDeviceID(YLSystem.getHandsetIMEI());
            user.setISWIFI(YLSystem.getNetWorkState());
            HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, 3000);
            HttpConnectionParams.setSoTimeout(httpParams, 3000);
            Gson gson = new Gson();
            JSONObject p = new JSONObject();
            try {
                p.put("user",gson.toJson(user));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                post.setEntity(new StringEntity(p.toString(),"UTF-8"));
                post.setHeader(HTTP.CONTENT_TYPE,"text/json");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            HttpClient client = new DefaultHttpClient(httpParams);
            try {
                HttpResponse response = client.execute(post);
                if (response.getStatusLine().getStatusCode() == 200){
                    String content = EntityUtils.toString(response.getEntity());
                    User getjsonuser = new User();
                    getjsonuser =  gson.fromJson(content, new TypeToken<User>() {
                    }.getType());
                    return getjsonuser;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    /**
     * 根据用户HF或EmpID及日期获取入库任务列表
     * @param user 业务员EmpHF,业务员编号,任务日期,手持机IMEI,库管员EmpID
     * @param context context
     * @return list<YLStask>
     * @throws Exception
     */

    public List<YLTask> GetHandovermanTask(User user,Context context)throws Exception{
        String url = YLSystem.GetBaseUrl(context)+"StoreInGetTask";
        GetHandovermanTaskAsyncTask getHandovermanTaskAsyncTask = new
                GetHandovermanTaskAsyncTask();
        getHandovermanTaskAsyncTask.execute(url,user.getEmpHFNo(),user.getEmpNO(),user.getTaskDate(),user.getDeviceID(),user.getEmpID());
        return getHandovermanTaskAsyncTask.get();
    }

    private class GetHandovermanTaskAsyncTask extends AsyncTask<String,Integer,List<YLTask>>{
        @Override
        protected List<YLTask> doInBackground(String... params) {
            String url = params[0];
            HttpPost post = new HttpPost(url);
            Gson gson = new Gson();
            JSONObject p = new JSONObject();
            try {
                p.put("HFNo",params[1]);
                p.put("EmpNo",params[2]);
                p.put("DataTime",params[3]);
                p.put("deviceID",params[4]);
                p.put("empid",params[5]);
                post.setEntity(new StringEntity(p.toString(),"UTF-8"));
                post.setHeader(HTTP.CONTENT_TYPE,"text/json");
                HttpClient client = new DefaultHttpClient();
                HttpResponse response = client.execute(post);
                if (response.getStatusLine().getStatusCode() == 200){
                    String content = EntityUtils.toString(response.getEntity());
                    return gson.fromJson(content, new TypeToken<List<YLTask>>() {}.getType());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

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

    private class GetVaultInBoxListAsycnTask extends  AsyncTask<String,Integer,List<Box>>{
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
                Log.e(YLSystem.getKimTag(),response.getStatusLine().getStatusCode()+"");
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
     * 上传金库入库箱列表
     * @param user 用户 empid 库管员ID,deviceID 手持机号
     * @param context context
     * @throws Exception
     */
    public String PostVaultInBoxList(User user,Context context)throws Exception{
        String url = YLSystem.GetBaseUrl(context)+"StoreUploadBox";
        PostVaultInBoxListAsycnTask postVaultInBoxListAsycnTask = new PostVaultInBoxListAsycnTask();
        postVaultInBoxListAsycnTask.execute(url,user.getEmpID(),user.getDeviceID());
        return postVaultInBoxListAsycnTask.get();
    }

    private class PostVaultInBoxListAsycnTask  extends AsyncTask<String,Integer,String>{
        @Override
        protected String doInBackground(String... params) {
            String url = params[0];
            HttpPost post = new HttpPost(url);
            Gson gson = new Gson();
            JSONObject p = new JSONObject();
            try {
                p.put("STask", gson.toJson(YLEditData.getYlTask()));
                p.put("empid", params[1]);
                p.put("deviceID", params[2]);
                post.setEntity(new StringEntity(p.toString(), "UTF-8"));
                post.setHeader(HTTP.CONTENT_TYPE, "text/json");
                HttpClient client = new DefaultHttpClient();
                HttpResponse response = client.execute(post);
                if (response.getStatusLine().getStatusCode() == 200) {
                    return gson.fromJson(EntityUtils.toString(response.getEntity()), String.class);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }


    /**
     * 根据业务员HF或ID获取出库任务列表
     * @param user HFNo 业务员IC卡卡号, EmpNo业务员员工号,DataTime 任务日期,deviceID 手持机IMEI,empid 库管员EmpID,Line 线路编号
     * @param context context
     * @return list<YLTask>
     * @throws Exception
     */
    public List<YLTask> GetVaultOutTask(User user,Context context)throws Exception{
        String url = YLSystem.GetBaseUrl(context)+"StoreOutGetTask";
        GetVaultOutTaskAsycnTask getVaultOutTaskAsycnTask = new GetVaultOutTaskAsycnTask();
        getVaultOutTaskAsycnTask.execute(url,user.getEmpHFNo(),user.getEmpNO(),user.getTaskDate(),
                YLSystem.getHandsetIMEI(),user.getEmpID(),user.getName());
        Log.e(YLSystem.getKimTag(), user.toString());
        return getVaultOutTaskAsycnTask.get();
    }

    private class GetVaultOutTaskAsycnTask extends AsyncTask<String,Integer,List<YLTask>>{
        @Override
        protected List<YLTask> doInBackground(String... params) {
            String url = params[0];
            HttpPost post = new HttpPost(url);
            Gson gson = new Gson();
            JSONObject p = new JSONObject();
            try {
                p.put("HFNo",params[1]);
                p.put("EmpNo",params[2]);
                p.put("DataTime",params[3]);
                p.put("deviceID",params[4]);
                p.put("empid",params[5]);
                p.put("Line",params[6]);
                post.setEntity(new StringEntity(p.toString(),"UTF-8"));
                post.setHeader(HTTP.CONTENT_TYPE,"text/json");
                HttpClient client = new DefaultHttpClient();
                HttpResponse response = client.execute(post);
                if (response.getStatusLine().getStatusCode() == 200){
                    String content = EntityUtils.toString(response.getEntity());
                    return gson.fromJson(content, new TypeToken<List<YLTask>>() {}.getType());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    /**
     * 获取金库所有款箱类
     * <param name="BaseName">基地名称：南海基地</param>
     * <param name="deviceID">手持机号：</param>
     * <param name="empid">金库库管员ID：3361</param>
     * @param user user
     * @param context context
     * @return list<Box>listbox</Box>
     * @throws Exception
     */
    public List<Box> GetAllBox(User user,Context context) throws Exception{
        String url = YLSystem.GetBaseUrl(context)+"StoreGetNowInBoxlList";
        GetAllBoxAsycnTask getAllBoxAsycnTask = new GetAllBoxAsycnTask();
        getAllBoxAsycnTask.execute(url,user.getTaskDate(),YLSystem.getHandsetIMEI(),user.getEmpID());
        return getAllBoxAsycnTask.get();
    }

    private class GetAllBoxAsycnTask extends AsyncTask<String,Integer,List<Box>>{
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
     * 业务员确认入库
     * @param user TaskID user.getTaskDate(),empid user.getEmpID(),deviceID user.getDeviceID(),ISWIFI user.getISWIFI()
     * @param context
     * @return  "1";为正确，其他为出错。
     * @throws Exception
     */

    public String ComfirmStoreIn(User user ,Context context)throws Exception{
        String url = YLSystem.GetBaseUrl(context)+"ComfirmStoreIn";
        ConfirmStoreInAsycnTask confirmStoreInAsycnTask = new ConfirmStoreInAsycnTask();
        confirmStoreInAsycnTask.execute(url,user.getTaskDate(),user.getEmpID(),YLSystem.getHandsetIMEI(),user.getISWIFI());
        return confirmStoreInAsycnTask.get();
    }

    private class  ConfirmStoreInAsycnTask extends AsyncTask<String,Integer,String>{
        @Override
        protected String doInBackground(String... params) {
            String url = params[0];
            HttpPost post = new HttpPost(url);
            Gson gson = new Gson();
            JSONObject p = new JSONObject();
            try {
                p.put("TaskID",params[1]);
                p.put("empid",params[2]);
                p.put("deviceID",params[3]);
                p.put("ISWIFI",params[4]);
                post.setEntity(new StringEntity(p.toString(),"UTF-8"));
                post.setHeader(HTTP.CONTENT_TYPE,"text/json");
                HttpClient client = new DefaultHttpClient();
                HttpResponse response = client.execute(post);
                if (response.getStatusLine().getStatusCode() == 200){
                    String content = EntityUtils.toString(response.getEntity());
                    return gson.fromJson(content,String.class);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    /// <summary>
    /// 获取本基地业务员已确认未入库任务
    /// </summary>
    /// <param name="DataTime">日期：2015-06-30</param>
    /// <param name="deviceID">手持机硬件号:861189013980320 </param>
    /// <param name="empid">库管员id：3280</param>
    /// <returns>
    ///YLTask.ServerReturn "1";为正确，其他为出错
    public List<YLTask> StoreInGetBaseAllTask (User user,Context context) throws  Exception{
        String url = YLSystem.GetBaseUrl(context)+"StoreInGetBaseAllTask";
        StoreInGetBaseAllTaskAsycnTask storeInGetBaseAllTaskAsycnTask =
                new StoreInGetBaseAllTaskAsycnTask();
        storeInGetBaseAllTaskAsycnTask.execute(url,user.getTaskDate(),YLSystem.getHandsetIMEI(),user.getEmpID());
        return storeInGetBaseAllTaskAsycnTask.get();
    }

    private class StoreInGetBaseAllTaskAsycnTask extends AsyncTask<String,Integer,List<YLTask>>{
        @Override
        protected List<YLTask> doInBackground(String... params) {
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
                    return gson.fromJson(content, new TypeToken<List<YLTask>>() {}.getType());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public String GetServerTime(Context context,String Reason)throws Exception{
        String url = YLSystem.GetBaseUrl(context)+"GetSeverTimeOnReason";
        GetServerTimeAsy getServerTimeAsy = new  GetServerTimeAsy();
        getServerTimeAsy.execute(url,Reason);
        return  getServerTimeAsy.get();
    }

    private class GetServerTimeAsy extends AsyncTask<String,Integer,String>{
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
                p.put("deviceID",YLSystem.getHandsetIMEI());
                p.put("ISWIFI",YLSystem.getNetWorkState());
                p.put("empid",YLSystem.getUser().getEmpID());
                p.put("Reason", strings[1]);
//                Log.e(YLSystem.getKimTag(),p.toString()+strings[0]);
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

    //region  2018.12.21 全局的
    public static void GetFp(final Context ctx, final Handler mHandler) {
        new Thread() {
            public void run() {
                try {
                    String url = YLSystem.GetBaseUrl(ctx)+"GetEmpFPPhone";
                    HttpPost post = new HttpPost(url);

                    User user=YLSystem.getUser();
                    Gson gson = new Gson();
                    Map map=new HashMap();
                    map.put("empid", user.getEmpID());
                    map.put("deviceID", user.getDeviceID());//YLSystem.getHandsetIMEI();
                    map.put("ISWIFI", user.getISWIFI());
                    String poststring=  gson.toJson(map);

                    //设置POST请求中的参数
//                    JSONObject p = new JSONObject();
//                    p.put("user", gson.toJson(user));//将User类转换成Json传到服务器。

                    post.setEntity(new StringEntity(poststring, "UTF-8"));//将参数设置入POST请求
                    post.setHeader(HTTP.CONTENT_TYPE, "text/json");//设置为json格式。
                    HttpClient client = new DefaultHttpClient();
                    HttpResponse response = client.execute(post);
                    if (response.getStatusLine().getStatusCode() == 200) {
                        String content = EntityUtils.toString(response.getEntity());

                        List<YLTask> lstYLTask = gson.fromJson(content, new TypeToken<List<YLTask>>() {
                        }.getType());
                        String result = lstYLTask.get(0).ServerReturn;
                        if (result.equals("1")) {
                            Message msg = mHandler.obtainMessage(20);//成功
                            msg.obj = lstYLTask;
                            mHandler.sendMessage(msg);
                        } else {
                            Message msg = mHandler.obtainMessage(21);//失败
                            msg.obj = result;
                            mHandler.sendMessage(msg);
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }. start();
    }



    //endregion
}

