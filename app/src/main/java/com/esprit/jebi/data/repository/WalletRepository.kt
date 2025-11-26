package com.esprit.jebi.data.repository

import com.esprit.jebi.data.local.dao.WalletDao
import com.esprit.jebi.data.local.entity.Wallet
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WalletRepository @Inject constructor(private val walletDao: WalletDao) {

    fun getAllWallets(): Flow<List<Wallet>> = walletDao.getAllWallets()

    suspend fun getWalletById(id: Int): Wallet? = walletDao.getWalletById(id)

    suspend fun insertWallet(wallet: Wallet) = walletDao.insertWallet(wallet)

    suspend fun updateWallet(wallet: Wallet) = walletDao.updateWallet(wallet)

    suspend fun deleteWallet(wallet: Wallet) = walletDao.deleteWallet(wallet)
}
