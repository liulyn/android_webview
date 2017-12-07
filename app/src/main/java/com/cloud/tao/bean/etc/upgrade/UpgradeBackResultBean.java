package com.cloud.tao.bean.etc.upgrade;

import java.io.Serializable;

/**
 * author: yhf
 * Description: 版本下载 结果回调
 */
public class UpgradeBackResultBean implements Serializable{

    private boolean isFinish; //是否完成
    private boolean isError;    //发送错误
    private String downloadSize; //已下载大小
    private String downloadTotleSize; //安装包总大小
    private int downloadProgress; //下载进度

    public boolean isFinish() {
        return isFinish;
    }

    public void setFinish(boolean finish) {
        isFinish = finish;
    }

    public boolean isError() {
        return isError;
    }

    public void setError(boolean error) {
        isError = error;
    }

    public String getDownloadSize() {
        return downloadSize;
    }

    public void setDownloadSize(String downloadSize) {
        this.downloadSize = downloadSize;
    }

    public String getDownloadTotleSize() {
        return downloadTotleSize;
    }

    public void setDownloadTotleSize(String downloadTotleSize) {
        this.downloadTotleSize = downloadTotleSize;
    }

    public int getDownloadProgress() {
        return downloadProgress;
    }

    public void setDownloadProgress(int downloadProgress) {
        this.downloadProgress = downloadProgress;
    }
}
