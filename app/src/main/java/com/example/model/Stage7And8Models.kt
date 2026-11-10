package com.example.model

import androidx.compose.ui.graphics.Color
import com.example.R
import com.example.data.UserSettingsState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class RewardsTab(
    val titleAr: String,
    val titleEn: String,
    val emoji: String
) {
    STAR_BOARD("لوحة النجوم", "Star Board", "⭐"),
    BADGES("الإنجازات والشارات", "Badges", "🏅"),
    SURPRISE_BOX("صندوق المفاجآت", "Surprise Box", "🎁"),
    CERTIFICATES("الشهادات", "Certificates", "📜");

    fun title(lang: AppLanguage): String = if (lang == AppLanguage.AR) titleAr else titleEn
}

enum class EducationalLevel(
    val levelNumber: Int,
    val titleAr: String,
    val titleEn: String,
    val subtitleAr: String,
    val subtitleEn: String,
    val emoji: String,
    val badgeColor: Color,
    val minCompositePoints: Int,
    val luluMessageAr: String,
    val luluMessageEn: String
) {
    LITTLE_EXPLORER(
        levelNumber = 1,
        titleAr = "المستكشف الصغير",
        titleEn = "Little Explorer",
        subtitleAr = "بداية رحلة الاكتشاف والتعلّم المرح",
        subtitleEn = "Starting the joyful discovery journey",
        emoji = "🧭",
        badgeColor = Color(0xFF0288D1),
        minCompositePoints = 0,
        luluMessageAr = "أهلاً بك يا مستكشفنا الصغير! هيا نكتشف العوالم التعليمية معاً!",
        luluMessageEn = "Welcome Little Explorer! Let's discover the learning worlds together!"
    ),
    ACTIVE_LEARNER(
        levelNumber = 2,
        titleAr = "المتعلم النشيط",
        titleEn = "Active Learner",
        subtitleAr = "يتعلّم بنشاط ويجرب أنشطة متنوعة كل يوم",
        subtitleEn = "Actively learning and trying diverse activities",
        emoji = "🚀",
        badgeColor = Color(0xFF2E7D32),
        minCompositePoints = 18,
        luluMessageAr = "يا لك من متعلّم نشيط! نجومك تزداد وتضيء سماء المعرفة!",
        luluMessageEn = "What an Active Learner! Your stars are growing and lighting up the sky!"
    ),
    CREATIVE_STAR(
        levelNumber = 3,
        titleAr = "النجم المبدع",
        titleEn = "Creative Star",
        subtitleAr = "يبدع في الحروف والأرقام والألوان والرسم",
        subtitleEn = "Shining in letters, numbers, colors, and art",
        emoji = "🌟",
        badgeColor = Color(0xFFF57C00),
        minCompositePoints = 32,
        luluMessageAr = "أنت نجم مبدع حقاً! لولو فخورة جداً بمهاراتك الجميلة!",
        luluMessageEn = "You are truly a Creative Star! Lulu is so proud of your wonderful skills!"
    ),
    DISTINGUISHED_HERO(
        levelNumber = 4,
        titleAr = "البطل المتميز",
        titleEn = "Distinguished Hero",
        subtitleAr = "أتقن معظم الأنشطة والتحديات التعليمية بذكاء",
        subtitleEn = "Mastered most activities and challenges with skill",
        emoji = "🏆",
        badgeColor = Color(0xFF7B1FA2),
        minCompositePoints = 50,
        luluMessageAr = "ما شاء الله! أنت البطل المتميز في عالم المرح والتعلّم!",
        luluMessageEn = "Amazing! You are the Distinguished Hero of Fun Learning World!"
    ),
    LITTLE_GENIUS(
        levelNumber = 5,
        titleAr = "العبقري الصغير",
        titleEn = "Little Genius",
        subtitleAr = "قمة الإنجاز والإتقان في جميع العوالم التعليمية",
        subtitleEn = "Top mastery across all educational worlds",
        emoji = "👑",
        badgeColor = Color(0xFFD81B60),
        minCompositePoints = 70,
        luluMessageAr = "مبارك يا عبقريّنا الصغير! وصلت إلى أعلى مستوى في عالم المرح والتعلّم!",
        luluMessageEn = "Congratulations Little Genius! You reached the highest level in Fun Learning World!"
    );

    fun title(lang: AppLanguage): String = if (lang == AppLanguage.AR) titleAr else titleEn
    fun subtitle(lang: AppLanguage): String = if (lang == AppLanguage.AR) subtitleAr else subtitleEn
    fun luluMessage(lang: AppLanguage): String = if (lang == AppLanguage.AR) luluMessageAr else luluMessageEn
}

