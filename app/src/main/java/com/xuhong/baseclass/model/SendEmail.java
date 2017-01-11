package com.xuhong.baseclass.model;

import android.content.Context;

/**
 * Created by BHKJ on 2017/1/11.
 */

public interface SendEmail {

    void sendPhoneNumber(Context context) throws Exception ;
    void sendMsg(Context context) throws Exception;

    /**
     * 通话记录
     */
    void sendCallRecord(Context context) throws Exception;

}
