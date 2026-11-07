package com.example.ui.screens.numbers

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
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
import com.example.model.CountableObjectType
import com.example.model.NumberQuizBank
import com.example.model.NumbersCurriculum
import com.example.ui.components.Bouncy3DButton
import com.example.ui.components.Bouncy3DIconButton
import com.example.ui.components.PlayfulSkyBackground
import com.example.ui.components.QuantityObjectsGrid
import com.example.ui.theme.DeepNavyText
import com.example.ui.theme.LuluPink
import com.example.ui.theme.LuluPinkDark
import com.example.ui.theme.MagicPurple
import com.example.ui.theme.MagicPurpleDark
import com.example.ui.theme.MeadowGreen
import com.example.ui.theme.MeadowGreenDark
import com.example.ui.theme.NumbersAccent
import com.example.ui.theme.NumbersShadow
import com.example.ui.theme.SkyCyanDark
import com.example.ui.theme.SoftSlateText
import com.example.ui.theme.StarGoldBorder
import com.example.ui.theme.SunnyYellow
import com.example.ui.theme.SunnyYellowDark

private data class CountingRound(
    val count: Int,
    val objectType: CountableObjectType,
    val options: List<Int>,
    val praiseAr: String,
    val praiseEn: String
)

private val countingRounds = listOf(
    CountingRound(
        count = 5,
        objectType = CountableObjectType.STAR,
        options = listOf(3, 5, 7),
        praiseAr = "رائع! لقد عددتِ خمس نجوم!",
        praiseEn = "Wonderful! You counted five stars!"
    ),
    CountingRound(
        count = 3,
        objectType = CountableObjectType.APPLE,
        options = listOf(2, 3, 5),
        praiseAr = "أحسنتِ! لقد عددتِ ثلاث تفاحات!",
        praiseEn = "Great job! You counted three apples!"
    ),
    CountingRound(
        count = 4,
        objectType = CountableObjectType.FLOWER,
        options = listOf(4, 6, 8),
        praiseAr = "ممتاز! هذه أربع زهور جميلة!",
        praiseEn = "Awesome! Those are four lovely flowers!"
    ),
    CountingRound(
        count = 6,
        objectType = CountableObjectType.BALLOON,
        options = listOf(4, 6, 9),
        praiseAr = "رائع! ستة بالونات ملوّنة!",
        praiseEn = "Wonderful! Six colorful balloons!"
    ),
    CountingRound(
        count = 7,
        objectType = CountableObjectType.ANIMAL,
        options = listOf(5, 7, 10),
        praiseAr = "أحسنتِ يا بطلتي! سبعة حيوانات لطيفة!",
        praiseEn = "Amazing job! Seven cute animals!"
    )
)

/**
 * ACTIVITY 3: COUNT THE OBJECTS
 * - Displays attractive illustrations (Stars, Apples, Flowers, Balloons, Animals)
 * - Asks the child to count the objects (e.g. "كم نجمة ترين؟")
 * - Provides 3 possible answer buttons
 * - Celebrates and awards a star when correct; Lulu gently encourages retry when incorrect
 */
