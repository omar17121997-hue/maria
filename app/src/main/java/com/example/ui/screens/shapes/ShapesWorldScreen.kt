package com.example.ui.screens.shapes

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.VolumeOff
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.AppLanguage
import com.example.model.ShapeLessonItem
import com.example.model.ShapeType
import com.example.model.ShapesActivityId
import com.example.model.ShapesCurriculum
import com.example.ui.FunLearningUiState
import com.example.ui.components.Bouncy3DButton
import com.example.ui.components.Bouncy3DIconButton
import com.example.ui.components.LanguageSelectorSwitch
import com.example.ui.components.PlayfulSkyBackground
import com.example.ui.theme.DeepNavyText
import com.example.ui.theme.LuluPink
import com.example.ui.theme.LuluPinkDark
import com.example.ui.theme.MeadowGreen
import com.example.ui.theme.MeadowGreenDark
import com.example.ui.theme.ShapesAccent
import com.example.ui.theme.ShapesCardBg
import com.example.ui.theme.ShapesShadow
import com.example.ui.theme.SoftSlateText
import com.example.ui.theme.StarGoldBorder
import com.example.ui.theme.SunnyYellow
import com.example.ui.theme.SunnyYellowDark
import kotlin.math.cos
import kotlin.math.sin

sealed interface ShapesWorldRoute {
    data object Hub : ShapesWorldRoute
    data class Activity(
        val id: ShapesActivityId,
        val initialShapeIndex: Int = 1
    ) : ShapesWorldRoute
}

@Composable
fun ShapesWorldScreen(
    uiState: FunLearningUiState,
    onNavigateHome: () -> Unit,
    onSelectLanguage: (AppLanguage) -> Unit,
    onToggleSound: () -> Unit,
    onSpeakLulu: (String, AppLanguage) -> Unit,
    onCompleteActivity: (ShapesActivityId) -> Unit,
    modifier: Modifier = Modifier
) {
    var currentRoute by remember { mutableStateOf<ShapesWorldRoute>(ShapesWorldRoute.Hub) }

    BackHandler {
        if (currentRoute != ShapesWorldRoute.Hub) {
            currentRoute = ShapesWorldRoute.Hub
        } else {
            onNavigateHome()
        }
    }

    when (val route = currentRoute) {
        is ShapesWorldRoute.Hub -> {
            ShapesWorldHubScreen(
                uiState = uiState,
                onNavigateHome = onNavigateHome,
                onSelectLanguage = onSelectLanguage,
                onToggleSound = onToggleSound,
                onSpeakLulu = onSpeakLulu,
                onOpenActivity = { activityId, shapeIdx ->
                    currentRoute = ShapesWorldRoute.Activity(activityId, shapeIdx)
                },
                modifier = modifier
            )
        }

        is ShapesWorldRoute.Activity -> {
            when (route.id) {
                ShapesActivityId.LEARN_SHAPES -> {
                    LearnShapesActivityScreen(
                        initialShapeIndex = route.initialShapeIndex,
                        appLanguage = uiState.settings.language,
                        soundEnabled = uiState.settings.soundEnabled,
                        isCompleted = ShapesActivityId.LEARN_SHAPES in uiState.settings.completedShapesActivities,
                        onBackToHub = { currentRoute = ShapesWorldRoute.Hub },
                        onNavigateHome = onNavigateHome,
                        onToggleSound = onToggleSound,
                        onSpeakLulu = onSpeakLulu,
                        onMarkCompleted = { onCompleteActivity(ShapesActivityId.LEARN_SHAPES) }
                    )
                }

                ShapesActivityId.IDENTIFY_SHAPE -> {
                    IdentifyShapeActivityScreen(
                        appLanguage = uiState.settings.language,
                        soundEnabled = uiState.settings.soundEnabled,
                        isCompleted = ShapesActivityId.IDENTIFY_SHAPE in uiState.settings.completedShapesActivities,
                        onBackToHub = { currentRoute = ShapesWorldRoute.Hub },
                        onNavigateHome = onNavigateHome,
                        onToggleSound = onToggleSound,
                        onSpeakLulu = onSpeakLulu,
                        onMarkCompleted = { onCompleteActivity(ShapesActivityId.IDENTIFY_SHAPE) }
                    )
                }

                ShapesActivityId.MATCH_SHAPES -> {
                    MatchShapesActivityScreen(
                        appLanguage = uiState.settings.language,
                        soundEnabled = uiState.settings.soundEnabled,
                        isCompleted = ShapesActivityId.MATCH_SHAPES in uiState.settings.completedShapesActivities,
                        onBackToHub = { currentRoute = ShapesWorldRoute.Hub },
                        onNavigateHome = onNavigateHome,
                        onToggleSound = onToggleSound,
                        onSpeakLulu = onSpeakLulu,
                        onMarkCompleted = { onCompleteActivity(ShapesActivityId.MATCH_SHAPES) }
                    )
                }

                ShapesActivityId.SHAPES_AROUND_US -> {
                    ShapesAroundUsActivityScreen(
                        appLanguage = uiState.settings.language,
                        soundEnabled = uiState.settings.soundEnabled,
                        isCompleted = ShapesActivityId.SHAPES_AROUND_US in uiState.settings.completedShapesActivities,
                        onBackToHub = { currentRoute = ShapesWorldRoute.Hub },
                        onNavigateHome = onNavigateHome,
                        onToggleSound = onToggleSound,
                        onSpeakLulu = onSpeakLulu,
                        onMarkCompleted = { onCompleteActivity(ShapesActivityId.SHAPES_AROUND_US) }
                    )
                }
            }
        }
    }
}

/**
 * Custom 3D-styled Canvas illustration for all 8 shapes (Circle, Square, Triangle, Rectangle, Star, Heart, Oval, Diamond)
 * with 3D shadow, glossy white border, and a friendly smiling cartoon face.
 */
