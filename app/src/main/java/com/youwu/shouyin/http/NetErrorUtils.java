package com.youwu.shouyin.http;

import me.goldze.mvvmhabit.utils.ToastUtils;

public class NetErrorUtils {

    public static void errorFunction(ApiException apiException){
        if (apiException.getMessage()!=null){
            ToastUtils.showShort(apiException.getMessage());
        }

    }
}
