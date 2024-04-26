package com.amtech.vendorservices.V.Order.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.amtech.vendorservices.R
import com.amtech.vendorservices.V.Order.Model.ModelRelatedSer.Data
import com.amtech.vendorservices.V.Order.Model.ModelRelatedSer.ModelServiceRet
import com.amtech.vendorservices.databinding.SingleRowRelatedServiceBinding
import com.amtech.vendorservices.databinding.SingleRowSerRequestListBinding
import com.example.hhfoundation.sharedpreferences.SessionManager
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
                 binding.name.text = name
                binding.price.text = "$price$"
                binding.serviceDate.text = dates
                binding.serviceHour.text = ser_hour.toString()
                binding.serviceType.text = drone
                binding.toLan.text = tr_to
                binding.fromLan.text = tr_from
                binding.description.text = description


                binding.btnSendService.setOnClickListener {
                    sendRequest.sendRequest(foodid.toString())
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
    }
}
