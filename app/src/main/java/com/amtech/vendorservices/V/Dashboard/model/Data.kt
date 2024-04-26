package com.amtech.vendorservices.V.Dashboard.model

data class Data(
    val all: Int,
    val confirmed: Int,
    val cooking: Int,
    val delivered: Int,
    val food_on_the_way: Int,
    val most_rated_foods: List<MostRatedFood>,
    val ready_for_delivery: Int,
    val refunded: Int,
    val rejected: Int,
    val scheduled: Int,
    val top_sell: List<TopSell>
)