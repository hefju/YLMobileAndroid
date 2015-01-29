package TaskClass;

//import java.sql.Date;
import java.util.ArrayList;

/**
 * Created by asus on 2015/1/29.
 */
public class TasksManager {
    public String TaskDate;//任务日期
    public Task CurrentTask;//当前执行的任务
    public ArrayList<Task> lstAllTask;//全部任务列表
    public ArrayList<Task> lstLatesTask;//最新的任务

    public TasksManager(){
        CurrentTask=null;
        lstAllTask=new ArrayList<>();
        lstLatesTask=new ArrayList<>();
    }
    //从远程获取当前设备的任务列表
    public ArrayList<Task> DownTaskList()
    {
        String empid="";
        String deviceID="";
        String isWifi="";
        ArrayList<Task> lst=new ArrayList<Task>();
        return lst;
    }

    // 检查是否已经存在任务
    public boolean IsExistsTask(int taskID)
    {
        for(Task x:lstAllTask)
        {
            if(x.getId()==taskID) {
                return true;
            }
        }
        return false;
    }

    public void SetCurrentTask(int taskID)//设置当前运行的任务，将lstAllTask类中的对应taskID的任务装载到currentTask类里，currentTask是真正做了的任务列表。
    {
        CurrentTask = GetTaskByID(taskID);
    }

    private Task GetTaskByID(int taskID)
    {
        for(Task x:lstAllTask)
        {
            if(x.getId()==taskID) {
                return x;
            }
        }
        return null;
    }

    //从本地查找任务
    public Task GetTaskList()
    {

        Task task=new Task();
        return task;
    }

//    public ArrayList<Site> GetTask()
//    {
//
//    }

}
