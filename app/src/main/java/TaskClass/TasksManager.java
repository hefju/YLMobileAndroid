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
    public ArrayList<Task> lstLatestTask;//最新的任务

    public TasksManager(){
        CurrentTask=null;
        lstAllTask=new ArrayList<>();
        lstLatestTask=new ArrayList<>();
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
    public boolean IsExistsTask(String taskID)
    {
        for(Task x:lstAllTask)
        {
            if(x.getTaskID().equals(taskID)) {
                return true;
            }
        }
        return false;
    }

    public void SetCurrentTask(String taskID)//设置当前运行的任务，将lstAllTask类中的对应taskID的任务装载到currentTask类里，currentTask是真正做了的任务列表。
    {
        CurrentTask = GetTaskByID(taskID);
    }

    //从所有任务中查找指定的任务ID
    private Task GetTaskByID(String taskID)
    {
        for(Task x:lstLatestTask)
        {
            if(x.getTaskID().equals(taskID)) {
                return x;
            }
        }
        return null;
    }

    public void DownloadTaskByID(String taskID){
        Task t =new Task();
        this.TaskDate = t.getTaskDate();
        UpdateLatestTask(t);//将旧版本的信息更新到新版本

        lstAllTask.add(t);  //总列表添加远程下载的任务----------全部任务的列表-下载10次就有10个任务
        lstLatestTask.add(t);//最新列表添加远程下载的任务------最后合并完的任务列表
    }

    private void UpdateLatestTask(Task t) {
        //检查本地是否已经存在任务
        if (IsExistsTask(t.getTaskID()))
        {
            //在最新列表里面找到任务的版本, 更新远程下载的任务版本, 并在最新列表删除任务
            Task lastTask = GetTaskByID(t.getTaskID());
            if (lastTask != null)
            {
                t.setTaskVersion(String.valueOf(Integer.parseInt(t.getTaskVersion()) + 1));
                UpdateLatestTask(t, lastTask);//将旧版本的信息更新到新版本
                //删除lstLatestTask里的旧任务，最后插入最新版的同一TaskId的任务。
                lstLatestTask.remove(lastTask);
            }
        }
        else//此任务是第一次下载.
            t.setTaskVersion("1");
    }

    private void UpdateLatestTask(Task task_new, Task task_local) {
        //更新网点,
        for(Site site_local:task_local.lstSite)
        {
            Site site_new =FindSiteByID(site_local.SiteID,task_new);

            if (site_new != null)//新任务存在这个网点就更新
            {
                site_new.Status = site_local.Status;
                site_new.lstArriveTime = site_local.lstArriveTime;
            }
            else//新任务不存在这个网点就添加
            {
                if (site_local.Status == "已交接")
                    task_new.lstSite.add(site_local);
            }
        }

        //由于目前没有下载箱子, 所以全部添加
       task_new.lstBox = task_local.lstBox;
    }

    private Site FindSiteByID(String siteID,Task t){
        for (Site x:t.lstSite){
            if(x.SiteID.equals(siteID))
                return x;
        }
        return null;
    }

    // 记录ATM数
    public void SetATMCount(String strSiteID, String tempCount)
    {
        for(Site x:CurrentTask.lstSite) {
            if(x.SiteID.equals(strSiteID)){
                x.ATMCount=tempCount;
                break;
            }
        }
    }

    // 存到达时间到列表里。
    public void SetArriveTime(String strSiteID)
    {
        for(Site x:CurrentTask.lstSite) {
            if(x.SiteID.equals(strSiteID)){

                ArriveTime s = new ArriveTime();
                s.ATime =(new java.util.Date()).toString();
                s.EmpID ="";//YLSystem.EmpID;
                x.lstArriveTime.add(s);
                break;
            }
        }

    }


    //从本地查找任务
//    public Task GetTaskList()
//    {
//
//        Task task=new Task();
//        return task;
//    }

//    public ArrayList<Site> GetTask()
//    {
//
//    }

}
