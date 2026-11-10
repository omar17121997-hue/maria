package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.filled.VolumeOff
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalLibrary
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.SupervisorAccount
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.R
import com.example.model.AnimalsCurriculum
import com.example.model.AppLanguage
import com.example.model.CategoryId
import com.example.model.ColorsCurriculum
import com.example.model.LettersCurriculum
import com.example.model.NumbersCurriculum
import com.example.model.RewardsProgressCalculator
import com.example.model.ShapesCurriculum
import com.example.model.SurpriseRewardItem
import com.example.ui.FunLearningUiState
import com.example.ui.theme.DeepNavyText
import com.example.ui.theme.MagicPurple
import com.example.ui.theme.MagicPurpleDark
import com.example.ui.theme.SkyCyanDark
import com.example.ui.theme.SoftSlateText
import com.example.ui.theme.StarGoldBorder
import com.example.ui.theme.SunnyYellow

enum class HomeBottomNavTab {
    LESSONS,
    LIBRARY,
    HOME,
    ACHIEVEMENTS,
    ACCOUNT
}

enum class LuluMoodExpression(
    val labelAr: String,
    val labelEn: String,
    val glowColor: Color,
    val phraseAr: String,
    val phraseEn: String
) {
    HAPPY(
        labelAr = "سعيدة ومرحّبة",
        labelEn = "Happy & Welcoming",
        glowColor = Color(0xFFFFCA28),
        phraseAr = "مرحبًا يا بطل! أنا صديقتك لولو، هيا نتعلم ونلعب معاً!",
        phraseEn = "Hello Hero! I am your friend Lulu, let's learn and play together!"
    ),
    ENCOURAGING(
        labelAr = "فخورة ومشجعة",
        labelEn = "Proud & Encouraging",
        glowColor = Color(0xFF26C6DA),
        phraseAr = "أنت ذكي ومبدع! كل نجمة تجمعها تقربك من لقب العبقري الصغير!",
        phraseEn = "You are smart and creative! Every star brings you closer to Little Genius!"
    ),
    CELEBRATING(
        labelAr = "تحتفل بإنجازك",
        labelEn = "Celebrating You",
        glowColor = Color(0xFFEC407A),
        phraseAr = "ما شاء الله يا بطل الأبطال! استمر في التعلّم واكتشاف العوالم!",
        phraseEn = "Amazing job Super Champion! Keep exploring all the learning worlds!"
    );

    fun label(lang: AppLanguage): String = if (lang == AppLanguage.AR) labelAr else labelEn
    fun phrase(lang: AppLanguage): String = if (lang == AppLanguage.AR) phraseAr else phraseEn
}

// ============================================================================
// 1. TOP BAR + 3D HERO WELCOME BANNER (MATCHING REFERENCE IMAGE)
// ============================================================================

