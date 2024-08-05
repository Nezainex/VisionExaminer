package com.example.visionExaminer.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.visionExaminer.R
import com.example.visionExaminer.data.MidriaticResult
import com.example.visionExaminer.viewmodel.NinthActivityViewModel

class NinthActivity : AppCompatActivity() {

    //Кнопки
    private lateinit var tropicamideHalfAPercentButton: FrameLayout
    private lateinit var tropicamideOnePercentButton: FrameLayout
    private lateinit var mydriacylHalfAPercentButton: FrameLayout
    private lateinit var mydriacylOnePercentButton: FrameLayout
    private lateinit var cyclomedOnePercentButton: FrameLayout
    private lateinit var cyclopentolateOnePercentButton: FrameLayout
    private lateinit var atropineSulfateOnePercentButton: FrameLayout
    private lateinit var irifrinTwoAndAHalfPercentButton: FrameLayout
    private lateinit var irifrinWithoutPreservativeTwoAndAHalfPercentButton: FrameLayout
    private lateinit var mydrimaxTwoAndAHalfPercentButton: FrameLayout
    private lateinit var phenylephrineTenPercentButton: FrameLayout

    //Названия кнопок
    private lateinit var tropicamideHalfAPercentButtonTextView: TextView
    private lateinit var tropicamideOnePercentButtonTextView: TextView
    private lateinit var mydriacylHalfAPercentButtonTextView: TextView
    private lateinit var mydriacylOnePercentButtonTextView: TextView
    private lateinit var cyclomedOnePercentButtonTextView: TextView
    private lateinit var cyclopentolateOnePercentButtonTextView: TextView
    private lateinit var atropineSulfateOnePercentButtonTextView: TextView
    private lateinit var irifrinTwoAndAHalfPercentButtonTextView: TextView
    private lateinit var irifrinWithoutPreservativeTwoAndAHalfPercentButtonTextView: TextView
    private lateinit var mydrimaxTwoAndAHalfPercentButtonTextView: TextView
    private lateinit var phenylephrineTenPercentButtonTextView: TextView

    //Текст предупреждений рядом с кнопками
    private lateinit var tropicamideHalfAPercentTextView: TextView
    private lateinit var tropicamideOnePercentTextView: TextView
    private lateinit var mydriacylHalfAPercentTextView: TextView
    private lateinit var mydriacylOnePercentTextView: TextView
    private lateinit var cyclomedOnePercentTextView: TextView
    private lateinit var cyclopentolateOnePercentTextView: TextView
    private lateinit var atropineSulfateOnePercentTextView: TextView
    private lateinit var irifrinTwoAndAHalfPercentTextView: TextView
    private lateinit var irifrinWithoutPreservativeTwoAndAHalfPercentTextView: TextView
    private lateinit var mydrimaxTwoAndAHalfPercentTextView: TextView
    private lateinit var phenylephrineTenPercentTextView: TextView

    private var isNightMode = true
    private var currentGreenButtonId: Int = -1
    private lateinit var viewModel: NinthActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Получение информации о теме из Intent
        isNightMode = intent.getBooleanExtra("isNightMode", true)
        Log.d("NinthActivity", "isNightMode: $isNightMode")

        // Установка темы перед вызовом setContentView
        setTheme(if (isNightMode) R.style.AppTheme_Night else R.style.AppTheme_Day)
        Log.d("NinthActivity", "Theme set to: ${if (isNightMode) "Night" else "Day"}")
        val backgroundColor = if (isNightMode) ContextCompat.getColor(this, R.color.black) else ContextCompat.getColor(this,
            R.color.white
        )

        setContentView(R.layout.activity_ninth)
        supportActionBar?.hide()
        window.decorView.setBackgroundColor(backgroundColor)
        Log.d("NinthActivity", "Background color set")

        viewModel = ViewModelProvider(this)[NinthActivityViewModel::class.java]

