package com.steelkiwi.cropiwa;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.steelkiwi.cropiwa.util.Permissions;
import com.wt.app.photopreview.PhotoPreview;
import com.wt.app.photopreview.loader.ImageLoader;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by wuguilin on 6/2/2017.
 */

public class CropActivityDemo extends AppCompatActivity {

    private static final int REQUEST_CAMERA_PHOTO = 1100;
    private static final int REQUEST_CHOOSE_PHOTO = 1101;
    private static final int REQUEST_CROP_PHOTO = 1102;
    private static final int REQUEST_STORAGE_PERMISSION = 9;

    private static final String TAG_CHOOSE_IMAGE_FRAGMENT = "choose_image";


    private Button btn;
    private ImageView imageView;

    private Uri uri;
    private PopupWindow popupWindow;

    private static final String PHOTO_FILE_NAME = "temp_photo.png";
    private File tempFile;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_crop_demo);

        initPopWindow();

        btn = (Button) findViewById(R.id.button);

        imageView = (ImageView) findViewById(R.id.imageView);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startGalleryApp();


                // 设置好参数之后再show
                popupWindow.showAtLocation(v, Gravity.BOTTOM,0,0);

            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uri != null){
                    ArrayList<String> imageList = new ArrayList<String>();
                    imageList.add(uri.getPath());


                    PhotoPreview.builder()
                            .setPhotos(imageList)
                            .setShowDeleteButton(false)
                            .setImageLoader(new ImageLoader() {
                                @Override
                                public void displayImage(ImageView imageView, String path) {
                                    Glide.with(imageView.getContext()).load(path).thumbnail(0.3f).into(imageView);
                                }

                                @Override
                                public void displayImage(ImageView imageView, Uri uri) {
                                    Glide
                                            .with(imageView.getContext()).load(uri)
                                            .thumbnail(0.1f)
                                            .dontAnimate()
                                            .dontTransform()
                                            .override(800, 800)
                                            .into(imageView);
                                }

                                @Override
                                public void clear(View view) {

                                    Glide.clear(view);

                                }
                            })

                    .start(CropActivityDemo.this);

                }

            }
        });
    }

    private void initPopWindow(){
        View contentView = View.inflate(CropActivityDemo.this,
                R.layout.choose_for_crop, null);

        TextView camera = (TextView) contentView.findViewById(R.id.btn_camera_image);
        TextView gallary = (TextView) contentView.findViewById(R.id.btn_from_gallery);

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();

                startCamera();

                //Toast.makeText(CropActivityDemo.this, "get img from camera", Toast.LENGTH_SHORT).show();

            }
        });

        gallary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startGalleryApp();
                popupWindow.dismiss();
            }
        });




        popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT,true);

        popupWindow.setFocusable(true);

        ColorDrawable dw = new ColorDrawable(0xb0000000);
        popupWindow.setBackgroundDrawable(dw);
        
       


/*        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                Log.i("mengdd", "onTouch : ");

                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });*/

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CHOOSE_PHOTO && resultCode == RESULT_OK) {
            startCropActivity(data.getData());
        }
        else if (requestCode == REQUEST_CAMERA_PHOTO && resultCode == RESULT_OK) {
            startCropActivity(Uri.fromFile(tempFile));
        }else if (requestCode == REQUEST_CROP_PHOTO && resultCode == RESULT_OK) {
            if (data.getExtras() != null) {
                uri = data.getParcelableExtra("data");
                imageView.setImageURI(uri);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_STORAGE_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startGalleryApp();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void startGalleryApp() {
        if (Permissions.isGranted(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            intent = Intent.createChooser(intent, getString(R.string.title_choose_image));
            startActivityForResult(intent, REQUEST_CHOOSE_PHOTO);
        } else {
            requestPermissions(
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_STORAGE_PERMISSION);
        }
    }

    private void startCamera() {
        // 激活相机
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        // 判断存储卡是否可以用，可用进行存储
        if (hasSdcard()) {
            tempFile = new File(Environment.getExternalStorageDirectory(), PHOTO_FILE_NAME);
            // 从文件中创建uri
            Uri uri = Uri.fromFile(tempFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CAREMA
        startActivityForResult(intent, REQUEST_CAMERA_PHOTO);
    }

    /*
* 判断sdcard是否被挂载
*/
    private boolean hasSdcard() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }


//    private Uri getRandomImageUri() {
//        Random sizeRand = new Random();
//        int max = 1400;
//        int width = (600 + sizeRand.nextInt(max));
//        int height = (600 + sizeRand.nextInt(max));
//        width -= (width % 100);
//        height -= (height % 100);
//        String url = String.format(Locale.US, "http://lorempixel.com/%d/%d/", width, height);
//        return Uri.parse(url);
//    }

    private void startCropActivity(Uri uri) {
        startActivityForResult(CropActivity.callingIntent(this, uri), REQUEST_CROP_PHOTO);

    }
}
