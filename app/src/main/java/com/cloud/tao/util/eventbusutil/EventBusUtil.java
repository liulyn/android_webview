package com.cloud.tao.util.eventbusutil;
import org.greenrobot.eventbus.EventBus;

/**
 * Created by wyk on 2016/d/30.
 * 描述:EvnetBus的工具类
 * 作用:用于fragment之间的通信
 */
public class EventBusUtil {

    /** 注册*/
    public static void register(Object register){
        EventBus.getDefault().register(register);
    }
    /** 注销*/
    public static void unRegister(Object unRegister){
        EventBus.getDefault().unregister(unRegister);
    }

}
