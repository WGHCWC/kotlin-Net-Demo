package com.wghcwc.kotlindemo2.net.observer;


import io.reactivex.disposables.Disposable;

/**
 * @author wghcwc
 * @date 19-12-18
 * <p>
 * 带加载动画
 */
public abstract class ProcessObserver<T> extends BaseObserver<T> {
    private String info;

    public ProcessObserver(String info) {
        this.info = info;
    }

    public ProcessObserver() {
    }

    @Override
    public void onSubscribe(Disposable d) {
        if (info == null) {
//            LoadingHelper.showWith("加载中");
        } else if (info.isEmpty()) {
//            LoadingHelper.show();
        } else {
//            LoadingHelper.showWith(info);
        }
    }

}
