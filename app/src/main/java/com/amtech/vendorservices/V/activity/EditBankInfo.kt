package com.amtech.vendorservices.V.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.amtech.vendorservices.R
import com.amtech.vendorservices.databinding.ActivityEditBankInfoBinding
import com.example.hhfoundation.sharedpreferences.SessionManager

class EditBankInfo : AppCompatActivity() {
    val binding by lazy {
        ActivityEditBankInfoBinding.inflate(layoutInflater)
    }
    val context=this@EditBankInfo
    lateinit var sessionManager: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

    }
}