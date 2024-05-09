package com.amtech.vendorservices.V.Order.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.amtech.vendorservices.R
import com.amtech.vendorservices.V.Helper.AppProgressBar
import com.amtech.vendorservices.V.Helper.myToast
import com.amtech.vendorservices.V.Order.Adapter.AdapterRelatedServiceList
import com.amtech.vendorservices.V.Order.Adapter.AdapterSerRequestList
import com.amtech.vendorservices.V.Order.Model.Data
import com.amtech.vendorservices.V.Order.Model.ModeUpdatePrice.ModelUpdatePrice
import com.amtech.vendorservices.V.Order.Model.ModelRelatedSer.ModelServiceRet
import com.amtech.vendorservices.V.Order.Model.ModelSendSer.ModelSendSer
import com.amtech.vendorservices.V.Order.Model.ModelServiceReq
import com.amtech.vendorservices.V.retrofit.ApiClient
import com.amtech.vendorservices.databinding.ActivityServicesRequestListBinding
import com.example.hhfoundation.sharedpreferences.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ServicesRequestList : AppCompatActivity(), AdapterSerRequestList.Accept,AdapterRelatedServiceList.SendRequest {
    private val binding by lazy {
        ActivityServicesRequestListBinding.inflate(layoutInflater)
    }
    var dialog: Dialog? = null
    var requestId=""
    var count=0
    var count1=0
    var count2=0
    var count3=0
    var count4=0

    private val context = this@ServicesRequestList
    private lateinit var sessionManager: SessionManager
    private lateinit var mainData: ArrayList<Data>
    private lateinit var mainDataNew: ArrayList<com.amtech.vendorservices.V.Order.Model.ModelRelatedSer.Data>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        sessionManager = SessionManager(context)
        with(binding) {
            imagBack.setOnClickListener {
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
                            myToast(context, "Something went wrong")

                        } else if (response.code() == 500) {
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


                override fun onFailure(call: Call<ModelServiceReq>, t: Throwable) {
                    myToast(context, "Something went wrong")
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
                            myToast(context, "Something went wrong")

                        } else if (response.code() == 500) {
                            myToast(context, "Server Error")
                            AppProgressBar.hideLoaderDialog()

                        } else if (response.body()!!.data.isEmpty()) {
                            myToast(context, "No Data Found")
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
                                adapter = AdapterRelatedServiceList(this@ServicesRequestList, mainDataNew,this@ServicesRequestList)
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
                        myToast(context, "Something went wrong")
                        e.printStackTrace()
                        AppProgressBar.hideLoaderDialog()

                    }
                }


                override fun onFailure(call: Call<ModelServiceRet>, t: Throwable) {
                    //  myToast(context, t.message.toString())
                    myToast(context, "No Data Found")

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
                            myToast(context, "Something went wrong")

                        } else if (response.code() == 500) {
                            myToast(context, "Server Error")
                            AppProgressBar.hideLoaderDialog()

                        } else if (response.body()!!.data.isEmpty()) {
                            myToast(context, "No Data Found")
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
                                adapter = AdapterRelatedServiceList(this@ServicesRequestList, mainDataNew,this@ServicesRequestList)
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
                        myToast(context, "Something went wrong")
                        e.printStackTrace()
                        AppProgressBar.hideLoaderDialog()

                    }
                }


                override fun onFailure(call: Call<ModelServiceRet>, t: Throwable) {
                      myToast(context, t.message.toString())
                   // myToast(context, "No Data Found")

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
             tranServ,price
        )
            .enqueue(object : Callback<ModelServiceRet> {
                @SuppressLint("LogNotTimber", "SetTextI18n")
                override fun onResponse(
                    call: Call<ModelServiceRet>, response: Response<ModelServiceRet>
                ) {
                    try {
                        if (response.code() == 404) {
                            myToast(context, "Something went wrong")

                        } else if (response.code() == 500) {
                            myToast(context, "Server Error")
                            AppProgressBar.hideLoaderDialog()

                        } else if (response.body()!!.data.isEmpty()) {
                            myToast(context, "No Data Found")
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
                                adapter = AdapterRelatedServiceList(this@ServicesRequestList, mainDataNew,this@ServicesRequestList)
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
                        myToast(context, "Something went wrong")
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
            foodId,requestId
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

                        } else if (response.body()!!.data.isEmpty()) {
                            myToast(context, "No Data Found")
                            AppProgressBar.hideLoaderDialog()

                        } else {
                            myToast(context,response.body()!!.data)
                            AppProgressBar.hideLoaderDialog()
                            dialog?.dismiss()
                            refresh()




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

    }   private fun apiCallUpdatePrice(
        foodId: String,
        price: String,
     ) {
        AppProgressBar.showLoaderDialog(context)
        ApiClient.apiService.updatePrice(
            sessionManager.idToken.toString(),
            foodId,
            price
        )
            .enqueue(object : Callback<ModelUpdatePrice> {
                @SuppressLint("LogNotTimber", "SetTextI18n")
                override fun onResponse(
                    call: Call<ModelUpdatePrice>, response: Response<ModelUpdatePrice>
                ) {
                    try {
                        if (response.code() == 404) {
                            myToast(context, "Something went wrong")

                        } else if (response.code() == 500) {
                            myToast(context, "Server Error")
                            AppProgressBar.hideLoaderDialog()

                        } else {
                            myToast(context,response.body()!!.message)
                            AppProgressBar.hideLoaderDialog()
                            dialog?.dismiss()
                            refresh()
                        }
                    } catch (e: Exception) {
                        myToast(context, "Something went wrong")
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
        id: String,
        name: String,
        price: String,
        langFrom: String,
        langTo: String,
        tranServ: String,
    ) {
        requestId=id

        when (sessionManager.usertype) {
            "car" -> {
                apiCallRelatedServiceCar(langFrom, langTo, tranServ, price)
            }
            "home" -> {
                apiCallRelatedServiceHome(langFrom, langTo, tranServ, price)
            }
            else -> {
                apiCallRelatedService(langFrom, langTo, tranServ, price)

            }
        }


        AppProgressBar.hideLoaderDialog()
    }

    override fun sendRequest(foodId: String) {
        apiCallSendService(foodId.toString())
     }

    override fun updatePrice(foodId: String,price:String) {
         apiCallUpdatePrice(foodId,price)

    }

    fun refresh() {
        overridePendingTransition(0, 0)
        finish()
        startActivity(intent)
        overridePendingTransition(0, 0)
    }
}