package com.example.model

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

enum class LettersSectionMode(
    val key: String,
    val titleAr: String,
    val titleEn: String,
    val language: AppLanguage
) {
    ARABIC(
        key = "arabic",
        titleAr = "الحروف العربية (أ – ي)",
        titleEn = "Arabic Letters (28)",
        language = AppLanguage.AR
    ),
    ENGLISH(
        key = "english",
        titleAr = "الحروف الإنجليزية (A – Z)",
        titleEn = "English Letters (A–Z)",
        language = AppLanguage.EN
    )
}

enum class LettersActivityId(
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
    MEET_LETTER(
        key = "meet_letter",
        titleAr = "تعرّف على الحرف",
        titleEn = "Meet the Letter",
        subtitleAr = "الحرف وصوته والكلمة المصوّرة",
        subtitleEn = "Learn the letter, its sound & picture word",
        badgeEmoji = "🔤",
        accentColor = Color(0xFF00838F),
        shadowColor = Color(0xFF005662),
        cardBgColor = Color(0xFFE0F7FA)
    ),
    LETTER_TRACING(
        key = "letter_tracing",
        titleAr = "كتابة وتتبع الحرف",
        titleEn = "Letter Tracing",
        subtitleAr = "اكتب الحرف بإصبعك في الاتجاه الصحيح",
        subtitleEn = "Trace the dotted letter with your finger",
        badgeEmoji = "✏️",
        accentColor = Color(0xFFAD1457),
        shadowColor = Color(0xFF78002E),
        cardBgColor = Color(0xFFFCE4EC)
    ),
    FIND_LETTER(
        key = "find_letter",
        titleAr = "أين هو الحرف؟",
        titleEn = "Find the Letter",
        subtitleAr = "استمع إلى لولو واختر الحرف الصحيح",
        subtitleEn = "Listen to Lulu and tap the right letter",
        badgeEmoji = "🔍",
        accentColor = Color(0xFF2E7D32),
        shadowColor = Color(0xFF1B5E20),
        cardBgColor = Color(0xFFE8F5E9)
    ),
    MATCH_PICTURE(
        key = "match_picture",
        titleAr = "طابق الحرف بالصورة",
        titleEn = "Match Letter to Picture",
        subtitleAr = "اختر الصورة التي تبدأ بالحرف",
        subtitleEn = "Choose the picture starting with the letter",
        badgeEmoji = "🖼️",
        accentColor = Color(0xFF6A1B9A),
        shadowColor = Color(0xFF38006B),
        cardBgColor = Color(0xFFF3E5F5)
    ),
    MISSING_LETTER(
        key = "missing_letter",
        titleAr = "أكمل الحرف الناقص",
        titleEn = "Complete the Word",
        subtitleAr = "اختر الحرف الأول الناقص في الكلمة",
        subtitleEn = "Pick the missing first letter of the word",
        badgeEmoji = "🧩",
        accentColor = Color(0xFFE65100),
        shadowColor = Color(0xFFBF360C),
        cardBgColor = Color(0xFFFFF3E0)
    ),
    LETTER_QUIZ(
        key = "letter_quiz",
        titleAr = "مسابقة الحروف",
        titleEn = "Letter Quiz",
        subtitleAr = "أسئلة تفاعلية ممتعة عن الحروف",
        subtitleEn = "Fun interactive questions on letters",
        badgeEmoji = "🌟",
        accentColor = Color(0xFF1565C0),
        shadowColor = Color(0xFF0D47A1),
        cardBgColor = Color(0xFFE3F2FD)
    );

    fun title(lang: AppLanguage): String = if (lang == AppLanguage.AR) titleAr else titleEn
    fun subtitle(lang: AppLanguage): String = if (lang == AppLanguage.AR) subtitleAr else subtitleEn
}

