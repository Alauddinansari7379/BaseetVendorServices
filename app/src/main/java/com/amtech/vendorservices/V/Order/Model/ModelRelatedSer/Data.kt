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
    val tr_from: String,
    val tr_to: String
)