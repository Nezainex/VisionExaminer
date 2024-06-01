package com.example.testapp13

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider

class NinthActivity : AppCompatActivity() {

    private lateinit var tropicamideOnePercentButton: FrameLayout
    private lateinit var tropicamideHalfAPercentButton: FrameLayout
    private lateinit var mydriacylOnePercentButton: FrameLayout
    private lateinit var mydriacylHalfAPercentButton: FrameLayout
    private lateinit var cyclomedOnePercentButton: FrameLayout
    private lateinit var cyclopentolateOnePercentButton: FrameLayout
    private lateinit var cyclopticOnePercentButton: FrameLayout
    private lateinit var atropineOnePercentButton: FrameLayout
    private lateinit var atropineSulfateOnePercentButton: FrameLayout
    private lateinit var irifrinTwoAndAHalfPercentButton: FrameLayout
    private lateinit var irifrinWithoutPreservativeTwoAndAHalfPercentButton: FrameLayout
    private lateinit var tropicamideTwoAndAHalfPercentButton: FrameLayout
    private lateinit var mydrimaxTwoAndAHalfPercentButton: FrameLayout
    private lateinit var phenylephrineTwoAndAHalfPercentButton: FrameLayout

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
        val backgroundColor = if (isNightMode) ContextCompat.getColor(this, R.color.black) else ContextCompat.getColor(this, R.color.white)

        setContentView(R.layout.activity_ninth)
        supportActionBar?.hide()
        window.decorView.setBackgroundColor(backgroundColor)
        Log.d("NinthActivity", "Background color set")

        viewModel = ViewModelProvider(this)[NinthActivityViewModel::class.java]

        tropicamideOnePercentButton = findViewById(R.id.Tropicamide_one_percent_button)
        tropicamideHalfAPercentButton = findViewById(R.id.Tropicamide_half_a_percent_button)
        mydriacylOnePercentButton = findViewById(R.id.Mydriacyl_one_percent_button)
        mydriacylHalfAPercentButton = findViewById(R.id.Mydriacyl_half_a_percent_button)
        cyclomedOnePercentButton = findViewById(R.id.Cyclomed_one_percent_button)
        cyclopentolateOnePercentButton = findViewById(R.id.Cyclopentolate_one_percent_button)
        cyclopticOnePercentButton = findViewById(R.id.Cycloptic_one_percent_button)
        atropineOnePercentButton = findViewById(R.id.Atropine_one_percent_button)
        atropineSulfateOnePercentButton = findViewById(R.id.Atropine_sulfate_one_percent_button)
        irifrinTwoAndAHalfPercentButton = findViewById(R.id.Irifrin_two_and_a_half_percent_button)
        irifrinWithoutPreservativeTwoAndAHalfPercentButton = findViewById(R.id.Irifrin_without_preservative_two_and_a_half_percent_button)
        tropicamideTwoAndAHalfPercentButton = findViewById(R.id.Tropicamide_two_and_a_half_percent_button)
        mydrimaxTwoAndAHalfPercentButton = findViewById(R.id.Mydrimax_two_and_a_half_percent_button)
        phenylephrineTwoAndAHalfPercentButton = findViewById(R.id.Phenylephrine_two_and_a_half_percent_button)