        //Кнопки
        tropicamideOnePercentButton = findViewById(R.id.Tropicamide_one_percent_button)
        tropicamideHalfAPercentButton = findViewById(R.id.Tropicamide_half_a_percent_button)
        mydriacylOnePercentButton = findViewById(R.id.Mydriacyl_one_percent_button)
        mydriacylHalfAPercentButton = findViewById(R.id.Mydriacyl_half_a_percent_button)
        cyclomedOnePercentButton = findViewById(R.id.Cyclomed_one_percent_button)
        cyclopentolateOnePercentButton = findViewById(R.id.Cyclopentolate_one_percent_button)
        atropineSulfateOnePercentButton = findViewById(R.id.Atropine_sulfate_one_percent_button)
        irifrinTwoAndAHalfPercentButton = findViewById(R.id.Irifrin_two_and_a_half_percent_button)
        irifrinWithoutPreservativeTwoAndAHalfPercentButton = findViewById(R.id.Irifrin_without_preservative_two_and_a_half_percent_button)
        mydrimaxTwoAndAHalfPercentButton = findViewById(R.id.Mydrimax_two_and_a_half_percent_button)
        phenylephrineTenPercentButton = findViewById(R.id.Phenylephrine_ten_percent_button)

        //Названия кнопок
        tropicamideHalfAPercentButtonTextView = findViewById(R.id.Tropicamide_half_a_percent_button_TextView)
        tropicamideOnePercentButtonTextView = findViewById(R.id.Tropicamide_one_percent_button_TextView)
        mydriacylHalfAPercentButtonTextView = findViewById(R.id.Mydriacyl_half_a_percent_button_TextView)
        mydriacylOnePercentButtonTextView = findViewById(R.id.Mydriacyl_one_percent_button_TextView)
        cyclomedOnePercentButtonTextView = findViewById(R.id.Cyclomed_one_percent_button_TextView)
        cyclopentolateOnePercentButtonTextView = findViewById(R.id.Cyclopentolate_one_percent_button_TextView)
        atropineSulfateOnePercentButtonTextView = findViewById(R.id.Atropine_sulfate_one_percent_button_TextView)
        irifrinTwoAndAHalfPercentButtonTextView = findViewById(R.id.Irifrin_two_and_a_half_percent_button_TextView)
        irifrinWithoutPreservativeTwoAndAHalfPercentButtonTextView = findViewById(R.id.Irifrin_without_preservative_two_and_a_half_percent_button_TextView)
        mydrimaxTwoAndAHalfPercentButtonTextView = findViewById(R.id.Mydrimax_two_and_a_half_percent_button_TextView)
        phenylephrineTenPercentButtonTextView = findViewById(R.id.Phenylephrine_ten_percent_button_TextView)

        //Текст предупреждений рядом с кнопками
        tropicamideHalfAPercentTextView = findViewById(R.id.Tropicamide_half_a_percent_TextView)
        tropicamideOnePercentTextView = findViewById(R.id.Tropicamide_one_percent_TextView)
        mydriacylHalfAPercentTextView = findViewById(R.id.Mydriacyl_half_a_percent_TextView)
        mydriacylOnePercentTextView = findViewById(R.id.Mydriacyl_one_percent_TextView)
        cyclomedOnePercentTextView = findViewById(R.id.Cyclomed_one_percent_TextView)
        cyclopentolateOnePercentTextView = findViewById(R.id.Cyclopentolate_one_percent_TextView)
        atropineSulfateOnePercentTextView = findViewById(R.id.Atropine_sulfate_one_percent_TextView)
        irifrinTwoAndAHalfPercentTextView = findViewById(R.id.Irifrin_two_and_a_half_percent_TextView)
        irifrinWithoutPreservativeTwoAndAHalfPercentTextView = findViewById(R.id.Irifrin_without_preservative_two_and_a_half_percent_TextView)
        mydrimaxTwoAndAHalfPercentTextView = findViewById(R.id.Mydrimax_two_and_a_half_percent_TextView)
        phenylephrineTenPercentTextView = findViewById(R.id.Phenylephrine_ten_percent_TextView)

        setupMidriaticButtons()
        setInitialButtonState()

