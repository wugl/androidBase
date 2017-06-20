package com.wt.app.banner;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.wt.app.banner.bean.Entity;
import com.wt.app.banner.view.RecyclerBanner;

import java.util.ArrayList;
import java.util.List;

public class ViewDemoActivity extends AppCompatActivity {
    private static final String TAG = "ViewDemoActivity";
    private RecyclerBanner banner;
    private List<RecyclerBanner.BannerEntity> urls = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_demo);


        banner = (RecyclerBanner) findViewById(R.id.slide_banner);
        banner.setOnPagerClickListener(new RecyclerBanner.OnPagerClickListener() {
            @Override
            public void onClick(RecyclerBanner.BannerEntity entity) {
                //Toast.makeText(MainActivity.this, entity.getUrl(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "onClick: "+ entity.getUrl() );
            }
        });

        urls.add(new Entity("http://pic.58pic.com/58pic/12/46/13/03B58PICXxE.jpg"));
        urls.add(new Entity("http://www.jitu5.com/uploads/allimg/121120/260529-121120232T546.jpg"));
        urls.add(new Entity("http://pic34.nipic.com/20131025/2531170_132447503000_2.jpg"));
        urls.add(new Entity("http://img5.imgtn.bdimg.com/it/u=3462610901,3870573928&fm=206&gp=0.jpg"));

        banner.setDatas(urls);
    }
}
