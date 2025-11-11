// En este archivo se define el contrato de la base de datos para la aplicación GeoApp, incluyendo las tablas de puntuaciones y logros.

// Evaluación Parcial 2
// Integrantes: Diego Rodríguez, Maximiliano Gangas, Bastian González


package com.example.geoapp.db

import android.provider.BaseColumns

object DatabaseContract {

    // Definición de la tabla de puntuaciones
    object ScoreEntry : BaseColumns {
        const val TABLE_NAME = "scores"
        const val COLUMN_NAME_USERNAME = "username"
        const val COLUMN_NAME_SCORE = "score"
        const val COLUMN_NAME_DATE = "date"
    }

    // Definición de la tabla de logros
    object AchievementEntry : BaseColumns {
        const val TABLE_NAME = "achievements"
        const val COLUMN_NAME_NAME = "name"
        const val COLUMN_NAME_DESCRIPTION = "description"
        const val COLUMN_NAME_ICON_ID = "icon_id"
        const val COLUMN_NAME_IS_UNLOCKED = "is_unlocked"
    }
}