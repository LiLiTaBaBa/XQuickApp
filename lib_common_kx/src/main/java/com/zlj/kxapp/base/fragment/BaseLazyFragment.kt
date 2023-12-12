package com.zlj.kxapp.base.fragment

import androidx.viewbinding.ViewBinding

/**
 * Created by zlj on 2021/9/22.
 * @Word：Thought is the foundation of understanding
 */
 abstract class BaseLazyFragment<T:ViewBinding>:BaseFragment<T>(){

    private var isLoaded = false

    override fun onResume() {
        super.onResume()
        if (!isLoaded) {
            lazyInit()
            isLoaded = true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        isLoaded = false
    }

    abstract fun lazyInit()

}