package ylescort.ylmobileandroid;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.example.nfc.util.Tools;
import com.google.gson.Gson;
import com.handheld.HF.HfError;
import com.handheld.HF.HfManager;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import YLSystemDate.HFReader;
import YLSystemDate.YLSystem;

/**
 * Created by Administrator on 2016-01-27.
 */
public abstract class YLBaseActivity extends ActionBarActivity {

    public static ProgressDialog YLProgressDialog;
    public Gson gson;

    private HFReader hfReader;
    private KeyReceiver keyReceiver;

/*    private static class YLHandler extends Handler{
        WeakReference<ActionBarActivity> actionBarActivityWeakReference;
        YLHandler(ActionBarActivity actionBarActivity){
            actionBarActivityWeakReference =
                    new WeakReference<ActionBarActivity>(actionBarActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:YLProgressDialog.dismiss();
                    break;
                case 1:YLProgressDialog.dismiss();
                    break;
                case 2:
                    break;
            }
            super.handleMessage(msg);
        }
    }

    YLHandler ylHandler = new YLHandler(this);*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gson = new Gson();
        hfReader = new HFReader();
    }

    private void RegisterReceiver() {
        keyReceiver = new KeyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.rfid.FUN_KEY");
        registerReceiver(keyReceiver, filter);
    }

    protected abstract void InitLayout();

    protected abstract void InitData() throws Exception;

    public  void HandSetHotKey (int keyCode){}


    public   class KeyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            int keyCode = intent.getIntExtra("keyCode", 0);
            if (keyCode == 0) {
                keyCode = intent.getIntExtra("keycode", 0);
            }
            boolean keyDown = intent.getBooleanExtra("keydown", false);
            if (keyDown){
                HandSetHotKey(keyCode);
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        HandSetHotKey(keyCode);
        return super.onKeyDown(keyCode, event);
    }

    private void InitProgress() {
        YLProgressDialog = new ProgressDialog(YLBaseActivity.this);
        YLProgressDialog.setCancelable(false);
        YLProgressDialog.setMax(100);
        YLProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        YLProgressDialog.setMessage("正获取数据...");
        YLProgressDialog.setProgress(0);
        YLProgressDialog.show();
    }

    public abstract class YLWebDataAsyTaskForeground extends AsyncTask<String,Integer,String>{

        private JSONObject jsonObject;
        private String YLuRL;
        private int TimeOut;

        public YLWebDataAsyTaskForeground(JSONObject js, String Url,int TimeOut){
            this.jsonObject = js;
            this.YLuRL = Url;
            this.TimeOut = TimeOut;
        }

        @Override
        protected void onPreExecute() {
            InitProgress();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            HttpPost post = new HttpPost(YLuRL);
            HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, 1000*TimeOut);
            HttpConnectionParams.setSoTimeout(httpParams, 1000*TimeOut);
            InputStream inputStream = null;
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            String retstr = "";
            try {
                post.setEntity(new StringEntity(jsonObject.toString(),"UTF-8"));
                post.setHeader(HTTP.CONTENT_TYPE,"text/json");
                HttpClient client = new DefaultHttpClient(httpParams);
                HttpResponse response = client.execute(post);
                if (response.getStatusLine().getStatusCode() == 200){
                    inputStream = response.getEntity().getContent();
                    long filelength = response.getEntity().getContentLength();
                    int len = 0;
                    byte[] data = new byte[1024];
                    int totallength = 0;
                    int value = 0;
                    while ((len = inputStream.read(data)) != -1) {
                        totallength += len;
                        value = (int) ((totallength / (float) filelength) * 100);
                        publishProgress(value);
                        outputStream.write(data, 0, len);
                    }
                    byte[] result = outputStream.toByteArray();
                    retstr = new String(result, "UTF-8");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return retstr;
        }

        @Override
        protected abstract void onPostExecute(String s);

        @Override
        protected void onProgressUpdate(Integer... values) {
            YLProgressDialog.setProgress(values[0]);
            super.onProgressUpdate(values);
        }
    }

    public abstract class YLWebDataAsyTaskBlackground extends AsyncTask<String,Integer,String>{

        private JSONObject jsonObject;
        private String YLuRL;
        private int TimeOut;

        public YLWebDataAsyTaskBlackground(JSONObject js, String Url,int timeout){
            this.jsonObject = js;
            this.YLuRL = Url;
            this.TimeOut = timeout;
        }
        @Override
        protected String doInBackground(String... strings) {
            HttpPost post = new HttpPost(YLuRL);
            HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, 1000*TimeOut);
            HttpConnectionParams.setSoTimeout(httpParams, 1000*TimeOut);
            String serreturn = "";
            try {
                post.setEntity(new StringEntity(jsonObject.toString(),"UTF-8"));
                post.setHeader(HTTP.CONTENT_TYPE,"text/json");
                HttpClient client = new DefaultHttpClient(httpParams);
                HttpResponse response = client.execute(post);
                if (response.getStatusLine().getStatusCode() == 200){
                    String content = EntityUtils.toString(response.getEntity());
                    serreturn=  gson.fromJson(content, String.class);
                }
            } catch (Exception e) {
                Log.e(YLSystem.getKimTag(),"服务出错");
                e.printStackTrace();
            }
            return serreturn;
        }

        @Override
        protected abstract void onPostExecute(String s);
    }




    /** 通过Class跳转界面 **/
    protected void YLstartActivity(Class<?> cls) {
        YLstartActivity(cls, null);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    /** 含有Bundle通过Class跳转界面 **/
    protected void YLstartActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        if(intent.resolveActivity(getPackageManager()) != null){
            startActivity(intent);
        }
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    /** 通过Action跳转界面 **/
    protected void YLstartActivity(String action) {
        Intent intent = new Intent();
        intent.setAction(action);
        if(intent.resolveActivity(getPackageManager()) != null){
            startActivity(intent);
        }
    }

    /**含有Date通过Action跳转界面**/
    protected void YLstartActivity(String action,Uri data) {
        Intent intent = new Intent();
        intent.setAction(action);
        intent.setData(data);
        if(intent.resolveActivity(getPackageManager()) != null){
            startActivity(intent);
        }
    }

    /** 含有Bundle通过Action跳转界面 **/
    protected void YLstartActivity(String action, Bundle bundle) {
        Intent intent = new Intent();
        intent.setAction(action);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        if(intent.resolveActivity(getPackageManager()) != null){
            startActivity(intent);
        }
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }


    /** 短暂显示Toast提示(来自String) **/
    protected void YLshowShortToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    /** 长时间显示Toast提示(来自res) **/
    protected void YLshowLongToast(int resId) {
        Toast.makeText(this, getString(resId), Toast.LENGTH_LONG).show();
    }

    /** 带有右进右出动画的退出 **/
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    /** 带有右进右出动画的退出 **/
    public void MyLog(String msg) {
        Log.e(YLSystem.getKimTag(), msg);
    }

    public void YLMessagebox(String msg) {
        new AlertDialog.Builder(YLBaseActivity.this).setTitle("提示")
                .setMessage(msg)
                .setPositiveButton("确定", null).show();
    }

    /*读取HF信息*/
    public String HFReadUID()
    {
        return hfReader.ReadCardStr();
    }

    @Override
    protected void onPause() {
        try {
            if (keyReceiver != null) {
                unregisterReceiver(keyReceiver);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onPause();
    }

    @Override
    protected void onPostResume() {
        hfReader = new HFReader();
        RegisterReceiver();
        super.onPostResume();
    }

}


/*

    private class WebDataThread extends Thread {

        private JSONObject jsonObject;
        private String Url;

        public WebDataThread(JSONObject json,String Url){
            this.jsonObject = json;
            this.Url = Url;
        }

        @Override
        public void run() {
           try{
               YLWebDataabstract ylWebData = new YLWebDataabstract(jsonObject,Url);
               WebData =ylWebData.doInBackground();
               ylHandler.sendEmptyMessage(1);

           }catch (Exception e){
               ylHandler.sendEmptyMessage(0);
               e.printStackTrace();
           }
        }
    }

    public String GetWebData (JSONObject json,String Url){
        YLProgressDialog.show();
        WebDataThread  webDataThread = new WebDataThread(json,Url);
        Thread thread = new Thread(webDataThread,"");
        thread.start();
        return WebData;
    }
}
*/



