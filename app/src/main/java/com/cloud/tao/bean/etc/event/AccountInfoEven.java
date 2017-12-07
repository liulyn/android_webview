package com.cloud.tao.bean.etc.event;

/**
 * sunny created at 2016/11/2
 * des: 用户信息修改通知
 */
public class AccountInfoEven {

    public AccountInfoEven(String nickName){
        this.nickName=nickName;
    }

    public String nickName;
}
