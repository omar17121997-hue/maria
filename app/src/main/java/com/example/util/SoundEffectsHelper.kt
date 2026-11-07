package com.example.util

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.exp
import kotlin.math.sin

/**
 * Generates soft, cheerful marimba-like musical chimes for child-friendly UI interactions
 * when sound is enabled. Safe on all devices and unit tests.
 */
object SoundEffectsHelper {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    fun playPopChime(enabled: Boolean) {
        if (!enabled) return
        playChord(listOf(523.25, 659.25), durationMs = 130) // C5 + E5
    }

    fun playHappyMelody(enabled: Boolean) {
        if (!enabled) return
        scope.launch {
            playToneInternal(523.25, 90) // C5
            playToneInternal(659.25, 90) // E5
            playToneInternal(783.99, 150) // G5
        }
    }

    fun playStarCollectChime(enabled: Boolean) {
        if (!enabled) return
        scope.launch {
            playToneInternal(587.33, 80) // D5
            playToneInternal(880.0, 90)  // A5
            playToneInternal(1174.66, 180) // D6
        }
    }

    private fun playChord(frequencies: List<Double>, durationMs: Int) {
        scope.launch {
            playToneInternal(frequencies.firstOrNull() ?: 523.25, durationMs)
        }
    }

    private fun playToneInternal(freqHz: Double, durationMs: Int) {
        try {
            val sampleRate = 22050
            val numSamples = (sampleRate * (durationMs / 1000.0)).toInt().coerceAtLeast(1)
            val samples = ShortArray(numSamples)

            for (i in 0 until numSamples) {
                val t = i.toDouble() / sampleRate
                val envelope = exp(-6.0 * t / (durationMs / 1000.0))
                val wave = sin(2.0 * PI * freqHz * t)
                samples[i] = (wave * envelope * Short.MAX_VALUE * 0.25).toInt().toShort()
            }

            val bufferSize = samples.size * 2
            val audioTrack = AudioTrack.Builder()
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_GAME)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                )
                .setAudioFormat(
                    AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setSampleRate(sampleRate)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                        .build()
                )
                .setBufferSizeInBytes(bufferSize.coerceAtLeast(AudioTrack.getMinBufferSize(
                    sampleRate,
                    AudioFormat.CHANNEL_OUT_MONO,
                    AudioFormat.ENCODING_PCM_16BIT
                )))
                .setTransferMode(AudioTrack.MODE_STATIC)
                .build()

            audioTrack.write(samples, 0, samples.size)
            audioTrack.play()
            Thread.sleep(durationMs.toLong() + 20L)
            audioTrack.release()
        } catch (_: Throwable) {
            // Ignore audio errors in headless test environments
        }
    }
}
