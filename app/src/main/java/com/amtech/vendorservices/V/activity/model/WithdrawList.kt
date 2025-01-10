package com.amtech.vendorservices.V.activity.model

data class WithdrawList(
    val limit: Int,
    val offset: Int,
    val total_size: Int,
    val withdraw_requests: List<WithdrawRequest>
)