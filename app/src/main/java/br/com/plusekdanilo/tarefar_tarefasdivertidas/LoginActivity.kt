package br.com.plusekdanilo.tarefar_tarefasdivertidas

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val usernameEditText = findViewById<EditText>(R.id.username)
        val passwordEditText = findViewById<EditText>(R.id.password)
        val loginButton = findViewById<Button>(R.id.login)
        val registerTextView = findViewById<TextView>(R.id.register)
        val cadastroActivity = CadastroActivity()

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            val dbHelper = DatabaseHelper(this)
            val cursor = dbHelper.getResponsavel(username)

            if (cursor.moveToFirst()) { // Verifica se o usuário existe
                val senhaIndex = cursor.getColumnIndex("Senha")
                if (senhaIndex != -1) {
                    val dbPassword = cursor.getString(senhaIndex)
                    val senhaDigitadaHash = cadastroActivity.gerarHashSenha(password)
                    Log.d("LoginActivitySenha", "Senha do usuário obtida do cursor: $dbPassword") // Adicione este log
                    Log.d("LoginActivitySenhaDigitada", "Senha digitada pelo usuário: $senhaDigitadaHash") // Adicione este log
                    if (dbPassword == senhaDigitadaHash) {
                        // Login bem-sucedido
                        val idUsuarioIndex = cursor.getColumnIndex("ID_Usuario")
                        if (idUsuarioIndex != -1) {
                            val idUsuario = cursor.getInt(idUsuarioIndex)
                            Log.d("LoginActivity", "ID do usuário obtido do cursor: $idUsuario") // Adicione este log

                            // Passe o ID do usuário para a MainActivity através da Intent
                            val intent = Intent(this, MainActivity::class.java)
                            intent.putExtra("USER_ID", idUsuario)
                            startActivity(intent)
                            finish()
                        } else {
                            // A coluna "ID_Usuario" não existe no cursor (isso não deve acontecer)
                            Toast.makeText(this, "Erro ao obter ID do usuário", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this, "Senha incorreta", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "A coluna 'Senha' não existe", Toast.LENGTH_SHORT).show()
                }
            } else {
                // Usuário não encontrado
                Toast.makeText(this, "Responsável não encontrado", Toast.LENGTH_SHORT).show()
            }
        }

        registerTextView.setOnClickListener {
            val intent = Intent(this, CadastroActivity::class.java)
            startActivity(intent)
        }
    }
}