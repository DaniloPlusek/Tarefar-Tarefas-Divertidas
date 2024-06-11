package br.com.plusekdanilo.tarefar_tarefasdivertidas

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TarefaDAO {
    @Query("SELECT * FROM Tarefas")
    fun getAllTarefas(): LiveData<List<Tarefa>>

    @Insert
    fun insertTarefa(tarefa: Tarefa)

    @Query("DELETE FROM Tarefas WHERE id = :id")
    fun deleteTarefa(id: Int)

}