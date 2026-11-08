package com.example.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.model.AppLanguage
import com.example.model.CategoryId
import com.example.model.LettersActivityId
import com.example.model.LettersCurriculum
import com.example.model.LettersSectionMode
import com.example.model.NumbersActivityId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "fun_learning_world_prefs")

data class UserSettingsState(
    val language: AppLanguage = AppLanguage.AR,
    val soundEnabled: Boolean = true,
    val dailyGoalMinutes: Int = 20,
    val categoryStars: Map<CategoryId, Int> = defaultStarterStars(),
    val completedNumbersActivities: Set<NumbersActivityId> = emptySet(),
    val completedLettersActivities: Set<LettersActivityId> = emptySet(),
    val learnedArabicLetters: Set<String> = emptySet(),
    val learnedEnglishLetters: Set<String> = emptySet()
) {
    val numbersActivityStars: Int
        get() = completedNumbersActivities.size

    val totalNumbersActivities: Int
        get() = NumbersActivityId.entries.size

    val numbersWorldProgressFraction: Float
        get() = (numbersActivityStars.toFloat() / totalNumbersActivities.coerceAtLeast(1)).coerceIn(0f, 1f)

    val lettersActivityStars: Int
        get() = completedLettersActivities.size

    val totalLettersActivities: Int
        get() = LettersActivityId.entries.size

    val lettersWorldProgressFraction: Float
        get() = (lettersActivityStars.toFloat() / totalLettersActivities.coerceAtLeast(1)).coerceIn(0f, 1f)

    val arabicLettersLearnedCount: Int
        get() = learnedArabicLetters.size

    val englishLettersLearnedCount: Int
        get() = learnedEnglishLetters.size

    val totalEarnedStars: Int
        get() = categoryStars.values.sum() + completedNumbersActivities.size + completedLettersActivities.size

    val maxPossibleStars: Int
        get() = CategoryId.entries.size * 3 + NumbersActivityId.entries.size + LettersActivityId.entries.size

    val progressFraction: Float
        get() = (totalEarnedStars.toFloat() / maxPossibleStars.coerceAtLeast(1)).coerceIn(0f, 1f)
}

