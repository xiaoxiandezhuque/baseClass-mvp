package com.xuhong.baseclass.template.selectmorepicture.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;

import com.xuhong.baseclass.R;
import com.xuhong.baseclass.activity.BaseActivity;
import com.xuhong.baseclass.template.selectmorepicture.fragment.SelectFragment;
import com.xuhong.baseclass.template.selectmorepicture.SelectImageContract;

import java.io.Serializable;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by huanghaibin_dev
 * on 2016/7/13.
 */
@SuppressWarnings("All")
public class SelectImageActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks, SelectImageContract.Operator {
    private static final int RC_CAMERA_PERM = 0x03;
    private static final int RC_EXTERNAL_STORAGE = 0x04;
    public static final String KEY_CONFIG = "config";

    private static volatile Callback mCallbackSnapshot;
    private SelectImageContract.View mView;
    private Callback mCallback;
    private Config mConfig;

    public SelectImageActivity() {
        Callback callback = mCallbackSnapshot;
        if (callback == null) {
            throw new NullPointerException("SelectImageActivity's Callback isn't set null.");
        }
        mCallback = callback;
        mCallbackSnapshot = null;
    }

    public static void showImage(Context context, int selectCount, boolean haveCamera, String[] selectedImages, Callback callBack) {
        if (callBack == null)
            throw new NullPointerException("SelectImageActivity's Callback isn't set null.");

        if (selectCount <= 0)
            throw new RuntimeException("SelectCount must >= 1");

        mCallbackSnapshot = callBack;

        // Set config
        Config config = new Config();
        config.selectCount = selectCount;
        config.haveCamera = haveCamera;
        config.selectedImages = selectedImages;

        Intent intent = new Intent(context, SelectImageActivity.class);
        intent.putExtra(KEY_CONFIG, config);
        context.startActivity(intent);
    }

//    @Override
//    protected boolean initBundle(Bundle bundle) {
//        Serializable serializable = bundle.getSerializable(KEY_CONFIG);
//        if (serializable == null && serializable instanceof Config) {
//            return false;
//        } else {
//            mConfig = (Config) serializable;
//            // We must need set one result
//            return mConfig.getSelectCount() >= 1;
//        }
//    }


    @AfterPermissionGranted(RC_CAMERA_PERM)
    @Override
    public void requestCamera() {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.CAMERA)) {
            if (mView != null) {
                mView.onOpenCameraSuccess();
            }
        } else {
            EasyPermissions.requestPermissions(this, "", RC_CAMERA_PERM, Manifest.permission.CAMERA);
        }
    }

    @AfterPermissionGranted(RC_EXTERNAL_STORAGE)
    @Override
    public void requestExternalStorage() {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            if (mView == null) {
                handleView();
            }
        } else {
            EasyPermissions.requestPermissions(this, "", RC_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void onBack() {
        onSupportNavigateUp();
    }

    @Override
    public void setDataView(SelectImageContract.View view) {
        mView = view;
    }

    @Override
    public Callback getCallback() {
        return mCallback;
    }

    @Override
    public Config getConfig() {
        return mConfig;
    }

    @Override
    protected void onDestroy() {
        mConfig = null;
        mCallback = null;
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    @Override
    public boolean shouldShowRequestPermissionRationale(String permission) {
        return false;
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (requestCode == RC_EXTERNAL_STORAGE) {
            removeView();
            getConfirmDialog(this, "没有权限, 你需要去设置中开启读取手机存储权限.", "去设置", "取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(Settings.ACTION_APPLICATION_SETTINGS));
                    finish();
                }
            }, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            }).show();
        } else {
            if (mView != null)
                mView.onCameraPermissionDenied();
            getConfirmDialog(this, "没有权限, 你需要去设置中开启相机权限.", "去设置", "取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(Settings.ACTION_APPLICATION_SETTINGS));
                }
            }, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            }).show();
        }
    }

    private void removeView() {
        SelectImageContract.View view = mView;
        if (view == null)
            return;
        try {
            getSupportFragmentManager()
                    .beginTransaction()
                    .remove((Fragment) view)
                    .commitAllowingStateLoss();
//                    .commitNowAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleView() {
        try {
            Fragment fragment = Fragment.instantiate(this, SelectFragment.class.getName());
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fl_content, fragment)
                    .commitAllowingStateLoss();
//                    .commitNowAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static AlertDialog.Builder getConfirmDialog(Context context, String message, String
            okString, String cancelString,
                                                       DialogInterface.OnClickListener
                                                               okClickListener,
                                                       DialogInterface.OnClickListener
                                                               cancelClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setPositiveButton(okString, okClickListener);
        builder.setNegativeButton(cancelString, cancelClickListener);
        builder.setCancelable(false);
        return builder;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_select_image;
    }

    @Override
    protected void initView() {
        Serializable serializable = getIntent().getExtras().getSerializable(KEY_CONFIG);

        mConfig = (Config) serializable;

        requestExternalStorage();
    }

    public static class Config implements Serializable {
        private int selectCount;
        private boolean haveCamera;
        private String[] selectedImages;

        public int getSelectCount() {
            return selectCount;
        }

        public void setSelectCount(int selectCount) {
            this.selectCount = selectCount;
        }

        public boolean isHaveCamera() {
            return haveCamera;
        }

        public void setHaveCamera(boolean haveCamera) {
            this.haveCamera = haveCamera;
        }

        public String[] getSelectedImages() {
            return selectedImages;
        }

        public void setSelectedImages(String[] selectedImages) {
            this.selectedImages = selectedImages;
        }
    }

    public static interface Callback {
        void doSelectDone(String[] images);
    }
}
