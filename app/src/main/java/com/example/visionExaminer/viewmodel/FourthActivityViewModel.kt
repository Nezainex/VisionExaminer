package com.example.visionExaminer.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FourthActivityViewModel : ViewModel() {
    val questions = listOf(
        "Испытывали ли вы за последнюю неделю повышенную светочувствительность?",
        "Испытывали ли вы за последнюю неделю ощущение песка в глазах?",
        "Испытывали ли вы за последнюю неделю болезненность, покраснение глаз?",
        "Испытывали ли вы за последнюю неделю затуманивание зрения?",
        "Испытывали ли вы за последнюю неделю ухудшение зрения?",
        "Появились ли у вас за последнюю неделю проблемы со зрением, затрудняющие чтение?",
        "Появились ли у вас за последнюю неделю проблемы со зрением, затрудняющие ночное вождение?",
        "Появились ли у вас за последнюю неделю проблемы со зрением, затрудняющие работу за компьютером?",
        "Появились ли у вас за последнюю неделю проблемы со зрением, затрудняющие просмотр телевизора?",
        "Испытываете ли вы неприятные ощущения в глазах в ветреную погоду?",
        "Испытываете ли вы неприятные ощущения в глазах в местах с низкой влажностью (сухой воздух)?",
        "Испытываете ли вы неприятные ощущения в глазах в местах, где работает кондиционер?"
    )

    private val _currentQuestionIndex = MutableLiveData(0)
    val currentQuestionIndex: LiveData<Int> = _currentQuestionIndex

    private val _totalScore = MutableLiveData(0)
    val totalScore: LiveData<Int> = _totalScore

    private val _resultText = MutableLiveData("")
    val resultText: LiveData<String> = _resultText

    private val _isFinishButtonEnabled = MutableLiveData(false)
    val isFinishButtonEnabled: LiveData<Boolean> = _isFinishButtonEnabled

    fun onAnswerClick(score: Int) {
        _totalScore.value = (_totalScore.value ?: 0) + score
        _currentQuestionIndex.value = (_currentQuestionIndex.value ?: 0) + 1

        // Изменено условие активации кнопки
        _isFinishButtonEnabled.value = (_currentQuestionIndex.value ?: 0) >= questions.size

        if ((_currentQuestionIndex.value ?: 0) >= questions.size) {
            calculateResult()
        }
    }

    fun resetQuiz() {
        _currentQuestionIndex.value = 0
        _totalScore.value = 0
        _resultText.value = ""
        _isFinishButtonEnabled.value = false
    }

    private fun calculateResult() {
        _resultText.value = when (_totalScore.value ?: 0) {
            in 0..12 -> "Нормальная поверхность глаза."
            in 13..22 -> "Лёгкая степень заболевания поверхности глаза."
            in 23..32 -> "Средняя степень заболевания поверхности глаза."
            else -> "Тяжелая степень заболевания поверхности глаза."
        }
    }
}