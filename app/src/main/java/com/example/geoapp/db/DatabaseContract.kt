package com.example.geoapp.db // Asegúrate que este sea tu paquete

import android.provider.BaseColumns

// Objeto singleton para definir el esquema de la base de datos (contrato)
object DatabaseContract {

    // Constantes para la tabla Scores (Ranking)
    object ScoreEntry : BaseColumns {
        const val TABLE_NAME = "scores"
        const val COLUMN_NAME_USERNAME = "username"
        const val COLUMN_NAME_SCORE = "score"
        const val COLUMN_NAME_DATE = "date" // Usaremos un Long para la fecha (timestamp)
    }

    // Constantes para la tabla Achievements (Logros)
    object AchievementEntry : BaseColumns {
        const val TABLE_NAME = "achievements"
        const val COLUMN_NAME_NAME = "name" // Nombre del logro (ej. "Cerebrito")
        const val COLUMN_NAME_DESCRIPTION = "description" // Descripción (ej. "Consigue 100%")
        const val COLUMN_NAME_IS_UNLOCKED = "is_unlocked" // 0 = false, 1 = true
    }
}