package com.zlj.kxapp.widget.tab

import android.content.Context
import android.graphics.Color
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.ViewPager
import com.zlj.kxapp.base.adapter.TabPageAdapter
import net.lucode.hackware.magicindicator.MagicIndicator
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.UIUtil
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView

/**
 * Created by zlj on 2021/9/29.
 * @Word：Thought is the foundation of understanding
 */
class MagicNavListDelegate(val fragmentManager: FragmentManager,
                           val viewPager: ViewPager, val magicIndicator: MagicIndicator,
                           val baseTabView: IBaseTabView
                      ):INavList {
    lateinit var titles: MutableList<String>
    lateinit var list: MutableList<Fragment>

    override fun showNavigationData() {
        baseTabView.addTitles()
        baseTabView.addFragments()
        viewPager.adapter = TabPageAdapter(fragmentManager, titles, list)
        val commonNavigator = CommonNavigator(viewPager.context)
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
                simplePagerTitleView.setOnClickListener { viewPager.currentItem = i }
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
        magicIndicator.navigator = commonNavigator
        viewPager.currentItem = 0
        viewPager.offscreenPageLimit = list.size
        ViewPagerHelper.bind( magicIndicator,  viewPager)
    }
}