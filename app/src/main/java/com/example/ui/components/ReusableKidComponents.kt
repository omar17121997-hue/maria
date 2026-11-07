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
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
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
import com.example.ui.theme.MeadowGreen
import com.example.ui.theme.MeadowGreenDark
import com.example.ui.theme.SkyCyan
import com.example.ui.theme.SkyCyanDark
import com.example.ui.theme.SoftSlateText
import com.example.ui.theme.StarGold
import com.example.ui.theme.StarGoldBorder
import com.example.ui.theme.SunnyYellow
import com.example.ui.theme.SunnyYellowDark

/**
 * Tactile 3D cartoon button with a bottom lip shadow that depresses when tapped.
 * Designed for preschool children aged 4-6 with generous touch targets (minimum 48dp).
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
    lipHeight: Dp = 5.dp,
    contentPadding: PaddingValues = PaddingValues(horizontal = 18.dp, vertical = 10.dp),
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
                .padding(bottom = lipHeight)
                .offset(y = pressOffset),
            shape = shape,
            color = containerColor,
            border = BorderStroke(1.5.dp, Color.White.copy(alpha = 0.55f))
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
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                }
                Text(
                    text = text,
                    color = contentColor,
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
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
    val lipHeight = 4.dp
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
            border = BorderStroke(1.5.dp, Color.White.copy(alpha = 0.7f))
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
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
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = badgeLabel,
                        color = iconTint,
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
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
            .shadow(elevation = 8.dp, shape = RoundedCornerShape(28.dp), spotColor = Color(0x330288D1)),
        shape = RoundedCornerShape(28.dp),
        color = Color.White.copy(alpha = 0.94f),
        border = BorderStroke(2.dp, Color(0xFFB3E5FC))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Row 1: App Logo + Bilingual App Title
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    // 3D Star Book App Logo Emblem
                    Surface(
                        modifier = Modifier.size(54.dp),
                        shape = RoundedCornerShape(18.dp),
                        color = Color(0xFF4FC3F7),
                        border = BorderStroke(2.5.dp, SunnyYellow),
                        shadowElevation = 4.dp
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img_app_icon_1794072206224),
                            contentDescription = if (isArabic) "شعار عالم المرح والتعلّم" else "Fun Learning World Logo",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = if (isArabic) "عالم المرح والتعلّم" else "Fun Learning World",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 21.sp
                            ),
                            color = MagicPurpleDark,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = if (isArabic) "Fun Learning World • ٤–٦ سنوات" else "عالم المرح والتعلّم • Ages 4–6",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            ),
                            color = LuluPinkDark,
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
                // Language Selector Pill: العربية / English
                LanguageSelectorSwitch(
                    currentLanguage = language,
                    onSelectLanguage = onSelectLanguage
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Sound On/Off Button
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
                        testTagName = "sound_toggle_button"
                    )

                    // Parent Section Button
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
 * Interactive segmented pill for switching between العربية (RTL) and English (LTR).
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
        shape = RoundedCornerShape(20.dp),
        color = Color(0xFFEBF6FF),
        border = BorderStroke(1.5.dp, Color(0xFF90CAF9))
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
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        color = if (selected) SkyCyan else Color.Transparent,
        shadowElevation = if (selected) 3.dp else 0.dp
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            color = if (selected) Color.White else DeepNavyText,
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        )
    }
}

/**
 * Lulu Mascot Welcome Card on the Home Screen:
 * Features cute 3D cartoon Lulu with a gentle floating animation,
 * her friendly welcome message in a speech bubble, and an interactive button to hear/cycle her message.
 */
