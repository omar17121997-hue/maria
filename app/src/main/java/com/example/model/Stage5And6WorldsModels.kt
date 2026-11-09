package com.example.model

import androidx.compose.ui.graphics.Color

// =====================================================================
// STAGE 5: BRAIN GAMES WORLD | عالم ألعاب الذكاء والذاكرة
// =====================================================================

enum class BrainGameActivityId(
    val key: String,
    val titleAr: String,
    val titleEn: String,
    val subtitleAr: String,
    val subtitleEn: String,
    val badgeEmoji: String,
    val accentColor: Color,
    val shadowColor: Color,
    val cardBgColor: Color
) {
    MEMORY_MATCH(
        key = "memory_match",
        titleAr = "لعبة تطابق الذاكرة",
        titleEn = "Memory Match",
        subtitleAr = "اقلب البطاقات وطابق الصور المتشابهة",
        subtitleEn = "Flip cards and match identical pictures",
        badgeEmoji = "🃏",
        accentColor = Color(0xFF5E35B1),
        shadowColor = Color(0xFF311B92),
        cardBgColor = Color(0xFFEDE7F6)
    ),
    ODD_ONE_OUT(
        key = "odd_one_out",
        titleAr = "اكتشف العنصر المختلف",
        titleEn = "Odd One Out",
        subtitleAr = "ابحث عن الصورة المختلفة بين المجموعة",
        subtitleEn = "Spot the picture that doesn't belong",
        badgeEmoji = "🔍",
        accentColor = Color(0xFF00897B),
        shadowColor = Color(0xFF004D40),
        cardBgColor = Color(0xFFE0F2F1)
    ),
    COMPLETE_PATTERN(
        key = "complete_pattern",
        titleAr = "أكمل النمط",
        titleEn = "Complete the Pattern",
        subtitleAr = "اكتشف الترتيب الصحيح واختر العنصر التالي",
        subtitleEn = "Find the sequence and pick what comes next",
        badgeEmoji = "🔄",
        accentColor = Color(0xFF1E88E5),
        shadowColor = Color(0xFF0D47A1),
        cardBgColor = Color(0xFFE3F2FD)
    ),
    ORDER_BY_SIZE(
        key = "order_by_size",
        titleAr = "الترتيب حسب الحجم",
        titleEn = "Order by Size",
        subtitleAr = "رتّب العناصر من الأصغر للأكبر أو العكس بالضغط",
        subtitleEn = "Tap items from smallest to largest or largest to smallest",
        badgeEmoji = "📏",
        accentColor = Color(0xFFFB8C00),
        shadowColor = Color(0xFFE65100),
        cardBgColor = Color(0xFFFFF3E0)
    ),
    PICTURE_PUZZLES(
        key = "picture_puzzles",
        titleAr = "الألغاز المصورة",
        titleEn = "Picture Puzzles",
        subtitleAr = "ركّب قطع الصور الكرتونية (٤ و ٦ و ٩ قطع)",
        subtitleEn = "Assemble cartoon puzzles (4, 6 & 9 pieces)",
        badgeEmoji = "🧩",
        accentColor = Color(0xFFD81B60),
        shadowColor = Color(0xFF880E4F),
        cardBgColor = Color(0xFFFCE4EC)
    ),
    COUNT_AND_THINK(
        key = "count_and_think",
        titleAr = "العدّ والتفكير",
        titleEn = "Count & Think",
        subtitleAr = "عدّ الأشياء واختر المجموعة الأكبر أو الأصغر",
        subtitleEn = "Count items and pick the larger, smaller, or exact group",
        badgeEmoji = "🔢",
        accentColor = Color(0xFF43A047),
        shadowColor = Color(0xFF1B5E20),
        cardBgColor = Color(0xFFE8F5E9)
    );

    fun title(lang: AppLanguage): String = if (lang == AppLanguage.AR) titleAr else titleEn
    fun subtitle(lang: AppLanguage): String = if (lang == AppLanguage.AR) subtitleAr else subtitleEn
}

data class MemoryCardThemeItem(
    val id: String,
    val emoji: String,
    val nameAr: String,
    val nameEn: String,
    val categoryAr: String,
    val categoryEn: String,
    val bgColor: Color
) {
    fun name(lang: AppLanguage): String = if (lang == AppLanguage.AR) nameAr else nameEn
}

data class OddOneOutQuestion(
    val id: Int,
    val promptAr: String,
    val promptEn: String,
    val items: List<OddOptionItem>,
    val oddIndex: Int,
    val explanationAr: String,
    val explanationEn: String
) {
    fun prompt(lang: AppLanguage): String = if (lang == AppLanguage.AR) promptAr else promptEn
    fun explanation(lang: AppLanguage): String = if (lang == AppLanguage.AR) explanationAr else explanationEn
}

data class OddOptionItem(
    val emoji: String,
    val labelAr: String,
    val labelEn: String,
    val bgColor: Color
) {
    fun label(lang: AppLanguage): String = if (lang == AppLanguage.AR) labelAr else labelEn
}

