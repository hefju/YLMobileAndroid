package ylescort.ylmobileandroid;

import android.app.Application;
import android.os.AsyncTask;
import android.test.ApplicationTestCase;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import TaskClass.Box;
import TaskClass.Site;
import TaskClass.TasksManager;
import TaskClass.User;
import TaskClass.YLTask;
import YLDataService.AnalysisBoxList;
import YLDataService.BaseBoxDBSer;
import YLDataService.WebServerBaseData;
import YLDataService.WebServerValutturnover;
import YLDataService.WebServerYLSite;
import YLDataService.WebService;
import YLSystemDate.YLEditData;
import YLSystemDate.YLSystem;
import YLWebService.YLWebService;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Created by Administrator on 2015/4/29.
 */
public class WebServerTest extends ApplicationTestCase<Application> {
    public WebServerTest() { super(Application.class); }

    private static final String TAG = "kim";

    public void testValutoutID()throws Exception{
        User user = new User();
        user.setEmpHFNo("002DE3A6");
        user.setEmpNO("520037");
        user.setTaskDate("2015-05-04");
        user.setDeviceID("123");
        user.setEmpID("3166");
        YLSystem.setUser(user);
        YLSystem.setHandsetIMEI("123-123-123");
        WebServerYLSite webServerYLSite = new WebServerYLSite();
        String id=  webServerYLSite.GetCarBoxOutID2(getContext(),"298248");
        Log.e(TAG,id);

    }

    public void testValutinTask()throws Exception{
        WebService webService = new WebService();
        String url = YLSystem.GetBaseUrl(getContext())+"StoreInGetTask";
        User user = new User();
        user.setEmpHFNo("002DE3A6");
        user.setEmpNO("520037");
        user.setTaskDate("2015-05-04");
        user.setDeviceID("123");
        user.setEmpID("3166");
        List<YLTask> ylTaskList = webService.GetHandovermanTask(user, getContext());
        Log.e(TAG,ylTaskList.toString());
    }

    public void testValutinBox()throws Exception{
        WebService webService = new WebService();
        List<Box> boxList = webService.GetVaultInBoxList("91610", "1", "3166", getContext());
        Log.e(TAG,boxList.toString());
    }

    public void testValultinBox()throws Exception{
        WebService webService = new WebService();
        User user = new User();
        List<YLTask> ylTaskList= webService.GetHandovermanTask(user, getContext());
        Log.e(TAG,ylTaskList.toString());
    }

    public void testValultoutTask()throws Exception{
        WebService webService = new WebService();
        User user = new User();
        user.setEmpHFNo("");
        user.setEmpNO("");
        user.setTaskDate("2015-05-04");
        user.setDeviceID("231");
        user.setEmpID("3361");
        user.setName("033");//借用user类传入线路编号
        List<YLTask> ylTaskList = webService.GetVaultOutTask(user, getContext());
        Log.e(TAG,ylTaskList.toString());
    }

    public void testUploadvaultinbox()throws Exception{
        YLTask ylTask = new YLTask();
        YLEditData.setYlTask(ylTask);
        WebService webService = new WebService();
        User user = new User();
        user.setEmpID("3361");
        user.setDeviceID("123");
        String get = webService.PostVaultInBoxList(user, getContext());
        Log.e(TAG,get+"");
    }

    public void testAllBox()throws Exception{
        WebService webService = new WebService();
        User user = new User();
        user.setISWIFI("南海基地");
        user.setDeviceID("1");
        user.setEmpID("3361");
        List<Box> list = webService.GetAllBox(user, getContext());

        AnalysisBoxList analysisBoxList = new AnalysisBoxList();
//        List<Integer> integerList =
//        analysisBoxList.AnsysisBoxList(list);
        Log.e(TAG,list.toString()+"");
    }

    public void testGetTaskList() throws Exception{
        WebService webService = new WebService();
        User user = new User();
        user.setTaskDate("2015-05-29");
        user.setEmpID("3727");
        user.setDeviceID("123");
        user.setName("001");
        List<YLTask> ylTaskList = webService.GetVaultOutTask(user,getContext());
        Log.e(TAG,ylTaskList.toString());
    }

