package com.example.testapp13

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView


class FourthActivity : AppCompatActivity() {

    private lateinit var questionTextView: TextView
    private lateinit var resultTextView: TextView
    private lateinit var answerButtons: List<Button>
    private lateinit var resetButton: Button
    private lateinit var imageViewDryEye: ImageView

    private val questions = listOf(
        "1/12 Испытывали ли вы за последнюю неделю повышенную светочувствительность?",
        "2/12 Испытывали ли вы за последнюю неделю ощущение песка в глазах?",
        "3/12 Испытывали ли вы за последнюю неделю болезненность, покраснение глаз?",
        "4/12 Испытывали ли вы за последнюю неделю затуманивание зрения?",
        "5/12 Испытывали ли вы за последнюю неделю ухудшение зрения?",
        "6/12 Появились ли у вас за последнюю неделю проблемы со зрением, затрудняющие чтение?",
        "7/12 Появились ли у вас за последнюю неделю проблемы со зрением, затрудняющие ночное вождение?",
        "8/12 Появились ли у вас за последнюю неделю проблемы со зрением, затрудняющие работу за компьютером?",
        "9/12 Появились ли у вас за последнюю неделю проблемы со зрением, затрудняющие просмотр телевизора?",
        "10/12 Испытываете ли вы неприятные ощущения в глазах в ветреную погоду?",
        "11/12 Испытываете ли вы неприятные ощущения в глазах в местах с низкой влажностью (сухой воздух)?",
        "12/12 Испытываете ли вы неприятные ощущения в глазах в местах, где работает кондиционер?"
    )

    private val answerOptions = listOf(
        "Постоянно" to 4,
        "Большую часть времени" to 3,
        "Примерно половину указанного временного периода" to 2,
        "Иногда" to 1,
        "Никогда" to 0,
        "Затрудняюсь ответить" to 0
    )

    private val imageResources = listOf(
        R.drawable.dryeye1,
        R.drawable.dryeye2,
        R.drawable.dryeye3,
        R.drawable.dryeye4,
        R.drawable.dryeye5,
        R.drawable.dryeye6,
        R.drawable.dryeye7,
        R.drawable.dryeye8,
        R.drawable.dryeye9,
        R.drawable.dryeye10,
        R.drawable.dryeye11,
        R.drawable.dryeye12,
    )

    private var currentQuestionIndex = 0
    private var totalScore = 0
    private var answeredQuestions = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fourth)
        supportActionBar?.hide() // Hide the activity title

        questionTextView = findViewById(R.id.textViewQuestion)
        resultTextView = findViewById(R.id.textViewResult)
        resetButton = findViewById(R.id.resetButton)
        imageViewDryEye = findViewById(R.id.imageViewdryeye)

        answerButtons = listOf(
            findViewById(R.id.buttonAlways),
            findViewById(R.id.buttonMostofthetime),
            findViewById(R.id.buttonHalftime),
            findViewById(R.id.buttonSometimes),
            findViewById(R.id.buttonNever),
            findViewById(R.id.buttonCantTell)
        )

        resetButton.setOnClickListener { resetQuiz() }

        showQuestion()
        disableFinishButton() // Деактивируем кнопку при запуске
        val finishTestButton = findViewById<Button>(R.id.finishTestButton)
        finishTestButton.setOnClickListener {
            saveOsdiResultAndFinish()
        }
    }

    private fun showQuestion() {
        val currentQuestion = questions[currentQuestionIndex]
        questionTextView.text = currentQuestion

        // Set text and click listeners for answer buttons
        for (i in answerOptions.indices) {
            answerButtons[i].text = answerOptions[i].first
            answerButtons[i].setOnClickListener { onAnswerClick(answerOptions[i].second) }
        }

        // Set the image for the current question
        imageViewDryEye.setImageResource(imageResources[currentQuestionIndex])
    }

    private fun onAnswerClick(score: Int) {
        totalScore += score
        answeredQuestions++
        if (answeredQuestions >= questions.size) {
            enableFinishButton() // Активируем кнопку, если все вопросы отвечены
        }
        currentQuestionIndex++
        if (currentQuestionIndex < questions.size) {
            showQuestion()
        } else {
            showResult()
            disableAnswerButtons()
        }
    }


    private fun showResult() {
        val resultText = when (totalScore) {
            in 0..12 -> "Нормальная поверхность глаза."
            in 13..22 -> "Лёгкая степень заболевания поверхности глаза."
            in 23..32 -> "Средняя степень заболевания поверхности глаза."
            else -> "Тяжелая степень заболевания поверхности глаза."
        }
        resultTextView.text = resultText

        // Display the corresponding image based on the result
        val imageResource = when (totalScore) {
            in 0..12 -> R.drawable.norm
            in 13..22 -> R.drawable.soft
            in 23..32 -> R.drawable.moderate
            else -> R.drawable.severe
        }
        imageViewDryEye.setImageResource(imageResource)
    }

    private fun disableAnswerButtons() {
        for (button in answerButtons) {
            button.isEnabled = false
        }
    }
    private fun enableFinishButton() {
        val finishTestButton = findViewById<Button>(R.id.finishTestButton)
        finishTestButton.isEnabled = true
    }

    private fun disableFinishButton() {
        val finishTestButton = findViewById<Button>(R.id.finishTestButton)
        finishTestButton.isEnabled = false
    }

    private fun resetQuiz() {
        currentQuestionIndex = 0
        totalScore = 0
        resultTextView.text = ""
        enableAnswerButtons()
        showQuestion()
        disableFinishButton() // Деактивируем кнопку при сбросе
    }

    private fun enableAnswerButtons() {
        for (button in answerButtons) {
            button.isEnabled = true
        }
    }

    private fun saveOsdiResultAndFinish() {
        val resultText = resultTextView.text.toString()
        val score = totalScore

        // Создаем объект OsdiResult, но не сохраняем его здесь
        val osdiResult = OsdiResult(
            patientProfileId = -1, // ID будет установлен позже
            score = score,
            resultText = resultText
        )

        // Возвращаемся во SecondActivity и передаем результат OSDI
        val intent = Intent(this, SecondActivity::class.java)
        intent.putExtra("osdiResult", osdiResult)
        setResult(RESULT_OK, intent)
        finish()
    }
}
