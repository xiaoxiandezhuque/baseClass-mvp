package com.xuhong.baseclass.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;

import com.xuhong.baseclass.R;
import com.xuhong.baseclass.presenter.BasePresenter;
import com.xuhong.baseclass.ui.iview.IHeadView;
import com.xuhong.baseclass.utils.ImageLoaderUtils;
import com.xuhong.baseclass.view.SelectPhotoDialog;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by BHKJ on 2016/8/8.
 */

public class HeadActivity extends BaseActivity implements IHeadView {
    @BindView(R.id.iv_head)
    ImageView ivHead;

    private SelectPhotoDialog mSelectPhotoDialog;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_head;
    }

    @Override
    protected void initView() {
        Log.e("shape_transparent", "shape_transparent");
        ImageLoaderUtils.displayImageAvatar(this,
                "http://f1.diyitui.com/34/3b/af/57/08/1e/39/c3/d8/8e/c8/6e/35/34/f3/c0.jpg"
                , ivHead);


    }

    @Override
    protected BasePresenter createdPresenter() {
        return null;
    }


    @OnClick(R.id.iv_head)
    public void onClick() {
        if (mSelectPhotoDialog == null) {
            mSelectPhotoDialog = new SelectPhotoDialog(this);
        }
        mSelectPhotoDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case SelectPhotoDialog.TO_PHOTO_GALLERY:
                if (data != null) {
                    Uri uri = data.getData();
                    ImageLoaderUtils.displayImageAvatar(this, uri, ivHead);
                }
                break;
            case SelectPhotoDialog.TO_CAMERA:
                Uri uri = mSelectPhotoDialog.getUri();
                if (uri != null) {
                    ImageLoaderUtils.displayImageAvatar(this, uri, ivHead);

                }
                break;
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == SelectPhotoDialog.REQUEST_PERMISSION_CAMERA){
            if (permissions[0].equals(Manifest.permission.CAMERA) &&grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //成功
            }
        }

    }

}
