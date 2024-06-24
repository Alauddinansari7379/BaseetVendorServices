package com.amtech.vendorservices.V.MyTranslotor.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.amtech.vendorservices.R
import com.amtech.vendorservices.V.Dashboard.Dashboard
import com.amtech.vendorservices.V.Helper.AppProgressBar
import com.amtech.vendorservices.V.Helper.myToast
import com.amtech.vendorservices.V.Login.activity.Login
import com.amtech.vendorservices.V.MyTranslotor.Model.ModelMyTra
import com.amtech.vendorservices.V.activity.EditTranslaterInformation
import com.amtech.vendorservices.V.retrofit.ApiClient
import com.amtech.vendorservices.databinding.ActivityProfileBinding
 import com.amtech.vendorservices.V.sharedpreferences.SessionManager
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Profile : AppCompatActivity() {
    private val binding by lazy {
        ActivityProfileBinding.inflate(layoutInflater)
    }
    lateinit var sessionManager: SessionManager
    private val context = this@Profile
    var count=0
    private val PREF_NAME = "MyPrefs"
    private val PREF_USERNAME = "username"
    private val PREF_PASSWORD = "password"
    private val FCM_TOKEN = "fcmtoken"
    private lateinit var sharedPreferences: SharedPreferences
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

            when (sessionManager.usertype) {
                "car" -> {
                    binding.tvTitle.text = resources.getString(R.string.Car_Rental_Information)
                     binding.tvInfo.text = resources.getString(R.string.Car_Rental_Info)
                }
                "home" -> {
                    binding.tvTitle.text =resources.getString(R.string.Home_Rental_Information)
                     binding.tvInfo.text = resources.getString(R.string.Home_Rental_Info)

                }else->{

                }
            }
            btnEdit.setOnClickListener {
                startActivity(Intent(context, EditTranslaterInformation::class.java))
            }
            apiCallGetProfile()
        }
    }

    private fun apiCallGetProfile() {

        //  AppProgressBar.showLoaderDialog(context)
        ApiClient.apiService.getProfile(
            sessionManager.idToken.toString()
        ).enqueue(object :
            Callback<ModelMyTra> {
            @SuppressLint("LogNotTimber", "LongLogTag", "SetTextI18n")
            override fun onResponse(
                call: Call<ModelMyTra>,
                response: Response<ModelMyTra>
            ) {
                try {
                    if (response.code() == 500) {
                        myToast(context, resources.getString(R.string.Server_Error))
                        AppProgressBar.hideLoaderDialog()

                    } else if (response.code() == 401) {
                        // myToast(context, "Unauthorized")
                        myToast(context,  resources.getString(R.string.User_Logged_in_other_Device))
                        sessionManager.logout()
//                        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
//                        if(sessionManager.fcmToken!!.isNotEmpty()){
//                            saveFCM(sessionManager.fcmToken.toString())
//                        }
//                        fcmTokenNew = sharedPreferences.getString(FCM_TOKEN, "").toString()
//
//                        Log.e("FCMNewSession",fcmTokenNew)
                        val intent = Intent(applicationContext, Login::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                        finish()
                        startActivity(intent)
                        AppProgressBar.hideLoaderDialog()

                    } else if (response.code() == 200) {
                        binding.tvName.text = resources.getString(R.string.name_) + response.body()!!.name
                         binding.tvPhone.text =resources.getString(R.string.Phone) + response.body()!!.phone
                        binding.tvAddress.text = resources.getString(R.string.address_) + response.body()!!.address

                        if (response.body()!!.comission != null) {
                            binding.tvDiscount.text =
                                resources.getString(R.string.Discount) + response.body()!!.comission + "%"
                        }
                        if (response.body()!!.comission != null) {
                            binding.tvCommission.text =
                                resources.getString(R.string.Admin_commission) + response.body()!!.comission + "%"
                        }

                        if (response.body()!!.applogo != null) {
                            Picasso.get().load(response.body()!!.applogo.replace("http","https"))
                                .placeholder(R.drawable.user)
                                .error(R.drawable.error_placeholder)
                                .memoryPolicy(MemoryPolicy.NO_CACHE)
                                .into(binding.imgProfile)
                        }

                        if (response.body()!!.applogo != null) {
                            Picasso.get().load(response.body()!!.appcoverlogo.replace("http","https"))
                                .placeholder(R.drawable.user)
                                .error(R.drawable.error_placeholder)
                                .memoryPolicy(MemoryPolicy.NO_CACHE)
                                .into(binding.imgCover)
                        }
//                        for (i in response.body()!!.earning.3){
//                            earning.add(i)
//                        }
                    } else {
                        myToast(context, resources.getString(R.string.Something_went_wrong))

                        AppProgressBar.hideLoaderDialog()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    myToast(context, resources.getString(R.string.Something_went_wrong))
                    AppProgressBar.hideLoaderDialog()

                }
            }

            override fun onFailure(call: Call<ModelMyTra>, t: Throwable) {
                 AppProgressBar.hideLoaderDialog()
                   count++
                   if (count<= 3) {
                       Log.e("count", count.toString())
                       apiCallGetProfile()
                   } else {
                       myToast(context, t.message.toString())
                       AppProgressBar.hideLoaderDialog()

                   }
                   AppProgressBar.hideLoaderDialog()
            }

        })

    }
    override fun onDestroy() {
        super.onDestroy()
        Dashboard.refreshLanNew=true
    }

    private fun refresh() {
        overridePendingTransition(0, 0)
        finish()
        startActivity(intent)
        overridePendingTransition(0, 0)
    }

}