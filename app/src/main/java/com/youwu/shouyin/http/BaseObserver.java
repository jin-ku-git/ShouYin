package com.youwu.shouyin.http;

import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.util.EndConsumerHelper;

public abstract class BaseObserver<T> implements Observer<T>, Disposable {
    final AtomicReference<Disposable> upstream = new AtomicReference<Disposable>();

    @Override
    public final void onSubscribe(@NonNull Disposable d) {
        if (EndConsumerHelper.setOnce(this.upstream, d, getClass())) {
            onStart();
        }
    }

    /**
     * Called once the single upstream Disposable is set via onSubscribe.
     */
    protected void onStart() {
    }

    @Override
    public void onError(Throwable e) {
        onError(ApiException.handleException(e));
    }

    @Override
    public final boolean isDisposed() {
        return upstream.get() == DisposableHelper.DISPOSED;
    }

    @Override
    public final void dispose() {
        DisposableHelper.dispose(upstream);
    }

    private void onError(ApiException apiException){
        NetErrorUtils.errorFunction(apiException);
    }

}
