package com.example

import com.example.model.AnimalHabitat
import com.example.model.AnimalsActivityId
import com.example.model.AnimalsCurriculum
import com.example.model.AppLanguage
import com.example.model.ColorsActivityId
import com.example.model.ColorsCurriculum
import com.example.model.LearningCategoriesCatalog
import com.example.model.LettersActivityId
import com.example.model.LettersCurriculum
import com.example.model.LettersQuizBank
import com.example.model.LettersSectionMode
import com.example.model.NumberQuizBank
import com.example.model.NumbersActivityId
import com.example.model.NumbersCurriculum
import com.example.model.ShapeType
import com.example.model.ShapesActivityId
import com.example.model.ShapesCurriculum
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

        assertEquals(6, NumbersActivityId.entries.size)

        NumberQuizBank.questions.forEach { q ->
            assertEquals(3, q.options.size)
            assertTrue(q.correctValue in q.options)
        }
    }

    @Test
    fun verifyArabicAndEnglishLettersWorldCurriculumAndActivities() {
        // Verify all 28 Arabic letters (أ to ي)
        val arabicLetters = LettersCurriculum.arabicLetters
        assertEquals(28, arabicLetters.size)
        assertEquals("أ", arabicLetters.first().isolatedSymbol)
        assertEquals("أسد", arabicLetters.first().wordAr)
        assertEquals("ب", arabicLetters[1].isolatedSymbol)
        assertEquals("بطة", arabicLetters[1].wordAr)
        assertEquals("_طة", arabicLetters[1].missingWordDisplay)
        assertEquals("ي", arabicLetters.last().isolatedSymbol)
        assertEquals("يد", arabicLetters.last().wordAr)

        // Verify all 26 English letters (A a to Z z)
        val englishLetters = LettersCurriculum.englishLetters
        assertEquals(26, englishLetters.size)
        assertEquals("A", englishLetters.first().uppercaseSymbol)
        assertEquals("a", englishLetters.first().lowercaseSymbol)
        assertEquals("Apple", englishLetters.first().wordEn)
        assertEquals("Z", englishLetters.last().uppercaseSymbol)
        assertEquals("z", englishLetters.last().lowercaseSymbol)
        assertEquals("Zebra", englishLetters.last().wordEn)

        // Verify all 6 Letters World activities exist
        assertEquals(6, LettersActivityId.entries.size)

        // Verify Arabic and English Quiz banks have 3 choices and valid answers
        LettersQuizBank.questionsFor(LettersSectionMode.ARABIC).forEach { q ->
            assertEquals(3, q.options.size)
            assertTrue(q.correctOption in q.options)
        }
        LettersQuizBank.questionsFor(LettersSectionMode.ENGLISH).forEach { q ->
            assertEquals(3, q.options.size)
            assertTrue(q.correctOption in q.options)
        }
    }

    @Test
    fun verifyStage4ColorsShapesAndAnimalsWorlds() {
        // World 1: Colors (10 colors, 4 activities)
        val colors = ColorsCurriculum.allColors
        assertEquals(10, colors.size)
        assertEquals("أحمر", colors.first().nameAr)
        assertEquals("Red", colors.first().nameEn)
        assertEquals("بني", colors.last().nameAr)
        assertEquals("Brown", colors.last().nameEn)
        assertEquals(4, ColorsActivityId.entries.size)

        // World 2: Shapes (8 shapes, 6 everyday objects, 4 activities)
        val shapes = ShapesCurriculum.allShapes
        assertEquals(8, shapes.size)
        assertEquals("دائرة", shapes.first().nameAr)
        assertEquals("Circle", shapes.first().nameEn)
        assertEquals("معين", shapes.last().nameAr)
        assertEquals("Diamond", shapes.last().nameEn)
        assertEquals(6, ShapesCurriculum.everydayQuestions.size)
        assertEquals(ShapeType.CIRCLE, ShapesCurriculum.everydayQuestions.first().targetShape)
        assertEquals(4, ShapesActivityId.entries.size)

        // World 3: Animals (15 animals, 4 habitats, 4 activities)
        val animals = AnimalsCurriculum.allAnimals
        assertEquals(15, animals.size)
        assertEquals("أسد", animals.first().nameAr)
        assertEquals("Lion", animals.first().nameEn)
        assertEquals("فراشة", animals.last().nameAr)
        assertEquals("Butterfly", animals.last().nameEn)
        assertEquals(4, AnimalHabitat.entries.size)
        assertEquals(4, AnimalsActivityId.entries.size)
    }
}
