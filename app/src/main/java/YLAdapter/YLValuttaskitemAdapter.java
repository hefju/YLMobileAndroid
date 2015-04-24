package YLAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import TaskClass.YLTask;
import ylescort.ylmobileandroid.R;

/**
 * Created by Administrator on 2015/4/24.
 */
public class YLValuttaskitemAdapter extends BaseAdapter {

    private List<YLTask> ylTaskList;
    private int resource;
    private LayoutInflater inflater;

    public YLValuttaskitemAdapter(Context context, List<YLTask> ylTaskList, int resource){
        this.ylTaskList = ylTaskList;
        this.resource = resource;
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return ylTaskList.size();
    }

    @Override
    public Object getItem(int position) {
        return ylTaskList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView taskName = null;
        TextView handoverman = null;
        TextView taskState = null;
        if (convertView == null){
            convertView= inflater.inflate(resource,null);
            taskName = (TextView) convertView.findViewById(R.id.vault_in_operate_taskitme_taskname);
            handoverman = (TextView) convertView.findViewById(R.id.vault_in_operate_taskitme_handoverman);
            taskState = (TextView) convertView.findViewById(R.id.vault_in_operate_taskitme_taskstaut);
            ViewCache viewCache = new ViewCache();
            viewCache.taskNameView = taskName;
            viewCache.handoverman = handoverman;
            viewCache.taskStateView = taskState;
            convertView.setTag(viewCache);
        }else {
            ViewCache viewCache = (ViewCache) convertView.getTag();
            taskName = viewCache.taskNameView;
            handoverman = viewCache.handoverman;
            taskState = viewCache.taskStateView;
        }
        YLTask ylTask = ylTaskList.get(position);
        taskName.setText(ylTask.getLine());
        handoverman.setText(ylTask.getTaskManager());
        taskState.setText(ylTask.getTaskState());
        return convertView;
    }

    private final class ViewCache{
        public TextView taskNameView;
        public TextView handoverman;
        public TextView taskStateView;
    }
}
