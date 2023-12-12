package com.zlj.kxapp.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import com.zlj.kxapp.databinding.TitleHeaderBinding

/**
 * Created by zlj on 2021/9/22.
 * @Word：Thought is the foundation of understanding
 * 依托于ViewBinding实现的TitleHeaderView
 */
class TitleHeaderView(context: Context,attrs:AttributeSet?,defStyleAttr:Int):FrameLayout(context,attrs,defStyleAttr) {
    private var binding: TitleHeaderBinding

    constructor(context: Context) : this(context,null)
    constructor(context: Context,attrs:AttributeSet?) : this(context,attrs,0)

    init {
        val inflater=context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = TitleHeaderBinding.inflate(inflater, this, true)
    }

    /**
     * 设置标题
     */
    fun setTitle(title:String){
        binding.tvTitle.text=title
    }

    /**
     * 设置左边文本
     */
    fun setLeftText(text:String){
        binding.leftTv.text=text
    }

    /**
     * 设置右边文本
     */
    fun setRightText(text:String){
        binding.rightTv.text=text
    }

    /**
     * 设置左边图标
     */
    fun setLeftIcon(@DrawableRes img:Int ){
        binding.leftIv.setImageResource(img)
    }

    /**
     * 设置右边图标
     */
    fun setRightIcon(@DrawableRes img:Int ){
        binding.rightIv.setImageResource(img)
    }

    /**
     * 设置标题和左边图标
     */
    fun setTitleAndLeftIcon(text:String,@DrawableRes img:Int){
        setTitle(text)
        setLeftIcon(img)
    }

    /**
     * 设置标题和右边图标
     */
    fun setTitleAndRightIcon(text:String,@DrawableRes img:Int){
        setTitle(text)
        setRightIcon(img)
    }

    /**
     * 设置标题和右边文本
     */
    fun setTitleAndRightText(text:String, right:String){
        setTitle(text)
        setRightText(right)
    }

    /**
     * 设置标题和左边文本
     */
    fun setTitleAndLeftText(text:String, right:String){
        setTitle(text)
        setLeftText(right)
    }

    /**
     * 获取标题TextView
     */
    fun getTitleView():TextView =binding.tvTitle

    /**
     * 获取左边TextView
     */
    fun getLeftTextView():TextView =binding.leftTv

    /**
     * 获取右边TextView
     */
    fun getRightTextView():TextView =binding.rightTv

    /**
     * 获取左边边ImageView
     */
    fun getLeftImageView():ImageView =binding.leftIv

    /**
     * 获取右边ImageView
     */
    fun getRightImageView():ImageView =binding.rightIv


}