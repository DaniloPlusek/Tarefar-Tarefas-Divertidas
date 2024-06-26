package br.com.plusekdanilo.tarefar_tarefasdivertidas

import android.database.Cursor

fun Cursor.toTarefaList(): List<Tarefa> {
    val tarefas = mutableListOf<Tarefa>()
    while (moveToNext()) {
        val id = getInt(getColumnIndexOrThrow("ID_Tarefa"))
        val titulo = getString(getColumnIndexOrThrow("Titulo"))
        val descricao = getString(getColumnIndexOrThrow("Descricao"))
        val status = getInt(getColumnIndexOrThrow("Status"))
        val userID = getInt(getColumnIndexOrThrow("ID_Usuario"))
        // ... obtenha outros campos da tarefa, se necessário

        tarefas.add(Tarefa(id, userID, titulo, descricao, status))
    }
    return tarefas
}