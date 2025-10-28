package com.example.geoapp.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import com.example.geoapp.db.DatabaseContract.AchievementEntry
import com.example.geoapp.db.DatabaseContract.ScoreEntry

// --- Constantes para la creación de las tablas ---
private const val SQL_CREATE_SCORES_TABLE =
    "CREATE TABLE ${ScoreEntry.TABLE_NAME} (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY," +
            "${ScoreEntry.COLUMN_NAME_USERNAME} TEXT," +
            "${ScoreEntry.COLUMN_NAME_SCORE} INTEGER," +
            "${ScoreEntry.COLUMN_NAME_DATE} LONG)"

private const val SQL_CREATE_ACHIEVEMENTS_TABLE =
    "CREATE TABLE ${AchievementEntry.TABLE_NAME} (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY," +
            "${AchievementEntry.COLUMN_NAME_NAME} TEXT," +
            "${AchievementEntry.COLUMN_NAME_DESCRIPTION} TEXT," +
            "${AchievementEntry.COLUMN_NAME_IS_UNLOCKED} INTEGER DEFAULT 0)" // 0 = false

private const val SQL_DELETE_SCORES_TABLE = "DROP TABLE IF EXISTS ${ScoreEntry.TABLE_NAME}"
private const val SQL_DELETE_ACHIEVEMENTS_TABLE = "DROP TABLE IF EXISTS ${AchievementEntry.TABLE_NAME}"


class QuizDbHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "GeoQuiz.db"
    }

    // 1. Este método se llama la primera vez que se accede a la BD
    override fun onCreate(db: SQLiteDatabase) {
        // Crea ambas tablas
        db.execSQL(SQL_CREATE_SCORES_TABLE)
        db.execSQL(SQL_CREATE_ACHIEVEMENTS_TABLE)

        // ¡Importante! Pre-pobla los logros
        prePopulateAchievements(db)
    }

    // 2. Este método se llama si aumentas DATABASE_VERSION (para actualizaciones)
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Por ahora, una política simple: borra todo y crea de nuevo
        db.execSQL(SQL_DELETE_SCORES_TABLE)
        db.execSQL(SQL_DELETE_ACHIEVEMENTS_TABLE)
        onCreate(db)
    }

    // 3. Método para pre-poblar los logros
    private fun prePopulateAchievements(db: SQLiteDatabase) {
        val achievements = listOf(
            Achievement(1, "Primeros Pasos", "Completa tu primer quiz", false),
            Achievement(2, "Cerebrito", "Consigue un 100% de aciertos (10/10)", false),
            Achievement(3, "Trotamundos", "Responde 50 preguntas correctamente en total", false) // (Lógica más avanzada, para después)
            // Añade más logros aquí
        )

        for (ach in achievements) {
            val values = ContentValues().apply {
                put(BaseColumns._ID, ach.id) // Asignamos ID manualmente
                put(AchievementEntry.COLUMN_NAME_NAME, ach.name)
                put(AchievementEntry.COLUMN_NAME_DESCRIPTION, ach.description)
                put(AchievementEntry.COLUMN_NAME_IS_UNLOCKED, if (ach.isUnlocked) 1 else 0)
            }
            db.insert(AchievementEntry.TABLE_NAME, null, values)
        }
    }

    /**
     * Inserta un nuevo puntaje en la base de datos
     * @return El ID de la nueva fila insertada, o -1 si hubo un error.
     */
    fun insertScore(username: String, score: Int): Long { // <--- CAMBIO: Devuelve Long
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(ScoreEntry.COLUMN_NAME_USERNAME, username)
            put(ScoreEntry.COLUMN_NAME_SCORE, score)
            put(ScoreEntry.COLUMN_NAME_DATE, System.currentTimeMillis())
        }
        // Devuelve el ID de la fila
        return db.insert(ScoreEntry.TABLE_NAME, null, values) // <--- CAMBIO
    }

    /**
     * Obtiene los 10 mejores puntajes
     */
    fun getHighScores(): List<Score> {
        val scoreList = mutableListOf<Score>()
        val db = this.readableDatabase

        // 1. La consulta a la base de datos
        val cursor = db.query(
            ScoreEntry.TABLE_NAME, // Nombre de la tabla
            null, // null = trae todas las columnas
            null, // Sin 'WHERE'
            null, // Sin args para 'WHERE'
            null, // Sin 'GROUP BY'
            null, // Sin 'HAVING'
            "${ScoreEntry.COLUMN_NAME_SCORE} DESC", // Ordenar por puntaje descendente
            "10" // Limitar a 10 resultados
        )

        // 2. El bucle para leer los datos
        with(cursor) {
            while (moveToNext()) {
                val score = Score(
                    id = getLong(getColumnIndexOrThrow(BaseColumns._ID)),
                    username = getString(getColumnIndexOrThrow(ScoreEntry.COLUMN_NAME_USERNAME)),
                    score = getInt(getColumnIndexOrThrow(ScoreEntry.COLUMN_NAME_SCORE)),
                    date = getLong(getColumnIndexOrThrow(ScoreEntry.COLUMN_NAME_DATE))
                )
                scoreList.add(score)
            }
        }
        cursor.close()
        return scoreList
    }

    /**
     * Obtiene TODOS los logros
     */

    fun getAchievements(): List<Achievement> {
        val achievementList = mutableListOf<Achievement>()
        val db = this.readableDatabase

        // 1. La consulta a la base de datos
        val cursor = db.query(
            AchievementEntry.TABLE_NAME, // Nombre de la tabla
            null, // null = trae todas las columnas
            null, // Sin 'WHERE'
            null, // Sin args para 'WHERE'
            null, // Sin 'GROUP BY'
            null, // Sin 'HAVING'
            "${BaseColumns._ID} ASC" // Ordenar por ID ascendente
        )

        // 2. El bucle para leer los datos
        with(cursor) {
            while (moveToNext()) {
                val achievement = Achievement(
                    id = getLong(getColumnIndexOrThrow(BaseColumns._ID)),
                    name = getString(getColumnIndexOrThrow(AchievementEntry.COLUMN_NAME_NAME)),
                    description = getString(getColumnIndexOrThrow(AchievementEntry.COLUMN_NAME_DESCRIPTION)),
                    isUnlocked = getInt(getColumnIndexOrThrow(AchievementEntry.COLUMN_NAME_IS_UNLOCKED)) == 1 // Convierte 1/0 a Boolean
                )
                achievementList.add(achievement)
            }
        }
        cursor.close()
        return achievementList
    }

    /**
     * Marca un logro como desbloqueado
     * @return El número de filas afectadas (debería ser 1).
     */
    fun unlockAchievement(achievementId: Long): Int { // <--- CAMBIO: Devuelve Int
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(AchievementEntry.COLUMN_NAME_IS_UNLOCKED, 1)
        }

        val selection = "${BaseColumns._ID} = ?"
        val selectionArgs = arrayOf(achievementId.toString())

        // Devuelve el número de filas actualizadas
        return db.update( // <--- CAMBIO
            AchievementEntry.TABLE_NAME,
            values,
            selection,
            selectionArgs
        )
    }
}