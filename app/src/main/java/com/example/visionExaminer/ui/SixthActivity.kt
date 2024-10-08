package com.example.visionExaminer.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.visionExaminer.R
import com.example.visionExaminer.data.RabkinResult
import com.example.visionExaminer.viewmodel.SixthActivityViewModel

class SixthActivity : AppCompatActivity() {

    private lateinit var questionTextViewrabkin: TextView
    private lateinit var resultTextViewrabkin: TextView
    private lateinit var answerButtonsrabkin: List<FrameLayout>
    private lateinit var resetButtonrabkin: FrameLayout
    private lateinit var finishTestButtonrabkin: FrameLayout
    private lateinit var imageViewrabkin: ImageView
    private lateinit var progressBarrabkin: ProgressBar
    private lateinit var progressTextrabkin: TextView

    private lateinit var viewModel: SixthActivityViewModel

    // Data class for Rabkin images and answers
    data class RabkinImage(
        val imageResource: Int,
        val correctAnswer: String,
        val answer2: String,
        val answer3: String,
        val answer4: String,
    )

    // List of Rabkin images and answers (fill in the missing answers)
    private val rabkinImages = listOf(
        // Image 1: Normal vision and color vision deficiency see "96"
        RabkinImage(
            R.drawable.rabkin1,
            "9 и 6 (96)",
            "56",
            "90",
            "Ничего",
        ),
        // Image 2: Normal vision and color vision deficiency see triangle and circle
        RabkinImage(
            R.drawable.rabkin2,
            "Треугольник и круг",
            "Ничего",  // All deficiencies
            "Квадрат", // All deficiencies
            "Два ромба", // All deficiencies
        ),
        // Image 3: Normal vision sees "9", Deuteranopia/Protanopia see "5"
        RabkinImage(
            R.drawable.rabkin3,
            "9",
            "5", // Deuteranopia/Protanopia
            "Треугольник",// All deficiencies
            "Ничего",// All deficiencies
        ),
        // Image 4: Normal vision sees triangle, Deuteranopia/Protanopia see circle
        RabkinImage(
            R.drawable.rabkin4,
            "Треугольник",
            "Круг", // Deuteranopia/Protanopia
            "13",// All deficiencies
            "Ничего",// All deficiencies
        ),
        // Image 5: Normal vision sees "1 and 3 (13)", Deuteranopia/Protanopia see "6"
        RabkinImage(
            R.drawable.rabkin5,
            "1 и 3 (13)", // Image 5: Normal
            "6",// Deuteranopia/Protanopia
            "Круг",
            "Ничего",
        ),
        // Image 6: Normal vision sees triangle and circle, Deuteranopia/Protanopia see neither
        RabkinImage(
            R.drawable.rabkin6,
            "Треугольник и круг",
            "Ничего",
            "10",
            "96",
        ),
        // Image 7: Normal vision and color vision deficiency see "9" and "6", Deuteranopia only 6   ОШИБКА - ПЕРЕДЕЛАТЬ ИНТЕРПРЕТАЦИЮ ОТВЕТА
        RabkinImage(
            R.drawable.rabkin7,
            "9 и 6 (96)",
            "6",
            "Ничего",
            "Квадрат",
        ),
        // Image 8: Normal and Deuteranopia/Protanopia see "5" (difficult for latter)
        RabkinImage(
            R.drawable.rabkin8,
            "5",
            "Ничего",
            "6",
            "8",
        ),
        // Image 9: Normal and Deuteranomaly see "9", Protanomaly may see "9", "8", or "6"
        RabkinImage(
            R.drawable.rabkin9,
            "9",
            "6",
            "Ничего",
            "8",
        ),
        // Image 10: Normal sees "136", Deuteranopia/Protanopia see "69", "68", or "66"
        RabkinImage(
            R.drawable.rabkin10,
            "1, 3 и 6 (136)",
            "69",
            "68",
            "66",
        ),
        // Image 11: Normal sees a circle and a triangle, Protanopia sees  a triangle,  Deuteranopia sees a circle, or a circle and a triangle.
        RabkinImage(
            R.drawable.rabkin11,
            "Круг и треугольник",
            "Круг",
            "Треугольник",
            "Ничего",
        ),
        // Image 12: Normal and Deuteranomaly see "1 and 2 (12)", Protanopia see neither
        RabkinImage(
            R.drawable.rabkin12,
            "1 и 2 (12)",
            "Ничего",
            "Круг",
            "Треугольник",
        ),
        // Image 13: Normal sees circle and triangle, Protanopia sees circle, Deuteranopia sees triangle
        RabkinImage(
            R.drawable.rabkin13,
            "Круг и треугольник",
            "Круг",
            "Треугольник",
            "Ничего",
        ),
        // Image 14: Normal sees "3 and 0 (30)", Protanopia sees "10" and "6", Deuteranopia sees "1" and "6"
        RabkinImage(
            R.drawable.rabkin14,
            "3 и 0 (30)",
            "6; 1 и 0 (10)",
            "6 и 1",
            "Ничего",
        ),
        // Image 15: Normal sees circle and triangle, Protanopia sees 2 triangles and square, Deuteranopia sees triangle and square
        RabkinImage(
            R.drawable.rabkin15,
            "Круг и треугольник",
            "2 треугольника и квадрат",
            "Треугольник и квадрат",
            "Квадрат",
        ),
        // Image 16: Normal sees "9" and "6" (96), Protanopia sees "9", Deuteranopia sees "6"
        RabkinImage(
            R.drawable.rabkin16,
            "9 и 6 (96)",
            "9",
            "6",
            "Ничего",
        ),
        // Image 17: Normal sees circle and triangle, Protanopia sees triangle, Deuteranopia sees circle
        RabkinImage(
            R.drawable.rabkin17,
            "Круг и треугольник",
            "Треугольник",
            "Круг",
            "Квадрат",
        ),
        // Image 18: Normal sees the horizontal rows in the table of eight squares each (color rows 9th, 10th, 11th, 12th, 13th, 14th, 15th and 16th) as monochromatic ; vertical rows are perceived by them as multi-colored. Dichromats perceive vertical rows as monochromatic, and protanopes perceive vertical color rows as monochromatic - 3rd, 5th and 7th, and deuteranopes - vertical color rows - 1st, 2nd, 4th, 6th th and 8th. Colored squares located horizontally are perceived by protanopes and deuteranopes as multi-colored.
        RabkinImage(
            R.drawable.rabkin18,
            "Одноцветные горизонтальные ряды",
            "Одноцветные вертикальные ряды: 3-й, 5-й и 7-й",
            "Одноцветные вертикальные ряды: 1-й, 2-й, 4-й, 6-й и 8-й",
            "Одноцветные горизонтальные и вертикальные ряды",
        ),
        // Image 19: Normal sees 9 and 5 (95). Deuteranopia/Protanopia see only the number 5.
        RabkinImage(
            R.drawable.rabkin19,
            "9 и 5 (95)",
            "5",
            "Круг",
            "22",
        ),
        // Image 20: Normal sees triangle and circle, Deuteranopia/Protanopia see neither
        RabkinImage(
            R.drawable.rabkin20,
            "Треугольник и круг",
            "Ничего",
            "Квадрат",
            "Ромб",
        ),
        // Image 21: Normal sees 66. Deuteranopia/Protanopia see neither    (вопросы 21 и 22 в таблице рабкина в pdf файле идут в порядке 22 , 21 ) , а на сайте рабкин.ру они в том же порядке, как у меня в приложении сейчас.
        RabkinImage(
            R.drawable.rabkin21,
            "66",
            "Ничего",
            "90",
            "22",
        ),
        // Image 22: Normal sees colored horizontal and single-color vertical rows. Specific details for Deuteranopia/Protanopia
        RabkinImage(
            R.drawable.rabkin22,
            "Одноцветные вертикальные ряды, горизонтальные - цветные",
            "Одноцветные горизонтальные ряды, вертикальные - разноцветные",
            "Нет одноцветных рядов",
            "Все квадраты одного цвета",
        ),
        // Image 23: Normal and Protanopia see "3 and 6 (36)", Deuteranopia/Protanopia see see neither
        RabkinImage(
            R.drawable.rabkin23,
            "3 и 6 (36)",
            "Ничего",
            "Круг",
            "9 и 8 (98)",
        ),
        // Image 24: Normal see "1 and 4 (14)", Deuteranopia/Protanopia see see neither
        RabkinImage(
            R.drawable.rabkin24,
            "1 и 4 (14)",
            "Ничего",
            "9 и 2 (92)",
            "Треугольник и круг",
        ),
        // Image 25: Normal see "9", Deuteranopia/Protanopia see neither
        RabkinImage(
            R.drawable.rabkin25,
            "9",
            "Ничего",
            "Круг",
            "5",
        ),
        // Image 26: Normal see "4", Deuteranopia/Protanopia see neither
        RabkinImage(
            R.drawable.rabkin26,
            "4",
            "Ничего",
            "Квадрат",
            "9",
        ),
        // Image 27: Normal see "13", Deuteranopia/Protanopia see neither
        RabkinImage(
            R.drawable.rabkin27,
            "13",
            "Ничего",
            "Квадрат",
            "76",
        ),
    )

