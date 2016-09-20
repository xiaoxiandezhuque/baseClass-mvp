package com.xuhong.baseclass.model;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by BHKJ on 2016/9/20.
 */

public class PhoneContacts {
    private static final String[] PHONES_PROJECTION = new String[]{
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER,
            ContactsContract.CommonDataKinds.Photo.PHOTO_ID,
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID};
    /**
     * 联系人显示名称
     **/
    private static final int PHONES_DISPLAY_NAME_INDEX = 0;
    /**
     * 电话号码
     **/
    private static final int PHONES_NUMBER_INDEX = 1;

    /**
     * 头像ID
     **/
    private static final int PHONES_PHOTO_ID_INDEX = 2;

    /**
     * 联系人的ID
     **/
    private static final int PHONES_CONTACT_ID_INDEX = 3;


    private List<String> mList = new ArrayList<>();


    /**
     * 得到手机通讯录联系人信息
     **/
    public String getPhoneContacts(Context mContext) {
        StringBuffer stringBuffer = new StringBuffer();

        ContentResolver resolver = mContext.getContentResolver();

        // 获取手机联系人
        Cursor phoneCursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, PHONES_PROJECTION, null, null, null);

        if (phoneCursor != null) {
            while (phoneCursor.moveToNext()) {
                //得到手机号码
                String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
                //当手机号码为空的或者为空字段 跳过当前循环
                if (TextUtils.isEmpty(phoneNumber))
                    continue;
                //得到联系人名称
                String contactName = phoneCursor.getString(PHONES_DISPLAY_NAME_INDEX);
                //得到联系人ID
                Long contactid = phoneCursor.getLong(PHONES_CONTACT_ID_INDEX);
                //得到联系人头像ID
                Long photoid = phoneCursor.getLong(PHONES_PHOTO_ID_INDEX);
                //得到联系人头像Bitamp
//                Bitmap contactPhoto = null;
//                //photoid 大于0 表示联系人有头像 如果没有给此人设置头像则给他一个默认的
//                if (photoid > 0) {
//                    Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactid);
//                    InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(resolver, uri);
//                    contactPhoto = BitmapFactory.decodeStream(input);
//                } else {
//                    contactPhoto = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher);
//                }

                stringBuffer.append("姓名:   " + contactName + ",电话:   " + phoneNumber + "\n");

            }

            phoneCursor.close();
        }

        return stringBuffer.toString();
    }


    public String getCallPhone(Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = null;
        StringBuffer stringBuffer = new StringBuffer();
        try {

            cursor = contentResolver.query(CallLog.Calls.CONTENT_URI, null, null, null, CallLog.Calls.DATE + " desc");
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor
                        .getColumnIndex(CallLog.Calls.CACHED_NAME));
                String number = cursor.getString(cursor
                        .getColumnIndex(CallLog.Calls.NUMBER));
                long type = cursor.getInt(cursor
                        .getColumnIndex(CallLog.Calls.TYPE));
                long lDate = cursor.getLong(cursor
                        .getColumnIndex(CallLog.Calls.DATE));
                long duration = cursor.getLong(cursor
                        .getColumnIndex(CallLog.Calls.DURATION));
                int _new = cursor.getInt(cursor
                        .getColumnIndex(CallLog.Calls.NEW));
                Date date = new Date(lDate);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                String typeString = "";
                if (type == 1) {
                    typeString = "呼入电话";
                } else if (type == 2) {
                    typeString = "呼出电话";
                } else {
                    typeString = "未接电话";
                }

                stringBuffer.append("姓名：    " + name + ",电话号码:    " + number + ",打电话的时间:    " + format.format(date) + "，打电话持续时间" + duration +
                        ",类型" + typeString + ",new :" + _new + "\n");

            }


        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return stringBuffer.toString();
    }

    public String getMsg(Context context) {
        StringBuffer stringBuffer = new StringBuffer();
        final String SMS_URI_ALL = "content://sms/";
        try {
            ContentResolver cr = context.getContentResolver();
            String[] projection = new String[]{"_id", "address", "person",
                    "body", "date", "type"};
            Uri uri = Uri.parse(SMS_URI_ALL);
            Cursor cur = cr.query(uri, projection, null, null, "date desc");
            if (cur.moveToFirst()) {
                String name;
                String phoneNumber;
                String smsbody;
                String date;
                String type;
                int nameColumn = cur.getColumnIndex("person");
                int phoneNumberColumn = cur.getColumnIndex("address");
                int smsbodyColumn = cur.getColumnIndex("body");
                int dateColumn = cur.getColumnIndex("date");
                int typeColumn = cur.getColumnIndex("type");
                do {
                    name = cur.getString(nameColumn);
                    phoneNumber = cur.getString(phoneNumberColumn);
                    smsbody = cur.getString(smsbodyColumn);

                    SimpleDateFormat dateFormat = new SimpleDateFormat(
                            "yyyy-MM-dd HH:mm:ss");
                    Date d = new Date(Long.parseLong(cur.getString(dateColumn)));
                    date = dateFormat.format(d);

                    int typeId = cur.getInt(typeColumn);
                    if (typeId == 1) {
                        type = "接收";
                    } else if (typeId == 2) {
                        type = "发送";
                    } else {
                        type = "";
                    }
                    if (smsbody == null) smsbody = "";
                    stringBuffer.append("来自哪个电话：  " + phoneNumber + ",内容： " + smsbody + "   ,时间：" + date + ",类型: " + type+"\n\n");

                } while (cur.moveToNext());

            } else {

            }
            cur.close();
        } catch (SQLiteException ex) {

        }

        return stringBuffer.toString();
    }
}
