package com.amtech.vendorservices.V.Dashboard

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.app.NotificationManager
import android.content.ComponentName
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.content.res.Resources
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import cn.pedant.SweetAlert.SweetAlertDialog
import com.amtech.vendorservices.R
import com.amtech.vendorservices.V.Dashboard.model.ModelDashTra.ModelDashTra
import com.amtech.vendorservices.V.Dashboard.model.ModelDashboard
import com.amtech.vendorservices.V.Dashboard.model.ModelSpinner
import com.amtech.vendorservices.V.Helper.AppProgressBar
import com.amtech.vendorservices.V.Helper.isOnline
import com.amtech.vendorservices.V.Helper.myToast
import com.amtech.vendorservices.V.Login.activity.Login
import com.amtech.vendorservices.V.MyTranslotor.Model.ModelMyTra
import com.amtech.vendorservices.V.MyTranslotor.activity.Profile
import com.amtech.vendorservices.V.Order.Model.ModelOrderDetail.ModelOrderDetail
import com.amtech.vendorservices.V.Order.activity.AllOrders
import com.amtech.vendorservices.V.Order.activity.CanceledOrders
import com.amtech.vendorservices.V.Order.activity.ConfirmOrders
import com.amtech.vendorservices.V.Order.activity.DeliveredOrders
import com.amtech.vendorservices.V.Order.activity.OrderDetails
import com.amtech.vendorservices.V.Order.activity.RefundedOrders
import com.amtech.vendorservices.V.Order.activity.ServicesRequestList
import com.amtech.vendorservices.V.TranslatorServices.activity.AddNewTranslatorServices
import com.amtech.vendorservices.V.TranslatorServices.activity.TranslaterServiceList
import com.amtech.vendorservices.V.activity.CustomerReviews
import com.amtech.vendorservices.V.activity.ServiceUpdate
import com.amtech.vendorservices.V.activity.TranslaterSetup
import com.amtech.vendorservices.V.activity.TranslatorBankInfo
import com.amtech.vendorservices.V.activity.TranslatorWallet
import com.amtech.vendorservices.V.retrofit.ApiClient
import com.amtech.vendorservices.V.sharedpreferences.SessionManager
import com.amtech.vendorservices.databinding.ActivityDashboardBinding
import com.example.easywaylocation.EasyWayLocation
import com.example.easywaylocation.GetLocationDetail
import com.example.easywaylocation.Listener
import com.example.easywaylocation.LocationData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import com.squareup.picasso.Picasso
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import rezwan.pstu.cse12.youtubeonlinestatus.recievers.NetworkChangeReceiver
import java.io.IOException
import java.util.Locale

class Dashboard : AppCompatActivity(), Listener, LocationData.AddressCallBack {
    private val context = this@Dashboard
    private val binding by lazy {
        ActivityDashboardBinding.inflate(layoutInflater)
    }
    private lateinit var drawerLayout: DrawerLayout
    private var statisticsList = ArrayList<ModelSpinner>()
    lateinit var sessionManager: SessionManager
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private lateinit var easyWayLocation: EasyWayLocation
    private lateinit var getLocationDetail: GetLocationDetail
    private lateinit var lm: LocationManager
    private val REQUEST_CODE = 100
    private var count = 1
    private var count1 = 1
    private var currentAddress = ""
    private var postalCodeNew = ""
    val commission = ArrayList<Int>()
    private val NOTIFICATION_PERMISSION_CODE = 123
    private var dialog: Dialog? = null

    private lateinit var firebaseApp: FirebaseApp

