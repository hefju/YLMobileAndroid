package TaskClass;

/**
 * Created by Administrator on 2015/1/19.
 */
public class User {
    public String EmpID;//ID---服务器返回的值
    public String EmpNO;//NO---登陆的时候填的
    public String Pass;//密码---登陆的时候填的
    public String Name;//名字---服务器返回的值
    public String DeviceID;//手持机号---登陆的时候填的
    public String ISWIFI;//是否在用wifi--登陆的时候填的
    public String Time;//时间---服务器返回的值
    public String ServerReturn;//是否访问成功，成功返回1---服务器返回的值
    public String TaskDate;//获取当前日期的任务用到的日期---任务界面的时候填的
    public String EmpHFNo;//HF卡号

    public String getEmpHFNo() {
        return EmpHFNo;
    }

    public void setEmpHFNo(String empHFNo) {
        EmpHFNo = empHFNo;
    }

    public String getTaskDate() {
        return TaskDate;
    }

    public void setTaskDate(String taskDate) {
        TaskDate = taskDate;
    }

    public String getServerReturn() {
        return ServerReturn;
    }

    public void setServerReturn(String serverReturn) {
        ServerReturn = serverReturn;
    }

    public User(){}

    public String getEmpID() {
        return EmpID;
    }

    public void setEmpID(String empID) {
        EmpID = empID;
    }

    public String getEmpNO() {
        return EmpNO;
    }

    public void setEmpNO(String empNO) {
        EmpNO = empNO;
    }

    public String getPass() {
        return Pass;
    }

    public void setPass(String pass) {
        Pass = pass;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDeviceID() {
        return DeviceID;
    }

    public void setDeviceID(String deviceID) {
        DeviceID = deviceID;
    }

    public String getISWIFI() {
        return ISWIFI;
    }

    public void setISWIFI(String ISWIFI) {
        this.ISWIFI = ISWIFI;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    @Override
    public String toString() {
        return "User{" +
                "EmpID='" + EmpID + '\'' +
                ", EmpNO='" + EmpNO + '\'' +
                ", Pass='" + Pass + '\'' +
                ", Name='" + Name + '\'' +
                ", DeviceID='" + DeviceID + '\'' +
                ", ISWIFI='" + ISWIFI + '\'' +
                ", Time='" + Time + '\'' +
                ", ServerReturn='" + ServerReturn + '\'' +
                ", TaskDate='" + TaskDate + '\'' +
                '}';
    }
}