data class PatternSequenceQuestion(
    val id: Int,
    val level: Int,
    val promptAr: String,
    val promptEn: String,
    val sequenceEmojis: List<String>,
    val sequenceLabelsAr: List<String>,
    val sequenceLabelsEn: List<String>,
    val options: List<OddOptionItem>,
    val correctOptionIndex: Int
) {
    fun prompt(lang: AppLanguage): String = if (lang == AppLanguage.AR) promptAr else promptEn
}

enum class SizeOrderDirection {
    SMALLEST_TO_LARGEST,
    LARGEST_TO_SMALLEST
}

data class SizeOrderItem(
    val id: String,
    val sizeRank: Int, // 1 = smallest, 2 = medium, 3 = large, 4 = extra large
    val scaleFactor: Float,
    val sizeLabelAr: String,
    val sizeLabelEn: String
) {
    fun sizeLabel(lang: AppLanguage): String = if (lang == AppLanguage.AR) sizeLabelAr else sizeLabelEn
}

data class SizeOrderChallenge(
    val id: Int,
    val emoji: String,
    val itemNameAr: String,
    val itemNameEn: String,
    val direction: SizeOrderDirection,
    val cardColor: Color,
    val items: List<SizeOrderItem>
) {
    fun prompt(lang: AppLanguage): String = when (direction) {
        SizeOrderDirection.SMALLEST_TO_LARGEST ->
            if (lang == AppLanguage.AR) "رتّبي $itemNameAr من الأصغر إلى الأكبر بالضغط عليها!"
            else "Tap the $itemNameEn from Smallest to Largest!"
        SizeOrderDirection.LARGEST_TO_SMALLEST ->
            if (lang == AppLanguage.AR) "رتّبي $itemNameAr من الأكبر إلى الأصغر بالضغط عليها!"
            else "Tap the $itemNameEn from Largest to Smallest!"
    }
}

data class PicturePuzzleTemplate(
    val id: String,
    val titleAr: String,
    val titleEn: String,
    val pieceCount: Int, // 4, 6, or 9
    val rows: Int,
    val cols: Int,
    val themeColor: Color,
    val tileEmojis: List<String>,
    val tileColors: List<Color>,
    val centerEmoji: String
) {
    fun title(lang: AppLanguage): String = if (lang == AppLanguage.AR) titleAr else titleEn
}

enum class CountThinkQuestionType {
    LARGEST_GROUP,
    SMALLEST_GROUP,
    EXACT_COUNT
}

data class CountGroupOption(
    val emoji: String,
    val count: Int,
    val labelAr: String,
    val labelEn: String,
    val cardColor: Color
) {
    fun numberSymbol(lang: AppLanguage): String =
        NumbersCurriculum.byValue(count.coerceIn(1, 10)).symbol(lang)
}

data class CountAndThinkQuestion(
    val id: Int,
    val type: CountThinkQuestionType,
    val targetCount: Int = 0,
    val promptAr: String,
    val promptEn: String,
    val groups: List<CountGroupOption>,
    val correctGroupIndex: Int
) {
    fun prompt(lang: AppLanguage): String = if (lang == AppLanguage.AR) promptAr else promptEn
}

object BrainGamesCurriculum {
    val memoryPool: List<MemoryCardThemeItem> = listOf(
        MemoryCardThemeItem("lion", "🦁", "أسد", "Lion", "حيوانات", "Animals", Color(0xFFFFF3E0)),
        MemoryCardThemeItem("rabbit", "🐰", "أرنب", "Rabbit", "حيوانات", "Animals", Color(0xFFFCE4EC)),
        MemoryCardThemeItem("elephant", "🐘", "فيل", "Elephant", "حيوانات", "Animals", Color(0xFFE3F2FD)),
        MemoryCardThemeItem("apple", "🍎", "تفاحة", "Apple", "فواكه", "Fruits", Color(0xFFFFEBEE)),
        MemoryCardThemeItem("banana", "🍌", "موزة", "Banana", "فواكه", "Fruits", Color(0xFFFFFDE7)),
        MemoryCardThemeItem("grapes", "🍇", "عنب", "Grapes", "فواكه", "Fruits", Color(0xFFF3E5F5)),
        MemoryCardThemeItem("star", "⭐", "نجمة", "Star", "أشكال", "Shapes", Color(0xFFFFF8E1)),
        MemoryCardThemeItem("heart", "❤️", "قلب", "Heart", "أشكال", "Shapes", Color(0xFFFCE4EC))
    )

