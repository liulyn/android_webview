package com.cloud.tao.bean.etc;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * sunny created at 2016/10/20
 * des: 商品详情attr
 */
public class GoodsDetailAttrInfo implements Parcelable{
    public String attr_id;
    public String attr_level;
    public String attr_name;

    protected GoodsDetailAttrInfo(Parcel in) {
        attr_id = in.readString();
        attr_level = in.readString();
        attr_name = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(attr_id);
        dest.writeString(attr_level);
        dest.writeString(attr_name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GoodsDetailAttrInfo> CREATOR = new Creator<GoodsDetailAttrInfo>() {
        @Override
        public GoodsDetailAttrInfo createFromParcel(Parcel in) {
            return new GoodsDetailAttrInfo(in);
        }

        @Override
        public GoodsDetailAttrInfo[] newArray(int size) {
            return new GoodsDetailAttrInfo[size];
        }
    };
}
