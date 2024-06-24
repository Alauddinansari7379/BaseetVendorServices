package com.amtech.vendorservices.V.Login.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import com.amtech.vendorservices.R
import com.amtech.vendorservices.V.Dashboard.Dashboard
import com.amtech.vendorservices.V.Dashboard.Dashboard.Companion.refreshLan
import com.amtech.vendorservices.V.Helper.AppProgressBar
import com.amtech.vendorservices.V.Helper.isOnline
import com.amtech.vendorservices.V.Helper.myToast
import com.amtech.vendorservices.V.Login.model.ModelLogin
import com.amtech.vendorservices.V.MyTranslotor.Model.ModelMyTra
import com.amtech.vendorservices.V.retrofit.ApiClient
import com.amtech.vendorservices.V.sharedpreferences.SessionManager
import com.amtech.vendorservices.databinding.ActivityLoginBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import rezwan.pstu.cse12.youtubeonlinestatus.recievers.NetworkChangeReceiver
import java.util.Locale

class Login : AppCompatActivity() {
    private val context = this@Login
    val binding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }
    var count = 0
    private var fcmTokenNew = ""
    var countlogin = 0
    lateinit var sessionManager: SessionManager
    private val PREF_NAME = "MyPrefs"
    private val PREF_USERNAME = "username"
    private val PREF_PASSWORD = "password"
    private val FCM_TOKEN = "fcmtoken"
    private lateinit var sharedPreferences: SharedPreferences
    private var dialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        sessionManager = SessionManager(context)
        Log.e("sessionManager.fcmToken", sessionManager.fcmToken.toString())
        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)


        //  languageDialog()

        if (sessionManager.selectedLanguage.isNullOrEmpty()) {
            languageDialog()
        } else {
            if (refreshLan) {
                refresh()
                refreshLan = false
            }
            langaugeSetting()
//            binding.edtEmail.gravity = Gravity.END
//            binding.edtPassword.gravity = Gravity.END
        }
//9876543211
        if (sessionManager.isLogin) {
            startActivity(Intent(context, Dashboard::class.java))
            finish()
        }

//         Log.e("LoginEmail",sessionManagerNew.email.toString())
//         Log.e("LoginEmail",sessionManagerNew.password.toString())
        with(binding) {
            btnSignIn.setOnClickListener {
                if (edtEmail.text!!.isEmpty()) {
                    edtEmail.error = (resources.getString(R.string.enter_email))
                    edtEmail.requestFocus()
                    return@setOnClickListener
                }
                if (edtPassword.text!!.isEmpty()) {
                    edtPassword.error = (resources.getString(R.string.enter_password))
                    edtPassword.requestFocus()
                    return@setOnClickListener
                }

                if (sessionManager.fcmToken!!.isNotEmpty()) {
                    saveFCM(sessionManager.fcmToken.toString())
                }
                fcmTokenNew = sharedPreferences.getString(FCM_TOKEN, "").toString()

                Log.e("FCMNewSession", fcmTokenNew)

                apiCallLogin()
            }
//            if (sessionManager.selectedLanguage.isNullOrEmpty()) {
//                languageDialog()
//            } else {
//                val locale: Locale = Locale(sessionManager.selectedLanguage!!)
//                Locale.setDefault(locale)
//                val config: Configuration = Configuration()
//                config.locale = locale
//                resources.updateConfiguration(config, resources.displayMetrics)
//            }
            //  sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
//            val savedUsername = sharedPreferences.getString(PREF_USERNAME, "")
//            val savedPassword = sharedPreferences.getString(PREF_PASSWORD, "")
//
//            if (savedUsername!!.isNotEmpty()) {
//                binding.edtEmail.setText(savedUsername.toString())
//                binding.edtPassword.setText(savedPassword.toString())
//                // checkBox.isChecked=true
//            }
//
//
//            // Set saved username and password if they exist
//            edtEmail.setText(savedUsername)
//            edtPassword.setText(savedPassword)

            checkBox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    // Save username and password when checkbox is checked
                    saveCredentials(
                        edtEmail.text.toString(),
                        edtPassword.text.toString()
                    )
                }
