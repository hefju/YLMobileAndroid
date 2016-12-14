package YLDataService;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import TaskClass.TasksManager;
import YLSystemDate.YLSystem;

/**
 * Created by rush on 2015-02-27.
 */

    public class TasksManagerDBSer {
    private YLSQLHelper ylsqlHelper;

    public TasksManagerDBSer(Context context) {
        this.ylsqlHelper = new YLSQLHelper(context);
    }

    public TasksManager GetTasksManager(String TaskDate) {
        SQLiteDatabase sdb =ylsqlHelper.getReadableDatabase();
        Cursor cursor = sdb.rawQuery("SELECT * FROM TasksManager WHERE TaskDate = ? and EMPID=?",new String[]{TaskDate,YLSystem.getUser().getEmpID()});
        TasksManager tasksManager=null;
        while (cursor.moveToNext()){
            String content=cursor.getString(cursor.getColumnIndex("Data"));
            Gson gson = new Gson();
            tasksManager = gson.fromJson(content, new TypeToken<TasksManager>() { }.getType());
        }
        sdb.close();
        if (tasksManager==null)
            tasksManager=new TasksManager();
        return tasksManager;
    }

    public void SaveTasksManager(TasksManager tasksManager) {
        DeleteTasksManager(tasksManager);

        SQLiteDatabase sdb = ylsqlHelper.getWritableDatabase();
        sdb.beginTransaction();
        try {
            Gson gson = new Gson();
            String content=gson.toJson(tasksManager);
                sdb.execSQL("INSERT INTO TasksManager(TaskDate , Data,EMPID) VALUES (?,?,?)",
                        new Object[]{tasksManager.TaskDate,content,YLSystem.getUser().getEmpID()} );
        }
        finally {
            sdb.setTransactionSuccessful();
            sdb.endTransaction();
            sdb.close();
        }
    }

    public void DeleteTasksManager(TasksManager tasksManager) {
        SQLiteDatabase sdb = ylsqlHelper.getWritableDatabase();
        sdb.beginTransaction();
        try {
//            Gson gson = new Gson();
//            String content=gson.toJson(tasksManager);
            sdb.execSQL("delete from TasksManager where TaskDate= ? and EMPID =?"
                    ,new Object[]{tasksManager.TaskDate,YLSystem.getUser().getEmpID()} );
        }
        finally {
            sdb.setTransactionSuccessful();
            sdb.endTransaction();
            sdb.close();
        }
    }

    public void DeleteTasksManagerbydate(String date) {
        SQLiteDatabase sdb = ylsqlHelper.getWritableDatabase();
        sdb.beginTransaction();
        try {
            String sql = "delete from TasksManager where TaskDate <='"+date+"'";
            sdb.execSQL(sql);
        }
        finally {
            sdb.setTransactionSuccessful();
            sdb.endTransaction();
            sdb.close();
        }
    }



}

