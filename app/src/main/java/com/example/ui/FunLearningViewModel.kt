package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.UserPreferencesRepository
import com.example.data.UserSettingsState
import com.example.model.AchievementBadge
import com.example.model.AnimalsActivityId
import com.example.model.AppLanguage
import com.example.model.BrainGameActivityId
import com.example.model.CategoryId
import com.example.model.ColorsActivityId
import com.example.model.LearningCategoriesCatalog
import com.example.model.LearningCategory
import com.example.model.LettersActivityId
import com.example.model.LettersSectionMode
import com.example.model.NumbersActivityId
import com.example.model.RewardsProgressCalculator
import com.example.model.ShapesActivityId
import com.example.model.SurpriseRewardItem
import com.example.model.WritingDrawingActivityId
import com.example.util.LuluVoiceHelper
import com.example.util.SoundEffectsHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface AppDestination {
    data object Home : AppDestination
    data class CategoryScreen(val categoryId: CategoryId) : AppDestination
    data object RewardsWorld : AppDestination
    data object ParentDashboard : AppDestination
}

data class FunLearningUiState(
    val settings: UserSettingsState = UserSettingsState(),
    val destination: AppDestination = AppDestination.Home,
    val luluGreetingIndex: Int = 0,
    val showParentDialog: Boolean = false,
    val showJourneySheet: Boolean = false,
    val celebratingBadge: AchievementBadge? = null,
    val categories: List<LearningCategory> = LearningCategoriesCatalog.allCategories
) {
    val isArabic: Boolean
        get() = settings.language == AppLanguage.AR

    val currentLuluGreeting: String
        get() {
            val list = if (isArabic) {
                LearningCategoriesCatalog.luluGreetingsAr
            } else {
                LearningCategoriesCatalog.luluGreetingsEn
            }
            return list[luluGreetingIndex % list.size]
        }

    fun getCategory(id: CategoryId): LearningCategory =
        categories.firstOrNull { it.id == id } ?: categories.first()
}

class FunLearningViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = UserPreferencesRepository(application.applicationContext)
    private val voiceHelper = LuluVoiceHelper(application.applicationContext)

    private val _destination = MutableStateFlow<AppDestination>(AppDestination.Home)
    private val _luluGreetingIndex = MutableStateFlow(0)
    private val _showParentDialog = MutableStateFlow(false)
    private val _showJourneySheet = MutableStateFlow(false)
    private val _celebratingBadge = MutableStateFlow<AchievementBadge?>(null)

    val uiState: StateFlow<FunLearningUiState> = combine(
        repository.userSettingsFlow,
        _destination,
        _luluGreetingIndex,
        _showParentDialog,
        combine(_showJourneySheet, _celebratingBadge) { sheet, badge -> Pair(sheet, badge) }
    ) { settings, destination, greetingIdx, showParent, (showJourney, celebratingBadge) ->
        FunLearningUiState(
            settings = settings,
            destination = destination,
            luluGreetingIndex = greetingIdx,
            showParentDialog = showParent,
            showJourneySheet = showJourney,
            celebratingBadge = celebratingBadge
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = FunLearningUiState()
    )

    fun toggleLanguage() {
        val current = uiState.value.settings.language
        val next = if (current == AppLanguage.AR) AppLanguage.EN else AppLanguage.AR
        selectLanguage(next)
    }

    fun selectLanguage(language: AppLanguage) {
        if (uiState.value.settings.language == language) return
        SoundEffectsHelper.playPopChime(uiState.value.settings.soundEnabled)
        viewModelScope.launch {
            repository.setLanguage(language)
        }
    }

    fun toggleSound() {
        val nextEnabled = !uiState.value.settings.soundEnabled
        if (nextEnabled) {
            SoundEffectsHelper.playHappyMelody(true)
        } else {
            voiceHelper.stop()
        }
        viewModelScope.launch {
            repository.setSoundEnabled(nextEnabled)
        }
    }

    fun cycleLuluGreeting() {
        val nextIdx = (_luluGreetingIndex.value + 1) % 3
        _luluGreetingIndex.value = nextIdx
        val lang = uiState.value.settings.language
        val phrase = if (lang == AppLanguage.AR) {
            LearningCategoriesCatalog.luluGreetingsAr[nextIdx]
        } else {
            LearningCategoriesCatalog.luluGreetingsEn[nextIdx]
        }
        speakLulu(phrase, lang)
    }

    fun speakLulu(text: String, language: AppLanguage = uiState.value.settings.language) {
        voiceHelper.speak(
            text = text,
            language = language,
            soundEnabled = uiState.value.settings.soundEnabled
        )
    }

    fun replayLuluVoice() {
        voiceHelper.replayLast(soundEnabled = uiState.value.settings.soundEnabled)
    }

    fun openCategory(categoryId: CategoryId) {
        SoundEffectsHelper.playPopChime(uiState.value.settings.soundEnabled)
        _showJourneySheet.value = false
        _showParentDialog.value = false
        _destination.value = AppDestination.CategoryScreen(categoryId)
    }

    fun navigateHome() {
        voiceHelper.stop()
        SoundEffectsHelper.playPopChime(uiState.value.settings.soundEnabled)
        _destination.value = AppDestination.Home
    }

    fun openRewardsWorld() {
        SoundEffectsHelper.playHappyMelody(uiState.value.settings.soundEnabled)
        _showJourneySheet.value = false
        _destination.value = AppDestination.RewardsWorld
    }

    fun openParentSection() {
        SoundEffectsHelper.playPopChime(uiState.value.settings.soundEnabled)
        _showParentDialog.value = true
    }

    fun closeParentSection() {
        _showParentDialog.value = false
    }

    fun unlockParentDashboardWithPin(pin: String) {
        val clean = pin.filter { it.isDigit() }.take(4)
        viewModelScope.launch {
            if (uiState.value.settings.parentPin.isEmpty() && clean.length == 4) {
                repository.setParentPin(clean)
            }
            _showParentDialog.value = false
            SoundEffectsHelper.playPopChime(uiState.value.settings.soundEnabled)
            _destination.value = AppDestination.ParentDashboard
        }
    }

    fun openJourneyModal() {
        openRewardsWorld()
    }

    fun closeJourneyModal() {
        _showJourneySheet.value = false
    }

    fun dismissAchievementCelebration() {
        val current = _celebratingBadge.value
        _celebratingBadge.value = null
        if (current != null) {
            viewModelScope.launch {
                repository.markAchievementsCelebrated(listOf(current.id))
            }
        }
    }

    private suspend fun checkAndTriggerNewAchievementCelebration() {
        val latest = repository.userSettingsFlow.first()
        val unlocked = RewardsProgressCalculator.unlockedBadges(latest)
        val uncelebrated = unlocked.firstOrNull { it.id !in latest.celebratedAchievementIds }
        if (uncelebrated != null) {
            repository.markAchievementsCelebrated(listOf(uncelebrated.id))
            _celebratingBadge.value = uncelebrated
            SoundEffectsHelper.playHappyMelody(latest.soundEnabled)
            speakLulu(uncelebrated.luluCelebrate(latest.language), latest.language)
        }
    }

    fun updateChildName(name: String) {
        SoundEffectsHelper.playPopChime(uiState.value.settings.soundEnabled)
        viewModelScope.launch {
            repository.setChildName(name)
        }
    }

    fun updateParentPin(newPin: String) {
        SoundEffectsHelper.playPopChime(uiState.value.settings.soundEnabled)
        viewModelScope.launch {
            repository.setParentPin(newPin)
        }
    }

    fun openSurpriseBox(item: SurpriseRewardItem) {
        SoundEffectsHelper.playHappyMelody(uiState.value.settings.soundEnabled)
        viewModelScope.launch {
            val opened = repository.openSurpriseBox(item)
            if (opened) {
                val lang = uiState.value.settings.language
                val msg = if (lang == AppLanguage.AR) {
                    "مفاجأة رائعة! لقد ربحت ${item.titleAr} و${item.bonusStars} نجوم إضافية!"
                } else {
                    "Awesome surprise! You unlocked ${item.titleEn} and ${item.bonusStars} bonus stars!"
                }
                speakLulu(msg, lang)
                checkAndTriggerNewAchievementCelebration()
            }
        }
    }

    fun equipLuluAccessory(accessoryId: String) {
        SoundEffectsHelper.playPopChime(uiState.value.settings.soundEnabled)
        viewModelScope.launch {
            repository.equipLuluAccessory(accessoryId)
        }
    }

    fun collectPreviewStar(categoryId: CategoryId) {
        SoundEffectsHelper.playStarCollectChime(uiState.value.settings.soundEnabled)
        viewModelScope.launch {
            repository.incrementCategoryStar(categoryId)
            checkAndTriggerNewAchievementCelebration()
        }
    }

    fun completeNumbersActivity(activityId: NumbersActivityId) {
        SoundEffectsHelper.playStarCollectChime(uiState.value.settings.soundEnabled)
        viewModelScope.launch {
            repository.markNumbersActivityCompleted(activityId)
            checkAndTriggerNewAchievementCelebration()
        }
    }

    fun completeLettersActivity(activityId: LettersActivityId) {
        SoundEffectsHelper.playStarCollectChime(uiState.value.settings.soundEnabled)
        viewModelScope.launch {
            repository.markLettersActivityCompleted(activityId)
            checkAndTriggerNewAchievementCelebration()
        }
    }

    fun markLetterLearned(letterId: String, section: LettersSectionMode) {
        viewModelScope.launch {
            val newlyAdded = repository.markLetterLearned(letterId, section)
            if (newlyAdded) {
                SoundEffectsHelper.playStarCollectChime(uiState.value.settings.soundEnabled)
                checkAndTriggerNewAchievementCelebration()
            }
        }
    }

    fun completeColorsActivity(activityId: ColorsActivityId) {
        SoundEffectsHelper.playStarCollectChime(uiState.value.settings.soundEnabled)
        viewModelScope.launch {
            repository.markColorsActivityCompleted(activityId)
            checkAndTriggerNewAchievementCelebration()
        }
    }

    fun completeShapesActivity(activityId: ShapesActivityId) {
        SoundEffectsHelper.playStarCollectChime(uiState.value.settings.soundEnabled)
        viewModelScope.launch {
            repository.markShapesActivityCompleted(activityId)
            checkAndTriggerNewAchievementCelebration()
        }
    }

    fun completeAnimalsActivity(activityId: AnimalsActivityId) {
        SoundEffectsHelper.playStarCollectChime(uiState.value.settings.soundEnabled)
        viewModelScope.launch {
            repository.markAnimalsActivityCompleted(activityId)
            checkAndTriggerNewAchievementCelebration()
        }
    }

    fun completeBrainActivity(activityId: BrainGameActivityId, levelReached: Int = 1) {
        SoundEffectsHelper.playStarCollectChime(uiState.value.settings.soundEnabled)
        viewModelScope.launch {
            repository.markBrainActivityCompleted(activityId, levelReached)
            checkAndTriggerNewAchievementCelebration()
        }
    }

    fun completeWritingActivity(activityId: WritingDrawingActivityId) {
        SoundEffectsHelper.playStarCollectChime(uiState.value.settings.soundEnabled)
        viewModelScope.launch {
            repository.markWritingActivityCompleted(activityId)
            checkAndTriggerNewAchievementCelebration()
        }
    }

    fun markWritingItemPracticed(itemKey: String) {
        viewModelScope.launch {
            val newlyPracticed = repository.markWritingItemPracticed(itemKey)
            if (newlyPracticed) {
                SoundEffectsHelper.playStarCollectChime(uiState.value.settings.soundEnabled)
                checkAndTriggerNewAchievementCelebration()
            }
        }
    }

    fun saveChildDrawing(title: String) {
        SoundEffectsHelper.playStarCollectChime(uiState.value.settings.soundEnabled)
        viewModelScope.launch {
            repository.saveChildDrawingEntry(title)
            checkAndTriggerNewAchievementCelebration()
        }
    }

    fun setDailyGoalMinutes(minutes: Int) {
        SoundEffectsHelper.playPopChime(uiState.value.settings.soundEnabled)
        viewModelScope.launch {
            repository.setDailyGoalMinutes(minutes)
        }
    }

    fun resetProgress() {
        SoundEffectsHelper.playPopChime(uiState.value.settings.soundEnabled)
        viewModelScope.launch {
            repository.resetAllStars()
        }
    }

    fun restoreSampleStars() {
        SoundEffectsHelper.playStarCollectChime(uiState.value.settings.soundEnabled)
        viewModelScope.launch {
            repository.restoreStarterStars()
        }
    }

    override fun onCleared() {
        super.onCleared()
        voiceHelper.shutdown()
    }
}
