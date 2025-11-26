package com.esprit.jebi.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.esprit.jebi.data.local.dao.BudgetDao
import com.esprit.jebi.data.local.dao.ReminderDao
import com.esprit.jebi.data.local.dao.TransactionDao
import com.esprit.jebi.data.local.dao.WalletDao
import com.esprit.jebi.data.local.entity.Budget
import com.esprit.jebi.data.local.entity.Reminder
import com.esprit.jebi.data.local.entity.Transaction
import com.esprit.jebi.data.local.entity.Wallet

@Database(entities = [Wallet::class, Transaction::class, Budget::class, Reminder::class], version = 4, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun walletDao(): WalletDao
    abstract fun transactionDao(): TransactionDao
    abstract fun budgetDao(): BudgetDao
    abstract fun reminderDao(): ReminderDao
}
