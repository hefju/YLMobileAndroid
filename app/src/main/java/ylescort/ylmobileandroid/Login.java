package ylescort.ylmobileandroid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.hdhe.nfc.NFCcmdManager;
import com.example.nfc.util.Tools;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import TaskClass.TasksManager;
import TaskClass.Vision;
import TaskClass.YLTask;
import YLDataService.EmpDBSer;
import YLDataService.TasksManagerDBSer;
import YLSystem.YLSystem;
import TaskClass.User;


public class Login extends ActionBarActivity {

    private EditText Log_Name;
    private EditText Log_PassWord;
    private Button Log_BN_HF;
    private Button Log_BN_Cal;
    private String message;
    private NFCcmdManager manager ;
    private byte[] uid ;
    private MediaPlayer mPlayer;  //媒体播放者，用于播放提示音

    private TasksManager tasksManager = null;//任务管理类

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log_Name = (EditText) findViewById(R.id.Log_ET_Name);
        Log_PassWord = (EditText) findViewById(R.id.Log_ET_PassWord);

        try{
            manager = NFCcmdManager.getNFCcmdManager(13, 115200, 0);
            manager.readerPowerOn();
            InitHF();
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"HF初始化失败",Toast.LENGTH_SHORT).show();
        }

        //Log_Name.setInputType(EditorInfo.TYPE_CLASS_NUMBER);

        Button    btnju1=(Button)findViewById(R.id.btnTest1);
        Button btnju2=(Button)findViewById(R.id.btnTest2);
        btnju1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                JuTestClass mytest=new JuTestClass();
//                mytest.goahead1(getApplicationContext());
                //Toast.makeText(getApplicationContext(), "启动测试1", Toast.LENGTH_SHORT).show();
