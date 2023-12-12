package com.zlj.kxapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import me.drakeet.support.toast.ToastCompat


/**
 * Created by zlj on 2021/9/18.
 * @Word：Thought is the foundation of understanding
 * 扩展方法
 */

//--------------------dip2px工具方法拓展------------------------------
private const val DOT_FIVE: Float = 0.5f

fun dip2px(resource: Resources, dip: Float):Int{
    var sDisplayMetrics = resource.displayMetrics
    val density: Float =sDisplayMetrics.density
    return (dip * density + DOT_FIVE).toInt()
}

fun px2dip( resource:Resources,px: Float): Int {
    var sDisplayMetrics = resource.displayMetrics
    val density: Float =sDisplayMetrics.density
    return (px / density + DOT_FIVE).toInt()
}

fun View.dip2px(dip:Float):Int{
    return dip2px(context.resources,dip)
}

fun View.px2dip( px: Float): Int {
    return px2dip(context.resources,px)
}

fun Activity.dip2px(dip:Float):Int{
    return dip2px(this.resources,dip)
}

fun Activity.px2dip( px: Float): Int {
    return px2dip(this.resources,px)
}

fun Fragment.dip2px(dip:Float):Int{
    return return dip2px(this.resources,dip)
}

fun Fragment.px2dip( px: Float): Int {
    return px2dip(this.resources,px)
}

//--------------------Toast工具方法拓展------------------------------

fun toast(context:Context?,msg:String?){
    context?.let {
        msg?.let {
            ToastCompat.makeText(context,msg,Toast.LENGTH_SHORT).show()
        }
    }
}

fun Activity.toast(msg:String?){
    msg?.let { toast(this, it) }
}

fun Fragment.toast(msg:String?){
    msg?.let { toast(activity, it) }
}

fun View.toast(msg:String?){
    msg?.let { toast(context, it) }
}

//--------------------Activity页面跳转工具方法拓展------------------------------

fun <T : Activity?> Activity.readGo(clazz: Class<T>?) {
    startActivity(Intent(this, clazz))
}

fun <T : Activity?> Activity.readGo(clazz: Class<T>?, requestCode: Int) {
    startActivityForResult( Intent(this, clazz), requestCode)
}

fun <T : Activity?> Activity.readGoThenKill(clazz: Class<T>?) {
    readGo(clazz)
    finish()
}

fun <T : Activity?> Activity.readGoForData(clazz: Class<T>?, bundle: Bundle?) {
    val intent = Intent(this, clazz)
    intent.putExtras(bundle!!)
    startActivity(intent)
}

fun <T : Activity?> Activity.readGoThenKillForData(clazz: Class<T>?, bundle: Bundle?) {
    readGoForData(clazz,bundle)
    finish()
}

fun <T : Activity?> Activity.readGoForData(clazz: Class<T>?, bundle: Bundle?, requestCode: Int) {
    val intent = Intent(this, clazz)
    intent.putExtras(bundle!!)
    startActivityForResult(intent, requestCode)
}

//--------------------Fragment页面跳转工具方法拓展------------------------------
fun <T : Activity?> Fragment.readGo(clazz: Class<T>?) {
    startActivity(Intent(activity, clazz))
}

fun <T : Activity?> Fragment.readGo(clazz: Class<T>?, requestCode: Int) {
    startActivityForResult( Intent(activity, clazz), requestCode)
}

fun <T : Activity?> Fragment.readGoForData(clazz: Class<T>?, bundle: Bundle?) {
    val intent = Intent(activity, clazz)
    intent.putExtras(bundle!!)
    startActivity(intent)
}

fun <T : Activity?> Fragment.readGoForData(clazz: Class<T>?, bundle: Bundle?, requestCode: Int) {
    val intent = Intent(activity, clazz)
    intent.putExtras(bundle!!)
    startActivityForResult(intent, requestCode)
}