        setupMidriaticButtons()
        setInitialButtonState()
    }

    private fun setupMidriaticButtons() {
        tropicamideOnePercentButton.setOnClickListener {
            updateButtonBackground(tropicamideOnePercentButton)
            viewModel.updateMidriaticAgent("тропикамид 1%")
            navigateBackToSecondActivity()
        }

        tropicamideHalfAPercentButton.setOnClickListener {
            updateButtonBackground(tropicamideHalfAPercentButton)
            viewModel.updateMidriaticAgent("тропикамид 0,5%")
            navigateBackToSecondActivity()
        }

        mydriacylOnePercentButton.setOnClickListener {
            updateButtonBackground(mydriacylOnePercentButton)
            viewModel.updateMidriaticAgent("мидриацил 1%")
            navigateBackToSecondActivity()
        }

        mydriacylHalfAPercentButton.setOnClickListener {
            updateButtonBackground(mydriacylHalfAPercentButton)
            viewModel.updateMidriaticAgent("мидриацил 0,5%")
            navigateBackToSecondActivity()
        }

        cyclomedOnePercentButton.setOnClickListener {
            updateButtonBackground(cyclomedOnePercentButton)
            viewModel.updateMidriaticAgent("цикломед 1%")
            navigateBackToSecondActivity()
        }

        cyclopentolateOnePercentButton.setOnClickListener {
            updateButtonBackground(cyclopentolateOnePercentButton)
            viewModel.updateMidriaticAgent("циклопентолат 1%")
            navigateBackToSecondActivity()
        }

        cyclopticOnePercentButton.setOnClickListener {
            updateButtonBackground(cyclopticOnePercentButton)
            viewModel.updateMidriaticAgent("циклоптик 1%")
            navigateBackToSecondActivity()
        }

        atropineOnePercentButton.setOnClickListener {
            updateButtonBackground(atropineOnePercentButton)
            viewModel.updateMidriaticAgent("атропин 1%")
            navigateBackToSecondActivity()
        }

        atropineSulfateOnePercentButton.setOnClickListener {
            updateButtonBackground(atropineSulfateOnePercentButton)
            viewModel.updateMidriaticAgent("атропина сульфат 1%")
            navigateBackToSecondActivity()
        }

        irifrinTwoAndAHalfPercentButton.setOnClickListener {
            updateButtonBackground(irifrinTwoAndAHalfPercentButton)
            viewModel.updateMidriaticAgent("ирифрин 2,5%")
            navigateBackToSecondActivity()
        }

        irifrinWithoutPreservativeTwoAndAHalfPercentButton.setOnClickListener {
            updateButtonBackground(irifrinWithoutPreservativeTwoAndAHalfPercentButton)
            viewModel.updateMidriaticAgent("ирифрин БК 2,5%")
            navigateBackToSecondActivity()
        }

        tropicamideTwoAndAHalfPercentButton.setOnClickListener {
            updateButtonBackground(tropicamideTwoAndAHalfPercentButton)
            viewModel.updateMidriaticAgent("тропикамид 2,5%")
            navigateBackToSecondActivity()
        }

        mydrimaxTwoAndAHalfPercentButton.setOnClickListener {
            updateButtonBackground(mydrimaxTwoAndAHalfPercentButton)
            viewModel.updateMidriaticAgent("мидримакс 2,5%")
            navigateBackToSecondActivity()
        }

        phenylephrineTwoAndAHalfPercentButton.setOnClickListener {
            updateButtonBackground(phenylephrineTwoAndAHalfPercentButton)
            viewModel.updateMidriaticAgent("фенилэфрин 2,5%")
            navigateBackToSecondActivity()
        }
    }
    private fun navigateBackToSecondActivity() {
        val intent = Intent()
        intent.putExtra("midriaticAgent", viewModel.midriaticAgent)
        setResult(RESULT_OK, intent) // Изменено: используйте setResult
        finish() // Завершите NinthActivity
    }
    private fun setInitialButtonState() {
        val midriaticAgent = viewModel.midriaticAgent
        val initialButton = when (midriaticAgent) {
            "тропикамид 1%" -> tropicamideOnePercentButton
            "тропикамид 0,5%" -> tropicamideHalfAPercentButton
            "мидриацил 1%" -> mydriacylOnePercentButton
            "мидриацил 0,5%" -> mydriacylHalfAPercentButton
            "цикломед 1%" -> cyclomedOnePercentButton
            "циклопентолат 1%" -> cyclopentolateOnePercentButton
            "циклоптик 1%" -> cyclopticOnePercentButton
            "атропин 1%" -> atropineOnePercentButton
            "атропина сульфат 1%" -> atropineSulfateOnePercentButton
            "ирифрин 2,5%" -> irifrinTwoAndAHalfPercentButton
            "ирифрин БК 2,5%" -> irifrinWithoutPreservativeTwoAndAHalfPercentButton
            "тропикамид 2,5%" -> tropicamideTwoAndAHalfPercentButton
            "мидримакс 2,5%" -> mydrimaxTwoAndAHalfPercentButton
            "фенилэфрин 2,5%" -> phenylephrineTwoAndAHalfPercentButton
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
}
