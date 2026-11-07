package com.example.ui.components

import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
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
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.model.CountableObjectType
import com.example.model.NumberLessonItem
import com.example.model.NumbersCurriculum
import com.example.ui.theme.DeepNavyText
import com.example.ui.theme.StarGoldBorder
import com.example.ui.theme.SunnyYellow
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin

/**
 * Displays a mathematically exact group of [count] attractive 3D-styled cartoon objects
 * (Apples, Stars, Balloons, Flowers, or Cute Animals).
 * Children can tap each object to see it bounce and hear its count number!
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun QuantityObjectsGrid(
    count: Int,
    objectType: CountableObjectType,
    numeralLanguage: AppLanguage,
    modifier: Modifier = Modifier,
    itemSize: Dp = 62.dp,
    showCountBadgeOnTap: Boolean = true,
    onObjectTapped: ((Int) -> Unit)? = null
) {
    var lastTappedIndex by remember(count, objectType) { mutableStateOf(-1) }

    FlowRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        maxItemsInEachRow = 5
    ) {
        repeat(count.coerceIn(1, 10)) { idx ->
            val numberIndex = idx + 1
            val isHighlighted = lastTappedIndex == idx
            val scale by animateFloatAsState(
                targetValue = if (isHighlighted) 1.15f else 1f,
                animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                label = "obj_bounce_$idx"
            )

            Box(
                modifier = Modifier
                    .size(itemSize)
                    .scale(scale)
                    .let {
                        if (onObjectTapped != null) {
                            it.clickable {
                                lastTappedIndex = idx
                                onObjectTapped(numberIndex)
                            }
                        } else {
                            it
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                CuteObjectIllustration(
                    objectType = objectType,
                    modifier = Modifier.fillMaxSize()
                )

                if (showCountBadgeOnTap && isHighlighted) {
                    Surface(
                        modifier = Modifier.align(Alignment.TopEnd),
                        shape = CircleShape,
                        color = SunnyYellow,
                        border = BorderStroke(1.5.dp, Color.White),
                        shadowElevation = 3.dp
                    ) {
                        Text(
                            text = NumbersCurriculum.formatNumeral(numberIndex, numeralLanguage),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 1.dp),
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
}

/**
 * Renders a single colorful 3D-styled cartoon illustration for Apples, Stars, Balloons, Flowers, or Animals.
 */
@Composable
fun CuteObjectIllustration(
    objectType: CountableObjectType,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val cx = w / 2f
        val cy = h / 2f
        val r = minOf(w, h) * 0.40f

        when (objectType) {
            CountableObjectType.APPLE -> drawCute3DApple(cx, cy, r)
            CountableObjectType.STAR -> drawCute3DStar(cx, cy, r * 1.12f)
            CountableObjectType.BALLOON -> drawCute3DBalloon(cx, cy, r)
            CountableObjectType.FLOWER -> drawCute3DFlower(cx, cy, r)
            CountableObjectType.ANIMAL -> drawCute3DBunny(cx, cy, r)
        }
    }
}

