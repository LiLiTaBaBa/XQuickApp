package com.zlj.kxapp.widget.list

import com.chad.library.adapter.base.BaseQuickAdapter
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.zlj.kxapp.widget.BindViewHolder

/**
 * Created by zlj on 2021/9/29.
 * @Word：Thought is the foundation of understanding
 */
class PageListDelegate <T>(val smartRefreshLayout: SmartRefreshLayout?,
                           val adapter: BaseQuickAdapter<T, BindViewHolder>?,
                           val baseListView: IBaseListView?): IPagerList<T> {


    override fun showPagingData(list: List<T>?) {
        if (smartRefreshLayout != null) {
            smartRefreshLayout!!.finishRefresh()
        }

        if (list == null || list.isEmpty()) {
            finishLoadMoreEnd()
            if (baseListView != null) {
                baseListView!!.clearData()
            }
            return
        }
        finishLoadMore()
        if (baseListView != null) {
            baseListView!!.clearData()
        }
        if (adapter != null) {
            adapter!!.addData(list)
        }
    }

    /**
     * 完成加载更多
     */
    private fun finishLoadMore() {
        if (adapter == null) return
        if (!adapter!!.loadMoreModule.isEnableLoadMore) {
            smartRefreshLayout!!.finishLoadMore()
        } else {
            adapter!!.loadMoreModule.loadMoreComplete()
        }
    }

    /**
     * 完成加载更多到底
     */
    private fun finishLoadMoreEnd() {
        if (adapter == null) return
        if (smartRefreshLayout == null) return
        if (adapter!!.loadMoreModule.isEnableLoadMore) {
            adapter!!.loadMoreModule.loadMoreEnd()
        } else {
            smartRefreshLayout!!.finishLoadMoreWithNoMoreData()
        }
    }

}
