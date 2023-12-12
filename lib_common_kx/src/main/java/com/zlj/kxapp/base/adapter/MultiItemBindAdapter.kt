package com.zlj.kxapp.base.adapter

import androidx.annotation.LayoutRes
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.chad.library.adapter.base.module.LoadMoreModule
import com.zlj.kxapp.widget.BindViewHolder

/**
 * Created by zlj on 2021/6/7.
 * @Word：Thought is the foundation of understanding
 * @since 1.0.3
 */
abstract class MultiItemBindAdapter<T : MultiItemEntity>(data: MutableList<T>?)
    : BaseMultiItemQuickAdapter<T, BindViewHolder>(data) , LoadMoreModule {
    protected abstract fun addItemTypes(tMultiItemAdapter: MultiItemBindAdapter<T>)

    /**
     * 注册 ItemVIew的布局样式
     * @param type
     * @param layoutResId
     */
    fun _addItemType(type: Int, @LayoutRes layoutResId: Int) {
        addItemType(type, layoutResId)
    }

    init {
        addItemTypes(this)
    }
}