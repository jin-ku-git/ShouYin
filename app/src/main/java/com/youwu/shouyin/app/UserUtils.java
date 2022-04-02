package com.youwu.shouyin.app;

import android.text.TextUtils;

import com.blankj.utilcode.util.SPStaticUtils;

/**
 * 将用户信息保存到sharePreference里面
 */
public class UserUtils {

    public static boolean isLogin() {
        return !TextUtils.isEmpty(getToken());
    }

    public static void setUserName(String name) {
        SPStaticUtils.put("userName", name);
    }

    public static String getUserName() {
        return SPStaticUtils.getString("userName", "");
    }


    public static void setToken(String token) {
        SPStaticUtils.put("token", token);
    }

    public static String getToken() {
        return SPStaticUtils.getString("token", "");
    }

    public static void setTokenType(String token_type) {
        SPStaticUtils.put("tokenType", token_type);
    }

    public static String getTokenType() {
        return SPStaticUtils.getString("tokenType", "");
    }
    public static void setName(String name) {
        SPStaticUtils.put("name", name);
    }

    public static String getName() {
        return SPStaticUtils.getString("name", "");
    }
    public static void setTel(String tel) {
        SPStaticUtils.put("tel", tel);
    }

    public static String getTel() {
        return SPStaticUtils.getString("tel", "");
    }

    public static void logout() {
        setToken("");
        setTokenType("");
        setName("");
        setTel("");
        setLogoTime("");
        setLogoName("");
    }

    public static void setLogoTime(String logo_time) {
        SPStaticUtils.put("logo_time", logo_time);
    }

    public static String getLogoTime() {
        return SPStaticUtils.getString("logo_time", "");
    }
    public static void setLogoName(String logo_name) {
        SPStaticUtils.put("logo_name", logo_name);
    }

    public static String getLogoName() {
        return SPStaticUtils.getString("logo_name", "");
    }


}
