package com.cloud.tao.ui.widget.bean;

/**
 * sunny created at 2016/10/18
 * des: 标签
 */
public class TagInfo {
    private boolean isChecked;
    private String text;
    private boolean isSelect;
    private int positon=0;
    public TagInfo(boolean isChecked, String text) {
        this.isChecked = isChecked;
        this.text = text;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public TagInfo(String text) {
        this.isChecked = true;
        this.text = text;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