    val oddOneOutQuestions: List<OddOneOutQuestion> = listOf(
        OddOneOutQuestion(
            id = 1,
            promptAr = "أين الصورة المختلفة؟ (ثلاث فواكه وصورة واحدة مختلفة)",
            promptEn = "Which one is different? (Three fruits and one different)",
            items = listOf(
                OddOptionItem("🍎", "تفاحة", "Apple", Color(0xFFFFEBEE)),
                OddOptionItem("🍌", "موزة", "Banana", Color(0xFFFFFDE7)),
                OddOptionItem("🍊", "برتقالة", "Orange", Color(0xFFFFF3E0)),
                OddOptionItem("🚗", "سيارة", "Car", Color(0xFFE3F2FD))
            ),
            oddIndex = 3,
            explanationAr = "أحسنتِ! السيارة ليست من الفواكه!",
            explanationEn = "Great job! The Car is not a fruit!"
        ),
        OddOneOutQuestion(
            id = 2,
            promptAr = "اكتشفي العنصر المختلف بين هذه الحيوانات:",
            promptEn = "Spot the odd one out among these animals:",
            items = listOf(
                OddOptionItem("🦁", "أسد", "Lion", Color(0xFFFFF3E0)),
                OddOptionItem("🐱", "قطة", "Cat", Color(0xFFFCE4EC)),
                OddOptionItem("🐶", "كلب", "Dog", Color(0xFFEFEBE9)),
                OddOptionItem("🟢", "دائرة خضراء", "Green Circle", Color(0xFFE8F5E9))
            ),
            oddIndex = 3,
            explanationAr = "ممتاز! الدائرة شكل هندسي وليست حيواناً!",
            explanationEn = "Awesome! The Circle is a shape, not an animal!"
        ),
        OddOneOutQuestion(
            id = 3,
            promptAr = "أين الشكل المختلف عن الأشكال الهندسية؟",
            promptEn = "Which picture is NOT a geometric shape?",
            items = listOf(
                OddOptionItem("🔵", "دائرة", "Circle", Color(0xFFE3F2FD)),
                OddOptionItem("🟧", "مربع", "Square", Color(0xFFFFF3E0)),
                OddOptionItem("🦆", "بطة", "Duck", Color(0xFFFFFDE7)),
                OddOptionItem("🔺", "مثلث", "Triangle", Color(0xFFFFEBEE))
            ),
            oddIndex = 2,
            explanationAr = "رائع! البطة طائر لطيف وليست شكلاً هندسياً!",
            explanationEn = "Wonderful! The Duck is a bird, not a geometric shape!"
        ),
        OddOneOutQuestion(
            id = 4,
            promptAr = "ما الشيء المختلف الذي لا يطير في السماء؟",
            promptEn = "Which one does NOT fly in the sky?",
            items = listOf(
                OddOptionItem("🐦", "عصفور", "Bird", Color(0xFFE1F5FE)),
                OddOptionItem("🦋", "فراشة", "Butterfly", Color(0xFFF3E5F5)),
                OddOptionItem("🐟", "سمكة", "Fish", Color(0xFFE0F7FA)),
                OddOptionItem("✈️", "طائرة", "Airplane", Color(0xFFEDE7F6))
            ),
            oddIndex = 2,
            explanationAr = "ذكية جداً! السمكة تسبح في البحر!",
            explanationEn = "So smart! The Fish swims in the sea!"
        ),
        OddOneOutQuestion(
            id = 5,
            promptAr = "أين العنصر المختلف عن أدوات الرسم؟",
            promptEn = "Which item is NOT for drawing & coloring?",
            items = listOf(
                OddOptionItem("✏️", "قلم رصاص", "Pencil", Color(0xFFFFF8E1)),
                OddOptionItem("🖍️", "قلم تلوين", "Crayon", Color(0xFFFFEBEE)),
                OddOptionItem("🎨", "لوحة ألوان", "Palette", Color(0xFFF3E5F5)),
                OddOptionItem("🐘", "فيل", "Elephant", Color(0xFFE3F2FD))
            ),
            oddIndex = 3,
            explanationAr = "أحسنتِ! الفيل حيوان كبير وليس من أدوات الرسم!",
            explanationEn = "Well done! The Elephant is an animal, not an art tool!"
        )
    )

