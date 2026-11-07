package com.example.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Abc
import androidx.compose.material.icons.filled.AutoAwesome
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
            subtitleAr = "عدّ وامرح من ١ إلى ٢٠",
            subtitleEn = "Count & Play from 1 to 20",
            buttonLabelAr = "هيا نعدّ!",
            buttonLabelEn = "Let's Count!",
            luluGuideAr = "أهلاً بك في عالم الأرقام! هنا سنتعلم العدّ من ١ إلى ٢٠ مع النجوم والفواكه اللذيذة!",
            luluGuideEn = "Welcome to Numbers World! Here we will learn to count from 1 to 20 with stars and yummy fruits!",
            cardBgColor = NumbersCardBg,
            accentColor = NumbersAccent,
            shadowColor = NumbersShadow,
            icon = Icons.Filled.Filter1,
            illustrationResId = R.drawable.img_card_numbers_letters_1794072209030,
            floatingBadges = listOf("١", "2", "٣", "4"),
            previewItems = listOf(
                PreviewActivityItem("١ ٢ ٣", "الأرقام العربية", "Arabic Numbers", Color(0xFFFFB300)),
                PreviewActivityItem("1 2 3", "الأرقام الإنجليزية", "English Numbers", Color(0xFFFB8C00)),
                PreviewActivityItem("🍎×5", "عدّ الفواكه", "Count the Fruits", Color(0xFF43A047)),
                PreviewActivityItem("🎵 10", "أغنية الأرقام", "Numbers Song", Color(0xFF1E88E5))
            )
        ),
        LearningCategory(
            id = CategoryId.ARABIC_LETTERS,
            numberIndex = 2,
            titleAr = "الحروف العربية",
            titleEn = "Arabic Letters",
            subtitleAr = "تعلّم حروف الهجاء من أ إلى ي",
            subtitleEn = "Learn Arabic Alphabet أ ب ت",
            buttonLabelAr = "اقرأ معي!",
            buttonLabelEn = "Read with Me!",
            luluGuideAr = "مرحباً بك في بستان الحروف العربية! سنكتشف معاً حروف أ، ب، ت مع الكلمات والصور الجميلة!",
            luluGuideEn = "Welcome to the Arabic Letters Garden! Together we'll discover Alif, Baa, Taa with joyful words!",
            cardBgColor = ArabicCardBg,
            accentColor = ArabicAccent,
            shadowColor = ArabicShadow,
            icon = Icons.Filled.Translate,
            illustrationResId = R.drawable.img_card_numbers_letters_1794072209030,
            floatingBadges = listOf("أ", "ب", "ت", "ث"),
            previewItems = listOf(
                PreviewActivityItem("أ 🦁", "أرنب وأسد", "Alif Words", Color(0xFF00ACC1)),
                PreviewActivityItem("ب 🦆", "بطة جميلة", "Baa Words", Color(0xFF26A69A)),
                PreviewActivityItem("ت 🍎", "تفاحة حمراء", "Taa Words", Color(0xFFEF5350)),
                PreviewActivityItem("🎶 أ ب", "أنشودة الحروف", "Alphabet Nasheed", Color(0xFF7E57C2))
            )
        ),
        LearningCategory(
            id = CategoryId.ENGLISH_LETTERS,
            numberIndex = 3,
            titleAr = "الحروف الإنجليزية",
            titleEn = "English Letters",
            subtitleAr = "اكتشف الحروف من A إلى Z",
            subtitleEn = "Explore Letters A to Z",
            buttonLabelAr = "هيا نكتشف!",
            buttonLabelEn = "Explore ABC!",
            luluGuideAr = "رائع! في جزيرة الحروف الإنجليزية سنتعلم نطق A B C مع أصدقائنا الصغار!",
            luluGuideEn = "Awesome! On English Letters Island we will learn phonics for A, B, C with our cute friends!",
            cardBgColor = EnglishCardBg,
            accentColor = EnglishAccent,
            shadowColor = EnglishShadow,
            icon = Icons.Filled.Abc,
            illustrationResId = R.drawable.img_card_numbers_letters_1794072209030,
            floatingBadges = listOf("A", "B", "C", "D"),
            previewItems = listOf(
                PreviewActivityItem("A 🍎", "Apple & Ant", "Letter A", Color(0xFFE91E63)),
                PreviewActivityItem("B 🦋", "Butterfly", "Letter B", Color(0xFF8E24AA)),
                PreviewActivityItem("C 🐱", "Cute Cat", "Letter C", Color(0xFFFF7043)),
                PreviewActivityItem("🎵 ABC", "أغنية ABC", "Phonics Song", Color(0xFF29B6F6))
            )
        ),
        LearningCategory(
            id = CategoryId.COLORS,
            numberIndex = 4,
            titleAr = "الألوان",
            titleEn = "Colors",
            subtitleAr = "قوس قزح والألوان المبهجة",
            subtitleEn = "Rainbow & Cheerful Colors",
            buttonLabelAr = "لوّن عالمك!",
            buttonLabelEn = "Color Now!",
            luluGuideAr = "يا لها من ألوان زاهية! هيا نمزج ألوان قوس قزح ونتعرف على الأحمر والأصفر والأزرق!",
            luluGuideEn = "What bright colors! Let's mix rainbow colors and discover Red, Yellow, Blue, and Green!",
            cardBgColor = ColorsCardBg,
            accentColor = ColorsAccent,
            shadowColor = ColorsShadow,
            icon = Icons.Filled.Palette,
            illustrationResId = R.drawable.img_card_art_shapes_1794072210824,
            floatingBadges = listOf("🌈", "🎨", "❤️", "💛"),
            previewItems = listOf(
                PreviewActivityItem("❤️", "الأحمر الزاهي", "Bright Red", Color(0xFFE53935)),
                PreviewActivityItem("💙", "الأزرق السماوي", "Sky Blue", Color(0xFF1E88E5)),
                PreviewActivityItem("💛", "الأصفر المشمس", "Sunny Yellow", Color(0xFFFDD835)),
                PreviewActivityItem("🌈", "ألوان قوس قزح", "Rainbow Mix", Color(0xFF8E24AA))
            )
        ),
        LearningCategory(
            id = CategoryId.SHAPES,
            numberIndex = 5,
            titleAr = "الأشكال",
            titleEn = "Shapes",
            subtitleAr = "الدائرة، المربع، النجمة والمزيد",
            subtitleEn = "Circle, Square, Star & More",
            buttonLabelAr = "ركّب الأشكال!",
            buttonLabelEn = "Match Shapes!",
            luluGuideAr = "انظر حولك! الأشكال في كل مكان: الشمس دائرة، والنجوم تتلألأ في السماء!",
            luluGuideEn = "Look around! Shapes are everywhere: the sun is a circle, and stars twinkle in the sky!",
            cardBgColor = ShapesCardBg,
            accentColor = ShapesAccent,
            shadowColor = ShapesShadow,
            icon = Icons.Filled.Category,
            illustrationResId = R.drawable.img_card_art_shapes_1794072210824,
            floatingBadges = listOf("⭐", "🟢", "🔺", "🔷"),
            previewItems = listOf(
                PreviewActivityItem("🟢", "الدائرة المرحة", "Happy Circle", Color(0xFF43A047)),
                PreviewActivityItem("🟧", "المربع المتساوي", "Friendly Square", Color(0xFFFB8C00)),
                PreviewActivityItem("🔺", "المثلث الذكي", "Smart Triangle", Color(0xFFE53935)),
                PreviewActivityItem("⭐", "النجمة اللامعة", "Shining Star", Color(0xFFFBC02D))
            )
        ),
        LearningCategory(
            id = CategoryId.ANIMALS,
            numberIndex = 6,
            titleAr = "الحيوانات",
            titleEn = "Animals",
            subtitleAr = "أصوات ومغامرات الحيوانات اللطيفة",
            subtitleEn = "Cute Animals & Their Sounds",
            buttonLabelAr = "زر الغابة!",
            buttonLabelEn = "Visit Safari!",
            luluGuideAr = "أصدقاؤنا الحيوانات بانتظارك! استمع لصوت الأسد الصغير والزرافة الطويلة والأرنب اللطيف!",
            luluGuideEn = "Our animal friends are waiting for you! Listen to the baby lion, tall giraffe, and cute bunny!",
            cardBgColor = AnimalsCardBg,
            accentColor = AnimalsAccent,
            shadowColor = AnimalsShadow,
            icon = Icons.Filled.Pets,
            illustrationResId = R.drawable.img_card_animals_nature_1794072210949,
            floatingBadges = listOf("🦁", "🦒", "🐰", "🐘"),
            previewItems = listOf(
                PreviewActivityItem("🦁", "الأسد الشجاع", "Brave Lion", Color(0xFFF57C00)),
                PreviewActivityItem("🦒", "الزرافة اللطيفة", "Gentle Giraffe", Color(0xFFFFB300)),
                PreviewActivityItem("🐰", "الأرنب السريع", "Bouncy Bunny", Color(0xFFEC407A)),
                PreviewActivityItem("🐘", "الفيل المرح", "Playful Elephant", Color(0xFF42A5F5))
            )
        ),
        LearningCategory(
            id = CategoryId.BRAIN_GAMES,
            numberIndex = 7,
            titleAr = "ألعاب الذكاء",
            titleEn = "Brain Games",
            subtitleAr = "ألغاز، ذاكرة وتفكير ممتع",
            subtitleEn = "Puzzles, Memory & Fun Logic",
            buttonLabelAr = "شغّل عقلك!",
            buttonLabelEn = "Play Puzzle!",
            luluGuideAr = "أنت طفل ذكي جداً! هيا نلعب ألعاب الذاكرة وتركيب الصور المسلية معاً!",
            luluGuideEn = "You are super smart! Let's play memory matching, shadow puzzles, and fun logic games together!",
            cardBgColor = BrainCardBg,
            accentColor = BrainAccent,
            shadowColor = BrainShadow,
            icon = Icons.Filled.Extension,
            illustrationResId = R.drawable.img_card_animals_nature_1794072210949,
            floatingBadges = listOf("🧩", "💡", "🎯", "🏆"),
            previewItems = listOf(
                PreviewActivityItem("🧩", "تركيب الصور", "Jigsaw Puzzle", Color(0xFF5E35B1)),
                PreviewActivityItem("🃏", "بطاقات الذاكرة", "Memory Match", Color(0xFF00897B)),
                PreviewActivityItem("🔍", "ابحث عن الظل", "Shadow Finder", Color(0xFF3949AB)),
                PreviewActivityItem("🏆", "تحدي الأذكياء", "Smart Maze", Color(0xFFD81B60))
            )
        ),
        LearningCategory(
            id = CategoryId.WRITING_DRAWING,
            numberIndex = 8,
            titleAr = "الكتابة والرسم",
            titleEn = "Writing & Drawing",
            subtitleAr = "ارسم ولوّن وتتبع الخطوط",
            subtitleEn = "Draw, Color & Trace Lines",
            buttonLabelAr = "ابدأ الرسم!",
            buttonLabelEn = "Start Drawing!",
            luluGuideAr = "أمسك فرشاتك السحرية! هنا سنرسم أجمل اللوحات ونتتبع الحروف والأرقام بألوان قوس قزح!",
            luluGuideEn = "Grab your magic paintbrush! Here we'll paint wonderful pictures and trace letters in rainbow colors!",
            cardBgColor = DrawingCardBg,
            accentColor = DrawingAccent,
            shadowColor = DrawingShadow,
            icon = Icons.Filled.Brush,
            illustrationResId = R.drawable.img_card_art_shapes_1794072210824,
            floatingBadges = listOf("🖌️", "🖍️", "✨", "🎨"),
            previewItems = listOf(
                PreviewActivityItem("✏️", "تتبع الخطوط", "Line Tracing", Color(0xFF00897B)),
                PreviewActivityItem("🖍️", "دفتر التلوين", "Coloring Book", Color(0xFFE91E63)),
                PreviewActivityItem("🖌️", "اللوحة الحرة", "Free Canvas", Color(0xFF7B1FA2)),
                PreviewActivityItem("✨", "فرشاة النجوم", "Magic Sparkle Brush", Color(0xFFFBC02D))
            )
        )
    )

    val luluGreetingsAr = listOf(
        "مرحباً يا بطل! أنا صديقتك لولو 🌟 اختر بطاقة لنبدأ رحلة التعلّم والمرح معاً!",
        "يا له من يوم مشمس وجميل! 🌈 أي مغامرة تحب أن نستكشفها اليوم؟",
        "أنت رائع! ⭐ اجمع النجوم الذهبية في كل قسم لتكمل رحلتك التعليمية!"
    )

    val luluGreetingsEn = listOf(
        "Hello little explorer! I'm your friend Lulu 🌟 Pick a card to start our fun learning adventure!",
        "What a sunny and joyful day! 🌈 Which magical world would you like to explore today?",
        "You're doing amazing! ⭐ Collect golden stars in every category on your learning journey!"
    )
}
