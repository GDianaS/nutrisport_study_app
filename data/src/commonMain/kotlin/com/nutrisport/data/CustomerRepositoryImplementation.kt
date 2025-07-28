package com.nutrisport.data

import com.nutrisport.data.domain.CustomerRepository
import com.nutrisport.shared.domain.Customer
import com.nutrisport.shared.util.RequestState
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseUser
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest

// Customer Repository: define o que o repositório deve fazer
// Customer Repostitory Implementation: implementa a lógica

class CustomerRepositoryImplementation: CustomerRepository {
    override fun getCurrentUserId(): String? {
        // se o usuário não for autentificado, retornar null
        return Firebase.auth.currentUser?.uid
    }

    // Salvar os dados do usuário no Firestore
    override suspend fun createCustomer(
        user: FirebaseUser?, // usuário autentifiado
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            if(user != null ){ // verifica se o usuário existe
                // Prepara a referência para a collection "customer"
                val customerCollection = Firebase.firestore.collection(collectionPath = "customer")
                // criar um objeto Customer
                val customer = Customer(
                    id = user.uid,
                    firstName = user.displayName?.split(" ")?.firstOrNull() ?: "Unknown",
                    lastName = user.displayName?.split(" ")?.lastOrNull() ?: "Unknown",
                    email = user.email ?: "Unknown",
                )

                // .document() : acessar um documento específico dentro da collection
                // .get : busca o documento
                // .exist : indica se está salvo no banco de dados ou não
                val customerExists = customerCollection.document(user.uid).get().exists

                if (customerExists){
                    onSuccess()
                }else{
                    customerCollection.document(user.uid).set(customer)
                    onSuccess()
                }

            } else{
                onError("User is not available.")
            }

        }catch (e: Exception) {
            onError("Error while creating a Customer: ${e.message}")
        }
    }

    // Flow: Se algo mudar no banco, ele avisa automaticamente -> Observar valores em tempo real
    override fun readCustomerFlow(): Flow<RequestState<Customer>> = channelFlow {
        try{
            val userId = getCurrentUserId()
            if(userId != null){ // verifica se o usuário está logado
                val database = Firebase.firestore
                database.collection(collectionPath = "customer") // acessa a coleção chamada de "customer"
                    .document(userId) // cada cliente está em um documento, que tem um ID
                    .snapshots // transformar o documento em flow
                    .collectLatest { document -> // coleta os dados
                        if(document.exists){
                            val customer = Customer(
                                id = document.id,
                                firstName = document.get(field = "firstName"),
                                lastName = document.get(field = "lastName"),
                                email = document.get(field = "email"),
                                city = document.get(field = "city"),
                                postalCode = document.get(field = "postalCode"),
                                address = document.get(field = "address"),
                                phoneNumber = document.get(field = "phoneNumber"),
                                cart = document.get(field = "cart"),
                            )

                            send(RequestState.Success(data = customer))

                        }else{
                            send(RequestState.Error("Queries customer document does not exist."))
                        }
                    }
            }else{
                send(RequestState.Error("User is not available."))
            }

        }catch (e: Exception){
            send(RequestState.Error("Error while reading a Customer information: ${e.message}"))
        }
    }


    // UpdateCustomer:
    // Tenta atualizar o documento do cliente no Firestore
    // Primeiro verifica se o usuário está logado
    // Depois verifica se o cliente já existe
    // Se sim → faz o update dos campos preenchidos
    // Se não → retorna erro ("Cliente não encontrado")
    // Se qualquer exceção acontecer → também retorna erro
    override suspend fun updateCustomer(
        customer: Customer,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            val userId = getCurrentUserId()
            if(userId != null){
                val firestore = Firebase.firestore
                val customerCollection = firestore.collection(collectionPath = "customer")

                val existingCustomer = customerCollection
                    .document(customer.id)
                    .get()
                if (existingCustomer.exists){
                    customerCollection
                        .document(customer.id)
                        .update(
                            // apenas os campos se quer atualizar
                            "firstName" to customer.firstName,
                            "lastName" to customer.lastName,
                            "city" to customer.city,
                            "postalCode" to customer.postalCode,
                            "address" to customer.address,
                            "phoneNumber" to customer.phoneNumber
                        )
                    onSuccess()
                }else{
                    onError("Customer not found.")
                }

            }else{
                onError("User is not available.")
            }

        }catch (e: Exception){
            onError("Error while updating a Customer information: ${e.message}")
        }
    }

    override suspend fun signOut(): RequestState<Unit> {
        // Sair da conta
        return try {
            Firebase.auth.signOut()
            RequestState.Success(data = Unit)
        }catch (e: Exception){
            RequestState.Error("Error while signing out: ${e.message}")
        }
    }
}