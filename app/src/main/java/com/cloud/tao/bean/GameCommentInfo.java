package com.cloud.tao.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by janecer on 2016/8/14 0014
 * des:
 */
public class GameCommentInfo implements Parcelable {

    public String id ;//(自动增长)
    public String app_id ;//(ID)
    public String mem_id ;//（发表者评论ID）
    public String content ;//（评论内容）
    public String create_time ;//（发表时间）
    public String parentid ;//(回复的评论ID，默认为0)
    public String nickname ;//(评论者名称),
    public String head_img ;//(评论者头像)
    public String user_id ;//(评论者ID，0为游客)


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.app_id);
        dest.writeString(this.mem_id);
        dest.writeString(this.content);
        dest.writeString(this.create_time);
        dest.writeString(this.parentid);
        dest.writeString(this.nickname);
        dest.writeString(this.head_img);
        dest.writeString(this.user_id);
    }

    public GameCommentInfo() {
    }

    protected GameCommentInfo(Parcel in) {
        this.id = in.readString();
        this.app_id = in.readString();
        this.mem_id = in.readString();
        this.content = in.readString();
        this.create_time = in.readString();
        this.parentid = in.readString();
        this.nickname = in.readString();
        this.head_img = in.readString();
        this.user_id = in.readString();
    }

    public static final Creator<GameCommentInfo> CREATOR = new Creator<GameCommentInfo>() {
        @Override
        public GameCommentInfo createFromParcel(Parcel source) {
            return new GameCommentInfo(source);
        }

        @Override
        public GameCommentInfo[] newArray(int size) {
            return new GameCommentInfo[size];
        }
    };
}
