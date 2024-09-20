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
import com.amtech.vendorservices.V.retrofit.ApiClient
import com.amtech.vendorservices.databinding.ActivityCanceledOrdersBinding
import com.amtech.vendorservices.V.sharedpreferences.SessionManager
import com.amtech.vendorservices.V.Order.Model.ModelOrderDetail.Data
import com.amtech.vendorservices.V.Order.Model.ModelOrderDetail.ModelOrderDetail
import org.jitsi.meet.sdk.JitsiMeetActivity
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions
import org.jitsi.meet.sdk.JitsiMeetUserInfo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.MalformedURLException
import java.net.URL

class CanceledOrders : AppCompatActivity(),AdapterCompleteOrder.VideoCall {
    private val binding by lazy {
        ActivityCanceledOrdersBinding.inflate(layoutInflater)
    }
    var count=0
        private val context=this@CanceledOrders
        private lateinit var sessionManager: SessionManager
        private lateinit var mainData: ArrayList<Data>
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
                apiCallAllOrder()

                edtSearch.addTextChangedListener { str ->
                    setRecyclerViewAdapter(mainData.filter {
                        it.id != null && it.id.toString()!!.contains(str.toString(), ignoreCase = true)
                    } as ArrayList<Data>)
                }
            }

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
                            val pendingOrders = mainData.filter { order ->
                                order.order_status == "canceled"
                            }
                            setRecyclerViewAdapter(pendingOrders as ArrayList)


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
        private fun setRecyclerViewAdapter(data: ArrayList<Data>) {
            binding.recyclerView.apply {
                adapter = AdapterCompleteOrder(context, data,this@CanceledOrders)
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
        Dashboard.refreshLanNew=true
    }

}