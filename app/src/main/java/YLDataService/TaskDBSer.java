package YLDataService;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

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
        SQLiteDatabase sdb = ylsqlHelper.getWritableDatabase();
        sdb.beginTransaction();
        try {
           // sdb.execSQL("",task.getId().toString());
        }
        finally {
            sdb.endTransaction();
        }
    }
    public void InsertYLTask(YLTask x) {
        SQLiteDatabase sdb = ylsqlHelper.getWritableDatabase();
        sdb.beginTransaction();
        try {
//            sdb.execSQL("INSERT INTO YLTask(ServerVersion, TaskVersion, TaskID, TaskType, Handset, " +
//                            "TaskDate, Line, TaskManager, TaskATMBeginTime, TaskATMEndTime, " +
//                            "TaskManagerNo, ServerReturn) VALUES   (?,?,?,?,?,?,?,?,?,?,?,?,)",
//                    new Object[]{x.getServerReturn(),x.getTaskVersion(),x.getTaskID(),x.getTaskType(),
//                            x.getHandset(),x.getTaskDate(),x.getLine(),x.getTaskManager()} );
            //暂停更新, 跟服务器端的版本不同
        }
        finally {
            sdb.endTransaction();
        }
    }
    public void UpdateYLTask(YLTask x) {

    }
    public void DeleteYLTask(YLTask x) {

    }

}
