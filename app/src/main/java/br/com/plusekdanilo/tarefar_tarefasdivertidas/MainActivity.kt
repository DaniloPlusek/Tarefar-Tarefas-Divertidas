package br.com.plusekdanilo.tarefar_tarefasdivertidas

import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var tarefasRecyclerView: RecyclerView
    private lateinit var lockImageView: ImageView
    private lateinit var characterImageView: ImageView

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

        tarefasRecyclerView = findViewById(R.id.recyclerView)
        lockImageView = findViewById(R.id.lockImageView)
        characterImageView = findViewById(R.id.characterImageView)

        val tarefas = listOf(
            Tarefa("Tarefa 1", "Descrição da Tarefa 1"),
            Tarefa("Tarefa 2", "Descrição da Tarefa 2"),
            Tarefa("Tarefa 3", "Descrição da Tarefa 3"),
            Tarefa("Tarefa 4", "Descrição da Tarefa 4"),
            Tarefa("Tarefa 5", "Descrição da Tarefa 5"),
            Tarefa("Tarefa 6", "Descrição da Tarefa 6"),
            Tarefa("Tarefa 7", "Descrição da Tarefa 7"),
            Tarefa("Tarefa 8", "Descrição da Tarefa 8"),
            Tarefa("Tarefa 9", "Descrição da Tarefa 9"),
            Tarefa("Tarefa 10", "Descrição da Tarefa 10"),
            Tarefa("Tarefa 11", "Descrição da Tarefa 11"),
            Tarefa("Tarefa 12", "Descrição da Tarefa 12"),
            Tarefa("Tarefa 13", "Descrição da Tarefa 13"),
            Tarefa("Tarefa 14", "Descrição da Tarefa 14"),
            Tarefa("Tarefa 15", "Descrição da Tarefa 15"),
        )

        // Inicialize o RecyclerView com um Adapter e um LayoutManager
        tarefasRecyclerView.adapter = TarefaAdapter(tarefas)
        tarefasRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        // Adicione um OnClickListener para o cadeado
        lockImageView.setOnClickListener {
            // Apresente a janela de autenticação
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
}