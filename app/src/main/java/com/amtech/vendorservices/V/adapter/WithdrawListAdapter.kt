package com.amtech.vendorservices.V.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amtech.vendorservices.V.activity.model.WithdrawRequest
import com.amtech.vendorservices.databinding.WithdrawlistItemBinding

class WithdrawListAdapter(private val withdrawList :  List<WithdrawRequest>) : RecyclerView.Adapter<WithdrawListAdapter.WithdrawListViewHolder>() {
    inner class WithdrawListViewHolder(val binding : WithdrawlistItemBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WithdrawListViewHolder {
        val binding = WithdrawlistItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return WithdrawListViewHolder(binding)
    }

    override fun getItemCount(): Int = withdrawList.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: WithdrawListViewHolder, position: Int) {
        with(holder.binding) {
            with(withdrawList[position]) {
                tvEarning.text = total_earning
                tvSlNo.text = "${position+1}"
                tvCommission.text = commission
                tvTotalWithdraw.text = total_withdrawn
                tvPendingWithdraw.text = pending_withdraw
                tvBankName.text = bank_name
            }
        }
    }
}