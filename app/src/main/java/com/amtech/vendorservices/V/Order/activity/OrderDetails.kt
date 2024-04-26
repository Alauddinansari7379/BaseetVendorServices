package com.amtech.vendorservices.V.Order.activity

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.amtech.vendorservices.R
import com.amtech.vendorservices.V.Helper.AppProgressBar
import com.amtech.vendorservices.V.Helper.myToast
import com.amtech.vendorservices.V.Order.Model.ModelOrderDet.ModelOrderDet
import com.amtech.vendorservices.V.Order.Model.ModelSendSer.ModelSendSer
import com.amtech.vendorservices.V.retrofit.ApiClient
import com.amtech.vendorservices.databinding.ActivityOrderDetailsBinding
import com.example.hhfoundation.sharedpreferences.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

class OrderDetails : AppCompatActivity() {
    private val binding by lazy {
        ActivityOrderDetailsBinding.inflate(layoutInflater)
    }
    var orderId = ""
    var orderStatus = ""
    var date = ""
    var currentdate = ""
    var count = 0
    var dateNew: LocalDateTime? =null
    var currentDateNew: LocalDateTime? =null
    private val context = this@OrderDetails
    lateinit var sessionManager: SessionManager
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        sessionManager = SessionManager(context)
        orderId = intent.getStringExtra("orderId").toString()
        orderStatus = intent.getStringExtra("orderStatus").toString()
        when (orderStatus) {
            "confirmed" -> {
                binding.btnDeleverd.visibility = View.VISIBLE
            }

            "pending" -> {
                binding.btnConfirm.visibility = View.VISIBLE
                binding.btnCancel.visibility = View.VISIBLE
            }

            "precessing" -> {
                binding.btnDeleverd.visibility = View.VISIBLE
            }else->{

            }
        }
        apiCallOrderDet(orderId)
        binding.imgBack.setOnClickListener {
            onBackPressed()
        }

        binding.btnConfirm.setOnClickListener {
            apiCallStatuesChange(orderId, "confirmed")
        }

        binding.btnCancel.setOnClickListener {
            apiCallStatuesChange(orderId, "canceled")

        }

