package com.amtech.vendorservices.V.Order.activity

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.amtech.vendorservices.R
import com.amtech.vendorservices.V.Dashboard.Dashboard
import com.amtech.vendorservices.V.Helper.AppProgressBar
import com.amtech.vendorservices.V.Helper.myToast
import com.amtech.vendorservices.V.Order.Model.DeliveryAddress
import com.amtech.vendorservices.V.Order.Model.ModelOrderDet.ModelOrderDet
import com.amtech.vendorservices.V.Order.Model.ModelSendSer.ModelSendSer
import com.amtech.vendorservices.V.retrofit.ApiClient
import com.amtech.vendorservices.V.sharedpreferences.SessionManager
import com.amtech.vendorservices.databinding.ActivityOrderDetailsBinding
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.LocalDate
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
    var type = ""
    var currentdate = ""
    var orderPayment = ""
    var serviceDateNew = ""
    var count = 0
    var count1 = 0
    var dateNew: LocalDateTime? = null
    var currentDateNew: LocalDateTime? = null
    private val context = this@OrderDetails
    lateinit var sessionManager: SessionManager


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        sessionManager = SessionManager(context)

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
        when (sessionManager.usertype) {
            "car" -> {
                binding.tvCarTypeH.text = resources.getString(R.string.Car_Type)
             }

            "home" -> {
                binding.tvCarTypeH.text = resources.getString(R.string.Home_Type)
                 binding.tvTravelingPersonsH.text = resources.getString(R.string.Persons)
                 binding.tvDrivingTypeH.text = resources.getString(R.string.Type)
             }

            else -> {
                binding.tvCarTypeH.text = resources.getString(R.string.Translotor_Type)
                 binding.tvTravelingPersonsH.text = resources.getString(R.string.Persons)
                binding.tvDrivingTypeH.text = resources.getString(R.string.Type)


            }
        }

        orderId = intent.getStringExtra("orderId").toString()
        orderStatus = intent.getStringExtra("orderStatus").toString()
        serviceDateNew = intent.getStringExtra("serDate").toString()
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
            }

            else -> {

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
            if (orderPayment=="partial"){
                myToast(context,"Full payment not paid yet")
            }else{
                apiCallStatuesChange(orderId, "delivered")

            }
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
                @SuppressLint("LogNotTimber", "SetTextI18n", "ResourceType",
                    "SuspiciousIndentation"
                )
                override fun onResponse(
                    call: Call<ModelOrderDet>, response: Response<ModelOrderDet>
                ) {
                    try {
                        if (response.code() == 404) {
                            myToast(context, resources.getString(R.string.Something_went_wrong))

                        } else if (response.code() == 500) {
                            myToast(context, resources.getString(R.string.Server_Error))
                            AppProgressBar.hideLoaderDialog()

                        }
                        /*else if (response.body()!!.data.isEmpty()) {
                            myToast(context, "No Data Found")
                            binding.layoutOrderDet.visibility=View.GONE
                            binding.layoutNotFoundN.visibility=View.VISIBLE
                            AppProgressBar.hideLoaderDialog()

                        }*/
                        else {
                            binding.orderId.text = response.body()!!.id.toString()
                            date = response.body()!!.created_at
                            binding.tvPaymentStatus.text = response.body()!!.payment_status
                            binding.tvPayType.text = response.body()!!.pay_type
                            binding.tvOrderPayment.text = response.body()!!.order_payment
                            orderPayment = response.body()!!.order_payment
                            if (response.body()!!.order_status == "delivered") {
                                binding.tvOrderStatus.text = resources.getString(R.string.Completed)
                            } else {
                                binding.tvOrderStatus.text = response.body()!!.order_status
                            }
                            binding.date.text=serviceDateNew
                            orderStatus = response.body()!!.order_status
                            binding.tvTotal.text = response.body()!!.order_amount.toString() + " $"

                            for (i in response.body()!!.data) {
                                binding.tvHomeDays.text = i.food_details.home_days

                                //  binding.date.text = pmFormate(response.body()!!.created_at)
                                if (i.food_details.dates.isNotEmpty()) {
                                  //  binding.date.text = i.food_details.dates
                                    val currentDate: String =
                                        SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).format(
                                            Date()
                                        )
                                   // serviceDateNew = i.food_details.dates.substringBefore(",").replace(",", "")
                                }
                                binding.tvName.text = i.food_details.name
                                binding.tvPrice.text = i.food_details.price.toString() + " $"
                                binding.tvPrice1.text = i.food_details.price.toString() + " $"

                                binding.tvCarType.text = i.food_details.car_type.toString()
                                binding.tvTravelingPersons.text = i.food_details.trperson.toString()
                                binding.tvDrivingType.text = i.food_details.driv_type.toString()
                                binding.tvRentType.text = i.food_details.rent_typ.toString()
                                binding.tvAmenities.text = i.food_details.amenities.toString()

//
//                                if (i.food_details.discount != null) {
//                                    binding.tvDiscount.text = i.food_details.discount.toString()
//                                }

                                type = i.food_details.food_type

//                                var jsonString=""
//
//                                for (i in response.body()!!.details){
//                                     jsonString = i.food_details.toString()
//
//                                }
//                                val jsonObject = JSONObject(jsonString)

//
//                                val nameNew = jsonObject.getString("name")
//                                val amenities = jsonObject.getString("amenities")
//                                val rent_typ = jsonObject.getString("rent_typ")
//                                val trFrom = jsonObject.getString("tr_from")
//                                val trTo = jsonObject.getString("tr_to")
//                                val trPerson = jsonObject.getString("trperson")
//                                val drivType = jsonObject.optString("driv_type", "N/A")


                                for (i in response.body()!!.details) {
                                    binding.tvTrFrom.text = resources.getString(R.string.From) +i.food_details.tr_from
                                    binding.tvTraTo.text = resources.getString(R.string.To) +i.food_details.tr_to
                                    binding.tvDrivingType.text = i.food_details.driv_type.toString()
                                }

                                val orderStatues = response.body()!!.order_status

//
//                                if (orderStatues == "delivered") {
//                                    binding.tvOrderStatus.text = "Completed"
//                                } else {
//                                    binding.tvOrderStatus.text =
//                                        intent.getStringExtra("orderStatus").toString()
//
//                                }


                            }

                            binding.tvCusName.text =
                                response.body()!!.customer.f_name + " " + response.body()!!.customer.l_name
                            binding.tvEmail.text = response.body()!!.customer.email
                            binding.tvNumber.text = response.body()!!.customer.phone
                            val jsonString = response.body()!!.delivery_address


                            val deliveryAddress =
                                Gson().fromJson(jsonString, DeliveryAddress::class.java)
                            binding.tvDelName.text = deliveryAddress.contact_person_name
                            binding.tvDelNumber.text = deliveryAddress.contact_person_number

                            AppProgressBar.hideLoaderDialog()

                            when (type) {
                                "translator" -> {
                                    binding.layoutTravling.visibility = View.GONE
                                    binding.layoutDrivingType.visibility = View.GONE
                                    binding.layoutRentType.visibility = View.GONE
                                    binding.layoutAminites.visibility = View.GONE
                                    binding.layoutHomeDays.visibility = View.GONE
                                }

                                "home" -> {
                                    binding.layoutTravling.visibility = View.GONE
                                    binding.layoutDrivingType.visibility = View.GONE
                                    binding.layoutTraTo.visibility = View.GONE

                                }

                                else -> {
                                    binding.layoutHomeDays.visibility = View.GONE
                                    binding.layoutRentType.visibility = View.GONE
                                    binding.layoutAminites.visibility = View.GONE
                                    binding.layoutTraTo.visibility = View.GONE

                                }
                            }
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
                                    binding.btnDeleverdDesable.visibility = View.GONE
                                    binding.btnDeleverd.visibility = View.GONE
                                }

                                "pending" -> {
                                    binding.btnConfirm.visibility = View.VISIBLE
                                    binding.btnCancel.visibility = View.VISIBLE
                                    binding.btnDeleverdDesable.visibility = View.GONE

                                }

                                "precessing" -> {
                                    binding.btnDeleverd.visibility = View.VISIBLE
                                }

                                "delivered" -> {
                                    binding.btnDeleverd.visibility = View.GONE
                                    binding.btnConfirm.visibility = View.GONE
                                    binding.btnCancel.visibility = View.GONE
                                    binding.btnDeleverdDesable.visibility = View.GONE

                                }

                                else -> {

                                }

                            }
                            val currentDate: String = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).format(Date())
