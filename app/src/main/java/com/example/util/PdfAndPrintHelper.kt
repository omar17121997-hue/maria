package com.example.util

import android.content.ContentValues
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.os.Bundle
import android.os.CancellationSignal
import android.os.Environment
import android.os.ParcelFileDescriptor
import android.print.PageRange
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import android.print.PrintDocumentInfo
import android.print.PrintManager
import com.example.data.UserSettingsState
import com.example.model.AppLanguage
import com.example.model.ParentAnalyticsEngine
import com.example.model.RewardsProgressCalculator
import com.example.model.WorldCertificateType
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object PdfAndPrintHelper {

    fun currentFormattedDate(): String =
        SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())

    /**
     * Generates a landscape A4 PDF for the given [certificate] and saves it to Downloads.
     */
    fun exportCertificateAsPdf(
        context: Context,
        childName: String,
        certificate: WorldCertificateType,
        language: AppLanguage
    ): String {
        val isAr = language == AppLanguage.AR
        val pdfDocument = PdfDocument()
        try {
            val pageInfo = PdfDocument.PageInfo.Builder(842, 595, 1).create()
            val page = pdfDocument.startPage(pageInfo)
            drawCertificateOnCanvas(
                canvas = page.canvas,
                width = 842f,
                height = 595f,
                childName = childName.ifBlank { if (isAr) "بطل التعلّم" else "Little Learner" },
                certificate = certificate,
                dateStr = currentFormattedDate()
            )
            pdfDocument.finishPage(page)

            val fileName = "FunLearning_Certificate_${certificate.id}_${System.currentTimeMillis()}.pdf"
            val savedPath = writePdfToDownloads(context, pdfDocument, fileName)
            return if (savedPath != null) {
                if (isAr) "تم تنزيل الشهادة بصيغة PDF في مجلد التنزيلات ($fileName) 📄✨"
                else "Certificate PDF saved to Downloads ($fileName) 📄✨"
            } else {
                if (isAr) "تعذر حفظ ملف PDF، يرجى المحاولة مرة أخرى"
                else "Could not save PDF file, please try again"
            }
        } catch (e: Exception) {
            return if (isAr) "حدث خطأ أثناء إنشاء ملف PDF" else "Error creating PDF file"
        } finally {
            pdfDocument.close()
        }
    }

    /**
     * Opens the Android system Print dialog to print the [certificate].
     */
    fun printCertificate(
        context: Context,
        childName: String,
        certificate: WorldCertificateType,
        language: AppLanguage
    ): String {
        val isAr = language == AppLanguage.AR
        return try {
            val printManager = context.getSystemService(Context.PRINT_SERVICE) as? PrintManager
                ?: return if (isAr) "خدمة الطباعة غير متوفرة على هذا الجهاز" else "Print service not available"

            val jobName = "FunLearningWorld_${certificate.id}"
            val cleanChildName = childName.ifBlank { if (isAr) "بطل التعلّم" else "Little Learner" }
            val dateStr = currentFormattedDate()

            val adapter = object : PrintDocumentAdapter() {
                override fun onLayout(
                    oldAttributes: PrintAttributes?,
                    newAttributes: PrintAttributes?,
                    cancellationSignal: CancellationSignal?,
                    callback: LayoutResultCallback?,
                    extras: Bundle?
                ) {
                    if (cancellationSignal?.isCanceled == true) {
                        callback?.onLayoutCancelled()
                        return
                    }
                    val info = PrintDocumentInfo.Builder("$jobName.pdf")
                        .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                        .setPageCount(1)
                        .build()
                    callback?.onLayoutFinished(info, true)
                }

                override fun onWrite(
                    pages: Array<out PageRange>?,
                    destination: ParcelFileDescriptor?,
                    cancellationSignal: CancellationSignal?,
                    callback: WriteResultCallback?
                ) {
                    if (destination == null) {
                        callback?.onWriteFailed("No destination")
                        return
                    }
                    val pdfDoc = PdfDocument()
                    try {
                        val pageInfo = PdfDocument.PageInfo.Builder(842, 595, 1).create()
                        val page = pdfDoc.startPage(pageInfo)
                        drawCertificateOnCanvas(
                            canvas = page.canvas,
                            width = 842f,
                            height = 595f,
                            childName = cleanChildName,
                            certificate = certificate,
                            dateStr = dateStr
                        )
                        pdfDoc.finishPage(page)
                        FileOutputStream(destination.fileDescriptor).use { out ->
                            pdfDoc.writeTo(out)
                        }
                        callback?.onWriteFinished(arrayOf(PageRange.ALL_PAGES))
                    } catch (e: Exception) {
                        callback?.onWriteFailed(e.message)
                    } finally {
                        pdfDoc.close()
                    }
                }
            }

            val printAttributes = PrintAttributes.Builder()
                .setMediaSize(PrintAttributes.MediaSize.ISO_A4.asLandscape())
                .setColorMode(PrintAttributes.COLOR_MODE_COLOR)
                .build()

            printManager.print(jobName, adapter, printAttributes)
            if (isAr) "تم فتح نافذة طباعة الشهادة بنجاح 🖨️✨"
            else "Opened Certificate Print preview 🖨️✨"
        } catch (e: Exception) {
            if (isAr) "تعذر فتح نافذة الطباعة على هذا الجهاز"
            else "Unable to open Print dialog on this device"
        }
    }

    /**
     * Generates a comprehensive Parent Educational Progress Report PDF and saves it to Downloads.
     */
    fun exportParentReportAsPdf(
        context: Context,
        settings: UserSettingsState
    ): String {
        val isAr = settings.language == AppLanguage.AR
        val pdfDocument = PdfDocument()
        try {
            val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
            val page = pdfDocument.startPage(pageInfo)
            drawParentReportOnCanvas(
                canvas = page.canvas,
                width = 595f,
                height = 842f,
                settings = settings,
                dateStr = currentFormattedDate()
            )
            pdfDocument.finishPage(page)

            val fileName = "FunLearning_ParentReport_${System.currentTimeMillis()}.pdf"
            val savedPath = writePdfToDownloads(context, pdfDocument, fileName)
            return if (savedPath != null) {
                if (isAr) "تم تنزيل تقرير تقدم الطفل بصيغة PDF في مجلد التنزيلات ($fileName) 📊✨"
                else "Educational Progress Report PDF saved to Downloads ($fileName) 📊✨"
            } else {
                if (isAr) "تعذر حفظ التقرير بصيغة PDF"
                else "Could not save PDF report"
            }
        } catch (e: Exception) {
            return if (isAr) "حدث خطأ أثناء تصدير التقرير" else "Error exporting PDF report"
        } finally {
            pdfDocument.close()
        }
    }

    private fun drawCertificateOnCanvas(
        canvas: Canvas,
        width: Float,
        height: Float,
        childName: String,
        certificate: WorldCertificateType,
        dateStr: String
    ) {
        val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#FFFDF7")
            style = Paint.Style.FILL
        }
        canvas.drawRect(0f, 0f, width, height, bgPaint)

        // Outer decorative border
        val outerBorderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#D81B60")
            style = Paint.Style.STROKE
            strokeWidth = 10f
        }
        canvas.drawRoundRect(RectF(24f, 24f, width - 24f, height - 24f), 28f, 28f, outerBorderPaint)

        // Inner gold border
        val innerBorderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#FFB300")
            style = Paint.Style.STROKE
            strokeWidth = 4f
        }
        canvas.drawRoundRect(RectF(38f, 38f, width - 38f, height - 38f), 20f, 20f, innerBorderPaint)

        val centerX = width / 2f

        val headerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#6A1B9A")
            textSize = 22f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textAlign = Paint.Align.CENTER
        }
        canvas.drawText("عالم المرح والتعلّم  |  Fun Learning World", centerX, 92f, headerPaint)

        val titleArPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#0D253F")
            textSize = 32f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textAlign = Paint.Align.CENTER
        }
        canvas.drawText(certificate.titleAr, centerX, 145f, titleArPaint)

        val titleEnPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#0288D1")
            textSize = 20f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textAlign = Paint.Align.CENTER
        }
        canvas.drawText(certificate.titleEn, centerX, 178f, titleEnPaint)

        val subtitlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#455A64")
            textSize = 18f
            textAlign = Paint.Align.CENTER
        }
        canvas.drawText("تُمنح هذه الشهادة بكل فخر واعتزاز إلى البطل(ة) / Proudly presented to:", centerX, 226f, subtitlePaint)

        // Child Name Banner Box
        val nameBoxPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#FFF8E1")
            style = Paint.Style.FILL
        }
        val nameRect = RectF(centerX - 240f, 245f, centerX + 240f, 315f)
        canvas.drawRoundRect(nameRect, 18f, 18f, nameBoxPaint)
        canvas.drawRoundRect(nameRect, 18f, 18f, innerBorderPaint)

        val childNamePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#C2185B")
            textSize = 34f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textAlign = Paint.Align.CENTER
        }
        canvas.drawText(childName, centerX, 292f, childNamePaint)

        val worldPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#2E7D32")
            textSize = 21f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textAlign = Paint.Align.CENTER
        }
        canvas.drawText("${certificate.worldNameAr}  •  ${certificate.worldNameEn}", centerX, 362f, worldPaint)

        val msgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#263238")
            textSize = 17f
            textAlign = Paint.Align.CENTER
        }
        canvas.drawText(certificate.messageAr, centerX, 405f, msgPaint)
        canvas.drawText(certificate.messageEn, centerX, 434f, msgPaint)

        val luluQuotePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#AD1457")
            textSize = 17f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textAlign = Paint.Align.CENTER
        }
        canvas.drawText("لولو تقول: فخورة بك جداً يا نجم التعلّم! استمر في الإبداع والتألق!", centerX, 478f, luluQuotePaint)

        val footerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#37474F")
            textSize = 15f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }
        footerPaint.textAlign = Paint.Align.LEFT
        canvas.drawText("Date / التاريخ: $dateStr", 64f, 535f, footerPaint)

        footerPaint.textAlign = Paint.Align.RIGHT
        canvas.drawText("Lulu Mascot Seal ★ ختم لولو الذهبي", width - 64f, 535f, footerPaint)
    }

    private fun drawParentReportOnCanvas(
        canvas: Canvas,
        width: Float,
        height: Float,
        settings: UserSettingsState,
        dateStr: String
    ) {
        val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
            style = Paint.Style.FILL
        }
        canvas.drawRect(0f, 0f, width, height, bgPaint)

        val headerBg = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#E1F5FE")
            style = Paint.Style.FILL
        }
        canvas.drawRoundRect(RectF(24f, 24f, width - 24f, 125f), 16f, 16f, headerBg)

        val titlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#0D253F")
            textSize = 20f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textAlign = Paint.Align.CENTER
        }
        canvas.drawText("تقرير متابعة التقدم التعليمي | Educational Progress Report", width / 2f, 60f, titlePaint)

        val subHeaderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#0288D1")
            textSize = 14f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textAlign = Paint.Align.CENTER
        }
        canvas.drawText(
            "الطفل / Child: ${settings.childName}   •   التاريخ / Date: $dateStr",
            width / 2f,
            92f,
            subHeaderPaint
        )

        val currentLevel = RewardsProgressCalculator.computeCurrentLevel(settings)
        val completedCount = RewardsProgressCalculator.totalCompletedActivitiesCount(settings)
        val maxCount = RewardsProgressCalculator.maxActivitiesCount()
        val exploredWorlds = RewardsProgressCalculator.exploredWorldsCount(settings)
        val unlockedBadges = RewardsProgressCalculator.unlockedBadges(settings)
        val insights = ParentAnalyticsEngine.generateInsightsAndRecommendations(settings)

        var y = 160f
        val sectionTitlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#6A1B9A")
            textSize = 15f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }
        val bodyPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#263238")
            textSize = 12.5f
        }

        // Section 1: Summary
        canvas.drawText("1. الملخص العام | General Summary", 36f, y, sectionTitlePaint)
        y += 22f
        val summaryLines = listOf(
            "• المستوى التعليمي الحالي / Current Level: ${currentLevel.titleAr} (${currentLevel.titleEn})",
            "• مجموع النجوم المكتسبة / Total Stars Earned: ${settings.totalEarnedStars} / ${settings.maxPossibleStars}",
            "• الأنشطة المكتملة / Completed Activities: $completedCount / $maxCount",
            "• العوالم المستكشفة / Worlds Explored: $exploredWorlds / 8",
            "• الحروف العربية المتعلمة / Arabic Letters Learned: ${settings.learnedArabicLetters.size} / 28",
            "• الحروف الإنجليزية المتعلمة / English Letters Learned: ${settings.learnedEnglishLetters.size} / 26",
            "• العناصر المتدرب على كتابتها / Writing Items Practiced: ${settings.practicedWritingItems.size}"
        )
        summaryLines.forEach { line ->
            canvas.drawText(line, 44f, y, bodyPaint)
            y += 19f
        }

        y += 12f
        // Section 2: Unlocked Badges
        canvas.drawText("2. الشارات والإنجازات المفتوحة | Unlocked Badges (${unlockedBadges.size}/10)", 36f, y, sectionTitlePaint)
        y += 22f
        if (unlockedBadges.isEmpty()) {
            canvas.drawText("• في بداية الرحلة - شجعوا الطفل على إكمال الأنشطة لفتح الشارات!", 44f, y, bodyPaint)
            y += 19f
        } else {
            unlockedBadges.chunked(2).forEach { pair ->
                val text = pair.joinToString("   |   ") { "★ ${it.titleAr} (${it.titleEn})" }
                canvas.drawText(text, 44f, y, bodyPaint)
                y += 19f
            }
        }

        y += 12f
        // Section 3: Strengths
        canvas.drawText("3. نقاط القوة | Strengths", 36f, y, sectionTitlePaint)
        y += 22f
        insights.strengthsAr.take(4).forEachIndexed { idx, ar ->
            canvas.drawText("✓ $ar", 44f, y, bodyPaint)
            y += 18f
            val en = insights.strengthsEn.getOrNull(idx)
            if (en != null) {
                canvas.drawText("   $en", 44f, y, bodyPaint)
                y += 18f
            }
        }

        y += 12f
        // Section 4: Recommendations
        canvas.drawText("4. التوصيات التعليمية المقترحة | Recommended Next Activities", 36f, y, sectionTitlePaint)
        y += 22f
        insights.recommendations.take(4).forEach { rec ->
            if (y < height - 50f) {
                canvas.drawText("• ${rec.titleAr} (${rec.titleEn})", 44f, y, bodyPaint)
                y += 17f
                canvas.drawText("  نصيحة / Tip: ${rec.parentTipAr}", 50f, y, bodyPaint)
                y += 20f
            }
        }
    }

    private fun writePdfToDownloads(
        context: Context,
        pdfDocument: PdfDocument,
        fileName: String
    ): String? {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val resolver = context.contentResolver
                val contentValues = ContentValues().apply {
                    put( android.provider.MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                    put(android.provider.MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                    put(android.provider.MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                }
                val uri = resolver.insert(android.provider.MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
                if (uri != null) {
                    resolver.openOutputStream(uri)?.use { out: OutputStream ->
                        pdfDocument.writeTo(out)
                    }
                    return uri.toString()
                }
            }
            val dir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) ?: context.filesDir
            val file = File(dir, fileName)
            FileOutputStream(file).use { out ->
                pdfDocument.writeTo(out)
            }
            file.absolutePath
        } catch (e: Exception) {
            null
        }
    }
}
