package com.amtech.vendorservices.V.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.amtech.vendorservices.R
import com.amtech.vendorservices.V.Dashboard.Dashboard
import com.amtech.vendorservices.databinding.ActivityEditTranslaterInformationBinding
 import com.amtech.vendorservices.V.sharedpreferences.SessionManager

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


        Dashboard().languageSetting(context, sessionManager.selectedLanguage.toString())

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



        when (sessionManager.usertype) {
            "car" -> {
                binding.tvTitle.text = resources.getString(R.string.Edit_Car_Rental_Information)
                 binding.tvName.text =  resources.getString(R.string.Car_Rental_Name)
             }
            "home" -> {
                binding.tvTitle.text =  resources.getString(R.string.Edit_Home_Rental_Information)
                 binding.tvName.text =  resources.getString(R.string.Home_Rental_Name)
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