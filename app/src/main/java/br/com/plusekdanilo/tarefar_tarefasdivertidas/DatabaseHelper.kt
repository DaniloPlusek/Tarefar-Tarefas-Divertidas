package br.com.plusekdanilo.tarefar_tarefasdivertidas

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 6 // Incrementamos a versão para refletir a mudança no schema
        private const val DATABASE_NAME = "Tarefar.db"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_PERSONAGEM_TABLE = "CREATE TABLE Personagem (ID_Personagem INTEGER PRIMARY KEY, Aparencia INTEGER)"
        val CREATE_USUARIO_TABLE = "CREATE TABLE Usuario (ID_Usuario INTEGER PRIMARY KEY, Nome TEXT, Progresso INTEGER)"
        val CREATE_MINIJOGO_TABLE = "CREATE TABLE Minijogo (ID_Minijogo INTEGER PRIMARY KEY, Tipo_de_Jogo TEXT, Resultado TEXT, Feedback_Emocional TEXT)"
        val CREATE_TAREFA_TABLE = """
            CREATE TABLE Tarefa (
                ID_Tarefa INTEGER PRIMARY KEY AUTOINCREMENT, 
                Titulo TEXT, 
                Descricao TEXT, 
                Status INTEGER DEFAULT 0, 
                ID_Usuario INTEGER, 
                Imagem BLOB, 
                FOREIGN KEY (ID_Usuario) REFERENCES Usuario(ID_Usuario)
            )
        """.trimIndent()
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
        if (oldVersion < 4) {
            // Cria tabelas temporárias com o novo esquema
            db.execSQL("CREATE TABLE UsuarioNew (ID_Usuario INTEGER PRIMARY KEY, Nome TEXT, Progresso INTEGER)")
            db.execSQL("CREATE TABLE TarefaNew (ID_Tarefa INTEGER PRIMARY KEY, Titulo TEXT, Descricao TEXT, Status TEXT, ID_Usuario INTEGER, Imagem BLOB, FOREIGN KEY (ID_Usuario) REFERENCES UsuarioNew(ID_Usuario))")

            // Copia dados das tabelas antigas para as novas tabelas
            db.execSQL("INSERT INTO UsuarioNew(ID_Usuario, Nome, Progresso) SELECT ID_Usuario, Nome, Progresso FROM Usuario")
            db.execSQL("INSERT INTO TarefaNew(ID_Tarefa, Titulo, Descricao, Status, ID_Usuario) SELECT ID_Tarefa, Titulo, Descricao, Status, ID_Usuario FROM Tarefa")

            // Exclui as tabelas antigas
            db.execSQL("DROP TABLE Usuario")
            db.execSQL("DROP TABLE Tarefa")

            // Renomeia as novas tabelas para os nomes originais
            db.execSQL("ALTER TABLE UsuarioNew RENAME TO Usuario")
            db.execSQL("ALTER TABLE TarefaNew RENAME TO Tarefa")
        }
        if (oldVersion < 5) {
            // Recriamos a tabela Tarefa com a coluna Status do tipo INTEGER
            db.execSQL("CREATE TABLE TarefaNew (ID_Tarefa INTEGER PRIMARY KEY, Titulo TEXT, Descricao TEXT, Status INTEGER DEFAULT 0, ID_Usuario INTEGER, Imagem BLOB, FOREIGN KEY (ID_Usuario) REFERENCES Usuario(ID_Usuario))")
            db.execSQL("INSERT INTO TarefaNew (ID_Tarefa, Titulo, Descricao, ID_Usuario, Imagem) SELECT ID_Tarefa, Titulo, Descricao, ID_Usuario, Imagem FROM Tarefa")
            db.execSQL("DROP TABLE Tarefa")
            db.execSQL("ALTER TABLE TarefaNew RENAME TO Tarefa")
        }
        if (oldVersion < 6) {
            // 1. Criar uma nova tabela temporária com o novo esquema
            db.execSQL("""
        CREATE TABLE TarefaNew (
            ID_Tarefa INTEGER PRIMARY KEY AUTOINCREMENT, 
            Titulo TEXT, 
            Descricao TEXT, 
            Status INTEGER DEFAULT 0, 
            ID_Usuario INTEGER, 
            Imagem BLOB, 
            FOREIGN KEY (ID_Usuario) REFERENCES Usuario(ID_Usuario)
        )
    """.trimIndent())

            // 2. Copiar os dados da tabela antiga para a nova tabela
            db.execSQL("INSERT INTO TarefaNew (Titulo, Descricao, Status, ID_Usuario, Imagem) SELECT Titulo, Descricao, Status, ID_Usuario, Imagem FROM Tarefa")

            // 3. Excluir a tabela antiga
            db.execSQL("DROP TABLE Tarefa")

            // 4. Renomear a nova tabela para o nome original
            db.execSQL("ALTER TABLE TarefaNew RENAME TO Tarefa")
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

    fun addResponsavel(nome: String, idade: Int, login: String, senhaHash: String) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("Nome", nome)
        values.put("Idade", idade)
        values.put("Login", login)
        values.put("Senha", senhaHash)

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

    // Criar Tarefa (com status padrão 0 - não concluída)
    fun addTarefa(titulo: String, descricao: String, idUsuario: Int, imagemBlob: ByteArray?, dbHelper: DatabaseHelper) { // Adiciona o parâmetro dbHelper
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("Titulo", titulo)
        values.put("Descricao", descricao)
        values.put("Status", 0) // Status padrão: não concluída
        values.put("ID_Usuario", idUsuario) // Usa o ID do usuário passado como parâmetro
        if (imagemBlob != null) {
            values.put("ImagemBlob", imagemBlob)
        }

        db.insert("Tarefa", null, values)
        db.close()
    }

    // Editar Tarefa (ajustado para o novo tipo de Status)
    fun updateTarefa(idTarefa: Int, titulo: String, descricao: String, status: Int, imagemBlob: ByteArray?) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("Titulo", titulo)
        values.put("Descricao", descricao)
        values.put("Status", status) // Agora o status é um inteiro (0 ou 1)
        if (imagemBlob != null) {
            values.put("ImagemBlob", imagemBlob)
        }

        db.update("Tarefa", values, "ID_Tarefa = ?", arrayOf(idTarefa.toString()))
        db.close()
    }

    // Excluir Tarefa
    fun deleteTarefa(idTarefa: Int) {
        val db = this.writableDatabase
        db.delete("Tarefa", "ID_Tarefa = ?", arrayOf(idTarefa.toString()))
        db.close()
    }

    // Função para atualizar o status da tarefa (ajustado para o novo tipo de Status)
    fun updateStatusTarefa(idTarefa: Int, novoStatus: Int) { // novoStatus agora é um inteiro (0 ou 1)
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("Status", novoStatus)

        db.update("Tarefa", values, "ID_Tarefa = ?", arrayOf(idTarefa.toString()))
        db.close()
    }

    // Função para obter uma lista de tarefas por usuário
    fun getTarefasDoUsuario(idUsuario: Int): Cursor {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM Tarefa WHERE ID_Usuario = ?", arrayOf(idUsuario.toString()))
    }
}