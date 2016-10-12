package com.xuhong.baseclass.presenter;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import com.xuhong.baseclass.iview.ILoginView;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func0;

/**
 * Created by BHKJ on 2016/5/11.
 */
public class LoginPresenter extends BasePresenter<ILoginView> {


    public LoginPresenter(ILoginView view) {
        super(view);
    }

    public void login(final Context context) {
//        LoadingDialog loadingDialog =new LoadingDialog(context);
//        loadingDialog.show();

        Uri.parse("content://media/external/images/media/74972").getPath();
        Environment.getExternalStorageDirectory ().getAbsolutePath ();

//        Scheduler s = (Scheduler) Schedulers.newThread().createWorker().schedule(new Action0() {
//            @Override
//            public void call() {
//
//            }
//        });

        Observable.defer(new Func0<Observable<Integer>>() {
            @Override
            public Observable<Integer> call() {
                return Observable.just(1);
            }
        });
        Observable.interval(100, TimeUnit.MILLISECONDS)
                .just(1)
                ;
        Observable.range(1,3);
        Observable.just("","")
                .isEmpty()
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {

                    }
                })
            ;


//        Observable.just(1, 1)
//                .map(new Func1<Integer, Integer>() {
//                    @Override
//                    public Integer call(Integer integer) {
//                        return null;
//                    }
//                })
//                .subscribeOn(s)
//                .subscribe(new Subscriber<Integer>() {
//                    @Override
//                    public void onStart() {
//                        super.onStart();
//                    }
//
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onNext(Integer integer) {
//
//                    }
//                });
//        Single.just(1).concatWith(Single.just(2));




    }


}
