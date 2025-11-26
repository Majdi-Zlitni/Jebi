package com.esprit.jebi.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.esprit.jebi.ui.auth.LoginScreen
import com.esprit.jebi.ui.auth.RegisterScreen
import com.esprit.jebi.ui.wallet.DashboardScreen
import com.esprit.jebi.ui.wallet.AddWalletScreen
import com.esprit.jebi.ui.wallet.WalletDetailScreen

import com.esprit.jebi.ui.transaction.AddTransactionScreen
import com.esprit.jebi.ui.transaction.TransactionHistoryScreen
import com.esprit.jebi.ui.budget.AddBudgetScreen
import com.esprit.jebi.ui.budget.BudgetScreen
import com.esprit.jebi.ui.reminder.AddReminderScreen
import com.esprit.jebi.ui.reminder.ReminderScreen

import com.esprit.jebi.ui.auth.VerificationScreen

@Composable
fun JebiNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Login.route) {
        composable(Screen.Login.route) {
            LoginScreen(
                onNavigateToRegister = { navController.navigate(Screen.Register.route) },
                onNavigateToDashboard = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToVerification = {
                    navController.navigate(Screen.Verification.route)
                }
            )
        }
        composable(Screen.Register.route) {
            RegisterScreen(
                onNavigateToLogin = { navController.popBackStack() },
                onNavigateToVerification = {
                    navController.navigate(Screen.Verification.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToDashboard = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.Verification.route) {
            VerificationScreen(
                onNavigateToDashboard = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Verification.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.Dashboard.route) {
            DashboardScreen(
                onNavigateToAddWallet = { navController.navigate(Screen.AddWallet.route) },
                onNavigateToWalletDetail = { walletId ->
                    navController.navigate(Screen.WalletDetail.createRoute(walletId))
                },
                onNavigateToTransactions = { navController.navigate(Screen.TransactionHistory.route) },
                onNavigateToBudgets = { navController.navigate(Screen.Budget.route) },
                onNavigateToReminders = { navController.navigate(Screen.Reminder.route) }
            )
        }
        composable(Screen.AddWallet.route) {
            AddWalletScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(Screen.WalletDetail.route) { backStackEntry ->
            val walletId = backStackEntry.arguments?.getString("walletId")?.toIntOrNull()
            if (walletId != null) {
                WalletDetailScreen(walletId = walletId, onNavigateBack = { navController.popBackStack() })
            }
        }
        composable(Screen.TransactionHistory.route) {
            TransactionHistoryScreen(navController = navController)
        }
        composable(Screen.AddTransaction.route) {
            AddTransactionScreen(navController = navController)
        }
        composable(Screen.Budget.route) {
            BudgetScreen(navController = navController)
        }
        composable(Screen.AddBudget.route) {
            AddBudgetScreen(navController = navController)
        }
        composable(Screen.Reminder.route) {
            ReminderScreen(navController = navController)
        }
        composable(Screen.AddReminder.route) {
            AddReminderScreen(navController = navController)
        }
    }
}
