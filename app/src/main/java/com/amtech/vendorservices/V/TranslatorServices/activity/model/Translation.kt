package com.amtech.vendorservices.V.TranslatorServices.activity.model

data class Translation(
    val created_at: Any,
    val id: Int,
    val key: String,
    val locale: String,
    val translationable_id: Int,
    val translationable_type: String,
    val updated_at: Any,
    val value: String
)