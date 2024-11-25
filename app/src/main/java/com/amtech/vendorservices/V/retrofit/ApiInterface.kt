package com.amtech.vendorservices.V.retrofit

import com.amtech.vendorservices.V.Dashboard.model.ModelDashTra.ModelDashTra
import com.amtech.vendorservices.V.Dashboard.model.ModelDashboard
import com.amtech.vendorservices.V.Dashboard.model.modelStatic.ModelStatics
import com.amtech.vendorservices.V.Login.model.ModelLogin
import com.amtech.vendorservices.V.MyTranslotor.Model.ModelMyTra
import com.amtech.vendorservices.V.Order.Model.ModeUpdatePrice.ModelUpdatePrice
import com.amtech.vendorservices.V.Order.Model.ModelChange.ModelChange
import com.amtech.vendorservices.V.Order.Model.ModelComplete
import com.amtech.vendorservices.V.Order.Model.ModelOrderDet.ModelOrderDet
import com.amtech.vendorservices.V.Order.Model.ModelOrderDetail.ModelOrderDetail
import com.amtech.vendorservices.V.Order.Model.ModelRelatedSer.ModelServiceRet
import com.amtech.vendorservices.V.Order.Model.ModelSendSer.ModelSendSer
import com.amtech.vendorservices.V.Order.Model.ModelServiceReq
import com.amtech.vendorservices.V.Order.modeldetails.ModelDetails
import com.amtech.vendorservices.V.TranslatorServices.activity.model.ModeCar.ModelGetListCar
import com.amtech.vendorservices.V.TranslatorServices.activity.model.ModelGetProdile.ModelGetProfile
import com.amtech.vendorservices.V.TranslatorServices.activity.model.ModelServiceList
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Query


interface ApiInterface {


    @POST("auth/vendor/login")
    fun login(
        @Query("email") email: String,
        @Query("password") password: String,
        @Query("device_token") device_token: String,
        @Query("device_type") device_type: String,
    ): Call<ModelLogin>

    @GET("vendor/dashboard-data")
    fun dashboard(
        @Header("Authorization") authorization: String
    ): Call<ModelDashboard>

    @GET("vendor/dashboard-data")
    fun dashboardTra(
        @Header("Authorization") authorization: String
    ): Call<ModelDashTra>

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
    ): Call<ModelOrderDetail>

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
        @Query("tr_from") tr_from: String,
        @Query("tr_to") tr_to: String,
        @Query("type") type: String,
        @Query("price") price: String,
    ): Call<ModelServiceRet>

    @GET("vendor/service-related")
    fun getRelatedServiceCar(
        @Header("Authorization") authorization: String,
        @Query("driv_type") driv_type: String,
        @Query("car_type") car_type: String,
        @Query("price") price: String,
    ): Call<ModelServiceRet>

    @GET("vendor/service-related")
    fun getRelatedServiceHome(
        @Header("Authorization") authorization: String,
        @Query("hometype") hometype: String,
        @Query("price") price: String,
    ): Call<ModelServiceRet>

    @GET("vendor/sendservice")
    fun sendService(
        @Header("Authorization") authorization: String,
        @Query("food_id") food_id: String,
        @Query("request_id") request_id: String,
    ): Call<ModelSendSer>

    @POST("vendor/product/changes_status")
    fun changesStatus(
        @Header("Authorization") authorization: String,
        @Query("id") id: String,
        @Query("restaurant_id") restaurant_id: String,
        @Query("food_id") food_id: String,
        @Query("status") status: String,
    ): Call<ModelChange>

    @POST("vendor/product/active")
    fun active(
        @Header("Authorization") authorization: String,
        @Query("id") id: String,
        @Query("IsActive") IsActive: String,
    ): Call<ModelServiceList>

    @GET("vendor/product/edit")
    fun getProfileList(
        @Header("Authorization") authorization: String,
        @Query("id") id: String,
    ): Call<ModelGetProfile>

    @POST("vendor/product/statistics")
    fun getStatistics(
        @Header("Authorization") authorization: String,
        @Query("filter") filter: String,
    ): Call<ModelStatics>

    @PUT("vendor/update-order-status")
    fun statuesChange(
        @Header("Authorization") authorization: String,
        @Query("order_id") order_id: String,
        @Query("status") status: String,
    ): Call<ModelSendSer>

    @GET("vendor/order-details")
    fun orderDetails(
        @Header("Authorization") authorization: String,
        @Query("order_id") order_id: String,
    ): Call<ModelOrderDet>

    @POST("vendor/update-price")
    fun updatePrice(
        @Header("Authorization") authorization: String,
        @Query("food_id") food_id: String,
        @Query("price") price: String,
        @Query("request_id") request_id: String,
    ): Call<ModelUpdatePrice>

    @Multipart
    @POST("vendor/docs_upload")
    fun docsUpload(
        @Header("Authorization") authorization: String,
        @Query("id") id: String,
        @Part documents: MultipartBody.Part,
        @Part image: MultipartBody.Part,
    ): Call<ModelServiceList>

    @Multipart
    @POST("vendor/product/store")
    fun addNewService(
        @Header("Authorization") authorization: String,
        @Query("food_type") food_type: String,
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
        @Part amenities: List<MultipartBody.Part>, // Updated to MultipartBody.Part for amenities
        @Part image: MutableList<MultipartBody.Part>,
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
        @Part amenities: List<MultipartBody.Part>, // Updated to MultipartBody.Part for amenities
        @Part image: MutableList<MultipartBody.Part>,
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
        @Part amenities: List<MultipartBody.Part>, // Updated to MultipartBody.Part for amenities
        @Part image_number: List<MultipartBody.Part>, // Updated to MultipartBody.Part for amenities
        @Part image: MutableList<MultipartBody.Part>,
    ): Call<ModelServiceList>

    @POST("vendor/product/update")
    @Multipart
    fun updateService(
        @Header("Authorization") authorization: String,
        @Query("id") id: String,
        @Query("name") name: String,
        @Query("description") description: String,
        @Query("price") price: String,
        @Query("type") type: String,
        @Query("tr_from") tr_from: String,
        @Query("tr_to") tr_to: String,
        @Query("dates") dates: String,
        @Query("driv_type") driv_type: String,
        @Query("ser_hour") ser_hour: String,
        @Query("car_type") car_type: String,
        @Query("car_model") car_model: String,
        @Query("car_brand") car_brand: String,
        @Query("home_days") home_days: String,
        @Part images_number: List<MultipartBody.Part>, // Updated to MultipartBody.Part for amenities
        @Part amenities: List<MultipartBody.Part>, // Updated to MultipartBody.Part for amenities
        @Part image: MutableList<MultipartBody.Part>,
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
    @POST("vendor/product/serviceById")
    fun getDetails(
        @Header("Authorization") authorization: String,
        @Query("id") id: String
    ): Call<ModelDetails>

    @POST("vendor/update-price")
    fun updatePrice1(
        @Header("Authorization") authorization: String,
        @Query("food_id") id: String,
        @Query("price") price: String,
    ): Call<ModelUpdatePrice>

}