@Composable
fun Shape3DCanvasIllustration(
    shapeType: ShapeType,
    fillColor: Color,
    shadowColor: Color,
    modifier: Modifier = Modifier,
    showCuteFace: Boolean = true
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val cx = w / 2f
        val cy = h / 2f
        val r = minOf(w, h) * 0.38f

        // 3D Shadow offset
        drawGeometricShapePath(
            type = shapeType,
            cx = cx,
            cy = cy + r * 0.14f,
            r = r,
            color = shadowColor.copy(alpha = 0.42f),
            isOutline = false
        )

        // Main 3D Shape Body
        drawGeometricShapePath(
            type = shapeType,
            cx = cx,
            cy = cy,
            r = r,
            color = fillColor,
            isOutline = false
        )

        // White Glossy Outline
        drawGeometricShapePath(
            type = shapeType,
            cx = cx,
            cy = cy,
            r = r,
            color = Color.White,
            isOutline = true
        )

        // Glossy Highlight
        drawCircle(
            color = Color.White.copy(alpha = 0.42f),
            radius = r * 0.16f,
            center = Offset(cx - r * 0.25f, cy - r * 0.25f)
        )

        if (showCuteFace) {
            val eyeOffset = r * 0.24f
            drawCircle(Color(0xFF141829), radius = r * 0.08f, center = Offset(cx - eyeOffset, cy - r * 0.04f))
            drawCircle(Color(0xFF141829), radius = r * 0.08f, center = Offset(cx + eyeOffset, cy - r * 0.04f))
            drawArc(
                color = Color(0xFF141829),
                startAngle = 20f,
                sweepAngle = 140f,
                useCenter = false,
                topLeft = Offset(cx - r * 0.18f, cy),
                size = Size(r * 0.36f, r * 0.24f),
                style = Stroke(width = r * 0.07f, cap = StrokeCap.Round)
            )
        }
    }
}

