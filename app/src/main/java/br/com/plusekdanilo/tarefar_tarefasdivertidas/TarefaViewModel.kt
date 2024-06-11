package br.com.plusekdanilo.tarefar_tarefasdivertidas

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class TarefaViewModel(application: Application) : AndroidViewModel(application) {
    private val tarefaDAO: TarefaDAO
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    init {
        val db = AppDatabase.getDatabase(application)
        tarefaDAO = db.tarefaDAO()
    }

    val tarefas: LiveData<List<Tarefa>> = tarefaDAO.getAllTarefas()

    fun insertTarefa(tarefa: Tarefa) {
        uiScope.launch {
            tarefaDAO.insertTarefa(tarefa)
        }
    }

    fun deleteTarefa(id: Int) {
        uiScope.launch {
            tarefaDAO.deleteTarefa(id)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel() // Cancel the coroutine when ViewModel is cleared
    }
}