package com.esprit.jebi.ui.transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.esprit.jebi.data.local.entity.Transaction
import com.esprit.jebi.data.local.entity.TransactionType
import com.esprit.jebi.data.local.entity.Wallet
import com.esprit.jebi.data.repository.TransactionRepository
import com.esprit.jebi.data.repository.WalletRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val walletRepository: WalletRepository
) : ViewModel() {

    val wallets: StateFlow<List<Wallet>> = walletRepository.getAllWallets()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val transactions: StateFlow<List<Transaction>> = transactionRepository.getAllTransactions()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _uiState = MutableStateFlow(TransactionUiState())
    val uiState: StateFlow<TransactionUiState> = _uiState.asStateFlow()

    fun addTransaction(
        walletId: Int,
        amount: Double,
        type: TransactionType,
        category: String,
        note: String?
    ) {
        viewModelScope.launch {
            val transaction = Transaction(
                walletId = walletId,
                amount = amount,
                type = type,
                category = category,
                date = System.currentTimeMillis(),
                note = note
            )
            transactionRepository.insertTransaction(transaction)
            
            // Update wallet balance
            val wallet = walletRepository.getWalletById(walletId)
            if (wallet != null) {
                val newBalance = if (type == TransactionType.INCOME) {
                    wallet.balance + amount
                } else {
                    wallet.balance - amount
                }
                walletRepository.updateWallet(wallet.copy(balance = newBalance))
            }
        }
    }
}

data class TransactionUiState(
    val isLoading: Boolean = false,
    val error: String? = null
)
