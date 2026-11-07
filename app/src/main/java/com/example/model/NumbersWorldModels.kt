package com.example.model

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

enum class NumbersActivityId(
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
    LEARN(
        key = "learn_numbers",
        titleAr = "هيا نتعلّم الأرقام",
        titleEn = "Let's Learn Numbers",
        subtitleAr = "الأرقام من ١ إلى ١٠ مع النطق والصور",
        subtitleEn = "Numbers 1 to 10 with voice & objects",
        badgeEmoji = "🔢",
        accentColor = Color(0xFFE65100),
        shadowColor = Color(0xFFBF360C),
        cardBgColor = Color(0xFFFFF3E0)
    ),
    TRACING(
        key = "number_tracing",
        titleAr = "كتابة وتتبع الأرقام",
        titleEn = "Number Tracing",
        subtitleAr = "اكتب الرقم بإصبعك على النقاط",
        subtitleEn = "Trace the dotted numbers with your finger",
        badgeEmoji = "✏️",
        accentColor = Color(0xFF00838F),
        shadowColor = Color(0xFF005662),
        cardBgColor = Color(0xFFE0F7FA)
    ),
    COUNTING(
        key = "count_objects",
        titleAr = "عدّ الأشياء",
        titleEn = "Count the Objects",
        subtitleAr = "عدّ التفاح والنجوم والبالونات",
        subtitleEn = "Count apples, stars, balloons & animals",
        badgeEmoji = "🍎",
        accentColor = Color(0xFF2E7D32),
        shadowColor = Color(0xFF1B5E20),
        cardBgColor = Color(0xFFE8F5E9)
    ),
    MATCHING(
        key = "match_number",
        titleAr = "طابق الرقم بالعدد",
        titleEn = "Match the Number",
        subtitleAr = "صل كل رقم بالمجموعة المناسبة",
        subtitleEn = "Match each number to the right group",
        badgeEmoji = "🧩",
        accentColor = Color(0xFF7B1FA2),
        shadowColor = Color(0xFF4A148C),
        cardBgColor = Color(0xFFF3E5F5)
    ),
    QUIZ(
        key = "number_quiz",
        titleAr = "مسابقة الأرقام",
        titleEn = "Number Quiz",
        subtitleAr = "أسئلة ذكية وممتعة عن الأرقام",
        subtitleEn = "Fun interactive number questions",
        badgeEmoji = "🌟",
        accentColor = Color(0xFFD81B60),
        shadowColor = Color(0xFF880E4F),
        cardBgColor = Color(0xFFFCE4EC)
    ),
    ORDER(
        key = "number_order",
        titleAr = "ترتيب الأرقام",
        titleEn = "Number Order",
        subtitleAr = "رتّب الأرقام من ١ إلى ١٠",
        subtitleEn = "Arrange numbers in order from 1 to 10",
        badgeEmoji = "🚂",
        accentColor = Color(0xFF1565C0),
        shadowColor = Color(0xFF0D47A1),
        cardBgColor = Color(0xFFE3F2FD)
    );

    fun title(lang: AppLanguage): String = if (lang == AppLanguage.AR) titleAr else titleEn
    fun subtitle(lang: AppLanguage): String = if (lang == AppLanguage.AR) subtitleAr else subtitleEn
}

enum class CountableObjectType(
    val singularAr: String,
    val pluralAr: String,
    val pluralEn: String,
    val questionAr: String,
    val questionEn: String,
    val primaryColor: Color,
    val secondaryColor: Color
) {
    APPLE(
        singularAr = "تفاحة",
        pluralAr = "تفاحات",
        pluralEn = "apples",
        questionAr = "كم تفاحة ترين؟",
        questionEn = "How many apples do you see?",
        primaryColor = Color(0xFFE53935),
        secondaryColor = Color(0xFF43A047)
    ),
    STAR(
        singularAr = "نجمة",
        pluralAr = "نجوم",
        pluralEn = "stars",
        questionAr = "كم نجمة ترين؟",
        questionEn = "How many stars do you see?",
        primaryColor = Color(0xFFFFB300),
        secondaryColor = Color(0xFFFFF176)
    ),
    BALLOON(
        singularAr = "بالون",
        pluralAr = "بالونات",
        pluralEn = "balloons",
        questionAr = "كم بالوناً ترين؟",
        questionEn = "How many balloons do you see?",
        primaryColor = Color(0xFFEC407A),
        secondaryColor = Color(0xFF29B6F6)
    ),
    FLOWER(
        singularAr = "زهرة",
        pluralAr = "زهور",
        pluralEn = "flowers",
        questionAr = "كم زهرة ترين؟",
        questionEn = "How many flowers do you see?",
        primaryColor = Color(0xFFAB47BC),
        secondaryColor = Color(0xFFFFEE58)
    ),
    ANIMAL(
        singularAr = "أرنب لطيف",
        pluralAr = "حيوانات لطيفة",
        pluralEn = "cute bunnies",
        questionAr = "كم حيواناً لطيفاً ترين؟",
        questionEn = "How many cute animals do you see?",
        primaryColor = Color(0xFFFF8F00),
        secondaryColor = Color(0xFFFFCC80)
    )
}