data class LetterLessonItem(
    val id: String,
    val index: Int,
    val section: LettersSectionMode,
    val isolatedSymbol: String,
    val uppercaseSymbol: String,
    val lowercaseSymbol: String,
    val letterNameAr: String,
    val letterNameEn: String,
    val phoneticHintAr: String,
    val phoneticHintEn: String,
    val dotsDescriptionAr: String,
    val dotsDescriptionEn: String,
    val wordAr: String,
    val wordEn: String,
    val missingWordDisplay: String,
    val emoji: String,
    val color: Color,
    val shadowColor: Color,
    val cardBgColor: Color,
    val strokeDirectionHintAr: String,
    val strokeDirectionHintEn: String,
    val traceCheckpoints: List<Offset>
) {
    /**
     * Display label on alphabet grid tiles:
     * - For Arabic: isolated letter ("أ", "ب", ...)
     * - For English: uppercase + lowercase ("A a", "B b", ...)
     */
    val gridDisplaySymbol: String
        get() = if (section == LettersSectionMode.ARABIC) {
            isolatedSymbol
        } else {
            "$uppercaseSymbol $lowercaseSymbol"
        }

    val primaryWord: String
        get() = if (section == LettersSectionMode.ARABIC) wordAr else wordEn

    val secondaryWordMeaning: String
        get() = if (section == LettersSectionMode.ARABIC) wordEn else wordAr

    val letterDisplayName: String
        get() = if (section == LettersSectionMode.ARABIC) {
            "حرف ال$letterNameAr"
        } else {
            "Letter $uppercaseSymbol ($letterNameEn)"
        }

    /**
     * Lulu's greeting when introducing the letter in Activity 1 (Meet the Letter).
     */
    val luluMeetSentence: String
        get() = if (section == LettersSectionMode.ARABIC) {
            "هيا نتعرف على حرف ال$letterNameAr! هذا حرف ال$letterNameAr. رائع! $wordAr يبدأ بحرف ال$letterNameAr."
        } else {
            "Let's meet letter $uppercaseSymbol! This is letter $uppercaseSymbol. $uppercaseSymbol is for $wordEn!"
        }

    /**
     * Lulu's short praise when the letter or word speaker button is tapped.
     */
    val luluPronunciationSentence: String
        get() = if (section == LettersSectionMode.ARABIC) {
            "أحسنتِ! هذا حرف ال$letterNameAr. $wordAr يبدأ بحرف ال$letterNameAr."
        } else {
            "Great job! This is letter $uppercaseSymbol. $uppercaseSymbol is for $wordEn!"
        }

    /**
     * Lulu's prompt when starting Activity 2 (Letter Tracing).
     */
    val luluTracePrompt: String
        get() = if (section == LettersSectionMode.ARABIC) {
            "هيا نكتب حرف ال$letterNameAr معاً! اتبعي النقاط من البداية."
        } else {
            "Let's write letter $uppercaseSymbol together! Follow the glowing dots."
        }
}

object LettersCurriculum {

    private val paletteColors = listOf(
        Triple(Color(0xFFE53935), Color(0xFFB71C1C), Color(0xFFFFEBEE)),
        Triple(Color(0xFF00ACC1), Color(0xFF006064), Color(0xFFE0F7FA)),
        Triple(Color(0xFF43A047), Color(0xFF1B5E20), Color(0xFFE8F5E9)),
        Triple(Color(0xFFFB8C00), Color(0xFFE65100), Color(0xFFFFF3E0)),
        Triple(Color(0xFF8E24AA), Color(0xFF4A148C), Color(0xFFF3E5F5)),
        Triple(Color(0xFFD81B60), Color(0xFF880E4F), Color(0xFFFCE4EC)),
        Triple(Color(0xFF3949AB), Color(0xFF1A237E), Color(0xFFE8EAF6)),
        Triple(Color(0xFF00897B), Color(0xFF004D40), Color(0xFFE0F2F1))
    )

    // Standard Arabic tracing paths (respecting Right-to-Left and Top-to-Bottom stroke order)
    private val verticalArabicStroke = listOf(
        Offset(0.50f, 0.20f),
        Offset(0.50f, 0.40f),
        Offset(0.50f, 0.60f),
        Offset(0.50f, 0.78f)
    )

    private val boatRightToLeftStroke = listOf(
        Offset(0.74f, 0.48f),
        Offset(0.60f, 0.66f),
        Offset(0.45f, 0.68f),
        Offset(0.28f, 0.50f)
    )

    private val jeemStroke = listOf(
        Offset(0.36f, 0.30f),
        Offset(0.64f, 0.32f),
        Offset(0.44f, 0.54f),
        Offset(0.58f, 0.76f)
    )

    private val dalStroke = listOf(
        Offset(0.58f, 0.32f),
        Offset(0.66f, 0.54f),
        Offset(0.48f, 0.68f),
        Offset(0.32f, 0.68f)
    )

    private val raaStroke = listOf(
        Offset(0.60f, 0.32f),
        Offset(0.58f, 0.52f),
        Offset(0.46f, 0.70f),
        Offset(0.32f, 0.78f)
    )

    private val seenStroke = listOf(
        Offset(0.74f, 0.44f),
        Offset(0.58f, 0.52f),
        Offset(0.44f, 0.66f),
        Offset(0.28f, 0.58f)
    )

