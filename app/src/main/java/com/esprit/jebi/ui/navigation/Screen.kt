package com.esprit.jebi.ui.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Dashboard : Screen("dashboard")
    object AddWallet : Screen("add_wallet")
    object WalletDetail : Screen("wallet_detail/{walletId}") {
        fun createRoute(walletId: Int) = "wallet_detail/$walletId"
    }
    object TransactionHistory : Screen("transaction_history")
    object AddTransaction : Screen("add_transaction")
    object Budget : Screen("budget")
    object AddBudget : Screen("add_budget")
    object Reminder : Screen("reminder")
    object AddReminder : Screen("add_reminder")
    object Verification : Screen("verification")
}
