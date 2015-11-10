package YLAdapter;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import TaskClass.YLTask;
import ylescort.ylmobileandroid.R;
/**
 * Created by Administrator on 2015/4/21.
 */
public class YLTaskAdapter extends BaseAdapter {

    private List<YLTask> ylTaskList;
    private int resource;
    private LayoutInflater inflater;

    public YLTaskAdapter(Context context,List<YLTask> ylTaskList,int resource){
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
        TextView taskType = null;
        TextView taskState = null;
        if (convertView == null){
            convertView= inflater.inflate(resource,null);
            taskName = (TextView) convertView.findViewById(R.id.Task_taskname);
            taskType = (TextView) convertView.findViewById(R.id.Task_taskstype);
            taskState = (TextView) convertView.findViewById(R.id.Task_taskstaut);
            ViewCache viewCache = new ViewCache();
            viewCache.taskNameView = taskName;
            viewCache.taskTypeView = taskType;
            viewCache.taskStateView = taskState;
            convertView.setTag(viewCache);
        }else {
            ViewCache viewCache = (ViewCache) convertView.getTag();
            taskName = viewCache.taskNameView;
            taskType = viewCache.taskTypeView;
            taskState = viewCache.taskStateView;
        }
        YLTask ylTask = ylTaskList.get(position);
        taskName.setText(ylTask.getLine());
        taskType.setText(ylTask.getTaskType());
        taskState.setText(ylTask.getTaskState());

        if (ylTask.getTaskType() != null) {
            switch (ylTask.getTaskType()) {
                case "早送":
                    convertView.setBackgroundColor(-5383962);
                    break;
                case "晚收":
                    convertView.setBackgroundColor(-13210);
                    break;
                default:
                    convertView.setBackgroundColor(Color.TRANSPARENT);
                    break;
            }
        }

        return convertView;
    }

    private final class ViewCache{
        public TextView taskNameView;
        public TextView taskTypeView;
        public TextView taskStateView;
    }

}
