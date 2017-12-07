package com.cloud.tao.bean.etc.upgrade;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * sunny created at 2016/10/24
 * des: 创业中心我的二维码信息
 */
public class FenxiaoQRInfo implements Parcelable{

    public String client_url;
    public String store_name;
    public String store_logo_url;
    public String store_android_app_download_url;
    public String recommend_code;

    public String webpageUrl;
    public String title;
    public String description;
    public boolean isTimeLine;


    protected FenxiaoQRInfo(Parcel in) {
        client_url = in.readString();
        store_name = in.readString();
        store_logo_url = in.readString();
        store_android_app_download_url = in.readString();
        recommend_code = in.readString();
        webpageUrl = in.readString();
        title = in.readString();
        description = in.readString();
        isTimeLine = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(client_url);
        dest.writeString(store_name);
        dest.writeString(store_logo_url);
        dest.writeString(store_android_app_download_url);
        dest.writeString(recommend_code);
        dest.writeString(webpageUrl);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeByte((byte) (isTimeLine ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FenxiaoQRInfo> CREATOR = new Creator<FenxiaoQRInfo>() {
        @Override
        public FenxiaoQRInfo createFromParcel(Parcel in) {
            return new FenxiaoQRInfo(in);
        }

        @Override
        public FenxiaoQRInfo[] newArray(int size) {
            return new FenxiaoQRInfo[size];
        }
    };
}