/**
临时清除数据
 **/
                tasksManager = new TasksManager();
                tasksManager.TaskDate = "2015-03-13";
                TasksManagerDBSer tasksManagerDBSer = new TasksManagerDBSer(getApplicationContext());
                tasksManagerDBSer.DeleteTasksManager(tasksManager);
                YLSystem.setTasksManager(tasksManager);
            }
        });
        btnju2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JuTestClass mytest=new JuTestClass();
                mytest.goahead2(getApplicationContext());
               // Toast.makeText(getApplicationContext(), "启动测试2", Toast.LENGTH_SHORT).show();
            }
        });

        //action_settings
    }

    private void InitHF() {
        Log_BN_HF = (Button)findViewById(R.id.Log_BN_HF);
        Log_BN_Cal = (Button)findViewById(R.id.Log_BN_Cal);
        Log_BN_Cal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Log_BN_HF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.init_14443A();
                uid = manager.inventory_14443A();
                if(uid != null){
                    //editUid.setText(Tools.Bytes2HexString(uid, uid.length));
                    singleThreadExecutor.execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                String url = "http://58.252.75.149:8055/YLMobileServiceAndroid.svc/LoginByHF";//网址
                                HttpPost post = new HttpPost(url);
                                //添加数值到User类

                                User user = new User();
                                user.setEmpNO(Tools.Bytes2HexString(uid, uid.length));
                                //user.setPass(YLSystem.md5(Log_PassWord.getText().toString()));
                                Gson gson = new Gson();
                                //设置POST请求中的参数
                                JSONObject p = new JSONObject();
                                p.put("user", gson.toJson(user));//将User类转换成Json传到服务器。
                                post.setEntity(new StringEntity(p.toString(), "UTF-8"));//将参数设置入POST请求
                                post.setHeader(HTTP.CONTENT_TYPE, "text/json");//设置为json格式。
                                HttpClient client = new DefaultHttpClient();
                                HttpResponse response = client.execute(post);
                                if (response.getStatusLine().getStatusCode() == 200) {
                                    String content = EntityUtils.toString(response.getEntity());    //得到返回字符串
                                    User getjsonuser = gson.fromJson(content, new TypeToken<User>() {
                                    }.getType());
                                    Log.d("jutest", content);//打印到logcat
                                    if (getjsonuser.getServerReturn().equals("1")){

                                        getjsonuser.setISWIFI("1");
                                        YLSystem.setUser(getjsonuser);

                                        Intent intent = new Intent();
                                        intent.setClass(Login.this, YLBoxOperate.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putString("AName","Kim");
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                        message= "登录成功";
                                        YLBoxMediaPlay("success");
                                        mh.sendEmptyMessage(0);
                                    }
                                    else {
                                        message= "登录失败";
                                        YLBoxMediaPlay("faile");
                                        mh.sendEmptyMessage(0);
                                    }

                                }
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            } catch (ClientProtocolException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    });
                }else{
                    Toast.makeText(getApplicationContext(), "未寻到卡", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
//    private    Button btnju1;
//    private Button btnju2;

    List<String> listViewdata = new ArrayList<String>();
    ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();

    Handler mh = new Handler() {   //以Handler为桥梁将结果传入UI
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        }
    };
    public void chongtest(View view){
        Intent intent = new Intent();
        intent.setClass(Login.this, chongTest3.class);
     /*   Bundle bundle = new Bundle();
        bundle.putString("AName","Kim");
        intent.putExtras(bundle);*/
        startActivity(intent);
    }
    public void LoginEnter(View view) throws ClassNotFoundException {
        /*
        Intent intent = new Intent();
        intent.setClass(Login.this, Task.class);
        Bundle bundle = new Bundle();
        bundle.putString("AName","Kim");
        intent.putExtras(bundle);
        startActivity(intent);
        */

        singleThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    String url = "http://58.252.75.149:8055/YLMobileServiceAndroid.svc/Login1";//网址
                    HttpPost post = new HttpPost(url);
                    //添加数值到User类

                    User user = new User();
                    user.setEmpNO(Log_Name.getText().toString());
                    user.setPass(YLSystem.md5(Log_PassWord.getText().toString()));
                    Gson gson = new Gson();
                    //设置POST请求中的参数
                    JSONObject p = new JSONObject();
                    p.put("user", gson.toJson(user));//将User类转换成Json传到服务器。
                    post.setEntity(new StringEntity(p.toString(), "UTF-8"));//将参数设置入POST请求
                    post.setHeader(HTTP.CONTENT_TYPE, "text/json");//设置为json格式。
                    HttpClient client = new DefaultHttpClient();
                    HttpResponse response = client.execute(post);
                    if (response.getStatusLine().getStatusCode() == 200) {
                        String content = EntityUtils.toString(response.getEntity());    //得到返回字符串
                        User getjsonuser = gson.fromJson(content, new TypeToken<User>() {
                        }.getType());
                        Log.d("jutest", content);//打印到logcat
                        if (getjsonuser.getServerReturn().equals("1")){

                            getjsonuser.setISWIFI("1");
                            YLSystem.setUser(getjsonuser);
                            //GetVisionFromSer();
                            /*
                            此处添加更新基础数据
                             */
                            Intent intent = new Intent();
                            intent.setClass(Login.this, Task.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("AName","Kim");
                            intent.putExtras(bundle);
                            startActivity(intent);
                            message= "登录成功";
                            mh.sendEmptyMessage(0);
                        }
                        else {
                            message= "登录失败";
                            mh.sendEmptyMessage(0);
                        }

                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });

    }

    private void GetVisionFromSer() {
        singleThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    //添加数值到User类
                    User s1 = new User();
                    s1 = YLSystem.getUser();
                    String url = "http://58.252.75.149:8055/YLMobileServiceAndroid.svc/GetVision";//网址
                    HttpPost post = new HttpPost(url);
                    //添加数值到User类
                    Gson gson = new Gson();
                    //设置POST请求中的参数-------返回EmpID员工ID，ServerReturn 服务器错误，1为没错误，Time 服务器时间。
                    JSONObject p = new JSONObject();

                    p.put("DeviceID", s1.DeviceID);//手持机号=====================自定义。。。。。
                    p.put("ISWIFI", s1.ISWIFI);//是否用WIFI=====================自定义。。。。。

                    post.setEntity(new StringEntity(p.toString(), "UTF-8"));//将参数设置入POST请求
                    post.setHeader(HTTP.CONTENT_TYPE, "text/json");//设置为json格式。
                    HttpClient client = new DefaultHttpClient();
                    HttpResponse response = client.execute(post);
                    if (response.getStatusLine().getStatusCode() == 200) {
                        String content = EntityUtils.toString(response.getEntity());    //得到返回字符串
                        Vision vision = gson.fromJson(content, new TypeToken<Vision>() {
                        }.getType());
                        Log.d("WCF", "ok");//打印到logcat
                        String locatvision = null;
                        try {
                            locatvision = getVersionName();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (vision.getVision()!= null){
                        if (!vision.getVision().equals(locatvision)){



                        }
                        }

                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private String getVersionName() throws Exception
    {
        // 获取packagemanager的实例
        PackageManager packageManager = getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(),0);
        String version = packInfo.versionName;
        return version;
    }

    private void YLBoxMediaPlay(String mediavoice) {
        mPlayer = new MediaPlayer();

        if (mediavoice.equals("success")){
            mPlayer = MediaPlayer.create(Login.this, R.raw.msg);
            if(mPlayer.isPlaying()){
                return;
            }
        }else {
            try {
                mPlayer.setDataSource("/system/media/audio/notifications/Proxima.ogg");  //选用系统声音文件
                mPlayer.prepare();
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (SecurityException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalStateException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        //mPlayer.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent();
            intent.setClass(getApplicationContext(), setup.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