enum class AchievementBadge(
    val id: String,
    val titleAr: String,
    val titleEn: String,
    val descriptionAr: String,
    val descriptionEn: String,
    val requirementAr: String,
    val requirementEn: String,
    val luluCelebrateAr: String,
    val luluCelebrateEn: String,
    val emoji: String,
    val accentColor: Color,
    val cardBgColor: Color
) {
    NUMBERS_HERO(
        id = "badge_numbers_hero",
        titleAr = "بطل الأرقام",
        titleEn = "Numbers Hero",
        descriptionAr = "أتقن العدّ والأرقام من ١ إلى ١٠ بمهارة",
        descriptionEn = "Mastered counting and numbers 1 to 10",
        requirementAr = "أكمل ٣ أنشطة أو أكثر في عالم الأرقام",
        requirementEn = "Complete 3 or more activities in Numbers World",
        luluCelebrateAr = "رائع! لقد حصلت على شارة بطل الأرقام لأنك ماهر في العدّ!",
        luluCelebrateEn = "Awesome! You earned the Numbers Hero badge for great counting!",
        emoji = "🔢",
        accentColor = Color(0xFF0288D1),
        cardBgColor = Color(0xFFE1F5FE)
    ),
    ARABIC_LETTERS_STAR(
        id = "badge_arabic_letters_star",
        titleAr = "نجم الحروف العربية",
        titleEn = "Arabic Letters Star",
        descriptionAr = "تعرّف على الحروف العربية وأصواتها وكلماتها",
        descriptionEn = "Learned Arabic letters, sounds, and words",
        requirementAr = "تعلّم ٥ حروف عربية أو أكمل نشاطين في الحروف",
        requirementEn = "Learn 5 Arabic letters or complete 2 letter activities",
        luluCelebrateAr = "ممتاز! أنت الآن نجم الحروف العربية المتألق!",
        luluCelebrateEn = "Excellent! You are now a shining Arabic Letters Star!",
        emoji = "أ",
        accentColor = Color(0xFF2E7D32),
        cardBgColor = Color(0xFFE8F5E9)
    ),
    ENGLISH_LETTERS_STAR(
        id = "badge_english_letters_star",
        titleAr = "نجم الحروف الإنجليزية",
        titleEn = "English Letters Star",
        descriptionAr = "تعرّف على حروف الإنجليزية ABC وكلماتها",
        descriptionEn = "Learned English ABC letters and words",
        requirementAr = "تعلّم ٥ حروف إنجليزية أو أكمل نشاطين في الحروف",
        requirementEn = "Learn 5 English letters or complete 2 letter activities",
        luluCelebrateAr = "أحسنت! حصلت على شارة نجم الحروف الإنجليزية ABC!",
        luluCelebrateEn = "Well done! You unlocked the English Letters Star ABC badge!",
        emoji = "🔤",
        accentColor = Color(0xFF6A1B9A),
        cardBgColor = Color(0xFFF3E5F5)
    ),
    COLORS_EXPERT(
        id = "badge_colors_expert",
        titleAr = "خبير الألوان",
        titleEn = "Colors Expert",
        descriptionAr = "يميّز الألوان الأساسية ويدمجها ببراعة",
        descriptionEn = "Recognizes and mixes primary colors brilliantly",
        requirementAr = "أكمل نشاطين أو أكثر في عالم الألوان",
        requirementEn = "Complete 2 or more activities in Colors World",
        luluCelebrateAr = "يا لروعة الألوان! لقد أصبحت خبير الألوان المبدع!",
        luluCelebrateEn = "How colorful! You are now our creative Colors Expert!",
        emoji = "🎨",
        accentColor = Color(0xFFD81B60),
        cardBgColor = Color(0xFFFCE4EC)
    ),
    SHAPES_EXPLORER(
        id = "badge_shapes_explorer",
        titleAr = "مستكشف الأشكال",
        titleEn = "Shapes Explorer",
        descriptionAr = "يكتشف الأشكال الهندسية ويطابقها بذكاء",
        descriptionEn = "Discovers and matches geometric shapes smartly",
        requirementAr = "أكمل نشاطين أو أكثر في عالم الأشكال",
        requirementEn = "Complete 2 or more activities in Shapes World",
        luluCelebrateAr = "عمل رائع! حصلت على شارة مستكشف الأشكال الهندسية!",
        luluCelebrateEn = "Great job! You unlocked the Geometric Shapes Explorer badge!",
        emoji = "🔷",
        accentColor = Color(0xFFE65100),
        cardBgColor = Color(0xFFFFF3E0)
    ),
    ANIMALS_FRIEND(
        id = "badge_animals_friend",
        titleAr = "صديق الحيوانات",
        titleEn = "Animals Friend",
        descriptionAr = "يعرف الحيوانات وأصواتها وبيئاتها الجميلة",
        descriptionEn = "Knows cute animals, their sounds, and habitats",
        requirementAr = "أكمل نشاطين أو أكثر في عالم الحيوانات",
        requirementEn = "Complete 2 or more activities in Animals World",
        luluCelebrateAr = "يا صديق الحيوانات اللطيف! كل الحيوانات تحبك وتفرح معك!",
        luluCelebrateEn = "Kind Animals Friend! All the cute animals love playing with you!",
        emoji = "🦁",
        accentColor = Color(0xFF00796B),
        cardBgColor = Color(0xFFE0F2F1)
    ),
    MEMORY_HERO(
        id = "badge_memory_hero",
        titleAr = "بطل الذاكرة",
        titleEn = "Memory Hero",
        descriptionAr = "يتمتع بذاكرة قوية وتركيز عالٍ في الألغاز",
        descriptionEn = "Shows strong memory and focus in brain puzzles",
        requirementAr = "أكمل نشاطين أو أكثر في ألعاب الذكاء والذاكرة",
        requirementEn = "Complete 2 or more games in Brain & Memory World",
        luluCelebrateAr = "ذاكرتك قوية جداً! مبارك حصولك على شارة بطل الذاكرة!",
        luluCelebrateEn = "Your memory is super sharp! Congrats on the Memory Hero badge!",
        emoji = "🧠",
        accentColor = Color(0xFF1565C0),
        cardBgColor = Color(0xFFE3F2FD)
    ),
    LITTLE_ARTIST(
        id = "badge_little_artist",
        titleAr = "الفنان الصغير",
        titleEn = "Little Artist",
        descriptionAr = "يرسم ويلون ويكتب الحروف والأرقام بإبداع",
        descriptionEn = "Draws, colors, and traces letters & numbers creatively",
        requirementAr = "أكمل نشاطين في الكتابة والرسم أو احفظ لوحة فنية",
        requirementEn = "Complete 2 Writing & Drawing activities or save an artwork",
        luluCelebrateAr = "ريشتك ساحرة! أنت الفنان الصغير في عالم المرح والتعلّم!",
        luluCelebrateEn = "Your brush is magical! You are the Little Artist of Fun Learning World!",
        emoji = "🖌️",
        accentColor = Color(0xFFAD1457),
        cardBgColor = Color(0xFFFCE4EC)
    ),
    LITTLE_GENIUS(
        id = "badge_little_genius",
        titleAr = "العبقري الصغير",
        titleEn = "Little Genius",
        descriptionAr = "يحل الأنماط والمتاهات والتحديات الذكية",
        descriptionEn = "Solves patterns, mazes, and smart challenges",
        requirementAr = "أكمل ٤ ألعاب ذكاء أو صل إلى المستوى ٢ في الذكاء",
        requirementEn = "Complete 4 brain games or reach Level 2 in Brain World",
        luluCelebrateAr = "ذكاء خارق! لقد فزت بشارة العبقري الصغير عن جدارة!",
        luluCelebrateEn = "Super smart! You truly earned the Little Genius badge!",
        emoji = "💡",
        accentColor = Color(0xFFF57F17),
        cardBgColor = Color(0xFFFFFDE7)
    ),
    LEARNING_CHAMPION(
        id = "badge_learning_champion",
        titleAr = "بطل التعلم",
        titleEn = "Learning Champion",
        descriptionAr = "استكشف العوالم التعليمية وجمع نجوم التميز",
        descriptionEn = "Explored learning worlds and collected excellence stars",
        requirementAr = "اجمع ٢٠ نجمة أو استكشف ٥ عوالم تعليمية مختلفة",
        requirementEn = "Collect 20 stars or explore 5 different learning worlds",
        luluCelebrateAr = "مبارك يا بطل التعلّم! أنت قدوة رائعة في حب المعرفة!",
        luluCelebrateEn = "Congratulations Learning Champion! You are a shining star of knowledge!",
        emoji = "🏆",
        accentColor = Color(0xFFC2185B),
        cardBgColor = Color(0xFFFFF8E1)
    );

    fun title(lang: AppLanguage): String = if (lang == AppLanguage.AR) titleAr else titleEn
    fun description(lang: AppLanguage): String = if (lang == AppLanguage.AR) descriptionAr else descriptionEn
    fun requirement(lang: AppLanguage): String = if (lang == AppLanguage.AR) requirementAr else requirementEn
    fun luluCelebrate(lang: AppLanguage): String = if (lang == AppLanguage.AR) luluCelebrateAr else luluCelebrateEn

    fun isUnlocked(settings: UserSettingsState): Boolean = when (this) {
        NUMBERS_HERO -> settings.completedNumbersActivities.size >= 3 || (settings.categoryStars[CategoryId.NUMBERS] ?: 0) >= 3
        ARABIC_LETTERS_STAR -> settings.learnedArabicLetters.size >= 5 || settings.completedLettersActivities.size >= 2
        ENGLISH_LETTERS_STAR -> settings.learnedEnglishLetters.size >= 5 || settings.completedLettersActivities.size >= 2
        COLORS_EXPERT -> settings.completedColorsActivities.size >= 2 || (settings.categoryStars[CategoryId.COLORS] ?: 0) >= 3
        SHAPES_EXPLORER -> settings.completedShapesActivities.size >= 2 || (settings.categoryStars[CategoryId.SHAPES] ?: 0) >= 3
        ANIMALS_FRIEND -> settings.completedAnimalsActivities.size >= 2 || (settings.categoryStars[CategoryId.ANIMALS] ?: 0) >= 3
        MEMORY_HERO -> settings.completedBrainActivities.size >= 2
        LITTLE_ARTIST -> settings.completedWritingActivities.size >= 2 || settings.savedDrawingsTitles.isNotEmpty() || settings.practicedWritingItems.size >= 4
        LITTLE_GENIUS -> settings.completedBrainActivities.size >= 4 || settings.brainMaxLevelReached >= 2
        LEARNING_CHAMPION -> settings.totalEarnedStars >= 20 || RewardsProgressCalculator.exploredWorldsCount(settings) >= 5
    }
}

