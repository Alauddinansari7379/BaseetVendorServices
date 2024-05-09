package com.amtech.vendorservices.V.retrofit

import com.amtech.vendorservices.V.Dashboard.model.ModelDashboard
import com.amtech.vendorservices.V.Login.model.ModelLogin
import com.amtech.vendorservices.V.MyTranslotor.Model.ModelMyTra
import com.amtech.vendorservices.V.Order.Model.MAllOrder.ModelAllOrder
import com.amtech.vendorservices.V.Order.Model.ModeUpdatePrice.ModelUpdatePrice
import com.amtech.vendorservices.V.Order.Model.ModelComplete
import com.amtech.vendorservices.V.Order.Model.ModelOrderDet.ModelOrderDet
import com.amtech.vendorservices.V.Order.Model.ModelRelatedSer.ModelServiceRet
import com.amtech.vendorservices.V.Order.Model.ModelSendSer.ModelSendSer
import com.amtech.vendorservices.V.Order.Model.ModelServiceReq
import com.amtech.vendorservices.V.TranslatorServices.activity.model.ModeCar.ModelGetListCar
import com.amtech.vendorservices.V.TranslatorServices.activity.model.ModelServiceList
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*


interface ApiInterface {


    @POST("auth/vendor/login")
    fun login(
        @Query("email") email: String,
        @Query("password") password: String,
    ): Call<ModelLogin>

    @GET("vendor/dashboard-data")
    fun dashboard(
        @Header("Authorization") authorization: String
    ): Call<ModelDashboard>

    @GET("vendor/completed-orders")
    fun completeOrder(
        @Header("Authorization") authorization: String,
        @Query("limit") limit: String,
        @Query("offset") offset: String,
        @Query("status") status: String,
    ): Call<ModelComplete>

    @GET("vendor/all-orders")
    fun allOrders(
        @Header("Authorization") authorization: String,
    ): Call<ModelAllOrder>

    @GET("vendor/cancl-orders")
    fun cancelledOrders(
        @Header("Authorization") authorization: String,
    ): Call<ModelComplete>

    @GET("vendor/current-orders")
    fun confirmedOrders(
        @Header("Authorization") authorization: String,
    ): Call<ModelComplete>

    @GET("vendor/service-request?")
    fun serviceRequest(
        @Header("Authorization") authorization: String
    ): Call<ModelServiceReq>

    @GET("vendor/get-products-list")
    fun serviceList(
        @Header("Authorization") authorization: String
    ): Call<ModelServiceList>

    @GET("vendor/get-products-list")
    fun serviceListCar(
        @Header("Authorization") authorization: String
    ): Call<ModelGetListCar>

    @GET("vendor/profile")
    fun getProfile(
        @Header("Authorization") authorization: String
    ): Call<ModelMyTra>

    @GET("vendor/service-related")
    fun getRelatedService(
        @Header("Authorization") authorization: String,
        @Query("tr_from") tr_from:String,
        @Query("tr_to") tr_to:String,
        @Query("type") type:String,
        @Query("price") price:String,
    ): Call<ModelServiceRet>
    @GET("vendor/service-related")
    fun getRelatedServiceCar(
        @Header("Authorization") authorization: String,
        @Query("driv_type") driv_type:String,
        @Query("car_type") car_type:String,
         @Query("price") price:String,
    ): Call<ModelServiceRet>

    @GET("vendor/service-related")
    fun getRelatedServiceHome(
        @Header("Authorization") authorization: String,
        @Query("hometype") hometype:String,
          @Query("price") price:String,
    ): Call<ModelServiceRet>

    @GET("vendor/sendservice")
    fun sendService(
        @Header("Authorization") authorization: String,
        @Query("food_id") food_id:String,
        @Query("request_id") request_id:String,
    ): Call<ModelSendSer>

    @PUT("vendor/update-order-status")
    fun statuesChange(
        @Header("Authorization") authorization: String,
        @Query("order_id") order_id:String,
        @Query("status") status:String,
    ): Call<ModelSendSer>

