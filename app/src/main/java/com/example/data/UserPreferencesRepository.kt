package com.example.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.model.AppLanguage
import com.example.model.CategoryId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "fun_learning_world_prefs")

data class UserSettingsState(
    val language: AppLanguage = AppLanguage.AR,
    val soundEnabled: Boolean = true,
    val dailyGoalMinutes: Int = 20,
    val categoryStars: Map<CategoryId, Int> = defaultStarterStars()
) {
    val totalEarnedStars: Int
        get() = categoryStars.values.sum()

    val maxPossibleStars: Int
        get() = CategoryId.entries.size * 3

    val progressFraction: Float
        get() = (totalEarnedStars.toFloat() / maxPossibleStars.coerceAtLeast(1)).coerceIn(0f, 1f)
}

private fun defaultStarterStars(): Map<CategoryId, Int> = mapOf(
    CategoryId.NUMBERS to 2,
    CategoryId.ARABIC_LETTERS to 2,
    CategoryId.ENGLISH_LETTERS to 1,
    CategoryId.COLORS to 2,
    CategoryId.SHAPES to 1,
    CategoryId.ANIMALS to 2,
    CategoryId.BRAIN_GAMES to 1,
    CategoryId.WRITING_DRAWING to 1
)

class UserPreferencesRepository(private val context: Context) {

    private object Keys {
        val LANGUAGE = stringPreferencesKey("app_language")
        val SOUND_ENABLED = booleanPreferencesKey("sound_enabled")
        val DAILY_GOAL_MINUTES = intPreferencesKey("daily_goal_minutes")
        val INITIALIZED = booleanPreferencesKey("stars_initialized")

        fun starKey(categoryId: CategoryId) = intPreferencesKey("stars_${categoryId.key}")
    }

    val userSettingsFlow: Flow<UserSettingsState> = context.dataStore.data.map { prefs ->
        val langCode = prefs[Keys.LANGUAGE] ?: AppLanguage.AR.code
        val language = if (langCode == AppLanguage.EN.code) AppLanguage.EN else AppLanguage.AR
        val soundEnabled = prefs[Keys.SOUND_ENABLED] ?: true
        val dailyGoal = prefs[Keys.DAILY_GOAL_MINUTES] ?: 20
        val initialized = prefs[Keys.INITIALIZED] ?: false
        val defaults = defaultStarterStars()

        val starsMap = CategoryId.entries.associateWith { id ->
            val saved = prefs[Keys.starKey(id)]
            if (saved != null) {
                saved.coerceIn(0, 3)
            } else if (!initialized) {
                defaults[id] ?: 1
            } else {
                0
            }
        }

        UserSettingsState(
            language = language,
            soundEnabled = soundEnabled,
            dailyGoalMinutes = dailyGoal,
            categoryStars = starsMap
        )
    }

    suspend fun setLanguage(language: AppLanguage) {
        context.dataStore.edit { prefs ->
            prefs[Keys.LANGUAGE] = language.code
        }
    }

    suspend fun setSoundEnabled(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[Keys.SOUND_ENABLED] = enabled
        }
    }

    suspend fun setDailyGoalMinutes(minutes: Int) {
        context.dataStore.edit { prefs ->
            prefs[Keys.DAILY_GOAL_MINUTES] = minutes.coerceIn(10, 60)
        }
    }

    suspend fun incrementCategoryStar(categoryId: CategoryId) {
        context.dataStore.edit { prefs ->
            ensureInitialized(prefs)
            val current = prefs[Keys.starKey(categoryId)] ?: (defaultStarterStars()[categoryId] ?: 1)
            val next = if (current >= 3) 1 else current + 1
            prefs[Keys.starKey(categoryId)] = next
        }
    }

    suspend fun resetAllStars() {
        context.dataStore.edit { prefs ->
            prefs[Keys.INITIALIZED] = true
            CategoryId.entries.forEach { id ->
                prefs[Keys.starKey(id)] = 0
            }
        }
    }

    suspend fun restoreStarterStars() {
        context.dataStore.edit { prefs ->
            prefs[Keys.INITIALIZED] = true
            val defaults = defaultStarterStars()
            CategoryId.entries.forEach { id ->
                prefs[Keys.starKey(id)] = defaults[id] ?: 1
            }
        }
    }

    private fun ensureInitialized(prefs: androidx.datastore.preferences.core.MutablePreferences) {
        if (prefs[Keys.INITIALIZED] != true) {
            prefs[Keys.INITIALIZED] = true
            val defaults = defaultStarterStars()
            CategoryId.entries.forEach { id ->
                if (prefs[Keys.starKey(id)] == null) {
                    prefs[Keys.starKey(id)] = defaults[id] ?: 1
                }
            }
        }
    }
}
