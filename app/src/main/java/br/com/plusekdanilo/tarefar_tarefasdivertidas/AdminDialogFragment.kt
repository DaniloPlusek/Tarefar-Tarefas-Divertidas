package br.com.plusekdanilo.tarefar_tarefasdivertidas

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageButton
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class AdminDialogFragment : DialogFragment() {

    private lateinit var tarefaViewModel: TarefaViewModel
    private lateinit var tarefaAdapter: TarefaAdapter
    private var loggedUserId: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.admin_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tarefaViewModel = ViewModelProvider(this)[TarefaViewModel::class.java]
        val tarefaAdapter = TarefaAdapter(tarefaViewModel, emptyList(), R.layout.item_tarefa_admin, onEditClickListener = { tarefa ->
            // Lógica para abrir o diálogo de edição da tarefa, passando a tarefa como argumento
        })

        val tarefasRecyclerView = view.findViewById<RecyclerView>(R.id.tarefasRecyclerView)
        tarefasRecyclerView.adapter = tarefaAdapter
        tarefasRecyclerView.layoutManager = LinearLayoutManager(context)

        tarefaViewModel.tarefas.observe(viewLifecycleOwner) { tarefas ->
            tarefaAdapter.tarefas = tarefas
            tarefaAdapter.notifyDataSetChanged()
        }

        val newDialogButton = view.findViewById<ImageButton>(R.id.newDialogButton)
        newDialogButton.setOnClickListener {
            val criarTarefaDialog = CriarTarefaDialogFragment()
            criarTarefaDialog.show(parentFragmentManager, "criar_tarefa_dialog")
        }

        val playMiniGameButton = view.findViewById<ImageButton>(R.id.playMinigameButton)
        playMiniGameButton.setOnClickListener {
            val intent = Intent(activity, SlidingPuzzleActivity::class.java)
            startActivity(intent)
            dismiss() // Fecha o diálogo após iniciar o minijogo
        }
    }

    override fun onResume() {
        super.onResume()
        val params: ViewGroup.LayoutParams? = dialog?.window?.attributes
        params?.width = (resources.displayMetrics.widthPixels * 0.5).toInt()
        params?.height = (resources.displayMetrics.heightPixels * 0.65).toInt()
        dialog?.window?.attributes = params as WindowManager.LayoutParams?
    }
}