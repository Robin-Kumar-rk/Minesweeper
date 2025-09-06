package com.example.minesweeper.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

class SoundManager(private val context: Context) {
    private val soundPool: SoundPool
    private val sounds = mutableMapOf<String, Int>()
    
    init {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()
            
        soundPool = SoundPool.Builder()
            .setMaxStreams(4)
            .setAudioAttributes(audioAttributes)
            .build()
            
        // Load sound effects (we'll create simple programmatic sounds)
        loadSounds()
    }
    
    private fun loadSounds() {
        // For now, we'll use system sounds as placeholders
        // In a real app, you'd load actual sound files from assets
        sounds["click"] = 1
        sounds["flag"] = 2
        sounds["mine"] = 3
        sounds["win"] = 4
    }
    
    fun playClick() {
        playSound("click", 0.3f)
    }
    
    fun playFlag() {
        playSound("flag", 0.4f)
    }
    
    fun playMine() {
        playSound("mine", 0.8f)
    }
    
    fun playWin() {
        playSound("win", 0.6f)
    }
    
    private fun playSound(soundKey: String, volume: Float) {
        try {
            // For demo purposes, we'll use system sounds
            when (soundKey) {
                "click" -> {
                    // Play a soft click sound
                    android.media.ToneGenerator(android.media.AudioManager.STREAM_MUSIC, 50)
                        .startTone(android.media.ToneGenerator.TONE_PROP_BEEP, 100)
                }
                "flag" -> {
                    // Play a flag sound
                    android.media.ToneGenerator(android.media.AudioManager.STREAM_MUSIC, 60)
                        .startTone(android.media.ToneGenerator.TONE_PROP_ACK, 150)
                }
                "mine" -> {
                    // Play an explosion-like sound
                    android.media.ToneGenerator(android.media.AudioManager.STREAM_MUSIC, 80)
                        .startTone(android.media.ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 300)
                }
                "win" -> {
                    // Play a victory sound
                    android.media.ToneGenerator(android.media.AudioManager.STREAM_MUSIC, 70)
                        .startTone(android.media.ToneGenerator.TONE_CDMA_CONFIRM, 500)
                }
            }
        } catch (e: Exception) {
            // Silently handle any sound errors
        }
    }
    
    fun release() {
        soundPool.release()
    }
}

@Composable
fun rememberSoundManager(): SoundManager {
    val context = LocalContext.current
    return remember { SoundManager(context) }
}
