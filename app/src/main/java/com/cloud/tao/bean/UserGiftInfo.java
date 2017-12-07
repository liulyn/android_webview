package com.cloud.tao.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by janecer on 2016/8/14 0014
 * des:
 */
public class UserGiftInfo implements Parcelable{

    public String code ;//（礼包码）
    public String title ;//（礼包标题）
    public String icon ;//（对应图标）
    public String id ;//（对应的ID）
    public String update_time;//（领取时间）
    public int status ;//（0代表未使用1代表已使用，用于优惠券判断是否已经使用）
    public String starttime ;//(礼包开始时间)
    public String endtime ;//（礼包介绍时间）
    public String content;//（礼包描述）

    public UserGiftInfo() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.code);
        dest.writeString(this.title);
        dest.writeString(this.icon);
        dest.writeString(this.id);
        dest.writeString(this.update_time);
        dest.writeInt(this.status);
        dest.writeString(this.starttime);
        dest.writeString(this.endtime);
        dest.writeString(this.content);
    }

    protected UserGiftInfo(Parcel in) {
        this.code = in.readString();
        this.title = in.readString();
        this.icon = in.readString();
        this.id = in.readString();
        this.update_time = in.readString();
        this.status = in.readInt();
        this.starttime = in.readString();
        this.endtime = in.readString();
        this.content = in.readString();
    }

    public static final Creator<UserGiftInfo> CREATOR = new Creator<UserGiftInfo>() {
        @Override
        public UserGiftInfo createFromParcel(Parcel source) {
            return new UserGiftInfo(source);
        }

        @Override
        public UserGiftInfo[] newArray(int size) {
            return new UserGiftInfo[size];
        }
    };
}