        ageWarning() // Вызываем функцию для проверки возраста
    }

    private fun setupMidriaticButtons() {
        tropicamideHalfAPercentButton.setOnClickListener {
            updateButtonBackground(tropicamideHalfAPercentButton)
            viewModel.updateMidriaticAgent("тропикамид 0,5%")
            saveMidriaticResultAndFinish()
        }

        tropicamideOnePercentButton.setOnClickListener {
            updateButtonBackground(tropicamideOnePercentButton)
            viewModel.updateMidriaticAgent("тропикамид 1%")
            saveMidriaticResultAndFinish()
        }

        mydriacylHalfAPercentButton.setOnClickListener {
            updateButtonBackground(mydriacylHalfAPercentButton)
            viewModel.updateMidriaticAgent("мидриацил 0,5%")
            saveMidriaticResultAndFinish()
        }

        mydriacylOnePercentButton.setOnClickListener {
            updateButtonBackground(mydriacylOnePercentButton)
            viewModel.updateMidriaticAgent("мидриацил 1%")
            saveMidriaticResultAndFinish()
        }

        cyclomedOnePercentButton.setOnClickListener {
            updateButtonBackground(cyclomedOnePercentButton)
            viewModel.updateMidriaticAgent("цикломед 1%")
            saveMidriaticResultAndFinish()
        }

        cyclopentolateOnePercentButton.setOnClickListener {
            updateButtonBackground(cyclopentolateOnePercentButton)
            viewModel.updateMidriaticAgent("циклопентолат 1%")
            saveMidriaticResultAndFinish()
        }

        atropineSulfateOnePercentButton.setOnClickListener {
            updateButtonBackground(atropineSulfateOnePercentButton)
            viewModel.updateMidriaticAgent("атропина сульфат 1%")
            saveMidriaticResultAndFinish()
        }

        irifrinTwoAndAHalfPercentButton.setOnClickListener {
            updateButtonBackground(irifrinTwoAndAHalfPercentButton)
            viewModel.updateMidriaticAgent("ирифрин 2,5%")
            saveMidriaticResultAndFinish()
        }

        irifrinWithoutPreservativeTwoAndAHalfPercentButton.setOnClickListener {
            updateButtonBackground(irifrinWithoutPreservativeTwoAndAHalfPercentButton)
            viewModel.updateMidriaticAgent("ирифрин БК 2,5%")
            saveMidriaticResultAndFinish()
        }

        mydrimaxTwoAndAHalfPercentButton.setOnClickListener {
            updateButtonBackground(mydrimaxTwoAndAHalfPercentButton)
            viewModel.updateMidriaticAgent("мидримакс 2,5%")
            saveMidriaticResultAndFinish()
        }

        phenylephrineTenPercentButton.setOnClickListener {
            updateButtonBackground(phenylephrineTenPercentButton)
            viewModel.updateMidriaticAgent("фенилэфрин 2,5%")
            saveMidriaticResultAndFinish()
        }
    }

    private fun ageWarning() {
        val age = intent.getDoubleExtra("age", 0.0)

        // Тропикамид 0,5%
            tropicamideHalfAPercentTextView.text = ""

        // Тропикамид 1%
        if (age < 6) {
            tropicamideOnePercentTextView.text = "Нельзя пациентам до 6 лет"
            tropicamideOnePercentButtonTextView.setTextColor(ContextCompat.getColor(this,
                R.color.red
            ))
        } else {
            tropicamideOnePercentTextView.text = ""
        }

        //  Мидриацил 0,5%
            mydriacylHalfAPercentTextView.text = ""

        //  Мидриацил 1%
        if (age < 6) {
            mydriacylOnePercentTextView.text = "Нельзя пациентам до 6 лет"
            mydriacylOnePercentButtonTextView.setTextColor(ContextCompat.getColor(this, R.color.red))
        } else {
            mydriacylOnePercentTextView.text = ""
        }

        // Цикломед 1%
        if (age < 3) {
            cyclomedOnePercentTextView.text = "С осторожностью пациентам до 3 лет"
            cyclomedOnePercentButtonTextView.setTextColor(ContextCompat.getColor(this,
                R.color.yellow
            ))
        } else {
            cyclomedOnePercentTextView.text = ""
        }

        // Циклопентолат 1%
        if (age < 3) {
            cyclopentolateOnePercentTextView.text = "Нельзя пациентам до 3 лет"
            cyclopentolateOnePercentButtonTextView.setTextColor(ContextCompat.getColor(this,
                R.color.red
            ))
        } else if (age >= 60) {
            cyclopentolateOnePercentTextView.text = "С осторожностью пациентам пожилого возраста"
            cyclopentolateOnePercentButtonTextView.setTextColor(ContextCompat.getColor(this,
                R.color.yellow
            ))
        } else {
            cyclopentolateOnePercentTextView.text = ""
        }

        // Атропина сульфат 1%
        if (age < 7) {
            atropineSulfateOnePercentTextView.text = "Нельзя пациентам младше 7 лет"
            atropineSulfateOnePercentButtonTextView.setTextColor(ContextCompat.getColor(this,
                R.color.red
            ))
        } else if (age >= 40) {
            "С осторожностью у пациентов старше 40 лет".also { atropineSulfateOnePercentTextView.text = it }
            atropineSulfateOnePercentButtonTextView.setTextColor(ContextCompat.getColor(this,
                R.color.yellow
            ))
        } else {
            atropineSulfateOnePercentTextView.text = ""
        }

        //  Ирифрин 2,5%
        if (age < 6) {
            irifrinTwoAndAHalfPercentTextView.text = "Нельзя пациентам до 6 лет"
            irifrinTwoAndAHalfPercentButtonTextView.setTextColor(ContextCompat.getColor(this,
                R.color.red
            ))
        } else {
            irifrinTwoAndAHalfPercentTextView.text = ""
        }

        //  Ирифрин БК 2,5%
        if (age < 6) {
            irifrinWithoutPreservativeTwoAndAHalfPercentTextView.text = "Нельзя пациентам до 6 лет"
            irifrinWithoutPreservativeTwoAndAHalfPercentButtonTextView.setTextColor(ContextCompat.getColor(this,
                R.color.red
            ))
        } else {
            irifrinWithoutPreservativeTwoAndAHalfPercentTextView.text = ""
        }


        // Фенилэфрин 10%
        if (age < 12) {
            "Нельзя пациентам младше 12 лет".also { phenylephrineTenPercentTextView.text = it }
            phenylephrineTenPercentButtonTextView.setTextColor(ContextCompat.getColor(this,
                R.color.red
            ))
        } else {
            phenylephrineTenPercentTextView.text = ""
        }

        //Мидримакс 2,5%
        if (age < 12) {
            "Нельзя пациентам младше 12 лет".also { mydrimaxTwoAndAHalfPercentTextView.text = it }
            mydrimaxTwoAndAHalfPercentButtonTextView.setTextColor(ContextCompat.getColor(this,
                R.color.red
            ))
        } else {
            mydrimaxTwoAndAHalfPercentTextView.text = ""
        }
    }

    private fun setInitialButtonState() {
        val midriaticAgent = viewModel.midriaticAgent
        val initialButton = when (midriaticAgent) {
            "тропикамид 0,5%" -> tropicamideHalfAPercentButton
            "тропикамид 1%" -> tropicamideOnePercentButton
            "мидриацил 0,5%" -> mydriacylHalfAPercentButton
            "мидриацил 1%" -> mydriacylOnePercentButton
            "цикломед 1%" -> cyclomedOnePercentButton
            "циклопентолат 1%" -> cyclopentolateOnePercentButton
            "атропина сульфат 1%" -> atropineSulfateOnePercentButton
            "ирифрин 2,5%" -> irifrinTwoAndAHalfPercentButton
            "ирифрин БК 2,5%" -> irifrinWithoutPreservativeTwoAndAHalfPercentButton
            "фенилэфрин 10%" -> phenylephrineTenPercentButton
            "мидримакс 2,5%" -> mydrimaxTwoAndAHalfPercentButton

            else -> null
        }

        initialButton?.let {
            updateButtonBackground(it)
        }
    }

    private fun updateButtonBackground(selectedButton: FrameLayout) {
        if (currentGreenButtonId != -1) {
            findViewById<FrameLayout>(currentGreenButtonId).setBackgroundResource(R.drawable.button4_red)
        }
        currentGreenButtonId = selectedButton.id
        selectedButton.setBackgroundResource(R.drawable.button4_green)
    }
    private fun saveMidriaticResultAndFinish() {
        //  Изменения в  NinthActivity
        val midriaticAgent = viewModel.midriaticAgent

        val midriaticResult = MidriaticResult(
            patientProfileId = -1, // Замените -1 на фактический ID профиля
            midriaticAgent = midriaticAgent
        )

        // Передаем результат во SecondActivity
        val intent = Intent(this, SecondActivity::class.java)
        intent.putExtra("midriaticResult", midriaticResult)
        setResult(RESULT_OK, intent)
        finish()
    }
}
