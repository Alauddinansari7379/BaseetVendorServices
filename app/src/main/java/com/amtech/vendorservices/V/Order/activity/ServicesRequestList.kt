package com.amtech.vendorservices.V.Order.activity

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.amtech.vendorservices.R
import com.amtech.vendorservices.V.Dashboard.Dashboard
import com.amtech.vendorservices.V.Helper.AppProgressBar
import com.amtech.vendorservices.V.Helper.myToast
import com.amtech.vendorservices.V.Order.Adapter.AdapterRelatedServiceList
import com.amtech.vendorservices.V.Order.Adapter.AdapterSerRequestList
import com.amtech.vendorservices.V.Order.Adapter.AdapterSerRequestList.Companion.requestIdNew
import com.amtech.vendorservices.V.Order.Model.Data
import com.amtech.vendorservices.V.Order.Model.ModeUpdatePrice.ModelUpdatePrice
import com.amtech.vendorservices.V.Order.Model.ModelChange.ModelChange
import com.amtech.vendorservices.V.Order.Model.ModelRelatedSer.ModelServiceRet
import com.amtech.vendorservices.V.Order.Model.ModelSendSer.ModelSendSer
import com.amtech.vendorservices.V.Order.Model.ModelServiceReq
import com.amtech.vendorservices.V.Order.modeldetails.ModelDetails
import com.amtech.vendorservices.V.retrofit.ApiClient
import com.amtech.vendorservices.V.sharedpreferences.SessionManager
import com.amtech.vendorservices.databinding.ActivityServicesRequestListBinding
import com.amtech.vendorservices.databinding.PopupServicDetailsBinding
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ServicesRequestList : AppCompatActivity(), AdapterSerRequestList.Accept,
    AdapterRelatedServiceList.SendRequest {
    private val binding by lazy {
        ActivityServicesRequestListBinding.inflate(layoutInflater)
    }
    var dialog: Dialog? = null
    var requestId = ""
    var count = 0
    var count1 = 0
    var count2 = 0
    var count3 = 0
    var count4 = 0
    private var allServiceDetails: com.amtech.vendorservices.V.Order.modeldetails.Data? = null
    var count5 = 0

    private val context = this@ServicesRequestList
    private lateinit var sessionManager: SessionManager
    private lateinit var mainData: ArrayList<Data>
    private lateinit var mainDataNew: ArrayList<com.amtech.vendorservices.V.Order.Model.ModelRelatedSer.Data>
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
        with(binding) {
            imgBack.setOnClickListener {
                onBackPressed()
            }
            apiCallServiceReqList()

            edtSearch.addTextChangedListener { str ->
                setRecyclerViewAdapter(mainData.filter {
                    it.name != null && it.name!!.contains(str.toString(), ignoreCase = true)
                } as ArrayList<Data>)
            }
        }

    }

    private fun apiCallServiceReqList() {
        AppProgressBar.showLoaderDialog(context)
        ApiClient.apiService.serviceRequest(
            sessionManager.idToken.toString(),

            )
            .enqueue(object : Callback<ModelServiceReq> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelServiceReq>, response: Response<ModelServiceReq>
                ) {
                    try {
                        if (response.code() == 404) {
                            myToast(context, resources.getString(R.string.Something_went_wrong))

                        } else if (response.code() == 500) {
                            myToast(context, resources.getString(R.string.Server_Error))
                            AppProgressBar.hideLoaderDialog()

                        } else if (response.body()!!.data.isEmpty()) {
                            myToast(context, resources.getString(R.string.No_Data_Found))
                            AppProgressBar.hideLoaderDialog()

                        } else {
                            mainData = response.body()!!.data
                            mainData.reverse()

                            setRecyclerViewAdapter(mainData)
                            AppProgressBar.hideLoaderDialog()


                        }
                    } catch (e: Exception) {
                        myToast(context, resources.getString(R.string.Something_went_wrong))
                        e.printStackTrace()
                        AppProgressBar.hideLoaderDialog()

                    }
                }


                override fun onFailure(call: Call<ModelServiceReq>, t: Throwable) {
                    AppProgressBar.hideLoaderDialog()
                    count++
                    if (count <= 3) {
                        Log.e("count", count.toString())
                        apiCallServiceReqList()
                    } else {
                        myToast(context, t.message.toString())
                        AppProgressBar.hideLoaderDialog()

                    }
                    AppProgressBar.hideLoaderDialog()
                }

            })

    }

    private fun setRecyclerViewAdapter(data: ArrayList<Data>) {
        binding.recyclerView.apply {
            adapter =
                AdapterSerRequestList(this@ServicesRequestList, data, this@ServicesRequestList)
            AppProgressBar.hideLoaderDialog()

        }
    }

    private fun apiCallRelatedService(
        langFrom: String,
        langTo: String,
        tranServ: String,
        price: String
    ) {
        AppProgressBar.showLoaderDialog(context)
        ApiClient.apiService.getRelatedService(
            sessionManager.idToken.toString(),
            langFrom, langTo, tranServ, price
        )
            .enqueue(object : Callback<ModelServiceRet> {
                @SuppressLint("LogNotTimber", "SetTextI18n")
                override fun onResponse(
                    call: Call<ModelServiceRet>, response: Response<ModelServiceRet>
                ) {
                    try {
                        if (response.code() == 404) {
                            myToast(context, resources.getString(R.string.Something_went_wrong))

                        } else if (response.code() == 500) {
                            myToast(context, resources.getString(R.string.Server_Error))
                            AppProgressBar.hideLoaderDialog()

                        } else if (response.body()!!.data.isEmpty()) {
                            myToast(context, resources.getString(R.string.No_Data_Found))
                            AppProgressBar.hideLoaderDialog()

                        } else {
                            val view = layoutInflater.inflate(R.layout.dialog_accepct_service, null)
                            dialog = Dialog(context)

                            /*     var nameTv = view!!.findViewById<TextView>(R.id.name)
                              val priceTv = view!!.findViewById<TextView>(R.id.price)
                              val fromLanTv = view!!.findViewById<TextView>(R.id.fromLan)
                              val toLanTv = view!!.findViewById<TextView>(R.id.toLan)
                              val serviceTypeTv = view!!.findViewById<TextView>(R.id.serviceType)
                              val serviceDateTv =
                                  view!!.findViewById<TextView>(R.id.serviceDate)
                              val serviceHourTv = view!!.findViewById<TextView>(R.id.serviceHour)
                              val portfolioVideoTv =
                                  view!!.findViewById<TextView>(R.id.portfolioVideo)
                              val descriptionTv = view!!.findViewById<TextView>(R.id.description)
                              val btnSendServiceTv = view!!.findViewById<Button>(R.id.btnSendService)
                              val imageViewTv = view!!.findViewById<ImageView>(R.id.imageView)


                              nameTv.text = response.body()!!.data.name
                              priceTv.text = response.body()!!.data.price + "$"
                              fromLanTv.text = response.body()!!.data.tr_from
                              toLanTv.text = response.body()!!.data.tr_to
                              serviceTypeTv.text = response.body()!!.data.drone
                              serviceDateTv.text = response.body()!!.data.dates.toString()
                              serviceHourTv.text = response.body()!!.data.ser_hour.toString()
                              portfolioVideoTv.text = response.body()!!.data.port_video
                              descriptionTv.text = response.body()!!.data.description
                                   Picasso.get()
                                .load("https://baseet.thedemostore.in/storage/app/public/product/2024-04-01-660a99b5ec3bd.png")
                                .placeholder(R.drawable.user_logo)
                                .error(R.drawable.error_placeholder)
                                .into(imageViewTv)
                              */


                            val imgClose = view!!.findViewById<ImageView>(R.id.imgCloseDil)
                            val recyclerView = view!!.findViewById<RecyclerView>(R.id.recyclerView)

                            mainDataNew = response.body()!!.data

                            recyclerView.apply {
                                adapter = AdapterRelatedServiceList(
                                    this@ServicesRequestList,
                                    mainDataNew,
                                    this@ServicesRequestList
                                )
                                AppProgressBar.hideLoaderDialog()
                            }

                            dialog = Dialog(context)
                            if (view.parent != null) {
                                (view.parent as ViewGroup).removeView(view) // <- fix
                            }
                            dialog!!.setContentView(view)
                            dialog?.setCancelable(true)

                            dialog?.show()

                            imgClose.setOnClickListener {
                                dialog?.dismiss()
                            }


                            AppProgressBar.hideLoaderDialog()

                        }
                    } catch (e: Exception) {
                        myToast(context, resources.getString(R.string.Something_went_wrong))
                        e.printStackTrace()
                        AppProgressBar.hideLoaderDialog()

                    }
                }


                override fun onFailure(call: Call<ModelServiceRet>, t: Throwable) {
                    //  myToast(context, t.message.toString())
                    AppProgressBar.hideLoaderDialog()
                    count1++
                    if (count1 <= 3) {
                        Log.e("count", count1.toString())
                        apiCallRelatedService(langFrom, langTo, tranServ, price)
                    } else {
                        myToast(context, t.message.toString())
                        AppProgressBar.hideLoaderDialog()

                    }
                    AppProgressBar.hideLoaderDialog()
                }

            })

    }

    private fun apiCallRelatedServiceCar(
        langFrom: String,
        langTo: String,
        tranServ: String,
        price: String
    ) {
        AppProgressBar.showLoaderDialog(context)
        ApiClient.apiService.getRelatedServiceCar(
            sessionManager.idToken.toString(),
            langFrom, tranServ, price
        )
            .enqueue(object : Callback<ModelServiceRet> {
                @SuppressLint("LogNotTimber", "SetTextI18n")
                override fun onResponse(
                    call: Call<ModelServiceRet>, response: Response<ModelServiceRet>
                ) {
                    try {
                        if (response.code() == 404) {
                            myToast(context, resources.getString(R.string.Something_went_wrong))

                        } else if (response.code() == 500) {
                            myToast(context, resources.getString(R.string.Server_Error))
                            AppProgressBar.hideLoaderDialog()

                        } else if (response.body()!!.data.isEmpty()) {
                            myToast(context, resources.getString(R.string.No_Data_Found))
                            AppProgressBar.hideLoaderDialog()

                        } else {
                            val view = layoutInflater.inflate(R.layout.dialog_accepct_service, null)
                            dialog = Dialog(context)
                            /*     var nameTv = view!!.findViewById<TextView>(R.id.name)
                              val priceTv = view!!.findViewById<TextView>(R.id.price)
                              val fromLanTv = view!!.findViewById<TextView>(R.id.fromLan)
                              val toLanTv = view!!.findViewById<TextView>(R.id.toLan)
                              val serviceTypeTv = view!!.findViewById<TextView>(R.id.serviceType)
                              val serviceDateTv =
                                  view!!.findViewById<TextView>(R.id.serviceDate)
                              val serviceHourTv = view!!.findViewById<TextView>(R.id.serviceHour)
                              val portfolioVideoTv =
                                  view!!.findViewById<TextView>(R.id.portfolioVideo)
                              val descriptionTv = view!!.findViewById<TextView>(R.id.description)
                              val btnSendServiceTv = view!!.findViewById<Button>(R.id.btnSendService)
                              val imageViewTv = view!!.findViewById<ImageView>(R.id.imageView)


                              nameTv.text = response.body()!!.data.name
                              priceTv.text = response.body()!!.data.price + "$"
                              fromLanTv.text = response.body()!!.data.tr_from
                              toLanTv.text = response.body()!!.data.tr_to
                              serviceTypeTv.text = response.body()!!.data.drone
                              serviceDateTv.text = response.body()!!.data.dates.toString()
                              serviceHourTv.text = response.body()!!.data.ser_hour.toString()
                              portfolioVideoTv.text = response.body()!!.data.port_video
                              descriptionTv.text = response.body()!!.data.description
                                   Picasso.get()
                                .load("https://baseet.thedemostore.in/storage/app/public/product/2024-04-01-660a99b5ec3bd.png")
                                .placeholder(R.drawable.user_logo)
                                .error(R.drawable.error_placeholder)
                                .into(imageViewTv)
                              */


                            val imgClose = view!!.findViewById<ImageView>(R.id.imgCloseDil)
                            val recyclerView = view!!.findViewById<RecyclerView>(R.id.recyclerView)

                            mainDataNew = response.body()!!.data

                            recyclerView.apply {
                                adapter = AdapterRelatedServiceList(
                                    this@ServicesRequestList,
                                    mainDataNew,
                                    this@ServicesRequestList
                                )
                                AppProgressBar.hideLoaderDialog()
                            }

                            dialog = Dialog(context)
                            if (view.parent != null) {
                                (view.parent as ViewGroup).removeView(view) // <- fix
                            }
                            dialog!!.setContentView(view)
                            dialog?.setCancelable(true)

                            dialog?.show()

                            imgClose.setOnClickListener {
                                dialog?.dismiss()
                            }


                            AppProgressBar.hideLoaderDialog()

                        }
                    } catch (e: Exception) {
                        myToast(context, resources.getString(R.string.Something_went_wrong))
                        e.printStackTrace()
                        AppProgressBar.hideLoaderDialog()

                    }
                }

                override fun onFailure(call: Call<ModelServiceRet>, t: Throwable) {
                    AppProgressBar.hideLoaderDialog()
                    count2++
                    if (count2 <= 3) {
                        Log.e("count", count2.toString())
                        apiCallRelatedServiceCar(langFrom, langTo, tranServ, price)
                    } else {
                        myToast(context, t.message.toString())
                        AppProgressBar.hideLoaderDialog()

                    }
                    AppProgressBar.hideLoaderDialog()
                }

            })

    }

    private fun apiCallRelatedServiceHome(
        langFrom: String,
        langTo: String,
        tranServ: String,
        price: String
    ) {
        AppProgressBar.showLoaderDialog(context)
        ApiClient.apiService.getRelatedServiceHome(
            sessionManager.idToken.toString(),
            tranServ, price
        )
            .enqueue(object : Callback<ModelServiceRet> {
                @SuppressLint("LogNotTimber", "SetTextI18n")
                override fun onResponse(
                    call: Call<ModelServiceRet>, response: Response<ModelServiceRet>
                ) {
                    try {
                        if (response.code() == 401) {
                            myToast(context, resources.getString(R.string.Something_went_wrong))

                        } else if (response.code() == 500) {
                            myToast(context, resources.getString(R.string.Server_Error))
                            AppProgressBar.hideLoaderDialog()

                        } else if (response.body()!!.data.isEmpty()) {
                            myToast(context, resources.getString(R.string.No_Data_Found))
                            AppProgressBar.hideLoaderDialog()

                        } else {
                            val view = layoutInflater.inflate(R.layout.dialog_accepct_service, null)
                            dialog = Dialog(context)
                            /*     var nameTv = view!!.findViewById<TextView>(R.id.name)
                              val priceTv = view!!.findViewById<TextView>(R.id.price)
                              val fromLanTv = view!!.findViewById<TextView>(R.id.fromLan)
                              val toLanTv = view!!.findViewById<TextView>(R.id.toLan)
                              val serviceTypeTv = view!!.findViewById<TextView>(R.id.serviceType)
                              val serviceDateTv =
                                  view!!.findViewById<TextView>(R.id.serviceDate)
                              val serviceHourTv = view!!.findViewById<TextView>(R.id.serviceHour)
                              val portfolioVideoTv =
                                  view!!.findViewById<TextView>(R.id.portfolioVideo)
                              val descriptionTv = view!!.findViewById<TextView>(R.id.description)
                              val btnSendServiceTv = view!!.findViewById<Button>(R.id.btnSendService)
                              val imageViewTv = view!!.findViewById<ImageView>(R.id.imageView)


                              nameTv.text = response.body()!!.data.name
                              priceTv.text = response.body()!!.data.price + "$"
                              fromLanTv.text = response.body()!!.data.tr_from
                              toLanTv.text = response.body()!!.data.tr_to
                              serviceTypeTv.text = response.body()!!.data.drone
                              serviceDateTv.text = response.body()!!.data.dates.toString()
                              serviceHourTv.text = response.body()!!.data.ser_hour.toString()
                              portfolioVideoTv.text = response.body()!!.data.port_video
                              descriptionTv.text = response.body()!!.data.description
                                   Picasso.get()
                                .load("https://baseet.thedemostore.in/storage/app/public/product/2024-04-01-660a99b5ec3bd.png")
                                .placeholder(R.drawable.user_logo)
                                .error(R.drawable.error_placeholder)
                                .into(imageViewTv)
                              */


                            val imgClose = view!!.findViewById<ImageView>(R.id.imgCloseDil)
                            val recyclerView = view!!.findViewById<RecyclerView>(R.id.recyclerView)

                            mainDataNew = response.body()!!.data

                            recyclerView.apply {
                                adapter = AdapterRelatedServiceList(
                                    this@ServicesRequestList,
                                    mainDataNew,
                                    this@ServicesRequestList
                                )
                                AppProgressBar.hideLoaderDialog()
                            }

                            dialog = Dialog(context)
                            if (view.parent != null) {
                                (view.parent as ViewGroup).removeView(view) // <- fix
                            }
                            dialog!!.setContentView(view)
                            dialog?.setCancelable(true)

                            dialog?.show()

                            imgClose.setOnClickListener {
                                dialog?.dismiss()
                            }


                            AppProgressBar.hideLoaderDialog()

                        }
                    } catch (e: Exception) {
                        myToast(context, resources.getString(R.string.Something_went_wrong))
                        e.printStackTrace()
                        AppProgressBar.hideLoaderDialog()

                    }
                }


                override fun onFailure(call: Call<ModelServiceRet>, t: Throwable) {
                    myToast(context, t.message.toString())
                    // myToast(context, "No Data Found")

                    AppProgressBar.hideLoaderDialog()
                    count3++
                    if (count3 <= 3) {
                        Log.e("count", count3.toString())
                        apiCallRelatedServiceHome(langFrom, langTo, tranServ, price)
                    } else {
                        myToast(context, t.message.toString())
                        AppProgressBar.hideLoaderDialog()

                    }
                    AppProgressBar.hideLoaderDialog()
                }

            })

    }

    private fun apiCallSendService(
        foodId: String,
    ) {
        AppProgressBar.showLoaderDialog(context)
        ApiClient.apiService.sendService(
            sessionManager.idToken.toString(),
            foodId, requestId
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

                        } else if (response.body()!!.data.isEmpty()) {
                            myToast(context, resources.getString(R.string.No_Data_Found))
                            AppProgressBar.hideLoaderDialog()

                        } else {
                            myToast(context, response.body()!!.data)
                            AppProgressBar.hideLoaderDialog()
                            dialog?.dismiss()
                            refresh()


                        }
                    } catch (e: Exception) {
                        myToast(context, resources.getString(R.string.Something_went_wrong))
                        e.printStackTrace()
                        AppProgressBar.hideLoaderDialog()

                    }
                }


                override fun onFailure(call: Call<ModelSendSer>, t: Throwable) {
                    myToast(context, t.message.toString())
                    AppProgressBar.hideLoaderDialog()
                    count4++
                    if (count4 <= 3) {
                        Log.e("count", count4.toString())
                        apiCallSendService(foodId)
                    } else {
                        myToast(context, t.message.toString())
                        AppProgressBar.hideLoaderDialog()

                    }
                    AppProgressBar.hideLoaderDialog()
                }

            })

    }

    private fun apiCallStatuesChange(
        id: String,
        restaurant_id: String?,
        foodId: String,
    ) {
        AppProgressBar.showLoaderDialog(context)
        ApiClient.apiService.changesStatus(
            sessionManager.idToken.toString(),
            id, restaurant_id.toString(), foodId, "accept"
        )
            .enqueue(object : Callback<ModelChange> {
                @SuppressLint("LogNotTimber", "SetTextI18n")
                override fun onResponse(
                    call: Call<ModelChange>, response: Response<ModelChange>
                ) {
                    try {
                        if (response.code() == 404) {
                            myToast(context, resources.getString(R.string.Something_went_wrong))

                        } else if (response.code() == 500) {
                            myToast(context, resources.getString(R.string.Server_Error))
                            AppProgressBar.hideLoaderDialog()

                        } else {
                            count5 = 0
                            myToast(context, response.body()!!.msg)
                            AppProgressBar.hideLoaderDialog()
                            dialog?.dismiss()
                            refresh()


                        }
                    } catch (e: Exception) {
                        myToast(context, resources.getString(R.string.Something_went_wrong))
                        e.printStackTrace()
                        AppProgressBar.hideLoaderDialog()

                    }
                }


                override fun onFailure(call: Call<ModelChange>, t: Throwable) {
                    myToast(context, t.message.toString())
                    AppProgressBar.hideLoaderDialog()
                    count5++
                    if (count5 <= 3) {
                        Log.e("count", count5.toString())
                        apiCallStatuesChange(id, restaurant_id, foodId)
                    } else {
                        myToast(context, t.message.toString())
                        AppProgressBar.hideLoaderDialog()

                    }
                    AppProgressBar.hideLoaderDialog()
                }

            })

    }

    private fun apiCallUpdatePrice(
        foodId: String,
        price: String,

        ) {
        AppProgressBar.showLoaderDialog(context)
        ApiClient.apiService.updatePrice(
            sessionManager.idToken.toString(),
            foodId,
            price, requestIdNew
        )
            .enqueue(object : Callback<ModelUpdatePrice> {
                @SuppressLint("LogNotTimber", "SetTextI18n")
                override fun onResponse(
                    call: Call<ModelUpdatePrice>, response: Response<ModelUpdatePrice>
                ) {
                    try {
                        if (response.code() == 404) {
                            myToast(context, resources.getString(R.string.Something_went_wrong))

                        } else if (response.code() == 500) {
                            myToast(context, resources.getString(R.string.Server_Error))
                            AppProgressBar.hideLoaderDialog()

                        } else {
                            myToast(context, response.body()!!.message)
                            AppProgressBar.hideLoaderDialog()
                            dialog?.dismiss()
                            refresh()
                        }
                    } catch (e: Exception) {
                        myToast(context, resources.getString(R.string.Something_went_wrong))
                        e.printStackTrace()
                        AppProgressBar.hideLoaderDialog()

                    }
                }

                override fun onFailure(call: Call<ModelUpdatePrice>, t: Throwable) {
                    myToast(context, t.message.toString())
                    AppProgressBar.hideLoaderDialog()
                    count4++
                    if (count4 <= 3) {
                        Log.e("count", count4.toString())
                        apiCallSendService(foodId)
                    } else {
                        myToast(context, t.message.toString())
                        AppProgressBar.hideLoaderDialog()

                    }
                    AppProgressBar.hideLoaderDialog()
                }

            })

    }

    @SuppressLint("SetTextI18n")
    override fun accept(
        id: String, venId: String, whchserv: String
    ) {
        requestId = id
        apiCallStatuesChange(id, venId, whchserv)

//        when (sessionManager.usertype) {
//            "car" -> {
//                apiCallRelatedServiceCar(langFrom, langTo, tranServ, price)
//            }
//            "home" -> {
//                apiCallRelatedServiceHome(langFrom, langTo, tranServ, price)
//            }
//            else -> {
//                apiCallRelatedService(langFrom, langTo, tranServ, price)
//
//            }
//        }


        AppProgressBar.hideLoaderDialog()
    }


    @SuppressLint("MissingInflatedId")
    override fun viewDoc(url: String) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.popup_document, null)
        val imgDoc = dialogView.findViewById<ImageView>(R.id.imgDoc)
        val btnClose = dialogView.findViewById<Button>(R.id.btnClose)

        val close = dialogView.findViewById<ImageView>(R.id.imgClose)

        val dialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .create()

        close.setOnClickListener {
            dialog.dismiss()
        }
        btnClose.setOnClickListener {
            dialog.dismiss()
        }
        if (url != null) {
            Picasso.get().load("https://baseet.thedemostore.in/storage/app/public/product/" + url)
                .error(R.drawable.error_placeholder)
                .into(imgDoc)
        }
        dialog.show()
    }

    override fun showDetailsPopup(venId: String) {
        apiCallGetDetails(venId)
    }

    override fun updatePricer(venId: String, price: String, position: Int) {
        apiCallUpdatePriceNew(venId,price,position)
    }

    override fun sendRequest(foodId: String) {
        apiCallSendService(foodId.toString())
    }

    override fun updatePrice(foodId: String, price: String) {
        apiCallUpdatePrice(foodId, price)

    }

    override fun onResume() {
        super.onResume()
        apiCallServiceReqList()

    }

    fun refresh() {
        overridePendingTransition(0, 0)
        finish()
        startActivity(intent)
        overridePendingTransition(0, 0)
    }

    override fun onDestroy() {
        super.onDestroy()
        Dashboard.refreshLanNew = true
    }

    fun apiCallGetDetails(id: String) {
        AppProgressBar.showLoaderDialog(context)
        ApiClient.apiService.getDetails(sessionManager.idToken.toString(), id)
            .enqueue(object : Callback<ModelDetails> {

                override fun onResponse(call: Call<ModelDetails>, response: Response<ModelDetails>) {
                    try {
                        if (response.code() == 404) {
                            myToast(context, resources.getString(R.string.Something_went_wrong))
                            AppProgressBar.hideLoaderDialog()
                        } else if (response.code() == 500) {
                            myToast(context, resources.getString(R.string.Server_Error))
                            AppProgressBar.hideLoaderDialog()
                        } else {
                            count = 0
                            AppProgressBar.hideLoaderDialog()
                            allServiceDetails = response.body()?.data

                            val binding = PopupServicDetailsBinding.inflate(LayoutInflater.from(context))
                            with(binding) {
                                if (allServiceDetails != null) {
                                    tvName.text = allServiceDetails!!.name
                                    tvPrice.text = allServiceDetails!!.price.toString()
                                    if (allServiceDetails!!.food_type == "translator") {
                                        tvFrom.text = "Tr from : ${allServiceDetails!!.tr_from}"
                                        tvTo.text = "Tr to : ${allServiceDetails!!.tr_to}"
                                    } else {
                                        tvTo.visibility = View.GONE
                                        tvFrom.visibility = View.GONE
                                    }

                                    tvType.text = "Type :${allServiceDetails!!.food_type}"
                                    tvDescription.text = allServiceDetails!!.description
                                    tvTax.text = "Tax : ${allServiceDetails!!.tax}"
                                    tvStatus.text = "Status : ${allServiceDetails!!.status}"
                                    tvOrderCount.text = "Order count : ${allServiceDetails!!.order_count}"
                                    tvAvgRating.text = "Avg rating : ${allServiceDetails!!.avg_rating}"
                                    tvRatingCount.text = "Rating count : ${allServiceDetails!!.rating_count}"
                                    tvQty.text = "Qty : ${allServiceDetails!!.qty}"

                                    if (allServiceDetails!!.food_type == "car") {
                                        tvCarType.text = allServiceDetails!!.car_type.toString()
                                        tvTravlingPer.text = allServiceDetails!!.trperson.toString()
                                        tvDatesN.text = "Dates : ${allServiceDetails!!.dates}"
                                        tvHomeDays.text = "Home days : ${allServiceDetails!!.home_days}"
                                    } else {
                                        tvCarType.visibility = View.GONE
                                        tvTravlingPer.visibility = View.GONE
                                        tvDatesN.visibility = View.GONE
                                        tvHomeDays.visibility = View.GONE
                                        tvTravalPerson.visibility = View.GONE
                                        tvCartypeHN.visibility = View.GONE
                                    }

                                    if (allServiceDetails!!.food_type == "home") {
                                        tvHomeDetail.text = "Home details :${allServiceDetails!!.amenities}"
                                        tvHomeType.text = "Home type :${allServiceDetails!!.car_type}"
                                    } else {
                                        tvHomeDetail.visibility = View.GONE
                                        tvHomeType.visibility = View.GONE
                                    }

                                    Glide.with(context)
                                        .load(allServiceDetails!!.appimage)
                                        .into(ivImage)
                                }

                                val dialog = AlertDialog.Builder(context)
                                    .setView(root)
                                    .create()

                                imgClose.setOnClickListener {
                                    dialog.dismiss()
                                }

                                btnClose.setOnClickListener {
                                    dialog.dismiss()
                                }

                                dialog.show()
                            }
                        }
                    } catch (e: Exception) {
                        myToast(context, resources.getString(R.string.Something_went_wrong))
                        e.printStackTrace()
                    }
                }

                override fun onFailure(call: Call<ModelDetails>, t: Throwable) {
                    myToast(context, t.message.toString())
                    AppProgressBar.hideLoaderDialog()
                    count++
                    if (count <= 3) {
                        apiCallGetDetails(id)
                    } else {
                        myToast(context, t.message.toString())
                    }
                    AppProgressBar.hideLoaderDialog()
                }
            })
    }
    fun apiCallUpdatePriceNew(id: String, price: String,position: Int) {
        AppProgressBar.showLoaderDialog(context)
        ApiClient.apiService.updatePrice1(sessionManager.idToken.toString(), id,price)
            .enqueue(object : Callback<ModelUpdatePrice> {

                override fun onResponse(call: Call<ModelUpdatePrice>, response: Response<ModelUpdatePrice>) {
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
                                myToast(context, "Price update successfully.")
                                apiCallServiceReqList()
//                                AdapterSerRequestList().notifyItemChanged(position)
                            }
                        }
                    } catch (e: Exception) {
                        myToast(context, resources.getString(R.string.Something_went_wrong))
                        e.printStackTrace()
                    }
                }

                override fun onFailure(call: Call<ModelUpdatePrice>, t: Throwable) {
                    myToast(context, t.message.toString())
                    AppProgressBar.hideLoaderDialog()
                    count++
                    if (count <= 3) {
                        apiCallUpdatePriceNew(id,price, position)
                    } else {
                        myToast(context, t.message.toString())
                    }
                    AppProgressBar.hideLoaderDialog()
                }
            })
    }

}