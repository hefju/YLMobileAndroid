package YLDataService;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import TaskClass.FingerPrint;

/**
 * Created by Administrator on 2018/12/15.
 * 指纹数据库操作,插入,删除全部,返回全部
 */

public class FingerPrintDBSer {
    private String TAG="FingerPrintDBSer";
    private YLSQLHelper ylsqlHelper;

    public FingerPrintDBSer(Context context){this.ylsqlHelper = new YLSQLHelper(context);}


    //插入指纹,返回1表示插入成功,返回0表示插入失败
    public int InsFingerPrint(FingerPrint fingerPrint){
        Log.e("unit_test","InsFingerPrint:"+fingerPrint.toString());
        int count = 0;
        SQLiteDatabase sdb = ylsqlHelper.getWritableDatabase();
        sdb.beginTransaction();
        try {

            fingerPrint.setServerReturn("0");//设置为0表示未同步
            fingerPrint.setCreateAt( System.currentTimeMillis());//设置插入时间为当前时间
            //数据库插入参考:https://blog.csdn.net/woshixuye/article/details/8276582
            ContentValues cv=new ContentValues();
            cv.put("ServerReturn", fingerPrint.getServerReturn());
            cv.put("EmpNum", fingerPrint.getEmpNum());
            cv.put("Finger", fingerPrint.getFinger());
            cv.put("FingerType", fingerPrint.getFingerType());
            cv.put("CreateAt", fingerPrint.getCreateAt());
            long rowid=   sdb.insert("FingerPrint",null,cv);
            if(rowid>0){
                count = 1;
            }
        } catch (Exception e) {
            Log.e(TAG,e.getMessage());
            count = 0;
        }
        finally {
            sdb.setTransactionSuccessful();
            sdb.endTransaction();
            sdb.close();
        }
        return count;
    }
    //插入指纹,返回1表示插入成功,返回0表示插入失败
    public int UpdateFingerPrint(FingerPrint fingerPrint){
        Log.d("unit_test","UpdateFingerPrint:"+fingerPrint.toString());
        int count = 0;
        SQLiteDatabase sdb = ylsqlHelper.getWritableDatabase();
        sdb.beginTransaction();
        try {
            fingerPrint.setServerReturn("0");//设置为0表示未同步
            fingerPrint.setCreateAt( System.currentTimeMillis());//设置插入时间为当前时间
            sdb.execSQL("update FingerPrint set Finger=?,CreateAt=? where EmpNum=? and FingerType=?"
                    ,new Object[]{fingerPrint.getFinger(),fingerPrint.getCreateAt(),fingerPrint.getEmpNum(),fingerPrint.getFingerType()} );
            count = 1;
        } catch (Exception e) {
            count = 0;
        }
        finally {
            sdb.setTransactionSuccessful();
            sdb.endTransaction();
            sdb.close();
        }
        return count;
    }
    //设置指纹数据已经上传到服务器
    public int SetFingerPrintUpload(FingerPrint fingerPrint){
        int count = 0;
        SQLiteDatabase sdb = ylsqlHelper.getWritableDatabase();
        sdb.beginTransaction();
        try {
            sdb.execSQL("update FingerPrint set ServerReturn='1'  where EmpNum=?",new Object[]{fingerPrint.getEmpNum()} );
            count = 1;
        } catch (Exception e) {
            count = 0;
        }
        finally {
            sdb.setTransactionSuccessful();
            sdb.endTransaction();
            sdb.close();
        }
        return count;
    }
    //查询员工编号的指纹是否存在
    public boolean Exists(FingerPrint fingerPrint) {
        Log.d("unit_test","Exists:"+fingerPrint.toString());
        int count = 0;
        SQLiteDatabase sdb = ylsqlHelper.getReadableDatabase();
        sdb.beginTransaction();
        try {
            Cursor cursor = sdb.rawQuery("select Id from FingerPrint where EmpNum=? and FingerType=?", new String[]{fingerPrint.getEmpNum(),fingerPrint.getFingerType()});
            while (cursor.moveToNext()) {

                FingerPrint f = new FingerPrint();
                f.setId(cursor.getInt(cursor.getColumnIndex("Id")));
                Log.d("unit_test","FingerPrint.getId:"+f.getId());
                if(f.getId()>0)
                    count = 1;
            }

        } catch (Exception e) {
            count = 0;
        }
        finally {
            sdb.setTransactionSuccessful();
            sdb.endTransaction();
            sdb.close();
        }
        return count>0;
    }