    private val loopRightToLeftStroke = listOf(
        Offset(0.68f, 0.42f),
        Offset(0.54f, 0.36f),
        Offset(0.46f, 0.58f),
        Offset(0.30f, 0.68f)
    )

    /**
     * SECTION 1: ALL 28 ARABIC LETTERS (Isolated forms with clear dot hints and words)
     * أ ب ت ث ج ح خ د ذ ر ز س ش ص ض ط ظ ع غ ف ق ك ل م ن هـ و ي
     */
    val arabicLetters: List<LetterLessonItem> = listOf(
        createArabicLetter(
            index = 1, symbol = "أ", nameAr = "ألف", nameEn = "Alif",
            dotsAr = "فوقه همزة (ء)", dotsEn = "Hamza on top",
            wordAr = "أسد", wordEn = "Lion", missingWord = "_سد", emoji = "🦁",
            strokeHintAr = "من الأعلى إلى الأسفل مع الهمزة",
            checkpoints = verticalArabicStroke
        ),
        createArabicLetter(
            index = 2, symbol = "ب", nameAr = "باء", nameEn = "Baa",
            dotsAr = "نقطة واحدة تحت الحرف (•)", dotsEn = "1 dot below",
            wordAr = "بطة", wordEn = "Duck", missingWord = "_طة", emoji = "🦆",
            strokeHintAr = "من اليمين إلى اليسار ثم نقطة في الأسفل",
            checkpoints = boatRightToLeftStroke
        ),
        createArabicLetter(
            index = 3, symbol = "ت", nameAr = "تاء", nameEn = "Taa",
            dotsAr = "نقطتان فوق الحرف (••)", dotsEn = "2 dots on top",
            wordAr = "تفاحة", wordEn = "Apple", missingWord = "_فاحة", emoji = "🍎",
            strokeHintAr = "من اليمين إلى اليسار ثم نقطتان في الأعلى",
            checkpoints = boatRightToLeftStroke
        ),
        createArabicLetter(
            index = 4, symbol = "ث", nameAr = "ثاء", nameEn = "Thaa",
            dotsAr = "ثلاث نقاط فوق الحرف (•••)", dotsEn = "3 dots on top",
            wordAr = "ثعلب", wordEn = "Fox", missingWord = "_علب", emoji = "🦊",
            strokeHintAr = "من اليمين إلى اليسار ثم ثلاث نقاط في الأعلى",
            checkpoints = boatRightToLeftStroke
        ),
        createArabicLetter(
            index = 5, symbol = "ج", nameAr = "جيم", nameEn = "Jeem",
            dotsAr = "نقطة واحدة في بطن الحرف (•)", dotsEn = "1 dot inside",
            wordAr = "جمل", wordEn = "Camel", missingWord = "_مل", emoji = "🐪",
            strokeHintAr = "خط علوي ثم نصف دائرة ونقطة في الوسط",
            checkpoints = jeemStroke
        ),
        createArabicLetter(
            index = 6, symbol = "ح", nameAr = "حاء", nameEn = "Haa",
            dotsAr = "بدون أي نقاط", dotsEn = "No dots",
            wordAr = "حصان", wordEn = "Horse", missingWord = "_صان", emoji = "🐴",
            strokeHintAr = "خط علوي ثم نصف دائرة بدون نقاط",
            checkpoints = jeemStroke
        ),
        createArabicLetter(
            index = 7, symbol = "خ", nameAr = "خاء", nameEn = "Khaa",
            dotsAr = "نقطة واحدة فوق رأسه (•)", dotsEn = "1 dot on top",
            wordAr = "خروف", wordEn = "Sheep", missingWord = "_روف", emoji = "🐑",
            strokeHintAr = "خط علوي ثم نصف دائرة ونقطة فوقه",
            checkpoints = jeemStroke
        ),
        createArabicLetter(
            index = 8, symbol = "د", nameAr = "دال", nameEn = "Dal",
            dotsAr = "بدون أي نقاط", dotsEn = "No dots",
            wordAr = "دب", wordEn = "Bear", missingWord = "_ب", emoji = "🐻",
            strokeHintAr = "من الأعلى مائلاً ثم على السطر إلى اليسار",
            checkpoints = dalStroke
        ),
        createArabicLetter(
            index = 9, symbol = "ذ", nameAr = "ذال", nameEn = "Thal",
            dotsAr = "نقطة واحدة فوق الحرف (•)", dotsEn = "1 dot on top",
            wordAr = "ذرة", wordEn = "Corn", missingWord = "_رة", emoji = "🌽",
            strokeHintAr = "من الأعلى إلى السطر ثم نقطة في الأعلى",
            checkpoints = dalStroke
        ),
        createArabicLetter(
            index = 10, symbol = "ر", nameAr = "راء", nameEn = "Raa",
            dotsAr = "بدون أي نقاط", dotsEn = "No dots",
            wordAr = "رمان", wordEn = "Pomegranate", missingWord = "_مان", emoji = "🍎",
            strokeHintAr = "قوس لطيف ينزل من الأعلى نحو اليسار",
            checkpoints = raaStroke
        ),
        createArabicLetter(
            index = 11, symbol = "ز", nameAr = "زاي", nameEn = "Zay",
            dotsAr = "نقطة واحدة فوق الحرف (•)", dotsEn = "1 dot on top",
            wordAr = "زرافة", wordEn = "Giraffe", missingWord = "_رافة", emoji = "🦒",
            strokeHintAr = "قوس ينزل نحو اليسار وفوقه نقطة",
            checkpoints = raaStroke
        ),
        createArabicLetter(
            index = 12, symbol = "س", nameAr = "سين", nameEn = "Seen",
            dotsAr = "ثلاث أسنان بدون نقاط", dotsEn = "No dots",
            wordAr = "سمكة", wordEn = "Fish", missingWord = "_مكة", emoji = "🐟",
            strokeHintAr = "من اليمين إلى اليسار: أسنان صغيرة ثم كأس",
            checkpoints = seenStroke
        ),
        createArabicLetter(
            index = 13, symbol = "ش", nameAr = "شين", nameEn = "Sheen",
            dotsAr = "ثلاث نقاط فوق الحرف (•••)", dotsEn = "3 dots on top",
            wordAr = "شمس", wordEn = "Sun", missingWord = "_مس", emoji = "☀️",
            strokeHintAr = "من اليمين إلى اليسار مع ثلاث نقاط في الأعلى",
            checkpoints = seenStroke
        ),
        createArabicLetter(
            index = 14, symbol = "ص", nameAr = "صاد", nameEn = "Sad",
            dotsAr = "بدون أي نقاط", dotsEn = "No dots",
            wordAr = "صاروخ", wordEn = "Rocket", missingWord = "_اروخ", emoji = "🚀",
            strokeHintAr = "حلقة بيضاوية من اليمين ثم كأس إلى اليسار",
            checkpoints = loopRightToLeftStroke
        ),
        createArabicLetter(
            index = 15, symbol = "ض", nameAr = "ضاد", nameEn = "Dad",
            dotsAr = "نقطة واحدة فوق الحرف (•)", dotsEn = "1 dot on top",
            wordAr = "ضفدع", wordEn = "Frog", missingWord = "_فدع", emoji = "🐸",
            strokeHintAr = "حلقة من اليمين ثم كأس ونقطة في الأعلى",
            checkpoints = loopRightToLeftStroke
        ),
        createArabicLetter(
            index = 16, symbol = "ط", nameAr = "طاء", nameEn = "Taa",
            dotsAr = "بدون أي نقاط", dotsEn = "No dots",
            wordAr = "طائرة", wordEn = "Airplane", missingWord = "_ائرة", emoji = "✈️",
            strokeHintAr = "حلقة على السطر ثم عصا عمودية من الأعلى",
            checkpoints = loopRightToLeftStroke
        ),
        createArabicLetter(
            index = 17, symbol = "ظ", nameAr = "ظاء", nameEn = "Zaa",
            dotsAr = "نقطة واحدة فوق الحرف (•)", dotsEn = "1 dot on top",
            wordAr = "ظبي", wordEn = "Gazelle", missingWord = "_بي", emoji = "🦌",
            strokeHintAr = "حلقة وعصا عمودية مع نقطة في الأعلى",
            checkpoints = loopRightToLeftStroke
        ),
        createArabicLetter(
            index = 18, symbol = "ع", nameAr = "عين", nameEn = "Ayn",
            dotsAr = "بدون أي نقاط", dotsEn = "No dots",
            wordAr = "عصفور", wordEn = "Bird", missingWord = "_صفور", emoji = "🐦",
            strokeHintAr = "نصف دائرة صغير في الأعلى ونصف دائرة كبير تحته",
            checkpoints = jeemStroke
        ),
        createArabicLetter(
            index = 19, symbol = "غ", nameAr = "غين", nameEn = "Ghayn",
            dotsAr = "نقطة واحدة فوق الحرف (•)", dotsEn = "1 dot on top",
            wordAr = "غيمة", wordEn = "Cloud", missingWord = "_يمة", emoji = "☁️",
            strokeHintAr = "نصف دائرة صغير وكبير مع نقطة في الأعلى",
            checkpoints = jeemStroke
        ),
        createArabicLetter(
            index = 20, symbol = "ف", nameAr = "فاء", nameEn = "Faa",
            dotsAr = "نقطة واحدة فوق الحرف (•)", dotsEn = "1 dot on top",
            wordAr = "فراشة", wordEn = "Butterfly", missingWord = "_راشة", emoji = "🦋",
            strokeHintAr = "رأس دائري صغير ثم طبق على السطر ونقطة فوقه",
            checkpoints = boatRightToLeftStroke
        ),
        createArabicLetter(
            index = 21, symbol = "ق", nameAr = "قاف", nameEn = "Qaf",
            dotsAr = "نقطتان فوق الحرف (••)", dotsEn = "2 dots on top",
            wordAr = "قمر", wordEn = "Moon", missingWord = "_مر", emoji = "🌙",
            strokeHintAr = "رأس دائري وكأس عميقة مع نقطتين في الأعلى",
            checkpoints = loopRightToLeftStroke
        ),
        createArabicLetter(
            index = 22, symbol = "ك", nameAr = "كاف", nameEn = "Kaf",
            dotsAr = "بداخله شارة الكاف الصغيرة", dotsEn = "Small Kaf mark inside",
            wordAr = "كتاب", wordEn = "Book", missingWord = "_تاب", emoji = "📚",
            strokeHintAr = "من الأعلى إلى الأسفل ثم على السطر إلى اليسار",
            checkpoints = listOf(
                Offset(0.62f, 0.22f),
                Offset(0.62f, 0.58f),
                Offset(0.46f, 0.68f),
                Offset(0.30f, 0.66f)
            )
        ),
        createArabicLetter(
            index = 23, symbol = "ل", nameAr = "لام", nameEn = "Lam",
            dotsAr = "بدون أي نقاط", dotsEn = "No dots",
            wordAr = "ليمون", wordEn = "Lemon", missingWord = "_يمون", emoji = "🍋",
            strokeHintAr = "من الأعلى إلى الأسفل ثم قوس نحو اليسار",
            checkpoints = listOf(
                Offset(0.58f, 0.20f),
                Offset(0.58f, 0.52f),
                Offset(0.46f, 0.72f),
                Offset(0.34f, 0.64f)
            )
        ),
        createArabicLetter(
            index = 24, symbol = "م", nameAr = "ميم", nameEn = "Meem",
            dotsAr = "بدون أي نقاط", dotsEn = "No dots",
            wordAr = "موز", wordEn = "Banana", missingWord = "_وز", emoji = "🍌",
            strokeHintAr = "رأس صغير ثم خط ينزل إلى الأسفل",
            checkpoints = listOf(
                Offset(0.56f, 0.34f),
                Offset(0.42f, 0.40f),
                Offset(0.48f, 0.60f),
                Offset(0.48f, 0.78f)
            )
        ),
        createArabicLetter(
            index = 25, symbol = "ن", nameAr = "نون", nameEn = "Noon",
            dotsAr = "نقطة واحدة في وسط الكأس (•)", dotsEn = "1 dot above",
            wordAr = "نحلة", wordEn = "Bee", missingWord = "_حلة", emoji = "🐝",
            strokeHintAr = "كأس نصف دائرية من اليمين إلى اليسار وفوقها نقطة",
            checkpoints = listOf(
                Offset(0.68f, 0.42f),
                Offset(0.56f, 0.68f),
                Offset(0.42f, 0.68f),
                Offset(0.32f, 0.44f)
            )
        ),
        createArabicLetter(
            index = 26, symbol = "هـ", nameAr = "هاء", nameEn = "Haa",
            dotsAr = "بدون أي نقاط", dotsEn = "No dots",
            wordAr = "هدية", wordEn = "Gift", missingWord = "_دية", emoji = "🎁",
            strokeHintAr = "حلقة دائرية مزدوجة على السطر من اليمين إلى اليسار",
            checkpoints = loopRightToLeftStroke
        ),
        createArabicLetter(
            index = 27, symbol = "و", nameAr = "واو", nameEn = "Waw",
            dotsAr = "بدون أي نقاط", dotsEn = "No dots",
            wordAr = "وردة", wordEn = "Rose", missingWord = "_ردة", emoji = "🌹",
            strokeHintAr = "رأس دائري في الأعلى ثم ذيل ينزل نحو اليسار",
            checkpoints = raaStroke
        ),
        createArabicLetter(
            index = 28, symbol = "ي", nameAr = "ياء", nameEn = "Yaa",
            dotsAr = "نقطتان تحت الحرف (••)", dotsEn = "2 dots below",
            wordAr = "يد", wordEn = "Hand", missingWord = "_د", emoji = "✋",
            strokeHintAr = "منحنى من اليمين ثم كأس إلى اليسار ونقطتان في الأسفل",
            checkpoints = loopRightToLeftStroke
        )
    )

