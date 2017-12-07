package com.cloud.tao.bean.etc;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * Created by gezi-pc on 2016/10/14.
 */

public class RechargeCard  implements Parcelable {

        public String label_num;
        public String client_label_id;
        public String label_id;



        protected RechargeCard(Parcel in) {
                label_id = in.readString();
                client_label_id = in.readString();
                label_num = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(label_id);
                dest.writeString(client_label_id);
                dest.writeString(label_id);
        }

        @Override
        public int describeContents() {
                return 0;
        }

        public static final Creator<RechargeCard> CREATOR = new Creator<RechargeCard>() {
                @Override
                public RechargeCard createFromParcel(Parcel in) {
                        return new RechargeCard(in);
                }

                @Override
                public RechargeCard[] newArray(int size) {
                        return new RechargeCard[size];
                }
        };
}
