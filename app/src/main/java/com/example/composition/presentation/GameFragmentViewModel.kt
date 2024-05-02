package com.example.composition.presentation

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.composition.data.GameRepositoryImpl
import com.example.composition.domain.entity.GameSettings
import com.example.composition.domain.entity.Level
import com.example.composition.domain.entity.Question
import com.example.composition.domain.usecase.GenerateQuestionUseCase
import com.example.composition.domain.usecase.GetGameSettingsUseCase

class GameFragmentViewModel : ViewModel() {

    private lateinit var level: Level
    private lateinit var gameSettings: GameSettings
    private var timer : CountDownTimer? = null

    private val repository = GameRepositoryImpl

    private val generateQuestionUseCase = GenerateQuestionUseCase(repository)
    private val getGameSettingsUseCase = GetGameSettingsUseCase(repository)

    private var _formattedTime = MutableLiveData<String>()
    val formattedTIme: LiveData<String>
        get() = _formattedTime

    private var _question = MutableLiveData<Question>()
    val question: LiveData<Question>
        get() = _question

    private var _rightAnswersCount = MutableLiveData<Int>()
    val rightAnswersCount: LiveData<Int>
        get() = _rightAnswersCount

    fun startGame(level: Level) {
        getGameSettings(level)
        startTimer()
    }
    //todo далее надо генерировать вопрос

    private fun getGameSettings(level: Level) {
        this.level = level
        this.gameSettings = getGameSettingsUseCase(level)
    }

    private fun startTimer() {
        timer = object : CountDownTimer(
            gameSettings.gameTimeInSeconds * MILLIS_IN_SECCOND,
            MILLIS_IN_SECCOND
        ) {
            override fun onTick(millisUntilFinished: Long) {
                _formattedTime.value = formatTime(millisUntilFinished)
            }

            override fun onFinish() {
                finishGame()
            }
        }
        timer?.start()
    }

    private fun formatTime(millisUntilFinished: Long) : String {
        val seconds = millisUntilFinished / MILLIS_IN_SECCOND
        val minutes = seconds / SECONDS_IN_MINUTES
        val remainingSecond = seconds - (minutes * SECONDS_IN_MINUTES)
        return String.format("%02d:%02d", minutes, remainingSecond)
    }

    private fun finishGame() {

    }


    fun checkAnswer(i: Int) {
        question.value?.options?.get(i)?.let {
            if (it == question.value?.rightAnswer) {
                rightAnswersCount.value?.plus(1)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        timer?.cancel()
    }

    private fun generateQuestion(maxSumValue: Int) {
        _question.value = generateQuestionUseCase.invoke(maxSumValue)
    }

    companion object {

        private const val MILLIS_IN_SECCOND = 1000L
        private const val SECONDS_IN_MINUTES = 60
    }

}