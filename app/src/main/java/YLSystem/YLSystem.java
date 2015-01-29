package YLSystem;

import TaskClass.User;

/**
 * Created by asus on 2015/1/29.
 */
public class YLSystem {
    public static User user;//登录后的用户

    public void SetUser(User _user)//设置用户
    {
        user=_user;
    }
    public String GetDeviceID()
    {
        return "NH008";//todo 等待添加方法从本机数据库取数
    }

    public String GetISWIFI()
    {
        return "1";//todo 安卓如何获取wifi状态
    }
}
