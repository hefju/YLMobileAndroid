package YLDataService;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import YLSystemDate.YLSysTime;
import YLSystemDate.YLSystem;

/**
 * Created by Administrator on 2015/1/19.
 */
public class YLSQLHelper extends SQLiteOpenHelper {
    public YLSQLHelper(Context context) { super(context, "YLDB.db", null,4);}
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE BaseBox (Id INTEGER PRIMARY KEY autoincrement NOT NULL, ServerReturn varchar(50), " +
                "BoxID varchar(50), BoxName varchar(50), BoxUHFNo varchar(50), BoxBCNo varchar(50), " +
                "BoxType varchar(50), ClientID varchar(50), SiteID varchar(50) NULL)");
        db.execSQL("CREATE TABLE BaseClient (Id INTEGER PRIMARY KEY autoincrement NOT NULL, " +
                "ServerReturn varchar(50), ClientID varchar(50), ClientName varchar(50)," +
                " ClientType varchar(50), HFNo varchar(50))");

        db.execSQL("CREATE TABLE BaseEmp (Id INTEGER PRIMARY KEY autoincrement NOT NULL, ServerReturn varchar(50)," +
                " EmpID varchar(50), EmpName varchar(50), EmpNo varchar(50), EmpHFNo varchar(50)," +
                " EmpWorkState varchar(50), EmpJJNo varchar(50))");

        db.execSQL("CREATE TABLE BaseSite (Id INTEGER PRIMARY KEY autoincrement NOT NULL, ServerReturn varchar(50)," +
                " SiteID varchar(50), SiteName varchar(50), SiteType varchar(50), ClientID varchar(50),"+
                " SiteBCNo varchar(50),SiteHFNo varchar(50))");

        db.execSQL("CREATE TABLE TasksManager (Id INTEGER PRIMARY KEY autoincrement NOT NULL, TaskDate varchar(50),Data TEXT,EMPID varchar(50))");

        db.execSQL("CREATE TABLE BaseClient_HF (Id INTEGER PRIMARY KEY autoincrement NOT NULL, " +
                "ServerReturn varchar(50), ClientID varchar(50), HFNo varchar(50)," +
                " Mark varchar(50), ServerTime varchar(50))");

        db.execSQL("CREATE TABLE BaseATMBox    (Id INTEGER PRIMARY KEY autoincrement NOT NULL, " +
                "ATMBoxID varchar(50), ClientID varchar(50), UseClientID varchar(50), BoxCode varchar(50), "+
                " BoxName varchar(50), BoxBrand varchar(50), Boxtype varchar(50), Boxvalue varchar(50),"+
                " Passageway varchar(50),ServerReturn varchar(50),Mark varchar(50),ServerTime varchar(50))");

        db.execSQL("CREATE TABLE BaseATMMachine    (Id INTEGER PRIMARY KEY autoincrement NOT NULL, " +
                "ServerReturn varchar(50), MachineID varchar(50), SiteID varchar(50), MachineName varchar(50), "+
                " MachineType varchar(50), MachineNo varchar(50), MachineHFNo varchar(50), MachineCode varchar(50),"+
                " Mark varchar(50),ServerTime varchar(50))");

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL("ALTER TABLE person ADD amount integer");
        Log.e(YLSystem.getKimTag(),"数据库升级"+newVersion);
        switch (oldVersion){
            case 2:
                db.execSQL("ALTER TABLE BaseSite ADD SiteHFNo varchar(50) ");
                db.execSQL("CREATE TABLE BaseATMBox    (Id INTEGER PRIMARY KEY autoincrement NOT NULL, " +
                        "ATMBoxID varchar(50), ClientID varchar(50), UseClientID varchar(50), BoxCode varchar(50), "+
                        " BoxName varchar(50), BoxBrand varchar(50), Boxtype varchar(50), Boxvalue varchar(50),"+
                        " Passageway varchar(50),ServerReturn varchar(50),Mark varchar(50),ServerTime varchar(50))");
                db.execSQL("CREATE TABLE BaseATMMachine    (Id INTEGER PRIMARY KEY autoincrement NOT NULL, " +
                        "ServerReturn varchar(50), MachineID varchar(50), SiteID varchar(50), MachineName varchar(50), "+
                        " MachineType varchar(50), MachineNo varchar(50), MachineHFNo varchar(50), MachineCode varchar(50),"+
                        " Mark varchar(50),ServerTime varchar(50))");
                break;
            case 3:
                db.execSQL("CREATE TABLE BaseATMBox    (Id INTEGER PRIMARY KEY autoincrement NOT NULL, " +
                        "ATMBoxID varchar(50), ClientID varchar(50), UseClientID varchar(50), BoxCode varchar(50), "+
                        " BoxName varchar(50), BoxBrand varchar(50), Boxtype varchar(50), Boxvalue varchar(50),"+
                        " Passageway varchar(50),ServerReturn varchar(50),Mark varchar(50),ServerTime varchar(50))");
                db.execSQL("CREATE TABLE BaseATMMachine    (Id INTEGER PRIMARY KEY autoincrement NOT NULL, " +
                        "ServerReturn varchar(50), MachineID varchar(50), SiteID varchar(50), MachineName varchar(50), "+
                        " MachineType varchar(50), MachineNo varchar(50), MachineHFNo varchar(50), MachineCode varchar(50),"+
                        " Mark varchar(50),ServerTime varchar(50))");
                break;
        }
    }
}
