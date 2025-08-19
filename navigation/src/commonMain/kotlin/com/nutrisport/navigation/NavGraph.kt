package com.nutrisport.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.nutrisport.admin_panel.AdminPanelScreen
import com.nutrisport.auth.AuthScreen
import com.nutrisport.profile.ProfileScreen
import com.nutrisport.home.HomeGraphScreen
import com.nutrisport.manage_product.ManageProductScreen
import com.nutrisport.shared.navigation.Screen

// navController : controla qual a tela visível
// NavHost: tela atual. Conforme se troca de tela, o seu conteúdo muda
// popUpTo → Define até onde “limpar a pilha de telas” (muito usado para evitar que o usuário volte para telas antigas, como login).
// navigateUp() → Volta uma tela (remove a tela atual e mostra a anterior).

@Composable
fun SetUpNavGraph(startDestination: Screen = Screen.Auth){
    // função de configuração de rotas
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = startDestination
    ){
        // ---------- Tela de Login (Auth) ----------
        composable<Screen.Auth> {
            AuthScreen(navigateToHome = {
                // Quando login é bem sucedido, vai para a tela Home
                navController.navigate(Screen.HomeGraph) {
                    popUpTo<Screen.Auth> { inclusive = true } // Remove da pilha de navegação -> isso impede que o usuário volte para a tela de login

                    }
                }
            )
        }

        // ---------- Tela principal (Home com BottomNav) ----------
        composable<Screen.HomeGraph>{
            HomeGraphScreen(
                navigateToAuth = {
                    navController.navigate(Screen.Auth) {
                        popUpTo<Screen.HomeGraph> { inclusive = true } // Remove da pilha de navegação -> isso impede que o usuário volte para a tela de login

                    }
                },
                navigateToProfile = {
                    // Abre a tela de perfil (fica em cima da Home)
                    navController.navigate(Screen.Profile)
                },
                navigatetoAdminPanel = {
                    // Abre o painel administrativo
                    navController.navigate(Screen.AdminPanel)
                }
            )
        }

        // ---------- Tela de Perfil ----------
        composable<Screen.Profile> {
            ProfileScreen(
                navigateBack = {
                    navController.navigateUp() // Remove the Profile Screen in top of Home Graph Screen
                }
            )
        }

        // ---------- Tela Painel Administrativo ----------
        composable<Screen.AdminPanel> {
            AdminPanelScreen (
                navigateBack = {
                    navController.navigateUp()
                },
                navigateToManageProduct = {id ->
                    navController.navigate(Screen.ManageProduct(id = id))
                }
            )
        }

        // ---------- Tela de Gerenciamento de Produto ----------
        composable<Screen.ManageProduct> {
            // Pega o ID passado como argumento (por exemplo: produto a ser editado)
            // o 'it' que carrega os argumentos que foram enviados para esta rota
            // it.toRoute<Screen.ManageProduct>().id: converte os argumentos recebidos de volta para o objeto Screen.ManageProduct e pega o campo id.
            val id = it.toRoute<Screen.ManageProduct>().id
            ManageProductScreen (
                //abre a tela de fato e passa o id.
                id = id ,
                navigateBack = {
                    navController.navigateUp()  // Fecha ManageProduct e volta para a tela anterior
                }
            )
        }

    }
}