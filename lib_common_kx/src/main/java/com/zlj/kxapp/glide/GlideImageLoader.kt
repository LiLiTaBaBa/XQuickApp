package com.zlj.kxapp.glide

import android.content.Context
import android.widget.ImageView
import com.zlj.kxapp.R

/**
 * Created by zlj on 2022/3/25.
 * @Word：Thought is the foundation of understanding
 * ImageView控件的扩展方法
 * 后续的框架加载替换此处可以是一个地方进行修改
 * 此处是一个设计的方案可以后续衍生
 */

/**
 * displayImage
 * @param context
 * @param url
 */
fun ImageView.displayImage(context: Context?, url: String?){
    ImageLoader.displayImage(context, url, this, R.drawable.progress_bar_bg, R.drawable.progress_bar_bg)
}

/**
 * displayImage
 * @param url
 */
fun ImageView.displayImage(url: String?){
   displayImage(context,url)
}


/**
 * displayCircleImage
 * @param context
 * @param url
 */
fun ImageView.displayCircleImage(context: Context, url: String?) {
    ImageLoader.displayCircleImage(context,url,this)
}

/**
 * displayCircleImage
 * @param url
 */
fun ImageView.displayCircleImage(url: String?) {
    displayCircleImage(context,url)
}

/**
 * displayImage
 * @param context
 * @param placeHolderResId
 */
fun ImageView.displayImage(context: Context?, placeHolderResId: Int) {
    ImageLoader.displayImage(context,this,placeHolderResId)
}

/**
 * displayImage
 * @param url
 */
fun ImageView.displayImage(placeHolderResId: Int) {
    displayImage(context,placeHolderResId)
}

/**
 * displayRoundImage
 * @param context
 * @param url
 */
fun ImageView.displayRoundImage(context: Context, url: String?) {
    ImageLoader.displayRoundImage(context,url,this)
}

/**
 * displayRoundImage
 * @param url
 */
fun ImageView.displayRoundImage(url: String?) {
    displayRoundImage(context,url)
}

