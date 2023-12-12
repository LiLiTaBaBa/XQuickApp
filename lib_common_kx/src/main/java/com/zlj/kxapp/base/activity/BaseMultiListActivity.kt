package com.zlj.kxapp.base.activity

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.zlj.kxapp.base.adapter.MultiItemAdapter
import com.zlj.kxapp.widget.BindViewHolder

/**
 * Created by zlj on 2021/9/22.
 * @Word：Thought is the foundation of understanding
 */
abstract class BaseMultiListActivity<T : MultiItemEntity> : BaseListActivity<T>(){

    /**
     * 创建支持多布局的Adapter
     * @return
     */
    override fun createQuickAdapter(): BaseQuickAdapter<T, BindViewHolder> {
        return object : MultiItemAdapter<T>(mData) {
            override fun convert(helper: BindViewHolder, item: T) {
                try {
                    this@BaseMultiListActivity.convert(helper, item)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            override fun addItemTypes(tMultiItemAdapter: MultiItemAdapter<T>) {
                this@BaseMultiListActivity.addItemTypes(tMultiItemAdapter)
            }
        } as BaseQuickAdapter<T, BindViewHolder>
    }

    override fun getItemLayoutId(): Int {
        return 0
    }

    /**
     * 添加多布局的View
     * @param moreItemAdapter
     */
    protected abstract fun addItemTypes(moreItemAdapter: MultiItemAdapter<T>?)
}