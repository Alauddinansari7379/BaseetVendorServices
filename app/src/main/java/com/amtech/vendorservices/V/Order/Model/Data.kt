package com.amtech.vendorservices.V.Order.Model

data class Data(
    val accept_by: String,
    val child: Any,
    val country: String,
    val created_at: String,
    val customer_id: String,
    val description: String,
    val details: Any,
    val driv_type: String,
    val end_time: String,
    val id: Int,
    val name: String?,
    val price: String?,
    val res_id: String,
    val serv_date: String,
    val serv_hour: String,
    val serv_id: String,
    val start_time: String,
    val status: Int,
    val tr_from: String?,
    val tr_to: String?,
    val trperson: String?,
    val type: String?,
    val updated_at: String,
    val ven_id: String,
    val whchserv: String
)