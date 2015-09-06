package TaskClass;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/1/19.
 */
public class YLTask {
    public Integer Id;
    public String ServerVersion;//服务器任务版本.
    public String TaskVersion ;//任务版本.
    public String TaskID ;//任务编后 是ID吗?
    public String TaskType ;//任务类型
    public String Handset ;//手持机
    public String TaskDate ;//任务日期
    public String Line ;//线路
    public String TaskManager ;//任务负责人
    public String TaskATMBeginTime  ;//任务负责人卡号
    public String TaskATMEndTime  ;//ATM开始任务时间
    public String TaskManagerNo ;//ATM完成任务时间
    public String ServerReturn;
    public String TaskState;//任务状态

    public List<Site> lstSite;
    public List<Box> lstBox;
    public List<YLATM> lstATM;
    public List<Box> lstCarBox;//车内箱类列表

    public List<Box> getLstCarBox() {
        return lstCarBox;
    }

    public void setLstCarBox(List<Box> lstCarBox) {
        this.lstCarBox = lstCarBox;
    }

    //public List<Site> lstDeleteSite;//被删除的网点(包含atm)

    public List<YLATM> getLstATM() {
        return lstATM;
    }

    public void setLstATM(List<YLATM> lstATM) {
        this.lstATM = lstATM;
    }

    public List<Site> getLstSite() {
        return lstSite;
    }

    public void setLstSite(List<Site> lstSite) {
        this.lstSite = lstSite;
    }

    public YLTask(){

        //lstDeleteSite=new ArrayList<>();
    }

    public YLTask(Integer id, String ServerVersion, String TaskVersion, String TaskID, String TaskType,
                  String Handset, String TaskDate, String Line, String TaskManager, String TaskATMBeginTime,
                  String TaskATMEndTime, String TaskManagerNo)
    {
        this.Id = id;
        this.ServerVersion = ServerVersion;
        this.TaskVersion=TaskVersion;
        this. TaskID=TaskID;
        this. TaskType=TaskType;
        this. Handset=Handset;
        this. TaskDate=TaskDate;
        this. Line=Line;
        this. TaskManager=TaskManager;
        this. TaskATMBeginTime=TaskATMBeginTime;
        this. TaskATMEndTime=TaskATMEndTime;
        this. TaskManagerNo=TaskManagerNo;
    }

    public YLTask(String ServerVersion, String TaskVersion, String TaskID, String TaskType,
                  String Handset, String TaskDate, String Line, String TaskManager, String TaskATMBeginTime,
                  String TaskATMEndTime, String TaskManagerNo)
    {
        this.ServerVersion = ServerVersion;
        this.TaskVersion=TaskVersion;
        this. TaskID=TaskID;
        this. TaskType=TaskType;
        this. Handset=Handset;
        this. TaskDate=TaskDate;
        this. Line=Line;
        this. TaskManager=TaskManager;
        this. TaskATMBeginTime=TaskATMBeginTime;
        this. TaskATMEndTime=TaskATMEndTime;
        this. TaskManagerNo=TaskManagerNo;
    }

    public List<Box> getLstBox() {
        return lstBox;
    }

    public void setLstBox(List<Box> lstBox) {
        this.lstBox = lstBox;
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        this.Id = id;
    }

    public String getServerReturn() {
        return ServerReturn;
    }

    public void setServerReturn(String serverReturn) {
        ServerReturn = serverReturn;
    }

    public String getTaskManagerNo() {
        return TaskManagerNo;
    }

    public void setTaskManagerNo(String taskManagerNo) {
        TaskManagerNo = taskManagerNo;
    }

    public String getTaskATMEndTime() {
        return TaskATMEndTime;
    }

    public void setTaskATMEndTime(String taskATMEndTime) {
        TaskATMEndTime = taskATMEndTime;
    }

    public String getTaskATMBeginTime() {
        return TaskATMBeginTime;
    }

    public void setTaskATMBeginTime(String taskATMBeginTime) {
        TaskATMBeginTime = taskATMBeginTime;
    }

    public String getTaskManager() {
        return TaskManager;
    }

    public void setTaskManager(String taskManager) {
        TaskManager = taskManager;
    }

    public String getLine() {
        return Line;
    }

    public void setLine(String line) {
        Line = line;
    }

    public String getTaskDate() {
        return TaskDate;
    }

    public void setTaskDate(String taskDate) {
        TaskDate = taskDate;
    }

    public String getHandset() {
        return Handset;
    }

    public void setHandset(String handset) {
        Handset = handset;
    }

    public String getTaskType() {
        return TaskType;
    }

    public void setTaskType(String taskType) {
        TaskType = taskType;
    }

    public String getTaskID() {
        return TaskID;
    }

    public void setTaskID(String taskID) {
        TaskID = taskID;
    }

    public String getTaskVersion() {
        return TaskVersion;
    }

    public void setTaskVersion(String taskVersion) {
        TaskVersion = taskVersion;
    }

    public String getServerVersion() {
        return ServerVersion;
    }

    public void setServerVersion(String serverVersion) {
        ServerVersion = serverVersion;
    }

    public String getTaskState() {
        return TaskState;
    }

    public void setTaskState(String taskState) {
        TaskState = taskState;
    }

    @Override
    public String toString() {
        return "YLTask{" +
                "Id=" + Id +
                ", ServerVersion='" + ServerVersion + '\'' +
                ", TaskVersion='" + TaskVersion + '\'' +
                ", TaskID='" + TaskID + '\'' +
                ", TaskType='" + TaskType + '\'' +
                ", Handset='" + Handset + '\'' +
                ", TaskDate='" + TaskDate + '\'' +
                ", Line='" + Line + '\'' +
                ", TaskManager='" + TaskManager + '\'' +
                ", TaskATMBeginTime='" + TaskATMBeginTime + '\'' +
                ", TaskATMEndTime='" + TaskATMEndTime + '\'' +
                ", TaskManagerNo='" + TaskManagerNo + '\'' +
                ", ServerReturn='" + ServerReturn + '\'' +
                ", TaskState='" + TaskState + '\'' +
                ", lstSite=" + lstSite +
                ", lstBox=" + lstBox +
                ", lstATM=" + lstATM +
                '}';
    }
}
