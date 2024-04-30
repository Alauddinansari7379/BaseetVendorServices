package com.amtech.vendorservices.V.Order.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.amtech.vendorservices.V.Helper.AppProgressBar
import com.amtech.vendorservices.V.Helper.myToast
import com.amtech.vendorservices.V.Order.Adapter.AdapterAllOrder
import com.amtech.vendorservices.V.Order.Model.MAllOrder.ModelAllOrder
import com.amtech.vendorservices.V.Order.Model.MAllOrder.ModelAllOrderItem
import com.amtech.vendorservices.V.Order.activity.OrderDetails.Companion.back
import com.amtech.vendorservices.V.retrofit.ApiClient
import com.amtech.vendorservices.databinding.ActivityAllOrdersBinding
import com.example.hhfoundation.sharedpreferences.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllOrders : AppCompatActivity() {
    private val binding by lazy {
        ActivityAllOrdersBinding.inflate(layoutInflater)
    }
    var count=0
    val context=this@AllOrders
    private lateinit var mainData: ArrayList<ModelAllOrderItem>

    lateinit var sessionManager:SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        sessionManager= SessionManager(context)
        with(binding){
            imgBack.setOnClickListener {
                onBackPressed()
            }
            apiCallAllOrder()

            edtSearch.addTextChangedListener { str ->
                setRecyclerViewAdapter(mainData.filter {
                    it.id != null && it.id.toString()!!.contains(str.toString(), ignoreCase = true)
                } as ArrayList<ModelAllOrderItem>)
            }
        }

    }
    private fun apiCallAllOrder() {
        AppProgressBar.showLoaderDialog(context)
        AppProgressBar.showLoaderDialog(context)
        ApiClient.apiService.allOrders(
            sessionManager.idToken.toString(),
        )
            .enqueue(object : Callback<ModelAllOrder> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelAllOrder>, response: Response<ModelAllOrder>
                ) {
                    try {
                        if (response.code() == 404) {
                            myToast(context, "Something went wrong")
                            AppProgressBar.hideLoaderDialog()

                        }
                        else if  (response.code() == 500) {
                            myToast(context, "Server Error")
                            AppProgressBar.hideLoaderDialog()

                        } else if (response.body()!!.isEmpty()) {
                            myToast(context, "No Data Found")
                            AppProgressBar.hideLoaderDialog()

                        } else {
                            mainData = response.body()!!
                            setRecyclerViewAdapter(mainData)
                            AppProgressBar.hideLoaderDialog()
                        }
                    } catch (e: Exception) {
                        myToast(context, "Something went wrong")
                        e.printStackTrace()
                        AppProgressBar.hideLoaderDialog()

                    }
                }


                override fun onFailure(call: Call<ModelAllOrder>, t: Throwable) {
                    myToast(context, t.message.toString())
                    AppProgressBar.hideLoaderDialog()
                    count++
                    if (count <= 3) {
                        Log.e("count", count.toString())
                        apiCallAllOrder()
                    } else {
                        myToast(context, t.message.toString())
                        AppProgressBar.hideLoaderDialog()

                    }
                    AppProgressBar.hideLoaderDialog()
                }

            })

    }
    private fun setRecyclerViewAdapter(data: ArrayList<ModelAllOrderItem>) {
        binding.recyclerView.apply {
            adapter = AdapterAllOrder(context, data)
            AppProgressBar.hideLoaderDialog()

        }
    }

    override fun onResume() {
        super.onResume()
        if (back){
            back=false
            apiCallAllOrder()
        }
    }
}