package com.edsonlimadev.bancodigital.data.model

import com.edsonlimadev.bancodigital.data.enum.Operation
import com.edsonlimadev.bancodigital.data.enum.Type

data class Transaction(
    var id: String = "",
    var operation: Operation? = null,
    var type: Type? = null,
    var date: Long = 0,
    var value: Float = 0f
) {
    init {
        this.date = System.currentTimeMillis()
    }
}