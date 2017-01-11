package com.bassclass;

import android.content.Context;
import android.content.Intent;
import android.os.RemoteException;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.support.test.uiautomator.Until;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.xuhong.baseclass", appContext.getPackageName());
    }

    private static final int LAUNCH_TIMEOUT = 5000;
    private UiDevice mDevice;

    @Before
    public void startMain() {
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        mDevice.pressHome();

        //wait for launcher
        final String launcherPackage = mDevice.getLauncherPackageName();
        mDevice.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)), LAUNCH_TIMEOUT);

        //launch the app

        Context context = InstrumentationRegistry.getContext();
        final Intent intent = context.getPackageManager().getLaunchIntentForPackage("com.tencent.tmgp.mhzxsy");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);

        // Wait for the app to appear
        mDevice.wait(Until.hasObject(By.pkg("com.tencent.tmgp.mhzxsy").depth(0)),
                LAUNCH_TIMEOUT);


    }
    @Test
    public void login() throws UiObjectNotFoundException, RemoteException, InterruptedException {



        UiObject  editLogin = mDevice.findObject(new UiSelector().text("与qq好友一起玩"));
//        editLogin.setText("18180646037");
        editLogin.clickAndWaitForNewWindow(10000);

//        UiObject edit = mDevice.findObject(new UiSelector().text("请输入密码,好嘛"));
//        edit.setText("dasdasdsa");
//
//        UiObject  btnLogin = mDevice.findObject(new UiSelector().text("登录"));
//        btnLogin.clickAndWaitForNewWindow();
//
//        UiScrollable  select =   new UiScrollable(new UiSelector().className("android.support.v7.widget.RecyclerView"));
//        int count = select.getChildCount();
//        UiObject image = select.getChild(new UiSelector().index(3));
//        image.click();
//
//        select.flingToEnd(2);
//        select.flingBackward();
////        mDevice.drag(500,500,200,200,1);
//        UiObject btnOk = mDevice.findObject(new UiSelector().textContains("完成"));
//        btnOk.clickAndWaitForNewWindow();
//
//
//
//        UiObject  aa = mDevice.findObject(new UiSelector().text("请输入用户名"));
//        aa.setText("18180646037");

        mDevice.sleep();
//        mDevice.wait(10000);
    }
}
