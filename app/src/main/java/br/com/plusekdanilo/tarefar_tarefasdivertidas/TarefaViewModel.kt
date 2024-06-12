package br.com.plusekdanilo.tarefar_tarefasdivertidas

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class TarefaViewModel(application: Application) : AndroidViewModel(application) {
    private val tarefaDAO: TarefaDAO
    private val viewModelJob = Job()
    private val scope = CoroutineScope(Dispatchers.IO)

    init {
        val db = AppDatabase.getDatabase(application)
        tarefaDAO = db.tarefaDAO()
    }

    val tarefas: LiveData<List<Tarefa>> = tarefaDAO.getAllTarefas()

    fun getTarefaById(id: Int): Tarefa = runBlocking {
        scope.async { tarefaDAO.getTarefa(id) }.await()
    }

    fun getTarefaByTitulo(titulo: String): Tarefa = runBlocking {
        scope.async { tarefaDAO.getTarefaByTitulo(titulo) }.await()
    }

    fun insertTarefa(tarefa: Tarefa) {
        scope.launch {
            tarefaDAO.insertTarefa(tarefa)
        }
    }

    fun deleteTarefa(id: Int) {
        scope.launch {
            tarefaDAO.deleteTarefa(id)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel() // Cancel the coroutine when ViewModel is cleared
    }
}