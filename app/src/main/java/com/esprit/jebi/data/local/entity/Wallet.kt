package com.esprit.jebi.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wallets")
data class Wallet(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val currency: String,
    val balance: Double,
    val type: String // e.g., Cash, Bank, Crypto
)
