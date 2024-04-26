package com.amtech.vendorservices.V.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.amtech.vendorservices.databinding.ActivityTranslatorWalletBinding
import com.example.hhfoundation.sharedpreferences.SessionManager

class TranslatorWallet : AppCompatActivity() {
    private val binding by lazy {
        ActivityTranslatorWalletBinding.inflate(layoutInflater)
    }
    private val context=this@TranslatorWallet
    lateinit var sessionManager:SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        sessionManager= SessionManager(context)
        with(binding) {
            imgBack.setOnClickListener {
                onBackPressed()
            }
            when (sessionManager.usertype) {
                "car" -> {
                    binding.tvTitle.text = "Car Rental Wallet"
                }
                "home" -> {
                    binding.tvTitle.text = "Home Rental Wallet"

                }else->{

            }
            }
        }
    }
}