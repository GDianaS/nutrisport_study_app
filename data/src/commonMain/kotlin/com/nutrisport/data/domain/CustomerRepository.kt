package com.nutrisport.data.domain

import com.nutrisport.shared.domain.Customer
import com.nutrisport.shared.util.RequestState
import dev.gitlive.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

// Funções declaradas aqui devem ser Implementadas em CustomerRepositoryImplementation
interface CustomerRepository {

    // verificar se o usuario está autentificado
    fun getCurrentUserId(): String?

    suspend fun createCustomer(
        user: FirebaseUser?,
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
    )

    fun readCustomerFlow(): Flow<RequestState<Customer>>

    // Salvar as alterações do formulário de perfil do usuário no Firestore
    suspend fun updateCustomer(
        customer: Customer,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    )

    // Sign out user
    suspend fun signOut(): RequestState<Unit>
}