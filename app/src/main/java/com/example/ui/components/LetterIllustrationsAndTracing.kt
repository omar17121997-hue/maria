package com.example.ui.components

import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.model.LetterLessonItem
import com.example.model.LettersSectionMode
import com.example.ui.theme.DeepNavyText
import com.example.ui.theme.MeadowGreen
import com.example.ui.theme.SoftSlateText
import com.example.ui.theme.StarGoldBorder
import com.example.ui.theme.SunnyYellow
import kotlin.math.hypot

/**
 * Animated 3D Alphabet Block that introduces a letter with a bouncy spring animation,
 * glossy 3D lighting, clear dot indicator for Arabic letters, and Uppercase + Lowercase
 * display for English letters.
 */
@Composable
fun AnimatedLetter3DBlock(
    lesson: LetterLessonItem,
    isLearned: Boolean,
    onTapSpeak: () -> Unit,
    modifier: Modifier = Modifier
) {
    var appeared by remember(lesson.id) { mutableStateOf(false) }
    LaunchedEffect(lesson.id) {
        appeared = true
    }

    val entryScale by animateFloatAsState(
        targetValue = if (appeared) 1f else 0.72f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "letter_block_entry_scale"
    )

    val infiniteTransition = rememberInfiniteTransition(label = "letter_float")
    val floatOffset by infiniteTransition.animateFloat(
        initialValue = -3f,
        targetValue = 3f,
        animationSpec = infiniteRepeatable(
            animation = tween(1600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "letter_float_offset"
    )

    Box(
        modifier = modifier
            .scale(entryScale)
            .clickable(onClick = onTapSpeak),
        contentAlignment = Alignment.Center
    ) {
        // 3D Bottom Extrusion Layer
        Box(
            modifier = Modifier
                .size(164.dp)
                .padding(top = 8.dp)
                .clip(RoundedCornerShape(34.dp))
                .background(lesson.shadowColor)
        )

        // Main Glossy 3D Block Face
        Surface(
            modifier = Modifier
                .size(156.dp)
                .padding(bottom = 4.dp)
                .shadow(10.dp, RoundedCornerShape(34.dp)),
            shape = RoundedCornerShape(34.dp),
            color = lesson.color,
            border = BorderStroke(4.dp, Color.White)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                // Decorative 3D glossy highlight
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawRoundRect(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.35f),
                                Color.Transparent
                            )
                        ),
                        topLeft = Offset(12f, 10f),
                        size = Size(size.width - 24f, size.height * 0.42f),
                        cornerRadius = CornerRadius(42f, 42f)
                    )
                    drawCircle(
                        color = Color.White.copy(alpha = 0.45f),
                        radius = 12f,
                        center = Offset(28f, 28f + floatOffset)
                    )
                }

                if (lesson.section == LettersSectionMode.ARABIC) {
                    Text(
                        text = lesson.isolatedSymbol,
                        color = Color.White,
                        style = MaterialTheme.typography.displayLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 78.sp
                        ),
                        textAlign = TextAlign.Center
                    )
                } else {
                    Row(
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = lesson.uppercaseSymbol,
                            color = Color.White,
                            style = MaterialTheme.typography.displayLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 66.sp
                            )
                        )
                        Text(
                            text = lesson.lowercaseSymbol,
                            color = SunnyYellow,
                            style = MaterialTheme.typography.displayMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 52.sp
                            )
                        )
                    }
                }

                if (isLearned) {
                    Surface(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp),
                        shape = CircleShape,
                        color = Color.White,
                        border = BorderStroke(1.5.dp, MeadowGreen)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.CheckCircle,
                            contentDescription = null,
                            tint = MeadowGreen,
                            modifier = Modifier
                                .padding(2.dp)
                                .size(22.dp)
                        )
                    }
                }
            }
        }
    }
}

/**
 * Rich 3D Cartoon Illustration Card showing the word that begins with [lesson],
 * with a glowing 3D stage, clear word typography highlighting the starting letter,
 * and tap-to-hear interaction.
 */
