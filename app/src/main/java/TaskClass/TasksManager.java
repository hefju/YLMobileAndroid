package TaskClass;

//import java.sql.Date;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import YLDataService.TasksManagerDBSer;

/**
 * Created by asus on 2015/1/29.
 */
public class TasksManager {
    public String TaskDate;//任务日期
    public YLTask CurrentTask;//当前执行的任务
   // public ArrayList<YLTask> lstAllTask;//全部任务列表
    public ArrayList<YLTask> lstLatestTask;//最新的任务

    public ArrayList<YLTask> getLstLatestTask() {
        return lstLatestTask;
    }

    public void setLstLatestTask(ArrayList<YLTask> lstLatestTask) {
        this.lstLatestTask = lstLatestTask;
    }

    public YLTask getCurrentTask() {
        return CurrentTask;
    }

    public void setCurrentTask(YLTask currentTask) {
        CurrentTask = currentTask;
    }

    public String getTaskDate() {
        return TaskDate;
    }

    public void setTaskDate(String taskDate) {
        TaskDate = taskDate;
    }

    public TasksManager(){
        TaskDate="";
       // CurrentTask=null;
      //  lstAllTask=new ArrayList<>();
      //  lstLatestTask=new ArrayList<>();
    }

    //TasksManager获取指定日期的数据
    public void Loading(Context context,String taskDate){
        if(TaskDate!=taskDate){
            TaskDate=taskDate;
            InitFormLoacl(context,taskDate);
        }
    }
    //合并任务, (合并之前会从本地加载指定日期的任务)
    public void Merge(String taskDate,List<YLTask> lst){
       // TaskDate=taskDate;
      //  CurrentTask=null;
        //lstLatestTask=( ArrayList<YLTask>)lst;
    }
    public  void  SaveTask(Context context){
        if(TaskDate.length()<1||lstLatestTask.size()<1)
            return;
        TasksManagerDBSer tasksManagerDBSer=new TasksManagerDBSer(context);
        tasksManagerDBSer.SaveTasksManager(this);//保存
    }

    //从本地获取数据初始化TasksManager
    public void InitFormLoacl(Context context,String taskDate){
        TasksManagerDBSer tasksManagerDBSer=new TasksManagerDBSer(context);
        TasksManager tmp=  tasksManagerDBSer.GetTasksManager(taskDate);
        //Log.d("jutest","InitFormLoacl:"+tmp.TaskDate);
        if(tmp!=null){
            CurrentTask=null;
            lstLatestTask=tmp.lstLatestTask;
        }
    }

    // 检查是否已经存在任务
    public boolean IsExistsTask(String taskID)
    {
        for(YLTask x:lstLatestTask)
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
    private YLTask GetTaskByID(String taskID)
    {
        for(YLTask x:lstLatestTask)
        {
            if(x.getTaskID().equals(taskID)) {
                return x;
            }
        }
        return null;
    }

    public void DownloadTaskByID(String taskID){
        YLTask t =new YLTask();
        this.TaskDate = t.getTaskDate();
        UpdateLatestTask(t);//将旧版本的信息更新到新版本

       // lstAllTask.add(t);  //总列表添加远程下载的任务----------全部任务的列表-下载10次就有10个任务
        lstLatestTask.add(t);//最新列表添加远程下载的任务------最后合并完的任务列表
    }

    private void UpdateLatestTask(YLTask t) {
        //检查本地是否已经存在任务
        if (IsExistsTask(t.getTaskID()))
        {
            //在最新列表里面找到任务的版本, 更新远程下载的任务版本, 并在最新列表删除任务
            YLTask lastTask = GetTaskByID(t.getTaskID());
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

    private void UpdateLatestTask(YLTask task_new, YLTask task_local) {
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

    private Site FindSiteByID(String siteID,YLTask t){
        for (Site x:t.lstSite){
            if(x.SiteID.equals(siteID))
                return x;
        }
        return null;
    }

    private Site FindSiteByID(String siteID,List<Site> lstSite){
        for (Site x:lstSite){
            if(x.SiteID.equals(siteID))
                return x;
        }
        return null;
    }
    public void MergeSite(List<Site> lstSiteRemote){
        List<Site> lstSiteLocal=   CurrentTask.lstSite;
        if(lstSiteLocal!=null&&lstSiteLocal.size()>0)
        {
            for(Site site_local:lstSiteLocal)
            {
                Site site_remote =FindSiteByID(site_local.SiteID,lstSiteRemote);

                if (site_remote != null)//新任务存在这个网点就更新
                {
                    site_remote.Status = site_local.Status;
                    site_remote.lstArriveTime = site_local.lstArriveTime;
                }
                else//新任务不存在这个网点就添加
                {
                    if (site_local.Status == "已交接")
                        lstSiteRemote.add(site_local);
                }
            }
        }
        CurrentTask.lstSite=lstSiteRemote;
        CurrentTask.setTaskState("已下载");
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

    //从本地数据库加载数据
//    public boolean GetTaskListFromLoacl(String taskdate, Context ctx) {
//        TaskDBSer dbSer=new TaskDBSer(ctx);
//       List<YLTask> lst =dbSer.SelTaskbydatetolist(taskdate);
//      //  lstAllTask=lst.
////        public ArrayList<YLTask> lstAllTask;//全部任务列表
////        public ArrayList<YLTask> lstLatestTask;//最新的任务
//        return false;
//    }


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
