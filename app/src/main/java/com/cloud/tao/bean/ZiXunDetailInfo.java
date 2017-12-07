package com.cloud.tao.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by janecer on 2016/9/20 0020
 * des:
 */

public class ZiXunDetailInfo implements Parcelable {


    public String advice_id ;//(资讯ID)
    public String post_title;//（资讯标题）
    public long post_modified;//（更新时间）
    public String icon ;//（图标）
    public String id ;//(ID)
    public String type;//（类型）
    public String post_content;//（资讯内容）
    public String name;//(名称)
    public String clicknum;//(人气)
    public int post_type;//（1新闻，2攻略）
    public String post_hits;//（资讯阅读量）



    public boolean isHaveGameInfo(){
        return false ;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.advice_id);
        dest.writeString(this.post_title);
        dest.writeLong(this.post_modified);
        dest.writeString(this.icon);
        dest.writeString(this.id);
        dest.writeString(this.type);
        dest.writeString(this.post_content);
        dest.writeString(this.name);
        dest.writeString(this.clicknum);
        dest.writeInt(this.post_type);
        dest.writeString(this.post_hits);
    }

    public ZiXunDetailInfo() {
    }

    protected ZiXunDetailInfo(Parcel in) {
        this.advice_id = in.readString();
        this.post_title = in.readString();
        this.post_modified = in.readLong();
        this.icon = in.readString();
        this.id = in.readString();
        this.type = in.readString();
        this.post_content = in.readString();
        this.name = in.readString();
        this.clicknum = in.readString();
        this.post_type = in.readInt();
        this.post_hits = in.readString();
    }

    public static final Creator<ZiXunDetailInfo> CREATOR = new Creator<ZiXunDetailInfo>() {
        @Override
        public ZiXunDetailInfo createFromParcel(Parcel source) {
            return new ZiXunDetailInfo(source);
        }

        @Override
        public ZiXunDetailInfo[] newArray(int size) {
            return new ZiXunDetailInfo[size];
        }
    };
}