    private val englishStandardCheckpoints = listOf(
        Offset(0.36f, 0.24f),
        Offset(0.50f, 0.48f),
        Offset(0.64f, 0.48f),
        Offset(0.50f, 0.76f)
    )

    /**
     * SECTION 2: ALL 26 ENGLISH LETTERS (A to Z, Uppercase + Lowercase)
     */
    val englishLetters: List<LetterLessonItem> = listOf(
        createEnglishLetter(1, "A", "a", "إي", "Ay", "Apple", "تفاحة", "_pple", "🍎"),
        createEnglishLetter(2, "B", "b", "بي", "Bee", "Ball", "كرة", "_all", "⚽"),
        createEnglishLetter(3, "C", "c", "سي", "See", "Cat", "قطة", "_at", "🐱"),
        createEnglishLetter(4, "D", "d", "دي", "Dee", "Dog", "كلب", "_og", "🐶"),
        createEnglishLetter(5, "E", "e", "إي", "Ee", "Elephant", "فيل", "_lephant", "🐘"),
        createEnglishLetter(6, "F", "f", "إف", "Ef", "Fish", "سمكة", "_ish", "🐟"),
        createEnglishLetter(7, "G", "g", "جي", "Jee", "Giraffe", "زرافة", "_iraffe", "🦒"),
        createEnglishLetter(8, "H", "h", "إتش", "Aitch", "Horse", "حصان", "_orse", "🐴"),
        createEnglishLetter(9, "I", "i", "آي", "Eye", "Ice Cream", "مثلجات", "_ce Cream", "🍦"),
        createEnglishLetter(10, "J", "j", "جاي", "Jay", "Juice", "عصير", "_uice", "🧃"),
        createEnglishLetter(11, "K", "k", "كاي", "Kay", "Kite", "طائرة ورقية", "_ite", "🪁"),
        createEnglishLetter(12, "L", "l", "إل", "El", "Lion", "أسد", "_ion", "🦁"),
        createEnglishLetter(13, "M", "m", "إم", "Em", "Monkey", "قرد", "_onkey", "🐵"),
        createEnglishLetter(14, "N", "n", "إن", "En", "Nest", "عش", "_est", "🪺"),
        createEnglishLetter(15, "O", "o", "أو", "Oh", "Orange", "برتقالة", "_range", "🍊"),
        createEnglishLetter(16, "P", "p", "بي", "Pee", "Penguin", "بطريق", "_enguin", "🐧"),
        createEnglishLetter(17, "Q", "q", "كيو", "Cue", "Queen", "ملكة", "_ueen", "👑"),
        createEnglishLetter(18, "R", "r", "آر", "Ar", "Rabbit", "أرنب", "_abbit", "🐰"),
        createEnglishLetter(19, "S", "s", "إس", "Es", "Sun", "شمس", "_un", "☀️"),
        createEnglishLetter(20, "T", "t", "تي", "Tee", "Train", "قطار", "_rain", "🚂"),
        createEnglishLetter(21, "U", "u", "يو", "You", "Umbrella", "مظلة", "_mbrella", "☂️"),
        createEnglishLetter(22, "V", "v", "في", "Vee", "Violin", "كمان", "_iolin", "🎻"),
        createEnglishLetter(23, "W", "w", "دبليو", "Double-U", "Whale", "حوت", "_hale", "🐋"),
        createEnglishLetter(24, "X", "x", "إكس", "Ex", "Xylophone", "إكسيلوفون", "_ylophone", "🎹"),
        createEnglishLetter(25, "Y", "y", "واي", "Why", "Yacht", "قارب شراعي", "_acht", "⛵"),
        createEnglishLetter(26, "Z", "z", "زد", "Zee", "Zebra", "حمار وحشي", "_ebra", "🦓")
    )

