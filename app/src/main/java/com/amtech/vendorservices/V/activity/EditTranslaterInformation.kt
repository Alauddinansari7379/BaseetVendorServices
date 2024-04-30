package com.amtech.vendorservices.V.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.amtech.vendorservices.R
import com.amtech.vendorservices.databinding.ActivityEditTranslaterInformationBinding
 import com.example.hhfoundation.sharedpreferences.SessionManager

class EditTranslaterInformation : AppCompatActivity() {
    private  val binding by lazy {
        ActivityEditTranslaterInformationBinding.inflate(layoutInflater)
    }
    val context=this@EditTranslaterInformation
    lateinit var sessionManager: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        sessionManager= SessionManager(context)
        when (sessionManager.usertype) {
            "car" -> {
                binding.tvTitle.text = "Edit Car Rental Information"
                binding.tvName.text = "Car Rental Name"
            }
            "home" -> {
                binding.tvTitle.text = "Edit Home Rental Information"
                binding.tvName.text = "Home Rental Name"
            }
            else -> {

            }
        }
        with(binding){
            imgBack.setOnClickListener {
                onBackPressed()
            }
        }
    }
}