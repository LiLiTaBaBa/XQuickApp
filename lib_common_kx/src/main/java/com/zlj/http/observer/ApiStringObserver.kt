package com.zlj.http.observer

import androidx.lifecycle.Observer
import com.zlj.general.exception.ApiException
import com.zlj.general.exception.ErrorStatus
import com.zlj.kxapp.base.BaseApplication
import com.zlj.kxapp.toast
import me.linshen.retrofit2.adapter.ApiErrorResponse
import me.linshen.retrofit2.adapter.ApiResponse
import me.linshen.retrofit2.adapter.ApiSuccessResponse

/**
 * Created by zlj on 2022/1/28.
 * @Word：Thought is the foundation of understanding
 * 直接解析成String返回
 * 继承 androidx.lifecycle.Observer 接口
 */
abstract class ApiStringObserver:Observer<ApiResponse<String>>{

    override fun onChanged(response: ApiResponse<String>) {
        if(response is ApiSuccessResponse){
            onSuccess(response.body)
        }else if(response is ApiErrorResponse){
            onFailure(ApiException(ErrorStatus.SERVER_ERROR,response.errorMessage))
        }
    }

    //解析成功的回调方法
    abstract fun onSuccess(response:String?)

    //解析失败的回调方法 异常都包装在了ApiException当中了
    open fun onFailure(exception: ApiException){
        exception.printStackTrace()
        toast(BaseApplication.getContext(),exception.message)
    }
}