    fun lettersForSection(section: LettersSectionMode): List<LetterLessonItem> =
        if (section == LettersSectionMode.ARABIC) arabicLetters else englishLetters

    fun byIdOrFirst(section: LettersSectionMode, id: String?): LetterLessonItem {
        val list = lettersForSection(section)
        return list.firstOrNull { it.id == id } ?: list.first()
    }

    /**
     * Returns distractor letters that do NOT share the exact same base shape when we want
     * clear visual distinction for 4–6 year old learners.
     */
    fun getDistinctDistractors(
        target: LetterLessonItem,
        count: Int
    ): List<LetterLessonItem> {
        val pool = lettersForSection(target.section).filter { candidate ->
            candidate.id != target.id &&
                Math.abs(candidate.index - target.index) >= 2
        }
        return pool.shuffled().take(count)
    }

    private fun createArabicLetter(
        index: Int,
        symbol: String,
        nameAr: String,
        nameEn: String,
        dotsAr: String,
        dotsEn: String,
        wordAr: String,
        wordEn: String,
        missingWord: String,
        emoji: String,
        strokeHintAr: String,
        checkpoints: List<Offset>
    ): LetterLessonItem {
        val palette = paletteColors[(index - 1) % paletteColors.size]
        return LetterLessonItem(
            id = "ar_$index",
            index = index,
            section = LettersSectionMode.ARABIC,
            isolatedSymbol = symbol,
            uppercaseSymbol = symbol,
            lowercaseSymbol = symbol,
            letterNameAr = nameAr,
            letterNameEn = nameEn,
            phoneticHintAr = "حرف ال$nameAr ($symbol)",
            phoneticHintEn = "Letter $nameEn ($symbol)",
            dotsDescriptionAr = dotsAr,
            dotsDescriptionEn = dotsEn,
            wordAr = wordAr,
            wordEn = wordEn,
            missingWordDisplay = missingWord,
            emoji = emoji,
            color = palette.first,
            shadowColor = palette.second,
            cardBgColor = palette.third,
            strokeDirectionHintAr = strokeHintAr,
            strokeDirectionHintEn = "Trace from Right to Left",
            traceCheckpoints = checkpoints
        )
    }