@Composable
fun LuluMascotWelcomeCard(
    greetingText: String,
    isArabic: Boolean,
    onTapLulu: () -> Unit,
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
                elevation = 10.dp,
                shape = RoundedCornerShape(30.dp),
                spotColor = Color(0x40FF6F91)
            )
            .clickable(onClick = onTapLulu),
        shape = RoundedCornerShape(30.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(2.5.dp, Color(0xFFFFC1E3))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFFFFF5F8),
                            Color(0xFFFFFDE7),
                            Color(0xFFEDF8FF)
                        )
                    )
                )
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Lulu 3D Avatar Frame with floating star badge
                Box(
                    modifier = Modifier
                        .size(106.dp)
                        .offset(y = floatOffset.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Surface(
                        modifier = Modifier.size(100.dp),
                        shape = CircleShape,
                        color = Color(0xFFDFF4FF),
                        border = BorderStroke(3.5.dp, SunnyYellow),
                        shadowElevation = 6.dp
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img_lulu_mascot_1794072207514),
                            contentDescription = if (isArabic) "لولو صديقة الأطفال" else "Lulu the friendly mascot",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    // Name Tag Pill under Lulu
                    Surface(
                        modifier = Modifier.align(Alignment.BottomCenter),
                        shape = RoundedCornerShape(12.dp),
                        color = LuluPink,
                        border = BorderStroke(1.5.dp, Color.White),
                        shadowElevation = 3.dp
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.AutoAwesome,
                                contentDescription = null,
                                tint = SunnyYellow,
                                modifier = Modifier.size(13.dp)
                            )
                            Text(
                                text = if (isArabic) "لولو | Lulu" else "Lulu | لولو",
                                color = Color.White,
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            )
                        }
                    }
                }

                // Speech Bubble Content
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = Color.White.copy(alpha = 0.92f),
                        border = BorderStroke(1.5.dp, Color(0xFFFFE082))
                    ) {
                        Text(
                            text = greetingText,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                            color = DeepNavyText,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                lineHeight = 23.sp
                            )
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = SunnyYellow.copy(alpha = 0.35f),
                            border = BorderStroke(1.dp, SunnyYellowDark.copy(alpha = 0.4f))
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                                    contentDescription = null,
                                    tint = DeepNavyText,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = if (isArabic) "اضغط لرسالة جديدة من لولو 💬" else "Tap for a new tip from Lulu 💬",
                                    color = DeepNavyText,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Large, colorful, illustrated 3D card for each of the 8 learning categories.
 * Includes:
 * - Unique colorful 3D illustration + floating themed 3D badges
 * - Friendly icon badge
 * - Earned stars indicator (e.g., ⭐⭐⭐)
 * - Large readable title + secondary language subtitle
 * - Clear tactile 3D interaction button
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
        targetValue = if (isPressed) 0.97f else 1f,
        animationSpec = spring(stiffness = 600f),
        label = "card_scale"
    )
    val lipHeight = 7.dp
    val pressOffset by animateDpAsState(
        targetValue = if (isPressed) 4.dp else 0.dp,
        animationSpec = spring(stiffness = 700f),
        label = "card_lip_offset"
    )

    val cardShape = RoundedCornerShape(30.dp)

    Box(
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
            border = BorderStroke(2.5.dp, Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Top Row: Friendly Icon Badge + Star Progress Pill
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Friendly Icon + Category Number Badge
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = category.accentColor,
                        border = BorderStroke(1.5.dp, Color.White),
                        shadowElevation = 3.dp
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = category.icon,
                                contentDescription = category.title(language),
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "#${category.numberIndex}",
                                color = Color.White,
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            )
                        }
                    }

                    // 3-Star Progress Badge
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color.White.copy(alpha = 0.9f),
                        border = BorderStroke(1.dp, StarGoldBorder.copy(alpha = 0.5f))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            repeat(category.maxStars) { idx ->
                                val filled = idx < earnedStars
                                Icon(
                                    imageVector = if (filled) Icons.Filled.Star else Icons.Filled.StarBorder,
                                    contentDescription = null,
                                    tint = if (filled) StarGoldBorder else Color(0xFFBDBDBD),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }

                // Unique Colorful 3D Illustration Box with Floating Character Emblems
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(132.dp)
                        .clip(RoundedCornerShape(22.dp))
                        .background(Color.White)
                        .border(2.dp, Color.White, RoundedCornerShape(22.dp))
                ) {
                    Image(
                        painter = painterResource(id = category.illustrationResId),
                        contentDescription = category.title(language),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    // Gradient Scrim at Bottom of Illustration for 3D Floating Badges
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        category.accentColor.copy(alpha = 0.55f)
                                    )
                                )
                            )
                    )

                    // Unique 3D Floating Symbol Chips for this specific Category
                    Row(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        category.floatingBadges.forEachIndexed { idx, symbol ->
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = Color.White.copy(alpha = 0.95f),
                                border = BorderStroke(1.5.dp, category.accentColor),
                                shadowElevation = 4.dp
                            ) {
                                Text(
                                    text = symbol,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                    color = category.shadowColor,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 17.sp
                                    )
                                )
                            }
                        }
                    }
                }

                // Large Readable Category Title + Bilingual Pill + Description
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = category.title(language),
                            color = DeepNavyText,
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 22.sp
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f)
                        )

                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Color.White.copy(alpha = 0.75f)
                        ) {
                            Text(
                                text = category.secondaryTitle(language),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                color = category.shadowColor,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            )
                        }
                    }

                    Text(
                        text = category.subtitle(language),
                        color = SoftSlateText,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Clear Tactile Interaction Button
                Bouncy3DButton(
                    text = category.buttonLabel(language),
                    onClick = onClick,
                    modifier = Modifier.fillMaxWidth(),
                    containerColor = category.accentColor,
                    shadowColor = category.shadowColor,
                    contentColor = Color.White,
                    icon = Icons.Filled.PlayArrow,
                    testTagName = "category_btn_${category.id.key}"
                )
            }
        }
    }
}

