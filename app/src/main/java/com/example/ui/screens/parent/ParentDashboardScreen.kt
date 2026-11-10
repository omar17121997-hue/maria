package com.example.ui.screens.parent

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.automirrored.filled.Launch
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.SupervisorAccount
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.AppLanguage
import com.example.model.CategoryId
import com.example.model.ParentAnalyticsEngine
import com.example.model.ParentDashboardTab
import com.example.model.RewardsProgressCalculator
import com.example.ui.FunLearningUiState
import com.example.ui.components.LanguageSelectorSwitch
import com.example.ui.theme.DeepNavyText
import com.example.ui.theme.MagicPurple
import com.example.ui.theme.MagicPurpleDark
import com.example.ui.theme.MeadowGreenDark
import com.example.ui.theme.SkyCyanDark
import com.example.ui.theme.SoftSlateText
import com.example.ui.theme.StarGoldBorder
import com.example.util.PdfAndPrintHelper

/**
 * 4-Digit PIN Gate Dialog for Parent Dashboard:
 * - Never displays the PIN in plain text in the child UI.
 * - If no PIN has been set yet (`savedPin.isEmpty()`), prompts the parent to set a 4-digit PIN.
 * - Once set, requires entering the 4-digit PIN using a masked keypad (`••••`).
 */
@Composable
fun ParentPinGateDialog(
    uiState: FunLearningUiState,
    onDismiss: () -> Unit,
    onPinVerified: (String) -> Unit
) {
    val isAr = uiState.isArabic
    val savedPin = uiState.settings.parentPin
    val isCreatingPin = savedPin.isEmpty()

    var enteredDigits by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("parent_pin_gate_dialog"),
            shape = RoundedCornerShape(28.dp),
            color = Color.White,
            border = BorderStroke(3.dp, MagicPurple),
            shadowElevation = 12.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(22.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = MagicPurple.copy(alpha = 0.15f),
                            modifier = Modifier.size(42.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Filled.Lock,
                                    contentDescription = null,
                                    tint = MagicPurpleDark
                                )
                            }
                        }
                        Text(
                            text = if (isAr) "بوابة ولي الأمر" else "Parent Gate",
                            color = DeepNavyText,
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                        )
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = if (isAr) "إغلاق" else "Close",
                            tint = SoftSlateText
                        )
                    }
                }

                Text(
                    text = if (isCreatingPin) {
                        if (isAr) "أهلاً بك يا ولي الأمر! اختر رمز PIN من ٤ أرقام لحماية لوحة المتابعة والإعدادات:"
                        else "Welcome Parent! Create a 4-digit PIN to protect the Parent Dashboard & Settings:"
                    } else {
                        if (isAr) "أدخل رمز المرور المكون من ٤ أرقام للدخول إلى لوحة متابعة ولي الأمر:"
                        else "Enter your 4-digit PIN to access the Parent Dashboard:"
                    },
                    color = SoftSlateText,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    textAlign = TextAlign.Center
                )

                // Masked 4-Dot PIN Indicator
                Row(
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(4) { idx ->
                        val filled = idx < enteredDigits.length
                        Surface(
                            modifier = Modifier.size(48.dp),
                            shape = RoundedCornerShape(14.dp),
                            color = if (filled) MagicPurple.copy(alpha = 0.14f) else Color(0xFFF5F5F5),
                            border = BorderStroke(
                                width = 2.dp,
                                color = if (filled) MagicPurpleDark else Color(0xFFBDBDBD)
                            )
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = if (filled) "●" else "",
                                    color = MagicPurpleDark,
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                if (errorMessage != null) {
                    Text(
                        text = errorMessage!!,
                        color = Color(0xFFC62828),
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                        textAlign = TextAlign.Center
                    )
                }

                // Numeric Keypad (1–9, Clear, 0, Backspace)
                val rows = listOf(
                    listOf("1", "2", "3"),
                    listOf("4", "5", "6"),
                    listOf("7", "8", "9")
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    rows.forEach { digitRow ->
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            digitRow.forEach { digit ->
                                PinDigitKeyButton(
                                    label = digit,
                                    onClick = {
                                        if (enteredDigits.length < 4) {
                                            val next = enteredDigits + digit
                                            enteredDigits = next
                                            errorMessage = null
                                            if (next.length == 4) {
                                                if (isCreatingPin || next == savedPin) {
                                                    onPinVerified(next)
                                                } else {
                                                    errorMessage = if (isAr) "رمز PIN غير صحيح، حاول مرة أخرى" else "Incorrect PIN, please try again"
                                                    enteredDigits = ""
                                                }
                                            }
                                        }
                                    }
                                )
                            }
                        }
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Surface(
                            modifier = Modifier
                                .size(width = 72.dp, height = 52.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .clickable {
                                    enteredDigits = ""
                                    errorMessage = null
                                },
                            shape = RoundedCornerShape(16.dp),
                            color = Color(0xFFECEFF1)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = if (isAr) "مسح" else "Clear",
                                    color = DeepNavyText,
                                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                                )
                            }
                        }

                        PinDigitKeyButton(
                            label = "0",
                            onClick = {
                                if (enteredDigits.length < 4) {
                                    val next = enteredDigits + "0"
                                    enteredDigits = next
                                    errorMessage = null
                                    if (next.length == 4) {
                                        if (isCreatingPin || next == savedPin) {
                                            onPinVerified(next)
                                        } else {
                                            errorMessage = if (isAr) "رمز PIN غير صحيح، حاول مرة أخرى" else "Incorrect PIN, please try again"
                                            enteredDigits = ""
                                        }
                                    }
                                }
                            }
                        )

                        Surface(
                            modifier = Modifier
                                .size(width = 72.dp, height = 52.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .clickable {
                                    if (enteredDigits.isNotEmpty()) {
                                        enteredDigits = enteredDigits.dropLast(1)
                                    }
                                },
                            shape = RoundedCornerShape(16.dp),
                            color = Color(0xFFECEFF1)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.Backspace,
                                    contentDescription = "Backspace",
                                    tint = DeepNavyText,
                                    modifier = Modifier.size(20.dp)
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
private fun PinDigitKeyButton(
    label: String,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .size(width = 72.dp, height = 52.dp)
            .testTag("pin_key_$label")
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFFF3E5F5),
        border = BorderStroke(1.5.dp, MagicPurple.copy(alpha = 0.5f))
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = label,
                color = DeepNavyText,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 21.sp
                )
            )
        }
    }
}

