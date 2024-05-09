package com.amtech.vendorservices.V.Order.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amtech.vendorservices.V.Helper.pmFormate
import com.amtech.vendorservices.V.Order.Model.MAllOrder.ModelAllOrderItem
import com.amtech.vendorservices.V.Order.activity.OrderDetails
import com.amtech.vendorservices.databinding.SingleRowCompleteOrderListBinding
import com.example.hhfoundation.sharedpreferences.SessionManager
import java.text.SimpleDateFormat
import java.util.Locale


class AdapterAllOrder(
    val context: Context,
    var list: ArrayList<ModelAllOrderItem>,
 ) : RecyclerView.Adapter<AdapterAllOrder.ViewHolder>() {
    lateinit var sessionManager: SessionManager

    inner class ViewHolder(val binding: SingleRowCompleteOrderListBinding) : RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SingleRowCompleteOrderListBinding.inflate(LayoutInflater.from(parent.context), parent, false)

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
                  //  binding.tvName.text = delivery_address.contact_person_name
                    binding.tvName.text =customer.f_name+" "+customer.l_name
                    binding.tvServiceDate.text = pmFormate(schedule_at)
                    binding.tvDate.text = created_at.subSequence(0, 11)
                    binding.tvOrderStatus.text = order_status
                    binding.tvPaymentStatus.text = payment_status
                    binding.tvTotal.text = "$order_amount$"

                    binding.layoutAction.setOnClickListener {
                        val i = Intent(context, OrderDetails::class.java)
                            .putExtra("orderId",id.toString())
                            .putExtra("orderStatus",order_status)
                        context.startActivity(i)
                     }


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

//    interface Cart{
//        fun addToCart(toString: String)
//    }
}
