package ylescort.ylmobileandroid;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

/**
 * Created by rush on 2015-01-31.
 */
public class JuTestClass {

    public  void goahead1(Context ctx){

        ContextWrapper wrapper=new ContextWrapper(ctx);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        String content =  prefs.getString("HandsetName", "无数据");
        Toast.makeText(ctx.getApplicationContext(),content, Toast.LENGTH_SHORT).show();
       // android.util.Log.d("jutest", content);
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
