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
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeOff
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.SupervisorAccount
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.AppLanguage
import com.example.model.LearningCategory
import com.example.ui.theme.DeepNavyText
import com.example.ui.theme.LuluPink
import com.example.ui.theme.LuluPinkDark
import com.example.ui.theme.MagicPurple
import com.example.ui.theme.MagicPurpleDark
import com.example.ui.theme.SkyCyanDark
import com.example.ui.theme.SoftSlateText
import com.example.ui.theme.StarGoldBorder
import com.example.ui.theme.SunnyYellow
import com.example.ui.theme.SunnyYellowDark

/**
 * Tactile 3D cartoon button with a bottom lip shadow that depresses when tapped.
 * Designed for preschool children aged 4-6 with generous touch targets and high contrast.
 */
@Composable
fun Bouncy3DButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = LuluPink,
    shadowColor: Color = LuluPinkDark,
    contentColor: Color = Color.White,
    icon: ImageVector? = null,
    cornerRadius: Dp = 22.dp,
    lipHeight: Dp = 6.dp,
    fontSize: TextUnit = 17.sp,
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
    testTagName: String? = null
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val pressOffset by animateDpAsState(
        targetValue = if (isPressed) (lipHeight - 1.dp).coerceAtLeast(0.dp) else 0.dp,
        animationSpec = spring(stiffness = 700f),
        label = "button_press_offset"
    )
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.96f else 1f,
        animationSpec = spring(stiffness = 600f),
        label = "button_scale"
    )

    val shape = RoundedCornerShape(cornerRadius)

    Box(
        modifier = modifier
            .minimumInteractiveComponentSize()
            .scale(scale)
            .let { if (testTagName != null) it.testTag(testTagName) else it }
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.TopCenter
    ) {
        // Bottom 3D lip shadow
        Box(
            modifier = Modifier
                .matchParentSize()
                .padding(top = lipHeight)
                .background(color = shadowColor, shape = shape)
        )

        // Top face
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = lipHeight)
                .offset(y = pressOffset),
            shape = shape,
            color = containerColor,
            border = BorderStroke(2.dp, Color.White.copy(alpha = 0.75f))
        ) {
            Row(
                modifier = Modifier.padding(contentPadding),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                if (icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = text,
                        tint = contentColor,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                }
                Text(
                    text = text,
                    color = contentColor,
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = fontSize
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

/**
 * Tactile 3D Icon Button for top navigation actions (Sound On/Off, Home, Parent Gate).
 */
@Composable
fun Bouncy3DIconButton(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = SunnyYellow,
    shadowColor: Color = SunnyYellowDark,
    iconTint: Color = DeepNavyText,
    badgeLabel: String? = null,
    testTagName: String? = null
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val lipHeight = 5.dp
    val pressOffset by animateDpAsState(
        targetValue = if (isPressed) 3.dp else 0.dp,
        animationSpec = spring(stiffness = 700f),
        label = "icon_btn_offset"
    )
    val shape = RoundedCornerShape(18.dp)

    Box(
        modifier = modifier
            .minimumInteractiveComponentSize()
            .let { if (testTagName != null) it.testTag(testTagName) else it }
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.TopCenter
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .padding(top = lipHeight)
                .background(color = shadowColor, shape = shape)
        )
        Surface(
            modifier = Modifier
                .padding(bottom = lipHeight)
                .offset(y = pressOffset),
            shape = shape,
            color = containerColor,
            border = BorderStroke(1.5.dp, Color.White.copy(alpha = 0.8f))
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 13.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = contentDescription,
                    tint = iconTint,
                    modifier = Modifier.size(22.dp)
                )
                if (badgeLabel != null) {
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = badgeLabel,
                        color = iconTint,
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    )
                }
            }
        }
    }
}

/**
 * Top Header Bar containing:
 * - Application logo & bilingual name (عالم المرح والتعلّم | Fun Learning World)
 * - Language selector pill: العربية / English
 * - Sound On/Off 3D button
 * - Parent Section 3D button
 */
