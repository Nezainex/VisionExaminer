package com.example.VisionExaminer

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SixthActivityViewModel : ViewModel() {

    // Данные для теста Рабкина
    private val rabkinImages = listOf(
        // Image 1: Normal vision and color vision deficiency see "96"
        SixthActivity.RabkinImage(
            R.drawable.rabkin1,
            "9 и 6 (96)",
            "56",
            "90",
            "Ничего",
        ),
        // Image 2: Normal vision and color vision deficiency see triangle and circle
        SixthActivity.RabkinImage(
            R.drawable.rabkin2,
            "Треугольник и круг",
            "Ничего",  // All deficiencies
            "Квадрат", // All deficiencies
            "Два ромба", // All deficiencies
        ),
        // Image 3: Normal vision sees "9", Deuteranopia/Protanopia see "5"
        SixthActivity.RabkinImage(
            R.drawable.rabkin3,
            "9",
            "5", // Deuteranopia/Protanopia
            "Треугольник",// All deficiencies
            "Ничего",// All deficiencies
        ),
        // Image 4: Normal vision sees triangle, Deuteranopia/Protanopia see circle
        SixthActivity.RabkinImage(
            R.drawable.rabkin4,
            "Треугольник",
            "Круг", // Deuteranopia/Protanopia
            "13",// All deficiencies
            "Ничего",// All deficiencies
        ),
        // Image 5: Normal vision sees "1 and 3 (13)", Deuteranopia/Protanopia see "6"
        SixthActivity.RabkinImage(
            R.drawable.rabkin5,
            "1 и 3 (13)", // Image 5: Normal
            "6",// Deuteranopia/Protanopia
            "Круг",
            "Ничего",
        ),
        // Image 6: Normal vision sees triangle and circle, Deuteranopia/Protanopia see neither
        SixthActivity.RabkinImage(
            R.drawable.rabkin6,
            "Треугольник и круг",
            "Ничего",
            "10",
            "96",
        ),
        // Image 7: Normal vision and color vision deficiency see "9" and "6", Deuteranopia only 6   ОШИБКА - ПЕРЕДЕЛАТЬ ИНТЕРПРЕТАЦИЮ ОТВЕТА
        SixthActivity.RabkinImage(
            R.drawable.rabkin7,
            "9 и 6 (96)",
            "6",
            "Ничего",
            "Квадрат",
        ),
        // Image 8: Normal and Deuteranopia/Protanopia see "5" (difficult for latter)
        SixthActivity.RabkinImage(
            R.drawable.rabkin8,
            "5",
            "Ничего",
            "6",
            "8",
        ),
        // Image 9: Normal and Deuteranomaly see "9", Protanomaly may see "9", "8", or "6"
        SixthActivity.RabkinImage(
            R.drawable.rabkin9,
            "9",
            "6",
            "Ничего",
            "8",
        ),
        // Image 10: Normal sees "136", Deuteranopia/Protanopia see "69", "68", or "66"
        SixthActivity.RabkinImage(
            R.drawable.rabkin10,
            "1, 3 и 6 (136)",
            "69",
            "68",
            "66",
        ),
        // Image 11: Normal sees a circle and a triangle, Protanopia sees  a triangle,  Deuteranopia sees a circle, or a circle and a triangle.
        SixthActivity.RabkinImage(
            R.drawable.rabkin11,
            "Круг и треугольник",
            "Круг",
            "Треугольник",
            "Ничего",
        ),
        // Image 12: Normal and Deuteranomaly see "1 and 2 (12)", Protanopia see neither
        SixthActivity.RabkinImage(
            R.drawable.rabkin12,
            "1 и 2 (12)",
            "Ничего",
            "Круг",
            "Треугольник",
        ),
        // Image 13: Normal sees circle and triangle, Protanopia sees circle, Deuteranopia sees triangle
        SixthActivity.RabkinImage(
            R.drawable.rabkin13,
            "Круг и треугольник",
            "Круг",
            "Треугольник",
            "Ничего",
        ),
        // Image 14: Normal sees "3 and 0 (30)", Protanopia sees "10" and "6", Deuteranopia sees "1" and "6"
        SixthActivity.RabkinImage(
            R.drawable.rabkin14,
            "3 и 0 (30)",
            "6; 1 и 0 (10)",
            "6 и 1",
            "Ничего",
        ),
        // Image 15: Normal sees circle and triangle, Protanopia sees 2 triangles and square, Deuteranopia sees triangle and square
        SixthActivity.RabkinImage(
            R.drawable.rabkin15,
            "Круг и треугольник",
            "2 треугольника и квадрат",
            "Треугольник и квадрат",
            "Квадрат",
        ),
        // Image 16: Normal sees "9" and "6" (96), Protanopia sees "9", Deuteranopia sees "6"
        SixthActivity.RabkinImage(
            R.drawable.rabkin16,
            "9 и 6 (96)",
            "9",
            "6",
            "Ничего",
        ),
        // Image 17: Normal sees circle and triangle, Protanopia sees triangle, Deuteranopia sees circle
        SixthActivity.RabkinImage(
            R.drawable.rabkin17,
            "Круг и треугольник",
            "Треугольник",
            "Круг",
            "Квадрат",
        ),
        // Image 18: Normal sees the horizontal rows in the table of eight squares each (color rows 9th, 10th, 11th, 12th, 13th, 14th, 15th and 16th) as monochromatic ; vertical rows are perceived by them as multi-colored. Dichromats perceive vertical rows as monochromatic, and protanopes perceive vertical color rows as monochromatic - 3rd, 5th and 7th, and deuteranopes - vertical color rows - 1st, 2nd, 4th, 6th th and 8th. Colored squares located horizontally are perceived by protanopes and deuteranopes as multi-colored.
        SixthActivity.RabkinImage(
            R.drawable.rabkin18,
            "Одноцветные горизонтальные ряды",
            "Одноцветные вертикальные ряды: 3-й, 5-й и 7-й",
            "Одноцветные вертикальные ряды: 1-й, 2-й, 4-й, 6-й и 8-й",
            "Одноцветные горизонтальные и вертикальные ряды",
        ),
        // Image 19: Normal sees 9 and 5 (95). Deuteranopia/Protanopia see only the number 5.
        SixthActivity.RabkinImage(
            R.drawable.rabkin19,
            "9 и 5 (95)",
            "5",
            "Круг",
            "22",
        ),
        // Image 20: Normal sees triangle and circle, Deuteranopia/Protanopia see neither
        SixthActivity.RabkinImage(
            R.drawable.rabkin20,
            "Треугольник и круг",
            "Ничего",
            "Квадрат",
            "Ромб",
        ),
        // Image 21: Normal sees 66. Deuteranopia/Protanopia see neither    (вопросы 21 и 22 в таблице рабкина в pdf файле идут в порядке 22 , 21 ) , а на сайте рабкин.ру они в том же порядке, как у меня в приложении сейчас.
        SixthActivity.RabkinImage(
            R.drawable.rabkin21,
            "66",
            "Ничего",
            "90",
            "22",
        ),
        // Image 22: Normal sees colored horizontal and single-color vertical rows. Specific details for Deuteranopia/Protanopia
        SixthActivity.RabkinImage(
            R.drawable.rabkin22,
            "Одноцветные вертикальные ряды, горизонтальные - цветные",
            "Одноцветные горизонтальные ряды, вертикальные - разноцветные",
            "Нет одноцветных рядов",
            "Все квадраты одного цвета",
        ),
        // Image 23: Normal and Protanopia see "3 and 6 (36)", Deuteranopia/Protanopia see see neither
        SixthActivity.RabkinImage(
            R.drawable.rabkin23,
            "3 и 6 (36)",
            "Ничего",
            "Круг",
            "9 и 8 (98)",
        ),
        // Image 24: Normal see "1 and 4 (14)", Deuteranopia/Protanopia see see neither
        SixthActivity.RabkinImage(
            R.drawable.rabkin24,
            "1 и 4 (14)",
            "Ничего",
            "9 и 2 (92)",
            "Треугольник и круг",
        ),
        // Image 25: Normal see "9", Deuteranopia/Protanopia see neither
        SixthActivity.RabkinImage(
            R.drawable.rabkin25,
            "9",
            "Ничего",
            "Круг",
            "5",
        ),
        // Image 26: Normal see "4", Deuteranopia/Protanopia see neither
        SixthActivity.RabkinImage(
            R.drawable.rabkin26,
            "4",
            "Ничего",
            "Квадрат",
            "9",
        ),
        // Image 27: Normal see "13", Deuteranopia/Protanopia see neither
        SixthActivity.RabkinImage(
            R.drawable.rabkin27,
            "13",
            "Ничего",
            "Квадрат",
            "76",
        ),
    )

    // Текущий номер вопроса
    private var currentQuestionIndex = 0
    // Общий счет
    private var totalScore = 0
    // Количество отвеченных вопросов
    private var answeredQuestions = 0
    // Счетчики аномалий
    private var protanomalyCount = 0
    private var deuteranomalyCount = 0
    private var tritanomalyCount = 0
    private var protanopiaCount = 0
    private var deuteranopiaCount = 0

    // LiveData для отображения вопросов
    val questionData = MutableLiveData<QuestionData>()
    // LiveData для отображения результата
    val resultData = MutableLiveData<ResultData?>()

    // Данные для отображения текущего вопроса
    data class QuestionData(
        val imageResource: Int,
        val questionNumber: Int,
        val answerOptions: List<String>,
        val progress: Int
    )

    // Данные для отображения результата теста
    data class ResultData(
        val resultText: String,
        val score: Int,
        val deficiencyType: String
    )
    // LiveData для отслеживания состояния кнопок
    val buttonsEnabled = MutableLiveData(true)

    // Инициализация LiveData с первым вопросом
    init {
        showNextQuestion()
    }

    // Функция для показа следующего вопроса
    private fun showNextQuestion() {
        val currentImage = rabkinImages[currentQuestionIndex]
        buttonsEnabled.value = true
        // Создаем список вариантов ответов в случайном порядке
        val answerOptions = listOf(
            currentImage.correctAnswer,
            currentImage.answer2,
            currentImage.answer3,
            currentImage.answer4
        ).shuffled()

        // Заполняем LiveData данными для текущего вопроса
        questionData.value = QuestionData(
            imageResource = currentImage.imageResource,
            questionNumber = currentQuestionIndex + 1,
            answerOptions = answerOptions,
            progress = (currentQuestionIndex + 1) * 100 / rabkinImages.size
        )
        resultData.value = null // Сбрасываем resultData при переходе к новому вопросу
    }

    // Функция для обработки ответа пользователя
    fun onAnswerSelected(selectedAnswer: String) {
        var scoreForQuestion = 0
        if (selectedAnswer == rabkinImages[currentQuestionIndex].correctAnswer) {
            scoreForQuestion = 1
        } else {
        }

        totalScore += scoreForQuestion
        answeredQuestions++

        // Переход к следующему вопросу, если еще остались вопросы
        if (answeredQuestions < rabkinImages.size) {
            currentQuestionIndex++
            showNextQuestion()
        } else {
            // Вычисление и отображение результата
            showResult()
        }
    }

    // Функция для вычисления и отображения результата
    private fun showResult() {
        val resultText = when (totalScore) {
            in 0..10 -> "Монохромазия"
            in 11..16 -> "Аномальная трихромазия (сильная степень)"
            in 17..21 -> "Аномальная трихромазия (средняя степень)"
            in 22..26 -> "Аномальная трихромазия (слабая степень)"
            27 -> "Нормальное цветовое зрение"
            else -> "Монохромазия или симуляция"
        }
        val deficiencyType = getDeficiencyType(
            totalScore,
            protanomalyCount,
            deuteranomalyCount,
            tritanomalyCount,
            protanopiaCount,
            deuteranopiaCount
        )
        // Проверка, чтобы не обновлять TextView, если resultData.value = null
        if (resultData.value == null) {
            resultData.value = ResultData(
                resultText = resultText,
                score = totalScore,
                deficiencyType = deficiencyType
            )
        }
        buttonsEnabled.value = false // Отключаем кнопки после завершения теста
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

    // Функция для сброса теста
    fun resetQuiz() {
        currentQuestionIndex = 0
        totalScore = 0
        answeredQuestions = 0
        protanomalyCount = 0
        deuteranomalyCount = 0
        tritanomalyCount = 0
        protanopiaCount = 0
        deuteranopiaCount = 0
        showNextQuestion()
        buttonsEnabled.value = true // Включаем кнопки при начале нового теста
        resultData.value = null // Сбрасываем результат
    }
}