    private fun createEnglishLetter(
        index: Int,
        upper: String,
        lower: String,
        nameAr: String,
        nameEn: String,
        wordEn: String,
        wordAr: String,
        missingWord: String,
        emoji: String
    ): LetterLessonItem {
        val palette = paletteColors[(index - 1) % paletteColors.size]
        return LetterLessonItem(
            id = "en_$upper",
            index = index,
            section = LettersSectionMode.ENGLISH,
            isolatedSymbol = "$upper $lower",
            uppercaseSymbol = upper,
            lowercaseSymbol = lower,
            letterNameAr = nameAr,
            letterNameEn = nameEn,
            phoneticHintAr = "حرف $upper ($lower)",
            phoneticHintEn = "Uppercase $upper • Lowercase $lower",
            dotsDescriptionAr = "حرف كبير $upper وحرف صغير $lower",
            dotsDescriptionEn = "Capital $upper & small $lower",
            wordAr = wordAr,
            wordEn = wordEn,
            missingWordDisplay = missingWord,
            emoji = emoji,
            color = palette.first,
            shadowColor = palette.second,
            cardBgColor = palette.third,
            strokeDirectionHintAr = "ابدأ من الأعلى إلى الأسفل ومن اليسار إلى اليمين",
            strokeDirectionHintEn = "Start from the top and follow the strokes (1 → 4)",
            traceCheckpoints = englishStandardCheckpoints
        )
    }
}

