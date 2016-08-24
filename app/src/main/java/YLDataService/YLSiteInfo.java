package YLDataService;

import android.content.Context;
import android.util.Log;

import java.util.List;
import TaskClass.BaseClient;
import TaskClass.BaseSite;
import TaskClass.Site;
import YLSystemDate.YLSystem;

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


    public boolean CheckSiteHF (String SiteID,String HFNo){
        BaseSiteDBSer dbSer = new BaseSiteDBSer(context);
        String dbstr = dbSer.GetSiteHFNo(SiteID);
        String[] strings = dbstr.split(",");
        for (String s : strings) {
            if (s.equals(HFNo)){
                return  true;
            }
        }
        return false;
    }

    public String  GetBaseSiteName (String rehf){
        BaseSiteDBSer dbSer = new BaseSiteDBSer(context);
        BaseSite baseSite = dbSer.GetBasesingleSite("where SiteHFNo like '%"+rehf+"%'");
        String sitename = "";
        if (baseSite.getId() !=0){
            sitename = baseSite.getSiteName();
        }else {
            sitename = "未录入网点";
        }
        return sitename;
    }


}
