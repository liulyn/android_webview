package com.cloud.tao.bean.etc;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by gezi-pc on 2016/10/14.
 */

public class CardType implements Parcelable {

    public String label_id;
    public String name;
    public ArrayList<RechargeCard> data;

    protected CardType(Parcel in) {
        label_id = in.readString();
        name = in.readString();
        data = in.createTypedArrayList(RechargeCard.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(label_id);
        dest.writeString(name);
        dest.writeTypedList(data);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CardType> CREATOR = new Creator<CardType>() {
        @Override
        public CardType createFromParcel(Parcel in) {
            return new CardType(in);
        }

        @Override
        public CardType[] newArray(int size) {
            return new CardType[size];
        }
    };
}
