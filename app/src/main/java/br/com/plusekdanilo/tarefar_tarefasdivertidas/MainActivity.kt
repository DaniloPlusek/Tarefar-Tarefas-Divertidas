package br.com.plusekdanilo.tarefar_tarefasdivertidas

import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.plusekdanilo.tarefar_tarefasdivertidas.toTarefaList

class MainActivity : AppCompatActivity() {
    private lateinit var tarefasRecyclerView: RecyclerView
    private lateinit var lockImageView: ImageView
    private lateinit var characterImageView: ImageView
    private var loggedUserId: Int = -1
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // Esconde a barra de título
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        // Esconde a barra de status
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)

        setContentView(R.layout.activity_main)

        // Inicializa o DatabaseHelper
        dbHelper = DatabaseHelper(this)

        // Recupere o ID do usuário da Intent
        loggedUserId = intent.getIntExtra("USER_ID", -1)

        if (loggedUserId == -1) {
            // Tratar o caso em que o ID do usuário não foi encontrado (erro no login)
            Toast.makeText(this, "Erro ao obter ID do usuário", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        tarefasRecyclerView = findViewById(R.id.recyclerView)
        lockImageView = findViewById(R.id.lockImageView)
        characterImageView = findViewById(R.id.characterImageView)

        val tarefas = dbHelper.getTarefasDoUsuario(loggedUserId).toTarefaList()
//        dbHelper.addTarefa("Tarefa Exemplo", "Tarefa apenas para exemplo", loggedUserId, null, dbHelper)
//        dbHelper.deleteTarefa(1)

        // Inicialize o RecyclerView com um Adapter e um LayoutManager
        tarefasRecyclerView.adapter = TarefaAdapter(tarefas)
        tarefasRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        lockImageView.setOnClickListener {
            val dialog = AdminLoginDialogFragment()
            dialog.show(supportFragmentManager, "AdminLoginDialogFragment")
        }

        // Adicione um OnClickListener para o personagem
        characterImageView.setOnClickListener {
            // Apresente a janela com opções de aparência para o personagem
        }

        // Exemplo de como chamar a função addTarefa
        // (agora passando loggedUserId e a instância de dbHelper)


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

    }
}