    val patternQuestions: List<PatternSequenceQuestion> = listOf(
        PatternSequenceQuestion(
            id = 1,
            level = 1,
            promptAr = "أكملي نمط الألوان: أحمر، أزرق، أحمر، أزرق، ماذا يأتي بعده؟",
            promptEn = "Complete the color pattern: Red, Blue, Red, Blue, what comes next?",
            sequenceEmojis = listOf("🔴", "🔵", "🔴", "🔵"),
            sequenceLabelsAr = listOf("أحمر", "أزرق", "أحمر", "أزرق"),
            sequenceLabelsEn = listOf("Red", "Blue", "Red", "Blue"),
            options = listOf(
                OddOptionItem("🔴", "أحمر", "Red", Color(0xFFFFEBEE)),
                OddOptionItem("🔵", "أزرق", "Blue", Color(0xFFE3F2FD)),
                OddOptionItem("🟡", "أصفر", "Yellow", Color(0xFFFFFDE7))
            ),
            correctOptionIndex = 0
        ),
        PatternSequenceQuestion(
            id = 2,
            level = 1,
            promptAr = "أكملي نمط الأشكال: دائرة، مربع، دائرة، مربع، ؟",
            promptEn = "Complete the shape pattern: Circle, Square, Circle, Square, ?",
            sequenceEmojis = listOf("🟢", "🟧", "🟢", "🟧"),
            sequenceLabelsAr = listOf("دائرة", "مربع", "دائرة", "مربع"),
            sequenceLabelsEn = listOf("Circle", "Square", "Circle", "Square"),
            options = listOf(
                OddOptionItem("🔺", "مثلث", "Triangle", Color(0xFFFFEBEE)),
                OddOptionItem("🟢", "دائرة", "Circle", Color(0xFFE8F5E9)),
                OddOptionItem("🟧", "مربع", "Square", Color(0xFFFFF3E0))
            ),
            correctOptionIndex = 1
        ),
        PatternSequenceQuestion(
            id = 3,
            level = 2,
            promptAr = "أكملي نمط الفواكه: تفاحة، موزة، تفاحة، موزة، ؟",
            promptEn = "Complete the fruit pattern: Apple, Banana, Apple, Banana, ?",
            sequenceEmojis = listOf("🍎", "🍌", "🍎", "🍌"),
            sequenceLabelsAr = listOf("تفاحة", "موزة", "تفاحة", "موزة"),
            sequenceLabelsEn = listOf("Apple", "Banana", "Apple", "Banana"),
            options = listOf(
                OddOptionItem("🍇", "عنب", "Grapes", Color(0xFFF3E5F5)),
                OddOptionItem("🍌", "موزة", "Banana", Color(0xFFFFFDE7)),
                OddOptionItem("🍎", "تفاحة", "Apple", Color(0xFFFFEBEE))
            ),
            correctOptionIndex = 2
        ),
        PatternSequenceQuestion(
            id = 4,
            level = 2,
            promptAr = "أكملي نمط الحيوانات: قطة، كلب، قطة، كلب، ؟",
            promptEn = "Complete the animal pattern: Cat, Dog, Cat, Dog, ?",
            sequenceEmojis = listOf("🐱", "🐶", "🐱", "🐶"),
            sequenceLabelsAr = listOf("قطة", "كلب", "قطة", "كلب"),
            sequenceLabelsEn = listOf("Cat", "Dog", "Cat", "Dog"),
            options = listOf(
                OddOptionItem("🐱", "قطة", "Cat", Color(0xFFFCE4EC)),
                OddOptionItem("🦁", "أسد", "Lion", Color(0xFFFFF3E0)),
                OddOptionItem("🐶", "كلب", "Dog", Color(0xFFEFEBE9))
            ),
            correctOptionIndex = 0
        ),
        PatternSequenceQuestion(
            id = 5,
            level = 3,
            promptAr = "تحدي الأذكياء: نجمة، نجمة، قلب، نجمة، نجمة، ؟",
            promptEn = "Smart Challenge: Star, Star, Heart, Star, Star, ?",
            sequenceEmojis = listOf("⭐", "⭐", "❤️", "⭐", "⭐"),
            sequenceLabelsAr = listOf("نجمة", "نجمة", "قلب", "نجمة", "نجمة"),
            sequenceLabelsEn = listOf("Star", "Star", "Heart", "Star", "Star"),
            options = listOf(
                OddOptionItem("⭐", "نجمة", "Star", Color(0xFFFFF8E1)),
                OddOptionItem("❤️", "قلب", "Heart", Color(0xFFFCE4EC)),
                OddOptionItem("🌙", "قمر", "Moon", Color(0xFFE8EAF6))
            ),
            correctOptionIndex = 1
        )
    )

    val sizeOrderChallenges: List<SizeOrderChallenge> = listOf(
        SizeOrderChallenge(
            id = 1,
            emoji = "🐘",
            itemNameAr = "الفيلة اللطيفة",
            itemNameEn = "Cute Elephants",
            direction = SizeOrderDirection.SMALLEST_TO_LARGEST,
            cardColor = Color(0xFFE3F2FD),
            items = listOf(
                SizeOrderItem("s1", 1, 0.56f, "صغير جداً", "Tiny"),
                SizeOrderItem("s2", 2, 0.72f, "صغير", "Small"),
                SizeOrderItem("s3", 3, 0.88f, "متوسط", "Medium"),
                SizeOrderItem("s4", 4, 1.06f, "كبير", "Large")
            )
        ),
        SizeOrderChallenge(
            id = 2,
            emoji = "⭐",
            itemNameAr = "النجوم اللامعة",
            itemNameEn = "Shining Stars",
            direction = SizeOrderDirection.LARGEST_TO_SMALLEST,
            cardColor = Color(0xFFFFF8E1),
            items = listOf(
                SizeOrderItem("s1", 1, 0.56f, "صغيرة جداً", "Tiny"),
                SizeOrderItem("s2", 2, 0.72f, "صغيرة", "Small"),
                SizeOrderItem("s3", 3, 0.88f, "متوسطة", "Medium"),
                SizeOrderItem("s4", 4, 1.06f, "كبيرة", "Large")
            )
        ),
        SizeOrderChallenge(
            id = 3,
            emoji = "🧸",
            itemNameAr = "الدببة الجميلة",
            itemNameEn = "Teddy Bears",
            direction = SizeOrderDirection.SMALLEST_TO_LARGEST,
            cardColor = Color(0xFFFFF3E0),
            items = listOf(
                SizeOrderItem("s1", 1, 0.56f, "صغير جداً", "Tiny"),
                SizeOrderItem("s2", 2, 0.72f, "صغير", "Small"),
                SizeOrderItem("s3", 3, 0.88f, "متوسط", "Medium"),
                SizeOrderItem("s4", 4, 1.06f, "كبير", "Large")
            )
        ),
        SizeOrderChallenge(
            id = 4,
            emoji = "🦁",
            itemNameAr = "الأسود الشجاعة",
            itemNameEn = "Brave Lions",
            direction = SizeOrderDirection.LARGEST_TO_SMALLEST,
            cardColor = Color(0xFFFCE4EC),
            items = listOf(
                SizeOrderItem("s1", 1, 0.56f, "صغير جداً", "Tiny"),
                SizeOrderItem("s2", 2, 0.72f, "صغير", "Small"),
                SizeOrderItem("s3", 3, 0.88f, "متوسط", "Medium"),
                SizeOrderItem("s4", 4, 1.06f, "كبير", "Large")
            )
        )
    )

