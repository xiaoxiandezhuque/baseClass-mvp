package com.xuhong.baseclass.template.selectmorepicture.fragment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.xuhong.baseclass.R;
import com.xuhong.baseclass.ui.adapter.OnItemClickListener;
import com.xuhong.baseclass.template.selectmorepicture.ImageFolderPopupWindow;
import com.xuhong.baseclass.template.selectmorepicture.ImageLoaderListener;
import com.xuhong.baseclass.template.selectmorepicture.MediaStoreDataFactory;
import com.xuhong.baseclass.template.selectmorepicture.SelectImageContract;
import com.xuhong.baseclass.template.selectmorepicture.Util;
import com.xuhong.baseclass.template.selectmorepicture.activity.ImageGalleryActivity;
import com.xuhong.baseclass.template.selectmorepicture.activity.SelectImageActivity;
import com.xuhong.baseclass.template.selectmorepicture.adapter.ImageAdapter;
import com.xuhong.baseclass.template.selectmorepicture.adapter.ImageFolderAdapter;
import com.xuhong.baseclass.template.selectmorepicture.adapter.SpaceGridItemDecoration;
import com.xuhong.baseclass.template.selectmorepicture.bean.Image;
import com.xuhong.baseclass.template.selectmorepicture.bean.ImageFolder;

import net.qiujuer.genius.ui.Ui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 图片选择库实现界面
 */
