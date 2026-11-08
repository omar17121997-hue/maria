package com.example.model

import androidx.compose.ui.graphics.Color

// =====================================================================
// WORLD 1: COLORS | عالم الألوان
// =====================================================================

enum class ColorsActivityId(
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
    MEET_COLORS(
        key = "meet_colors",
        titleAr = "تعرّف على الألوان",
        titleEn = "Meet the Colors",
        subtitleAr = "١٠ ألوان مبهجة مع الأشياء الملونة والنطق",
        subtitleEn = "10 cheerful colors with objects & voice",
        badgeEmoji = "🌈",
        accentColor = Color(0xFFE53935),
        shadowColor = Color(0xFFB71C1C),
        cardBgColor = Color(0xFFFFEBEE)
    ),
    FIND_COLOR(
        key = "find_color",
        titleAr = "أين هو اللون؟",
        titleEn = "Find the Color",
        subtitleAr = "استمع إلى لولو واختر اللون الصحيح",
        subtitleEn = "Listen to Lulu and tap the right color",
        badgeEmoji = "🔍",
        accentColor = Color(0xFF1E88E5),
        shadowColor = Color(0xFF0D47A1),
        cardBgColor = Color(0xFFE3F2FD)
    ),
    MATCH_COLOR(
        key = "match_color",
        titleAr = "طابق الألوان",
        titleEn = "Color Matching",
        subtitleAr = "صل كل شكل بلونه المناسب",
        subtitleEn = "Match objects with their colors",
        badgeEmoji = "🧩",
        accentColor = Color(0xFF43A047),
        shadowColor = Color(0xFF1B5E20),
        cardBgColor = Color(0xFFE8F5E9)
    ),
    COLORING(
        key = "coloring_studio",
        titleAr = "مرسم التلوين المرح",
        titleEn = "Fun Coloring Studio",
        subtitleAr = "لوّن الرسومات الجميلة بإصبعك وبالألوان",
        subtitleEn = "Color cute drawings with touch palette",
        badgeEmoji = "🎨",
        accentColor = Color(0xFF8E24AA),
        shadowColor = Color(0xFF4A148C),
        cardBgColor = Color(0xFFF3E5F5)
    );

    fun title(lang: AppLanguage): String = if (lang == AppLanguage.AR) titleAr else titleEn
    fun subtitle(lang: AppLanguage): String = if (lang == AppLanguage.AR) subtitleAr else subtitleEn
}

data class ColorObjectExample(
    val emoji: String,
    val nameAr: String,
    val nameEn: String
) {
    fun name(lang: AppLanguage): String = if (lang == AppLanguage.AR) nameAr else nameEn
}

data class ColorLessonItem(
    val id: String,
    val index: Int,
    val nameAr: String,
    val nameEn: String,
    val colorValue: Color,
    val shadowColor: Color,
    val cardBgColor: Color,
    val isLightColor: Boolean = false,
    val mainEmoji: String,
    val matchingObjects: List<ColorObjectExample>,
    val luluIntroAr: String,
    val luluIntroEn: String,
    val findQuestionAr: String,
    val findQuestionEn: String
) {
    fun name(lang: AppLanguage): String = if (lang == AppLanguage.AR) nameAr else nameEn
    fun secondaryName(lang: AppLanguage): String = if (lang == AppLanguage.AR) nameEn else nameAr
    fun luluIntro(lang: AppLanguage): String = if (lang == AppLanguage.AR) luluIntroAr else luluIntroEn
    fun findQuestion(lang: AppLanguage): String = if (lang == AppLanguage.AR) findQuestionAr else findQuestionEn
}

