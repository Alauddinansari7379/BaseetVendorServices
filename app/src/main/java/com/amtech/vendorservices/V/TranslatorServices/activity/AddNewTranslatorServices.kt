package com.amtech.vendorservices.V.TranslatorServices.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.ContentResolver
import android.content.Intent
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
import com.aminography.primecalendar.civil.CivilCalendar
import com.aminography.primedatepicker.picker.PrimeDatePicker
import com.aminography.primedatepicker.picker.PrimeDatePicker.Companion.dialogWith
import com.aminography.primedatepicker.picker.callback.MultipleDaysPickCallback
import com.amtech.vendorservices.R
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
        with(binding) {
            imgBack.setOnClickListener {
                onBackPressed()
            }
            when (sessionManager.usertype) {
                "car" -> {
                    binding.tvTitle.text = "Car Service"
                    binding.tvAddNew.text = "Add New Car Rental Service"
                    binding.tvName.text = "Car Rental Name"
                    binding.tvDesc.text = "Car Rental Description"
                    binding.tvImage.text = "Car Image"
                    binding.tvHoure.text = "Days"
                    binding.layoutOnCall.visibility = View.GONE
                    binding.layoutTrasanlotor.visibility = View.GONE
                    binding.layoutHomeSpinner.visibility = View.GONE
                    binding.layoutServiceDateHome.visibility = View.GONE
                    binding.layoutHomeType.visibility = View.GONE
                }

                "home" -> {
                    binding.tvTitle.text = "Home Service"
                    binding.tvAddNew.text = "Add New Home Rental Service"
                    binding.tvName.text = "Home Rental Name"
                    binding.tvDesc.text = "Home Rental Details"
                    binding.tvImage.text = "Home Image"

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

            statisticsList.add(ModelSpinner("Turkish", "1"))
            statisticsList.add(ModelSpinner("Arabic", "1"))
            statisticsList.add(ModelSpinner("English", "1"))
            statisticsList.add(ModelSpinner("Russian", "1"))

            zoneList.add(ModelSpinner("India", "1"))
            zoneList.add(ModelSpinner("Trab Zone", "1"))
            zoneList.add(ModelSpinner("Turkey", "1"))
            zoneList.add(ModelSpinner("Saudi", "1"))

            carTypeList.add(ModelSpinner("Sedan", "1"))
            carTypeList.add(ModelSpinner("Coupe", "1"))
            carTypeList.add(ModelSpinner("Wagon", "1"))
            carTypeList.add(ModelSpinner("Hatchback", "1"))
            carTypeList.add(ModelSpinner("Off-road", "1"))
            carTypeList.add(ModelSpinner("SUV", "1"))
            carTypeList.add(ModelSpinner("Pickup", "1"))
            carTypeList.add(ModelSpinner("Cabriolet", "1"))
            carTypeList.add(ModelSpinner("Limbo", "1"))
            carTypeList.add(ModelSpinner("Minivan", "1"))


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
                            translationFrom = statisticsList[i].text
                        }
                    }

                    override fun onNothingSelected(adapterView: AdapterView<*>?) {

                    }
                }

            homeTypeList.add(ModelSpinner("1+1(1 Room + Hall)", "1"))
            homeTypeList.add(ModelSpinner("2+1(2 Room + Hall)", "1"))
            homeTypeList.add(ModelSpinner("3+1(3 Room + Hall)", "1"))
            homeTypeList.add(ModelSpinner("4+1(4 Room + Hall)", "1"))
            homeTypeList.add(ModelSpinner("5+1(5 Room + Hall)", "1"))
            homeTypeList.add(ModelSpinner("Villa", "1"))
            homeTypeList.add(ModelSpinner("Wood Home", "1"))



            mainEntranceList.add(ModelSpinner("The main Entrance", "1"))
            mainEntranceList.add(ModelSpinner("Balcony", "1"))
            mainEntranceList.add(ModelSpinner("The Kichen", "1"))
            mainEntranceList.add(ModelSpinner("The Lounge", "1"))
            mainEntranceList.add(ModelSpinner("The Bathroom", "1"))
            mainEntranceList.add(ModelSpinner("Master Bedroom", "1"))
            mainEntranceList.add(ModelSpinner("Bedroom", "1"))
            mainEntranceList.add(ModelSpinner("The Store", "1"))

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
                            maniEntrance = mainEntranceList[i].text
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
                            homeType = homeTypeList[i].text
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
                            zone = zoneList[i].text
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
                            carType = carTypeList[i].text
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
                            translationTo = statisticsList[i].text
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
                tvSerNameEn.text = " (EN)"
                tvDescEn.text = " (EN)"
                layoutEnglishLine.setBackgroundColor(resources.getColor(R.color.blue))
                layoutArbicLine.setBackgroundColor(Color.parseColor("#FFFFFF"))
            }
            tvArabic.setOnClickListener {
                tvSerNameEn.text = " (AR)"
                tvDescEn.text = " (AR)"
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
                    edtName.error = "Enter Name"
                    edtName.requestFocus()
                    return@setOnClickListener
                }
                if (edtDescription.text.toString().isEmpty()) {
                    edtDescription.error = "Enter Description"
                    edtDescription.requestFocus()
                    return@setOnClickListener
                }

                if (edtPrice.text.toString().isEmpty()) {
                    edtPrice.error = "Enter Price"
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

        }
        binding.ServiceDate.setOnClickListener {
            val today = CivilCalendar()
            val datePicker = dialogWith(today)
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
        scenicList.add(ModelSpinner("Mountain view", "1"))
        scenicList.add(ModelSpinner("Park view", "1"))

        for (i in scenicList) {
            itemsNew.add(SearchableItem(i.text, i.toString()))
        }
        binding.spinnerScenic.setOnClickListener {
            SearchableMultiSelectSpinner.show(
                context,
                "Select Items",
                "Done",
                itemsNew,
                object :
                    SelectionCompleteListener {
                    override fun onCompleteSelection(selectedItems: ArrayList<SearchableItem>) {
                        Log.e("data", selectedItems.toString())
                        val selectedData = StringBuilder()
                        for (i in selectedItems) {
                            selectedData.append("${i.text}, ")
                            ammentiesData.add(ModelSpinner(i.text, "1"))
                            Log.e("chosenItems", i.text)
                        }
                        binding.spinnerScenic.text = selectedData.toString()
                    }

                })
        }
    }

    private fun spinnerBathroom() {
        val itemsNew: MutableList<SearchableItem> = ArrayList()
        bathroomList.add(ModelSpinner("Hair dryer", "1"))
        bathroomList.add(ModelSpinner("Cleaning products", "1"))
        bathroomList.add(ModelSpinner("Shampoo", "1"))
        bathroomList.add(ModelSpinner("Body soap", "1"))
        bathroomList.add(ModelSpinner("Hot water", "1"))
        bathroomList.add(ModelSpinner("Bedroom and laundry", "1"))

        for (i in bathroomList) {
            itemsNew.add(SearchableItem(i.text, i.toString()))
        }
        binding.spinnerBathroom.setOnClickListener {
            SearchableMultiSelectSpinner.show(
                context,
                "Select Items",
                "Done",
                itemsNew,
                object :
                    SelectionCompleteListener {
                    override fun onCompleteSelection(selectedItems: ArrayList<SearchableItem>) {
                        Log.e("data", selectedItems.toString())
                        val selectedData = StringBuilder()
                        for (i in selectedItems) {
                            selectedData.append("${i.text}, ")
                            Log.e("chosenItems", i.text)
                            ammentiesData.add(ModelSpinner(i.text, "1"))

                        }
                        binding.spinnerBathroom.text = selectedData.toString()
                    }

                })
        }
    }

    private fun spinnerEssentials() {
        val itemsNew: MutableList<SearchableItem> = ArrayList()
        essentialsList.add(ModelSpinner("Towels, bed sheets", "1"))
        essentialsList.add(ModelSpinner("soap and toilet paper", "1"))
        essentialsList.add(ModelSpinner("Hangers", "1"))
        essentialsList.add(ModelSpinner("Bed linen", "1"))
        essentialsList.add(ModelSpinner("Cotton linen", "1"))
        essentialsList.add(ModelSpinner("Extra pillows & blankets", "1"))
        essentialsList.add(ModelSpinner("Room-darkening blinds", "1"))
        essentialsList.add(ModelSpinner("Iron", "1"))
        essentialsList.add(ModelSpinner("Clothes drying rack", "1"))


        for (i in essentialsList) {
            itemsNew.add(SearchableItem(i.text, i.toString()))
        }
        binding.spinnerEssentials.setOnClickListener {
            SearchableMultiSelectSpinner.show(
                context,
                "Select Items",
                "Done",
                itemsNew,
                object :
                    SelectionCompleteListener {
                    override fun onCompleteSelection(selectedItems: ArrayList<SearchableItem>) {
                        Log.e("data", selectedItems.toString())
                        val selectedData = StringBuilder()
                        for (i in selectedItems) {
                            selectedData.append("${i.text}, ")
                            Log.e("chosenItems", i.text)
                            ammentiesData.add(ModelSpinner(i.text, "1"))

                        }
                        binding.spinnerEssentials.text = selectedData.toString()
                    }

                })
        }
    }

    private fun spinnerClothesStorage() {
        val itemsNew: MutableList<SearchableItem> = ArrayList()
        clothesStorageList.add(ModelSpinner("Wardrobe and chest of drawers", "1"))
        clothesStorageList.add(ModelSpinner("Entertainment", "1"))
        clothesStorageList.add(
            ModelSpinner(
                "42 HDTV with Amazon Prime Video, Netflix, cable/satellite TV, Disney+, standard cable/satellite",
                "1"
            )
        )
        clothesStorageList.add(ModelSpinner("Family", "1"))
        clothesStorageList.add(
            ModelSpinner(
                "Clamp-on table seat high chair – available upon request",
                "1"
            )
        )
        clothesStorageList.add(ModelSpinner("With straps/harness and food tray", "1"))
        clothesStorageList.add(ModelSpinner("Children’s tableware", "1"))
        clothesStorageList.add(ModelSpinner("Window guards", "1"))
        clothesStorageList.add(ModelSpinner("Board games", "1"))
        clothesStorageList.add(ModelSpinner("Heating and cooling", "1"))
        clothesStorageList.add(ModelSpinner("Portable heater", "1"))
        clothesStorageList.add(ModelSpinner("Home safety", "1"))
        clothesStorageList.add(ModelSpinner("Security cameras on property", "1"))
        clothesStorageList.add(ModelSpinner("CCTVs are placed outside the property only", "1"))
        clothesStorageList.add(ModelSpinner("Fire extinguisher", "1"))
        clothesStorageList.add(ModelSpinner("First aid kit", "1"))
        clothesStorageList.add(ModelSpinner("Internet and office", "1"))
        clothesStorageList.add(ModelSpinner("Wifi – 20 Mbps Verified by speed test", "1"))
        clothesStorageList.add(ModelSpinner("Stream HD videos", "1"))
        clothesStorageList.add(ModelSpinner("Dedicated workspace", "1"))
        clothesStorageList.add(ModelSpinner("In a room with a door", "1"))
        clothesStorageList.add(ModelSpinner("Kitchen and dining", "1"))
        clothesStorageList.add(ModelSpinner("Microwave", "1"))
        clothesStorageList.add(ModelSpinner("Dishes and cutlery", "1"))
        clothesStorageList.add(ModelSpinner("Bowls, chopsticks, plates, cups, etc", "1"))
        clothesStorageList.add(ModelSpinner("Samsung 23L stainless steel oven", "1"))
        clothesStorageList.add(ModelSpinner("Kettle", "1"))
        clothesStorageList.add(ModelSpinner("Toaster", "1"))
        clothesStorageList.add(ModelSpinner("Dining table", "1"))
        clothesStorageList.add(ModelSpinner("Coffee", "1"))
        clothesStorageList.add(ModelSpinner("Bread maker", "1"))
        clothesStorageList.add(ModelSpinner("Location features", "1"))
        clothesStorageList.add(ModelSpinner("Private entrance", "1"))
        clothesStorageList.add(ModelSpinner("Separate street or building entrance", "1"))
        clothesStorageList.add(ModelSpinner("Outdoor", "1"))
        clothesStorageList.add(ModelSpinner("Private patio or balcony", "1"))
        clothesStorageList.add(ModelSpinner("Private back garden – Fully fenced", "1"))
        clothesStorageList.add(
            ModelSpinner(
                "An open space on the property usually covered in grass",
                "1"
            )
        )
        clothesStorageList.add(ModelSpinner("Outdoor furniture", "1"))



        for (i in clothesStorageList) {
            itemsNew.add(SearchableItem(i.text, i.toString()))
        }
        binding.spinnerClothesstorage.setOnClickListener {
            SearchableMultiSelectSpinner.show(
                context,
                "Select Items",
                "Done",
                itemsNew,
                object :
                    SelectionCompleteListener {
                    override fun onCompleteSelection(selectedItems: ArrayList<SearchableItem>) {
                        Log.e("data", selectedItems.toString())
                        val selectedData = StringBuilder()
                        for (i in selectedItems) {
                            selectedData.append("${i.text}, ")
                            Log.e("chosenItems", i.text)
                            ammentiesData1.add(ModelSpinner(i.text, "1"))

                        }
                        binding.spinnerClothesstorage.text = selectedData.toString()

                    }

                })
        }
    }

    private fun spinnerPrivateBBQGrill() {
        val itemsNew: MutableList<SearchableItem> = ArrayList()
        privateBBQGrillList.add(ModelSpinner("Electric", "1"))
        privateBBQGrillList.add(ModelSpinner("Parking and facilities", "1"))
        privateBBQGrillList.add(ModelSpinner("Free residential garage on premises – 1 space", "1"))
        privateBBQGrillList.add(ModelSpinner("Free on-street parking", "1"))
        privateBBQGrillList.add(ModelSpinner("Services", "1"))
        privateBBQGrillList.add(ModelSpinner("Luggage drop-off allowed", "1"))
        privateBBQGrillList.add(
            ModelSpinner(
                "For guests' convenience when they are arriving early or departing late",
                "1"
            )
        )
        privateBBQGrillList.add(ModelSpinner("Breakfast", "1"))
        privateBBQGrillList.add(ModelSpinner("Breakfast is provided", "1"))
        privateBBQGrillList.add(ModelSpinner("Long-term stays allowed", "1"))
        privateBBQGrillList.add(ModelSpinner("Allow stays of 28 days or more", "1"))
        privateBBQGrillList.add(ModelSpinner("Self check-in", "1"))
        privateBBQGrillList.add(ModelSpinner("Building staff", "1"))
        privateBBQGrillList.add(
            ModelSpinner(
                "Someone is available 24 hours a day to let guests in",
                "1"
            )
        )
        privateBBQGrillList.add(ModelSpinner("Cleaning available during stay", "1"))
        privateBBQGrillList.add(ModelSpinner("Not included", "1"))



        for (i in privateBBQGrillList) {
            itemsNew.add(SearchableItem(i.text, i.toString()))
        }
        binding.spinnerPrivateBBQgrill.setOnClickListener {
            SearchableMultiSelectSpinner.show(
                context,
                "Select Items",
                "Done",
                itemsNew,
                object :
                    SelectionCompleteListener {
                    override fun onCompleteSelection(selectedItems: ArrayList<SearchableItem>) {
                        Log.e("data", selectedItems.toString())
                        val selectedData = StringBuilder()
                        for (i in selectedItems) {
                            selectedData.append("${i.text}, ")
                            Log.e("chosenItems", i.text)
                            ammentiesData1.add(ModelSpinner(i.text, "1"))

                        }
                        binding.spinnerPrivateBBQgrill.text = selectedData.toString()

                    }

                })
        }
    }

    private fun spinnerUtilities() {
        val itemsNew: MutableList<SearchableItem> = ArrayList()
        utilitiesList.add(ModelSpinner("Kitchen", "1"))
        utilitiesList.add(ModelSpinner("Free washer – In unitFree washer – In unit", "1"))
        utilitiesList.add(ModelSpinner("Dryer", "1"))
        utilitiesList.add(ModelSpinner("Air conditioningAir conditioning", "1"))
        utilitiesList.add(ModelSpinner("Smoke alarmSmoke alarm", "1"))
        utilitiesList.add(ModelSpinner("Carbon monoxide alarmCarbon monoxide alarm", "1"))




        for (i in utilitiesList) {
            itemsNew.add(SearchableItem(i.text, i.toString()))
        }
        binding.spinnerUtilities.setOnClickListener {
            SearchableMultiSelectSpinner.show(
                context,
                "Select Items",
                "Done",
                itemsNew,
                object :
                    SelectionCompleteListener {
                    override fun onCompleteSelection(selectedItems: ArrayList<SearchableItem>) {
                        Log.e("data", selectedItems.toString())
                        val selectedData = StringBuilder()
                        for (i in selectedItems) {
                            selectedData.append("${i.text}, ")
                            Log.e("chosenItems", i.text)
                            ammentiesData1.add(ModelSpinner(i.text, "1"))

                        }
                        binding.spinnerUtilities.text = selectedData.toString()

                    }

                })
        }
    }

    private fun apiCallAddNewService() {
        if (selectedImageUri == null) {
            myToast(context, "Select an Image First")
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
                        myToast(context, "Server Error")
                        AppProgressBar.hideLoaderDialog()

                    } else if (response.code() == 404) {
                        myToast(context, "Something went wrong")
                        AppProgressBar.hideLoaderDialog()

                    } else if (response.code() == 200) {
                        myToast(context, "${response.body()!!.message}")
                        AppProgressBar.hideLoaderDialog()
                        refresh()

                    } else {
                        myToast(context, "${response.body()!!.message}")
                        AppProgressBar.hideLoaderDialog()
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                    myToast(context, "Something went wrong")
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
                myToast(context, "Something went wrong")
                AppProgressBar.hideLoaderDialog()
            }

        })

    }

    private fun apiCallAddCar() {
        if (selectedImageUri == null) {
            myToast(context, "Select an Image First")
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
                        myToast(context, "Server Error")
                        AppProgressBar.hideLoaderDialog()

                    } else if (response.code() == 404) {
                        myToast(context, "Something went wrong")
                        AppProgressBar.hideLoaderDialog()

                    } else if (response.code() == 200) {
                        myToast(context, "${response.body()!!.message}")
                        AppProgressBar.hideLoaderDialog()
                        refresh()

                    } else {
                        myToast(context, "${response.body()!!.message}")
                        AppProgressBar.hideLoaderDialog()
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                    myToast(context, "Something went wrong")
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
                myToast(context, "Something went wrong")
                AppProgressBar.hideLoaderDialog()
            }

        })

    }

    private fun apiCallAddHome() {
        if (selectedImageUri == null) {
            myToast(context, "Select an Image First")
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
                        myToast(context, "Server Error")
                        AppProgressBar.hideLoaderDialog()

                    } else if (response.code() == 404) {
                        myToast(context, "Something went wrong")
                        AppProgressBar.hideLoaderDialog()

                    } else if (response.code() == 200) {
                        myToast(context, "${response.body()!!.message}")
                        AppProgressBar.hideLoaderDialog()
                        refresh()

                    } else {
                        myToast(context, "${response.body()!!.message}")
                        AppProgressBar.hideLoaderDialog()
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                    myToast(context, "Something went wrong")
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
                myToast(context, "Something went wrong")
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
                    binding!!.tvChoice.setTextColor(Color.parseColor("#FF4CAF50"))
                    binding!!.tvChoice.text = "Image Selected"
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
}