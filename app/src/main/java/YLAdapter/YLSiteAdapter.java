package YLAdapter;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import TaskClass.Site;
import ylescort.ylmobileandroid.R;
/**
 * Created by Administrator on 2015/4/21.
 */
public class YLSiteAdapter extends BaseAdapter {

    private List<Site> siteList;
    private int resource;
    private LayoutInflater inflater;
    public YLSiteAdapter(Context context,List<Site> siteList,int resource){
        this.siteList = siteList;
        this.resource = resource;
        inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return siteList.size();
    }

    @Override
    public Object getItem(int position) {
        return siteList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TextView sitenum = null;
        TextView sitename = null;
        TextView sitestate = null;

        if (convertView == null)
        {
            convertView = inflater.inflate(resource,null);
            sitenum = (TextView) convertView.findViewById(R.id.ylsiteitem_Num);
            sitename = (TextView) convertView.findViewById(R.id.ylsiteitem_Name);
            sitestate = (TextView) convertView.findViewById(R.id.ylsiteitem_state);
            ViewCache viewCache = new ViewCache();
            viewCache.YLsitenumview = sitenum;
            viewCache.YLsiteNameview = sitename;
            viewCache.YLsitestateview = sitestate;
            convertView.setTag(viewCache);
        }else {
            ViewCache viewCache = (ViewCache) convertView.getTag();
            sitenum = viewCache.YLsitenumview;
            sitename  = viewCache.YLsiteNameview;
            sitestate = viewCache.YLsitestateview;
        }

        Site site = siteList.get(position);
        sitenum.setText(site.getSiteType());
        sitename.setText(site.getSiteName());
        sitestate.setText(site.getStatus());

        if (sitestate.getText().equals("已完成")){
            convertView.setBackgroundColor(-13210);
        }else {
            convertView.setBackgroundColor(Color.TRANSPARENT);
        }
        return convertView;
    }


    private final class ViewCache{
        public TextView YLsitenumview;
        public TextView YLsiteNameview;
        public TextView YLsitestateview;
    }


}
