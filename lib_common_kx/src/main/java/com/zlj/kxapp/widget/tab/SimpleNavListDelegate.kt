package com.zlj.kxapp.widget.tab

import android.view.View
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.zlj.kxapp.base.adapter.TabPageAdapter
import com.zlj.kxapp.dip2px
import net.lucode.hackware.magicindicator.buildins.UIUtil.getScreenWidth
/**
 * Created by zlj on 2021/9/29.
 * @Word：Thought is the foundation of understanding
 */
class SimpleNavListDelegate(val fragmentManager: FragmentManager,
                            val viewPager: ViewPager, val tabLayout: TabLayout, val line: View,
                            val baseTabView: IBaseTabView
                      ):INavList {
    private var left: Int=0
    lateinit var titles: MutableList<String>
    lateinit var list: MutableList<Fragment>

    override fun showNavigationData() {
        baseTabView.addTitles()
        baseTabView.addFragments()
        viewPager.adapter = TabPageAdapter(fragmentManager, titles, list)
        viewPager.offscreenPageLimit = titles.size
        tabLayout.setupWithViewPager(viewPager)
        val lp =  line.layoutParams as FrameLayout.LayoutParams
        lp.leftMargin = getScreenWidth(viewPager.context) / (list.size * 2) - dip2px(viewPager.resources,13f)
        left = lp.leftMargin
        line.layoutParams = lp
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                val lp =  line.layoutParams as FrameLayout.LayoutParams
                lp.leftMargin = (positionOffset * (getScreenWidth(viewPager.context) / list.size)) as Int + left + position * (getScreenWidth(viewPager.context) / list.size)
                line.layoutParams = lp
            }

            override fun onPageSelected(position: Int) {}
            override fun onPageScrollStateChanged(state: Int) {}
        })
    }
}