    //读取单个个员工的指纹, 一个员工可能有多个指纹
    public List<FingerPrint> GetFingerPrint(FingerPrint fingerPrint) {
        List<FingerPrint> lstFP = new ArrayList<>();
        SQLiteDatabase sdb = ylsqlHelper.getReadableDatabase();
        sdb.beginTransaction();
        try {
            Cursor cursor = sdb.rawQuery("select * from FingerPrint where EmpNum=?", new String[]{fingerPrint.getEmpNum()});
            while (cursor.moveToNext()) {

                FingerPrint f = new FingerPrint();
                f.setId(cursor.getInt(cursor.getColumnIndex("Id")));
                f.setEmpNum(cursor.getString(cursor.getColumnIndex("EmpNum")));
                f.setFinger(cursor.getString(cursor.getColumnIndex("Finger")));
                f.setFingerType(cursor.getString(cursor.getColumnIndex("FingerType")));
                f.setCreateAt(cursor.getLong(cursor.getColumnIndex("CreateAt")));
                lstFP.add(f);
            }

        } catch (Exception e) {
        }
        finally {
            sdb.setTransactionSuccessful();
            sdb.endTransaction();
            sdb.close();
        }
        return lstFP;
    }

    //返回0表示操作不成功,返回1表示删除成功
    public  int  DeleteAllFingerPrint() {
        int count = 0;
        SQLiteDatabase sdb = ylsqlHelper.getWritableDatabase();
        sdb.beginTransaction();
        try {
            sdb.execSQL("delete from FingerPrint");
            count = 1;
        } catch (Exception e) {
            count = 0;
        } finally {
            sdb.setTransactionSuccessful();
            sdb.endTransaction();
            sdb.close();
        }
        return count;
    }
    //删除数据表重建
    public  int  DropFingerPrint() {
        int count = 0;
        SQLiteDatabase sdb = ylsqlHelper.getWritableDatabase();
        sdb.beginTransaction();
        try {
            sdb.execSQL("drop table FingerPrint");
            count = 1;
        } catch (Exception e) {
            count = 0;
        } finally {
            sdb.setTransactionSuccessful();
            sdb.endTransaction();
            sdb.close();
        }
        return count;
    }

    //返回所有的指纹
    public List<FingerPrint> GetAllFingerPrint() {
        List<FingerPrint> lstFP = new ArrayList<>();
        SQLiteDatabase sdb = ylsqlHelper.getReadableDatabase();

        try {
            Cursor cursor = sdb.rawQuery("select * from FingerPrint ", null);
            while (cursor.moveToNext()) {

                FingerPrint f = new FingerPrint();
                f.setId(cursor.getInt(cursor.getColumnIndex("Id")));
                f.setEmpNum(cursor.getString(cursor.getColumnIndex("EmpNum")));
                f.setFinger(cursor.getString(cursor.getColumnIndex("Finger")));
                f.setFingerType(cursor.getString(cursor.getColumnIndex("FingerType")));
                f.setCreateAt(cursor.getLong(cursor.getColumnIndex("CreateAt")));

                lstFP.add(f);
            }

        } catch (Exception e) {
            Log.e("unit_test", e.getMessage());
        } finally {
            sdb.close(); //关闭数据库
        }
        return lstFP;
    }




}
