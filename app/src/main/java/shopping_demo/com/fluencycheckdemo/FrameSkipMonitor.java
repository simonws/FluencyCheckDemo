package shopping_demo.com.fluencycheckdemo;

import android.annotation.TargetApi;
import android.os.Build;
import android.util.Log;
import android.view.Choreographer;

import java.util.HashMap;
import java.util.Map;


@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class FrameSkipMonitor implements Choreographer.FrameCallback {
    protected final String TAG = "FrameSkipMonitor";
    private static final long ONE_FRAME_TIME = 16600000; // 1 Frame time cost
    private static final long MIN_FRAME_TIME = ONE_FRAME_TIME * 3; // 3 Frame time cost
    private static final long MAX_FRAME_TIME = 60 * ONE_FRAME_TIME; // 60 Frame time cost, not record some special cases.

    private static final String SKIP_EVENT_NAME = "frame_skip";

    private static FrameSkipMonitor sInstance;

    private long mLastFrameNanoTime = 0;
    private HashMap<String, Long> mSkipRecordMap;
    private HashMap<String, Long> mActivityShowTimeMap;
    private String mActivityName;
    private long mActivityStartTime = 0;

    private FrameSkipMonitor() {
        mSkipRecordMap = new HashMap<>();
        mActivityShowTimeMap = new HashMap<>();
    }

    public static FrameSkipMonitor getInstance() {
        if (sInstance == null) {
            sInstance = new FrameSkipMonitor();
        }
        return sInstance;
    }

    public void setActivityName(String activityName) {
        mActivityName = activityName;
    }

    public void start() {
        Choreographer.getInstance().postFrameCallback(FrameSkipMonitor.getInstance());
    }

    @Override
    public void doFrame(long frameTimeNanos) {
//        Log.d(TAG, "doFrame");
        if (mLastFrameNanoTime != 0) {
            long frameInterval = frameTimeNanos - mLastFrameNanoTime;
            if (frameInterval > MIN_FRAME_TIME && frameInterval < MAX_FRAME_TIME) {
                long time = 0;
                if (mSkipRecordMap.containsKey(mActivityName)) {
                    time = mSkipRecordMap.get(mActivityName);
                }

                Log.d(TAG, "doFrame：" + mActivityName + " time：" + (time + frameInterval));
                mSkipRecordMap.put(mActivityName, time + frameInterval);
            }
        }
        mLastFrameNanoTime = frameTimeNanos;
        Choreographer.getInstance().postFrameCallback(this);
    }

    public void report() {
        Choreographer.getInstance().removeFrameCallback(this);
        for (Map.Entry<String, Long> entry :
            mSkipRecordMap.entrySet()) {
            Log.d(TAG, "page：" + entry.getKey() + " time：" + (long) entry.getValue() / ONE_FRAME_TIME);
        }
        mSkipRecordMap.clear();
    }

    public void OnActivityResume() {
        mActivityStartTime = System.currentTimeMillis();
    }

    public void OnActivityPause() {
        long activityShowInterval = System.currentTimeMillis() - mActivityStartTime;
        long time = 0;
        if (mActivityShowTimeMap.containsKey(mActivityName)) {
            time = mActivityShowTimeMap.get(mActivityName);
        }
        mActivityShowTimeMap.put(mActivityName, time + activityShowInterval);
    }
}