@Composable
fun TopKidHeaderBar(
    language: AppLanguage,
    soundEnabled: Boolean,
    onSelectLanguage: (AppLanguage) -> Unit,
    onToggleSound: () -> Unit,
    onOpenParentSection: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isArabic = language == AppLanguage.AR

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .shadow(elevation = 10.dp, shape = RoundedCornerShape(30.dp), spotColor = Color(0x400288D1)),
        shape = RoundedCornerShape(30.dp),
        color = Color.White,
        border = BorderStroke(2.5.dp, Color(0xFF81D4FA))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Row 1: App Logo + High-Contrast Bilingual App Title
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Surface(
                        modifier = Modifier.size(60.dp),
                        shape = RoundedCornerShape(20.dp),
                        color = Color(0xFF4FC3F7),
                        border = BorderStroke(3.dp, SunnyYellow),
                        shadowElevation = 5.dp
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img_app_icon_1794072206224),
                            contentDescription = if (isArabic) "شعار عالم المرح والتعلّم" else "Fun Learning World Logo",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(
                            text = if (isArabic) "عالم المرح والتعلّم" else "Fun Learning World",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = if (isArabic) 25.sp else 22.sp,
                                lineHeight = if (isArabic) 32.sp else 28.sp
                            ),
                            color = DeepNavyText,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = if (isArabic) "تعلّم والعب مع لولو • ٤ إلى ٦ سنوات" else "Learn & Play with Lulu • Ages 4–6",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = if (isArabic) 15.sp else 14.sp
                            ),
                            color = Color(0xFFAD1457),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            // Row 2: Action Controls (Language Switcher: العربية / English, Sound Toggle, Parent Section)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                LanguageSelectorSwitch(
                    currentLanguage = language,
                    onSelectLanguage = onSelectLanguage
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Bouncy3DIconButton(
                        icon = if (soundEnabled) {
                            Icons.AutoMirrored.Filled.VolumeUp
                        } else {
                            Icons.AutoMirrored.Filled.VolumeOff
                        },
                        contentDescription = if (soundEnabled) "Sound On" else "Sound Off",
                        onClick = onToggleSound,
                        containerColor = if (soundEnabled) Color(0xFF2E7D32) else Color(0xFFCFD8DC),
                        shadowColor = if (soundEnabled) Color(0xFF1B5E20) else Color(0xFF78909C),
                        iconTint = if (soundEnabled) Color.White else DeepNavyText,
                        testTagName = "sound_toggle_button"
                    )

                    Bouncy3DIconButton(
                        icon = Icons.Filled.SupervisorAccount,
                        contentDescription = if (isArabic) "ركن الأهل" else "Parent Section",
                        onClick = onOpenParentSection,
                        containerColor = MagicPurple,
                        shadowColor = MagicPurpleDark,
                        iconTint = Color.White,
                        badgeLabel = if (isArabic) "الأهل" else "Parents",
                        testTagName = "parent_section_button"
                    )
                }
            }
        }
    }
}

/**
 * High-contrast interactive segmented pill for switching between العربية (RTL) and English (LTR).
 */
@Composable
fun LanguageSelectorSwitch(
    currentLanguage: AppLanguage,
    onSelectLanguage: (AppLanguage) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .minimumInteractiveComponentSize()
            .testTag("language_selector"),
        shape = RoundedCornerShape(22.dp),
        color = Color(0xFFE1F5FE),
        border = BorderStroke(2.dp, SkyCyanDark)
    ) {
        Row(
            modifier = Modifier.padding(4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            val isAr = currentLanguage == AppLanguage.AR
            LanguageTabChip(
                label = "العربية",
                selected = isAr,
                onClick = { onSelectLanguage(AppLanguage.AR) },
                tag = "lang_ar_button"
            )
            LanguageTabChip(
                label = "English",
                selected = !isAr,
                onClick = { onSelectLanguage(AppLanguage.EN) },
                tag = "lang_en_button"
            )
        }
    }
}

