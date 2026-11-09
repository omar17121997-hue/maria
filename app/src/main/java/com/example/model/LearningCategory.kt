package com.example.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Abc
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.Filter1
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Translate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.R
import com.example.ui.theme.AnimalsAccent
import com.example.ui.theme.AnimalsCardBg
import com.example.ui.theme.AnimalsShadow
import com.example.ui.theme.ArabicAccent
import com.example.ui.theme.ArabicCardBg
import com.example.ui.theme.ArabicShadow
import com.example.ui.theme.BrainAccent
import com.example.ui.theme.BrainCardBg
import com.example.ui.theme.BrainShadow
import com.example.ui.theme.ColorsAccent
import com.example.ui.theme.ColorsCardBg
import com.example.ui.theme.ColorsShadow
import com.example.ui.theme.DrawingAccent
import com.example.ui.theme.DrawingCardBg
import com.example.ui.theme.DrawingShadow
import com.example.ui.theme.EnglishAccent
import com.example.ui.theme.EnglishCardBg
import com.example.ui.theme.EnglishShadow
import com.example.ui.theme.NumbersAccent
import com.example.ui.theme.NumbersCardBg
import com.example.ui.theme.NumbersShadow
import com.example.ui.theme.ShapesAccent
import com.example.ui.theme.ShapesCardBg
import com.example.ui.theme.ShapesShadow

enum class AppLanguage(val code: String, val isRtl: Boolean) {
    AR("ar", true),
    EN("en", false)
}

enum class CategoryId(val key: String) {
    NUMBERS("numbers"),
    ARABIC_LETTERS("arabic_letters"),
    ENGLISH_LETTERS("english_letters"),
    COLORS("colors"),
    SHAPES("shapes"),
    ANIMALS("animals"),
    BRAIN_GAMES("brain_games"),
    WRITING_DRAWING("writing_drawing")
}

data class PreviewActivityItem(
    val symbol: String,
    val labelAr: String,
    val labelEn: String,
    val badgeColor: Color
)

data class LearningCategory(
    val id: CategoryId,
    val numberIndex: Int,
    val titleAr: String,
    val titleEn: String,
    val subtitleAr: String,
    val subtitleEn: String,
    val buttonLabelAr: String,
    val buttonLabelEn: String,
    val luluGuideAr: String,
    val luluGuideEn: String,
    val cardBgColor: Color,
    val accentColor: Color,
    val shadowColor: Color,
    val icon: ImageVector,
    val illustrationResId: Int,
    val floatingBadges: List<String>,
    val previewItems: List<PreviewActivityItem>,
    val maxStars: Int = 3
) {
    fun title(lang: AppLanguage): String = if (lang == AppLanguage.AR) titleAr else titleEn
    fun secondaryTitle(lang: AppLanguage): String = if (lang == AppLanguage.AR) titleEn else titleAr
    fun subtitle(lang: AppLanguage): String = if (lang == AppLanguage.AR) subtitleAr else subtitleEn
    fun buttonLabel(lang: AppLanguage): String = if (lang == AppLanguage.AR) buttonLabelAr else buttonLabelEn
    fun luluGuide(lang: AppLanguage): String = if (lang == AppLanguage.AR) luluGuideAr else luluGuideEn
}

