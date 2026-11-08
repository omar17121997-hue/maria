package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.UserPreferencesRepository
import com.example.data.UserSettingsState
import com.example.model.AnimalsActivityId
import com.example.model.AppLanguage
import com.example.model.CategoryId
import com.example.model.ColorsActivityId
import com.example.model.LearningCategoriesCatalog
import com.example.model.LearningCategory
import com.example.model.LettersActivityId
import com.example.model.LettersSectionMode
import com.example.model.NumbersActivityId
import com.example.model.ShapesActivityId
import com.example.util.LuluVoiceHelper
import com.example.util.SoundEffectsHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface AppDestination {
    data object Home : AppDestination
    data class CategoryScreen(val categoryId: CategoryId) : AppDestination
}

data class FunLearningUiState(
    val settings: UserSettingsState = UserSettingsState(),
    val destination: AppDestination = AppDestination.Home,
    val luluGreetingIndex: Int = 0,
    val showParentDialog: Boolean = false,
    val showJourneySheet: Boolean = false,
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

    val uiState: StateFlow<FunLearningUiState> = combine(
        repository.userSettingsFlow,
        _destination,
        _luluGreetingIndex,
        _showParentDialog,
        _showJourneySheet
    ) { settings, destination, greetingIdx, showParent, showJourney ->
        FunLearningUiState(
            settings = settings,
            destination = destination,
            luluGreetingIndex = greetingIdx,
            showParentDialog = showParent,
            showJourneySheet = showJourney
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
        _destination.value = AppDestination.CategoryScreen(categoryId)
    }

    fun navigateHome() {
        voiceHelper.stop()
        SoundEffectsHelper.playPopChime(uiState.value.settings.soundEnabled)
        _destination.value = AppDestination.Home
    }

    fun openParentSection() {
        SoundEffectsHelper.playPopChime(uiState.value.settings.soundEnabled)
        _showParentDialog.value = true
    }

    fun closeParentSection() {
        _showParentDialog.value = false
    }

    fun openJourneyModal() {
        SoundEffectsHelper.playHappyMelody(uiState.value.settings.soundEnabled)
        _showJourneySheet.value = true
    }

    fun closeJourneyModal() {
        _showJourneySheet.value = false
    }

    fun collectPreviewStar(categoryId: CategoryId) {
        SoundEffectsHelper.playStarCollectChime(uiState.value.settings.soundEnabled)
        viewModelScope.launch {
            repository.incrementCategoryStar(categoryId)
        }
    }

    fun completeNumbersActivity(activityId: NumbersActivityId) {
        SoundEffectsHelper.playStarCollectChime(uiState.value.settings.soundEnabled)
        viewModelScope.launch {
            repository.markNumbersActivityCompleted(activityId)
        }
    }

    fun completeLettersActivity(activityId: LettersActivityId) {
        SoundEffectsHelper.playStarCollectChime(uiState.value.settings.soundEnabled)
        viewModelScope.launch {
            repository.markLettersActivityCompleted(activityId)
        }
    }

    fun markLetterLearned(letterId: String, section: LettersSectionMode) {
        viewModelScope.launch {
            val newlyAdded = repository.markLetterLearned(letterId, section)
            if (newlyAdded) {
                SoundEffectsHelper.playStarCollectChime(uiState.value.settings.soundEnabled)
            }
        }
    }

    fun completeColorsActivity(activityId: ColorsActivityId) {
        SoundEffectsHelper.playStarCollectChime(uiState.value.settings.soundEnabled)
        viewModelScope.launch {
            repository.markColorsActivityCompleted(activityId)
        }
    }

    fun completeShapesActivity(activityId: ShapesActivityId) {
        SoundEffectsHelper.playStarCollectChime(uiState.value.settings.soundEnabled)
        viewModelScope.launch {
            repository.markShapesActivityCompleted(activityId)
        }
    }

    fun completeAnimalsActivity(activityId: AnimalsActivityId) {
        SoundEffectsHelper.playStarCollectChime(uiState.value.settings.soundEnabled)
        viewModelScope.launch {
            repository.markAnimalsActivityCompleted(activityId)
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
