package com.zlj.http.observer

import androidx.lifecycle.Observer
import com.zlj.general.bean.IResponse
import com.zlj.general.exception.ApiException
import com.zlj.general.exception.ErrorStatus
import com.zlj.kxapp.base.BaseApplication
import com.zlj.kxapp.toast
import me.linshen.retrofit2.adapter.ApiErrorResponse
import me.linshen.retrofit2.adapter.ApiResponse
import me.linshen.retrofit2.adapter.ApiSuccessResponse

/**
 * Created by zlj on 2023/5/11.
 * @Word：Thought is the foundation of understanding
 * 解析成 BaseResponse
 */
abstract class ApiResponseObserver<T:IResponse<V>,V>:Observer<ApiResponse<T>>{
    override fun onChanged(response: ApiResponse<T>) {
        if(response is ApiSuccessResponse){
            if(response.body.isSuccess){
                onSuccess(response.body.data)
            }else{
                onFailure(ApiException(response.body.code,response.body.message))
            }
        }else if(response is ApiErrorResponse){
            onFailure(ApiException(ErrorStatus.SERVER_ERROR,response.errorMessage))
        }
    }

    //onSuccess
    abstract fun onSuccess(response:V?)

    //onFailure
    open fun onFailure(exception: ApiException){
        exception.printStackTrace()
        toast(BaseApplication.getContext(),exception.message)
    }
}