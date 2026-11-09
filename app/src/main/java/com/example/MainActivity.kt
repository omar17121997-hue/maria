package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.CategoryId
import com.example.model.LettersSectionMode
import com.example.ui.AppDestination
import com.example.ui.FunLearningUiState
import com.example.ui.FunLearningViewModel
import com.example.ui.screens.CategoryPlaceholderScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LearningJourneyModal
import com.example.ui.screens.ParentSectionDialog
import com.example.ui.screens.animals.AnimalsWorldScreen
import com.example.ui.screens.brain.BrainGamesWorldScreen
import com.example.ui.screens.colors.ColorsWorldScreen
import com.example.ui.screens.letters.LettersWorldScreen
import com.example.ui.screens.numbers.NumbersWorldScreen
import com.example.ui.screens.shapes.ShapesWorldScreen
import com.example.ui.screens.writing.WritingDrawingWorldScreen
import com.example.ui.theme.FunLearningWorldTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val appViewModel: FunLearningViewModel = viewModel()
            val uiState by appViewModel.uiState.collectAsStateWithLifecycle()

            FunLearningWorldApp(
                uiState = uiState,
                viewModel = appViewModel
            )
        }
    }
}

@Composable
fun FunLearningWorldApp(
    uiState: FunLearningUiState,
    viewModel: FunLearningViewModel,
    modifier: Modifier = Modifier
) {
    FunLearningWorldTheme(isArabic = uiState.isArabic) {
        AnimatedContent(
            targetState = uiState.destination,
            modifier = modifier.fillMaxSize(),
            transitionSpec = {
                (fadeIn(animationSpec = tween(260)) + scaleIn(initialScale = 0.96f, animationSpec = tween(260)))
                    .togetherWith(fadeOut(animationSpec = tween(200)))
            },
            label = "screen_navigation"
        ) { destination ->
            when (destination) {
                is AppDestination.Home -> {
                    HomeScreen(
                        uiState = uiState,
                        onSelectLanguage = viewModel::selectLanguage,
                        onToggleSound = viewModel::toggleSound,
                        onOpenParentSection = viewModel::openParentSection,
                        onTapLuluGreeting = viewModel::cycleLuluGreeting,
                        onSelectCategory = viewModel::openCategory,
                        onOpenJourneyModal = viewModel::openJourneyModal
                    )
                }

                is AppDestination.CategoryScreen -> {
                    when (destination.categoryId) {
                        CategoryId.NUMBERS -> {
                            NumbersWorldScreen(
                                uiState = uiState,
                                onNavigateHome = viewModel::navigateHome,
                                onSelectLanguage = viewModel::selectLanguage,
                                onToggleSound = viewModel::toggleSound,
                                onSpeakLulu = viewModel::speakLulu,
                                onCompleteActivity = viewModel::completeNumbersActivity
                            )
                        }

                        CategoryId.ARABIC_LETTERS,
                        CategoryId.ENGLISH_LETTERS -> {
                            val startSection = if (destination.categoryId == CategoryId.ARABIC_LETTERS) {
                                LettersSectionMode.ARABIC
                            } else {
                                LettersSectionMode.ENGLISH
                            }
                            LettersWorldScreen(
                                initialSection = startSection,
                                uiState = uiState,
                                onNavigateHome = viewModel::navigateHome,
                                onSelectLanguage = viewModel::selectLanguage,
                                onToggleSound = viewModel::toggleSound,
                                onSpeakLulu = viewModel::speakLulu,
                                onCompleteActivity = viewModel::completeLettersActivity,
                                onMarkLetterLearned = viewModel::markLetterLearned
                            )
                        }

                        CategoryId.COLORS -> {
                            ColorsWorldScreen(
                                uiState = uiState,
                                onNavigateHome = viewModel::navigateHome,
                                onSelectLanguage = viewModel::selectLanguage,
                                onToggleSound = viewModel::toggleSound,
                                onSpeakLulu = viewModel::speakLulu,
                                onCompleteActivity = viewModel::completeColorsActivity
                            )
                        }

                        CategoryId.SHAPES -> {
                            ShapesWorldScreen(
                                uiState = uiState,
                                onNavigateHome = viewModel::navigateHome,
                                onSelectLanguage = viewModel::selectLanguage,
                                onToggleSound = viewModel::toggleSound,
                                onSpeakLulu = viewModel::speakLulu,
                                onCompleteActivity = viewModel::completeShapesActivity
                            )
                        }

                        CategoryId.ANIMALS -> {
                            AnimalsWorldScreen(
                                uiState = uiState,
                                onNavigateHome = viewModel::navigateHome,
                                onSelectLanguage = viewModel::selectLanguage,
                                onToggleSound = viewModel::toggleSound,
                                onSpeakLulu = viewModel::speakLulu,
                                onCompleteActivity = viewModel::completeAnimalsActivity
                            )
                        }

                        CategoryId.BRAIN_GAMES -> {
                            BrainGamesWorldScreen(
                                uiState = uiState,
                                onNavigateHome = viewModel::navigateHome,
                                onSelectLanguage = viewModel::selectLanguage,
                                onToggleSound = viewModel::toggleSound,
                                onSpeakLulu = viewModel::speakLulu,
                                onCompleteActivity = viewModel::completeBrainActivity
                            )
                        }

                        CategoryId.WRITING_DRAWING -> {
                            WritingDrawingWorldScreen(
                                uiState = uiState,
                                onNavigateHome = viewModel::navigateHome,
                                onSelectLanguage = viewModel::selectLanguage,
                                onToggleSound = viewModel::toggleSound,
                                onSpeakLulu = viewModel::speakLulu,
                                onCompleteActivity = viewModel::completeWritingActivity,
                                onMarkItemPracticed = viewModel::markWritingItemPracticed,
                                onSaveDrawing = viewModel::saveChildDrawing
                            )
                        }
                    }
                }
            }
        }

        if (uiState.showParentDialog) {
            ParentSectionDialog(
                uiState = uiState,
                onDismiss = viewModel::closeParentSection,
                onSetDailyGoal = viewModel::setDailyGoalMinutes,
                onResetStars = viewModel::resetProgress,
                onRestoreSampleStars = viewModel::restoreSampleStars,
                onSelectLanguage = viewModel::selectLanguage,
                onToggleSound = viewModel::toggleSound
            )
        }

        if (uiState.showJourneySheet) {
            LearningJourneyModal(
                uiState = uiState,
                onDismiss = viewModel::closeJourneyModal,
                onSelectCategory = viewModel::openCategory
            )
        }
    }
}
