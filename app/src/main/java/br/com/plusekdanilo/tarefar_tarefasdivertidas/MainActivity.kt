package br.com.plusekdanilo.tarefar_tarefasdivertidas

import android.os.Bundle
import android.view.View
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
        setContentView(R.layout.activity_main)

        tarefasRecyclerView = findViewById(R.id.recyclerView)
        lockImageView = findViewById(R.id.lockImageView)
        characterImageView = findViewById(R.id.characterImageView)

        val tarefas = listOf(
            Tarefa("Tarefa 1", "Descrição da Tarefa 1"),
            Tarefa("Tarefa 2", "Descrição da Tarefa 2"),
            Tarefa("Tarefa 3", "Descrição da Tarefa 3")
        )

        // Inicialize o RecyclerView com um Adapter e um LayoutManager
        tarefasRecyclerView.adapter = TarefaAdapter(tarefas)
        tarefasRecyclerView.layoutManager = LinearLayoutManager(this)

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