package com.xuhong.baseclass.template.selectmorepicture.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Vibrator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.xuhong.baseclass.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JuQiu
 * on 16/7/15.
 */
public class TweetSelectImageAdapter extends RecyclerView.Adapter<TweetSelectImageAdapter.TweetSelectImageHolder> implements TweetPicturesPreviewerItemTouchCallback.ItemTouchHelperAdapter {
    private final int MAX_SIZE = 9;
    private final int TYPE_NONE = 0;
    private final int TYPE_ADD = 1;
    private final List<Model> mModels = new ArrayList<>();
    private Callback mCallback;

    public TweetSelectImageAdapter(Callback callback) {
        mCallback = callback;
    }

    @Override
    public int getItemViewType(int position) {
        int size = mModels.size();
        if (size >= MAX_SIZE)
            return TYPE_NONE;
        else if (position == size) {
            return TYPE_ADD;
        } else {
            return TYPE_NONE;
        }
    }

    @Override
    public TweetSelectImageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_tweet_publish_selecter, parent, false);
        if (viewType == TYPE_NONE) {
            return new TweetSelectImageHolder(view, new TweetSelectImageHolder.HolderListener() {
                @Override
                public void onDelete(Model model) {
                    Callback callback = mCallback;
                    if (callback != null) {
                        int pos = mModels.indexOf(model);
                        if (pos == -1)
                            return;
                        mModels.remove(pos);
                        if (mModels.size() > 0)
                            notifyItemRemoved(pos);
                        else
                            notifyDataSetChanged();
                    }
                }

                @Override
                public void onDrag(TweetSelectImageHolder holder) {
                    Callback callback = mCallback;
                    if (callback != null) {
                        // Start a drag whenever the handle view it touched
                        mCallback.onStartDrag(holder);
                    }
                }
            });
        } else {
            return new TweetSelectImageHolder(view, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Callback callback = mCallback;
                    if (callback != null) {
                        callback.onLoadMoreClick();
                    }
                }
            });
        }
    }

    @Override
    public void onBindViewHolder(final TweetSelectImageHolder holder, int position) {
        int size = mModels.size();
        if (size >= MAX_SIZE || size != position) {
            Model model = mModels.get(position);
            holder.bind(position, model, mCallback.getImgLoader());
        }
    }

    @Override
    public int getItemCount() {
        int size = mModels.size();
        if (size == MAX_SIZE) {
            return size;
        } else if (size == 0) {
            return 0;
        } else {
            return size + 1;
        }
    }

    public void clear() {
        mModels.clear();
    }

    public void add(Model model) {
        if (mModels.size() >= MAX_SIZE)
            return;
        mModels.add(model);
    }

    public void add(String path) {
        add(new Model(path));
    }

    public String[] getPaths() {
        int size = mModels.size();
        if (size == 0)
            return null;
        String[] paths = new String[size];
        int i = 0;
        for (Model model : mModels) {
            paths[i++] = model.path;
        }
        return paths;
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        //Collections.swap(mModels, fromPosition, toPosition);
        if (fromPosition == toPosition)
            return false;

        if (fromPosition < toPosition) {
            Model fromModel = mModels.get(fromPosition);
            Model toModel = mModels.get(toPosition);

            mModels.remove(fromPosition);
            mModels.add(mModels.indexOf(toModel) + 1, fromModel);
        } else {
            Model fromModel = mModels.get(fromPosition);
            mModels.remove(fromPosition);
            mModels.add(toPosition, fromModel);
        }

        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        mModels.remove(position);
        notifyItemRemoved(position);
    }

    public static class Model {
        public Model(String path) {
            this.path = path;
        }

        public String path;
        public boolean isUpload;
    }

    public interface Callback {
        void onLoadMoreClick();

        RequestManager getImgLoader();

        /**
         * Called when a view is requesting a start of a drag.
         *
         * @param viewHolder The holder of the view to drag.
         */
        void onStartDrag(RecyclerView.ViewHolder viewHolder);
    }

    /**
     * TweetSelectImageHolder
     */
    static class TweetSelectImageHolder extends RecyclerView.ViewHolder implements TweetPicturesPreviewerItemTouchCallback.ItemTouchHelperViewHolder {
        private ImageView mImage;
        private ImageView mDelete;
        private HolderListener mListener;

        private TweetSelectImageHolder(View itemView, HolderListener listener) {
            super(itemView);
            mListener = listener;
            mImage = (ImageView) itemView.findViewById(R.id.iv_content);
            mDelete = (ImageView) itemView.findViewById(R.id.iv_delete);

            mDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Object obj = v.getTag();
                    final HolderListener holderListener = mListener;
                    if (holderListener != null && obj != null && obj instanceof TweetSelectImageAdapter.Model) {
                        holderListener.onDelete((TweetSelectImageAdapter.Model) obj);
                    }
                }
            });
            mImage.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    final HolderListener holderListener = mListener;
                    if (holderListener != null) {
                        holderListener.onDrag(TweetSelectImageHolder.this);
                    }
                    return true;
                }
            });
        }

        private TweetSelectImageHolder(View itemView, View.OnClickListener clickListener) {
            super(itemView);

            mImage = (ImageView) itemView.findViewById(R.id.iv_content);
            mDelete = (ImageView) itemView.findViewById(R.id.iv_delete);

            mDelete.setVisibility(View.GONE);
            mImage.setImageResource(R.mipmap.ic_tweet_add);
            mImage.setOnClickListener(clickListener);
        }

        public void bind(int position, TweetSelectImageAdapter.Model model, RequestManager loader) {
            mDelete.setTag(model);
            DrawableRequestBuilder builder = loader.load(model.path)
                    .centerCrop()
                    .placeholder(Color.parseColor("#eeeeee"))
                    .error(R.mipmap.ic_default_image_error);
            if (model.path.toLowerCase().endsWith("gif"))
                builder = builder.diskCacheStrategy(DiskCacheStrategy.SOURCE);
            builder.into(mImage);
        }

        @Override
        public void onItemSelected() {
            try {
                Vibrator vibrator = (Vibrator) itemView.getContext().getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(20);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onItemClear() {

        }

        /**
         * Holder 与Adapter之间的桥梁
         */
        interface HolderListener {
            void onDelete(TweetSelectImageAdapter.Model model);

            void onDrag(TweetSelectImageHolder holder);
        }
    }

}
