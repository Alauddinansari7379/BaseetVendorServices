package com.amtech.vendorservices.V.activity.model

data class WithdrawRequest(
    val balance: Double,
    val bank_name: String,
    val collected_cash: String,
    val commission: String,
    val id: Int,
    val pending_withdraw: String,
    val requested_at: String,
    val total_earning: String,
    val total_withdrawn: String,
    val updated_at: String,
    val vendor_id: String
)