@Composable
fun Reference3DHeroHeaderSection(
    uiState: FunLearningUiState,
    luluMood: LuluMoodExpression,
    onCycleLuluMood: () -> Unit,
    onOpenNotifications: () -> Unit,
    onOpenQuickMenu: () -> Unit,
    onSelectLanguage: (AppLanguage) -> Unit,
    onToggleSound: () -> Unit,
    onOpenParentSection: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isAr = uiState.isArabic
    val lang = uiState.settings.language
    val childName = uiState.settings.childName
    val unreadRewardsCount = RewardsProgressCalculator.availableSurpriseBoxesToOpen(uiState.settings).size +
        RewardsProgressCalculator.unlockedBadges(uiState.settings).size

    val infiniteTransition = rememberInfiniteTransition(label = "hero_3d_anim")
    val floatY by infiniteTransition.animateFloat(
        initialValue = -3f,
        targetValue = 4f,
        animationSpec = infiniteRepeatable(
            animation = tween(2100, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "hero_float"
    )

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .testTag("lulu_welcome_card")
            .shadow(14.dp, RoundedCornerShape(32.dp), spotColor = Color(0x550288D1)),
        shape = RoundedCornerShape(32.dp),
        color = Color(0xFF29B6F6),
        border = BorderStroke(3.dp, Color.White)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF039BE5),
                            Color(0xFF4FC3F7),
                            Color(0xFFB3E5FC)
                        )
                    )
                )
        ) {
            // Background 3D Boy & Girl Illustration Banner
            Image(
                painter = painterResource(id = R.drawable.img_hero_kids_3d_banner_1794241205574),
                contentDescription = if (isAr) "أطفال عالم المرح والتعلّم" else "Fun Learning World Kids",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(255.dp)
            )

            // Subtle gradient overlay at bottom so text and Lulu pop with crystal clarity
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(255.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0x2201579B),
                                Color.Transparent,
                                Color(0x991A237E)
                            )
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // ROW 1: Top Action Row (Notification Bell | Glossy "تطبيق" Pill | Menu Button)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // 3D Circular Notification Button
                    Box(contentAlignment = Alignment.TopEnd) {
                        GlossyCircularIconButton(
                            onClick = onOpenNotifications,
                            contentDescription = if (isAr) "الإشعارات" else "Notifications",
                            testTag = "top_notifications_button"
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Notifications,
                                contentDescription = null,
                                tint = Color(0xFF1565C0),
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        if (unreadRewardsCount > 0) {
                            Surface(
                                shape = CircleShape,
                                color = Color(0xFFE53935),
                                border = BorderStroke(1.5.dp, Color.White),
                                modifier = Modifier.offset(x = 2.dp, y = (-2).dp)
                            ) {
                                Text(
                                    text = "$unreadRewardsCount",
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp
                                    )
                                )
                            }
                        }
                    }

                    // Center Royal Blue 3D Glossy Capsule ("تطبيق / عالم المرح")
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = Color(0xFF0D47A1),
                        border = BorderStroke(2.5.dp, Color.White),
                        shadowElevation = 8.dp
                    ) {
                        Box(
                            modifier = Modifier
                                .background(
                                    Brush.verticalGradient(
                                        listOf(Color(0xFF1E88E5), Color(0xFF0D47A1))
                                    )
                                )
                                .padding(horizontal = 20.dp, vertical = 5.dp)
                        ) {
                            Text(
                                text = if (isAr) "✨ مرحبًا يا $childName! ✨" else "✨ Hello $childName! ✨",
                                color = Color.White,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = if (isAr) 16.sp else 15.sp
                                )
                            )
                        }
                    }

                    // 3D Circular Menu Button
                    GlossyCircularIconButton(
                        onClick = onOpenQuickMenu,
                        contentDescription = if (isAr) "القائمة" else "Menu",
                        testTag = "top_menu_button"
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = null,
                            tint = Color(0xFF1565C0),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                // ROW 2: Center 3D Purple-Magenta Glossy Title Plaque (like "السنة الثالثة ابتدائي")
                Surface(
                    modifier = Modifier
                        .offset(y = floatY.dp)
                        .width(240.dp)
                        .shadow(12.dp, RoundedCornerShape(26.dp), spotColor = Color(0x884A148C))
                        .clickable(onClick = onCycleLuluMood),
                    shape = RoundedCornerShape(26.dp),
                    color = Color(0xFF6A1B9A),
                    border = BorderStroke(2.5.dp, Color(0xFFF8BBD0))
                ) {
                    Column(
                        modifier = Modifier
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color(0xFFAB47BC),
                                        Color(0xFF7B1FA2),
                                        Color(0xFF4A148C)
                                    )
                                )
                            )
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Text(
                            text = if (isAr) "عالم المرح والتعلّم" else "Fun Learning World",
                            color = Color.White,
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = if (isAr) 22.sp else 19.sp
                            ),
                            textAlign = TextAlign.Center,
                            maxLines = 1
                        )
                        Text(
                            text = if (isAr) "هيا نتعلم ونلعب!" else "Let's Learn & Play!",
                            color = Color(0xFFFFD54F),
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = if (isAr) 20.sp else 17.sp
                            ),
                            textAlign = TextAlign.Center,
                            maxLines = 1
                        )
                    }
                }

                // ROW 3: Interactive 3D Lulu Mascot Strip + Quick Language/Sound/Parent Controls
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = onCycleLuluMood),
                    shape = RoundedCornerShape(24.dp),
                    color = Color.White.copy(alpha = 0.95f),
                    border = BorderStroke(2.dp, luluMood.glowColor),
                    shadowElevation = 6.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // 3D Lulu Portrait with Expression Ring
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(contentAlignment = Alignment.BottomEnd) {
                                Surface(
                                    modifier = Modifier.size(54.dp),
                                    shape = CircleShape,
                                    color = Color(0xFFE1F5FE),
                                    border = BorderStroke(3.dp, luluMood.glowColor),
                                    shadowElevation = 5.dp
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.img_lulu_mascot_1794072207514),
                                        contentDescription = if (isAr) "شخصية لولو" else "Lulu Mascot",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = luluMood.glowColor,
                                    border = BorderStroke(1.dp, Color.White)
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                                        contentDescription = null,
                                        tint = DeepNavyText,
                                        modifier = Modifier
                                            .padding(2.dp)
                                            .size(12.dp)
                                    )
                                }
                            }

                            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        text = if (isAr) "صديقتكم لولو" else "Lulu Mascot",
                                        color = Color(0xFFC2185B),
                                        style = MaterialTheme.typography.labelLarge.copy(
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 13.sp
                                        )
                                    )
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = luluMood.glowColor.copy(alpha = 0.25f)
                                    ) {
                                        Text(
                                            text = luluMood.label(lang),
                                            color = DeepNavyText,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 1.dp),
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 10.sp
                                            )
                                        )
                                    }
                                }
                                Text(
                                    text = uiState.currentLuluGreeting,
                                    color = DeepNavyText,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = if (isAr) 13.sp else 12.sp,
                                        lineHeight = 17.sp
                                    ),
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(6.dp))

                        // Compact Language, Sound, and Parent Buttons (preserving testTags!)
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            LanguageSelectorSwitch(
                                currentLanguage = lang,
                                onSelectLanguage = onSelectLanguage
                            )

                            Bouncy3DIconButton(
                                icon = if (uiState.settings.soundEnabled) {
                                    Icons.AutoMirrored.Filled.VolumeUp
                                } else {
                                    Icons.AutoMirrored.Filled.VolumeOff
                                },
                                contentDescription = if (uiState.settings.soundEnabled) "Sound On" else "Sound Off",
                                onClick = onToggleSound,
                                containerColor = if (uiState.settings.soundEnabled) Color(0xFF2E7D32) else Color(0xFFCFD8DC),
                                shadowColor = if (uiState.settings.soundEnabled) Color(0xFF1B5E20) else Color(0xFF78909C),
                                iconTint = if (uiState.settings.soundEnabled) Color.White else DeepNavyText,
                                testTagName = "sound_toggle_button"
                            )

                            Bouncy3DIconButton(
                                icon = Icons.Filled.SupervisorAccount,
                                contentDescription = if (isAr) "لوحة ولي الأمر" else "Parent Dashboard",
                                onClick = onOpenParentSection,
                                containerColor = MagicPurple,
                                shadowColor = MagicPurpleDark,
                                iconTint = Color.White,
                                testTagName = "parent_section_button"
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun GlossyCircularIconButton(
    onClick: () -> Unit,
    contentDescription: String,
    testTag: String,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = Modifier
            .size(46.dp)
            .minimumInteractiveComponentSize()
            .testTag(testTag)
            .shadow(6.dp, CircleShape)
            .clickable(onClick = onClick),
        shape = CircleShape,
        color = Color.White,
        border = BorderStroke(2.dp, Color(0xFFE1F5FE))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(Color.White, Color(0xFFE3F2FD))
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            content()
        }
    }
}

// ============================================================================
// 2. THREE LARGE 3D GLOSSY ACTION CARDS: التعلم | المراجعة | الألعاب
// ============================================================================

