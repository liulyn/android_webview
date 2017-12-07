package com.cloud.tao.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by janecer on 2016/8/14 0014
 * des:
 */
public class LoginGameDayInfo implements Parcelable {

    public String id ;//(主键)
    public String mem_id ;//(用户ID)
    public String app_id ;//（ID）
    public String create_time ;//（第一次玩的时间）
    public String update_time ;//（最近一次玩的时间）
    public int num ;// (连续登录天数);

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.mem_id);
        dest.writeString(this.app_id);
        dest.writeString(this.create_time);
        dest.writeString(this.update_time);
        dest.writeInt(this.num);
    }

    public LoginGameDayInfo() {
    }

    protected LoginGameDayInfo(Parcel in) {
        this.id = in.readString();
        this.mem_id = in.readString();
        this.app_id = in.readString();
        this.create_time = in.readString();
        this.update_time = in.readString();
        this.num = in.readInt();
    }

    public static final Parcelable.Creator<LoginGameDayInfo> CREATOR = new Parcelable.Creator<LoginGameDayInfo>() {
        @Override
        public LoginGameDayInfo createFromParcel(Parcel source) {
            return new LoginGameDayInfo(source);
        }

        @Override
        public LoginGameDayInfo[] newArray(int size) {
            return new LoginGameDayInfo[size];
        }
    };
}