@Composable
private fun LanguageTabChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    tag: String
) {
    Surface(
        modifier = Modifier
            .testTag(tag)
            .clip(RoundedCornerShape(18.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        color = if (selected) SkyCyanDark else Color.Transparent,
        shadowElevation = if (selected) 3.dp else 0.dp
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp),
            color = if (selected) Color.White else DeepNavyText,
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
        )
    }
}

/**
 * Engaging Lulu Mascot Hero Welcome Card on the Home Screen:
 * - Features a large, fully visible 3D cartoon girl mascot portrait (no overlapping badges on her face/body)
 * - High-contrast speech bubble with natural, preschool-friendly Arabic/English text
 * - Prominent "ابدأ التعلّم الآن! / Start Learning Now!" main 3D CTA button
 */
@Composable
fun LuluMascotWelcomeCard(
    greetingText: String,
    isArabic: Boolean,
    onTapLulu: () -> Unit,
    onStartLearning: () -> Unit = onTapLulu,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "lulu_float")
    val floatOffset by infiniteTransition.animateFloat(
        initialValue = -4f,
        targetValue = 4f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "lulu_vertical_bob"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("lulu_welcome_card")
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(32.dp),
                spotColor = Color(0x55D81B60)
            ),
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(3.dp, Color(0xFFFF80AB))
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFFFF0F6),
                            Color(0xFFFFFDE7),
                            Color(0xFFE8F8FF)
                        )
                    )
                )
                .padding(18.dp)
        ) {
            val isWide = maxWidth >= 520.dp
            val portraitWidth = if (isWide) 158.dp else 130.dp
            val portraitHeight = if (isWide) 172.dp else 144.dp

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Fully Visible 3D Lulu Character Portrait + Name Pill underneath
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier
                            .offset(y = floatOffset.dp)
                            .clickable(onClick = onTapLulu)
                    ) {
                        Surface(
                            modifier = Modifier.size(width = portraitWidth, height = portraitHeight),
                            shape = RoundedCornerShape(28.dp),
                            color = Color(0xFFCFE9FF),
                            border = BorderStroke(4.dp, SunnyYellow),
                            shadowElevation = 8.dp
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.img_lulu_mascot_1794072207514),
                                contentDescription = if (isArabic) "لولو صديقة الأطفال" else "Lulu the friendly mascot",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        // Name Ribbon cleanly below the portrait so Lulu is 100% visible
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = Color(0xFFC2185B),
                            border = BorderStroke(2.dp, Color.White),
                            shadowElevation = 3.dp
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.AutoAwesome,
                                    contentDescription = null,
                                    tint = SunnyYellow,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = if (isArabic) "صديقتكم لولو" else "Lulu • لولو",
                                    color = Color.White,
                                    style = MaterialTheme.typography.labelLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = if (isArabic) 14.sp else 13.sp
                                    )
                                )
                            }
                        }
                    }

                    // High-Contrast Speech Bubble Content
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFF6A1B9A)
                        ) {
                            Text(
                                text = if (isArabic) "مرحباً يا أصدقائي! 🌟" else "Hello My Friends! 🌟",
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp),
                                color = Color.White,
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = if (isArabic) 15.sp else 13.sp
                                )
                            )
                        }

                        // Crisp White Speech Bubble with Dark High-Contrast Text
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(onClick = onTapLulu),
                            shape = RoundedCornerShape(22.dp),
                            color = Color.White,
                            border = BorderStroke(2.dp, StarGoldBorder),
                            shadowElevation = 3.dp
                        ) {
                            Column(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = greetingText,
                                    color = DeepNavyText,
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = if (isArabic) 19.sp else 17.sp,
                                        lineHeight = if (isArabic) 28.sp else 24.sp
                                    )
                                )
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                                        contentDescription = null,
                                        tint = Color(0xFFC2185B),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = if (isArabic) "اضغط هنا لسماع رسالة أخرى من لولو" else "Tap here for another message from Lulu",
                                        color = Color(0xFFC2185B),
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = if (isArabic) 13.sp else 12.sp
                                        )
                                    )
                                }
                            }
                        }
                    }
                }

                // Prominent Main "Start Learning" Hero CTA Button
                Bouncy3DButton(
                    text = if (isArabic) "ابدأ التعلّم الآن!" else "Start Learning Now!",
                    onClick = onStartLearning,
                    modifier = Modifier.fillMaxWidth(),
                    containerColor = Color(0xFFD81B60),
                    shadowColor = Color(0xFF880E4F),
                    contentColor = Color.White,
                    icon = Icons.Filled.RocketLaunch,
                    cornerRadius = 24.dp,
                    lipHeight = 7.dp,
                    fontSize = if (isArabic) 20.sp else 18.sp,
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 14.dp),
                    testTagName = "main_start_learning_button"
                )
            }
        }
    }
}

