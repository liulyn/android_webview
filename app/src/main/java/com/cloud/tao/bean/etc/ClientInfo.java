package com.cloud.tao.bean.etc;

/**
 * Created by gezi-pc on 2016/11/2.
 */

public class ClientInfo {

    public PersonInfo client_info;

    public class PersonInfo{
        public String private_name;//名称
        public String nick_name;
        public String private_mobilephone;//手机号
        public int private_sex;//性别
        public String private_province;//省
        public String private_city;//市
        public String private_district;//区
        public Long private_birthday;//生日
        public String private_weixin;//微信号
        public String private_headimgurl;
        public int client_id;
    }
}
