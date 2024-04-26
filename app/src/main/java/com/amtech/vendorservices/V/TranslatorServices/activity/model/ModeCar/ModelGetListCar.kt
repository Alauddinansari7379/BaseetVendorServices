package com.amtech.vendorservices.V.TranslatorServices.activity.model.ModeCar

data class ModelGetListCar(
    val limit: Int,
    val offset: Int,
    val products: ArrayList<Product>,
    val total_size: Int
)