// =====================================================================
// STAGE 8: FULL PARENT DASHBOARD SCREEN (لوحة متابعة ولي الأمر)
// =====================================================================

@Composable
fun ParentDashboardScreen(
    uiState: FunLearningUiState,
    onNavigateHome: () -> Unit,
    onOpenCategory: (CategoryId) -> Unit,
    onSelectLanguage: (AppLanguage) -> Unit,
    onToggleSound: () -> Unit,
    onSetDailyGoal: (Int) -> Unit,
    onUpdateChildName: (String) -> Unit,
    onUpdateParentPin: (String) -> Unit,
    onResetProgress: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler(onBack = onNavigateHome)

    val settings = uiState.settings
    val lang = settings.language
    val isAr = lang == AppLanguage.AR
    val layoutDirection = if (isAr) LayoutDirection.Rtl else LayoutDirection.Ltr

    var selectedTab by rememberSaveable { mutableStateOf(ParentDashboardTab.OVERVIEW) }
    var reportStatusBanner by remember { mutableStateOf<String?>(null) }

    CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
        Scaffold(
            modifier = modifier
                .fillMaxSize()
                .testTag("parent_dashboard_screen"),
            contentWindowInsets = WindowInsets.safeDrawing,
            containerColor = Color(0xFFF4F7FB)
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 1. Calm, Professional Parent Header Bar
                item {
                    ParentDashboardTopHeader(
                        language = lang,
                        childName = settings.childName,
                        onNavigateHome = onNavigateHome,
                        onSelectLanguage = onSelectLanguage,
                        modifier = Modifier.widthIn(max = 960.dp)
                    )
                }

                // 2. 5-Tab Bar (Overview, World Progress, Activity Log, Insights & Recommendations, Settings & PDF)
                item {
                    ParentDashboardTabRow(
                        selectedTab = selectedTab,
                        language = lang,
                        onSelectTab = {
                            selectedTab = it
                            reportStatusBanner = null
                        },
                        modifier = Modifier.widthIn(max = 960.dp)
                    )
                }

                // 3. Active Section Content
                when (selectedTab) {
                    ParentDashboardTab.OVERVIEW -> {
                        item {
                            ParentOverviewTabContent(
                                uiState = uiState,
                                onJumpToRecommendations = { selectedTab = ParentDashboardTab.INSIGHTS },
                                modifier = Modifier.widthIn(max = 960.dp)
                            )
                        }
                    }

                    ParentDashboardTab.WORLD_PROGRESS -> {
                        item {
                            ParentWorldProgressTabContent(
                                uiState = uiState,
                                onOpenCategory = onOpenCategory,
                                modifier = Modifier.widthIn(max = 960.dp)
                            )
                        }
                    }

                    ParentDashboardTab.ACTIVITY_LOG -> {
                        item {
                            ParentActivityLogTabContent(
                                uiState = uiState,
                                modifier = Modifier.widthIn(max = 960.dp)
                            )
                        }
                    }

                    ParentDashboardTab.INSIGHTS -> {
                        item {
                            ParentInsightsAndRecommendationsTabContent(
                                uiState = uiState,
                                onOpenCategory = onOpenCategory,
                                modifier = Modifier.widthIn(max = 960.dp)
                            )
                        }
                    }

                    ParentDashboardTab.SETTINGS -> {
                        item {
                            ParentSettingsAndReportTabContent(
                                uiState = uiState,
                                reportStatusBanner = reportStatusBanner,
                                onUpdateReportBanner = { reportStatusBanner = it },
                                onSelectLanguage = onSelectLanguage,
                                onToggleSound = onToggleSound,
                                onSetDailyGoal = onSetDailyGoal,
                                onUpdateChildName = onUpdateChildName,
                                onUpdateParentPin = onUpdateParentPin,
                                onResetProgress = onResetProgress,
                                modifier = Modifier.widthIn(max = 960.dp)
                            )
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
private fun ParentDashboardTopHeader(
    language: AppLanguage,
    childName: String,
    onNavigateHome: () -> Unit,
    onSelectLanguage: (AppLanguage) -> Unit,
    modifier: Modifier = Modifier
) {
    val isAr = language == AppLanguage.AR

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        border = BorderStroke(2.dp, Color(0xFFCFD8DC)),
        shadowElevation = 4.dp
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
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color(0xFF1565C0),
                        modifier = Modifier.size(48.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Filled.SupervisorAccount,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(26.dp)
                            )
                        }
                    }

                    Column {
                        Text(
                            text = if (isAr) "لوحة متابعة ولي الأمر" else "Parent Monitoring Dashboard",
                            color = DeepNavyText,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = if (isAr) 21.sp else 19.sp
                            )
                        )
                        Text(
                            text = if (isAr) "متابعة تقدم الطفل: $childName" else "Tracking Progress for: $childName",
                            color = SoftSlateText,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }

                Button(
                    onClick = onNavigateHome,
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0288D1)),
                    modifier = Modifier.testTag("parent_back_to_kid_btn")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isAr) "واجهة الطفل" else "Kid View",
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                LanguageSelectorSwitch(
                    currentLanguage = language,
                    onSelectLanguage = onSelectLanguage
                )

                Text(
                    text = if (isAr) "🔒 منطقة محمية برمز PIN" else "🔒 PIN-Protected Area",
                    color = Color(0xFF2E7D32),
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}

@Composable
private fun ParentDashboardTabRow(
    selectedTab: ParentDashboardTab,
    language: AppLanguage,
    onSelectTab: (ParentDashboardTab) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 2.dp)
    ) {
        items(ParentDashboardTab.entries) { tab ->
            val selected = tab == selectedTab
            Surface(
                modifier = Modifier
                    .testTag("parent_tab_${tab.name.lowercase()}")
                    .clickable { onSelectTab(tab) },
                shape = RoundedCornerShape(18.dp),
                color = if (selected) Color(0xFF1565C0) else Color.White,
                border = BorderStroke(1.5.dp, if (selected) Color(0xFF1565C0) else Color(0xFFCFD8DC)),
                shadowElevation = if (selected) 4.dp else 1.dp
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(text = tab.emoji, fontSize = 16.sp)
                    Text(
                        text = tab.title(language),
                        color = if (selected) Color.White else DeepNavyText,
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = if (language == AppLanguage.AR) 15.sp else 14.sp
                        )
                    )
                }
            }
        }
    }
}

