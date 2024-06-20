package com.amtech.vendorservices.V.Order.Model.ModelOrderDetail

data class Detail(
    val add_ons: String,
    val created_at: String,
    val discount_on_food: Any,
    val discount_type: String,
    val food_details: String,
    val food_id: Int,
    val id: Int,
    val item_campaign_id: Any,
    val order_id: Int,
    val price: Int,
    val quantity: Int,
    val tax_amount: Int,
    val total_add_on_price: Int,
    val updated_at: String,
    val variant: String,
    val variation: String
)