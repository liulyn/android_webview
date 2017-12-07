package com.cloud.tao.bean.etc;

/**
 * Created by gezi-pc on 2016/10/23.
 */

public class OrderMessage {
    public String leave_message_id;
    public int type;
    public String type_name;
    public String message;
    public String is_new;
    public Long create_time;

    @Override
    public String toString() {
        return "OrderMessage{" +
                "leave_message_id='" + leave_message_id + '\'' +
                ", type='" + type + '\'' +
                ", type_name='" + type_name + '\'' +
                ", message='" + message + '\'' +
                ", is_new='" + is_new + '\'' +
                ", create_time='" + create_time + '\'' +
                '}';
    }
}
