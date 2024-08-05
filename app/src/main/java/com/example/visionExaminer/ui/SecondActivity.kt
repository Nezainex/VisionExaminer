@file:Suppress("DEPRECATION")
package com.example.visionExaminer.ui

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.visionExaminer.R
import com.example.visionExaminer.data.DatabaseInstance
import com.example.visionExaminer.data.MidriaticResult
import com.example.visionExaminer.data.PatientProfile
import com.example.visionExaminer.viewmodel.SecondActivityViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.Calendar
import java.util.Locale
import kotlin.math.abs

class SecondActivity : AppCompatActivity(), TextView.OnEditorActionListener, AdapterView.OnItemSelectedListener {
    private lateinit var birthDateEditText: EditText
    private lateinit var textViewAgeCalc: TextView
    private lateinit var agetransformtextView: TextView
    private lateinit var lastNameEditText: EditText
    private lateinit var firstNameEditText: EditText
    private lateinit var middleNameEditText: EditText
    private lateinit var examinationdateEditText: EditText
    private lateinit var midrtextView: TextView
    private lateinit var vistransformtextViewOD: TextView
    private lateinit var vistransformtextViewOS: TextView
    private lateinit var vistransformtextViewOU: TextView
    private lateinit var vistransformtextViewODcorr: TextView
    private lateinit var vistransformtextViewOScorr: TextView
    private lateinit var vistransformtextViewOUcorr: TextView
    private lateinit var visODinput: EditText
    private lateinit var visOSinput: EditText
    private lateinit var visOUinput: EditText
    private lateinit var visODcorrinput: EditText
    private lateinit var visOScorrinput: EditText
    private lateinit var visOUcorrinput: EditText
    private lateinit var sphODinput: EditText
    private lateinit var cylODinput: EditText
    private lateinit var axODinput: EditText
    private lateinit var sphOSinput: EditText
    private lateinit var cylOSinput: EditText
    private lateinit var axOSinput: EditText
    private lateinit var sphODLabel: TextView
    private lateinit var cylODLabel: TextView
    private lateinit var axODLabel: TextView
    private lateinit var sphOSLabel: TextView
    private lateinit var cylOSLabel: TextView
    private lateinit var axOSLabel: TextView
    private lateinit var textViewsphOD: TextView
    private lateinit var textViewcylOD: TextView
    private lateinit var textViewsphOS: TextView
    private lateinit var textViewcylOS: TextView
    private lateinit var comparesphtextView: TextView
    private lateinit var savebutton: FrameLayout
    private lateinit var clearbutton: FrameLayout
    private lateinit var camerabutton: FrameLayout
    private lateinit var osdiResultTextView: TextView
    private lateinit var rabkinResultTextView: TextView
    private lateinit var ishiharaResultTextView: TextView
    private lateinit var buttonMale: FrameLayout
    private lateinit var buttonFemale: FrameLayout
    private lateinit var midriaticButton: FrameLayout

    private val viewModel: SecondActivityViewModel by viewModels()

    private var isNightMode = true
    private var selectedGender = "Мужской" // Изначально устанавливаем мужской пол
    private var currentGreenButtonId: Int = -1

