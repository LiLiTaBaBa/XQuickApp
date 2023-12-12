package com.zlj.kxapp.base.activity

import android.annotation.TargetApi
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.foundation.app.arc.activity.BaseFragmentManagerActivity
import com.gyf.barlibrary.ImmersionBar
import com.zlj.kxapp.R
import com.zlj.kxapp.utils.AppManager

/**
 * Created by zlj on 2021/9/17.
 * @Word：Thought is the foundation of understanding
 */
abstract class BaseActivity : BaseFragmentManagerActivity(){
    lateinit var mAppContext: Context
    lateinit var mContext: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        //赋值context对象
        mContext = this
        val extras = intent.extras
        extras?.let { getBundleExtras(it) }
        super.onCreate(savedInstanceState)
        setActivityRequestedOrientation()
        AppManager.addActivity(this)
        //AppContext 不同的地方使用
        mAppContext = applicationContext
        //初始化页面的View以及事件监听
        //initViewsAndEvent()
        //设置沉浸式状态栏
        setImmersionBar()
    }

    open fun setActivityRequestedOrientation() {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    override fun afterSuperOnCreate(savedInstanceState: Bundle?) {
    }

    override fun beforeSuperOnCreate(savedInstanceState: Bundle?) {
    }

    override fun initViewModel() {
    }

    /**
     * 设置沉浸式状态栏
     */
    protected open fun setImmersionBar() {
        if(isNeedImmersionBar()){
            ImmersionBar.with(this)
                    .fitsSystemWindows(true)
                    .navigationBarColor(getNavigationBarColor())
                    .statusBarDarkFont(true, 0.2f).init()
            setLightNavigationBar(true)
        }
    }

    /**
     * 设置导航栏的背景色
     * @return
     */
    protected open fun getNavigationBarColor(): Int = R.color.white

    /**
     * 获取状态栏颜色
     */
    protected open fun getStatusBarColor(): Int = R.color.white

    /**
     * 获取屏幕宽度由
     * @return
     */
    protected open fun getScreenWidth(): Int {
        return resources!!.displayMetrics.widthPixels
    }

    /**
     * 修改NavigationBar按键颜色 两色可选【黑，白】
     * @param light
     */
    protected open fun setLightNavigationBar(light: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            var vis = window.decorView.systemUiVisibility
            vis = if (light) {
                vis or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR // 黑色
            } else {
                vis and View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR.inv() //白色
            }
            window.decorView.systemUiVisibility = vis
        }
    }


    //Android 4.0 之后提供的一个API，它的主要作用是提示开发者在系统内存不足的时候，
    // 通过处理部分资源来释放内存，从而避免被 Android 系统杀死。
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        Glide.get(this).trimMemory(level)
    }

    //在系统内存不足，所有后台程序（优先级为background的进程，
    // 不是指后台运行的进程）都被杀死时，系统会调用OnLowMemory。
    override fun onLowMemory() {
        super.onLowMemory()
        Glide.get(this).clearMemory()
    }

    /**
     * 是否需要沉浸式
     */
    open fun isNeedImmersionBar():Boolean = true

    /**
     * 处理intent中携带的Bundle数据
     * @param bundle
     */
    open fun getBundleExtras(bundle: Bundle) {

    }

    /**
     * 关闭页面的方法
     */
    override fun finish() {
        AppManager.removeActivity(this)
        super.finish()
    }

     /**
     * 不需要刷新纯列表
     * @param recyclerView
     * @param adapter
     * @param isNeedLine
     */
    open fun setRecyclerView(recyclerView:RecyclerView, adapter: RecyclerView.Adapter<*>, isNeedLine:Boolean) {
        val  manager =  LinearLayoutManager(mContext);
         recyclerView.layoutManager = manager;
        recyclerView.setHasFixedSize(true);
         recyclerView.itemAnimator = DefaultItemAnimator();
        if (isNeedLine)
            recyclerView.addItemDecoration( DividerItemDecoration(mContext, LinearLayoutManager.VERTICAL));
         recyclerView.adapter = adapter;
    }


    /**
     * App的文字不跟随系统设置的改变
     * @return Resources
     */
    override fun getResources(): Resources? {
        val resources = super.getResources()
        if (resources != null && resources.configuration != null && resources.configuration.fontScale != 1.0f) {
            val configuration = resources.configuration
            configuration.fontScale = 1.0f
            createConfigurationContext(configuration)
        }
        return resources
    }

    /**
     * 页面触摸时，如果软件盘打开则关闭
     */
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (null != this.currentFocus) {
            val mInputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            return mInputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        }
        return super.onTouchEvent(event)
    }

    override fun onDestroy() {
        super.onDestroy()
        if(isNeedImmersionBar()){
            ImmersionBar.with(this).destroy()
        }
    }

    //https://blog.csdn.net/oLengYueZa/article/details/109207492
    override fun onBackPressed() {
        //super.onBackPressed();
        finishAfterTransition()
    }

}