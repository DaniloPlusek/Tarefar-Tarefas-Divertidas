package br.com.plusekdanilo.tarefar_tarefasdivertidas

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast

class CadastroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)

        val nameEditText = findViewById<EditText>(R.id.name)
        val ageEditText = findViewById<EditText>(R.id.age)
        val usernameEditText = findViewById<EditText>(R.id.username)
        val passwordEditText = findViewById<EditText>(R.id.password)
        val confirmPasswordEditText = findViewById<EditText>(R.id.confirm_password)
        val registerButton = findViewById<Button>(R.id.register)

        registerButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val age = ageEditText.text.toString().toInt()
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()
            val confirmPassword = confirmPasswordEditText.text.toString()

            if (password == confirmPassword) {
                // Adicione o usuário ao banco de dados
                val dbHelper = DatabaseHelper(this)
                dbHelper.addResponsavel(name, age, username, password) // Substitua 1 pelo ID do usuário correto

                // Se o cadastro for bem-sucedido, inicie a LoginActivity
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()  // Fecha a CadastroActivity
            } else {
                Toast.makeText(this, "As senhas não correspondem", Toast.LENGTH_SHORT).show()
            }
        }

        val backButton = findViewById<ImageButton>(R.id.backButton)

        backButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()  // Fecha a CadastroActivity
        }

    }
}
