package com.amtech.vendorservices.V.Order.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.widget.addTextChangedListener
import com.amtech.vendorservices.R
import com.amtech.vendorservices.V.Helper.AppProgressBar
import com.amtech.vendorservices.V.Helper.myToast
import com.amtech.vendorservices.V.Order.Adapter.AdapterCompleteOrder
import com.amtech.vendorservices.V.Order.Adapter.AdapterSerRequestList
import com.amtech.vendorservices.V.Order.Model.Data
import com.amtech.vendorservices.V.Order.Model.DataX
import com.amtech.vendorservices.V.Order.Model.ModelComplete
import com.amtech.vendorservices.V.retrofit.ApiClient
import com.amtech.vendorservices.databinding.ActivityDeliveredOrdersBinding
import com.amtech.vendorservices.databinding.ActivityServicesRequestListBinding
import com.example.hhfoundation.sharedpreferences.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DeliveredOrders : AppCompatActivity() {
    private val binding by lazy {
        ActivityDeliveredOrdersBinding.inflate(layoutInflater)
    }
    private val context=this@DeliveredOrders
    private lateinit var sessionManager: SessionManager
    private lateinit var mainData: ArrayList<DataX>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        sessionManager= SessionManager(context)
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
                            myToast(context, "Something went wrong")
                            AppProgressBar.hideLoaderDialog()

                        }
                        else if  (response.code() == 500) {
                            myToast(context, "Server Error")
                            AppProgressBar.hideLoaderDialog()

                        } else if (response.body()!!.data.isEmpty()) {
                            myToast(context, "No Data Found")
                            AppProgressBar.hideLoaderDialog()

                        } else {
                            mainData = response.body()!!.data
                            setRecyclerViewAdapter(mainData)
                            AppProgressBar.hideLoaderDialog()
                        }
                    } catch (e: Exception) {
                        myToast(context, "Something went wrong")
                        e.printStackTrace()
                        AppProgressBar.hideLoaderDialog()

                    }
                }


                override fun onFailure(call: Call<ModelComplete>, t: Throwable) {
                    myToast(context, "Something went wrong")
                    AppProgressBar.hideLoaderDialog()
//                    count++
//                    if (count <= 3) {
//                        Log.e("count", count.toString())
//                        apiCallAppointmentList(status)
//                    } else {
//                        myToast(this@ConsaltationRequest, t.message.toString())
//                        AppProgressBar.hideLoaderDialog()
//
//                    }
//                    AppProgressBar.hideLoaderDialog()
                }

            })

    }
    private fun setRecyclerViewAdapter(data: ArrayList<DataX>) {
        binding.recyclerView.apply {
            adapter = AdapterCompleteOrder(context, data)
            AppProgressBar.hideLoaderDialog()

        }
    }
}