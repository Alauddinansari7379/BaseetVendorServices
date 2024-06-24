package com.amtech.vendorservices.V.TranslatorServices.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.amtech.vendorservices.R
import com.amtech.vendorservices.V.Dashboard.Dashboard
import com.amtech.vendorservices.V.Dashboard.model.ModelSpinner
import com.amtech.vendorservices.V.Helper.AppProgressBar
import com.amtech.vendorservices.V.Helper.myToast
import com.amtech.vendorservices.V.TranslatorServices.activity.adapter.AdapterServiceList
import com.amtech.vendorservices.V.TranslatorServices.activity.adapter.AdapterServiceListCar
import com.amtech.vendorservices.V.TranslatorServices.activity.model.ModeCar.ModelGetListCar
import com.amtech.vendorservices.V.TranslatorServices.activity.model.ModelServiceList
import com.amtech.vendorservices.V.TranslatorServices.activity.model.Product
import com.amtech.vendorservices.V.retrofit.ApiClient
import com.amtech.vendorservices.databinding.ActivityTranslaterServiceListBinding
import com.amtech.vendorservices.V.sharedpreferences.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TranslaterServiceList : AppCompatActivity() {
    private val binding by lazy {
        ActivityTranslaterServiceListBinding.inflate(layoutInflater)
    }
    var count = 0
    private var statisticsList = ArrayList<ModelSpinner>()

    private lateinit var mainData: ArrayList<Product>
    private lateinit var mainDataCar: ArrayList<com.amtech.vendorservices.V.TranslatorServices.activity.model.ModeCar.Product>

    val context = this@TranslaterServiceList
    lateinit var sessionManager: SessionManager
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

            if (sessionManager.usertype=="car"){
                apiCallServiceListCar()
            }else{
                apiCallServiceList()
            }


            when (sessionManager.usertype) {
                "car" -> {
                    binding.tvTitle.text = resources.getString(R.string.Car_Rental_List)
                    binding.btnAddNew.text = resources.getString(R.string.Add_New_Item)
                    binding.tvList.text = resources.getString(R.string.Item_List)
                }

                "home" -> {
                    binding.tvTitle.text = resources.getString(R.string.Home_Rental_List)
                    binding.btnAddNew.text = resources.getString(R.string.Add_New_Item)
                    binding.tvList.text = resources.getString(R.string.Item_List)
                }
//                "translator"->{
//                    binding.includedrawar1.tvService.text="Translator Service"
//                    binding.includedrawar1.tvConfig.text="Translator Config"
//                    binding.includedrawar1.tvMy.text="Translator Rental"
                //  }
                else -> {

                }

            }
            if (sessionManager.usertype=="car"){
                binding.edtSearch.visibility=View.GONE
                binding.edtSearchCar.visibility=View.VISIBLE
            }

            edtSearch.addTextChangedListener { str ->
                setRecyclerViewAdapter(mainData.filter {
                    it.name != null && it.name.toString()
                        .contains(str.toString(), ignoreCase = true)
                } as ArrayList<Product>)
            }

            edtSearchCar.addTextChangedListener { str ->
                setRecyclerViewAdapterCar(mainDataCar.filter {
                    it.name != null && it.name.toString()
                        .contains(str.toString(), ignoreCase = true)
                } as ArrayList<com.amtech.vendorservices.V.TranslatorServices.activity.model.ModeCar.Product>)
            }

            statisticsList.add(ModelSpinner(resources.getString(R.string.All_Category), "All Category"))
            statisticsList.add(ModelSpinner(resources.getString(R.string.Indian_Main), "Indian (Main)"))
            statisticsList.add(ModelSpinner(resources.getString(R.string.Italian_Main), "Italian (Main)"))
            statisticsList.add(ModelSpinner(resources.getString(R.string.Arabian_Main), "Arabian (Main)"))
            statisticsList.add(ModelSpinner(resources.getString(R.string.Chains_Main), "Chains (Main)"))

            binding.spinnerCat.adapter =
                ArrayAdapter<ModelSpinner>(
                    context,
                    R.layout.spinner_layout,
                    statisticsList
                )
            binding.spinnerCat.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        p0: AdapterView<*>?,
                        view: View?,
                        i: Int,
                        l: Long
                    )
                    {
                        if (statisticsList.size > 0) {
                            val statusChange = statisticsList[i].value
                        }
                    }
                    override fun onNothingSelected(adapterView: AdapterView<*>?) {

                    }
                }
        }
    }
    private fun apiCallServiceList() {
        AppProgressBar.showLoaderDialog(context)
        ApiClient.apiService.serviceList(
            sessionManager.idToken.toString())
            .enqueue(object : Callback<ModelServiceList> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelServiceList>, response: Response<ModelServiceList>
                ) {
                    try {
                        if (response.code() == 404) {
                            myToast(context, resources.getString(R.string.Server_Error))
                            AppProgressBar.hideLoaderDialog()

                        } else if (response.code() == 500) {
                            myToast(context, resources.getString(R.string.Server_Error))
                            AppProgressBar.hideLoaderDialog()

                        } else if (response.body()!!.products.isEmpty()) {
                            myToast(context, resources.getString(R.string.No_Data_Found))
                            AppProgressBar.hideLoaderDialog()

                        } else {
                            mainData = response.body()!!.products
                            setRecyclerViewAdapter(mainData)
                            AppProgressBar.hideLoaderDialog()
                        }
                    } catch (e: Exception) {
                        myToast(context, resources.getString(R.string.Something_went_wrong))
                        e.printStackTrace()
                        AppProgressBar.hideLoaderDialog()

                    }
                }


                override fun onFailure(call: Call<ModelServiceList>, t: Throwable) {
                    AppProgressBar.hideLoaderDialog()
                    count++
                    if (count <= 3) {
                        Log.e("count", count.toString())
                        apiCallServiceList()
                    } else {
                        myToast(context, t.message.toString())
                        AppProgressBar.hideLoaderDialog()

                    }
                    AppProgressBar.hideLoaderDialog()
                }

            })

    }

    fun refresh() {
        overridePendingTransition(0, 0)
        finish()
        startActivity(intent)
        overridePendingTransition(0, 0)
    }
    private fun apiCallServiceListCar() {
         AppProgressBar.showLoaderDialog(context)
        ApiClient.apiService.serviceListCar(
            sessionManager.idToken.toString(),
        )
            .enqueue(object : Callback<ModelGetListCar> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelGetListCar>, response: Response<ModelGetListCar>
                ) {
                    try {
                        if (response.code() == 404) {
                            myToast(context, resources.getString(R.string.Something_went_wrong))
                            AppProgressBar.hideLoaderDialog()

                        } else if (response.code() == 500) {
                            myToast(context, resources.getString(R.string.Server_Error))
                            AppProgressBar.hideLoaderDialog()

                        } else if (response.body()!!.products.isEmpty()) {
                            myToast(context, resources.getString(R.string.No_Data_Found))
                            AppProgressBar.hideLoaderDialog()

                        } else {
                            mainDataCar = response.body()!!.products
                            setRecyclerViewAdapterCar(mainDataCar)
                            AppProgressBar.hideLoaderDialog()
                        }
                    } catch (e: Exception) {
                        myToast(context, resources.getString(R.string.Something_went_wrong))
                        e.printStackTrace()
                        AppProgressBar.hideLoaderDialog()
                    }
                }
                override fun onFailure(call: Call<ModelGetListCar>, t: Throwable) {
                    AppProgressBar.hideLoaderDialog()
                    count++
                    if (count <= 3) {
                        Log.e("count", count.toString())
                        apiCallServiceListCar()
                    } else {
                        myToast(context, t.message.toString())
                        AppProgressBar.hideLoaderDialog()
                    }
                    AppProgressBar.hideLoaderDialog()
                }
            })
    }
    private fun setRecyclerViewAdapter(data: ArrayList<Product>) {
        binding.recyclerView.apply {
            adapter = AdapterServiceList(context, data)
            AppProgressBar.hideLoaderDialog()

        }
    }

    private fun setRecyclerViewAdapterCar(data: ArrayList<com.amtech.vendorservices.V.TranslatorServices.activity.model.ModeCar.Product>) {
        binding.recyclerView.apply {
            adapter = AdapterServiceListCar(context, data)
            AppProgressBar.hideLoaderDialog()

        }
    }
    override fun onDestroy() {
        super.onDestroy()
        Dashboard.refreshLanNew=true
    }
}