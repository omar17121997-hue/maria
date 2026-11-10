package com.example

import com.example.data.UserSettingsState
import com.example.model.AchievementBadge
import com.example.model.ActivityLogEntry
import com.example.model.AnimalHabitat
import com.example.model.AnimalsActivityId
import com.example.model.AnimalsCurriculum
import com.example.model.AppLanguage
import com.example.model.BrainGameActivityId
import com.example.model.BrainGamesCurriculum
import com.example.model.CategoryId
import com.example.model.ColoringTemplateTheme
import com.example.model.ColorsActivityId
import com.example.model.ColorsCurriculum
import com.example.model.EducationalLevel
import com.example.model.LearningCategoriesCatalog
import com.example.model.LettersActivityId
import com.example.model.LettersCurriculum
import com.example.model.LettersQuizBank
import com.example.model.LettersSectionMode
import com.example.model.NumberQuizBank
import com.example.model.NumbersActivityId
import com.example.model.NumbersCurriculum
import com.example.model.ParentAnalyticsEngine
import com.example.model.RewardsProgressCalculator
import com.example.model.ShapeType
import com.example.model.ShapesActivityId
import com.example.model.ShapesCurriculum
import com.example.model.SurpriseRewardItem
import com.example.model.WorldCertificateType
import com.example.model.WritingDrawingActivityId
import com.example.model.WritingDrawingCurriculum
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
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

    @Test
    fun verifyStage5BrainGamesAndStage6WritingDrawingWorlds() {
        // Stage 5: Brain Games World (6 activities)
        assertEquals(6, BrainGameActivityId.entries.size)
        assertTrue(BrainGamesCurriculum.memoryPool.size >= 4)
        assertEquals(5, BrainGamesCurriculum.oddOneOutQuestions.size)
        assertEquals(5, BrainGamesCurriculum.patternQuestions.size)
        assertEquals(4, BrainGamesCurriculum.sizeOrderChallenges.size)
        assertEquals(listOf(4, 6, 9), BrainGamesCurriculum.picturePuzzles.map { it.pieceCount })
        assertEquals(5, BrainGamesCurriculum.countAndThinkQuestions.size)

        // Stage 6: Writing & Drawing World (6 activities)
        assertEquals(6, WritingDrawingActivityId.entries.size)
        assertEquals(6, ColoringTemplateTheme.entries.size)
        assertEquals(5, WritingDrawingCurriculum.drawAndLearnChallenges.size)

        val baaForms = WritingDrawingCurriculum.getArabicLetterForms("ب")
        assertEquals("بـ", baaForms.initial)
        assertEquals("ـبـ", baaForms.medial)
        assertEquals("ـب", baaForms.finalForm)
    }

    @Test
    fun verifyStage7RewardsBadgesLevelsCertificatesAndStage8ParentDashboard() {
        // Stage 7: 10 Badges, 5 Educational Levels, 6 Surprise Items, 7 Certificates
        assertEquals(10, AchievementBadge.entries.size)
        assertEquals("بطل الأرقام", AchievementBadge.NUMBERS_HERO.titleAr)
        assertEquals("بطل التعلم", AchievementBadge.LEARNING_CHAMPION.titleAr)

        assertEquals(5, EducationalLevel.entries.size)
        assertEquals("المستكشف الصغير", EducationalLevel.LITTLE_EXPLORER.titleAr)
        assertEquals("العبقري الصغير", EducationalLevel.LITTLE_GENIUS.titleAr)

        assertEquals(6, SurpriseRewardItem.entries.size)
        assertEquals(7, WorldCertificateType.entries.size)

        // Verify level progression and badge unlocking based on UserSettingsState
        val starterState = UserSettingsState()
        assertEquals(EducationalLevel.LITTLE_EXPLORER, RewardsProgressCalculator.computeCurrentLevel(starterState))

        val advancedState = UserSettingsState(
            completedNumbersActivities = NumbersActivityId.entries.toSet(),
            completedLettersActivities = LettersActivityId.entries.toSet(),
            learnedArabicLetters = LettersCurriculum.arabicLetters.take(10).map { it.id }.toSet(),
            learnedEnglishLetters = LettersCurriculum.englishLetters.take(10).map { it.id }.toSet(),
            completedColorsActivities = ColorsActivityId.entries.toSet(),
            completedShapesActivities = ShapesActivityId.entries.toSet(),
            completedAnimalsActivities = AnimalsActivityId.entries.toSet(),
            completedBrainActivities = BrainGameActivityId.entries.toSet(),
            brainMaxLevelReached = 3,
            completedWritingActivities = WritingDrawingActivityId.entries.toSet(),
            savedDrawingsTitles = setOf("لوحة قوس قزح")
        )
        assertEquals(EducationalLevel.LITTLE_GENIUS, RewardsProgressCalculator.computeCurrentLevel(advancedState))
        assertEquals(10, RewardsProgressCalculator.unlockedBadges(advancedState).size)
        assertEquals(7, RewardsProgressCalculator.unlockedCertificates(advancedState).size)

        // Stage 8: ActivityLogEntry serialization & ParentAnalyticsEngine insights
        val logEntry = ActivityLogEntry.createNow(
            id = "test_1",
            worldCategory = CategoryId.NUMBERS,
            titleAr = "هيا نتعلم الأرقام",
            titleEn = "Let's Learn Numbers",
            starsEarned = 2
        )
        val deserialized = ActivityLogEntry.deserialize(logEntry.serialize())
        assertNotNull(deserialized)
        assertEquals("هيا نتعلم الأرقام", deserialized?.activityTitleAr)
        assertEquals(CategoryId.NUMBERS, deserialized?.worldCategory)

        val worldProgress = ParentAnalyticsEngine.buildWorldProgressList(starterState)
        assertEquals(7, worldProgress.size)

        val insights = ParentAnalyticsEngine.generateInsightsAndRecommendations(starterState)
        assertTrue(insights.strengthsAr.isNotEmpty())
        assertTrue(insights.recommendations.isNotEmpty())

        // Verify 5-Tab 3D Bottom Navigation & Lulu 3D Mood Expressions
        assertEquals(5, com.example.ui.components.HomeBottomNavTab.entries.size)
        assertEquals(3, com.example.ui.components.LuluMoodExpression.entries.size)
    }
}
