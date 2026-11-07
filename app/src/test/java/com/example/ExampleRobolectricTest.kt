package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.model.AppLanguage
import com.example.model.LearningCategoriesCatalog
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read app name and verify 8 learning categories`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Fun Learning World", appName)

        val categories = LearningCategoriesCatalog.allCategories
        assertEquals(8, categories.size)
        assertEquals("الأرقام", categories[0].title(AppLanguage.AR))
        assertEquals("Numbers", categories[0].title(AppLanguage.EN))
        assertTrue(AppLanguage.AR.isRtl)
    }
}