object ColorsCurriculum {
    val allColors: List<ColorLessonItem> = listOf(
        ColorLessonItem(
            id = "red",
            index = 1,
            nameAr = "أحمر",
            nameEn = "Red",
            colorValue = Color(0xFFE53935),
            shadowColor = Color(0xFFB71C1C),
            cardBgColor = Color(0xFFFFEBEE),
            mainEmoji = "🍎",
            matchingObjects = listOf(
                ColorObjectExample("🍎", "تفاحة حمراء", "Red Apple"),
                ColorObjectExample("🍓", "فراولة", "Strawberry"),
                ColorObjectExample("🚒", "سيارة إطفاء", "Fire Truck")
            ),
            luluIntroAr = "أحسنتِ! هذا اللون الأحمر! التفاحة والفراولة لونهما أحمر جميل.",
            luluIntroEn = "Great job! This is the color Red! Apples and strawberries are red.",
            findQuestionAr = "أين اللون الأحمر؟",
            findQuestionEn = "Where is the color Red?"
        ),
        ColorLessonItem(
            id = "blue",
            index = 2,
            nameAr = "أزرق",
            nameEn = "Blue",
            colorValue = Color(0xFF1E88E5),
            shadowColor = Color(0xFF0D47A1),
            cardBgColor = Color(0xFFE3F2FD),
            mainEmoji = "🐋",
            matchingObjects = listOf(
                ColorObjectExample("🐋", "حوت أزرق", "Blue Whale"),
                ColorObjectExample("🦋", "فراشة زرقاء", "Blue Butterfly"),
                ColorObjectExample("💧", "قطرة ماء", "Water Drop")
            ),
            luluIntroAr = "أحسنتِ! هذا اللون الأزرق! السماء الصافية والبحر لونهما أزرق.",
            luluIntroEn = "Great job! This is the color Blue! The sky and ocean are blue.",
            findQuestionAr = "أين اللون الأزرق؟",
            findQuestionEn = "Where is the color Blue?"
        ),
        ColorLessonItem(
            id = "yellow",
            index = 3,
            nameAr = "أصفر",
            nameEn = "Yellow",
            colorValue = Color(0xFFFDD835),
            shadowColor = Color(0xFFF57F17),
            cardBgColor = Color(0xFFFFFDE7),
            isLightColor = true,
            mainEmoji = "☀️",
            matchingObjects = listOf(
                ColorObjectExample("☀️", "شمس مشرقة", "Bright Sun"),
                ColorObjectExample("🍌", "موزة لذيذة", "Yummy Banana"),
                ColorObjectExample("🍋", "ليمونة", "Lemon")
            ),
            luluIntroAr = "ما أجمله! هذا اللون الأصفر! الشمس المشرقة والموز لونهما أصفر.",
            luluIntroEn = "How lovely! This is the color Yellow! The bright sun and bananas are yellow.",
            findQuestionAr = "أين اللون الأصفر؟",
            findQuestionEn = "Where is the color Yellow?"
        ),
        ColorLessonItem(
            id = "green",
            index = 4,
            nameAr = "أخضر",
            nameEn = "Green",
            colorValue = Color(0xFF43A047),
            shadowColor = Color(0xFF1B5E20),
            cardBgColor = Color(0xFFE8F5E9),
            mainEmoji = "🐸",
            matchingObjects = listOf(
                ColorObjectExample("🐸", "ضفدع أخضر", "Green Frog"),
                ColorObjectExample("🌳", "شجرة خضراء", "Green Tree"),
                ColorObjectExample("🍃", "ورقة شجر", "Green Leaf")
            ),
            luluIntroAr = "رائع! هذا اللون الأخضر! الأشجار والضفدع الصغير لونهما أخضر.",
            luluIntroEn = "Awesome! This is the color Green! Trees and little frogs are green.",
            findQuestionAr = "أين اللون الأخضر؟",
            findQuestionEn = "Where is the color Green?"
        ),
        ColorLessonItem(
            id = "orange",
            index = 5,
            nameAr = "برتقالي",
            nameEn = "Orange",
            colorValue = Color(0xFFFB8C00),
            shadowColor = Color(0xFFE65100),
            cardBgColor = Color(0xFFFFF3E0),
            mainEmoji = "🍊",
            matchingObjects = listOf(
                ColorObjectExample("🍊", "برتقالة", "Orange Fruit"),
                ColorObjectExample("🥕", "جزرة", "Carrot"),
                ColorObjectExample("🦊", "ثعلب صغير", "Little Fox")
            ),
            luluIntroAr = "أحسنتِ! هذا اللون البرتقالي! البرتقال والجزر لونهما برتقالي.",
            luluIntroEn = "Great job! This is the color Orange! Oranges and carrots are orange.",
            findQuestionAr = "أين اللون البرتقالي؟",
            findQuestionEn = "Where is the color Orange?"
        ),
        ColorLessonItem(
            id = "purple",
            index = 6,
            nameAr = "بنفسجي",
            nameEn = "Purple",
            colorValue = Color(0xFF8E24AA),
            shadowColor = Color(0xFF4A148C),
            cardBgColor = Color(0xFFF3E5F5),
            mainEmoji = "🍇",
            matchingObjects = listOf(
                ColorObjectExample("🍇", "عنب بنفسجي", "Purple Grapes"),
                ColorObjectExample("🍆", "باذنجان", "Eggplant"),
                ColorObjectExample("🔮", "بلورة سحرية", "Magic Crystal")
            ),
            luluIntroAr = "رائع! هذا اللون البنفسجي! العنب اللذيذ لونه بنفسجي.",
            luluIntroEn = "Wonderful! This is the color Purple! Sweet grapes are purple.",
            findQuestionAr = "أين اللون البنفسجي؟",
            findQuestionEn = "Where is the color Purple?"
        ),
        ColorLessonItem(
            id = "pink",
            index = 7,
            nameAr = "وردي",
            nameEn = "Pink",
            colorValue = Color(0xFFEC407A),
            shadowColor = Color(0xFFAD1457),
            cardBgColor = Color(0xFFFCE4EC),
            mainEmoji = "🌸",
            matchingObjects = listOf(
                ColorObjectExample("🌸", "زهرة وردية", "Pink Flower"),
                ColorObjectExample("🦩", "طائر الفلامنغو", "Flamingo"),
                ColorObjectExample("🧁", "كعكة حلوة", "Pink Cupcake")
            ),
            luluIntroAr = "ما أجمله! هذا اللون الوردي! الزهور الجميلة وطائر الفلامنغو لونهما وردي.",
            luluIntroEn = "So pretty! This is the color Pink! Blossoms and flamingos are pink.",
            findQuestionAr = "أين اللون الوردي؟",
            findQuestionEn = "Where is the color Pink?"
        ),
        ColorLessonItem(
            id = "black",
            index = 8,
            nameAr = "أسود",
            nameEn = "Black",
            colorValue = Color(0xFF263238),
            shadowColor = Color(0xFF000000),
            cardBgColor = Color(0xFFECEFF1),
            mainEmoji = "🐧",
            matchingObjects = listOf(
                ColorObjectExample("🐧", "بطريق لطيف", "Cute Penguin"),
                ColorObjectExample("🐈‍⬛", "قطة سوداء", "Black Cat"),
                ColorObjectExample("🎩", "قبعة أنيقة", "Top Hat")
            ),
            luluIntroAr = "أحسنتِ! هذا اللون الأسود! سماء الليل والبطريق فيهما اللون الأسود.",
            luluIntroEn = "Well done! This is the color Black! Night skies and penguins have black.",
            findQuestionAr = "أين اللون الأسود؟",
            findQuestionEn = "Where is the color Black?"
        ),
        ColorLessonItem(
            id = "white",
            index = 9,
            nameAr = "أبيض",
            nameEn = "White",
            colorValue = Color(0xFFFAFAFA),
            shadowColor = Color(0xFF90A4AE),
            cardBgColor = Color(0xFFF5F7FA),
            isLightColor = true,
            mainEmoji = "☁️",
            matchingObjects = listOf(
                ColorObjectExample("☁️", "غيمة بيضاء", "White Cloud"),
                ColorObjectExample("🐑", "خروف أبيض", "White Sheep"),
                ColorObjectExample("⛄", "رجل الثلج", "Snowman")
            ),
            luluIntroAr = "رائع! هذا اللون الأبيض! الغيوم في السماء والثلج لونهما أبيض ناصع.",
            luluIntroEn = "Awesome! This is the color White! Fluffy clouds and snowmen are white.",
            findQuestionAr = "أين اللون الأبيض؟",
            findQuestionEn = "Where is the color White?"
        ),
        ColorLessonItem(
            id = "brown",
            index = 10,
            nameAr = "بني",
            nameEn = "Brown",
            colorValue = Color(0xFF6D4C41),
            shadowColor = Color(0xFF3E2723),
            cardBgColor = Color(0xFFEFEBE9),
            mainEmoji = "🐻",
            matchingObjects = listOf(
                ColorObjectExample("🐻", "دب بني", "Brown Bear"),
                ColorObjectExample("🐴", "حصان", "Brown Horse"),
                ColorObjectExample("🍫", "شوكولاتة", "Chocolate")
            ),
            luluIntroAr = "أحسنتِ! هذا اللون البني! الدب اللطيف والشوكولاتة لونهما بني.",
            luluIntroEn = "Great job! This is the color Brown! Teddy bears and chocolate are brown.",
            findQuestionAr = "أين اللون البني؟",
            findQuestionEn = "Where is the color Brown?"
        )
    )
}

