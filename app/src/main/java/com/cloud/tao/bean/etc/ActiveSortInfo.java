package com.cloud.tao.bean.etc;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;

/**
 * sunny created at 2016/10/15
 * des: 分类页一级菜单
 */
public class ActiveSortInfo implements Parcelable {

    public String category_id;
    public String category_name;
    public String category_pic_url;
    public String parent_id;
    public ArrayList<ActiveCategoryInfo> son;
    public String store_id;

    protected ActiveSortInfo(Parcel in) {
        category_id = in.readString();
        category_name = in.readString();
        category_pic_url = in.readString();
        parent_id = in.readString();
        store_id = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(category_id);
        dest.writeString(category_name);
        dest.writeString(category_pic_url);
        dest.writeString(parent_id);
        dest.writeString(store_id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ActiveSortInfo> CREATOR = new Creator<ActiveSortInfo>() {
        @Override
        public ActiveSortInfo createFromParcel(Parcel in) {
            return new ActiveSortInfo(in);
        }

        @Override
        public ActiveSortInfo[] newArray(int size) {
            return new ActiveSortInfo[size];
        }
    };
}