private fun DrawScope.drawCute3DApple(cx: Float, cy: Float, r: Float) {
    // Soft bottom shadow
    drawCircle(
        color = Color(0x26000000),
        radius = r * 0.85f,
        center = Offset(cx, cy + r * 0.30f)
    )
    // Brown stem
    drawLine(
        color = Color(0xFF6D4C41),
        start = Offset(cx, cy - r * 0.65f),
        end = Offset(cx + r * 0.1f, cy - r * 1.05f),
        strokeWidth = r * 0.16f,
        cap = StrokeCap.Round
    )
    // Green leaf
    val leafPath = Path().apply {
        moveTo(cx + r * 0.05f, cy - r * 0.85f)
        quadraticTo(cx + r * 0.65f, cy - r * 1.18f, cx + r * 0.55f, cy - r * 0.62f)
        quadraticTo(cx + r * 0.25f, cy - r * 0.60f, cx + r * 0.05f, cy - r * 0.85f)
        close()
    }
    drawPath(leafPath, color = Color(0xFF43A047))

    // Left & right lobes of 3D red apple
    drawCircle(
        color = Color(0xFFE53935),
        radius = r * 0.78f,
        center = Offset(cx - r * 0.24f, cy + r * 0.08f)
    )
    drawCircle(
        color = Color(0xFFD32F2F),
        radius = r * 0.78f,
        center = Offset(cx + r * 0.24f, cy + r * 0.08f)
    )
    // 3D glossy highlight
    drawCircle(
        color = Color.White.copy(alpha = 0.55f),
        radius = r * 0.22f,
        center = Offset(cx - r * 0.42f, cy - r * 0.20f)
    )
    // Cute smiling face
    drawCuteFace(cx, cy + r * 0.12f, r * 0.45f)
}

private fun DrawScope.drawCute3DStar(cx: Float, cy: Float, r: Float) {
    val starPath = Path()
    val points = 5
    val innerRadius = r * 0.48f
    for (i in 0 until points * 2) {
        val radius = if (i % 2 == 0) r else innerRadius
        val angle = Math.toRadians((i * 36.0) - 90.0)
        val x = cx + (cos(angle) * radius).toFloat()
        val y = cy + (sin(angle) * radius).toFloat()
        if (i == 0) starPath.moveTo(x, y) else starPath.lineTo(x, y)
    }
    starPath.close()

    // Golden shadow border + bright star body
    drawPath(starPath, color = Color(0xFFF57F17), style = Stroke(width = r * 0.16f, join = StrokeJoin.Round))
    drawPath(starPath, color = Color(0xFFFFD54F))
    drawCircle(color = Color.White.copy(alpha = 0.5f), radius = r * 0.18f, center = Offset(cx - r * 0.2f, cy - r * 0.25f))
    drawCuteFace(cx, cy + r * 0.06f, r * 0.36f)
}

private fun DrawScope.drawCute3DBalloon(cx: Float, cy: Float, r: Float) {
    // String
    val stringPath = Path().apply {
        moveTo(cx, cy + r * 0.65f)
        quadraticTo(cx - r * 0.25f, cy + r * 0.92f, cx + r * 0.1f, cy + r * 1.15f)
    }
    drawPath(stringPath, color = Color(0xFF90A4AE), style = Stroke(width = r * 0.08f, cap = StrokeCap.Round))

    // Balloon body
    drawOval(
        color = Color(0xFFEC407A),
        topLeft = Offset(cx - r * 0.78f, cy - r * 0.92f),
        size = Size(r * 1.56f, r * 1.65f)
    )
    // Gloss highlight
    drawCircle(
        color = Color.White.copy(alpha = 0.55f),
        radius = r * 0.20f,
        center = Offset(cx - r * 0.32f, cy - r * 0.42f)
    )
    drawCuteFace(cx, cy - r * 0.05f, r * 0.38f)
}

private fun DrawScope.drawCute3DFlower(cx: Float, cy: Float, r: Float) {
    val petalRadius = r * 0.44f
    val petalDist = r * 0.55f
    val petalColors = listOf(
        Color(0xFFFF80AB),
        Color(0xFFCE93D8),
        Color(0xFF80D8FF),
        Color(0xFFA5D6A7),
        Color(0xFFFFCC80),
        Color(0xFFFF8A80)
    )
    for (i in 0 until 6) {
        val angle = Math.toRadians((i * 60).toDouble())
        val px = cx + (cos(angle) * petalDist).toFloat()
        val py = cy + (sin(angle) * petalDist).toFloat()
        drawCircle(color = petalColors[i], radius = petalRadius, center = Offset(px, py))
    }
    drawCircle(color = Color(0xFFFFEE58), radius = r * 0.52f, center = Offset(cx, cy))
    drawCuteFace(cx, cy, r * 0.35f)
}