private fun DrawScope.drawGeometricShapePath(
    type: ShapeType,
    cx: Float,
    cy: Float,
    r: Float,
    color: Color,
    isOutline: Boolean
) {
    val strokeStyle = Stroke(width = r * 0.12f, cap = StrokeCap.Round, join = StrokeJoin.Round)
    when (type) {
        ShapeType.CIRCLE -> {
            if (isOutline) {
                drawCircle(color, radius = r, center = Offset(cx, cy), style = strokeStyle)
            } else {
                drawCircle(color, radius = r, center = Offset(cx, cy))
            }
        }

        ShapeType.SQUARE -> {
            val side = r * 1.75f
            val topLeft = Offset(cx - side / 2f, cy - side / 2f)
            if (isOutline) {
                drawRoundRect(color, topLeft, Size(side, side), CornerRadius(r * 0.22f), style = strokeStyle)
            } else {
                drawRoundRect(color, topLeft, Size(side, side), CornerRadius(r * 0.22f))
            }
        }

        ShapeType.RECTANGLE -> {
            val rw = r * 2.15f
            val rh = r * 1.32f
            val topLeft = Offset(cx - rw / 2f, cy - rh / 2f)
            if (isOutline) {
                drawRoundRect(color, topLeft, Size(rw, rh), CornerRadius(r * 0.20f), style = strokeStyle)
            } else {
                drawRoundRect(color, topLeft, Size(rw, rh), CornerRadius(r * 0.20f))
            }
        }

        ShapeType.OVAL -> {
            val ow = r * 2.10f
            val oh = r * 1.36f
            val topLeft = Offset(cx - ow / 2f, cy - oh / 2f)
            if (isOutline) {
                drawOval(color, topLeft, Size(ow, oh), style = strokeStyle)
            } else {
                drawOval(color, topLeft, Size(ow, oh))
            }
        }

        ShapeType.TRIANGLE -> {
            val path = Path().apply {
                moveTo(cx, cy - r * 1.02f)
                lineTo(cx - r * 1.05f, cy + r * 0.85f)
                lineTo(cx + r * 1.05f, cy + r * 0.85f)
                close()
            }
            if (isOutline) drawPath(path, color, style = strokeStyle) else drawPath(path, color)
        }

        ShapeType.STAR -> {
            val path = Path()
            val innerR = r * 0.48f
            for (i in 0 until 10) {
                val rad = if (i % 2 == 0) r * 1.12f else innerR
                val angle = Math.toRadians((i * 36.0) - 90.0)
                val x = cx + (cos(angle) * rad).toFloat()
                val y = cy + (sin(angle) * rad).toFloat()
                if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
            }
            path.close()
            if (isOutline) drawPath(path, color, style = strokeStyle) else drawPath(path, color)
        }

        ShapeType.HEART -> {
            val path = Path().apply {
                moveTo(cx, cy + r * 0.92f)
                cubicTo(cx - r * 1.25f, cy + r * 0.15f, cx - r * 0.95f, cy - r * 0.95f, cx, cy - r * 0.35f)
                cubicTo(cx + r * 0.95f, cy - r * 0.95f, cx + r * 1.25f, cy + r * 0.15f, cx, cy + r * 0.92f)
                close()
            }
            if (isOutline) drawPath(path, color, style = strokeStyle) else drawPath(path, color)
        }

        ShapeType.DIAMOND -> {
            val path = Path().apply {
                moveTo(cx, cy - r * 1.08f)
                lineTo(cx + r * 0.88f, cy)
                lineTo(cx, cy + r * 1.08f)
                lineTo(cx - r * 0.88f, cy)
                close()
            }
            if (isOutline) drawPath(path, color, style = strokeStyle) else drawPath(path, color)
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ShapesWorldHubScreen(
    uiState: FunLearningUiState,
    onNavigateHome: () -> Unit,
    onSelectLanguage: (AppLanguage) -> Unit,
    onToggleSound: () -> Unit,
    onSpeakLulu: (String, AppLanguage) -> Unit,
    onOpenActivity: (ShapesActivityId, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val isArabic = uiState.isArabic
    val soundEnabled = uiState.settings.soundEnabled
    val completedSet = uiState.settings.completedShapesActivities

    val welcomeText = if (isArabic) {
        "مرحباً يا بطلتي في عالم الأشكال! اضغطي على أي شكل هندسي لنتعرّف على اسمه ونراه في الأشياء من حولنا!"
    } else {
        "Welcome to Shapes World! Tap any 3D shape below to learn its name and spot shapes all around us!"
    }

    LaunchedEffect(uiState.settings.language) {
        onSpeakLulu(
            if (isArabic) "انظري حولكِ! هيا نتعرّف على الأشكال الهندسية الجميلة!" else "Let's learn colorful geometric shapes together!",
            uiState.settings.language
        )
    }

    PlayfulSkyBackground(
        modifier = modifier
            .fillMaxSize()
            .testTag("shapes_world_hub"),
        accentTint = ShapesAccent
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .widthIn(max = 920.dp)
                .align(Alignment.TopCenter)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Top Bar
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(8.dp, RoundedCornerShape(26.dp)),
                shape = RoundedCornerShape(26.dp),
                color = Color.White,
                border = BorderStroke(2.5.dp, ShapesAccent)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Bouncy3DButton(
                        text = if (isArabic) "الرئيسية" else "Home",
                        onClick = onNavigateHome,
                        containerColor = LuluPink,
                        shadowColor = LuluPinkDark,
                        contentColor = Color.White,
                        icon = Icons.Filled.Home,
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
                        testTagName = "shapes_hub_home_button"
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        LanguageSelectorSwitch(
                            currentLanguage = uiState.settings.language,
                            onSelectLanguage = onSelectLanguage
                        )

                        Bouncy3DIconButton(
                            icon = if (soundEnabled) Icons.AutoMirrored.Filled.VolumeUp else Icons.AutoMirrored.Filled.VolumeOff,
                            contentDescription = "Sound",
                            onClick = onToggleSound,
                            containerColor = if (soundEnabled) MeadowGreen else Color(0xFFCFD8DC),
                            shadowColor = if (soundEnabled) MeadowGreenDark else Color(0xFF78909C),
                            iconTint = if (soundEnabled) Color.White else DeepNavyText
                        )
                    }
                }
            }

            // Hero Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(10.dp, RoundedCornerShape(30.dp)),
                shape = RoundedCornerShape(30.dp),
                colors = CardDefaults.cardColors(containerColor = ShapesCardBg),
                border = BorderStroke(3.dp, Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(175.dp),
                        shape = RoundedCornerShape(24.dp),
                        border = BorderStroke(2.5.dp, Color.White),
                        shadowElevation = 4.dp
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img_cat_shapes_1794073880569),
                            contentDescription = if (isArabic) "عالم الأشكال" else "Shapes World",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (isArabic) "عالم الأشكال الهندسية" else "Shapes Learning World",
                                color = DeepNavyText,
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = if (isArabic) 27.sp else 24.sp
                                )
                            )
                            Text(
                                text = if (isArabic) "٨ أشكال هندسية ملوّنة مع الأمثلة اليومية" else "8 Colorful 3D Shapes & Real-World Examples",
                                color = SoftSlateText,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(18.dp),
                            color = Color.White,
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
                                    contentDescription = null,
                                    tint = StarGoldBorder,
                                    modifier = Modifier.size(22.dp)
                                )
                                Text(
                                    text = "${completedSet.size} / 4",
                                    color = DeepNavyText,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                )
                            }
                        }
                    }

                    // Progress Bar
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(18.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color.White)
                            .border(1.5.dp, ShapesAccent.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                    ) {
                        val fraction = uiState.settings.shapesWorldProgressFraction.coerceAtLeast(0.08f)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(fraction)
                                .height(18.dp)
                                .background(
                                    Brush.horizontalGradient(
                                        listOf(ShapesAccent, SunnyYellow, Color(0xFF00ACC1))
                                    )
                                )
                        )
                    }

                    // Lulu Welcome Speech Bubble
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSpeakLulu(welcomeText, uiState.settings.language) },
                        shape = RoundedCornerShape(22.dp),
                        color = Color.White,
                        border = BorderStroke(2.dp, SunnyYellowDark)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Surface(
                                modifier = Modifier.size(70.dp),
                                shape = RoundedCornerShape(20.dp),
                                border = BorderStroke(2.5.dp, LuluPink),
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
                                    text = welcomeText,
                                    color = DeepNavyText,
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = if (isArabic) 17.sp else 15.sp
                                    )
                                )
                                Text(
                                    text = if (isArabic) "اضغطي لسماع صوت لولو 🔊" else "Tap to hear Lulu's voice 🔊",
                                    color = LuluPink,
                                    style = MaterialTheme.typography.labelLarge.copy(fontSize = 13.sp)
                                )
                            }
                        }
                    }
                }
            }

            // All 8 Shapes Interactive Grid
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(26.dp),
                color = Color.White,
                border = BorderStroke(2.dp, ShapesAccent.copy(alpha = 0.55f)),
                shadowElevation = 6.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = if (isArabic) {
                            "الأشكال الهندسية الثمانية (اضغطي على أي شكل لتعلّمه!)"
                        } else {
                            "All 8 Geometric Shapes (Tap any shape to learn it!)"
                        },
                        color = DeepNavyText,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = if (isArabic) 18.sp else 16.sp
                        ),
                        textAlign = TextAlign.Center
                    )

                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        maxItemsInEachRow = 4
                    ) {
                        ShapesCurriculum.allShapes.forEach { shape ->
                            Surface(
                                modifier = Modifier
                                    .size(width = 136.dp, height = 122.dp)
                                    .testTag("hub_shape_tile_${shape.id}")
                                    .clickable {
                                        onOpenActivity(ShapesActivityId.LEARN_SHAPES, shape.index)
                                    },
                                shape = RoundedCornerShape(22.dp),
                                color = shape.cardBgColor,
                                border = BorderStroke(2.dp, shape.color),
                                shadowElevation = 4.dp
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(8.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Shape3DCanvasIllustration(
                                        shapeType = shape.type,
                                        fillColor = shape.color,
                                        shadowColor = shape.shadowColor,
                                        modifier = Modifier.size(62.dp)
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "${shape.nameAr} • ${shape.nameEn}",
                                        color = DeepNavyText,
                                        style = MaterialTheme.typography.labelLarge.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp
                                        ),
                                        maxLines = 1
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 4 Interactive Shapes Activities (2-Column Grid)
            Text(
                text = if (isArabic) "أنشطة وألعاب عالم الأشكال (٤ أنشطة تفاعلية)" else "Shapes World Activities (4 Interactive Activities)",
                color = DeepNavyText,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = if (isArabic) 22.sp else 20.sp
                ),
                modifier = Modifier.padding(horizontal = 4.dp)
            )

            ShapesActivityId.entries.chunked(2).forEach { rowItems ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    rowItems.forEach { activity ->
                        val isDone = activity in completedSet
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .testTag("shapes_activity_${activity.key}")
                                .shadow(8.dp, RoundedCornerShape(26.dp))
                                .clickable { onOpenActivity(activity, 1) },
                            shape = RoundedCornerShape(26.dp),
                            color = activity.cardBgColor,
                            border = BorderStroke(2.5.dp, Color.White)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Surface(
                                        shape = RoundedCornerShape(16.dp),
                                        color = activity.accentColor,
                                        border = BorderStroke(1.5.dp, Color.White)
                                    ) {
                                        Text(
                                            text = activity.badgeEmoji,
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                            fontSize = 22.sp
                                        )
                                    }

                                    Surface(
                                        shape = RoundedCornerShape(14.dp),
                                        color = Color.White,
                                        border = BorderStroke(1.5.dp, if (isDone) StarGoldBorder else Color(0xFFCFD8DC))
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            Icon(
                                                imageVector = if (isDone) Icons.Filled.Star else Icons.Filled.StarBorder,
                                                contentDescription = null,
                                                tint = if (isDone) StarGoldBorder else Color(0xFFB0BEC5),
                                                modifier = Modifier.size(18.dp)
                                            )
                                            Text(
                                                text = if (isDone) (if (isArabic) "مكتمل" else "Done") else (if (isArabic) "جديد" else "New"),
                                                color = DeepNavyText,
                                                style = MaterialTheme.typography.labelLarge.copy(fontSize = 12.sp)
                                            )
                                        }
                                    }
                                }

                                Text(
                                    text = activity.title(uiState.settings.language),
                                    color = DeepNavyText,
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = if (isArabic) 20.sp else 18.sp
                                    ),
                                    textAlign = TextAlign.Center,
                                    maxLines = 1
                                )

                                Text(
                                    text = activity.subtitle(uiState.settings.language),
                                    color = SoftSlateText,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = if (isArabic) 14.sp else 13.sp
                                    ),
                                    textAlign = TextAlign.Center,
                                    maxLines = 2
                                )

                                Bouncy3DButton(
                                    text = if (isArabic) "ابدأ النشاط" else "Play Now",
                                    onClick = { onOpenActivity(activity, 1) },
                                    modifier = Modifier.fillMaxWidth(),
                                    containerColor = activity.accentColor,
                                    shadowColor = activity.shadowColor,
                                    contentColor = Color.White,
                                    icon = Icons.Filled.PlayArrow,
                                    fontSize = if (isArabic) 16.sp else 15.sp,
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 10.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun ShapesActivityTopBar(
    title: String,
    accentColor: Color,
    shadowColor: Color,
    isArabic: Boolean,
    soundEnabled: Boolean,
    isCompleted: Boolean,
    onBackToHub: () -> Unit,
    onNavigateHome: () -> Unit,
    onToggleSound: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(26.dp)),
        shape = RoundedCornerShape(26.dp),
        color = Color.White,
        border = BorderStroke(2.5.dp, accentColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Bouncy3DIconButton(
                    icon = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = if (isArabic) "الأشكال" else "Shapes Hub",
                    onClick = onBackToHub,
                    containerColor = accentColor,
                    shadowColor = shadowColor,
                    iconTint = Color.White,
                    badgeLabel = if (isArabic) "الأشكال" else "Shapes",
                    testTagName = "back_to_shapes_hub_btn"
                )

                Bouncy3DIconButton(
                    icon = Icons.Filled.Home,
                    contentDescription = if (isArabic) "الرئيسية" else "Home",
                    onClick = onNavigateHome,
                    containerColor = LuluPink,
                    shadowColor = LuluPinkDark,
                    iconTint = Color.White
                )
            }

            Text(
                text = title,
                color = DeepNavyText,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = if (isArabic) 20.sp else 18.sp
                ),
                maxLines = 1
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                if (isCompleted) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = null,
                        tint = StarGoldBorder,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Bouncy3DIconButton(
                    icon = if (soundEnabled) Icons.AutoMirrored.Filled.VolumeUp else Icons.AutoMirrored.Filled.VolumeOff,
                    contentDescription = "Sound",
                    onClick = onToggleSound,
                    containerColor = if (soundEnabled) MeadowGreen else Color(0xFFCFD8DC),
                    shadowColor = if (soundEnabled) MeadowGreenDark else Color(0xFF78909C),
                    iconTint = if (soundEnabled) Color.White else DeepNavyText
                )
            }
        }
    }
}

