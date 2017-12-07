package com.cloud.tao.net;

/**
 * sunny created at 2016/9/10/06
 */

public class NetCodeList {

    public static final int BASE_RESPONSE_CODE = 0 ; //<0:退出重新登录、=0：成功、>0:失败

    public static final int BASE_FAIL_CODE =500; //服务器内部错误
    public static final int BASE_NET_FAIL_CODE =606; //网络错误

    public static final int CODE＿NET_SUCCESS = 000000 ;//网络操作成功状态码
    public static final int CODE_NET_ERROR_PARAMETER = 110002 ;//如果是上传的参数错误
}
