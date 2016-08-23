package ylescort.ylmobileandroid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import YLSystemDate.YLSystem;

/**
 * Created by Administrator on 2016-01-27.
 */
public abstract class YLBaseScanActivity extends YLBaseActivity {

    private Scan1DRecive Scan1D;
    private IntentFilter intentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InitReciveScan1D();
        GetScreen();
    }

    @Override
    protected abstract void InitLayout() ;

    @Override
    protected abstract void InitData() ;

    private void InitReciveScan1D() {
        Scan1D = new Scan1DRecive();
        IntentFilter filter = new IntentFilter();
        filter.addAction("ylescort.ylmobileandroid.YLBaseScanActivity");
        registerReceiver(Scan1D, filter);
        Intent start = new Intent(YLBaseScanActivity.this,Scan1DService.class);
        YLBaseScanActivity.this.startService(start);
    }

    public class Scan1DRecive extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String recivedata = intent.getStringExtra("result");
            Log.e(YLSystem.getKimTag(), recivedata);
            if (recivedata != null) {
                if (recivedata.length() == 10) {
                    YLPutdatatoList(recivedata);
                }
            }
        }
    }

    public abstract void YLPutdatatoList(String recivedata) ;


    /**
     * 启动红外扫描
     * @param：0: 不扫描 1：扫描一次 2：多次扫描
     */
    public void Scan1DCmd (int onceornot){
        String activity = "ylescort.ylmobileandroid.YLBaseScanActivity";
        Intent ac = new Intent();
        ac.setAction("ylescort.ylmobileandroid.Scan1DService");
        ac.putExtra("activity", activity);
        sendBroadcast(ac);
        Intent sendToservice = new Intent(YLBaseScanActivity.this, Scan1DService.class); // 用于发送指令
        switch (onceornot){
            case 0:sendToservice.putExtra("cmd", "stopscan");
                break;
            case 1:sendToservice.putExtra("cmd", "scan");
                break;
            case 2:sendToservice.putExtra("cmd", "toscan100ms");
                break;
            default:sendToservice.putExtra("cmd", "stopscan");
                break;
        }

        this.startService(sendToservice); // 发送指令
    }


    private void GetScreen() {
        intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
        if (mBatlnfoReceiver != null) {
            registerReceiver(mBatlnfoReceiver, intentFilter);
        }
    }

    private BroadcastReceiver mBatlnfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Intent.ACTION_SCREEN_OFF.equals(action)){
                Scan1DCmd(0);
            }
        }
    };


    @Override
    protected void onDestroy() {
        if (Scan1D !=null){
            unregisterReceiver(Scan1D);
        }
        if (mBatlnfoReceiver != null) {
            unregisterReceiver(mBatlnfoReceiver);
        }
        Scan1DCmd(0);
        super.onDestroy();
    }
}