/**
 * Bottom Section:
 * - My Learning Journey (رحلتي التعليمية)
 * - Stars Earned (النجوم المكتسبة)
 * - Rainbow Progress Indicator + Interactive Journey Button
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
                elevation = 12.dp,
                shape = RoundedCornerShape(28.dp),
                spotColor = Color(0x40845EC2)
            )
            .clickable(onClick = onOpenJourney),
        shape = RoundedCornerShape(28.dp),
        color = Color.White.copy(alpha = 0.97f),
        border = BorderStroke(2.5.dp, SunnyYellow)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Title with Trophy Icon
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = MagicPurple,
                        modifier = Modifier.size(38.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Filled.EmojiEvents,
                                contentDescription = null,
                                tint = SunnyYellow,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }

                    Column {
                        Text(
                            text = if (isArabic) "رحلتي التعليمية" else "My Learning Journey",
                            color = DeepNavyText,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 17.sp
                            )
                        )
                        Text(
                            text = if (isArabic) "اضغط لعرض خريطة النجوم والجوائز" else "Tap to view your Star Map & Rewards",
                            color = SoftSlateText,
                            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp)
                        )
                    }
                }

                // Stars Earned Counter Pill
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color(0xFFFFF8E1),
                    border = BorderStroke(2.dp, StarGoldBorder),
                    shadowElevation = 3.dp
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = if (isArabic) "النجوم" else "Stars",
                            tint = StarGoldBorder,
                            modifier = Modifier.size(22.dp)
                        )
                        Text(
                            text = "$earnedStars / $maxStars",
                            color = DeepNavyText,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        )
                    }
                }
            }

            // Rainbow Progress Bar Indicator
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(18.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFECEFF1))
                    .border(1.dp, Color(0xFFCFD8DC), RoundedCornerShape(10.dp))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(fraction = animatedProgress.coerceAtLeast(0.08f))
                        .height(18.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    LuluPink,
                                    SunnyYellow,
                                    MeadowGreen,
                                    SkyCyan,
                                    MagicPurple
                                )
                            )
                        )
                )

                Text(
                    text = "${(animatedProgress * 100).toInt()}%",
                    modifier = Modifier.align(Alignment.Center),
                    color = DeepNavyText,
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )
                )
            }
        }
    }
}