@Composable
fun Reference3DThreeMainActionCards(
    language: AppLanguage,
    onClickLearn: () -> Unit,
    onClickReview: () -> Unit,
    onClickGames: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isAr = language == AppLanguage.AR

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 1. التعلم (Learn) - Glossy Vibrant Orange 3D Card with 3D Books Stack
        Glossy3DActionFeatureCard(
            title = if (isAr) "التعلم" else "Learn",
            topColor = Color(0xFFFFB74D),
            midColor = Color(0xFFFF9800),
            bottomColor = Color(0xFFE65100),
            lipColor = Color(0xFFBF360C),
            onClick = onClickLearn,
            testTag = "main_start_learning_button",
            modifier = Modifier.weight(1f)
        ) {
            Canvas3DStackOfBooks(modifier = Modifier.size(56.dp))
        }

        // 2. المراجعة (Review) - Glossy Teal/Cyan 3D Card with 3D Book & Magnifying Glass
        Glossy3DActionFeatureCard(
            title = if (isAr) "المراجعة" else "Review",
            topColor = Color(0xFF4DD0E1),
            midColor = Color(0xFF00ACC1),
            bottomColor = Color(0xFF006064),
            lipColor = Color(0xFF004D40),
            onClick = onClickReview,
            testTag = "main_review_card_button",
            modifier = Modifier.weight(1f)
        ) {
            Canvas3DBookWithMagnifier(modifier = Modifier.size(56.dp))
        }

        // 3. الألعاب (Games / Competition) - Glossy Purple-Magenta 3D Card with 3D Golden Trophy
        Glossy3DActionFeatureCard(
            title = if (isAr) "الألعاب" else "Games",
            topColor = Color(0xFFBA68C8),
            midColor = Color(0xFF8E24AA),
            bottomColor = Color(0xFF4A148C),
            lipColor = Color(0xFF311B92),
            onClick = onClickGames,
            testTag = "main_games_card_button",
            modifier = Modifier.weight(1f)
        ) {
            Surface(
                modifier = Modifier.size(54.dp),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.5.dp, Color(0xFFFFD54F)),
                shadowElevation = 4.dp
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_golden_trophy_3d_1794241206008),
                    contentDescription = if (isAr) "الألعاب" else "Games",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
private fun Glossy3DActionFeatureCard(
    title: String,
    topColor: Color,
    midColor: Color,
    bottomColor: Color,
    lipColor: Color,
    onClick: () -> Unit,
    testTag: String,
    modifier: Modifier = Modifier,
    illustration: @Composable () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(stiffness = 600f),
        label = "action_card_scale"
    )
    val lipHeight = 6.dp
    val pressOffset by animateDpAsState(
        targetValue = if (isPressed) 4.dp else 0.dp,
        animationSpec = spring(stiffness = 700f),
        label = "action_card_offset"
    )
    val shape = RoundedCornerShape(24.dp)

    Box(
        modifier = modifier
            .scale(scale)
            .testTag(testTag)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.TopCenter
    ) {
        // 3D Bottom Lip Shadow
        Box(
            modifier = Modifier
                .matchParentSize()
                .padding(top = lipHeight)
                .background(lipColor, shape)
        )

        // Glossy 3D Surface
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = lipHeight)
                .offset(y = pressOffset),
            shape = shape,
            color = midColor,
            border = BorderStroke(2.5.dp, Color.White),
            shadowElevation = 8.dp
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(topColor, midColor, bottomColor)
                        )
                    )
            ) {
                // Top Specular Gloss Highlight Pill
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                        .height(16.dp)
                        .clip(RoundedCornerShape(50))
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    Color.White.copy(alpha = 0.45f),
                                    Color.Transparent
                                )
                            )
                        )
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    illustration()

                    Text(
                        text = title,
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 18.sp
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

// ============================================================================
// 3. REAL-TIME CHILD PROGRESS STRIP (مستواي | شريط الإنجاز | نجومي)
// ============================================================================

@Composable
fun Reference3DChildProgressStrip(
    uiState: FunLearningUiState,
    onOpenRewardsWorld: () -> Unit,
    modifier: Modifier = Modifier
) {
    val settings = uiState.settings
    val lang = settings.language
    val isAr = lang == AppLanguage.AR

    val currentLevel = RewardsProgressCalculator.computeCurrentLevel(settings)
    val totalStars = settings.totalEarnedStars
    val progressPercent = (settings.progressFraction * 100).toInt().coerceIn(5, 100)
    val animatedFraction by animateFloatAsState(
        targetValue = settings.progressFraction.coerceIn(0.08f, 1f),
        animationSpec = tween(700, easing = FastOutSlowInEasing),
        label = "home_progress_strip"
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .testTag("bottom_learning_journey_bar")
            .clickable(onClick = onOpenRewardsWorld),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 1. مستواي (My Level Card)
        Surface(
            modifier = Modifier
                .weight(0.95f)
                .height(82.dp)
                .shadow(6.dp, RoundedCornerShape(22.dp), spotColor = Color(0x330288D1)),
            shape = RoundedCornerShape(22.dp),
            color = Color(0xFFE8F4FE),
            border = BorderStroke(2.dp, Color.White)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(Color.White, Color(0xFFD9ECFF))
                        )
                    )
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Canvas3DGlossyStar(modifier = Modifier.size(32.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = if (isAr) "مستواي" else "Level",
                        color = SoftSlateText,
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    )
                    Text(
                        text = if (isAr) {
                            currentLevel.titleAr.split(" ").lastOrNull() ?: currentLevel.titleAr
                        } else {
                            "Lv.${currentLevel.levelNumber}"
                        },
                        color = DeepNavyText,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 15.sp
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }

        // 2. شريط الإنجاز (Center Progress Bar Card)
        Surface(
            modifier = Modifier
                .weight(1.35f)
                .height(82.dp)
                .shadow(6.dp, RoundedCornerShape(22.dp), spotColor = Color(0x330288D1)),
            shape = RoundedCornerShape(22.dp),
            color = Color(0xFFE8F4FE),
            border = BorderStroke(2.dp, Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(Color.White, Color(0xFFD9ECFF))
                        )
                    )
                    .padding(horizontal = 10.dp, vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = if (isAr) "شريط الإنجاز" else "Progress Bar",
                    color = DeepNavyText,
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 14.sp
                    ),
                    maxLines = 1
                )

                // Glossy 3D Capsule Progress Track with Green Fill + %
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(30.dp),
                    shape = RoundedCornerShape(50),
                    color = Color(0xFFCFD8DC),
                    border = BorderStroke(1.5.dp, Color.White)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(20.dp)
                                .clip(RoundedCornerShape(50))
                                .background(Color(0xFFB0BEC5))
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(animatedFraction)
                                    .fillMaxHeight()
                                    .clip(RoundedCornerShape(50))
                                    .background(
                                        Brush.verticalGradient(
                                            listOf(
                                                Color(0xFF81C784),
                                                Color(0xFF43A047),
                                                Color(0xFF2E7D32)
                                            )
                                        )
                                    )
                            )
                        }

                        Spacer(modifier = Modifier.width(6.dp))

                        Text(
                            text = "%$progressPercent",
                            color = DeepNavyText,
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 13.sp
                            )
                        )
                    }
                }
            }
        }

        // 3. نقاطي / نجومي (My Stars Card)
        Surface(
            modifier = Modifier
                .weight(0.95f)
                .height(82.dp)
                .shadow(6.dp, RoundedCornerShape(22.dp), spotColor = Color(0x330288D1)),
            shape = RoundedCornerShape(22.dp),
            color = Color(0xFFE8F4FE),
            border = BorderStroke(2.dp, Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(Color.White, Color(0xFFD9ECFF))
                        )
                    )
                    .padding(horizontal = 6.dp, vertical = 6.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // 3 Mini 3D Stars at Top
                Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                    repeat(3) {
                        Canvas3DGlossyStar(modifier = Modifier.size(16.dp))
                    }
                }

                Text(
                    text = if (isAr) "نجومي" else "My Stars",
                    color = SoftSlateText,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Text(
                        text = "$totalStars",
                        color = DeepNavyText,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 16.sp
                        )
                    )
                    Canvas3DGlossyStar(modifier = Modifier.size(16.dp))
                }
            }
        }
    }
}

