package com.youwu.shouyin.data.source.local;

import com.youwu.shouyin.data.source.LocalDataSource;

import me.goldze.mvvmhabit.utils.SPUtils;

/**
 * 本地数据源，可配合Room框架使用
 * Created by goldze on 2019/3/26.
 */
public class LocalDataSourceImpl implements LocalDataSource {
    private volatile static LocalDataSourceImpl INSTANCE = null;

    public static LocalDataSourceImpl getInstance() {
        if (INSTANCE == null) {
            synchronized (LocalDataSourceImpl.class) {
                if (INSTANCE == null) {
                    INSTANCE = new LocalDataSourceImpl();
                }
            }
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    private LocalDataSourceImpl() {
        //数据库Helper构建
    }

    /**
     * 修改账户
     * @param userName
     */
    @Override
    public void saveUserName(String userName) {
        SPUtils.getInstance().put("UserName", userName);
    }

    /**
     * 修改密码
     * @param password
     */
    @Override
    public void savePassword(String password) {
        SPUtils.getInstance().put("password", password);
    }

    /**
     * 获取账户
     * @return
     */
    @Override
    public String getUserName() {
        return SPUtils.getInstance().getString("UserName");
    }

    /**
     * 获取密码
     * @return
     */
    @Override
    public String getPassword() {
        return SPUtils.getInstance().getString("password");
    }

    /**
     * 退出账户
     */
    @Override
    public void SignOutAccount() {
        SPUtils.getInstance().remove("UserName");
        SPUtils.getInstance().remove("password");
    }

}