    @GET("vendor/order-details")
    fun orderDetails(
        @Header("Authorization") authorization: String,
         @Query("order_id") order_id:String,
    ): Call<ModelOrderDet>

    @POST("vendor/update-price")
    fun updatePrice(
        @Header("Authorization") authorization: String,
         @Query("food_id") food_id:String,
         @Query("price") price:String,
    ): Call<ModelUpdatePrice>

    @Multipart
    @POST("vendor/product/store")
    fun addNewService(
        @Header("Authorization") authorization: String,
        @Query("food_type") food_type: String,
        @Part image: MultipartBody.Part,
        @Query("price") price: String,
        @Query("port_video") port_video: String,
        @Query("ser_hour") ser_hour: String,
        @Query("name") name: String,
        @Query("description") description: String,
        @Query("dates") dates: String,
        @Query("available_time_starts") available_time_starts: String,
        @Query("available_time_ends") available_time_ends: String,
        @Query("tr_to") tr_to: String,
        @Query("tr_from") tr_from: String,
        @Query("drone") drone: String,
        @Query("longitude") longitude: String,
        @Query("latitude") latitude: String,
        @Query("zone_id") zone_id: String,
        @Query("Dates") Dates: String,
    ): Call<ModelServiceList>


    @POST("vendor/product/store")
    @Multipart
    fun addCar(
        @Header("Authorization") authorization: String,
        @Query("food_type") food_type: String,
        @Query("price") price: String,
        @Query("port_video") port_video: String,
        @Query("home_days") home_days: String,
        @Query("name") name: String,
        @Query("description") description: String,
        @Query("dates") dates: String,
        @Query("available_time_starts") available_time_starts: String,
        @Query("available_time_ends") available_time_ends: String,
        @Query("trperson") trperson: String,
        @Query("car_model") car_model: String,
        @Query("car_type") car_type: String,
        @Query("longitude") longitude: String,
        @Query("latitude") latitude: String,
        @Query("zone_id") zone_id: String,
        @Query("driv_type") driv_type: String,
      //  @Query("ammenties[]") ammenties: String,
        @Part image: MultipartBody.Part,
    ): Call<ModelServiceList>

    @POST("vendor/product/store")
    @Multipart
    fun addHome(
        @Header("Authorization") authorization: String,
        @Query("food_type") food_type: String,
        @Query("price") price: String,
         @Query("home_days") home_days: String,
        @Query("name") name: String,
        @Query("description") description: String,
        @Query("dates") dates: String,
        @Query("available_time_starts") available_time_starts: String,
        @Query("available_time_ends") available_time_ends: String,
        @Query("car_type") car_type: String,
         @Query("longitude") longitude: String,
        @Query("latitude") latitude: String,
        @Query("zone_id") zone_id: String,
         @Query("ammenties[]") ammenties: List<String>,
         @Query("ammenties[]") ammenties1: List<String>,
        @Part image: MultipartBody.Part,
    ): Call<ModelServiceList>
//
//
//    @GET("viewmedicalinfo")
//    fun viewMedicalInfo(
//        @Query("nurse_id") nurse_id: String,
//        @Query("idToken") idToken: String,
//        @Query("group") group: String,
//        @Query("patient_id") patient_id: String
//    ): Call<ModelMedLIst>
//
//    @Multipart
//    @POST("adddoctor")
//    fun adddoctor(
//        @Query("nurse_id") nurse_id: String,
//        @Query("idToken") idToken: String,
//        @Query("group") group: String,
//        @Query("name") name: String,
//        @Query("password") password: String,
//        @Query("email") email: String,
//        @Query("address") address: String,
//        @Query("phone") phone: String,
//        @Query("department") department: String,
//        @Query("reg_no") reg_no: String,
//        @Query("profile") profile: String,
//        @Part img_url: MultipartBody.Part,
//    ): Call<ModelNewAppoint>


}