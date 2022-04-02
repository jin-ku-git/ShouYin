package com.youwu.shouyin.app;

import android.content.Context;

import androidx.multidex.MultiDex;

import com.youwu.shouyin.BuildConfig;
import com.youwu.shouyin.R;
import com.youwu.shouyin.ui.login.LoginActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.squareup.leakcanary.LeakCanary;
import com.youwu.shouyin.util.SPUtils;
import com.youwu.shouyin.utils_view.RxToast;

import me.goldze.mvvmhabit.base.BaseApplication;
import me.goldze.mvvmhabit.crash.CaocConfig;
import me.goldze.mvvmhabit.utils.KLog;


/**
 * Created by goldze on 2017/7/16.
 */

public class AppApplication extends BaseApplication {

    public static SPUtils spUtils;
    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        //是否开启打印日志
        KLog.init(BuildConfig.DEBUG);
        //初始化全局异常崩溃
        initCrash();
        RxToast.setContext(this);
        context = getApplicationContext();
        spUtils = new SPUtils("com.youwu.shouyin");

        //内存泄漏检测
//        if (!LeakCanary.isInAnalyzerProcess(this)) {
//            LeakCanary.install(this);
//        }
    }

    private void initCrash() {
        CaocConfig.Builder.create()
                .backgroundMode(CaocConfig.BACKGROUND_MODE_SILENT) //背景模式,开启沉浸式
                .enabled(true) //是否启动全局异常捕获
                .showErrorDetails(true) //是否显示错误详细信息
                .showRestartButton(true) //是否显示重启按钮
                .trackActivities(true) //是否跟踪Activity
                .minTimeBetweenCrashesMs(2000) //崩溃的间隔时间(毫秒)
                .errorDrawable(R.mipmap.ic_launcher) //错误图标
                .restartActivity(LoginActivity.class) //重新启动后的activity
//                .errorActivity(YourCustomErrorActivity.class) //崩溃后的错误activity
//                .eventListener(new YourCustomEventListener()) //崩溃后的错误监听
                .apply();
    }

    /**
     * 获取全局Context
     */
    public static Context getContext() {
        return context;
    }

    /**
     * 格式化输出JSON字符串
     * @return 格式化后的JSON字符串
     */
    public static String toPrettyFormat(String json) {
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = jsonParser.parse(json).getAsJsonObject();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(jsonObject);
    }

    /**
     * 重写此方法
     * @param base
     */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        // 主要是添加下面这句代码
        MultiDex.install(this);
    }
}
