package com.zlj.kxapp.base.activity

import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.zlj.kxapp.base.adapter.BaseItemAdapter
import com.zlj.kxapp.databinding.ActivitySmartListBinding
import com.zlj.kxapp.widget.BindViewHolder
import com.zlj.kxapp.widget.list.IBaseListView
import com.zlj.kxapp.widget.list.IPagerList
import com.zlj.kxapp.widget.list.PageListDelegate

/**
 * Created by zlj on 2021/9/18.
 * @Word：Thought is the foundation of understanding
 */
open abstract class BaseListActivity<T>:BaseActivity(), IBaseListView,
    IPagerList<T> {

    protected val binding by lazyAndSetRoot<ActivitySmartListBinding>()

    //分页需要的参数
    var pageSize = 10
    //当前页码
    var pageNum = 1
    //数据集合
    lateinit var mData: MutableList<T>
    lateinit var mPowerRefresh: SmartRefreshLayout
    lateinit var mRecyclerView: RecyclerView
    lateinit var mAdapter: BaseQuickAdapter<T, BindViewHolder>
    lateinit var pageListDelegate: PageListDelegate<T>
    abstract fun requestServerData()

    override fun init(savedInstanceState: Bundle?) {
        mData = mutableListOf()
        mPowerRefresh = binding.smartRefreshLayout
        mRecyclerView = binding.recyclerView
        //初始化adapter
        mAdapter = createQuickAdapter()
        //设置adapter
        setRecyclerView(mRecyclerView, mAdapter, false)

        mPowerRefresh.setEnableRefresh(true)
        mPowerRefresh.setEnableLoadMore(!mAdapter.loadMoreModule.isEnableLoadMore)



        pageListDelegate= PageListDelegate(adapter = mAdapter,smartRefreshLayout = mPowerRefresh,baseListView = this)

        if(mAdapter.loadMoreModule.isEnableLoadMore){
            mAdapter.loadMoreModule.setOnLoadMoreListener {
                pageNum++
                requestServerData()
            }
        }else{
            //可以 设置是否是自动下拉刷新  默认不是
            mPowerRefresh.setOnLoadMoreListener(OnLoadMoreListener {
                pageNum++
                requestServerData()
            })
        }

        //设置监听
        mPowerRefresh.setOnRefreshListener(OnRefreshListener {
            pageNum = 1
            requestServerData()
        })

    }

    override fun bindData() {
    }


    override fun showPagingData(list: List<T>?) {
        pageListDelegate.showPagingData(list)
    }

    /**
     * 根据条件清空列表数据
     */
    override fun clearData() {
        if (pageNum == 1) {
            mData.clear()
            mAdapter.replaceData(mData)
        }
    }


    /**
     * 创建BaseQuickAdapter
     * 可以自定义创建
     * @return
     */
    protected open fun createQuickAdapter(): BaseQuickAdapter<T, BindViewHolder> {
        return object : BaseItemAdapter<T, BindViewHolder>(getItemLayoutId(), mData) {
            override fun convert(helper: BindViewHolder, item: T) {
                try {
                    this@BaseListActivity.convert(helper, item)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    /**
     * 加载item布局
     * @return
     */
    protected abstract fun getItemLayoutId(): Int

    /**
     * 绑定视图
     * @param helper
     * @param item
     */
    protected abstract fun convert(helper: BindViewHolder, item: T)

}