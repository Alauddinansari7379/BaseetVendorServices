package com.amtech.vendorservices.V.MyTranslotor.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.amtech.vendorservices.R
import com.amtech.vendorservices.V.Helper.AppProgressBar
import com.amtech.vendorservices.V.Helper.myToast
import com.amtech.vendorservices.V.Login.activity.Login
import com.amtech.vendorservices.V.MyTranslotor.Model.ModelMyTra
import com.amtech.vendorservices.V.activity.EditTranslaterInformation
import com.amtech.vendorservices.V.retrofit.ApiClient
import com.amtech.vendorservices.databinding.ActivityProfileBinding
 import com.example.hhfoundation.sharedpreferences.SessionManager
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        sessionManager = SessionManager(context)
        with(binding) {
            imgBack.setOnClickListener {
                onBackPressed()
            }

            when (sessionManager.usertype) {
                "car" -> {
                    binding.tvTitle.text = "Car Rental Information "
                    binding.tvInfo.text = "Car Rental Info"
                }
                "home" -> {
                    binding.tvTitle.text = "Home Rental Information"
                    binding.tvInfo.text = "Home Rental Info"

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
                        myToast(context, "Server Error")
                        AppProgressBar.hideLoaderDialog()

                    } else if (response.code() == 401) {
                        // myToast(context, "Unauthorized")
                        myToast(context, "User Logged in other Device")
                        sessionManager.logout()
                        val intent = Intent(applicationContext, Login::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                        finish()
                        startActivity(intent)
                        AppProgressBar.hideLoaderDialog()

                    } else if (response.code() == 200) {
                        binding.tvName.text =
                            "Name : " + response.body()!!.name
                        binding.tvPhone.text = "Phone : " + response.body()!!.phone
                        binding.tvAddress.text = "Address : " + response.body()!!.address

                        if (response.body()!!.comission != null) {
                            binding.tvDiscount.text =
                                "Discount : " + response.body()!!.comission + "%"
                        }
                        if (response.body()!!.comission != null) {
                            binding.tvCommission.text =
                                "Admin commission : " + response.body()!!.comission + "%"
                        }

                        if (response.body()!!.applogo != null) {
                            Picasso.get().load(response.body()!!.applogo.replace("http","https"))
                                .placeholder(R.drawable.user)
                                .error(R.drawable.error_placeholder)
                                .memoryPolicy(MemoryPolicy.NO_CACHE)
                                .into(binding.imgProfile)
                        }
//                        for (i in response.body()!!.earning.3){
//                            earning.add(i)
//                        }
                    } else {
                        myToast(context, "Something went wrong")

                        AppProgressBar.hideLoaderDialog()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    myToast(context, "Something went wrong")
                    AppProgressBar.hideLoaderDialog()

                }
            }

            override fun onFailure(call: Call<ModelMyTra>, t: Throwable) {
                myToast(context, "Something went wrong")
                AppProgressBar.hideLoaderDialog()
                /*   count++
                   if (count<= 3) {
                       Log.e("count", count.toString())
                       apiCallDashboard()
                   } else {
                       myToast(context, t.message.toString())
                       AppProgressBar.hideLoaderDialog()

                   }
                   AppProgressBar.hideLoaderDialog()*/
            }

        })

    }

}