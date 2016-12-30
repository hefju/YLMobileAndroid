package YLAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.List;
import TaskClass.OverWeightBox;
import ylescort.ylmobileandroid.R;

/**
 * Created by Administrator on 2016-12-28.
 */

public class YLOverweightAdapter extends BaseAdapter {
    private List<OverWeightBox> overweightList;
    private int resource;
    private LayoutInflater inflater;

    public YLOverweightAdapter(Context context, List<OverWeightBox> overweights, int resource){
        this.overweightList = overweights;
        this.resource = resource;
        inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return overweightList.size();
    }

    @Override
    public Object getItem(int position) {
        return overweightList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView Overweightorderview = null;
        TextView Overweightnameview = null;
        TextView Overweightweightview = null;

        if (convertView == null)
        {
            convertView = inflater.inflate(resource,null);
            Overweightorderview = (TextView) convertView.findViewById(R.id.overweight_listitem_order);
            Overweightnameview = (TextView) convertView.findViewById(R.id.overweight_listitem_boxname);
            Overweightweightview = (TextView) convertView.findViewById(R.id.overweight_listitem_weight);
            YLOverweightAdapter.ViewCache viewCache = new YLOverweightAdapter.ViewCache();
            viewCache.Overweightorderview = Overweightorderview;
            viewCache.Overweightnameview = Overweightnameview;
            viewCache.Overweightweightview = Overweightweightview;
            convertView.setTag(viewCache);
        }else {
            YLOverweightAdapter.ViewCache viewCache = (YLOverweightAdapter.ViewCache) convertView.getTag();
            Overweightorderview = viewCache.Overweightorderview;
            Overweightnameview  = viewCache.Overweightnameview;
            Overweightweightview = viewCache.Overweightweightview;
        }

        OverWeightBox overweight = overweightList.get(position);
        Overweightorderview.setText(overweight.getServerReturn());
        Overweightnameview.setText(overweight.getBoxName());
        Overweightweightview.setText(overweight.getBoxWeight());

        return convertView;
    }

    private final class ViewCache{
        public TextView Overweightorderview;
        public TextView Overweightnameview;
        public TextView Overweightweightview;
    }


}
