package com.xuhong.baseclass.template.selectmorepicture;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.util.ArrayMap;

import com.xuhong.baseclass.template.selectmorepicture.bean.Image;
import com.xuhong.baseclass.template.selectmorepicture.bean.ImageFolder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by JuQiu
 * on 16/7/28.
 */
public class MediaStoreDataFactory implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String[] IMAGE_PROJECTION = {
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATE_ADDED,
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.MINI_THUMB_MAGIC,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME};

    private final List<Image> mSource = new ArrayList<>();
    private final Map<String, ImageFolder> mFolderSource = new ArrayMap<>();
    private String mRegisterKey = "";
    private PictureSourceCallback mImageCallback;
    private FolderSourceCallback mFolderCallback;
    private Context mContext;

    public MediaStoreDataFactory(Context context, PictureSourceCallback pictureSourceCallback,
                                 FolderSourceCallback folderSourceCallback) {
        this.mContext = context;
        this.mImageCallback = pictureSourceCallback;
        this.mFolderCallback = folderSourceCallback;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(mContext,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                null, null, IMAGE_PROJECTION[2] + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null) {
            ArrayList<Image> images = new ArrayList<>();
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
                } while (data.moveToNext());
            }
            doSource(images);
        }
    }


    private void doSource(List<Image> changes) {
        final List<Image> source = mSource;
        if (source.size() == 0) {
            source.addAll(changes);
            callDataAdded(source);
        }
        if (changes.size() == 0) {
            callDataRemoved(source);
            source.clear();
        } else {
            // check remove
            List<Image> removes = new ArrayList<>();
            for (Image image : source) {
                if (!changes.contains(image))
                    removes.add(image);
            }
            if (removes.size() > 0) {
                source.removeAll(removes);
                callDataRemoved(removes);
            }

            // check add
            List<Image> adds = new ArrayList<>();
            for (Image image : changes) {
                if (!source.contains(image))
                    adds.add(image);
            }
            if (adds.size() > 0) {
                source.addAll(adds);
                callDataAdded(adds);
            }
        }
    }

    private void callDataAdded(List<Image> images) {
        List<ImageFolder> updateFolder = new ArrayList<>();
        List<ImageFolder> addFolder = new ArrayList<>();
        List<Image> addImages = new ArrayList<>();

        final Map<String, ImageFolder> folderSource = mFolderSource;
        for (Image image : images) {
            File folderFile = new File(image.getPath()).getParentFile();
            String folderPath = folderFile.getAbsolutePath().toLowerCase();
            if (folderSource.containsKey(folderPath)) {
                // update
                ImageFolder folder = folderSource.get(folderPath);
                folder.getImages().add(image);
                if (!addFolder.contains(folder) && !updateFolder.contains(folder)) {
                    updateFolder.add(folder);
                }
            } else {
                // Add new
                ImageFolder folder = new ImageFolder();
                folder.setName(folderFile.getName());
                folder.setPath(folderFile.getAbsolutePath());
                folder.setAlbumPath(image.getPath());
                folder.getImages().add(image);
                folderSource.put(folderPath, folder);
                addFolder.add(folder);
            }

            if (mRegisterKey.equals(folderPath)) {
                addImages.add(image);
            }
        }

        notifyFolderAdd(addFolder);
        notifyFolderUpdate(updateFolder);
        notifyImageAdd(addImages);
    }

    private void callDataRemoved(List<Image> images) {
        List<ImageFolder> updateFolder = new ArrayList<>();
        List<ImageFolder> removeFolder = new ArrayList<>();
        List<Image> removeImages = new ArrayList<>();

        final Map<String, ImageFolder> folderSource = mFolderSource;
        for (Image image : images) {
            File folderFile = new File(image.getPath()).getParentFile();
            String folderPath = folderFile.getAbsolutePath().toLowerCase();
            if (folderSource.containsKey(folderPath)) {
                ImageFolder folder = folderSource.get(folderPath);
                List<Image> folderImages = folder.getImages();
                if (folderImages.contains(image)) {
                    // only remove option
                    folderImages.remove(image);
                    if (folderImages.size() == 0) {
                        // Remove
                        folderSource.remove(folderPath);
                        removeFolder.add(folder);
                    } else {
                        // Update data remove
                        if (!updateFolder.contains(folder)) {
                            updateFolder.add(folder);
                        }
                    }
                }
            }

            if (mRegisterKey.equals(folderPath)) {
                removeImages.add(image);
            }
        }

        notifyFolderUpdate(updateFolder);
        notifyFolderRemove(removeFolder);
        notifyImageRemove(removeImages);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private void notifyFolderUpdate(List<ImageFolder> items) {
        if (items.size() == 0)
            return;
        mFolderCallback.onFolderUpdated(items);
    }

    private void notifyFolderAdd(List<ImageFolder> items) {
        if (items.size() == 0)
            return;
        mFolderCallback.onFolderAdded(items);
    }

    private void notifyFolderRemove(List<ImageFolder> items) {
        if (items.size() == 0)
            return;
        mFolderCallback.onFolderRemoved(items);
    }


    private void notifyImageAdd(List<Image> items) {
        if (items.size() == 0)
            return;
        mImageCallback.onPictureAdded(items);
    }

    private void notifyImageRemove(List<Image> items) {
        if (items.size() == 0)
            return;
        mImageCallback.onPictureRemoved(items);
    }

    public void selectFolder(ImageFolder folder){
        this.mRegisterKey = folder.getPath().toLowerCase();
    }

    public interface PictureSourceCallback {
        void onPictureRemoved(List<Image> images);

        void onPictureAdded(List<Image> images);
    }

    public interface FolderSourceCallback {
        void onFolderRemoved(List<ImageFolder> images);

        void onFolderAdded(List<ImageFolder> images);

        void onFolderUpdated(List<ImageFolder> images);
    }
}