private fun DrawScope.drawCute3DBunny(cx: Float, cy: Float, r: Float) {
    // Left & Right Ears
    drawOval(
        color = Color(0xFFFFF3E0),
        topLeft = Offset(cx - r * 0.62f, cy - r * 1.15f),
        size = Size(r * 0.42f, r * 0.95f)
    )
    drawOval(
        color = Color(0xFFFF80AB),
        topLeft = Offset(cx - r * 0.52f, cy - r * 1.00f),
        size = Size(r * 0.22f, r * 0.65f)
    )
    drawOval(
        color = Color(0xFFFFF3E0),
        topLeft = Offset(cx + r * 0.20f, cy - r * 1.15f),
        size = Size(r * 0.42f, r * 0.95f)
    )
    drawOval(
        color = Color(0xFFFF80AB),
        topLeft = Offset(cx + r * 0.30f, cy - r * 1.00f),
        size = Size(r * 0.22f, r * 0.65f)
    )

    // Head
    drawCircle(
        color = Color(0xFFFFCC80),
        radius = r * 0.78f,
        center = Offset(cx, cy + r * 0.12f)
    )
    drawCuteFace(cx, cy + r * 0.14f, r * 0.42f)
}

private fun DrawScope.drawCuteFace(cx: Float, cy: Float, scale: Float) {
    // Eyes
    drawCircle(color = Color(0xFF141829), radius = scale * 0.20f, center = Offset(cx - scale * 0.55f, cy - scale * 0.15f))
    drawCircle(color = Color(0xFF141829), radius = scale * 0.20f, center = Offset(cx + scale * 0.55f, cy - scale * 0.15f))
    // Eye sparkles
    drawCircle(color = Color.White, radius = scale * 0.07f, center = Offset(cx - scale * 0.60f, cy - scale * 0.20f))
    drawCircle(color = Color.White, radius = scale * 0.07f, center = Offset(cx + scale * 0.50f, cy - scale * 0.20f))
    // Rosy cheeks
    drawCircle(color = Color(0xFFFF80AB).copy(alpha = 0.65f), radius = scale * 0.20f, center = Offset(cx - scale * 0.80f, cy + scale * 0.10f))
    drawCircle(color = Color(0xFFFF80AB).copy(alpha = 0.65f), radius = scale * 0.20f, center = Offset(cx + scale * 0.80f, cy + scale * 0.10f))
    // Smile arc
    drawArc(
        color = Color(0xFF141829),
        startAngle = 20f,
        sweepAngle = 140f,
        useCenter = false,
        topLeft = Offset(cx - scale * 0.38f, cy - scale * 0.10f),
        size = Size(scale * 0.76f, scale * 0.55f),
        style = Stroke(width = scale * 0.14f, cap = StrokeCap.Round)
    )
}

/**
 * Interactive Finger Tracing Surface for Activity 2 (Number Tracing).
 * - Displays a large dotted guide numeral (Arabic ١..١٠ or English 1..10)
 * - Displays visual guide checkpoints with a green starting star badge
 * - Allows children to trace with their finger on mobile phones and tablets
 * - Reports progress and triggers [onTracingCompleted] when the checkpoints are covered
 */
