package com.zlj.kxapp.widget;

import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.zlj.kxapp.R;
import androidx.databinding.BindingAdapter;

/**
 * Created by zlj on 2021/9/23.
 * @Word：Thought is the foundation of understanding
 */
public class BindingAdapters {
    @BindingAdapter("imageUrl")
    public static void loadImage(ImageView imageView, String url) {
        Glide.with(imageView.getContext())
                .load(url)
                .apply(new RequestOptions()
                        .skipMemoryCache(false)
                        .error(R.drawable.progress_bar_bg)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.progress_bar_bg))
                .into(imageView);
    }

    @BindingAdapter({"imageUrl","placeHolder","error"})
    public static void loadImage(ImageView imageView, String url, int holderDrawable, int errorDrawable) {
        Glide.with(imageView.getContext())
                .load(url)
                .apply(new RequestOptions()
                        .skipMemoryCache(false)
                        .error(errorDrawable)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(holderDrawable))
                .into(imageView);
    }

}
