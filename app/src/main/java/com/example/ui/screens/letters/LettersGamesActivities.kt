package com.example.ui.screens.letters

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
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
import com.example.model.LetterLessonItem
import com.example.model.LettersActivityId
import com.example.model.LettersCurriculum
import com.example.model.LettersQuizBank
import com.example.model.LettersSectionMode
import com.example.ui.components.Bouncy3DButton
import com.example.ui.components.PlayfulSkyBackground
import com.example.ui.theme.DeepNavyText
import com.example.ui.theme.LuluPink
import com.example.ui.theme.MeadowGreen
import com.example.ui.theme.MeadowGreenDark
import com.example.ui.theme.SoftSlateText
import com.example.ui.theme.StarGoldBorder
import com.example.ui.theme.SunnyYellow
import com.example.ui.theme.SunnyYellowDark

/**
 * ACTIVITY 3: FIND THE LETTER (أين هو الحرف؟)
 * Shows 4 colorful 3D letter blocks and asks:
 * "أين حرف ب؟" / "Where is letter B?"
 */
@Composable
fun FindTheLetterActivityScreen(
    sectionMode: LettersSectionMode,
    appLanguage: AppLanguage,
    soundEnabled: Boolean,
    isActivityCompleted: Boolean,
    onSelectSectionMode: (LettersSectionMode) -> Unit,
    onBackToHub: () -> Unit,
    onNavigateHome: () -> Unit,
    onToggleSound: () -> Unit,
    onSpeakLulu: (String, AppLanguage) -> Unit,
    onMarkActivityCompleted: () -> Unit
) {
    val isArabicApp = appLanguage == AppLanguage.AR
    val isArabicSection = sectionMode == LettersSectionMode.ARABIC
    val allLetters = LettersCurriculum.lettersForSection(sectionMode)

    // Curated 6-round progression covering iconic letters across the alphabet
    val roundIndices = remember(sectionMode) {
        listOf(1, 2, 3, 5, 12, 20).map { it.coerceIn(1, allLetters.size) }
    }

    var currentRound by remember(sectionMode) { mutableIntStateOf(0) }
    var starsEarnedInSession by remember(sectionMode) { mutableIntStateOf(0) }
    var selectedLetterId by remember(currentRound, sectionMode) { mutableStateOf<String?>(null) }
    var isAnswerCorrect by remember(currentRound, sectionMode) { mutableStateOf<Boolean?>(null) }

    val targetLetter = allLetters[roundIndices[currentRound % roundIndices.size] - 1]
    val options: List<LetterLessonItem> = remember(targetLetter.id) {
        val distractors = LettersCurriculum.getDistinctDistractors(targetLetter, 3)
        (distractors + targetLetter).shuffled()
    }

    val questionPrompt = if (isArabicSection) {
        "أين حرف ${targetLetter.isolatedSymbol} (ال${targetLetter.letterNameAr}) ؟"
    } else {
        if (isArabicApp) {
            "أين هو الحرف ${targetLetter.uppercaseSymbol} (${targetLetter.lowercaseSymbol}) ؟"
        } else {
            "Where is letter ${targetLetter.uppercaseSymbol} (${targetLetter.lowercaseSymbol})?"
        }
    }

    val spokenQuestion = if (isArabicSection) {
        "أين حرف ال${targetLetter.letterNameAr}؟"
    } else {
        "Where is letter ${targetLetter.uppercaseSymbol}?"
    }

    LaunchedEffect(targetLetter.id) {
        onSpeakLulu(spokenQuestion, sectionMode.language)
    }

    val activityMeta = LettersActivityId.FIND_LETTER

    PlayfulSkyBackground(accentTint = activityMeta.accentColor) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .widthIn(max = 860.dp)
                .align(Alignment.TopCenter)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            LettersActivityTopBar(
                title = activityMeta.title(appLanguage),
                accentColor = activityMeta.accentColor,
                shadowColor = activityMeta.shadowColor,
                isArabic = isArabicApp,
                soundEnabled = soundEnabled,
                isActivityCompleted = isActivityCompleted,
                onBackToHub = onBackToHub,
                onNavigateHome = onNavigateHome,
                onToggleSound = onToggleSound
            )

            // Section Switcher
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Bouncy3DButton(
                    text = "الحروف العربية",
                    onClick = { onSelectSectionMode(LettersSectionMode.ARABIC) },
                    modifier = Modifier.weight(1f),
                    containerColor = if (isArabicSection) activityMeta.accentColor else Color.White,
                    shadowColor = if (isArabicSection) activityMeta.shadowColor else Color(0xFFB0BEC5),
                    contentColor = if (isArabicSection) Color.White else DeepNavyText,
                    fontSize = 15.sp
                )
                Bouncy3DButton(
                    text = "English Letters",
                    onClick = { onSelectSectionMode(LettersSectionMode.ENGLISH) },
                    modifier = Modifier.weight(1f),
                    containerColor = if (!isArabicSection) activityMeta.accentColor else Color.White,
                    shadowColor = if (!isArabicSection) activityMeta.shadowColor else Color(0xFFB0BEC5),
                    contentColor = if (!isArabicSection) Color.White else DeepNavyText,
                    fontSize = 15.sp
                )
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(12.dp, RoundedCornerShape(32.dp)),
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(3.dp, activityMeta.accentColor)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Round & Stars Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isArabicApp) {
                                "السؤال ${currentRound + 1} من ${roundIndices.size}"
                            } else {
                                "Round ${currentRound + 1} of ${roundIndices.size}"
                            },
                            color = SoftSlateText,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )

                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = Color(0xFFFFF8E1),
                            border = BorderStroke(1.5.dp, StarGoldBorder)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Star,
                                    contentDescription = null,
                                    tint = StarGoldBorder,
                                    modifier = Modifier.size(20.dp)
                                )
                                Text(
                                    text = "$starsEarnedInSession",
                                    color = DeepNavyText,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                )
                            }
                        }
                    }

                    // Lulu Question Prompt Bubble
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSpeakLulu(spokenQuestion, sectionMode.language) },
                        shape = RoundedCornerShape(24.dp),
                        color = activityMeta.cardBgColor,
                        border = BorderStroke(2.dp, activityMeta.accentColor)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Surface(
                                modifier = Modifier.size(64.dp),
                                shape = RoundedCornerShape(18.dp),
                                border = BorderStroke(2.dp, LuluPink),
                                color = Color(0xFFCFE9FF)
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
                                    text = questionPrompt,
                                    color = DeepNavyText,
                                    style = MaterialTheme.typography.headlineMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = if (isArabicApp) 24.sp else 21.sp
                                    )
                                )
                                Text(
                                    text = if (isArabicSection) {
                                        "تلميح: ${targetLetter.dotsDescriptionAr} • مثل كلمة (${targetLetter.wordAr} ${targetLetter.emoji})"
                                    } else {
                                        "Hint: ${targetLetter.uppercaseSymbol} is for ${targetLetter.wordEn} ${targetLetter.emoji}"
                                    },
                                    color = SoftSlateText,
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                                )
                            }

                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                                contentDescription = "Replay question",
                                tint = activityMeta.accentColor,
                                modifier = Modifier.size(30.dp)
                            )
                        }
                    }

                    // 4 Answer Choice Cards in a 2x2 Grid
                    val optionRows = options.chunked(2)
                    optionRows.forEach { rowItems ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            rowItems.forEach { item ->
                                val isSelected = selectedLetterId == item.id
                                val isTarget = item.id == targetLetter.id
                                val cardColor = when {
                                    isSelected && isTarget -> MeadowGreen
                                    isSelected && !isTarget -> Color(0xFFE53935)
                                    else -> item.color
                                }

                                Surface(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(118.dp)
                                        .testTag("find_letter_option_${item.id}")
                                        .shadow(8.dp, RoundedCornerShape(26.dp))
                                        .clickable {
                                            selectedLetterId = item.id
                                            if (item.id == targetLetter.id) {
                                                if (isAnswerCorrect != true) {
                                                    starsEarnedInSession++
                                                    if (starsEarnedInSession >= 2) {
                                                        onMarkActivityCompleted()
                                                    }
                                                }
                                                isAnswerCorrect = true
                                                val praise = if (isArabicSection) {
                                                    "أحسنتِ! هذا حرف ال${targetLetter.letterNameAr}!"
                                                } else {
                                                    "Great job! This is letter ${targetLetter.uppercaseSymbol}!"
                                                }
                                                onSpeakLulu(praise, sectionMode.language)
                                            } else {
                                                isAnswerCorrect = false
                                                val retryMsg = if (isArabicSection) {
                                                    "حاولي مرة أخرى يا بطلة! ابحثي عن حرف ال${targetLetter.letterNameAr}."
                                                } else {
                                                    "Try again superstar! Look for letter ${targetLetter.uppercaseSymbol}."
                                                }
                                                onSpeakLulu(retryMsg, sectionMode.language)
                                            }
                                        },
                                    shape = RoundedCornerShape(26.dp),
                                    color = cardColor,
                                    border = BorderStroke(3.dp, Color.White)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            text = item.gridDisplaySymbol,
                                            color = Color.White,
                                            style = MaterialTheme.typography.displayMedium.copy(
                                                fontWeight = FontWeight.Bold,
                                                fontSize = if (isArabicSection) 48.sp else 38.sp
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Feedback Banner + Next Question Button
                    AnimatedVisibility(
                        visible = isAnswerCorrect != null,
                        enter = fadeIn() + scaleIn(),
                        exit = fadeOut() + scaleOut()
                    ) {
                        val correct = isAnswerCorrect == true
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(22.dp),
                            color = if (correct) Color(0xFFE8F5E9) else Color(0xFFFFF3E0),
                            border = BorderStroke(2.dp, if (correct) MeadowGreen else SunnyYellowDark)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = if (correct) {
                                        if (isArabicApp) {
                                            "أحسنتِ! هذا حرف ال${targetLetter.letterNameAr} ⭐"
                                        } else {
                                            "Great job! That is letter ${targetLetter.uppercaseSymbol} ⭐"
                                        }
                                    } else {
                                        if (isArabicApp) {
                                            "قريب جداً! جربي اختيار مكعب آخر 💪"
                                        } else {
                                            "Almost! Try tapping another letter block 💪"
                                        }
                                    },
                                    color = DeepNavyText,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    modifier = Modifier.weight(1f)
                                )

                                if (correct) {
                                    Bouncy3DButton(
                                        text = if (isArabicApp) "السؤال التالي" else "Next",
                                        onClick = {
                                            currentRound = (currentRound + 1) % roundIndices.size
                                        },
                                        containerColor = MeadowGreen,
                                        shadowColor = MeadowGreenDark,
                                        contentColor = Color.White,
                                        icon = Icons.AutoMirrored.Filled.ArrowForward,
                                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
                                        testTagName = "find_letter_next_btn"
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * ACTIVITY 4: MATCH LETTER TO PICTURE (طابق الحرف بالصورة)
 * Displays a large letter and 4 cartoon picture cards.
 * Asks the child to choose the picture whose name begins with that letter (e.g., ب → بطة).
 */
@Composable
fun MatchLetterToPictureActivityScreen(
    sectionMode: LettersSectionMode,
    appLanguage: AppLanguage,
    soundEnabled: Boolean,
    isActivityCompleted: Boolean,
    onSelectSectionMode: (LettersSectionMode) -> Unit,
    onBackToHub: () -> Unit,
    onNavigateHome: () -> Unit,
    onToggleSound: () -> Unit,
    onSpeakLulu: (String, AppLanguage) -> Unit,
    onMarkActivityCompleted: () -> Unit
) {
    val isArabicApp = appLanguage == AppLanguage.AR
    val isArabicSection = sectionMode == LettersSectionMode.ARABIC
    val allLetters = LettersCurriculum.lettersForSection(sectionMode)

    val roundIndices = remember(sectionMode) {
        listOf(2, 1, 3, 4, 8, 12, 25).map { it.coerceIn(1, allLetters.size) }
    }

    var currentRound by remember(sectionMode) { mutableIntStateOf(0) }
    var starsEarnedInSession by remember(sectionMode) { mutableIntStateOf(0) }
    var selectedOptionId by remember(currentRound, sectionMode) { mutableStateOf<String?>(null) }
    var isAnswerCorrect by remember(currentRound, sectionMode) { mutableStateOf<Boolean?>(null) }

    val targetLetter = allLetters[roundIndices[currentRound % roundIndices.size] - 1]
    val pictureOptions = remember(targetLetter.id) {
        val distractors = LettersCurriculum.getDistinctDistractors(targetLetter, 3)
        (distractors + targetLetter).shuffled()
    }

    val spokenQuestion = if (isArabicSection) {
        "أي صورة تبدأ بحرف ال${targetLetter.letterNameAr}؟"
    } else {
        "Which picture starts with letter ${targetLetter.uppercaseSymbol}?"
    }

    LaunchedEffect(targetLetter.id) {
        onSpeakLulu(spokenQuestion, sectionMode.language)
    }

    val activityMeta = LettersActivityId.MATCH_PICTURE

    PlayfulSkyBackground(accentTint = activityMeta.accentColor) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .widthIn(max = 860.dp)
                .align(Alignment.TopCenter)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            LettersActivityTopBar(
                title = activityMeta.title(appLanguage),
                accentColor = activityMeta.accentColor,
                shadowColor = activityMeta.shadowColor,
                isArabic = isArabicApp,
                soundEnabled = soundEnabled,
                isActivityCompleted = isActivityCompleted,
                onBackToHub = onBackToHub,
                onNavigateHome = onNavigateHome,
                onToggleSound = onToggleSound
            )

            // Section Switcher
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Bouncy3DButton(
                    text = "الحروف العربية",
                    onClick = { onSelectSectionMode(LettersSectionMode.ARABIC) },
                    modifier = Modifier.weight(1f),
                    containerColor = if (isArabicSection) activityMeta.accentColor else Color.White,
                    shadowColor = if (isArabicSection) activityMeta.shadowColor else Color(0xFFB0BEC5),
                    contentColor = if (isArabicSection) Color.White else DeepNavyText,
                    fontSize = 15.sp
                )
                Bouncy3DButton(
                    text = "English Letters",
                    onClick = { onSelectSectionMode(LettersSectionMode.ENGLISH) },
                    modifier = Modifier.weight(1f),
                    containerColor = if (!isArabicSection) activityMeta.accentColor else Color.White,
                    shadowColor = if (!isArabicSection) activityMeta.shadowColor else Color(0xFFB0BEC5),
                    contentColor = if (!isArabicSection) Color.White else DeepNavyText,
                    fontSize = 15.sp
                )
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(12.dp, RoundedCornerShape(32.dp)),
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(3.dp, activityMeta.accentColor)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Target Letter Showcase Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            modifier = Modifier
                                .size(104.dp)
                                .clickable { onSpeakLulu(spokenQuestion, sectionMode.language) },
                            shape = RoundedCornerShape(26.dp),
                            color = targetLetter.color,
                            border = BorderStroke(3.dp, Color.White),
                            shadowElevation = 8.dp
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = targetLetter.gridDisplaySymbol,
                                    color = Color.White,
                                    style = MaterialTheme.typography.displayMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = if (isArabicSection) 48.sp else 36.sp
                                    )
                                )
                            }
                        }

                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = if (isArabicSection) {
                                    "أي صورة تبدأ بحرف (${targetLetter.isolatedSymbol}) ؟"
                                } else {
                                    if (isArabicApp) {
                                        "أي صورة تبدأ بحرف (${targetLetter.uppercaseSymbol}) ؟"
                                    } else {
                                        "Which picture begins with (${targetLetter.uppercaseSymbol} ${targetLetter.lowercaseSymbol})?"
                                    }
                                },
                                color = DeepNavyText,
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = if (isArabicApp) 22.sp else 20.sp
                                )
                            )
                            Text(
                                text = if (isArabicApp) {
                                    "اضغطي على الصورة الصحيحة!"
                                } else {
                                    "Tap the cartoon picture that starts with this letter!"
                                },
                                color = SoftSlateText,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }

                    // 4 Cartoon Picture Choice Cards (2x2 Grid)
                    val rows = pictureOptions.chunked(2)
                    rows.forEach { rowItems ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            rowItems.forEach { item ->
                                val isSelected = selectedOptionId == item.id
                                val isTarget = item.id == targetLetter.id
                                val borderCol = when {
                                    isSelected && isTarget -> MeadowGreen
                                    isSelected && !isTarget -> Color(0xFFE53935)
                                    else -> item.color
                                }

                                Surface(
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("match_picture_option_${item.id}")
                                        .shadow(6.dp, RoundedCornerShape(24.dp))
                                        .clickable {
                                            selectedOptionId = item.id
                                            if (isTarget) {
                                                if (isAnswerCorrect != true) {
                                                    starsEarnedInSession++
                                                    if (starsEarnedInSession >= 2) {
                                                        onMarkActivityCompleted()
                                                    }
                                                }
                                                isAnswerCorrect = true
                                                val praise = if (isArabicSection) {
                                                    "رائع! ${targetLetter.wordAr} يبدأ بحرف ال${targetLetter.letterNameAr}!"
                                                } else {
                                                    "Great job! ${targetLetter.uppercaseSymbol} is for ${targetLetter.wordEn}!"
                                                }
                                                onSpeakLulu(praise, sectionMode.language)
                                            } else {
                                                isAnswerCorrect = false
                                                val retry = if (isArabicSection) {
                                                    "حاولي مرة أخرى! ابحثي عن كلمة تبدأ بحرف ال${targetLetter.letterNameAr}."
                                                } else {
                                                    "Try again! Find the picture starting with ${targetLetter.uppercaseSymbol}."
                                                }
                                                onSpeakLulu(retry, sectionMode.language)
                                            }
                                        },
                                    shape = RoundedCornerShape(24.dp),
                                    color = if (isSelected && isTarget) Color(0xFFE8F5E9) else item.cardBgColor,
                                    border = BorderStroke(3.dp, borderCol)
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(14.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Text(
                                            text = item.emoji,
                                            fontSize = 48.sp
                                        )
                                        Text(
                                            text = item.primaryWord,
                                            color = DeepNavyText,
                                            style = MaterialTheme.typography.titleLarge.copy(
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 22.sp
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Celebration & Next Button
                    AnimatedVisibility(
                        visible = isAnswerCorrect != null,
                        enter = fadeIn() + scaleIn(),
                        exit = fadeOut() + scaleOut()
                    ) {
                        val correct = isAnswerCorrect == true
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(22.dp),
                            color = if (correct) Color(0xFFE8F5E9) else Color(0xFFFFF3E0),
                            border = BorderStroke(2.dp, if (correct) MeadowGreen else SunnyYellowDark)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = if (correct) {
                                        if (isArabicSection) {
                                            "رائع! ${targetLetter.isolatedSymbol} ← ${targetLetter.wordAr} ${targetLetter.emoji} ⭐"
                                        } else {
                                            "Awesome! ${targetLetter.uppercaseSymbol} → ${targetLetter.wordEn} ${targetLetter.emoji} ⭐"
                                        }
                                    } else {
                                        if (isArabicApp) "جربي صورة أخرى يا بطلة!" else "Try another picture!"
                                    },
                                    color = DeepNavyText,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    modifier = Modifier.weight(1f)
                                )

                                if (correct) {
                                    Bouncy3DButton(
                                        text = if (isArabicApp) "التالي" else "Next",
                                        onClick = {
                                            currentRound = (currentRound + 1) % roundIndices.size
                                        },
                                        containerColor = MeadowGreen,
                                        shadowColor = MeadowGreenDark,
                                        contentColor = Color.White,
                                        icon = Icons.AutoMirrored.Filled.ArrowForward,
                                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
                                        testTagName = "match_picture_next_btn"
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * ACTIVITY 5: COMPLETE THE MISSING LETTER (أكمل الحرف الناقص)
 * Shows a simple word with a missing initial letter (e.g., _طة for بطة, _pple for Apple)
 * and asks the child to select the missing letter.
 */
@Composable
fun CompleteMissingLetterActivityScreen(
    sectionMode: LettersSectionMode,
    appLanguage: AppLanguage,
    soundEnabled: Boolean,
    isActivityCompleted: Boolean,
    onSelectSectionMode: (LettersSectionMode) -> Unit,
    onBackToHub: () -> Unit,
    onNavigateHome: () -> Unit,
    onToggleSound: () -> Unit,
    onSpeakLulu: (String, AppLanguage) -> Unit,
    onMarkActivityCompleted: () -> Unit
) {
    val isArabicApp = appLanguage == AppLanguage.AR
    val isArabicSection = sectionMode == LettersSectionMode.ARABIC
    val allLetters = LettersCurriculum.lettersForSection(sectionMode)

    val roundIndices = remember(sectionMode) {
        listOf(2, 1, 3, 5, 7, 13, 24).map { it.coerceIn(1, allLetters.size) }
    }

    var currentRound by remember(sectionMode) { mutableIntStateOf(0) }
    var solvedCount by remember(sectionMode) { mutableIntStateOf(0) }
    var selectedLetterId by remember(currentRound, sectionMode) { mutableStateOf<String?>(null) }
    var isSolved by remember(currentRound, sectionMode) { mutableStateOf(false) }

    val targetLetter = allLetters[roundIndices[currentRound % roundIndices.size] - 1]
    val choices = remember(targetLetter.id) {
        val distractors = LettersCurriculum.getDistinctDistractors(targetLetter, 2)
        (distractors + targetLetter).shuffled()
    }

    val spokenPrompt = if (isArabicSection) {
        "ما الحرف الأول الناقص في كلمة ${targetLetter.wordAr}؟"
    } else {
        "Which first letter is missing in the word ${targetLetter.wordEn}?"
    }

    LaunchedEffect(targetLetter.id) {
        onSpeakLulu(spokenPrompt, sectionMode.language)
    }

    val activityMeta = LettersActivityId.MISSING_LETTER

    PlayfulSkyBackground(accentTint = activityMeta.accentColor) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .widthIn(max = 860.dp)
                .align(Alignment.TopCenter)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            LettersActivityTopBar(
                title = activityMeta.title(appLanguage),
                accentColor = activityMeta.accentColor,
                shadowColor = activityMeta.shadowColor,
                isArabic = isArabicApp,
                soundEnabled = soundEnabled,
                isActivityCompleted = isActivityCompleted,
                onBackToHub = onBackToHub,
                onNavigateHome = onNavigateHome,
                onToggleSound = onToggleSound
            )

            // Section Switcher
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Bouncy3DButton(
                    text = "الحروف العربية",
                    onClick = { onSelectSectionMode(LettersSectionMode.ARABIC) },
                    modifier = Modifier.weight(1f),
                    containerColor = if (isArabicSection) activityMeta.accentColor else Color.White,
                    shadowColor = if (isArabicSection) activityMeta.shadowColor else Color(0xFFB0BEC5),
                    contentColor = if (isArabicSection) Color.White else DeepNavyText,
                    fontSize = 15.sp
                )
                Bouncy3DButton(
                    text = "English Letters",
                    onClick = { onSelectSectionMode(LettersSectionMode.ENGLISH) },
                    modifier = Modifier.weight(1f),
                    containerColor = if (!isArabicSection) activityMeta.accentColor else Color.White,
                    shadowColor = if (!isArabicSection) activityMeta.shadowColor else Color(0xFFB0BEC5),
                    contentColor = if (!isArabicSection) Color.White else DeepNavyText,
                    fontSize = 15.sp
                )
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(12.dp, RoundedCornerShape(32.dp)),
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(3.dp, activityMeta.accentColor)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = if (isArabicApp) {
                            "اختاري الحرف الناقص لإكمال الكلمة!"
                        } else {
                            "Pick the missing first letter to complete the word!"
                        },
                        color = DeepNavyText,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = if (isArabicApp) 22.sp else 20.sp
                        ),
                        textAlign = TextAlign.Center
                    )

                    // Cartoon Picture + Missing Letter Word Box (e.g. _طة -> بطة)
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSpeakLulu(spokenPrompt, sectionMode.language) },
                        shape = RoundedCornerShape(28.dp),
                        color = targetLetter.cardBgColor,
                        border = BorderStroke(2.5.dp, targetLetter.color)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(
                                text = targetLetter.emoji,
                                fontSize = 72.sp
                            )

                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = Color.White,
                                border = BorderStroke(2.dp, if (isSolved) MeadowGreen else targetLetter.color)
                            ) {
                                Text(
                                    text = if (isSolved) {
                                        targetLetter.primaryWord
                                    } else {
                                        targetLetter.missingWordDisplay
                                    },
                                    color = if (isSolved) MeadowGreen else DeepNavyText,
                                    style = MaterialTheme.typography.displayMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 44.sp
                                    ),
                                    modifier = Modifier.padding(horizontal = 28.dp, vertical = 10.dp)
                                )
                            }

                            Text(
                                text = targetLetter.secondaryWordMeaning,
                                color = SoftSlateText,
                                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }

                    // 3 Letter Choices
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        choices.forEach { choice ->
                            val isSelected = selectedLetterId == choice.id
                            val isRight = choice.id == targetLetter.id
                            val bg = when {
                                isSelected && isRight -> MeadowGreen
                                isSelected && !isRight -> Color(0xFFE53935)
                                else -> choice.color
                            }

                            Surface(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(96.dp)
                                    .testTag("missing_letter_choice_${choice.id}")
                                    .shadow(8.dp, RoundedCornerShape(24.dp))
                                    .clickable {
                                        selectedLetterId = choice.id
                                        if (isRight) {
                                            if (!isSolved) {
                                                solvedCount++
                                                if (solvedCount >= 2) {
                                                    onMarkActivityCompleted()
                                                }
                                            }
                                            isSolved = true
                                            val praise = if (isArabicSection) {
                                                "ممتاز يا بطلة! ${targetLetter.wordAr} تبدأ بحرف ال${targetLetter.letterNameAr}!"
                                            } else {
                                                "Awesome! ${targetLetter.wordEn} starts with letter ${targetLetter.uppercaseSymbol}!"
                                            }
                                            onSpeakLulu(praise, sectionMode.language)
                                        } else {
                                            val retry = if (isArabicSection) {
                                                "جربي حرفاً آخر يا بطلة! استمعي إلى أول صوت في كلمة ${targetLetter.wordAr}."
                                            } else {
                                                "Try another letter! Listen to the first sound in ${targetLetter.wordEn}."
                                            }
                                            onSpeakLulu(retry, sectionMode.language)
                                        }
                                    },
                                shape = RoundedCornerShape(24.dp),
                                color = bg,
                                border = BorderStroke(3.dp, Color.White)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = if (isArabicSection) choice.isolatedSymbol else choice.uppercaseSymbol,
                                        color = Color.White,
                                        style = MaterialTheme.typography.displayMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 42.sp
                                        )
                                    )
                                }
                            }
                        }
                    }

                    // Solved Celebration & Next Word Button
                    AnimatedVisibility(
                        visible = isSolved,
                        enter = fadeIn() + scaleIn(),
                        exit = fadeOut() + scaleOut()
                    ) {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(22.dp),
                            color = Color(0xFFE8F5E9),
                            border = BorderStroke(2.dp, MeadowGreen)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = if (isArabicApp) {
                                        "أحسنتِ! الكلمة الكاملة هي: ${targetLetter.primaryWord} ⭐"
                                    } else {
                                        "Great job! The complete word is: ${targetLetter.primaryWord} ⭐"
                                    },
                                    color = DeepNavyText,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    modifier = Modifier.weight(1f)
                                )

                                Bouncy3DButton(
                                    text = if (isArabicApp) "الكلمة التالية" else "Next Word",
                                    onClick = {
                                        currentRound = (currentRound + 1) % roundIndices.size
                                    },
                                    containerColor = MeadowGreen,
                                    shadowColor = MeadowGreenDark,
                                    contentColor = Color.White,
                                    icon = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
                                    testTagName = "missing_letter_next_btn"
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
 * ACTIVITY 6: LETTER QUIZ (مسابقة الحروف)
 * Interactive questions covering the letters learned with colorful illustrations and 3 answer choices.
 */
@Composable
fun LetterQuizActivityScreen(
    sectionMode: LettersSectionMode,
    appLanguage: AppLanguage,
    soundEnabled: Boolean,
    isActivityCompleted: Boolean,
    onSelectSectionMode: (LettersSectionMode) -> Unit,
    onBackToHub: () -> Unit,
    onNavigateHome: () -> Unit,
    onToggleSound: () -> Unit,
    onSpeakLulu: (String, AppLanguage) -> Unit,
    onMarkActivityCompleted: () -> Unit
) {
    val isArabicApp = appLanguage == AppLanguage.AR
    val isArabicSection = sectionMode == LettersSectionMode.ARABIC
    val questions = LettersQuizBank.questionsFor(sectionMode)

    var currentQuestionIdx by remember(sectionMode) { mutableIntStateOf(0) }
    var score by remember(sectionMode) { mutableIntStateOf(0) }
    var selectedOption by remember(currentQuestionIdx, sectionMode) { mutableStateOf<String?>(null) }
    var quizFinished by remember(sectionMode) { mutableStateOf(false) }

    val currentQuestion = questions[currentQuestionIdx.coerceIn(0, questions.lastIndex)]
    val activityMeta = LettersActivityId.LETTER_QUIZ

    LaunchedEffect(currentQuestion.id, sectionMode, quizFinished) {
        if (!quizFinished) {
            val spoken = if (isArabicSection) currentQuestion.spokenAr else currentQuestion.spokenEn
            onSpeakLulu(spoken, sectionMode.language)
        }
    }

    PlayfulSkyBackground(accentTint = activityMeta.accentColor) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .widthIn(max = 860.dp)
                .align(Alignment.TopCenter)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            LettersActivityTopBar(
                title = activityMeta.title(appLanguage),
                accentColor = activityMeta.accentColor,
                shadowColor = activityMeta.shadowColor,
                isArabic = isArabicApp,
                soundEnabled = soundEnabled,
                isActivityCompleted = isActivityCompleted,
                onBackToHub = onBackToHub,
                onNavigateHome = onNavigateHome,
                onToggleSound = onToggleSound
            )

            // Section Switcher
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Bouncy3DButton(
                    text = "مسابقة الحروف العربية",
                    onClick = { onSelectSectionMode(LettersSectionMode.ARABIC) },
                    modifier = Modifier.weight(1f),
                    containerColor = if (isArabicSection) activityMeta.accentColor else Color.White,
                    shadowColor = if (isArabicSection) activityMeta.shadowColor else Color(0xFFB0BEC5),
                    contentColor = if (isArabicSection) Color.White else DeepNavyText,
                    fontSize = 15.sp
                )
                Bouncy3DButton(
                    text = "English Letters Quiz",
                    onClick = { onSelectSectionMode(LettersSectionMode.ENGLISH) },
                    modifier = Modifier.weight(1f),
                    containerColor = if (!isArabicSection) activityMeta.accentColor else Color.White,
                    shadowColor = if (!isArabicSection) activityMeta.shadowColor else Color(0xFFB0BEC5),
                    contentColor = if (!isArabicSection) Color.White else DeepNavyText,
                    fontSize = 15.sp
                )
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(12.dp, RoundedCornerShape(32.dp)),
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(3.dp, activityMeta.accentColor)
            ) {
                if (quizFinished) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.CheckCircle,
                            contentDescription = null,
                            tint = MeadowGreen,
                            modifier = Modifier.size(64.dp)
                        )
                        Text(
                            text = if (isArabicApp) {
                                "ممتاز يا بطلة! أكملتِ مسابقة الحروف بنجاح! 🎉"
                            } else {
                                "Amazing Superstar! You completed the Letter Quiz! 🎉"
                            },
                            color = DeepNavyText,
                            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = if (isArabicApp) {
                                "النتيجة: $score من ${questions.size} نجوم ⭐"
                            } else {
                                "Score: $score / ${questions.size} Stars ⭐"
                            },
                            color = activityMeta.accentColor,
                            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                        )

                        Bouncy3DButton(
                            text = if (isArabicApp) "العب مرة أخرى" else "Play Again",
                            onClick = {
                                currentQuestionIdx = 0
                                score = 0
                                selectedOption = null
                                quizFinished = false
                            },
                            containerColor = SunnyYellow,
                            shadowColor = SunnyYellowDark,
                            contentColor = DeepNavyText,
                            icon = Icons.Filled.Refresh,
                            testTagName = "restart_letter_quiz_btn"
                        )
                    }
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (isArabicApp) {
                                    "السؤال ${currentQuestionIdx + 1} من ${questions.size}"
                                } else {
                                    "Question ${currentQuestionIdx + 1} of ${questions.size}"
                                },
                                color = SoftSlateText,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )

                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = Color(0xFFFFF8E1),
                                border = BorderStroke(1.5.dp, StarGoldBorder)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Star,
                                        contentDescription = null,
                                        tint = StarGoldBorder,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Text(
                                        text = "$score / ${questions.size}",
                                        color = DeepNavyText,
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                    )
                                }
                            }
                        }

                        // Visual Illustration & Question Prompt
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    val spoken = if (isArabicSection) currentQuestion.spokenAr else currentQuestion.spokenEn
                                    onSpeakLulu(spoken, sectionMode.language)
                                },
                            shape = RoundedCornerShape(26.dp),
                            color = activityMeta.cardBgColor,
                            border = BorderStroke(2.dp, activityMeta.accentColor)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(18.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = currentQuestion.visualEmoji,
                                    fontSize = 62.sp
                                )
                                Text(
                                    text = currentQuestion.visualCaption,
                                    color = activityMeta.accentColor,
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 24.sp
                                    )
                                )
                                Text(
                                    text = if (isArabicApp) currentQuestion.promptAr else currentQuestion.promptEn,
                                    color = DeepNavyText,
                                    style = MaterialTheme.typography.headlineSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = if (isArabicApp) 22.sp else 20.sp
                                    ),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }

                        // 3 Answer Choices
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            currentQuestion.options.forEachIndexed { idx, option ->
                                val isSelected = selectedOption == option
                                val isCorrect = option == currentQuestion.correctOption
                                val containerCol = when {
                                    isSelected && isCorrect -> MeadowGreen
                                    isSelected && !isCorrect -> Color(0xFFE53935)
                                    else -> Color.White
                                }
                                val textCol = if (isSelected) Color.White else DeepNavyText

                                Surface(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(88.dp)
                                        .testTag("letter_quiz_option_$idx")
                                        .shadow(6.dp, RoundedCornerShape(22.dp))
                                        .clickable {
                                            selectedOption = option
                                            if (isCorrect) {
                                                score++
                                                val praise = if (isArabicSection) {
                                                    "إجابة صحيحة! ممتاز يا بطلة!"
                                                } else {
                                                    "Correct answer! Great job!"
                                                }
                                                onSpeakLulu(praise, sectionMode.language)
                                            } else {
                                                val retry = if (isArabicSection) {
                                                    "حاولي مرة أخرى يا بطلة!"
                                                } else {
                                                    "Try again superstar!"
                                                }
                                                onSpeakLulu(retry, sectionMode.language)
                                            }
                                        },
                                    shape = RoundedCornerShape(22.dp),
                                    color = containerCol,
                                    border = BorderStroke(2.5.dp, activityMeta.accentColor)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            text = option,
                                            color = textCol,
                                            style = MaterialTheme.typography.headlineSmall.copy(
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 24.sp
                                            ),
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            }
                        }

                        if (selectedOption == currentQuestion.correctOption) {
                            Bouncy3DButton(
                                text = if (currentQuestionIdx < questions.lastIndex) {
                                    if (isArabicApp) "السؤال التالي" else "Next Question"
                                } else {
                                    if (isArabicApp) "إنهاء المسابقة 🌟" else "Finish Quiz 🌟"
                                },
                                onClick = {
                                    if (currentQuestionIdx < questions.lastIndex) {
                                        currentQuestionIdx++
                                    } else {
                                        quizFinished = true
                                        onMarkActivityCompleted()
                                    }
                                },
                                containerColor = MeadowGreen,
                                shadowColor = MeadowGreenDark,
                                contentColor = Color.White,
                                icon = Icons.AutoMirrored.Filled.ArrowForward,
                                testTagName = "letter_quiz_next_btn"
                            )
                        }
                    }
                }
            }
        }
    }
}
