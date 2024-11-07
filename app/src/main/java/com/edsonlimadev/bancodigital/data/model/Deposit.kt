package com.edsonlimadev.bancodigital.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
data class Deposit(
    var id: String = "",
    var amount: Float = 0f,
    var date: Long = 0
) : Parcelable {
    init {
        this.id = UUID.randomUUID().toString()
    }
}