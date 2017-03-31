package YLDataService;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import TaskClass.ArriveTime;
import TaskClass.Box;
import TaskClass.Site;
import TaskClass.YLTask;

/**
 * Created by Administrator on 2015/1/19.
 */
public class TaskDBSer {
    private YLSQLHelper ylsqlHelper;
    private Context _context;

    public TaskDBSer(Context context) {
        _context=context;
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

            }
        }
        finally {
            sdb.setTransactionSuccessful();
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

            }
        }
        finally {
            sdb.setTransactionSuccessful();
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

            }
        }
        finally {
            sdb.setTransactionSuccessful();
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

        }
        finally {
            sdb.setTransactionSuccessful();
            sdb.endTransaction();
            sdb.close();
        }
    }

    public List<YLTask> FindTaskIDByDate(String datetime){
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

    //删除指定日期的任务
    public void DeleteYLTask(String TaskDate) {
        SQLiteDatabase sdb = ylsqlHelper.getWritableDatabase();

        List<YLTask> ylTaskList=FindTaskIDByDate(TaskDate);
        List<Site> lstSite=new ArrayList<>();
        List<Box> lstBox=new ArrayList<>();
        List<ArriveTime> lstArriveTime=new ArrayList<>();

        for (YLTask x : ylTaskList) {
            String where = " where TaskID=" + String.valueOf(x.getTaskID());
            SiteDBSer siteDBSer = new SiteDBSer(_context);
            lstSite.addAll( siteDBSer.GetSites(where));
        }
        for (Site x : lstSite) {
            String where = " where SiteID=" + String.valueOf(x.getSiteID());
            BoxDBSer boxDBSer=new BoxDBSer(_context);
            lstBox.addAll(boxDBSer.GetBoxs(where));

            ArriveTimeDBSer arriveTimeDBSer=new ArriveTimeDBSer(_context);
            lstArriveTime.addAll(arriveTimeDBSer.GetArriveTime(where));
        }

        sdb.beginTransaction();
        try {

             /*   sdb.execSQL("delete from Box where TaskDate=?", new Object[]{TaskDate} );
                sdb.execSQL("delete from YLTask where TaskDate=?", new Object[]{TaskDate} );
            sdb.execSQL("delete from Box where TaskDate=?", new Object[]{TaskDate} );
            sdb.execSQL("delete from Site where TaskDate=?", new Object[]{TaskDate} );
            sdb.execSQL("delete from Site where TaskDate=?", new Object[]{TaskDate} );*/
        }
        finally {
            sdb.setTransactionSuccessful();
            sdb.endTransaction();
            sdb.close();
        }

    }






}
