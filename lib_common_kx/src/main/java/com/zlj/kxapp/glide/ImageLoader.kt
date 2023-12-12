package com.zlj.kxapp.glide

import android.content.Context
import android.graphics.Bitmap
import android.os.Looper
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.zlj.kxapp.R

/**
 * Created by zlj on 2021/9/18.
 * @Word：Thought is the foundation of understanding
 * 可以使用coil的图片加载框架代替图片加载的功能
 */
object ImageLoader {

    /**
     * 加载图片
     * @param context
     * @param url
     * @param iv
     */
    fun displayImage(context: Context?, url: String?, iv: ImageView?) {
        displayImage(context, url, iv, R.drawable.progress_bar_bg, R.drawable.progress_bar_bg)
    }


    /**
     * 加载图片
     * @param context
     * @param url
     * @param iv
     * @param placeholder
     * @param errorId
     */
    fun displayImage(context: Context?, url: String?, iv: ImageView?, placeholder: Int, errorId: Int) {
        if (iv == null) return
        Glide.with(context!!)
                .load(url)
                .apply(RequestOptions()
                        .skipMemoryCache(false)
                        .error(errorId)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(placeholder))
                .into(iv)
    }

    /**
     * 加载圆形图片
     * @param context
     * @param url
     * @param iv
     */
    fun displayCircleImage(context: Context, url: String?, iv: ImageView?) {
        if (iv == null) return
        Glide.with(context)
                .asBitmap()
                .load(url)
                .apply(RequestOptions()
                        .skipMemoryCache(false)
                        .error(R.drawable.default_circle_bg)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .placeholder(R.drawable.default_circle_bg))
                .into(object : BitmapImageViewTarget(iv) {
                    override fun setResource(resource: Bitmap?) {
                        val circularBitmapDrawable = RoundedBitmapDrawableFactory.create(context.resources, resource)
                        circularBitmapDrawable.isCircular = true
                        iv.setImageDrawable(circularBitmapDrawable)
                        //startAnim(iv);
                    }
                })
    }


    /**
     * 加载图片
     * @param context
     * @param iv
     * @param placeHolderResId
     */
    fun displayImage(context: Context?, iv: ImageView?, placeHolderResId: Int) {
        if (iv == null) return
        Glide.with(context!!)
                .load(placeHolderResId)
                .apply(RequestOptions()
                        .skipMemoryCache(false)
                        .error(placeHolderResId)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .placeholder(R.drawable.progress_bar_bg))
                .into(iv)
    }

    /**
     * 加载圆角图片
     * @param context
     * @param url
     * @param iv
     */
    fun displayRoundImage(context: Context, url: String?, iv: ImageView?) {
        if (iv == null) return
        Glide.with(context)
                .asBitmap()
                .load(url)
                .apply(RequestOptions()
                        .skipMemoryCache(false)
                        .error(R.drawable.progress_bar_bg)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .placeholder(R.drawable.progress_bar_bg))
                .into(object : BitmapImageViewTarget(iv) {
                    override fun setResource(resource: Bitmap?) {
                        val circularBitmapDrawable = RoundedBitmapDrawableFactory.create(context.resources, resource)
                        circularBitmapDrawable.cornerRadius = 50f
                        iv.setImageDrawable(circularBitmapDrawable)
                        //startAnim(iv);
                    }
                })
    }

    /**
     * 开启动画
     * @param iv
     */
    fun startAnim(iv: ImageView?) {
        if (iv == null) return
        iv.clearAnimation()
        val anim: Animation = AlphaAnimation(0f, 1f)
        anim.fillAfter = true
        anim.duration = 300
        anim.interpolator = LinearInterpolator()
        iv.startAnimation(anim)
    }

    /**
     * 需要在子线程中执行
     * @param context
     */
    fun clearAllCache(context: Context?): Boolean {
        return try {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                Thread { Glide.get(context!!).clearDiskCache() }.start()
            } else {
                Glide.get(context!!).clearDiskCache()
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * 清除Glide内存缓存
     * @param context
     * @return
     */
    fun clearCacheMemory(context: Context?): Boolean {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) { //只能在主线程执行
                Glide.get(context!!).clearMemory()
                return true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }
}