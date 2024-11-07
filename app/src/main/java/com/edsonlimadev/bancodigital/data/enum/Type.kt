package com.edsonlimadev.bancodigital.data.enum

enum class Type {

    CASH_IN, CASH_OUT;

    companion object {

        fun getType(operation: Operation): String {

            return when (operation) {
                Operation.DEPOSIT -> {
                    "D"
                }
                Operation.RECHARGE -> {
                    "R"
                }
                Operation.TRANSFER -> {
                    "T"
                }
            }
        }
    }
}