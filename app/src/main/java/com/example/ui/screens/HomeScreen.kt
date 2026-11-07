package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.draw.shadow
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
import com.example.ui.theme.MagicPurpleDark
import com.example.ui.theme.StarGoldBorder
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
            val isTablet = maxWidth >= 600.dp
            val horizontalPad = if (isTablet) 24.dp else 14.dp
            val gridSpacing = if (isTablet) 20.dp else 14.dp

            val nextCategory = uiState.categories.firstOrNull {
                (uiState.settings.categoryStars[it.id] ?: 0) < 3
            }?.id ?: CategoryId.NUMBERS

            // Normal scrolling layout where the bottom Learning Journey section scrolls naturally
            // at the end of the grid and never covers any category cards or content.
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .widthIn(max = 980.dp)
                    .align(Alignment.TopCenter)
                    .testTag("categories_grid"),
                contentPadding = PaddingValues(
                    start = horizontalPad,
                    end = horizontalPad,
                    top = 12.dp,
                    bottom = 32.dp
                ),
                horizontalArrangement = Arrangement.spacedBy(gridSpacing),
                verticalArrangement = Arrangement.spacedBy(gridSpacing + 4.dp)
            ) {
                // 1. Top Header Bar (Full 2-Column Span)
                item(span = { GridItemSpan(maxLineSpan) }) {
                    TopKidHeaderBar(
                        language = language,
                        soundEnabled = uiState.settings.soundEnabled,
                        onSelectLanguage = onSelectLanguage,
                        onToggleSound = onToggleSound,
                        onOpenParentSection = onOpenParentSection
                    )
                }

                // 2. Engaging Lulu Welcome Hero Card + Prominent Start Learning Button (Full 2-Column Span)
                item(span = { GridItemSpan(maxLineSpan) }) {
                    LuluMascotWelcomeCard(
                        greetingText = uiState.currentLuluGreeting,
                        isArabic = isArabic,
                        onTapLulu = onTapLuluGreeting,
                        onStartLearning = { onSelectCategory(nextCategory) }
                    )
                }

                // 3. Section Header Banner (Full 2-Column Span)
                item(span = { GridItemSpan(maxLineSpan) }) {
                    SectionTitleBanner(isArabic = isArabic)
                }

                // 4. All 8 Educational Category Cards in a Beautiful 2-Column Grid
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

                // 5. Bottom Section in Normal Scroll Flow: My Learning Journey + Stars Earned + Progress Bar
                item(span = { GridItemSpan(maxLineSpan) }) {
                    BottomLearningJourneyBar(
                        language = language,
                        earnedStars = uiState.settings.totalEarnedStars,
                        maxStars = uiState.settings.maxPossibleStars,
                        progressFraction = uiState.settings.progressFraction,
                        onOpenJourney = onOpenJourneyModal,
                        modifier = Modifier.padding(top = 6.dp)
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
        modifier = modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(22.dp)),
        shape = RoundedCornerShape(22.dp),
        color = Color.White,
        border = BorderStroke(2.dp, StarGoldBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.Filled.AutoAwesome,
                    contentDescription = null,
                    tint = MagicPurpleDark,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = if (isArabic) "أقسام التعلّم الممتعة" else "Fun Learning Categories",
                    color = DeepNavyText,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = if (isArabic) 21.sp else 19.sp
                    )
                )
            }

            Surface(
                shape = RoundedCornerShape(14.dp),
                color = SunnyYellow,
                border = BorderStroke(1.5.dp, StarGoldBorder)
            ) {
                Text(
                    text = if (isArabic) "اختر قسماً 🌟" else "Pick a Category 🌟",
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp),
                    color = DeepNavyText,
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = if (isArabic) 15.sp else 14.sp
                    )
                )
            }
        }
    }
}