// ============================================================================
// 4. GLOSSY PURPLE SECTION PILL ("العوالم والدروس 📖")
// ============================================================================

@Composable
fun Reference3DSectionPillHeader(
    isArabic: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier.shadow(8.dp, RoundedCornerShape(50), spotColor = Color(0x664A148C)),
            shape = RoundedCornerShape(50),
            color = Color(0xFF7B1FA2),
            border = BorderStroke(2.dp, Color.White)
        ) {
            Row(
                modifier = Modifier
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                Color(0xFF9C27B0),
                                Color(0xFF7B1FA2),
                                Color(0xFF6A1B9A)
                            )
                        )
                    )
                    .padding(horizontal = 24.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.MenuBook,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = if (isArabic) "العوالم والدروس" else "Worlds & Lessons",
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = if (isArabic) 18.sp else 16.sp
                    )
                )
            }
        }
    }
}

// ============================================================================
// 5. PROMINENT HERO CHALLENGE BUTTON ("تحدي الأبطال! 🏆")
// ============================================================================

@Composable
fun Reference3DHeroesChallengeButton(
    isArabic: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.96f else 1f,
        animationSpec = spring(stiffness = 600f),
        label = "challenge_btn_scale"
    )
    val lipHeight = 7.dp
    val pressOffset by animateDpAsState(
        targetValue = if (isPressed) 5.dp else 0.dp,
        animationSpec = spring(stiffness = 700f),
        label = "challenge_btn_offset"
    )
    val shape = RoundedCornerShape(50)

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        // Left Sparkle Rays
        CanvasSparkleRays(isLeft = true, modifier = Modifier.size(28.dp))

        Spacer(modifier = Modifier.width(6.dp))

        Box(
            modifier = Modifier
                .weight(1f)
                .scale(scale)
                .testTag("heroes_challenge_button")
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = onClick
                ),
            contentAlignment = Alignment.TopCenter
        ) {
            // 3D Bottom Lip
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .padding(top = lipHeight)
                    .background(Color(0xFF4A148C), shape)
            )

            // Main Glossy Purple-Magenta Capsule
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = lipHeight)
                    .offset(y = pressOffset),
                shape = shape,
                color = Color(0xFF8E24AA),
                border = BorderStroke(3.dp, Color.White),
                shadowElevation = 10.dp
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xFF7B1FA2),
                                    Color(0xFFAB47BC),
                                    Color(0xFFD81B60)
                                )
                            )
                        )
                ) {
                    // Top Gloss Highlight
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 4.dp)
                            .height(16.dp)
                            .clip(RoundedCornerShape(50))
                            .background(
                                Brush.verticalGradient(
                                    listOf(
                                        Color.White.copy(alpha = 0.4f),
                                        Color.Transparent
                                    )
                                )
                            )
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 18.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // 3D Golden Trophy Icon
                        Surface(
                            modifier = Modifier.size(50.dp),
                            shape = CircleShape,
                            border = BorderStroke(2.dp, Color(0xFFFFD54F)),
                            shadowElevation = 5.dp
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.img_golden_trophy_3d_1794241206008),
                                contentDescription = if (isArabic) "كأس التحدي" else "Challenge Trophy",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        Text(
                            text = if (isArabic) "تحدي الأبطال!" else "Heroes Challenge!",
                            color = Color.White,
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = if (isArabic) 23.sp else 20.sp
                            ),
                            textAlign = TextAlign.Center
                        )

                        Surface(
                            shape = CircleShape,
                            color = Color.White.copy(alpha = 0.25f),
                            modifier = Modifier.size(38.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Filled.PlayArrow,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(26.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.width(6.dp))

        // Right Sparkle Rays
        CanvasSparkleRays(isLeft = false, modifier = Modifier.size(28.dp))
    }
}

// ============================================================================
// 6. FIXED 3D BOTTOM NAVIGATION BAR (الدروس | مكتبتي | الرئيسية | إنجازاتي | حسابي)
// ============================================================================

@Composable
fun Reference3DBottomNavigationBar(
    selectedTab: HomeBottomNavTab,
    language: AppLanguage,
    onSelectTab: (HomeBottomNavTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val isAr = language == AppLanguage.AR

    Box(
        modifier = modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.navigationBars)
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .testTag("bottom_navigation_bar"),
        contentAlignment = Alignment.BottomCenter
    ) {
        // White/Sky Glossy 3D Dock Background
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(74.dp)
                .shadow(14.dp, RoundedCornerShape(30.dp), spotColor = Color(0x440D47A1)),
            shape = RoundedCornerShape(30.dp),
            color = Color.White,
            border = BorderStroke(2.dp, Color(0xFFE1F5FE))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(Color.White, Color(0xFFEAF4FF))
                        )
                    )
                    .padding(horizontal = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // 1. الدروس (Lessons)
                BottomNav3DItem(
                    label = if (isAr) "الدروس" else "Lessons",
                    selected = selectedTab == HomeBottomNavTab.LESSONS,
                    activeColor = Color(0xFF1565C0),
                    testTag = "bottom_nav_lessons",
                    onClick = { onSelectTab(HomeBottomNavTab.LESSONS) }
                ) {
                    Icon(
                        imageVector = Icons.Filled.GridView,
                        contentDescription = null,
                        tint = if (selectedTab == HomeBottomNavTab.LESSONS) Color(0xFF1565C0) else Color(0xFF546E7A),
                        modifier = Modifier.size(24.dp)
                    )
                }

                // 2. مكتبتي (My Library)
                BottomNav3DItem(
                    label = if (isAr) "مكتبتي" else "Library",
                    selected = selectedTab == HomeBottomNavTab.LIBRARY,
                    activeColor = Color(0xFF00838F),
                    testTag = "bottom_nav_library",
                    onClick = { onSelectTab(HomeBottomNavTab.LIBRARY) }
                ) {
                    Canvas3DMiniBooksIcon(modifier = Modifier.size(26.dp))
                }

                // Spacer for Center Raised Home Circle
                Spacer(modifier = Modifier.width(68.dp))

                // 4. إنجازاتي (Achievements)
                BottomNav3DItem(
                    label = if (isAr) "إنجازاتي" else "Rewards",
                    selected = selectedTab == HomeBottomNavTab.ACHIEVEMENTS,
                    activeColor = Color(0xFFF57C00),
                    testTag = "bottom_nav_achievements",
                    onClick = { onSelectTab(HomeBottomNavTab.ACHIEVEMENTS) }
                ) {
                    Canvas3DGlossyStar(modifier = Modifier.size(26.dp))
                }

                // 5. حسابي (My Account)
                BottomNav3DItem(
                    label = if (isAr) "حسابي" else "Account",
                    selected = selectedTab == HomeBottomNavTab.ACCOUNT,
                    activeColor = Color(0xFF6A1B9A),
                    testTag = "bottom_nav_account",
                    onClick = { onSelectTab(HomeBottomNavTab.ACCOUNT) }
                ) {
                    Icon(
                        imageVector = Icons.Filled.AccountCircle,
                        contentDescription = null,
                        tint = if (selectedTab == HomeBottomNavTab.ACCOUNT) Color(0xFF6A1B9A) else Color(0xFF1976D2),
                        modifier = Modifier.size(26.dp)
                    )
                }
            }
        }

        // 3. Center Raised Glowing Blue "الرئيسية (Home)" Circle Button
        Surface(
            modifier = Modifier
                .offset(y = (-8).dp)
                .size(76.dp)
                .testTag("bottom_nav_home")
                .shadow(12.dp, CircleShape, spotColor = Color(0x880288D1))
                .clickable { onSelectTab(HomeBottomNavTab.HOME) },
            shape = CircleShape,
            color = Color(0xFF0288D1),
            border = BorderStroke(3.5.dp, Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = if (selectedTab == HomeBottomNavTab.HOME) {
                                listOf(Color(0xFF4FC3F7), Color(0xFF0288D1), Color(0xFF01579B))
                            } else {
                                listOf(Color(0xFF29B6F6), Color(0xFF0277BD))
                            }
                        )
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Home,
                    contentDescription = if (isAr) "الرئيسية" else "Home",
                    tint = Color.White,
                    modifier = Modifier.size(30.dp)
                )
                Text(
                    text = if (isAr) "الرئيسية" else "Home",
                    color = Color.White,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 11.sp
                    )
                )
            }
        }
    }
}

