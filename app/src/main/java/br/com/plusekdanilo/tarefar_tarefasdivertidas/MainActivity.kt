package br.com.plusekdanilo.tarefar_tarefasdivertidas

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity(), AdminLoginCallback, TarefaInterface {
    private lateinit var tarefasRecyclerView: RecyclerView
    private lateinit var lockImageView: ImageView
    private lateinit var characterImageView: ImageView
    private var loggedUserId: Int = -1
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var tarefaViewModel: TarefaViewModel
    private lateinit var tarefaAdapter: TarefaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // Esconde a barra de título
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        // Esconde a barra de status
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        setContentView(R.layout.activity_main)

        // Inicializa o DatabaseHelper
        dbHelper = DatabaseHelper(this)

        // Recupere o ID do usuário da Intent
        loggedUserId = intent.getIntExtra("USER_ID", -1)
        Log.d("MainActivity", "ID do usuário recebido da Intent: $loggedUserId") // Adicione este log

        if (loggedUserId == -1) {
            // Tratar o caso em que o ID do usuário não foi encontrado (erro no login)
            Toast.makeText(this, "Erro ao obter ID do usuário", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        tarefasRecyclerView = findViewById(R.id.recyclerView)
        lockImageView = findViewById(R.id.lockImageView)
        characterImageView = findViewById(R.id.characterImageView)
        tarefaViewModel = ViewModelProvider(this)[TarefaViewModel::class.java]

        // Crie o Adapter e passe a referência para o ViewModel
        tarefaAdapter = TarefaAdapter(tarefaViewModel, emptyList(), R.layout.item_tarefa, onEditClickListener = {})
        tarefaViewModel.tarefaAdapter = tarefaAdapter
        tarefasRecyclerView.adapter = tarefaAdapter

        tarefaViewModel.tarefas.observe(this) { tarefas ->
            val tarefasFiltradas = tarefas.filter { it.userID == loggedUserId }.sortedBy { it.id }
            Log.d("MainActivity", "Tarefas filtradas pelo ID do usuário: $tarefasFiltradas") // Adicione este log

            tarefaAdapter.tarefas = tarefasFiltradas
            tarefaAdapter.notifyDataSetChanged()
        }

        //tarefasRecyclerView.adapter = tarefaAdapter
        tarefasRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        lockImageView.setOnClickListener {
            val dialog = AdminLoginDialogFragment()
            dialog.show(supportFragmentManager, "AdminLoginDialogFragment")
        }

        // Adicione um OnClickListener para o personagem
        characterImageView.setOnClickListener {
            // Apresente a janela com opções de aparência para o personagem
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    // Método chamado quando o login do administrador é bem-sucedido
    override fun onAdminLoginSuccessful() {
        Handler(Looper.getMainLooper()).post {
            val adminDialogFragment = AdminDialogFragment()
            adminDialogFragment.show(supportFragmentManager, "AdminDialogFragment")
        }
    }

    override fun criarTarefa(titulo: String, descricao: String) {
        tarefaViewModel.insertTarefa(Tarefa(titulo = titulo, descricao = descricao, userID = loggedUserId))
    }
}