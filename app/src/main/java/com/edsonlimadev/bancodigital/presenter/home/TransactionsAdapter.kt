package com.edsonlimadev.bancodigital.presenter.home

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.edsonlimadev.bancodigital.R
import com.edsonlimadev.bancodigital.data.enum.Operation
import com.edsonlimadev.bancodigital.data.enum.Type
import com.edsonlimadev.bancodigital.data.model.Transaction
import com.edsonlimadev.bancodigital.databinding.ItemTransactionBinding
import com.example.bancodigital.util.GetMask
import com.example.bancodigital.util.GetMask.Companion.DAY_MONTH_YEAR_HOUR_MINUTE
import com.example.bancodigital.util.GetMask.Companion.getFormatedDate
import com.example.bancodigital.util.GetMask.Companion.getFormatedValue


class TransactionsAdapter(
    private val context: Context,
    private val transactionSelected: (Transaction) -> Unit
) : ListAdapter<Transaction, TransactionsAdapter.ViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Transaction>() {
            override fun areItemsTheSame(
                oldItem: Transaction,
                newItem: Transaction
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: Transaction,
                newItem: Transaction
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class ViewHolder(private val binding: ItemTransactionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(transaction: Transaction) {

            with(binding) {

                transaction.operation?.let {
                    textTransactionTitle.text = Operation.getOperantion(it)
                    textRoundedTitle.text = Type.getType(it)
                    textRoundedTitle.backgroundTintList =
                        if (transaction.type == Type.CASH_IN) {
                            ColorStateList.valueOf(ContextCompat.getColor(context, R.color.green_500))
                        } else {
                            ColorStateList.valueOf(ContextCompat.getColor(context, R.color.red_500))
                        }
                }

                textTransactionDate.text =
                    getFormatedDate(transaction.date, DAY_MONTH_YEAR_HOUR_MINUTE)

                textTransactionValue.text =  context.getString(R.string.currency_simble, getFormatedValue(transaction.value))

                selectTransaction.setOnClickListener {
                    transactionSelected(transaction)
                }
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemTransactionBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val transaction = getItem(position)
        holder.bind(transaction)
    }
}