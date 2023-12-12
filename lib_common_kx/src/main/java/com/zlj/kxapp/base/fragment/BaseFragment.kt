package com.zlj.kxapp.base.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.foundation.app.arc.fragment.BaseFragmentManagerFragment
import com.foundation.app.arc.utils.ext.ViewBindingLifecycleListener
import com.zlj.kxapp.utils.ViewBindingUtils

/**
 * Created by zlj on 2021/9/17.
 * @Word：Thought is the foundation of understanding
 */
open abstract class BaseFragment<T:ViewBinding>: BaseFragmentManagerFragment(),
    ViewBindingLifecycleListener {
    var mAppContext: Context?=null
    var mContext: Context?=null
    lateinit var binding:T

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding=createViewBinding(inflater,container)

        if(binding is ViewDataBinding){
            (binding as ViewDataBinding).lifecycleOwner = this
        }

        return binding.root
    }

    protected open fun createViewBinding(inflater: LayoutInflater, container: ViewGroup?): T {
        return  ViewBindingUtils.binding(this)
        //return DataBindingUtil.inflate(inflater,getContentViewId(),container,false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mContext=activity
        mAppContext= activity?.applicationContext
        super.onViewCreated(view, savedInstanceState)
    }

    override fun bindData() {
    }

    override fun onDestroy() {
        super.onDestroy()
        if(binding is ViewDataBinding){
            (binding as ViewDataBinding).unbind()
        }
    }


    /**
     * getScreenWidth
     * @return
     */
    protected open fun getScreenWidth(): Int {
        return resources.displayMetrics.widthPixels
    }

    /**
     * 不需要刷新纯列表
     * @param recyclerView
     * @param adapter
     * @param isNeedLine
     */
    open fun setRecyclerView(recyclerView: RecyclerView, adapter: RecyclerView.Adapter<*>?, isNeedLine: Boolean) {
        val manager = LinearLayoutManager(mContext)
        recyclerView.layoutManager = manager
        recyclerView.setHasFixedSize(true)
        recyclerView.itemAnimator = DefaultItemAnimator()
        if (isNeedLine) recyclerView.addItemDecoration(DividerItemDecoration(mContext, LinearLayoutManager.VERTICAL))
        recyclerView.adapter = adapter
    }

}














