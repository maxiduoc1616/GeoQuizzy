// En este archivo se define la clase QuizDbHelper que maneja la creación y actualización de la base de datos SQLite para la aplicación GeoApp.

// Evaluación Parcial 2
// Integrantes: Diego Rodríguez, Maximiliano Gangas, Bastian González


package com.example.geoapp.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import com.example.geoapp.db.DatabaseContract.AchievementEntry
import com.example.geoapp.db.DatabaseContract.ScoreEntry

// Nueva tabla para puntajes
private val SQL_CREATE_SCORES_TABLE =
    "CREATE TABLE ${ScoreEntry.TABLE_NAME} (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY," +
            "${ScoreEntry.COLUMN_NAME_USERNAME} TEXT," +
            "${ScoreEntry.COLUMN_NAME_SCORE} INTEGER," +
            "${ScoreEntry.COLUMN_NAME_DATE} LONG)"

// Nueva tabla para logros
private val SQL_CREATE_ACHIEVEMENTS_TABLE =
    "CREATE TABLE ${AchievementEntry.TABLE_NAME} (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY," +
            "${AchievementEntry.COLUMN_NAME_NAME} TEXT," +
            "${AchievementEntry.COLUMN_NAME_DESCRIPTION} TEXT," +
            "${AchievementEntry.COLUMN_NAME_ICON_ID} INTEGER NOT NULL," +
            "${AchievementEntry.COLUMN_NAME_IS_UNLOCKED} INTEGER DEFAULT 0)" // 0 = false

// Sentencias SQL para eliminar las tablas
private val SQL_DELETE_SCORES_TABLE = "DROP TABLE IF EXISTS ${ScoreEntry.TABLE_NAME}"
private val SQL_DELETE_ACHIEVEMENTS_TABLE = "DROP TABLE IF EXISTS ${AchievementEntry.TABLE_NAME}"

// Clase que ayuda a manejar la base de datos SQLite
class QuizDbHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    // Versión y nombre de la base de datos
    companion object {
        // Incrementa la versión para forzar onUpgrade y recrear la tabla con el nuevo campo.
        const val DATABASE_VERSION = 2
        const val DATABASE_NAME = "GeoQuiz.db"
    }

    // Crea las tablas cuando se crea la base de datos por primera vez
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_SCORES_TABLE)
        db.execSQL(SQL_CREATE_ACHIEVEMENTS_TABLE)
        prePopulateAchievements(db)
    }

    // Actualiza la base de datos cuando se cambia la versión
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_SCORES_TABLE)
        db.execSQL(SQL_DELETE_ACHIEVEMENTS_TABLE)
        onCreate(db)
    }

    // Pre-pobla la tabla de logros con algunos logros iniciales
    private fun prePopulateAchievements(db: SQLiteDatabase) {
        val achievements = listOf(
            Achievement(id = 1, name = "Primeros Pasos", description = "Completa tu primer quiz", iconResId = 0, isUnlocked = false),
            Achievement(id = 2, name = "Cerebrito", description = "Consigue un 100% de aciertos (10/10)", iconResId = 0, isUnlocked = false),
            Achievement(id = 3, name = "Trotamundos", description = "Responde 50 preguntas correctamente en total", iconResId = 0, isUnlocked = false)
        )

        for (ach in achievements) {
            val values = ContentValues().apply {
                put(BaseColumns._ID, ach.id)
                put(AchievementEntry.COLUMN_NAME_NAME, ach.name)
                put(AchievementEntry.COLUMN_NAME_DESCRIPTION, ach.description)
                put(AchievementEntry.COLUMN_NAME_ICON_ID, ach.iconResId)
                put(AchievementEntry.COLUMN_NAME_IS_UNLOCKED, if (ach.isUnlocked) 1 else 0)
            }
            db.insert(AchievementEntry.TABLE_NAME, null, values)
        }
    }

    // Inserta un nuevo puntaje en la tabla de puntajes
    fun insertScore(username: String, score: Int): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(ScoreEntry.COLUMN_NAME_USERNAME, username)
            put(ScoreEntry.COLUMN_NAME_SCORE, score)
            put(ScoreEntry.COLUMN_NAME_DATE, System.currentTimeMillis())
        }
        return db.insert(ScoreEntry.TABLE_NAME, null, values)
    }

    // Obtiene los 10 mejores puntajes ordenados de mayor a menor
    fun getHighScores(): List<Score> {
        val scoreList = mutableListOf<Score>()
        val db = this.readableDatabase
        val cursor = db.query(
            ScoreEntry.TABLE_NAME, null, null, null, null, null,
            "${ScoreEntry.COLUMN_NAME_SCORE} DESC", "10"
        )
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

    // Obtiene todos los logros
    fun getAchievements(): List<Achievement> {
        val achievementList = mutableListOf<Achievement>()
        val db = this.readableDatabase
        val cursor = db.query(
            AchievementEntry.TABLE_NAME, null, null, null, null, null,
            "${BaseColumns._ID} ASC"
        )

        with(cursor) {
            while (moveToNext()) {
                val achievement = Achievement(
                    id = getInt(getColumnIndexOrThrow(BaseColumns._ID)),
                    name = getString(getColumnIndexOrThrow(AchievementEntry.COLUMN_NAME_NAME)),
                    description = getString(getColumnIndexOrThrow(AchievementEntry.COLUMN_NAME_DESCRIPTION)),
                    iconResId = getInt(getColumnIndexOrThrow(AchievementEntry.COLUMN_NAME_ICON_ID)),
                    isUnlocked = getInt(getColumnIndexOrThrow(AchievementEntry.COLUMN_NAME_IS_UNLOCKED)) == 1
                )
                achievementList.add(achievement)
            }
        }
        cursor.close()
        return achievementList
    }

    // Desbloquea un logro dado su ID
    fun unlockAchievement(achievementId: Long): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(AchievementEntry.COLUMN_NAME_IS_UNLOCKED, 1)
        }
        val selection = "${BaseColumns._ID} = ?"
        val selectionArgs = arrayOf(achievementId.toString())
        return db.update(
            AchievementEntry.TABLE_NAME, values, selection, selectionArgs
        )
    }
}