/**
 * Large, colorful, illustrated 3D card for each of the 8 learning categories.
 * Features:
 * - Clean header row with category icon badge and 3-star progress pill
 * - Full, unobstructed 3D cartoon illustration for the category (no covering symbols or dark scrims)
 * - Large readable Arabic/English title + natural subtitle
 * - Prominent tactile 3D interaction button
 */
@Composable
fun LearningCategoryCard(
    category: LearningCategory,
    language: AppLanguage,
    earnedStars: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isArabic = language == AppLanguage.AR
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.96f else 1f,
        animationSpec = spring(stiffness = 600f),
        label = "card_scale"
    )
    val lipHeight = 8.dp
    val pressOffset by animateDpAsState(
        targetValue = if (isPressed) 5.dp else 0.dp,
        animationSpec = spring(stiffness = 700f),
        label = "card_lip_offset"
    )

    val cardShape = RoundedCornerShape(28.dp)

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .scale(scale)
            .testTag("category_card_${category.id.key}")
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.TopCenter
    ) {
        val isTabletCard = maxWidth >= 230.dp
        val illustrationHeight = if (isTabletCard) 190.dp else 144.dp

        // 3D Bottom Shadow Lip
        Box(
            modifier = Modifier
                .matchParentSize()
                .padding(top = lipHeight)
                .background(color = category.shadowColor, shape = cardShape)
        )

        // Main Card Surface
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = lipHeight)
                .offset(y = pressOffset),
            shape = cardShape,
            color = category.cardBgColor,
            border = BorderStroke(3.dp, Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // 1. Top Row: Category Icon Badge + 3-Star Progress Pill (above image so illustration is 100% clear)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = category.accentColor,
                        border = BorderStroke(1.5.dp, Color.White),
                        shadowElevation = 3.dp
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 9.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = category.icon,
                                contentDescription = category.title(language),
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "${category.numberIndex}",
                                color = Color.White,
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            )
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = Color.White,
                        border = BorderStroke(1.5.dp, StarGoldBorder),
                        shadowElevation = 2.dp
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(1.dp)
                        ) {
                            repeat(category.maxStars) { idx ->
                                val filled = idx < earnedStars
                                Icon(
                                    imageVector = if (filled) Icons.Filled.Star else Icons.Filled.StarBorder,
                                    contentDescription = null,
                                    tint = if (filled) StarGoldBorder else Color(0xFFB0BEC5),
                                    modifier = Modifier.size(15.dp)
                                )
                            }
                        }
                    }
                }

                // 2. Full, Unobstructed Colorful 3D Cartoon Illustration
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(illustrationHeight),
                    shape = RoundedCornerShape(22.dp),
                    color = Color.White,
                    border = BorderStroke(2.5.dp, Color.White),
                    shadowElevation = 4.dp
                ) {
                    Image(
                        painter = painterResource(id = category.illustrationResId),
                        contentDescription = category.title(language),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                // 3. Large, Ultra-Readable Category Title + Secondary Language Badge + Subtitle
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = category.title(language),
                        color = DeepNavyText,
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = when {
                                isTabletCard && isArabic -> 25.sp
                                isTabletCard -> 23.sp
                                isArabic -> 21.sp
                                else -> 19.sp
                            },
                            lineHeight = if (isArabic) 28.sp else 25.sp
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center
                    )

                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Color.White,
                        border = BorderStroke(1.dp, category.accentColor.copy(alpha = 0.45f))
                    ) {
                        Text(
                            text = category.secondaryTitle(language),
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 2.dp),
                            color = category.shadowColor,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = if (isTabletCard) 14.sp else 12.sp
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Text(
                        text = category.subtitle(language),
                        color = SoftSlateText,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = if (isArabic) 15.sp else 13.sp,
                            lineHeight = if (isArabic) 21.sp else 18.sp
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center
                    )
                }

                // 4. Prominent, Easy-to-Tap 3D Interaction Button
                Bouncy3DButton(
                    text = category.buttonLabel(language),
                    onClick = onClick,
                    modifier = Modifier.fillMaxWidth(),
                    containerColor = category.accentColor,
                    shadowColor = category.shadowColor,
                    contentColor = Color.White,
                    icon = Icons.Filled.PlayArrow,
                    cornerRadius = 20.dp,
                    lipHeight = 5.dp,
                    fontSize = if (isArabic) 17.sp else 15.sp,
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 10.dp),
                    testTagName = "category_btn_${category.id.key}"
                )
            }
        }
    }
}

