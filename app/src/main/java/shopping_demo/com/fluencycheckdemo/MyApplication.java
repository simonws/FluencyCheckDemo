package shopping_demo.com.fluencycheckdemo;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import java.util.List;


public class MyApplication extends Application {

    private static Application mInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
        registerActivityLifecycleCallbacks(MyActivityLifeCycle.getInstance());
    }

    public static Application getApplication() {
        return mInstance;
    }

    // 应用是否在前台
    public boolean isAppOnForeground() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return MyActivityLifeCycle.getInstance().isForground();
        } else {
            ActivityManager mActivityManager = ((ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE));
            List<ActivityManager.RunningTaskInfo> taskInfos = mActivityManager.getRunningTasks(1);
            return null != taskInfos
                && taskInfos.size() > 0
                && TextUtils.equals(this.getPackageName(),
                taskInfos.get(0).topActivity.getPackageName());
        }
    }

    public Activity getCurrentActivity() {
        return MyActivityLifeCycle.getInstance().getCurrentActivity();
    }
}
