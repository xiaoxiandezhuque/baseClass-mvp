package com.xuhong.baseclass.view;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.xuhong.baseclass.R;
import com.xuhong.baseclass.utils.Constants;
import com.xuhong.baseclass.utils.StringUtils;

import java.io.File;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by BHKJ on 2016/8/8.
 */

public class SelectPhotoDialog {


    public static final int TO_PHOTO_GALLERY = 101;
    public static final int TO_CAMERA = 102;
    public static final int REQUEST_PERMISSION_CAMERA = 103;

    @BindView(R.id.btn_photo_gallery)
    Button btnPhotoGallery;
    @BindView(R.id.btn_camera)
    Button btnCamera;
    @BindView(R.id.btn_cancel)
    Button btnCancel;

    private final AlertDialog mDialog;
    private final WeakReference<Context> mContext;
    private LayoutInflater inflater;

    public Uri getUri() {
        return mUri;
    }

    private Uri mUri;

    public SelectPhotoDialog(Context context) {
        mDialog = new AlertDialog.Builder(context).create();
        mContext = new WeakReference<>(context);

    }

    public void show() {

        if (!mDialog.isShowing()) {
            mDialog.show();
//            View view = inflater.inflate(R.layout.dialog_select_photo,null,false);
            Window window = mDialog.getWindow();
            assert window != null;


            window.setBackgroundDrawableResource(R.drawable.shape_transparent);
            View view = window.getLayoutInflater().inflate(R.layout.dialog_select_photo, null);
            window.setContentView(view);
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setGravity(Gravity.BOTTOM);

            ButterKnife.bind(this, view);


        }
    }

    public void dismiss() {
        if (mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }


    @OnClick({R.id.btn_photo_gallery, R.id.btn_camera, R.id.btn_cancel})
    public void onClick(View view) {
        if (mContext.get() == null) {
            return;
        }
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.btn_photo_gallery:


                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                ((Activity) mContext.get()).startActivityForResult(intent, TO_PHOTO_GALLERY);


                break;
            case R.id.btn_camera:


                //用于android 6.0 检测运行时权限问题
                int hasWriteContactsPermission = ContextCompat.checkSelfPermission(mContext.get(), Manifest.permission.CAMERA);
                if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {

//                    boolean a =ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext.get(), Manifest.permission.CAMERA);
                    if (!ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext.get(), Manifest.permission.CAMERA)) {
                        showMessageOKCancel("You need to allow access to Contacts",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ActivityCompat.requestPermissions((Activity) mContext.get(), new String[]{Manifest.permission.CAMERA},
                                                REQUEST_PERMISSION_CAMERA);
                                    }
                                });
//                        Toast.makeText(mContext.get(), "你需要同意使用相机", Toast.LENGTH_LONG).show();
                        return;
                    }
                    ActivityCompat.requestPermissions((Activity) mContext.get(), new String[]{Manifest.permission.CAMERA},
                            REQUEST_PERMISSION_CAMERA);
                    return;
                }

                //相片保存地址
                String savePath = "";
                String storageState = Environment.getExternalStorageState();
                if (storageState.equals(Environment.MEDIA_MOUNTED)) {
                    savePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + Constants.CACHE_PATH;
                    File savedir = new File(savePath);
                    if (!savedir.exists()) {
                        savedir.mkdirs();
                    }
                }
                // 没有挂载SD卡，无法保存文件
                if (StringUtils.isEmpty(savePath)) {
                    Toast.makeText(mContext.get(), "无法保存照片，请检查SD卡是否挂载", Toast.LENGTH_LONG).show();
                    return;
                }

                // 根据文件地址创建文件
                String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                String fileName = timeStamp + ".jpg";// 照片命名
                File out = new File(savePath, fileName);
                mUri = Uri.fromFile(out);

                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);

                ((Activity) mContext.get()).startActivityForResult(intent, TO_CAMERA);

                break;
            case R.id.btn_cancel:

                break;
        }
        mDialog.dismiss();
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(mContext.get())
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

//    public void crop(Uri uri) {
//        // 裁剪图片意图
//        Intent intent = new Intent("com.android.camera.action.CROP");
//        intent.setDataAndType(uri, "image/*");
//        intent.putExtra("output", this.getUploadTempFile(uri));
//        intent.putExtra("crop", "true");
//        intent.putExtra("aspectX", 1);// 裁剪框比例
//        intent.putExtra("aspectY", 1);
//        intent.putExtra("outputX", 200);// 输出图片大小
//        intent.putExtra("outputY", 200);
//        intent.putExtra("scale", true);// 去黑边
//        intent.putExtra("scaleUpIfNeeded", true);// 去黑边
//        startActivityForResult(intent, ImageUtils.REQUEST_CODE_GETIMAGE_BYSDCARD);
//        dialog.dismiss();// 头像选择完成，dialog消失
//    }
}
