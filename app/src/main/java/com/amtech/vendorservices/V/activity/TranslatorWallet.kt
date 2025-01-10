package com.amtech.vendorservices.V.activity

import android.graphics.ColorSpace.Model
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.amtech.vendorservices.R
import com.amtech.vendorservices.V.Dashboard.Dashboard
import com.amtech.vendorservices.V.Helper.AppProgressBar
import com.amtech.vendorservices.V.Helper.myToast
import com.amtech.vendorservices.V.Order.Model.ModeUpdatePrice.ModelUpdatePrice
import com.amtech.vendorservices.V.activity.model.WithdrawList
import com.amtech.vendorservices.V.activity.model.WithdrawRequest
import com.amtech.vendorservices.V.activity.model.withdrawrequest.ModelWithdrawRequest
import com.amtech.vendorservices.V.adapter.WithdrawListAdapter
import com.amtech.vendorservices.V.retrofit.ApiClient
import com.amtech.vendorservices.databinding.ActivityTranslatorWalletBinding
import com.amtech.vendorservices.V.sharedpreferences.SessionManager
import com.amtech.vendorservices.databinding.WithdrawPopupBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TranslatorWallet : AppCompatActivity() {
    private var withdrawList: List<WithdrawRequest> = ArrayList()
    private val binding by lazy {
        ActivityTranslatorWalletBinding.inflate(layoutInflater)
    }
    var count = 0
    var count1 = 0
    private val context=this@TranslatorWallet
    lateinit var sessionManager: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        sessionManager= SessionManager(context)
        Dashboard().languageSetting(this, sessionManager.selectedLanguage.toString())
        apiCallGetWithdrawList()
        if (Dashboard.refreshLanNew) {
            Dashboard.refreshLanNew = false
            refresh()
        }
        if (sessionManager.selectedLanguage == "en") {
            binding.imgLan.background = ContextCompat.getDrawable(context, R.drawable.arabic_text)
        } else {
            binding.imgLan.background = ContextCompat.getDrawable(context, R.drawable.english_text)
        }
        binding.tvTotalEarning.text = Dashboard.earningsSum.toString()
        binding.llWithdraw.setOnClickListener{
            showPopupDialog()
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
        with(binding) {
            imgBack.setOnClickListener {
                onBackPressed()
            }
            when (sessionManager.usertype) {
                "car" -> {
                    binding.tvTitle.text = resources.getString(R.string.Car_Rental_Wallet)
                 }
                "home" -> {
                    binding.tvTitle.text = resources.getString(R.string.Home_Rental_Wallet)

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
    fun apiCallGetWithdrawList() {
        AppProgressBar.showLoaderDialog(context)
        ApiClient.apiService.getWithdrawList(sessionManager.idToken.toString())
            .enqueue(object : Callback<WithdrawList> {

                override fun onResponse(call: Call<WithdrawList>, response: Response<WithdrawList>) {
                    try {
                        if (response.code() == 404) {
                            myToast(context, resources.getString(R.string.Something_went_wrong))
                            AppProgressBar.hideLoaderDialog()
                        } else if (response.code() == 500) {
                            myToast(context, resources.getString(R.string.Server_Error))
                            AppProgressBar.hideLoaderDialog()
                        } else {
                            if (response.isSuccessful)
                            {
                                AppProgressBar.hideLoaderDialog()
                                withdrawList = response.body()!!.withdraw_requests
                                binding.rvWithdrawList.apply {
                                    adapter = WithdrawListAdapter(withdrawList)
                                }

                            }
                        }
                    } catch (e: Exception) {
                        myToast(context, resources.getString(R.string.Something_went_wrong))
                        e.printStackTrace()
                    }
                }

                override fun onFailure(call: Call<WithdrawList>, t: Throwable) {
                    myToast(context, t.message.toString())
                    AppProgressBar.hideLoaderDialog()
                    count++
                    if (count <= 3) {
                        apiCallGetWithdrawList()
                    } else {
                        myToast(context, t.message.toString())
                    }
                    AppProgressBar.hideLoaderDialog()
                }
            })
    }
    private fun apiCallWithdrawRequest(amount : String) {
        AppProgressBar.showLoaderDialog(context)
        ApiClient.apiService.withdrawRequest(sessionManager.idToken.toString(),amount)
            .enqueue(object : Callback<ModelWithdrawRequest> {

                override fun onResponse(call: Call<ModelWithdrawRequest>, response: Response<ModelWithdrawRequest>) {
                    try {
                        if (response.code() == 404) {
                            myToast(context, resources.getString(R.string.Something_went_wrong))
                            AppProgressBar.hideLoaderDialog()
                        } else if (response.code() == 500) {
                            myToast(context, resources.getString(R.string.Server_Error))
                            AppProgressBar.hideLoaderDialog()
                        } else {
                            if (response.isSuccessful)
                            {
                                count1 = 0
                                AppProgressBar.hideLoaderDialog()
                                myToast(context,response.body()!!.message)
                            }else{
                                myToast(context, resources.getString(R.string.Something_went_wrong))
                                AppProgressBar.hideLoaderDialog()
                            }
                        }
                    } catch (e: Exception) {
                        myToast(context, resources.getString(R.string.Something_went_wrong))
                        e.printStackTrace()
                    }
                }

                override fun onFailure(call: Call<ModelWithdrawRequest>, t: Throwable) {
                    myToast(context, t.message.toString())
                    AppProgressBar.hideLoaderDialog()
                    count1++
                    if (count1 <= 3) {
                        apiCallWithdrawRequest(amount)
                    } else {
                        myToast(context, t.message.toString())
                    }
                    AppProgressBar.hideLoaderDialog()
                }
            })
    }
    private fun showPopupDialog() {
        // Inflate the custom layout using ViewBinding
        val dialogBinding = WithdrawPopupBinding.inflate(layoutInflater)

        // Build the AlertDialog
        val dialog = AlertDialog.Builder(this)
            .setView(dialogBinding.root)
            .create()

        // Set the action for the Submit button
        dialogBinding.btnSubmit.setOnClickListener {
            val inputText = dialogBinding.edtPrice.text.toString()
            if (inputText.isNotEmpty() && inputText.toDouble()>0 && inputText.toDouble()<= Dashboard.earningsSum) {
                apiCallWithdrawRequest(inputText)
                dialog.dismiss()
            } else if(inputText.isEmpty()) {
                myToast(context,"Please enter the amount.")
            }else{
                myToast(context,"The withdrawal amount cannot exceed the earned amount.")
            }
        }

        dialogBinding.imgCloseDil.setOnClickListener {
            dialog.dismiss()
        }

        // Show the dialog
        dialog.show()
    }

}