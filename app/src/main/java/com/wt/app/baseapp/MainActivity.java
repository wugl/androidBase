package com.wt.app.baseapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.hankkin.library.ListPopWindow;
import com.hankkin.library.PopBean;
import com.ody.library.util.util.PinyinUtils;
import com.steelkiwi.cropiwa.CropActivityDemo;
import com.wt.app.banner.ViewDemoActivity;
import com.wt.app.selectdate.view.SelectAddressPopWindow;
import com.wt.app.selectdate.view.SelectDatePopWindow;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener , ListPopWindow.OnPopItemClickListener,ListPopWindow.OnBottomTextviewClickListener {

    private static final String TAG = "MainActivity";
    private ListPopWindow popWindow;
    private LinearLayout container;
    @Override
    public void onBottomClick() {
        popWindow.dismiss();
    }

    @Override
    public void onPopItemClick(View view, int position) {
        Toast.makeText(this, "click index:"+position, Toast.LENGTH_SHORT).show();
        switch (position){

            case 0:
                Log.d("tag>>>>>","click index:"+position);

                break;
            case 1:
                Log.d("tag>>>>>","click index:"+position);
                break;
            case 2:
                Log.d("tag>>>>>","click index:"+position);
                break;
            case 3:
                Log.d("tag>>>>>","click index:"+position);
                break;
            case 4:
                Log.d("tag>>>>>","click index:"+position);
                break;
            default:
                break;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        container = (LinearLayout) findViewById(R.id.container);


        Log.e(TAG, "onCreate: "+ PinyinUtils.getPinyinFirstLetters("中华人民") );


        findViewById(R.id.button).setOnClickListener(this);
        findViewById(R.id.button2).setOnClickListener(this);
        findViewById(R.id.button3).setOnClickListener(this);

        findViewById(R.id.button4).setOnClickListener(this);
        findViewById(R.id.button5).setOnClickListener(this);
        findViewById(R.id.button6).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button:
                SelectDatePopWindow selectDate = new SelectDatePopWindow(MainActivity.this);
                selectDate.showAtLocation(v, Gravity.BOTTOM, 0, 0);

                selectDate.setDatekListener(new SelectDatePopWindow.OnDateCListener() {
                    @Override
                    public void onClick(String year, String month, String day, String hour, String minute) {
                        Log.e(TAG, "onClick: " + year + ":"+month +":"+day +":"+hour +":"+minute );
                    }
                });


                break;



            case R.id.button2:
                SelectAddressPopWindow selectAddress = new SelectAddressPopWindow(MainActivity.this,"北京","海淀区","");
                //selectAddress.setAddress("北京","海淀区","");
                selectAddress.showAtLocation(v, Gravity.BOTTOM, 0, 0);
                selectAddress.setAddresskListener(new SelectAddressPopWindow.OnAddressCListener() {
                    @Override
                    public void onClick(String province, String city, String area) {
                        Log.e(TAG, "onClick: "+ province + ":" + city +":"+area );
                    }
                });



                break;
            case R.id.button3:

                new IntentIntegrator(this)
                        .setOrientationLocked(false)
                        .initiateScan();
                break;
            case R.id.button4:

                Intent intent = new Intent(MainActivity.this,CropActivityDemo.class);

                startActivity(intent);


                break;
            case R.id.button5:

                List<PopBean> pops = new ArrayList<>();
                for (int i=0;i<5;i++){
                    PopBean pop = new PopBean("item"+i,0);
                    pops.add(pop);
                }
                popWindow = new ListPopWindow(MainActivity.this,this,this,container,pops,"cancel","title");
                popWindow.showAtLocation(container, Gravity.CENTER| Gravity.BOTTOM,0,0);

//                PhotoPicker.builder()
//                        .setGridColumnCount(4)
//                        .setPhotoCount(1)
//
//                        .setImageLoader(new ImageLoader() {
//                            @Override
//                            public void displayImage(ImageView imageView, String path) {
//                                Glide.with(imageView.getContext()).load(path).thumbnail(0.3f).into(imageView);
//                            }
//
//                            @Override
//                            public void displayImage(ImageView imageView, Uri uri) {
//                                Glide
//                                        .with(imageView.getContext()).load(uri)
//                                        .thumbnail(0.1f)
//                                        .dontAnimate()
//                                        .dontTransform()
//                                        .override(800, 800)
//                                        .into(imageView);
//                            }
//
//                            @Override
//                            public void clear(View view) {
//                                GlideUtil.clear(view);
//                            }
//                        })
//                        .start(MainActivity.this);


                break;

            case R.id.button6:

                startActivity(new Intent(MainActivity.this,ViewDemoActivity.class));



                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
