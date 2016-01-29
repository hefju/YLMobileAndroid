package ylescort.ylmobileandroid;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import YLSystemDate.YLSystem;

public class YLCarToCarTask extends YLBaseScanActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ylcar_to_car_task);
        InitLayout();
    }

    @Override
    protected void InitLayout() {


    }

    @Override
    protected void InitData() {

    }

    @Override
    public void YLPutdatatoList(String recivedata) {
        YLshowShortToast(recivedata);
    }

    @Override
    protected void onStop() {
        Log.e(YLSystem.getKimTag(),"stop activity");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.e(YLSystem.getKimTag(),"Destroy activity");
        super.onDestroy();
    }
}