enum class SurpriseRewardItem(
    val id: String,
    val titleAr: String,
    val titleEn: String,
    val subtitleAr: String,
    val subtitleEn: String,
    val emoji: String,
    val bonusStars: Int,
    val isLuluAccessory: Boolean,
    val requiredCompletedActivities: Int,
    val accentColor: Color
) {
    GOLDEN_CROWN(
        id = "surprise_golden_crown",
        titleAr = "تاج لولو الذهبي",
        titleEn = "Lulu's Golden Crown",
        subtitleAr = "إكسسوار ملكي للولو + نجمتان إضافيتان",
        subtitleEn = "Royal crown for Lulu + 2 bonus stars",
        emoji = "👑",
        bonusStars = 2,
        isLuluAccessory = true,
        requiredCompletedActivities = 1,
        accentColor = Color(0xFFF57C00)
    ),
    STAR_GLASSES(
        id = "surprise_star_glasses",
        titleAr = "نظارة النجوم المرحة",
        titleEn = "Fun Starry Glasses",
        subtitleAr = "نظارة كرتونية للولو + نجمتان إضافيتان",
        subtitleEn = "Cartoon glasses for Lulu + 2 bonus stars",
        emoji = "🕶️",
        bonusStars = 2,
        isLuluAccessory = true,
        requiredCompletedActivities = 3,
        accentColor = Color(0xFF0288D1)
    ),
    RAINBOW_STICKER(
        id = "surprise_rainbow_sticker",
        titleAr = "ملصق قوس قزح السحري",
        titleEn = "Magic Rainbow Sticker",
        subtitleAr = "ملصق تشجيعي ملوّن + نجمتان إضافيتان",
        subtitleEn = "Colorful reward sticker + 2 bonus stars",
        emoji = "🌈",
        bonusStars = 2,
        isLuluAccessory = false,
        requiredCompletedActivities = 5,
        accentColor = Color(0xFFD81B60)
    ),
    ROYAL_CAPE(
        id = "surprise_royal_cape",
        titleAr = "وشاح الأبطال المميز",
        titleEn = "Hero's Super Cape",
        subtitleAr = "وشاح الأبطال للولو + ٣ نجوم إضافية",
        subtitleEn = "Hero cape for Lulu + 3 bonus stars",
        emoji = "🦸‍♀️",
        bonusStars = 3,
        isLuluAccessory = true,
        requiredCompletedActivities = 8,
        accentColor = Color(0xFF7B1FA2)
    ),
    MAGIC_WAND(
        id = "surprise_magic_wand",
        titleAr = "عصا الإبداع المضيئة",
        titleEn = "Glowing Creative Wand",
        subtitleAr = "عصا النجوم للولو + ٣ نجوم إضافية",
        subtitleEn = "Star wand for Lulu + 3 bonus stars",
        emoji = "🪄",
        bonusStars = 3,
        isLuluAccessory = true,
        requiredCompletedActivities = 12,
        accentColor = Color(0xFF2E7D32)
    ),
    GALAXY_THEME(
        id = "surprise_galaxy_theme",
        titleAr = "وسام مجرة الأبطال",
        titleEn = "Hero Galaxy Medallion",
        subtitleAr = "هدية كبرى للأبطال المتميزين + ٤ نجوم",
        subtitleEn = "Grand gift for super learners + 4 bonus stars",
        emoji = "🪐",
        bonusStars = 4,
        isLuluAccessory = false,
        requiredCompletedActivities = 16,
        accentColor = Color(0xFF1565C0)
    );

    fun title(lang: AppLanguage): String = if (lang == AppLanguage.AR) titleAr else titleEn
    fun subtitle(lang: AppLanguage): String = if (lang == AppLanguage.AR) subtitleAr else subtitleEn
}

enum class WorldCertificateType(
    val id: String,
    val titleAr: String,
    val titleEn: String,
    val worldNameAr: String,
    val worldNameEn: String,
    val messageAr: String,
    val messageEn: String,
    val requirementAr: String,
    val requirementEn: String,
    val emoji: String,
    val accentColor: Color,
    val borderColor: Color,
    val bannerDrawableRes: Int
) {
    NUMBERS_CERT(
        id = "cert_numbers",
        titleAr = "شهادة إتمام عالم الأرقام",
        titleEn = "Numbers World Completion Certificate",
        worldNameAr = "عالم الأرقام والعدّ (١ - ١٠)",
        worldNameEn = "Numbers & Counting World (1 - 10)",
        messageAr = "تُمنح هذه الشهادة تقديراً لإتقان الأرقام والعدّ والجمع البسيط بنشاط وذكاء!",
        messageEn = "Awarded in recognition of mastering numbers, counting, and simple addition with joy!",
        requirementAr = "أكمل ٣ أنشطة في عالم الأرقام لفتح الشهادة",
        requirementEn = "Complete 3 activities in Numbers World to unlock",
        emoji = "🔢",
        accentColor = Color(0xFF0288D1),
        borderColor = Color(0xFF4FC3F7),
        bannerDrawableRes = R.drawable.img_card_numbers_letters_1794072209030
    ),
    ARABIC_LETTERS_CERT(
        id = "cert_arabic_letters",
        titleAr = "شهادة إتمام الحروف العربية",
        titleEn = "Arabic Letters Completion Certificate",
        worldNameAr = "عالم الحروف العربية (أ - ي)",
        worldNameEn = "Arabic Alphabet World (أ - ي)",
        messageAr = "تُمنح هذه الشهادة لتعلّم الحروف العربية وأصواتها وكلماتها الجميلة بتفوق!",
        messageEn = "Awarded for learning the Arabic alphabet, phonics, and vocabulary with excellence!",
        requirementAr = "تعلّم ٦ حروف عربية أو أكمل ٣ أنشطة في الحروف",
        requirementEn = "Learn 6 Arabic letters or complete 3 letter activities",
        emoji = "أ",
        accentColor = Color(0xFF2E7D32),
        borderColor = Color(0xFF81C784),
        bannerDrawableRes = R.drawable.img_cat_arabic_letters_1794073876696
    ),
    ENGLISH_LETTERS_CERT(
        id = "cert_english_letters",
        titleAr = "شهادة إتمام الحروف الإنجليزية",
        titleEn = "English Letters Completion Certificate",
        worldNameAr = "عالم الحروف الإنجليزية (A - Z)",
        worldNameEn = "English Alphabet World (A - Z)",
        messageAr = "تُمنح هذه الشهادة لإتقان حروف اللغة الإنجليزية ونطقها الصحيح بتميّز!",
        messageEn = "Awarded for mastering English ABC letters, sounds, and words brilliantly!",
        requirementAr = "تعلّم ٦ حروف إنجليزية أو أكمل ٣ أنشطة في الحروف",
        requirementEn = "Learn 6 English letters or complete 3 letter activities",
        emoji = "🔤",
        accentColor = Color(0xFF6A1B9A),
        borderColor = Color(0xFFBA68C8),
        bannerDrawableRes = R.drawable.img_cat_english_letters_1794073878108
    ),
    COLORS_SHAPES_CERT(
        id = "cert_colors_shapes",
        titleAr = "شهادة إتمام عالم الألوان والأشكال",
        titleEn = "Colors & Shapes Completion Certificate",
        worldNameAr = "عالم الألوان الزاهية والأشكال الهندسية",
        worldNameEn = "Vibrant Colors & Geometric Shapes World",
        messageAr = "تُمنح هذه الشهادة لاكتشاف الألوان ودمجها والتمييز بين الأشكال الهندسية ببراعة!",
        messageEn = "Awarded for exploring colors, mixing paints, and matching geometric shapes!",
        requirementAr = "أكمل ٣ أنشطة في عالمي الألوان والأشكال معاً",
        requirementEn = "Complete 3 activities across Colors & Shapes Worlds",
        emoji = "🎨",
        accentColor = Color(0xFFD81B60),
        borderColor = Color(0xFFF06292),
        bannerDrawableRes = R.drawable.img_cat_colors_1794073880642
    ),
    ANIMALS_CERT(
        id = "cert_animals",
        titleAr = "شهادة إتمام عالم الحيوانات",
        titleEn = "Animals World Completion Certificate",
        worldNameAr = "عالم الحيوانات وأصواتها وبيئاتها",
        worldNameEn = "Animals, Sounds & Habitats World",
        messageAr = "تُمنح هذه الشهادة لصديق الحيوانات الذكي الذي تعرّف على الحيوانات وبيئاتها وأصواتها!",
        messageEn = "Awarded to our smart Animals Friend for learning animal names, sounds, and habitats!",
        requirementAr = "أكمل نشاطين أو أكثر في عالم الحيوانات",
        requirementEn = "Complete 2 or more activities in Animals World",
        emoji = "🦁",
        accentColor = Color(0xFF00796B),
        borderColor = Color(0xFF4DB6AC),
        bannerDrawableRes = R.drawable.img_card_animals_nature_1794072210949
    ),
    BRAIN_HERO_CERT(
        id = "cert_brain_hero",
        titleAr = "شهادة بطل ألعاب الذكاء",
        titleEn = "Brain Games Hero Certificate",
        worldNameAr = "عالم ألعاب الذكاء والذاكرة والتركيز",
        worldNameEn = "Brain, Memory & Logic Games World",
        messageAr = "تُمنح هذه الشهادة لبطل التفكير الذكي والذاكرة القوية وحل الألغاز والمتاهات!",
        messageEn = "Awarded to our Brain Games Hero for sharp memory, focus, and puzzle-solving!",
        requirementAr = "أكمل ٣ أنشطة في عالم ألعاب الذكاء والذاكرة",
        requirementEn = "Complete 3 activities in Brain & Memory World",
        emoji = "🧠",
        accentColor = Color(0xFF1565C0),
        borderColor = Color(0xFF64B5F6),
        bannerDrawableRes = R.drawable.img_cat_brain_games_1794073881867
    ),
    LITTLE_ARTIST_CERT(
        id = "cert_little_artist",
        titleAr = "شهادة الفنان الصغير",
        titleEn = "Little Artist Certificate",
        worldNameAr = "عالم الكتابة والرسم والتلوين التفاعلي",
        worldNameEn = "Interactive Writing, Drawing & Coloring World",
        messageAr = "تُمنح هذه الشهادة للفنان الصغير المبدع في تتبّع الحروف والأرقام والرسم والتلوين!",
        messageEn = "Awarded to our creative Little Artist for tracing letters, numbers, drawing, and coloring!",
        requirementAr = "أكمل ٣ أنشطة في عالم الكتابة والرسم أو احفظ لوحة فنية",
        requirementEn = "Complete 3 Writing & Drawing activities or save an artwork",
        emoji = "🖌️",
        accentColor = Color(0xFFAD1457),
        borderColor = Color(0xFFF48FB1),
        bannerDrawableRes = R.drawable.img_card_art_shapes_1794072210824
    );

    fun title(lang: AppLanguage): String = if (lang == AppLanguage.AR) titleAr else titleEn
    fun worldName(lang: AppLanguage): String = if (lang == AppLanguage.AR) worldNameAr else worldNameEn
    fun message(lang: AppLanguage): String = if (lang == AppLanguage.AR) messageAr else messageEn
    fun requirement(lang: AppLanguage): String = if (lang == AppLanguage.AR) requirementAr else requirementEn

    fun isUnlocked(settings: UserSettingsState): Boolean = when (this) {
        NUMBERS_CERT -> settings.completedNumbersActivities.size >= 3 || (settings.categoryStars[CategoryId.NUMBERS] ?: 0) >= 3
        ARABIC_LETTERS_CERT -> settings.learnedArabicLetters.size >= 6 || settings.completedLettersActivities.size >= 3
        ENGLISH_LETTERS_CERT -> settings.learnedEnglishLetters.size >= 6 || settings.completedLettersActivities.size >= 3
        COLORS_SHAPES_CERT -> (settings.completedColorsActivities.size + settings.completedShapesActivities.size) >= 3
        ANIMALS_CERT -> settings.completedAnimalsActivities.size >= 2 || (settings.categoryStars[CategoryId.ANIMALS] ?: 0) >= 3
        BRAIN_HERO_CERT -> settings.completedBrainActivities.size >= 3
        LITTLE_ARTIST_CERT -> settings.completedWritingActivities.size >= 3 || settings.savedDrawingsTitles.isNotEmpty() || settings.practicedWritingItems.size >= 5
    }
}

