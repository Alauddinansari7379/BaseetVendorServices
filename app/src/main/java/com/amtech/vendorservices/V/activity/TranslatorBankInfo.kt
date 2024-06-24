package com.amtech.vendorservices.V.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.amtech.vendorservices.R
import com.amtech.vendorservices.V.Dashboard.Dashboard
import com.amtech.vendorservices.databinding.ActivityTranslaterBankInfoBinding
import com.amtech.vendorservices.V.sharedpreferences.SessionManager

class TranslatorBankInfo : AppCompatActivity() {
    private val binding by lazy{
        ActivityTranslaterBankInfoBinding.inflate(layoutInflater)
    }
    private val context=this@TranslatorBankInfo
    lateinit var sessionManager: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        sessionManager= SessionManager(context)
        Dashboard().languageSetting(this, sessionManager.selectedLanguage.toString())

        if (Dashboard.refreshLanNew) {
            Dashboard.refreshLanNew = false
            refresh()
        }
        if (sessionManager.selectedLanguage == "en") {
            binding.imgLan.background = ContextCompat.getDrawable(context, R.drawable.arabic_text)
        } else {
            binding.imgLan.background = ContextCompat.getDrawable(context, R.drawable.english_text)
        }

        binding.imgLan.setOnClickListener {
            if (sessionManager.selectedLanguage == "en") {
                sessionManager.selectedLanguage = "ar"
                Dashboard().languageSetting(context, sessionManager.selectedLanguage.toString())
                overridePendingTransition(0, 0)
                finish()
                startActivity(intent)
                overridePendingTransition(0, 0)
            } else {
                sessionManager.selectedLanguage = "en"
                Dashboard().languageSetting(context, sessionManager.selectedLanguage.toString())
                overridePendingTransition(0, 0)
                finish()
                startActivity(intent)
                overridePendingTransition(0, 0)
            }
        }
        with(binding){
            imgBack.setOnClickListener {
                onBackPressed()
            }
            when (sessionManager.usertype) {
                "car" -> {
                    binding.tvTitle.text =  resources.getString(R.string.Car_Rental_Bank_Info)
                  }
                "home" -> {
                    binding.tvTitle.text =  resources.getString(R.string.Home_Rental_Bank_Info)

                }else->{

            }
            }
        }
    }
    private fun refresh() {
        overridePendingTransition(0, 0)
        finish()
        startActivity(intent)
        overridePendingTransition(0, 0)
    }
    override fun onDestroy() {
        super.onDestroy()
        Dashboard.refreshLanNew=true
    }
}