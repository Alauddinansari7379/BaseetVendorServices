package com.amtech.vendorservices.V.Order.Model.ModelOrderDet

data class Data(
    val accepted: Any,
    val add_ons: List<Any>,
    val confirmed: String,
    val created_at: String,
    val delivered: Any,
    val discount_on_food: Any,
    val discount_type: String,
    val food_details: FoodDetails,
    val food_id: Int,
    val handover: Any,
    val id: Int,
    val item_campaign_id: Any,
    val order_id: Int,
    val pending: String,
    val picked_up: Any,
    val price: Int,
    val processing: Any,
    val quantity: Int,
    val tax_amount: Int,
    val total_add_on_price: Int,
    val updated_at: String,
    val variant: String,
    val variation: List<Any>
)