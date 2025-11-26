package com.esprit.jebi.di

import android.content.Context
import androidx.room.Room
import com.esprit.jebi.data.local.AppDatabase
import com.esprit.jebi.data.local.dao.BudgetDao
import com.esprit.jebi.data.local.dao.ReminderDao
import com.esprit.jebi.data.local.dao.TransactionDao
import com.esprit.jebi.data.local.dao.WalletDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "jebi_database"
        )
        .fallbackToDestructiveMigration()
        .build()
    }

    @Provides
    fun provideWalletDao(database: AppDatabase): WalletDao {
        return database.walletDao()
    }

    @Provides
    fun provideTransactionDao(database: AppDatabase): TransactionDao {
        return database.transactionDao()
    }

    @Provides
    fun provideBudgetDao(database: AppDatabase): BudgetDao {
        return database.budgetDao()
    }

    @Provides
    fun provideReminderDao(database: AppDatabase): ReminderDao {
        return database.reminderDao()
    }
}
