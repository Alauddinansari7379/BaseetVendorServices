package com.amtech.vendorservices.V.Order.Model.MAllOrder

data class DeliveryAddress(
    val address: String,
    val address_type: String,
    val contact_person_name: String,
    val contact_person_number: String,
    val latitude: String,
    val longitude: String
)