object LearningCategoriesCatalog {
    val allCategories: List<LearningCategory> = listOf(
        LearningCategory(
            id = CategoryId.NUMBERS,
            numberIndex = 1,
            titleAr = "الأرقام",
            titleEn = "Numbers",
            subtitleAr = "تعلّم العدّ بالأرقام الملوّنة",
            subtitleEn = "Colorful 3D Numbers",
            buttonLabelAr = "هيا نعدّ",
            buttonLabelEn = "Let's Count",
            luluGuideAr = "أهلاً بك في عالم الأرقام! هيا نعدّ معاً من ١ إلى ١٠ مع النجوم والفواكه الجميلة.",
            luluGuideEn = "Welcome to Numbers World! Let's count from 1 to 10 together with stars and yummy fruits.",
            cardBgColor = NumbersCardBg,
            accentColor = NumbersAccent,
            shadowColor = NumbersShadow,
            icon = Icons.Filled.Filter1,
            illustrationResId = R.drawable.img_card_numbers_letters_1794072209030,
            floatingBadges = listOf("١", "٢", "٣"),
            previewItems = listOf(
                PreviewActivityItem("١ ٢ ٣", "الأرقام العربية", "Arabic Numbers", Color(0xFFFFB300)),
                PreviewActivityItem("1 2 3", "الأرقام الإنجليزية", "English Numbers", Color(0xFFFB8C00)),
                PreviewActivityItem("🍎×5", "عدّ الفواكه", "Count the Fruits", Color(0xFF43A047)),
                PreviewActivityItem("🎵 10", "أنشودة الأرقام", "Numbers Song", Color(0xFF1E88E5))
            )
        ),
        LearningCategory(
            id = CategoryId.ARABIC_LETTERS,
            numberIndex = 2,
            titleAr = "الحروف العربية",
            titleEn = "Arabic Letters",
            subtitleAr = "مكعبات الحروف أ ب ت",
            subtitleEn = "Arabic Alphabet Blocks",
            buttonLabelAr = "تعلّم الحروف",
            buttonLabelEn = "Learn Letters",
            luluGuideAr = "مرحباً بك في حديقة الحروف العربية! سنتعرّف معاً على مكعبات الحروف أ، ب، ت مع الكلمات المصوّرة.",
            luluGuideEn = "Welcome to the Arabic Letters Garden! Together we'll discover Alif, Baa, Taa alphabet blocks.",
            cardBgColor = ArabicCardBg,
            accentColor = ArabicAccent,
            shadowColor = ArabicShadow,
            icon = Icons.Filled.Translate,
            illustrationResId = R.drawable.img_cat_arabic_letters_1794073876696,
            floatingBadges = listOf("أ", "ب", "ت"),
            previewItems = listOf(
                PreviewActivityItem("أ 🦁", "حرف الألف", "Letter Alif", Color(0xFF00ACC1)),
                PreviewActivityItem("ب 🦆", "حرف الباء", "Letter Baa", Color(0xFF26A69A)),
                PreviewActivityItem("ت 🍎", "حرف التاء", "Letter Taa", Color(0xFFEF5350)),
                PreviewActivityItem("🎶 أ ب", "أنشودة الحروف", "Alphabet Song", Color(0xFF7E57C2))
            )
        ),
        LearningCategory(
            id = CategoryId.ENGLISH_LETTERS,
            numberIndex = 3,
            titleAr = "الحروف الإنجليزية",
            titleEn = "English Letters",
            subtitleAr = "مكعبات الحروف A B C",
            subtitleEn = "Colorful ABC Blocks",
            buttonLabelAr = "تعلّم ABC",
            buttonLabelEn = "Explore ABC",
            luluGuideAr = "رائع! في عالم الحروف الإنجليزية سنتعلّم نطق الحروف A و B و C مع الصور الملوّنة.",
            luluGuideEn = "Awesome! In English Letters World we will learn A, B, and C blocks with colorful pictures.",
            cardBgColor = EnglishCardBg,
            accentColor = EnglishAccent,
            shadowColor = EnglishShadow,
            icon = Icons.Filled.Abc,
            illustrationResId = R.drawable.img_cat_english_letters_1794073878108,
            floatingBadges = listOf("A", "B", "C"),
            previewItems = listOf(
                PreviewActivityItem("A 🍎", "حرف A • تفاحة", "Letter A • Apple", Color(0xFFE91E63)),
                PreviewActivityItem("B 🦋", "حرف B • فراشة", "Letter B • Butterfly", Color(0xFF8E24AA)),
                PreviewActivityItem("C 🐱", "حرف C • قطة", "Letter C • Cat", Color(0xFFFF7043)),
                PreviewActivityItem("🎵 ABC", "أنشودة ABC", "ABC Song", Color(0xFF29B6F6))
            )
        ),
        LearningCategory(
            id = CategoryId.COLORS,
            numberIndex = 4,
            titleAr = "الألوان",
            titleEn = "Colors",
            subtitleAr = "قوس قزح ولوحة الألوان",
            subtitleEn = "Rainbow & Paint Palette",
            buttonLabelAr = "اكتشف الألوان",
            buttonLabelEn = "Explore Colors",
            luluGuideAr = "ما أجمل الألوان! هيا نتعرّف على ألوان قوس قزح: الأحمر، الأصفر، الأزرق، والأخضر.",
            luluGuideEn = "What lovely colors! Let's discover rainbow colors: Red, Yellow, Blue, and Green.",
            cardBgColor = ColorsCardBg,
            accentColor = ColorsAccent,
            shadowColor = ColorsShadow,
            icon = Icons.Filled.Palette,
            illustrationResId = R.drawable.img_cat_colors_1794073880642,
            floatingBadges = listOf("🌈", "🎨", "❤️"),
            previewItems = listOf(
                PreviewActivityItem("❤️", "اللون الأحمر", "Red Color", Color(0xFFE53935)),
                PreviewActivityItem("💙", "اللون الأزرق", "Blue Color", Color(0xFF1E88E5)),
                PreviewActivityItem("💛", "اللون الأصفر", "Yellow Color", Color(0xFFFDD835)),
                PreviewActivityItem("🌈", "ألوان قوس قزح", "Rainbow Colors", Color(0xFF8E24AA))
            )
        ),
        LearningCategory(
            id = CategoryId.SHAPES,
            numberIndex = 5,
            titleAr = "الأشكال",
            titleEn = "Shapes",
            subtitleAr = "أشكال هندسية ملوّنة",
            subtitleEn = "Colorful Geometric Shapes",
            buttonLabelAr = "تعرّف على الأشكال",
            buttonLabelEn = "Match Shapes",
            luluGuideAr = "انظر حولك! سنتعرّف معاً على الدائرة، المربع، المثلث، والنجمة اللامعة.",
            luluGuideEn = "Look around! Together we'll learn the Circle, Square, Triangle, and Shining Star.",
            cardBgColor = ShapesCardBg,
            accentColor = ShapesAccent,
            shadowColor = ShapesShadow,
            icon = Icons.Filled.Category,
            illustrationResId = R.drawable.img_cat_shapes_1794073880569,
            floatingBadges = listOf("⭐", "🟢", "🔺"),
            previewItems = listOf(
                PreviewActivityItem("🟢", "الدائرة", "Circle", Color(0xFF43A047)),
                PreviewActivityItem("🟧", "المربع", "Square", Color(0xFFFB8C00)),
                PreviewActivityItem("🔺", "المثلث", "Triangle", Color(0xFFE53935)),
                PreviewActivityItem("⭐", "النجمة", "Star", Color(0xFFFBC02D))
            )
        ),
        LearningCategory(
            id = CategoryId.ANIMALS,
            numberIndex = 6,
            titleAr = "الحيوانات",
            titleEn = "Animals",
            subtitleAr = "حيوانات كرتونية لطيفة",
            subtitleEn = "Cute Cartoon Animals",
            buttonLabelAr = "شاهد الحيوانات",
            buttonLabelEn = "Meet Animals",
            luluGuideAr = "أصدقاؤنا الحيوانات في انتظارك! هيا نتعرّف على الأسد الصغير والزرافة والأرنب اللطيف.",
            luluGuideEn = "Our animal friends are waiting for you! Let's meet the baby lion, giraffe, and cute bunny.",
            cardBgColor = AnimalsCardBg,
            accentColor = AnimalsAccent,
            shadowColor = AnimalsShadow,
            icon = Icons.Filled.Pets,
            illustrationResId = R.drawable.img_card_animals_nature_1794072210949,
            floatingBadges = listOf("🦁", "🦒", "🐰"),
            previewItems = listOf(
                PreviewActivityItem("🦁", "الأسد الصغير", "Baby Lion", Color(0xFFF57C00)),
                PreviewActivityItem("🦒", "الزرافة اللطيفة", "Gentle Giraffe", Color(0xFFFFB300)),
                PreviewActivityItem("🐰", "الأرنب المرح", "Happy Bunny", Color(0xFFEC407A)),
                PreviewActivityItem("🐘", "الفيل الصغير", "Little Elephant", Color(0xFF42A5F5))
            )
        ),
        LearningCategory(
            id = CategoryId.BRAIN_GAMES,
            numberIndex = 7,
            titleAr = "ألعاب الذكاء والذاكرة",
            titleEn = "Brain & Memory Games",
            subtitleAr = "ذاكرة وأنماط وألغاز مصوّرة",
            subtitleEn = "Memory, Patterns & Puzzles",
            buttonLabelAr = "العب وفكّر",
            buttonLabelEn = "Play & Think",
            luluGuideAr = "أنت طفل ذكي! هيا نلعب ألعاب الذاكرة والأنماط وتركيب الصور والعدّ معاً.",
            luluGuideEn = "You are so smart! Let's play memory match, patterns, jigsaw puzzles, and counting games together.",
            cardBgColor = BrainCardBg,
            accentColor = BrainAccent,
            shadowColor = BrainShadow,
            icon = Icons.Filled.Extension,
            illustrationResId = R.drawable.img_cat_brain_games_1794073881867,
            floatingBadges = listOf("🧩", "🃏", "💡"),
            previewItems = listOf(
                PreviewActivityItem("🃏", "تطابق الذاكرة", "Memory Match", Color(0xFF5E35B1)),
                PreviewActivityItem("🔍", "العنصر المختلف", "Odd One Out", Color(0xFF00897B)),
                PreviewActivityItem("🔄", "أكمل النمط", "Complete Pattern", Color(0xFF1E88E5)),
                PreviewActivityItem("🧩", "الألغاز المصوّرة", "Picture Puzzles", Color(0xFFD81B60))
            )
        ),
        LearningCategory(
            id = CategoryId.WRITING_DRAWING,
            numberIndex = 8,
            titleAr = "عالم الكتابة والرسم",
            titleEn = "Writing & Drawing",
            subtitleAr = "كتابة الحروف والأرقام والرسم الحر",
            subtitleEn = "Write Letters, Numbers & Draw",
            buttonLabelAr = "اكتب وارسم",
            buttonLabelEn = "Write & Draw",
            luluGuideAr = "أمسك القلم والألوان! سنتدرّب على كتابة الحروف والأرقام والرسم الحر والتلوين الجميل.",
            luluGuideEn = "Grab your pen and colors! Here we'll write letters and numbers, draw freely, and color pictures.",
            cardBgColor = DrawingCardBg,
            accentColor = DrawingAccent,
            shadowColor = DrawingShadow,
            icon = Icons.Filled.Brush,
            illustrationResId = R.drawable.img_card_art_shapes_1794072210824,
            floatingBadges = listOf("✍️", "🎨", "🖍️"),
            previewItems = listOf(
                PreviewActivityItem("✍️", "كتابة الحروف", "Write Letters", Color(0xFF00897B)),
                PreviewActivityItem("🔢", "كتابة الأرقام", "Write Numbers", Color(0xFF1E88E5)),
                PreviewActivityItem("🎨", "الرسم الحر", "Free Drawing", Color(0xFFE91E63)),
                PreviewActivityItem("🖍️", "التلوين التفاعلي", "Coloring Book", Color(0xFFFB8C00))
            )
        )
    )

    val luluGreetingsAr = listOf(
        "أهلاً بكم يا أصدقائي! أنا لولو 🌟 اختاروا قسماً جميلاً لنبدأ التعلّم والمرح معاً!",
        "يا له من يوم مشمس وجميل! 🌈 أي عالم تحبون أن نستكشفه اليوم؟",
        "أنتم رائعون! ⭐ اجمعوا النجوم الذهبية في كل قسم خلال رحلتكم التعليمية!"
    )

    val luluGreetingsEn = listOf(
        "Hello my friends! I'm Lulu 🌟 Pick a colorful world below to start learning and playing together!",
        "What a sunny and joyful day! 🌈 Which fun learning category would you like to explore today?",
        "You're doing amazing! ⭐ Collect golden stars in every category on your learning journey!"
    )
}
