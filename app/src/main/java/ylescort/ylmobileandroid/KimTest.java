package ylescort.ylmobileandroid;

import android.app.AlertDialog;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpResponse;
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

import TaskClass.User;
import YLSystemDate.YLSystem;


public class KimTest extends ActionBarActivity implements View.OnClickListener {

    private Button kim_test1;
    private Button kim_test2;
    private AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kim_test);
        //NolableDialog();
        kim_test1 = (Button) findViewById(R.id.kim_test1);
        kim_test2 = (Button) findViewById(R.id.kim_test2);
        kim_test1.setOnClickListener(this);
        kim_test2.setOnClickListener(this);
    }


    private void NolableDialog() {

//        final EditText et = new EditText(this);
//        et.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
//        et.requestFocus();
//        InputMethodManager inputManager = (InputMethodManager) et.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//        //imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
//        inputManager.showSoftInput(et,0);
//        new AlertDialog.Builder(this).setTitle("请输入").setIcon
//                (android.R.drawable.ic_dialog_info).setView(et).setPositiveButton("确定", null)
//                .setNegativeButton("取消", null).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_kim_test, menu);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.kim_test1:NolableDialog();
                break;
        }
    }

    private void GetDataFromServer() {

        kim_test2.setText("11");
        kim_test2.setEnabled(false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    GetUserFormServer getUserFormServer = new GetUserFormServer();
                    getUserFormServer.execute("");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    public class GetUserFormServer extends AsyncTask<String,Integer, User>{

        @Override
        protected User doInBackground(String... params) {
            String url = YLSystem.GetBaseUrl(getApplicationContext())+"Login1";
            HttpPost post = new HttpPost(url);
            User user = new User();
            user.setEmpNO("710161");
            user.setPass(YLSystem.SetMD5("710161"));
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
            HttpClient client = new DefaultHttpClient();
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

        @Override
        protected void onPostExecute(User user) {

            if (user.getServerReturn().equals("1")){
                kim_test2.setText("22");
                kim_test2.setEnabled(true);
            }else {
                kim_test2.setText("33");
                kim_test2.setEnabled(true);
            }
            super.onPostExecute(user);
        }
    }
}
