package com.amtech.vendorservices.V.Order.Model.ModelOrderDet

data class AddOnX(
    val created_at: String,
    val id: Int,
    val image: Any,
    val name: String,
    val price: Int,
    val restaurant_id: Int,
    val status: Int,
    val updated_at: String
)