// =====================================================================
// WORLD 2: SHAPES | عالم الأشكال
// =====================================================================

enum class ShapesActivityId(
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
    LEARN_SHAPES(
        key = "learn_shapes",
        titleAr = "تعرّف على الأشكال",
        titleEn = "Learn Shapes",
        subtitleAr = "٨ أشكال هندسية ثلاثية الأبعاد مع النطق",
        subtitleEn = "8 colorful 3D shapes with voice",
        badgeEmoji = "⭐",
        accentColor = Color(0xFF2E7D32),
        shadowColor = Color(0xFF1B5E20),
        cardBgColor = Color(0xFFE8F5E9)
    ),
    IDENTIFY_SHAPE(
        key = "identify_shape",
        titleAr = "أين هو الشكل؟",
        titleEn = "Identify the Shape",
        subtitleAr = "استمع إلى لولو واختر الشكل الصحيح",
        subtitleEn = "Listen to Lulu and choose the shape",
        badgeEmoji = "🔍",
        accentColor = Color(0xFF00838F),
        shadowColor = Color(0xFF005662),
        cardBgColor = Color(0xFFE0F7FA)
    ),
    MATCH_SHAPES(
        key = "match_shapes",
        titleAr = "طابق الأشكال المتماثلة",
        titleEn = "Match Shapes",
        subtitleAr = "صل كل شكل بالشكل المطابق له",
        subtitleEn = "Match identical shapes together",
        badgeEmoji = "🧩",
        accentColor = Color(0xFF6A1B9A),
        shadowColor = Color(0xFF38006B),
        cardBgColor = Color(0xFFF3E5F5)
    ),
    SHAPES_AROUND_US(
        key = "shapes_around_us",
        titleAr = "الأشكال من حولنا",
        titleEn = "Shapes Around Us",
        subtitleAr = "اكتشف أشكال الأشياء في حياتنا اليومية",
        subtitleEn = "Identify shapes of everyday objects",
        badgeEmoji = "🍕",
        accentColor = Color(0xFFE65100),
        shadowColor = Color(0xFFBF360C),
        cardBgColor = Color(0xFFFFF3E0)
    );

    fun title(lang: AppLanguage): String = if (lang == AppLanguage.AR) titleAr else titleEn
    fun subtitle(lang: AppLanguage): String = if (lang == AppLanguage.AR) subtitleAr else subtitleEn
}

enum class ShapeType {
    CIRCLE,
    SQUARE,
    TRIANGLE,
    RECTANGLE,
    STAR,
    HEART,
    OVAL,
    DIAMOND
}

data class ShapeLessonItem(
    val id: String,
    val type: ShapeType,
    val index: Int,
    val nameAr: String,
    val nameEn: String,
    val color: Color,
    val shadowColor: Color,
    val cardBgColor: Color,
    val descriptionAr: String,
    val descriptionEn: String,
    val realWorldExampleEmoji: String,
    val realWorldExampleAr: String,
    val realWorldExampleEn: String,
    val luluIntroAr: String,
    val luluIntroEn: String,
    val questionAr: String,
    val questionEn: String
) {
    fun name(lang: AppLanguage): String = if (lang == AppLanguage.AR) nameAr else nameEn
    fun secondaryName(lang: AppLanguage): String = if (lang == AppLanguage.AR) nameEn else nameAr
    fun description(lang: AppLanguage): String = if (lang == AppLanguage.AR) descriptionAr else descriptionEn
    fun realWorldExample(lang: AppLanguage): String =
        if (lang == AppLanguage.AR) "$realWorldExampleEmoji $realWorldExampleAr" else "$realWorldExampleEmoji $realWorldExampleEn"
    fun luluIntro(lang: AppLanguage): String = if (lang == AppLanguage.AR) luluIntroAr else luluIntroEn
    fun question(lang: AppLanguage): String = if (lang == AppLanguage.AR) questionAr else questionEn
}

