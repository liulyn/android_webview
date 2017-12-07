package com.cloud.tao.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

/**
 * author:janecer on 2016/8/10 12:04
 */


public class GameInfo implements Parcelable {
    public String name ;
    public String icon ;
    public String id ;//id
    public int clicknum ;//人气
    public int gcount ;//可领取礼包个数

    public String initial ;//（首字母）
    public String type ;//（分类ID，多个用逗号隔开）
    public String typename ;//（分类名称）  ,（获取最近登录记录 针对 获取用户记录）
    public String desc ;//（简介）


    public int num ;//(连续登录的天数)   针对 获取用户记录
    public long update_time;//（最新登录时间）针对 获取用户记录
    public int ts ;//(礼包要求连续登录的天数) 针对 获取用户记录
    public String title ;//(礼包标题) 针对 获取用户记录
    public String lb_id ;//(礼包ID) 针对 获取用户记录

    /**
     * 该方法 ，针对获取用户记录，是否有礼包
     * @return
     */
    public boolean isHasLoginGift() {
        return (ts != 0 && !TextUtils.isEmpty(lb_id)) ;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.icon);
        dest.writeString(this.id);
        dest.writeInt(this.clicknum);
        dest.writeInt(this.gcount);
        dest.writeString(this.initial);
        dest.writeString(this.type);
        dest.writeString(this.typename);
        dest.writeString(this.desc);
    }

    public GameInfo() {
    }

    protected GameInfo(Parcel in) {
        this.name = in.readString();
        this.icon = in.readString();
        this.id = in.readString();
        this.clicknum = in.readInt();
        this.gcount = in.readInt();
        this.initial = in.readString();
        this.type = in.readString();
        this.typename = in.readString();
        this.desc = in.readString();
    }

    public static final Parcelable.Creator<GameInfo> CREATOR = new Parcelable.Creator<GameInfo>() {
        @Override
        public GameInfo createFromParcel(Parcel source) {
            return new GameInfo(source);
        }

        @Override
        public GameInfo[] newArray(int size) {
            return new GameInfo[size];
        }
    };
}
