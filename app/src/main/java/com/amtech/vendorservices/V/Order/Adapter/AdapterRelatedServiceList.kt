package com.amtech.vendorservices.V.Order.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amtech.vendorservices.R
import com.amtech.vendorservices.V.Order.Model.ModelRelatedSer.Data
import com.amtech.vendorservices.databinding.SingleRowRelatedServiceBinding
import com.amtech.vendorservices.V.sharedpreferences.SessionManager
import com.squareup.picasso.Picasso


class AdapterRelatedServiceList(
    val context: Context,
    var list: ArrayList<Data>,
    val sendRequest: SendRequest
  ) : RecyclerView.Adapter<AdapterRelatedServiceList.ViewHolder>() {
    lateinit var sessionManager: SessionManager

    inner class ViewHolder(val binding: SingleRowRelatedServiceBinding) : RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SingleRowRelatedServiceBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }
    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        sessionManager= SessionManager(context)
        with(holder){
            with(list[position]){
                when (sessionManager.usertype) {
                    "car" -> {
                        binding.tvDrivingType.text="Driving Type : "
                        binding.tvCarType.text="Car Type : "
                        binding.tvTravlingPer.text="Travelling Person : "
                        binding.tvServiceH.text="Days : "

                        binding.name.text = name
                        binding.price.text = "$price$"
                        binding.serviceDate.text = servicesdates
                        binding.serviceHour.text = days
                        binding.serviceType.text = trperson
                        binding.toLan.text = car_type
                        binding.fromLan.text = driv_type
                        binding.description.text = description
                        binding.edtUpdatePrice.setText(price)
                    }
                    "home" -> {
                        binding.tvCarType.text="Home Type : "
                        binding.name.text = name
                        binding.price.text = "$price$"
                        binding.description.text = description
                        binding.toLan.text = home_type
                        binding.edtUpdatePrice.setText(price)

                        binding.layoutDate.visibility=View.GONE
                        binding.layoutServiceType.visibility=View.GONE
                        binding.layoutTrasanlotorFrom.visibility=View.GONE
                        binding.layoutVideo.visibility=View.GONE
                        if (days!=null){
                            binding.serviceHour.text = days
                        }

                    }
                    else -> {
                        binding.name.text = name
                        binding.price.text = "$price$"
                        binding.serviceDate.text = dates
                        binding.serviceHour.text = ser_hour.toString()
                        binding.serviceType.text = drone
                        binding.toLan.text = tr_to
                        binding.fromLan.text = tr_from
                        binding.description.text = description
                        binding.edtUpdatePrice.setText(price)

                    }
                }



                binding.btnSendService.setOnClickListener {
                    sendRequest.sendRequest(foodid.toString())
                }

                binding.btnUpdatePrice.setOnClickListener {
                    sendRequest.updatePrice(foodid.toString(),binding.edtUpdatePrice.text.toString())
                }

                binding.tvPortfolio.setOnClickListener {
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(port_video))
                    context.startActivity(browserIntent)
                }


                if (list[position].image != null) {
                    Picasso.get().load(sessionManager.imageURL+list[position].image)
                        .placeholder(R.drawable.user)
                        .error(R.drawable.error_placeholder)
                        .into(binding.imageView)

                }


             }
        }
    }
    interface SendRequest{
        fun sendRequest(id: String,)
        fun updatePrice(id: String,price:String)
    }
}
