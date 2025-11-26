package com.esprit.jebi.ui.budget

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.esprit.jebi.data.local.entity.Budget
import com.esprit.jebi.data.local.entity.TransactionType
import com.esprit.jebi.data.repository.BudgetRepository
import com.esprit.jebi.data.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class BudgetViewModel @Inject constructor(
    private val budgetRepository: BudgetRepository,
    private val transactionRepository: TransactionRepository
) : ViewModel() {

    private val currentMonth = Calendar.getInstance().get(Calendar.MONTH)
    private val currentYear = Calendar.getInstance().get(Calendar.YEAR)

    val budgets: StateFlow<List<BudgetWithProgress>> = combine(
        budgetRepository.getBudgetsForMonth(currentMonth, currentYear),
        transactionRepository.getAllTransactions()
    ) { budgets, transactions ->
        budgets.map { budget ->
            val spent = transactions
                .filter { 
                    it.type == TransactionType.EXPENSE && 
                    it.category == budget.category &&
                    isSameMonth(it.date, currentMonth, currentYear)
                }
                .sumOf { it.amount }
            BudgetWithProgress(budget, spent)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addBudget(category: String, amount: Double) {
        viewModelScope.launch {
            val budget = Budget(
                category = category,
                amount = amount,
                month = currentMonth,
                year = currentYear
            )
            budgetRepository.insertBudget(budget)
        }
    }

    private fun isSameMonth(dateMillis: Long, month: Int, year: Int): Boolean {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = dateMillis
        return calendar.get(Calendar.MONTH) == month && calendar.get(Calendar.YEAR) == year
    }
}

data class BudgetWithProgress(
    val budget: Budget,
    val spent: Double
)
