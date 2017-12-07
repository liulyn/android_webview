package com.cloud.tao.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by janecer on 2016/8/14 0014
 * des:
 */
public class GameDetailInfo implements Parcelable{

    public int id ;
    public String  name;
    public String  icon;
    public int classify;
    public String image;
    public int clicknum ;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.icon);
        dest.writeInt(this.classify);
        dest.writeString(this.image);
        dest.writeInt(this.clicknum);
    }

    public GameDetailInfo() {
    }

    protected GameDetailInfo(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.icon = in.readString();
        this.classify = in.readInt();
        this.image = in.readString();
        this.clicknum = in.readInt();
    }

    public static final Creator<GameDetailInfo> CREATOR = new Creator<GameDetailInfo>() {
        @Override
        public GameDetailInfo createFromParcel(Parcel source) {
            return new GameDetailInfo(source);
        }

        @Override
        public GameDetailInfo[] newArray(int size) {
            return new GameDetailInfo[size];
        }
    };
}
