package com.aegisnav.data.services

import android.speech.tts.TextToSpeech
import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton
import java.util.Locale

/**
 * Provides voice guidance for navigation
 */
@Singleton
class VoiceGuidanceService @Inject constructor(context: Context) : TextToSpeech.OnInitListener {
    private val tts = TextToSpeech(context, this)
    
    private val _isReadyFlow = MutableStateFlow(false)
    val isReadyFlow = _isReadyFlow.asStateFlow()
    
    private var isReady = false

    init {
        tts.language = Locale.getDefault()
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            isReady = true
            _isReadyFlow.value = true
            tts.language = Locale.getDefault()
            // Set speech rate and pitch
            tts.setSpeechRate(1.0f)
            tts.setPitch(1.0f)
        }
    }

    fun speak(text: String) {
        if (isReady) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null)
        }
    }

    fun speakQueue(text: String) {
        if (isReady) {
            tts.speak(text, TextToSpeech.QUEUE_ADD, null)
        }
    }

    fun stop() {
        if (isReady) {
            tts.stop()
        }
    }

    fun shutdown() {
        if (isReady) {
            tts.stop()
            tts.shutdown()
            isReady = false
            _isReadyFlow.value = false
        }
    }

    fun setLanguage(locale: Locale) {
        if (isReady) {
            tts.language = locale
        }
    }

    fun isReady(): Boolean = isReady
}