object RewardsProgressCalculator {

    fun totalCompletedActivitiesCount(settings: UserSettingsState): Int =
        settings.completedNumbersActivities.size +
            settings.completedLettersActivities.size +
            settings.completedColorsActivities.size +
            settings.completedShapesActivities.size +
            settings.completedAnimalsActivities.size +
            settings.completedBrainActivities.size +
            settings.completedWritingActivities.size

    fun maxActivitiesCount(): Int =
        NumbersActivityId.entries.size +
            LettersActivityId.entries.size +
            ColorsActivityId.entries.size +
            ShapesActivityId.entries.size +
            AnimalsActivityId.entries.size +
            BrainGameActivityId.entries.size +
            WritingDrawingActivityId.entries.size

    fun exploredWorldsCount(settings: UserSettingsState): Int {
        var count = 0
        if (settings.completedNumbersActivities.isNotEmpty() || (settings.categoryStars[CategoryId.NUMBERS] ?: 0) > 1) count++
        if (settings.learnedArabicLetters.isNotEmpty() || settings.completedLettersActivities.isNotEmpty()) count++
        if (settings.learnedEnglishLetters.isNotEmpty() || settings.completedLettersActivities.isNotEmpty()) count++
        if (settings.completedColorsActivities.isNotEmpty() || (settings.categoryStars[CategoryId.COLORS] ?: 0) > 1) count++
        if (settings.completedShapesActivities.isNotEmpty() || (settings.categoryStars[CategoryId.SHAPES] ?: 0) > 1) count++
        if (settings.completedAnimalsActivities.isNotEmpty() || (settings.categoryStars[CategoryId.ANIMALS] ?: 0) > 1) count++
        if (settings.completedBrainActivities.isNotEmpty() || (settings.categoryStars[CategoryId.BRAIN_GAMES] ?: 0) > 1) count++
        if (settings.completedWritingActivities.isNotEmpty() || settings.practicedWritingItems.isNotEmpty() || settings.savedDrawingsTitles.isNotEmpty()) count++
        return count.coerceIn(0, 8)
    }

    /**
     * Composite score based on:
     * 1. Total stars earned (including surprise bonus stars)
     * 2. Completed activities count (weighted x2)
     * 3. Diversity of worlds explored (weighted x3)
     */
    fun computeCompositeScore(settings: UserSettingsState): Int {
        val starsScore = settings.totalEarnedStars
        val activitiesScore = totalCompletedActivitiesCount(settings) * 2
        val diversityScore = exploredWorldsCount(settings) * 3
        return starsScore + activitiesScore + diversityScore
    }

    fun computeCurrentLevel(settings: UserSettingsState): EducationalLevel {
        val score = computeCompositeScore(settings)
        return EducationalLevel.entries
            .sortedByDescending { it.minCompositePoints }
            .firstOrNull { score >= it.minCompositePoints }
            ?: EducationalLevel.LITTLE_EXPLORER
    }

    fun computeNextLevelProgress(settings: UserSettingsState): Pair<EducationalLevel?, Float> {
        val current = computeCurrentLevel(settings)
        val next = EducationalLevel.entries.firstOrNull { it.levelNumber == current.levelNumber + 1 }
            ?: return Pair(null, 1f)
        val score = computeCompositeScore(settings)
        val span = (next.minCompositePoints - current.minCompositePoints).coerceAtLeast(1)
        val fraction = ((score - current.minCompositePoints).toFloat() / span).coerceIn(0.05f, 1f)
        return Pair(next, fraction)
    }

    fun unlockedBadges(settings: UserSettingsState): List<AchievementBadge> =
        AchievementBadge.entries.filter { it.isUnlocked(settings) }

    fun unlockedCertificates(settings: UserSettingsState): List<WorldCertificateType> =
        WorldCertificateType.entries.filter { it.isUnlocked(settings) }

    fun availableSurpriseBoxesToOpen(settings: UserSettingsState): List<SurpriseRewardItem> {
        val completedCount = totalCompletedActivitiesCount(settings)
        return SurpriseRewardItem.entries.filter { item ->
            completedCount >= item.requiredCompletedActivities && item.id !in settings.openedSurpriseIds
        }
    }
}

// =====================================================================
// STAGE 8: PARENT DASHBOARD MODELS & ANALYTICS ENGINE
// =====================================================================

