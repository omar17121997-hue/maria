package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeOff
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.AppLanguage
import com.example.model.LearningCategory
import com.example.model.PreviewActivityItem
import com.example.ui.components.Bouncy3DButton
import com.example.ui.components.Bouncy3DIconButton
import com.example.ui.components.LanguageSelectorSwitch
import com.example.ui.components.PlayfulSkyBackground
import com.example.ui.theme.DeepNavyText
import com.example.ui.theme.LuluPink
import com.example.ui.theme.LuluPinkDark
import com.example.ui.theme.MeadowGreen
import com.example.ui.theme.MeadowGreenDark
import com.example.ui.theme.SoftSlateText
import com.example.ui.theme.StarGold
import com.example.ui.theme.StarGoldBorder
import com.example.ui.theme.SunnyYellow
import com.example.ui.theme.SunnyYellowDark
import com.example.util.SoundEffectsHelper

/**
 * Dedicated Placeholder Screen for each Learning Category.
 * Features:
 * - Prominent Home Button to return to the main screen (+ BackHandler)
 * - Language switcher & Sound toggle
 * - Category 3D Hero Illustration + Title + Star Counter
 * - Lulu's custom guide speech bubble for this world
 * - 4 Interactive Stage-1 Preview Cards (tap to preview & hear chime)
 * - "Collect a Fun Star!" button so children can interact with their Learning Journey stars
 */
