package com.nutrisport.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nutrisport.admin_panel.AdminPanelScreen
import com.nutrisport.auth.AuthScreen
import com.nutrisport.profile.ProfileScreen
import com.nutrisport.home.HomeGraphScreen
import com.nutrisport.shared.navigation.Screen

// navController : controla qual a tela visível
// NavHost: tela atual. Conforme se troca de tela, o seu conteúdo muda

@Composable
fun SetUpNavGraph(startDestination: Screen = Screen.Auth){
    // função de configuração de rotas
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = startDestination
    ){
        composable<Screen.Auth> {
            AuthScreen(navigateToHome = {
                navController.navigate(Screen.HomeGraph) {
                    popUpTo<Screen.Auth> { inclusive = true } // Remove da pilha de navegação -> isso impede que o usuário volte para a tela de login

                    }
                }
            )
        }

        composable<Screen.HomeGraph>{
            HomeGraphScreen(
                navigateToAuth = {
                    navController.navigate(Screen.Auth) {
                        popUpTo<Screen.HomeGraph> { inclusive = true } // Remove da pilha de navegação -> isso impede que o usuário volte para a tela de login

                    }
                },
                navigateToProfile = {
                    navController.navigate(Screen.Profile)
                },
                navigatetoAdminPanel = {
                    navController.navigate(Screen.AdminPanel)
                }
            )
        }

        composable<Screen.Profile> {
            ProfileScreen(
                navigateBack = {
                    navController.navigateUp() // Remove the Profile Screen in top of Home Graph Screen
                }
            )
        }

        composable<Screen.AdminPanel> {
            AdminPanelScreen (
                navigateBack = {
                    navController.navigateUp() // Remove the Profile Screen in top of Home Graph Screen
                }
            )
        }

    }
}