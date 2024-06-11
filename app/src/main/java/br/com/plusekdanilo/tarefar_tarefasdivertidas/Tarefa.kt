package br.com.plusekdanilo.tarefar_tarefasdivertidas

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Tarefas")
data class Tarefa(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val titulo: String,
    val descricao: String,
    val status: Int = 0
)