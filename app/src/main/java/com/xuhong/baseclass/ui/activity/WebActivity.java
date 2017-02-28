package com.xuhong.baseclass.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.xuhong.baseclass.R;
import com.xuhong.baseclass.utils.Constants;
import com.xuhong.baseclass.utils.MyLog;
import com.xuhong.baseclass.utils.StatusBarUtils;
import com.xuhong.baseclass.view.TitleView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import io.reactivex.android.schedulers.AndroidSchedulers;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by BHKJ on 2016/8/31.
 */

public class WebActivity extends BaseActivity  {

    public ValueCallback<Uri> mUploadMessage;
    public ValueCallback<Uri[]> mUploadMessageForAndroid5;

    public final static int FILECHOOSER_RESULTCODE = 1;
    public final static int FILECHOOSER_RESULTCODE_FOR_ANDROID_5 = 2;

//    @BindView(R.id.tv_title)
//    TitleView tvTitle;
//    @BindView(R.id.wv_details)
    WebView mWebView;

    private ImageView ivCollection;
    private LinearLayout llCollection;
    private LinearLayout llShare;

//    private NewsListBean.DataEntity mData;
//    private CollectionPresenter mCollectionPresenter;

    private boolean isCollection;

    private PopupWindow mPopupWindow;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_web;
    }

    @Override
    protected void initView() {
//        mData = getIntent().getParcelableExtra("data");



//        tvTitle.setTitleText(mData.getTitle());

        mWebView.getSettings().setJavaScriptEnabled(true);

//        if (NetWorkUtils.isNetworkConnected()) {
//            mWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);  //设置 缓存模式
//        } else {
//            mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);  //设置 缓存模式
//        }

        // 开启 DOM storage API 功能
        mWebView.getSettings().setDomStorageEnabled(true);
        //开启 database storage API 功能
        mWebView.getSettings().setDatabaseEnabled(true);

        String cacheDirPath = getFilesDir().getAbsolutePath() + Constants.CACHE_PATH;

        //设置  Application Caches 缓存目录
        mWebView.getSettings().setAppCachePath(cacheDirPath);
        //开启 Application Caches 功能
        mWebView.getSettings().setAppCacheEnabled(true);
        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
//        mWebView.loadUrl(mData.getUrl());
        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器

                //判断是否是文件下载链接，如果不是则返回，直接访问
                String fileName = url.substring(url.lastIndexOf("/"));
                if (null == getFileType(fileName) || getFileType(fileName).equals("")) {
                    view.loadUrl(url);
                }

                //如果是文件下载链接，先下载，再调用系统安装的阅读器打开

                Observable.just(url)
                        .map(new Func1<String, File>() {
                            @Override
                            public File call(String s) {
                                return downloadFile(s);
                            }
                        })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<File>() {
                            @Override
                            public void call(File file) {
                                startActivity(getFileIntent(file));
                            }
                        }, new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                MyLog.e("web加载文件",throwable.toString());
                            }
                        });


                return true;
            }
        });
        mWebView.setWebChromeClient(
                new WebChromeClient() {
                    public void onProgressChanged(WebView view, int progress) {// 载入进度改变而触发
                        if (progress == 100) {
                            //handler.sendEmptyMessage(1);// 如果全部载入,隐藏进度对话框
                        }

                        super.onProgressChanged(view, progress);
                    }

                    //扩展支持alert事件
                    @Override
                    public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                        builder.setTitle("提示").setMessage(message).setPositiveButton("确定", null);
                        builder.setCancelable(false);
                        AlertDialog dialog = builder.create();
                        dialog.show();
                        result.confirm();
                        return true;
                    }

                    //扩展浏览器上传文件
                    //3.0++版本
                    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
                        openFileChooserImpl(uploadMsg);
                    }

                    //3.0--版本
                    public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                        openFileChooserImpl(uploadMsg);
                    }

                    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                        openFileChooserImpl(uploadMsg);
                    }

                    // For Android > 5.0
                    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> uploadMsg, WebChromeClient.FileChooserParams fileChooserParams) {
                        openFileChooserImplForAndroid5(uploadMsg);
                        return true;
                    }
                }
        );


        tvTitle.setOnClickRightListener(new TitleView.OnClickRightListener() {
            @Override
            public void onClick(View v) {
                if (mPopupWindow == null) {
                    initPop();
                }
                mPopupWindow.showAtLocation(tvTitle, Gravity.RIGHT | Gravity.TOP, 0,
                        tvTitle.getHeight() + StatusBarUtils.getStatusBarHeight());


            }
        });

    }


    private void initPop() {
        // 一个自定义的布局，作为显示的内容
//        View contentView = LayoutInflater.from(this).inflate(
//                R.layout.pop_collection_share, null, false);
//        ivCollection = (ImageView) contentView.findViewById(R.id.iv_collection);
//        llCollection = (LinearLayout) contentView.findViewById(R.id.ll_collection);
//        llShare = (LinearLayout) contentView.findViewById(R.id.ll_share);
//
//        if (isCollection) {
//            ivCollection.setImageResource(R.mipmap.collection_select);
//        } else {
//            ivCollection.setImageResource(R.mipmap.collection);
//        }
//        llCollection.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mCollectionPresenter != null) {
//                    mCollectionPresenter.addOrCancelCollection();
//                }
//            }
//        });
//        llShare.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(WebActivity.this, ShareActivity.class);
//                intent.putExtra("data", mData);
//                startActivity(intent);
//                if (mPopupWindow.isShowing()) {
//                    mPopupWindow.dismiss();
//                }
//            }
//        });
//
//
//        mPopupWindow = new PopupWindow(contentView,
//                DensityUtils.dip2px(150), ViewGroup.LayoutParams.WRAP_CONTENT, true);
//
//        mPopupWindow.setTouchable(true);
//
//        mPopupWindow.setTouchInterceptor(new View.OnTouchListener() {
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//
//
//                return false;
//                // 这里如果返回true的话，touch事件将被拦截
//                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
//            }
//        });
//
//        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
//        // 我觉得这里是API的一个bug
//        mPopupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.kuang));

    }