private fun defaultStarterStars(): Map<CategoryId, Int> = mapOf(
    CategoryId.NUMBERS to 1,
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
        val COMPLETED_NUMBERS_ACTIVITIES = stringSetPreferencesKey("completed_numbers_activities")
        val COMPLETED_LETTERS_ACTIVITIES = stringSetPreferencesKey("completed_letters_activities")
        val LEARNED_ARABIC_LETTERS = stringSetPreferencesKey("learned_arabic_letters")
        val LEARNED_ENGLISH_LETTERS = stringSetPreferencesKey("learned_english_letters")

        fun starKey(categoryId: CategoryId) = intPreferencesKey("stars_${categoryId.key}")
    }

    val userSettingsFlow: Flow<UserSettingsState> = context.dataStore.data.map { prefs ->
        val langCode = prefs[Keys.LANGUAGE] ?: AppLanguage.AR.code
        val language = if (langCode == AppLanguage.EN.code) AppLanguage.EN else AppLanguage.AR
        val soundEnabled = prefs[Keys.SOUND_ENABLED] ?: true
        val dailyGoal = prefs[Keys.DAILY_GOAL_MINUTES] ?: 20
        val initialized = prefs[Keys.INITIALIZED] ?: false
        val defaults = defaultStarterStars()

        val completedNumberKeys = prefs[Keys.COMPLETED_NUMBERS_ACTIVITIES] ?: emptySet()
        val completedNumbersActivities = NumbersActivityId.entries.filter { it.key in completedNumberKeys }.toSet()

        val completedLetterKeys = prefs[Keys.COMPLETED_LETTERS_ACTIVITIES] ?: emptySet()
        val completedLettersActivities = LettersActivityId.entries.filter { it.key in completedLetterKeys }.toSet()

        val validArabicIds = LettersCurriculum.arabicLetters.map { it.id }.toSet()
        val validEnglishIds = LettersCurriculum.englishLetters.map { it.id }.toSet()
        val learnedArabic = (prefs[Keys.LEARNED_ARABIC_LETTERS] ?: emptySet()).intersect(validArabicIds)
        val learnedEnglish = (prefs[Keys.LEARNED_ENGLISH_LETTERS] ?: emptySet()).intersect(validEnglishIds)

        val starsMap = CategoryId.entries.associateWith { id ->
            val saved = prefs[Keys.starKey(id)]
            val baseStars = if (saved != null) {
                saved.coerceIn(0, 3)
            } else if (!initialized) {
                defaults[id] ?: 1
            } else {
                0
            }
            when {
                id == CategoryId.NUMBERS && completedNumbersActivities.isNotEmpty() -> {
                    val derivedStars = when {
                        completedNumbersActivities.size >= 5 -> 3
                        completedNumbersActivities.size >= 2 -> 2
                        else -> 1
                    }
                    maxOf(baseStars, derivedStars)
                }

                (id == CategoryId.ARABIC_LETTERS || id == CategoryId.ENGLISH_LETTERS) &&
                    (completedLettersActivities.isNotEmpty() || learnedArabic.isNotEmpty() || learnedEnglish.isNotEmpty()) -> {
                    val derivedStars = when {
                        completedLettersActivities.size >= 5 || learnedArabic.size >= 14 || learnedEnglish.size >= 13 -> 3
                        completedLettersActivities.size >= 2 || learnedArabic.size >= 5 || learnedEnglish.size >= 5 -> 2
                        else -> 1
                    }
                    maxOf(baseStars, derivedStars)
                }

                else -> baseStars
            }
        }

        UserSettingsState(
            language = language,
            soundEnabled = soundEnabled,
            dailyGoalMinutes = dailyGoal,
            categoryStars = starsMap,
            completedNumbersActivities = completedNumbersActivities,
            completedLettersActivities = completedLettersActivities,
            learnedArabicLetters = learnedArabic,
            learnedEnglishLetters = learnedEnglish
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

    /**
     * Marks a Numbers World activity as completed and awards its star ONLY once.
     */
    suspend fun markNumbersActivityCompleted(activityId: NumbersActivityId): Boolean {
        var newlyAwarded = false
        context.dataStore.edit { prefs ->
            ensureInitialized(prefs)
            val currentSet = prefs[Keys.COMPLETED_NUMBERS_ACTIVITIES] ?: emptySet()
            if (activityId.key !in currentSet) {
                val updated = currentSet + activityId.key
                prefs[Keys.COMPLETED_NUMBERS_ACTIVITIES] = updated
                newlyAwarded = true

                val count = updated.size
                val categoryStarLevel = when {
                    count >= 5 -> 3
                    count >= 2 -> 2
                    else -> 1
                }
                val existingCatStars = prefs[Keys.starKey(CategoryId.NUMBERS)] ?: 1
                if (categoryStarLevel > existingCatStars) {
                    prefs[Keys.starKey(CategoryId.NUMBERS)] = categoryStarLevel
                }
            }
        }
        return newlyAwarded
    }

    /**
     * Marks a Letters World activity as completed and awards its star ONLY once
     * (prevents duplicate rewards for repeated attempts).
     */
    suspend fun markLettersActivityCompleted(activityId: LettersActivityId): Boolean {
        var newlyAwarded = false
        context.dataStore.edit { prefs ->
            ensureInitialized(prefs)
            val currentSet = prefs[Keys.COMPLETED_LETTERS_ACTIVITIES] ?: emptySet()
            if (activityId.key !in currentSet) {
                val updated = currentSet + activityId.key
                prefs[Keys.COMPLETED_LETTERS_ACTIVITIES] = updated
                newlyAwarded = true

                val count = updated.size
                val categoryStarLevel = when {
                    count >= 5 -> 3
                    count >= 2 -> 2
                    else -> 1
                }
                listOf(CategoryId.ARABIC_LETTERS, CategoryId.ENGLISH_LETTERS).forEach { catId ->
                    val existing = prefs[Keys.starKey(catId)] ?: 1
                    if (categoryStarLevel > existing) {
                        prefs[Keys.starKey(catId)] = categoryStarLevel
                    }
                }
            }
        }
        return newlyAwarded
    }

    /**
     * Records a letter as learned in either the Arabic Letters (28) or English Letters (26) section.
     * Prevents duplicate entries when the child revisits the same letter.
     */
    suspend fun markLetterLearned(letterId: String, section: LettersSectionMode): Boolean {
        var newlyLearned = false
        context.dataStore.edit { prefs ->
            ensureInitialized(prefs)
            val key = if (section == LettersSectionMode.ARABIC) {
                Keys.LEARNED_ARABIC_LETTERS
            } else {
                Keys.LEARNED_ENGLISH_LETTERS
            }
            val current = prefs[key] ?: emptySet()
            if (letterId !in current) {
                val updated = current + letterId
                prefs[key] = updated
                newlyLearned = true

                val catId = if (section == LettersSectionMode.ARABIC) {
                    CategoryId.ARABIC_LETTERS
                } else {
                    CategoryId.ENGLISH_LETTERS
                }
                val starLevel = when {
                    updated.size >= 14 -> 3
                    updated.size >= 5 -> 2
                    else -> 1
                }
                val existing = prefs[Keys.starKey(catId)] ?: 1
                if (starLevel > existing) {
                    prefs[Keys.starKey(catId)] = starLevel
                }
            }
        }
        return newlyLearned
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
            prefs[Keys.COMPLETED_NUMBERS_ACTIVITIES] = emptySet()
            prefs[Keys.COMPLETED_LETTERS_ACTIVITIES] = emptySet()
            prefs[Keys.LEARNED_ARABIC_LETTERS] = emptySet()
            prefs[Keys.LEARNED_ENGLISH_LETTERS] = emptySet()
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
