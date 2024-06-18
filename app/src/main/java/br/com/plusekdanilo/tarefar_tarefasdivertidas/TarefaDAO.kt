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

    @Query("SELECT * FROM Tarefas WHERE id = :id")
    fun getTarefa(id: Int): Tarefa

    @Query("SELECT * FROM Tarefas WHERE titulo = :titulo LIMIT 1")
    fun getTarefaByTitulo(titulo: String): Tarefa

    @Insert
    fun insertTarefa(tarefa: Tarefa)

    @Query("DELETE FROM Tarefas WHERE id = :id")
    fun deleteTarefa(id: Int)

    @Query("UPDATE Tarefas SET Status = NOT Status WHERE id = :id")
    fun marcarTarefaComoConcluida(id: Int)
}