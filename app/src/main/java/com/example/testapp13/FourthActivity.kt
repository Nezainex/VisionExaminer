package com.example.testapp13

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class FourthActivity : AppCompatActivity() {

    private lateinit var questionTextView: TextView
    private lateinit var resultTextView: TextView
    private lateinit var answerButtons: List<FrameLayout>
    private lateinit var resetButton: FrameLayout
    private lateinit var imageViewDryEye: ImageView
    private lateinit var progressBardryeye: ProgressBar
    private lateinit var progressTextdryeye: TextView
    private lateinit var finishTestButton: FrameLayout
    private val viewModel: FourthActivityViewModel by viewModels()
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
    private var isNightMode = true
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
            setContentView(R.layout.activity_fourth_landscape)
        } else {
            setContentView(R.layout.activity_fourth)
        }
        supportActionBar?.hide()
        window.decorView.setBackgroundColor(backgroundColor)

        // Инициализация UI элементов
        questionTextView = findViewById(R.id.textViewQuestion)
        resultTextView = findViewById(R.id.textViewResult)
        resetButton = findViewById(R.id.resetButton)
        imageViewDryEye = findViewById(R.id.imageViewdryeye)
        progressBardryeye = findViewById(R.id.progressBardryeye)
        progressTextdryeye = findViewById(R.id.progressTextdryeye)
        finishTestButton = findViewById(R.id.finishTestButton)

        answerButtons = listOf(
            findViewById(R.id.buttonAlways),
            findViewById(R.id.buttonMostofthetime),
            findViewById(R.id.buttonHalftime),
            findViewById(R.id.buttonSometimes),
            findViewById(R.id.buttonNever),
            findViewById(R.id.buttonCantTell)
        )

        // Наблюдатели LiveData
        viewModel.currentQuestionIndex.observe(this) { index ->
            updateQuestion(index)
        }
        viewModel.totalScore.observe(this) {
            // В данном случае score не используется,
            // так как результат вычисляется в конце теста
        }
        viewModel.resultText.observe(this) { result ->
            resultTextView.text = result
            updateResultImage(result)
        }
        viewModel.isFinishButtonEnabled.observe(this) { enabled ->
            finishTestButton.isEnabled = enabled
        }

        // Обработчики нажатий
        resetButton.setOnClickListener { viewModel.resetQuiz() }

        for (i in answerOptions.indices) {
            val textView = answerButtons[i].getChildAt(0) as TextView
            textView.text = answerOptions[i].first
            answerButtons[i].setOnClickListener {
                viewModel.onAnswerClick(answerOptions[i].second)
            }
        }

        finishTestButton.setOnClickListener {
            saveOsdiResultAndFinish()
        }

        // Вызов updateQuestion после инициализации UI
        updateQuestion(viewModel.currentQuestionIndex.value ?: 0)
    }

    // Методы обновления UI
    @SuppressLint("SetTextI18n")
    private fun updateQuestion(index: Int) {
        if (index < viewModel.questions.size) {
            val currentQuestion = viewModel.questions[index]
            questionTextView.text = currentQuestion
            val progress = (index + 1) * 100 / viewModel.questions.size
            progressBardryeye.progress = progress
            progressTextdryeye.text = "Question ${index + 1} of ${viewModel.questions.size}"
            imageViewDryEye.setImageResource(imageResources[index])
            enableAnswerButtons()
        } else {
            disableAnswerButtons()
        }
    }

    private fun updateResultImage(result: String) {
        val imageResource = when (result) {
            "" -> R.drawable.dryeye1 // Отображаем dryeye1, если result пустой
            "Нормальная поверхность глаза." -> R.drawable.norm
            "Лёгкая степень заболевания поверхности глаза." -> R.drawable.soft
            "Средняя степень заболевания поверхности глаза." -> R.drawable.moderate
            else -> R.drawable.severe
        }
        imageViewDryEye.setImageResource(imageResource)
    }

    private fun disableAnswerButtons() {
        for (button in answerButtons) {
            button.isEnabled = false
        }
    }

    private fun enableAnswerButtons() {
        for (button in answerButtons) {
            button.isEnabled = true
        }
    }

    private fun saveOsdiResultAndFinish() {
        val osdiResult = OsdiResult(
            patientProfileId = -1, // ID будет установлен позже
            score = viewModel.totalScore.value ?: 0,
            resultText = viewModel.resultText.value ?: ""
        )
        val intent = Intent(this, SecondActivity::class.java)
        intent.putExtra("osdiResult", osdiResult)
        setResult(RESULT_OK, intent)
        finish()
    }
}