package br.com.plusekdanilo.tarefar_tarefasdivertidas

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 3
        private const val DATABASE_NAME = "Tarefar.db"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_PERSONAGEM_TABLE = "CREATE TABLE Personagem (ID_Personagem INTEGER PRIMARY KEY, Aparencia INTEGER)"
        val CREATE_USUARIO_TABLE = "CREATE TABLE Usuario (ID_Usuario INTEGER PRIMARY KEY, Nome TEXT, Progresso INTEGER, ID_Personagem INTEGER, FOREIGN KEY (ID_Personagem) REFERENCES Personagem(ID_Personagem))"
        val CREATE_MINIJOGO_TABLE = "CREATE TABLE Minijogo (ID_Minijogo INTEGER PRIMARY KEY, Tipo_de_Jogo TEXT, Resultado TEXT, Feedback_Emocional TEXT)"
        val CREATE_TAREFA_TABLE = "CREATE TABLE Tarefa (ID_Tarefa INTEGER PRIMARY KEY, Titulo TEXT, Descricao TEXT, Prazo TEXT, Status TEXT, ID_Usuario INTEGER, ID_Minijogo INTEGER, FOREIGN KEY (ID_Usuario) REFERENCES Usuario(ID_Usuario), FOREIGN KEY (ID_Minijogo) REFERENCES Minijogo(ID_Minijogo))"
        val CREATE_RESPONSAVEL_TABLE = "CREATE TABLE Responsavel (ID_Responsavel INTEGER PRIMARY KEY AUTOINCREMENT, Nome TEXT, Idade INTEGER, Login TEXT, Senha TEXT, ID_Usuario INTEGER, FOREIGN KEY (ID_Usuario) REFERENCES Usuario(ID_Usuario))"
        val CREATE_PLANOFUNDO_TABLE = "CREATE TABLE PlanoFundo (ID_PlanoFundo INTEGER PRIMARY KEY, Tema TEXT)"

        db.execSQL(CREATE_PERSONAGEM_TABLE)
        db.execSQL(CREATE_USUARIO_TABLE)
        db.execSQL(CREATE_MINIJOGO_TABLE)
        db.execSQL(CREATE_TAREFA_TABLE)
        db.execSQL(CREATE_RESPONSAVEL_TABLE)
        db.execSQL(CREATE_PLANOFUNDO_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE Responsavel ADD COLUMN Login TEXT")
            db.execSQL("ALTER TABLE Responsavel ADD COLUMN Senha TEXT")
        }
        if (oldVersion < 3) {
            db.execSQL("CREATE TABLE ResponsavelNew (ID_Responsavel INTEGER PRIMARY KEY AUTOINCREMENT, Nome TEXT, Idade INTEGER, Login TEXT, Senha TEXT, ID_Usuario INTEGER, FOREIGN KEY (ID_Usuario) REFERENCES Usuario(ID_Usuario))")
            db.execSQL("INSERT INTO ResponsavelNew(Nome, Idade, Login, Senha, ID_Usuario) SELECT Nome, Idade, Login, Senha, ID_Usuario FROM Responsavel")
            db.execSQL("DROP TABLE Responsavel")
            db.execSQL("ALTER TABLE ResponsavelNew RENAME TO Responsavel")
        }
    }

    fun addUsuario(nome: String, progresso: Int, idPersonagem: Int) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("Nome", nome)
        values.put("Progresso", progresso)
        values.put("ID_Personagem", idPersonagem)

        db.insert("Usuario", null, values)
        db.close()
    }

    fun updateUsuario(idUsuario: Int, nome: String, progresso: Int, idPersonagem: Int) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("Nome", nome)
        values.put("Progresso", progresso)
        values.put("ID_Personagem", idPersonagem)

        db.update("Usuario", values, "ID_Usuario = ?", arrayOf(idUsuario.toString()))
        db.close()
    }

    fun deleteUsuario(idUsuario: Int) {
        val db = this.writableDatabase
        db.delete("Usuario", "ID_Usuario = ?", arrayOf(idUsuario.toString()))
        db.close()
    }

    fun getUsuario(idUsuario: Int): Cursor {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM Usuario WHERE ID_Usuario = ?", arrayOf(idUsuario.toString()))
    }

    fun getAllUsuarios(): Cursor {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM Usuario", null)
    }

    fun addResponsavel(nome: String, idade: Int, login: String, senha: String) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("Nome", nome)
        values.put("Idade", idade)
        values.put("Login", login)
        values.put("Senha", senha)

        db.insert("Responsavel", null, values)
        db.close()
    }

    fun getResponsavel(login: String): Cursor {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM Responsavel WHERE Login = ?", arrayOf(login))
    }

    fun getAllResponsaveis(): Cursor {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM Responsavel", null)
    }

}