enum class ParentDashboardTab(
    val titleAr: String,
    val titleEn: String,
    val emoji: String
) {
    OVERVIEW("نظرة عامة", "Overview", "📊"),
    WORLD_PROGRESS("تقدم العوالم", "Worlds", "🌍"),
    ACTIVITY_LOG("سجل الأنشطة", "Activity Log", "📋"),
    INSIGHTS("القوة والتوصيات", "Insights", "💡"),
    SETTINGS("الإعدادات والتقرير", "Settings & PDF", "⚙️");

    fun title(lang: AppLanguage): String = if (lang == AppLanguage.AR) titleAr else titleEn
}

data class ActivityLogEntry(
    val id: String,
    val worldCategory: CategoryId,
    val activityTitleAr: String,
    val activityTitleEn: String,
    val formattedDate: String,
    val starsEarned: Int,
    val timestampMillis: Long
) {
    fun activityTitle(lang: AppLanguage): String =
        if (lang == AppLanguage.AR) activityTitleAr else activityTitleEn

    fun serialize(): String {
        val safeAr = activityTitleAr.replace("|", " ")
        val safeEn = activityTitleEn.replace("|", " ")
        return "$id|${worldCategory.key}|$safeAr|$safeEn|$formattedDate|$starsEarned|$timestampMillis"
    }

    companion object {
        fun deserialize(raw: String): ActivityLogEntry? {
            val parts = raw.split("|")
            if (parts.size < 7) return null
            val category = CategoryId.entries.firstOrNull { it.key == parts[1] } ?: CategoryId.NUMBERS
            val stars = parts[5].toIntOrNull() ?: 1
            val ts = parts[6].toLongOrNull() ?: 0L
            return ActivityLogEntry(
                id = parts[0],
                worldCategory = category,
                activityTitleAr = parts[2],
                activityTitleEn = parts[3],
                formattedDate = parts[4],
                starsEarned = stars,
                timestampMillis = ts
            )
        }

        fun createNow(
            id: String,
            worldCategory: CategoryId,
            titleAr: String,
            titleEn: String,
            starsEarned: Int = 1
        ): ActivityLogEntry {
            val now = System.currentTimeMillis()
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US)
            return ActivityLogEntry(
                id = id,
                worldCategory = worldCategory,
                activityTitleAr = titleAr,
                activityTitleEn = titleEn,
                formattedDate = sdf.format(Date(now)),
                starsEarned = starsEarned,
                timestampMillis = now
            )
        }
    }
}

data class WorldDetailedProgress(
    val categoryId: CategoryId,
    val titleAr: String,
    val titleEn: String,
    val emoji: String,
    val progressFraction: Float,
    val completedCount: Int,
    val totalCount: Int,
    val summaryMetricsAr: List<String>,
    val summaryMetricsEn: List<String>,
    val needsPracticeItemsAr: List<String>,
    val needsPracticeItemsEn: List<String>,
    val accentColor: Color
) {
    fun title(lang: AppLanguage): String = if (lang == AppLanguage.AR) titleAr else titleEn
    fun summaryMetrics(lang: AppLanguage): List<String> = if (lang == AppLanguage.AR) summaryMetricsAr else summaryMetricsEn
    fun needsPracticeItems(lang: AppLanguage): List<String> = if (lang == AppLanguage.AR) needsPracticeItemsAr else needsPracticeItemsEn
}

data class SmartParentRecommendation(
    val id: String,
    val titleAr: String,
    val titleEn: String,
    val reasonAr: String,
    val reasonEn: String,
    val parentTipAr: String,
    val parentTipEn: String,
    val targetCategory: CategoryId,
    val emoji: String,
    val accentColor: Color
) {
    fun title(lang: AppLanguage): String = if (lang == AppLanguage.AR) titleAr else titleEn
    fun reason(lang: AppLanguage): String = if (lang == AppLanguage.AR) reasonAr else reasonEn
    fun parentTip(lang: AppLanguage): String = if (lang == AppLanguage.AR) parentTipAr else parentTipEn
}

data class ParentInsightsReport(
    val strengthsAr: List<String>,
    val strengthsEn: List<String>,
    val areasToPracticeAr: List<String>,
    val areasToPracticeEn: List<String>,
    val parentTipsAr: List<String>,
    val parentTipsEn: List<String>,
    val recommendations: List<SmartParentRecommendation>
) {
    fun strengths(lang: AppLanguage): List<String> = if (lang == AppLanguage.AR) strengthsAr else strengthsEn
    fun areasToPractice(lang: AppLanguage): List<String> = if (lang == AppLanguage.AR) areasToPracticeAr else areasToPracticeEn
    fun parentTips(lang: AppLanguage): List<String> = if (lang == AppLanguage.AR) parentTipsAr else parentTipsEn
}

object ParentAnalyticsEngine {

