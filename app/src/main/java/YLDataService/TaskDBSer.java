package YLDataService;

import android.content.Context;
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
            sdb.close();
        }
    }

}
