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
 * Created by Administrator on 2015/4/30.
 */
public class YLValutboxitemAdapter extends BaseAdapter {

    private List<Box> boxList;
    private  int resource;
    private LayoutInflater inflater;
    private int selectItem = 0;
    public YLValutboxitemAdapter(Context context,List<Box> boxList,int resource){
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
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView ylboxnameorid = null;
        TextView ylboxStatus = null;
        TextView ylboxType = null;
        TextView ylboxTaskType = null;
        TextView ylboxcheck = null;
        if (convertView == null){
            convertView = inflater.inflate(resource,null);
            ylboxnameorid = (TextView)convertView.findViewById(R.id.vault_in_detail_boxitme_boxname);
            ylboxStatus = (TextView)convertView.findViewById(R.id.vault_in_detail_boxitme_boxstaut);
            ylboxType = (TextView)convertView.findViewById(R.id.vault_in_detail_boxitme_boxtype);
            ylboxTaskType = (TextView)convertView.findViewById(R.id.vault_in_detail_boxitme_boxtask);
            ylboxcheck= (TextView)convertView.findViewById(R.id.vault_in_detail_boxitme_boxcheck);
            ViewCache viewCache = new ViewCache();
            viewCache.ylboxidview = ylboxnameorid;
            viewCache.ylboxStatusview = ylboxStatus;
            viewCache.ylboxTypeview = ylboxType;
            viewCache.ylboxcheckview = ylboxcheck;
            viewCache.ylboxTaskTypeview = ylboxTaskType;
            convertView.setTag(viewCache);
        }else {
            ViewCache viewCache = (ViewCache)convertView.getTag();
            ylboxnameorid = viewCache.ylboxidview;
            ylboxStatus = viewCache.ylboxStatusview;
            ylboxType = viewCache.ylboxTypeview;
            ylboxcheck = viewCache.ylboxcheckview;
            ylboxTaskType = viewCache.ylboxTaskTypeview;
        }
        Box box = boxList.get(position);
        ylboxnameorid.setText(box.getBoxName());
        ylboxStatus.setText(box.getBoxStatus());
        ylboxType.setText(box.getBoxType());
        ylboxcheck.setText(box.getValutcheck());
        ylboxTaskType.setText(box.getBoxTaskType());

        if (box.getValutcheck()!= null){
            switch (box.getValutcheck()){
                case "对":convertView.setBackgroundColor(-5383962);
                    break;
                case "多":convertView.setBackgroundColor(-13210);
                    break;
                case "核":convertView.setBackgroundColor(-5374161);
                    break;
                default:convertView.setBackgroundColor(Color.TRANSPARENT);
                    break;
            }
        }else {
            convertView.setBackgroundColor(Color.TRANSPARENT);
        }

        return convertView;
    }

    private final class ViewCache{
        public TextView ylboxidview ;
        public TextView ylboxStatusview ;
        public TextView ylboxTypeview ;
        public TextView ylboxcheckview;
        public TextView ylboxTaskTypeview ;

    }
}
