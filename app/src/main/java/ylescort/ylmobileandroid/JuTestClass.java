package ylescort.ylmobileandroid;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

import TaskClass.TasksManager;
import YLDataService.TasksManagerDBSer;
import YLDataService.WebService;
import YLSystem.YLSystem;
import ylescort.ylmobileandroid.util.CacheDataView;

/**
 * Created by rush on 2015-01-31.
 */
public class JuTestClass {

    public  void goahead1(Context ctx){
        TasksManager tasksManager=YLSystem.getTasksManager();//获取任务管理类
//      int taskNum=  tasksManager.lstLatestTask.size();
//        Toast.makeText(ctx.getApplicationContext(),"任务数量:"+taskNum, Toast.LENGTH_SHORT).show();

        tasksManager.TaskDate="2015-02-28";//"2014-08-07";
        TasksManagerDBSer dbSer=new TasksManagerDBSer(ctx);
        dbSer.DeleteTasksManager(tasksManager);
        Toast.makeText(ctx.getApplicationContext(),"删除任务:"+tasksManager.TaskDate, Toast.LENGTH_SHORT).show();
//        Intent intent=new Intent();
//        intent.setClass(ctx, CacheDataView.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        ctx.startActivity(intent);

//        WebService ws=new WebService();
//        String serverVer=   ws.getServerVer();
//        String localVer= YLSystem.getVerName(ctx);
//        if(  Double.parseDouble(serverVer)>  Double.parseDouble(localVer)) {
//            Toast.makeText(ctx.getApplicationContext(),"你需要升级到:"+serverVer, Toast.LENGTH_SHORT).show();
//        }else{
//            Toast.makeText(ctx.getApplicationContext(),"你无需升级!", Toast.LENGTH_SHORT).show();
//        }


//        ContextWrapper wrapper=new ContextWrapper(ctx);
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
//        String content =  prefs.getString("HandsetName", "无数据");
//        Toast.makeText(ctx.getApplicationContext(),content, Toast.LENGTH_SHORT).show();
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
