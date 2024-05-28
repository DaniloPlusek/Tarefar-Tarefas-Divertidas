package br.com.plusekdanilo.tarefar_tarefasdivertidas

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TarefaAdapter(private val tarefas: List<Tarefa>) : RecyclerView.Adapter<TarefaAdapter.TarefaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TarefaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_tarefa, parent, false)
        return TarefaViewHolder(view)
    }

    override fun onBindViewHolder(holder: TarefaViewHolder, position: Int) {
        val tarefa = tarefas[position]
        holder.bind(tarefa)
    }

    override fun getItemCount() = tarefas.size

    class TarefaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tituloTarefa: TextView = itemView.findViewById(R.id.tituloTarefa)

        fun bind(tarefa: Tarefa) {
            tituloTarefa.text = tarefa.titulo
            itemView.setOnClickListener {
                // Iniciar Activity ou Fragment com detalhes da tarefa
            }
        }
    }
}