//
//                            val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
//
//                            dateNew = LocalDateTime.parse(serviceDateNew, formatter)
//                            currentDateNew = LocalDateTime.parse(currentDate, formatter)

                            val serviceDate = serviceDateNew.substringBefore(",")
                            val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
                            if (serviceDate!="null") {
                            // Define the dates as strings
                            val dateStr1 = currentDate
                            val dateStr2 = serviceDate

                            // Parse the strings to LocalDate objects
                            val date1 = LocalDate.parse(dateStr1, formatter)
                            val date2 = LocalDate.parse(dateStr2, formatter)

                            // Compare the dates

                                if (date1 > date2) {
                                    println("$date1 is after $date2")
                                    if (orderStatus == "delivered" || orderStatus == "canceled" || orderStatus == "pending") {
                                        binding.btnDeleverdDesable.visibility = View.GONE
                                        binding.btnDeleverd.visibility = View.GONE
                                    } else {
                                        binding.btnDeleverdDesable.visibility = View.GONE
                                        binding.btnDeleverd.visibility = View.VISIBLE
                                    }

                                } else if (date1 < date2) {
                                    println("$date1 is before $date2")
                                    if (orderStatus == "delivered" || orderStatus == "canceled" || orderStatus == "pending") {
                                        binding.btnDeleverdDesable.visibility = View.GONE
                                        binding.btnDeleverd.visibility = View.GONE
                                    } else {
                                        binding.btnDeleverdDesable.visibility = View.VISIBLE
                                        binding.btnDeleverd.visibility = View.GONE
                                    }
                                } else {
                                    if (orderStatus == "delivered" || orderStatus == "canceled" || orderStatus == "pending") {
                                        binding.btnDeleverdDesable.visibility = View.GONE
                                        binding.btnDeleverd.visibility = View.GONE
                                    } else {
                                        binding.btnDeleverdDesable.visibility = View.GONE
                                        binding.btnDeleverd.visibility = View.VISIBLE
                                    }
                                    println("$date1 is the same as $date2")
                                }
                            }


                         //   val newDateString = currentDate.replace("-", "")

