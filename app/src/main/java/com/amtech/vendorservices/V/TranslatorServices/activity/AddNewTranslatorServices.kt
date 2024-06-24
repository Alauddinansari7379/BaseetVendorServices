package com.amtech.vendorservices.V.TranslatorServices.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.ContentResolver
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.aminography.primecalendar.civil.CivilCalendar
import com.aminography.primedatepicker.picker.PrimeDatePicker
import com.aminography.primedatepicker.picker.PrimeDatePicker.Companion.dialogWith
import com.aminography.primedatepicker.picker.callback.MultipleDaysPickCallback
import com.amtech.vendorservices.R
import com.amtech.vendorservices.V.Dashboard.Dashboard
import com.amtech.vendorservices.V.Dashboard.model.ModelSpinner
import com.amtech.vendorservices.V.Helper.AppProgressBar
import com.amtech.vendorservices.V.Helper.currentDate
import com.amtech.vendorservices.V.Helper.myToast
import com.amtech.vendorservices.V.TranslatorServices.activity.model.ModelServiceList
import com.amtech.vendorservices.V.retrofit.ApiClient
import com.amtech.vendorservices.databinding.ActivityAddNewTranslatorServicesBinding
import com.devstune.searchablemultiselectspinner.SearchableItem
import com.devstune.searchablemultiselectspinner.SearchableMultiSelectSpinner
import com.devstune.searchablemultiselectspinner.SelectionCompleteListener
import com.example.hhfoundation.Helper.ImageUploadClass.UploadRequestBody
import com.amtech.vendorservices.V.sharedpreferences.SessionManager
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Locale

class AddNewTranslatorServices : AppCompatActivity(), UploadRequestBody.UploadCallback {
    private val binding by lazy {
        ActivityAddNewTranslatorServicesBinding.inflate(layoutInflater)
    }
    private val context = this@AddNewTranslatorServices
    private var datePicker: PrimeDatePicker? = null

    //Lists
    private var statisticsList = ArrayList<ModelSpinner>()
    private var zoneList = ArrayList<ModelSpinner>()
    private var carTypeList = ArrayList<ModelSpinner>()
    private var carModelList = ArrayList<ModelSpinner>()
    private var travellingList = ArrayList<ModelSpinner>()
    private var serviceHour = ArrayList<ModelSpinner>()
    private var scenicList = ArrayList<ModelSpinner>()
    private var bathroomList = ArrayList<ModelSpinner>()
    private var essentialsList = ArrayList<ModelSpinner>()
    private var clothesStorageList = ArrayList<ModelSpinner>()
    private var privateBBQGrillList = ArrayList<ModelSpinner>()
    private var utilitiesList = ArrayList<ModelSpinner>()
    private var homeTypeList = ArrayList<ModelSpinner>()
    private var ammentiesData = ArrayList<ModelSpinner>()
    private var ammentiesData1 = ArrayList<ModelSpinner>()
    private var mainEntranceList = ArrayList<ModelSpinner>()
    private var multipleSelectedDate = StringBuilder()
    var count = 0
    var count1 = 0
    var count2 = 0

    private var selectedImageUri: Uri? = null
    private var drone = "On Call"
    private var translationFrom = ""
    private var zone = ""
    private var homeType = ""
    private var maniEntrance = ""
    private var carType = ""
    private var drivingType = ""
    private var carModel = ""
    private var travelPerson = ""
    private var translationTo = ""
    private var serviceHourNew = ""
    private val mydilaog: Dialog? = null

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
            when (sessionManager.usertype) {
                "car" -> {
                    binding.tvTitle.text = resources.getString(R.string.Car_Servicet)
                    binding.tvAddNew.text = resources.getString(R.string.Add_New_Car_Rental_Service)
                    binding.tvName.text = resources.getString(R.string.Car_Rental_Name)
                    binding.tvDesc.text = resources.getString(R.string.Car_Rental_Description)
                    binding.tvImage.text = resources.getString(R.string.Car_Image)
                     binding.tvHoure.text = resources.getString(R.string.Days)
                    binding.layoutOnCall.visibility = View.GONE
                    binding.layoutTrasanlotor.visibility = View.GONE
                    binding.layoutHomeSpinner.visibility = View.GONE
                    binding.layoutServiceDateHome.visibility = View.GONE
                    binding.layoutHomeType.visibility = View.GONE
                }

                "home" -> {
                    binding.tvTitle.text = resources.getString(R.string.Home_Service)
                    binding.tvAddNew.text = resources.getString(R.string.Add_New_Home_Rental_Service)

                    binding.tvName.text = resources.getString(R.string.Home_Rental_Name)
                    binding.tvDesc.text = resources.getString(R.string.Home_Rental_Details)
                    binding.tvImage.text = resources.getString(R.string.Home_Image)

                    binding.layoutOnCall.visibility = View.GONE
                    binding.layoutTrasanlotor.visibility = View.GONE
                    binding.layoutDrivingType.visibility = View.GONE
                    binding.layoutVideo.visibility = View.GONE
                    //binding.layoutServiceDate.visibility = View.GONE
                }
                else -> {
                    binding.layoutZone.visibility = View.GONE
                    binding.layoutDrivingType.visibility = View.GONE
                    binding.layoutHomeSpinner.visibility = View.GONE
                    binding.layoutServiceDateHome.visibility = View.GONE
                    binding.layoutHomeType.visibility = View.GONE
                }

            }

            statisticsList.add(ModelSpinner(resources.getString(R.string.Turkey), "Turkish"))
            statisticsList.add(ModelSpinner(resources.getString(R.string.Arabic), "Arabic"))
            statisticsList.add(ModelSpinner(resources.getString(R.string.English), "English"))
            statisticsList.add(ModelSpinner(resources.getString(R.string.Russian), "Russian"))

