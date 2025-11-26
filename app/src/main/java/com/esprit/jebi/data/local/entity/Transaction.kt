package com.esprit.jebi.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "transactions",
    foreignKeys = [
        ForeignKey(
            entity = Wallet::class,
            parentColumns = ["id"],
            childColumns = ["walletId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [androidx.room.Index(value = ["walletId"])]
)
data class Transaction(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val walletId: Int,
    val amount: Double,
    val type: TransactionType, // INCOME, EXPENSE, TRANSFER
    val category: String,
    val date: Long, // Store as timestamp
    val note: String? = null
)

enum class TransactionType {
    INCOME, EXPENSE, TRANSFER
}
