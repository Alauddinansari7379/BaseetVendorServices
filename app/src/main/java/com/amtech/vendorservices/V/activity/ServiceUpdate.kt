package com.amtech.vendorservices.V.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.amtech.vendorservices.R
import com.amtech.vendorservices.V.Dashboard.Dashboard
import com.amtech.vendorservices.databinding.ActivityServiceUpdateBinding
import com.amtech.vendorservices.V.sharedpreferences.SessionManager

class ServiceUpdate : AppCompatActivity() {
    private val binding by lazy {
        ActivityServiceUpdateBinding.inflate(layoutInflater)
    }
    private val context=this@ServiceUpdate
    lateinit var sessionManager: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        with(binding) {
            imgBack.setOnClickListener {
                onBackPressed()
            }
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
           /* when (sessionManager.usertype) {
                "car" -> {
                    binding.tvTitle.text = "Car Service Update"
                    binding.tvImage.text = "Car Image"
                    binding.tvAvlabile.text = "Car Rental Available"

                }

                "home" -> {
                    binding.tvTitle.text = "Home Service Update"
                    binding.tvImage.text = "Home Image"
                    binding.tvAvlabile.text = "Home Rental Available"

                }else->{
                binding.tvAvlabile.text = "Translotor Service Available"

            }
            }

            radioOnCall.setOnCheckedChangeListener { _, _ ->
                if (radioOnCall.isChecked) {
                    radioInPerson.isChecked = false
                    radioDoc.isChecked = false

//                    follow=""
//                    layoutTrea.visibility = View.VISIBLE
//                    layoutNotTreated.visibility = View.GONE
//                    layoutNotTreated.visibility = View.GONE

                }

            }


            radioInPerson.setOnCheckedChangeListener { _, _ ->
                if (radioInPerson.isChecked) {
                    radioOnCall.isChecked = false
                    radioDoc.isChecked = false
//                    follow= "Not Following the Treatment"
//                    layoutNotTreated.visibility = View.VISIBLE
//                    layoutTrea.visibility = View.GONE
//                    layoutNotTreated.visibility = View.VISIBLE

                }

                radioDoc.setOnCheckedChangeListener { _, _ ->
                    if (radioDoc.isChecked) {
                        radioOnCall.isChecked = false
                        radioInPerson.isChecked = false
//                    follow= "Not Following the Treatment"
//                    layoutNotTreated.visibility = View.VISIBLE
//                    layoutTrea.visibility = View.GONE
//                    layoutNotTreated.visibility = View.VISIBLE

                    }
                }

            }
        }*/
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