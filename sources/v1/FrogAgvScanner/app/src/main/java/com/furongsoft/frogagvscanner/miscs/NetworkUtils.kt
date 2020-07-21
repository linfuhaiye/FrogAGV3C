package com.furongsoft.frogagvscanner.miscs

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * 网络工具
 *
 * @author alex
 */
object NetworkUtils {
    /**
     * 使用基础URL[baseUrl]和服务类型[clazz]创建网络服务 [T]
     */
    fun <T : Any> createService(baseUrl: String, clazz: Class<T>): T {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder().connectTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(15, TimeUnit.SECONDS).writeTimeout(15, TimeUnit.SECONDS).build()
            )
            .build()
            .create(clazz)
    }
}