data class EverydayObjectShapeItem(
    val id: Int,
    val objectEmoji: String,
    val objectNameAr: String,
    val objectNameEn: String,
    val promptAr: String,
    val promptEn: String,
    val targetShape: ShapeType,
    val options: List<ShapeType>
)

object ShapesCurriculum {
    val allShapes: List<ShapeLessonItem> = listOf(
        ShapeLessonItem(
            id = "circle",
            type = ShapeType.CIRCLE,
            index = 1,
            nameAr = "دائرة",
            nameEn = "Circle",
            color = Color(0xFFE53935),
            shadowColor = Color(0xFFB71C1C),
            cardBgColor = Color(0xFFFFEBEE),
            descriptionAr = "مستديرة تماماً وليس لها زوايا!",
            descriptionEn = "Round and smooth with zero corners!",
            realWorldExampleEmoji = "⚽",
            realWorldExampleAr = "الكرة مستديرة مثل الدائرة",
            realWorldExampleEn = "A Ball is round like a Circle",
            luluIntroAr = "هذه دائرة! الدائرة مستديرة مثل الكرة والشمس.",
            luluIntroEn = "This is a Circle! A circle is round like a ball and the sun.",
            questionAr = "أين الدائرة؟",
            questionEn = "Where is the Circle?"
        ),
        ShapeLessonItem(
            id = "square",
            type = ShapeType.SQUARE,
            index = 2,
            nameAr = "مربع",
            nameEn = "Square",
            color = Color(0xFF1E88E5),
            shadowColor = Color(0xFF0D47A1),
            cardBgColor = Color(0xFFE3F2FD),
            descriptionAr = "له أربعة أضلاع متساوية وأربع زوايا!",
            descriptionEn = "Has 4 equal sides and 4 corners!",
            realWorldExampleEmoji = "🪟",
            realWorldExampleAr = "النافذة على شكل مربع",
            realWorldExampleEn = "A Window is shaped like a Square",
            luluIntroAr = "هذا مربع! للمربع أربعة أضلاع متساوية مثل النافذة.",
            luluIntroEn = "This is a Square! A square has four equal sides like a window.",
            questionAr = "أين المربع؟",
            questionEn = "Where is the Square?"
        ),
        ShapeLessonItem(
            id = "triangle",
            type = ShapeType.TRIANGLE,
            index = 3,
            nameAr = "مثلث",
            nameEn = "Triangle",
            color = Color(0xFF43A047),
            shadowColor = Color(0xFF1B5E20),
            cardBgColor = Color(0xFFE8F5E9),
            descriptionAr = "له ثلاثة أضلاع وثلاث زوايا!",
            descriptionEn = "Has 3 sides and 3 pointy corners!",
            realWorldExampleEmoji = "🍕",
            realWorldExampleAr = "قطعة البيتزا على شكل مثلث",
            realWorldExampleEn = "A Pizza slice is a Triangle",
            luluIntroAr = "هذا مثلث! للمثلث ثلاثة أضلاع مثل قطعة البيتزا.",
            luluIntroEn = "This is a Triangle! A triangle has three sides like a slice of pizza.",
            questionAr = "أين المثلث؟",
            questionEn = "Where is the Triangle?"
        ),
        ShapeLessonItem(
            id = "rectangle",
            type = ShapeType.RECTANGLE,
            index = 4,
            nameAr = "مستطيل",
            nameEn = "Rectangle",
            color = Color(0xFFFB8C00),
            shadowColor = Color(0xFFE65100),
            cardBgColor = Color(0xFFFFF3E0),
            descriptionAr = "له أربعة أضلاع: ضلعان طويلان وضلعان قصيران!",
            descriptionEn = "Has 2 long sides and 2 short sides!",
            realWorldExampleEmoji = "🚪",
            realWorldExampleAr = "الباب والكتاب على شكل مستطيل",
            realWorldExampleEn = "A Door is shaped like a Rectangle",
            luluIntroAr = "هذا مستطيل! الباب وشاشة التلفاز على شكل مستطيل.",
            luluIntroEn = "This is a Rectangle! A door and a book are rectangles.",
            questionAr = "أين المستطيل؟",
            questionEn = "Where is the Rectangle?"
        ),
        ShapeLessonItem(
            id = "star",
            type = ShapeType.STAR,
            index = 5,
            nameAr = "نجمة",
            nameEn = "Star",
            color = Color(0xFFFFB300),
            shadowColor = Color(0xFFF57F17),
            cardBgColor = Color(0xFFFFF8E1),
            descriptionAr = "تتلألأ في السماء ولها خمسة رؤوس لامعة!",
            descriptionEn = "Shines in the sky with 5 bright points!",
            realWorldExampleEmoji = "🌟",
            realWorldExampleAr = "نجمة السماء المضيئة",
            realWorldExampleEn = "A twinkling Star in the night sky",
            luluIntroAr = "ما أجمل هذه النجمة! النجمة تلمع في السماء ليلاً.",
            luluIntroEn = "What a beautiful Star! Stars twinkle brightly in the sky.",
            questionAr = "أين النجمة؟",
            questionEn = "Where is the Star?"
        ),
        ShapeLessonItem(
            id = "heart",
            type = ShapeType.HEART,
            index = 6,
            nameAr = "قلب",
            nameEn = "Heart",
            color = Color(0xFFEC407A),
            shadowColor = Color(0xFFAD1457),
            cardBgColor = Color(0xFFFCE4EC),
            descriptionAr = "شكل المحبة والحنان اللطيف!",
            descriptionEn = "The sweet shape of love and kindness!",
            realWorldExampleEmoji = "🍓",
            realWorldExampleAr = "حبة الفراولة تشبه القلب",
            realWorldExampleEn = "A sweet Strawberry looks like a Heart",
            luluIntroAr = "هذا قلب جميل! القلب يرمز للمحبة والسعادة.",
            luluIntroEn = "This is a lovely Heart! Hearts remind us of love and joy.",
            questionAr = "أين القلب؟",
            questionEn = "Where is the Heart?"
        ),
        ShapeLessonItem(
            id = "oval",
            type = ShapeType.OVAL,
            index = 7,
            nameAr = "بيضاوي",
            nameEn = "Oval",
            color = Color(0xFF8E24AA),
            shadowColor = Color(0xFF4A148C),
            cardBgColor = Color(0xFFF3E5F5),
            descriptionAr = "يشبه الدائرة الممدودة مثل البيضة!",
            descriptionEn = "Looks like a stretched circle, just like an egg!",
            realWorldExampleEmoji = "🥚",
            realWorldExampleAr = "البيضة والبطيخة على شكل بيضاوي",
            realWorldExampleEn = "An Egg is shaped like an Oval",
            luluIntroAr = "هذا شكل بيضاوي! إنه يشبه البيضة والبالون.",
            luluIntroEn = "This is an Oval! An oval is shaped just like an egg.",
            questionAr = "أين الشكل البيضاوي؟",
            questionEn = "Where is the Oval?"
        ),
        ShapeLessonItem(
            id = "diamond",
            type = ShapeType.DIAMOND,
            index = 8,
            nameAr = "معين",
            nameEn = "Diamond",
            color = Color(0xFF00ACC1),
            shadowColor = Color(0xFF006064),
            cardBgColor = Color(0xFFE0F7FA),
            descriptionAr = "يشبه الطائرة الورقية المتلألئة في الهواء!",
            descriptionEn = "Shaped like a high-flying kite in the sky!",
            realWorldExampleEmoji = "🪁",
            realWorldExampleAr = "الطائرة الورقية على شكل معين",
            realWorldExampleEn = "A flying Kite is a Diamond shape",
            luluIntroAr = "هذا معين! الطائرة الورقية الجميلة تكون على شكل معين.",
            luluIntroEn = "This is a Diamond! A colorful kite in the sky is a diamond.",
            questionAr = "أين المعين؟",
            questionEn = "Where is the Diamond?"
        )
    )

