package com.amtech.vendorservices.V.Order.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.RecyclerView
import com.amtech.vendorservices.R
import com.amtech.vendorservices.V.Order.Model.Data
import com.amtech.vendorservices.V.sharedpreferences.SessionManager
import com.amtech.vendorservices.databinding.SingleRowSerRequestListBinding
import kotlin.reflect.jvm.internal.impl.descriptors.Visibilities


class AdapterSerRequestList(
    val context: Context,
    var list: ArrayList<Data>,
    val accept: Accept,
) : RecyclerView.Adapter<AdapterSerRequestList.ViewHolder>() {
    lateinit var sessionManager: SessionManager

    inner class ViewHolder(val binding: SingleRowSerRequestListBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SingleRowSerRequestListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        sessionManager = SessionManager(context)
        try {
            with(holder) {
                with(list[position]) {
                    binding.tvSNo.text = id.toString()
                    binding.tvName.text = name
                    binding.tvPrice.text = "$price$"
                    binding.tvServiceD.text = serv_date
                    binding.tvServiceH.text = serv_hour
                    binding.tvType.text = type
                    binding.tvToLang.text = tr_to
                    binding.tvFromLang.text = tr_from
                    binding.tvDesc.text = description
                    binding.tvDrivingType.text = driv_type

                    if (sessionManager.usertype == "car") {
                        binding.layoutDrivingType.visibility = View.VISIBLE
                        binding.layoutLangauge.visibility = View.GONE
                    } else if (sessionManager.usertype == "home") {
                        binding.tvTypeTv.text = context.resources.getString(R.string.Home_Type)
                        binding.layoutDrivingType.visibility = View.GONE
                        binding.layoutLangauge.visibility = View.GONE

                    } else {
                        binding.layoutDrivingType.visibility = View.GONE

                    }

                    if (from_ven_stts != "accept") {
                        binding.btnAccept.visibility = View.VISIBLE
                        binding.tvWatingFor.visibility = View.GONE
                    } else {
                        binding.tvWatingFor.visibility = View.VISIBLE
                        binding.btnAccept.visibility = View.GONE
                        binding.btnUpdate.visibility = View.GONE
                        binding.layoutEdit.visibility = View.GONE

                    }
                    binding.layoutEdit.setOnClickListener {
                        binding.etEditedPrice.visibility = View.VISIBLE
                        binding.etEditedPrice.setText(price)
                        binding.layoutEdit.visibility = View.GONE
                        binding.btnUpdate.visibility = View.VISIBLE
                        binding.tvPrice.visibility = View.GONE
                        binding.etEditedPrice.requestFocus()
                        val imm =
                            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.showSoftInput(binding.etEditedPrice, InputMethodManager.SHOW_IMPLICIT)
                    }
                    binding.btnUpdate.setOnClickListener {
                        if (binding.etEditedPrice.text!!.isEmpty()) {
                            binding.etEditedPrice.error = ("Please enter the price.")
                            binding.etEditedPrice.requestFocus()
                            return@setOnClickListener
                        } else {
                            accept.updatePricer(
                                whchserv,
                                binding.etEditedPrice.text.toString(),
                                position
                            )
                        }
                    }
                    binding.btnAccept.setOnClickListener {
                        //   accept.sendRequest(serv_id.toString())
                        accept.accept(id.toString(), ven_id, whchserv)

//                        when (sessionManager.usertype) {
//                            "car" -> {
//                                requestIdNew=id.toString()
//                                accept.accept(id.toString(), name!!, price!!, driv_type.toString(), trperson!!, type!!)
//                            }
//                            "home" -> {
//                                requestIdNew=id.toString()
//                                accept.accept(id.toString(), name!!, price!!,
//                                    "driv_type", trperson!!, type!!
//                                )
//                            }
//                            else -> {
//                                requestIdNew=id.toString()
//                                accept.accept(id.toString(), name!!, price!!, tr_from!!, tr_to!!, type!!)
//
//                            }
//                        }
                    }
                    binding.layoutDetail.setOnClickListener {
                        accept.showDetailsPopup(whchserv)
                    }
                    if (type == "Doc") {
                        binding.tvViewDoc.visibility = View.VISIBLE
                    }
                    binding.tvViewDoc.setOnClickListener {
                        accept.viewDoc(document.toString())
                    }
//
//                binding.btnAccept.setOnClickListener {
//                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse())
//                    context.startActivity(browserIntent)
//                }


//                if (list[position].preview != null) {
//                    Picasso.get().load("https:"+list[position].preview)
//                        .placeholder(R.drawable.placeholder_n)
//                        .error(R.drawable.error_placeholder)
//                        .into(binding.image)
//
//                }


                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    interface Accept {
        fun accept(id: String, ven_id: String, whchserv: String)
        fun sendRequest(id: String)
        fun viewDoc(url: String)
        fun showDetailsPopup(venId: String)
        fun updatePricer(venId: String, price: String, position: Int)
    }

    companion object {
        var requestIdNew = ""
    }
}
