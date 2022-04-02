package com.youwu.shouyin.http;


import android.net.ParseException;

import com.blankj.utilcode.util.NetworkUtils;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;

import java.io.NotSerializableException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;


public class ApiException extends Exception {
    //对应HTTP的状态码
    private static final int BADREQUEST = 400;
    private static final int UNAUTHORIZED = 401;
    private static final int FORBIDDEN = 403;
    private static final int NOT_FOUND = 404;
    private static final int METHOD_NOT_ALLOWED = 405;
    private static final int REQUEST_TIMEOUT = 408;
    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final int BAD_GATEWAY = 502;
    private static final int SERVICE_UNAVAILABLE = 503;
    private static final int GATEWAY_TIMEOUT = 504;

    private final int code;
    private String displayMessage;

    public static final int UNKNOWN = 1000;
    public static final int PARSE_ERROR = 1001;
    private String message;

    public boolean isNull() {
        return code == 1010;
    }

    public ApiException(Throwable throwable, int code) {
        super(throwable);
        this.code = code;
        this.message = throwable.getMessage();
    }

    public int getCode() {
        return code;
    }

    public String getDisplayMessage() {
        return displayMessage;
    }

    public void setDisplayMessage(String msg) {
        this.displayMessage = msg + "(code:" + code + ")";
    }

    public static ApiException handleException(Throwable e) {
        ApiException ex;
        if (e instanceof ServerException) {
            ex = new ApiException(e, ((ServerException) e).getErrCode());
            ex.message = e.getMessage();
            return ex;
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof JsonSyntaxException
                || e instanceof JsonSerializer
                || e instanceof NotSerializableException
                || e instanceof ParseException) {
            ex = new ApiException(e, ERROR.PARSE_ERROR);
            ex.message = "数据解析失败,请稍后再试";
            return ex;
        } else if (e instanceof ClassCastException) {
            ex = new ApiException(e, ERROR.CAST_ERROR);
            ex.message = "类型转换错误";
            return ex;
        } else if (e instanceof ConnectException) {
            ex = new ApiException(e, ERROR.NETWORD_ERROR);
            ex.message = "网络不给力，请稍候重试！";
            return ex;
        } else if (e instanceof javax.net.ssl.SSLHandshakeException) {
            ex = new ApiException(e, ERROR.SSL_ERROR);
            ex.message = "证书验证失败";
            return ex;
        } else if (e instanceof java.net.SocketTimeoutException || e instanceof TimeoutException || e instanceof ConnectTimeoutException) {
            ex = new ApiException(e, ERROR.TIMEOUT_ERROR);
            ex.message = "连接超时,请稍后再试";
            return ex;
        } else if (e instanceof UnknownHostException) {
            ex = new ApiException(e, ERROR.UNKNOWNHOST_ERROR);
            if (!NetworkUtils.isConnected()) {
                ex.message = "当前无网络，请检查你的网络设置";
            } else {
                ex.message = "网络连接不可用，请稍后重试！";
            }

            return ex;
        } else if (e instanceof NullPointerException) {
            ex = new ApiException(e, ERROR.NULLPOINTER_EXCEPTION);
            ex.message = "NullPointerException";
            return ex;
        } else {
            ex = new ApiException(e, ERROR.UNKNOWN);
            ex.message = "未知错误";
            return ex;
        }
    }

    @Override
    public String getMessage() {
        return message;
    }

    /*public String getErrMessage() {
        return message;
    }*/

    /**
     * 约定异常
     */
    public static class ERROR {
        /**
         * 未知错误
         */
        public static final int UNKNOWN = 1000;
        /**
         * 解析错误
         */
        public static final int PARSE_ERROR = UNKNOWN + 1;
        /**
         * 网络错误
         */
        public static final int NETWORD_ERROR = PARSE_ERROR + 1;
        /**
         * 协议出错
         */
        public static final int HTTP_ERROR = NETWORD_ERROR + 1;

        /**
         * 证书出错
         */
        public static final int SSL_ERROR = HTTP_ERROR + 1;

        /**
         * 连接超时
         */
        public static final int TIMEOUT_ERROR = SSL_ERROR + 1;

        /**
         * 调用错误
         */
        public static final int INVOKE_ERROR = TIMEOUT_ERROR + 1;
        /**
         * 类转换错误
         */
        public static final int CAST_ERROR = INVOKE_ERROR + 1;
        /**
         * 请求取消
         */
        public static final int REQUEST_CANCEL = CAST_ERROR + 1;
        /**
         * 未知主机错误
         */
        public static final int UNKNOWNHOST_ERROR = REQUEST_CANCEL + 1;

        /**
         * 空指针错误
         */
        public static final int NULLPOINTER_EXCEPTION = UNKNOWNHOST_ERROR + 1;

        public static final int HttpStatusCodeException = NULLPOINTER_EXCEPTION + 1;
    }
}