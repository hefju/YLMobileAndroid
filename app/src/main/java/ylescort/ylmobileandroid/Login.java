package ylescort.ylmobileandroid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

import YLSystem.YLSystem;
import TaskClass.User;


public class Login extends ActionBarActivity {

    private EditText Log_Name;
    private EditText Log_PassWord;
    private Button Log_Ent;
    private String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log_Name = (EditText) findViewById(R.id.Log_ET_Name);
        Log_PassWord = (EditText) findViewById(R.id.Log_ET_PassWord);

        Button    btnju1=(Button)findViewById(R.id.btnTest1);
        Button btnju2=(Button)findViewById(R.id.btnTest2);
        btnju1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JuTestClass mytest=new JuTestClass();
                mytest.goahead1(getApplicationContext());
                //Toast.makeText(getApplicationContext(), "启动测试1", Toast.LENGTH_SHORT).show();

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
//    private    Button btnju1;
//    private Button btnju2;

    List<String> listViewdata = new ArrayList<String>();
    ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();

    Handler mh = new Handler() {   //以Handler为桥梁将结果传入UI
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }
    };

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
                    user.setPass(Log_PassWord.getText().toString());
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
                        Log.d("WCF", getjsonuser.getServerReturn()+"");//打印到logcat
                        if (getjsonuser.getServerReturn().equals("1")){
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
            intent.setClass(getApplicationContext(), SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
