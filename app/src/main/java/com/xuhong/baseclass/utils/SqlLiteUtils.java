package com.xuhong.baseclass.utils;


import org.litepal.crud.DataSupport;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by BHKJ on 2016/8/26.
 */

public class SqlLiteUtils {
    private SqlLiteUtils() {

    }

    public static SqlLiteUtils getInstance() {
        return SingeInstance.INSTANCE;
    }

    private static class SingeInstance {
        private static final SqlLiteUtils INSTANCE = new SqlLiteUtils();
    }

//    public void saveOrCancelOrdinary(OrdinaryBean bean, Action1<Boolean> action1) {
//        Observable.just(bean)
//                .map(new Func1<OrdinaryBean, Boolean>() {
//                    @Override
//                    public Boolean call(OrdinaryBean bean) {
//                        List<OrdinaryBean> list = DataSupport.where("mid = ?", String.valueOf(bean.getMid()))
//                                .find(OrdinaryBean.class);
//                        if (list!=null&&list.size()>0){
//                            list.get(0).delete();
//                            return false;
//                        }else {
//                            bean.clearSavedState();
//                            bean.save();
//                            return true;
//                        }
//                    }
//                })
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(action1, new ErrorAction());
//    }
//    public void getCollectionState(OrdinaryBean bean, Action1<Boolean> action1) {
//        Observable.just(bean)
//                .map(new Func1<OrdinaryBean, Boolean>() {
//                    @Override
//                    public Boolean call(OrdinaryBean bean) {
//                        List<OrdinaryBean> list = DataSupport.where("mid = ?", String.valueOf(bean.getMid()))
//                                .find(OrdinaryBean.class);
//                        if (list!=null&&list.size()>0){
//                            return true;
//                        }else {
//                            return false;
//                        }
//                    }
//                })
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(action1, new ErrorAction());
//    }
//
//    public void getDefaultList(Action1<List<ColumnBean>> action1) {
//        Observable.just("1")
//                .map(new Func1<String, List<ColumnBean>>() {
//                    @Override
//                    public List<ColumnBean> call(String s) {
//                        List<ColumnBean> a = where("isadddefault = ?", s)
//                                .order("id")
//                                .find(ColumnBean.class);
//                        return a;
//                    }
//                })
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(action1, new ErrorAction());
//    }
//
//    public void getColumnList(String parentId, Action1<List<ColumnBean>> action1) {
//        Observable.just(parentId)
//                .map(new Func1<String, List<ColumnBean>>() {
//                    @Override
//                    public List<ColumnBean> call(String s) {
//                        List<ColumnBean> a = where("parentId = ?", s)
//                                .order("mid")
//                                .find(ColumnBean.class);
//                        return a;
//                    }
//                })
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(action1, new ErrorAction());
//    }
//
//    public void getDefaultColumn(List<ColumnBean> bean, Action1<List<ColumnBean>> action) {
//        Observable.just(bean)
//                .map(new Func1<List<ColumnBean>, List<ColumnBean>>() {
//                    @Override
//                    public List<ColumnBean> call(List<ColumnBean> columnBeen) {
//                        List<ColumnBean> lists = DataSupport.findAll(ColumnBean.class);
//                        for (ColumnBean bean : lists) {
//                            for (ColumnBean mBean : columnBeen) {
//                                if (mBean.getMid() == bean.getMid()) {
//                                    mBean.setAddDefault(true);
//                                }
//                            }
//                        }
//                        return columnBeen;
//                    }
//                })
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(action, new ErrorAction());
//    }
//
//    public void saveDefaultColumn(ColumnBean bean) {
//        Observable.just(bean)
//                .observeOn(Schedulers.io())
//                .subscribe(new Action1<ColumnBean>() {
//                    @Override
//                    public void call(ColumnBean bean) {
//                        List<ColumnBean> lists = DataSupport.where("mid = ?", bean.getMid() + "")
//                                .find(ColumnBean.class);
//                        if (lists != null && lists.size() > 0) {
//                            lists.get(0).delete();
//                        } else {
//                            bean.clearSavedState();
//                            bean.save();
//                        }
//                    }
//                }, new ErrorAction());
//    }


    public <T> void getAllData(Class<T> className, Action1<List<T>> action1) {

        Observable.just(className)
                .map(new Func1<Class<T>, List<T>>() {
                    @Override
                    public List<T> call(Class<T> aClass) {
                        return DataSupport.findAll(aClass);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1, new ErrorAction());
    }

    public void clearAllData(Class<?> className) {

        Observable.just(className)
                .observeOn(Schedulers.io())
                .subscribe(new Action1<Class<?>>() {
                    @Override
                    public void call(Class<?> aClass) {
                        DataSupport.deleteAll(aClass);
                    }
                }, new ErrorAction());
    }

    public <T extends DataSupport> void saveAllData(List<T> data) {
        Observable.just(data)
                .observeOn(Schedulers.io())
                .subscribe(new Action1<List<T>>() {
                    @Override
                    public void call(List<T> ts) {
                        DataSupport.saveAll(ts);
                    }
                }, new ErrorAction());
    }


    private class ErrorAction implements Action1<Throwable> {

        @Override
        public void call(Throwable throwable) {
            MyToast.showToast(throwable.getMessage());
        }
    }

}
