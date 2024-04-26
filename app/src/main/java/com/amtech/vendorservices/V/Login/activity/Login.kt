package com.amtech.vendorservices.V.Login.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.amtech.vendorservices.V.Dashboard.Dashboard
import com.amtech.vendorservices.V.Helper.AppProgressBar
import com.amtech.vendorservices.V.Helper.isOnline
import com.amtech.vendorservices.V.Helper.myToast
import com.amtech.vendorservices.V.Login.model.ModelLogin
import com.amtech.vendorservices.V.MyTranslotor.Model.ModelMyTra
import com.amtech.vendorservices.V.retrofit.ApiClient
import com.amtech.vendorservices.databinding.ActivityLoginBinding
import com.example.hhfoundation.sharedpreferences.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import rezwan.pstu.cse12.youtubeonlinestatus.recievers.NetworkChangeReceiver

class Login : AppCompatActivity() {
    private val context = this@Login
    val binding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }
    var count = 0
    var countlogin = 0
    lateinit var sessionManager: SessionManager

    //    private lateinit var sessionManagerNew: SessionManagerNew
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        sessionManager = SessionManager(context)
        //  sessionManagerNew=SessionManagerNew(context)

        if (sessionManager.isLogin) {
            startActivity(Intent(context, Dashboard::class.java))
            finish()
        }
//         if (sessionManagerNew.email!!.isNotEmpty()){
//             binding.edtEmail.setText(sessionManagerNew.email.toString())
//             binding.edtPassword.setText(sessionManagerNew.password.toString())
//         }
//         Log.e("LoginEmail",sessionManagerNew.email.toString())
//         Log.e("LoginEmail",sessionManagerNew.password.toString())
        with(binding) {
            btnSignIn.setOnClickListener {
                if (edtEmail.text!!.isEmpty()) {
                    edtEmail.error = "Enter Email"
                    edtEmail.requestFocus()
                    return@setOnClickListener
                }
                if (edtPassword.text!!.isEmpty()) {
                    edtPassword.error = "Enter Password"
                    edtPassword.requestFocus()
                    return@setOnClickListener
                }
//                if (checkBox.isChecked){
//                    sessionManagerNew.email=edtEmail.text.toString().trim()
//                    sessionManagerNew.password=edtPassword.text.toString().trim()
//                }
                apiCallLogin()
            }

            checkBox.setOnClickListener {
//                if (checkBox.isChecked){
//                    sessionManagerNew.email=edtEmail.text.toString().trim()
//                    sessionManagerNew.password=edtPassword.text.toString().trim()
//                }
            }
        }

    }

    private fun apiCallLogin() {

        AppProgressBar.showLoaderDialog(this@Login)
        ApiClient.apiService.login(
            binding.edtEmail.text.toString().trim(),
            binding.edtPassword.text.toString().trim(),
        ).enqueue(object :
            Callback<ModelLogin> {
            @SuppressLint("LogNotTimber", "LongLogTag")
            override fun onResponse(
                call: Call<ModelLogin>,
                response: Response<ModelLogin>
            ) {
                try {
                    if (response.code() == 500) {
                        myToast(this@Login, "Server Error")
                        AppProgressBar.hideLoaderDialog()

                    } else if (response.code() == 404) {
                        myToast(this@Login, "Unauthorized")
                        AppProgressBar.hideLoaderDialog()

                    } else if (response.code() == 200) {
                        sessionManager.isLogin = true
                        sessionManager.idToken = "Bearer " + response.body()!!.token
                        apiCallGetProfile()

                        Handler(Looper.getMainLooper()).postDelayed({
                            AppProgressBar.hideLoaderDialog()

                            Log.e("sessionManager.idToken", sessionManager.idToken.toString())
                            Log.e("sessionManager.ionID", sessionManager.ionId.toString())
                            Log.e("sessionManager.group", sessionManager.group.toString())
                            myToast(this@Login, "Login Sucessfully")
                            val intent = Intent(applicationContext, Dashboard::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                            finish()
                            startActivity(intent)

                        }, 300)
                    } else {
                        myToast(this@Login, "Something went wrong")
                        AppProgressBar.hideLoaderDialog()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    myToast(this@Login, "Try Again")
                    AppProgressBar.hideLoaderDialog()

                }
            }

            override fun onFailure(call: Call<ModelLogin>, t: Throwable) {
                myToast(this@Login, "Something went wrong")
                AppProgressBar.hideLoaderDialog()
                countlogin++
                if (countlogin <= 3) {
                    Log.e("count", countlogin.toString())
                    apiCallLogin()
                } else {
                    myToast(this@Login, t.message.toString())
                    AppProgressBar.hideLoaderDialog()
                }
                AppProgressBar.hideLoaderDialog()
            }

        })

    }

    private fun apiCallGetProfile() {

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

                    } else if (response.code() == 200) {
                        sessionManager.usertype = response.body()!!.type
                        sessionManager.customerName = response.body()!!.name
                        sessionManager.phoneNumber = response.body()!!.phone
                        sessionManager.email = response.body()!!.email
                        sessionManager.profilePic = response.body()!!.applogo
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
                AppProgressBar.hideLoaderDialog()
                count++
                if (count <= 3) {
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


    override fun onStart() {
        super.onStart()
        if (isOnline(context)) {
        } else {
            val changeReceiver = NetworkChangeReceiver(context)
            changeReceiver.build()

        }
    }

}