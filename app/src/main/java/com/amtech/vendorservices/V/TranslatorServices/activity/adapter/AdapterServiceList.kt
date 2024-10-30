package com.amtech.vendorservices.V.TranslatorServices.activity.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amtech.vendorservices.R
import com.amtech.vendorservices.V.TranslatorServices.activity.UpdateServices
import com.amtech.vendorservices.V.TranslatorServices.activity.model.Product
import com.amtech.vendorservices.V.sharedpreferences.SessionManager
import com.amtech.vendorservices.databinding.SingleRowServiceListBinding
import com.squareup.picasso.Picasso


class AdapterServiceList(
    val context: Context,
    var list: ArrayList<Product>,
    val active: Active
) : RecyclerView.Adapter<AdapterServiceList.ViewHolder>() {
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
                    binding.tvType.text = sessionManager.usertype+context.resources.getString(R.string.Service)
                     binding.tvPrice.text = "$price$"
                    if (isActive == "0" || isActive == null) {
                        binding.switchStatus.isChecked = false
                    }else{
                        binding.switchStatus.isChecked = true
                    }
                if (list[position].image != null) {
                     Picasso.get().load("https://baseet.thedemostore.in/storage/app/public/product/"+list[position].image.substringBefore(","))
                        .placeholder(R.drawable.user)
                        .error(R.drawable.error_placeholder)
                        .into(binding.imgProfile)

                    Log.i("ImgrURL",sessionManager.imageURL+list[position].image)

                }
                    binding.switchStatus.setOnClickListener {
                        var statues = "0"
                        if (isActive=="0"|| isActive==null) {
                            statues = "1"
                        }
                        active.active(id.toString(), statues)
                    }

                    binding.layoutEdit.setOnClickListener {
                        val i = Intent(context, UpdateServices::class.java)
//                            .putExtra("callFrom", "Home")
//                            .putExtra("statues", binding.tvRequested.text.toString())
                            .putExtra("id", id.toString())
                        context.startActivity(i)
                    }



                }
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    interface Active {
        fun active(id: String, statues: String)
    }
}