data class LetterQuizQuestion(
    val id: Int,
    val promptAr: String,
    val promptEn: String,
    val spokenAr: String,
    val spokenEn: String,
    val visualEmoji: String,
    val visualCaption: String,
    val options: List<String>,
    val correctOption: String
)

object LettersQuizBank {

    val arabicQuestions: List<LetterQuizQuestion> = listOf(
        LetterQuizQuestion(
            id = 1,
            promptAr = "بأي حرف تبدأ كلمة (أسد) ؟",
            promptEn = "Which letter starts the word (أسد - Lion)?",
            spokenAr = "بأي حرف تبدأ كلمة أسد؟",
            spokenEn = "Which letter starts the word Asad?",
            visualEmoji = "🦁",
            visualCaption = "أسد",
            options = listOf("أ", "ب", "س"),
            correctOption = "أ"
        ),
        LetterQuizQuestion(
            id = 2,
            promptAr = "أي حرف تحته نقطة واحدة وتبدأ به كلمة (بطة) ؟",
            promptEn = "Which letter has one dot below and starts (بطة - Duck)?",
            spokenAr = "أي حرف تحته نقطة واحدة وتبدأ به كلمة بطة؟",
            spokenEn = "Which letter has one dot below for Batta?",
            visualEmoji = "🦆",
            visualCaption = "بطة",
            options = listOf("ب", "ج", "ر"),
            correctOption = "ب"
        ),
        LetterQuizQuestion(
            id = 3,
            promptAr = "ما الكلمة التي تبدأ بحرف التاء (ت) ؟",
            promptEn = "Which word begins with letter Taa (ت)?",
            spokenAr = "ما الكلمة التي تبدأ بحرف التاء؟",
            spokenEn = "Which word starts with letter Taa?",
            visualEmoji = "🍎",
            visualCaption = "حرف ت",
            options = listOf("تفاحة 🍎", "حصان 🐴", "سمكة 🐟"),
            correctOption = "تفاحة 🍎"
        ),
        LetterQuizQuestion(
            id = 4,
            promptAr = "بأي حرف تبدأ كلمة (شمس) ؟",
            promptEn = "Which letter starts the word (شمس - Sun)?",
            spokenAr = "بأي حرف تبدأ كلمة شمس؟",
            spokenEn = "Which letter starts the word Shams?",
            visualEmoji = "☀️",
            visualCaption = "شمس",
            options = listOf("ش", "ف", "م"),
            correctOption = "ش"
        ),
        LetterQuizQuestion(
            id = 5,
            promptAr = "ما الحرف الأخير في الحروف العربية وتبدأ به كلمة (يد) ؟",
            promptEn = "Which letter starts the word (يد - Hand)?",
            spokenAr = "ما الحرف الذي تبدأ به كلمة يد؟",
            spokenEn = "Which letter starts the word Yad?",
            visualEmoji = "✋",
            visualCaption = "يد",
            options = listOf("و", "ي", "ن"),
            correctOption = "ي"
        )
    )

