package TaskClass;

/**
 * Created by Administrator on 2015/1/19.
 */
public class User {
    public String EmpID;
    public String EmpNO;
    public String Pass;
    public String Name;
    public String DeviceID;
    public String ISWIFI;//这个不是一个单纯的user包含了其他内容
    public String Time;
    public String ServerReturn;

    public String getServerReturn() {
        return ServerReturn;
    }

    public void setServerReturn(String serverReturn) {
        ServerReturn = serverReturn;
    }

    public User(){};

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
                '}';
    }
}