//                if (checkBox.isChecked){
//                    sessionManagerNew.email=edtEmail.text.toString().trim()
//                    sessionManagerNew.password=edtPassword.text.toString().trim()
//                }
            }
        }

    }



    private fun langaugeSetting() {
        val locale: Locale = Locale(sessionManager.selectedLanguage!!)
        Locale.setDefault(locale)
        val config: Configuration = Configuration()
        config.locale = locale
        resources.updateConfiguration(config, resources.displayMetrics)
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

    private fun apiCallLogin() {

        AppProgressBar.showLoaderDialog(this@Login)
        ApiClient.apiService.login(
            binding.edtEmail.text.toString().trim(),
            binding.edtPassword.text.toString().trim(),
            fcmTokenNew, "android"
        ).enqueue(object :
            Callback<ModelLogin> {
            @SuppressLint("LogNotTimber", "LongLogTag")
            override fun onResponse(
                call: Call<ModelLogin>,
                response: Response<ModelLogin>
            ) {
                try {
                    if (response.code() == 500) {
                        myToast(this@Login, resources.getString(R.string.Server_Error))
                        AppProgressBar.hideLoaderDialog()

                    } else if (response.code() == 401) {
                        myToast(this@Login, resources.getString(R.string.Unauthorized))
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
                            myToast(this@Login, resources.getString(R.string.Login_Sucessfully))
                            val intent = Intent(applicationContext, Dashboard::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                            finish()
                            startActivity(intent)

                        }, 300)
                    } else {
                        myToast(this@Login, resources.getString(R.string.Something_went_wrong))
                        AppProgressBar.hideLoaderDialog()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    myToast(this@Login, "Try Again")
                    AppProgressBar.hideLoaderDialog()

                }
            }

            override fun onFailure(call: Call<ModelLogin>, t: Throwable) {
                myToast(this@Login, resources.getString(R.string.Something_went_wrong))
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
                        myToast(this@Login, resources.getString(R.string.Server_Error))
                        AppProgressBar.hideLoaderDialog()

                    } else if (response.code() == 200) {
                        sessionManager.usertype = response.body()!!.type
                        sessionManager.customerName = response.body()!!.name
                        sessionManager.phoneNumber = response.body()!!.phone
                        sessionManager.email = response.body()!!.email
                        sessionManager.profilePic = response.body()!!.applogo
                    } else {
                        myToast(this@Login, resources.getString(R.string.Something_went_wrong))
                        AppProgressBar.hideLoaderDialog()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    myToast(this@Login, resources.getString(R.string.Something_went_wrong))
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


    private fun saveCredentials(username: String, password: String) {
        val editor = sharedPreferences.edit()
        editor.putString(PREF_USERNAME, username)
        editor.putString(PREF_PASSWORD, password)
        editor.apply()
    }

    private fun saveFCM(fcmToken: String) {
        val editor = sharedPreferences.edit()
        editor.putString(FCM_TOKEN, fcmToken)
        editor.apply()
    }

    private fun languageDialog() {
        val view = layoutInflater.inflate(R.layout.dialog_langauge, null)
        dialog = Dialog(context)


        val radioEnglish = view!!.findViewById<RadioButton>(R.id.radioEnglish)
        val radioArabic = view!!.findViewById<RadioButton>(R.id.radioArabic)


        radioEnglish.setOnCheckedChangeListener { _, _ ->
            val languageToLoad = "en"
            val locale: Locale = Locale(languageToLoad)
            Locale.setDefault(locale)
            val config: Configuration = Configuration()
            config.locale = locale
            resources.updateConfiguration(config, resources.displayMetrics)
            sessionManager.selectedLanguage = "en"
            dialog?.dismiss()
            refresh()

        }
        radioArabic.setOnCheckedChangeListener { _, _ ->
            val languageToLoad = "ar"
            val locale: Locale = Locale(languageToLoad)
            Locale.setDefault(locale)
            val config: Configuration = Configuration()
            config.locale = locale
            resources.updateConfiguration(config, resources.displayMetrics)
            sessionManager.selectedLanguage = "ar"

            dialog?.dismiss()
            refresh()
        }

        dialog = Dialog(context)
        if (view.parent != null) {
            (view.parent as ViewGroup).removeView(view) // <- fix
        }
        dialog!!.setContentView(view)
        dialog?.setCancelable(false)

        dialog?.show()


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
