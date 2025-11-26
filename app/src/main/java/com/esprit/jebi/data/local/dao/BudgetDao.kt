package com.esprit.jebi.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.esprit.jebi.data.local.entity.Budget
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetDao {
    @Query("SELECT * FROM budgets WHERE month = :month AND year = :year")
    fun getBudgetsForMonth(month: Int, year: Int): Flow<List<Budget>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBudget(budget: Budget)

    @Update
    suspend fun updateBudget(budget: Budget)

    @Delete
    suspend fun deleteBudget(budget: Budget)
    
    @Query("SELECT * FROM budgets WHERE category = :category AND month = :month AND year = :year LIMIT 1")
    suspend fun getBudgetByCategory(category: String, month: Int, year: Int): Budget?
}
