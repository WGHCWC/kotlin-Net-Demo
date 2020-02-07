package com.wghcwc.kotlindemo2.net;

import java.util.HashMap;

import io.reactivex.disposables.Disposable;


/**
 * @author wghcwc
 * 请求管理,(待完善)
 */
public class RequestManager {
    private static HashMap<Object, HashMap<String, Disposable>> subscriptions;
    private static RequestManager singleInstance = new RequestManager();

    private RequestManager() {
        subscriptions = new HashMap<>();
    }

    public static RequestManager get() {
        return singleInstance;
    }

    public void regist(Object tag) {
        if (!subscriptions.containsKey(tag)) {
            subscriptions.put(tag, new HashMap<String, Disposable>());
        }
    }

    public void unRegist(Object tag) {
        cacelAll(tag);
        if (subscriptions.containsKey(tag)) {
            subscriptions.remove(tag);
        }
    }

    public void cacel(Object tag, String name) {
        try {
            getAll(tag).get(name).dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void add(Object tag, String name, Disposable subscription) {
        try {
            getAll(tag).put(name, subscription);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cacelAll(Object tag) {
        try {
            HashMap<String, Disposable> map = getAll(tag);
            for (Disposable subscription : map.values()) {
                subscription.dispose();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private HashMap<String, Disposable> getAll(Object tag) {
        return subscriptions.get(tag);
    }


}
