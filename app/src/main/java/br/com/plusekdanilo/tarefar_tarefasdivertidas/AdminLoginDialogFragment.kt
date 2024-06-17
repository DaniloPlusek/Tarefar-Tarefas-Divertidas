package br.com.plusekdanilo.tarefar_tarefasdivertidas

import android.content.Context
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

    private var listener: AdminLoginCallback? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is AdminLoginCallback) {
            listener = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.admin_login_dialog, container, false)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val loginButton = view?.findViewById<Button>(R.id.login)
        loginButton?.setOnClickListener {
            val username = view?.findViewById<EditText>(R.id.username)?.text.toString()
            val password = view?.findViewById<EditText>(R.id.password)?.text.toString()

            context?.let {
                val dbHelper = DatabaseHelper(it)
                val cursor = dbHelper.getResponsavel(username)

                if (cursor.moveToFirst()) {
                    val senhaIndex = cursor.getColumnIndex("Senha")
                    if (senhaIndex != -1) {
                        val dbPassword = cursor.getString(senhaIndex)
                        if (dbPassword == password) {
                            dismiss()
                            listener?.onAdminLoginSuccessful() // Notifica a MainActivity
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