data class NumberLessonItem(
    val value: Int,
    val symbolAr: String,
    val symbolEn: String,
    val wordAr: String,
    val wordEn: String,
    val spokenPhraseAr: String,
    val spokenPhraseEn: String,
    val objectType: CountableObjectType,
    val color: Color,
    val shadowColor: Color,
    val traceCheckpoints: List<Offset>
) {
    fun symbol(numeralLang: AppLanguage): String =
        if (numeralLang == AppLanguage.AR) symbolAr else symbolEn

    fun word(numeralLang: AppLanguage): String =
        if (numeralLang == AppLanguage.AR) wordAr else wordEn

    fun spokenSentence(numeralLang: AppLanguage): String =
        if (numeralLang == AppLanguage.AR) spokenPhraseAr else spokenPhraseEn
}

object NumbersCurriculum {

    val lessons: List<NumberLessonItem> = listOf(
        NumberLessonItem(
            value = 1,
            symbolAr = "١",
            symbolEn = "1",
            wordAr = "واحد",
            wordEn = "One",
            spokenPhraseAr = "الرقم واحد! تفاحة واحدة جميلة.",
            spokenPhraseEn = "Number One! One shiny red apple.",
            objectType = CountableObjectType.APPLE,
            color = Color(0xFFE53935),
            shadowColor = Color(0xFFB71C1C),
            traceCheckpoints = listOf(
                Offset(0.50f, 0.20f),
                Offset(0.50f, 0.40f),
                Offset(0.50f, 0.60f),
                Offset(0.50f, 0.80f)
            )
        ),
        NumberLessonItem(
            value = 2,
            symbolAr = "٢",
            symbolEn = "2",
            wordAr = "اثنان",
            wordEn = "Two",
            spokenPhraseAr = "الرقم اثنان! نجمتان لامعتان في السماء.",
            spokenPhraseEn = "Number Two! Two twinkling golden stars.",
            objectType = CountableObjectType.STAR,
            color = Color(0xFFFB8C00),
            shadowColor = Color(0xFFE65100),
            traceCheckpoints = listOf(
                Offset(0.32f, 0.28f),
                Offset(0.65f, 0.28f),
                Offset(0.55f, 0.55f),
                Offset(0.32f, 0.80f),
                Offset(0.68f, 0.80f)
            )
        ),
        NumberLessonItem(
            value = 3,
            symbolAr = "٣",
            symbolEn = "3",
            wordAr = "ثلاثة",
            wordEn = "Three",
            spokenPhraseAr = "ثلاثة! ثلاث تفاحات لذيذة.",
            spokenPhraseEn = "Three! Three colorful apples.",
            objectType = CountableObjectType.APPLE,
            color = Color(0xFF43A047),
            shadowColor = Color(0xFF1B5E20),
            traceCheckpoints = listOf(
                Offset(0.32f, 0.24f),
                Offset(0.66f, 0.34f),
                Offset(0.45f, 0.50f),
                Offset(0.68f, 0.68f),
                Offset(0.32f, 0.78f)
            )
        ),
        NumberLessonItem(
            value = 4,
            symbolAr = "٤",
            symbolEn = "4",
            wordAr = "أربعة",
            wordEn = "Four",
            spokenPhraseAr = "الرقم أربعة! أربع زهور ملوّنة.",
            spokenPhraseEn = "Number Four! Four colorful flowers.",
            objectType = CountableObjectType.FLOWER,
            color = Color(0xFF00ACC1),
            shadowColor = Color(0xFF006064),
            traceCheckpoints = listOf(
                Offset(0.60f, 0.22f),
                Offset(0.30f, 0.58f),
                Offset(0.70f, 0.58f),
                Offset(0.60f, 0.80f)
            )
        ),
        NumberLessonItem(
            value = 5,
            symbolAr = "٥",
            symbolEn = "5",
            wordAr = "خمسة",
            wordEn = "Five",
            spokenPhraseAr = "أحسنتِ! هذا هو الرقم خمسة. خمس نجوم متلألئة!",
            spokenPhraseEn = "Great job! This is number Five. Five shining stars!",
            objectType = CountableObjectType.STAR,
            color = Color(0xFF8E24AA),
            shadowColor = Color(0xFF4A148C),
            traceCheckpoints = listOf(
                Offset(0.65f, 0.22f),
                Offset(0.35f, 0.22f),
                Offset(0.35f, 0.48f),
                Offset(0.66f, 0.62f),
                Offset(0.35f, 0.78f)
            )
        ),
        NumberLessonItem(
            value = 6,
            symbolAr = "٦",
            symbolEn = "6",
            wordAr = "ستة",
            wordEn = "Six",
            spokenPhraseAr = "الرقم ستة! ستة بالونات مبهجة.",
            spokenPhraseEn = "Number Six! Six cheerful balloons.",
            objectType = CountableObjectType.BALLOON,
            color = Color(0xFFD81B60),
            shadowColor = Color(0xFF880E4F),
            traceCheckpoints = listOf(
                Offset(0.62f, 0.22f),
                Offset(0.34f, 0.50f),
                Offset(0.48f, 0.80f),
                Offset(0.66f, 0.64f),
                Offset(0.45f, 0.50f)
            )
        ),
        NumberLessonItem(
            value = 7,
            symbolAr = "٧",
            symbolEn = "7",
            wordAr = "سبعة",
            wordEn = "Seven",
            spokenPhraseAr = "الرقم سبعة! سبعة حيوانات لطيفة في الحديقة.",
            spokenPhraseEn = "Number Seven! Seven cute animals in the meadow.",
            objectType = CountableObjectType.ANIMAL,
            color = Color(0xFF3949AB),
            shadowColor = Color(0xFF1A237E),
            traceCheckpoints = listOf(
                Offset(0.30f, 0.24f),
                Offset(0.70f, 0.24f),
                Offset(0.55f, 0.52f),
                Offset(0.42f, 0.80f)
            )
        ),
        NumberLessonItem(
            value = 8,
            symbolAr = "٨",
            symbolEn = "8",
            wordAr = "ثمانية",
            wordEn = "Eight",
            spokenPhraseAr = "الرقم ثمانية! ثماني تفاحات طازجة.",
            spokenPhraseEn = "Number Eight! Eight fresh red apples.",
            objectType = CountableObjectType.APPLE,
            color = Color(0xFF00897B),
            shadowColor = Color(0xFF004D40),
            traceCheckpoints = listOf(
                Offset(0.50f, 0.22f),
                Offset(0.34f, 0.36f),
                Offset(0.50f, 0.50f),
                Offset(0.66f, 0.66f),
                Offset(0.50f, 0.80f)
            )
        ),
        NumberLessonItem(
            value = 9,
            symbolAr = "٩",
            symbolEn = "9",
            wordAr = "تسعة",
            wordEn = "Nine",
            spokenPhraseAr = "الرقم تسعة! تسع زهور عطرة.",
            spokenPhraseEn = "Number Nine! Nine lovely flowers.",
            objectType = CountableObjectType.FLOWER,
            color = Color(0xFFF4511E),
            shadowColor = Color(0xFFBF360C),
            traceCheckpoints = listOf(
                Offset(0.50f, 0.22f),
                Offset(0.34f, 0.36f),
                Offset(0.64f, 0.36f),
                Offset(0.64f, 0.60f),
                Offset(0.44f, 0.80f)
            )
        ),
        NumberLessonItem(
            value = 10,
            symbolAr = "١٠",
            symbolEn = "10",
            wordAr = "عشرة",
            wordEn = "Ten",
            spokenPhraseAr = "الرقم عشرة! عشر نجوم ذهبية رائعة!",
            spokenPhraseEn = "Number Ten! Ten amazing golden stars!",
            objectType = CountableObjectType.STAR,
            color = Color(0xFF6A1B9A),
            shadowColor = Color(0xFF38006B),
            traceCheckpoints = listOf(
                Offset(0.28f, 0.25f),
                Offset(0.28f, 0.78f),
                Offset(0.66f, 0.25f),
                Offset(0.50f, 0.52f),
                Offset(0.66f, 0.78f),
                Offset(0.82f, 0.52f)
            )
        )
    )

