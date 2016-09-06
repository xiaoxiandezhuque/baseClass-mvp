package com.xuhong.baseclass.iview;

/**
 * Created by BHKJ on 2016/5/11.
 */
public interface ILoginView {

    String  getUserName();
    String getUserPassword();
    void  toOtherActivity();
    void  clearUserName();
    void clearUserPassword();

}
