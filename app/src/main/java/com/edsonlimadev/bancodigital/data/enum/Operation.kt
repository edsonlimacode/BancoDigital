package com.edsonlimadev.bancodigital.data.enum

enum class Operation {

    DEPOSIT,
    RECHARGE,
    TRANSFER;

    companion object {

        fun getOperantion(operation: Operation): String {
            return when (operation) {
                DEPOSIT -> {
                    "DEPOSITO"
                }
                RECHARGE -> {
                    "RECARGA"
                }
                TRANSFER -> {
                    "TRANSFERÊNCIA"
                }
            }
        }

    }

}