public class SelectFragment extends Fragment implements SelectImageContract.View, View.OnClickListener,
        ImageLoaderListener, MediaStoreDataFactory.PictureSourceCallback, MediaStoreDataFactory.FolderSourceCallback {
    @BindView(R.id.rv_image)
    RecyclerView mContentView;
    @BindView(R.id.btn_title_select)
    Button mSelectFolderView;
    @BindView(R.id.iv_title_select)
    ImageView mSelectFolderIcon;
    @BindView(R.id.toolbar)
    View mToolbar;
    @BindView(R.id.btn_done)
    Button mDoneView;
    @BindView(R.id.btn_preview)
    Button mPreviewView;

    private ImageFolderPopupWindow mFolderPopupWindow;
    private ImageFolderAdapter mImageFolderAdapter;
    private ImageAdapter mImageAdapter;

    private List<Image> mSelectedImage;

    private String mCamImageName;
    private LoaderListener mCursorLoader = new LoaderListener();

    private SelectImageContract.Operator mOperator;
    private MediaStoreDataFactory mFactory;

    private List<Image> mData;
    private List<ImageFolder> mImageFolders;

    @Override
    public void onAttach(Context context) {
        this.mOperator = (SelectImageContract.Operator) context;
        this.mOperator.setDataView(this);
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
     View   mView = inflater.inflate(getLayoutId(), container, false);
        ButterKnife.bind(this, mView);

        lazyLoad();
        return mView;
    }


    protected void lazyLoad() {
        mData = new ArrayList<>();
        mImageFolders = new ArrayList<>();
        mContentView.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        mContentView.addItemDecoration(new SpaceGridItemDecoration((int) Ui.dipToPx(getResources(), 1)));
        mImageAdapter = new ImageAdapter(getContext(), this, mData);
        mImageFolderAdapter = new ImageFolderAdapter(getActivity());
        mImageFolderAdapter.setLoader(this);
        mContentView.setAdapter(mImageAdapter);
        mContentView.setItemAnimator(null);
        mImageAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onClick(View v, int position) {
                SelectImageActivity.Config config = mOperator.getConfig();
                if (config.isHaveCamera()) {
                    if (position != 0) {
                        handleSelectChange(position);
                    } else {
                        if (mSelectedImage.size() < config.getSelectCount()) {
                            mOperator.requestCamera();
                        } else {
                            Toast.makeText(getActivity(), "最多只能选择 " + config.getSelectCount() + " 张图片", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    handleSelectChange(position);
                }
            }
        });


        mSelectedImage = new ArrayList<>();
        SelectImageActivity.Config config = mOperator.getConfig();
        if (config.getSelectCount() > 1 && config.getSelectedImages() != null) {
            String[] images = config.getSelectedImages();
            for (String s : images) {
                // check file exists
                if (s != null && new File(s).exists()) {
                    Image image = new Image();
                    image.setSelect(true);
                    image.setPath(s);
                    mSelectedImage.add(image);
                }
            }
        }
        getLoaderManager().initLoader(0, null, mCursorLoader);

        mFactory = new MediaStoreDataFactory(getContext(), this, this);
        getLoaderManager().initLoader(1, null, mFactory);
    }


    protected int getLayoutId() {
        return R.layout.fragment_select_image;
    }

    @OnClick({R.id.btn_preview, R.id.icon_back, R.id.btn_title_select, R.id.btn_done})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.icon_back:
                mOperator.onBack();
                break;
            case R.id.btn_preview:
                if (mSelectedImage.size() > 0) {

                    ImageGalleryActivity.show(getActivity(), Util.toArray(mSelectedImage), 0, false);
                }
                break;
            case R.id.btn_title_select:
                showPopupFolderList();
                break;
            case R.id.btn_done:
                onSelectComplete();
                break;
        }
    }


    private void handleSelectSizeChange(int size) {
        if (size > 0) {
            mPreviewView.setEnabled(true);
            mDoneView.setEnabled(true);
            mDoneView.setText(String.format("%s(%s)", "完成", size));
        } else {
            mPreviewView.setEnabled(false);
            mDoneView.setEnabled(false);
            mDoneView.setText("完成");
        }
    }

    private void handleSelectChange(int position) {
        Image image = mData.get(position);
        SelectImageActivity.Config config = mOperator.getConfig();
        //如果是多选模式
        final int selectCount = config.getSelectCount();
        if (selectCount > 1) {
            if (image.isSelect()) {
                image.setSelect(false);
                mSelectedImage.remove(image);
                if (mImageAdapter.getItemCount() > position) {
                    mImageAdapter.notifyItemChanged(position);
                }
            } else {
                if (mSelectedImage.size() == selectCount) {
                    Toast.makeText(getActivity(), "最多只能选择 " + selectCount + " 张照片", Toast.LENGTH_SHORT).show();
                } else {
                    image.setSelect(true);
                    mSelectedImage.add(image);
                    if (mImageAdapter.getItemCount() > position) {
                        mImageAdapter.notifyItemChanged(position);
                    }

                }
            }
            handleSelectSizeChange(mSelectedImage.size());
        } else {
            mSelectedImage.add(image);
            handleResult();
        }
    }

    private void handleResult() {
        if (mOperator != null && mSelectedImage.size() != 0) {
            mOperator.getCallback().doSelectDone(Util.toArray(mSelectedImage));
            getActivity().finish();
        }
    }

    /**
     * 完成选择
     */
    public void onSelectComplete() {
        handleResult();
    }

    /**
     * 申请相机权限成功
     */
    @Override
    public void onOpenCameraSuccess() {
        toOpenCamera();
    }


    @Override
    public void onCameraPermissionDenied() {

    }

    /**
     * 创建弹出的相册
     */
    private void showPopupFolderList() {
        if (mFolderPopupWindow == null) {
            ImageFolderPopupWindow popupWindow = new ImageFolderPopupWindow(getActivity(), new ImageFolderPopupWindow.Callback() {
                @Override
                public void onSelect(ImageFolderPopupWindow popupWindow, ImageFolder model) {
                    mFactory.selectFolder(model);

                    addImagesToAdapter(model.getImages());
                    mContentView.scrollToPosition(0);
                    popupWindow.dismiss();
                    mSelectFolderView.setText(model.getName());
                }

                @Override
                public void onDismiss() {
                    mSelectFolderIcon.setImageResource(R.mipmap.ic_arrow_bottom);
                }

                @Override
                public void onShow() {
                    mSelectFolderIcon.setImageResource(R.mipmap.ic_arrow_top);
                }
            });
            popupWindow.setAdapter(mImageFolderAdapter);
            mFolderPopupWindow = popupWindow;
        }
        mFolderPopupWindow.showAsDropDown(mToolbar);
    }

    /**
     * 打开相机
     */
    private void toOpenCamera() {
        // 判断是否挂载了SD卡
        mCamImageName = null;
        String savePath = "";
        if (Util.hasSDCard()) {
            savePath = Util.getCameraPath();
            File saveDir = new File(savePath);
            if (!saveDir.exists()) {
                saveDir.mkdirs();
            }
        }

        // 没有挂载SD卡，无法保存文件
        if (TextUtils.isEmpty(savePath)) {
            Toast.makeText(getActivity(), "无法保存照片，请检查SD卡是否挂载", Toast.LENGTH_LONG).show();
            return;
        }

        mCamImageName = Util.getSaveImageFullName();
        File out = new File(savePath, mCamImageName);
        Uri uri = Uri.fromFile(out);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent,
                0x03);
    }

    /**
     * 拍照完成通知系统添加照片
     *
     * @param requestCode requestCode
     * @param resultCode  resultCode
     * @param data        data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == AppCompatActivity.RESULT_OK && requestCode == 0x03 && mCamImageName != null) {
            Uri localUri = Uri.fromFile(new File(Util.getCameraPath() + mCamImageName));
            Intent localIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, localUri);
            getActivity().sendBroadcast(localIntent);
        }
    }

    @Override
    public void displayImage(ImageView iv, String path) {
        DrawableRequestBuilder builder = Glide.with(getActivity()).load(path)
                .centerCrop()
                .placeholder(R.color.grey_200)
                .error(R.mipmap.ic_default_image_error);
        if (path.toLowerCase().endsWith("gif"))
            builder = builder.diskCacheStrategy(DiskCacheStrategy.SOURCE);
        builder.into(iv);
    }

    @Override
    public void onFolderRemoved(List<ImageFolder> images) {
        Log.e("TAG", "onFolderRemoved: " + images.size());
    }

    @Override
    public void onFolderAdded(List<ImageFolder> images) {
        Log.e("TAG", "onFolderAdded: " + images.size());
    }

    @Override
    public void onFolderUpdated(List<ImageFolder> images) {
        Log.e("TAG", "onFolderUpdated: " + images.size());
    }

    @Override
    public void onPictureRemoved(List<Image> images) {
        Log.e("TAG", "onPictureRemoved: " + images.size());
    }

    @Override
    public void onPictureAdded(List<Image> images) {
        Log.e("TAG", "onPictureAdded: " + images.size());
    }


    private class LoaderListener implements LoaderManager.LoaderCallbacks<Cursor> {
        private final String[] IMAGE_PROJECTION = {
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATE_ADDED,
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.MINI_THUMB_MAGIC,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME};

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            if (id == 0) {
                //数据库光标加载器
                return new CursorLoader(getContext(),
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                        null, null, IMAGE_PROJECTION[2] + " DESC");
            }
            return null;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            if (data != null) {
                ArrayList<Image> images = new ArrayList<>();
                List<ImageFolder> imageFolders = new ArrayList<>();

                ImageFolder defaultFolder = new ImageFolder();
                defaultFolder.setName("全部照片");
                defaultFolder.setPath("");
                imageFolders.add(defaultFolder);

                int count = data.getCount();
                if (count > 0) {
                    data.moveToFirst();
                    do {
                        String path = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
                        String name = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]));
                        long dateTime = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]));
                        int id = data.getInt(data.getColumnIndexOrThrow(IMAGE_PROJECTION[3]));
                        String thumbPath = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[4]));
                        String bucket = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[5]));

                        Image image = new Image();
                        image.setPath(path);
                        image.setName(name);
                        image.setDate(dateTime);
                        image.setId(id);
                        image.setThumbPath(thumbPath);
                        image.setFolderName(bucket);
                        images.add(image);

                        //如果是新拍的照片
                        if (mCamImageName != null && mCamImageName.equals(image.getName())) {
                            image.setSelect(true);
                            mSelectedImage.add(image);
                        }

                        //如果是被选中的图片
                        if (mSelectedImage.size() > 0) {
                            for (Image i : mSelectedImage) {
                                if (i.getPath().equals(image.getPath())) {
                                    image.setSelect(true);
                                }
                            }
                        }

                        File imageFile = new File(path);
                        File folderFile = imageFile.getParentFile();
                        ImageFolder folder = new ImageFolder();
                        folder.setName(folderFile.getName());
                        folder.setPath(folderFile.getAbsolutePath());
                        if (!imageFolders.contains(folder)) {
                            folder.getImages().add(image);
                            folder.setAlbumPath(image.getPath());//默认相册封面
                            imageFolders.add(folder);
                        } else {
                            // 更新
                            ImageFolder f = imageFolders.get(imageFolders.indexOf(folder));
                            f.getImages().add(image);
                        }


                    } while (data.moveToNext());
                }

                addImagesToAdapter(images);
                defaultFolder.getImages().addAll(images);
                if (mOperator.getConfig().isHaveCamera()) {
                    defaultFolder.setAlbumPath(images.size() > 1 ? images.get(1).getPath() : null);
                } else {
                    defaultFolder.setAlbumPath(images.size() > 0 ? images.get(0).getPath() : null);
                }
                mImageFolderAdapter.resetData(imageFolders);


//                mImageFolders.clear();
//                mImageFolders.addAll(imageFolders);
//                mImageFolderAdapter.notifyDataSetChanged();
                //删除掉不存在的，在于用户选择了相片，又去相册删除
                if (mSelectedImage.size() > 0) {
                    List<Image> rs = new ArrayList<>();
                    for (Image i : mSelectedImage) {
                        File f = new File(i.getPath());
                        if (!f.exists()) {
                            rs.add(i);
                        }
                    }
                    mSelectedImage.removeAll(rs);
                }

                // If add new mCamera picture, and we only need one picture, we result it.
                if (mOperator.getConfig().getSelectCount() == 1 && mCamImageName != null) {
                    handleResult();
                }

                handleSelectSizeChange(mSelectedImage.size());
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    }

    private void addImagesToAdapter(ArrayList<Image> images) {
//        mImageAdapter.clear();
//        if (mOperator.getConfig().isHaveCamera()) {
//            Image cam = new Image();
//            mImageAdapter.addItem(cam);
//        }
//        mImageAdapter.addAllData(images);
        mData.clear();
        if (mOperator.getConfig().isHaveCamera()) {
            Image cam = new Image();
            mData.add(cam);
        }
        mData.addAll(images);
        mImageAdapter.notifyDataSetChanged();
    }
}
