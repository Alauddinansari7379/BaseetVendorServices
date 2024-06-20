package com.amtech.vendorservices.V.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.amtech.vendorservices.databinding.ActivityServiceUpdateBinding
import com.amtech.vendorservices.V.sharedpreferences.SessionManager

class ServiceUpdate : AppCompatActivity() {
    private val binding by lazy {
        ActivityServiceUpdateBinding.inflate(layoutInflater)
    }
    private val context=this@ServiceUpdate
    lateinit var sessionManager: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        with(binding) {
            imgBack.setOnClickListener {
                onBackPressed()
            }
            sessionManager= SessionManager(context)

           /* when (sessionManager.usertype) {
                "car" -> {
                    binding.tvTitle.text = "Car Service Update"
                    binding.tvImage.text = "Car Image"
                    binding.tvAvlabile.text = "Car Rental Available"

                }

                "home" -> {
                    binding.tvTitle.text = "Home Service Update"
                    binding.tvImage.text = "Home Image"
                    binding.tvAvlabile.text = "Home Rental Available"

                }else->{
                binding.tvAvlabile.text = "Translotor Service Available"

            }
            }

            radioOnCall.setOnCheckedChangeListener { _, _ ->
                if (radioOnCall.isChecked) {
                    radioInPerson.isChecked = false
                    radioDoc.isChecked = false

//                    follow=""
//                    layoutTrea.visibility = View.VISIBLE
//                    layoutNotTreated.visibility = View.GONE
//                    layoutNotTreated.visibility = View.GONE

                }

            }


            radioInPerson.setOnCheckedChangeListener { _, _ ->
                if (radioInPerson.isChecked) {
                    radioOnCall.isChecked = false
                    radioDoc.isChecked = false
//                    follow= "Not Following the Treatment"
//                    layoutNotTreated.visibility = View.VISIBLE
//                    layoutTrea.visibility = View.GONE
//                    layoutNotTreated.visibility = View.VISIBLE

                }

                radioDoc.setOnCheckedChangeListener { _, _ ->
                    if (radioDoc.isChecked) {
                        radioOnCall.isChecked = false
                        radioInPerson.isChecked = false
//                    follow= "Not Following the Treatment"
//                    layoutNotTreated.visibility = View.VISIBLE
//                    layoutTrea.visibility = View.GONE
//                    layoutNotTreated.visibility = View.VISIBLE

                    }
                }

            }
        }*/
    }
    }
}