    private var currentQuestionIndex = 0
    private var totalScore = 0
    private var answeredQuestions = 0
    private var protanomalyCount = 0
    private var deuteranomalyCount = 0
    private var tritanomalyCount = 0
    private var protanopiaCount = 0
    private var deuteranopiaCount = 0

    private var isNightMode = true

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isNightMode = intent.getBooleanExtra("isNightMode", true) // Получение isNightMode
        // Устанавливаем тему
        if (isNightMode) {
            setTheme(R.style.AppTheme_Night)
        } else {
            setTheme(R.style.AppTheme_Day)
        }
        // Устанавливаем цвет фона в зависимости от темы
        val backgroundColor = if (isNightMode) {
            ContextCompat.getColor(this, R.color.black)
        } else {
            ContextCompat.getColor(this, R.color.white)
        }
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_sixth_landscape)
        } else {
            setContentView(R.layout.activity_sixth)
        }
        supportActionBar?.hide()
        window.decorView.setBackgroundColor(backgroundColor)

        // Initialize views
        questionTextViewrabkin = findViewById(R.id.textViewQuestionrabkin)
        resultTextViewrabkin = findViewById(R.id.textViewResultrabkin)
        finishTestButtonrabkin = findViewById(R.id.finishTestButtonrabkin)
        resetButtonrabkin = findViewById(R.id.resetButtonrabkin)
        imageViewrabkin = findViewById(R.id.imageViewrabkin)
        answerButtonsrabkin = listOf(
            findViewById(R.id.buttonAnswer1),
            findViewById(R.id.buttonAnswer2),
            findViewById(R.id.buttonAnswer3),
            findViewById(R.id.buttonAnswer4),
        )
        progressBarrabkin = findViewById(R.id.progressBarrabkin)
        progressTextrabkin = findViewById(R.id.progressTextrabkin)

        viewModel = ViewModelProvider(this)[SixthActivityViewModel::class.java]

        resetButtonrabkin.setOnClickListener {
            viewModel.resetQuiz() // Сбрасываем ViewModel
            currentQuestionIndex = 0 // Обновляем currentQuestionIndex
            showQuestion() // Показывает первый вопрос заново
            enableAnswerButtons() // Включаем кнопки ответов
            disableFinishButton() // Отключаем кнопку "Завершить тест"
        }


        viewModel.questionData.observe(this) { questionData ->
            imageViewrabkin.setImageResource(questionData.imageResource)
            questionTextViewrabkin.text = "Вопрос ${questionData.questionNumber} из ${rabkinImages.size}"
            progressBarrabkin.progress = questionData.progress
            progressTextrabkin.text = "Question ${questionData.questionNumber} of ${rabkinImages.size}"

            for (i in 0..3) {
                val answerButtonFrameLayout = answerButtonsrabkin[i]
                val answerTextView = answerButtonFrameLayout.getChildAt(0) as TextView
                answerTextView.text = questionData.answerOptions[i]

                answerButtonFrameLayout.setOnClickListener {
                    viewModel.onAnswerSelected(questionData.answerOptions[i])
                }
            }
        }

        viewModel.resultData.observe(this) { resultData ->
            if (resultData != null) { // Проверка на null
                resultTextViewrabkin.text = "${resultData.resultText}\n" +
                        "Ваш балл: ${resultData.score}\n" +
                        "Тип аномалии: ${resultData.deficiencyType}"
            } else {
                resultTextViewrabkin.text = "" // Очищаем TextView, если resultData = null
            }
        }

        finishTestButtonrabkin.setOnClickListener {
            saverabkinResultAndFinish()
        }
        viewModel.buttonsEnabled.observe(this) { enabled ->
            answerButtonsrabkin.forEach { button ->
                button.isEnabled = enabled
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showQuestion() {
        val currentImage = rabkinImages[currentQuestionIndex]
        imageViewrabkin.setImageResource(currentImage.imageResource)
        val progress = (currentQuestionIndex + 1) * 100 / rabkinImages.size
        progressBarrabkin.progress = progress
        progressTextrabkin.text = "Question ${currentQuestionIndex + 1} of ${rabkinImages.size}"

        // Create a list of all answer options, including the correct answer and others
        val answerOptions = listOf(
            currentImage.correctAnswer,
            currentImage.answer2,
            currentImage.answer3,
            currentImage.answer4
        )

        // Shuffle the answer options to randomize their order on the buttons
        val shuffledAnswers = answerOptions.shuffled()

        // Set the text for each answer button
        for (i in 0..3) {
            val answerButtonFrameLayout = answerButtonsrabkin[i]
            val answerTextView = answerButtonFrameLayout.getChildAt(0) as TextView
            answerTextView.text = shuffledAnswers[i]

            answerButtonFrameLayout.setOnClickListener {
                onAnswerClick(shuffledAnswers[i], currentImage.correctAnswer)
            }
        }
    }

    private fun onAnswerClick(selectedAnswer: String, correctAnswer: String) {
        var scoreForQuestion = 0
        if (selectedAnswer == correctAnswer) {
            scoreForQuestion = 1
        } else {
            // Анализ неправильных ответов для определения типа аномалии
            when (selectedAnswer) {
                rabkinImages[currentQuestionIndex].answer2 -> {
                    // Проверка на протаномалию
                    if (currentQuestionIndex in listOf(2, 3, 8, 9, 12, 14, 15, 16, 18, 19, 21, 22)) {
                        protanomalyCount++
                    }
                }
                rabkinImages[currentQuestionIndex].answer3 -> {
                    // Проверка на дейтеранопию
                    if (currentQuestionIndex in listOf(2, 3, 8, 9, 12, 13, 14, 16, 17, 18, 19, 20, 22)) {
                        deuteranomalyCount++
                    }
                }
                rabkinImages[currentQuestionIndex].answer4 -> {
                    // Проверка на тританомалию
                    if (currentQuestionIndex in listOf(0, 10, 15, 17, 27)) {
                        tritanomalyCount++
                    }
                }
                "Ничего" -> {
                    // Проверка на протанопию и дейтеранопию
                    if (currentQuestionIndex in listOf(5, 20, 23, 24, 25, 26, 27)) {
                        protanopiaCount++
                        deuteranopiaCount++
                    }
                }
            }
        }

        totalScore += scoreForQuestion
        answeredQuestions++

        // Проверка, все ли вопросы отвечены
        if (answeredQuestions >= rabkinImages.size) {
            enableFinishButton()
            showResult()
            disableAnswerButtons() // Отключаем кнопки ответов
        } else {
            currentQuestionIndex++
            showQuestion()
        }
        viewModel.onAnswerSelected(selectedAnswer)
    }

    @SuppressLint("SetTextI18n")
    private fun showResult() {
        val resultText = when (totalScore) {
            in 0..10 -> "Монохромазия"
            in 11..16 -> "Аномальная трихромазия (сильная степень)"
            in 17..21 -> "Аномальная трихромазия (средняя степень)"
            in 22..26 -> "Аномальная трихромазия (слабая степень)"
            27 -> "Нормальное цветовое зрение"
            else -> "Монохромазия или симуляция"
        }

        // Определение типа аномалии
        val deficiencyType = getDeficiencyType(totalScore, protanomalyCount, deuteranomalyCount, tritanomalyCount, protanopiaCount, deuteranopiaCount)

        // Отображение результата
        if (deficiencyType.isNotEmpty()) {
            resultTextViewrabkin.text = "$resultText\nТип аномалии: $deficiencyType\n" +
                    "Ваш балл: $totalScore"
        } else {
            resultTextViewrabkin.text = "$resultText\nВаш балл: $totalScore"
        }
    }

    // Функция для определения типа аномалии
    private fun getDeficiencyType(
        totalScore: Int,
        protanomalyCount: Int,
        deuteranomalyCount: Int,
        tritanomalyCount: Int,
        protanopiaCount: Int,
        deuteranopiaCount: Int
    ): String {
        if (totalScore in 11..26) {
            return when {
                protanomalyCount > deuteranomalyCount && protanomalyCount > tritanomalyCount -> "Протаномалия"
                deuteranomalyCount > protanomalyCount && deuteranomalyCount > tritanomalyCount -> "Дейтераномалия"
                tritanomalyCount > protanomalyCount && tritanomalyCount > deuteranomalyCount -> "Тритономалия"
                protanopiaCount > deuteranopiaCount -> "Протанопия"
                deuteranopiaCount > protanopiaCount -> "Дейтеранопия"
                else -> "Невозможно определить тип аномалии"
            }
        }
        return ""
    }

    private fun disableAnswerButtons() {
        for (button in answerButtonsrabkin) {
            button.isEnabled = false
        }
    }

    private fun enableFinishButton() {
        finishTestButtonrabkin.isEnabled = true
    }

    private fun disableFinishButton() {
        finishTestButtonrabkin.isEnabled = false
    }

    private fun enableAnswerButtons() {
        for (button in answerButtonsrabkin) {
            button.isEnabled = true
        }
    }

    private fun saverabkinResultAndFinish() {
        val resultText = resultTextViewrabkin.text.toString()
        val score = totalScore

        // Create RabkinResult object
        val rabkinResult = RabkinResult(
            patientProfileId = -1, // ID will be set later
            score = score,
            resultText = resultText
        )

        // Return to SecondActivity and pass Rabkin result
        val intent = Intent(this, SecondActivity::class.java)
        intent.putExtra("rabkinResult", rabkinResult)
        setResult(RESULT_OK, intent)
        finish()
    }
}