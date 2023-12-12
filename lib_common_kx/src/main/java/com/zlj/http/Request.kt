package com.zlj.http

import android.os.NetworkOnMainThreadException
import androidx.lifecycle.*
import com.zlj.general.bean.IResponse
import com.zlj.general.exception.ApiException
import com.zlj.kxapp.base.BaseApplication
import com.zlj.kxapp.toast
import com.zlj.kxapp.utils.ToastUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.json.JSONException
import java.lang.Exception
import java.net.SocketException
import java.net.SocketTimeoutException

/**
 * Created by zlj on 2021/9/18.
 * @Word：Thought is the foundation of understanding
 */

/**
 * 请求网络的方法
 * Activity以及Fragment中请求的方式
 */

//failure为null的默认处理方式
private suspend fun defaultHandleException(e:Exception,failure: (suspend (apiException: ApiException) -> Unit)? =null){
    if(failure==null){
        val context = BaseApplication.getContext()
        toast(context, ApiException.handleException(e).message)
    }else{
        failure(ApiException.handleException(e))
    }
}


//Activity以及Fragment中封装使用协程的方式请求网络的操作
fun LifecycleOwner.requestHandleFail( block: suspend CoroutineScope.() -> Unit, failure: (suspend (apiException: ApiException) -> Unit)? =null){
    lifecycleScope.launch {
        try {
            block()
        }catch (e:Exception){
            e.printStackTrace()
            defaultHandleException(e,failure)
        }
    }
}


//配合请求返回Flow自处理的时候使用
fun LifecycleOwner.request( action: suspend CoroutineScope.() -> Unit){
    lifecycleScope.launch {
        action()
    }
}

//配合请求返回Flow自处理的时候使用   入参的挂载函数为CoroutineScope的扩展函数
fun ViewModel.request( action: suspend CoroutineScope.() -> Unit){
    viewModelScope.launch {
        action()
    }
}

//Activity以及Fragment中封装使用协程的方式请求网络的操作
fun <T> LifecycleOwner.request(block:suspend CoroutineScope.() -> IResponse<T>,
                               success: (suspend (t:T?) -> Unit),
                               failure: (suspend (apiException: ApiException) -> Unit)? =null){
    lifecycleScope.launch {
        val response= handleResponse<T> { block() }
        if(response.isSuccess){
            success(response.data)
        }else{
            defaultHandleException(ApiException(response.code,response.message),failure)
        }
    }
}

/**
 * ViewModel中请求的方法
 */
//ViewModel中封装使用协程的方式请求网络的操作
fun ViewModel.requestHandleFail(block: suspend CoroutineScope.() -> Unit, failure: (suspend (apiException: ApiException) -> Unit)? =null){
    viewModelScope.launch {
        try {
            block()
        }catch (e:Exception){
            e.printStackTrace()
            defaultHandleException(e,failure)
        }
    }
}

//ViewModel中封装使用协程的方式请求网络的操作
fun <T> ViewModel.request(block:suspend CoroutineScope.() -> IResponse<T>,
                          success: (suspend (t:T?) -> Unit)? =null,
                          failure: (suspend (apiException: ApiException) -> Unit)? =null){
    viewModelScope.launch {
        val response= handleResponse<T> { block() }
        if(response.isSuccess){
            success?.let {
                it(response.data)
            }
        }else{
            defaultHandleException(ApiException(response.code,response.message),failure)
        }
    }
}

//handleResponse
suspend fun <T>handleResponse(block:suspend () -> IResponse<T>): IResponse<T> {
    val response: IResponse<T>
    try {
        response=block()
        if(!response.isSuccess){
            //可以做一个弹出错误信息
            return ApiException(response.code,response.message).toResponse()
        }
    }catch (e:Exception){
        e.printStackTrace()
        return ApiException.handleException(e).toResponse()
    }
    return response
}

//捕获Response相关的异常
fun <T> Flow<T>.catchResponse(showBadInfo: Boolean=true,action: suspend FlowCollector<T>.(ApiException) -> Unit): Flow<T> {
    return catch {
        //把抛出的throwable异常信息用ApiException封装下 包裹
        val apiException= ApiException.handleException(it)
        //此处处理打印以及其他的而一些逻辑问题，通常的处理就是提示下错误信息
        if(showBadInfo){
            ToastUtils.toast(apiException.message)
        }
        //执行传入的action函数动作
        action(apiException)
    }
}

//针对于Retrofit中包裹Flow返回数据的 方法封装
inline fun <reified V:IResponse<T>,T> Flow<V>.mapResponse(): Flow<T> {
    return map {
        //针对code值的分析，做出判断  非200如何处理
        if(it.isSuccess) {//响应的请求成功，取出data数据用Flow包裹返回
            it.data as T
        }else{
            //响应值不成功 抛出ApiException的异常
            throw ApiException(it.code,it.message)
        }
    }
}

//处理返回的BaseResponse业务逻辑
inline fun <reified V:IResponse<T>,T> Flow<V>.handleResponse(showBadInfo:Boolean=true,noinline action:(suspend FlowCollector<T>.(ApiException) -> Unit)?=null ): Flow<T> {
    return mapResponse<V,T>().catchResponse(showBadInfo) {
        val apiException=it
        action?.let {
            it(apiException)
        }
    }
}

//处理出现异常情况使用LiveData回调错误数据的方法
inline fun <reified V:IResponse<T>,T> Flow<V>.catch(liveData: MutableLiveData<V>,showBadInfo:Boolean=true): Flow<V> {
    return catchResponse(showBadInfo) {
        liveData.postValue(it.toResponse<T>() as V)
    }
}
