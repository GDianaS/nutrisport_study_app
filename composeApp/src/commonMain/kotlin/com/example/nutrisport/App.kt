package com.example.nutrisport

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.mmk.kmpauth.google.GoogleAuthCredentials
import com.mmk.kmpauth.google.GoogleAuthProvider
import com.nutrisport.data.domain.CustomerRepository
import com.nutrisport.shared.navigation.Screen
import com.nutrisport.navigation.SetUpNavGraph
import com.nutrisport.shared.Constants
import org.jetbrains.compose.ui.tooling.preview.Preview

import org.koin.compose.koinInject

@Composable
@Preview
fun App() {
    MaterialTheme {

        // Usa o Koin para injetar automaticamente o CustomerRepository, que sabe se o usuário está autenticado ou não.
        val customerRepository = koinInject<CustomerRepository>()
        var appReady by remember { mutableStateOf(false) }
        val isUserAuthenticated = remember { customerRepository.getCurrentUserId() != null }
        val startDestination = remember {
            if (isUserAuthenticated) Screen.HomeGraph // autentificado
            else Screen.Auth //não autentificado
        }

        LaunchedEffect(Unit){
            GoogleAuthProvider.create(
                credentials = GoogleAuthCredentials(serverId = Constants.WEB_CLIENT_ID)
            )
            appReady = true
        }

        AnimatedVisibility(
            modifier = Modifier.fillMaxSize(),
            visible = appReady
        ) {
            SetUpNavGraph(startDestination = startDestination)
        }
    }
}