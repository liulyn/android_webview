package com.cloud.tao.bean.etc;

import java.io.Serializable;

/**
 * sunny created at 2016/9/10/07
 * des: 账号用户基本信息
 */
public class AccountInfo implements Serializable {


    private static final long serialVersionUID = -2500039733076165421L;

    public AccountInfo(String login_mobilephone, String private_district, String private_phone, String nick_name, String private_mobilephone, String private_name, String private_city, String private_province, String private_detail_address, String login_name, String private_headimgurl, String private_email, String create_time, String client_id, String private_sex) {
        this.login_mobilephone = login_mobilephone;
        this.private_district = private_district;
        this.private_phone = private_phone;
        this.nick_name = nick_name;
        this.private_mobilephone = private_mobilephone;
        this.private_name = private_name;
        this.private_city = private_city;
        this.private_province = private_province;
        this.private_detail_address = private_detail_address;
        this.login_name = login_name;
        this.private_headimgurl = private_headimgurl;
        this.private_email = private_email;
        this.create_time = create_time;
        this.client_id = client_id;
        this.private_sex = private_sex;
    }

    public AccountInfo() {
    }

    public String login_mobilephone;
    public String private_district;
    public String private_phone;
    public String nick_name;
    public String private_mobilephone;
    public String private_name;
    public String private_city;
    public String private_province;
    public String private_detail_address;
    public String login_name;
    public String private_headimgurl;
    public String private_email;
    public String create_time;
    public String client_id;
    public String private_sex;

    public String getLogin_mobilephone() {
        return login_mobilephone;
    }

    public void setLogin_mobilephone(String login_mobilephone) {
        this.login_mobilephone = login_mobilephone;
    }

    public String getPrivate_district() {
        return private_district;
    }

    public void setPrivate_district(String private_district) {
        this.private_district = private_district;
    }

    public String getPrivate_phone() {
        return private_phone;
    }

    public void setPrivate_phone(String private_phone) {
        this.private_phone = private_phone;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
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

    public String getPrivate_detail_address() {
        return private_detail_address;
    }

    public void setPrivate_detail_address(String private_detail_address) {
        this.private_detail_address = private_detail_address;
    }

    public String getLogin_name() {
        return login_name;
    }

    public void setLogin_name(String login_name) {
        this.login_name = login_name;
    }

    public String getPrivate_headimgurl() {
        return private_headimgurl;
    }

    public void setPrivate_headimgurl(String private_headimgurl) {
        this.private_headimgurl = private_headimgurl;
    }

    public String getPrivate_email() {
        return private_email;
    }

    public void setPrivate_email(String private_email) {
        this.private_email = private_email;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getPrivate_sex() {
        return private_sex;
    }

    public void setPrivate_sex(String private_sex) {
        this.private_sex = private_sex;
    }
}
