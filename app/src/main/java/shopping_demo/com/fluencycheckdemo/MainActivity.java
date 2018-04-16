package shopping_demo.com.fluencycheckdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Printer;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    String TAG = "Main_Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.looper_printer:
                getMainLooper().setMessageLogging(new Printer() {
                    @Override
                    public void println(String x) {
                        Log.d(TAG, System.currentTimeMillis() + " == " + x);
                    }
                });
                break;
            case R.id.frame_call_back:
                Intent intent = new Intent();
                intent.setClassName(MainActivity.this, FrameSkipTestActivity.class.getName());
                startActivity(intent);
                break;
            default:
                break;
        }

    }
}