@Composable
fun LetterWord3DIllustrationCard(
    lesson: LetterLessonItem,
    appLanguage: AppLanguage,
    onTapSpeakWord: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isArabicApp = appLanguage == AppLanguage.AR

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(28.dp))
            .clickable(onClick = onTapSpeakWord),
        shape = RoundedCornerShape(28.dp),
        color = lesson.cardBgColor,
        border = BorderStroke(2.5.dp, lesson.color.copy(alpha = 0.55f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 3D Cartoon Picture Stage
            Box(
                modifier = Modifier.size(116.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    val h = size.height
                    // Soft 3D pedestal shadow
                    drawOval(
                        color = lesson.shadowColor.copy(alpha = 0.22f),
                        topLeft = Offset(w * 0.14f, h * 0.76f),
                        size = Size(w * 0.72f, h * 0.16f)
                    )
                    // Glowing pastel circle backdrop
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(Color.White, lesson.color.copy(alpha = 0.25f))
                        ),
                        radius = w * 0.45f,
                        center = Offset(w / 2f, h * 0.46f)
                    )
                    drawCircle(
                        color = Color.White,
                        radius = w * 0.45f,
                        center = Offset(w / 2f, h * 0.46f),
                        style = Stroke(width = 6f)
                    )
                    // Decorative 3D stars around illustration
                    drawCircle(
                        color = SunnyYellow,
                        radius = 8f,
                        center = Offset(w * 0.16f, h * 0.18f)
                    )
                    drawCircle(
                        color = lesson.color,
                        radius = 6f,
                        center = Offset(w * 0.85f, h * 0.22f)
                    )
                }

                Text(
                    text = lesson.emoji,
                    fontSize = 58.sp,
                    modifier = Modifier.padding(bottom = 6.dp)
                )
            }

            // Word + Highlighted Initial Letter + Speaker Badge
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White,
                    border = BorderStroke(1.5.dp, lesson.color.copy(alpha = 0.4f))
                ) {
                    Text(
                        text = if (lesson.section == LettersSectionMode.ARABIC) {
                            "كلمة تبدأ بحرف ال${lesson.letterNameAr}"
                        } else {
                            if (isArabicApp) {
                                "كلمة تبدأ بحرف ${lesson.uppercaseSymbol}"
                            } else {
                                "${lesson.uppercaseSymbol} is for..."
                            }
                        },
                        color = lesson.color,
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        ),
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }

                // Word with highlighted first letter
                val primaryWord = lesson.primaryWord
                val highlightedWord = buildAnnotatedString {
                    if (primaryWord.isNotEmpty()) {
                        withStyle(
                            SpanStyle(
                                color = lesson.color,
                                fontWeight = FontWeight.ExtraBold
                            )
                        ) {
                            append(primaryWord.first())
                        }
                        withStyle(
                            SpanStyle(
                                color = DeepNavyText,
                                fontWeight = FontWeight.Bold
                            )
                        ) {
                            append(primaryWord.substring(1))
                        }
                    }
                }

                Text(
                    text = highlightedWord,
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontSize = 34.sp,
                        fontWeight = FontWeight.Bold
                    )
                )

                Text(
                    text = lesson.secondaryWordMeaning,
                    color = SoftSlateText,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                )

                Spacer(modifier = Modifier.height(2.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = lesson.color
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier
                                .padding(6.dp)
                                .size(18.dp)
                        )
                    }
                    Text(
                        text = if (isArabicApp) "اضغط لسماع الحرف والكلمة 🔊" else "Tap to hear letter & word 🔊",
                        color = lesson.color,
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
 * Interactive Finger Tracing Surface for Activity 2 (Letter Tracing).
 * - Displays a large dotted letter (isolated Arabic letter with clear dots, or English Uppercase/Lowercase)
 * - Displays numbered stroke guide checkpoints (1 -> 2 -> 3 -> 4) respecting Arabic RTL or English LTR stroke order
 * - Allows the child to trace using their finger on mobile phones and tablets
 */
@Composable
fun LetterFingerTracingPad(
    lesson: LetterLessonItem,
    traceSymbol: String,
    strokes: List<List<Offset>>,
    hitCheckpoints: Set<Int>,
    onAddPoint: (Offset, Size) -> Unit,
    onStartNewStroke: (Offset, Size) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(300.dp)
            .clip(RoundedCornerShape(28.dp))
            .background(Color(0xFFFFFDF7))
            .border(3.dp, lesson.color, RoundedCornerShape(28.dp))
            .testTag("letter_tracing_canvas")
            .pointerInput(lesson.id, traceSymbol) {
                detectTapGestures { offset ->
                    val canvasSize = Size(size.width.toFloat(), size.height.toFloat())
                    onStartNewStroke(offset, canvasSize)
                }
            }
            .pointerInput(lesson.id, traceSymbol) {
                detectDragGestures(
                    onDragStart = { offset ->
                        val canvasSize = Size(size.width.toFloat(), size.height.toFloat())
                        onStartNewStroke(offset, canvasSize)
                    },
                    onDrag = { change, _ ->
                        change.consume()
                        val canvasSize = Size(size.width.toFloat(), size.height.toFloat())
                        onAddPoint(change.position, canvasSize)
                    }
                )
            }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // 1. Primary school writing guide lines
            val guideColor = Color(0xFFE1F5FE)
            drawLine(guideColor, Offset(24f, h * 0.22f), Offset(w - 24f, h * 0.22f), strokeWidth = 3f)
            drawLine(
                color = Color(0xFF81D4FA),
                start = Offset(24f, h * 0.62f),
                end = Offset(w - 24f, h * 0.62f),
                strokeWidth = 4f
            )
            drawLine(guideColor, Offset(24f, h * 0.84f), Offset(w - 24f, h * 0.84f), strokeWidth = 3f)

            // 2. Large Dotted Guide Letter in Center
            drawContext.canvas.nativeCanvas.apply {
                val textY = h * 0.68f
                val fontSizePx = if (traceSymbol.length > 2) h * 0.50f else h * 0.62f

                val fillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    color = lesson.color.copy(alpha = 0.14f).toArgb()
                    textSize = fontSizePx
                    textAlign = Paint.Align.CENTER
                    typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                }
                drawText(traceSymbol, w / 2f, textY, fillPaint)

                val dottedPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    color = lesson.color.copy(alpha = 0.72f).toArgb()
                    style = Paint.Style.STROKE
                    strokeWidth = 9f
                    pathEffect = DashPathEffect(floatArrayOf(18f, 14f), 0f)
                    textSize = fontSizePx
                    textAlign = Paint.Align.CENTER
                    typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                }
                drawText(traceSymbol, w / 2f, textY, dottedPaint)
            }

            // 3. Directional Guide Lines & Numbered Checkpoints (1 -> 2 -> 3 -> 4)
            val checkpoints = lesson.traceCheckpoints
            for (i in 0 until checkpoints.size - 1) {
                val startPt = Offset(checkpoints[i].x * w, checkpoints[i].y * h)
                val endPt = Offset(checkpoints[i + 1].x * w, checkpoints[i + 1].y * h)
                drawLine(
                    color = lesson.color.copy(alpha = 0.28f),
                    start = startPt,
                    end = endPt,
                    strokeWidth = 6f,
                    cap = StrokeCap.Round
                )
            }

            checkpoints.forEachIndexed { idx, normPt ->
                val pt = Offset(normPt.x * w, normPt.y * h)
                val isHit = idx in hitCheckpoints
                val dotRadius = if (idx == 0) 22f else 17f
                val dotColor = when {
                    isHit -> Color(0xFF43A047)
                    idx == 0 -> Color(0xFFFFB300)
                    else -> lesson.color.copy(alpha = 0.58f)
                }
                drawCircle(color = Color.White, radius = dotRadius + 4f, center = pt)
                drawCircle(color = dotColor, radius = dotRadius, center = pt)

                // Draw step number inside checkpoint
                drawContext.canvas.nativeCanvas.apply {
                    val numPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                        color = android.graphics.Color.WHITE
                        textSize = 24f
                        textAlign = Paint.Align.CENTER
                        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                    }
                    drawText("${idx + 1}", pt.x, pt.y + 8f, numPaint)
                }
            }

            // 4. Child's Finger-Traced Strokes
            strokes.forEach { strokePoints ->
                if (strokePoints.size == 1) {
                    drawCircle(
                        color = lesson.color,
                        radius = 22f,
                        center = strokePoints.first()
                    )
                } else if (strokePoints.size > 1) {
                    val path = Path().apply {
                        moveTo(strokePoints.first().x, strokePoints.first().y)
                        for (i in 1 until strokePoints.size) {
                            lineTo(strokePoints[i].x, strokePoints[i].y)
                        }
                    }
                    drawPath(
                        path = path,
                        color = SunnyYellow.copy(alpha = 0.55f),
                        style = Stroke(width = 48f, cap = StrokeCap.Round, join = StrokeJoin.Round)
                    )
                    drawPath(
                        path = path,
                        color = lesson.color,
                        style = Stroke(width = 36f, cap = StrokeCap.Round, join = StrokeJoin.Round)
                    )
                }
            }
        }

        // Top-Start Progress Pill
        Surface(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(12.dp),
            shape = RoundedCornerShape(14.dp),
            color = Color.White.copy(alpha = 0.94f),
            border = BorderStroke(1.5.dp, StarGoldBorder)
        ) {
            Text(
                text = "✏️ ${hitCheckpoints.size} / ${lesson.traceCheckpoints.size}",
                color = DeepNavyText,
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                ),
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
            )
        }

        // Top-End Direction Indicator Pill
        Surface(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(12.dp),
            shape = RoundedCornerShape(14.dp),
            color = Color.White.copy(alpha = 0.94f),
            border = BorderStroke(1.5.dp, lesson.color.copy(alpha = 0.5f))
        ) {
            Text(
                text = if (lesson.section == LettersSectionMode.ARABIC) {
                    "⬅️ اتجاه الكتابة: من اليمين"
                } else {
                    "➡️ Stroke Order: 1 → 4"
                },
                color = lesson.color,
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                ),
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
            )
        }
    }
}

fun findLetterHitCheckpoints(
    point: Offset,
    canvasSize: Size,
    lesson: LetterLessonItem,
    existingHits: Set<Int>
): Set<Int> {
    if (canvasSize.width <= 0f || canvasSize.height <= 0f) return existingHits
    val hitRadius = minOf(canvasSize.width, canvasSize.height) * 0.20f
    val updated = existingHits.toMutableSet()
    lesson.traceCheckpoints.forEachIndexed { idx, norm ->
        val targetX = norm.x * canvasSize.width
        val targetY = norm.y * canvasSize.height
        if (hypot(point.x - targetX, point.y - targetY) <= hitRadius) {
            updated.add(idx)
        }
    }
    return updated
}