    private val notificationManager: NotificationManager by lazy {
        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        sessionManager = SessionManager(context)
        Dashboard().languageSetting(context, sessionManager.selectedLanguage.toString())

        if (sessionManager.selectedLanguage == "en") {
            binding.imgLan.background = ContextCompat.getDrawable(context, R.drawable.arabic_text)
        } else {
            binding.imgLan.background = ContextCompat.getDrawable(context, R.drawable.english_text)
        }

        binding.imgLan.setOnClickListener {
            if (sessionManager.selectedLanguage == "en") {
                sessionManager.selectedLanguage = "ar"
                Dashboard().languageSetting(context, sessionManager.selectedLanguage.toString())
                refreshNew()
            } else {
                sessionManager.selectedLanguage = "en"
                Dashboard().languageSetting(context, sessionManager.selectedLanguage.toString())
                refreshNew()

            }
        }
        apiCallAllOrderCount()
//        binding.imgLan.setOnClickListener {
//            languageDialog()
//        }
//      FirebaseApp.initializeApp(context)!!
//
//       // firebaseApp.name.toString()

        // FirebaseInstanceId.getInstance().getToken();

        requestNotificationPermission()
        //   getCurrentFCMToken()

        when {
            ContextCompat.checkSelfPermission(
                this, Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED -> {
                // You can use the API that requires the permission.
                Log.e("Notification", "onCreate: PERMISSION GRANTED")
                // sendNotification(this)
            }

            shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS) -> {
                Snackbar.make(
                    findViewById<View>(android.R.id.content).rootView,
                    "Notification blocked",
                    Snackbar.LENGTH_LONG
                ).setAction("Settings") {
                    // Responds to click on the action
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    val uri: Uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                }.show()
            }

            else -> {
                // The registered ActivityResultCallback gets the result of this request
                requestPermissionLauncher.launch(
                    Manifest.permission.POST_NOTIFICATIONS
                )
            }
        }

        sessionManager.imageURL = "https://baseet.thedemostore.in/storage/app/public/product/"

        Log.i("AuthToken", sessionManager.idToken.toString())
        apiCallGetProfile()


        when (sessionManager.usertype) {
            "car" -> {
                binding.includedrawar1.tvService.text = resources.getString(R.string.Car_Servicet)

                binding.includedrawar1.tvConfig.text =
                    resources.getString(R.string.Car_Rental_Config)
                binding.includedrawar1.tvMy.text = resources.getString(R.string.My_Car_Rental)
            }
//                "translator"->{
//                    binding.includedrawar1.tvService.text="Translator Service"
//                    binding.includedrawar1.tvConfig.text="Translator Config"
//                    binding.includedrawar1.tvMy.text="Translator Rental"
//                }
            "home" -> {
                binding.includedrawar1.tvService.text = resources.getString(R.string.Home_Service)
                binding.includedrawar1.tvConfig.text =
                    resources.getString(R.string.Home_Rental_Config)
                binding.includedrawar1.tvMy.text = resources.getString(R.string.My_Home_Rental)
            }

            else -> {

            }

        }
        binding.includedrawar1.tvName.text = sessionManager.customerName.toString()
        try {
            if (sessionManager.profilePic != null) {
                Picasso.get().load(sessionManager.profilePic!!.replace("http", "https"))
                    .placeholder(R.drawable.user)
                    .error(R.drawable.user_logo)
                    .into(binding.includedrawar1.imgProfile)
                Log.e("sessionManager.profilePicIn", sessionManager.profilePic.toString())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
//        Glide.with(context).load("xvdfx")
//            .into()


        binding.imgBack.setOnClickListener {
            binding.drawerlayout1.openDrawer(GravityCompat.START)
            binding.includedrawar1.tvDashboard.setOnClickListener {
                drawerLayout.closeDrawer(GravityCompat.START)
            }




            binding.includedrawar1.layoutServiceRequest.setOnClickListener {
                startActivity(Intent(this, ServicesRequestList::class.java))
                drawerLayout.closeDrawer(GravityCompat.START)
            }

            binding.includedrawar1.layoutConfirmed.setOnClickListener {
                val intent = Intent(this@Dashboard, ConfirmOrders::class.java)
                intent.putExtra("Type", "Confirm")
                startActivity(intent)
                drawerLayout.closeDrawer(GravityCompat.START)
            }

            binding.includedrawar1.layoutPending.setOnClickListener {
                val intent = Intent(this@Dashboard, ConfirmOrders::class.java)
                intent.putExtra("Type", "Pending")
                startActivity(intent)
                drawerLayout.closeDrawer(GravityCompat.START)
            }

            binding.includedrawar1.layoutCompleted.setOnClickListener {
                startActivity(Intent(this, DeliveredOrders::class.java))
                drawerLayout.closeDrawer(GravityCompat.START)
            }


            binding.includedrawar1.layoutCanceled.setOnClickListener {
                startActivity(Intent(this, CanceledOrders::class.java))
                drawerLayout.closeDrawer(GravityCompat.START)
            }

            binding.includedrawar1.layoutRefunded.setOnClickListener {
                startActivity(Intent(this, RefundedOrders::class.java))
                drawerLayout.closeDrawer(GravityCompat.START)
            }

            binding.includedrawar1.layoutAll.setOnClickListener {
                startActivity(Intent(this, AllOrders::class.java))
                drawerLayout.closeDrawer(GravityCompat.START)
            }

            binding.includedrawar1.layoutAddNew.setOnClickListener {
                startActivity(Intent(this, AddNewTranslatorServices::class.java))
                drawerLayout.closeDrawer(GravityCompat.START)
            }

            binding.includedrawar1.layoutList.setOnClickListener {
                startActivity(Intent(this, TranslaterServiceList::class.java))
                drawerLayout.closeDrawer(GravityCompat.START)
            }

            binding.includedrawar1.myTranslator.setOnClickListener {
                startActivity(Intent(this, TranslaterSetup::class.java))
                drawerLayout.closeDrawer(GravityCompat.START)
            }

            binding.includedrawar1.bankInfo.setOnClickListener {
                startActivity(Intent(this, TranslatorBankInfo::class.java))
                drawerLayout.closeDrawer(GravityCompat.START)
            }


            binding.includedrawar1.Reviews.setOnClickListener {
                startActivity(Intent(this, CustomerReviews::class.java))
                drawerLayout.closeDrawer(GravityCompat.START)
            }

            binding.includedrawar1.layoutAdvisedAttach.setOnClickListener {
//                startActivity(Intent(this, LabReport::class.java))
                drawerLayout.closeDrawer(GravityCompat.START)
            }
            binding.includedrawar1.layoutAdvisedLabIn.setOnClickListener {
//                startActivity(Intent(this, LabInvestigations::class.java))
                drawerLayout.closeDrawer(GravityCompat.START)
            }
            binding.includedrawar1.layoutReferralDoc.setOnClickListener {
                //           val intent = Intent(this@Dashboard, ReferralDocHis::class.java)
                intent.putExtra("History", "Doc")
//                startActivity(intent)
                drawerLayout.closeDrawer(GravityCompat.START)
            }

            binding.includedrawar1.layoutReferralDischarge.setOnClickListener {
                //     val intent = Intent(this@Dashboard, ReferralDocHis::class.java)
                intent.putExtra("History", "Dic")
//                startActivity(intent)
                drawerLayout.closeDrawer(GravityCompat.START)
            }

            binding.includedrawar1.layoutDaliVital.setOnClickListener {
//                startActivity(Intent(this, DailyVitalUpdate::class.java))
                drawerLayout.closeDrawer(GravityCompat.START)
            }
            binding.includedrawar1.tvLogout.setOnClickListener {
                SweetAlertDialog(
                    this@Dashboard,
                    SweetAlertDialog.WARNING_TYPE
                ).setTitleText(resources.getString(R.string.Are_you_sure_want_to_Logout))
                    .setCancelText(resources.getString(R.string.No))
                    .setConfirmText(resources.getString(R.string.Yes)).showCancelButton(true)
                    .setConfirmClickListener { sDialog ->
                        sDialog.cancel()
                        sessionManager.logout()
                        // logOut()
                        val intent = Intent(applicationContext, Login::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                        finish()
                        startActivity(intent)
                    }.setCancelClickListener { sDialog ->
                        sDialog.cancel()
                    }.show()
                drawerLayout.closeDrawer(GravityCompat.START)
            }
            binding.includedrawar1.TrasnlatorService.setOnClickListener {
                if (binding.includedrawar1.PreventiveSLayout.visibility == View.VISIBLE) {
                    binding.includedrawar1.PreventiveSLayout.visibility = View.GONE
//                    binding.includedrawar1.reportLayout.setBackgroundColor(resources.getColor(R.color.white))
//                    binding.includedrawar1.Report.setBackgroundColor(resources.getColor(R.color.white))
                    binding.includedrawar1.reportArrowPS.setImageResource(R.drawable.baseline_add_24)
                } else {
                    binding.includedrawar1.PreventiveSLayout.visibility = View.VISIBLE
                    // binding.includedrawar1.PreventiveScreening.setBackgroundColor(resources.getColor(R.color.gray))
                    binding.includedrawar1.PreventiveSLayout.setBackgroundColor(resources.getColor(R.color.main_color))
                    binding.includedrawar1.reportArrowPS.setImageResource(R.drawable.baseline_remove_24)
                }

                if (binding.includedrawar1.orderListLayout.visibility == View.VISIBLE) {
                    binding.includedrawar1.orderListLayout.visibility = View.GONE
                    binding.includedrawar1.orderArrowPs.setImageResource(R.drawable.baseline_add_24)
                }
                if (binding.includedrawar1.ClinicalMLayout.visibility == View.VISIBLE) {
                    binding.includedrawar1.ClinicalMLayout.visibility = View.GONE
                    binding.includedrawar1.reportArrowCM.setImageResource(R.drawable.baseline_add_24)
                }
                if (binding.includedrawar1.RefferalsAFULayout.visibility == View.VISIBLE) {
                    binding.includedrawar1.RefferalsAFULayout.visibility = View.GONE
                    binding.includedrawar1.reportArrowRAFU.setImageResource(R.drawable.baseline_add_24)
                }
                if (binding.includedrawar1.LabReportsLayout.visibility == View.VISIBLE) {
                    binding.includedrawar1.LabReportsLayout.visibility = View.GONE
                    binding.includedrawar1.reportArrowLabR.setImageResource(R.drawable.baseline_add_24)
                }
                if (binding.includedrawar1.LabTestsLayout.visibility == View.VISIBLE) {
                    binding.includedrawar1.LabTestsLayout.visibility = View.GONE
                    binding.includedrawar1.reportArrowLabT.setImageResource(R.drawable.baseline_add_24)
                }

            }

            binding.includedrawar1.Order.setOnClickListener {
                if (binding.includedrawar1.orderListLayout.visibility == View.VISIBLE) {
                    binding.includedrawar1.orderListLayout.visibility = View.GONE
                    binding.includedrawar1.orderArrowPs.setImageResource(R.drawable.baseline_add_24)
                } else {
                    binding.includedrawar1.orderListLayout.visibility = View.VISIBLE
                    binding.includedrawar1.orderListLayout.setBackgroundColor(resources.getColor(R.color.main_color))
                    binding.includedrawar1.orderArrowPs.setImageResource(R.drawable.baseline_remove_24)
                }
                if (binding.includedrawar1.PreventiveSLayout.visibility == View.VISIBLE) {
                    binding.includedrawar1.PreventiveSLayout.visibility = View.GONE
                    binding.includedrawar1.reportArrowPS.setImageResource(R.drawable.baseline_add_24)
                }
            }
            binding.includedrawar1.transalaterConfig.setOnClickListener {
                startActivity(Intent(this, ServiceUpdate::class.java))
                drawerLayout.closeDrawer(GravityCompat.START)
//                if (binding.includedrawar1.ClinicalMLayout.visibility == View.VISIBLE) {
//                    binding.includedrawar1.ClinicalMLayout.visibility = View.GONE
//                    binding.includedrawar1.reportArrowCM.setImageResource(R.drawable.baseline_add_24)
//                } else {
//                    binding.includedrawar1.ClinicalMLayout.visibility = View.VISIBLE
//                    binding.includedrawar1.ClinicalMLayout.setBackgroundColor(resources.getColor(R.color.main_color))
//                    binding.includedrawar1.reportArrowCM.setImageResource(R.drawable.baseline_remove_24)
//                }
//                if (binding.includedrawar1.PreventiveSLayout.visibility == View.VISIBLE) {
//                    binding.includedrawar1.PreventiveSLayout.visibility = View.GONE
//                    binding.includedrawar1.reportArrowPS.setImageResource(R.drawable.baseline_add_24)
//                }
//
//                if (binding.includedrawar1.LabDoctor.visibility == View.VISIBLE) {
//                    binding.includedrawar1.LabDoctor.visibility = View.GONE
//                    binding.includedrawar1.DoctorArrowPS.setImageResource(R.drawable.baseline_add_24)
//                }
//
//                if (binding.includedrawar1.RefferalsAFULayout.visibility == View.VISIBLE) {
//                    binding.includedrawar1.RefferalsAFULayout.visibility = View.GONE
//                    binding.includedrawar1.reportArrowRAFU.setImageResource(R.drawable.baseline_add_24)
//                }
//                if (binding.includedrawar1.LabReportsLayout.visibility == View.VISIBLE) {
//                    binding.includedrawar1.LabReportsLayout.visibility = View.GONE
//                    binding.includedrawar1.reportArrowLabR.setImageResource(R.drawable.baseline_add_24)
//                }
//                if (binding.includedrawar1.LabTestsLayout.visibility == View.VISIBLE) {
//                    binding.includedrawar1.LabTestsLayout.visibility = View.GONE
//                    binding.includedrawar1.reportArrowLabT.setImageResource(R.drawable.baseline_add_24)
//                }
//                if (binding.includedrawar1.PreventiveSLayout.visibility == View.VISIBLE) {
//                    binding.includedrawar1.PreventiveSLayout.visibility = View.GONE
//                    binding.includedrawar1.reportArrowPS.setImageResource(R.drawable.baseline_add_24)
//                }
            }

            binding.includedrawar1.myTranslator.setOnClickListener {
                startActivity(Intent(this, Profile::class.java))
                drawerLayout.closeDrawer(GravityCompat.START)
//                if (binding.includedrawar1.RefferalsAFULayout.visibility == View.VISIBLE) {
//                    binding.includedrawar1.RefferalsAFULayout.visibility = View.GONE
//                    binding.includedrawar1.reportArrowRAFU.setImageResource(R.drawable.baseline_add_24)
//                } else {
//                    binding.includedrawar1.RefferalsAFULayout.visibility = View.VISIBLE
//                    binding.includedrawar1.RefferalsAFULayout.setBackgroundColor(
//                        resources.getColor(
//                            R.color.main_color
//                        )
//                    )
//                    binding.includedrawar1.reportArrowRAFU.setImageResource(R.drawable.baseline_remove_24)
//                }
//
//                if (binding.includedrawar1.LabDoctor.visibility == View.VISIBLE) {
//                    binding.includedrawar1.LabDoctor.visibility = View.GONE
//                    binding.includedrawar1.DoctorArrowPS.setImageResource(R.drawable.baseline_add_24)
//                }
//                if (binding.includedrawar1.ClinicalMLayout.visibility == View.VISIBLE) {
//                    binding.includedrawar1.ClinicalMLayout.visibility = View.GONE
//                    binding.includedrawar1.reportArrowCM.setImageResource(R.drawable.baseline_add_24)
//                }
//
//                if (binding.includedrawar1.LabReportsLayout.visibility == View.VISIBLE) {
//                    binding.includedrawar1.LabReportsLayout.visibility = View.GONE
//                    binding.includedrawar1.reportArrowLabR.setImageResource(R.drawable.baseline_add_24)
//                }
//                if (binding.includedrawar1.LabTestsLayout.visibility == View.VISIBLE) {
//                    binding.includedrawar1.LabTestsLayout.visibility = View.GONE
//                    binding.includedrawar1.reportArrowLabT.setImageResource(R.drawable.baseline_add_24)
//                }
//                if (binding.includedrawar1.PreventiveSLayout.visibility == View.VISIBLE) {
//                    binding.includedrawar1.PreventiveSLayout.visibility = View.GONE
//                    binding.includedrawar1.reportArrowPS.setImageResource(R.drawable.baseline_add_24)
//                }
            }

            binding.includedrawar1.myWallet.setOnClickListener {
                startActivity(Intent(this, TranslatorWallet::class.java))
                drawerLayout.closeDrawer(GravityCompat.START)
//                if (binding.includedrawar1.LabReportsLayout.visibility == View.VISIBLE) {
//                    binding.includedrawar1.LabReportsLayout.visibility = View.GONE
//                    binding.includedrawar1.reportArrowLabR.setImageResource(R.drawable.baseline_add_24)
//                } else {
//                    binding.includedrawar1.LabReportsLayout.visibility = View.VISIBLE
//                    binding.includedrawar1.LabReportsLayout.setBackgroundColor(resources.getColor(R.color.main_color))
//                    binding.includedrawar1.reportArrowLabR.setImageResource(R.drawable.baseline_remove_24)
//                }
//
//                if (binding.includedrawar1.LabDoctor.visibility == View.VISIBLE) {
//                    binding.includedrawar1.LabDoctor.visibility = View.GONE
//                    binding.includedrawar1.DoctorArrowPS.setImageResource(R.drawable.baseline_add_24)
//                }
//                if (binding.includedrawar1.ClinicalMLayout.visibility == View.VISIBLE) {
//                    binding.includedrawar1.ClinicalMLayout.visibility = View.GONE
//                    binding.includedrawar1.reportArrowCM.setImageResource(R.drawable.baseline_add_24)
//                }
//                if (binding.includedrawar1.RefferalsAFULayout.visibility == View.VISIBLE) {
//                    binding.includedrawar1.RefferalsAFULayout.visibility = View.GONE
//                    binding.includedrawar1.reportArrowRAFU.setImageResource(R.drawable.baseline_add_24)
//                }
//
//                if (binding.includedrawar1.LabTestsLayout.visibility == View.VISIBLE) {
//                    binding.includedrawar1.LabTestsLayout.visibility = View.GONE
//                    binding.includedrawar1.reportArrowLabT.setImageResource(R.drawable.baseline_add_24)
//                }
//                if (binding.includedrawar1.PreventiveSLayout.visibility == View.VISIBLE) {
//                    binding.includedrawar1.PreventiveSLayout.visibility = View.GONE
//                    binding.includedrawar1.reportArrowPS.setImageResource(R.drawable.baseline_add_24)
//                }
            }/*
                        binding.includedrawar1.LabTest.setOnClickListener {
                            if (binding.includedrawar1.LabTestsLayout.visibility == View.VISIBLE) {
                                binding.includedrawar1.LabTestsLayout.visibility = View.GONE
                                binding.includedrawar1.reportArrowLabT.setImageResource(R.drawable.baseline_add_24)
                            } else {
                                binding.includedrawar1.LabTestsLayout.visibility = View.VISIBLE
                                binding.includedrawar1.LabTestsLayout.setBackgroundColor(resources.getColor(R.color.main_color))
                                binding.includedrawar1.reportArrowLabT.setImageResource(R.drawable.baseline_remove_24)
                            }

                            if (binding.includedrawar1.orderListLayout.visibility == View.VISIBLE) {
                                binding.includedrawar1.orderListLayout.visibility = View.GONE
                                binding.includedrawar1.orderArrowPs.setImageResource(R.drawable.baseline_add_24)
                            }
                            if (binding.includedrawar1.ClinicalMLayout.visibility == View.VISIBLE) {
                                binding.includedrawar1.ClinicalMLayout.visibility = View.GONE
                                binding.includedrawar1.reportArrowCM.setImageResource(R.drawable.baseline_add_24)
                            }
                            if (binding.includedrawar1.RefferalsAFULayout.visibility == View.VISIBLE) {
                                binding.includedrawar1.RefferalsAFULayout.visibility = View.GONE
                                binding.includedrawar1.reportArrowRAFU.setImageResource(R.drawable.baseline_add_24)
                            }
                            if (binding.includedrawar1.LabReportsLayout.visibility == View.VISIBLE) {
                                binding.includedrawar1.LabReportsLayout.visibility = View.GONE
                                binding.includedrawar1.reportArrowLabR.setImageResource(R.drawable.baseline_add_24)
                            }

                            if (binding.includedrawar1.PreventiveSLayout.visibility == View.VISIBLE) {
                                binding.includedrawar1.PreventiveSLayout.visibility = View.GONE
                                binding.includedrawar1.reportArrowPS.setImageResource(R.drawable.baseline_add_24)
                            }
                        }
            */
            if (binding.includedrawar1.PreventiveSLayout.visibility == View.VISIBLE) {
                binding.includedrawar1.PreventiveSLayout.visibility = View.GONE
                binding.includedrawar1.reportArrowPS.setImageResource(R.drawable.baseline_add_24)
            }

            if (binding.includedrawar1.ClinicalMLayout.visibility == View.VISIBLE) {
                binding.includedrawar1.ClinicalMLayout.visibility = View.GONE
                binding.includedrawar1.reportArrowCM.setImageResource(R.drawable.baseline_add_24)
            }
            if (binding.includedrawar1.RefferalsAFULayout.visibility == View.VISIBLE) {
                binding.includedrawar1.RefferalsAFULayout.visibility = View.GONE
                binding.includedrawar1.reportArrowRAFU.setImageResource(R.drawable.baseline_add_24)
            }

            if (binding.includedrawar1.LabReportsLayout.visibility == View.VISIBLE) {
                binding.includedrawar1.LabReportsLayout.visibility = View.GONE
                binding.includedrawar1.reportArrowLabR.setImageResource(R.drawable.baseline_add_24)
            }

            if (binding.includedrawar1.orderListLayout.visibility == View.VISIBLE) {
                binding.includedrawar1.orderListLayout.visibility = View.GONE
                binding.includedrawar1.orderArrowPs.setImageResource(R.drawable.baseline_add_24)
            }

            if (binding.includedrawar1.TrasnlatorService.visibility == View.VISIBLE) {
                binding.includedrawar1.PreventiveSLayout.visibility = View.GONE
                binding.includedrawar1.reportArrowPS.setImageResource(R.drawable.baseline_add_24)
            }


        }
        drawerLayout = binding.drawerlayout1

        statisticsList.add(
            ModelSpinner(
                resources.getString(R.string.Overall_Statistics),
                "Overall Statistics"
            )
        )
        statisticsList.add(
            ModelSpinner(
                resources.getString(R.string.Today_Statistics),
                "Today's Statistics"
            )
        )
        statisticsList.add(
            ModelSpinner(
                resources.getString(R.string.This_Month_Statistics),
                "This Month's Statistics"
            )
        )

        binding.spinnerStatistics.adapter = ArrayAdapter<ModelSpinner>(
            context, R.layout.spinner_layout, statisticsList
        )

        binding.spinnerStatistics.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    p0: AdapterView<*>?, view: View?, i: Int, l: Long
                ) {
                    if (statisticsList.size > 0) {
                        val statusChange = statisticsList[i].value
                    }
                }

                override fun onNothingSelected(adapterView: AdapterView<*>?) {

                }
            }


        getLocationDetail = GetLocationDetail(this, context)
        easyWayLocation = EasyWayLocation(context, false, false, this)


        lm = context.getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
        getLastLocation()
        if (sessionManager.usertype == "translator") {
            apiCallDashboardTra()
        } else {
            apiCallDashboard()

        }
    }

    private fun apiCallAllOrderCount() {
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

                        } else if (response.code() == 500) {
                            myToast(context, resources.getString(R.string.Server_Error))
                            AppProgressBar.hideLoaderDialog()

                        } else {
                            val mainData = response.body()?.data ?: emptyList()
                            val canceled = mainData.filter { order ->
                                order.order_status == "canceled"
                            }
                            val pending = mainData.filter { order ->
                                order.order_status == "pending"
                            }
                            val confirmed = mainData.filter { order ->
                                order.order_status == "confirmed"
                            }
                            val delivered = mainData.filter { order ->
                                order.order_status == "delivered"
                            }

                            binding.includedrawar1.tvPendingCount.text = pending.size.toString()
                            binding.includedrawar1.tvConfirmedCount.text = confirmed.size.toString()
                            binding.includedrawar1.tvComCount.text = delivered.size.toString()
                            binding.includedrawar1.tvCanCount.text = canceled.size.toString()
                            binding.includedrawar1.tvRefundCount.text = canceled.size.toString()
                            binding.includedrawar1.tvAllCount.text = mainData.size.toString()


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
                        apiCallAllOrderCount()
                    } else {
                        myToast(context, t.message.toString())
                        AppProgressBar.hideLoaderDialog()

                    }
                }
            })
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission())
        { isGranted: Boolean ->
            if (isGranted) {
                // Permission is granted. Continue the action or workflow in your
                // app.
                //  sendNotification(this)
                // myToast(this@MainActivity,"Permission granted")
            } else {
                // Explain to the user that the feature is unavailable because the
                // features requires a permission that the user has denied. At the
                // same time, respect the user's decision. Don't link to system
                // settings in an effort to convince the user to change their
                // decision.
            }
        }

    private fun requestNotificationPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_NOTIFICATION_POLICY
            ) == PackageManager.PERMISSION_GRANTED
        ) return
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.ACCESS_NOTIFICATION_POLICY
            )
        ) {
        }
        ActivityCompat.requestPermissions(
            this,
            arrayOf<String>(Manifest.permission.ACCESS_NOTIFICATION_POLICY),
            NOTIFICATION_PERMISSION_CODE
        )
    }

    private val notificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            hasNotificationPermissionGranted = isGranted
            if (!isGranted) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (Build.VERSION.SDK_INT >= 33) {
                        if (shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS)) {
                            showNotificationPermissionRationale()
                        } else {
                            showSettingDialog()
                        }
                    }
                }
            } else {
                Toast.makeText(
                    applicationContext,
                    "notification permission granted",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }
    var hasNotificationPermissionGranted = false
    private val isNotificationListenerEnabled: Boolean
        get() {
            val pkgName = packageName
            val cn = ComponentName(pkgName, "$pkgName.NotificationListener")
            val flat = Settings.Secure.getString(contentResolver, "enabled_notification_listeners")
            return flat != null && flat.contains(cn.flattenToString())
        }

    private fun showSettingDialog() {
        MaterialAlertDialogBuilder(
            this,
            com.google.android.material.R.style.MaterialAlertDialog_Material3
        )
            .setTitle("Notification Permission")
            .setMessage("Notification permission is required, Please allow notification permission from setting")
            .setPositiveButton("Ok") { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.parse("package:$packageName")
                startActivity(intent)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showNotificationPermissionRationale() {

        MaterialAlertDialogBuilder(
            this,
            com.google.android.material.R.style.MaterialAlertDialog_Material3
        )
            .setTitle("Alert")
            .setMessage("Notification permission is required, to show notification")
            .setPositiveButton("Ok") { _, _ ->
                if (Build.VERSION.SDK_INT >= 33) {
                    notificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<String?>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//
//        // Checking the request code of our request
//        if (requestCode == NOTIFICATION_PERMISSION_CODE) {
//            // If permission is granted
//            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                // Displaying a toast
//                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show()
//            } else {
//                // Displaying another toast if permission is not granted
//                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG)
//                    .show()
//            }
//        }
//    }

    private fun getCurrentFCMToken() {
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("FCM Token", "Fetching FCM token failed", task.exception)
                    return@addOnCompleteListener
                }

                // Get new FCM token
                val token = task.result
                Log.d("FCM Token", "FCM token: $token")
                // Send the token to your server or save it as needed
            }
    }

    fun refresh() {
        overridePendingTransition(0, 0)
        finish()
        startActivity(intent)
        overridePendingTransition(0, 0)
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
                        myToast(context, resources.getString(R.string.Server_Error))
                        AppProgressBar.hideLoaderDialog()

                    } else if (response.code() == 200) {
                        sessionManager.usertype = response.body()!!.type
                        sessionManager.customerName = response.body()!!.name
                        sessionManager.phoneNumber = response.body()!!.phone
                        sessionManager.email = response.body()!!.email
                        sessionManager.profilePic = response.body()!!.applogo
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

    @SuppressLint("SetTextI18n", "LogNotTimber")
    private fun getLastLocation() {
        if (ContextCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationProviderClient!!.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    try {
                        val geocoder = Geocoder(context, Locale.getDefault())
                        val addresses =
                            geocoder.getFromLocation(location.latitude, location.longitude, 1)
//                            lattitude!!.text = "Lattitude: " + addresses[0].latitude
//                            longitude!!.text = "Longitude: " + addresses[0].longitude
                        Log.e(
                            ContentValues.TAG,
                            " addresses[0].latitude${addresses?.get(0)?.latitude}"
                        )
                        Log.e(
                            ContentValues.TAG,
                            " addresses[0].latitude${addresses?.get(0)?.longitude}"
                        )
                        sessionManager.latitude = addresses?.get(0)?.latitude.toString()
                        sessionManager.longitude = addresses?.get(0)?.longitude.toString()

                        addresses?.get(0)?.getAddressLine(0)

                        val locality = addresses?.get(0)?.locality
                        val countryName = addresses?.get(0)?.countryName
                        val countryCode = addresses?.get(0)?.countryCode
                        val postalCode = addresses?.get(0)?.postalCode.toString()
                        val subLocality = addresses?.get(0)?.subLocality
                        val subAdminArea = addresses?.get(0)?.subAdminArea
                        currentAddress = "$subLocality, $locality, $countryName"
                        postalCodeNew = postalCode


                        // binding.tvLocation.text = currentAddress
                        //binding.tvLocation.text = addresses?.get(0)?.getAddressLine(0)


                        Log.e(ContentValues.TAG, "locality-$locality")
                        Log.e(ContentValues.TAG, "countryName-$countryName")
                        Log.e(ContentValues.TAG, "countryCode-$countryCode")
                        Log.e(ContentValues.TAG, "postalCode-$postalCode")
                        Log.e(ContentValues.TAG, "subLocality-$subLocality")
                        Log.e(ContentValues.TAG, "subAdminArea-$subAdminArea")

                        Log.e(
                            ContentValues.TAG,
                            " addresses[0].Address${addresses?.get(0)?.getAddressLine(0)}"
                        )

                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        } else {
            askPermission()
        }
    }

    private fun apiCallDashboard() {

        //  AppProgressBar.showLoaderDialog(context)
        ApiClient.apiService.dashboard(
            sessionManager.idToken.toString()
        ).enqueue(object : Callback<ModelDashboard> {
            @SuppressLint("LogNotTimber", "LongLogTag")
            override fun onResponse(
                call: Call<ModelDashboard>, response: Response<ModelDashboard>
            ) {
                try {
                    if (response.code() == 500) {
                        myToast(context, resources.getString(R.string.Server_Error))
                        AppProgressBar.hideLoaderDialog()

                    } else if (response.code() == 401) {
                        // myToast(context, "Unauthorized")
                        myToast(
                            this@Dashboard,
                            resources.getString(R.string.User_Logged_in_other_Device)
                        )
                        sessionManager.logout()
                        val intent = Intent(applicationContext, Login::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                        finish()
                        startActivity(intent)
                        AppProgressBar.hideLoaderDialog()

                    } else {
                        // binding.tvTotalSer.text=response.body()!!.data.top_sell.toString()
                        binding.tvOrderedSer.text = response.body()!!.data.all.toString()
                        binding.tvCompletedSer.text = response.body()!!.data.delivered.toString()
                        binding.tvRejectedSer.text = response.body()!!.data.rejected.toString()
                        val jsonResponse = """
                    {
                        "earning": {
                            "1": ${response.body()!!.earning.`1`},
                            "2": ${response.body()!!.earning.`2`},
                            "3": ${response.body()!!.earning.`3`},
                            "4": ${response.body()!!.earning.`4`},
                            "5": ${response.body()!!.earning.`5`},
                            "6": ${response.body()!!.earning.`6`},
                            "7": ${response.body()!!.earning.`7`},
                            "8": ${response.body()!!.earning.`8`},
                            "9": ${response.body()!!.earning.`9`},
                            "10": ${response.body()!!.earning.`10`},
                            "11": ${response.body()!!.earning.`11`},
                            "12": ${response.body()!!.earning.`12`}
                        },
                        "commission": {
                             "1": ${response.body()!!.commission.`1`},
                            "2": ${response.body()!!.commission.`2`},
                            "3": ${response.body()!!.commission.`3`},
                            "4": ${response.body()!!.commission.`4`},
                            "5": ${response.body()!!.commission.`5`},
                            "6": ${response.body()!!.commission.`6`},
                            "7": ${response.body()!!.commission.`7`},
                            "8": ${response.body()!!.commission.`8`},
                            "9": ${response.body()!!.commission.`9`},
                            "10": ${response.body()!!.commission.`10`},
                            "11": ${response.body()!!.commission.`11`},
                            "12": ${response.body()!!.commission.`12`}
                        }
                    }
                """
                        // Parse JSON response
                        val jsonObject = JSONObject(jsonResponse)
                        // Extract earning and commission objects
                        val earningObject = jsonObject.getJSONObject("earning")
                        val commissionObject = jsonObject.getJSONObject("commission")

                        // Calculate sum of earnings
                        var earningsSum = 0.0
                        for (key in earningObject.keys()) {
                            earningsSum += earningObject.getDouble(key)
                        }
                        // Calculate sum of commissions
                        var commissionsSum = 0.0
                        for (key in commissionObject.keys()) {
                            commissionsSum += commissionObject.getDouble(key)
                        }

                        // Print the sums
                        println("Sum of earnings: $earningsSum")
                        println("Sum of commissions: $commissionsSum")

                        binding.totalEarning.text = earningsSum.toString() + " $"
                        binding.commission.text = commissionsSum.toString() + " $"


                        AppProgressBar.hideLoaderDialog()
                        // binding.tvRejectedSer

                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    myToast(context, "Something went wrong")
                    AppProgressBar.hideLoaderDialog()

                }
            }

            override fun onFailure(call: Call<ModelDashboard>, t: Throwable) {
                AppProgressBar.hideLoaderDialog()
                count1++
                if (count1 <= 3) {
                    Log.e("count", count1.toString())
                    apiCallDashboard()
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
        Dashboard.refreshLanNew = true
    }

    private fun apiCallDashboardTra() {

        //  AppProgressBar.showLoaderDialog(context)
        ApiClient.apiService.dashboardTra(
            sessionManager.idToken.toString()
        ).enqueue(object : Callback<ModelDashTra> {
            @SuppressLint("LogNotTimber", "LongLogTag")
            override fun onResponse(
                call: Call<ModelDashTra>, response: Response<ModelDashTra>
            ) {
                try {
                    if (response.code() == 500) {
                        myToast(context, resources.getString(R.string.Server_Error))
                        AppProgressBar.hideLoaderDialog()

                    } else if (response.code() == 401) {
                        // myToast(context, "Unauthorized")
                        myToast(
                            this@Dashboard,
                            resources.getString(R.string.User_Logged_in_other_Device)
                        )
                        sessionManager.logout()
                        val intent = Intent(applicationContext, Login::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                        finish()
                        startActivity(intent)
                        AppProgressBar.hideLoaderDialog()

                    } else {
                        // binding.tvTotalSer.text=response.body()!!.data.top_sell.toString()
                        binding.tvOrderedSer.text = response.body()!!.data.all.toString()
                        binding.tvCompletedSer.text = response.body()!!.data.delivered.toString()
                        binding.tvRejectedSer.text = response.body()!!.data.rejected.toString()
                        val jsonResponse = """
                    {
                        "earning": {
                            "1": ${response.body()!!.earning.`1`},
                            "2": ${response.body()!!.earning.`2`},
                            "3": ${response.body()!!.earning.`3`},
                            "4": ${response.body()!!.earning.`4`},
                            "5": ${response.body()!!.earning.`5`},
                            "6": ${response.body()!!.earning.`6`},
                            "7": ${response.body()!!.earning.`7`},
                            "8": ${response.body()!!.earning.`8`},
                            "9": ${response.body()!!.earning.`9`},
                            "10": ${response.body()!!.earning.`10`},
                            "11": ${response.body()!!.earning.`11`},
                            "12": ${response.body()!!.earning.`12`}
                        },
                        "commission": {
                             "1": ${response.body()!!.commission.`1`},
                            "2": ${response.body()!!.commission.`2`},
                            "3": ${response.body()!!.commission.`3`},
                            "4": ${response.body()!!.commission.`4`},
                            "5": ${response.body()!!.commission.`5`},
                            "6": ${response.body()!!.commission.`6`},
                            "7": ${response.body()!!.commission.`7`},
                            "8": ${response.body()!!.commission.`8`},
                            "9": ${response.body()!!.commission.`9`},
                            "10": ${response.body()!!.commission.`10`},
                            "11": ${response.body()!!.commission.`11`},
                            "12": ${response.body()!!.commission.`12`}
                        }
                    }
                """
                        // Parse JSON response
                        val jsonObject = JSONObject(jsonResponse)
                        // Extract earning and commission objects
                        val earningObject = jsonObject.getJSONObject("earning")
                        val commissionObject = jsonObject.getJSONObject("commission")

                        // Calculate sum of earnings
                        var earningsSum = 0.0
                        for (key in earningObject.keys()) {
                            earningsSum += earningObject.getDouble(key)
                        }
                        // Calculate sum of commissions
                        var commissionsSum = 0.0
                        for (key in commissionObject.keys()) {
                            commissionsSum += commissionObject.getDouble(key)
                        }

                        // Print the sums
                        println("Sum of earnings: $earningsSum")
                        println("Sum of commissions: $commissionsSum")

                        binding.totalEarning.text = earningsSum.toString() + " $"
                        binding.commission.text = commissionsSum.toString() + " $"

                        binding.includedrawar1.tvComCount.text =
                            response.body()!!.data.delivered.toString()
                        binding.includedrawar1.tvCanCount.text =
                            response.body()!!.data.rejected.toString()
                        binding.includedrawar1.tvRefundCount.text =
                            response.body()!!.data.refunded.toString()
                        binding.includedrawar1.tvAllCount.text =
                            response.body()!!.data.all.toString()
                        AppProgressBar.hideLoaderDialog()
                        // binding.tvRejectedSer

                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    myToast(context, resources.getString(R.string.Something_went_wrong))
                    AppProgressBar.hideLoaderDialog()

                }
            }

            override fun onFailure(call: Call<ModelDashTra>, t: Throwable) {
                AppProgressBar.hideLoaderDialog()
                count1++
                if (count1 <= 3) {
                    Log.e("count", count1.toString())
                    apiCallDashboardTra()
                } else {
                    myToast(context, t.message.toString())


                }
                AppProgressBar.hideLoaderDialog()
            }

        })

    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation()
            } else {
                Toast.makeText(
                    context, "Please provide the required permission", Toast.LENGTH_SHORT
                ).show()
            }
        }
        // Checking the request code of our request
        if (requestCode == NOTIFICATION_PERMISSION_CODE) {
            // If permission is granted
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Displaying a toast
                Toast.makeText(
                    this,
                    "Permission granted now you can read the storage",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                // Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG)
                    .show()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions!!, grantResults)
    }

    override fun locationOn() {
        getLastLocation()
        // easyWayLocation.startLocation()
    }

    @OptIn(DelicateCoroutinesApi::class)
    @SuppressLint("LogNotTimber")
    override fun currentLocation(location: Location?) {
        val latitude = location!!.latitude
        val longitude = location.longitude

        Log.e("getCurrentLocation", ">>>>>>>>>>:$latitude\n$longitude ")
        GlobalScope.launch {
            // getLocationDetail.getAddress(location.latitude, location.longitude, "xyz")
            getLocationDetail.getAddress(location.latitude, location.longitude, "AAA")
            //getLastLocation()
        }

    }

    override fun locationCancelled() {

    }

    @SuppressLint("LogNotTimber")
    override fun locationData(locationData: LocationData?) {
        locationData?.full_address.toString()
//        val currentAddress1 = locationData?.country.toString()
//        val Address = currentAddress+currentAddress1

    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            EasyWayLocation.LOCATION_SETTING_REQUEST_CODE -> easyWayLocation.onActivityResult(
                resultCode
            )
        }
    }

    override fun onResume() {
        super.onResume()
        easyWayLocation.startLocation()
        if (OrderDetails.back) {
            OrderDetails.back = false
            refresh()
        }
        if (sessionManager.usertype == "translator") {
            apiCallDashboardTra()
        } else {
            apiCallDashboard()

        }
    }

    private fun askPermission() {
        ActivityCompat.requestPermissions(
            context, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE
        )
    }


    companion object {
        var refreshLan = true
        var back = false
        var refreshLanNew = true
    }

    fun languageSetting(context: Context, languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val resources: Resources = context.resources
        val config: Configuration = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)

//        if (context is Activity) {
//            context.recreate()
//        }
    }

    private fun languageDialog() {
        val view = layoutInflater.inflate(R.layout.dialog_langauge, null)
        dialog = Dialog(context)


        val radioEnglish = view!!.findViewById<RadioButton>(R.id.radioEnglish)
        val radioArabic = view!!.findViewById<RadioButton>(R.id.radioArabic)

        if (sessionManager.selectedLanguage == "ar") {
            radioArabic.isChecked = true
        } else {
            radioEnglish.isChecked = true
        }

        radioEnglish.setOnCheckedChangeListener { _, _ ->
            val languageToLoad = "en"
            val locale: Locale = Locale(languageToLoad)
            Locale.setDefault(locale)
            val config: Configuration = Configuration()
            config.locale = locale
            resources.updateConfiguration(config, resources.displayMetrics)
            sessionManager.selectedLanguage = "en"
            dialog?.dismiss()
            refreshNew()


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
            refreshNew()
        }

        dialog = Dialog(context)
        if (view.parent != null) {
            (view.parent as ViewGroup).removeView(view) // <- fix
        }
        dialog!!.setContentView(view)
        dialog?.setCancelable(true)

        dialog?.show()
    }

    private fun refreshNew() {
        val intent = Intent(applicationContext, Dashboard::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        finish()
        startActivity(intent)
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