@Composable
fun CategoryPlaceholderScreen(
    category: LearningCategory,
    language: AppLanguage,
    soundEnabled: Boolean,
    earnedStars: Int,
    onNavigateHome: () -> Unit,
    onSelectLanguage: (AppLanguage) -> Unit,
    onToggleSound: () -> Unit,
    onCollectStar: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler(onBack = onNavigateHome)

    val isArabic = language == AppLanguage.AR
    var selectedPreviewItem by remember(category.id) {
        mutableStateOf<PreviewActivityItem?>(category.previewItems.firstOrNull())
    }

    PlayfulSkyBackground(
        modifier = modifier
            .fillMaxSize()
            .testTag("category_screen_${category.id.key}"),
        accentTint = category.accentColor
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .widthIn(max = 860.dp)
                .align(Alignment.TopCenter)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Top Navigation Bar with Prominent Home Button + Language & Sound Controls
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(8.dp, RoundedCornerShape(26.dp)),
                shape = RoundedCornerShape(26.dp),
                color = Color.White.copy(alpha = 0.95f),
                border = BorderStroke(2.dp, category.accentColor.copy(alpha = 0.5f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Large, Kid-Friendly Home Button
                    Bouncy3DButton(
                        text = if (isArabic) "الرئيسية" else "Home",
                        onClick = onNavigateHome,
                        containerColor = LuluPink,
                        shadowColor = LuluPinkDark,
                        contentColor = Color.White,
                        icon = Icons.Filled.Home,
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
                        testTagName = "return_home_button"
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        LanguageSelectorSwitch(
                            currentLanguage = language,
                            onSelectLanguage = onSelectLanguage
                        )

                        Bouncy3DIconButton(
                            icon = if (soundEnabled) {
                                Icons.AutoMirrored.Filled.VolumeUp
                            } else {
                                Icons.AutoMirrored.Filled.VolumeOff
                            },
                            contentDescription = if (soundEnabled) "Sound On" else "Sound Off",
                            onClick = onToggleSound,
                            containerColor = if (soundEnabled) MeadowGreen else Color(0xFFCFD8DC),
                            shadowColor = if (soundEnabled) MeadowGreenDark else Color(0xFF90A4AE),
                            iconTint = if (soundEnabled) Color.White else DeepNavyText,
                            testTagName = "category_sound_toggle"
                        )
                    }
                }
            }

            // 2. Hero Category Banner Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(10.dp, RoundedCornerShape(32.dp)),
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(containerColor = category.cardBgColor),
                border = BorderStroke(3.dp, Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Category Number + Stars Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(18.dp),
                            color = category.accentColor,
                            border = BorderStroke(1.5.dp, Color.White)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = category.icon,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(22.dp)
                                )
                                Text(
                                    text = "${category.titleAr} | ${category.titleEn}",
                                    color = Color.White,
                                    style = MaterialTheme.typography.labelLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )
                                )
                            }
                        }

                        // Star Rating Pill
                        Surface(
                            shape = RoundedCornerShape(18.dp),
                            color = Color.White,
                            border = BorderStroke(1.5.dp, StarGoldBorder)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                repeat(category.maxStars) { idx ->
                                    val earned = idx < earnedStars
                                    Icon(
                                        imageVector = if (earned) Icons.Filled.Star else Icons.Filled.StarBorder,
                                        contentDescription = null,
                                        tint = if (earned) StarGoldBorder else Color(0xFFBDBDBD),
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                            }
                        }
                    }

                    // 3D Illustration Banner
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(185.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .border(2.5.dp, Color.White, RoundedCornerShape(24.dp))
                    ) {
                        Image(
                            painter = painterResource(id = category.illustrationResId),
                            contentDescription = category.title(language),
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            Color.Transparent,
                                            category.accentColor.copy(alpha = 0.6f)
                                        )
                                    )
                                )
                        )
                        Row(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            category.floatingBadges.forEach { badge ->
                                Surface(
                                    shape = RoundedCornerShape(16.dp),
                                    color = Color.White.copy(alpha = 0.95f),
                                    border = BorderStroke(2.dp, category.accentColor),
                                    shadowElevation = 4.dp
                                ) {
                                    Text(
                                        text = badge,
                                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                                        color = category.shadowColor,
                                        style = MaterialTheme.typography.headlineMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 22.sp
                                        )
                                    )
                                }
                            }
                        }
                    }

                    // Large Category Title & Subtitle
                    Text(
                        text = category.title(language),
                        color = DeepNavyText,
                        style = MaterialTheme.typography.displayMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 30.sp
                        ),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = category.subtitle(language),
                        color = SoftSlateText,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Medium,
                            fontSize = 17.sp
                        ),
                        textAlign = TextAlign.Center
                    )
                }
            }

            // 3. Lulu Guide Speech Bubble for this Category
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(26.dp),
                color = Color.White.copy(alpha = 0.95f),
                border = BorderStroke(2.dp, SunnyYellow),
                shadowElevation = 6.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Surface(
                        modifier = Modifier.size(74.dp),
                        shape = CircleShape,
                        border = BorderStroke(3.dp, LuluPink),
                        shadowElevation = 4.dp
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img_lulu_mascot_1794072207514),
                            contentDescription = "Lulu",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = if (isArabic) "لولو تقول لك:" else "Lulu says:",
                            color = LuluPinkDark,
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        )
                        Text(
                            text = category.luluGuide(language),
                            color = DeepNavyText,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        )
                    }
                }
            }

            // 4. Interactive Preview Activities Grid (Stage 1 Preview Showcase)
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                color = Color.White.copy(alpha = 0.92f),
                border = BorderStroke(2.dp, category.accentColor.copy(alpha = 0.4f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.AutoAwesome,
                                contentDescription = null,
                                tint = category.accentColor,
                                modifier = Modifier.size(22.dp)
                            )
                            Text(
                                text = if (isArabic) "أنشطة القسم القادمة (اضغط للتجربة!)" else "Upcoming Activities (Tap to Preview!)",
                                color = DeepNavyText,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                            )
                        }
                    }

                    // 2x2 Grid of Preview Cards
                    val pairs = category.previewItems.chunked(2)
                    pairs.forEach { rowItems ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            rowItems.forEach { item ->
                                val isSelected = selectedPreviewItem == item
                                PreviewActivityTile(
                                    item = item,
                                    isArabic = isArabic,
                                    isSelected = isSelected,
                                    onClick = {
                                        SoundEffectsHelper.playPopChime(soundEnabled)
                                        selectedPreviewItem = item
                                    },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }

                    // Active Preview Spotlight Banner
                    AnimatedVisibility(
                        visible = selectedPreviewItem != null,
                        enter = fadeIn() + scaleIn(),
                        exit = fadeOut() + scaleOut()
                    ) {
                        selectedPreviewItem?.let { currentItem ->
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(20.dp),
                                color = category.cardBgColor,
                                border = BorderStroke(2.dp, currentItem.badgeColor)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(14.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Surface(
                                            shape = RoundedCornerShape(16.dp),
                                            color = currentItem.badgeColor
                                        ) {
                                            Text(
                                                text = currentItem.symbol,
                                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                                                color = Color.White,
                                                style = MaterialTheme.typography.headlineMedium.copy(
                                                    fontWeight = FontWeight.Bold
                                                )
                                            )
                                        }
                                        Column {
                                            Text(
                                                text = if (isArabic) currentItem.labelAr else currentItem.labelEn,
                                                color = DeepNavyText,
                                                style = MaterialTheme.typography.titleMedium.copy(
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 18.sp
                                                )
                                            )
                                            Text(
                                                text = if (isArabic) {
                                                    "قريباً في المرحلة الثانية من عالم المرح والتعلّم! 🎉"
                                                } else {
                                                    "Coming soon in Stage 2 of Fun Learning World! 🎉"
                                                },
                                                color = SoftSlateText,
                                                style = MaterialTheme.typography.bodyMedium
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Interactive Star Reward Button + Return Home Button
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Bouncy3DButton(
                            text = if (isArabic) "اجمع نجمة المرح! ⭐" else "Collect a Fun Star! ⭐",
                            onClick = onCollectStar,
                            modifier = Modifier.weight(1f),
                            containerColor = SunnyYellow,
                            shadowColor = SunnyYellowDark,
                            contentColor = DeepNavyText,
                            icon = Icons.Filled.Star,
                            testTagName = "collect_star_button"
                        )

                        Bouncy3DButton(
                            text = if (isArabic) "العودة للرئيسية" else "Back to Home",
                            onClick = onNavigateHome,
                            modifier = Modifier.weight(1f),
                            containerColor = category.accentColor,
                            shadowColor = category.shadowColor,
                            contentColor = Color.White,
                            icon = Icons.Filled.Home,
                            testTagName = "bottom_return_home_button"
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun PreviewActivityTile(
    item: PreviewActivityItem,
    isArabic: Boolean,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(22.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(22.dp),
        color = if (isSelected) item.badgeColor.copy(alpha = 0.16f) else Color(0xFFF7FAFC),
        border = BorderStroke(
            width = if (isSelected) 2.5.dp else 1.5.dp,
            color = if (isSelected) item.badgeColor else Color(0xFFE0E0E0)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = item.badgeColor,
                shadowElevation = 3.dp
            ) {
                Text(
                    text = item.symbol,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                    color = Color.White,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                )
            }
            Text(
                text = if (isArabic) item.labelAr else item.labelEn,
                color = DeepNavyText,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                ),
                textAlign = TextAlign.Center
            )
        }
    }
}
