package YLAdapter;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import TaskClass.Box;
import ylescort.ylmobileandroid.R;
/**
 * Created by Administrator on 2015/4/21.
 */
public class YLBoxEdiAdapter extends BaseAdapter {
    private List<Box> boxList;
    private  int resource;
    private LayoutInflater inflater;
    private int selectItem = 0;
    public YLBoxEdiAdapter(Context context,List<Box> boxList,int resource){
        this.boxList = boxList;
        this.resource = resource;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getSelectItem() {
        return selectItem;
    }

    public void setSelectItem(int selectItem) {
        this.selectItem = selectItem;
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
        TextView ylboxnameorid = null;
        TextView ylboxTradeAction = null;
        TextView ylboxStatus = null;
        TextView ylboxType = null;
        TextView ylboxcount = null;
        TextView ylboxTaskType = null;

        if (convertView == null){
            convertView = inflater.inflate(resource,null);
            ylboxorder = (TextView)convertView.findViewById(R.id.boxediitem_tv_order);
            ylboxnameorid = (TextView)convertView.findViewById(R.id.boxediitem_tv_name);
            ylboxTradeAction = (TextView)convertView.findViewById(R.id.boxediitem_tv_gog);
            ylboxStatus = (TextView)convertView.findViewById(R.id.boxediitem_tv_eof);
            ylboxType = (TextView)convertView.findViewById(R.id.boxediitem_tv_boxtype);
            ylboxcount = (TextView)convertView.findViewById(R.id.boxediitem_tv_count);
            ylboxTaskType= (TextView)convertView.findViewById(R.id.boxediitem_tv_tasktype);
            ViewCache viewCache = new ViewCache();
            viewCache.ylboxorderview = ylboxorder;
            viewCache.ylboxidview = ylboxnameorid;
            viewCache.ylboxTradeActionview = ylboxTradeAction;
            viewCache.ylboxStatusview = ylboxStatus;
            viewCache.ylboxTypeview = ylboxType;
            viewCache.ylboxcountview = ylboxcount;
            viewCache.ylboxTaskTypeview = ylboxTaskType;
            convertView.setTag(viewCache);
        }else {
            ViewCache viewCache = (ViewCache)convertView.getTag();
            ylboxorder = viewCache.ylboxorderview;
            ylboxnameorid = viewCache.ylboxidview;
            ylboxTradeAction = viewCache.ylboxTradeActionview;
            ylboxStatus = viewCache.ylboxStatusview;
            ylboxType = viewCache.ylboxTypeview;
            ylboxcount = viewCache.ylboxcountview;
            ylboxTaskType = viewCache.ylboxTaskTypeview;
        }
        Box box = boxList.get(position);
        ylboxorder.setText(box.BoxOrder);
        ylboxnameorid.setText(box.BoxName);
        ylboxTradeAction.setText(box.TradeAction);
        ylboxStatus.setText(box.BoxStatus);
        ylboxType.setText(box.BoxType);
        ylboxcount.setText(box.BoxCount);
        ylboxTaskType.setText(box.BoxTaskType);

        if(position == selectItem){
            convertView.setBackgroundColor(-5383962);
        } else {
            convertView.setBackgroundColor(Color.TRANSPARENT);
        }


        return convertView;
    }

    private final class ViewCache{
        public TextView ylboxorderview;
        public TextView ylboxidview ;
        public TextView ylboxTradeActionview ;
        public TextView ylboxStatusview ;
        public TextView ylboxTypeview ;
        public TextView ylboxcountview ;
        public TextView ylboxTaskTypeview ;

    }
}
