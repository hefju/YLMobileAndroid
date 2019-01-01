package YLWebService;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import TaskClass.User;
import YLSystemDate.YLSystem;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Created by asus on 2015/1/29.
 * 用于访问远程服务
 * 2018.12.22重新设计, 网络请求应该使用同步线程,可通过单元测试的, 异步多线程状态等麻烦的东西还是留给UI去处理.
 * 全局网络请求
 */
public class YLWebService {
//  private static Map map = new HashMap();
    private  String TAG="YLWebService";

    public YLWebService()
    {
    }

    //根据员工ID,返回指纹列表
    public List<String> GetEmpFingerPrints(Context context,String empId,String deviceID, String ISWIFI){
        List<String> list=new ArrayList<>();
        String url= YLSystem.GetBaseUrl(context)+"GetEmpFPPhone";  //GetEmpFPPhone //没有分手指类型 GetEmpFPPhoneMore //分手指类型
        Map map=new HashMap();//EmpID empid
        map.put("empid", empId);
        map.put("deviceID", deviceID);
        map.put("ISWIFI",ISWIFI);
        String webresult=BaseWebRequest(url,map);
        if(webresult!=null&&!webresult.equals("")) {
            webresult= webresult.replace("\"","");
            String[] result = webresult.split(",");
            for (int i = 0; i < result.length; i++) {
                list.add(result[i]);
            }
        }
        return list;
    }

    //根据员工ID,返回指纹列表
    public List<String> UploadEmpFPPhone(Context context,String empId,String type,String deviceID, String ISWIFI,String FP){
        List<String> list=new ArrayList<>();
        String url= YLSystem.GetBaseUrl(context)+"GetEmpFPPhone";  //GetEmpFPPhone //没有分手指类型 GetEmpFPPhoneMore //分手指类型
        Map map=new HashMap();//EmpID empid
        map.put("empid", empId);
        map.put("type", type);
        map.put("deviceID", deviceID);
        map.put("FP", FP);
        map.put("ISWIFI",ISWIFI);
        String webresult=BaseWebRequest(url,map);
        if(webresult!=null&&!webresult.equals("")) {
            webresult= webresult.replace("\"","");
            String[] result = webresult.split(",");
            for (int i = 0; i < result.length; i++) {
                list.add(result[i]);
            }
        }
        return list;
    }

    //基础的网络请求, 输入请求地址url和请求参数map发起网络请求, 直接返回请求字符串结果,不作任何处理.
    private String BaseWebRequest(  String url,Map map ) {
        String result="";//返回结果,如果是空表示出现错误了.
        Gson gson = new Gson();
        String postBody=gson.toJson(map);

        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), postBody);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .build();

        try {
            Response response = client.newCall(request).execute();
            if(response.isSuccessful()) {
                result = response.body().string();
            }
        } catch (IOException e) {
            Log.e(TAG,e.getMessage());
            e.printStackTrace();
        }
        return result;
    }
















    //获取网点列表 public List<Site> GetSiteList(String taskID)

    //获取任务列表  public List<YLTask>  DownloadTaskList()

    //用户登录 public boolean Login(String EmpNO,String UserName,String pwd,String DeviceID, String ISWIFI)


}
