package com.steelkiwi.cropiwa;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.steelkiwi.cropiwa.util.Permissions;
import com.steelkiwi.cropiwa.util.PermissionsUtils;

/**
 * Created by wuguilin on 6/2/2017.
 */

public class CropBuild {

    public static final int REQUEST_CODE = 233;

    private static final int REQUEST_CHOOSE_PHOTO = 1101;
    private static final int REQUEST_CROP_PHOTO = 1102;
    private static final int REQUEST_STORAGE_PERMISSION = 9;

    private static CropBuild instance;

    private Bundle mPickerOptionsBundle;
    private Intent mPickerIntent;

    private CropBuild(){

        mPickerOptionsBundle = new Bundle();
        mPickerIntent = new Intent();

    }

    public static CropBuild getInstance(){
        if(instance == null){
            instance = new CropBuild();
        }
        return instance;
    }

    public void start(@NonNull Activity activity, int requestCode) {
        if (PermissionsUtils.checkReadStoragePermission(activity)) {
            activity.startActivityForResult(getIntent(activity), requestCode);
        }
    }


    public void start(@NonNull Activity activity) {
        //start(activity, REQUEST_CODE);

        startGalleryApp(activity);
    }

    public Intent getIntent(@NonNull Context context) {
        mPickerIntent.setClass(context, CropActivity.class);
        mPickerIntent.putExtras(mPickerOptionsBundle);
        return mPickerIntent;
    }
    @TargetApi(Build.VERSION_CODES.M)
    private void startGalleryApp(@NonNull Activity activity) {
        if (Permissions.isGranted(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            intent = Intent.createChooser(intent, activity.getString(R.string.title_choose_image));
            activity.startActivityForResult(intent, REQUEST_CHOOSE_PHOTO);
        } else {
            activity.requestPermissions(
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_STORAGE_PERMISSION);
        }
    }
    private void startCropActivity(@NonNull Activity activity,Uri uri) {
        activity.startActivityForResult(CropActivity.callingIntent(activity, uri),REQUEST_CROP_PHOTO);

    }
}
