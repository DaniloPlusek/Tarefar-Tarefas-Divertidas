package br.com.plusekdanilo.tarefar_tarefasdivertidas

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment

class AdminLoginDialogFragment : DialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.admin_login_dialog, container, false)

        val usernameEditText = view.findViewById<EditText>(R.id.username)
        val passwordEditText = view.findViewById<EditText>(R.id.password)
        val loginButton = view.findViewById<Button>(R.id.login)

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Cria uma instância de DatabaseHelper e verifica se o responsável existe no banco de dados
            context?.let {
                val dbHelper = DatabaseHelper(it)
                val cursor = dbHelper.getResponsavel(username)

                if (cursor.moveToFirst()) {
                    val senhaIndex = cursor.getColumnIndex("Senha")
                    if (senhaIndex != -1) {
                        val dbPassword = cursor.getString(senhaIndex)
                        if (dbPassword == password) {
                            // Se a autenticação for bem-sucedida, feche o diálogo
                            dismiss()
                        } else {
                            Toast.makeText(it, "Senha incorreta", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(it, "A coluna 'Senha' não existe", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(it, "Responsável não encontrado", Toast.LENGTH_SHORT).show()
                }
            }
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        val displayMetrics = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        val params: ViewGroup.LayoutParams = dialog!!.window!!.attributes
        params.width = (displayMetrics.widthPixels * 0.30).toInt()
        params.height = (displayMetrics.heightPixels * 0.65).toInt()
        dialog!!.window!!.attributes = params as WindowManager.LayoutParams
    }

}