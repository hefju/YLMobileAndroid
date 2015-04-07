package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
import TaskClass.YLATM;
import ylescort.ylmobileandroid.R;

/**
 * Created by Administrator on 2015/4/7.
 */
public class YLATMSiteAdapter extends BaseAdapter {

    private List<YLATM> ylatmList ;
    private int resource;
    private LayoutInflater inflater;
    public YLATMSiteAdapter(Context context,List<YLATM> ylatmList,int resource){
        this.ylatmList = ylatmList;
        this.resource = resource;
        inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return ylatmList.size();
    }

    @Override
    public Object getItem(int position) {
        return ylatmList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView atmsitenum = null;
        TextView atmsitename = null;
        TextView atmsitestate = null;

        if (convertView == null)
        {
            convertView = inflater.inflate(resource,null);
            atmsitenum = (TextView) convertView.findViewById(R.id.ATMSiteitem_order);
            atmsitename = (TextView) convertView.findViewById(R.id.ATMSiteitem_Name);
            atmsitestate = (TextView) convertView.findViewById(R.id.ATMSiteitem_staut);
            ViewCache viewCache = new ViewCache();
            viewCache.YLatmsitenumview = atmsitenum;
            viewCache.YLatmsiteNameview = atmsitename;
            viewCache.YLatmsitestateview = atmsitestate;
            convertView.setTag(viewCache);
        }else {
            ViewCache viewCache = (ViewCache) convertView.getTag();
            atmsitenum = viewCache.YLatmsitenumview;
            atmsitename  = viewCache.YLatmsiteNameview;
            atmsitestate = viewCache.YLatmsitestateview;
        }

        YLATM ylatm = ylatmList.get(position);
        atmsitenum.setText(ylatm.getId()+"");
        atmsitename.setText(ylatm.getSiteName());
        atmsitestate.setText(ylatm.getSiteType());

        return convertView;
    }
    private final class ViewCache{
        public TextView YLatmsitenumview;
        public TextView YLatmsiteNameview;
        public TextView YLatmsitestateview;
    }
}