    val picturePuzzles: List<PicturePuzzleTemplate> = listOf(
        PicturePuzzleTemplate(
            id = "puzzle_4_garden",
            titleAr = "حديقة الحيوانات المرحة (٤ قطع)",
            titleEn = "Sunny Animal Garden (4 Pieces)",
            pieceCount = 4,
            rows = 2,
            cols = 2,
            themeColor = Color(0xFF43A047),
            tileEmojis = listOf("☀️", "🦋", "🦁", "🌻"),
            tileColors = listOf(
                Color(0xFFFFF9C4),
                Color(0xFFE1F5FE),
                Color(0xFFFFE0B2),
                Color(0xFFC8E6C9)
            ),
            centerEmoji = "🦁"
        ),
        PicturePuzzleTemplate(
            id = "puzzle_6_sea",
            titleAr = "مغامرة البحر الجميل (٦ قطع)",
            titleEn = "Ocean Friends Adventure (6 Pieces)",
            pieceCount = 6,
            rows = 2,
            cols = 3,
            themeColor = Color(0xFF0288D1),
            tileEmojis = listOf("⛵", "☀️", "☁️", "🐠", "🐢", "🐙"),
            tileColors = listOf(
                Color(0xFFE1F5FE),
                Color(0xFFFFF9C4),
                Color(0xFFE3F2FD),
                Color(0xFFB3E5FC),
                Color(0xFFB2DFDB),
                Color(0xFFD1C4E9)
            ),
            centerEmoji = "🐢"
        ),
        PicturePuzzleTemplate(
            id = "puzzle_9_kingdom",
            titleAr = "قلعة قوس قزح السحرية (٩ قطع)",
            titleEn = "Rainbow Magic Kingdom (9 Pieces)",
            pieceCount = 9,
            rows = 3,
            cols = 3,
            themeColor = Color(0xFF8E24AA),
            tileEmojis = listOf("🌈", "☀️", "☁️", "🦋", "🏰", "🎈", "🌸", "🐰", "🌷"),
            tileColors = listOf(
                Color(0xFFF3E5F5),
                Color(0xFFFFF9C4),
                Color(0xFFE1F5FE),
                Color(0xFFFCE4EC),
                Color(0xFFEDE7F6),
                Color(0xFFFFEBEE),
                Color(0xFFE8F5E9),
                Color(0xFFFFF3E0),
                Color(0xFFE0F2F1)
            ),
            centerEmoji = "🏰"
        )
    )

