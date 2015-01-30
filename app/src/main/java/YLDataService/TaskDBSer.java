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

}
