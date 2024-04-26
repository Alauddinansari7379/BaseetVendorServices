package com.amtech.vendorservices.V.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.amtech.vendorservices.databinding.ActivityCustomerReviewsBinding

class CustomerReviews : AppCompatActivity() {
    private val binding by lazy {
        ActivityCustomerReviewsBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        with(binding) {
            imgBack.setOnClickListener {
                onBackPressed()
            }
        }
    }
}