//    @Override
//    public OrdinaryBean getData() {
//        return mData;
//    }
//
//    @Override
//    public void setCollectionState(boolean bool) {
//        isCollection = bool;
//        if (mPopupWindow != null) {
//            if (bool) {
//                ivCollection.setImageResource(R.mipmap.collection_select);
//            } else {
//                ivCollection.setImageResource(R.mipmap.collection);
//            }
//        }
//    }

    @Override
    protected void onDestroy() {
        if (mCollectionPresenter != null) {
            mCollectionPresenter.detachView();
        }

        super.onDestroy();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage)
                return;
            Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;

        } else if (requestCode == FILECHOOSER_RESULTCODE_FOR_ANDROID_5) {
            if (null == mUploadMessageForAndroid5)
                return;
            Uri result = (intent == null || resultCode != RESULT_OK) ? null : intent.getData();
            if (result != null) {
                mUploadMessageForAndroid5.onReceiveValue(new Uri[]{result});
            } else {
                mUploadMessageForAndroid5.onReceiveValue(new Uri[]{});
            }
            mUploadMessageForAndroid5 = null;
        }

    }

    private void openFileChooserImpl(ValueCallback<Uri> uploadMsg) {
        mUploadMessage = uploadMsg;
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("image/*");
        startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
    }


    private void openFileChooserImplForAndroid5(ValueCallback<Uri[]> uploadMsg) {
        mUploadMessageForAndroid5 = uploadMsg;
        Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
        contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
        contentSelectionIntent.setType("image/*");

        Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
        chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
        chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");

        startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE_FOR_ANDROID_5);
    }


    /**
     * 从配置文件获取要下载的文件后缀和对应的MIME类型
     *
     * @param fileName
     * @return
     */
    public String getFileType(String fileName) {
        String[] names = {
          ".doc",".xml",".ppt",".pdf"
        };
        String[] types = {
                "application/msword",
                "application/vnd.ms-excel",
                "application/vnd.ms-powerpoint",
                "application/pdf",
        };
        for (int i = 0; i < names.length; i++) {
            if (fileName.toLowerCase().indexOf(names[i]) >= 0) {
                return types[i];
            }
        }
        return "";
    }

    /**
     * 获取用于文件打开的intent
     *
     * @param file
     * @return
     */
    public Intent getFileIntent(File file) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(file);
        String fileType = getFileType(file.getName());
        intent.setDataAndType(uri, fileType);
        return intent;
    }

    /**
     * 下载文件
     *
     * @param fileUrl
     * @return
     */
    public File downloadFile(String fileUrl) {
        File apkFile = null;
        String fileName = fileUrl.substring(fileUrl.lastIndexOf("/"));
        try {
            if (Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                // 获得存储卡的路径
                String sdpath = Environment.getExternalStorageDirectory() + "/";
                String mSavePath = sdpath + "download";
                URL url = new URL(fileUrl);
                // 创建连接
                HttpURLConnection conn = (HttpURLConnection) url
                        .openConnection();
                conn.connect();
                // 获取文件大小
                //int length = conn.getContentLength();
                // 创建输入流
                InputStream is = conn.getInputStream();
                File file = new File(mSavePath);
                // 判断文件目录是否存在
                if (!file.exists()) {
                    file.mkdir();
                }
                apkFile = new File(mSavePath, fileName);
                if (apkFile.exists()) {
                    return apkFile;
                }
                FileOutputStream fos = new FileOutputStream(apkFile);
                int count = 0;
                int numread = 0;
                byte buf[] = new byte[1024];
                while ((numread = is.read(buf)) != -1) {
                    fos.write(buf, 0, numread);
                }
                fos.flush();
                fos.close();
                is.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return apkFile;
    }
}