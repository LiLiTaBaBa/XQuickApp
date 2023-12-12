package com.zlj.kxapp.base.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.PagerAdapter

/**
 * Created by zlj on 2021/6/4.
 * @Word：Thought is the foundation of understanding
 * @since 1.0.1
 */
class TabPageAdapter(fm: FragmentManager, private val titles: List<String>, private val fragments: List<Fragment>)
    : FragmentPagerAdapter(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int {
        return fragments.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titles[position]
    }

    override fun getItemId(position: Int): Long {
        //注意一定要重写
        return fragments[position].hashCode().toLong()
    }

    override fun getItemPosition(`object`: Any): Int {
        //第一种方法是直接返回POSITION_NONE
        //第二种就是先判断是否发生了修改再判断
        val index = fragments.indexOf(`object`)
        if (index == -1) {
            return PagerAdapter.POSITION_NONE
        }
        return PagerAdapter.POSITION_UNCHANGED
    }
}