@Composable
fun NumberFingerTracingPad(
    lesson: NumberLessonItem,
    numeralLanguage: AppLanguage,
    strokes: List<List<Offset>>,
    hitCheckpoints: Set<Int>,
    onAddPoint: (Offset, Size) -> Unit,
    onStartNewStroke: (Offset, Size) -> Unit,
    modifier: Modifier = Modifier
) {
    val numeralSymbol = lesson.symbol(numeralLanguage)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(290.dp)
            .clip(RoundedCornerShape(28.dp))
            .background(Color(0xFFFFFDF7))
            .border(3.dp, lesson.color, RoundedCornerShape(28.dp))
            .testTag("number_tracing_canvas")
            .pointerInput(lesson.value, numeralLanguage) {
                detectTapGestures { offset ->
                    val canvasSize = Size(size.width.toFloat(), size.height.toFloat())
                    onStartNewStroke(offset, canvasSize)
                }
            }
            .pointerInput(lesson.value, numeralLanguage) {
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

            // 1. Subtle notebook guide lines
            val lineColor = Color(0xFFE1F5FE)
            drawLine(lineColor, Offset(24f, h * 0.22f), Offset(w - 24f, h * 0.22f), strokeWidth = 3f)
            drawLine(
                color = Color(0xFFB3E5FC),
                start = Offset(24f, h * 0.52f),
                end = Offset(w - 24f, h * 0.52f),
                strokeWidth = 3f
            )
            drawLine(lineColor, Offset(24f, h * 0.82f), Offset(w - 24f, h * 0.82f), strokeWidth = 3f)

            // 2. Large Dotted Guide Numeral in Center
            drawContext.canvas.nativeCanvas.apply {
                val textY = h * 0.74f
                val fontSizePx = h * 0.68f

                // Soft pastel fill inside the numeral
                val fillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    color = lesson.color.copy(alpha = 0.14f).toArgb()
                    textSize = fontSizePx
                    textAlign = Paint.Align.CENTER
                    typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                }
                drawText(numeralSymbol, w / 2f, textY, fillPaint)

                // Dotted outline guide for tracing
                val dottedPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    color = lesson.color.copy(alpha = 0.68f).toArgb()
                    style = Paint.Style.STROKE
                    strokeWidth = 9f
                    pathEffect = DashPathEffect(floatArrayOf(18f, 14f), 0f)
                    textSize = fontSizePx
                    textAlign = Paint.Align.CENTER
                    typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                }
                drawText(numeralSymbol, w / 2f, textY, dottedPaint)
            }

            // 3. Visual Guide Checkpoints (Start dot + path dots)
            lesson.traceCheckpoints.forEachIndexed { idx, normPt ->
                val pt = Offset(normPt.x * w, normPt.y * h)
                val isHit = idx in hitCheckpoints
                val dotRadius = if (idx == 0) 20f else 15f
                val dotColor = when {
                    isHit -> Color(0xFF43A047)
                    idx == 0 -> Color(0xFFFFB300)
                    else -> lesson.color.copy(alpha = 0.45f)
                }
                drawCircle(color = Color.White, radius = dotRadius + 4f, center = pt)
                drawCircle(color = dotColor, radius = dotRadius, center = pt)
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
                    // Outer glow stroke
                    drawPath(
                        path = path,
                        color = SunnyYellow.copy(alpha = 0.6f),
                        style = Stroke(width = 50f, cap = StrokeCap.Round, join = StrokeJoin.Round)
                    )
                    // Main colorful crayon stroke
                    drawPath(
                        path = path,
                        color = lesson.color,
                        style = Stroke(width = 38f, cap = StrokeCap.Round, join = StrokeJoin.Round)
                    )
                }
            }
        }

        // Visual Guide Hint Pill in top corner
        Surface(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(12.dp),
            shape = RoundedCornerShape(14.dp),
            color = Color.White.copy(alpha = 0.92f),
            border = BorderStroke(1.5.dp, StarGoldBorder)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "✏️ ${hitCheckpoints.size} / ${lesson.traceCheckpoints.size}",
                    color = DeepNavyText,
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                )
            }
        }
    }
}

/**
 * Checks which guide checkpoints in [lesson] are within tracing radius of [point].
 */
fun findHitCheckpoints(
    point: Offset,
    canvasSize: Size,
    lesson: NumberLessonItem,
    existingHits: Set<Int>
): Set<Int> {
    if (canvasSize.width <= 0f || canvasSize.height <= 0f) return existingHits
    val hitRadius = minOf(canvasSize.width, canvasSize.height) * 0.18f
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
