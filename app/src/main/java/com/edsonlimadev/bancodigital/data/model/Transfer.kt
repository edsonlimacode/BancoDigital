package com.edsonlimadev.bancodigital.data.model

import java.util.UUID

data class Transfer(
    var id: String = "",
    val userSentId: String = "",
    val userReceiverId: String = "",
    val value: Float = 0f,
    var date: Long = 0
) {
    init {
        this.id = UUID.randomUUID().toString()
        this.date = System.currentTimeMillis()
    }
}
