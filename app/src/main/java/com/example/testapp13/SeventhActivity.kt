package com.example.testapp13

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SeventhActivity : AppCompatActivity() {

    private lateinit var questionTextViewishihara: TextView
    private lateinit var resultTextViewishihara: TextView
    private lateinit var answerButtonsishihara: List<Button>
    private lateinit var resetButtonishihara: Button
    private lateinit var finishTestButtonishihara: Button
    private lateinit var imageViewishihara: ImageView
    private lateinit var progressBarishihara: ProgressBar
    private lateinit var progressTextishihara: TextView

    // Data class for Ishihara images and answers
    data class IshiharaImage(
        val imageResource: Int,
        val correctAnswer: String,
        val answer2: String,
        val answer3: String,
        val answer4: String,
    )

    // List of Ishihara images and answers (fill in the missing answers)
    private val ishiharaImages = listOf(
        // Image 1: Everyone should see number 12.
        IshiharaImage(
            R.drawable.ishihara1,
            "1 и 2 (12)",
            "33",
            "41",
            "Ничего",
        ),
        // Image 2: Normal view: 8. Deuteranopia/Protanopia see 3
        IshiharaImage(
            R.drawable.ishihara2,
            "8", // Normal view: 8
            "3",  //  Deuteranopia/Protanopia
            "ничего", // All deficiencies
            "ничего", // All deficiencies
        ),
        // Image 3: Normal vision sees "6", Deuteranopia/Protanopia see "5"
        IshiharaImage(
            R.drawable.ishihara3,
            "6",
            "5", // Deuteranopia/Protanopia
            "Треугольник",// All deficiencies
            "Ничего",// All deficiencies
        ),
        // Image 4: Normal vision sees 29, Deuteranopia/Protanopia see 70
        IshiharaImage(
            R.drawable.ishihara4,
            "29",
            "70", // Deuteranopia/Protanopia
            "15",// All deficiencies
            "Ничего",// All deficiencies
        ),
        // Image 5: Normal vision sees "5 and 7 (57)", Deuteranopia/Protanopia see "35"
        IshiharaImage(
            R.drawable.ishihara5,
            "5 и 7 (57)", // Normal
            "35",// Deuteranopia/Protanopia
            "Ромб",
            "Ничего",
        ),
        // Image 6: Normal vision sees 5, Deuteranopia/Protanopia see 2
        IshiharaImage(
            R.drawable.ishihara6,
            "5", // Normal vision sees 5
            "2", // Deuteranopia/Protanopia see 2
            "10",
            "Ничего",
        ),
        // Image 7: Normal vision and color vision deficiency see "3", Deuteranopia only 5
        IshiharaImage(
            R.drawable.ishihara7,
            "3", // Normal vision sees 3
            "5",  // Deuteranopia/Protanopia see 5
            "Ничего",
            "Квадрат",
        ),
        // Image 8: Normal see "15" , Deuteranopia/Protanopia see 17
        IshiharaImage(
            R.drawable.ishihara8,
            "15",
            "Ничего",
            "17",
            "8",
        ),
        // Image 9: Normal and Deuteranomaly see "74", Deuteranopia/Protanopia see 21
        IshiharaImage(
            R.drawable.ishihara9,
            "74",
            "21",
            "Ничего",
            "9",
        ),
        // Image 10: Normal sees "2", Deuteranopia/Protanopia don’t see anything or
        //see something wrong.
        IshiharaImage(
            R.drawable.ishihara10,
            "2",
            "Ничего",
            "круг",
            "18",
        ),
        // Image 11: Normal sees "6", Deuteranopia/Protanopia don’t see anything or
        IshiharaImage(
            R.drawable.ishihara11,
            "6",
            "Ничего",
            "Круг",
            "12",
        ),
        // Image 12: Normal see "9 and 7 (97)", Deuteranopia/Protanopia see neither
        IshiharaImage(
            R.drawable.ishihara12,
            "9 и 7 (97)",
            "Ничего",
            "Круг",
            "21",
        ),
        // Image 13: Normal see "4 and 5 (45)", Deuteranopia/Protanopia see neither
        IshiharaImage(
            R.drawable.ishihara13,
            "4 и 5 (45)",
            "Ничего",
            "Квадрат",
            "32",
        ),
        // Image 14: Normal sees "5", Deuteranopia/Protanopia see neither
        IshiharaImage(
            R.drawable.ishihara14,
            "5",
            "Ничего",
            "Круг",
            "24",
        ),
        // Image 15:  Normal sees "7", Deuteranopia/Protanopia see neither
        IshiharaImage(
            R.drawable.ishihara15,
            "7",
            "Ничего",
            "Квадрат",
            "16",
        ),
        // Image 16: Normal sees "1" and "6" (16), Deuteranopia/Protanopia see neither
        IshiharaImage(
            R.drawable.ishihara16,
            "1 и 6 (16)",
            "4",
            "8",
            "Ничего",
        ),
        // Image 17: Normal sees "7" and "3" (73), Deuteranopia/Protanopia see neither
        IshiharaImage(
            R.drawable.ishihara17,
            "7 и 3 (73)",
            "Треугольник",
            "24",
            "Ничего",
        ),
        // Image 18: Normal sees nothing , Deuteranopia/Protanopia see 5
        IshiharaImage(
            R.drawable.ishihara18,
            "Ничего",
            "5",
            "6",
            "8",
        ),
        // Image 19: Normal sees nothing , Deuteranopia/Protanopia see 2
        IshiharaImage(
            R.drawable.ishihara19,
            "Ничего",
            "2",
            "46",
            "Круг",
        ),
        // Image 20:  Normal sees nothing , Deuteranopia/Protanopia see 45
        IshiharaImage(
            R.drawable.ishihara20,
            "Ничего",
            "45",
            "71",
            "Круг",
        ),
        // Image 21:  Normal sees nothing , Deuteranopia/Protanopia see 73
        IshiharaImage(
            R.drawable.ishihara21,
            "Ничего",
            "73",
            "40",
            "Круг",
        ),
        // Image 22:  Normal and Protanopia see "2 and 6 (26)", Deuteranopia or Deuteranomaly  see "2", Protanopia or Protanomaly see "6"
        IshiharaImage(
            R.drawable.ishihara22,
            "2 и 6 (26)", // Normal
            "Ничего",
            "6", // Protanopia or Protanomaly see "6"
            "2", // Deuteranopia or Deuteranomaly  see "2"
        ),
        // Image 23: Normal and Protanopia see "4 and 2 (42)", Deuteranopia or Deuteranomaly  see "4", Protanopia or Protanomaly see "2"
        IshiharaImage(
            R.drawable.ishihara23,
            "4 и 2 (42)", // Normal
            "Ничего",
            "2", // Protanopia or Protanomaly see "2"
            "4", // Deuteranopia or Deuteranomaly  see "4"
        ),
        // Image 24: Normal and Protanopia see "3 and 5 (35)", Deuteranopia or Deuteranomaly  see "3", Protanopia or Protanomaly see "5"
        IshiharaImage(
            R.drawable.ishihara24,
            "3 и 5 (35)",
            "Ничего",
            "5", // Protanopia or Protanomaly see "5"
            "3", // Deuteranopia or Deuteranomaly  see "3"
        ),
        // Image 25: Normal and Protanopia see "9 and 6 (96)", Deuteranopia or Deuteranomaly  see "9", Protanopia or Protanomaly see "6"
        IshiharaImage(
            R.drawable.ishihara25,
            "9 и 6 (96)",
            "Ничего",
            "6", // Protanopia or Protanomaly see "5"
            "9", // Deuteranopia or Deuteranomaly  see "3"
        ),
        // Image 26: Normal and Protanopia see purple and red spots, Deuteranopia or Deuteranomaly  see only the red line, Protanopia or Protanomaly see only the purple line
        IshiharaImage(
            R.drawable.ishihara26,
            "Розовая и красная линия",
            "Ничего",
            "Розовая линия", // Protanopia or Protanomaly see only the purple line
            "Красная линия", // Deuteranopia or Deuteranomaly  see only the red line
        ),
        // Image 27: Normal and Protanopia see purple and red spots, Deuteranopia or Deuteranomaly  see only the red line, Protanopia or Protanomaly see only the purple line
        IshiharaImage(
            R.drawable.ishihara27,
            "Розовая и красная линия",
            "Ничего",
            "Розовая линия", // Protanopia or Protanomaly see only the purple line
            "Красная линия", // Deuteranopia or Deuteranomaly  see only the red line
        ),
        // Image 28:  Normal sees nothing, Deuteranopia/Protanopia see line
        IshiharaImage(
            R.drawable.ishihara28,
            "Ничего", // Normal sees nothing
            "Линия", // Deuteranopia/Protanopia see line
            "22", //
            "Круг", //
        ),
        // Image 29:  Normal sees nothing, Deuteranopia/Protanopia see line
        IshiharaImage(
            R.drawable.ishihara29,
            "Ничего", // Normal sees nothing
            "Линия", // Deuteranopia/Protanopia see line
            "42", //
            "Квадрат", //
        ),
        // Image 30:  Normal sees line, Deuteranopia/Protanopia see nothing
        IshiharaImage(
            R.drawable.ishihara30,
            "Линия", // Normal sees line
            "Ничего", // Deuteranopia/Protanopia see line
            "13", //
            "Круг", //
        ),
        // Image 31:  Normal sees line, Deuteranopia/Protanopia see nothing
        IshiharaImage(
            R.drawable.ishihara31,
            "Линия", // Normal sees line
            "Ничего", // Deuteranopia/Protanopia see line
            "54", //
            "Треугольник", //
        ),
        // Image 32:  Normal sees line, Deuteranopia/Protanopia see nothing
        IshiharaImage(
            R.drawable.ishihara32,
            "Линия", // Normal sees line
            "Ничего", // Deuteranopia/Protanopia see line
            "33", //
            "Квадрат", //
        ),
        // Image 33:  Normal sees line, Deuteranopia/Protanopia see nothing
        IshiharaImage(
            R.drawable.ishihara33,
            "Линия", // Normal sees line
            "Ничего", // Deuteranopia/Protanopia see line
            "8", //
            "Круг", //
        ),
        // Image 34:  Normal sees  blue-green and yellow-green line, Deuteranopia/Protanopia see only red-green and violet line
        IshiharaImage(
            R.drawable.ishihara34,
            "Сине-зелёная и жёлто-зелёная линия", //blue-green and yellow-green line
            "Красно-зелёная и фиолетовая линия", // Deuteranopia/Protanopia see only red-green and violet line
            "Ничего", //
            "Квадрат", //
        ),
        // Image 35:  Normal sees  blue-green and yellow-green line, Deuteranopia/Protanopia see only red-green and violet line
        IshiharaImage(
            R.drawable.ishihara35,
            "Сине-зелёная и жёлто-зелёная линия", //blue-green and yellow-green line
            "Красно-зелёная и фиолетовая линия", // Deuteranopia/Protanopia see only red-green and violet line
            "Ничего", //
            "Треугольник", //
        ),
        // Image 36:  Normal sees violet and orange line, Deuteranopia/Protanopia see blue-green and violet line
        IshiharaImage(
            R.drawable.ishihara36,
            "Розовато-оранжевая линия", // Normal sees violet and orange line
            "Голубо-зелёная и розовая линия", // Deuteranopia/Protanopia see blue-green and violet line
            "Ничего", //
            "Треугольник", //
        ),
        // Image 37:  Normal sees violet and orange line, Deuteranopia/Protanopia see blue-green and violet line
        IshiharaImage(
            R.drawable.ishihara37,
            "Розовато-оранжевая линия", // Normal sees violet and orange line
            "Голубо-зелёная и розовая линия", // Deuteranopia/Protanopia see blue-green and violet line
            "Ничего", //
            "Треугольник", //
        ),
        // Image 38:  Normal sees line, Deuteranopia/Protanopia also sees line
        IshiharaImage(
            R.drawable.ishihara38,
            "Линия", // Normal sees line, Deuteranopia/Protanopia also sees line
            "Круг", //
            "Ничего", //
            "14", //
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seventh)
        supportActionBar?.hide() // Hide the activity title

        // Initialize views
        questionTextViewishihara = findViewById(R.id.textViewQuestionishihara)
        resultTextViewishihara = findViewById(R.id.textViewResultishihara)
        finishTestButtonishihara = findViewById(R.id.finishTestButtonishihara)
        resetButtonishihara = findViewById(R.id.resetButtonishihara)
        imageViewishihara = findViewById(R.id.imageViewishihara)
        answerButtonsishihara = listOf(
            findViewById(R.id.buttonAnswer1),
            findViewById(R.id.buttonAnswer2),
            findViewById(R.id.buttonAnswer3),
            findViewById(R.id.buttonAnswer4),
        )
        progressBarishihara = findViewById(R.id.progressBarishihara)
        progressTextishihara = findViewById(R.id.progressTextishihara)
        resetButtonishihara.setOnClickListener { resetQuiz() }
        showQuestion()
        disableFinishButton()
        finishTestButtonishihara.setOnClickListener {
            saveishiharaResultAndFinish()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showQuestion() {
        val currentImage = ishiharaImages[currentQuestionIndex]
        imageViewishihara.setImageResource(currentImage.imageResource)
        val progress = (currentQuestionIndex + 1) * 100 / ishiharaImages.size
        progressBarishihara.progress = progress
        progressTextishihara.text = "Question ${currentQuestionIndex + 1} of ${ishiharaImages.size}"
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
            answerButtonsishihara[i].text = shuffledAnswers[i]
            answerButtonsishihara[i].setOnClickListener {
                onAnswerClick(shuffledAnswers[i], currentImage.correctAnswer)
            }
        }
    }

    private fun onAnswerClick(selectedAnswer: String, correctAnswer: String) {
        var scoreForQuestion = 0
        if (selectedAnswer == correctAnswer) {
            scoreForQuestion = 1
        } else {
            // Analyze incorrect answers for deficiency type
            when (selectedAnswer) {
                ishiharaImages[currentQuestionIndex].answer2 -> {
                    // Check for Deuteranopia/Protanopia
                    if (currentQuestionIndex in listOf(1, 2, 3, 4, 5, 6, 8, 9, 17, 18, 19, 20, 21, 28, 29)) {
                        deuteranopiaCount++
                        protanopiaCount++
                    }
                }
                ishiharaImages[currentQuestionIndex].answer3 -> {
                    // Check for Protanomaly/Protanopia
                    if (currentQuestionIndex in listOf(22, 23, 24, 25, 26, 27)) {
                        protanomalyCount++
                        protanopiaCount++
                    }
                }
                ishiharaImages[currentQuestionIndex].answer4 -> {
                    // Check for Deuteranomaly/Deuteranopia
                    if (currentQuestionIndex in listOf(22, 23, 24, 25, 26, 27)) {
                        deuteranomalyCount++
                        deuteranopiaCount++
                    }
                }
            }
        }
        totalScore += scoreForQuestion
        answeredQuestions++

        // Check if all questions answered
        if (answeredQuestions >= ishiharaImages.size) {
            enableFinishButton()
            showResult()
            disableAnswerButtons()
        } else {
            currentQuestionIndex++
            showQuestion()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showResult() {
        val resultText = when (totalScore) {
            in 0..10 -> "Явные симптомы нарушения цветового зрения"
            in 11..21 -> "Умеренные симптомы нарушения цветового зрения"
            in 22..30 -> "Слабые симптомы нарушения цветового зрения."
            in 31..38 -> "Нормальное цветовое зрение"
            else -> "Неубедительный результат" // Should not happen
        }

        val deficiencyType = getDeficiencyType(protanomalyCount, deuteranomalyCount, protanopiaCount, deuteranopiaCount)

        // Display result
        if (deficiencyType.isNotEmpty()) {
            resultTextViewishihara.text = "$resultText\nВероятный тип нарушения цветового зрения: $deficiencyType\n" +
                    "Ваш балл: $totalScore"
        } else {
            resultTextViewishihara.text = "$resultText\nВаш балл: $totalScore"
        }
    }

    // Function to determine deficiency type
    private fun getDeficiencyType(
        protanomalyCount: Int,
        deuteranomalyCount: Int,
        protanopiaCount: Int,
        deuteranopiaCount: Int
    ): String {
        if (totalScore < 31) { // Only analyze if there's a deficiency
            return when {
                protanomalyCount >= 2 || protanopiaCount >= 2  -> "Протанопия или протаномалия"
                deuteranomalyCount >= 2 || deuteranopiaCount >= 2 -> "Дейтеранопия или дейтераномалия"
                else -> "Невозможно определить тип аномалии"
            }
        }
        return ""
    }

    private fun disableAnswerButtons() {
        for (button in answerButtonsishihara) {
            button.isEnabled = false
        }
    }

    private fun enableFinishButton() {
        val finishTestButton = findViewById<Button>(R.id.finishTestButtonishihara)
        finishTestButton.isEnabled = true
    }

    private fun disableFinishButton() {
        val finishTestButton = findViewById<Button>(R.id.finishTestButtonishihara)
        finishTestButton.isEnabled = false
    }

    private fun resetQuiz() {
        currentQuestionIndex = 0
        totalScore = 0
        answeredQuestions = 0 // Сброс счётчика отвеченных вопросов
        protanomalyCount = 0   // Сброс счётчиков аномалий
        deuteranomalyCount = 0
        tritanomalyCount = 0
        protanopiaCount = 0
        deuteranopiaCount = 0
        enableAnswerButtons()
        disableFinishButton()
        showQuestion() // Вызываем showQuestion() только один раз
        resultTextViewishihara.text = ""  // Очищаем resultTextView
    }

    private fun enableAnswerButtons() {
        for (button in answerButtonsishihara) {
            button.isEnabled = true
        }
    }

    private fun saveishiharaResultAndFinish() {
        val resultText = resultTextViewishihara.text.toString()
        val score = totalScore

        // Create IshiharaResult object
        val ishiharaResult = IshiharaResult(
            patientProfileId = -1, // ID will be set later
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

