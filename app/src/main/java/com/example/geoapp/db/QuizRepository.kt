package com.example.geoapp.db

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Repositorio Singleton para manejar TODAS las operaciones de la base de datos local.
 * Sigue el patrón del proyecto de referencia:
 * - Es un 'object' (Singleton)
 * - Usa funciones 'suspend'
 * - Ejecuta todo en 'Dispatchers.IO'
 * - Devuelve 'Result<T>' para manejo de errores
 * - Usa 'QuizDbHelper(context).use { ... }' para auto-cerrar la BD
 */
object QuizRepository {

    /**
     * Inserta un puntaje.
     * Devuelve un Result con el ID de la fila.
     */
    suspend fun insertScore(context: Context, username: String, score: Int): Result<Long> {
        return withContext(Dispatchers.IO) {
            runCatching {
                QuizDbHelper(context).use { dbHelper ->
                    dbHelper.insertScore(username, score)
                }
            }
        }
    }

    /**
     * Desbloquea un logro.
     * Devuelve un Result con el número de filas afectadas.
     */
    suspend fun unlockAchievement(context: Context, achievementId: Long): Result<Int> {
        return withContext(Dispatchers.IO) {
            runCatching {
                QuizDbHelper(context).use { dbHelper ->
                    dbHelper.unlockAchievement(achievementId)
                }
            }
        }
    }

    /**
     * Obtiene la lista de mejores puntajes.
     * Devuelve un Result con la lista de Scores.
     */
    suspend fun getHighScores(context: Context): Result<List<Score>> {
        return withContext(Dispatchers.IO) {
            runCatching {
                QuizDbHelper(context).use { dbHelper ->
                    dbHelper.getHighScores()
                }
            }
        }
    }

    /**
     * Obtiene la lista de todos los logros.
     * Devuelve un Result con la lista de Achievements.
     */
    suspend fun getAchievements(context: Context): Result<List<Achievement>> {
        return withContext(Dispatchers.IO) {
            runCatching {
                QuizDbHelper(context).use { dbHelper ->
                    dbHelper.getAchievements()
                }
            }
        }
    }
}