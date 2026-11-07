package com.example

import com.example.model.AppLanguage
import com.example.model.LearningCategoriesCatalog
import com.example.model.NumberQuizBank
import com.example.model.NumbersActivityId
import com.example.model.NumbersCurriculum
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun verifyEightCategoriesAndUnique3DIllustrations() {
        val categories = LearningCategoriesCatalog.allCategories
        assertEquals(8, categories.size)

        val uniqueIllustrations = categories.map { it.illustrationResId }.toSet()
        assertEquals(8, uniqueIllustrations.size)

        assertTrue(AppLanguage.AR.isRtl)
        assertFalse(AppLanguage.EN.isRtl)

        assertEquals("الأرقام", categories[0].title(AppLanguage.AR))
        assertEquals("Numbers", categories[0].title(AppLanguage.EN))
    }

    @Test
    fun verifyNumbersWorldCurriculumAndActivities() {
        // Verify 10 numbers (1..10) with mathematically accurate values and bilingual numerals
        val lessons = NumbersCurriculum.lessons
        assertEquals(10, lessons.size)
        assertEquals(1, lessons.first().value)
        assertEquals("١", lessons.first().symbolAr)
        assertEquals("1", lessons.first().symbolEn)
        assertEquals("واحد", lessons.first().wordAr)
        assertEquals("One", lessons.first().wordEn)

        val numberThree = NumbersCurriculum.byValue(3)
        assertEquals("٣", numberThree.symbolAr)
        assertEquals("ثلاثة", numberThree.wordAr)
        assertEquals("Three", numberThree.wordEn)

        val numberTen = NumbersCurriculum.byValue(10)
        assertEquals("١٠", numberTen.symbolAr)
        assertEquals("عشرة", numberTen.wordAr)
        assertEquals("Ten", numberTen.wordEn)

        // Verify all 6 Numbers World activities exist
        assertEquals(6, NumbersActivityId.entries.size)

        // Verify Quiz Bank has 3 options per question and valid correct answers
        NumberQuizBank.questions.forEach { q ->
            assertEquals(3, q.options.size)
            assertTrue(q.correctValue in q.options)
        }
    }
}
