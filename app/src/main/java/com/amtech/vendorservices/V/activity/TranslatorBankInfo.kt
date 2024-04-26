package com.amtech.vendorservices.V.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.amtech.vendorservices.R
import com.amtech.vendorservices.databinding.ActivityTranslaterBankInfoBinding
import com.example.hhfoundation.sharedpreferences.SessionManager

class TranslatorBankInfo : AppCompatActivity() {
    private val binding by lazy{
        ActivityTranslaterBankInfoBinding.inflate(layoutInflater)
    }
    private val contex=this@TranslatorBankInfo
    lateinit var sessionManager:SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        sessionManager= SessionManager(contex)
        with(binding){
            imgBack.setOnClickListener {
                onBackPressed()
            }
            when (sessionManager.usertype) {
                "car" -> {
                    binding.tvTitle.text = "Car Rental Bank Info"
                 }
                "home" -> {
                    binding.tvTitle.text = "Home Rental Bank Info"

                }else->{

            }
            }
        }
    }
}