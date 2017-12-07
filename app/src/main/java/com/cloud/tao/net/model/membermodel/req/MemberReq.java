package com.cloud.tao.net.model.membermodel.req;

import com.cloud.tao.net.base.BaseReq;

import java.io.Serializable;

/**
 * Created by gezi-pc on 2016/10/9.
 */

public class MemberReq extends BaseReq implements Serializable{


    private static final long serialVersionUID = 5763538063826702213L;

    public String private_name;//名称
    public String private_mobilephone;//手机号
    public short private_sex;//性别
    public String private_province;//省
    public String private_city;//市
    public String private_district;//区
    public Long private_birthday;//生日
    public String private_weixin;//微信号
    public String private_headimgurl;

    public MemberReq(String private_name, String private_mobilephone, short private_sex, String private_province, String private_city, Long private_birthday, String private_weixin) {
        this.private_name = private_name;
        this.private_mobilephone = private_mobilephone;
        this.private_sex = private_sex;
        this.private_province = private_province;
        this.private_city = private_city;
        this.private_birthday = private_birthday;
        this.private_weixin = private_weixin;
    }

    public MemberReq() {
    }

    public String getPrivate_weixin() {
        return private_weixin;
    }

    public void setPrivate_weixin(String private_weixin) {
        this.private_weixin = private_weixin;
    }

    public Long getPrivate_birthday() {
        return private_birthday;
    }

    public void setPrivate_birthday(Long private_birthday) {
        this.private_birthday = private_birthday;
    }

    public String getPrivate_city() {
        return private_city;
    }

    public void setPrivate_city(String private_city) {
        this.private_city = private_city;
    }

    public String getPrivate_province() {
        return private_province;
    }

    public void setPrivate_province(String private_province) {
        this.private_province = private_province;
    }

    public short getPrivate_sex() {
        return private_sex;
    }

    public void setPrivate_sex(short private_sex) {
        this.private_sex = private_sex;
    }

    public String getPrivate_mobilephone() {
        return private_mobilephone;
    }

    public void setPrivate_mobilephone(String private_mobilephone) {
        this.private_mobilephone = private_mobilephone;
    }

    public String getPrivate_name() {
        return private_name;
    }

    public void setPrivate_name(String private_name) {
        this.private_name = private_name;
    }

    public String getPrivate_district() {
        return private_district;
    }

    public void setPrivate_district(String private_district) {
        this.private_district = private_district;
    }

    public String getPrivate_headimgurl() {
        return private_headimgurl;
    }

    public void setPrivate_headimgurl(String private_headimgurl) {
        this.private_headimgurl = private_headimgurl;
    }
}
