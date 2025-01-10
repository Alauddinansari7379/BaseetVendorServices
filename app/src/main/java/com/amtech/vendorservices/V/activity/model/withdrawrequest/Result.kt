package com.amtech.vendorservices.V.activity.model.withdrawrequest

data class Result(
    val collected_cash: String,
    val commission: Double,
    val created_at: String,
    val id: Int,
    val pending_withdraw: Int,
    val total_earning: String,
    val total_withdrawn: String,
    val updated_at: String,
    val vendor_id: String
)