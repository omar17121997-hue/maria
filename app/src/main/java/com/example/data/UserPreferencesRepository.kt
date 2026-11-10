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
import com.example.model.ActivityLogEntry
import com.example.model.AnimalsActivityId
import com.example.model.AppLanguage
import com.example.model.BrainGameActivityId
import com.example.model.CategoryId
import com.example.model.ColorsActivityId
import com.example.model.LettersActivityId
import com.example.model.LettersCurriculum
import com.example.model.LettersSectionMode
import com.example.model.NumbersActivityId
import com.example.model.ShapesActivityId
import com.example.model.SurpriseRewardItem
import com.example.model.WritingDrawingActivityId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "fun_learning_world_prefs")

data class UserSettingsState(
    val language: AppLanguage = AppLanguage.AR,
    val soundEnabled: Boolean = true,
    val dailyGoalMinutes: Int = 20,
    val childName: String = "بطل التعلّم",
    val parentPin: String = "",
    val categoryStars: Map<CategoryId, Int> = defaultStarterStars(),
    val completedNumbersActivities: Set<NumbersActivityId> = emptySet(),
    val completedLettersActivities: Set<LettersActivityId> = emptySet(),
    val learnedArabicLetters: Set<String> = emptySet(),
    val learnedEnglishLetters: Set<String> = emptySet(),
    val completedColorsActivities: Set<ColorsActivityId> = emptySet(),
    val completedShapesActivities: Set<ShapesActivityId> = emptySet(),
    val completedAnimalsActivities: Set<AnimalsActivityId> = emptySet(),
    val completedBrainActivities: Set<BrainGameActivityId> = emptySet(),
    val brainMaxLevelReached: Int = 1,
    val completedWritingActivities: Set<WritingDrawingActivityId> = emptySet(),
    val practicedWritingItems: Set<String> = emptySet(),
    val savedDrawingsTitles: Set<String> = emptySet(),
    val celebratedAchievementIds: Set<String> = emptySet(),
    val openedSurpriseIds: Set<String> = emptySet(),
    val equippedLuluAccessory: String = "",
    val surpriseBonusStars: Int = 0,
    val activityLogRawEntries: Set<String> = emptySet()
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

    val colorsWorldProgressFraction: Float
        get() = (completedColorsActivities.size.toFloat() / ColorsActivityId.entries.size.coerceAtLeast(1)).coerceIn(0f, 1f)

    val shapesWorldProgressFraction: Float
        get() = (completedShapesActivities.size.toFloat() / ShapesActivityId.entries.size.coerceAtLeast(1)).coerceIn(0f, 1f)

    val animalsWorldProgressFraction: Float
        get() = (completedAnimalsActivities.size.toFloat() / AnimalsActivityId.entries.size.coerceAtLeast(1)).coerceIn(0f, 1f)

    val brainWorldProgressFraction: Float
        get() = (completedBrainActivities.size.toFloat() / BrainGameActivityId.entries.size.coerceAtLeast(1)).coerceIn(0f, 1f)

    val writingWorldProgressFraction: Float
        get() = (completedWritingActivities.size.toFloat() / WritingDrawingActivityId.entries.size.coerceAtLeast(1)).coerceIn(0f, 1f)

    val arabicLettersLearnedCount: Int
        get() = learnedArabicLetters.size

    val englishLettersLearnedCount: Int
        get() = learnedEnglishLetters.size

    val totalEarnedStars: Int
        get() = categoryStars.values.sum() +
            completedNumbersActivities.size +
            completedLettersActivities.size +
            completedColorsActivities.size +
            completedShapesActivities.size +
            completedAnimalsActivities.size +
            completedBrainActivities.size +
            completedWritingActivities.size +
            surpriseBonusStars

    val maxPossibleStars: Int
        get() = CategoryId.entries.size * 3 +
            NumbersActivityId.entries.size +
            LettersActivityId.entries.size +
            ColorsActivityId.entries.size +
            ShapesActivityId.entries.size +
            AnimalsActivityId.entries.size +
            BrainGameActivityId.entries.size +
            WritingDrawingActivityId.entries.size

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
        val CHILD_NAME = stringPreferencesKey("child_name")
        val PARENT_PIN = stringPreferencesKey("parent_pin")
        val INITIALIZED = booleanPreferencesKey("stars_initialized")
        val COMPLETED_NUMBERS_ACTIVITIES = stringSetPreferencesKey("completed_numbers_activities")
        val COMPLETED_LETTERS_ACTIVITIES = stringSetPreferencesKey("completed_letters_activities")
        val LEARNED_ARABIC_LETTERS = stringSetPreferencesKey("learned_arabic_letters")
        val LEARNED_ENGLISH_LETTERS = stringSetPreferencesKey("learned_english_letters")
        val COMPLETED_COLORS_ACTIVITIES = stringSetPreferencesKey("completed_colors_activities")
        val COMPLETED_SHAPES_ACTIVITIES = stringSetPreferencesKey("completed_shapes_activities")
        val COMPLETED_ANIMALS_ACTIVITIES = stringSetPreferencesKey("completed_animals_activities")
        val COMPLETED_BRAIN_ACTIVITIES = stringSetPreferencesKey("completed_brain_activities")
        val BRAIN_MAX_LEVEL = intPreferencesKey("brain_max_level")
        val COMPLETED_WRITING_ACTIVITIES = stringSetPreferencesKey("completed_writing_activities")
        val PRACTICED_WRITING_ITEMS = stringSetPreferencesKey("practiced_writing_items")
        val SAVED_DRAWINGS_TITLES = stringSetPreferencesKey("saved_drawings_titles")
        val CELEBRATED_ACHIEVEMENTS = stringSetPreferencesKey("celebrated_achievements")
        val OPENED_SURPRISES = stringSetPreferencesKey("opened_surprises")
        val EQUIPPED_LULU_ACCESSORY = stringPreferencesKey("equipped_lulu_accessory")
        val SURPRISE_BONUS_STARS = intPreferencesKey("surprise_bonus_stars")
        val ACTIVITY_LOG_ENTRIES = stringSetPreferencesKey("activity_log_entries")

        fun starKey(categoryId: CategoryId) = intPreferencesKey("stars_${categoryId.key}")
    }

    val userSettingsFlow: Flow<UserSettingsState> = context.dataStore.data.map { prefs ->
        val langCode = prefs[Keys.LANGUAGE] ?: AppLanguage.AR.code
        val language = if (langCode == AppLanguage.EN.code) AppLanguage.EN else AppLanguage.AR
        val soundEnabled = prefs[Keys.SOUND_ENABLED] ?: true
        val dailyGoal = prefs[Keys.DAILY_GOAL_MINUTES] ?: 20
        val childName = prefs[Keys.CHILD_NAME]?.takeIf { it.isNotBlank() } ?: "بطل التعلّم"
        val parentPin = prefs[Keys.PARENT_PIN] ?: ""
        val initialized = prefs[Keys.INITIALIZED] ?: false
        val defaults = defaultStarterStars()

        val completedNumberKeys = prefs[Keys.COMPLETED_NUMBERS_ACTIVITIES] ?: emptySet()
        val completedNumbersActivities = NumbersActivityId.entries.filter { it.key in completedNumberKeys }.toSet()

        val completedLetterKeys = prefs[Keys.COMPLETED_LETTERS_ACTIVITIES] ?: emptySet()
        val completedLettersActivities = LettersActivityId.entries.filter { it.key in completedLetterKeys }.toSet()

        val completedColorKeys = prefs[Keys.COMPLETED_COLORS_ACTIVITIES] ?: emptySet()
        val completedColorsActivities = ColorsActivityId.entries.filter { it.key in completedColorKeys }.toSet()

        val completedShapeKeys = prefs[Keys.COMPLETED_SHAPES_ACTIVITIES] ?: emptySet()
        val completedShapesActivities = ShapesActivityId.entries.filter { it.key in completedShapeKeys }.toSet()

        val completedAnimalKeys = prefs[Keys.COMPLETED_ANIMALS_ACTIVITIES] ?: emptySet()
        val completedAnimalsActivities = AnimalsActivityId.entries.filter { it.key in completedAnimalKeys }.toSet()

        val completedBrainKeys = prefs[Keys.COMPLETED_BRAIN_ACTIVITIES] ?: emptySet()
        val completedBrainActivities = BrainGameActivityId.entries.filter { it.key in completedBrainKeys }.toSet()
        val brainMaxLevel = (prefs[Keys.BRAIN_MAX_LEVEL] ?: 1).coerceIn(1, 3)

        val completedWritingKeys = prefs[Keys.COMPLETED_WRITING_ACTIVITIES] ?: emptySet()
        val completedWritingActivities = WritingDrawingActivityId.entries.filter { it.key in completedWritingKeys }.toSet()
        val practicedWritingItems = prefs[Keys.PRACTICED_WRITING_ITEMS] ?: emptySet()
        val savedDrawingsTitles = prefs[Keys.SAVED_DRAWINGS_TITLES] ?: emptySet()

        val celebratedAchievements = prefs[Keys.CELEBRATED_ACHIEVEMENTS] ?: emptySet()
        val openedSurprises = prefs[Keys.OPENED_SURPRISES] ?: emptySet()
        val equippedLuluAccessory = prefs[Keys.EQUIPPED_LULU_ACCESSORY] ?: ""
        val surpriseBonusStars = (prefs[Keys.SURPRISE_BONUS_STARS] ?: 0).coerceAtLeast(0)
        val activityLogEntries = prefs[Keys.ACTIVITY_LOG_ENTRIES] ?: emptySet()

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

                id == CategoryId.COLORS && completedColorsActivities.isNotEmpty() -> {
                    val derivedStars = when {
                        completedColorsActivities.size >= 3 -> 3
                        completedColorsActivities.size >= 2 -> 2
                        else -> 1
                    }
                    maxOf(baseStars, derivedStars)
                }

                id == CategoryId.SHAPES && completedShapesActivities.isNotEmpty() -> {
                    val derivedStars = when {
                        completedShapesActivities.size >= 3 -> 3
                        completedShapesActivities.size >= 2 -> 2
                        else -> 1
                    }
                    maxOf(baseStars, derivedStars)
                }

                id == CategoryId.ANIMALS && completedAnimalsActivities.isNotEmpty() -> {
                    val derivedStars = when {
                        completedAnimalsActivities.size >= 3 -> 3
                        completedAnimalsActivities.size >= 2 -> 2
                        else -> 1
                    }
                    maxOf(baseStars, derivedStars)
                }

                id == CategoryId.BRAIN_GAMES && completedBrainActivities.isNotEmpty() -> {
                    val derivedStars = when {
                        completedBrainActivities.size >= 5 -> 3
                        completedBrainActivities.size >= 2 -> 2
                        else -> 1
                    }
                    maxOf(baseStars, derivedStars)
                }

                id == CategoryId.WRITING_DRAWING &&
                    (completedWritingActivities.isNotEmpty() || practicedWritingItems.isNotEmpty() || savedDrawingsTitles.isNotEmpty()) -> {
                    val derivedStars = when {
                        completedWritingActivities.size >= 5 || practicedWritingItems.size >= 12 -> 3
                        completedWritingActivities.size >= 2 || practicedWritingItems.size >= 4 || savedDrawingsTitles.isNotEmpty() -> 2
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
            childName = childName,
            parentPin = parentPin,
            categoryStars = starsMap,
            completedNumbersActivities = completedNumbersActivities,
            completedLettersActivities = completedLettersActivities,
            learnedArabicLetters = learnedArabic,
            learnedEnglishLetters = learnedEnglish,
            completedColorsActivities = completedColorsActivities,
            completedShapesActivities = completedShapesActivities,
            completedAnimalsActivities = completedAnimalsActivities,
            completedBrainActivities = completedBrainActivities,
            brainMaxLevelReached = brainMaxLevel,
            completedWritingActivities = completedWritingActivities,
            practicedWritingItems = practicedWritingItems,
            savedDrawingsTitles = savedDrawingsTitles,
            celebratedAchievementIds = celebratedAchievements,
            openedSurpriseIds = openedSurprises,
            equippedLuluAccessory = equippedLuluAccessory,
            surpriseBonusStars = surpriseBonusStars,
            activityLogRawEntries = activityLogEntries
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

    suspend fun setChildName(name: String) {
        val clean = name.trim().ifBlank { "بطل التعلّم" }
        context.dataStore.edit { prefs ->
            prefs[Keys.CHILD_NAME] = clean
        }
    }

    suspend fun setParentPin(pin: String) {
        val clean = pin.filter { it.isDigit() }.take(4)
        if (clean.length == 4) {
            context.dataStore.edit { prefs ->
                prefs[Keys.PARENT_PIN] = clean
            }
        }
    }

    suspend fun markAchievementsCelebrated(badgeIds: Collection<String>) {
        if (badgeIds.isEmpty()) return
        context.dataStore.edit { prefs ->
            val current = prefs[Keys.CELEBRATED_ACHIEVEMENTS] ?: emptySet()
            prefs[Keys.CELEBRATED_ACHIEVEMENTS] = current + badgeIds
        }
    }

    suspend fun openSurpriseBox(item: SurpriseRewardItem): Boolean {
        var newlyOpened = false
        context.dataStore.edit { prefs ->
            ensureInitialized(prefs)
            val current = prefs[Keys.OPENED_SURPRISES] ?: emptySet()
            if (item.id !in current) {
                prefs[Keys.OPENED_SURPRISES] = current + item.id
                val bonus = (prefs[Keys.SURPRISE_BONUS_STARS] ?: 0) + item.bonusStars
                prefs[Keys.SURPRISE_BONUS_STARS] = bonus
                if (item.isLuluAccessory) {
                    prefs[Keys.EQUIPPED_LULU_ACCESSORY] = item.id
                }
                newlyOpened = true
            }
        }
        return newlyOpened
    }

    suspend fun equipLuluAccessory(accessoryId: String) {
        context.dataStore.edit { prefs ->
            prefs[Keys.EQUIPPED_LULU_ACCESSORY] = accessoryId
        }
    }

    private fun appendActivityLog(
        prefs: androidx.datastore.preferences.core.MutablePreferences,
        id: String,
        worldCategory: CategoryId,
        titleAr: String,
        titleEn: String,
        starsEarned: Int = 1
    ) {
        val current = prefs[Keys.ACTIVITY_LOG_ENTRIES] ?: emptySet()
        val entry = ActivityLogEntry.createNow(
            id = id,
            worldCategory = worldCategory,
            titleAr = titleAr,
            titleEn = titleEn,
            starsEarned = starsEarned
        )
        // Keep up to 60 recent entries
        val updated = (current + entry.serialize()).toList().takeLast(60).toSet()
        prefs[Keys.ACTIVITY_LOG_ENTRIES] = updated
    }

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
            appendActivityLog(
                prefs = prefs,
                id = "num_${activityId.key}_${System.currentTimeMillis()}",
                worldCategory = CategoryId.NUMBERS,
                titleAr = activityId.titleAr,
                titleEn = activityId.titleEn,
                starsEarned = 1
            )
        }
        return newlyAwarded
    }

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
            appendActivityLog(
                prefs = prefs,
                id = "let_${activityId.key}_${System.currentTimeMillis()}",
                worldCategory = CategoryId.ARABIC_LETTERS,
                titleAr = activityId.titleAr,
                titleEn = activityId.titleEn,
                starsEarned = 1
            )
        }
        return newlyAwarded
    }

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

    suspend fun markColorsActivityCompleted(activityId: ColorsActivityId): Boolean {
        var newlyAwarded = false
        context.dataStore.edit { prefs ->
            ensureInitialized(prefs)
            val currentSet = prefs[Keys.COMPLETED_COLORS_ACTIVITIES] ?: emptySet()
            if (activityId.key !in currentSet) {
                val updated = currentSet + activityId.key
                prefs[Keys.COMPLETED_COLORS_ACTIVITIES] = updated
                newlyAwarded = true

                val starLevel = when {
                    updated.size >= 3 -> 3
                    updated.size >= 2 -> 2
                    else -> 1
                }
                val existing = prefs[Keys.starKey(CategoryId.COLORS)] ?: 1
                if (starLevel > existing) {
                    prefs[Keys.starKey(CategoryId.COLORS)] = starLevel
                }
            }
            appendActivityLog(
                prefs = prefs,
                id = "col_${activityId.key}_${System.currentTimeMillis()}",
                worldCategory = CategoryId.COLORS,
                titleAr = activityId.titleAr,
                titleEn = activityId.titleEn,
                starsEarned = 1
            )
        }
        return newlyAwarded
    }

    suspend fun markShapesActivityCompleted(activityId: ShapesActivityId): Boolean {
        var newlyAwarded = false
        context.dataStore.edit { prefs ->
            ensureInitialized(prefs)
            val currentSet = prefs[Keys.COMPLETED_SHAPES_ACTIVITIES] ?: emptySet()
            if (activityId.key !in currentSet) {
                val updated = currentSet + activityId.key
                prefs[Keys.COMPLETED_SHAPES_ACTIVITIES] = updated
                newlyAwarded = true

                val starLevel = when {
                    updated.size >= 3 -> 3
                    updated.size >= 2 -> 2
                    else -> 1
                }
                val existing = prefs[Keys.starKey(CategoryId.SHAPES)] ?: 1
                if (starLevel > existing) {
                    prefs[Keys.starKey(CategoryId.SHAPES)] = starLevel
                }
            }
            appendActivityLog(
                prefs = prefs,
                id = "shp_${activityId.key}_${System.currentTimeMillis()}",
                worldCategory = CategoryId.SHAPES,
                titleAr = activityId.titleAr,
                titleEn = activityId.titleEn,
                starsEarned = 1
            )
        }
        return newlyAwarded
    }

    suspend fun markAnimalsActivityCompleted(activityId: AnimalsActivityId): Boolean {
        var newlyAwarded = false
        context.dataStore.edit { prefs ->
            ensureInitialized(prefs)
            val currentSet = prefs[Keys.COMPLETED_ANIMALS_ACTIVITIES] ?: emptySet()
            if (activityId.key !in currentSet) {
                val updated = currentSet + activityId.key
                prefs[Keys.COMPLETED_ANIMALS_ACTIVITIES] = updated
                newlyAwarded = true

                val starLevel = when {
                    updated.size >= 3 -> 3
                    updated.size >= 2 -> 2
                    else -> 1
                }
                val existing = prefs[Keys.starKey(CategoryId.ANIMALS)] ?: 1
                if (starLevel > existing) {
                    prefs[Keys.starKey(CategoryId.ANIMALS)] = starLevel
                }
            }
            appendActivityLog(
                prefs = prefs,
                id = "ani_${activityId.key}_${System.currentTimeMillis()}",
                worldCategory = CategoryId.ANIMALS,
                titleAr = activityId.titleAr,
                titleEn = activityId.titleEn,
                starsEarned = 1
            )
        }
        return newlyAwarded
    }

    suspend fun markBrainActivityCompleted(activityId: BrainGameActivityId, levelReached: Int = 1): Boolean {
        var newlyAwarded = false
        context.dataStore.edit { prefs ->
            ensureInitialized(prefs)
            val currentSet = prefs[Keys.COMPLETED_BRAIN_ACTIVITIES] ?: emptySet()
            if (activityId.key !in currentSet) {
                val updated = currentSet + activityId.key
                prefs[Keys.COMPLETED_BRAIN_ACTIVITIES] = updated
                newlyAwarded = true

                val starLevel = when {
                    updated.size >= 5 -> 3
                    updated.size >= 2 -> 2
                    else -> 1
                }
                val existing = prefs[Keys.starKey(CategoryId.BRAIN_GAMES)] ?: 1
                if (starLevel > existing) {
                    prefs[Keys.starKey(CategoryId.BRAIN_GAMES)] = starLevel
                }
            }
            val currentLevel = prefs[Keys.BRAIN_MAX_LEVEL] ?: 1
            if (levelReached > currentLevel) {
                prefs[Keys.BRAIN_MAX_LEVEL] = levelReached.coerceIn(1, 3)
            }
            appendActivityLog(
                prefs = prefs,
                id = "brn_${activityId.key}_${System.currentTimeMillis()}",
                worldCategory = CategoryId.BRAIN_GAMES,
                titleAr = activityId.titleAr,
                titleEn = activityId.titleEn,
                starsEarned = 1
            )
        }
        return newlyAwarded
    }

    suspend fun markWritingActivityCompleted(activityId: WritingDrawingActivityId): Boolean {
        var newlyAwarded = false
        context.dataStore.edit { prefs ->
            ensureInitialized(prefs)
            val currentSet = prefs[Keys.COMPLETED_WRITING_ACTIVITIES] ?: emptySet()
            if (activityId.key !in currentSet) {
                val updated = currentSet + activityId.key
                prefs[Keys.COMPLETED_WRITING_ACTIVITIES] = updated
                newlyAwarded = true

                val starLevel = when {
                    updated.size >= 5 -> 3
                    updated.size >= 2 -> 2
                    else -> 1
                }
                val existing = prefs[Keys.starKey(CategoryId.WRITING_DRAWING)] ?: 1
                if (starLevel > existing) {
                    prefs[Keys.starKey(CategoryId.WRITING_DRAWING)] = starLevel
                }
            }
            appendActivityLog(
                prefs = prefs,
                id = "wrt_${activityId.key}_${System.currentTimeMillis()}",
                worldCategory = CategoryId.WRITING_DRAWING,
                titleAr = activityId.titleAr,
                titleEn = activityId.titleEn,
                starsEarned = 1
            )
        }
        return newlyAwarded
    }

    suspend fun markWritingItemPracticed(itemKey: String): Boolean {
        var newlyPracticed = false
        context.dataStore.edit { prefs ->
            ensureInitialized(prefs)
            val current = prefs[Keys.PRACTICED_WRITING_ITEMS] ?: emptySet()
            if (itemKey !in current) {
                val updated = current + itemKey
                prefs[Keys.PRACTICED_WRITING_ITEMS] = updated
                newlyPracticed = true

                val starLevel = when {
                    updated.size >= 12 -> 3
                    updated.size >= 4 -> 2
                    else -> 1
                }
                val existing = prefs[Keys.starKey(CategoryId.WRITING_DRAWING)] ?: 1
                if (starLevel > existing) {
                    prefs[Keys.starKey(CategoryId.WRITING_DRAWING)] = starLevel
                }
            }
        }
        return newlyPracticed
    }

    suspend fun saveChildDrawingEntry(title: String) {
        context.dataStore.edit { prefs ->
            ensureInitialized(prefs)
            val current = prefs[Keys.SAVED_DRAWINGS_TITLES] ?: emptySet()
            prefs[Keys.SAVED_DRAWINGS_TITLES] = current + title
            val existing = prefs[Keys.starKey(CategoryId.WRITING_DRAWING)] ?: 1
            if (existing < 2) {
                prefs[Keys.starKey(CategoryId.WRITING_DRAWING)] = 2
            }
            appendActivityLog(
                prefs = prefs,
                id = "drw_${System.currentTimeMillis()}",
                worldCategory = CategoryId.WRITING_DRAWING,
                titleAr = "حفظ لوحة فنية ($title)",
                titleEn = "Saved Artwork ($title)",
                starsEarned = 1
            )
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
            prefs[Keys.COMPLETED_NUMBERS_ACTIVITIES] = emptySet()
            prefs[Keys.COMPLETED_LETTERS_ACTIVITIES] = emptySet()
            prefs[Keys.LEARNED_ARABIC_LETTERS] = emptySet()
            prefs[Keys.LEARNED_ENGLISH_LETTERS] = emptySet()
            prefs[Keys.COMPLETED_COLORS_ACTIVITIES] = emptySet()
            prefs[Keys.COMPLETED_SHAPES_ACTIVITIES] = emptySet()
            prefs[Keys.COMPLETED_ANIMALS_ACTIVITIES] = emptySet()
            prefs[Keys.COMPLETED_BRAIN_ACTIVITIES] = emptySet()
            prefs[Keys.BRAIN_MAX_LEVEL] = 1
            prefs[Keys.COMPLETED_WRITING_ACTIVITIES] = emptySet()
            prefs[Keys.PRACTICED_WRITING_ITEMS] = emptySet()
            prefs[Keys.SAVED_DRAWINGS_TITLES] = emptySet()
            prefs[Keys.CELEBRATED_ACHIEVEMENTS] = emptySet()
            prefs[Keys.OPENED_SURPRISES] = emptySet()
            prefs[Keys.EQUIPPED_LULU_ACCESSORY] = ""
            prefs[Keys.SURPRISE_BONUS_STARS] = 0
            prefs[Keys.ACTIVITY_LOG_ENTRIES] = emptySet()
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