/**
 * Bottom Section (Normal Scrolling Item):
 * - My Learning Journey (رحلتي التعليمية)
 * - Stars Earned (النجوم المكتسبة)
 * - High-Contrast Rainbow Progress Indicator + Interactive Journey Button
 */
@Composable
fun BottomLearningJourneyBar(
    language: AppLanguage,
    earnedStars: Int,
    maxStars: Int,
    progressFraction: Float,
    onOpenJourney: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isArabic = language == AppLanguage.AR
    val animatedProgress by animateFloatAsState(
        targetValue = progressFraction,
        animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing),
        label = "journey_progress"
    )

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .testTag("bottom_learning_journey_bar")
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(28.dp),
                spotColor = Color(0x404A148C)
            )
            .clickable(onClick = onOpenJourney),
        shape = RoundedCornerShape(28.dp),
        color = Color.White,
        border = BorderStroke(3.dp, StarGoldBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = MagicPurpleDark,
                        modifier = Modifier.size(44.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Filled.EmojiEvents,
                                contentDescription = null,
                                tint = SunnyYellow,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    Column {
                        Text(
                            text = if (isArabic) "رحلتي التعليمية" else "My Learning Journey",
                            color = DeepNavyText,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = if (isArabic) 20.sp else 18.sp
                            )
                        )
                        Text(
                            text = if (isArabic) "اضغط هنا لمشاهدة نجومك وجوائزك 🌟" else "Tap here to view your stars & rewards 🌟",
                            color = SoftSlateText,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = if (isArabic) 14.sp else 13.sp
                            )
                        )
                    }
                }

                // High-Contrast Stars Earned Counter Pill
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color(0xFFFFF8E1),
                    border = BorderStroke(2.dp, StarGoldBorder),
                    shadowElevation = 4.dp
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = if (isArabic) "النجوم" else "Stars",
                            tint = StarGoldBorder,
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = "$earnedStars / $maxStars",
                            color = DeepNavyText,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 17.sp
                            )
                        )
                    }
                }
            }

            // High-Contrast Rainbow Progress Bar Indicator
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(22.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFCFD8DC))
                    .border(1.5.dp, Color(0xFF90A4AE), RoundedCornerShape(12.dp))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(fraction = animatedProgress.coerceAtLeast(0.10f))
                        .height(22.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xFFD81B60),
                                    Color(0xFFF57C00),
                                    Color(0xFF2E7D32),
                                    Color(0xFF0288D1),
                                    Color(0xFF6A1B9A)
                                )
                            )
                        )
                )

                Surface(
                    modifier = Modifier.align(Alignment.Center),
                    shape = RoundedCornerShape(8.dp),
                    color = Color.White.copy(alpha = 0.92f)
                ) {
                    Text(
                        text = "${(animatedProgress * 100).toInt()}%",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 1.dp),
                        color = DeepNavyText,
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    )
                }
            }
        }
    }
}
