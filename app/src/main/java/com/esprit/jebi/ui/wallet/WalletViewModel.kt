package com.esprit.jebi.ui.wallet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.esprit.jebi.data.local.entity.Wallet
import com.esprit.jebi.data.repository.WalletRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WalletViewModel @Inject constructor(
    private val repository: WalletRepository
) : ViewModel() {

    val wallets: StateFlow<List<Wallet>> = repository.getAllWallets()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _selectedWallet = MutableStateFlow<Wallet?>(null)
    val selectedWallet: StateFlow<Wallet?> = _selectedWallet.asStateFlow()

    fun loadWallet(id: Int) {
        viewModelScope.launch {
            _selectedWallet.value = repository.getWalletById(id)
        }
    }

    fun addWallet(name: String, currency: String, balance: Double, type: String) {
        viewModelScope.launch {
            repository.insertWallet(Wallet(name = name, currency = currency, balance = balance, type = type))
        }
    }

    fun deleteWallet(wallet: Wallet) {
        viewModelScope.launch {
            repository.deleteWallet(wallet)
        }
    }

    fun updateWallet(wallet: Wallet) {
        viewModelScope.launch {
            repository.updateWallet(wallet)
            _selectedWallet.value = wallet
        }
    }
}
