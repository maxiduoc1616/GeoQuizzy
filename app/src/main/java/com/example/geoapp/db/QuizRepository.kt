// En este archivo se define el repositorio QuizRepository que maneja todas las operaciones de la base de datos local para la aplicación GeoApp.
// Utiliza corrutinas para operaciones asíncronas y sigue el patrón de diseño de repositorio.

// Evaluación Parcial 2
// Integrantes: Diego Rodríguez, Maximiliano Gangas, Bastian González


package com.example.geoapp.db

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// Repositorio que maneja las operaciones de la base de datos
object QuizRepository {

    // Inserta un nuevo puntaje en la base de datos.
    // Devuelve un Result con el ID del nuevo registro insertado.
    suspend fun insertScore(context: Context, username: String, score: Int): Result<Long> {
        return withContext(Dispatchers.IO) {
            runCatching {
                QuizDbHelper(context).use { dbHelper ->
                    dbHelper.insertScore(username, score)
                }
            }
        }
    }

    // Desbloquea un logro dado su ID.
    // Devuelve un Result con el número de filas afectadas.
    suspend fun unlockAchievement(context: Context, achievementId: Long): Result<Int> {
        return withContext(Dispatchers.IO) {
            runCatching {
                QuizDbHelper(context).use { dbHelper ->
                    dbHelper.unlockAchievement(achievementId)
                }
            }
        }
    }

    // Obtiene la lista de los 10 mejores puntajes.
    // Devuelve un Result con la lista de Scores.
    suspend fun getHighScores(context: Context): Result<List<Score>> {
        return withContext(Dispatchers.IO) {
            runCatching {
                QuizDbHelper(context).use { dbHelper ->
                    dbHelper.getHighScores()
                }
            }
        }
    }

    // Obtiene la lista de logros.
    // Devuelve un Result con la lista de Achievements.
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