    @SuppressLint("SetTextI18n")
    private val startNinthForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) { // Проверка результата
            result.data?.getParcelableExtra<MidriaticResult>("midriaticResult")?.let { midriaticResult ->
                viewModel.updateMidriaticAgent(midriaticResult.midriaticAgent) // Обновление viewModel
                midrtextView.text = viewModel.midriaticAgent // Обновление TextView
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private val startOsdiForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            viewModel.osdiResult = result.data?.getParcelableExtra("osdiResult")
            viewModel.osdiResult?.let {
                osdiResultTextView.text = "Результат OSDI: ${it.resultText} (Баллы: ${it.score})"
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private val startRabkinForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            viewModel.rabkinResult = result.data?.getParcelableExtra("rabkinResult")
            viewModel.rabkinResult?.let {
                rabkinResultTextView.text = "Результат Rabkin: ${it.resultText} (Баллы: ${it.score})"
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private val startIshiharaForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            viewModel.ishiharaResult = result.data?.getParcelableExtra("ishiharaResult")
            viewModel.ishiharaResult?.let {
                ishiharaResultTextView.text = "Результат Ishihara: ${it.resultText} (Баллы: ${it.score})"
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
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
        setContentView(R.layout.activity_second)
        supportActionBar?.hide()
        window.decorView.setBackgroundColor(backgroundColor)


        // Initialize views
        birthDateEditText = findViewById(R.id.birthDate_edit_text)
        textViewAgeCalc = findViewById(R.id.textViewAgeCalc)
        agetransformtextView = findViewById(R.id.agetransform_text_View)
        lastNameEditText = findViewById(R.id.last_name_edit_text)
        firstNameEditText = findViewById(R.id.first_name_edit_text)
        middleNameEditText = findViewById(R.id.middle_name_edit_text)
        examinationdateEditText = findViewById(R.id.examination_date_edit_text)

        midrtextView = findViewById(R.id.midrtextView)
        visODinput = findViewById(R.id.visODinput)
        visOSinput = findViewById(R.id.visOSinput)
        visOUinput = findViewById(R.id.visOUinput)
        visODcorrinput = findViewById(R.id.visODcorrinput)
        visOScorrinput = findViewById(R.id.visOScorrinput)
        visOUcorrinput = findViewById(R.id.visOUcorrinput)
        sphODinput = findViewById(R.id.sphODinput)
        cylODinput = findViewById(R.id.cylODinput)
        axODinput = findViewById(R.id.axODinput)
        sphOSinput = findViewById(R.id.sphOSinput)
        cylOSinput = findViewById(R.id.cylOSinput)
        axOSinput = findViewById(R.id.axOSinput)
        comparesphtextView = findViewById(R.id.comparesphtextView)
        vistransformtextViewOD = findViewById(R.id.vistransformtextViewOD)
        vistransformtextViewOS = findViewById(R.id.vistransformtextViewOS)
        vistransformtextViewOU = findViewById(R.id.vistransformtextViewOU)
        vistransformtextViewODcorr = findViewById(R.id.vistransformtextViewODcorr)
        vistransformtextViewOScorr = findViewById(R.id.vistransformtextViewOScorr)
        vistransformtextViewOUcorr = findViewById(R.id.vistransformtextViewOUcorr)
        textViewsphOD = findViewById(R.id.textViewsphOD)
        textViewcylOD = findViewById(R.id.textViewcylOD)
        textViewsphOS = findViewById(R.id.textViewsphOS)
        textViewcylOS = findViewById(R.id.textViewcylOS)
        sphODLabel = findViewById(R.id.sphOD_label)
        cylODLabel = findViewById(R.id.cylOD_label)
        axODLabel = findViewById(R.id.axOD_label)
        sphOSLabel = findViewById(R.id.sphOS_label)
        cylOSLabel = findViewById(R.id.cylOS_label)
        axOSLabel = findViewById(R.id.axOS_label)
        sphODLabel.setTextColor(if (isNightMode) ContextCompat.getColor(this, R.color.white) else ContextCompat.getColor(this,
            R.color.black
        ))
        cylODLabel.setTextColor(if (isNightMode) ContextCompat.getColor(this, R.color.white) else ContextCompat.getColor(this,
            R.color.black
        ))
        axODLabel.setTextColor(if (isNightMode) ContextCompat.getColor(this, R.color.white) else ContextCompat.getColor(this,
            R.color.black
        ))
        sphOSLabel.setTextColor(if (isNightMode) ContextCompat.getColor(this, R.color.white) else ContextCompat.getColor(this,
            R.color.black
        ))
        cylOSLabel.setTextColor(if (isNightMode) ContextCompat.getColor(this, R.color.white) else ContextCompat.getColor(this,
            R.color.black
        ))
        axOSLabel.setTextColor(if (isNightMode) ContextCompat.getColor(this, R.color.white) else ContextCompat.getColor(this,
            R.color.black
        ))
        osdiResultTextView = findViewById(R.id.osdi_result_text_view)
        rabkinResultTextView = findViewById(R.id.rabkin_result_text_view)
        ishiharaResultTextView = findViewById(R.id.ishihara_result_text_view)
        savebutton = findViewById(R.id.buttonsave)
        clearbutton = findViewById(R.id.buttonclear)
        camerabutton = findViewById(R.id.camera_button)
        buttonMale = findViewById(R.id.buttonMale)
        buttonFemale = findViewById(R.id.buttonFemale)
        midriaticButton = findViewById(R.id.midriaticButton)

        midriaticButton.setOnClickListener {
            val intent = Intent(this, NinthActivity::class.java)
            val isNightMode = intent.getBooleanExtra("isNightMode", true)
            intent.putExtra("isNightMode", isNightMode) // Передача isNightMode в NinthActivity
            intent.putExtra("age", calculateAgeFromBirthDate()) // Передача возраста
            startNinthForResult.launch(intent) // Запускаем NinthActivity и ожидаем результат
        }

        // Загрузка данных из ViewModel
        loadViewModelData()

        // Create a single TextWatcher instance
        val textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // Automatically format the date as it's typed
                val inputText = s.toString()
                if (inputText.length == 2 || inputText.length == 5) {
                    if (!inputText.endsWith(".")) {
                        s?.append(".")
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val changedView = currentFocus // Get the view that was just edited
                Log.d("TextWatcher", "onTextChanged called for view: ${changedView?.id}")
                when (changedView?.id) {
                    R.id.birthDate_edit_text -> {
                        updateAgeDescription()
                        updateVisDescription()
                        updateVisDescriptioncorr()
                        updateSphDescription()
                        updateComparesphText()
                        updateCylDescription()
                        transposeOD()
                        transposeOS()
                    }
                    // Добавьте обработку других полей при необходимости
                }
            }
        }

        // Set up examination date picker dialog
        val examinationCalendar = Calendar.getInstance()
        DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            examinationCalendar.set(Calendar.YEAR, year)
            examinationCalendar.set(Calendar.MONTH, month)
            examinationCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            val examinationDateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            examinationdateEditText.setText(examinationDateFormat.format(examinationCalendar.time))
        }

        // Set up birth date picker dialog
        val birthCalendar = Calendar.getInstance()
        DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            birthCalendar.set(Calendar.YEAR, year)
            birthCalendar.set(Calendar.MONTH, month)
            birthCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            birthDateEditText.setText(dateFormat.format(birthCalendar.time))
        }
        birthDateEditText.addTextChangedListener(textWatcher)

        // Обработчик нажатия кнопки "Сохранить"
        savebutton.setOnClickListener {
            saveProfile()
        }

        // Обработчики для кнопок выбора пола
        buttonMale.setOnTouchListener { view, event ->
            when (event?.action) {
                MotionEvent.ACTION_DOWN -> { // Нажатие
                    if (currentGreenButtonId != buttonMale.id) { // Если кнопка не зеленая
                        buttonMale.setBackgroundResource(R.drawable.button4_red_pressed)
                    }
                }
                MotionEvent.ACTION_UP -> { // Отпускание
                    if (currentGreenButtonId != buttonMale.id) { // Если кнопка не зеленая
                        currentGreenButtonId = buttonMale.id
                        buttonMale.setBackgroundResource(R.drawable.button4_green)
                        buttonFemale.setBackgroundResource(R.drawable.button4_red)
                    }
                    view.performClick()  // Добавлено вызов performClick()
                }
                else -> {
                    return@setOnTouchListener false
                }
            }
            selectedGender = "Мужской"
            updateAgeDescription()
            return@setOnTouchListener true
        }

        buttonFemale.setOnTouchListener { view, event ->
            when (event?.action) {
                MotionEvent.ACTION_DOWN -> { // Нажатие
                    if (currentGreenButtonId != buttonFemale.id) { // Если кнопка не зеленая
                        buttonFemale.setBackgroundResource(R.drawable.button4_red_pressed)
                    }
                }
                MotionEvent.ACTION_UP -> { // Отпускание
                    if (currentGreenButtonId != buttonFemale.id) { // Если кнопка не зеленая
                        currentGreenButtonId = buttonFemale.id
                        buttonFemale.setBackgroundResource(R.drawable.button4_green)
                        buttonMale.setBackgroundResource(R.drawable.button4_red)
                    }
                    view.performClick() // Добавлено вызов performClick()
                }
                else -> {
                    return@setOnTouchListener false
                }
            }
            selectedGender = "Женский"
            updateAgeDescription()
            return@setOnTouchListener true
        }

        // Обработчики для других кнопок
        val fourthActbutton = findViewById<FrameLayout>(R.id.fourth_act_button)
        fourthActbutton.setOnClickListener {
            val intent = Intent(this, FourthActivity::class.java)
            intent.putExtra("isNightMode", isNightMode)
            startOsdiForResult.launch(intent)
        }

        val sixthActbutton = findViewById<FrameLayout>(R.id.sixth_act_button)
        sixthActbutton.setOnClickListener {
            val intent = Intent(this, SixthActivity::class.java)
            intent.putExtra("isNightMode", isNightMode)
            startRabkinForResult.launch(intent)
        }

        val seventhActbutton = findViewById<FrameLayout>(R.id.seventh_act_button)
        seventhActbutton.setOnClickListener {
            val intent = Intent(this, SeventhActivity::class.java)
            intent.putExtra("isNightMode", isNightMode)
            startIshiharaForResult.launch(intent)
        }

        camerabutton.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java)
            intent.putExtra("isNightMode", isNightMode)
            startActivity(intent)
        }

        // Обработчик нажатия кнопки "Clear"
        clearbutton.setOnClickListener {
            birthDateEditText.text.clear()
            lastNameEditText.text.clear()
            firstNameEditText.text.clear()
            middleNameEditText.text.clear()
            examinationdateEditText.text.clear()
            visODinput.text.clear()
            visOSinput.text.clear()
            visOUinput.text.clear()
            visODcorrinput.text.clear()
            visOScorrinput.text.clear()
            visOUcorrinput.text.clear()
            sphODinput.text.clear()
            cylODinput.text.clear()
            axODinput.text.clear()
            sphOSinput.text.clear()
            cylOSinput.text.clear()
            axOSinput.text.clear()
            sphODLabel.text = ""
            cylODLabel.text = ""
            axODLabel.text = ""
            sphOSLabel.text = ""
            cylOSLabel.text = ""
            axOSLabel.text = ""
            osdiResultTextView.text = ""
            rabkinResultTextView.text = ""
            ishiharaResultTextView.text = ""
            midrtextView.text = ""

            // Установка "мужского" пола по умолчанию
            buttonMale.background = ContextCompat.getDrawable(this, R.drawable.button4_green)
            buttonFemale.background = ContextCompat.getDrawable(this, R.drawable.button4_red)
            selectedGender = "Мужской"
        }

        // Attach the TextWatcher to all relevant input fields
        birthDateEditText.addTextChangedListener(textWatcher)
        examinationdateEditText.addTextChangedListener(textWatcher)
        visODinput.addTextChangedListener(textWatcher)
        visOSinput.addTextChangedListener(textWatcher)
        visOUinput.addTextChangedListener(textWatcher)
        visODcorrinput.addTextChangedListener(textWatcher)
        visOScorrinput.addTextChangedListener(textWatcher)
        visOUcorrinput.addTextChangedListener(textWatcher)
        sphODinput.addTextChangedListener(textWatcher)
        cylODinput.addTextChangedListener(textWatcher)
        axODinput.addTextChangedListener(textWatcher)
        sphOSinput.addTextChangedListener(textWatcher)
        cylOSinput.addTextChangedListener(textWatcher)
        axOSinput.addTextChangedListener(textWatcher)

        birthDateEditText.setOnEditorActionListener(this)
        lastNameEditText.setOnEditorActionListener(this)
        firstNameEditText.setOnEditorActionListener(this)
        middleNameEditText.setOnEditorActionListener(this)
        examinationdateEditText.setOnEditorActionListener(this)
        visODinput.setOnEditorActionListener(this)
        visOSinput.setOnEditorActionListener(this)
        visOUinput.setOnEditorActionListener(this)
        visODcorrinput.setOnEditorActionListener(this)
        visOScorrinput.setOnEditorActionListener(this)
        visOUcorrinput.setOnEditorActionListener(this)
        sphODinput.setOnEditorActionListener(this)
        cylODinput.setOnEditorActionListener(this)
        axODinput.setOnEditorActionListener(this)
        sphOSinput.setOnEditorActionListener(this)
        cylOSinput.setOnEditorActionListener(this)
        axOSinput.setOnEditorActionListener(this)
    }

    private fun loadViewModelData() {
        birthDateEditText.setText(viewModel.birthDate)
        lastNameEditText.setText(viewModel.lastName)
        firstNameEditText.setText(viewModel.firstName)
        middleNameEditText.setText(viewModel.middleName)
        selectedGender = viewModel.gender // Загрузка пола из ViewModel
        if (selectedGender == "Мужской") {
            buttonMale.background = ContextCompat.getDrawable(this, R.drawable.button4_green)
            buttonFemale.background = ContextCompat.getDrawable(this, R.drawable.button4_red)
        } else {
            buttonMale.background = ContextCompat.getDrawable(this, R.drawable.button4_red)
            buttonFemale.background = ContextCompat.getDrawable(this, R.drawable.button4_green)
        }
        examinationdateEditText.setText(viewModel.examinationdate)
        visODinput.setText(viewModel.visOD.toString())
        visOSinput.setText(viewModel.visOS.toString())
        visOUinput.setText(viewModel.visOU.toString())
        visODcorrinput.setText(viewModel.visODcorr.toString())
        visOScorrinput.setText(viewModel.visOScorr.toString())
        visOUcorrinput.setText(viewModel.visOUcorr.toString())
        sphODinput.setText(viewModel.sphOD.toString())
        cylODinput.setText(viewModel.cylOD.toString())
        axODinput.setText(viewModel.axOD.toString())
        sphOSinput.setText(viewModel.sphOS.toString())
        cylOSinput.setText(viewModel.cylOS.toString())
        axOSinput.setText(viewModel.axOS.toString())
        sphODLabel.text = viewModel.sphODLabel
        cylODLabel.text = viewModel.cylODLabel
        axODLabel.text = viewModel.axODLabel
        sphOSLabel.text = viewModel.sphOSLabel
        cylOSLabel.text = viewModel.cylOSLabel
        axOSLabel.text = viewModel.axOSLabel
        comparesphtextView.text = viewModel.comparesphResult
        osdiResultTextView.text = viewModel.osdiResult?.let {
            "Результат OSDI: ${it.resultText} (Баллы: ${it.score})"
        } ?: ""
        rabkinResultTextView.text = viewModel.rabkinResult?.let {
            "Результат Rabkin: ${it.resultText} (Баллы: ${it.score})"
        } ?: ""
        ishiharaResultTextView.text = viewModel.ishiharaResult?.let {
            "Результат Ishihara: ${it.resultText} (Баллы: ${it.score})"
        } ?: ""
    }

    private fun saveProfile() {
        viewModel.updateBirthDate(birthDateEditText.text.toString())
        viewModel.updateLastName(lastNameEditText.text.toString())
        viewModel.updateFirstName(firstNameEditText.text.toString())
        viewModel.updateMiddleName(middleNameEditText.text.toString())
        viewModel.updateGender(selectedGender) // Сохранение выбранного пола
        viewModel.updateAge(calculateAgeFromBirthDate())
        viewModel.updateExaminationDate(examinationdateEditText.text.toString())
        viewModel.updateVisOD(visODinput.text.toString().toDoubleOrNull() ?: 0.0)
        viewModel.updateVisOS(visOSinput.text.toString().toDoubleOrNull() ?: 0.0)
        viewModel.updateVisOU(visOUinput.text.toString().toDoubleOrNull() ?: 0.0)
        viewModel.updateVisODcorr(visODcorrinput.text.toString().toDoubleOrNull() ?: 0.0)
        viewModel.updateVisOScorr(visOScorrinput.text.toString().toDoubleOrNull() ?: 0.0)
        viewModel.updateVisOUcorr(visOUcorrinput.text.toString().toDoubleOrNull() ?: 0.0)
        viewModel.updateSphOD(sphODinput.text.toString().toDoubleOrNull() ?: 0.0)
        viewModel.updateCylOD(cylODinput.text.toString().toDoubleOrNull() ?: 0.0)
        viewModel.updateAxOD(axODinput.text.toString().toDoubleOrNull() ?: 0.0)
        viewModel.updateSphOS(sphOSinput.text.toString().toDoubleOrNull() ?: 0.0)
        viewModel.updateCylOS(cylOSinput.text.toString().toDoubleOrNull() ?: 0.0)
        viewModel.updateAxOS(axOSinput.text.toString().toDoubleOrNull() ?: 0.0)
        viewModel.updateSphODLabel(sphODLabel.text.toString())
        viewModel.updateCylODLabel(cylODLabel.text.toString())
        viewModel.updateAxODLabel(axODLabel.text.toString())
        viewModel.updateSphOSLabel(sphOSLabel.text.toString())
        viewModel.updateCylOSLabel(cylOSLabel.text.toString())
        viewModel.updateAxOSLabel(axOSLabel.text.toString())
        viewModel.updateComparesphResult(comparesphtextView.text.toString())


        val newProfile = PatientProfile(
            birthDate = viewModel.birthDate,
            lastName = viewModel.lastName,
            firstName = viewModel.firstName,
            middleName = viewModel.middleName,
            gender = viewModel.gender,
            age = viewModel.age,
            examinationdate = viewModel.examinationdate,
            visOD = viewModel.visOD,
            visOS = viewModel.visOS,
            visOU = viewModel.visOU,
            visODcorr = viewModel.visODcorr,
            visOScorr = viewModel.visOScorr,
            visOUcorr = viewModel.visOUcorr,
            sphOD = viewModel.sphOD,
            cylOD = viewModel.cylOD,
            axOD = viewModel.axOD,
            sphOS = viewModel.sphOS,
            cylOS = viewModel.cylOS,
            axOS = viewModel.axOS,
            sphODLabel = viewModel.sphODLabel,
            cylODLabel = viewModel.cylODLabel,
            axODLabel = viewModel.axODLabel,
            sphOSLabel = viewModel.sphOSLabel,
            cylOSLabel = viewModel.cylOSLabel,
            axOSLabel = viewModel.axOSLabel,
            comparesphResult = viewModel.comparesphResult,
            midriaticAgent = viewModel.midriaticAgent
        )

        val database = DatabaseInstance.getInstance(this)
        lifecycleScope.launch(Dispatchers.IO) {
            val profileId = database.patientProfileDao().insert(newProfile)

            viewModel.osdiResult?.let {
                it.patientProfileId = profileId.toInt()
                database.patientProfileDao().insertOsdiResult(it)
            }
            viewModel.rabkinResult?.let {
                it.patientProfileId = profileId.toInt()
                database.patientProfileDao().insertRabkinResult(it)
            }
            viewModel.ishiharaResult?.let {
                it.patientProfileId = profileId.toInt()
                database.patientProfileDao().insertIshiharaResult(it)
            }
        }

        Toast.makeText(this, "Профиль сохранен", Toast.LENGTH_SHORT).show()

        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("profileId", newProfile.id)
        intent.putExtra("birthDate", newProfile.birthDate)
        startActivity(intent)
    }

    private fun calculateAgeFromBirthDate(): Double {
        val birthDateString = birthDateEditText.text.toString()
        if (birthDateString.length < 8) return 0.0
        val dateParts = birthDateString.split("[.,]".toRegex())
        if (dateParts.size != 3) return 0.0
        return try {
            val day = dateParts[0].toInt()
            val month = dateParts[1].toInt() - 1
            val year = dateParts[2].toInt()
            val today = LocalDate.now()
            val birthDate = LocalDate.of(year, month + 1, day)
            val ageInYears = ChronoUnit.YEARS.between(birthDate, today)
            ageInYears.toDouble()
        } catch (e: Exception) {
            Log.w("BirthDateCalc", "Error parsing birth date", e)
            0.0
        }
    }

    private fun updateAgeDescription() {
        val age = calculateAgeFromBirthDate()
        val isMale = selectedGender == "Мужской"
        agetransformtextView.text = ageTransform(age, isMale)
    }

    private fun updateVisDescription() {
        val age = calculateAgeFromBirthDate()
        val visOD = try {
            visODinput.text.toString().toDouble()
        } catch (e: Exception) {
            0.0
        }
        vistransformtextViewOD.text = visTransform(visOD, age, textViewAgeCalc)

        val visOS = try {
            visOSinput.text.toString().toDouble()
        } catch (e: Exception) {
            0.0
        }
        vistransformtextViewOS.text = visTransform(visOS, age, textViewAgeCalc)

        val visOU = try {
            visOUinput.text.toString().toDouble()
        } catch (e: Exception) {
            0.0
        }
        vistransformtextViewOU.text = visTransform(visOU, age, textViewAgeCalc)
    }

    private fun updateVisDescriptioncorr() {
        val age = calculateAgeFromBirthDate()

        val visODcorr = try {
            visODcorrinput.text.toString().toDouble()
        } catch (e: Exception) {
            0.0
        }
        vistransformtextViewODcorr.text = visTransformcorr(visODcorr, age, textViewAgeCalc)

        val visOScorr = try {
            visOScorrinput.text.toString().toDouble()
        } catch (e: Exception) {
            0.0
        }
        vistransformtextViewOScorr.text = visTransformcorr(visOScorr, age, textViewAgeCalc)

        val visOUcorr = try {
            visOUcorrinput.text.toString().toDouble()
        } catch (e: Exception) {
            0.0
        }
        vistransformtextViewOUcorr.text = visTransformcorr(visOUcorr, age, textViewAgeCalc)
    }

    private fun updateSphDescription() {
        val age = calculateAgeFromBirthDate()

        val sphOD = try {
            sphODinput.text.toString().toDouble()
        } catch (e: Exception) {
            0.0
        }
        textViewsphOD.text = sphCalculate(sphOD, age, "OD")

        val sphOS = try {
            sphOSinput.text.toString().toDouble()
        } catch (e: Exception) {
            0.0
        }
        textViewsphOS.text = sphCalculate(sphOS, age, "OS")
    }

    private fun updateCylDescription() {
        val cylOD = try {
            cylODinput.text.toString().toDouble()
        } catch (e: Exception) {
            0.0
        }
        textViewcylOD.text = cylCalculate(cylOD, "OD")

        val cylOS = try {
            cylOSinput.text.toString().toDouble()
        } catch (e: Exception) {
            0.0
        }
        textViewcylOS.text = cylCalculate(cylOS, "OS")
    }

    private fun updateComparesphText() {
        val sphODinput = try {
            sphODinput.text.toString().toDouble()
        } catch (e: NumberFormatException) {
            0.0
        }
        val sphOSinput = try {
            sphOSinput.text.toString().toDouble()
        } catch (e: NumberFormatException) {
            0.0
        }
        comparesph(sphODinput, sphOSinput, comparesphtextView)
    }

    @SuppressLint("SetTextI18n")
    private fun transposeOD() {
        val sphOD = getDoubleValueOD(sphODinput.text.toString())
        val cylOD = getDoubleValueOD(cylODinput.text.toString())
        val axOD = getDoubleValueOD(axODinput.text.toString())

        // Проверка на валидность значений перед вычислениями
        if (sphOD.isNaN() || cylOD.isNaN() || axOD.isNaN()) return

        val results = transponirOD(sphOD, cylOD, axOD)
        sphODLabel.text = "sph ${results[0]} "
        cylODLabel.text = "cyl ${results[1]} "
        axODLabel.text = "ax ${results[2]}°"
    }

    private fun getDoubleValueOD(text: String): Double {
        return text.toDoubleOrNull() ?: 0.0
    }

    private fun transponirOD(sphOD: Double, cylOD: Double, axOD: Double): Array<Double> {
        val sphODNew = sphOD + cylOD
        val cylODNew = -cylOD
        val axODNew = when {
            axOD <= 90 -> axOD + 90
            else -> axOD - 90
        }
        return arrayOf(sphODNew, cylODNew, axODNew)
    }

    @SuppressLint("SetTextI18n")
    private fun transposeOS() {
        val sphOS = getDoubleValueOS(sphOSinput.text.toString())
        val cylOS = getDoubleValueOS(cylOSinput.text.toString())
        val axOS = getDoubleValueOS(axOSinput.text.toString())

        // Проверка на валидность значений перед вычислениями
        if (sphOS.isNaN() || cylOS.isNaN() || axOS.isNaN()) return

        val results = transponirOS(sphOS, cylOS, axOS)
        sphOSLabel.text = "sph ${results[0]} "
        cylOSLabel.text = "cyl ${results[1]} "
        axOSLabel.text = "ax ${results[2]}°"
    }

    private fun getDoubleValueOS(text: String): Double {
        return text.toDoubleOrNull() ?: 0.0
    }

    private fun transponirOS(sphOS: Double, cylOS: Double, axOS: Double): Array<Double> {
        val sphOSNew = sphOS + cylOS
        val cylOSNew = -cylOS
        val axOSNew = when {
            axOS <= 90 -> axOS + 90
            else -> axOS - 90
        }
        return arrayOf(sphOSNew, cylOSNew, axOSNew)
    }

    companion object {
        fun sphCalculate(sph: Double, age: Double = 0.0, eye: String): String {
            if (sph == 0.0) return "$eye эмметропия"
            val correction = when {
                age > 0 && age < 0.5 -> 3.6
                age >= 0.5 && age < 1 -> 2.6
                age >= 1 && age < 2 -> 1.9
                age >= 2 && age < 3 -> 1.4
                age >= 3 && age < 4 -> 1.3
                age >= 4 && age < 5 -> 1.1
                age >= 5 && age < 6 -> 1.0
                age >= 6 && age < 8 -> 0.7
                age >= 8 && age < 9 -> 0.6
                age >= 9 && age < 10 -> -0.01
                age >= 10 && age < 11 -> -0.2
                age >= 11 && age < 12 -> -0.3
                age >= 12 && age < 14 -> -0.4
                else -> 0.0
            }
            if (sph >= -0.74 + correction && sph <= 0.74 + correction) return "$eye эмметропия"
            return when {
                sph <= -0.75 + correction && sph > -3.25 + correction -> "$eye миопия \n слабой степени"
                sph <= -3.25 + correction && sph > -6.00 + correction -> "$eye миопия \n средней степени"
                sph <= -6.00 + correction -> "$eye миопия \n высокой степени"
                sph >= 0.75 + correction && sph < 3.25 + correction -> "$eye гиперметропия \n слабой степени"
                sph >= 3.25 + correction && sph < 6.00 + correction -> "$eye гиперметропия \n средней степени"
                sph >= 6.00 + correction -> "$eye гиперметропия \n высокой степени"
                else -> "$eye некорректное \n значение sph"
            }
        }

        // Функция, определяющая степень астигматизма, в зависимости от данных cyl
        fun cylCalculate(cyl: Double, eye: String): String {
            if (cyl == 0.0) return ""
            if (cyl >= -0.74 && cyl <= 0.74) return ""
            return when {
                (cyl <= -0.75 && cyl > -3) || (cyl >= 0.75 && cyl < 3) -> "$eye астигматизм \n слабой степени"
                (cyl <= -3 && cyl > -6) || (cyl >= 3 && cyl < 6) -> "$eye астигматизм \n средней степени"
                cyl <= -6 || cyl >= 6 -> "$eye астигматизм \n высокой степени"
                else -> "$eye некорректное \n значение cyl"
            }
        }

        // Функция рассчёта возраста
        fun ageTransform(age: Double, isMale: Boolean): String {
            if (age == 0.0) return ""
            if (age >= 0.01 && age < 0.0767123 ) {
                return "(Неонатальный период)"
            } else if (age >= 0.0767123 && age < 1 ) {
                return "(Грудной возраст)"
            } else if (age >= 1 && age < 3 ) {
                return "(Раннее детство)"
            } else if (age >= 3 && age < 6 ) {
                return "(Дошкольный возраст)"
            } else if ((age >= 6 && age < 12 && isMale) || (age >= 6 && age < 10)) {
                return "(Младший школьный возраст)"
            } else if ((age >= 12 && age < 18 && isMale) || (age >= 10 && age < 18)) {
                return "(Подростковый возраст)"
            } else if (age >= 18 && age < 35 ) {
                return "(Взрослый возраст)"
            } else if (age >= 35 && age < 60) {
                return "(Зрелый возраст)"
            } else if (age >= 60 && age < 75) {
                return "(Пожилой возраст)"
            } else if (age >= 75 && age < 90) {
                return "(Старческий возраст)"
            } else if (age >= 90 && age < 138000000000000) {
                return "(Долгожитель)"
            } else return "(Возраст неопределён)"
        }

        // Функция рассчёта остроты зрения
        fun visTransform(vis: Double, age: Double, textViewAgeCalc: TextView): String {
            val ageFromTextView = try {
                textViewAgeCalc.text.toString().toDouble()
            } catch (e: Exception) {
                0.0 // Default to 0 if textViewAgeCalc is empty or invalid
            }

            val finalAge = if (ageFromTextView > 0.0) ageFromTextView else age // Prioritize age from textViewAgeCalc

            if (finalAge == 0.0 || vis == 0.0) return ""

            val normVis = when {
                (age in 0.0191781..0.0833334) && (vis in 0.002..0.02) -> "(Нормальная острота зрения)"
                (age in 0.0191781..0.0833334) && (vis > 0.02) -> "(Острота зрения выше нормы)"
                (age in 0.0833334..0.25) && (vis in 0.008..0.03) -> "(Нормальная острота зрения)"
                (age in 0.0833334..0.25) && (vis > 0.03) -> "(Острота зрения выше нормы)"
                (age in 0.25..0.5) && (vis in 0.05..0.1) -> "(Нормальная острота зрения)"
                (age in 0.25..0.5) && (vis > 0.1) -> "(Острота зрения выше нормы)"
                (age in 0.5..1.0) && (vis in 0.3..0.6) -> "(Нормальная острота зрения)"
                (age in 0.5..1.0) && (vis > 0.6) -> "(Острота зрения выше нормы)"
                (age in 1.0..2.0) && (vis in 0.3..0.6) -> "(Нормальная острота зрения)"
                (age in 1.0..2.0) && (vis > 0.6) -> "(Острота зрения выше нормы)"
                (age in 2.0..3.0) && (vis in 0.4..0.7) -> "(Нормальная острота зрения)"
                (age in 2.0..3.0) && (vis > 0.7) -> "(Острота зрения выше нормы)"
                (age in 3.0..4.0) && (vis in 0.6..0.9) -> "(Нормальная острота зрения)"
                (age in 3.0..4.0) && (vis > 0.9) -> "(Острота зрения выше нормы)"
                (age in 4.0..5.0) && (vis in 0.7..1.0) -> "(Нормальная острота зрения)"
                (age in 4.0..5.0) && (vis > 1.0) -> "(Острота зрения выше нормы)"
                (age in 5.0..7.0) && (vis in 0.8..1.0) -> "(Нормальная острота зрения)"
                (age in 5.0..7.0) && (vis > 1.0) -> "(Острота зрения выше нормы)"
                (age in 7.0..8.0) && (vis in 0.9..1.2) -> "(Нормальная острота зрения)"
                (age in 7.0..8.0) && (vis > 1.2) -> "(Острота зрения выше нормы)"
                (age in 8.0..15.0) && (vis in 0.9..1.5) -> "(Нормальная острота зрения)"
                (age in 8.0..15.0) && (vis > 1.5) -> "(Острота зрения выше нормы)"
                (15 <= age && age < 18) && (vis in 1.0..1.5) -> "(Нормальная острота зрения)"
                (15 <= age && age < 18) && (vis > 1.5) -> "(Острота зрения выше нормы)"
                else -> when {
                    age >= 18 && (vis in 1.0..1.5) -> "(Нормальная острота зрения)"
                    age >= 18 && (vis > 1.5) -> "(Острота зрения выше нормы)"
                    else -> "(Недостаточная острота зрения)"
                }
            }
            return normVis
        }


        // Функция рассчёта остроты зрения в коррекции
        fun visTransformcorr(vis: Double, age: Double, textViewAgeCalc: TextView): String {
            val ageFromTextView = try {
                textViewAgeCalc.text.toString().toDouble()
            } catch (e: Exception) {
                0.0 // Default to 0 if textViewAgeCalc is empty or invalid
            }

            val finalAge = if (ageFromTextView > 0.0) ageFromTextView else age // Prioritize age from textViewAgeCalc

            if (finalAge == 0.0 || vis == 0.0) return ""

            val normVis = when {
                (age in 0.0191781..0.0833334) && (vis in 0.002..0.02) -> "(Нормальная острота зрения)"
                (age in 0.0191781..0.0833334) && (vis > 0.02) -> "(Острота зрения выше нормы)"
                (age in 0.0833334..0.25) && (vis in 0.008..0.03) -> "(Нормальная острота зрения)"
                (age in 0.0833334..0.25) && (vis > 0.03) -> "(Острота зрения выше нормы)"
                (age in 0.25..0.5) && (vis in 0.05..0.1) -> "(Нормальная острота зрения)"
                (age in 0.25..0.5) && (vis > 0.1) -> "(Острота зрения выше нормы)"
                (age in 0.5..1.0) && (vis in 0.3..0.6) -> "(Нормальная острота зрения)"
                (age in 0.5..1.0) && (vis > 0.6) -> "(Острота зрения выше нормы)"
                (age in 1.0..2.0) && (vis in 0.3..0.6) -> "(Нормальная острота зрения)"
                (age in 1.0..2.0) && (vis > 0.6) -> "(Острота зрения выше нормы)"
                (age in 2.0..3.0) && (vis in 0.4..0.7) -> "(Нормальная острота зрения)"
                (age in 2.0..3.0) && (vis > 0.7) -> "(Острота зрения выше нормы)"
                (age in 3.0..4.0) && (vis in 0.6..0.9) -> "(Нормальная острота зрения)"
                (age in 3.0..4.0) && (vis > 0.9) -> "(Острота зрения выше нормы)"
                (age in 4.0..5.0) && (vis in 0.7..1.0) -> "(Нормальная острота зрения)"
                (age in 4.0..5.0) && (vis > 1.0) -> "(Острота зрения выше нормы)"
                (age in 5.0..7.0) && (vis in 0.8..1.0) -> "(Нормальная острота зрения)"
                (age in 5.0..7.0) && (vis > 1.0) -> "(Острота зрения выше нормы)"
                (age in 7.0..8.0) && (vis in 0.9..1.2) -> "(Нормальная острота зрения)"
                (age in 7.0..8.0) && (vis > 1.2) -> "(Острота зрения выше нормы)"
                (age in 8.0..15.0) && (vis in 0.9..1.5) -> "(Нормальная острота зрения)"
                (age in 8.0..15.0) && (vis > 1.5) -> "(Острота зрения выше нормы)"
                (15 <= age && age < 18) && (vis in 1.0..1.5) -> "(Нормальная острота зрения)"
                (15 <= age && age < 18) && (vis > 1.5) -> "(Острота зрения выше нормы)"
                else -> when {
                    age >= 18 && (vis in 1.0..1.5) -> "(Нормальная острота зрения)"
                    age >= 18 && (vis > 1.5) -> "(Острота зрения выше нормы)"
                    else -> "(Недостаточная острота зрения)"
                }
            }
            return normVis
        }

        fun comparesph(sphODinput: Double, sphOSinput: Double, comparesphtextView: TextView): String {
            if (sphODinput == 0.0 || sphOSinput == 0.0) {
                comparesphtextView.text = "" // Clear the TextView if either input is 0.0
                return "" // Возвращаем пустую строку
            }

            val sphODMinusSphOS = sphODinput - sphOSinput
            val sphOSMinusSphOD = sphOSinput - sphODinput
            val absoluteDifference = abs(sphODMinusSphOS)

            val resultText = when {
                absoluteDifference < 0.5 -> " "
                absoluteDifference in 0.5..3.0 -> "Анизометропия слабой степени"
                absoluteDifference in 3.0..6.0 -> "Анизометропия средней степени"
                absoluteDifference > 6.0 -> "Анизометропия высокой степени"
                else -> " "
            }

            // Устанавливаем текст в TextView во второй активности
            comparesphtextView.text = resultText

            // Информация о разнице между сферами правого и левого глаза
            val result1 = "Разница OD - OS: $sphODMinusSphOS"
            val result2 = "Разница OS - OD: $sphOSMinusSphOD"
            comparesphtextView.append("\n$result1\n$result2")

            // Возвращаем resultText для сохранения в профиле пациента
            return resultText
        }
    }

    override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
        if (actionId == EditorInfo.IME_ACTION_NEXT) {
            when (v?.id) {
                R.id.birthDate_edit_text -> lastNameEditText.requestFocus()
                R.id.last_name_edit_text -> firstNameEditText.requestFocus()
                R.id.first_name_edit_text -> middleNameEditText.requestFocus()
                R.id.middle_name_edit_text -> examinationdateEditText.requestFocus()
                R.id.examination_date_edit_text -> visODinput.requestFocus()
                R.id.visODinput -> visOSinput.requestFocus()
                R.id.visOSinput -> visOUinput.requestFocus()
                R.id.visOUinput -> visODcorrinput.requestFocus()
                R.id.visODcorrinput -> visOScorrinput.requestFocus()
                R.id.visOScorrinput -> visOUcorrinput.requestFocus()
                R.id.visOUcorrinput -> sphODinput.requestFocus()
                R.id.sphODinput -> cylODinput.requestFocus()
                R.id.cylODinput -> axODinput.requestFocus()
                R.id.axODinput -> sphOSinput.requestFocus()
                R.id.sphOSinput -> cylOSinput.requestFocus()
                R.id.cylOSinput -> axOSinput.requestFocus()
                else -> return false
            }
            return true
        }
        return false
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        updateAgeDescription() // Update age description when gender changes
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        // You can leave this empty or handle the case where nothing is selected
    }
}


