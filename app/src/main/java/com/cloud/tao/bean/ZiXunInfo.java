package com.cloud.tao.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by janecer on 2016/8/14 0014
 * des:
 */
public class ZiXunInfo implements Parcelable {

    public int id ;//(资讯ID)
    public String post_title ;//（标题）
    public String post_content ;//(内容)
    public String smeta ;//（缩略图）
    public String app_id ;//（对应ID）


    //最新资讯绑定的
    public GameDetailInfo mGameInfo ;

    public ZiXunInfo() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.post_title);
        dest.writeString(this.post_content);
        dest.writeString(this.smeta);
        dest.writeString(this.app_id);
        dest.writeParcelable(this.mGameInfo, flags);
    }

    protected ZiXunInfo(Parcel in) {
        this.id = in.readInt();
        this.post_title = in.readString();
        this.post_content = in.readString();
        this.smeta = in.readString();
        this.app_id = in.readString();
        this.mGameInfo = in.readParcelable(GameDetailInfo.class.getClassLoader());
    }

    public static final Creator<ZiXunInfo> CREATOR = new Creator<ZiXunInfo>() {
        @Override
        public ZiXunInfo createFromParcel(Parcel source) {
            return new ZiXunInfo(source);
        }

        @Override
        public ZiXunInfo[] newArray(int size) {
            return new ZiXunInfo[size];
        }
    };
}
