package com.zlj.kxapp.base.fragment

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.fragment.app.Fragment
import com.zlj.kxapp.R
import com.zlj.kxapp.base.adapter.TabPageAdapter
import com.zlj.kxapp.databinding.ActivityBaseMagicTabBinding
import com.zlj.kxapp.databinding.ActivitySmartListBinding
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.UIUtil
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView

/**
 * Created by zlj on 2021/9/22.
 * @Word：Thought is the foundation of understanding
 */
abstract class BaseMagicTabFragment:BaseFragment<ActivityBaseMagicTabBinding>() {
    lateinit var titles: MutableList<String>
    lateinit var list: MutableList<Fragment>

    override fun init(savedInstanceState: Bundle?) {
        titles = mutableListOf()
        list = mutableListOf()
        //加载Tabs
        showNavigationData()
    }

    /**
     * 加载Tabs
     */
    protected open fun showNavigationData() {
        addTitles()
        addFragments()
        binding.viewPager.adapter = TabPageAdapter(childFragmentManager, titles, list)
        val commonNavigator = CommonNavigator(mContext)
        commonNavigator.scrollPivotX = 0.65f
        commonNavigator.isAdjustMode = true // 标题是否平分屏幕
        commonNavigator.adapter = object : CommonNavigatorAdapter() {
            override fun getCount(): Int {
                return if (null == list) 0 else list.size
            }

            override fun getTitleView(context: Context, i: Int): IPagerTitleView {
                val simplePagerTitleView = SimplePagerTitleView(context)
                simplePagerTitleView.text = titles[i]
                simplePagerTitleView.normalColor = Color.parseColor("#333333")
                simplePagerTitleView.selectedColor = Color.parseColor("#FE4F2B")
                simplePagerTitleView.textSize = 15f
                simplePagerTitleView.setOnClickListener { binding.viewPager.currentItem = i }
                return simplePagerTitleView
            }

            override fun getIndicator(context: Context): IPagerIndicator {
                val indicator = LinePagerIndicator(context)
                indicator.mode = LinePagerIndicator.MODE_WRAP_CONTENT
                indicator.lineHeight = UIUtil.dip2px(context, 3.0).toFloat()
                indicator.roundRadius = UIUtil.dip2px(context, 0.0).toFloat()
                indicator.startInterpolator = AccelerateInterpolator()
                indicator.endInterpolator = DecelerateInterpolator(2.0f)
                indicator.setColors(Color.parseColor("#FE4F2B"))
                return indicator
            }
        }
        binding.magicIndicator.navigator = commonNavigator
        binding.viewPager.currentItem = 0
        binding.viewPager.offscreenPageLimit = list.size
        ViewPagerHelper.bind( binding.magicIndicator,  binding.viewPager)
    }

    /**
     * 添加Fragments
     */
    protected abstract fun addFragments()

    /**
     * 添加Titles
     */
    protected abstract fun addTitles()


    override fun createViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): ActivityBaseMagicTabBinding {
        return ActivityBaseMagicTabBinding.inflate(layoutInflater)
    }

}