package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import TaskClass.Box;
import ylescort.ylmobileandroid.R;

/**
 * Created by KimStore on 2015/3/1.
 */
public class YLBoxAdapter extends BaseAdapter {

    private List<Box> boxList;
    private  int resource;
    private LayoutInflater inflater;
    public YLBoxAdapter(Context context,List<Box> boxList,int resource){
        this.boxList = boxList;
        this.resource = resource;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return boxList.size();
    }

    @Override
    public Object getItem(int posintion) {
        return boxList.get(posintion);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        TextView ylboxorder = null;
        TextView ylboxid = null;
        TextView ylboxTradeAction = null;
        TextView ylboxStatus = null;
        TextView ylboxType = null;
        TextView ylboxcount = null;

        if (convertView == null){
            convertView = inflater.inflate(resource,null);
            ylboxorder = (TextView)convertView.findViewById(R.id.boxlv_tv_order);
            ylboxid = (TextView)convertView.findViewById(R.id.boxlv_tv_Boxid);
            ylboxTradeAction = (TextView)convertView.findViewById(R.id.boxlv_tv_TradeAction);
            ylboxStatus = (TextView)convertView.findViewById(R.id.boxlv_tv_Status);
            ylboxType = (TextView)convertView.findViewById(R.id.boxlv_tv_type);
            ylboxcount = (TextView)convertView.findViewById(R.id.boxlv_tv_count);
            ViewCache viewCache = new ViewCache();
            viewCache.ylboxorderview = ylboxorder;
            viewCache.ylboxidview = ylboxid;
            viewCache.ylboxTradeActionview = ylboxTradeAction;
            viewCache.ylboxStatusview = ylboxStatus;
            viewCache.ylboxTypeview = ylboxType;
            viewCache.ylboxcountview = ylboxcount;
            convertView.setTag(viewCache);
        }else {
            ViewCache viewCache = (ViewCache)convertView.getTag();
            ylboxorder = viewCache.ylboxorderview;
            ylboxid = viewCache.ylboxidview;
            ylboxTradeAction = viewCache.ylboxTradeActionview;
            ylboxStatus = viewCache.ylboxStatusview;
            ylboxType = viewCache.ylboxTypeview;
            ylboxcount = viewCache.ylboxcountview;
        }
        Box box = boxList.get(position);
        ylboxorder.setText(box.boxorder);
        ylboxid.setText(box.BoxID);
        ylboxTradeAction.setText(box.TradeAction);
        ylboxStatus.setText(box.BoxStatus);
        ylboxType.setText(box.BoxType);
        ylboxcount.setText(box.boxcount);

        return convertView;
    }

    private final class ViewCache{
        public TextView ylboxorderview;
        public TextView ylboxidview ;
        public TextView ylboxTradeActionview ;
        public TextView ylboxStatusview ;
        public TextView ylboxTypeview ;
        public TextView ylboxcountview ;

    }
}