            zoneList.add(ModelSpinner(resources.getString(R.string.India), "India"))
            zoneList.add(ModelSpinner(resources.getString(R.string.Trab_Zone), "Trab Zone"))
            zoneList.add(ModelSpinner(resources.getString(R.string.Turkey), "Turkey"))
            zoneList.add(ModelSpinner(resources.getString(R.string.Saudi), "Saudi"))

            carTypeList.add(ModelSpinner(resources.getString(R.string.Sedan), "Sedan"))
            carTypeList.add(ModelSpinner(resources.getString(R.string.Coupe), "Coupe"))
            carTypeList.add(ModelSpinner(resources.getString(R.string.Wagon), "Wagon"))
            carTypeList.add(ModelSpinner(resources.getString(R.string.Hatchback), "Hatchback"))
            carTypeList.add(ModelSpinner(resources.getString(R.string.Off_road), "Off-road"))
            carTypeList.add(ModelSpinner(resources.getString(R.string.SUV), "SUV"))
            carTypeList.add(ModelSpinner(resources.getString(R.string.Pickup), "Pickup"))
            carTypeList.add(ModelSpinner(resources.getString(R.string.Cabriolet), "Cabriolet"))
            carTypeList.add(ModelSpinner(resources.getString(R.string.Limbo), "Limbo"))
            carTypeList.add(ModelSpinner(resources.getString(R.string.Minivan), "Minivan"))


            carModelList.add(ModelSpinner("2021", "1"))
            carModelList.add(ModelSpinner("2022", "1"))
            carModelList.add(ModelSpinner("2023", "1"))
            carModelList.add(ModelSpinner("2024", "1"))

            ServiceDate.text = currentDate

            spinnerFrom.adapter =
                ArrayAdapter<ModelSpinner>(
                    context,
                    R.layout.spinner_layout_big,
                    statisticsList
                )

            spinnerFrom.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        p0: AdapterView<*>?,
                        view: View?,
                        i: Int,
                        l: Long
                    ) {
                        if (statisticsList.size > 0) {
                            translationFrom = statisticsList[i].value
                        }
                    }

                    override fun onNothingSelected(adapterView: AdapterView<*>?) {

                    }
                }

            homeTypeList.add(ModelSpinner(resources.getString(R.string.Room_Hall1), "1+1(1 Room + Hall)"))
            homeTypeList.add(ModelSpinner(resources.getString(R.string.Room_Hall2), "2+1(2 Room + Hall)"))
            homeTypeList.add(ModelSpinner(resources.getString(R.string.Room_Hall3), "3+1(3 Room + Hall)"))
            homeTypeList.add(ModelSpinner(resources.getString(R.string.Room_Hall4), "4+1(4 Room + Hall)"))
            homeTypeList.add(ModelSpinner(resources.getString(R.string.Room_Hall5), "5+1(5 Room + Hall)"))
            homeTypeList.add(ModelSpinner(resources.getString(R.string.Villa), "Villa"))
            homeTypeList.add(ModelSpinner(resources.getString(R.string.Wood_Home), "Wood Home"))



            mainEntranceList.add(ModelSpinner(resources.getString(R.string.The_main_Entrance), "The main Entrance"))
            mainEntranceList.add(ModelSpinner(resources.getString(R.string.Balcony), "Balcony"))
            mainEntranceList.add(ModelSpinner(resources.getString(R.string.The_Kichen), "The Kitchen"))
            mainEntranceList.add(ModelSpinner(resources.getString(R.string.The_Lounge), "The Lounge"))
            mainEntranceList.add(ModelSpinner(resources.getString(R.string.The_Bathroom), "The Bathroom"))
            mainEntranceList.add(ModelSpinner(resources.getString(R.string.Master_Bedroom), "Master Bedroom"))
            mainEntranceList.add(ModelSpinner(resources.getString(R.string.Bedroom), "Bedroom"))
            mainEntranceList.add(ModelSpinner(resources.getString(R.string.The_Store), "The Store"))

            spinnerMainEntrance.adapter =
                ArrayAdapter<ModelSpinner>(
                    context,
                    R.layout.spinner_layout_big,
                    mainEntranceList
                )

            spinnerMainEntrance.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        p0: AdapterView<*>?,
                        view: View?,
                        i: Int,
                        l: Long
                    ) {
                        if (mainEntranceList.size > 0) {
                            maniEntrance = mainEntranceList[i].value
                        }
                    }

                    override fun onNothingSelected(adapterView: AdapterView<*>?) {

                    }
                }

            spinnerHomeType.adapter =
                ArrayAdapter<ModelSpinner>(
                    context,
                    R.layout.spinner_layout_big,
                    homeTypeList
                )

            spinnerHomeType.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        p0: AdapterView<*>?,
                        view: View?,
                        i: Int,
                        l: Long
                    ) {
                        if (homeTypeList.size > 0) {
                            homeType = homeTypeList[i].value
                        }
                    }

                    override fun onNothingSelected(adapterView: AdapterView<*>?) {

                    }
                }

            spinnerCarModel.adapter =
                ArrayAdapter<ModelSpinner>(
                    context,
                    R.layout.spinner_layout_big,
                    carModelList
                )

            spinnerCarModel.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        p0: AdapterView<*>?,
                        view: View?,
                        i: Int,
                        l: Long
                    ) {
                        if (carModelList.size > 0) {
                            carModel = carModelList[i].text
                        }
                    }

                    override fun onNothingSelected(adapterView: AdapterView<*>?) {

                    }
                }

            spinnerZone.adapter =
                ArrayAdapter<ModelSpinner>(
                    context,
                    R.layout.spinner_layout_big,
                    zoneList
                )

            spinnerZone.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        p0: AdapterView<*>?,
                        view: View?,
                        i: Int,
                        l: Long
                    ) {
                        if (zoneList.size > 0) {
                            zone = zoneList[i].value
                        }
                    }

                    override fun onNothingSelected(adapterView: AdapterView<*>?) {

                    }
                }

            spinnerCarType.adapter =
                ArrayAdapter<ModelSpinner>(
                    context,
                    R.layout.spinner_layout_big,
                    carTypeList
                )

            spinnerCarType.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        p0: AdapterView<*>?,
                        view: View?,
                        i: Int,
                        l: Long
                    ) {
                        if (carTypeList.size > 0) {
                            carType = carTypeList[i].value
                        }
                    }

                    override fun onNothingSelected(adapterView: AdapterView<*>?) {

                    }
                }

            for (i in 1..12) {
                serviceHour.add(ModelSpinner(i.toString(), "1"))
            }

            for (i in 1..7) {
                travellingList.add(ModelSpinner(i.toString(), "1"))
            }

            spinnerServiceH.adapter =
                ArrayAdapter<ModelSpinner>(
                    context,
                    R.layout.spinner_layout_big,
                    serviceHour
                )

            spinnerServiceH.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        p0: AdapterView<*>?,
                        view: View?,
                        i: Int,
                        l: Long
                    ) {
                        if (serviceHour.size > 0) {
                            serviceHourNew = serviceHour[i].text
                        }
                    }

                    override fun onNothingSelected(adapterView: AdapterView<*>?) {

                    }
                }

            spinnerDays.adapter =
                ArrayAdapter<ModelSpinner>(
                    context,
                    R.layout.spinner_layout_big,
                    serviceHour
                )

            spinnerDays.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        p0: AdapterView<*>?,
                        view: View?,
                        i: Int,
                        l: Long
                    ) {
                        if (serviceHour.size > 0) {
                            serviceHourNew = serviceHour[i].text
                        }
                    }

                    override fun onNothingSelected(adapterView: AdapterView<*>?) {

                    }
                }

            spinnerTravelling.adapter =
                ArrayAdapter<ModelSpinner>(
                    context,
                    R.layout.spinner_layout_big,
                    travellingList
                )

            spinnerTravelling.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        p0: AdapterView<*>?,
                        view: View?,
                        i: Int,
                        l: Long
                    ) {
                        if (travellingList.size > 0) {
                            travelPerson = travellingList[i].text
                        }
                    }

                    override fun onNothingSelected(adapterView: AdapterView<*>?) {

                    }
                }

            spinnerTo.adapter =
                ArrayAdapter<ModelSpinner>(
                    context,
                    R.layout.spinner_layout_big,
                    statisticsList
                )

            spinnerTo.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        p0: AdapterView<*>?,
                        view: View?,
                        i: Int,
                        l: Long
                    ) {
                        if (statisticsList.size > 0) {
                            translationTo = statisticsList[i].value
                        }
                    }

                    override fun onNothingSelected(adapterView: AdapterView<*>?) {

                    }
                }

