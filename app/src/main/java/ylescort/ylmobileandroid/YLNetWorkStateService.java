package ylescort.ylmobileandroid;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import YLDataService.LocalSetting;
import YLSystemDate.YLSystem;

/**
 * Created by Administrator on 2015/3/24.
 */
public class YLNetWorkStateService extends Service {

    private static final String tag="tag";
    private ConnectivityManager connectivityManager;
    private NetworkInfo info;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                info = connectivityManager.getActiveNetworkInfo();

                if(info != null && info.isAvailable()) {

                    String name = info.getTypeName();
                    if (name.equals("mobile")){
                        YLSystem.setNetWorkState("0");
                        //Log.d("ping", "0");

                    }else {
                        YLSystem.setNetWorkState("1");
                        //Log.d("ping", "1");
                    }

//                    String str = new NetPing().doInBackground();
//                    if (str.equals("faild")){
//                        Toast.makeText(context,"没有可用网络",Toast.LENGTH_SHORT).show();
//                    }
                    //doSomething()
                } else {
                    YLSystem.setNetWorkState("2");
                    Toast.makeText(context,"没有可用网络",Toast.LENGTH_SHORT).show();
                    //doSomething()
                }
            }
        }
    };


    public String Ping(String str){
        String resault = "";
        Process p;
        try {
            //ping -c 3 -w 100  中  ，-c 是指ping的次数 3是指ping 3次 ，-w 100  以秒为单位指定超时间隔，是指超时时间为100秒
            p = Runtime.getRuntime().exec("ping -c 1 -w 50 " + str);
            int status = p.waitFor();

            InputStream input = p.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(input));
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while ((line = in.readLine()) != null){
                buffer.append(line);
            }
            System.out.println("Return ============" + buffer.toString());

            if (status == 0) {
                resault = "success";
            } else {
                resault = "faild";
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return resault;
    }

    private class NetPing extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            String s = "";
            s = Ping(LocalSetting.webintertest);
            Log.d("ping", s);
            return s;
        }
    }


    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mReceiver, mFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //new NetPing().doInBackground();
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
