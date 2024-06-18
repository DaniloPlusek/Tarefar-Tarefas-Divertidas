package br.com.plusekdanilo.tarefar_tarefasdivertidas

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView

class TarefaAdapter(
    private val viewModel: TarefaViewModel,
    var tarefas: List<Tarefa>,
    private val layoutId: Int,
    private val showTitleOnly: Boolean = false,
    private val onEditClickListener: (Tarefa) -> Unit = {}
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        val layoutInflater = LayoutInflater.from(parent.context)
//        val lifecycleOwner = parent.context as? LifecycleOwner
        return when (layoutId) {
            R.layout.item_tarefa -> {
                val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
                TarefaViewHolderMain(view, viewModel)
            }
            R.layout.item_tarefa_admin -> {
                val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
                TarefaViewHolderAdmin(view, viewModel, this, onEditClickListener) // Passa 'this' (o Adapter)
            }
            else -> throw IllegalArgumentException("Layout inválido")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val tarefa = tarefas[position]
        when (holder) {
            is TarefaViewHolderMain -> holder.bind(tarefa)
            is TarefaViewHolderAdmin -> holder.bind(tarefa)
        }
    }

    override fun getItemCount() = tarefas.size

    class TarefaViewHolderMain(itemView: View, private val viewModel: TarefaViewModel) : RecyclerView.ViewHolder(itemView) {
        private val tituloTarefa: TextView = itemView.findViewById(R.id.tituloTarefa)
        private val iconeTarefa: ImageView = itemView.findViewById(R.id.iconeTarefa)
        private val constraintLayout: ConstraintLayout = itemView.findViewById(R.id.itemTarefa)

        fun bind(tarefa: Tarefa) {
            tituloTarefa.text = tarefa.titulo
            // Configure o iconeTarefa aqui, se necessário

            // Verifica o status da tarefa e altera o background
            if (tarefa.status == 1) {
                constraintLayout.setBackgroundResource(R.drawable.card_border_concluido)
            } else {
                constraintLayout.setBackgroundResource(R.drawable.card_border)
            }
        }
    }

    class TarefaViewHolderAdmin(
        itemView: View,
        private val viewModel: TarefaViewModel,
        private val adapter: TarefaAdapter,
        private val onEditClickListener: (Tarefa) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        private val tituloTarefa: TextView = itemView.findViewById(R.id.tituloTarefaTextView)
        private val editButton: ImageButton = itemView.findViewById(R.id.editButton)
        private val concluirButton: ImageButton = itemView.findViewById(R.id.concluirButton)
        private val deleteButton: ImageButton = itemView.findViewById(R.id.deleteButton)

        fun bind(tarefa: Tarefa) {
            tituloTarefa.text = tarefa.titulo

            editButton.setOnClickListener {
                onEditClickListener.invoke(tarefa)
            }

            concluirButton.setOnClickListener {
                viewModel.marcarTarefaComoConcluida(tarefa, adapter) // Usa o adapter recebido
            }

            deleteButton.setOnClickListener {
                AlertDialog.Builder(itemView.context)
                    .setTitle("Confirmar Exclusão")
                    .setMessage("Tem certeza que deseja excluir esta tarefa?")
                    .setPositiveButton("Sim") { _, _ ->
                        viewModel.deleteTarefa(tarefa.id)
                    }
                    .setNegativeButton("Não", null)
                    .show()
            }
        }
    }
}