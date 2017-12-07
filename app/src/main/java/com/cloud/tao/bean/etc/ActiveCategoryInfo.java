package com.cloud.tao.bean.etc;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * sunny created at 2016/10/15
 * des: 分类页二级菜单
 */
public class ActiveCategoryInfo implements Parcelable {

    public String category_id;
    public String category_name;
    public String category_pic_url;
    public String parent_id;
    public String store_id;

    protected ActiveCategoryInfo(Parcel in) {
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

    public static final Creator<ActiveCategoryInfo> CREATOR = new Creator<ActiveCategoryInfo>() {
        @Override
        public ActiveCategoryInfo createFromParcel(Parcel in) {
            return new ActiveCategoryInfo(in);
        }

        @Override
        public ActiveCategoryInfo[] newArray(int size) {
            return new ActiveCategoryInfo[size];
        }
    };
}