//                            serviceDateNew.replace("-", "")
//                            if (newDateString.toInt() < serviceDate.toInt()) {
//                                binding.btnDeleverdDesable.visibility = View.VISIBLE
//                                binding.btnDeleverd.visibility = View.GONE
//                            } else if (orderStatus == "delivered" || orderStatus == "canceled") {
//                                binding.btnDeleverd.visibility = View.GONE
//                            } else if (orderStatus == "pending") {
//                                binding.btnDeleverd.visibility = View.GONE
//                                binding.btnDeleverdDesable.visibility = View.GONE
//
//                            } else {
//                                 binding.btnDeleverd.visibility = View.VISIBLE
//                             }

                        }
                    } catch (e: Exception) {
                        myToast(context, resources.getString(R.string.Something_went_wrong))
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
                            myToast(context, resources.getString(R.string.Something_went_wrong))

                        } else if (response.code() == 500) {
                            myToast(context, resources.getString(R.string.Server_Error))
                            AppProgressBar.hideLoaderDialog()

                        } else if (response.code() == 200) {
                            myToast(context, resources.getString(R.string.Status_updated))
                            apiCallOrderDet(orderId)
                            AppProgressBar.hideLoaderDialog()

                        } else {

                            //  apiCallOrderDet(orderId)

                            AppProgressBar.hideLoaderDialog()


                        }
                    } catch (e: Exception) {
                        myToast(context, resources.getString(R.string.Server_Error))
                        e.printStackTrace()
                        AppProgressBar.hideLoaderDialog()

                    }
                }


                override fun onFailure(call: Call<ModelSendSer>, t: Throwable) {
                    myToast(context, t.message.toString())
                    AppProgressBar.hideLoaderDialog()
                    count1++
                    if (count1 <= 3) {
                        Log.e("count", count1.toString())
                        apiCallStatuesChange(orderId, staues)
                    } else {
                        myToast(context, t.message.toString())
                        AppProgressBar.hideLoaderDialog()

                    }
                    AppProgressBar.hideLoaderDialog()
                }

            })

    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        back = true
    }

    companion object {
        var back = false
    }
    private fun refresh() {
        overridePendingTransition(0, 0)
        finish()
        startActivity(intent)
        overridePendingTransition(0, 0)
    }
    private fun pmFormate(date: String): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val date = dateFormat.parse(date)

        val pmFormat = SimpleDateFormat("dd MMM yyyy hh:mm aaa", Locale.getDefault())
        return pmFormat.format(date)
    }
    override fun onDestroy() {
        super.onDestroy()
        Dashboard.refreshLanNew=true
    }
}