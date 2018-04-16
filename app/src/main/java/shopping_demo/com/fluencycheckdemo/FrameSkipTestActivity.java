package shopping_demo.com.fluencycheckdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class FrameSkipTestActivity extends AppCompatActivity {
    List<RecyclerItem> mList = new ArrayList<>();
    RecyclerView recyclerView;
    RecyclerAdapter adapter;
    Button btnShowRecycleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);

        btnShowRecycleView = (Button) findViewById(R.id.btn_show_recyclerview);
        btnShowRecycleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isDelay = true;
                showRecycleView(isDelay);
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
    }

    /**
     * @param isNeedDelay 绘制item的时候，是否需要故意延时，来体现掉帧统计功能
     */
    private void showRecycleView(boolean isNeedDelay) {
        RecyclerItem item;
        for (int i = 0; i < 100; i++) {
            item = new RecyclerItem();
            item.setImg("http://fanyi.baidu.com/appdownload/download.html");
            item.setTitle("title :" + i);
            mList.add(item);
        }

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        recyclerView.setLayoutManager(gridLayoutManager);
        adapter = new RecyclerAdapter(mList, isNeedDelay);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
