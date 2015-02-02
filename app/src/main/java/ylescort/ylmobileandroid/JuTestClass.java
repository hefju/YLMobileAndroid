package ylescort.ylmobileandroid;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;

/**
 * Created by rush on 2015-01-31.
 */
public class JuTestClass {

    public  void goahead1(Context ctx){
       // android.content.getSharedPreferences
//        ContextWrapper wrapper=new ContextWrapper(ctx);
//        SharedPreferences settings =  wrapper.getSharedPreferences("Settings", wrapper.MODE_PRIVATE);
//        String content = settings.getString("Handset_name", "无数据");
//        android.util.Log.d("jutest", content);
        ContextWrapper wrapper=new ContextWrapper(ctx);
        SharedPreferences settings =  wrapper.getSharedPreferences("ju", wrapper.MODE_PRIVATE);
        SharedPreferences.Editor edit=settings.edit();
        String content = settings.getString("hefju", "无数据");
        android.util.Log.d("jutest", content);
    }

    public  void goahead2(Context ctx){
//        android.util.Log.d("jutest", "goahead2");
        Intent intent = new Intent();
        intent.setClass(ctx, SettingsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx. startActivity(intent);

//        ContextWrapper wrapper=new ContextWrapper(ctx);
//        SharedPreferences settings =  wrapper.getSharedPreferences("ju", wrapper.MODE_PRIVATE);
//        SharedPreferences.Editor edit=settings.edit();
//        edit.putString("hefju","gogo");
//        edit.apply();
    }

}
