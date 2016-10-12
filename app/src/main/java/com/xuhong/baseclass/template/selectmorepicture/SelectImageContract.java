package com.xuhong.baseclass.template.selectmorepicture;

import com.xuhong.baseclass.template.selectmorepicture.activity.SelectImageActivity;

/**
 * 图片选择器建立契约关系，将权限操作放在Activity，具体数据放在Fragment
 * Created by huanghaibin_dev
 * on 2016/7/15.
 */
public interface SelectImageContract {
    interface Operator {
        void requestCamera();

        void requestExternalStorage();

        void onBack();

        void setDataView(View view);

        SelectImageActivity.Callback getCallback();

        SelectImageActivity.Config getConfig();
    }

    interface View {

        void onOpenCameraSuccess();

        void onCameraPermissionDenied();
    }
}