    val countAndThinkQuestions: List<CountAndThinkQuestion> = listOf(
        CountAndThinkQuestion(
            id = 1,
            type = CountThinkQuestionType.LARGEST_GROUP,
            promptAr = "عدّي الفواكه واختاري المجموعة الأكبر عدداً!",
            promptEn = "Count the fruits and pick the LARGEST group!",
            groups = listOf(
                CountGroupOption("🍎", 2, "تفاحتان", "2 Apples", Color(0xFFFFEBEE)),
                CountGroupOption("🍎", 5, "٥ تفاحات", "5 Apples", Color(0xFFFFCDD2)),
                CountGroupOption("🍎", 3, "٣ تفاحات", "3 Apples", Color(0xFFFCE4EC))
            ),
            correctGroupIndex = 1
        ),
        CountAndThinkQuestion(
            id = 2,
            type = CountThinkQuestionType.SMALLEST_GROUP,
            promptAr = "عدّي النجوم واختاري المجموعة الأصغر عدداً!",
            promptEn = "Count the stars and pick the SMALLEST group!",
            groups = listOf(
                CountGroupOption("⭐", 4, "٤ نجوم", "4 Stars", Color(0xFFFFF8E1)),
                CountGroupOption("⭐", 1, "نجمة واحدة", "1 Star", Color(0xFFFFFDE7)),
                CountGroupOption("⭐", 6, "٦ نجوم", "6 Stars", Color(0xFFFFECB3))
            ),
            correctGroupIndex = 1
        ),
        CountAndThinkQuestion(
            id = 3,
            type = CountThinkQuestionType.EXACT_COUNT,
            targetCount = 4,
            promptAr = "أين المجموعة التي تحتوي على ٤ بطات بالضبط؟ (الرقم ٤)",
            promptEn = "Which group has EXACTLY 4 ducks? (Number 4)",
            groups = listOf(
                CountGroupOption("🦆", 2, "بطتان", "2 Ducks", Color(0xFFE1F5FE)),
                CountGroupOption("🦆", 4, "٤ بطات", "4 Ducks", Color(0xFFFFF9C4)),
                CountGroupOption("🦆", 6, "٦ بطات", "6 Ducks", Color(0xFFE8F5E9))
            ),
            correctGroupIndex = 1
        ),
        CountAndThinkQuestion(
            id = 4,
            type = CountThinkQuestionType.LARGEST_GROUP,
            promptAr = "أي مجموعة فيها عدد أكبر من الفراشات؟",
            promptEn = "Which group has the MOST butterflies?",
            groups = listOf(
                CountGroupOption("🦋", 7, "٧ فراشات", "7 Butterflies", Color(0xFFF3E5F5)),
                CountGroupOption("🦋", 3, "٣ فراشات", "3 Butterflies", Color(0xFFEDE7F6)),
                CountGroupOption("🦋", 5, "٥ فراشات", "5 Butterflies", Color(0xFFFCE4EC))
            ),
            correctGroupIndex = 0
        ),
        CountAndThinkQuestion(
            id = 5,
            type = CountThinkQuestionType.EXACT_COUNT,
            targetCount = 6,
            promptAr = "ابحثي عن المجموعة التي تحتوي على ٦ كرات! (الرقم ٦)",
            promptEn = "Find the group that has 6 soccer balls! (Number 6)",
            groups = listOf(
                CountGroupOption("⚽", 3, "٣ كرات", "3 Balls", Color(0xFFE3F2FD)),
                CountGroupOption("⚽", 5, "٥ كرات", "5 Balls", Color(0xFFE8F5E9)),
                CountGroupOption("⚽", 6, "٦ كرات", "6 Balls", Color(0xFFFFF3E0))
            ),
            correctGroupIndex = 2
        )
    )
}

// =====================================================================
// STAGE 6: WRITING & DRAWING WORLD | عالم الكتابة والرسم التفاعلي
// =====================================================================

enum class WritingDrawingActivityId(
    val key: String,
    val titleAr: String,
    val titleEn: String,
    val subtitleAr: String,
    val subtitleEn: String,
    val badgeEmoji: String,
    val accentColor: Color,
    val shadowColor: Color,
    val cardBgColor: Color
) {
    WRITE_ARABIC_LETTERS(
        key = "write_arabic_letters",
        titleAr = "كتابة الحروف العربية",
        titleEn = "Write Arabic Letters",
        subtitleAr = "تدرّب على كتابة ٢٨ حرفاً مع أشكال الحرف في الكلمة",
        subtitleEn = "Trace all 28 Arabic letters with step guides & forms",
        badgeEmoji = "✍️",
        accentColor = Color(0xFF00897B),
        shadowColor = Color(0xFF004D40),
        cardBgColor = Color(0xFFE0F2F1)
    ),
    WRITE_ENGLISH_LETTERS(
        key = "write_english_letters",
        titleAr = "كتابة الحروف الإنجليزية",
        titleEn = "Write English Letters",
        subtitleAr = "تعلّم كتابة الحروف الكبيرة والصغيرة A–Z خطوة بخطوة",
        subtitleEn = "Practice Uppercase & Lowercase A–Z letter writing",
        badgeEmoji = "🔤",
        accentColor = Color(0xFF8E24AA),
        shadowColor = Color(0xFF4A148C),
        cardBgColor = Color(0xFFF3E5F5)
    ),
    WRITE_NUMBERS(
        key = "write_numbers",
        titleAr = "كتابة الأرقام ١–١٠",
        titleEn = "Write Numbers 1–10",
        subtitleAr = "تدريب كتابة الأرقام العربية (١–١٠) والإنجليزية (1–10)",
        subtitleEn = "Trace Arabic (١–١٠) & English (1–10) numbers",
        badgeEmoji = "🔢",
        accentColor = Color(0xFF1E88E5),
        shadowColor = Color(0xFF0D47A1),
        cardBgColor = Color(0xFFE3F2FD)
    ),
    FREE_DRAWING_STUDIO(
        key = "free_drawing_studio",
        titleAr = "مساحة الرسم الحر",
        titleEn = "Free Drawing Studio",
        subtitleAr = "لوحة رسم بالألوان، أحجام القلم، الممحاة، التراجع والحفظ",
        subtitleEn = "Canvas with colors, brush sizes, eraser, undo & save",
        badgeEmoji = "🎨",
        accentColor = Color(0xFFE91E63),
        shadowColor = Color(0xFF880E4F),
        cardBgColor = Color(0xFFFCE4EC)
    ),
    INTERACTIVE_COLORING_BOOK(
        key = "interactive_coloring_book",
        titleAr = "التلوين التفاعلي",
        titleEn = "Interactive Coloring",
        subtitleAr = "لوّن الحيوانات، الفواكه، السيارات، الزهور، المنازل ولولو",
        subtitleEn = "Color animals, fruits, cars, flowers, houses & Lulu",
        badgeEmoji = "🖍️",
        accentColor = Color(0xFFFB8C00),
        shadowColor = Color(0xFFE65100),
        cardBgColor = Color(0xFFFFF3E0)
    ),
    DRAW_AND_LEARN_CHALLENGE(
        key = "draw_and_learn_challenge",
        titleAr = "تحدي ارسم وتعلّم",
        titleEn = "Draw & Learn Challenge",
        subtitleAr = "ارسم دائرة حمراء، مربعاً أزرق، شجرة، تفاحة وشمساً مع لولو",
        subtitleEn = "Fun guided drawing prompts with Lulu's encouragement",
        badgeEmoji = "🌟",
        accentColor = Color(0xFF43A047),
        shadowColor = Color(0xFF1B5E20),
        cardBgColor = Color(0xFFE8F5E9)
    );

    fun title(lang: AppLanguage): String = if (lang == AppLanguage.AR) titleAr else titleEn
    fun subtitle(lang: AppLanguage): String = if (lang == AppLanguage.AR) subtitleAr else subtitleEn
}

