package com.amtech.vendorservices.V.Order.Model.ModelOrderDetail

data class Detail(
    val add_ons: String,
    val created_at: String,
    val discount_on_food: Any,
    val discount_type: String,
    val food_details: String,
    val food_id: Double,
    val id: Int,
    val item_campaign_id: Any,
    val order_id: Double,
    val price: Double,
    val quantity: Double,
    val tax_amount: Double,
    val total_add_on_price: Double,
    val updated_at: String,
    val variant: String,
    val variation: String
)