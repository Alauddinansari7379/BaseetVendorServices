package com.amtech.vendorservices.V.Order.Model.ModelOrderDet

data class ModelOrderDet(
    val accepted: Any,
    val adjusment: String,
    val callback: Any,
    val cancel_note: Any,
    val canceled: Any,
    val checked: Int,
    val confirmed: String,
    val coupon_code: Any,
    val coupon_discount_amount: Int,
    val coupon_discount_title: String,
    val created_at: String,
    val customer: Customer,
    val `data`: List<Data>,
    val delivered: Any,
    val delivery_address: String,
    val delivery_address_id: Any,
    val delivery_charge: Int,
    val delivery_man_id: Any,
    val details: List<Detail>,
    val edited: Int,
    val failed: Any,
    val food_type: String,
    val handover: Any,
    val help_note: Any,
    val id: Int,
    val order_amount: Int,
    val order_note: Any,
    val order_payment: String,
    val order_status: String,
    val order_type: String,
    val original_delivery_charge: Int,
    val otp: String,
    val pay_type: String,
    val payment_method: String,
    val payment_status: String,
    val pending: String,
    val picked_up: Any,
    val processing: Any,
    val refund_note: Any,
    val refund_requested: Any,
    val refunded: Any,
    val restaurant_discount_amount: Int,
    val restaurant_id: Int,
    val schedule_at: String,
    val scheduled: Int,
    val total_tax_amount: Int,
    val transaction_reference: Any,
    val updated_at: String,
    val user_id: Int,
    val zone_id: Int
)