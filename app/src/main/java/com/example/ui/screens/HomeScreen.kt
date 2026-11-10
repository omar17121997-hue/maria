package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.model.AppLanguage
import com.example.model.CategoryId
import com.example.ui.FunLearningUiState
import com.example.ui.components.ChildAccountSubView
import com.example.ui.components.HomeBottomNavTab
import com.example.ui.components.LearningCategoryCard
import com.example.ui.components.LessonsExplorerSubView
import com.example.ui.components.LibraryReviewSubView
import com.example.ui.components.LuluMoodExpression
import com.example.ui.components.LuluNotificationsDialog
import com.example.ui.components.PlayfulSkyBackground
import com.example.ui.components.QuickMenuDialog
import com.example.ui.components.Reference3DBottomNavigationBar
import com.example.ui.components.Reference3DChildProgressStrip
import com.example.ui.components.Reference3DHeroHeaderSection
import com.example.ui.components.Reference3DHeroesChallengeButton
import com.example.ui.components.Reference3DSectionPillHeader
import com.example.ui.components.Reference3DThreeMainActionCards

@Composable
fun HomeScreen(
    uiState: FunLearningUiState,
    onSelectLanguage: (AppLanguage) -> Unit,
    onToggleSound: () -> Unit,
    onOpenParentSection: () -> Unit,
    onTapLuluGreeting: () -> Unit,
    onSelectCategory: (CategoryId) -> Unit,
    onOpenJourneyModal: () -> Unit,
    onSpeakLulu: (String) -> Unit = { _ -> onTapLuluGreeting() },
    onUpdateChildName: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val language = uiState.settings.language
    val isArabic = uiState.isArabic

    var selectedBottomTab by rememberSaveable { mutableStateOf(HomeBottomNavTab.HOME) }
    var luluMoodIndex by rememberSaveable { mutableIntStateOf(0) }
    var showNotificationsDialog by remember { mutableStateOf(false) }
    var showQuickMenuDialog by remember { mutableStateOf(false) }

    val currentLuluMood = LuluMoodExpression.entries[luluMoodIndex % LuluMoodExpression.entries.size]

    if (selectedBottomTab != HomeBottomNavTab.HOME) {
        BackHandler {
            selectedBottomTab = HomeBottomNavTab.HOME
        }
    }

    PlayfulSkyBackground(modifier = modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            contentWindowInsets = WindowInsets.safeDrawing,
            containerColor = Color.Transparent,
            bottomBar = {
                Reference3DBottomNavigationBar(
                    selectedTab = selectedBottomTab,
                    language = language,
                    onSelectTab = { tab ->
                        if (tab == HomeBottomNavTab.ACHIEVEMENTS) {
                            onOpenJourneyModal()
                        } else {
                            selectedBottomTab = tab
                        }
                    }
                )
            }
        ) { innerPadding ->
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                val isTablet = maxWidth >= 640.dp
                val columnsCount = if (maxWidth >= 840.dp) 3 else 2
                val horizontalPad = if (isTablet) 22.dp else 12.dp
                val gridSpacing = if (isTablet) 16.dp else 11.dp

                val nextCategory = uiState.categories.firstOrNull {
                    (uiState.settings.categoryStars[it.id] ?: 0) < 3
                }?.id ?: CategoryId.NUMBERS

                LazyVerticalGrid(
                    columns = GridCells.Fixed(columnsCount),
                    modifier = Modifier
                        .fillMaxSize()
                        .widthIn(max = 980.dp)
                        .align(Alignment.TopCenter)
                        .testTag("categories_grid"),
                    contentPadding = PaddingValues(
                        start = horizontalPad,
                        end = horizontalPad,
                        top = 8.dp,
                        bottom = 24.dp
                    ),
                    horizontalArrangement = Arrangement.spacedBy(gridSpacing),
                    verticalArrangement = Arrangement.spacedBy(gridSpacing + 2.dp)
                ) {
                    // 1. 3D Top Bar + Hero Welcome Banner (with Boy, Girl, Lulu, Notifications, Menu)
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        Reference3DHeroHeaderSection(
                            uiState = uiState,
                            luluMood = currentLuluMood,
                            onCycleLuluMood = {
                                luluMoodIndex = (luluMoodIndex + 1) % LuluMoodExpression.entries.size
                                onTapLuluGreeting()
                            },
                            onOpenNotifications = { showNotificationsDialog = true },
                            onOpenQuickMenu = { showQuickMenuDialog = true },
                            onSelectLanguage = onSelectLanguage,
                            onToggleSound = onToggleSound,
                            onOpenParentSection = onOpenParentSection
                        )
                    }

                    when (selectedBottomTab) {
                        HomeBottomNavTab.HOME -> {
                            // 2. Three Large Glossy 3D Action Cards: التعلم | المراجعة | الألعاب
                            item(span = { GridItemSpan(maxLineSpan) }) {
                                Reference3DThreeMainActionCards(
                                    language = language,
                                    onClickLearn = { onSelectCategory(nextCategory) },
                                    onClickReview = { selectedBottomTab = HomeBottomNavTab.LIBRARY },
                                    onClickGames = { onSelectCategory(CategoryId.BRAIN_GAMES) }
                                )
                            }

                            // 3. Real-Time Child Progress Strip: مستواي | شريط الإنجاز | نجومي
                            item(span = { GridItemSpan(maxLineSpan) }) {
                                Reference3DChildProgressStrip(
                                    uiState = uiState,
                                    onOpenRewardsWorld = onOpenJourneyModal
                                )
                            }

                            // 4. Glossy Purple Section Pill ("العوالم والدروس 📖")
                            item(span = { GridItemSpan(maxLineSpan) }) {
                                Reference3DSectionPillHeader(isArabic = isArabic)
                            }

                            // 5. All 8 Educational Worlds in a 3D Cushion Card Grid
                            items(
                                items = uiState.categories,
                                key = { it.id.key }
                            ) { category ->
                                val earnedStars = uiState.settings.categoryStars[category.id] ?: 1
                                LearningCategoryCard(
                                    category = category,
                                    language = language,
                                    earnedStars = earnedStars,
                                    onClick = { onSelectCategory(category.id) }
                                )
                            }

                            // 6. Prominent Hero Challenge Button ("تحدي الأبطال! 🏆")
                            item(span = { GridItemSpan(maxLineSpan) }) {
                                Reference3DHeroesChallengeButton(
                                    isArabic = isArabic,
                                    onClick = { onSelectCategory(CategoryId.BRAIN_GAMES) },
                                    modifier = Modifier.padding(top = 6.dp)
                                )
                            }
                        }

                        HomeBottomNavTab.LESSONS -> {
                            item(span = { GridItemSpan(maxLineSpan) }) {
                                LessonsExplorerSubView(
                                    uiState = uiState,
                                    onSelectCategory = onSelectCategory,
                                    onSpeakLulu = onSpeakLulu
                                )
                            }
                        }

                        HomeBottomNavTab.LIBRARY -> {
                            item(span = { GridItemSpan(maxLineSpan) }) {
                                LibraryReviewSubView(
                                    uiState = uiState,
                                    onSpeakLulu = onSpeakLulu,
                                    onSelectCategory = onSelectCategory
                                )
                            }
                        }

                        HomeBottomNavTab.ACCOUNT,
                        HomeBottomNavTab.ACHIEVEMENTS -> {
                            item(span = { GridItemSpan(maxLineSpan) }) {
                                ChildAccountSubView(
                                    uiState = uiState,
                                    onUpdateChildName = onUpdateChildName,
                                    onSelectLanguage = onSelectLanguage,
                                    onToggleSound = onToggleSound,
                                    onOpenRewardsWorld = onOpenJourneyModal,
                                    onOpenParentSection = onOpenParentSection
                                )
                            }
                        }
                    }

                    item(span = { GridItemSpan(maxLineSpan) }) {
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }
        }

        if (showNotificationsDialog) {
            LuluNotificationsDialog(
                uiState = uiState,
                onDismiss = { showNotificationsDialog = false },
                onOpenRewardsWorld = onOpenJourneyModal
            )
        }

        if (showQuickMenuDialog) {
            QuickMenuDialog(
                uiState = uiState,
                onDismiss = { showQuickMenuDialog = false },
                onSelectLanguage = onSelectLanguage,
                onToggleSound = onToggleSound,
                onOpenRewardsWorld = onOpenJourneyModal,
                onOpenParentSection = onOpenParentSection
            )
        }
    }
}
