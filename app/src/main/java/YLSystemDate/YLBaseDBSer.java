package YLSystemDate;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import YLDataService.WebServerBaseData;

/**
 * Created by Administrator on 2015-12-24.
 */
public class YLBaseDBSer {
    private Context context;
    public YLBaseDBSer (Context YLBasecontext){
        this.context = YLBasecontext;
    }

    public void CacheData() {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                    String timeLastUpdate = prefs.getString("CacheLastUpdate", "ALL");
                    Log.e(YLSystem.getKimTag(), timeLastUpdate + "缓存时间");
                    if (!timeLastUpdate.equals("ALL")) {
                        WebServerBaseData webServerBaseData = new WebServerBaseData();
                        webServerBaseData.GetBaseData(context, timeLastUpdate);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
}