@Composable
private fun BottomNav3DItem(
    label: String,
    selected: Boolean,
    activeColor: Color,
    testTag: String,
    onClick: () -> Unit,
    iconContent: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .minimumInteractiveComponentSize()
            .testTag(testTag)
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        iconContent()
        Text(
            text = label,
            color = if (selected) activeColor else DeepNavyText,
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = if (selected) FontWeight.ExtraBold else FontWeight.Bold,
                fontSize = 12.sp
            ),
            maxLines = 1
        )
    }
}

// ============================================================================
// 7. SUB-VIEWS FOR BOTTOM NAV TABS (الدروس | مكتبتي | حسابي) & POPUPS
// ============================================================================

@Composable
fun LessonsExplorerSubView(
    uiState: FunLearningUiState,
    onSelectCategory: (CategoryId) -> Unit,
    onSpeakLulu: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val lang = uiState.settings.language
    val isAr = uiState.isArabic

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(26.dp),
            color = Color.White,
            border = BorderStroke(2.5.dp, Color(0xFF4FC3F7)),
            shadowElevation = 6.dp
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = if (isAr) "📚 جميع الدروس والأنشطة التعليمية" else "📚 All Educational Lessons & Activities",
                    color = DeepNavyText,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold)
                )
                Text(
                    text = if (isAr) "اختر أي عالم للاستماع لشرح لولو أو بدء الدروس التفاعلية مباشرة:" else "Pick any world to hear Lulu's guide or launch interactive lessons:",
                    color = SoftSlateText,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
        }

        uiState.categories.forEach { category ->
            val stars = uiState.settings.categoryStars[category.id] ?: 1
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(6.dp, RoundedCornerShape(24.dp))
                    .clickable { onSelectCategory(category.id) },
                shape = RoundedCornerShape(24.dp),
                color = category.cardBgColor,
                border = BorderStroke(2.5.dp, Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Surface(
                            modifier = Modifier.size(74.dp),
                            shape = RoundedCornerShape(18.dp),
                            border = BorderStroke(2.dp, Color.White),
                            shadowElevation = 4.dp
                        ) {
                            Image(
                                painter = painterResource(id = category.illustrationResId),
                                contentDescription = category.title(lang),
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = category.title(lang),
                                color = DeepNavyText,
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 19.sp
                                )
                            )
                            Text(
                                text = category.luluGuide(lang),
                                color = SoftSlateText,
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                maxLines = 2
                            )
                            Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                                repeat(3) { idx ->
                                    Icon(
                                        imageVector = Icons.Filled.Star,
                                        contentDescription = null,
                                        tint = if (idx < stars) StarGoldBorder else Color(0xFFB0BEC5),
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }

                        Bouncy3DButton(
                            text = category.buttonLabel(lang),
                            onClick = { onSelectCategory(category.id) },
                            containerColor = category.accentColor,
                            shadowColor = category.shadowColor,
                            fontSize = 14.sp,
                            cornerRadius = 18.dp,
                            lipHeight = 4.dp
                        )
                    }

                    // Preview Topic Pills
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(category.previewItems) { item ->
                            Surface(
                                modifier = Modifier.clickable {
                                    onSpeakLulu(if (isAr) item.labelAr else item.labelEn)
                                },
                                shape = RoundedCornerShape(14.dp),
                                color = Color.White,
                                border = BorderStroke(1.5.dp, item.badgeColor)
                            ) {
                                Text(
                                    text = if (isAr) item.labelAr else item.labelEn,
                                    color = DeepNavyText,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LibraryReviewSubView(
    uiState: FunLearningUiState,
    onSpeakLulu: (String) -> Unit,
    onSelectCategory: (CategoryId) -> Unit,
    modifier: Modifier = Modifier
) {
    val isAr = uiState.isArabic
    val lang = uiState.settings.language
    var selectedSection by remember { mutableIntStateOf(0) }
    val sectionTitlesAr = listOf("الأرقام (١-١٠)", "الحروف العربية", "الحروف الإنجليزية", "الألوان والأشكال", "الحيوانات")
    val sectionTitlesEn = listOf("Numbers (1-10)", "Arabic Letters", "English Letters", "Colors & Shapes", "Animals")

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(26.dp),
            color = Color.White,
            border = BorderStroke(2.5.dp, Color(0xFF00ACC1)),
            shadowElevation = 6.dp
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = if (isAr) "📖 مكتبتي والمراجعة السريعة بالصوت" else "📖 My Audio Flashcard Library & Review",
                    color = DeepNavyText,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold)
                )
                Text(
                    text = if (isAr) "اضغط على أي بطاقة لسماع نطقها الصحيح مع لولو ومراجعتها بسرعة!" else "Tap any card to hear Lulu pronounce it clearly for quick review!",
                    color = SoftSlateText,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                )

                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(5) { idx ->
                        val selected = selectedSection == idx
                        Surface(
                            modifier = Modifier.clickable { selectedSection = idx },
                            shape = RoundedCornerShape(16.dp),
                            color = if (selected) Color(0xFF00838F) else Color(0xFFE0F7FA),
                            border = BorderStroke(1.5.dp, Color(0xFF00838F))
                        ) {
                            Text(
                                text = if (isAr) sectionTitlesAr[idx] else sectionTitlesEn[idx],
                                color = if (selected) Color.White else DeepNavyText,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                }
            }
        }

        when (selectedSection) {
            0 -> {
                NumbersCurriculum.lessons.chunked(2).forEach { pair ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        pair.forEach { num ->
                            Surface(
                                modifier = Modifier
                                    .weight(1f)
                                    .shadow(4.dp, RoundedCornerShape(22.dp))
                                    .clickable {
                                        onSpeakLulu(if (isAr) "${num.symbolAr} ، ${num.wordAr}" else "${num.symbolEn}, ${num.wordEn}")
                                    },
                                shape = RoundedCornerShape(22.dp),
                                color = Color.White,
                                border = BorderStroke(2.dp, num.color)
                            ) {
                                Column(
                                    modifier = Modifier.padding(14.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = if (isAr) num.symbolAr else num.symbolEn,
                                        color = num.color,
                                        fontSize = 34.sp,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                    Text(
                                        text = "${num.wordAr} • ${num.wordEn}",
                                        color = DeepNavyText,
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            1 -> {
                LettersCurriculum.arabicLetters.chunked(3).forEach { rowItems ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        rowItems.forEach { letter ->
                            Surface(
                                modifier = Modifier
                                    .weight(1f)
                                    .shadow(4.dp, RoundedCornerShape(20.dp))
                                    .clickable {
                                        onSpeakLulu("${letter.letterNameAr} ، ${letter.wordAr}")
                                    },
                                shape = RoundedCornerShape(20.dp),
                                color = Color.White,
                                border = BorderStroke(2.dp, letter.color)
                            ) {
                                Column(
                                    modifier = Modifier.padding(10.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = letter.isolatedSymbol,
                                        color = letter.color,
                                        fontSize = 28.sp,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                    Text(
                                        text = letter.wordAr,
                                        color = DeepNavyText,
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                                    )
                                }
                            }
                        }
                        repeat(3 - rowItems.size) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }

            2 -> {
                LettersCurriculum.englishLetters.chunked(3).forEach { rowItems ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        rowItems.forEach { letter ->
                            Surface(
                                modifier = Modifier
                                    .weight(1f)
                                    .shadow(4.dp, RoundedCornerShape(20.dp))
                                    .clickable {
                                        onSpeakLulu("${letter.uppercaseSymbol}, ${letter.wordEn}")
                                    },
                                shape = RoundedCornerShape(20.dp),
                                color = Color.White,
                                border = BorderStroke(2.dp, letter.color)
                            ) {
                                Column(
                                    modifier = Modifier.padding(10.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "${letter.uppercaseSymbol} ${letter.lowercaseSymbol}",
                                        color = letter.color,
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                    Text(
                                        text = letter.wordEn,
                                        color = DeepNavyText,
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                                    )
                                }
                            }
                        }
                        repeat(3 - rowItems.size) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }

            3 -> {
                ColorsCurriculum.allColors.chunked(2).forEach { pair ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        pair.forEach { colorItem ->
                            Surface(
                                modifier = Modifier
                                    .weight(1f)
                                    .shadow(4.dp, RoundedCornerShape(20.dp))
                                    .clickable {
                                        onSpeakLulu(if (isAr) colorItem.nameAr else colorItem.nameEn)
                                    },
                                shape = RoundedCornerShape(20.dp),
                                color = Color.White,
                                border = BorderStroke(2.dp, colorItem.colorValue)
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Surface(
                                        modifier = Modifier.size(32.dp),
                                        shape = CircleShape,
                                        color = colorItem.colorValue,
                                        border = BorderStroke(1.5.dp, DeepNavyText)
                                    ) {}
                                    Text(
                                        text = "${colorItem.nameAr} • ${colorItem.nameEn}",
                                        color = DeepNavyText,
                                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                                    )
                                }
                            }
                        }
                    }
                }

                ShapesCurriculum.allShapes.chunked(2).forEach { pair ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        pair.forEach { shapeItem ->
                            Surface(
                                modifier = Modifier
                                    .weight(1f)
                                    .shadow(4.dp, RoundedCornerShape(20.dp))
                                    .clickable {
                                        onSpeakLulu(if (isAr) shapeItem.nameAr else shapeItem.nameEn)
                                    },
                                shape = RoundedCornerShape(20.dp),
                                color = Color.White,
                                border = BorderStroke(2.dp, shapeItem.color)
                            ) {
                                Column(
                                    modifier = Modifier.padding(12.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "${shapeItem.nameAr} • ${shapeItem.nameEn}",
                                        color = shapeItem.color,
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            else -> {
                AnimalsCurriculum.allAnimals.chunked(2).forEach { pair ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        pair.forEach { animal ->
                            Surface(
                                modifier = Modifier
                                    .weight(1f)
                                    .shadow(4.dp, RoundedCornerShape(20.dp))
                                    .clickable {
                                        onSpeakLulu(animal.spokenMeet(lang))
                                    },
                                shape = RoundedCornerShape(20.dp),
                                color = Color.White,
                                border = BorderStroke(2.dp, animal.color)
                            ) {
                                Column(
                                    modifier = Modifier.padding(12.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "${animal.nameAr} • ${animal.nameEn}",
                                        color = DeepNavyText,
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold)
                                    )
                                    Text(
                                        text = animal.soundLabel(lang),
                                        color = animal.color,
                                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                                    )
                                }
                            }
                        }
                        if (pair.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ChildAccountSubView(
    uiState: FunLearningUiState,
    onUpdateChildName: (String) -> Unit,
    onSelectLanguage: (AppLanguage) -> Unit,
    onToggleSound: () -> Unit,
    onOpenRewardsWorld: () -> Unit,
    onOpenParentSection: () -> Unit,
    modifier: Modifier = Modifier
) {
    val settings = uiState.settings
    val lang = settings.language
    val isAr = uiState.isArabic
    val currentLevel = RewardsProgressCalculator.computeCurrentLevel(settings)
    var nameInput by remember(settings.childName) { mutableStateOf(settings.childName) }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            color = Color.White,
            border = BorderStroke(3.dp, MagicPurple),
            shadowElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Surface(
                    modifier = Modifier.size(96.dp),
                    shape = CircleShape,
                    border = BorderStroke(4.dp, SunnyYellow),
                    shadowElevation = 6.dp
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_lulu_mascot_1794072207514),
                        contentDescription = "Lulu",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Text(
                    text = if (isAr) "ملف البطل: ${settings.childName}" else "Hero Profile: ${settings.childName}",
                    color = DeepNavyText,
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.ExtraBold)
                )

                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = currentLevel.badgeColor
                ) {
                    Text(
                        text = if (isAr) "المستوى ${currentLevel.levelNumber}: ${currentLevel.titleAr}" else "Level ${currentLevel.levelNumber}: ${currentLevel.titleEn}",
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = nameInput,
                        onValueChange = { nameInput = it },
                        singleLine = true,
                        label = { Text(if (isAr) "اسم البطل أو البطلة" else "Hero's Name") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(16.dp)
                    )
                    Bouncy3DButton(
                        text = if (isAr) "حفظ" else "Save",
                        onClick = { onUpdateChildName(nameInput) },
                        containerColor = SkyCyanDark,
                        shadowColor = DeepNavyText,
                        fontSize = 15.sp,
                        cornerRadius = 16.dp,
                        lipHeight = 4.dp
                    )
                }
            }
        }

        // Quick Actions for Rewards & Parent Dashboard
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Bouncy3DButton(
                text = if (isAr) "الشهادات والشارات" else "Badges & Certificates",
                onClick = onOpenRewardsWorld,
                icon = Icons.Filled.EmojiEvents,
                containerColor = Color(0xFFF57C00),
                shadowColor = Color(0xFFE65100),
                modifier = Modifier.weight(1f)
            )
            Bouncy3DButton(
                text = if (isAr) "لوحة ولي الأمر" else "Parent Dashboard",
                onClick = onOpenParentSection,
                icon = Icons.Filled.SupervisorAccount,
                containerColor = MagicPurple,
                shadowColor = MagicPurpleDark,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun LuluNotificationsDialog(
    uiState: FunLearningUiState,
    onDismiss: () -> Unit,
    onOpenRewardsWorld: () -> Unit
) {
    val isAr = uiState.isArabic
    val settings = uiState.settings
    val unlockedBadges = RewardsProgressCalculator.unlockedBadges(settings)
    val availableBoxes = RewardsProgressCalculator.availableSurpriseBoxesToOpen(settings)

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            color = Color.White,
            border = BorderStroke(3.dp, Color(0xFF29B6F6)),
            shadowElevation = 12.dp
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isAr) "🔔 رسائل وإشعارات لولو" else "🔔 Lulu's Notifications",
                        color = DeepNavyText,
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold)
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Filled.Close, contentDescription = null)
                    }
                }

                Surface(
                    shape = RoundedCornerShape(18.dp),
                    color = Color(0xFFFFF8E1),
                    border = BorderStroke(1.5.dp, StarGoldBorder)
                ) {
                    Text(
                        text = if (isAr) {
                            "🌟 لديك ${settings.totalEarnedStars} نجمة ذهبية و${unlockedBadges.size} شارات مفتوحة!"
                        } else {
                            "🌟 You have ${settings.totalEarnedStars} golden stars and ${unlockedBadges.size} unlocked badges!"
                        },
                        color = DeepNavyText,
                        modifier = Modifier.padding(12.dp),
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }

                if (availableBoxes.isNotEmpty()) {
                    Surface(
                        shape = RoundedCornerShape(18.dp),
                        color = Color(0xFFFCE4EC),
                        border = BorderStroke(1.5.dp, Color(0xFFD81B60))
                    ) {
                        Text(
                            text = if (isAr) {
                                "🎁 لديك ${availableBoxes.size} صندوق مفاجآت جاهز للفتح في صفحة إنجازاتي!"
                            } else {
                                "🎁 You have ${availableBoxes.size} Surprise Box ready to open in Rewards!"
                            },
                            color = Color(0xFF880E4F),
                            modifier = Modifier.padding(12.dp),
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }

                Button(
                    onClick = {
                        onDismiss()
                        onOpenRewardsWorld()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0288D1))
                ) {
                    Text(
                        text = if (isAr) "انتقل إلى الجوائز والشهادات" else "Go to Rewards & Certificates",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun QuickMenuDialog(
    uiState: FunLearningUiState,
    onDismiss: () -> Unit,
    onSelectLanguage: (AppLanguage) -> Unit,
    onToggleSound: () -> Unit,
    onOpenRewardsWorld: () -> Unit,
    onOpenParentSection: () -> Unit
) {
    val isAr = uiState.isArabic
    val lang = uiState.settings.language

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            color = Color.White,
            border = BorderStroke(3.dp, MagicPurple),
            shadowElevation = 12.dp
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isAr) "☰ القائمة السريعة" else "☰ Quick Menu",
                        color = DeepNavyText,
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold)
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Filled.Close, contentDescription = null)
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isAr) "اللغة:" else "Language:",
                        color = DeepNavyText,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    LanguageSelectorSwitch(
                        currentLanguage = lang,
                        onSelectLanguage = onSelectLanguage
                    )
                }

                Bouncy3DButton(
                    text = if (uiState.settings.soundEnabled) {
                        if (isAr) "الصوت: قيد التشغيل 🔊" else "Sound: ON 🔊"
                    } else {
                        if (isAr) "الصوت: متوقف 🔇" else "Sound: OFF 🔇"
                    },
                    onClick = onToggleSound,
                    containerColor = if (uiState.settings.soundEnabled) Color(0xFF2E7D32) else Color(0xFF78909C),
                    shadowColor = DeepNavyText,
                    modifier = Modifier.fillMaxWidth()
                )

                Bouncy3DButton(
                    text = if (isAr) "عالم المكافآت والشهادات 🏅" else "Rewards & Certificates 🏅",
                    onClick = {
                        onDismiss()
                        onOpenRewardsWorld()
                    },
                    containerColor = Color(0xFFF57C00),
                    shadowColor = Color(0xFFE65100),
                    modifier = Modifier.fillMaxWidth()
                )

                Bouncy3DButton(
                    text = if (isAr) "لوحة متابعة ولي الأمر 🔒" else "Parent Dashboard 🔒",
                    onClick = {
                        onDismiss()
                        onOpenParentSection()
                    },
                    containerColor = MagicPurple,
                    shadowColor = MagicPurpleDark,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

// ============================================================================
// 8. CUSTOM 3D VECTOR/CANVAS ILLUSTRATIONS (NO EMOJIS FOR MAIN GRAPHICS)
// ============================================================================

@Composable
fun Canvas3DStackOfBooks(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        // Bottom Green Book
        drawRoundRect(
            color = Color(0xFF2E7D32),
            topLeft = Offset(w * 0.12f, h * 0.62f),
            size = Size(w * 0.76f, h * 0.22f),
            cornerRadius = CornerRadius(10f, 10f)
        )
        drawRoundRect(
            color = Color(0xFFFFF8E1),
            topLeft = Offset(w * 0.18f, h * 0.66f),
            size = Size(w * 0.66f, h * 0.14f),
            cornerRadius = CornerRadius(6f, 6f)
        )

        // Middle Yellow/Gold Book
        drawRoundRect(
            color = Color(0xFFFBC02D),
            topLeft = Offset(w * 0.16f, h * 0.40f),
            size = Size(w * 0.72f, h * 0.22f),
            cornerRadius = CornerRadius(10f, 10f)
        )
        drawRoundRect(
            color = Color(0xFFFFFDE7),
            topLeft = Offset(w * 0.22f, h * 0.44f),
            size = Size(w * 0.62f, h * 0.14f),
            cornerRadius = CornerRadius(6f, 6f)
        )

        // Top Sky-Blue 3D Book
        drawRoundRect(
            color = Color(0xFF0288D1),
            topLeft = Offset(w * 0.14f, h * 0.16f),
            size = Size(w * 0.74f, h * 0.24f),
            cornerRadius = CornerRadius(12f, 12f)
        )
        drawRoundRect(
            color = Color.White,
            topLeft = Offset(w * 0.20f, h * 0.21f),
            size = Size(w * 0.64f, h * 0.14f),
            cornerRadius = CornerRadius(6f, 6f)
        )
        // Red Bookmark Ribbon
        drawRoundRect(
            color = Color(0xFFE53935),
            topLeft = Offset(w * 0.68f, h * 0.32f),
            size = Size(w * 0.10f, h * 0.18f),
            cornerRadius = CornerRadius(4f, 4f)
        )
    }
}

@Composable
fun Canvas3DBookWithMagnifier(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        // Open Book Base Cover
        drawRoundRect(
            color = Color(0xFFD84315),
            topLeft = Offset(w * 0.08f, h * 0.28f),
            size = Size(w * 0.84f, h * 0.56f),
            cornerRadius = CornerRadius(12f, 12f)
        )
        // Left & Right White Pages
        drawRoundRect(
            color = Color(0xFFFFFDE7),
            topLeft = Offset(w * 0.12f, h * 0.22f),
            size = Size(w * 0.37f, h * 0.54f),
            cornerRadius = CornerRadius(8f, 8f)
        )
        drawRoundRect(
            color = Color(0xFFFFF8E1),
            topLeft = Offset(w * 0.51f, h * 0.22f),
            size = Size(w * 0.37f, h * 0.54f),
            cornerRadius = CornerRadius(8f, 8f)
        )

        // Magnifying Glass Handle
        drawLine(
            color = Color(0xFFE65100),
            start = Offset(w * 0.62f, h * 0.62f),
            end = Offset(w * 0.86f, h * 0.86f),
            strokeWidth = 10f
        )
        // Magnifying Glass Lens & Golden Rim
        drawCircle(
            color = Color(0xAA81D4FA),
            radius = w * 0.22f,
            center = Offset(w * 0.48f, h * 0.46f)
        )
        drawCircle(
            color = Color(0xFFFFB300),
            radius = w * 0.22f,
            center = Offset(w * 0.48f, h * 0.46f),
            style = Stroke(width = 7f)
        )
    }
}

@Composable
fun Canvas3DGlossyStar(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val cx = size.width / 2f
        val cy = size.height / 2f
        val outerR = size.minDimension * 0.48f
        val innerR = outerR * 0.45f
        val path = Path()
        for (i in 0 until 10) {
            val r = if (i % 2 == 0) outerR else innerR
            val angle = Math.toRadians((i * 36.0) - 90.0)
            val x = cx + (r * Math.cos(angle)).toFloat()
            val y = cy + (r * Math.sin(angle)).toFloat()
            if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
        }
        path.close()
        drawPath(
            path = path,
            brush = Brush.verticalGradient(
                listOf(Color(0xFFFFEE58), Color(0xFFFFB300), Color(0xFFF57C00))
            )
        )
        drawPath(
            path = path,
            color = Color(0xFFE65100),
            style = Stroke(width = 2f)
        )
    }
}

@Composable
private fun Canvas3DMiniBooksIcon(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        // 3 Vertical 3D Books (Blue, Gold, Orange) like the reference bottom nav "المكتبة" icon
        drawRoundRect(
            color = Color(0xFF1E88E5),
            topLeft = Offset(w * 0.12f, h * 0.22f),
            size = Size(w * 0.22f, h * 0.66f),
            cornerRadius = CornerRadius(5f, 5f)
        )
        drawRoundRect(
            color = Color(0xFFFFB300),
            topLeft = Offset(w * 0.39f, h * 0.14f),
            size = Size(w * 0.22f, h * 0.74f),
            cornerRadius = CornerRadius(5f, 5f)
        )
        drawRoundRect(
            color = Color(0xFF0288D1),
            topLeft = Offset(w * 0.66f, h * 0.26f),
            size = Size(w * 0.22f, h * 0.62f),
            cornerRadius = CornerRadius(5f, 5f)
        )
    }
}

@Composable
private fun CanvasSparkleRays(
    isLeft: Boolean,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val rayColor = Color(0xFFFFB300)
        if (isLeft) {
            drawLine(rayColor, Offset(w * 0.85f, h * 0.3f), Offset(w * 0.2f, h * 0.1f), strokeWidth = 6f)
            drawLine(rayColor, Offset(w * 0.85f, h * 0.5f), Offset(w * 0.1f, h * 0.5f), strokeWidth = 6f)
            drawLine(rayColor, Offset(w * 0.85f, h * 0.7f), Offset(w * 0.2f, h * 0.9f), strokeWidth = 6f)
        } else {
            drawLine(rayColor, Offset(w * 0.15f, h * 0.3f), Offset(w * 0.8f, h * 0.1f), strokeWidth = 6f)
            drawLine(rayColor, Offset(w * 0.15f, h * 0.5f), Offset(w * 0.9f, h * 0.5f), strokeWidth = 6f)
            drawLine(rayColor, Offset(w * 0.15f, h * 0.7f), Offset(w * 0.8f, h * 0.9f), strokeWidth = 6f)
        }
    }
}
