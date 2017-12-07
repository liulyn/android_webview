package com.cloud.tao.bean.etc;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * sunny created at 2016/9/29
 * des: 商品activity节点
 */
public class GoodsActivityInfo implements Parcelable {

    public String activity_id;
    public int wait_end_time;
    public String state;
    public String beizhu;
    public int wait_start_time;
    public String showtime;
    public String type;
    public String discount;
    public String price_cut;
    public String end_time;
    public String discount_type;
    public String discount_state;
    public String goods_id;
    public String name;
    public String type_name;
    public String create_time;
    public String start_time;
    public String ext_state_language;
    public int ext_state;

    protected GoodsActivityInfo(Parcel in) {
        activity_id = in.readString();
        wait_end_time = in.readInt();
        state = in.readString();
        beizhu = in.readString();
        showtime=in.readString();
        wait_start_time = in.readInt();
        type = in.readString();
        discount = in.readString();
        price_cut = in.readString();
        end_time = in.readString();
        discount_type = in.readString();
        discount_state = in.readString();
        goods_id = in.readString();
        name = in.readString();
        type_name = in.readString();
        create_time = in.readString();
        start_time = in.readString();
        ext_state_language = in.readString();
        ext_state = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(activity_id);
        dest.writeInt(wait_end_time);
        dest.writeString(state);
        dest.writeString(beizhu);
        dest.writeString(showtime);
        dest.writeInt(wait_start_time);
        dest.writeString(type);
        dest.writeString(discount);
        dest.writeString(price_cut);
        dest.writeString(end_time);
        dest.writeString(discount_type);
        dest.writeString(discount_state);
        dest.writeString(goods_id);
        dest.writeString(name);
        dest.writeString(type_name);
        dest.writeString(create_time);
        dest.writeString(start_time);
        dest.writeString(ext_state_language);
        dest.writeInt(ext_state);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GoodsActivityInfo> CREATOR = new Creator<GoodsActivityInfo>() {
        @Override
        public GoodsActivityInfo createFromParcel(Parcel in) {
            return new GoodsActivityInfo(in);
        }

        @Override
        public GoodsActivityInfo[] newArray(int size) {
            return new GoodsActivityInfo[size];
        }
    };
}
