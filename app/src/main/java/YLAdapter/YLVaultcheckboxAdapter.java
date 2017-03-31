package YLAdapter;

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
 * Created by Administrator on 2015/5/12.
 */
public class YLVaultcheckboxAdapter extends BaseAdapter {

    private List<Box> boxList;
    private int resource;
    private LayoutInflater inflater;

    public YLVaultcheckboxAdapter(Context context,List<Box> boxList,int resource){
        this.boxList = boxList;
        this.resource = resource;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return boxList.size();
    }

    @Override
    public Object getItem(int position) {
        return boxList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView ylboxname = null;
        if (convertView == null){
            convertView = inflater.inflate(resource,null);
            ylboxname = (TextView) convertView.findViewById(R.id.vault_check_ylboxitem_tv_name);
            ViewCache viewCache = new ViewCache();
            viewCache.ylboxnameview = ylboxname;
            convertView.setTag(viewCache);
        }else {
            ViewCache viewCache = (ViewCache) convertView.getTag();
            ylboxname = viewCache.ylboxnameview;
        }
        Box box = boxList.get(position);
        ylboxname.setText(box.getBoxName());
        return convertView;
    }

    private final class ViewCache{
        public TextView ylboxnameview;
    }

}
