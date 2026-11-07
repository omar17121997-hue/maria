package com.example

import com.example.model.AppLanguage
import com.example.model.LearningCategoriesCatalog
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun verifyEightCategoriesAndUnique3DIllustrations() {
        val categories = LearningCategoriesCatalog.allCategories
        assertEquals(8, categories.size)

        // Verify every category has its own distinct 3D cartoon illustration
        val uniqueIllustrations = categories.map { it.illustrationResId }.toSet()
        assertEquals(8, uniqueIllustrations.size)

        // Verify Arabic RTL and English LTR configurations
        assertTrue(AppLanguage.AR.isRtl)
        assertFalse(AppLanguage.EN.isRtl)

        // Verify bilingual titles
        assertEquals("الأرقام", categories[0].title(AppLanguage.AR))
        assertEquals("Numbers", categories[0].title(AppLanguage.EN))
    }
}
