package com.cloud.tao.bean.etc;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * sunny created at 2016/10/12
 * des: 首页轮播图片
 */
public class NavImgInfo implements Parcelable {

    public String thumb; // 链接地址
    public String image; //图片地址
    public String desc; //描述
    public String link;


    protected NavImgInfo(Parcel in) {
        thumb = in.readString();
        image = in.readString();
        desc = in.readString();
        link = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(thumb);
        dest.writeString(image);
        dest.writeString(desc);
        dest.writeString(link);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<NavImgInfo> CREATOR = new Creator<NavImgInfo>() {
        @Override
        public NavImgInfo createFromParcel(Parcel in) {
            return new NavImgInfo(in);
        }

        @Override
        public NavImgInfo[] newArray(int size) {
            return new NavImgInfo[size];
        }
    };
}
