package com.cloud.tao.bean.etc.upgrade;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * author: yhf
 * Description: 版本更新
 */
public class UpgradeBean implements Parcelable {

    public String update_info;
    public String version;
    public String android_download_url;

    protected UpgradeBean(Parcel in) {
        update_info = in.readString();
        version = in.readString();
        android_download_url = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(update_info);
        dest.writeString(version);
        dest.writeString(android_download_url);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UpgradeBean> CREATOR = new Creator<UpgradeBean>() {
        @Override
        public UpgradeBean createFromParcel(Parcel in) {
            return new UpgradeBean(in);
        }

        @Override
        public UpgradeBean[] newArray(int size) {
            return new UpgradeBean[size];
        }
    };
}
