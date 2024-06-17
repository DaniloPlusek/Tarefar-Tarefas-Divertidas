package br.com.plusekdanilo.tarefar_tarefasdivertidas

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val dbHelper = DatabaseHelper(application)
    private val _loginResult = MutableLiveData<Boolean>()
    val loginResult: LiveData<Boolean> = _loginResult

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    fun login(username: String, password: String) {
        uiScope.launch { // Usando uiScope aqui
            val cursor = dbHelper.getResponsavel(username)
            if (cursor.moveToFirst()) {
                val senhaIndex = cursor.getColumnIndex("Senha")
                if (senhaIndex != -1) {
                    val dbPassword = cursor.getString(senhaIndex)
                    _loginResult.postValue(dbPassword == password)
                } else {
                    _loginResult.postValue(false) // Coluna 'Senha' não existe
                }
            } else {
                _loginResult.postValue(false) // Usuário não encontrado
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel() // Cancela as corrotinas quando o ViewModel é limpo
    }
}