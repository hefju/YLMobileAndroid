package YLDataService;

import android.content.Context;
import java.util.List;
import TaskClass.BaseClient;
import TaskClass.BaseSite;

/**
 * Created by Administrator on 2016-03-18.
 */
public class YLSiteInfo {

    private  Context context;

    public YLSiteInfo(Context context) {
        this.context = context;
    }

    public String GetClientbySiteID(String SiteID){
        String ClienName = "";
        BaseSiteDBSer dbSer = new BaseSiteDBSer(context);
        List<BaseSite> baseSites = dbSer.GetBaseSites("where SiteID =" + SiteID);
        if (baseSites.size()>0){
            BaseClientDBSer clientDBSer = new BaseClientDBSer(context);
            List<BaseClient>  baseClient =  clientDBSer.GetBaseClients("where ClientID =" + baseSites.get(0).ClientID);
            if (baseClient.size() >0){
                ClienName = baseClient.get(0).ClientName;
            }else {
                ClienName = "粤龙交接客户";
            }
        }else {
            ClienName = "粤龙交接客户";
        }
        return ClienName;
    }




}
