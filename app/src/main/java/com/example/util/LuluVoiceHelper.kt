package com.example.util

import android.content.Context
import android.os.Build
import android.speech.tts.TextToSpeech
import com.example.model.AppLanguage
import java.util.Locale

/**
 * Manages Lulu's voice guidance and number pronunciation in Arabic and English.
 * - Ensures only one voice plays at a time (QUEUE_FLUSH).
 * - Selects a warm, natural female voice when available on the device.
 * - Uses child-friendly pitch and gentle speaking rate.
 * - Plays a soft musical chime fallback via SoundEffectsHelper when sound is enabled.
 */
class LuluVoiceHelper(context: Context) {

    private var tts: TextToSpeech? = null
    private var isReady: Boolean = false
    private var lastSpokenText: String = ""
    private var lastLanguage: AppLanguage = AppLanguage.AR

    init {
        try {
            tts = TextToSpeech(context.applicationContext) { status ->
                if (status == TextToSpeech.SUCCESS) {
                    isReady = true
                    configureChildFriendlyVoice(AppLanguage.AR)
                }
            }
        } catch (_: Throwable) {
            isReady = false
        }
    }

    private fun configureChildFriendlyVoice(language: AppLanguage) {
        val engine = tts ?: return
        try {
            val targetLocale = if (language == AppLanguage.AR) {
                Locale.forLanguageTag("ar-SA")
            } else {
                Locale.US
            }
            val result = engine.setLanguage(targetLocale)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                if (language == AppLanguage.AR) {
                    engine.setLanguage(Locale.forLanguageTag("ar"))
                }
            }

            // Prefer a warm, natural non-network female voice if available
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val langPrefix = if (language == AppLanguage.AR) "ar" else "en"
                val matchingVoices = engine.voices?.filter { voice ->
                    voice.locale.language.lowercase().startsWith(langPrefix) &&
                        !voice.isNetworkConnectionRequired
                }.orEmpty()

                val preferredVoice = matchingVoices.firstOrNull { v ->
                    val name = v.name.lowercase()
                    name.contains("female") || name.contains("laila") ||
                        name.contains("mariam") || name.contains("salma") ||
                        name.contains("samantha") || name.contains("sfg") ||
                        name.contains("local")
                } ?: matchingVoices.firstOrNull()

                if (preferredVoice != null) {
                    engine.voice = preferredVoice
                }
            }

            // Gentle, warm, child-friendly delivery
            engine.setPitch(1.1f)
            engine.setSpeechRate(0.86f)
        } catch (_: Throwable) {
            // Safe fallback in test or headless environments
        }
    }

    fun speak(
        text: String,
        language: AppLanguage,
        soundEnabled: Boolean
    ) {
        lastSpokenText = text
        lastLanguage = language
        if (!soundEnabled) {
            stop()
            return
        }

        SoundEffectsHelper.playPopChime(true)

        val engine = tts
        if (engine != null && isReady) {
            try {
                // Stop any ongoing speech so multiple voices never play simultaneously
                engine.stop()
                configureChildFriendlyVoice(language)
                engine.speak(text, TextToSpeech.QUEUE_FLUSH, null, "lulu_utterance_${System.currentTimeMillis()}")
            } catch (_: Throwable) {
                // Fallback chime already played
            }
        }
    }

    fun replayLast(soundEnabled: Boolean) {
        if (lastSpokenText.isNotBlank()) {
            speak(lastSpokenText, lastLanguage, soundEnabled)
        }
    }

    fun stop() {
        try {
            tts?.stop()
        } catch (_: Throwable) {
        }
    }

    fun shutdown() {
        try {
            tts?.stop()
            tts?.shutdown()
        } catch (_: Throwable) {
        }
    }
}
