package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import TaskClass.Box;

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
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }
}