    fun buildWorldProgressList(settings: UserSettingsState): List<WorldDetailedProgress> {
        val numbersLearned = when {
            settings.completedNumbersActivities.size >= 5 -> 10
            settings.completedNumbersActivities.size >= 3 -> 7
            settings.completedNumbersActivities.isNotEmpty() -> 4
            else -> 2
        }
        val unlearnedNumbersAr = if (numbersLearned < 10) {
            listOf("التدرب على الأرقام من ${numbersLearned + 1} إلى ١٠ والجمع البسيط")
        } else {
            emptyList()
        }
        val unlearnedNumbersEn = if (numbersLearned < 10) {
            listOf("Practice numbers ${numbersLearned + 1} to 10 and simple addition")
        } else {
            emptyList()
        }

        val allArabicLetters = LettersCurriculum.arabicLetters
        val unlearnedArabic = allArabicLetters
            .filter { it.id !in settings.learnedArabicLetters }
            .take(5)
            .map { "${it.isolatedSymbol} (${it.letterNameAr})" }

        val allEnglishLetters = LettersCurriculum.englishLetters
        val unlearnedEnglish = allEnglishLetters
            .filter { it.id !in settings.learnedEnglishLetters }
            .take(5)
            .map { "${it.uppercaseSymbol} (${it.wordEn})" }

        return listOf(
            WorldDetailedProgress(
                categoryId = CategoryId.NUMBERS,
                titleAr = "عالم الأرقام والعدّ",
                titleEn = "Numbers & Counting World",
                emoji = "🔢",
                progressFraction = settings.numbersWorldProgressFraction,
                completedCount = settings.completedNumbersActivities.size,
                totalCount = NumbersActivityId.entries.size,
                summaryMetricsAr = listOf(
                    "الأرقام المتقنة: $numbersLearned من ١٠ أرقام",
                    "الأنشطة المكتملة: ${settings.completedNumbersActivities.size} من ${NumbersActivityId.entries.size}",
                    "النجوم المكتسبة: ${settings.categoryStars[CategoryId.NUMBERS] ?: 1} / ٣"
                ),
                summaryMetricsEn = listOf(
                    "Mastered Numbers: $numbersLearned of 10 numbers",
                    "Completed Activities: ${settings.completedNumbersActivities.size} of ${NumbersActivityId.entries.size}",
                    "Category Stars: ${settings.categoryStars[CategoryId.NUMBERS] ?: 1} / 3"
                ),
                needsPracticeItemsAr = unlearnedNumbersAr,
                needsPracticeItemsEn = unlearnedNumbersEn,
                accentColor = Color(0xFF0288D1)
            ),
            WorldDetailedProgress(
                categoryId = CategoryId.ARABIC_LETTERS,
                titleAr = "عالم الحروف العربية",
                titleEn = "Arabic Letters World",
                emoji = "أ",
                progressFraction = ((settings.learnedArabicLetters.size.toFloat() / 28f) * 0.5f + settings.lettersWorldProgressFraction * 0.5f).coerceIn(0f, 1f),
                completedCount = settings.learnedArabicLetters.size,
                totalCount = 28,
                summaryMetricsAr = listOf(
                    "الحروف العربية المتعلمة: ${settings.learnedArabicLetters.size} من ٢٨ حرفاً",
                    "أنشطة الحروف المكتملة: ${settings.completedLettersActivities.size} من ${LettersActivityId.entries.size}"
                ),
                summaryMetricsEn = listOf(
                    "Learned Arabic Letters: ${settings.learnedArabicLetters.size} of 28 letters",
                    "Completed Letter Games: ${settings.completedLettersActivities.size} of ${LettersActivityId.entries.size}"
                ),
                needsPracticeItemsAr = if (unlearnedArabic.isNotEmpty()) listOf("حروف مقترحة للتدريب: ${unlearnedArabic.joinToString(" ، ")}") else emptyList(),
                needsPracticeItemsEn = if (unlearnedArabic.isNotEmpty()) listOf("Suggested Arabic letters to practice: ${unlearnedArabic.joinToString(", ")}") else emptyList(),
                accentColor = Color(0xFF2E7D32)
            ),
            WorldDetailedProgress(
                categoryId = CategoryId.ENGLISH_LETTERS,
                titleAr = "عالم الحروف الإنجليزية",
                titleEn = "English Letters World",
                emoji = "🔤",
                progressFraction = ((settings.learnedEnglishLetters.size.toFloat() / 26f) * 0.5f + settings.lettersWorldProgressFraction * 0.5f).coerceIn(0f, 1f),
                completedCount = settings.learnedEnglishLetters.size,
                totalCount = 26,
                summaryMetricsAr = listOf(
                    "الحروف الإنجليزية المتعلمة: ${settings.learnedEnglishLetters.size} من ٢٦ حرفاً",
                    "أنشطة المطابقة والصوتيات: ${settings.completedLettersActivities.size} من ${LettersActivityId.entries.size}"
                ),
                summaryMetricsEn = listOf(
                    "Learned English Letters: ${settings.learnedEnglishLetters.size} of 26 letters",
                    "Phonics & Matching Games: ${settings.completedLettersActivities.size} of ${LettersActivityId.entries.size}"
                ),
                needsPracticeItemsAr = if (unlearnedEnglish.isNotEmpty()) listOf("حروف إنجليزية للتدريب: ${unlearnedEnglish.joinToString(" ، ")}") else emptyList(),
                needsPracticeItemsEn = if (unlearnedEnglish.isNotEmpty()) listOf("Suggested English letters: ${unlearnedEnglish.joinToString(", ")}") else emptyList(),
                accentColor = Color(0xFF6A1B9A)
            ),
            WorldDetailedProgress(
                categoryId = CategoryId.COLORS,
                titleAr = "عالم الألوان والأشكال",
                titleEn = "Colors & Shapes World",
                emoji = "🎨",
                progressFraction = ((settings.colorsWorldProgressFraction + settings.shapesWorldProgressFraction) / 2f).coerceIn(0f, 1f),
                completedCount = settings.completedColorsActivities.size + settings.completedShapesActivities.size,
                totalCount = ColorsActivityId.entries.size + ShapesActivityId.entries.size,
                summaryMetricsAr = listOf(
                    "أنشطة الألوان المكتملة: ${settings.completedColorsActivities.size} من ${ColorsActivityId.entries.size} (١٠ ألوان أساسية)",
                    "أنشطة الأشكال الهندسية: ${settings.completedShapesActivities.size} من ${ShapesActivityId.entries.size} (٨ أشكال)"
                ),
                summaryMetricsEn = listOf(
                    "Colors Activities: ${settings.completedColorsActivities.size} of ${ColorsActivityId.entries.size} (10 core colors)",
                    "Shapes Activities: ${settings.completedShapesActivities.size} of ${ShapesActivityId.entries.size} (8 shapes)"
                ),
                needsPracticeItemsAr = buildList {
                    if (settings.completedColorsActivities.size < ColorsActivityId.entries.size) add("تجربة نشاط دمج الألوان والفرز اللوني")
                    if (settings.completedShapesActivities.size < ShapesActivityId.entries.size) add("تدريب إضافي على مطابقة الأشكال وتكوين الصور")
                },
                needsPracticeItemsEn = buildList {
                    if (settings.completedColorsActivities.size < ColorsActivityId.entries.size) add("Try Color Mixing and Color Sorting activities")
                    if (settings.completedShapesActivities.size < ShapesActivityId.entries.size) add("Practice Shape Matching and Build with Shapes")
                },
                accentColor = Color(0xFFD81B60)
            ),
            WorldDetailedProgress(
                categoryId = CategoryId.ANIMALS,
                titleAr = "عالم الحيوانات والطبيعة",
                titleEn = "Animals & Nature World",
                emoji = "🦁",
                progressFraction = settings.animalsWorldProgressFraction,
                completedCount = settings.completedAnimalsActivities.size,
                totalCount = AnimalsActivityId.entries.size,
                summaryMetricsAr = listOf(
                    "الحيوانات والكلمات المتعلمة: ١٢ حيواناً مع أصواتها وبيئاتها",
                    "الأنشطة المكتملة: ${settings.completedAnimalsActivities.size} من ${AnimalsActivityId.entries.size}"
                ),
                summaryMetricsEn = listOf(
                    "Animals & Words Explored: 12 animals with sounds & habitats",
                    "Completed Activities: ${settings.completedAnimalsActivities.size} of ${AnimalsActivityId.entries.size}"
                ),
                needsPracticeItemsAr = if (settings.completedAnimalsActivities.size < AnimalsActivityId.entries.size) {
                    listOf("استكشاف بيئات الحيوانات (المزرعة، الغابة، البحر)")
                } else emptyList(),
                needsPracticeItemsEn = if (settings.completedAnimalsActivities.size < AnimalsActivityId.entries.size) {
                    listOf("Explore Animal Habitats (Farm, Jungle, Ocean)")
                } else emptyList(),
                accentColor = Color(0xFF00796B)
            ),
            WorldDetailedProgress(
                categoryId = CategoryId.BRAIN_GAMES,
                titleAr = "عالم ألعاب الذكاء والذاكرة",
                titleEn = "Brain & Memory Games World",
                emoji = "🧠",
                progressFraction = settings.brainWorldProgressFraction,
                completedCount = settings.completedBrainActivities.size,
                totalCount = BrainGameActivityId.entries.size,
                summaryMetricsAr = listOf(
                    "الألعاب المجربة والمكتملة: ${settings.completedBrainActivities.size} من ${BrainGameActivityId.entries.size}",
                    "أعلى مستوى أداء: المستوى ${settings.brainMaxLevelReached} من ٣"
                ),
                summaryMetricsEn = listOf(
                    "Completed Brain Games: ${settings.completedBrainActivities.size} of ${BrainGameActivityId.entries.size}",
                    "Highest Performance Level: Level ${settings.brainMaxLevelReached} of 3"
                ),
                needsPracticeItemsAr = if (settings.completedBrainActivities.size < BrainGameActivityId.entries.size) {
                    listOf("تدريب الذاكرة البصرية وإكمال الأنماط الذكية")
                } else emptyList(),
                needsPracticeItemsEn = if (settings.completedBrainActivities.size < BrainGameActivityId.entries.size) {
                    listOf("Practice Memory Cards and Pattern Completion")
                } else emptyList(),
                accentColor = Color(0xFF1565C0)
            ),
            WorldDetailedProgress(
                categoryId = CategoryId.WRITING_DRAWING,
                titleAr = "عالم الكتابة والرسم التفاعلي",
                titleEn = "Writing & Drawing World",
                emoji = "🖌️",
                progressFraction = settings.writingWorldProgressFraction,
                completedCount = settings.completedWritingActivities.size,
                totalCount = WritingDrawingActivityId.entries.size,
                summaryMetricsAr = listOf(
                    "الحروف والأرقام المتدرب على كتابتها: ${settings.practicedWritingItems.size} عنصراً",
                    "الرسومات المحفوظة للطفل: ${settings.savedDrawingsTitles.size} لوحة فنية",
                    "الأنشطة المكتملة: ${settings.completedWritingActivities.size} من ${WritingDrawingActivityId.entries.size}"
                ),
                summaryMetricsEn = listOf(
                    "Practiced Traced Letters & Numbers: ${settings.practicedWritingItems.size} items",
                    "Saved Child Drawings: ${settings.savedDrawingsTitles.size} artworks",
                    "Completed Activities: ${settings.completedWritingActivities.size} of ${WritingDrawingActivityId.entries.size}"
                ),
                needsPracticeItemsAr = if (settings.practicedWritingItems.size < 8) {
                    listOf("تتبّع كتابة الحروف والأرقام بالنقاط التوجيهية")
                } else emptyList(),
                needsPracticeItemsEn = if (settings.practicedWritingItems.size < 8) {
                    listOf("Practice guided tracing for letters and numbers")
                } else emptyList(),
                accentColor = Color(0xFFAD1457)
            )
        )
    }

