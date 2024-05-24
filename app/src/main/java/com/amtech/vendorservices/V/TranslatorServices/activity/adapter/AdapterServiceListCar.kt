package com.amtech.vendorservices.V.TranslatorServices.activity.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amtech.vendorservices.R
import com.amtech.vendorservices.databinding.SingleRowServiceListBinding
import com.amtech.vendorservices.V.sharedpreferences.SessionManager
import com.squareup.picasso.Picasso


class AdapterServiceListCar(
    val context: Context,
    var list: ArrayList<com.amtech.vendorservices.V.TranslatorServices.activity.model.ModeCar.Product>,
 ) : RecyclerView.Adapter<AdapterServiceListCar.ViewHolder>() {
    lateinit var sessionManager: SessionManager

    inner class ViewHolder(val binding: SingleRowServiceListBinding) : RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SingleRowServiceListBinding.inflate(LayoutInflater.from(parent.context), parent, false)

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
                    binding.tvSrn.text = id.toString()
                    binding.tvName.text = name
                      binding.tvType.text = "Service"
                     binding.tvPrice.text = "$price$"
                    if (status==1){
                        binding.switchStatus.isChecked=true
                    }else{
                        binding.switchStatus.isChecked=false
                    }
                if (list[position].image != null) {
                     Picasso.get().load("https://baseet.thedemostore.in/storage/app/public/product/"+list[position].image)
                        .placeholder(R.drawable.user)
                        .error(R.drawable.error_placeholder)
                        .into(binding.imgProfile)

                    Log.i("ImgrURL",sessionManager.imageURL+list[position].image)

                }



                }
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
    }
//    interface Cart{
//        fun addToCart(toString: String)
//    }
}
