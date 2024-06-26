package br.com.plusekdanilo.tarefar_tarefasdivertidas

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment

class CriarTarefaDialogFragment(var listener: TarefaInterface? = null) : DialogFragment() {
    private lateinit var tituloEditText: EditText
    private lateinit var descricaoEditText: EditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_criar_tarefa, container, false)

        // Inicialização dos campos de entrada de texto
        tituloEditText = view.findViewById(R.id.editTextTitulo)
        descricaoEditText = view.findViewById(R.id.editTextDescricao)

        val criarTarefaButton: Button = view.findViewById(R.id.buttonCriarTarefa)
        criarTarefaButton.setOnClickListener {
            // Aqui você pode adicionar a lógica para criar a tarefa com os valores dos campos de texto
            val titulo = tituloEditText.text.toString()
            val descricao = descricaoEditText.text.toString()

            // Chamar o método do listener para criar a tarefa
            listener?.criarTarefa(titulo, descricao)

            // Fechar o diálogo após criar a tarefa (você pode adicionar a lógica apropriada aqui)
            dismiss()
        }
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Ensure that the activity implements the interface
        listener = try {
            context as TarefaInterface
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement TarefaInterface")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onResume() {
        super.onResume()
        val params: ViewGroup.LayoutParams? = dialog?.window?.attributes
        params?.width = (resources.displayMetrics.widthPixels * 0.35).toInt()
        params?.height = (resources.displayMetrics.heightPixels * 0.65).toInt()
        dialog?.window?.attributes = params as WindowManager.LayoutParams?
    }
}
