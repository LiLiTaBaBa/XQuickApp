package com.zlj.kxapp.widget.list

/**
 * Created by zlj on 2021/9/29.
 * @Word：Thought is the foundation of understanding
 */
interface IPagerList<T> {
    /**
     * 成功之后做的事情
     * @param list
     */
    fun showPagingData(list: List<T>?)
}