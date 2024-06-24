package com.amtech.vendorservices.V.Order.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.amtech.vendorservices.R
import com.amtech.vendorservices.V.Dashboard.Dashboard
import com.amtech.vendorservices.V.Helper.AppProgressBar
import com.amtech.vendorservices.V.Helper.myToast
import com.amtech.vendorservices.V.Order.Adapter.AdapterCompleteOrder
import com.amtech.vendorservices.V.Order.Model.DataX
import com.amtech.vendorservices.V.Order.Model.ModelComplete
import com.amtech.vendorservices.V.retrofit.ApiClient
import com.amtech.vendorservices.databinding.ActivityDeliveredOrdersBinding
import com.amtech.vendorservices.V.sharedpreferences.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DeliveredOrders : AppCompatActivity() {
    private val binding by lazy {
        ActivityDeliveredOrdersBinding.inflate(layoutInflater)
    }
    var count=0
    private val context=this@DeliveredOrders
    private lateinit var sessionManager: SessionManager
    private lateinit var mainData: ArrayList<DataX>
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
            apiCallCompleteOrder()

            edtSearch.addTextChangedListener { str ->
                setRecyclerViewAdapter(mainData.filter {
                    it.id != null && it.id.toString()!!.contains(str.toString(), ignoreCase = true)
                } as ArrayList<DataX>)
            }
        }

    }
    private fun apiCallCompleteOrder() {
        AppProgressBar.showLoaderDialog(context)
        AppProgressBar.showLoaderDialog(context)
        ApiClient.apiService.completeOrder(
            sessionManager.idToken.toString(),
            "12","1",
            "all"

            )
            .enqueue(object : Callback<ModelComplete> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelComplete>, response: Response<ModelComplete>
                ) {
                    try {
                        if (response.code() == 404) {
                            myToast(context, resources.getString(R.string.Something_went_wrong))
                            AppProgressBar.hideLoaderDialog()

                        }
                        else if  (response.code() == 500) {
                            myToast(context, resources.getString(R.string.Server_Error))
                            AppProgressBar.hideLoaderDialog()

                        } else if (response.body()!!.data.isEmpty()) {
                            myToast(context, resources.getString(R.string.No_Data_Found))
                            AppProgressBar.hideLoaderDialog()

                        } else {
                            mainData = response.body()!!.data
                            setRecyclerViewAdapter(mainData)
                            AppProgressBar.hideLoaderDialog()
                        }
                    } catch (e: Exception) {
                        myToast(context, resources.getString(R.string.Something_went_wrong))
                        e.printStackTrace()
                        AppProgressBar.hideLoaderDialog()

                    }
                }


                override fun onFailure(call: Call<ModelComplete>, t: Throwable) {
                     AppProgressBar.hideLoaderDialog()
                    count++
                    if (count <= 3) {
                        Log.e("count", count.toString())
                        apiCallCompleteOrder()
                    } else {
                        myToast(context, t.message.toString())
                        AppProgressBar.hideLoaderDialog()

                    }
                    AppProgressBar.hideLoaderDialog()
                }

            })

    }
    private fun setRecyclerViewAdapter(data: ArrayList<DataX>) {
        binding.recyclerView.apply {
            adapter = AdapterCompleteOrder(context, data)
            AppProgressBar.hideLoaderDialog()

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