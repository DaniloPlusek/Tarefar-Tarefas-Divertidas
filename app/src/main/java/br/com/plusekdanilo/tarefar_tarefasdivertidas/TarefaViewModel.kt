package br.com.plusekdanilo.tarefar_tarefasdivertidas

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class TarefaViewModel(application: Application) : AndroidViewModel(application) {
    private val tarefaDAO: TarefaDAO
    private val viewModelJob = Job()
    private val scope = CoroutineScope(Dispatchers.IO)
    lateinit var tarefaAdapter: TarefaAdapter

    val tarefas: LiveData<List<Tarefa>>

    init {
        val db = AppDatabase.getDatabase(application)
        tarefaDAO = db.tarefaDAO()
        tarefas = tarefaDAO.getAllTarefas()
    }

    fun getAllTarefas(): LiveData<List<Tarefa>> {
        return tarefaDAO.getAllTarefas()
    }

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

    fun marcarTarefaComoConcluida(tarefa: Tarefa, adapter: TarefaAdapter) {
        scope.launch {
            tarefaDAO.marcarTarefaComoConcluida(tarefa.id)
            adapter.notifyItemChanged(tarefa.id)

//            withContext(Dispatchers.Main) {
//                tarefaDAO.getAllTarefas().observe(lifecycleOwner) { tarefasAtualizadas -> // Usa observe com lifecycleOwner
//                    tarefas.value = tarefasAtualizadas
//                    adapter.notifyDataSetChanged()
//                }
//            }
        }
    }
}