    fun byType(type: ShapeType): ShapeLessonItem =
        allShapes.first { it.type == type }

    val everydayQuestions: List<EverydayObjectShapeItem> = listOf(
        EverydayObjectShapeItem(
            id = 1,
            objectEmoji = "⚽",
            objectNameAr = "كرة القدم",
            objectNameEn = "Soccer Ball",
            promptAr = "ما هو شكل كرة القدم؟",
            promptEn = "What shape is the Soccer Ball?",
            targetShape = ShapeType.CIRCLE,
            options = listOf(ShapeType.CIRCLE, ShapeType.SQUARE, ShapeType.TRIANGLE)
        ),
        EverydayObjectShapeItem(
            id = 2,
            objectEmoji = "🍕",
            objectNameAr = "قطعة البيتزا",
            objectNameEn = "Pizza Slice",
            promptAr = "ما هو شكل قطعة البيتزا اللذيذة؟",
            promptEn = "What shape is a slice of Pizza?",
            targetShape = ShapeType.TRIANGLE,
            options = listOf(ShapeType.SQUARE, ShapeType.TRIANGLE, ShapeType.OVAL)
        ),
        EverydayObjectShapeItem(
            id = 3,
            objectEmoji = "🪟",
            objectNameAr = "نافذة الغرفة",
            objectNameEn = "Window",
            promptAr = "ما هو شكل هذه النافذة؟",
            promptEn = "What shape is this Window?",
            targetShape = ShapeType.SQUARE,
            options = listOf(ShapeType.CIRCLE, ShapeType.SQUARE, ShapeType.HEART)
        ),
        EverydayObjectShapeItem(
            id = 4,
            objectEmoji = "🚪",
            objectNameAr = "باب المنزل",
            objectNameEn = "House Door",
            promptAr = "ما هو شكل باب المنزل؟",
            promptEn = "What shape is the House Door?",
            targetShape = ShapeType.RECTANGLE,
            options = listOf(ShapeType.RECTANGLE, ShapeType.TRIANGLE, ShapeType.STAR)
        ),
        EverydayObjectShapeItem(
            id = 5,
            objectEmoji = "🥚",
            objectNameAr = "البيضة",
            objectNameEn = "Egg",
            promptAr = "ما هو شكل البيضة؟",
            promptEn = "What shape is an Egg?",
            targetShape = ShapeType.OVAL,
            options = listOf(ShapeType.SQUARE, ShapeType.OVAL, ShapeType.DIAMOND)
        ),
        EverydayObjectShapeItem(
            id = 6,
            objectEmoji = "🪁",
            objectNameAr = "الطائرة الورقية",
            objectNameEn = "Flying Kite",
            promptAr = "ما هو شكل الطائرة الورقية؟",
            promptEn = "What shape is the Flying Kite?",
            targetShape = ShapeType.DIAMOND,
            options = listOf(ShapeType.DIAMOND, ShapeType.CIRCLE, ShapeType.RECTANGLE)
        )
    )
}

