package com.youwu.shouyin.utils_view;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.ColorInt;
import androidx.annotation.IntDef;
import androidx.annotation.IntRange;
import androidx.annotation.RequiresApi;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.regex.Pattern;

/**
 * @author：created by leaf on 2019-05-07
 * Github地址：https://github.com/Ye-Miao
 * Desc: 状态栏工具类
 */
public class StatusBarUtil {

    private static final int DEFAULT_ALPHA = 0;
    public final static int TYPE_MIUI = 0;
    public final static int TYPE_FLYME = 1;
    public final static int TYPE_M = 3;//6.0

    @IntDef({TYPE_MIUI, TYPE_FLYME, TYPE_M})
    @Retention(RetentionPolicy.SOURCE)
    @interface ViewType {
    }


    /**
     * 设置状态栏颜色（自定义颜色)
     *
     * @param activity 目标activity
     * @param color    状态栏颜色值
     */
    public static void setColor(@androidx.annotation.NonNull Activity activity, @ColorInt int color) {
        WeakReference<Activity> activityWeakReference = new WeakReference<>(activity);
        setColor(activityWeakReference.get(), color, DEFAULT_ALPHA);
    }

    /**
     * 设置纯色状态栏（自定义颜色，alpha）
     *
     * @param activity 目标activity
     * @param color    状态栏颜色值
     * @param alpha    状态栏透明度
     */
    public static void setColor(@androidx.annotation.NonNull Activity activity, @ColorInt int color, @IntRange(from = 0, to = 255) int alpha) {
        WeakReference<Activity> activityWeakReference = new WeakReference<>(activity);
        Window window = activityWeakReference.get().getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(cipherColor(color, alpha));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            setTranslucentView((ViewGroup) window.getDecorView(), color, alpha);
            setRootView(activityWeakReference.get(), true);
        }
    }

    /**
     * 设置状态栏渐变颜色
     *
     * @param activity 目标activity
     * @param view     目标View
     */
    public static void setGradientColor(@androidx.annotation.NonNull Activity activity, android.view.View view) {
        WeakReference<Activity> activityWeakReference = new WeakReference<>(activity);
        ViewGroup decorView = (ViewGroup) activityWeakReference.get().getWindow().getDecorView();
        android.view.View fakeStatusBarView = decorView.findViewById(android.R.id.custom);
        if (fakeStatusBarView != null) {
            decorView.removeView(fakeStatusBarView);
        }
        setRootView(activityWeakReference.get(), false);
        setTransparentForWindow(activityWeakReference.get());
        setPaddingTop(activityWeakReference.get(), view);
    }

    /**
     * 设置透明状态栏
     *
     * @param activity 目标界面
     */
    public static void setTransparentForWindow(@androidx.annotation.NonNull Activity activity) {
        WeakReference<Activity> activityWeakReference = new WeakReference<>(activity);
        Window window = activityWeakReference.get().getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(android.graphics.Color.TRANSPARENT);
            window.getDecorView()
                    .setSystemUiVisibility(android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE | android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    /**
     * 增加View的paddingTop,增加的值为状态栏高度 (智能判断，并设置高度)
     *
     * @param context 目标Context
     * @param view    需要增高的View
     */
    public static void setPaddingTop(Context context, @androidx.annotation.NonNull android.view.View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ViewGroup.LayoutParams lp = view.getLayoutParams();
            if (lp != null && lp.height > 0 && view.getPaddingTop() == 0) {
                lp.height += getStatusBarHeight(context);
                view.setPadding(view.getPaddingLeft(), view.getPaddingTop() + getStatusBarHeight(context),
                        view.getPaddingRight(), view.getPaddingBottom());
            }
        }
    }


    /**
     * 设置状态栏darkMode,字体颜色及icon变黑(目前支持MIUI6以上,Flyme4以上,Android M以上)
     *
     * @param activity 目标activity
     */
    public static void setDarkMode(@androidx.annotation.NonNull Activity activity) {
        WeakReference<Activity> activityWeakReference = new WeakReference<>(activity);
        darkMode(activityWeakReference.get().getWindow(), true);
    }

    /**
     * 设置状态栏darkMode,字体颜色及icon变亮(目前支持MIUI6以上,Flyme4以上,Android M以上)
     *
     * @param activity 目标activity
     */
    public static void setLightMode(@androidx.annotation.NonNull Activity activity) {
        WeakReference<Activity> activityWeakReference = new WeakReference<>(activity);
        darkMode(activityWeakReference.get().getWindow(), false);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static void darkMode(Window window, boolean dark) {
        if (isFlyme4()) {
            setModeForFlyme4(window, dark);
        } else if (isMIUI6()) {
            setModeForMIUI6(window, dark);
        }
        darkModeForM(window, dark);
    }


    /**
     * android 6.0设置字体颜色
     *
     * @param window 目标window
     * @param dark   亮色 or 暗色
     */
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private static void darkModeForM(Window window, boolean dark) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int systemUiVisibility = window.getDecorView().getSystemUiVisibility();
            if (dark) {
                systemUiVisibility |= android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            } else {
                systemUiVisibility &= ~android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            }
            window.getDecorView().setSystemUiVisibility(systemUiVisibility);
        }
    }


    /**
     * 设置MIUI6+的状态栏的darkMode,darkMode时候字体颜色及icon
     * http://dev.xiaomi.com/doc/p=4769/
     *
     * @param window 目标window
     * @param dark   亮色 or 暗色
     */
    private static void setModeForMIUI6(Window window, boolean dark) {
        Class<? extends Window> clazz = window.getClass();
        try {
            Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            int darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            extraFlagField.invoke(window, dark ? darkModeFlag : 0, darkModeFlag);
        } catch (Exception e) {
            android.util.Log.e("StatusBar", "darkIcon: failed");
        }

    }

    /**
     * 设置Flyme4+的状态栏的darkMode,darkMode时候字体颜色及icon
     * http://open-wiki.flyme.cn/index.php?title=Flyme%E7%B3%BB%E7%BB%9FAPI
     *
     * @param window 目标window
     * @param dark   亮色 or 暗色
     */
    private static void setModeForFlyme4(Window window, boolean dark) {
        try {
            WindowManager.LayoutParams lp = window.getAttributes();
            Field darkFlag = WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
            Field meizuFlags = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
            darkFlag.setAccessible(true);
            meizuFlags.setAccessible(true);
            int bit = darkFlag.getInt(null);
            int value = meizuFlags.getInt(lp);
            if (dark) {
                value |= bit;
            } else {
                value &= ~bit;
            }
            meizuFlags.setInt(lp, value);
            window.setAttributes(lp);
        } catch (Exception e) {
            android.util.Log.e("StatusBar", "darkIcon: failed");
        }
    }


    /**
     * 判断是否Flyme4以上
     */
    private static boolean isFlyme4() {
        return Build.FINGERPRINT.contains("Flyme_OS_4") || Build.VERSION.INCREMENTAL.contains("Flyme_OS_4")
                || Pattern.compile("Flyme OS [4|5]", Pattern.CASE_INSENSITIVE).matcher(Build.DISPLAY).find();
    }

    /**
     * 判断是否MIUI6以上
     */
    private static boolean isMIUI6() {
        try {
            Class<?> clz = Class.forName("android.os.SystemProperties");
            Method mtd = clz.getMethod("get", String.class);
            String val = (String) mtd.invoke(null, "ro.miui.ui.version.name");
            val = val.replaceAll("[vV]", "");
            int version = Integer.parseInt(val);
            return version >= 6;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 计算alpha色值
     *
     * @param color 状态栏颜色值
     * @param alpha 状态栏透明度
     */
    private static int cipherColor(@ColorInt int color, int alpha) {
        if (alpha == 0) {
            return color;
        }
        float a = 1 - alpha / 255f;
        int red = color >> 16 & 0xff;
        int green = color >> 8 & 0xff;
        int blue = color & 0xff;
        red = (int) (red * a + 0.5);
        green = (int) (green * a + 0.5);
        blue = (int) (blue * a + 0.5);
        return 0xff << 24 | red << 16 | green << 8 | blue;
    }


    /**
     * 创建透明View
     *
     * @param viewGroup 目标视图
     * @param color     状态栏颜色值
     * @param alpha     状态栏透明度
     */
    private static void setTranslucentView(ViewGroup viewGroup, @ColorInt int color, @IntRange(from = 0, to = 255) int alpha) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int cipherColor = cipherColor(color, alpha);
            android.view.View translucentView = viewGroup.findViewById(android.R.id.custom);
            if (translucentView == null && cipherColor != 0) {
                translucentView = new android.view.View(viewGroup.getContext());
                translucentView.setId(android.R.id.custom);
                ViewGroup.LayoutParams params =
                        new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(viewGroup.getContext()));
                viewGroup.addView(translucentView, params);
            }
            if (translucentView != null) {
                translucentView.setBackgroundColor(cipherColor);
            }
        }

    }

    /**
     * 设置根布局参数
     *
     * @param activity         目标activity
     * @param fitSystemWindows 是否预留toolbar的高度
     */
    private static void setRootView(Activity activity, boolean fitSystemWindows) {
        WeakReference<Activity> activityWeakReference = new WeakReference<>(activity);
        ViewGroup parent = activityWeakReference.get().findViewById(android.R.id.content);
        for (int i = 0, count = parent.getChildCount(); i < count; i++) {
            android.view.View childView = parent.getChildAt(i);
            if (childView instanceof ViewGroup) {
                childView.setFitsSystemWindows(fitSystemWindows);
                ((ViewGroup) childView).setClipToPadding(fitSystemWindows);
            }
        }
    }

    /**
     * 获取状态栏高度
     *
     * @param context 目标Context
     */
    private static int getStatusBarHeight(Context context) {
        // 获得状态栏高度
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }


    /**
     * 修改状态栏为全透明
     * @param activity
     */
    @TargetApi(19)
    public static void transparencyBar(Activity activity){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(android.graphics.Color.TRANSPARENT);
            window.setNavigationBarColor(android.graphics.Color.TRANSPARENT);
        } else
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window =activity.getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    /**
     * 修改状态栏颜色，支持4.4以上版本
     * @param activity
     * @param colorId
     */
    public static void setStatusBarColor(Activity activity, int colorId) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
