package com.amtech.vendorservices.V.TranslatorServices.activity.model

data class ModelServiceList(
    val limit: Int,
    val offset: Int,
    val products: ArrayList<Product>,
    val total_size: Int,
    val message: String,
    val msg: String
)