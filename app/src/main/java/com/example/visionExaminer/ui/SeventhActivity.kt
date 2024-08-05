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
import com.example.visionExaminer.data.IshiharaResult
import com.example.visionExaminer.viewmodel.SeventhActivityViewModel

class SeventhActivity : AppCompatActivity() {
    data class IshiharaImage(
        val imageResource: Int,
        val correctAnswer: String,
        val answer2: String,
        val answer3: String,
        val answer4: String,
    )

    private lateinit var questionTextViewishihara: TextView
    private lateinit var resultTextViewishihara: TextView
    private lateinit var answerButtonsishihara: List<FrameLayout>
    private lateinit var resetButtonishihara: FrameLayout
    private lateinit var finishTestButtonishihara: FrameLayout
    private lateinit var imageViewishihara: ImageView
    private lateinit var progressBarishihara: ProgressBar
    private lateinit var progressTextishihara: TextView

    private lateinit var viewModel: SeventhActivityViewModel

    private var isNightMode = true

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isNightMode = intent.getBooleanExtra("isNightMode", true)
        if (isNightMode) {
            setTheme(R.style.AppTheme_Night)
        } else {
            setTheme(R.style.AppTheme_Day)
        }
        val backgroundColor = if (isNightMode) {
            ContextCompat.getColor(this, R.color.black)
        } else {
            ContextCompat.getColor(this, R.color.white)
        }
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_seventh_landscape)
        } else {
            setContentView(R.layout.activity_seventh)
        }
        supportActionBar?.hide()
        window.decorView.setBackgroundColor(backgroundColor)

        questionTextViewishihara = findViewById(R.id.textViewQuestionishihara)
        resultTextViewishihara = findViewById(R.id.textViewResultishihara)
        finishTestButtonishihara = findViewById(R.id.finishTestButtonishihara)
        resetButtonishihara = findViewById(R.id.resetButtonishihara)
        imageViewishihara = findViewById(R.id.imageViewishihara)
        progressBarishihara = findViewById(R.id.progressBarishihara)
        progressTextishihara = findViewById(R.id.progressTextishihara)
        answerButtonsishihara = listOf(
            findViewById(R.id.buttonAnswer1),
            findViewById(R.id.buttonAnswer2),
            findViewById(R.id.buttonAnswer3),
            findViewById(R.id.buttonAnswer4)
        )

        viewModel = ViewModelProvider(this)[SeventhActivityViewModel::class.java]

        viewModel.questionData.observe(this) { questionData ->
            imageViewishihara.setImageResource(questionData.imageResource)
            questionTextViewishihara.text = "Вопрос ${questionData.questionNumber} из ${viewModel.getTotalQuestions()}"
            progressBarishihara.progress = questionData.progress
            progressTextishihara.text = "Question ${questionData.questionNumber} of ${viewModel.getTotalQuestions()}"

            for (i in 0..3) {
                val answerButtonFrameLayout = answerButtonsishihara[i]
                val answerTextView = answerButtonFrameLayout.getChildAt(0) as TextView
                answerTextView.text = questionData.answerOptions[i]

                answerButtonFrameLayout.setOnClickListener {
                    viewModel.onAnswerSelected(questionData.answerOptions[i])
                }
            }
        }

        viewModel.resultData.observe(this) { resultData ->
            if (resultData != null) {
                resultTextViewishihara.text = "${resultData.resultText}\n" +
                        "Ваш балл: ${resultData.score}\n" +
                        "Вероятный тип нарушения цветового зрения: ${resultData.deficiencyType}"
            } else {
                resultTextViewishihara.text = ""
            }
        }

        viewModel.buttonsEnabled.observe(this) { enabled ->
            answerButtonsishihara.forEach { button ->
                button.isEnabled = enabled
            }
        }

        resetButtonishihara.setOnClickListener {
            viewModel.resetQuiz()
        }

        finishTestButtonishihara.setOnClickListener {
            saveishiharaResultAndFinish()
        }
    }



    private fun saveishiharaResultAndFinish() {
        val resultText = resultTextViewishihara.text.toString()
        val score = viewModel.getScore() // Получаем score из ViewModel

        val ishiharaResult = IshiharaResult(
            patientProfileId = -1,
            score = score,
            resultText = resultText
        )

        // Return to SecondActivity and pass Ishihara result
        val intent = Intent(this, SecondActivity::class.java)
        intent.putExtra("ishiharaResult", ishiharaResult)
        setResult(RESULT_OK, intent)
        finish()
    }
}