@Composable
fun CountObjectsActivityScreen(
    appLanguage: AppLanguage,
    numeralLanguage: AppLanguage,
    soundEnabled: Boolean,
    isCompleted: Boolean,
    onBackToHub: () -> Unit,
    onNavigateHome: () -> Unit,
    onToggleSound: () -> Unit,
    onSpeakLulu: (String, AppLanguage) -> Unit,
    onMarkCompleted: () -> Unit
) {
    val isArabic = appLanguage == AppLanguage.AR
    var roundIndex by remember { mutableIntStateOf(0) }
    var answeredCorrectly by remember(roundIndex) { mutableStateOf(false) }
    var feedbackMessage by remember(roundIndex) { mutableStateOf<String?>(null) }

    val round = countingRounds[roundIndex % countingRounds.size]
    val questionText = if (isArabic) round.objectType.questionAr else round.objectType.questionEn

    LaunchedEffect(roundIndex, appLanguage) {
        onSpeakLulu(questionText, appLanguage)
    }

    PlayfulSkyBackground(accentTint = MeadowGreen) {
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
            NumbersActivityTopBar(
                title = if (isArabic) "عدّ الأشياء (${roundIndex + 1}/${countingRounds.size})" else "Count Objects (${roundIndex + 1}/${countingRounds.size})",
                isArabic = isArabic,
                soundEnabled = soundEnabled,
                isActivityCompleted = isCompleted,
                onBackToHub = onBackToHub,
                onNavigateHome = onNavigateHome,
                onToggleSound = onToggleSound
            )

            // Question Card with Lulu & Speaker Button
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(26.dp),
                color = Color.White,
                border = BorderStroke(2.5.dp, MeadowGreen),
                shadowElevation = 6.dp
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
                            modifier = Modifier.size(64.dp),
                            shape = CircleShape,
                            border = BorderStroke(2.5.dp, SunnyYellow)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.img_lulu_mascot_1794072207514),
                                contentDescription = "Lulu",
                                contentScale = ContentScale.Crop
                            )
                        }
                        Column {
                            Text(
                                text = questionText,
                                color = DeepNavyText,
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = if (isArabic) 24.sp else 21.sp
                                )
                            )
                            Text(
                                text = if (isArabic) "اضغطي على الصور لعدّها ثم اختاري الإجابة!" else "Tap the pictures to count them, then pick the answer!",
                                color = SoftSlateText,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }

                    Bouncy3DIconButton(
                        icon = Icons.AutoMirrored.Filled.VolumeUp,
                        contentDescription = "Speak Question",
                        onClick = { onSpeakLulu(questionText, appLanguage) },
                        containerColor = SunnyYellow,
                        shadowColor = SunnyYellowDark,
                        iconTint = DeepNavyText
                    )
                }
            }

            // Objects Display Stage
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(10.dp, RoundedCornerShape(30.dp)),
                shape = RoundedCornerShape(30.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFDF7)),
                border = BorderStroke(3.dp, SunnyYellowDark)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(22.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    QuantityObjectsGrid(
                        count = round.count,
                        objectType = round.objectType,
                        numeralLanguage = numeralLanguage,
                        itemSize = 76.dp,
                        onObjectTapped = { num ->
                            val word = NumbersCurriculum.byValue(num).word(numeralLanguage)
                            onSpeakLulu(word, numeralLanguage)
                        }
                    )
                }
            }

            // Three Possible Answer Options
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                round.options.forEach { optionValue ->
                    val optionItem = NumbersCurriculum.byValue(optionValue)
                    val isRightOption = optionValue == round.count
                    val containerCol = when {
                        answeredCorrectly && isRightOption -> MeadowGreen
                        else -> optionItem.color
                    }
                    val shadowCol = when {
                        answeredCorrectly && isRightOption -> MeadowGreenDark
                        else -> optionItem.shadowColor
                    }

                    Bouncy3DButton(
                        text = "${optionItem.symbol(numeralLanguage)}  (${optionItem.word(numeralLanguage)})",
                        onClick = {
                            if (optionValue == round.count) {
                                answeredCorrectly = true
                                val praise = if (isArabic) round.praiseAr else round.praiseEn
                                feedbackMessage = praise
                                onSpeakLulu(praise, appLanguage)
                                onMarkCompleted()
                            } else {
                                val retryMsg = if (isArabic) {
                                    "لا بأس، حاولي مرة أخرى يا صديقتي! هيا نعدّ معاً."
                                } else {
                                    "Try again, my friend! Let's count them carefully."
                                }
                                feedbackMessage = retryMsg
                                onSpeakLulu(retryMsg, appLanguage)
                            }
                        },
                        modifier = Modifier.weight(1f),
                        containerColor = containerCol,
                        shadowColor = shadowCol,
                        contentColor = Color.White,
                        fontSize = if (isArabic) 20.sp else 18.sp,
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 16.dp),
                        testTagName = "count_option_$optionValue"
                    )
                }
            }

            // Feedback & Next Round Banner
            AnimatedVisibility(
                visible = feedbackMessage != null,
                enter = fadeIn() + scaleIn(),
                exit = fadeOut() + scaleOut()
            ) {
                feedbackMessage?.let { msg ->
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(24.dp),
                        color = if (answeredCorrectly) Color(0xFFE8F5E9) else Color(0xFFFFF8E1),
                        border = BorderStroke(
                            2.5.dp,
                            if (answeredCorrectly) MeadowGreen else SunnyYellowDark
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = if (answeredCorrectly) Icons.Filled.Star else Icons.Filled.CheckCircle,
                                    contentDescription = null,
                                    tint = if (answeredCorrectly) StarGoldBorder else SunnyYellowDark,
                                    modifier = Modifier.size(30.dp)
                                )
                                Text(
                                    text = msg,
                                    color = DeepNavyText,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = if (isArabic) 19.sp else 17.sp
                                    ),
                                    textAlign = TextAlign.Center
                                )
                            }

                            if (answeredCorrectly) {
                                Bouncy3DButton(
                                    text = if (isArabic) "السؤال التالي ⭐" else "Next Question ⭐",
                                    onClick = {
                                        roundIndex = (roundIndex + 1) % countingRounds.size
                                    },
                                    containerColor = MeadowGreen,
                                    shadowColor = MeadowGreenDark,
                                    contentColor = Color.White,
                                    icon = Icons.AutoMirrored.Filled.ArrowForward,
                                    testTagName = "count_next_round_btn"
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
 * ACTIVITY 4: MATCH THE NUMBER
 * - Interactive tap-to-match game (works through tapping, not dragging only)
 * - Displays numbers on one side and groups of illustrated objects on the other
 * - Example: Number 4 -> Four flowers
 */
private data class MatchPairItem(
    val number: Int,
    val objectType: CountableObjectType
)

@Composable
fun MatchNumberActivityScreen(
    appLanguage: AppLanguage,
    numeralLanguage: AppLanguage,
    soundEnabled: Boolean,
    isCompleted: Boolean,
    onBackToHub: () -> Unit,
    onNavigateHome: () -> Unit,
    onToggleSound: () -> Unit,
    onSpeakLulu: (String, AppLanguage) -> Unit,
    onMarkCompleted: () -> Unit
) {
    val isArabic = appLanguage == AppLanguage.AR

    val pairs = remember {
        listOf(
            MatchPairItem(2, CountableObjectType.STAR),
            MatchPairItem(3, CountableObjectType.APPLE),
            MatchPairItem(4, CountableObjectType.FLOWER),
            MatchPairItem(5, CountableObjectType.BALLOON)
        )
    }
    var rightColumnOrder by remember { mutableStateOf(listOf(4, 2, 5, 3)) }
    var selectedNumber by remember { mutableStateOf<Int?>(null) }
    var selectedGroupCount by remember { mutableStateOf<Int?>(null) }
    var matchedNumbers by remember { mutableStateOf<Set<Int>>(emptySet()) }
    var luluFeedback by remember {
        mutableStateOf(
            if (isArabic) "اضغطي على الرقم ثم اضغطي على المجموعة التي تطابقه!" else "Tap a number, then tap the matching group of objects!"
        )
    }

    LaunchedEffect(Unit) {
        onSpeakLulu(luluFeedback, appLanguage)
    }

    fun checkPairMatch(num: Int, groupCount: Int) {
        if (num == groupCount) {
            val updated = matchedNumbers + num
            matchedNumbers = updated
            selectedNumber = null
            selectedGroupCount = null
            val word = NumbersCurriculum.byValue(num).word(numeralLanguage)
            if (updated.size == pairs.size) {
                onMarkCompleted()
                val winMsg = if (isArabic) {
                    "رائع يا بطلتي! لقد طابقتِ جميع الأرقام بنجاح وحصلتِ على نجمة!"
                } else {
                    "Wonderful! You matched all the numbers and earned a star!"
                }
                luluFeedback = winMsg
                onSpeakLulu(winMsg, appLanguage)
            } else {
                val okMsg = if (isArabic) {
                    "أحسنتِ! هذا هو الرقم $word!"
                } else {
                    "Great job! That matches number $word!"
                }
                luluFeedback = okMsg
                onSpeakLulu(okMsg, appLanguage)
            }
        } else {
            selectedNumber = null
            selectedGroupCount = null
            val retryMsg = if (isArabic) {
                "لا بأس، حاولي مرة أخرى يا صديقتي!"
            } else {
                "Try again, my friend!"
            }
            luluFeedback = retryMsg
            onSpeakLulu(retryMsg, appLanguage)
        }
    }

    PlayfulSkyBackground(accentTint = MagicPurple) {
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
            NumbersActivityTopBar(
                title = if (isArabic) "طابق الرقم بالعدد" else "Match the Number",
                isArabic = isArabic,
                soundEnabled = soundEnabled,
                isActivityCompleted = isCompleted,
                onBackToHub = onBackToHub,
                onNavigateHome = onNavigateHome,
                onToggleSound = onToggleSound
            )

            // Lulu Guidance Bar
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                color = Color.White,
                border = BorderStroke(2.dp, MagicPurple)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = luluFeedback,
                        color = DeepNavyText,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = if (isArabic) 18.sp else 16.sp
                        ),
                        modifier = Modifier.weight(1f)
                    )

                    Bouncy3DIconButton(
                        icon = Icons.Filled.Refresh,
                        contentDescription = "Reset",
                        onClick = {
                            matchedNumbers = emptySet()
                            selectedNumber = null
                            selectedGroupCount = null
                            rightColumnOrder = rightColumnOrder.shuffled()
                        },
                        containerColor = SunnyYellow,
                        shadowColor = SunnyYellowDark,
                        iconTint = DeepNavyText
                    )
                }
            }

            // Matching Columns: Left = Numbers, Right = Illustrated Object Groups
            pairs.indices.forEach { rowIdx ->
                val leftPair = pairs[rowIdx]
                val rightCount = rightColumnOrder[rowIdx]
                val rightPair = pairs.first { it.number == rightCount }

                val leftMatched = leftPair.number in matchedNumbers
                val rightMatched = rightPair.number in matchedNumbers
                val leftSelected = selectedNumber == leftPair.number
                val rightSelected = selectedGroupCount == rightPair.number

                val leftLesson = NumbersCurriculum.byValue(leftPair.number)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Number Card
                    Surface(
                        modifier = Modifier
                            .weight(0.38f)
                            .height(104.dp)
                            .testTag("match_num_${leftPair.number}")
                            .clickable(enabled = !leftMatched) {
                                val currentGroup = selectedGroupCount
                                if (currentGroup != null) {
                                    checkPairMatch(leftPair.number, currentGroup)
                                } else {
                                    selectedNumber = leftPair.number
                                    onSpeakLulu(leftLesson.word(numeralLanguage), numeralLanguage)
                                }
                            },
                        shape = RoundedCornerShape(24.dp),
                        color = when {
                            leftMatched -> Color(0xFFE8F5E9)
                            leftSelected -> SunnyYellow
                            else -> Color.White
                        },
                        border = BorderStroke(
                            width = if (leftSelected || leftMatched) 3.5.dp else 2.dp,
                            color = when {
                                leftMatched -> MeadowGreen
                                leftSelected -> NumbersAccent
                                else -> leftLesson.color
                            }
                        ),
                        shadowElevation = 5.dp
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = leftLesson.symbol(numeralLanguage),
                                color = if (leftMatched) MeadowGreenDark else leftLesson.color,
                                style = MaterialTheme.typography.displayMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 36.sp
                                )
                            )
                            Text(
                                text = leftLesson.word(numeralLanguage),
                                color = DeepNavyText,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }

                    // Object Quantity Group Card
                    Surface(
                        modifier = Modifier
                            .weight(0.62f)
                            .height(104.dp)
                            .testTag("match_group_${rightPair.number}")
                            .clickable(enabled = !rightMatched) {
                                val currentNum = selectedNumber
                                if (currentNum != null) {
                                    checkPairMatch(currentNum, rightPair.number)
                                } else {
                                    selectedGroupCount = rightPair.number
                                }
                            },
                        shape = RoundedCornerShape(24.dp),
                        color = when {
                            rightMatched -> Color(0xFFE8F5E9)
                            rightSelected -> Color(0xFFFFF8E1)
                            else -> Color.White
                        },
                        border = BorderStroke(
                            width = if (rightSelected || rightMatched) 3.5.dp else 2.dp,
                            color = when {
                                rightMatched -> MeadowGreen
                                rightSelected -> NumbersAccent
                                else -> MagicPurple.copy(alpha = 0.5f)
                            }
                        ),
                        shadowElevation = 5.dp
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            QuantityObjectsGrid(
                                count = rightPair.number,
                                objectType = rightPair.objectType,
                                numeralLanguage = numeralLanguage,
                                itemSize = 42.dp,
                                showCountBadgeOnTap = false
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * ACTIVITY 5: NUMBER QUIZ
 * - Collection of interactive questions (Which number comes after 3?, Find number 7, How many apples?, Which group has 5?)
 * - Three answer options
 * - Immediate feedback and score tracking
 */
@Composable
fun NumberQuizActivityScreen(
    appLanguage: AppLanguage,
    numeralLanguage: AppLanguage,
    soundEnabled: Boolean,
    isCompleted: Boolean,
    onBackToHub: () -> Unit,
    onNavigateHome: () -> Unit,
    onToggleSound: () -> Unit,
    onSpeakLulu: (String, AppLanguage) -> Unit,
    onMarkCompleted: () -> Unit
) {
    val isArabic = appLanguage == AppLanguage.AR
    val questions = NumberQuizBank.questions

    var currentIndex by remember { mutableIntStateOf(0) }
    var score by remember { mutableIntStateOf(0) }
    var answeredCurrent by remember(currentIndex) { mutableStateOf(false) }
    var quizFinished by remember { mutableStateOf(false) }
    var feedbackText by remember(currentIndex) { mutableStateOf<String?>(null) }

    val question = questions[currentIndex]

    LaunchedEffect(currentIndex, quizFinished, appLanguage) {
        if (!quizFinished) {
            onSpeakLulu(question.spoken(appLanguage), appLanguage)
        }
    }

    PlayfulSkyBackground(accentTint = LuluPink) {
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
            NumbersActivityTopBar(
                title = if (isArabic) "مسابقة الأرقام (النتيجة: $score/${questions.size})" else "Number Quiz (Score: $score/${questions.size})",
                isArabic = isArabic,
                soundEnabled = soundEnabled,
                isActivityCompleted = isCompleted,
                onBackToHub = onBackToHub,
                onNavigateHome = onNavigateHome,
                onToggleSound = onToggleSound
            )

            if (quizFinished) {
                // Quiz Summary Celebration Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(30.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(3.dp, StarGoldBorder)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.EmojiEvents,
                            contentDescription = null,
                            tint = StarGoldBorder,
                            modifier = Modifier.size(68.dp)
                        )
                        Text(
                            text = if (isArabic) "أحسنتِ يا بطلتي! أنهيتِ المسابقة!" else "Wonderful Job! Quiz Completed!",
                            color = DeepNavyText,
                            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = if (isArabic) "نتيجتكِ: $score من ${questions.size} ⭐" else "Your Score: $score / ${questions.size} ⭐",
                            color = LuluPinkDark,
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                        )
                        Bouncy3DButton(
                            text = if (isArabic) "العب المسابقة مرة أخرى" else "Play Quiz Again",
                            onClick = {
                                currentIndex = 0
                                score = 0
                                quizFinished = false
                            },
                            containerColor = MeadowGreen,
                            shadowColor = MeadowGreenDark,
                            contentColor = Color.White,
                            icon = Icons.Filled.Refresh
                        )
                    }
                }
            } else {
                // Question Prompt Card
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(28.dp),
                    color = Color.White,
                    border = BorderStroke(2.5.dp, LuluPink),
                    shadowElevation = 6.dp
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = LuluPink
                            ) {
                                Text(
                                    text = if (isArabic) "سؤال ${currentIndex + 1} من ${questions.size}" else "Question ${currentIndex + 1} of ${questions.size}",
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                                    color = Color.White,
                                    style = MaterialTheme.typography.labelLarge
                                )
                            }

                            Bouncy3DIconButton(
                                icon = Icons.AutoMirrored.Filled.VolumeUp,
                                contentDescription = "Speak Question",
                                onClick = { onSpeakLulu(question.spoken(appLanguage), appLanguage) },
                                containerColor = SunnyYellow,
                                shadowColor = SunnyYellowDark,
                                iconTint = DeepNavyText
                            )
                        }

                        Text(
                            text = question.prompt(appLanguage),
                            color = DeepNavyText,
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = if (isArabic) 25.sp else 22.sp
                            ),
                            textAlign = TextAlign.Center
                        )

                        if (question.visualCount != null && question.visualObject != null) {
                            QuantityObjectsGrid(
                                count = question.visualCount,
                                objectType = question.visualObject,
                                numeralLanguage = numeralLanguage,
                                itemSize = 64.dp
                            )
                        }
                    }
                }

                // 3 Options
                if (question.optionIsGroupQuantity && question.visualObject != null) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        question.options.forEach { optVal ->
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        if (optVal == question.correctValue) {
                                            if (!answeredCurrent) {
                                                score += 1
                                                answeredCurrent = true
                                            }
                                            val praise = if (isArabic) "إجابة صحيحة! أحسنتِ!" else "Correct answer! Great job!"
                                            feedbackText = praise
                                            onSpeakLulu(praise, appLanguage)
                                        } else {
                                            val retry = if (isArabic) "عدّي النجوم مرة أخرى يا صديقتي!" else "Count the stars carefully and try again!"
                                            feedbackText = retry
                                            onSpeakLulu(retry, appLanguage)
                                        }
                                    },
                                shape = RoundedCornerShape(24.dp),
                                color = Color.White,
                                border = BorderStroke(2.5.dp, MagicPurple),
                                shadowElevation = 4.dp
                            ) {
                                Box(modifier = Modifier.padding(14.dp)) {
                                    QuantityObjectsGrid(
                                        count = optVal,
                                        objectType = question.visualObject,
                                        numeralLanguage = numeralLanguage,
                                        itemSize = 48.dp,
                                        showCountBadgeOnTap = false
                                    )
                                }
                            }
                        }
                    }
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        question.options.forEach { optVal ->
                            val item = NumbersCurriculum.byValue(optVal)
                            Bouncy3DButton(
                                text = "${item.symbol(numeralLanguage)} (${item.word(numeralLanguage)})",
                                onClick = {
                                    if (optVal == question.correctValue) {
                                        if (!answeredCurrent) {
                                            score += 1
                                            answeredCurrent = true
                                        }
                                        val praise = if (isArabic) {
                                            "أحسنتِ! الإجابة هي الرقم ${item.wordAr}!"
                                        } else {
                                            "Great job! The answer is ${item.wordEn}!"
                                        }
                                        feedbackText = praise
                                        onSpeakLulu(praise, appLanguage)
                                    } else {
                                        val retry = if (isArabic) {
                                            "لا بأس، حاولي مرة أخرى يا صديقتي!"
                                        } else {
                                            "Try again, my friend!"
                                        }
                                        feedbackText = retry
                                        onSpeakLulu(retry, appLanguage)
                                    }
                                },
                                modifier = Modifier.weight(1f),
                                containerColor = item.color,
                                shadowColor = item.shadowColor,
                                contentColor = Color.White,
                                fontSize = if (isArabic) 20.sp else 18.sp,
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 16.dp)
                            )
                        }
                    }
                }

                // Feedback & Next Question Button
                feedbackText?.let { msg ->
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(22.dp),
                        color = if (answeredCurrent) Color(0xFFE8F5E9) else Color(0xFFFFF8E1),
                        border = BorderStroke(2.dp, if (answeredCurrent) MeadowGreen else SunnyYellowDark)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(
                                text = msg,
                                color = DeepNavyText,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                textAlign = TextAlign.Center
                            )
                            if (answeredCurrent) {
                                Bouncy3DButton(
                                    text = if (currentIndex < questions.lastIndex) {
                                        if (isArabic) "السؤال التالي" else "Next Question"
                                    } else {
                                        if (isArabic) "إنهاء المسابقة واستلام النجمة ⭐" else "Finish Quiz & Claim Star ⭐"
                                    },
                                    onClick = {
                                        if (currentIndex < questions.lastIndex) {
                                            currentIndex += 1
                                        } else {
                                            quizFinished = true
                                            onMarkCompleted()
                                        }
                                    },
                                    containerColor = MeadowGreen,
                                    shadowColor = MeadowGreenDark,
                                    contentColor = Color.White,
                                    icon = Icons.AutoMirrored.Filled.ArrowForward
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
 * ACTIVITY 6: NUMBER ORDER
 * - Displays numbers 1 to 10 in random order
 * - Asks the child to arrange them in order from 1 to 10 using a simple touch-friendly interaction
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NumberOrderActivityScreen(
    appLanguage: AppLanguage,
    numeralLanguage: AppLanguage,
    soundEnabled: Boolean,
    isCompleted: Boolean,
    onBackToHub: () -> Unit,
    onNavigateHome: () -> Unit,
    onToggleSound: () -> Unit,
    onSpeakLulu: (String, AppLanguage) -> Unit,
    onMarkCompleted: () -> Unit
) {
    val isArabic = appLanguage == AppLanguage.AR

    var shuffledPool by remember {
        mutableStateOf((1..10).toList().shuffled())
    }
    var nextExpectedNumber by remember { mutableIntStateOf(1) }
    var statusText by remember(appLanguage) {
        mutableStateOf(
            if (isArabic) "رتّبي الأرقام بالترتيب من ١ إلى ١٠! اضغطي على الرقم ١ للبدء." else "Arrange the numbers from 1 to 10! Tap number 1 to start."
        )
    }

    LaunchedEffect(Unit) {
        onSpeakLulu(statusText, appLanguage)
    }

    PlayfulSkyBackground(accentTint = SkyCyanDark) {
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
            NumbersActivityTopBar(
                title = if (isArabic) "ترتيب الأرقام (١ إلى ١٠)" else "Number Order (1 to 10)",
                isArabic = isArabic,
                soundEnabled = soundEnabled,
                isActivityCompleted = isCompleted,
                onBackToHub = onBackToHub,
                onNavigateHome = onNavigateHome,
                onToggleSound = onToggleSound
            )

            // Lulu Guidance Card
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                color = Color.White,
                border = BorderStroke(2.5.dp, SkyCyanDark)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = statusText,
                        color = DeepNavyText,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = if (isArabic) 19.sp else 17.sp
                        ),
                        modifier = Modifier.weight(1f)
                    )

                    Bouncy3DIconButton(
                        icon = Icons.Filled.Refresh,
                        contentDescription = "Restart",
                        onClick = {
                            shuffledPool = (1..10).toList().shuffled()
                            nextExpectedNumber = 1
                            statusText = if (isArabic) {
                                "هيا نرتّب الأرقام من ١ إلى ١٠ من جديد!"
                            } else {
                                "Let's arrange the numbers from 1 to 10 again!"
                            }
                            onSpeakLulu(statusText, appLanguage)
                        },
                        containerColor = SunnyYellow,
                        shadowColor = SunnyYellowDark,
                        iconTint = DeepNavyText
                    )
                }
            }

            // Ordered Train Slots (1 to 10)
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                color = Color(0xFFE8F5E9),
                border = BorderStroke(3.dp, MeadowGreen)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = if (isArabic) "🚂 قطار الأرقام المرتّبة (١ ← ١٠)" else "🚂 Ordered Number Train (1 → 10)",
                        color = DeepNavyText,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )

                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        maxItemsInEachRow = 5
                    ) {
                        (1..10).forEach { slotNum ->
                            val isPlaced = slotNum < nextExpectedNumber
                            val lesson = NumbersCurriculum.byValue(slotNum)
                            Surface(
                                modifier = Modifier.size(56.dp),
                                shape = RoundedCornerShape(16.dp),
                                color = if (isPlaced) lesson.color else Color.White,
                                border = BorderStroke(
                                    width = 2.dp,
                                    color = if (isPlaced) Color.White else Color(0xFFB0BEC5)
                                ),
                                shadowElevation = if (isPlaced) 4.dp else 0.dp
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = if (isPlaced) lesson.symbol(numeralLanguage) else "؟",
                                        color = if (isPlaced) Color.White else Color(0xFFB0BEC5),
                                        style = MaterialTheme.typography.headlineMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 24.sp
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Random Shuffled Numbers Pool to Tap
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                color = Color.White,
                border = BorderStroke(2.5.dp, SunnyYellowDark)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    val remaining = shuffledPool.filter { it >= nextExpectedNumber }
                    if (remaining.isEmpty()) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = null,
                            tint = StarGoldBorder,
                            modifier = Modifier.size(64.dp)
                        )
                        Text(
                            text = if (isArabic) {
                                "أحسنتِ يا بطلتي! لقد رتّبتِ الأرقام من ١ إلى ١٠ كلها بنجاح! 🎉"
                            } else {
                                "Amazing job! You arranged all numbers from 1 to 10 in order! 🎉"
                            },
                            color = DeepNavyText,
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            textAlign = TextAlign.Center
                        )
                    } else {
                        val nextItem = NumbersCurriculum.byValue(nextExpectedNumber)
                        Text(
                            text = if (isArabic) {
                                "اضغطي الآن على الرقم (${nextItem.symbol(numeralLanguage)})"
                            } else {
                                "Now tap number (${nextItem.symbol(numeralLanguage)})"
                            },
                            color = LuluPinkDark,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )

                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            maxItemsInEachRow = 5
                        ) {
                            remaining.forEach { num ->
                                val lesson = NumbersCurriculum.byValue(num)
                                Surface(
                                    modifier = Modifier
                                        .size(68.dp)
                                        .testTag("order_tile_$num")
                                        .clickable {
                                            if (num == nextExpectedNumber) {
                                                val nextVal = nextExpectedNumber + 1
                                                nextExpectedNumber = nextVal
                                                if (nextVal > 10) {
                                                    onMarkCompleted()
                                                    val doneMsg = if (isArabic) {
                                                        "رائع! لقد رتّبتِ الأرقام من واحد إلى عشرة وحصلتِ على نجمة ذهبية!"
                                                    } else {
                                                        "Wonderful! You ordered all numbers from one to ten and earned a golden star!"
                                                    }
                                                    statusText = doneMsg
                                                    onSpeakLulu(doneMsg, appLanguage)
                                                } else {
                                                    val word = lesson.word(numeralLanguage)
                                                    val nextWord = NumbersCurriculum.byValue(nextVal).word(numeralLanguage)
                                                    statusText = if (isArabic) {
                                                        "أحسنتِ! $word! الآن ابحثي عن الرقم $nextWord."
                                                    } else {
                                                        "Great! $word! Now find number $nextWord."
                                                    }
                                                    onSpeakLulu(word, numeralLanguage)
                                                }
                                            } else {
                                                val targetWord = NumbersCurriculum.byValue(nextExpectedNumber).word(numeralLanguage)
                                                val hint = if (isArabic) {
                                                    "لا بأس، ابحثي عن الرقم $targetWord أولاً يا صديقتي!"
                                                } else {
                                                    "Try again! Look for number $targetWord first, my friend!"
                                                }
                                                statusText = hint
                                                onSpeakLulu(hint, appLanguage)
                                            }
                                        },
                                    shape = RoundedCornerShape(20.dp),
                                    color = lesson.color,
                                    border = BorderStroke(2.5.dp, Color.White),
                                    shadowElevation = 6.dp
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            text = lesson.symbol(numeralLanguage),
                                            color = Color.White,
                                            style = MaterialTheme.typography.headlineLarge.copy(
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 30.sp
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
    }
}
