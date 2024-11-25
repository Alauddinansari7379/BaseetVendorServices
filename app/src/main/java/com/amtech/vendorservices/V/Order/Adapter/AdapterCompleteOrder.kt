package com.amtech.vendorservices.V.Order.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amtech.vendorservices.R
import com.amtech.vendorservices.V.Order.Adapter.AdapterAllOrder.VideoCall
import com.amtech.vendorservices.V.Order.Model.ModelOrderDetail.Data
import com.amtech.vendorservices.V.Order.activity.OrderDetails
import com.amtech.vendorservices.V.sharedpreferences.SessionManager
import com.amtech.vendorservices.databinding.SingleRowCompleteOrderListBinding


class AdapterCompleteOrder(
    val context: Context,
    var list: ArrayList<Data>, private val videoCall: VideoCall
 ) : RecyclerView.Adapter<AdapterCompleteOrder.ViewHolder>() {
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
                    binding.tvName.text =customer.f_name+" "+customer.l_name
                  //  binding.tvName.text = delivery_address.contact_person_name
                    binding.tvDate.text = created_at.subSequence(0, 11)
                    if (order_status == "delivered") {
                        binding.tvOrderStatus.text = context.resources.getString(R.string.Completed)
                    } else {
                        binding.tvOrderStatus.text = order_status
                    }
                     binding.tvPaymentStatus.text = payment_status
                     binding.tvType.text = food_type
                    if (food_type == "car")
                    {
//                        binding.llTrPDt.visibility = View.VISIBLE
//                        binding.tvTravellingPersons.text = pe
                    }
                    for (i in servrequests){
                        binding.tvTotal.text = "${i.price}$"

                    }

                    var type=""
                    var serDate=""
                    var docType=""
                    for (i in servrequests){
                         type = i.type
                        serDate = i.serv_date
                        docType = i.type
                     }
                    binding.tvServiceDate.text = serDate
                    binding.layoutAction.setOnClickListener {
                        val i = Intent(context, OrderDetails::class.java)
                            .putExtra("orderId",id.toString())
                            .putExtra("serDate",serDate)
                        context.startActivity(i)
                    }
                    if (sessionManager.usertype=="translator" && type=="On Call" && order_status=="confirmed"){
                        binding.layoutVideoCall.visibility= View.VISIBLE
                    }else{
                        binding.layoutVideoCall.visibility= View.GONE

                    }
                    binding.imgVideoCall.setOnClickListener {
                        videoCall.videoCall("Service$user_id")
                    }

                    if (docType == "Doc" && order_status == "delivered") {
                        binding.layoutUpload.visibility = View.VISIBLE
                    }
                    binding.btnUpload.setOnClickListener {
                        videoCall.upload(id.toString())
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
    interface VideoCall{
        fun videoCall(toString: String)
        fun upload(id: String)
    }
}
