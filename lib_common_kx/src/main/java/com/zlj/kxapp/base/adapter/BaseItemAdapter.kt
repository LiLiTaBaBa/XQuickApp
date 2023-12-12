package com.zlj.kxapp.base.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder

abstract class BaseItemAdapter<T,vB:BaseViewHolder>(layoutId:Int,data:MutableList<T>)
    :BaseQuickAdapter<T,vB>(layoutId,data), LoadMoreModule