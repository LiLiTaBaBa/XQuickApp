package com.zlj.kxapp.widget

import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 * Created by zlj on 2021/9/23.
 * @Word：Thought is the foundation of understanding
 */
class BindViewHolder(view:View):BaseViewHolder(view) {
    val vB:ViewDataBinding? by lazy {
        DataBindingUtil.bind(view)
    }
}