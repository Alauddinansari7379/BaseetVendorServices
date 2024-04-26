package com.amtech.vendorservices.V.TranslatorServices.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.ContentResolver
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
  import com.amtech.vendorservices.R
import com.amtech.vendorservices.V.Dashboard.model.ModelSpinner
import com.amtech.vendorservices.V.Helper.AppProgressBar
import com.amtech.vendorservices.V.Helper.currentDate
import com.amtech.vendorservices.V.Helper.myToast
import com.amtech.vendorservices.V.TranslatorServices.activity.model.ModelServiceList
import com.amtech.vendorservices.V.retrofit.ApiClient
import com.amtech.vendorservices.databinding.ActivityAddNewTranslatorServicesBinding
import com.example.hhfoundation.Helper.ImageUploadClass.UploadRequestBody
import com.example.hhfoundation.sharedpreferences.SessionManager
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddNewTranslatorServices : AppCompatActivity(), UploadRequestBody.UploadCallback {
    private val binding by lazy {
        ActivityAddNewTranslatorServicesBinding.inflate(layoutInflater)
    }
    private val context = this@AddNewTranslatorServices
    private var statisticsList = ArrayList<ModelSpinner>()
    private var zoneList = ArrayList<ModelSpinner>()
    private var carTypeList = ArrayList<ModelSpinner>()
    private var carModelList = ArrayList<ModelSpinner>()
    private var travellingList = ArrayList<ModelSpinner>()
    private var serviceHour = ArrayList<ModelSpinner>()
    private var selectedImageUri: Uri? = null
    private var drone = "On Call"
    private var translationFrom = ""
    private var zone = ""
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
                    binding.tvAddNew.text = "Add New Service Car Rental"
                    binding.tvName.text = "Car Rental Name"
                    binding.tvDesc.text = "Car Rental Description"
                    binding.tvImage.text = "Car Image"
                    binding.tvHoure.text = "Days"
                    binding.layoutOnCall.visibility = View.GONE
                    binding.layoutTrasanlotor.visibility = View.GONE
                }

                "home" -> {
                    binding.tvTitle.text = "Home Service"
                    binding.tvAddNew.text = "Add New Service Home Rental"
                    binding.tvName.text = "Home Rental Name"
                    binding.tvDesc.text = "Home Rental Details"
                    binding.tvImage.text = "Home Image"
                    binding.layoutZone.visibility = View.GONE
                    binding.layoutOnCall.visibility = View.GONE
                    binding.layoutTrasanlotor.visibility = View.GONE
                    binding.layoutDrivingType.visibility = View.GONE

                }

                else -> {
                    binding.layoutZone.visibility = View.GONE
                    binding.layoutDrivingType.visibility = View.GONE

                }

            }
            ServiceDate.text = currentDate

            statisticsList.add(ModelSpinner("Turkish", "1"))
            statisticsList.add(ModelSpinner("Arabic", "1"))
            statisticsList.add(ModelSpinner("English", "1"))
            statisticsList.add(ModelSpinner("Russian", "1"))

            zoneList.add(ModelSpinner("India", "1"))
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

            mydilaog?.setCanceledOnTouchOutside(false)
            mydilaog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val newCalendar = Calendar.getInstance()
            val datePicker = DatePickerDialog(
                context,
                { _, year, monthOfYear, dayOfMonth ->
                    val newDate = Calendar.getInstance()
                    newDate[year, monthOfYear] = dayOfMonth
                    DateFormat.getDateInstance().format(newDate.time)
                    // val Date = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(newDate.time)
                    ServiceDate.text =
                        SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(newDate.time)


                    //Log.e(ContentValues.TAG, "ServiceDate:>>$ServiceDate.text")
                },
                newCalendar[Calendar.YEAR],
                newCalendar[Calendar.MONTH],
                newCalendar[Calendar.DAY_OF_MONTH]
            )
            //datePicker.datePicker.minDate = System.currentTimeMillis() - 1000;

            binding.ServiceDate.setOnClickListener {
                datePicker.show()
            }

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
                if (sessionManager.usertype=="car"){
                    apiCallAddCar()
                }else{
                    apiCallAddNewService()
                }
            }

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
                /*   count++
                   if (count<= 3) {
                       Log.e("count", count.toString())
                       apiCallUploadDet()
                   } else {
                       myToast(context, t.message.toString())
                       AppProgressBar.hideLoaderDialog()

                   }*/
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
            binding.ServiceDate.text.toString().trim(),
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
                /*   count++
                   if (count<= 3) {
                       Log.e("count", count.toString())
                       apiCallUploadDet()
                   } else {
                       myToast(context, t.message.toString())
                       AppProgressBar.hideLoaderDialog()

                   }*/
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