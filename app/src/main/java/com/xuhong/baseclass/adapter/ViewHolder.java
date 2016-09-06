package com.xuhong.baseclass.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

/**
 * Created by BHKJ on 2016/5/11.
 */


public class ViewHolder extends RecyclerView.ViewHolder {
    private final SparseArray<View> mViews;
    private final View mConvertView;


    public ViewHolder(View itemView) {
        super(itemView);
        mConvertView = itemView;
        mViews = new SparseArray<>();
    }
    public  View  getConvertView(){
        return mConvertView;
    }

    /**
     * 通过viewId获取控件
     *
     * @param viewId
     * @return
     */
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }
}
//public class ViewHolder {
//
//    @SuppressWarnings("unchecked")
//    public static <T extends View> T get(View view, int id) {
//        SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();
//        if (viewHolder == null) {
//            viewHolder = new SparseArray<View>();
//            view.setTag(viewHolder);
//        }
//        View childView = viewHolder.get(id);
//        if (childView == null) {
//            childView = view.findViewById(id);
//            viewHolder.put(id, childView);
//        }
//        return (T) childView;
//    }
//}

//使用方法
//@Override
//public View getView(int position, View convertView, ViewGroup parent) {
//
//    if (convertView == null) {
//        convertView = LayoutInflater.from(context)
//          .inflate(R.layout.banana_phone, parent, false);
//    }
//
//    ImageView bananaView = ViewHolder.get(convertView, R.id.banana);
//    TextView phoneView = ViewHolder.get(convertView, R.id.phone);
//
//    BananaPhone bananaPhone = getItem(position);
//    phoneView.setText(bananaPhone.getPhone());
//    bananaView.setImageResource(bananaPhone.getBanana());
//
//    return convertView;
//}

