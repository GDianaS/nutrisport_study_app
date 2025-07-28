package com.nutrisport.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nutrisport.data.domain.CustomerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// View Model: ponte entre a tela e os dados
class HomeGraphViewModel(
    private val customerRepository: CustomerRepository
): ViewModel() {
    fun signOut(
        onSucess: () -> Unit,
        onError: (String) -> Unit
    ){
        viewModelScope.launch {
            //Inicia uma tarefa "em segundo plano"
            //Manda a tarefa de logout para uma fila especial de tarefas pesadas ou lentas.
            val result = withContext(Dispatchers.IO){
                customerRepository.signOut()
            }
            if (result.isSuccess()) {
                onSucess()
            } else if (result.isError()) {
                onError(result.getErrorMessage())
            }
        }

    }
}