    val englishQuestions: List<LetterQuizQuestion> = listOf(
        LetterQuizQuestion(
            id = 1,
            promptAr = "بأي حرف تبدأ كلمة Apple (تفاحة) ؟",
            promptEn = "Which letter does Apple start with?",
            spokenAr = "بأي حرف تبدأ كلمة Apple؟",
            spokenEn = "Which letter does Apple start with?",
            visualEmoji = "🍎",
            visualCaption = "Apple",
            options = listOf("A a", "B b", "C c"),
            correctOption = "A a"
        ),
        LetterQuizQuestion(
            id = 2,
            promptAr = "ما الحرف الصغير (Lowercase) للحرف الكبير B ؟",
            promptEn = "Which is the lowercase letter for capital B?",
            spokenAr = "ما الحرف الصغير للحرف الكبير B؟",
            spokenEn = "Which is the lowercase letter for capital B?",
            visualEmoji = "⚽",
            visualCaption = "B • Ball",
            options = listOf("b", "e", "k"),
            correctOption = "b"
        ),
        LetterQuizQuestion(
            id = 3,
            promptAr = "أي كلمة تبدأ بحرف C ؟",
            promptEn = "Which picture word starts with letter C?",
            spokenAr = "أي كلمة تبدأ بحرف C؟",
            spokenEn = "Which picture word starts with letter C?",
            visualEmoji = "🐱",
            visualCaption = "Letter C c",
            options = listOf("Cat 🐱", "Dog 🐶", "Sun ☀️"),
            correctOption = "Cat 🐱"
        ),
        LetterQuizQuestion(
            id = 4,
            promptAr = "بأي حرف تبدأ كلمة Lion (أسد) ؟",
            promptEn = "Which letter does Lion start with?",
            spokenAr = "بأي حرف تبدأ كلمة Lion؟",
            spokenEn = "Which letter does Lion start with?",
            visualEmoji = "🦁",
            visualCaption = "Lion",
            options = listOf("L l", "M m", "R r"),
            correctOption = "L l"
        ),
        LetterQuizQuestion(
            id = 5,
            promptAr = "بأي حرف تبدأ كلمة Zebra (حمار وحشي) ؟",
            promptEn = "Which letter does Zebra start with?",
            spokenAr = "بأي حرف تبدأ كلمة Zebra؟",
            spokenEn = "Which letter does Zebra start with?",
            visualEmoji = "🦓",
            visualCaption = "Zebra",
            options = listOf("X x", "Y y", "Z z"),
            correctOption = "Z z"
        )
    )

    fun questionsFor(section: LettersSectionMode): List<LetterQuizQuestion> =
        if (section == LettersSectionMode.ARABIC) arabicQuestions else englishQuestions
}
