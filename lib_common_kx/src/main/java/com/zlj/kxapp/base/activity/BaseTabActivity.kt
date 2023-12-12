package com.zlj.kxapp.base.activity

import android.os.Bundle
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.zlj.kxapp.base.adapter.TabPageAdapter
import com.zlj.kxapp.databinding.ActivityBaseTabBinding
import com.zlj.kxapp.dip2px

/**
 * Created by zlj on 2021/9/22.
 * @Word：Thought is the foundation of understanding
 */
abstract class BaseTabActivity: BaseActivity() {
    lateinit var titles: MutableList<String>
    lateinit var list: MutableList<Fragment>
    lateinit var mAdapter: TabPageAdapter
    var left = 0

    protected val binding by lazyAndSetRoot<ActivityBaseTabBinding>()

    override fun init(savedInstanceState: Bundle?) {
        list= mutableListOf()
        titles= mutableListOf()
        //加载Tabs
        showNavigationData()
    }

    override fun bindData() {
    }

    /**
     * 加载Tabs
     */
    protected open fun showNavigationData() {
        //添加标题
        addTitles()
        //添加Fragment
        addFragments()
        mAdapter = TabPageAdapter(supportFragmentManager, titles, list)
        binding.viewPager.offscreenPageLimit = titles.size
        binding.viewPager.adapter = mAdapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)
        val lp =  binding.line.layoutParams as FrameLayout.LayoutParams
        lp.leftMargin = getScreenWidth() / (list.size * 2) - dip2px(13f)
        left = lp.leftMargin
        binding.line.layoutParams = lp
        binding.viewPager.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                val lp =  binding.line.layoutParams as FrameLayout.LayoutParams
                lp.leftMargin = (positionOffset * (getScreenWidth() / list.size)) as Int + left + position * (getScreenWidth() / list.size)
                binding.line.layoutParams = lp
            }

            override fun onPageSelected(position: Int) {}
            override fun onPageScrollStateChanged(state: Int) {}
        })
    }

    /**
     * 添加需要的Fragment
     */
    protected abstract fun addFragments()

    /**
     * 添加标题
     */
    protected abstract fun addTitles()

}