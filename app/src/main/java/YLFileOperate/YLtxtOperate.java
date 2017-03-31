package YLFileOperate;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.util.SparseArray;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

import YLSystemDate.YLSystem;

/**
 * Created by Administrator on 2016-04-13.
 */
public class YLtxtOperate   {

    private Context context;
    /** SD卡是否存在**/
    private boolean hasSD = false;
    /** SD卡的路径**/
    public String SDPATH;
    /** 当前程序包的路径**/

//    private String FILESPATH;

    public YLtxtOperate(Context context) {
        this.context = context;
        hasSD = Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);
        SDPATH = Environment.getExternalStorageDirectory().getPath()+"//YLLOG";
//        FILESPATH = this.context.getFilesDir().getPath();
    }
    /**
     * 在SD卡上创建文件
     *
     * @throws IOException
     */
    public File createSDFile(String fileName) throws IOException {
        CreateDri();
        File file = new File(SDPATH + "//"+fileName);
        if (!file.exists()) {
            file.createNewFile();
        }
        return file;
    }

    /**
     *创建文件夹
     **/
    public void  CreateDri() throws IOException {
        File file = new File(SDPATH);
        if (!file.exists()) {
            file.mkdirs();
            SDPATH = SDPATH;
        }
    }

    /**
     * 删除SD卡上的文件
     *
     * @param fileName
     */
    public boolean deleteSDFile(String fileName) {
        File file = new File(SDPATH + "//" + fileName);

        if (file == null || !file.exists() || file.isDirectory())
            return false;
        return file.delete();
    }

    /**
     * 读取SD卡中文本文件
     *
     * @param fileName
     * @return
     */
    public String readSDFile(String fileName) throws  Exception{
        String res = "";
        File file = new File(fileName);
        FileInputStream fis = new FileInputStream(file);
        int length = fis.available();
        byte [] buffer = new byte[length];
        fis.read(buffer);
        res = new String(buffer,"UTF-8");
        fis.close();
        return res;
    }

    /**
     * 追加文件：使用FileWriter
     *
     * @param fileName
     * @param content
     */
    public  void AppendWrite(String fileName, String content) {
        try {
            // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
            FileWriter writer = new FileWriter(fileName, true);
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
