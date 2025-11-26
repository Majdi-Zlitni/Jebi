package com.esprit.jebi.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.esprit.jebi.data.local.entity.Wallet
import kotlinx.coroutines.flow.Flow

@Dao
interface WalletDao {
    @Query("SELECT * FROM wallets")
    fun getAllWallets(): Flow<List<Wallet>>

    @Query("SELECT * FROM wallets WHERE id = :id")
    suspend fun getWalletById(id: Int): Wallet?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWallet(wallet: Wallet)

    @Update
    suspend fun updateWallet(wallet: Wallet)

    @Delete
    suspend fun deleteWallet(wallet: Wallet)
}
