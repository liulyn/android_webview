package com.cloud.tao.bean.etc;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by gezi-pc on 2016/10/14.
 */

public class RechargeCardOrder implements Parcelable {

    public String label_id;
    public String label_name;
    public ArrayList<RechargeCard> data;

    public RechargeCardOrder() {
    }

    protected RechargeCardOrder(Parcel in) {
        label_id = in.readString();
        label_name = in.readString();
        data = in.createTypedArrayList(RechargeCard.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(label_id);
        dest.writeString(label_name);
        dest.writeTypedList(data);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RechargeCardOrder> CREATOR = new Creator<RechargeCardOrder>() {
        @Override
        public RechargeCardOrder createFromParcel(Parcel in) {
            return new RechargeCardOrder(in);
        }

        @Override
        public RechargeCardOrder[] newArray(int size) {
            return new RechargeCardOrder[size];
        }
    };
}
