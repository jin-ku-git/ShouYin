package com.youwu.shouyin.data;

import com.youwu.shouyin.data.source.HttpDataSource;
import com.youwu.shouyin.data.source.LocalDataSource;
import com.youwu.shouyin.entity.DemoEntity;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import io.reactivex.Observable;
import me.goldze.mvvmhabit.base.BaseModel;
import me.goldze.mvvmhabit.http.BaseResponse;

/**
 * MVVM的Model层，统一模块的数据仓库，包含网络数据和本地数据（一个应用可以有多个Repositor）
 * Created by goldze on 2019/3/26.
 */
public class DemoRepository extends BaseModel implements HttpDataSource, LocalDataSource {
    private volatile static DemoRepository INSTANCE = null;
    private final HttpDataSource mHttpDataSource;

    private final LocalDataSource mLocalDataSource;

    private DemoRepository(@NonNull HttpDataSource httpDataSource,
                           @NonNull LocalDataSource localDataSource) {
        this.mHttpDataSource = httpDataSource;
        this.mLocalDataSource = localDataSource;
    }

    public static DemoRepository getInstance(HttpDataSource httpDataSource,
                                             LocalDataSource localDataSource) {
        if (INSTANCE == null) {
            synchronized (DemoRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new DemoRepository(httpDataSource, localDataSource);
                }
            }
        }
        return INSTANCE;
    }

    @VisibleForTesting
    public static void destroyInstance() {
        INSTANCE = null;
    }


    @Override
    public Observable<Object> login() {
        return mHttpDataSource.login();
    }

    @Override
    public Observable<DemoEntity> loadMore() {
        return mHttpDataSource.loadMore();
    }

    @Override
    public Observable<BaseResponse<DemoEntity>> demoGet() {
        return mHttpDataSource.demoGet();
    }

    @Override
    public Observable<BaseResponse<DemoEntity>> demoPost(String catalog) {
        return mHttpDataSource.demoPost(catalog);
    }

    /**
     * 账户
     * @param userName
     */
    @Override
    public void saveUserName(String userName) {
        mLocalDataSource.saveUserName(userName);
    }

    /**
     * 密码
     * @param password
     */
    @Override
    public void savePassword(String password) {
        mLocalDataSource.savePassword(password);
    }

    /**
     * 获取账户
     * @return
     */
    @Override
    public String getUserName() {
        return mLocalDataSource.getUserName();
    }

    /**
     * 获取密码
     * @return
     */
    @Override
    public String getPassword() {
        return mLocalDataSource.getPassword();
    }

    /**
     * 退出账户
     * @return
     */
    @Override
    public void SignOutAccount() {
        mLocalDataSource.SignOutAccount();
    }

}
