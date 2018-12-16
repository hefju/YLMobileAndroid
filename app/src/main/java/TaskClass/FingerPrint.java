package TaskClass;

/**
 * Created by Administrator on 2018/12/15.
 */

public class FingerPrint {

    private int Id;
    private String ServerReturn;//服务器返回的成功与否，成功1
    private String EmpNum ;//员工编号
    private String Finger ;//指纹字符串
    private long CreateAt ;//新建时间



    @Override
    public String toString() {
        return "FingerPrint{" +
                "Id=" + Id +
                ", EmpNum='" + EmpNum + '\'' +
                ", CreateAt=" + CreateAt +
                ", Finger='" + Finger + '\'' +
                ", ServerReturn='" + ServerReturn + '\'' +
                '}';
    }
    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getServerReturn() {
        return ServerReturn;
    }

    public void setServerReturn(String serverReturn) {
        ServerReturn = serverReturn;
    }

    public String getEmpNum() {
        return EmpNum;
    }

    public void setEmpNum(String empNum) {
        EmpNum = empNum;
    }

    public String getFinger() {
        return Finger;
    }

    public void setFinger(String finger) {
        Finger = finger;
    }

    public long getCreateAt() {
        return CreateAt;
    }

    public void setCreateAt(long createAt) {
        CreateAt = createAt;
    }



}
