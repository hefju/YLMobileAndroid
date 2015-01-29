package YLDataService;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2015/1/19.
 */
public class YLSQLHelper extends SQLiteOpenHelper {
    public YLSQLHelper(Context context) { super(context, "YLDB", null,1);}
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE T_employee (ID integer primary key autoincrement,EmpID varchar(20),EmpNO varchar(20),Pass varchar(20),\n" +
                "Name varchar(20),DeviceID varchar(20),ISWIFI varchar(20),Time varchar(20),ServerReturn varchar(20))");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("ALTER TABLE person ADD amount integer");
    }
}