/**
 * ACTIVITY 1: LEARN SHAPES (تعرّف على الأشكال)
 * Shows each shape as a large colorful 3D illustration, displays its Arabic & English name,
 * and plays its pronunciation when tapped.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun LearnShapesActivityScreen(
    initialShapeIndex: Int,
    appLanguage: AppLanguage,
    soundEnabled: Boolean,
    isCompleted: Boolean,
    onBackToHub: () -> Unit,
    onNavigateHome: () -> Unit,
    onToggleSound: () -> Unit,
    onSpeakLulu: (String, AppLanguage) -> Unit,
    onMarkCompleted: () -> Unit
) {
    val isArabic = appLanguage == AppLanguage.AR
    val allShapes = ShapesCurriculum.allShapes
    var currentIndex by remember { mutableIntStateOf(initialShapeIndex.coerceIn(1, allShapes.size)) }
    var visitedCount by remember { mutableIntStateOf(1) }

    val shape = allShapes[(currentIndex - 1).coerceIn(0, allShapes.lastIndex)]

    LaunchedEffect(shape.id, appLanguage) {
        onSpeakLulu(shape.luluIntro(appLanguage), appLanguage)
        if (visitedCount >= 3) {
            onMarkCompleted()
        }
    }

    PlayfulSkyBackground(accentTint = shape.color) {
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
            ShapesActivityTopBar(
                title = if (isArabic) "تعرّف على الأشكال" else "Learn Shapes",
                accentColor = shape.color,
                shadowColor = shape.shadowColor,
                isArabic = isArabic,
                soundEnabled = soundEnabled,
                isCompleted = isCompleted,
                onBackToHub = onBackToHub,
                onNavigateHome = onNavigateHome,
                onToggleSound = onToggleSound
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(12.dp, RoundedCornerShape(32.dp)),
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(3.dp, shape.color)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Large Colorful 3D Shape Illustration + Arabic & English Names
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            modifier = Modifier
                                .size(170.dp)
                                .clickable { onSpeakLulu(shape.luluIntro(appLanguage), appLanguage) },
                            shape = RoundedCornerShape(32.dp),
                            color = shape.cardBgColor,
                            border = BorderStroke(2.5.dp, shape.color)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Shape3DCanvasIllustration(
                                    shapeType = shape.type,
                                    fillColor = shape.color,
                                    shadowColor = shape.shadowColor,
                                    modifier = Modifier.size(136.dp)
                                )
                            }
                        }

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = shape.nameAr,
                                color = DeepNavyText,
                                style = MaterialTheme.typography.displayMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 38.sp
                                )
                            )

                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = shape.cardBgColor,
                                border = BorderStroke(1.5.dp, shape.color)
                            ) {
                                Text(
                                    text = shape.nameEn,
                                    color = DeepNavyText,
                                    style = MaterialTheme.typography.headlineSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 24.sp
                                    ),
                                    modifier = Modifier.padding(horizontal = 18.dp, vertical = 6.dp)
                                )
                            }

                            Bouncy3DButton(
                                text = if (isArabic) "استمع للنطق 🔊" else "Hear Shape 🔊",
                                onClick = {
                                    onSpeakLulu("${shape.nameAr}. ${shape.nameEn}. ${shape.luluIntro(appLanguage)}", appLanguage)
                                },
                                containerColor = SunnyYellow,
                                shadowColor = SunnyYellowDark,
                                contentColor = DeepNavyText,
                                icon = Icons.AutoMirrored.Filled.VolumeUp,
                                fontSize = 15.sp,
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                            )
                        }
                    }

                    // Description & Everyday Object Card
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSpeakLulu(shape.luluIntro(appLanguage), appLanguage) },
                        shape = RoundedCornerShape(24.dp),
                        color = shape.cardBgColor,
                        border = BorderStroke(2.dp, shape.color)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = shape.description(appLanguage),
                                color = DeepNavyText,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                ),
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = shape.realWorldExample(appLanguage),
                                color = shape.color,
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp
                                ),
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    // Previous / Next Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Bouncy3DButton(
                            text = if (isArabic) "الشكل السابق" else "Previous",
                            onClick = {
                                currentIndex = if (currentIndex > 1) currentIndex - 1 else allShapes.size
                                visitedCount++
                            },
                            modifier = Modifier.weight(1f),
                            containerColor = Color(0xFFECEFF1),
                            shadowColor = Color(0xFF90A4AE),
                            contentColor = DeepNavyText,
                            icon = Icons.AutoMirrored.Filled.ArrowBack,
                            fontSize = 16.sp
                        )

                        Bouncy3DButton(
                            text = if (isArabic) "الشكل التالي" else "Next Shape",
                            onClick = {
                                currentIndex = if (currentIndex < allShapes.size) currentIndex + 1 else 1
                                visitedCount++
                            },
                            modifier = Modifier.weight(1f),
                            containerColor = MeadowGreen,
                            shadowColor = MeadowGreenDark,
                            contentColor = Color.White,
                            icon = Icons.AutoMirrored.Filled.ArrowForward,
                            fontSize = 16.sp
                        )
                    }
                }
            }

            // Quick 8-Shape Selector Strip
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                color = Color.White,
                border = BorderStroke(2.dp, shape.color.copy(alpha = 0.45f))
            ) {
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    allShapes.forEach { item ->
                        val isSelected = item.index == currentIndex
                        Surface(
                            modifier = Modifier
                                .size(58.dp)
                                .clickable {
                                    currentIndex = item.index
                                    visitedCount++
                                },
                            shape = RoundedCornerShape(16.dp),
                            color = if (isSelected) item.cardBgColor else Color(0xFFF5F7FA),
                            border = BorderStroke(if (isSelected) 2.5.dp else 1.5.dp, item.color)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Shape3DCanvasIllustration(
                                    shapeType = item.type,
                                    fillColor = item.color,
                                    shadowColor = item.shadowColor,
                                    modifier = Modifier.size(40.dp),
                                    showCuteFace = false
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
 * ACTIVITY 2: IDENTIFY THE SHAPE (أين هو الشكل؟)
 * Asks "أين الدائرة؟" / "Where is the Circle?" and provides 4 custom 3D shapes to choose from.
 */