// =====================================================================
// WORLD 3: ANIMALS | عالم الحيوانات
// =====================================================================

enum class AnimalsActivityId(
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
    MEET_ANIMALS(
        key = "meet_animals",
        titleAr = "تعرّف على الحيوانات",
        titleEn = "Meet the Animals",
        subtitleAr = "١٥ حيواناً لطيفاً مع أصواتها ومعلومات ممتعة",
        subtitleEn = "15 cute animals with sounds & fun facts",
        badgeEmoji = "🦁",
        accentColor = Color(0xFFEF6C00),
        shadowColor = Color(0xFFE65100),
        cardBgColor = Color(0xFFFFF8E1)
    ),
    GUESS_ANIMAL(
        key = "guess_animal",
        titleAr = "خمّن صوت الحيوان",
        titleEn = "Guess the Animal",
        subtitleAr = "استمع إلى الصوت واختر الحيوان الصحيح",
        subtitleEn = "Listen to the sound and pick the animal",
        badgeEmoji = "🔊",
        accentColor = Color(0xFF2E7D32),
        shadowColor = Color(0xFF1B5E20),
        cardBgColor = Color(0xFFE8F5E9)
    ),
    MATCH_ANIMAL_NAME(
        key = "match_animal_name",
        titleAr = "طابق الحيوان باسمه",
        titleEn = "Match Animal to Name",
        subtitleAr = "صل صورة الحيوان باسمه الصحيح",
        subtitleEn = "Match each animal picture to its name",
        badgeEmoji = "🏷️",
        accentColor = Color(0xFF6A1B9A),
        shadowColor = Color(0xFF38006B),
        cardBgColor = Color(0xFFF3E5F5)
    ),
    ANIMAL_HABITATS(
        key = "animal_habitats",
        titleAr = "أين يعيش الحيوان؟",
        titleEn = "Where Does It Live?",
        subtitleAr = "المزرعة • الغابة • البحر • الأدغال",
        subtitleEn = "Farm • Forest • Sea • Jungle",
        badgeEmoji = "🏡",
        accentColor = Color(0xFF0277BD),
        shadowColor = Color(0xFF01579B),
        cardBgColor = Color(0xFFE1F5FE)
    );

    fun title(lang: AppLanguage): String = if (lang == AppLanguage.AR) titleAr else titleEn
    fun subtitle(lang: AppLanguage): String = if (lang == AppLanguage.AR) subtitleAr else subtitleEn
}

enum class AnimalHabitat(
    val key: String,
    val nameAr: String,
    val nameEn: String,
    val emoji: String,
    val color: Color,
    val shadowColor: Color,
    val cardBgColor: Color
) {
    FARM(
        key = "farm",
        nameAr = "المزرعة",
        nameEn = "Farm",
        emoji = "🏡",
        color = Color(0xFF43A047),
        shadowColor = Color(0xFF1B5E20),
        cardBgColor = Color(0xFFE8F5E9)
    ),
    FOREST(
        key = "forest",
        nameAr = "الغابة",
        nameEn = "Forest",
        emoji = "🌲",
        color = Color(0xFF00897B),
        shadowColor = Color(0xFF004D40),
        cardBgColor = Color(0xFFE0F2F1)
    ),
    SEA(
        key = "sea",
        nameAr = "البحر",
        nameEn = "Sea",
        emoji = "🌊",
        color = Color(0xFF0288D1),
        shadowColor = Color(0xFF01579B),
        cardBgColor = Color(0xFFE1F5FE)
    ),
    JUNGLE(
        key = "jungle",
        nameAr = "الأدغال",
        nameEn = "Jungle",
        emoji = "🌴",
        color = Color(0xFFEF6C00),
        shadowColor = Color(0xFFE65100),
        cardBgColor = Color(0xFFFFF3E0)
    );

    fun title(lang: AppLanguage): String = if (lang == AppLanguage.AR) nameAr else nameEn
}

data class AnimalLessonItem(
    val id: String,
    val index: Int,
    val nameAr: String,
    val nameEn: String,
    val emoji: String,
    val habitat: AnimalHabitat,
    val soundNameAr: String,
    val soundNameEn: String,
    val soundOnomatopoeiaAr: String,
    val soundOnomatopoeiaEn: String,
    val factAr: String,
    val factEn: String,
    val color: Color,
    val shadowColor: Color,
    val cardBgColor: Color
) {
    fun name(lang: AppLanguage): String = if (lang == AppLanguage.AR) nameAr else nameEn
    fun secondaryName(lang: AppLanguage): String = if (lang == AppLanguage.AR) nameEn else nameAr
    fun soundLabel(lang: AppLanguage): String =
        if (lang == AppLanguage.AR) "$soundNameAr ($soundOnomatopoeiaAr)" else "$soundNameEn ($soundOnomatopoeiaEn)"
    fun fact(lang: AppLanguage): String = if (lang == AppLanguage.AR) factAr else factEn

    fun spokenMeet(lang: AppLanguage): String = if (lang == AppLanguage.AR) {
        "هذا $nameAr! صوته $soundNameAr: $soundOnomatopoeiaAr! $factAr"
    } else {
        "This is a $nameEn! It says $soundOnomatopoeiaEn! $factEn"
    }

    fun spokenGuessClue(lang: AppLanguage): String = if (lang == AppLanguage.AR) {
        "هل تستطيعين معرفة صاحب هذا الصوت؟ $soundOnomatopoeiaAr! إنه صوت $soundNameAr!"
    } else {
        "Can you guess this animal? It says $soundOnomatopoeiaEn!"
    }
}