data class ArabicLetterPositionForms(
    val isolated: String,
    val initial: String,
    val medial: String,
    val finalForm: String
)

enum class ColoringTemplateTheme(
    val id: String,
    val titleAr: String,
    val titleEn: String,
    val categoryAr: String,
    val categoryEn: String,
    val emoji: String,
    val defaultRegionColors: List<Color>,
    val regionNamesAr: List<String>,
    val regionNamesEn: List<String>
) {
    CUTE_LION(
        id = "lion",
        titleAr = "الأسد اللطيف",
        titleEn = "Cute Lion",
        categoryAr = "الحيوانات",
        categoryEn = "Animals",
        emoji = "🦁",
        defaultRegionColors = listOf(Color(0xFFEF6C00), Color(0xFFFFCA28), Color(0xFFFFB74D), Color(0xFF66BB6A)),
        regionNamesAr = listOf("شعر الأسد", "وجه الأسد", "جسم الأسد", "أرض الغابة"),
        regionNamesEn = listOf("Lion Mane", "Lion Face", "Lion Body", "Jungle Grass")
    ),
    FRUIT_BASKET(
        id = "fruits",
        titleAr = "سلة الفواكه",
        titleEn = "Fruit Basket",
        categoryAr = "الفواكه",
        categoryEn = "Fruits",
        emoji = "🍎",
        defaultRegionColors = listOf(Color(0xFFE53935), Color(0xFFFDD835), Color(0xFFFB8C00), Color(0xFF8D6E63)),
        regionNamesAr = listOf("التفاحة", "الموزة", "البرتقالة", "السلة"),
        regionNamesEn = listOf("Apple", "Banana", "Orange", "Basket")
    ),
    HAPPY_CAR(
        id = "car",
        titleAr = "السيارة المرحة",
        titleEn = "Happy Car",
        categoryAr = "السيارات",
        categoryEn = "Cars",
        emoji = "🚗",
        defaultRegionColors = listOf(Color(0xFF1E88E5), Color(0xFF81D4FA), Color(0xFF37474F), Color(0xFFFDD835)),
        regionNamesAr = listOf("جسم السيارة", "النافذة", "العجلات", "المصابيح"),
        regionNamesEn = listOf("Car Body", "Window", "Wheels", "Headlights")
    ),
    LOVELY_FLOWER(
        id = "flower",
        titleAr = "الزهرة الجميلة",
        titleEn = "Lovely Flower",
        categoryAr = "الزهور",
        categoryEn = "Flowers",
        emoji = "🌸",
        defaultRegionColors = listOf(Color(0xFFEC407A), Color(0xFFFDD835), Color(0xFF43A047), Color(0xFF29B6F6)),
        regionNamesAr = listOf("البتلات", "قلب الزهرة", "الساق والأوراق", "الأصيص"),
        regionNamesEn = listOf("Petals", "Flower Center", "Stem & Leaves", "Flower Pot")
    ),
    SUNNY_HOUSE(
        id = "house",
        titleAr = "المنزل السعيد",
        titleEn = "Happy House",
        categoryAr = "المنازل",
        categoryEn = "Houses",
        emoji = "🏡",
        defaultRegionColors = listOf(Color(0xFFE53935), Color(0xFFFFF176), Color(0xFF8D6E63), Color(0xFF29B6F6)),
        regionNamesAr = listOf("السقف", "الجدران", "الباب", "النافذة"),
        regionNamesEn = listOf("Roof", "Walls", "Door", "Window")
    ),
    LULU_CHARACTER(
        id = "lulu",
        titleAr = "لولو المرحة",
        titleEn = "Cheerful Lulu",
        categoryAr = "الشخصيات الكرتونية",
        categoryEn = "Cartoon Characters",
        emoji = "👧",
        defaultRegionColors = listOf(Color(0xFFEC407A), Color(0xFFFFCCBC), Color(0xFF5D4037), Color(0xFFFDD835)),
        regionNamesAr = listOf("فستان لولو", "وجه لولو", "شعر لولو", "تاج النجوم"),
        regionNamesEn = listOf("Lulu's Dress", "Lulu's Face", "Lulu's Hair", "Star Crown")
    );

    fun title(lang: AppLanguage): String = if (lang == AppLanguage.AR) titleAr else titleEn
    fun category(lang: AppLanguage): String = if (lang == AppLanguage.AR) categoryAr else categoryEn
}

