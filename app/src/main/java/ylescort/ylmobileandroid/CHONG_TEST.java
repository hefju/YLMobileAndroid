package ylescort.ylmobileandroid;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

import TaskClass.BaseEmp;
import TaskClass.User;
import YLSystem.YLSystem;


public class CHONG_TEST extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chong__test);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chong__test, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();

    Handler mh = new Handler() {   //以Handler为桥梁将结果传入UI
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            //Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }
    };
    public void chongtest_emp(View view) throws ClassNotFoundException {

        singleThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    String url = "http://58.252.75.149:8055/YLMobileServiceAndroid.svc/GetBaseEmp";//网址
                    HttpPost post = new HttpPost(url);
                    //添加数值到User类
                    Gson gson = new Gson();
                    post.setHeader(HTTP.CONTENT_TYPE, "text/json");//设置为json格式。
                    HttpClient client = new DefaultHttpClient();
                    HttpResponse response = client.execute(post);
                    if (response.getStatusLine().getStatusCode() == 200) {
                        String content = EntityUtils.toString(response.getEntity());    //得到返回字符串


                        List<BaseEmp> ListBaseEmp=new ArrayList<BaseEmp>();
                        ListBaseEmp = gson.fromJson(content, new TypeToken<List<BaseEmp>>() {
                        }.getType());
                        List listViewdata= new ArrayList<>();
                        listViewdata.clear();
                        for (BaseEmp baseEmp : ListBaseEmp) {

                                listViewdata.add(baseEmp.EmpName);

                        }
                        Log.d("WCF",  "ok");//打印到logcat

                            mh.sendEmptyMessage(0);


                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();

                }
            }
        });

    }
}