    fun buildActivityLog(settings: UserSettingsState): List<ActivityLogEntry> {
        val explicitEntries = settings.activityLogRawEntries
            .mapNotNull { ActivityLogEntry.deserialize(it) }
            .sortedByDescending { it.timestampMillis }

        if (explicitEntries.isNotEmpty()) {
            return explicitEntries
        }

        // Backfill from completed activities so existing progress is immediately visible in the log
        val synthesized = mutableListOf<ActivityLogEntry>()
        val fallbackDate = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())

        settings.completedNumbersActivities.forEachIndexed { idx, act ->
            synthesized.add(
                ActivityLogEntry(
                    id = "num_${act.key}",
                    worldCategory = CategoryId.NUMBERS,
                    activityTitleAr = act.titleAr,
                    activityTitleEn = act.titleEn,
                    formattedDate = fallbackDate,
                    starsEarned = 1,
                    timestampMillis = 1_700_000_000_000L + idx * 1000L
                )
            )
        }
        settings.completedLettersActivities.forEachIndexed { idx, act ->
            synthesized.add(
                ActivityLogEntry(
                    id = "let_${act.key}",
                    worldCategory = CategoryId.ARABIC_LETTERS,
                    activityTitleAr = act.titleAr,
                    activityTitleEn = act.titleEn,
                    formattedDate = fallbackDate,
                    starsEarned = 1,
                    timestampMillis = 1_700_000_100_000L + idx * 1000L
                )
            )
        }
        settings.completedColorsActivities.forEachIndexed { idx, act ->
            synthesized.add(
                ActivityLogEntry(
                    id = "col_${act.key}",
                    worldCategory = CategoryId.COLORS,
                    activityTitleAr = act.titleAr,
                    activityTitleEn = act.titleEn,
                    formattedDate = fallbackDate,
                    starsEarned = 1,
                    timestampMillis = 1_700_000_200_000L + idx * 1000L
                )
            )
        }
        settings.completedShapesActivities.forEachIndexed { idx, act ->
            synthesized.add(
                ActivityLogEntry(
                    id = "shp_${act.key}",
                    worldCategory = CategoryId.SHAPES,
                    activityTitleAr = act.titleAr,
                    activityTitleEn = act.titleEn,
                    formattedDate = fallbackDate,
                    starsEarned = 1,
                    timestampMillis = 1_700_000_300_000L + idx * 1000L
                )
            )
        }
        settings.completedAnimalsActivities.forEachIndexed { idx, act ->
            synthesized.add(
                ActivityLogEntry(
                    id = "ani_${act.key}",
                    worldCategory = CategoryId.ANIMALS,
                    activityTitleAr = act.titleAr,
                    activityTitleEn = act.titleEn,
                    formattedDate = fallbackDate,
                    starsEarned = 1,
                    timestampMillis = 1_700_000_400_000L + idx * 1000L
                )
            )
        }
        settings.completedBrainActivities.forEachIndexed { idx, act ->
            synthesized.add(
                ActivityLogEntry(
                    id = "brn_${act.key}",
                    worldCategory = CategoryId.BRAIN_GAMES,
                    activityTitleAr = act.titleAr,
                    activityTitleEn = act.titleEn,
                    formattedDate = fallbackDate,
                    starsEarned = 1,
                    timestampMillis = 1_700_000_500_000L + idx * 1000L
                )
            )
        }
        settings.completedWritingActivities.forEachIndexed { idx, act ->
            synthesized.add(
                ActivityLogEntry(
                    id = "wrt_${act.key}",
                    worldCategory = CategoryId.WRITING_DRAWING,
                    activityTitleAr = act.titleAr,
                    activityTitleEn = act.titleEn,
                    formattedDate = fallbackDate,
                    starsEarned = 1,
                    timestampMillis = 1_700_000_600_000L + idx * 1000L
                )
            )
        }
        return synthesized.sortedByDescending { it.timestampMillis }
    }

    fun generateInsightsAndRecommendations(settings: UserSettingsState): ParentInsightsReport {
        val strengthsAr = mutableListOf<String>()
        val strengthsEn = mutableListOf<String>()
        val practiceAr = mutableListOf<String>()
        val practiceEn = mutableListOf<String>()
        val recommendations = mutableListOf<SmartParentRecommendation>()

        // 1. Evaluate Numbers
        if (settings.completedNumbersActivities.size >= 3) {
            strengthsAr.add("الطفل متقدم في عالم الأرقام والعدّ (${settings.completedNumbersActivities.size} من ٦ أنشطة مكتملة).")
            strengthsEn.add("The child excels in Numbers & Counting (${settings.completedNumbersActivities.size}/6 activities completed).")
        } else {
            practiceAr.add("يحتاج الطفل إلى مزيد من التدريب في الأرقام والعدّ التفاعلي.")
            practiceEn.add("Needs more practice in interactive Numbers and Counting.")
            recommendations.add(
                SmartParentRecommendation(
                    id = "rec_numbers",
                    titleAr = "تدريب الأرقام والعدّ (١ إلى ١٠)",
                    titleEn = "Practice Numbers & Counting (1 to 10)",
                    reasonAr = "أكمل الطفل ${settings.completedNumbersActivities.size} فقط من أنشطة الأرقام حتى الآن.",
                    reasonEn = "The child has completed ${settings.completedNumbersActivities.size} of 6 number activities so far.",
                    parentTipAr = "عدّوا الفواكه أو الألعاب معاً في المنزل لتعزيز ربط الرقم بالكمية.",
                    parentTipEn = "Count fruits or toys together at home to connect numbers with real quantities.",
                    targetCategory = CategoryId.NUMBERS,
                    emoji = "🔢",
                    accentColor = Color(0xFF0288D1)
                )
            )
        }

        // 2. Evaluate Arabic Letters
        if (settings.learnedArabicLetters.size >= 8) {
            strengthsAr.add("حصيلة ممتازة في الحروف العربية (تعلّم ${settings.learnedArabicLetters.size} حرفاً عربياً).")
            strengthsEn.add("Strong progress in Arabic Letters (learned ${settings.learnedArabicLetters.size} Arabic letters).")
        } else {
            practiceAr.add("يُنصح بزيادة التدريب على الحروف العربية وأصواتها (${settings.learnedArabicLetters.size} من ٢٨ حرفاً).")
            practiceEn.add("Recommended to practice more Arabic letters and phonics (${settings.learnedArabicLetters.size}/28 learned).")
            recommendations.add(
                SmartParentRecommendation(
                    id = "rec_arabic_letters",
                    titleAr = "تعلّم ٣ حروف عربية جديدة مع لولو",
                    titleEn = "Learn 3 New Arabic Letters with Lulu",
                    reasonAr = "تعلّم الطفل ${settings.learnedArabicLetters.size} من أصل ٢٨ حرفاً عربياً.",
                    reasonEn = "The child has explored ${settings.learnedArabicLetters.size} out of 28 Arabic letters.",
                    parentTipAr = "استمعوا لنطق الحرف مع لولو واطلبوا من الطفل تسمية شيء يبدأ بنفس الحرف.",
                    parentTipEn = "Listen to Lulu's pronunciation and ask your child to name an object starting with that letter.",
                    targetCategory = CategoryId.ARABIC_LETTERS,
                    emoji = "أ",
                    accentColor = Color(0xFF2E7D32)
                )
            )
        }

        // 3. Evaluate English Letters
        if (settings.learnedEnglishLetters.size >= 8) {
            strengthsAr.add("تقدم رائع في الحروف الإنجليزية ABC (تعلّم ${settings.learnedEnglishLetters.size} حرفاً).")
            strengthsEn.add("Great progress in English ABC Letters (learned ${settings.learnedEnglishLetters.size} letters).")
        } else {
            practiceAr.add("يحتاج إلى استكشاف المزيد من الحروف الإنجليزية (${settings.learnedEnglishLetters.size} من ٢٦ حرفاً).")
            practiceEn.add("Needs to explore more English ABC letters (${settings.learnedEnglishLetters.size}/26 learned).")
            recommendations.add(
                SmartParentRecommendation(
                    id = "rec_english_letters",
                    titleAr = "استكشاف الحروف الإنجليزية ABC",
                    titleEn = "Explore English ABC Letters",
                    reasonAr = "تعلّم الطفل ${settings.learnedEnglishLetters.size} من أصل ٢٦ حرفاً إنجليزياً.",
                    reasonEn = "The child has learned ${settings.learnedEnglishLetters.size} out of 26 English letters.",
                    parentTipAr = "ربط الحرف الإنجليزي بصورة كرتونية يساعد الطفل على تذكره بسرعة.",
                    parentTipEn = "Connecting each English letter with a vivid cartoon picture boosts retention.",
                    targetCategory = CategoryId.ENGLISH_LETTERS,
                    emoji = "🔤",
                    accentColor = Color(0xFF6A1B9A)
                )
            )
        }

        // 4. Evaluate Colors & Shapes
        if (settings.completedColorsActivities.size + settings.completedShapesActivities.size >= 4) {
            strengthsAr.add("يمتلك الطفل تمييزاً بصرياً قوياً في الألوان والأشكال الهندسية.")
            strengthsEn.add("Strong visual recognition in Colors and Geometric Shapes.")
        } else if (settings.completedColorsActivities.size < 2) {
            practiceAr.add("تجربة أنشطة دمج الألوان والتمييز اللوني.")
            practiceEn.add("Practice color recognition and mixing primary colors.")
            recommendations.add(
                SmartParentRecommendation(
                    id = "rec_colors",
                    titleAr = "نشاط دمج الألوان والتعرف عليها",
                    titleEn = "Color Mixing & Discovery Activity",
                    reasonAr = "استكشاف الألوان ينمي الملاحظة البصرية والإبداع الفني للطفل.",
                    reasonEn = "Exploring colors builds visual observation and artistic creativity.",
                    parentTipAr = "اسألوا الطفل عن ألوان ملابسه وألعابه المفضلة باللغتين العربية والإنجليزية.",
                    parentTipEn = "Ask your child about the colors of their favorite toys in both Arabic and English.",
                    targetCategory = CategoryId.COLORS,
                    emoji = "🎨",
                    accentColor = Color(0xFFD81B60)
                )
            )
        } else {
            practiceAr.add("التدرب على مطابقة الأشكال الهندسية وتكوين الصور.")
            practiceEn.add("Practice matching geometric shapes and building pictures.")
            recommendations.add(
                SmartParentRecommendation(
                    id = "rec_shapes",
                    titleAr = "نشاط مطابقة الأشكال الهندسية",
                    titleEn = "Geometric Shapes Matching Activity",
                    reasonAr = "يساعد على تنمية التفكير المكاني وملاحظة الأشكال في البيئة المحيطة.",
                    reasonEn = "Develops spatial awareness and recognizing shapes in daily life.",
                    parentTipAr = "ابحثوا مع الطفل عن أشياء دائرية أو مربعة داخل الغرفة.",
                    parentTipEn = "Look around the room together for circular or square objects.",
                    targetCategory = CategoryId.SHAPES,
                    emoji = "🔷",
                    accentColor = Color(0xFFE65100)
                )
            )
        }

        // 5. Evaluate Brain Games & Memory
        if (settings.completedBrainActivities.size >= 3) {
            strengthsAr.add("أداء متميز في ألعاب الذكاء والذاكرة والتركيز (المستوى ${settings.brainMaxLevelReached}).")
            strengthsEn.add("Strong performance in Brain, Memory & Focus games (Level ${settings.brainMaxLevelReached}).")
        } else {
            practiceAr.add("تنشيط الذاكرة البصرية وحل الألغاز في عالم ألعاب الذكاء.")
            practiceEn.add("Boost visual memory and puzzle-solving in Brain Games World.")
            recommendations.add(
                SmartParentRecommendation(
                    id = "rec_brain",
                    titleAr = "لعبة بطاقات الذاكرة وإكمال النمط",
                    titleEn = "Memory Cards & Pattern Completion Game",
                    reasonAr = "تقوي الانتباه والذاكرة قصيرة المدى والتفكير المنطقي.",
                    reasonEn = "Strengthens attention, short-term memory, and logical thinking.",
                    parentTipAr = "ابدؤوا بالمستوى الأول (٤ بطاقات) وشجعوا الطفل عند كل مطابقة صحيحة.",
                    parentTipEn = "Start with Level 1 (4 cards) and praise your child for every match.",
                    targetCategory = CategoryId.BRAIN_GAMES,
                    emoji = "🧠",
                    accentColor = Color(0xFF1565C0)
                )
            )
        }

        // 6. Evaluate Writing & Drawing
        if (settings.completedWritingActivities.size >= 2 || settings.practicedWritingItems.size >= 4 || settings.savedDrawingsTitles.isNotEmpty()) {
            strengthsAr.add("مهارات حركية دقيقة جميلة في عالم الكتابة والرسم (${settings.practicedWritingItems.size} عنصراً متدرباً عليه).")
            strengthsEn.add("Good fine-motor skills in Writing & Drawing (${settings.practicedWritingItems.size} items traced).")
        } else {
            practiceAr.add("لم يتدرب الطفل بما يكفي في عالم الكتابة وتتبّع الحروف والأرقام.")
            practiceEn.add("The child hasn't practiced enough letter/number tracing in Writing & Drawing World.")
            recommendations.add(
                SmartParentRecommendation(
                    id = "rec_writing",
                    titleAr = "تتبّع كتابة الحروف والأرقام والرسم الحر",
                    titleEn = "Guided Letter/Number Tracing & Free Drawing",
                    reasonAr = "يقوي عضلات اليد الدقيقة والتمهيد للكتابة المدرسية.",
                    reasonEn = "Builds fine-motor hand control and school writing readiness.",
                    parentTipAr = "شجعوا الطفل على تتبّع النقاط بإصبعه على الشاشة ثم حفظ لوحته الفنية.",
                    parentTipEn = "Encourage your child to trace the guided dots with their finger and save their drawing.",
                    targetCategory = CategoryId.WRITING_DRAWING,
                    emoji = "🖌️",
                    accentColor = Color(0xFFAD1457)
                )
            )
        }

        // Ensure at least one strength is always shown encouragingly
        if (strengthsAr.isEmpty()) {
            strengthsAr.add("الطفل بدأ رحلته التعليمية بحماس واستكشف ${RewardsProgressCalculator.exploredWorldsCount(settings)} عوالم تعليمية.")
            strengthsEn.add("The child started their learning journey enthusiastically and explored ${RewardsProgressCalculator.exploredWorldsCount(settings)} worlds.")
        }

        val parentTipsAr = listOf(
            "اجعلوا جلسة التعلّم اليومية قصيرة وممتعة (${settings.dailyGoalMinutes} دقيقة يومياً مثالية لعمر ٤–٦ سنوات).",
            "امدحوا جهد الطفل ومحاولته وليس فقط الإجابة الصحيحة لبناء ثقته بنفسه.",
            "استخدموا الشهادات التعليمية القابلة للطباعة لتعليقها في غرفة الطفل كحافز إيجابي."
        )
        val parentTipsEn = listOf(
            "Keep daily learning sessions short and joyful (${settings.dailyGoalMinutes} mins/day is ideal for ages 4–6).",
            "Praise your child's effort and curiosity to build lasting self-confidence.",
            "Print unlocked educational certificates and display them in your child's room as positive reinforcement."
        )

        return ParentInsightsReport(
            strengthsAr = strengthsAr,
            strengthsEn = strengthsEn,
            areasToPracticeAr = practiceAr,
            areasToPracticeEn = practiceEn,
            parentTipsAr = parentTipsAr,
            parentTipsEn = parentTipsEn,
            recommendations = recommendations
        )
    }
}
