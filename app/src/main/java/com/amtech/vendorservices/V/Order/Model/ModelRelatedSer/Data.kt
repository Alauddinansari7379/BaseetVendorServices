package com.amtech.vendorservices.V.Order.Model.ModelRelatedSer

data class Data(
    val dates: String,
    val description: String,
    val drone: String,
    val foodid: Int,
    val image: String,
    val name: String,
    val port_video: String,
    val price: String,
    val ser_hour: Int,
    val trperson: String,
    val days: String?,
    val driv_type: String,
    val car_type: String,
    val home_type: String,
    val tr_from: String,
    val tr_to: String,
    val amenities: Any,
    val car_model: String,
    val servicesdates: String,
    )