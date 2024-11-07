package com.edsonlimadev.bancodigital.data.model

import java.util.UUID

data class Recharge(
    var id: String = "",
    var value: Float = 0f,
    var date: Long = 0,
    var number: String = ""
) {
    init {
        this.id = UUID.randomUUID().toString()
        this.date = System.currentTimeMillis()
    }
}