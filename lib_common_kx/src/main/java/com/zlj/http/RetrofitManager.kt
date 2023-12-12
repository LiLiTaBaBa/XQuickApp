package com.zlj.http

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.zlj.kxapp.base.BaseApplication
import com.zyj.retrofit.adapter.FlowCallAdapterFactory
import me.linshen.retrofit2.adapter.LiveDataCallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.*

/**
 * Created by zlj on 2021/9/18.
 * @Word：Thought is the foundation of understanding
 */
class RetrofitManager private constructor() {

    /**
     * 懒加载Retrofit的实例对象
     */
   private val retrofit: Retrofit by lazy {
        Retrofit.Builder().apply {
            //读取配置文件
            Properties().apply {
                val inputStream=BaseApplication.getContext().assets.open("config.properties")
                load(inputStream)
                baseUrl(getProperty("baseUrl"))
                inputStream.close()

            }
            addCallAdapterFactory(FlowCallAdapterFactory.createAsync())
            addCallAdapterFactory(CoroutineCallAdapterFactory.invoke())
            //支持返回数据的类型为LiveData
            addCallAdapterFactory(LiveDataCallAdapterFactory())
            addConverterFactory(ScalarsConverterFactory.create())
            addConverterFactory(GsonConverterFactory.create())
            client(BaseApplication.getInstance().okHttpClient())
        }.build()
   }

    //静态内部类的方式实现单列
    private object Holder {
        val instance = RetrofitManager()
    }

    companion object{
        //kotlin中的单列写法
        private val instance by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            RetrofitManager()
        }
        //private lateinit var instance:RetrofitManager
        //获取单列对象的方法
//        @JvmStatic
//        private fun getSingleton(): RetrofitManager {
//            if(instance==null){
//                synchronized(RetrofitManager::class.java){
//                    if(instance==null){
//                        instance = RetrofitManager()
//                    }
//                }
//            }
//            return instance!!
//        }

        //根据传入的接口类，创建实现接口的
        // 代理对象
        fun <T>create(clazz: Class<T>):T{
            return instance.retrofit.create(clazz)
        }

    }
}