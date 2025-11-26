package com.esprit.jebi

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.esprit.jebi.ui.navigation.JebiNavGraph
import com.esprit.jebi.ui.theme.JebiTheme
import dagger.hilt.android.AndroidEntryPoint

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.esprit.jebi.ui.navigation.Screen
import com.esprit.jebi.util.SessionManager
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : FragmentActivity() {
    
    @Inject lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JebiTheme {
                val navController = rememberNavController()
                val isSessionValid by sessionManager.isSessionValid.collectAsState()
                
                LaunchedEffect(isSessionValid) {
                    if (!isSessionValid) {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    JebiNavGraph(navController = navController)
                }
            }
        }
    }
    
    override fun onResume() {
        super.onResume()
        sessionManager.checkSession()
    }
    
    override fun onUserInteraction() {
        super.onUserInteraction()
        sessionManager.updateActivity()
    }
}
