package com.cloud.tao.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by janecer on 2016/8/14 0014
 * des:
 */
public class GiftInfo implements Parcelable{
    public String id ;//(礼包ID)
    public String icon ;//（礼包缩略图）
    public String title ;//（礼包标题）
    public int total ;//（礼包总数量）
    public int remain ;//（礼包剩余数量）
    public String andtime;//（兑换开始时间）
    public String starttime;//（兑换结束时间）
    public String content ;//（礼包内容）


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.icon);
        dest.writeString(this.title);
        dest.writeInt(this.total);
        dest.writeInt(this.remain);
        dest.writeString(this.andtime);
        dest.writeString(this.starttime);
        dest.writeString(this.content);
    }

    public GiftInfo() {
    }

    protected GiftInfo(Parcel in) {
        this.id = in.readString();
        this.icon = in.readString();
        this.title = in.readString();
        this.total = in.readInt();
        this.remain = in.readInt();
        this.andtime = in.readString();
        this.starttime = in.readString();
        this.content = in.readString();
    }

    public static final Creator<GiftInfo> CREATOR = new Creator<GiftInfo>() {
        @Override
        public GiftInfo createFromParcel(Parcel source) {
            return new GiftInfo(source);
        }

        @Override
        public GiftInfo[] newArray(int size) {
            return new GiftInfo[size];
        }
    };
}
