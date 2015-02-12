package YLDataService;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import TaskClass.YLTask;

/**
 * Created by Administrator on 2015/1/19.
 */
public class TaskDBSer {
    private YLSQLHelper ylsqlHelper;

    public TaskDBSer(Context context) {
        this.ylsqlHelper = new YLSQLHelper(context);
    }

    public void InsterTask (YLTask task){
        List<YLTask> lst=new ArrayList<>();
        lst.add(task);
        InsertYLTask(lst);
    }

    public void UpdateYLTask(YLTask x) {
        List<YLTask> lst=new ArrayList<>();
        lst.add(x);
        UpdateYLTask(lst);
    }
    public void DeleteYLTask(YLTask x) {
        List<YLTask> lst=new ArrayList<>();
        lst.add(x);
        DeleteYLTask(lst);
    }

    public void UpdateYLTask(List<YLTask> lst) {
        SQLiteDatabase sdb = ylsqlHelper.getWritableDatabase();
        sdb.beginTransaction();
        try {
            for(YLTask x:lst) {
                sdb.execSQL("UPDATE  YLTask SET ServerVersion =?, TaskVersion =?, TaskID =?, TaskType =?," +
                            " Handset =?, TaskDate =?, Line =?, TaskManager =?, TaskATMBeginTime =?," +
                            " TaskATMEndTime =?, TaskManagerNo =?, ServerReturn =? where Id=?",
                        new Object[]{x.ServerVersion,x.TaskVersion,x.TaskID,x.TaskType, x.Handset,
                                x.TaskDate,x.Line,x.TaskManager,x.TaskATMBeginTime,x.TaskATMEndTime,
                                x.TaskManagerNo,x.ServerReturn,x.Id} );
                sdb.setTransactionSuccessful();
            }
        }
        finally {
            sdb.endTransaction();
            sdb.close();
        }
    }
    public void DeleteYLTask(List<YLTask> lst) {
        SQLiteDatabase sdb = ylsqlHelper.getWritableDatabase();
        sdb.beginTransaction();
        try {
            for(YLTask x:lst) {
                sdb.execSQL("delete from YLTask where Id=?", new Object[]{x.Id} );
                sdb.setTransactionSuccessful();
            }
        }
        finally {
            sdb.endTransaction();
            sdb.close();
        }

    }
    public void InsertYLTask(List<YLTask> lst) {
        SQLiteDatabase sdb = ylsqlHelper.getWritableDatabase();
        sdb.beginTransaction();
        try {
            for(YLTask x:lst) {
            sdb.execSQL("INSERT INTO YLTask(ServerVersion, TaskVersion, TaskID, TaskType, Handset, " +
                            "TaskDate, Line, TaskManager, TaskATMBeginTime, TaskATMEndTime, " +
                            "TaskManagerNo, ServerReturn) VALUES   (?,?,?,?,?,?,?,?,?,?,?,?)",
                    new Object[]{x.getServerVersion(),x.getTaskVersion(),x.getTaskID(),x.getTaskType(), x.getHandset(),
                           x.getTaskDate(),x.getLine(),x.getTaskManager(),x.getTaskATMBeginTime(),x.getTaskATMEndTime(),
                    x.getTaskManagerNo(),x.getServerReturn()} );
                sdb.setTransactionSuccessful();
            }
        }
        finally {
            sdb.endTransaction();
            sdb.close();
        }
    }

    public void InsertYLTask2(YLTask x) {
        SQLiteDatabase sdb = ylsqlHelper.getWritableDatabase();
        sdb.beginTransaction();
        try {
                sdb.execSQL("INSERT INTO YLTask(ServerVersion, TaskVersion, TaskID, TaskType, Handset, " +
                                "TaskDate, Line, TaskManager, TaskATMBeginTime, TaskATMEndTime, " +
                                "TaskManagerNo, ServerReturn) VALUES   (?,?,?,?,?,?,?,?,?,?,?,?)",
                        new Object[]{x.getServerVersion(),x.getTaskVersion(),x.getTaskID(),x.getTaskType(), x.getHandset(),
                                x.getTaskDate(),x.getLine(),x.getTaskManager(),x.getTaskATMBeginTime(),x.getTaskATMEndTime(),
                                x.getTaskManagerNo(),x.getServerReturn()} );
            sdb.setTransactionSuccessful();
        }
        finally {

            sdb.endTransaction();
            sdb.close();
        }
    }

    public List<YLTask> SelTaskID(String datetime){
        SQLiteDatabase sdb =ylsqlHelper.getReadableDatabase();
        Cursor cursor = sdb.rawQuery("select TaskID from YLTask where TaskDate = ?",new String[]{datetime});

        List<YLTask> ylTaskList =new ArrayList<YLTask>();


        while (cursor.moveToNext()){
            YLTask ylTask = new YLTask();
            String yltaskid = cursor.getString(cursor.getColumnIndex("TaskID"));
            ylTask.setTaskID(yltaskid);
            ylTaskList.add(ylTask);
        }
        sdb.close();
        return ylTaskList;
    }

    public List<String> SelTaskID2(String TaskID){
        SQLiteDatabase sdb =ylsqlHelper.getReadableDatabase();
        Cursor cursor = sdb.rawQuery("select TaskID from YLTask where TaskDate = ?",new String[]{TaskID});
        List<String> stringList = new ArrayList<String>();
        while (cursor.moveToNext()){
            stringList.add(cursor.getString(cursor.getColumnIndex("TaskID")));
        }
        sdb.close();
        return stringList;
    }

    public List<YLTask> SelTaskbydatetolist(String TaskDate){
        SQLiteDatabase sdb =ylsqlHelper.getReadableDatabase();
        Cursor cursor = sdb.rawQuery("SELECT * FROM YLTask WHERE TaskDate = ?",new String[]{TaskDate});
        List<YLTask> ylTaskList = new ArrayList<>();

        while (cursor.moveToNext()){
            YLTask ylTask = new YLTask();
            ylTask.setId(cursor.getInt(cursor.getColumnIndex("ID")));
            ylTask.setServerVersion(cursor.getString(cursor.getColumnIndex("ServerVersion")));
            ylTask.setTaskVersion(cursor.getString(cursor.getColumnIndex("TaskVersion")));
            ylTask.setTaskID(cursor.getString(cursor.getColumnIndex("TaskID")));
            ylTask.setTaskType(cursor.getString(cursor.getColumnIndex("TaskType")));
            ylTask.setHandset(cursor.getString(cursor.getColumnIndex("Handset")));
            ylTask.setTaskDate(cursor.getString(cursor.getColumnIndex("TaskDate")));
            ylTask.setLine(cursor.getString(cursor.getColumnIndex("Line")));
            ylTask.setTaskManager(cursor.getString(cursor.getColumnIndex("TaskManager")));
            ylTask.setTaskATMBeginTime(cursor.getString(cursor.getColumnIndex("TaskATMBeginTime")));
            ylTask.setTaskATMEndTime(cursor.getString(cursor.getColumnIndex("TaskATMEndTime")));
            ylTask.setTaskManagerNo(cursor.getString(cursor.getColumnIndex("TaskManagerNo")));
            ylTask.setServerReturn(cursor.getString(cursor.getColumnIndex("ServerReturn")));
            ylTaskList.add(ylTask);
        }
        sdb.close();
        return ylTaskList;
    }






}
