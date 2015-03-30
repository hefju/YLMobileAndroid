package YLDataService;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2015/1/19.
 */
public class YLSQLHelper extends SQLiteOpenHelper {
    public YLSQLHelper(Context context) { super(context, "YLDB.db", null,1);}
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Employee (ID integer primary key autoincrement,EmpID varchar(20),EmpNO varchar(20),Pass varchar(20),\n" +
                "Name varchar(20),DeviceID varchar(20),ISWIFI varchar(20),Time varchar(20),ServerReturn varchar(20))");

        db.execSQL("CREATE TABLE Site (Id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, ServerReturn varchar(50), TaskID varchar(50)," +
                " SiteID varchar(50), SiteName varchar(50), SiteManager varchar(50), SiteManagerPhone varchar(50), SiteType varchar(50)," +
                " Status varchar(50), ATMCount varchar(50))" );

        db.execSQL("CREATE TABLE Box (Id INTEGER  PRIMARY KEY AUTOINCREMENT NOT NULL, ServerReturn varchar(50), SiteID varchar(50), BoxID varchar(50), " +
                        "BoxName varchar(50), TradeAction varchar(50), BoxStatus varchar(50), BoxType varchar(50), " +
                        "NextOutTime varchar(50), ActionTime varchar(50), TimeID varchar(50))");

        db.execSQL("CREATE TABLE ArriveTime (Id INTEGER  PRIMARY KEY AUTOINCREMENT NOT NULL, ServerReturn varchar(50), EmpID varchar(50)," +
                " ATime varchar(50)," +
                " TimeID varchar(50), TradeBegin varchar(50), TradeEnd varchar(50), TradeState varchar(50));");
        db.execSQL("CREATE TABLE YLTask (ID integer primary key autoincrement,ServerVersion varchar(50), TaskVersion varchar(50),"+
                " TaskID varchar(50), TaskType varchar(50), Handset varchar(50), TaskDate varchar(50), Line varchar(50), TaskManager varchar(50),"+
                "  TaskATMBeginTime varchar(50), TaskATMEndTime varchar(50), TaskManagerNo varchar(50), ServerReturn varchar(50))");

        db.execSQL("CREATE TABLE BaseBox (Id INTEGER PRIMARY KEY autoincrement NOT NULL, ServerReturn varchar(50), " +
                "BoxID varchar(50), BoxName varchar(50), BoxUHFNo varchar(50), BoxBCNo varchar(50), " +
                "BoxType varchar(50), ClientID varchar(50), SiteID varchar(50) NULL)");
        db.execSQL("CREATE TABLE BaseClient (Id INTEGER PRIMARY KEY autoincrement NOT NULL, " +
                "ServerReturn varchar(50), ClientID varchar(50), ClientName varchar(50)," +
                " ClientType varchar(50))");

        db.execSQL("CREATE TABLE BaseEmp (Id INTEGER PRIMARY KEY autoincrement NOT NULL, ServerReturn varchar(50)," +
                " EmpID varchar(50), EmpName varchar(50), EmpNo varchar(50), EmpHFNo varchar(50)," +
                " EmpWorkState varchar(50), EmpJJNo varchar(50))");
        db.execSQL("CREATE TABLE BaseSite (Id INTEGER PRIMARY KEY autoincrement NOT NULL, ServerReturn varchar(50)," +
                " SiteID varchar(50), SiteName varchar(50), SiteType varchar(50), ClientID varchar(50))");

        db.execSQL("CREATE TABLE TasksManager (Id INTEGER PRIMARY KEY autoincrement NOT NULL, TaskDate varchar(50),Data TEXT,EMPID varchar(50))");

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL("ALTER TABLE person ADD amount integer");
        //db.execSQL("ALTER TABLE TasksManager ADD EMPID varchar(50)");
    }
}