@Composable
private fun IdentifyShapeActivityScreen(
    appLanguage: AppLanguage,
    soundEnabled: Boolean,
    isCompleted: Boolean,
    onBackToHub: () -> Unit,
    onNavigateHome: () -> Unit,
    onToggleSound: () -> Unit,
    onSpeakLulu: (String, AppLanguage) -> Unit,
    onMarkCompleted: () -> Unit
) {
    val isArabic = appLanguage == AppLanguage.AR
    val allShapes = ShapesCurriculum.allShapes

    var currentRound by remember { mutableIntStateOf(0) }
    var starsInSession by remember { mutableIntStateOf(0) }
    var selectedId by remember(currentRound) { mutableStateOf<String?>(null) }
    var isCorrect by remember(currentRound) { mutableStateOf<Boolean?>(null) }

    val targetShape = allShapes[currentRound % allShapes.size]
    val options: List<ShapeLessonItem> = remember(targetShape.id) {
        val distractors = allShapes.filter { it.id != targetShape.id }.shuffled().take(3)
        (distractors + targetShape).shuffled()
    }

    LaunchedEffect(targetShape.id, appLanguage) {
        onSpeakLulu(targetShape.question(appLanguage), appLanguage)
    }

    val meta = ShapesActivityId.IDENTIFY_SHAPE

    PlayfulSkyBackground(accentTint = meta.accentColor) {
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
            ShapesActivityTopBar(
                title = meta.title(appLanguage),
                accentColor = meta.accentColor,
                shadowColor = meta.shadowColor,
                isArabic = isArabic,
                soundEnabled = soundEnabled,
                isCompleted = isCompleted,
                onBackToHub = onBackToHub,
                onNavigateHome = onNavigateHome,
                onToggleSound = onToggleSound
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(12.dp, RoundedCornerShape(32.dp)),
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(3.dp, meta.accentColor)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSpeakLulu(targetShape.question(appLanguage), appLanguage) },
                        shape = RoundedCornerShape(24.dp),
                        color = meta.cardBgColor,
                        border = BorderStroke(2.dp, meta.accentColor)
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

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = targetShape.question(appLanguage),
                                    color = DeepNavyText,
                                    style = MaterialTheme.typography.headlineMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = if (isArabic) 26.sp else 22.sp
                                    )
                                )
                                Text(
                                    text = "${targetShape.nameAr} • ${targetShape.nameEn}",
                                    color = SoftSlateText,
                                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                                )
                            }

                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                                contentDescription = "Replay",
                                tint = meta.accentColor,
                                modifier = Modifier.size(30.dp)
                            )
                        }
                    }

                    // 4 Shape Choices (2x2 Grid)
                    options.chunked(2).forEach { rowItems ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            rowItems.forEach { item ->
                                val isSel = selectedId == item.id
                                val isTarget = item.id == targetShape.id

                                Surface(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(140.dp)
                                        .testTag("identify_shape_option_${item.id}")
                                        .shadow(8.dp, RoundedCornerShape(26.dp))
                                        .clickable {
                                            selectedId = item.id
                                            if (isTarget) {
                                                if (isCorrect != true) {
                                                    starsInSession++
                                                    if (starsInSession >= 2) {
                                                        onMarkCompleted()
                                                    }
                                                }
                                                isCorrect = true
                                                onSpeakLulu(targetShape.luluIntro(appLanguage), appLanguage)
                                            } else {
                                                isCorrect = false
                                                val retry = if (isArabic) {
                                                    "حاولي مرة أخرى يا بطلة! ابحثي عن شكل ${targetShape.nameAr}."
                                                } else {
                                                    "Try again superstar! Look for the ${targetShape.nameEn}."
                                                }
                                                onSpeakLulu(retry, appLanguage)
                                            }
                                        },
                                    shape = RoundedCornerShape(26.dp),
                                    color = if (isSel && isTarget) Color(0xFFE8F5E9) else item.cardBgColor,
                                    border = BorderStroke(
                                        width = if (isSel) 4.dp else 2.5.dp,
                                        color = when {
                                            isSel && isTarget -> MeadowGreen
                                            isSel && !isTarget -> Color(0xFFE53935)
                                            else -> item.color
                                        }
                                    )
                                ) {
                                    Column(
                                        modifier = Modifier.fillMaxSize(),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Shape3DCanvasIllustration(
                                            shapeType = item.type,
                                            fillColor = item.color,
                                            shadowColor = item.shadowColor,
                                            modifier = Modifier.size(84.dp)
                                        )
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(
                                            text = if (isArabic) item.nameAr else item.nameEn,
                                            color = DeepNavyText,
                                            style = MaterialTheme.typography.titleMedium.copy(
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 18.sp
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }

                    AnimatedVisibility(
                        visible = isCorrect != null,
                        enter = fadeIn() + scaleIn(),
                        exit = fadeOut() + scaleOut()
                    ) {
                        val right = isCorrect == true
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(22.dp),
                            color = if (right) Color(0xFFE8F5E9) else Color(0xFFFFF3E0),
                            border = BorderStroke(2.dp, if (right) MeadowGreen else SunnyYellowDark)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = if (right) {
                                        if (isArabic) "أحسنتِ! هذا شكل ${targetShape.nameAr} ⭐" else "Great job! That is the ${targetShape.nameEn} ⭐"
                                    } else {
                                        if (isArabic) "قريب جداً! جربي شكلاً آخر 💪" else "Almost! Try tapping another shape 💪"
                                    },
                                    color = DeepNavyText,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    modifier = Modifier.weight(1f)
                                )

                                if (right) {
                                    Bouncy3DButton(
                                        text = if (isArabic) "التالي" else "Next",
                                        onClick = { currentRound = (currentRound + 1) % allShapes.size },
                                        containerColor = MeadowGreen,
                                        shadowColor = MeadowGreenDark,
                                        contentColor = Color.White,
                                        icon = Icons.AutoMirrored.Filled.ArrowForward,
                                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp)
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
 * ACTIVITY 3: MATCH SHAPES (طابق الأشكال المتماثلة)
 * Match identical shapes across two columns by tapping.
 */
@Composable
private fun MatchShapesActivityScreen(
    appLanguage: AppLanguage,
    soundEnabled: Boolean,
    isCompleted: Boolean,
    onBackToHub: () -> Unit,
    onNavigateHome: () -> Unit,
    onToggleSound: () -> Unit,
    onSpeakLulu: (String, AppLanguage) -> Unit,
    onMarkCompleted: () -> Unit
) {
    val isArabic = appLanguage == AppLanguage.AR
    var roundBatch by remember { mutableIntStateOf(0) }

    val leftShapes = remember(roundBatch) {
        val offset = (roundBatch * 4) % ShapesCurriculum.allShapes.size
        (0 until 4).map { idx ->
            ShapesCurriculum.allShapes[(offset + idx) % ShapesCurriculum.allShapes.size]
        }
    }
    val rightShapes = remember(roundBatch) { leftShapes.shuffled() }

    var selectedShapeId by remember(roundBatch) { mutableStateOf<String?>(null) }
    var matchedIds by remember(roundBatch) { mutableStateOf<Set<String>>(emptySet()) }

    val meta = ShapesActivityId.MATCH_SHAPES

    LaunchedEffect(roundBatch) {
        onSpeakLulu(
            if (isArabic) "اضغطي على الشكل ثم اضغطي على الشكل المطابق له!" else "Tap a shape, then tap the identical shape on the other side!",
            appLanguage
        )
    }

    PlayfulSkyBackground(accentTint = meta.accentColor) {
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
            ShapesActivityTopBar(
                title = meta.title(appLanguage),
                accentColor = meta.accentColor,
                shadowColor = meta.shadowColor,
                isArabic = isArabic,
                soundEnabled = soundEnabled,
                isCompleted = isCompleted,
                onBackToHub = onBackToHub,
                onNavigateHome = onNavigateHome,
                onToggleSound = onToggleSound
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(12.dp, RoundedCornerShape(32.dp)),
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(3.dp, meta.accentColor)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        text = if (isArabic) {
                            "طابقي الأشكال المتماثلة (${matchedIds.size} / 4)"
                        } else {
                            "Match Identical Shapes (${matchedIds.size} / 4)"
                        },
                        color = DeepNavyText,
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        textAlign = TextAlign.Center
                    )

                    leftShapes.indices.forEach { rowIdx ->
                        val leftItem = leftShapes[rowIdx]
                        val rightItem = rightShapes[rowIdx]
                        val leftDone = leftItem.id in matchedIds
                        val rightDone = rightItem.id in matchedIds
                        val leftSel = selectedShapeId == leftItem.id

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Left Column Shape
                            Surface(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(92.dp)
                                    .clickable(enabled = !leftDone) {
                                        selectedShapeId = leftItem.id
                                        onSpeakLulu(leftItem.name(appLanguage), appLanguage)
                                    },
                                shape = RoundedCornerShape(22.dp),
                                color = if (leftDone) Color(0xFFE8F5E9) else leftItem.cardBgColor,
                                border = BorderStroke(
                                    width = if (leftSel) 4.dp else 2.5.dp,
                                    color = when {
                                        leftDone -> MeadowGreen
                                        leftSel -> SunnyYellowDark
                                        else -> leftItem.color
                                    }
                                )
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(horizontal = 12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Shape3DCanvasIllustration(
                                        shapeType = leftItem.type,
                                        fillColor = leftItem.color,
                                        shadowColor = leftItem.shadowColor,
                                        modifier = Modifier.size(60.dp)
                                    )
                                    Spacer(modifier = Modifier.size(8.dp))
                                    Text(
                                        text = leftItem.name(appLanguage),
                                        color = DeepNavyText,
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                    )
                                }
                            }

                            // Right Column Identical Shape Silhouette / Partner
                            Surface(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(92.dp)
                                    .clickable(enabled = !rightDone) {
                                        val sel = selectedShapeId
                                        if (sel == null) {
                                            selectedShapeId = rightItem.id
                                            onSpeakLulu(rightItem.name(appLanguage), appLanguage)
                                        } else if (sel == rightItem.id) {
                                            val updated = matchedIds + rightItem.id
                                            matchedIds = updated
                                            selectedShapeId = null
                                            if (updated.size >= 4) {
                                                onMarkCompleted()
                                                onSpeakLulu(
                                                    if (isArabic) "ممتاز يا بطلة! طابقتِ كل الأشكال بنجاح!" else "Wonderful! You matched all shapes!",
                                                    appLanguage
                                                )
                                            } else {
                                                onSpeakLulu(
                                                    if (isArabic) "أحسنتِ! ${rightItem.nameAr}" else "Great job! ${rightItem.nameEn}",
                                                    appLanguage
                                                )
                                            }
                                        } else {
                                            onSpeakLulu(
                                                if (isArabic) "حاولي مرة أخرى! ابحثي عن الشكل المطابق." else "Try again! Tap the matching shape.",
                                                appLanguage
                                            )
                                        }
                                    },
                                shape = RoundedCornerShape(22.dp),
                                color = if (rightDone) Color(0xFFE8F5E9) else Color(0xFFF8FAFC),
                                border = BorderStroke(
                                    width = 2.5.dp,
                                    color = if (rightDone) MeadowGreen else rightItem.color
                                )
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Shape3DCanvasIllustration(
                                        shapeType = rightItem.type,
                                        fillColor = rightItem.color,
                                        shadowColor = rightItem.shadowColor,
                                        modifier = Modifier.size(64.dp),
                                        showCuteFace = rightDone
                                    )
                                }
                            }
                        }
                    }

                    if (matchedIds.size >= 4) {
                        Bouncy3DButton(
                            text = if (isArabic) "مجموعة أشكال جديدة 🌟" else "More Shapes 🌟",
                            onClick = { roundBatch++ },
                            containerColor = MeadowGreen,
                            shadowColor = MeadowGreenDark,
                            contentColor = Color.White,
                            icon = Icons.Filled.Refresh
                        )
                    }
                }
            }
        }
    }
}

/**
 * ACTIVITY 4: SHAPES AROUND US (الأشكال من حولنا)
 * Shows everyday objects (Ball = Circle, Pizza slice = Triangle, Window = Square, Door = Rectangle, etc.)
 * and asks the child to identify their shapes.
 */
@Composable
private fun ShapesAroundUsActivityScreen(
    appLanguage: AppLanguage,
    soundEnabled: Boolean,
    isCompleted: Boolean,
    onBackToHub: () -> Unit,
    onNavigateHome: () -> Unit,
    onToggleSound: () -> Unit,
    onSpeakLulu: (String, AppLanguage) -> Unit,
    onMarkCompleted: () -> Unit
) {
    val isArabic = appLanguage == AppLanguage.AR
    val questions = ShapesCurriculum.everydayQuestions

    var currentIdx by remember { mutableIntStateOf(0) }
    var solvedCount by remember { mutableIntStateOf(0) }
    var selectedType by remember(currentIdx) { mutableStateOf<ShapeType?>(null) }
    var isSolved by remember(currentIdx) { mutableStateOf(false) }

    val q = questions[currentIdx % questions.size]
    val meta = ShapesActivityId.SHAPES_AROUND_US

    LaunchedEffect(q.id, appLanguage) {
        onSpeakLulu(if (isArabic) q.promptAr else q.promptEn, appLanguage)
    }

    PlayfulSkyBackground(accentTint = meta.accentColor) {
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
            ShapesActivityTopBar(
                title = meta.title(appLanguage),
                accentColor = meta.accentColor,
                shadowColor = meta.shadowColor,
                isArabic = isArabic,
                soundEnabled = soundEnabled,
                isCompleted = isCompleted,
                onBackToHub = onBackToHub,
                onNavigateHome = onNavigateHome,
                onToggleSound = onToggleSound
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(12.dp, RoundedCornerShape(32.dp)),
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(3.dp, meta.accentColor)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Everyday Object Showcase
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSpeakLulu(if (isArabic) q.promptAr else q.promptEn, appLanguage) },
                        shape = RoundedCornerShape(28.dp),
                        color = meta.cardBgColor,
                        border = BorderStroke(2.5.dp, meta.accentColor)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(text = q.objectEmoji, fontSize = 72.sp)
                            Text(
                                text = if (isArabic) q.objectNameAr else q.objectNameEn,
                                color = meta.accentColor,
                                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = if (isArabic) q.promptAr else q.promptEn,
                                color = DeepNavyText,
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = if (isArabic) 24.sp else 20.sp
                                ),
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    // 3 Shape Options
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        q.options.forEach { shapeType ->
                            val shapeItem = ShapesCurriculum.byType(shapeType)
                            val isSel = selectedType == shapeType
                            val isRight = shapeType == q.targetShape

                            Surface(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(136.dp)
                                    .testTag("everyday_shape_option_${shapeItem.id}")
                                    .shadow(8.dp, RoundedCornerShape(24.dp))
                                    .clickable {
                                        selectedType = shapeType
                                        if (isRight) {
                                            if (!isSolved) {
                                                solvedCount++
                                                if (solvedCount >= 2) {
                                                    onMarkCompleted()
                                                }
                                            }
                                            isSolved = true
                                            val praise = if (isArabic) {
                                                "أحسنتِ! ${q.objectNameAr} على شكل ${shapeItem.nameAr}!"
                                            } else {
                                                "Great job! ${q.objectNameEn} is shaped like a ${shapeItem.nameEn}!"
                                            }
                                            onSpeakLulu(praise, appLanguage)
                                        } else {
                                            val retry = if (isArabic) {
                                                "انظري جيداً إلى حواف ${q.objectNameAr} وجربي شكلاً آخر!"
                                            } else {
                                                "Look closely at the ${q.objectNameEn} and try another shape!"
                                            }
                                            onSpeakLulu(retry, appLanguage)
                                        }
                                    },
                                shape = RoundedCornerShape(24.dp),
                                color = if (isSel && isRight) Color(0xFFE8F5E9) else shapeItem.cardBgColor,
                                border = BorderStroke(
                                    width = if (isSel) 4.dp else 2.5.dp,
                                    color = when {
                                        isSel && isRight -> MeadowGreen
                                        isSel && !isRight -> Color(0xFFE53935)
                                        else -> shapeItem.color
                                    }
                                )
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Shape3DCanvasIllustration(
                                        shapeType = shapeItem.type,
                                        fillColor = shapeItem.color,
                                        shadowColor = shapeItem.shadowColor,
                                        modifier = Modifier.size(72.dp)
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = shapeItem.name(appLanguage),
                                        color = DeepNavyText,
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 17.sp
                                        )
                                    )
                                }
                            }
                        }
                    }

                    AnimatedVisibility(
                        visible = isSolved,
                        enter = fadeIn() + scaleIn(),
                        exit = fadeOut() + scaleOut()
                    ) {
                        val rightShape = ShapesCurriculum.byType(q.targetShape)
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
                                    text = if (isArabic) {
                                        "رائع! ${q.objectEmoji} ${q.objectNameAr} = ${rightShape.nameAr} ⭐"
                                    } else {
                                        "Awesome! ${q.objectEmoji} ${q.objectNameEn} = ${rightShape.nameEn} ⭐"
                                    },
                                    color = DeepNavyText,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    modifier = Modifier.weight(1f)
                                )

                                Bouncy3DButton(
                                    text = if (isArabic) "السؤال التالي" else "Next",
                                    onClick = { currentIdx = (currentIdx + 1) % questions.size },
                                    containerColor = MeadowGreen,
                                    shadowColor = MeadowGreenDark,
                                    contentColor = Color.White,
                                    icon = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
