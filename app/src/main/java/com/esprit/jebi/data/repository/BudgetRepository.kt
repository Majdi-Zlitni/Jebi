package com.esprit.jebi.data.repository

import com.esprit.jebi.data.local.dao.BudgetDao
import com.esprit.jebi.data.local.entity.Budget
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BudgetRepository @Inject constructor(
    private val budgetDao: BudgetDao
) {
    fun getBudgetsForMonth(month: Int, year: Int): Flow<List<Budget>> =
        budgetDao.getBudgetsForMonth(month, year)

    suspend fun insertBudget(budget: Budget) = budgetDao.insertBudget(budget)

    suspend fun updateBudget(budget: Budget) = budgetDao.updateBudget(budget)

    suspend fun deleteBudget(budget: Budget) = budgetDao.deleteBudget(budget)
    
    suspend fun getBudgetByCategory(category: String, month: Int, year: Int): Budget? =
        budgetDao.getBudgetByCategory(category, month, year)
}