object AnimalsCurriculum {
    val allAnimals: List<AnimalLessonItem> = listOf(
        AnimalLessonItem(
            id = "lion",
            index = 1,
            nameAr = "أسد",
            nameEn = "Lion",
            emoji = "🦁",
            habitat = AnimalHabitat.JUNGLE,
            soundNameAr = "زئير",
            soundNameEn = "Roar",
            soundOnomatopoeiaAr = "راااور",
            soundOnomatopoeiaEn = "Roaaar",
            factAr = "الأسد يُسمّى ملك الغابة وله شعر كثيف حول رأسه.",
            factEn = "The lion is called the King of the Jungle and has a big fluffy mane.",
            color = Color(0xFFEF6C00),
            shadowColor = Color(0xFFE65100),
            cardBgColor = Color(0xFFFFF3E0)
        ),
        AnimalLessonItem(
            id = "cat",
            index = 2,
            nameAr = "قطة",
            nameEn = "Cat",
            emoji = "🐱",
            habitat = AnimalHabitat.FARM,
            soundNameAr = "مواء",
            soundNameEn = "Meow",
            soundOnomatopoeiaAr = "مياو مياو",
            soundOnomatopoeiaEn = "Meow Meow",
            factAr = "القطة حيوان أليف لطيف يحب اللعب والنظافة.",
            factEn = "Cats are gentle pets that love to play and keep their fur clean.",
            color = Color(0xFFEC407A),
            shadowColor = Color(0xFFAD1457),
            cardBgColor = Color(0xFFFCE4EC)
        ),
        AnimalLessonItem(
            id = "dog",
            index = 3,
            nameAr = "كلب",
            nameEn = "Dog",
            emoji = "🐶",
            habitat = AnimalHabitat.FARM,
            soundNameAr = "نباح",
            soundNameEn = "Bark",
            soundOnomatopoeiaAr = "هاو هاو",
            soundOnomatopoeiaEn = "Woof Woof",
            factAr = "الكلب صديق وفيّ يحرس المزرعة ويلعب بالكرة.",
            factEn = "Dogs are loyal friends who guard the farm and love chasing balls.",
            color = Color(0xFF8D6E63),
            shadowColor = Color(0xFF4E342E),
            cardBgColor = Color(0xFFEFEBE9)
        ),
        AnimalLessonItem(
            id = "elephant",
            index = 4,
            nameAr = "فيل",
            nameEn = "Elephant",
            emoji = "🐘",
            habitat = AnimalHabitat.JUNGLE,
            soundNameAr = "نهيم",
            soundNameEn = "Trumpet",
            soundOnomatopoeiaAr = "تووووت",
            soundOnomatopoeiaEn = "Pawoooh",
            factAr = "الفيل أكبر حيوان بري وله خرطوم طويل يشرب به الماء.",
            factEn = "Elephants are the largest land animals and use their long trunks to drink water.",
            color = Color(0xFF5C6BC0),
            shadowColor = Color(0xFF283593),
            cardBgColor = Color(0xFFE8EAF6)
        ),
        AnimalLessonItem(
            id = "giraffe",
            index = 5,
            nameAr = "زرافة",
            nameEn = "Giraffe",
            emoji = "🦒",
            habitat = AnimalHabitat.JUNGLE,
            soundNameAr = "همهمة لطيفة",
            soundNameEn = "Gentle Hum",
            soundOnomatopoeiaAr = "هممم",
            soundOnomatopoeiaEn = "Hummm",
            factAr = "الزرافة أطول حيوان في العالم وتأكل أوراق الأشجار العالية.",
            factEn = "Giraffes are the tallest animals and eat leaves from high treetops.",
            color = Color(0xFFFFB300),
            shadowColor = Color(0xFFF57F17),
            cardBgColor = Color(0xFFFFF8E1)
        ),
        AnimalLessonItem(
            id = "monkey",
            index = 6,
            nameAr = "قرد",
            nameEn = "Monkey",
            emoji = "🐵",
            habitat = AnimalHabitat.JUNGLE,
            soundNameAr = "ضحك وصياح",
            soundNameEn = "Chatter",
            soundOnomatopoeiaAr = "أوه أوه آه آه",
            soundOnomatopoeiaEn = "Ooh Ooh Aah Aah",
            factAr = "القرد ذكي ومرح يحب تسلق الأشجار وأكل الموز.",
            factEn = "Monkeys are playful and smart! They love climbing trees and eating bananas.",
            color = Color(0xFF6D4C41),
            shadowColor = Color(0xFF3E2723),
            cardBgColor = Color(0xFFEFEBE9)
        ),
        AnimalLessonItem(
            id = "rabbit",
            index = 7,
            nameAr = "أرنب",
            nameEn = "Rabbit",
            emoji = "🐰",
            habitat = AnimalHabitat.FOREST,
            soundNameAr = "قفز وهمس",
            soundNameEn = "Squeak & Hop",
            soundOnomatopoeiaAr = "بيب بيب",
            soundOnomatopoeiaEn = "Squeak Hop",
            factAr = "الأرنب سريع القفز وله أذنان طويلتان ويحب أكل الجزر.",
            factEn = "Rabbits have long ears, hop very fast, and love crunchy carrots.",
            color = Color(0xFFAB47BC),
            shadowColor = Color(0xFF6A1B9A),
            cardBgColor = Color(0xFFF3E5F5)
        ),
        AnimalLessonItem(
            id = "horse",
            index = 8,
            nameAr = "حصان",
            nameEn = "Horse",
            emoji = "🐴",
            habitat = AnimalHabitat.FARM,
            soundNameAr = "صهيل",
            soundNameEn = "Neigh",
            soundOnomatopoeiaAr = "إيهيهيهي",
            soundOnomatopoeiaEn = "Neighhh",
            factAr = "الحصان حيوان قوي وسريع الجري يعيش في المزرعة.",
            factEn = "Horses are strong, fast runners that live on the farm.",
            color = Color(0xFFD84315),
            shadowColor = Color(0xFFBF360C),
            cardBgColor = Color(0xFFFBE9E7)
        ),
        AnimalLessonItem(
            id = "cow",
            index = 9,
            nameAr = "بقرة",
            nameEn = "Cow",
            emoji = "🐮",
            habitat = AnimalHabitat.FARM,
            soundNameAr = "خوار",
            soundNameEn = "Moo",
            soundOnomatopoeiaAr = "موووو",
            soundOnomatopoeiaEn = "Moooo",
            factAr = "البقرة تعيش في المزرعة وتعطينا الحليب المفيد اللذيذ.",
            factEn = "Cows live on the farm and give us fresh, healthy milk.",
            color = Color(0xFF43A047),
            shadowColor = Color(0xFF1B5E20),
            cardBgColor = Color(0xFFE8F5E9)
        ),
        AnimalLessonItem(
            id = "sheep",
            index = 10,
            nameAr = "خروف",
            nameEn = "Sheep",
            emoji = "🐑",
            habitat = AnimalHabitat.FARM,
            soundNameAr = "ثغاء",
            soundNameEn = "Baa",
            soundOnomatopoeiaAr = "مااااء",
            soundOnomatopoeiaEn = "Baaaa",
            factAr = "الخروف يغطي جسمه الصوف الأبيض الدافئ.",
            factEn = "Sheep have soft, fluffy wool that keeps us warm in winter.",
            color = Color(0xFF00ACC1),
            shadowColor = Color(0xFF006064),
            cardBgColor = Color(0xFFE0F7FA)
        ),
        AnimalLessonItem(
            id = "duck",
            index = 11,
            nameAr = "بطة",
            nameEn = "Duck",
            emoji = "🦆",
            habitat = AnimalHabitat.FARM,
            soundNameAr = "بطبطة",
            soundNameEn = "Quack",
            soundOnomatopoeiaAr = "كواك كواك",
            soundOnomatopoeiaEn = "Quack Quack",
            factAr = "البطة تحب السباحة في بركة الماء في المزرعة.",
            factEn = "Ducks love swimming happily in the farm pond.",
            color = Color(0xFFFBC02D),
            shadowColor = Color(0xFFF57F17),
            cardBgColor = Color(0xFFFFFDE7)
        ),
        AnimalLessonItem(
            id = "bird",
            index = 12,
            nameAr = "عصفور",
            nameEn = "Bird",
            emoji = "🐦",
            habitat = AnimalHabitat.FOREST,
            soundNameAr = "زقزقة",
            soundNameEn = "Tweet",
            soundOnomatopoeiaAr = "تويت تويت",
            soundOnomatopoeiaEn = "Tweet Tweet",
            factAr = "العصفور يطير بجناحيه في السماء ويغرد بأجمل الألحان.",
            factEn = "Birds fly high in the sky with their wings and sing sweet songs.",
            color = Color(0xFF29B6F6),
            shadowColor = Color(0xFF0277BD),
            cardBgColor = Color(0xFFE1F5FE)
        ),
        AnimalLessonItem(
            id = "fish",
            index = 13,
            nameAr = "سمكة",
            nameEn = "Fish",
            emoji = "🐟",
            habitat = AnimalHabitat.SEA,
            soundNameAr = "فقاعات ماء",
            soundNameEn = "Bubbles",
            soundOnomatopoeiaAr = "بلوب بلوب",
            soundOnomatopoeiaEn = "Blub Blub",
            factAr = "السمكة تعيش وتسبح تحت الماء في البحر باستخدام زعانفها.",
            factEn = "Fish breathe underwater and swim in the sea using their fins.",
            color = Color(0xFF0288D1),
            shadowColor = Color(0xFF01579B),
            cardBgColor = Color(0xFFE1F5FE)
        ),
        AnimalLessonItem(
            id = "turtle",
            index = 14,
            nameAr = "سلحفاة",
            nameEn = "Turtle",
            emoji = "🐢",
            habitat = AnimalHabitat.SEA,
            soundNameAr = "سباحة هادئة",
            soundNameEn = "Gentle Splash",
            soundOnomatopoeiaAr = "سبلاش",
            soundOnomatopoeiaEn = "Splash",
            factAr = "السلحفاة تحمل بيتها القوي (الصدفة) فوق ظهرها لتحتمي به.",
            factEn = "Turtles carry a strong shell on their backs like a cozy home.",
            color = Color(0xFF00897B),
            shadowColor = Color(0xFF004D40),
            cardBgColor = Color(0xFFE0F2F1)
        ),
        AnimalLessonItem(
            id = "butterfly",
            index = 15,
            nameAr = "فراشة",
            nameEn = "Butterfly",
            emoji = "🦋",
            habitat = AnimalHabitat.FOREST,
            soundNameAr = "رفيف الأجنحة",
            soundNameEn = "Flutter",
            soundOnomatopoeiaAr = "رفرف رفرف",
            soundOnomatopoeiaEn = "Flutter Flutter",
            factAr = "الفراشة لها أجنحة ملوّنة جميلة وتطير بين الأزهار.",
            factEn = "Butterflies have colorful wings and dance from flower to flower.",
            color = Color(0xFF8E24AA),
            shadowColor = Color(0xFF4A148C),
            cardBgColor = Color(0xFFF3E5F5)
        )
    )
}