    public void testComfirmvalutin()throws Exception{
        WebService webService = new WebService();
        User user = new User();
        user.setTaskDate("123");
        user.setEmpID("123");
        user.setDeviceID("123");
        user.setISWIFI("123");
        String Serreturn =  webService.ComfirmStoreIn(user, getContext());
        Log.e(TAG,Serreturn+"");
    }

    public void testVaultComfirmTask()throws Exception{
        WebService webService = new WebService();
        User user = new User();
        user.setTaskDate("2015-06-16");
        user.setEmpID("3361");
        user.setDeviceID("123");
        user.setISWIFI("1");
        List<YLTask> ylTaskList= webService.StoreInGetBaseAllTask(user, getContext());
        Log.e(TAG, ylTaskList.toString());
    }

    public void testMaxDataUpload()throws Exception{
        User user = new User();
        user.setEmpID("3915");
        YLSystem.setUser(user);

        Postasytask postasytask = new Postasytask();
        postasytask.doInBackground();

    }

    private class Postasytask extends AsyncTask<String,Integer,String>{
        @Override
        protected String doInBackground(String... params) {

                TasksManager tasksManager = new TasksManager();
                tasksManager.Loading(getContext(), "2015-06-29");
                YLTask ylTask = tasksManager.lstLatestTask.get(0);
                String url = "http://192.168.200.137:8052/YLMobileServiceAndroid.svc/UpLoad";
//            try {
//                int tmout = 5;
//                HttpParams httpParams = new BasicHttpParams();
//                httpParams.setParameter("charset", "UTF-8");
//                HttpConnectionParams.setConnectionTimeout(httpParams,tmout * 1000);  //毫秒
//                HttpConnectionParams.setSoTimeout(httpParams, tmout * 1000);
//                HttpClient httpClient = new DefaultHttpClient(httpParams);
//                Gson gson = new Gson();
//                JSONObject p = new JSONObject();
//                YLTask t1 = ylTask;
//                t1.lstSite=ylTask.lstSite;
//                t1.lstBox=ylTask.lstBox;
//                p.put("STask",gson.toJson(t1));//整个任务=====================自定义。。。。。
//                p.put("empid", YLSystem.getUser().EmpID);//人员id=====================自定义。。。。。
//                p.put("deviceID", YLSystem.getUser().DeviceID);//手持机号=====================自定义。。。。。
//                p.put("ISWIFI", YLSystem.getUser().ISWIFI);//是否用WIFI=====================自定义。。。。。
//                HttpPost httpPost = new HttpPost(url);
//                httpPost.setEntity(new StringEntity(p.toString(), "UTF-8"));
//                HttpResponse response;
//                response = httpClient.execute(httpPost);
////检验状态码，如果成功接收数据
//                int code = response.getStatusLine().getStatusCode();
//                Log.e(YLSystem.getKimTag(),code+"");
//            }catch (Exception e){
//                e.printStackTrace();
//            }

            HttpPost post = new HttpPost(url);
            Gson gson = new Gson();
            JSONObject p = new JSONObject();
            try {
                YLTask t1 = ylTask;
                t1.lstSite=ylTask.lstSite;
                t1.lstBox=ylTask.lstBox;
                p.put("STask",gson.toJson(t1));//整个任务=====================自定义。。。。。
                p.put("empid", YLSystem.getUser().EmpID);//人员id=====================自定义。。。。。
                p.put("deviceID", YLSystem.getUser().DeviceID);//手持机号=====================自定义。。。。。
                p.put("ISWIFI", YLSystem.getUser().ISWIFI);//是否用WIFI=====================自定义。。。。。
                post.setEntity(new StringEntity(p.toString(),"UTF-8"));
                post.setHeader(HTTP.CONTENT_TYPE,"text/json");
                HttpClient client = new DefaultHttpClient();
                HttpResponse response = client.execute(post);
                Log.e(YLSystem.getKimTag(),"箱数："+t1.getLstBox().size()+"网点数："+t1.getLstSite().size()+
                       " 连接返回："+ response.getStatusLine().getStatusCode());
                if (response.getStatusLine().getStatusCode() == 200){
                    String content = EntityUtils.toString(response.getEntity());
                    Log.e(YLSystem.getKimTag(),t1.lstBox.size()+"boxlist");
                    return gson.fromJson(content,new TypeToken<String>(){}.getType()) ;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }


    public void testTurnOverbaseboxlist()throws Exception{
        User user = new User();
        user.setTaskDate("南海基地");
        user.setDeviceID("11");
        user.setEmpID("3647");

        WebServerValutturnover webServerValutturnover = new WebServerValutturnover();
        List<Box> boxList = webServerValutturnover.ValutOutBoxList(user, getContext());
        Log.e(TAG,boxList.size()+"");
    }

    public void testTurnOverupload()throws Exception{
        User user = new User();
        user.setTaskDate("南海基地");
        user.setDeviceID("11");
        user.setEmpID("3647");
        YLSystem.setUser(user);
        TasksManager tasksManager = new TasksManager();
        tasksManager.Loading(getContext(), "2015-06-18");
        YLTask ylTask = tasksManager.lstLatestTask.get(0);
        YLEditData.setYlTask(ylTask);
        WebServerValutturnover webServerValutturnover = new WebServerValutturnover();
        String returnstring = webServerValutturnover.Valutturnoverupload(user, getContext());
        Log.e(TAG,returnstring+"");
    }

    public void testTurnOverIn()throws Exception{
        User user = new User();
        user.setTaskDate("2015.06.25");
        user.setDeviceID("11");
        user.setEmpID("2708");
        user.setServerReturn("乐从基地");
        WebServerValutturnover webServerValutturnover = new WebServerValutturnover();
        List<Box> boxList =  webServerValutturnover.ValutInBoxList(user, getContext());
        String getstring = boxList.get(0).getSiteID();
        if (getstring == null){
            Log.e(TAG,"123");
        }else {
            Log.e(TAG,getstring+"");
        }
    }

    public void testVaultturnoverout()throws Exception{
        WebServerValutturnover webServerValutturnover = new WebServerValutturnover();
        List<Box> boxList = webServerValutturnover.VaultTrunoverOutBoxList
                ("南海基地", "乐从基地", "123", "3361", "2015-06-21", getContext());
        Log.e(TAG,"款箱数量："+boxList.toString());
    }

    public void testGetTaskSite()throws Exception{
        User user = new User();
        user.setTaskDate("108024");
        user.setDeviceID("11");
        user.setEmpID("3166");
        user.setISWIFI("1");
        WebServerYLSite webServerYLSite = new WebServerYLSite();
        List<Site> sites =  webServerYLSite.GetYLTaskSite(user, getContext());
        Log.e(TAG,"任务网点："+sites.toString());
    }

    public void testCache()throws Exception{
        YLSystem.setSerAdress("0");
        WebServerBaseData webServerBaseData = new WebServerBaseData();
//        webServerBaseData.GetBaseData(getContext());
    }

    public void testCheckSuccessUpLoad()throws Exception{
        WebServerYLSite w = new WebServerYLSite();
        String s = w.UploadState(getContext(),"272044");
        Log.e(TAG,"返回："+s);
    }

    //region 测试与服务器的通讯 2018.12.22
    private String juTAG="unit_test";

    //获取单个员工的指纹
    public void testGetFingerPrint() throws IOException {
        YLWebService ylWebService = new YLWebService();
        String empid = "3638";
        List<String> list = ylWebService.GetEmpFingerPrints(getContext(), empid, "unitest", "1");
        for (int i = 0; i < list.size(); i++) {
            Log.e(juTAG, list.get(i));
        }
        if (list.size() == 0) {
            Log.e(juTAG, "no finger print by emp:" + empid);
        }
    }


    //endregion
}