// =====================================================================
// TAB 1: OVERVIEW (الصفحة الرئيسية للوحة المتابعة)
// =====================================================================

@Composable
private fun ParentOverviewTabContent(
    uiState: FunLearningUiState,
    onJumpToRecommendations: () -> Unit,
    modifier: Modifier = Modifier
) {
    val settings = uiState.settings
    val lang = settings.language
    val isAr = lang == AppLanguage.AR

    val currentLevel = RewardsProgressCalculator.computeCurrentLevel(settings)
    val completedActivities = RewardsProgressCalculator.totalCompletedActivitiesCount(settings)
    val maxActivities = RewardsProgressCalculator.maxActivitiesCount()
    val exploredWorlds = RewardsProgressCalculator.exploredWorldsCount(settings)
    val learnedLettersTotal = settings.learnedArabicLetters.size + settings.learnedEnglishLetters.size
    val numbersLearned = when {
        settings.completedNumbersActivities.size >= 5 -> 10
        settings.completedNumbersActivities.size >= 3 -> 7
        settings.completedNumbersActivities.isNotEmpty() -> 4
        else -> 2
    }
    val activityLog = remember(settings) { ParentAnalyticsEngine.buildActivityLog(settings) }
    val lastActivity = activityLog.firstOrNull()
    val worldList = remember(settings) { ParentAnalyticsEngine.buildWorldProgressList(settings) }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Child Profile & Level Summary Card
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
            color = Color.White,
            border = BorderStroke(1.5.dp, Color(0xFFBBDEFB)),
            shadowElevation = 3.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = if (isAr) "اسم الطفل: ${settings.childName}" else "Child's Name: ${settings.childName}",
                            color = DeepNavyText,
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "${currentLevel.emoji} ${
                                if (isAr) "المستوى الحالي: ${currentLevel.titleAr}"
                                else "Current Level: ${currentLevel.titleEn}"
                            }",
                            color = currentLevel.badgeColor,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color(0xFFFFF8E1),
                        border = BorderStroke(1.5.dp, StarGoldBorder)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
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
                                text = "${settings.totalEarnedStars}",
                                color = DeepNavyText,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                }

                // Last Activity Banner
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    color = Color(0xFFF1F8E9),
                    border = BorderStroke(1.dp, Color(0xFF81C784))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = if (lastActivity != null) {
                                if (isAr) "آخر نشاط: ${lastActivity.activityTitleAr}"
                                else "Last Activity: ${lastActivity.activityTitleEn}"
                            } else {
                                if (isAr) "آخر نشاط: استكشاف الواجهة الرئيسية"
                                else "Last Activity: Exploring Home Screen"
                            },
                            color = Color(0xFF1B5E20),
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        if (lastActivity != null) {
                            Text(
                                text = lastActivity.formattedDate,
                                color = SoftSlateText,
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                }
            }
        }

        // 2. 6 Core Quantitative Stat Cards (2x3 Grid)
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ParentMetricBox(
                    title = if (isAr) "إجمالي النجوم" else "Total Stars",
                    value = "${settings.totalEarnedStars}",
                    subtitle = if (isAr) "من ${settings.maxPossibleStars} نجمة" else "of ${settings.maxPossibleStars} stars",
                    accentColor = Color(0xFFF57C00),
                    modifier = Modifier.weight(1f)
                )
                ParentMetricBox(
                    title = if (isAr) "الأنشطة المكتملة" else "Completed Activities",
                    value = "$completedActivities / $maxActivities",
                    subtitle = "${((completedActivities.toFloat() / maxActivities.coerceAtLeast(1)) * 100).toInt()}%",
                    accentColor = Color(0xFF2E7D32),
                    modifier = Modifier.weight(1f)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ParentMetricBox(
                    title = if (isAr) "الحروف المتعلمة" else "Learned Letters",
                    value = "$learnedLettersTotal / 54",
                    subtitle = if (isAr) {
                        "عربي: ${settings.learnedArabicLetters.size}/٢٨ • إنجليزي: ${settings.learnedEnglishLetters.size}/٢٦"
                    } else {
                        "AR: ${settings.learnedArabicLetters.size}/28 • EN: ${settings.learnedEnglishLetters.size}/26"
                    },
                    accentColor = Color(0xFF6A1B9A),
                    modifier = Modifier.weight(1f)
                )
                ParentMetricBox(
                    title = if (isAr) "الأرقام المتعلمة" else "Learned Numbers",
                    value = "$numbersLearned / 10",
                    subtitle = if (isAr) "الأرقام والعدّ (١–١٠)" else "Numbers 1 to 10",
                    accentColor = Color(0xFF0288D1),
                    modifier = Modifier.weight(1f)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ParentMetricBox(
                    title = if (isAr) "العوالم المستكشفة" else "Worlds Explored",
                    value = "$exploredWorlds / 8",
                    subtitle = if (isAr) "تنوع المجالات التعليمية" else "Curriculum Diversity",
                    accentColor = Color(0xFF00796B),
                    modifier = Modifier.weight(1f)
                )
                ParentMetricBox(
                    title = if (isAr) "الهدف اليومي" else "Daily Goal",
                    value = "${settings.dailyGoalMinutes} ${if (isAr) "دقيقة" else "min"}",
                    subtitle = if (isAr) "وقت التعلم الموصى به" else "Recommended session",
                    accentColor = Color(0xFFC2185B),
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // 3. Visual Progress Chart Across Worlds
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
            color = Color.White,
            border = BorderStroke(1.5.dp, Color(0xFFCFD8DC)),
            shadowElevation = 3.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.TrendingUp,
                        contentDescription = null,
                        tint = Color(0xFF1565C0)
                    )
                    Text(
                        text = if (isAr) "مؤشرات الإنجاز البصرية حسب المجال" else "Visual Progress Chart by Domain",
                        color = DeepNavyText,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }

                worldList.forEach { world ->
                    val pct = (world.progressFraction * 100).toInt()
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "${world.emoji} ${world.title(lang)}",
                                color = DeepNavyText,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = "$pct% (${world.completedCount}/${world.totalCount})",
                                color = world.accentColor,
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                        LinearProgressIndicator(
                            progress = { world.progressFraction.coerceAtLeast(0.06f) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(10.dp)
                                .clip(RoundedCornerShape(6.dp)),
                            color = world.accentColor,
                            trackColor = Color(0xFFECEFF1)
                        )
                    }
                }

                OutlinedButton(
                    onClick = onJumpToRecommendations,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(
                        text = if (isAr) "عرض تحليل نقاط القوة والتوصيات التعليمية ←" else "View Strengths & Smart Recommendations →",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun ParentMetricBox(
    title: String,
    value: String,
    subtitle: String,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        color = Color.White,
        border = BorderStroke(1.5.dp, accentColor.copy(alpha = 0.4f)),
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = title,
                color = SoftSlateText,
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                text = value,
                color = accentColor,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                )
            )
            Text(
                text = subtitle,
                color = DeepNavyText,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.5.sp
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

// =====================================================================
// TAB 2: DETAILED PROGRESS BY WORLD (متابعة التقدم حسب العالم)
// =====================================================================

@Composable
private fun ParentWorldProgressTabContent(
    uiState: FunLearningUiState,
    onOpenCategory: (CategoryId) -> Unit,
    modifier: Modifier = Modifier
) {
    val settings = uiState.settings
    val lang = settings.language
    val isAr = lang == AppLanguage.AR
    val worlds = remember(settings) { ParentAnalyticsEngine.buildWorldProgressList(settings) }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        worlds.forEach { world ->
            val pct = (world.progressFraction * 100).toInt()
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                color = Color.White,
                border = BorderStroke(2.dp, world.accentColor.copy(alpha = 0.55f)),
                shadowElevation = 3.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = world.accentColor.copy(alpha = 0.15f),
                                modifier = Modifier.size(44.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(text = world.emoji, fontSize = 22.sp)
                                }
                            }
                            Column {
                                Text(
                                    text = world.title(lang),
                                    color = DeepNavyText,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = if (isAr) 18.sp else 16.sp
                                    )
                                )
                                Text(
                                    text = if (isAr) "نسبة التقدم: $pct%" else "Progress: $pct%",
                                    color = world.accentColor,
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                                )
                            }
                        }

                        OutlinedButton(
                            onClick = { onOpenCategory(world.categoryId) },
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Text(
                                text = if (isAr) "فتح العالم" else "Open World",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }

                    LinearProgressIndicator(
                        progress = { world.progressFraction.coerceAtLeast(0.06f) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .clip(RoundedCornerShape(6.dp)),
                        color = world.accentColor,
                        trackColor = Color(0xFFECEFF1)
                    )

                    world.summaryMetrics(lang).forEach { metric ->
                        Text(
                            text = "• $metric",
                            color = DeepNavyText,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }

                    val practiceItems = world.needsPracticeItems(lang)
                    if (practiceItems.isNotEmpty()) {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFFFFF8E1),
                            border = BorderStroke(1.dp, StarGoldBorder)
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                practiceItems.forEach { item ->
                                    Text(
                                        text = "📌 $item",
                                        color = Color(0xFFE65100),
                                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
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

// =====================================================================
// TAB 3: ACTIVITY LOG WITH FILTERING (سجل الأنشطة)
// =====================================================================

@Composable
private fun ParentActivityLogTabContent(
    uiState: FunLearningUiState,
    modifier: Modifier = Modifier
) {
    val settings = uiState.settings
    val lang = settings.language
    val isAr = lang == AppLanguage.AR

    val allEntries = remember(settings) { ParentAnalyticsEngine.buildActivityLog(settings) }
    var selectedWorldFilter by rememberSaveable { mutableStateOf<CategoryId?>(null) }
    var todayOnlyFilter by rememberSaveable { mutableStateOf(false) }

    val todayPrefix = remember { PdfAndPrintHelper.currentFormattedDate() }

    val filteredEntries = remember(allEntries, selectedWorldFilter, todayOnlyFilter) {
        allEntries.filter { entry ->
            val worldMatch = selectedWorldFilter == null || entry.worldCategory == selectedWorldFilter
            val dateMatch = !todayOnlyFilter || entry.formattedDate.startsWith(todayPrefix)
            worldMatch && dateMatch
        }
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
            color = Color.White,
            border = BorderStroke(1.5.dp, Color(0xFFCFD8DC)),
            shadowElevation = 3.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isAr) "📋 سجل الأنشطة (${filteredEntries.size})" else "📋 Activity Log (${filteredEntries.size})",
                        color = DeepNavyText,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )

                    // Filter by Today toggle chip
                    Surface(
                        modifier = Modifier.clickable { todayOnlyFilter = !todayOnlyFilter },
                        shape = RoundedCornerShape(14.dp),
                        color = if (todayOnlyFilter) Color(0xFF0288D1) else Color(0xFFECEFF1)
                    ) {
                        Text(
                            text = if (isAr) "أنشطة اليوم فقط 📅" else "Today Only 📅",
                            color = if (todayOnlyFilter) Color.White else DeepNavyText,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }

                // Filter by World chips
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    item {
                        Surface(
                            modifier = Modifier.clickable { selectedWorldFilter = null },
                            shape = RoundedCornerShape(14.dp),
                            color = if (selectedWorldFilter == null) MagicPurpleDark else Color(0xFFF5F5F5)
                        ) {
                            Text(
                                text = if (isAr) "جميع العوالم" else "All Worlds",
                                color = if (selectedWorldFilter == null) Color.White else DeepNavyText,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }

                    items(uiState.categories) { cat ->
                        val isSelected = selectedWorldFilter == cat.id
                        Surface(
                            modifier = Modifier.clickable { selectedWorldFilter = cat.id },
                            shape = RoundedCornerShape(14.dp),
                            color = if (isSelected) cat.accentColor else Color(0xFFF5F5F5)
                        ) {
                            Text(
                                text = cat.title(lang),
                                color = if (isSelected) Color.White else DeepNavyText,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                }
            }
        }

        if (filteredEntries.isEmpty()) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                color = Color.White,
                border = BorderStroke(1.5.dp, Color(0xFFCFD8DC))
            ) {
                Text(
                    text = if (isAr) {
                        "لا توجد أنشطة مسجلة تطابق هذا الفلتر حالياً. عندما يكمل الطفل أي نشاط سيظهر هنا بالتفصيل."
                    } else {
                        "No logged activities match this filter yet. Completed activities will appear here chronologically."
                    },
                    color = SoftSlateText,
                    modifier = Modifier.padding(24.dp),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
        } else {
            filteredEntries.forEach { entry ->
                val cat = uiState.getCategory(entry.worldCategory)
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    color = Color.White,
                    border = BorderStroke(1.5.dp, cat.accentColor.copy(alpha = 0.45f)),
                    shadowElevation = 2.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = entry.activityTitle(lang),
                                color = DeepNavyText,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                            )
                            Text(
                                text = "${cat.title(lang)}  •  ${entry.formattedDate}",
                                color = SoftSlateText,
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color(0xFFFFF8E1),
                                border = BorderStroke(1.dp, StarGoldBorder)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Star,
                                        contentDescription = null,
                                        tint = StarGoldBorder,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = "+${entry.starsEarned}",
                                        color = DeepNavyText,
                                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                                    )
                                }
                            }

                            Icon(
                                imageVector = Icons.Filled.CheckCircle,
                                contentDescription = if (isAr) "مكتمل" else "Completed",
                                tint = Color(0xFF2E7D32),
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

// =====================================================================
// TAB 4: STRENGTHS, PRACTICE AREAS & EDUCATIONAL RECOMMENDATIONS
// =====================================================================

@Composable
private fun ParentInsightsAndRecommendationsTabContent(
    uiState: FunLearningUiState,
    onOpenCategory: (CategoryId) -> Unit,
    modifier: Modifier = Modifier
) {
    val settings = uiState.settings
    val lang = settings.language
    val isAr = lang == AppLanguage.AR
    val insights = remember(settings) { ParentAnalyticsEngine.generateInsightsAndRecommendations(settings) }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Strengths Card (نقاط القوة)
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
            color = Color(0xFFE8F5E9),
            border = BorderStroke(2.dp, Color(0xFF4CAF50)),
            shadowElevation = 3.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = if (isAr) "🌟 نقاط القوة لدى الطفل" else "🌟 Child's Strengths",
                    color = Color(0xFF1B5E20),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                )
                insights.strengths(lang).forEach { strength ->
                    Text(
                        text = "✓ $strength",
                        color = DeepNavyText,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }

        // 2. Areas Needing Practice (جوانب تحتاج إلى تدريب إضافي)
        if (insights.areasToPractice(lang).isNotEmpty()) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                color = Color(0xFFFFF8E1),
                border = BorderStroke(2.dp, Color(0xFFFFB300)),
                shadowElevation = 3.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = if (isAr) "🎯 جوانب تحتاج إلى تدريب إضافي" else "🎯 Areas Needing Extra Practice",
                        color = Color(0xFFE65100),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    )
                    insights.areasToPractice(lang).forEach { area ->
                        Text(
                            text = "• $area",
                            color = DeepNavyText,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }
        }

        // 3. Smart Educational Recommendations with Direct "انتقل إلى النشاط / Go to Activity" Button
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
            color = Color.White,
            border = BorderStroke(2.dp, Color(0xFF90CAF9)),
            shadowElevation = 3.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = if (isAr) "💡 التوصيات التعليمية الذكية" else "💡 Smart Educational Recommendations",
                    color = DeepNavyText,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 19.sp
                    )
                )

                insights.recommendations.forEach { rec ->
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        color = Color(0xFFF8FAFC),
                        border = BorderStroke(1.5.dp, rec.accentColor.copy(alpha = 0.5f))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(text = rec.emoji, fontSize = 24.sp)
                                Text(
                                    text = rec.title(lang),
                                    color = DeepNavyText,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                )
                            }

                            Text(
                                text = rec.reason(lang),
                                color = SoftSlateText,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                            )

                            Text(
                                text = "💬 ${if (isAr) "نصيحة لولي الأمر:" else "Parent Tip:"} ${rec.parentTip(lang)}",
                                color = rec.accentColor,
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                            )

                            Button(
                                onClick = { onOpenCategory(rec.targetCategory) },
                                colors = ButtonDefaults.buttonColors(containerColor = rec.accentColor),
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("go_to_activity_${rec.id}")
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.Launch,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (isAr) "انتقل إلى النشاط" else "Go to Activity",
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }

        // 4. General Pedagogical Tips
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
            color = Color(0xFFF3E5F5),
            border = BorderStroke(1.5.dp, MagicPurple)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = if (isAr) "📌 إرشادات تربوية لعمر ٤–٦ سنوات" else "📌 Pedagogical Tips for Ages 4–6",
                    color = MagicPurpleDark,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                insights.parentTips(lang).forEach { tip ->
                    Text(
                        text = "• $tip",
                        color = DeepNavyText,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }
}

// =====================================================================
// TAB 5: PARENT SETTINGS & PDF REPORT EXPORT (الإعدادات والتقارير)
// =====================================================================

@Composable
private fun ParentSettingsAndReportTabContent(
    uiState: FunLearningUiState,
    reportStatusBanner: String?,
    onUpdateReportBanner: (String) -> Unit,
    onSelectLanguage: (AppLanguage) -> Unit,
    onToggleSound: () -> Unit,
    onSetDailyGoal: (Int) -> Unit,
    onUpdateChildName: (String) -> Unit,
    onUpdateParentPin: (String) -> Unit,
    onResetProgress: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val settings = uiState.settings
    val lang = settings.language
    val isAr = lang == AppLanguage.AR

    var childNameField by remember(settings.childName) { mutableStateOf(settings.childName) }
    var newPinField by remember { mutableStateOf("") }
    var pinSavedMessage by remember { mutableStateOf<String?>(null) }
    var showResetConfirmationDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Downloadable PDF Educational Report Card
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
            color = Color.White,
            border = BorderStroke(2.dp, Color(0xFF1565C0)),
            shadowElevation = 4.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.PictureAsPdf,
                        contentDescription = null,
                        tint = Color(0xFFD32F2F),
                        modifier = Modifier.size(28.dp)
                    )
                    Text(
                        text = if (isAr) "تقرير تقدم الطفل بصيغة PDF" else "Child Progress Report (PDF)",
                        color = DeepNavyText,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 19.sp
                        )
                    )
                }

                Text(
                    text = if (isAr) {
                        "يتضمن التقرير: اسم الطفل، التاريخ، الملخص العام، المستوى الحالي، الشارات المفتوحة، الأنشطة المكتملة، نقاط القوة، والتوصيات التعليمية."
                    } else {
                        "Includes: Child's name, date, overall summary, current level, unlocked badges, completed activities, strengths, and recommended activities."
                    },
                    color = SoftSlateText,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                )

                Button(
                    onClick = {
                        val msg = PdfAndPrintHelper.exportParentReportAsPdf(context, settings)
                        onUpdateReportBanner(msg)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1565C0)),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("export_parent_pdf_report_btn")
                ) {
                    Icon(
                        imageVector = Icons.Filled.PictureAsPdf,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isAr) "تنزيل تقرير التقدم بصيغة PDF" else "Download Progress Report (PDF)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }

                AnimatedVisibility(
                    visible = reportStatusBanner != null,
                    enter = fadeIn() + scaleIn()
                ) {
                    reportStatusBanner?.let { banner ->
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            color = Color(0xFFE8F5E9),
                            border = BorderStroke(1.5.dp, Color(0xFF2E7D32))
                        ) {
                            Text(
                                text = banner,
                                color = Color(0xFF1B5E20),
                                modifier = Modifier.padding(12.dp),
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }

        // 2. Child Name & 4-Digit PIN Settings
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
            color = Color.White,
            border = BorderStroke(1.5.dp, Color(0xFFCFD8DC)),
            shadowElevation = 3.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = if (isAr) "⚙️ إعدادات الطفل ورمز الحماية PIN" else "⚙️ Child Profile & PIN Security",
                    color = DeepNavyText,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )

                // Edit Child Name
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = childNameField,
                        onValueChange = { childNameField = it },
                        singleLine = true,
                        label = { Text(if (isAr) "اسم الطفل" else "Child's Name") },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("parent_child_name_input"),
                        shape = RoundedCornerShape(14.dp)
                    )
                    Button(
                        onClick = { onUpdateChildName(childNameField) },
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SkyCyanDark)
                    ) {
                        Text(if (isAr) "حفظ" else "Save", fontWeight = FontWeight.Bold)
                    }
                }

                // Change 4-Digit PIN
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = newPinField,
                        onValueChange = { input ->
                            newPinField = input.filter { it.isDigit() }.take(4)
                            pinSavedMessage = null
                        },
                        singleLine = true,
                        label = {
                            Text(if (isAr) "رمز PIN جديد (٤ أرقام)" else "New 4-Digit PIN")
                        },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("parent_new_pin_input"),
                        shape = RoundedCornerShape(14.dp)
                    )
                    Button(
                        onClick = {
                            if (newPinField.length == 4) {
                                onUpdateParentPin(newPinField)
                                pinSavedMessage = if (isAr) "تم تحديث رمز PIN بنجاح ✓" else "PIN updated successfully ✓"
                                newPinField = ""
                            } else {
                                pinSavedMessage = if (isAr) "يرجى إدخال ٤ أرقام" else "Please enter 4 digits"
                            }
                        },
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MagicPurpleDark)
                    ) {
                        Text(if (isAr) "تغيير الرمز" else "Update PIN", fontWeight = FontWeight.Bold)
                    }
                }

                if (pinSavedMessage != null) {
                    Text(
                        text = pinSavedMessage!!,
                        color = MeadowGreenDark,
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }

        // 3. Language, Sound, and Daily Goal Controls
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
            color = Color.White,
            border = BorderStroke(1.5.dp, Color(0xFFCFD8DC)),
            shadowElevation = 3.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isAr) "اللغة الأساسية للتطبيق" else "Primary App Language",
                        color = DeepNavyText,
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                    )
                    LanguageSelectorSwitch(
                        currentLanguage = lang,
                        onSelectLanguage = onSelectLanguage
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isAr) "المؤثرات الصوتية وصوت لولو" else "Sound Effects & Lulu Voice",
                        color = DeepNavyText,
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                    )
                    Switch(
                        checked = settings.soundEnabled,
                        onCheckedChange = { onToggleSound() }
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = if (isAr) "هدف التعلّم اليومي (بالدقائق):" else "Daily Learning Goal (Minutes):",
                        color = DeepNavyText,
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(15, 20, 30, 45).forEach { mins ->
                            val selected = settings.dailyGoalMinutes == mins
                            Surface(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { onSetDailyGoal(mins) },
                                shape = RoundedCornerShape(14.dp),
                                color = if (selected) SkyCyanDark else Color(0xFFECEFF1)
                            ) {
                                Text(
                                    text = if (isAr) "$mins د" else "${mins}m",
                                    color = if (selected) Color.White else DeepNavyText,
                                    modifier = Modifier.padding(vertical = 10.dp),
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                                )
                            }
                        }
                    }
                }
            }
        }

        // 4. Protected Reset Child Progress Section (with explicit confirmation dialog)
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
            color = Color(0xFFFFEBEE),
            border = BorderStroke(1.5.dp, Color(0xFFEF9A9A))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = if (isAr) "إعادة تعيين تقدم الطفل" else "Reset Child Progress",
                    color = Color(0xFFC62828),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = if (isAr) {
                        "سيؤدي هذا الإجراء إلى تصفير جميع النجوم والشارات وسجل الأنشطة للبدء من جديد."
                    } else {
                        "This action will clear all earned stars, unlocked badges, and activity history to start fresh."
                    },
                    color = DeepNavyText,
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                )
                OutlinedButton(
                    onClick = { showResetConfirmationDialog = true },
                    border = BorderStroke(1.5.dp, Color(0xFFC62828)),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("parent_reset_progress_btn")
                ) {
                    Icon(
                        imageVector = Icons.Filled.DeleteForever,
                        contentDescription = null,
                        tint = Color(0xFFC62828)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isAr) "إعادة تعيين جميع البيانات..." else "Reset All Progress Data...",
                        color = Color(0xFFC62828),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }

    if (showResetConfirmationDialog) {
        AlertDialog(
            onDismissRequest = { showResetConfirmationDialog = false },
            title = {
                Text(
                    text = if (isAr) "تأكيد إعادة تعيين التقدم" else "Confirm Progress Reset",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = if (isAr) {
                        "هل أنت متأكد من رغبتك في حذف جميع النجوم والشارات وسجل الأنشطة؟ لا يمكن التراجع عن هذه الخطوة."
                    } else {
                        "Are you sure you want to reset all stars, badges, and activity logs? This action cannot be undone."
                    }
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showResetConfirmationDialog = false
                        onResetProgress()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC62828)),
                    modifier = Modifier.testTag("confirm_reset_progress_btn")
                ) {
                    Text(
                        text = if (isAr) "نعم، إعادة التعيين" else "Yes, Reset All",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showResetConfirmationDialog = false }) {
                    Text(if (isAr) "إلغاء" else "Cancel", fontWeight = FontWeight.Bold)
                }
            }
        )
    }
}