//            mydilaog?.setCanceledOnTouchOutside(false)
//            mydilaog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//            val newCalendar = Calendar.getInstance()
//            var datePicker = DatePickerDialog(
//                context,
//                { _, year, monthOfYear, dayOfMonth ->
//                    val newDate = Calendar.getInstance()
//                    newDate[year, monthOfYear] = dayOfMonth
//                    DateFormat.getDateInstance().format(newDate.time)
//                    // val Date = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(newDate.time)
//                    ServiceDate.text = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(newDate.time)
//
//
//                    //Log.e(ContentValues.TAG, "ServiceDate:>>$ServiceDate.text")
//                },
//                newCalendar[Calendar.YEAR],
//                newCalendar[Calendar.MONTH],
//                newCalendar[Calendar.DAY_OF_MONTH]
//            )

            //datePicker.datePicker.minDate = System.currentTimeMillis() - 1000;


            tvEnglish.setOnClickListener {
                tvSerNameEn.text = resources.getString(R.string._EN)
                tvDescEn.text =  resources.getString(R.string._EN)
                layoutEnglishLine.setBackgroundColor(resources.getColor(R.color.blue))
                layoutArbicLine.setBackgroundColor(Color.parseColor("#FFFFFF"))
            }
            tvArabic.setOnClickListener {
                tvSerNameEn.text =  resources.getString(R.string._AR)
                tvDescEn.text = resources.getString(R.string._AR)
                layoutEnglishLine.setBackgroundColor(Color.parseColor("#FFFFFF"))
                layoutArbicLine.setBackgroundColor(resources.getColor(R.color.blue))
            }
            radioOnCall.setOnCheckedChangeListener { _, _ ->
                if (radioOnCall.isChecked) {
                    drone = "On Call"
                    radioInPerson.isChecked = false
                    radioDoc.isChecked = false
                    layoutServiceDate.visibility = View.VISIBLE

                }

            }
            radioInPerson.setOnCheckedChangeListener { _, _ ->
                if (radioInPerson.isChecked) {
                    drone = "In Person"
                    radioOnCall.isChecked = false
                    radioDoc.isChecked = false
                    layoutServiceDate.visibility = View.VISIBLE
                }
            }
            radioDoc.setOnCheckedChangeListener { _, _ ->
                if (radioDoc.isChecked) {
                    drone = "Doc"
                    serviceHourNew = ""
                    ServiceDate.text = ""
                    radioOnCall.isChecked = false
                    radioInPerson.isChecked = false
                    layoutServiceDate.visibility = View.GONE
                }
            }

            radioSelf.setOnCheckedChangeListener { _, _ ->
                if (radioSelf.isChecked) {
                    drivingType = "Self-Driving"
                    radioDriver.isChecked = false


                }
            }
            radioDriver.setOnCheckedChangeListener { _, _ ->
                if (radioDriver.isChecked) {
                    drivingType = "Driver"
                    radioSelf.isChecked = false
                }
            }
            btnChoose.setOnClickListener {
                openImageChooser()
            }

            btnSubmit.setOnClickListener {
                if (edtName.text.toString().isEmpty()) {
                    edtName.error =resources.getString(R.string.Enter_Name)
                    edtName.requestFocus()
                    return@setOnClickListener
                }
                if (edtDescription.text.toString().isEmpty()) {
                    edtDescription.error = resources.getString(R.string.Enter_Description)
                    edtDescription.requestFocus()
                    return@setOnClickListener
                }

                if (edtPrice.text.toString().isEmpty()) {
                    edtPrice.error = resources.getString(R.string.Enter_Price)
                    edtPrice.requestFocus()
                    return@setOnClickListener
                }
                when (sessionManager.usertype) {
                    "car" -> {
                        apiCallAddCar()
                    }

                    "home" -> {
                        apiCallAddHome()
                    }

                    else -> {
                        apiCallAddNewService()
                    }
                }

            }

            //Home Rental
            spinnerScenic()
            spinnerBathroom()
            spinnerEssentials()
            spinnerClothesStorage()
            spinnerPrivateBBQGrill()
            spinnerUtilities()


        }
        val callback = MultipleDaysPickCallback {
            multipleSelectedDate.clear()
            for (i in it) {
                val conDate = convertDate(i.longDateString)
                multipleSelectedDate.append("${conDate},")
                Log.e("multipleSelectedDate", multipleSelectedDate.toString())
            }
            binding.ServiceDate.text = multipleSelectedDate.toString()
            val locale = Locale(sessionManager.selectedLanguage!!)
            Locale.setDefault(locale)
            val resources: Resources = context.resources
            val config: Configuration = resources.configuration
            config.setLocale(locale)
            resources.updateConfiguration(config, resources.displayMetrics)
        }
        binding.ServiceDate.setOnClickListener {
            val locale = Locale("en")
            Locale.setDefault(locale)
            val resources: Resources = context.resources
            val config: Configuration = resources.configuration
            config.setLocale(locale)
            resources.updateConfiguration(config, resources.displayMetrics)

            val today = CivilCalendar()
            val datePicker = PrimeDatePicker.dialogWith(today)
                .pickMultipleDays(callback)
                .build()
            datePicker.show(supportFragmentManager, "MultipleDatePicker")
        }
    }

    private fun convertDate(inputDate: String): String {
        val inputFormat = SimpleDateFormat("EEEE, d MMMM yyyy", Locale.ENGLISH)
        val outputFormat = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)

        val date = inputFormat.parse(inputDate)
        val formattedDate = outputFormat.format(date!!)

        return formattedDate

    }

    private fun spinnerScenic() {
        val itemsNew: MutableList<SearchableItem> = ArrayList()
        scenicList.add(ModelSpinner(resources.getString(R.string.Mountain_view), "Mountain view"))
        scenicList.add(ModelSpinner(resources.getString(R.string.Park_view), "Park view"))

        for (i in scenicList) {
            itemsNew.add(SearchableItem(i.text, i.value))
        }
        binding.spinnerScenic.setOnClickListener {
            SearchableMultiSelectSpinner.show(
                context,
                resources.getString(R.string.Select_Items),
                resources.getString(R.string.Done),
                itemsNew,
                object :
                    SelectionCompleteListener {
                    override fun onCompleteSelection(selectedItems: ArrayList<SearchableItem>) {
                        Log.e("data", selectedItems.toString())
                        val selectedData = StringBuilder()
                        for (i in selectedItems) {
                            selectedData.append("${i.text}, ")
                            ammentiesData.add(ModelSpinner(i.code, "1"))
                            Log.e("chosenItems", i.text)
                        }
                        binding.spinnerScenic.text = selectedData.toString()
                    }

                })
        }
    }

    private fun spinnerBathroom() {
        val itemsNew: MutableList<SearchableItem> = ArrayList()
        bathroomList.add(ModelSpinner(resources.getString(R.string.Hair_dryer), "Hair dryer"))
        bathroomList.add(ModelSpinner(resources.getString(R.string.Cleaning_products), "Cleaning products"))
        bathroomList.add(ModelSpinner(resources.getString(R.string.Shampoo), "Shampoo"))
        bathroomList.add(ModelSpinner(resources.getString(R.string.Body_soap), "Body soap"))
        bathroomList.add(ModelSpinner(resources.getString(R.string.Hot_water), "Hot water"))
        bathroomList.add(ModelSpinner(resources.getString(R.string.Bedroom_and_laundry), "Bedroom and laundry"))

        for (i in bathroomList) {
            itemsNew.add(SearchableItem(i.text, i.value))
        }
        binding.spinnerBathroom.setOnClickListener {
            SearchableMultiSelectSpinner.show(
                context,
                resources.getString(R.string.Select_Items),
                resources.getString(R.string.Done),
                itemsNew,
                object :
                    SelectionCompleteListener {
                    override fun onCompleteSelection(selectedItems: ArrayList<SearchableItem>) {
                        Log.e("data", selectedItems.toString())
                        val selectedData = StringBuilder()
                        for (i in selectedItems) {
                            selectedData.append("${i.text}, ")
                            Log.e("chosenItems", i.text)
                            ammentiesData.add(ModelSpinner(i.code, "1"))

                        }
                        binding.spinnerBathroom.text = selectedData.toString()
                    }

                })
        }
    }

    private fun spinnerEssentials() {
        val itemsNew: MutableList<SearchableItem> = ArrayList()
        essentialsList.add(ModelSpinner(resources.getString(R.string.Towels_bed_sheets), "Towels, bed sheets"))
        essentialsList.add(ModelSpinner(resources.getString(R.string.soap_and_toilet_paper), "soap and toilet paper"))
        essentialsList.add(ModelSpinner(resources.getString(R.string.Hangers), "Hangers"))
        essentialsList.add(ModelSpinner(resources.getString(R.string.Bed_linen), "Bed linen"))
        essentialsList.add(ModelSpinner(resources.getString(R.string.Cotton_linen), "Cotton linen"))
        essentialsList.add(ModelSpinner(resources.getString(R.string.Extra_pillows_blankets), "Extra pillows & blankets"))
        essentialsList.add(ModelSpinner(resources.getString(R.string.Room_darkening_blinds), "Room-darkening blinds"))
        essentialsList.add(ModelSpinner(resources.getString(R.string.Iron), "Iron"))
        essentialsList.add(ModelSpinner(resources.getString(R.string.Clothes_drying_rack), "Clothes drying rack"))


        for (i in essentialsList) {
            itemsNew.add(SearchableItem(i.text, i.value))
        }
        binding.spinnerEssentials.setOnClickListener {
            SearchableMultiSelectSpinner.show(
                context,
                resources.getString(R.string.Select_Items),
                resources.getString(R.string.Done),
                itemsNew,
                object :
                    SelectionCompleteListener {
                    override fun onCompleteSelection(selectedItems: ArrayList<SearchableItem>) {
                        Log.e("data", selectedItems.toString())
                        val selectedData = StringBuilder()
                        for (i in selectedItems) {
                            selectedData.append("${i.text}, ")
                            Log.e("chosenItems", i.code)
                            ammentiesData.add(ModelSpinner(i.code, "1"))

                        }
                        binding.spinnerEssentials.text = selectedData.toString()
                    }

                })
        }
    }

    private fun spinnerClothesStorage() {
        val itemsNew: MutableList<SearchableItem> = ArrayList()
        clothesStorageList.add(ModelSpinner( resources.getString(R.string.Wardrobe_and_chest_of_drawers), "Wardrobe and chest of drawers"))
        clothesStorageList.add(ModelSpinner( resources.getString(R.string.Entertainment),"Entertainment"))
        clothesStorageList.add(
            ModelSpinner(
                resources.getString(R.string.HDTV_with_Amazon_Prime_Video),
                "42 HDTV with Amazon Prime Video, Netflix, cable/satellite TV, Disney+, standard cable/satellite"
            )
        )
        clothesStorageList.add(ModelSpinner( resources.getString(R.string.Family), "Family"))
        clothesStorageList.add(
            ModelSpinner(
                resources.getString(R.string.Clamp_on_table_seat_high_chair),
                "1"
            )
        )
        clothesStorageList.add(ModelSpinner( resources.getString(R.string.With_straps_harness_and_food_tray), "With straps/harness and food tray"))
        clothesStorageList.add(ModelSpinner(resources.getString(R.string.Children_tableware), "Children’s tableware"))
        clothesStorageList.add(ModelSpinner(resources.getString(R.string.Window_guards), "Window guards"))
        clothesStorageList.add(ModelSpinner(resources.getString(R.string.Board_games), "Board games"))
        clothesStorageList.add(ModelSpinner(resources.getString(R.string.Heating_and_cooling), "Heating and cooling"))
        clothesStorageList.add(ModelSpinner(resources.getString(R.string.Portable_heater), "Portable heater"))
        clothesStorageList.add(ModelSpinner(resources.getString(R.string.Home_safety), "Home safety"))
        clothesStorageList.add(ModelSpinner(resources.getString(R.string.Security_cameras_on_property), "Security cameras on property"))
        clothesStorageList.add(ModelSpinner(resources.getString(R.string.CCTVs_are_placed), "CCTVs are placed outside the property only"))
        clothesStorageList.add(ModelSpinner(resources.getString(R.string.Fire_extinguisher), "Fire extinguisher"))
        clothesStorageList.add(ModelSpinner(resources.getString(R.string.First_aid_kit), "First aid kit"))
        clothesStorageList.add(ModelSpinner(resources.getString(R.string.Internet_and_office), "Internet and office"))
        clothesStorageList.add(ModelSpinner(resources.getString(R.string.Wifi_Mbps), "Wifi – 20 Mbps Verified by speed test"))
        clothesStorageList.add(ModelSpinner(resources.getString(R.string.Stream_HD_videos), "Stream HD videos"))
        clothesStorageList.add(ModelSpinner(resources.getString(R.string.Dedicated_workspace), "Dedicated workspace"))
        clothesStorageList.add(ModelSpinner(resources.getString(R.string.In_a_room_with_a_door), "In a room with a door"))
        clothesStorageList.add(ModelSpinner(resources.getString(R.string.Kitchen_and_dining), "Kitchen and dining"))
        clothesStorageList.add(ModelSpinner(resources.getString(R.string.Microwave), "Microwave"))
        clothesStorageList.add(ModelSpinner(resources.getString(R.string.Dishes_and_cutlery), "Dishes and cutlery"))
        clothesStorageList.add(ModelSpinner(resources.getString(R.string.Bowls_chopsticks_plates_cups_etc), "Bowls, chopsticks, plates, cups, etc"))
        clothesStorageList.add(ModelSpinner(resources.getString(R.string.Samsung_23L_stainless_steel_oven), "Samsung 23L stainless steel oven"))
        clothesStorageList.add(ModelSpinner(resources.getString(R.string.Kettle), "Kettle"))
        clothesStorageList.add(ModelSpinner(resources.getString(R.string.Toaster), "Toaster"))
        clothesStorageList.add(ModelSpinner(resources.getString(R.string.Dining_table), "Dining table"))
        clothesStorageList.add(ModelSpinner(resources.getString(R.string.Coffee), "Coffee"))
        clothesStorageList.add(ModelSpinner(resources.getString(R.string.Bread_maker), "Bread maker"))
        clothesStorageList.add(ModelSpinner(resources.getString(R.string.Location_features), "Location features"))
        clothesStorageList.add(ModelSpinner(resources.getString(R.string.Private_entrance), "Private entrance"))
        clothesStorageList.add(ModelSpinner(resources.getString(R.string.Separate_street_or_building_entrance), "Separate street or building entrance"))
        clothesStorageList.add(ModelSpinner(resources.getString(R.string.Outdoor), "Outdoor"))
        clothesStorageList.add(ModelSpinner(resources.getString(R.string.Private_patio_or_balcony), "Private patio or balcony"))
        clothesStorageList.add(ModelSpinner(resources.getString(R.string.Private_back_garden_Fully_fenced), "Private back garden – Fully fenced"))
        clothesStorageList.add(
            ModelSpinner(
                resources.getString(R.string.An_open_space_on_the_property_usually_covered_in_grass),
                "An open space on the property usually covered in grass"
            )
        )
        clothesStorageList.add(ModelSpinner(resources.getString(R.string.Outdoor_furniture), "Outdoor furniture"))



        for (i in clothesStorageList) {
            itemsNew.add(SearchableItem(i.text, i.value))
        }
        binding.spinnerClothesstorage.setOnClickListener {
            SearchableMultiSelectSpinner.show(
                context,
                resources.getString(R.string.Select_Items),
                resources.getString(R.string.Done),
                itemsNew,
                object :
                    SelectionCompleteListener {
                    override fun onCompleteSelection(selectedItems: ArrayList<SearchableItem>) {
                        Log.e("data", selectedItems.toString())
                        val selectedData = StringBuilder()
                        for (i in selectedItems) {
                            selectedData.append("${i.text}, ")
                            Log.e("chosenItems", i.code)
                            ammentiesData1.add(ModelSpinner(i.code, "1"))

                        }
                        binding.spinnerClothesstorage.text = selectedData.toString()

                    }

                })
        }
    }

    private fun spinnerPrivateBBQGrill() {
        val itemsNew: MutableList<SearchableItem> = ArrayList()
        privateBBQGrillList.add(ModelSpinner(resources.getString(R.string.Electric), "Electric"))
        privateBBQGrillList.add(ModelSpinner(resources.getString(R.string.Parking_and_facilities), "Parking and facilities"))
        privateBBQGrillList.add(ModelSpinner(resources.getString(R.string.Free_residential_garage_on_premises_space), "Free residential garage on premises – 1 space"))
        privateBBQGrillList.add(ModelSpinner(resources.getString(R.string.Free_on_street_parking), "Free on-street parking"))
        privateBBQGrillList.add(ModelSpinner(resources.getString(R.string.Services), "Services"))
        privateBBQGrillList.add(ModelSpinner(resources.getString(R.string.Luggage_drop_off_allowed), "Luggage drop-off allowed"))
        privateBBQGrillList.add(
            ModelSpinner(
                resources.getString(R.string.For_guests_convenience_when_they_are_arriving_early_or_departing_late),
                "For guests' convenience when they are arriving early or departing late"
            )
        )
        privateBBQGrillList.add(ModelSpinner(resources.getString(R.string.Breakfast), "Breakfast"))
        privateBBQGrillList.add(ModelSpinner(resources.getString(R.string.Breakfast_is_provided), "Breakfast is provided"))
        privateBBQGrillList.add(ModelSpinner(resources.getString(R.string.Long_term_stays_allowed), "Long-term stays allowed"))
        privateBBQGrillList.add(ModelSpinner(resources.getString(R.string.Allow_stays_of_28_days_or_more), "Allow stays of 28 days or more"))
        privateBBQGrillList.add(ModelSpinner(resources.getString(R.string.Self_check_in), "Self check-in"))
        privateBBQGrillList.add(ModelSpinner(resources.getString(R.string.Building_staff), "Building staff"))
        privateBBQGrillList.add(
            ModelSpinner(
                resources.getString(R.string.Someone_is_available_24_hours_a_day_to_let_guests_in),
                "Someone is available 24 hours a day to let guests in"
            )
        )
        privateBBQGrillList.add(ModelSpinner(resources.getString(R.string.Cleaning_available_during_stay), "Cleaning available during stay"))
        privateBBQGrillList.add(ModelSpinner(resources.getString(R.string.Not_included), "Not included"))



        for (i in privateBBQGrillList) {
            itemsNew.add(SearchableItem(i.text, i.value))
        }
        binding.spinnerPrivateBBQgrill.setOnClickListener {
            SearchableMultiSelectSpinner.show(
                context,
                resources.getString(R.string.Select_Items),
                resources.getString(R.string.Done),
                itemsNew,
                object :
                    SelectionCompleteListener {
                    override fun onCompleteSelection(selectedItems: ArrayList<SearchableItem>) {
                        Log.e("data", selectedItems.toString())
                        val selectedData = StringBuilder()
                        for (i in selectedItems) {
                            selectedData.append("${i.text}, ")
                            Log.e("chosenItems", i.text)
                            ammentiesData1.add(ModelSpinner(i.code, "1"))

                        }
                        binding.spinnerPrivateBBQgrill.text = selectedData.toString()

                    }

                })
        }
    }

    private fun spinnerUtilities() {
        val itemsNew: MutableList<SearchableItem> = ArrayList()
        utilitiesList.add(ModelSpinner(resources.getString(R.string.Kitchen), "Kitchen"))
        utilitiesList.add(ModelSpinner(resources.getString(R.string.Free_washer_In_unitFree_washer_In_unit), "Free washer – In unitFree washer – In unit"))
        utilitiesList.add(ModelSpinner(resources.getString(R.string.Dryer), "Dryer"))
        utilitiesList.add(ModelSpinner(resources.getString(R.string.Air_conditioningAir_conditioning), "Air conditioningAir conditioning"))
        utilitiesList.add(ModelSpinner(resources.getString(R.string.Smoke_alarmSmoke_alarm), "Smoke alarmSmoke alarm"))
        utilitiesList.add(ModelSpinner(resources.getString(R.string.Carbon_monoxide_alarmCarbon_monoxide_alarm), "Carbon monoxide alarmCarbon monoxide alarm"))




        for (i in utilitiesList) {
            itemsNew.add(SearchableItem(i.text, i.value))
        }
        binding.spinnerUtilities.setOnClickListener {
            SearchableMultiSelectSpinner.show(
                context,
                resources.getString(R.string.Select_Items),
                resources.getString(R.string.Done),
                itemsNew,
                object :
                    SelectionCompleteListener {
                    override fun onCompleteSelection(selectedItems: ArrayList<SearchableItem>) {
                        Log.e("data", selectedItems.toString())
                        val selectedData = StringBuilder()
                        for (i in selectedItems) {
                            selectedData.append("${i.text}, ")
                            Log.e("chosenItems", i.text)
                            ammentiesData1.add(ModelSpinner(i.code, "1"))

                        }
                        binding.spinnerUtilities.text = selectedData.toString()

                    }

                })
        }
    }

    private fun apiCallAddNewService() {
        if (selectedImageUri == null) {
            myToast(context, resources.getString(R.string.Select_an_Image_First))
            return
        }
        AppProgressBar.showLoaderDialog(context)
        val parcelFileDescriptor = contentResolver.openFileDescriptor(selectedImageUri!!, "r", null)

        val inputStream = FileInputStream(parcelFileDescriptor!!.fileDescriptor)
        val file = File(cacheDir, contentResolver.getFileName(selectedImageUri!!))
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)
        val body = UploadRequestBody(file, "image", this)
        ApiClient.apiService.addNewService(
            sessionManager.idToken.toString(),
            "translator",
            MultipartBody.Part.createFormData("image", file.name, body),
            binding.edtPrice.text.toString().trim(),
            binding.edtVideo.text.toString().trim(),
            serviceHourNew,
            binding.edtName.text.toString().trim(),
            binding.edtDescription.text.toString().trim(),
            multipleSelectedDate.toString(),
            "",
            "",
            translationTo,
            translationFrom,
            drone,
            sessionManager.longitude.toString(),
            sessionManager.latitude.toString(),
            "",
            binding.ServiceDate.text.toString().trim(),
        ).enqueue(object : Callback<ModelServiceList> {
            @SuppressLint("LogNotTimber")
            override fun onResponse(
                call: Call<ModelServiceList>, response: Response<ModelServiceList>
            ) {
                try {
                    if (response.code() == 500) {
                        myToast(context, resources.getString(R.string.Server_Error))
                        AppProgressBar.hideLoaderDialog()

                    } else if (response.code() == 404) {
                        myToast(context, resources.getString(R.string.Something_went_wrong))
                        AppProgressBar.hideLoaderDialog()

                    } else if (response.code() == 200) {
                        myToast(context, "${response.body()!!.message}")
                        AppProgressBar.hideLoaderDialog()
                        refresh()

                    } else {
                        myToast(context, "${response.body()!!.message}")
                        AppProgressBar.hideLoaderDialog()
                        onBackPressed()

                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                    myToast(context, resources.getString(R.string.Something_went_wrong))
                    AppProgressBar.hideLoaderDialog()
                }
            }

            override fun onFailure(call: Call<ModelServiceList>, t: Throwable) {
                   count++
                   if (count<= 3) {
                       Log.e("count", count.toString())
                       apiCallAddNewService()
                   } else {
                       myToast(context, t.message.toString())
                       AppProgressBar.hideLoaderDialog()

                   }
                 AppProgressBar.hideLoaderDialog()
            }

        })

    }

    private fun apiCallAddCar() {
        if (selectedImageUri == null) {
            myToast(context, resources.getString(R.string.Select_an_Image_First))
            return
        }
        AppProgressBar.showLoaderDialog(context)
        val parcelFileDescriptor = contentResolver.openFileDescriptor(selectedImageUri!!, "r", null)

        val inputStream = FileInputStream(parcelFileDescriptor!!.fileDescriptor)
        val file = File(cacheDir, contentResolver.getFileName(selectedImageUri!!))
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)
        val body = UploadRequestBody(file, "image", this)
        ApiClient.apiService.addCar(
            sessionManager.idToken.toString(),
            "car",
            binding.edtPrice.text.toString().trim(),
            binding.edtVideo.text.toString().trim(),
            serviceHourNew,
            binding.edtName.text.toString().trim(),
            binding.edtDescription.text.toString().trim(),
            multipleSelectedDate.toString(),
            "",
            "",
            travelPerson,
            carModel,
            carType,
            sessionManager.longitude.toString(),
            sessionManager.latitude.toString(),
            "1",
            drivingType,
            MultipartBody.Part.createFormData("image", file.name, body),


            ).enqueue(object : Callback<ModelServiceList> {
            @SuppressLint("LogNotTimber")
            override fun onResponse(
                call: Call<ModelServiceList>, response: Response<ModelServiceList>
            ) {
                try {
                    if (response.code() == 500) {
                        myToast(context, resources.getString(R.string.Server_Error))
                        AppProgressBar.hideLoaderDialog()

                    } else if (response.code() == 404) {
                        myToast(context, resources.getString(R.string.Something_went_wrong))
                        AppProgressBar.hideLoaderDialog()

                    } else if (response.code() == 200) {
                        myToast(context, "${response.body()!!.message}")
                        AppProgressBar.hideLoaderDialog()
                       // refresh()
                        onBackPressed()

                    } else {
                        myToast(context, "${response.body()!!.message}")
                        AppProgressBar.hideLoaderDialog()
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                    myToast(context, resources.getString(R.string.Something_went_wrong))
                    AppProgressBar.hideLoaderDialog()
                }
            }

            override fun onFailure(call: Call<ModelServiceList>, t: Throwable) {
                count1++
                if (count1 <= 3) {
                    Log.e("count", count1.toString())
                    apiCallAddCar()
                } else {
                    myToast(context, t.message.toString())
                    AppProgressBar.hideLoaderDialog()

                }
                 AppProgressBar.hideLoaderDialog()
            }

        })

    }

    private fun apiCallAddHome() {
        if (selectedImageUri == null) {
            myToast(context, resources.getString(R.string.Select_an_Image_First))
            return
        }
        AppProgressBar.showLoaderDialog(context)
        val parcelFileDescriptor = contentResolver.openFileDescriptor(selectedImageUri!!, "r", null)

        val inputStream = FileInputStream(parcelFileDescriptor!!.fileDescriptor)
        val file = File(cacheDir, contentResolver.getFileName(selectedImageUri!!))
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)
        val body = UploadRequestBody(file, "image", this)

        var mainCategory = arrayListOf("")
        var mainCategoryNew = arrayListOf("")


        for (i in ammentiesData) {
            mainCategory.add(i.text)
        }
        for (i in ammentiesData1) {
            mainCategoryNew.add(i.text)
        }
        ApiClient.apiService.addHome(
            sessionManager.idToken.toString(),
            "home",
            binding.edtPrice.text.toString().trim(),
            serviceHourNew,
            binding.edtName.text.toString().trim(),
            binding.edtDescription.text.toString().trim(),
            multipleSelectedDate.toString(),
            "",
            "",
            homeType,
            sessionManager.longitude.toString(),
            sessionManager.latitude.toString(),
            "1",
            mainCategory,
            mainCategoryNew,
            MultipartBody.Part.createFormData("image", file.name, body),

            ).enqueue(object : Callback<ModelServiceList> {
            @SuppressLint("LogNotTimber")
            override fun onResponse(
                call: Call<ModelServiceList>, response: Response<ModelServiceList>
            ) {
                try {
                    if (response.code() == 500) {
                        myToast(context, resources.getString(R.string.Server_Error))
                        AppProgressBar.hideLoaderDialog()

                    } else if (response.code() == 404) {
                        myToast(context, resources.getString(R.string.Something_went_wrong))
                        AppProgressBar.hideLoaderDialog()

                    } else if (response.code() == 200) {
                        myToast(context, "${response.body()!!.message}")
                        AppProgressBar.hideLoaderDialog()
                        refresh()

                    } else {
                        myToast(context, "${response.body()!!.message}")
                        AppProgressBar.hideLoaderDialog()
                        onBackPressed()

                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                    myToast(context, resources.getString(R.string.Something_went_wrong))
                    AppProgressBar.hideLoaderDialog()
                }
            }

            override fun onFailure(call: Call<ModelServiceList>, t: Throwable) {
                   count2++
                   if (count2<= 3) {
                       Log.e("count", count2.toString())
                       apiCallAddHome()
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

    private fun openImageChooser() {
        Intent(Intent.ACTION_PICK).also {
            it.type = "image/*"
            (MediaStore.ACTION_IMAGE_CAPTURE)
            val mimeTypes = arrayOf("image/jpeg", "image/png")
            it.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            startActivityForResult(it, REQUEST_CODE_IMAGE)
//
//        val pdfIntent = Intent(Intent.ACTION_GET_CONTENT)
//        pdfIntent.type = "application/pdf"
//        pdfIntent.addCategory(Intent.CATEGORY_OPENABLE)
//        startActivityForResult(pdfIntent, REQUEST_CODE_IMAGE)

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_IMAGE -> {
                    selectedImageUri = data?.data
                    Log.e("data?.data", data?.data.toString())
                    binding.tvChoice.setTextColor(Color.parseColor("#FF4CAF50"))
                    binding.tvChoice.text = resources.getString(R.string.Image_Selected)
                     if (sessionManager.usertype == "home") {
                        binding.layoutMainaentrance.visibility = View.VISIBLE

                    }

                    /*        when (choseFile) {
                                "1" -> {
                                    binding!!.NoFileChosen1.setTextColor(Color.parseColor("#FF4CAF50"))
                                    binding!!.NoFileChosen1.text = "Image Selected"
                                }
                                "2" -> {
                                    binding!!.NoFileChosen2.setTextColor(Color.parseColor("#FF4CAF50"))
                                    binding!!.NoFileChosen2.text = "Image Selected"

                                }
                                "3" -> {
                                    binding!!.NoFileChosen1R.setTextColor(Color.parseColor("#FF4CAF50"))
                                    binding!!.NoFileChosen1R.text = "Image Selected"
                                }
                                "4" -> {
                                    binding!!.NoFileChosen2R.setTextColor(Color.parseColor("#FF4CAF50"))
                                    binding!!.NoFileChosen2R.text = "Image Selected"
                                }
                            }*/

                    //binding.imageViewNew.visibility = View.VISIBLE
                    binding.imageView.setImageURI(selectedImageUri)
                }
            }
        }
    }

    companion object {
        const val REQUEST_CODE_IMAGE = 101
    }

    private fun ContentResolver.getFileName(selectedImageUri: Uri): String {
        var name = ""
        val returnCursor = this.query(selectedImageUri, null, null, null, null)
        if (returnCursor != null) {
            val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            returnCursor.moveToFirst()
            name = returnCursor.getString(nameIndex)
            returnCursor.close()

        }

        return name
    }

    override fun onProgressUpdate(percentage: Int) {

    }

    override fun onDestroy() {
        super.onDestroy()
        Dashboard.refreshLanNew=true
    }
}