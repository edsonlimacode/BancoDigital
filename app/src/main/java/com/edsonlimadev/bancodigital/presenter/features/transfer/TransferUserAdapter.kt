package com.edsonlimadev.bancodigital.presenter.features.transfer

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
import com.edsonlimadev.bancodigital.data.model.User
import com.edsonlimadev.bancodigital.databinding.ItemTransactionBinding
import com.edsonlimadev.bancodigital.databinding.ItemUserContactBinding
import com.example.bancodigital.util.GetMask
import com.example.bancodigital.util.GetMask.Companion.DAY_MONTH_YEAR_HOUR_MINUTE
import com.example.bancodigital.util.GetMask.Companion.getFormatedDate
import com.squareup.picasso.Picasso


class TransferUserAdapter(
    private val userSelected: (User) -> Unit
) : ListAdapter<User, TransferUserAdapter.ViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(
                oldItem: User,
                newItem: User
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: User,
                newItem: User
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class ViewHolder(private val binding: ItemUserContactBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(user: User) {

            with(binding) {

                if (user.image.isNotEmpty()) {
                    Picasso.get().load(user.image).into(imgUserContact)
                } else {
                    imgUserContact.setImageResource(R.drawable.avatar_default)
                }

                textUserContactName.text = user.name
            }

            binding.clItemUserContact.setOnClickListener {
                userSelected(user)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemUserContactBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)
    }
}