package com.esprit.jebi.data.repository

import com.esprit.jebi.data.local.dao.TransactionDao
import com.esprit.jebi.data.local.entity.Transaction
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TransactionRepository @Inject constructor(
    private val transactionDao: TransactionDao
) {
    fun getTransactionsForWallet(walletId: Int): Flow<List<Transaction>> =
        transactionDao.getTransactionsForWallet(walletId)

    fun getAllTransactions(): Flow<List<Transaction>> = transactionDao.getAllTransactions()

    suspend fun insertTransaction(transaction: Transaction) =
        transactionDao.insertTransaction(transaction)

    suspend fun updateTransaction(transaction: Transaction) =
        transactionDao.updateTransaction(transaction)

    suspend fun deleteTransaction(transaction: Transaction) =
        transactionDao.deleteTransaction(transaction)
        
    fun getTotalIncome(): Flow<Double?> = transactionDao.getTotalIncome()
    
    fun getTotalExpense(): Flow<Double?> = transactionDao.getTotalExpense()
}