data class DrawAndLearnChallengeItem(
    val id: Int,
    val titleAr: String,
    val titleEn: String,
    val instructionAr: String,
    val instructionEn: String,
    val targetColorNameAr: String,
    val targetColorNameEn: String,
    val targetColor: Color,
    val guideShapeType: String, // "circle", "square", "tree", "apple", "sun"
    val emoji: String
) {
    fun title(lang: AppLanguage): String = if (lang == AppLanguage.AR) titleAr else titleEn
    fun instruction(lang: AppLanguage): String = if (lang == AppLanguage.AR) instructionAr else instructionEn
}

object WritingDrawingCurriculum {
    val drawingPaletteColors: List<Pair<String, Color>> = listOf(
        "أحمر / Red" to Color(0xFFE53935),
        "أزرق / Blue" to Color(0xFF1E88E5),
        "أصفر / Yellow" to Color(0xFFFDD835),
        "أخضر / Green" to Color(0xFF43A047),
        "برتقالي / Orange" to Color(0xFFFB8C00),
        "بنفسجي / Purple" to Color(0xFF8E24AA),
        "وردي / Pink" to Color(0xFFEC407A),
        "سماوي / Cyan" to Color(0xFF00ACC1),
        "بني / Brown" to Color(0xFF6D4C41),
        "أسود / Black" to Color(0xFF263238)
    )

    fun getArabicLetterForms(isolated: String): ArabicLetterPositionForms {
        // Non-connecting right-only Arabic letters: أ د ذ ر ز و
        val rightOnly = setOf("أ", "د", "ذ", "ر", "ز", "و")
        return if (isolated in rightOnly) {
            ArabicLetterPositionForms(
                isolated = isolated,
                initial = isolated,
                medial = "ـ$isolated",
                finalForm = "ـ$isolated"
            )
        } else {
            val base = if (isolated == "هـ") "ه" else isolated
            ArabicLetterPositionForms(
                isolated = isolated,
                initial = "${base}ـ",
                medial = "ـ${base}ـ",
                finalForm = "ـ$base"
            )
        }
    }

    val drawAndLearnChallenges: List<DrawAndLearnChallengeItem> = listOf(
        DrawAndLearnChallengeItem(
            id = 1,
            titleAr = "ارسم دائرة حمراء",
            titleEn = "Draw a Red Circle",
            instructionAr = "اختاري اللون الأحمر ثم ارسمي دائرة جميلة حول الخط المتقطع!",
            instructionEn = "Pick the Red color and draw a circle along the dotted guide!",
            targetColorNameAr = "أحمر",
            targetColorNameEn = "Red",
            targetColor = Color(0xFFE53935),
            guideShapeType = "circle",
            emoji = "🔴"
        ),
        DrawAndLearnChallengeItem(
            id = 2,
            titleAr = "ارسم مربعاً أزرق",
            titleEn = "Draw a Blue Square",
            instructionAr = "اختاري اللون الأزرق ثم ارسمي مربعاً جميلاً له أربعة أضلاع!",
            instructionEn = "Pick the Blue color and draw a four-sided square!",
            targetColorNameAr = "أزرق",
            targetColorNameEn = "Blue",
            targetColor = Color(0xFF1E88E5),
            guideShapeType = "square",
            emoji = "🟦"
        ),
        DrawAndLearnChallengeItem(
            id = 3,
            titleAr = "ارسم شجرة خضراء",
            titleEn = "Draw a Green Tree",
            instructionAr = "اختاري اللون الأخضر وارسمي شجرة جميلة في الحديقة!",
            instructionEn = "Pick the Green color and draw a lovely tree!",
            targetColorNameAr = "أخضر",
            targetColorNameEn = "Green",
            targetColor = Color(0xFF43A047),
            guideShapeType = "tree",
            emoji = "🌳"
        ),
        DrawAndLearnChallengeItem(
            id = 4,
            titleAr = "لوّن التفاحة باللون الأحمر",
            titleEn = "Color the Apple Red",
            instructionAr = "اختاري اللون الأحمر ولوّني التفاحة اللذيذة!",
            instructionEn = "Pick the Red color and color the yummy apple!",
            targetColorNameAr = "أحمر",
            targetColorNameEn = "Red",
            targetColor = Color(0xFFE53935),
            guideShapeType = "apple",
            emoji = "🍎"
        ),
        DrawAndLearnChallengeItem(
            id = 5,
            titleAr = "ارسم شمساً صفراء",
            titleEn = "Draw a Yellow Sun",
            instructionAr = "اختاري اللون الأصفر وارسمي شمساً مشرقة في السماء!",
            instructionEn = "Pick the Yellow color and draw a bright shining sun!",
            targetColorNameAr = "أصفر",
            targetColorNameEn = "Yellow",
            targetColor = Color(0xFFFDD835),
            guideShapeType = "sun",
            emoji = "☀️"
        )
    )
}
