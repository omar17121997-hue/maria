package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.model.CategoryId
import com.example.ui.FunLearningUiState
import com.example.ui.components.BottomLearningJourneyBar
import com.example.ui.components.LearningCategoryCard
import com.example.ui.components.LuluMascotWelcomeCard
import com.example.ui.components.PlayfulSkyBackground
import com.example.ui.components.TopKidHeaderBar
import com.example.ui.theme.DeepNavyText
import com.example.ui.theme.MagicPurple
import com.example.ui.theme.SunnyYellow

@Composable
fun HomeScreen(
    uiState: FunLearningUiState,
    onSelectLanguage: (AppLanguage) -> Unit,
    onToggleSound: () -> Unit,
    onOpenParentSection: () -> Unit,
    onTapLuluGreeting: () -> Unit,
    onSelectCategory: (CategoryId) -> Unit,
    onOpenJourneyModal: () -> Unit,
    modifier: Modifier = Modifier
) {
    val language = uiState.settings.language
    val isArabic = uiState.isArabic

    PlayfulSkyBackground(modifier = modifier.fillMaxSize()) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
        ) {
            val screenWidth = maxWidth
            val columns = when {
                screenWidth >= 840.dp -> 4
                screenWidth >= 560.dp -> 2
                else -> 1
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .widthIn(max = 1100.dp)
                    .align(Alignment.TopCenter)
            ) {
                // Main scrollable content area with Top Header, Lulu Welcome, and Category Cards Grid
                LazyVerticalGrid(
                    columns = GridCells.Fixed(columns),
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .testTag("categories_grid"),
                    contentPadding = PaddingValues(
                        start = 16.dp,
                        end = 16.dp,
                        top = 12.dp,
                        bottom = 20.dp
                    ),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(18.dp)
                ) {
                    // 1. Top Header Bar (Full Span)
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        TopKidHeaderBar(
                            language = language,
                            soundEnabled = uiState.settings.soundEnabled,
                            onSelectLanguage = onSelectLanguage,
                            onToggleSound = onToggleSound,
                            onOpenParentSection = onOpenParentSection
                        )
                    }

                    // 2. Friendly Welcome Message from Lulu (Full Span)
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        LuluMascotWelcomeCard(
                            greetingText = uiState.currentLuluGreeting,
                            isArabic = isArabic,
                            onTapLulu = onTapLuluGreeting
                        )
                    }

                    // 3. Section Header Banner (Full Span)
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        SectionTitleBanner(isArabic = isArabic)
                    }

                    // 4. The 8 Learning Category Cards
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
                }

                // 5. Sticky Bottom Section: My Learning Journey + Stars Earned + Progress Indicator
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .windowInsetsPadding(WindowInsets.navigationBars)
                ) {
                    BottomLearningJourneyBar(
                        language = language,
                        earnedStars = uiState.settings.totalEarnedStars,
                        maxStars = uiState.settings.maxPossibleStars,
                        progressFraction = uiState.settings.progressFraction,
                        onOpenJourney = onOpenJourneyModal
                    )
                }
            }
        }
    }
}

@Composable
private fun SectionTitleBanner(
    isArabic: Boolean,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = Color.White.copy(alpha = 0.85f),
        border = BorderStroke(1.5.dp, SunnyYellow)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.AutoAwesome,
                    contentDescription = null,
                    tint = MagicPurple,
                    modifier = Modifier.size(22.dp)
                )
                Text(
                    text = if (isArabic) "أقسام التعلّم والمرح (٨ عوالم)" else "Learning & Fun Worlds (8 Categories)",
                    color = DeepNavyText,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                )
            }

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = SunnyYellow.copy(alpha = 0.35f)
            ) {
                Text(
                    text = if (isArabic) "اختر قسماً 🌟" else "Pick a World 🌟",
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    color = DeepNavyText,
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                )
            }
        }
    }
}
