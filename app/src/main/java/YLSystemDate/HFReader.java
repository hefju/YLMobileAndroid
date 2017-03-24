package YLSystemDate;


import com.android.hdhe.nfc.NFCcmdManager;
import com.example.nfc.util.Tools;
import com.handheld.HF.HfError;
import com.handheld.HF.HfManager;


public class HFReader {

    private HfManager hf ;
    private HfError error;
    private byte[] uid;

    private NFCcmdManager manager;

    public HFReader() {
        if (YLSystem.getHFport() == 14)
        {
            hf = new HfManager();
            error = new HfError();
        }else {
            manager = NFCcmdManager.getNFCcmdManager(YLSystem.getHFport(), 115200, 0);
            manager.readerPowerOn();
        }

    }

    /*读取HF卡信息*/
    public String ReadCardStr()
    {
        if (YLSystem.getHFport() == 14) {
            uid = hf.SearchCard14443A(error);
            if (uid != null) {
                return Tools.Bytes2HexString(uid, uid.length);
            } else {
                return "";
            }
        }else {
            manager.init_14443A();
            uid = manager.inventory_14443A();
            if (uid != null) {
                return Tools.Bytes2HexString(uid, uid.length);
            }else{
                return "";
            }
        }
    }

    /*关闭HF读卡*/
    public void CloseHF()
    {
        if (YLSystem.getHFport() == 14){
            hf.Close();
        }else {
            manager.readerPowerOff();
        }

    }

}
