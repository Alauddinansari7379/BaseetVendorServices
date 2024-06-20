package com.amtech.vendorservices.V.Order.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amtech.vendorservices.V.Order.Model.Data
import com.amtech.vendorservices.databinding.SingleRowSerRequestListBinding
import com.amtech.vendorservices.V.sharedpreferences.SessionManager


class AdapterSerRequestList(
    val context: Context,
    var list: ArrayList<Data>,
    val accept: Accept
 ) : RecyclerView.Adapter<AdapterSerRequestList.ViewHolder>() {
    lateinit var sessionManager: SessionManager

    inner class ViewHolder(val binding: SingleRowSerRequestListBinding) : RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SingleRowSerRequestListBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }
    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        sessionManager= SessionManager(context)
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
                    } else if (sessionManager.usertype == "home"){
                        binding.tvTypeTv.text="Home Type : "
                        binding.layoutDrivingType.visibility = View.GONE
                        binding.layoutLangauge.visibility = View.GONE

                    }else{
                        binding.layoutDrivingType.visibility = View.GONE

                    }

                    if (accept_by == "0") {
                        binding.btnAccept.visibility = View.VISIBLE
                        binding.tvWatingFor.visibility = View.GONE
                    } else {
                        binding.tvWatingFor.visibility = View.VISIBLE
                        binding.btnAccept.visibility = View.GONE

                    }
                    binding.btnAccept.setOnClickListener {
                        when (sessionManager.usertype) {
                            "car" -> {
                                requestIdNew=id.toString()
                                accept.accept(
                                    id.toString(),
                                    name!!,
                                    price!!,
                                    driv_type.toString(),
                                    trperson!!,
                                    type!!,
                                )
                            }
                            "home" -> {
                                requestIdNew=id.toString()

                                accept.accept(
                                    id.toString(),
                                    name!!,
                                    price!!,
                                    "driv_type"!!,
                                    trperson!!,
                                    type!!
                                )
                            }
                            else -> {
                                requestIdNew=id.toString()

                                accept.accept(
                                    id.toString(),
                                    name!!,
                                    price!!,
                                    tr_from!!,
                                    tr_to!!,
                                    type!!
                                )

                            }
                        }
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
        }catch (e:Exception){
            e.printStackTrace()
        }
    }
    interface Accept{
        fun accept(id: String,name:String,price:String,langFrom:String,langTo:String,tranServ:String)
    }
    companion object{
        var requestIdNew=""
    }
}
