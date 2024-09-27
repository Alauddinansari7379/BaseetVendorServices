package com.amtech.vendorservices.V.Order.Model.ModelOrderDet

data class Detail(
    val accepted: Any,
    val add_ons: List<AddOn>,
    val confirmed: Any,
    val created_at: String,
    val delivered: Any,
    val discount_on_food: Double?,
    val discount_type: String,
    val food_details: FoodDetails,
    val food_id: Double?,
    val handover: Any,
    val id: Double?,
    val item_campaign_id: Any,
    val order_id: Double?,
    val pending: String,
    val picked_up: Any,
    val price: Double?,
    val processing: Any,
    val quantity: Double?,
    val tax_amount: Double?,
    val total_add_on_price: Double?,
    val updated_at: String,
    val variant: String,
    val variation: List<Any>
)