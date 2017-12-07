package com.cloud.tao.bean.etc;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by gezi-pc on 2016/10/2.
 */

public class Account implements Parcelable{

    public String id;
    public String receiver;
    public String type;
    public String type_name;
    public String account;
    public String opening_bank;
    public Long create_time;
    public int is_default;
    public String pay_way;




    protected Account(Parcel in) {
        id = in.readString();
        receiver = in.readString();
        type = in.readString();
        type_name = in.readString();
        account = in.readString();
        opening_bank = in.readString();
        create_time = in.readLong();
        is_default = in.readInt();
        pay_way = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(receiver);
        dest.writeString(type);
        dest.writeString(type_name);
        dest.writeString(account);
        dest.writeString(opening_bank);
        dest.writeLong(create_time);
        dest.writeInt(is_default);
        dest.writeString(pay_way);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Account> CREATOR = new Creator<Account>() {
        @Override
        public Account createFromParcel(Parcel in) {
            return new Account(in);
        }

        @Override
        public Account[] newArray(int size) {
            return new Account[size];
        }
    };


    @Override
    public String toString() {
        return "Account{" +
                "id='" + id + '\'' +
                ", receiver='" + receiver + '\'' +
                ", type='" + type + '\'' +
                ", type_name='" + type_name + '\'' +
                ", account='" + account + '\'' +
                ", opening_bank='" + opening_bank + '\'' +
                ", create_time=" + create_time +
                ", is_default=" + is_default +
                ", pay_way='" + pay_way + '\'' +
                '}';
    }
}
