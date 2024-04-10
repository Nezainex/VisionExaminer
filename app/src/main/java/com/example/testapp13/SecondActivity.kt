package com.example.testapp13

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

class SecondActivity : AppCompatActivity() {

    private lateinit var genderSpinner: Spinner

    private lateinit var ageEditText: EditText
    private lateinit var agetransformtextView: TextView

    private lateinit var lastNameEditText: EditText
    private lateinit var firstNameEditText: EditText
    private lateinit var middleNameEditText: EditText
    private lateinit var dateEditText: EditText

    private lateinit var midriaticAgentSelect: Spinner
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

    private lateinit var savebutton: Button
    private lateinit var clearbutton: Button

    private var osdiResult: OsdiResult? = null
    private var rabkinResult: RabkinResult? = null
    private var ishiharaResult: IshiharaResult? = null

    private val startOsdiTestRequestCode= 1
    private val startRabkinTestRequestCode = 2
    private val startIshiharaTestRequestCode = 3

    private lateinit var osdiResultTextView: TextView
    private lateinit var rabkinResultTextView: TextView
    private lateinit var ishiharaResultTextView: TextView

    private val startOsdiForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            osdiResult = result.data?.getParcelableExtra<OsdiResult>("osdiResult")
            osdiResult?.let {
                val osdiResultTextView: TextView = findViewById(R.id.osdi_result_text_view)
                osdiResultTextView.text = "Результат OSDI: ${it.resultText} (Баллы: ${it.score})"
            }
        }
    }
    private val startRabkinForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            rabkinResult = result.data?.getParcelableExtra("rabkinResult")
            rabkinResult?.let {
                val rabkinResultTextView: TextView = findViewById(R.id.rabkin_result_text_view)
                rabkinResultTextView.text = "Результат Rabkin: ${it.resultText} (Баллы: ${it.score})"
        }
    }
}
private val startIshiharaForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
    if (result.resultCode == RESULT_OK) {
        ishiharaResult = result.data?.getParcelableExtra("ishiharaResult")
        ishiharaResult?.let {
            val ishiharaResultTextView: TextView = findViewById(R.id.ishihara_result_text_view)
            ishiharaResultTextView.text = "Результат Ishihara: ${it.resultText} (Баллы: ${it.score})"
        }
    }
}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        supportActionBar?.hide() //эта штука нужна, чтоб скрыть название активити сверху

        // Initialize views
        genderSpinner = findViewById(R.id.gender_spinner)

        ageEditText = findViewById(R.id.age_edit_text)
        agetransformtextView = findViewById(R.id.agetransform_text_View)

        lastNameEditText = findViewById(R.id.last_name_edit_text)
        firstNameEditText = findViewById(R.id.first_name_edit_text)
        middleNameEditText = findViewById(R.id.middle_name_edit_text)
        dateEditText = findViewById(R.id.date_edit_text)

        midriaticAgentSelect = findViewById(R.id.midriaticAgentSelect)
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

        osdiResultTextView = findViewById(R.id.osdi_result_text_view)
        rabkinResultTextView = findViewById(R.id.rabkin_result_text_view)
        ishiharaResultTextView = findViewById(R.id.ishihara_result_text_view)

        savebutton = findViewById(R.id.buttonsave)
        clearbutton = findViewById(R.id.buttonclear)

        // Настройка spinner для выбора мидриатика
        val midrAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.midriaticAgentArray,
            android.R.layout.simple_spinner_item
        )
        midriaticAgentSelect.adapter = midrAdapter

        // Set up gender spinner
        val genders = arrayOf("Мужской", "Женский")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, genders)
        genderSpinner.adapter = adapter


        // Create a single TextWatcher instance
        val textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                updateAgeDescription()
                updateVisDescriptionOD()
                updateVisDescriptionOS()
                updateVisDescriptionOU()
                updateVisDescriptionODcorr()
                updateVisDescriptionOScorr()
                updateVisDescriptionOUcorr()
                updateSphDescriptionOD()
                updateSphDescriptionOS()
                updateComparesphText()
                updateCylDescriptionOD()
                updateCylDescriptionOS()
                transposeOD()
                transposeOS()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }

        // Set up date picker dialog
        val calendar = Calendar.getInstance()
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            dateEditText.setText(dateFormat.format(calendar.time))
        }

        dateEditText.setOnClickListener {
            DatePickerDialog(
                this,
                dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        savebutton.setOnClickListener {
            // Get data from input fields
            val lastName = lastNameEditText.text.toString()
            val firstName = firstNameEditText.text.toString()
            val middleName = middleNameEditText.text.toString()
            val gender = genderSpinner.selectedItem.toString()

            // Convert age to Double
            val age = try {
                ageEditText.text.toString().toDouble()
            } catch (e: Exception) {
                0.0
            }
            val date = dateEditText.text.toString()

            // Get data from visual acuity fields
            val visOD = visODinput.text.toString().toDoubleOrNull() ?: 0.0
            val visOS = visOSinput.text.toString().toDoubleOrNull() ?: 0.0
            val visOU = visOUinput.text.toString().toDoubleOrNull() ?: 0.0
            val visODcorr = visODcorrinput.text.toString().toDoubleOrNull() ?: 0.0
            val visOScorr = visOScorrinput.text.toString().toDoubleOrNull() ?: 0.0
            val visOUcorr = visOUcorrinput.text.toString().toDoubleOrNull() ?: 0.0

            // Get data from refraction fields
            val sphOD = sphODinput.text.toString().toDoubleOrNull() ?: 0.0
            val cylOD = cylODinput.text.toString().toDoubleOrNull() ?: 0.0
            val axOD = axODinput.text.toString().toDoubleOrNull() ?: 0.0
            val sphOS = sphOSinput.text.toString().toDoubleOrNull() ?: 0.0
            val cylOS = cylOSinput.text.toString().toDoubleOrNull() ?: 0.0
            val axOS = axOSinput.text.toString().toDoubleOrNull() ?: 0.0

            // Get data from transposition result fields
            val sphODLabel =  sphODLabel.text.toString()
            val cylODLabel = cylODLabel.text.toString()
            val axODLabel = axODLabel.text.toString()
            val sphOSLabel =  sphOSLabel.text.toString()
            val cylOSLabel = cylOSLabel.text.toString()
            val axOSLabel = axOSLabel.text.toString()

            val comparesphResult = comparesphtextView.text.toString()

            // Get data from midriatic agent spinner
            val midriaticAgent = midriaticAgentSelect.selectedItem.toString()

            // Create a PatientProfile object
            val newProfile = PatientProfile(
                lastName = lastName,
                firstName = firstName,
                middleName = middleName,
                gender = gender,
                age = age,
                date = date,
                visOD = visOD,
                visOS = visOS,
                visOU = visOU,
                visODcorr = visODcorr,
                visOScorr = visOScorr,
                visOUcorr = visOUcorr,
                sphOD = sphOD,
                cylOD = cylOD,
                axOD = axOD,
                sphOS = sphOS,
                cylOS = cylOS,
                axOS = axOS,
                sphODLabel = sphODLabel,
                cylODLabel = cylODLabel,
                axODLabel = axODLabel,
                sphOSLabel = sphOSLabel,
                cylOSLabel = cylOSLabel,
                axOSLabel = axOSLabel,
                comparesphResult = comparesphResult,
                midriaticAgent = midriaticAgent
            )

            // Save the profile (or pass it to FifthActivity)
            saveProfile(newProfile)
            // ... (other code)
        }

        val fourthActbutton = findViewById<Button>(R.id.fourth_act_button)
        fourthActbutton.setOnClickListener {
            val intent = Intent(this, FourthActivity::class.java)
            startOsdiForResult.launch(intent) // Запуск FourthActivity с startOsdiForResult
        }

        val sixthActbutton = findViewById<Button>(R.id.sixth_act_button)
        sixthActbutton.setOnClickListener {
            val intent = Intent(this, SixthActivity::class.java)
            startRabkinForResult.launch(intent) // Запуск SixthActivity с startRabkinForResult
        }
        val seventhActbutton = findViewById<Button>(R.id.seventh_act_button)
        seventhActbutton.setOnClickListener {
            val intent = Intent(this, SeventhActivity::class.java)
            startIshiharaForResult.launch(intent) // Запуск SeventhActivity с startIshiharaForResult
        }

        // Обработчик нажатия кнопки "Clear"
        clearbutton.setOnClickListener {
            // Очистка текстовых полей
            ageEditText.text.clear()
            lastNameEditText.text.clear()
            firstNameEditText.text.clear()
            middleNameEditText.text.clear()
            dateEditText.text.clear()

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

            // Очистка спиннеров
            genderSpinner.setSelection(0)
            midriaticAgentSelect.setSelection(0)

        }
        // Attach the TextWatcher to all relevant input fields
        ageEditText.addTextChangedListener(textWatcher)
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

    }

    @SuppressLint("SetTextI18n")
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                startOsdiTestRequestCode -> {
                    osdiResult = data?.getParcelableExtra("osdiResult")
                    osdiResult?.let {
                        osdiResultTextView.text = "Результат OSDI: ${it.resultText} (Баллы: ${it.score})"
                    }
                }
                startRabkinTestRequestCode -> {
                    rabkinResult = data?.getParcelableExtra("rabkinResult")
                    rabkinResult?.let {
                        val rabkinResultTextView: TextView = findViewById(R.id.rabkin_result_text_view)
                        rabkinResultTextView.text = "Результат Rabkin: ${it.resultText} (Баллы: ${it.score})"
                    }
                }
                startIshiharaTestRequestCode -> {
                    ishiharaResult = data?.getParcelableExtra("ishiharaResult")
                    ishiharaResult?.let {
                        val ishiharaResultTextView: TextView = findViewById(R.id.ishihara_result_text_view)
                        ishiharaResultTextView.text = "Результат Ishihara: ${it.resultText} (Баллы: ${it.score})"
                    }
                }
            }
        }
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

    private fun saveProfile(profile: PatientProfile) {
        val database = DatabaseInstance.getInstance(this)
        lifecycleScope.launch(Dispatchers.IO) {
            val profileId = database.patientProfileDao().insert(profile)

            // Сохранение результатов тестов
            osdiResult?.let {
                it.patientProfileId = profileId.toInt()
                database.patientProfileDao().insertOsdiResult(it)
            }
            rabkinResult?.let {
                it.patientProfileId = profileId.toInt()
                database.patientProfileDao().insertRabkinResult(it)
            }
            ishiharaResult?.let {
                it.patientProfileId = profileId.toInt()
                database.patientProfileDao().insertIshiharaResult(it)
            }
        }

        Toast.makeText(this, "Профиль сохранен", Toast.LENGTH_SHORT).show()


        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("profileId", profile.id) // Передача ID профиля
        startActivity(intent)
    }

    // Обработчик изменения текста в поле ввода ageEditText
    private fun updateAgeDescription() {
        val age = try { ageEditText.text.toString().toDouble() } catch (e: Exception) { 0.0 }
        val isMale = genderSpinner.selectedItem.toString() == "Мужской"
        agetransformtextView.text = ageTransform(age, isMale)
    }
    // Обработчик изменения текста в поле ввода visODinput
    private fun updateVisDescriptionOD() {
        val vis = try { visODinput.text.toString().toDouble() } catch (e: Exception) { 0.0 }
        val age = try { ageEditText.text.toString().toDouble() } catch (e: Exception) { 0.0 }
        vistransformtextViewOD.text = visTransformOD(vis, age)
    }
    // Обработчик изменения текста в поле ввода visOSinput
    private fun updateVisDescriptionOS() {
        val vis = try { visOSinput.text.toString().toDouble() } catch (e: Exception) { 0.0 }
        val age = try { ageEditText.text.toString().toDouble() } catch (e: Exception) { 0.0 }
        vistransformtextViewOS.text = visTransformOS(vis, age)
    }
    // Обработчик изменения текста в поле ввода visOUinput
    private fun updateVisDescriptionOU() {
        val vis = try { visOUinput.text.toString().toDouble() } catch (e: Exception) { 0.0 }
        val age = try { ageEditText.text.toString().toDouble() } catch (e: Exception) { 0.0 }
        vistransformtextViewOU.text = visTransformOU(vis, age)
    }
    // Обработчик изменения текста в поле ввода visODcorrinput
    private fun updateVisDescriptionODcorr() {
        val vis = try { visODcorrinput.text.toString().toDouble() } catch (e: Exception) { 0.0 }
        val age = try { ageEditText.text.toString().toDouble() } catch (e: Exception) { 0.0 }
        vistransformtextViewODcorr.text = visTransformODcorr(vis, age)
    }
    // Обработчик изменения текста в поле ввода visOScorrinput
    private fun updateVisDescriptionOScorr() {
        val vis = try { visOScorrinput.text.toString().toDouble() } catch (e: Exception) { 0.0 }
        val age = try { ageEditText.text.toString().toDouble() } catch (e: Exception) { 0.0 }
        vistransformtextViewOScorr.text = visTransformOScorr(vis, age)
    }
    // Обработчик изменения текста в поле ввода visOUcorrinput
    private fun updateVisDescriptionOUcorr() {
        val vis = try { visOUcorrinput.text.toString().toDouble() } catch (e: Exception) { 0.0 }
        val age = try { ageEditText.text.toString().toDouble() } catch (e: Exception) { 0.0 }
        vistransformtextViewOUcorr.text = visTransformOUcorr(vis, age)
    }
    // Обработчик изменения текста в поле ввода sphODinput
    private fun updateSphDescriptionOD() {
        val sphOD = try { sphODinput.text.toString().toDouble() } catch (e: Exception) { 0.0 }
        val age = if (ageEditText.text.toString().isNotEmpty()) {
            try { ageEditText.text.toString().toDouble() } catch (e: Exception) { 0.0 }
        } else {
            0.0
        }
        textViewsphOD.text = sphCalculateOD(sphOD, age)
    }
    // Обработчик изменения текста в поле ввода sphOSinput
    private fun updateSphDescriptionOS() {
        val sphOS = try { sphOSinput.text.toString().toDouble() } catch (e: Exception) { 0.0 }
        val age = if (ageEditText.text.toString().isNotEmpty()) {
            try { ageEditText.text.toString().toDouble() } catch (e: Exception) { 0.0 }
        } else {
            0.0
        }
        textViewsphOS.text = sphCalculateOS(sphOS, age)
    }

    // Обработчик изменения текста в поле ввода cylODinput
    private fun updateCylDescriptionOD() {
        val cylOD = try { cylODinput.text.toString().toDouble() } catch (e: Exception) { 0.0 }
        textViewcylOD.text = cylCalculateOD(cylOD)
    }
    // Обработчик изменения текста в поле ввода cylOSinput
    private fun updateCylDescriptionOS() {
        val cylOS = try { cylOSinput.text.toString().toDouble() } catch (e: Exception) { 0.0 }
        textViewcylOS.text = cylCalculateOS(cylOS)
    }

    // Функция, определяющая степень миопии/гиперметропии, в зависимости от данных sph для OD
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

    companion object {
        fun sphCalculateOD(sphOD: Double, age: Double = 0.0): String {
            if (sphOD == 0.0) return "OD эмметропия"
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

            if (sphOD >= -0.74 + correction && sphOD <= 0.74 + correction) return "OD эмметропия"
            return when {
                sphOD <= -0.75 + correction && sphOD > -3.25 + correction -> "OD миопия \n слабой степени"
                sphOD <= -3.25 + correction && sphOD > -6.25 + correction -> "OD миопия \n средней степени"
                sphOD <= -6.25 + correction -> "OD миопия \n высокой степени"
                sphOD >= 0.75 + correction && sphOD < 2.5 + correction -> "OD гиперметропия \n слабой степени"
                sphOD >= 2.5 + correction && sphOD < 5 + correction -> "OD гиперметропия \n средней степени"
                sphOD >= 5 + correction -> "OD гиперметропия \n высокой степени"
                else -> "OD некорректное \n значение sph"
            }
        }

    // Функция, определяющая степень миопии/гиперметропии, в зависимости от данных sph для OS
        fun sphCalculateOS(sphOS: Double, age: Double = 0.0): String {
            if (sphOS == 0.0) return "OS эмметропия"
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
            if (sphOS >= -0.74 + correction && sphOS <= 0.74 + correction) return "OS эмметропия"
            return when {
                sphOS <= -0.75 + correction && sphOS > -3.25 + correction -> "OS миопия \n слабой степени"
                sphOS <= -3.25 + correction && sphOS > -6.25 + correction -> "OS миопия \n средней степени"
                sphOS <= -6.25 + correction -> "OS миопия \n высокой степени"
                sphOS >= 0.75 + correction && sphOS < 2.5 + correction -> "OS гиперметропия \n слабой степени"
                sphOS >= 2.5 + correction && sphOS < 5 + correction -> "OS гиперметропия \n средней степени"
                sphOS >= 5 + correction -> "OS гиперметропия \n высокой степени"
                else -> "OS некорректное \n значение sph"
            }
        }

        // Функция, определяющая степень астигматизма, в зависимости от данных cyl для OD
        fun cylCalculateOD(cylOD: Double): String {
            if (cylOD == 0.0) return ""
            if (cylOD >= -0.74 && cylOD <= 0.74 ) return ""
            return if ((cylOD <= -0.75 && cylOD > -3 ) || (cylOD >= 0.75 && cylOD < 3 )) {
                "OD астигматизм \n слабой степени"
            } else if ((cylOD <= -3 && cylOD > -6 ) || (cylOD >= 3 && cylOD < 6 )) {
                "OD астигматизм \n средней степени"
            } else if (cylOD <=-6 || cylOD >= 6) {
                "OD астигматизм \n высокой степени"
            } else "OD некорректное \n значение cyl"
        }

        // Функция, определяющая степень астигматизма, в зависимости от данных cyl для OS
        fun cylCalculateOS(cylOS: Double): String {
            if (cylOS == 0.0) return ""
            if (cylOS >= -0.74 && cylOS <= 0.74 ) return ""
            return if ((cylOS <= -0.75 && cylOS > -3 ) || (cylOS >= 0.75 && cylOS < 3 )) {
                "OS астигматизм \n слабой степени"
            } else if ((cylOS <= -3 && cylOS > -6 ) || (cylOS >= 3 && cylOS < 6 )) {
                "OS астигматизм \n средней степени"
            } else if (cylOS <=-6 || cylOS >= 6) {
                "OS астигматизм \n высокой степени"
            } else "OS некорректное \n значение cyl"
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
            } else if ((age >= 6 && age < 12 && isMale) || (age >= 6 && age < 10 && !isMale)) {
                return "(Младший школьный возраст)"
            } else if ((age >= 12 && age < 18 && isMale) || (age >= 10 && age < 18 && !isMale)) {
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

        // Функция рассчёта остроты зрения для OD
        fun visTransformOD(vis: Double, age: Double): String {
            if (age == 0.0 || vis == 0.0) return ""
            val normVis = when {
                (0.0191781 <= age && age <= 0.0833334) && (0.002 <= vis && vis <= 0.02) -> "(Нормальная острота зрения)"
                (0.0191781 <= age && age <= 0.0833334) && (vis > 0.02) -> "(Острота зрения выше нормы)"
                (0.0833334 <= age && age <= 0.25) && (0.008 <= vis && vis <= 0.03) -> "(Нормальная острота зрения)"
                (0.0833334 <= age && age <= 0.25) && (vis > 0.03) -> "(Острота зрения выше нормы)"
                (0.25 <= age && age <= 0.5) && (0.05 <= vis && vis <= 0.1) -> "(Нормальная острота зрения)"
                (0.25 <= age && age <= 0.5) && (vis > 0.1) -> "(Острота зрения выше нормы)"
                (0.5 <= age && age <= 1) && (0.3 <= vis && vis <= 0.6) -> "(Нормальная острота зрения)"
                (0.5 <= age && age <= 1) && (vis > 0.6) -> "(Острота зрения выше нормы)"
                (1 <= age && age <= 2) && (0.3 <= vis && vis <= 0.6) -> "(Нормальная острота зрения)"
                (1 <= age && age <= 2) && (vis > 0.6) -> "(Острота зрения выше нормы)"
                (2 <= age && age <= 3) && (0.4 <= vis && vis <= 0.7) -> "(Нормальная острота зрения)"
                (2 <= age && age <= 3) && (vis > 0.7) -> "(Острота зрения выше нормы)"
                (3 <= age && age <= 4) && (0.6 <= vis && vis <= 0.9) -> "(Нормальная острота зрения)"
                (3 <= age && age <= 4) && (vis > 0.9) -> "(Острота зрения выше нормы)"
                (4 <= age && age <= 5) && (0.7 <= vis && vis <= 1.0) -> "(Нормальная острота зрения)"
                (4 <= age && age <= 5) && (vis > 1.0) -> "(Острота зрения выше нормы)"
                (5 <= age && age <= 7) && (0.8 <= vis && vis <= 1.0) -> "(Нормальная острота зрения)"
                (5 <= age && age <= 7) && (vis > 1.0) -> "(Острота зрения выше нормы)"
                (7 <= age && age <= 8) && (0.9 <= vis && vis <= 1.2) -> "(Нормальная острота зрения)"
                (7 <= age && age <= 8) && (vis > 1.2) -> "(Острота зрения выше нормы)"
                (8 <= age && age <= 15) && (0.9 <= vis && vis <= 1.5) -> "(Нормальная острота зрения)"
                (8 <= age && age <= 15) && (vis > 1.5) -> "(Острота зрения выше нормы)"
                (15 <= age && age < 18) && (1.0 <= vis && vis <= 1.5) -> "(Нормальная острота зрения)"
                (15 <= age && age < 18) && (vis > 1.5) -> "(Острота зрения выше нормы)"
                else -> when {
                    age >= 18 && (1.0 <= vis && vis <= 1.5) -> "(Нормальная острота зрения)"
                    age >= 18 && (vis > 1.5) -> "(Острота зрения выше нормы)"
                    else -> "(Недостаточная острота зрения)"
                }
            }
            return normVis
        }


        // Функция рассчёта остроты зрения для OS
        fun visTransformOS(vis: Double, age: Double): String {
            if (age == 0.0 || vis == 0.0) return ""
            val normVis = when {
                (0.0191781 <= age && age <= 0.0833334) && (0.002 <= vis && vis <= 0.02) -> "(Нормальная острота зрения)"
                (0.0191781 <= age && age <= 0.0833334) && (vis > 0.02) -> "(Острота зрения выше нормы)"
                (0.0833334 <= age && age <= 0.25) && (0.008 <= vis && vis <= 0.03) -> "(Нормальная острота зрения)"
                (0.0833334 <= age && age <= 0.25) && (vis > 0.03) -> "(Острота зрения выше нормы)"
                (0.25 <= age && age <= 0.5) && (0.05 <= vis && vis <= 0.1) -> "(Нормальная острота зрения)"
                (0.25 <= age && age <= 0.5) && (vis > 0.1) -> "(Острота зрения выше нормы)"
                (0.5 <= age && age <= 1) && (0.3 <= vis && vis <= 0.6) -> "(Нормальная острота зрения)"
                (0.5 <= age && age <= 1) && (vis > 0.6) -> "(Острота зрения выше нормы)"
                (1 <= age && age <= 2) && (0.3 <= vis && vis <= 0.6) -> "(Нормальная острота зрения)"
                (1 <= age && age <= 2) && (vis > 0.6) -> "(Острота зрения выше нормы)"
                (2 <= age && age <= 3) && (0.4 <= vis && vis <= 0.7) -> "(Нормальная острота зрения)"
                (2 <= age && age <= 3) && (vis > 0.7) -> "(Острота зрения выше нормы)"
                (3 <= age && age <= 4) && (0.6 <= vis && vis <= 0.9) -> "(Нормальная острота зрения)"
                (3 <= age && age <= 4) && (vis > 0.9) -> "(Острота зрения выше нормы)"
                (4 <= age && age <= 5) && (0.7 <= vis && vis <= 1.0) -> "(Нормальная острота зрения)"
                (4 <= age && age <= 5) && (vis > 1.0) -> "(Острота зрения выше нормы)"
                (5 <= age && age <= 7) && (0.8 <= vis && vis <= 1.0) -> "(Нормальная острота зрения)"
                (5 <= age && age <= 7) && (vis > 1.0) -> "(Острота зрения выше нормы)"
                (7 <= age && age <= 8) && (0.9 <= vis && vis <= 1.2) -> "(Нормальная острота зрения)"
                (7 <= age && age <= 8) && (vis > 1.2) -> "(Острота зрения выше нормы)"
                (8 <= age && age <= 15) && (0.9 <= vis && vis <= 1.5) -> "(Нормальная острота зрения)"
                (8 <= age && age <= 15) && (vis > 1.5) -> "(Острота зрения выше нормы)"
                (15 <= age && age < 18) && (1.0 <= vis && vis <= 1.5) -> "(Нормальная острота зрения)"
                (15 <= age && age < 18) && (vis > 1.5) -> "(Острота зрения выше нормы)"
                else -> when {
                    age >= 18 && (1.0 <= vis && vis <= 1.5) -> "(Нормальная острота зрения)"
                    age >= 18 && (vis > 1.5) -> "(Острота зрения выше нормы)"
                    else -> "(Недостаточная острота зрения)"
                }
            }
            return normVis
        }
        // Функция рассчёта остроты зрения для OU
        fun visTransformOU(vis: Double, age: Double): String {
            if (age == 0.0 || vis == 0.0) return ""
            val normVis = when {
                (0.0191781 <= age && age <= 0.0833334) && (0.002 <= vis && vis <= 0.02) -> "(Нормальная острота зрения)"
                (0.0191781 <= age && age <= 0.0833334) && (vis > 0.02) -> "(Острота зрения выше нормы)"
                (0.0833334 <= age && age <= 0.25) && (0.008 <= vis && vis <= 0.03) -> "(Нормальная острота зрения)"
                (0.0833334 <= age && age <= 0.25) && (vis > 0.03) -> "(Острота зрения выше нормы)"
                (0.25 <= age && age <= 0.5) && (0.05 <= vis && vis <= 0.1) -> "(Нормальная острота зрения)"
                (0.25 <= age && age <= 0.5) && (vis > 0.1) -> "(Острота зрения выше нормы)"
                (0.5 <= age && age <= 1) && (0.3 <= vis && vis <= 0.6) -> "(Нормальная острота зрения)"
                (0.5 <= age && age <= 1) && (vis > 0.6) -> "(Острота зрения выше нормы)"
                (1 <= age && age <= 2) && (0.3 <= vis && vis <= 0.6) -> "(Нормальная острота зрения)"
                (1 <= age && age <= 2) && (vis > 0.6) -> "(Острота зрения выше нормы)"
                (2 <= age && age <= 3) && (0.4 <= vis && vis <= 0.7) -> "(Нормальная острота зрения)"
                (2 <= age && age <= 3) && (vis > 0.7) -> "(Острота зрения выше нормы)"
                (3 <= age && age <= 4) && (0.6 <= vis && vis <= 0.9) -> "(Нормальная острота зрения)"
                (3 <= age && age <= 4) && (vis > 0.9) -> "(Острота зрения выше нормы)"
                (4 <= age && age <= 5) && (0.7 <= vis && vis <= 1.0) -> "(Нормальная острота зрения)"
                (4 <= age && age <= 5) && (vis > 1.0) -> "(Острота зрения выше нормы)"
                (5 <= age && age <= 7) && (0.8 <= vis && vis <= 1.0) -> "(Нормальная острота зрения)"
                (5 <= age && age <= 7) && (vis > 1.0) -> "(Острота зрения выше нормы)"
                (7 <= age && age <= 8) && (0.9 <= vis && vis <= 1.2) -> "(Нормальная острота зрения)"
                (7 <= age && age <= 8) && (vis > 1.2) -> "(Острота зрения выше нормы)"
                (8 <= age && age <= 15) && (0.9 <= vis && vis <= 1.5) -> "(Нормальная острота зрения)"
                (8 <= age && age <= 15) && (vis > 1.5) -> "(Острота зрения выше нормы)"
                (15 <= age && age < 18) && (1.0 <= vis && vis <= 1.5) -> "(Нормальная острота зрения)"
                (15 <= age && age < 18) && (vis > 1.5) -> "(Острота зрения выше нормы)"
                else -> when {
                    age >= 18 && (1.0 <= vis && vis <= 1.5) -> "(Нормальная острота зрения)"
                    age >= 18 && (vis > 1.5) -> "(Острота зрения выше нормы)"
                    else -> "(Недостаточная острота зрения)"
                }
            }
            return normVis
        }
        // Функция рассчёта остроты зрения для OD corr
        fun visTransformODcorr(vis: Double, age: Double): String {
            if (age == 0.0 || vis == 0.0) return ""
            val normVis = when {
                (0.0191781 <= age && age <= 0.0833334) && (0.002 <= vis && vis <= 0.02) -> "(Нормальная острота зрения)"
                (0.0191781 <= age && age <= 0.0833334) && (vis > 0.02) -> "(Острота зрения выше нормы)"
                (0.0833334 <= age && age <= 0.25) && (0.008 <= vis && vis <= 0.03) -> "(Нормальная острота зрения)"
                (0.0833334 <= age && age <= 0.25) && (vis > 0.03) -> "(Острота зрения выше нормы)"
                (0.25 <= age && age <= 0.5) && (0.05 <= vis && vis <= 0.1) -> "(Нормальная острота зрения)"
                (0.25 <= age && age <= 0.5) && (vis > 0.1) -> "(Острота зрения выше нормы)"
                (0.5 <= age && age <= 1) && (0.3 <= vis && vis <= 0.6) -> "(Нормальная острота зрения)"
                (0.5 <= age && age <= 1) && (vis > 0.6) -> "(Острота зрения выше нормы)"
                (1 <= age && age <= 2) && (0.3 <= vis && vis <= 0.6) -> "(Нормальная острота зрения)"
                (1 <= age && age <= 2) && (vis > 0.6) -> "(Острота зрения выше нормы)"
                (2 <= age && age <= 3) && (0.4 <= vis && vis <= 0.7) -> "(Нормальная острота зрения)"
                (2 <= age && age <= 3) && (vis > 0.7) -> "(Острота зрения выше нормы)"
                (3 <= age && age <= 4) && (0.6 <= vis && vis <= 0.9) -> "(Нормальная острота зрения)"
                (3 <= age && age <= 4) && (vis > 0.9) -> "(Острота зрения выше нормы)"
                (4 <= age && age <= 5) && (0.7 <= vis && vis <= 1.0) -> "(Нормальная острота зрения)"
                (4 <= age && age <= 5) && (vis > 1.0) -> "(Острота зрения выше нормы)"
                (5 <= age && age <= 7) && (0.8 <= vis && vis <= 1.0) -> "(Нормальная острота зрения)"
                (5 <= age && age <= 7) && (vis > 1.0) -> "(Острота зрения выше нормы)"
                (7 <= age && age <= 8) && (0.9 <= vis && vis <= 1.2) -> "(Нормальная острота зрения)"
                (7 <= age && age <= 8) && (vis > 1.2) -> "(Острота зрения выше нормы)"
                (8 <= age && age <= 15) && (0.9 <= vis && vis <= 1.5) -> "(Нормальная острота зрения)"
                (8 <= age && age <= 15) && (vis > 1.5) -> "(Острота зрения выше нормы)"
                (15 <= age && age < 18) && (1.0 <= vis && vis <= 1.5) -> "(Нормальная острота зрения)"
                (15 <= age && age < 18) && (vis > 1.5) -> "(Острота зрения выше нормы)"
                else -> when {
                    age >= 18 && (1.0 <= vis && vis <= 1.5) -> "(Нормальная острота зрения)"
                    age >= 18 && (vis > 1.5) -> "(Острота зрения выше нормы)"
                    else -> "(Недостаточная острота зрения)"
                }
            }
            return normVis
        }

        // Функция рассчёта остроты зрения для OS corr
        fun visTransformOScorr(vis: Double, age: Double): String {
            if (age == 0.0 || vis == 0.0) return ""
            val normVis = when {
                (0.0191781 <= age && age <= 0.0833334) && (0.002 <= vis && vis <= 0.02) -> "(Нормальная острота зрения)"
                (0.0191781 <= age && age <= 0.0833334) && (vis > 0.02) -> "(Острота зрения выше нормы.)"
                (0.0833334 <= age && age <= 0.25) && (0.008 <= vis && vis <= 0.03) -> "(Нормальная острота зрения)"
                (0.0833334 <= age && age <= 0.25) && (vis > 0.03) -> "(Острота зрения выше нормы)"
                (0.25 <= age && age <= 0.5) && (0.05 <= vis && vis <= 0.1) -> "(Нормальная острота зрения)"
                (0.25 <= age && age <= 0.5) && (vis > 0.1) -> "(Острота зрения выше нормы)"
                (0.5 <= age && age <= 1) && (0.3 <= vis && vis <= 0.6) -> "(Нормальная острота зрения)"
                (0.5 <= age && age <= 1) && (vis > 0.6) -> "(Острота зрения выше нормы)"
                (1 <= age && age <= 2) && (0.3 <= vis && vis <= 0.6) -> "(Нормальная острота зрения)"
                (1 <= age && age <= 2) && (vis > 0.6) -> "(Острота зрения выше нормы)"
                (2 <= age && age <= 3) && (0.4 <= vis && vis <= 0.7) -> "(Нормальная острота зрения)"
                (2 <= age && age <= 3) && (vis > 0.7) -> "(Острота зрения выше нормы)"
                (3 <= age && age <= 4) && (0.6 <= vis && vis <= 0.9) -> "(Нормальная острота зрения)"
                (3 <= age && age <= 4) && (vis > 0.9) -> "(Острота зрения выше нормы)"
                (4 <= age && age <= 5) && (0.7 <= vis && vis <= 1.0) -> "(Нормальная острота зрения)"
                (4 <= age && age <= 5) && (vis > 1.0) -> "(Острота зрения выше нормы)"
                (5 <= age && age <= 7) && (0.8 <= vis && vis <= 1.0) -> "(Нормальная острота зрения)"
                (5 <= age && age <= 7) && (vis > 1.0) -> "(Острота зрения выше нормы)"
                (7 <= age && age <= 8) && (0.9 <= vis && vis <= 1.2) -> "(Нормальная острота зрения)"
                (7 <= age && age <= 8) && (vis > 1.2) -> "(Острота зрения выше нормы)"
                (8 <= age && age <= 15) && (0.9 <= vis && vis <= 1.5) -> "(Нормальная острота зрения)"
                (8 <= age && age <= 15) && (vis > 1.5) -> "(Острота зрения выше нормы)"
                (15 <= age && age < 18) && (1.0 <= vis && vis <= 1.5) -> "(Нормальная острота зрения)"
                (15 <= age && age < 18) && (vis > 1.5) -> "(Острота зрения выше нормы)"
                else -> when {
                    age >= 18 && (1.0 <= vis && vis <= 1.5) -> "(Нормальная острота зрения)"
                    age >= 18 && (vis > 1.5) -> "(Острота зрения выше нормы)"
                    else -> "(Недостаточная острота зрения)"
                }
            }
            return normVis
        }
        // Функция рассчёта остроты зрения для OU corr
        fun visTransformOUcorr(vis: Double, age: Double): String {
            if (age == 0.0 || vis == 0.0) return ""
            val normVis = when {
                (0.0191781 <= age && age <= 0.0833334) && (0.002 <= vis && vis <= 0.02) -> "(Нормальная острота зрения)"
                (0.0191781 <= age && age <= 0.0833334) && (vis > 0.02) -> "(Острота зрения выше нормы)"
                (0.0833334 <= age && age <= 0.25) && (0.008 <= vis && vis <= 0.03) -> "(Нормальная острота зрения)"
                (0.0833334 <= age && age <= 0.25) && (vis > 0.03) -> "(Острота зрения выше нормы)"
                (0.25 <= age && age <= 0.5) && (0.05 <= vis && vis <= 0.1) -> "(Нормальная острота зрения)"
                (0.25 <= age && age <= 0.5) && (vis > 0.1) -> "(Острота зрения выше нормы)"
                (0.5 <= age && age <= 1) && (0.3 <= vis && vis <= 0.6) -> "(Нормальная острота зрения)"
                (0.5 <= age && age <= 1) && (vis > 0.6) -> "(Острота зрения выше нормы)"
                (1 <= age && age <= 2) && (0.3 <= vis && vis <= 0.6) -> "(Нормальная острота зрения)"
                (1 <= age && age <= 2) && (vis > 0.6) -> "(Острота зрения выше нормы)"
                (2 <= age && age <= 3) && (0.4 <= vis && vis <= 0.7) -> "(Нормальная острота зрения)"
                (2 <= age && age <= 3) && (vis > 0.7) -> "(Острота зрения выше нормы)"
                (3 <= age && age <= 4) && (0.6 <= vis && vis <= 0.9) -> "(Нормальная острота зрения)"
                (3 <= age && age <= 4) && (vis > 0.9) -> "(Острота зрения выше нормы)"
                (4 <= age && age <= 5) && (0.7 <= vis && vis <= 1.0) -> "(Нормальная острота зрения)"
                (4 <= age && age <= 5) && (vis > 1.0) -> "(Острота зрения выше нормы)"
                (5 <= age && age <= 7) && (0.8 <= vis && vis <= 1.0) -> "(Нормальная острота зрения)"
                (5 <= age && age <= 7) && (vis > 1.0) -> "(Острота зрения выше нормы)"
                (7 <= age && age <= 8) && (0.9 <= vis && vis <= 1.2) -> "(Нормальная острота зрения)"
                (7 <= age && age <= 8) && (vis > 1.2) -> "(Острота зрения выше нормы)"
                (8 <= age && age <= 15) && (0.9 <= vis && vis <= 1.5) -> "(Нормальная острота зрения)"
                (8 <= age && age <= 15) && (vis > 1.5) -> "(Острота зрения выше нормы)"
                (15 <= age && age < 18) && (1.0 <= vis && vis <= 1.5) -> "(Нормальная острота зрения)"
                (15 <= age && age < 18) && (vis > 1.5) -> "(Острота зрения выше нормы)"
                else -> when {
                    age >= 18 && (1.0 <= vis && vis <= 1.5) -> "(Нормальная острота зрения)"
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
}



