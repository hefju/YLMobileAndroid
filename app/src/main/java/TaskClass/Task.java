package TaskClass;

import java.util.List;

/**
 * Created by Administrator on 2015/1/19.
 */
public class Task {
    private Integer id;
    public String ServerReturn ;//服务器返回的成功与否，成功1，其他异常是e.toString().
    private String ServerVersion;//服务器任务版本.
    private String TaskVersion ;//任务版本.
    private String TaskID ;//任务编后 是ID吗?
    private String TaskType ;//任务类型
    private String Handset ;//手持机
    private String TaskDate ;//任务日期
    private String Line ;//线路
    private String TaskManager ;//任务负责人
    private String TaskATMBeginTime  ;//任务负责人卡号
    private String TaskATMEndTime  ;//ATM开始任务时间
    private String TaskManagerNo ;//ATM完成任务时间
    public List<Site> lstSite;
    public List<Box> lstBox;


    public Task(){}

    public Task(Integer id,String ServerVersion,String TaskVersion, String TaskID, String TaskType,
                String Handset,String TaskDate, String Line,String TaskManager,String TaskATMBeginTime,
                String TaskATMEndTime, String TaskManagerNo )
    {
        this.id = id;
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

    public Task(String ServerVersion,String TaskVersion, String TaskID, String TaskType,
                String Handset,String TaskDate, String Line,String TaskManager,String TaskATMBeginTime,
                String TaskATMEndTime, String TaskManagerNo )
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
}
