package com.amtech.vendorservices.V.Order.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.amtech.vendorservices.R
import com.amtech.vendorservices.V.Dashboard.Dashboard
import com.amtech.vendorservices.V.Helper.AppProgressBar
import com.amtech.vendorservices.V.Helper.myToast
import com.amtech.vendorservices.V.Order.Adapter.AdapterCompleteOrder
import com.amtech.vendorservices.V.Order.Model.DataX
import com.amtech.vendorservices.V.Order.Model.ModelComplete
import com.amtech.vendorservices.V.Order.Model.ModelOrderDetail.ModelOrderDetail
import com.amtech.vendorservices.V.retrofit.ApiClient
import com.amtech.vendorservices.databinding.ActivityConfirmOrdersBinding
import com.amtech.vendorservices.V.sharedpreferences.SessionManager
import com.amtech.vendorservices.V.Order.Model.ModelOrderDetail.Data
import org.jitsi.meet.sdk.JitsiMeetActivity
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions
import org.jitsi.meet.sdk.JitsiMeetUserInfo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.MalformedURLException
import java.net.URL

class ConfirmOrders : AppCompatActivity(),AdapterCompleteOrder.VideoCall {
    private val binding by lazy {
        ActivityConfirmOrdersBinding.inflate(layoutInflater)
    }
    var count = 0
    var type = ""
    val context = this@ConfirmOrders
    private lateinit var sessionManager: SessionManager
    private lateinit var mainData: ArrayList<Data>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        sessionManager = SessionManager(context)

        Dashboard().languageSetting(this, sessionManager.selectedLanguage.toString())
        type = intent.getStringExtra("Type").toString()
        if (type=="Pending"){
            binding.tvTitleNew.text=getString(R.string.pending)
        }else{
            binding.tvTitleNew.text=getString(R.string.confirmed_orders)

        }

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

            //apiCallCompleteOrder()
            apiCallAllOrder()
            edtSearch.addTextChangedListener { str ->
                setRecyclerViewAdapter(mainData.filter {
                    it.id != null && it.id.toString()!!.contains(str.toString(), ignoreCase = true)
                } as ArrayList<Data>)
            }
        }


    }

    override fun upload(toString: String) {
        TODO("Not yet implemented")
    }
    private fun apiCallAllOrder() {
        AppProgressBar.showLoaderDialog(context)
        ApiClient.apiService.allOrders(
            sessionManager.idToken.toString(),
        )
            .enqueue(object : Callback<ModelOrderDetail> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelOrderDetail>, response: Response<ModelOrderDetail>
                ) {
                    try {
                        if (response.code() == 404) {
                            myToast(context, resources.getString(R.string.Something_went_wrong))
                            AppProgressBar.hideLoaderDialog()

                        } else if  (response.code() == 500) {
                            myToast(context, resources.getString(R.string.Server_Error))
                            AppProgressBar.hideLoaderDialog()

                        } else {
                             val mainData = response.body()?.data ?: emptyList()

                            if (type=="Pending"){
                                val pendingOrders = mainData.filter { order ->
                                    order.order_status == "pending"
                            }
                                setRecyclerViewAdapter(pendingOrders as ArrayList)
                            }else{
                                val pendingOrders = mainData.filter { order ->
                                    order.order_status == "confirmed"
                                }
                                setRecyclerViewAdapter(pendingOrders as ArrayList)
                            }

                            AppProgressBar.hideLoaderDialog()
                        }
                    } catch (e: Exception) {
                        myToast(context, resources.getString(R.string.Something_went_wrong))
                        e.printStackTrace()
                        AppProgressBar.hideLoaderDialog()

                    }
                }

                override fun onFailure(call: Call<ModelOrderDetail>, t: Throwable) {
                    AppProgressBar.hideLoaderDialog()
                    count++
                    if (count <= 3) {
                        Log.e("count", count.toString())
                        apiCallAllOrder()
                    } else {
                        myToast(context, t.message.toString())
                        AppProgressBar.hideLoaderDialog()

                    }
                }
            })
    }

/*
    private fun apiCallCompleteOrder() {
        AppProgressBar.showLoaderDialog(context)
        AppProgressBar.showLoaderDialog(context)
        ApiClient.apiService.cancelledOrders(
            sessionManager.idToken.toString(),
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

                        } else if (response.code() == 500) {
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
                    myToast(context, resources.getString(R.string.Something_went_wrong))
                    AppProgressBar.hideLoaderDialog()
                    count++
                    if (count <= 3) {
                        Log.e("count", count.toString())
                        apiCallCompleteOrder()
                    } else {
                        myToast(this@ConfirmOrders, t.message.toString())
                        AppProgressBar.hideLoaderDialog()

                    }
                    AppProgressBar.hideLoaderDialog()
                }

            })

    }
*/

    private fun setRecyclerViewAdapter(data: ArrayList<Data>) {
        binding.recyclerView.apply {
            adapter = AdapterCompleteOrder(context, data,this@ConfirmOrders)
            AppProgressBar.hideLoaderDialog()

        }
    }
    override fun videoCall(toString: String) {
        val jitsiMeetUserInfo = JitsiMeetUserInfo()
        jitsiMeetUserInfo.displayName = sessionManager.customerName
        jitsiMeetUserInfo.email = sessionManager.email
        try {
            val defaultOptions: JitsiMeetConferenceOptions = JitsiMeetConferenceOptions.Builder()
                .setServerURL(URL("https://ka-nnect.com/"))
                .setRoom(toString)
                .setAudioMuted(false)
                .setVideoMuted(true)
                .setAudioOnly(false)
                .setUserInfo(jitsiMeetUserInfo)
                .setConfigOverride("enableInsecureRoomNameWarning", false)
                .setFeatureFlag("readOnlyName", true)
                .setFeatureFlag("prejoinpage.enabled", false)
                //  .setFeatureFlag("lobby-mode.enabled", false)
                // .setToken("123") // Set the meeting password
                //.setFeatureFlag("autoKnockLobby", false) // Disable lobby mode
                //.setFeatureFlag("disableModeratorIndicator", false)
                //.setFeatureFlag("chat.enabled",false)
                .setConfigOverride("requireDisplayName", true)
                .build()
            JitsiMeetActivity.launch(context, defaultOptions)

            //  startActivity(Intent(requireContext(),Rating::class.java))
        } catch (e: MalformedURLException) {
            e.printStackTrace();
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
        Dashboard.refreshLanNew = true
    }
}