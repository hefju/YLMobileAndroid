package TaskClass;

/**
 * Created by Administrator on 2015/3/10 0010.
 */
public class Vision {
    public String ServerReturn ;//服务器返回的成功与否，成功1，其他异常是e.toString().
    public String vision ;//版本号

    public String getVision() {
        return vision;
    }

    public void setVision(String vision) {
        this.vision = vision;
    }

    public String getServerReturn() {
        return ServerReturn;
    }

    public void setServerReturn(String serverReturn) {
        ServerReturn = serverReturn;
    }
}
