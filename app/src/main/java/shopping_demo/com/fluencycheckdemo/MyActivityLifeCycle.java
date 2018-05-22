/*
 * Copyright (C) 2011-2016 Husor Inc.
 * All Rights Reserved.
 */

package shopping_demo.com.fluencycheckdemo;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;


import java.lang.ref.WeakReference;

public class MyActivityLifeCycle implements Application.ActivityLifecycleCallbacks {
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private boolean mPaused = true;
    private Runnable mCheckForegroundRunnable;
    private boolean mForeground = false;
    private static MyActivityLifeCycle sInstance;

    protected final String TAG = "MyActivityLifeCycle";


    private MyActivityLifeCycle() {
    }

    public static synchronized MyActivityLifeCycle getInstance() {
        if (sInstance == null) {
            sInstance = new MyActivityLifeCycle();
        }
        return sInstance;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        Log.d(TAG, activity.getLocalClassName() + " " + "onActivityCreated");
    }

    @Override
    public void onActivityStarted(Activity activity) {
        Log.d(TAG, activity.getLocalClassName() + " " + "onActivityStarted");
    }

    @Override
    public void onActivityResumed(Activity activity) {
        mPaused = false;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            if (!mForeground) {
                FrameSkipMonitor.getInstance().start();
            }
        }
        mForeground = true;
        if (mCheckForegroundRunnable != null) {
            mHandler.removeCallbacks(mCheckForegroundRunnable);
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {//pause事件后是否在前台要分情况判断
        mPaused = true;
        if (mCheckForegroundRunnable != null) {
            mHandler.removeCallbacks(mCheckForegroundRunnable);
        }

        mHandler.postDelayed(mCheckForegroundRunnable = new Runnable() {
            @Override
            public void run() {
                if (mPaused && mForeground) {
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN
                             /*&& ConfigManager.getInstance().isFrameSkipCheckOn()*//*配置是否开启统计*/) {
                        FrameSkipMonitor.getInstance().report();
                    }
                    mForeground = false;
                }
            }
        }, 1000);

    }

    @Override
    public void onActivityStopped(Activity activity) {
        Log.d(TAG, activity.getLocalClassName() + " " + "onActivityStopped");
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        Log.d(TAG, activity.getLocalClassName() + " " + "onActivityDestroyed");
    }

}