    fun byValue(number: Int): NumberLessonItem =
        lessons.firstOrNull { it.value == number.coerceIn(1, 10) } ?: lessons.first()

    fun formatNumeral(number: Int, lang: AppLanguage): String =
        byValue(number).symbol(lang)
}

enum class QuizQuestionType {
    AFTER_NUMBER,
    FIND_NUMBER,
    COUNT_OBJECTS,
    WHICH_GROUP_HAS_N
}

data class NumberQuizQuestion(
    val id: Int,
    val type: QuizQuestionType,
    val promptAr: String,
    val promptEn: String,
    val spokenAr: String,
    val spokenEn: String,
    val visualCount: Int? = null,
    val visualObject: CountableObjectType? = null,
    val options: List<Int>,
    val optionIsGroupQuantity: Boolean = false,
    val correctValue: Int
) {
    fun prompt(lang: AppLanguage): String = if (lang == AppLanguage.AR) promptAr else promptEn
    fun spoken(lang: AppLanguage): String = if (lang == AppLanguage.AR) spokenAr else spokenEn
}

object NumberQuizBank {
    val questions: List<NumberQuizQuestion> = listOf(
        NumberQuizQuestion(
            id = 1,
            type = QuizQuestionType.AFTER_NUMBER,
            promptAr = "ما الرقم الذي يأتي بعد ٣ ؟",
            promptEn = "Which number comes after 3?",
            spokenAr = "ما الرقم الذي يأتي بعد ثلاثة؟",
            spokenEn = "Which number comes after three?",
            options = listOf(2, 4, 6),
            correctValue = 4
        ),
        NumberQuizQuestion(
            id = 2,
            type = QuizQuestionType.FIND_NUMBER,
            promptAr = "أين هو الرقم سبعة (٧) ؟",
            promptEn = "Find number Seven (7)!",
            spokenAr = "أين هو الرقم سبعة؟",
            spokenEn = "Can you find number seven?",
            options = listOf(5, 7, 9),
            correctValue = 7
        ),
        NumberQuizQuestion(
            id = 3,
            type = QuizQuestionType.COUNT_OBJECTS,
            promptAr = "كم تفاحة ترين في الصورة؟",
            promptEn = "How many apples do you see?",
            spokenAr = "كم تفاحة ترين في الصورة؟ هيا نعدّها معاً!",
            spokenEn = "How many apples do you see? Let's count them!",
            visualCount = 6,
            visualObject = CountableObjectType.APPLE,
            options = listOf(4, 6, 8),
            correctValue = 6
        ),
        NumberQuizQuestion(
            id = 4,
            type = QuizQuestionType.WHICH_GROUP_HAS_N,
            promptAr = "أي مجموعة تحتوي على ٥ نجوم؟",
            promptEn = "Which group contains 5 stars?",
            spokenAr = "أي مجموعة تحتوي على خمس نجوم؟",
            spokenEn = "Which group contains five stars?",
            visualObject = CountableObjectType.STAR,
            options = listOf(3, 5, 8),
            optionIsGroupQuantity = true,
            correctValue = 5
        ),
        NumberQuizQuestion(
            id = 5,
            type = QuizQuestionType.AFTER_NUMBER,
            promptAr = "ما الرقم الذي يأتي بعد ٨ ؟",
            promptEn = "Which number comes after 8?",
            spokenAr = "ما الرقم الذي يأتي بعد ثمانية؟",
            spokenEn = "Which number comes after eight?",
            options = listOf(7, 9, 10),
            correctValue = 9
        )
    )
}