//      window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(activity.getResources().getColor(colorId));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //使用SystemBarTint库使4.4版本状态栏变色，需要先将状态栏设置为透明
            transparencyBar(activity);
            SystemBarTintManager tintManager = new SystemBarTintManager(activity);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(colorId);
        }
    }

    /**
     *设置状态栏黑色字体图标，
     * 适配4.4以上版本MIUIV、Flyme和6.0以上版本其他Android
     * @param activity
     * @return 1:MIUUI 2:Flyme 3:android6.0
     */
    public static int StatusBarLightMode(Activity activity){
        int result=0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if(MIUISetStatusBarLightMode(activity.getWindow(), true)){
                result=1;
            }else if(FlymeSetStatusBarLightMode(activity.getWindow(), true)){
                result=2;
            }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                activity.getWindow().getDecorView().setSystemUiVisibility( android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN| android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                result=3;
            }
        }
        return result;
    }

    /**
     * 已知系统类型时，设置状态栏黑色字体图标。
     * 适配4.4以上版本MIUIV、Flyme和6.0以上版本其他Android
     * @param activity
     * @param type 1:MIUUI 2:Flyme 3:android6.0
     */
    public static void StatusBarLightMode(Activity activity, int type){
        if(type==1){
            MIUISetStatusBarLightMode(activity.getWindow(), true);
        }else if(type==2){
            FlymeSetStatusBarLightMode(activity.getWindow(), true);
        }else if(type==3){
            activity.getWindow().getDecorView().setSystemUiVisibility( android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN| android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

    }

    /**
     * 清除MIUI或flyme或6.0以上版本状态栏黑色字体
     */
    public static void StatusBarDarkMode(Activity activity, int type){
        if(type==1){
            MIUISetStatusBarLightMode(activity.getWindow(), false);
        }else if(type==2){
            FlymeSetStatusBarLightMode(activity.getWindow(), false);
        }else if(type==3){
            activity.getWindow().getDecorView().setSystemUiVisibility(android.view.View.SYSTEM_UI_FLAG_VISIBLE);
        }

    }


    /**
     * 设置状态栏图标为深色和魅族特定的文字风格
     * 可以用来判断是否为Flyme用户
     * @param window 需要设置的窗口
     * @param dark 是否把状态栏字体及图标颜色设置为深色
     * @return  boolean 成功执行返回true
     *
     */
    public static boolean FlymeSetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
                result = true;
            } catch (Exception e) {

            }
        }
        return result;
    }

    /**
     * 设置状态栏字体图标为深色，需要MIUIV6以上
     * @param window 需要设置的窗口
     * @param dark 是否把状态栏字体及图标颜色设置为深色
     * @return  boolean 成功执行返回true
     *
     */
    public static boolean MIUISetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            Class clazz = window.getClass();
            try {
                int darkModeFlag = 0;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if(dark){
                    extraFlagField.invoke(window,darkModeFlag,darkModeFlag);//状态栏透明且黑色字体
                }else{
                    extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
                }
                result=true;
            }catch (Exception e){

            }
        }
        return result;
    }

    /**
     * 谷歌原生修改状态栏字体颜色
     * @param activity
     * @param dark
     */
    public static void setAndroidNativeLightStatusBar(Activity activity, boolean dark) {
        android.view.View decor = activity.getWindow().getDecorView();
        if (dark) {
            decor.setSystemUiVisibility(android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {
            decor.setSystemUiVisibility(android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }





    /**
     * 设置状态栏透明
     */
    @TargetApi(19)
    public static void setTranslucentStatus(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //5.x开始需要把颜色设置透明，否则导航栏会呈现系统默认的浅灰色
            Window window = activity.getWindow();
            android.view.View decorView = window.getDecorView();
            //两个 flag 要结合使用，表示让应用的主体内容占用系统状态栏的空间
            int option = android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(android.graphics.Color.TRANSPARENT);
            //导航栏颜色也可以正常设置
            //window.setNavigationBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = activity.getWindow();
            WindowManager.LayoutParams attributes = window.getAttributes();
            int flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            attributes.flags |= flagTranslucentStatus;
            //int flagTranslucentNavigation = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION; //attributes.flags |= flagTranslucentNavigation;
            window.setAttributes(attributes);
        }
    }

    /**
     * 代码实现android:fitsSystemWindows
     *
     * @param activity
     */
    public static void setRootViewFitsSystemWindows(Activity activity, boolean fitSystemWindows) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ViewGroup winContent = (ViewGroup) activity.findViewById(android.R.id.content);
            if (winContent.getChildCount() > 0) {
                ViewGroup rootView = (ViewGroup) winContent.getChildAt(0);
                if (rootView != null) {
                    rootView.setFitsSystemWindows(fitSystemWindows);
                }
            }
        }
    }

    /**
     * 设置状态栏深色浅色切换
     */
    @SuppressLint("WrongConstant")
    public static boolean setStatusBarDarkTheme(Activity activity, boolean dark) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                setStatusBarFontIconDark(activity, TYPE_M, dark);
            } else if (OSUtils.isMiui()) {
                setStatusBarFontIconDark(activity, TYPE_MIUI, dark);
            } else if (OSUtils.isFlyme()) {
                setStatusBarFontIconDark(activity, TYPE_FLYME, dark);
            } else {//其他情况
                return false;
            }
            return true;
        }
        return false;
    }

    /**
     * 设置 状态栏深色浅色切换
     */
    public static boolean setStatusBarFontIconDark(Activity activity, @StatusBarUtil.ViewType int type, boolean dark) {
        switch (type) {
            case TYPE_MIUI:
                return setMiuiUI(activity, dark);
            case TYPE_FLYME:
                return setFlymeUI(activity, dark);
            case TYPE_M:
            default:
                return setCommonUI(activity, dark);
        }
    }

    //设置6.0 状态栏深色浅色切换
    public static boolean setCommonUI(Activity activity, boolean dark) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            android.view.View decorView = activity.getWindow().getDecorView();
            if (decorView != null) {
                int vis = decorView.getSystemUiVisibility();
                if (dark) {
                    vis |= android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                } else {
                    vis &= ~android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                }
                if (decorView.getSystemUiVisibility() != vis) {
                    decorView.setSystemUiVisibility(vis);
                }
                return true;
            }
        }
        return false;
    }

    //设置Flyme 状态栏深色浅色切换
    public static boolean setFlymeUI(Activity activity, boolean dark) {
        try {
            Window window = activity.getWindow();
            WindowManager.LayoutParams lp = window.getAttributes();
            Field darkFlag = WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
            Field meizuFlags = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
            darkFlag.setAccessible(true);
            meizuFlags.setAccessible(true);
            int bit = darkFlag.getInt(null);
            int value = meizuFlags.getInt(lp);
            if (dark) {
                value |= bit;
            } else {
                value &= ~bit;
            }
            meizuFlags.setInt(lp, value);
            window.setAttributes(lp);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //设置MIUI 状态栏深色浅色切换
    public static boolean setMiuiUI(Activity activity, boolean dark) {
        try {
            Window window = activity.getWindow();
            Class<?> clazz = activity.getWindow().getClass();
            @SuppressLint("PrivateApi") Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            int darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getDeclaredMethod("setExtraFlags", int.class, int.class);
            extraFlagField.setAccessible(true);
            if (dark) {    //状态栏亮色且黑色字体.
                extraFlagField.invoke(window, darkModeFlag, darkModeFlag);
            } else {
                extraFlagField.invoke(window, 0, darkModeFlag);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


}
