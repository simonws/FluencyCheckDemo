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


    private static FrameSkipMonitor sInstance;

    private long mLastFrameNanoTime = 0;

    private FrameSkipMonitor() {
    }

    public static FrameSkipMonitor getInstance() {
        if (sInstance == null) {
            sInstance = new FrameSkipMonitor();
        }
        return sInstance;
    }

    public void start() {
        Choreographer.getInstance().postFrameCallback(getInstance());
    }

    @Override
    public void doFrame(long frameTimeNanos) {
//        Log.d(TAG, "doFrame");
        if (mLastFrameNanoTime != 0) {
            long frameInterval = frameTimeNanos - mLastFrameNanoTime;
            if (frameInterval > MIN_FRAME_TIME && frameInterval < MAX_FRAME_TIME) {
                Log.d(TAG, "doFrame frameInterval：" +  frameInterval/1000000);
            }
        }
        mLastFrameNanoTime = frameTimeNanos;
        Choreographer.getInstance().postFrameCallback(this);
    }

    public void report() {
        Choreographer.getInstance().removeFrameCallback(this);
    }
}