        binding.btnDeleverd.setOnClickListener {
                apiCallStatuesChange(orderId, "delivered")

        }
    }

    private fun apiCallOrderDet(
        orderId: String,
    ) {
        AppProgressBar.showLoaderDialog(context)
        ApiClient.apiService.orderDetails(
            sessionManager.idToken.toString(),
            orderId
        )
            .enqueue(object : Callback<ModelOrderDet> {
                @RequiresApi(Build.VERSION_CODES.O)
                @SuppressLint("LogNotTimber", "SetTextI18n", "ResourceType")
                override fun onResponse(
                    call: Call<ModelOrderDet>, response: Response<ModelOrderDet>
                ) {
                    try {
                        if (response.code() == 404) {
                            myToast(context, "Something went wrong")

                        } else if (response.code() == 500) {
                            myToast(context, "Server Error")
                            AppProgressBar.hideLoaderDialog()

                        } /*else if (response.body()!!.data.isEmpty()) {
                            myToast(context, "No Data Found")
                            binding.layoutOrderDet.visibility=View.GONE
                            binding.layoutNotFoundN.visibility=View.VISIBLE
                            AppProgressBar.hideLoaderDialog()

                        }*/ else {
                            binding.orderId.text = response.body()!!.id.toString()
                            binding.date.text = pmFormate(response.body()!!.created_at)
                            date = response.body()!!.created_at
                            binding.tvPaymentStatus.text = response.body()!!.payment_status
                            binding.tvOrderStatus.text = response.body()!!.order_status
                            orderStatus = response.body()!!.order_status
                            for (i in response.body()!!.data) {
                                binding.name.text = i.food_details.name
                                binding.tvPrice.text = i.food_details.price.toString() + " $"
                                binding.tvPrice1.text = i.food_details.price.toString() + " $"
                                binding.tvSubtotal.text = i.food_details.price.toString() + " $"
                                binding.tvTotal.text = i.food_details.price.toString() + " $"
                            }

                            binding.tvCusName.text =
                                response.body()!!.customer.f_name + " " + response.body()!!.customer.l_name
                            binding.tvEmail.text = response.body()!!.customer.email
                            binding.tvNumber.text = response.body()!!.customer.phone
                            binding.tvDelName.text =
                                response.body()!!.customer.f_name + " " + response.body()!!.customer.l_name
                            binding.tvDelNumber.text = response.body()!!.customer.phone

                            AppProgressBar.hideLoaderDialog()
                            when (orderStatus) {
                                "confirmed" -> {
                                    binding.btnDeleverd.visibility = View.VISIBLE
                                    binding.btnConfirm.visibility = View.GONE
                                    binding.btnCancel.visibility = View.GONE
                                }

                                "canceled" -> {
                                    binding.btnDeleverd.visibility = View.GONE
                                    binding.btnConfirm.visibility = View.GONE
                                    binding.btnCancel.visibility = View.GONE
                                    binding.btnDeleverdDesable.visibility=View.GONE
                                    binding.btnDeleverd.visibility=View.GONE
                                }

                                "pending" -> {
                                    binding.btnConfirm.visibility = View.VISIBLE
                                    binding.btnCancel.visibility = View.VISIBLE
                                    binding.btnDeleverdDesable.visibility=View.GONE

                                }

                                "precessing" -> {
                                    binding.btnDeleverd.visibility = View.VISIBLE
                                }

                                "delivered" -> {
                                    binding.btnDeleverd.visibility = View.GONE
                                    binding.btnConfirm.visibility = View.GONE
                                    binding.btnCancel.visibility = View.GONE
                                    binding.btnDeleverdDesable.visibility=View.GONE

                                }else->{

                            }

                            }
                            val currentDate: String = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

                            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

                             dateNew = LocalDateTime.parse(date, formatter)
                             currentDateNew = LocalDateTime.parse(currentDate, formatter)
                            if (currentDateNew!! <= dateNew) {
                                binding.btnDeleverdDesable.visibility=View.VISIBLE
                                binding.btnDeleverd.visibility=View.GONE

                            }else{
                                if (orderStatus=="delivered" ||orderStatus=="canceled" ){
                                    binding.btnDeleverd.visibility=View.GONE
                                }else{
                                    if (orderStatus=="pending"){
                                        binding.btnDeleverd.visibility=View.GONE
                                    }else
                                    binding.btnDeleverd.visibility=View.VISIBLE

                                }
                            }
                        }
                    } catch (e: Exception) {
                        myToast(context, "Something went wrong")
                        e.printStackTrace()
                        AppProgressBar.hideLoaderDialog()

                    }
                }


                override fun onFailure(call: Call<ModelOrderDet>, t: Throwable) {
                    myToast(context, t.message.toString())
                    AppProgressBar.hideLoaderDialog()
                    count++
                    if (count <= 3) {
                        Log.e("count", count.toString())
                        apiCallOrderDet(orderId)
                    } else {
                        myToast(context, t.message.toString())
                        AppProgressBar.hideLoaderDialog()

                    }
//                    AppProgressBar.hideLoaderDialog()
                }

            })

    }

    private fun apiCallStatuesChange(
        orderId: String,
        staues: String,
    ) {
        AppProgressBar.showLoaderDialog(context)
        ApiClient.apiService.statuesChange(
            sessionManager.idToken.toString(),
            orderId, staues
        )
            .enqueue(object : Callback<ModelSendSer> {
                @SuppressLint("LogNotTimber", "SetTextI18n")
                override fun onResponse(
                    call: Call<ModelSendSer>, response: Response<ModelSendSer>
                ) {
                    try {
                        if (response.code() == 404) {
                            myToast(context, "Something went wrong")

                        } else if (response.code() == 500) {
                            myToast(context, "Server Error")
                            AppProgressBar.hideLoaderDialog()

                        } else if (response.code() == 200) {
                            myToast(context, "Status updated")
                            apiCallOrderDet(orderId)
                            AppProgressBar.hideLoaderDialog()

                        } else {

                          //  apiCallOrderDet(orderId)

                            AppProgressBar.hideLoaderDialog()


                        }
                    } catch (e: Exception) {
                        myToast(context, "Something went wrong")
                        e.printStackTrace()
                        AppProgressBar.hideLoaderDialog()

                    }
                }


                override fun onFailure(call: Call<ModelSendSer>, t: Throwable) {
                    myToast(context, t.message.toString())
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

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        back=true
     }
    companion object{
        var back=false
    }
    private fun pmFormate(date: String): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val date = dateFormat.parse(date)

        val pmFormat = SimpleDateFormat("dd MMM yyyy hh:mm aaa", Locale.getDefault())
        return pmFormat.format(date)
    }
}