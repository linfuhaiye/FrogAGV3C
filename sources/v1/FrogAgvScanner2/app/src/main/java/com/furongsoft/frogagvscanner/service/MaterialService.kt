package com.furongsoft.frogagvscanner.service

import com.furongsoft.frogagvscanner.entity.Material
import com.furongsoft.frogagvscanner.entity.Product
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*


/**
 * 原料接口服务
 *
 * @author alex
 */
interface MaterialService {
    @GET("api/v1/agv/mobile/materials")
    fun getMaterials(): Call<List<Material>>

    @GET("api/v1/agv/mobile/products")
    fun getProducts(@Query("areaCode") areaCode: String): Call<List<Product>>

    @FormUrlEncoded
    @POST("api/v1/agv/mobile/stockUp")
    fun stockUp(
        @Field("materialName") materialName: String?,
        @Field("materialCarName") materialCarName: String?,
        @Field("landMaskName") landMaskName: String?
    ): Call<ResponseBody>

    @FormUrlEncoded
    @POST("/api/v1/agv/mobile/cleanCar")
    fun clear(
        @Field("landMaskName")  landMaskName: String?
    ): Call<ResponseBody>
}
