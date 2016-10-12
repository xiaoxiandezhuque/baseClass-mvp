package com.xuhong.baseclass.template.selectmorepicture;

/**
 * Created by JuQiu
 * on 16/7/14.
 */

public interface TweetPublishContract {
    interface Operator {
        void setDataView(View view);

        void publish();

        void onBack();

        void loadXmlData();
    }

    interface View {
        String getContent();

        void setContent(String content);

        String[] getImages();

        void setImages(String[] paths);
    }
}
