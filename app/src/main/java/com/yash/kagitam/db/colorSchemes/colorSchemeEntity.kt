package com.yash.kagitam.db.colorSchemes

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * ColorScheme need at least 6 colors
 */
@Entity(tableName = "color_schemes")
data class ColorSchemeEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,                 // Theme name

    // Primary colors
    val primary: Long,
    val onPrimary: Long,
    val primaryContainer: Long?,
    val onPrimaryContainer: Long?,

    // Secondary colors
    val secondary: Long,
    val onSecondary: Long,
    val secondaryContainer: Long?,
    val onSecondaryContainer: Long?,

    // Tertiary colors
    val tertiary: Long,
    val onTertiary: Long?,
    val tertiaryContainer: Long?,
    val onTertiaryContainer: Long?,

    // Error colors
    val error: Long,
    val onError: Long?,
    val errorContainer: Long?,
    val onErrorContainer: Long?,

    // Surface / Background
    val background: Long?,
    val onBackground: Long?,
    val surface: Long?,
    val onSurface: Long?,
    val surfaceVariant: Long?,
    val onSurfaceVariant: Long?,
    val outline: Long?,
    val inverseOnSurface: Long?,
    val inverseSurface: Long?,
    val inversePrimary: Long?,
    val surfaceTint: Long?,
    val outlineVariant: Long?,
    val scrim: Long?
)
