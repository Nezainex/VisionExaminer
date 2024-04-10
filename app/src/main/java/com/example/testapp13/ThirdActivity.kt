package com.example.testapp13

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ThirdActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third)
        supportActionBar?.hide()

        // Get the PatientProfile object from the Intent
        val profile = intent.getParcelableExtra("profile", PatientProfile::class.java)

        // Find TextViews in your layout (replace with your actual IDs)
        val lastNameTextViewTA: TextView = findViewById(R.id.lastNameTextViewTA)
        val firstNameTextViewTA: TextView = findViewById(R.id.firstNameTextViewTA)
        val middleNameTextViewTA: TextView = findViewById(R.id.middleNameTextViewTA)
        val genderTextViewTA: TextView = findViewById(R.id.genderTextViewTA)
        val ageTextViewTA: TextView = findViewById(R.id.ageTextViewTA)
        val dateTextViewTA: TextView = findViewById(R.id.dateTextViewTA)

        val visODtextViewTA: TextView = findViewById(R.id.visODtextViewTA)
        val visOStextViewTA: TextView = findViewById(R.id.visOStextViewTA)
        val visOUtextViewTA: TextView = findViewById(R.id.visOUtextViewTA)
        val visODcorrtextViewTA: TextView = findViewById(R.id.visODcorrtextViewTA)
        val visOScorrtextViewTA: TextView = findViewById(R.id.visOScorrtextViewTA)
        val visOUcorrtextViewTA: TextView = findViewById(R.id.visOUcorrtextViewTA)

        val sphODtextViewTA: TextView = findViewById(R.id.sphODtextViewTA)
        val cylODtextViewTA: TextView = findViewById(R.id.cylODtextViewTA)
        val axODtextViewTA: TextView = findViewById(R.id.axODtextViewTA)

        val sphOStextViewTA: TextView = findViewById(R.id.sphOStextViewTA)
        val cylOStextViewTA: TextView = findViewById(R.id.cylOStextViewTA)
        val axOStextViewTA: TextView = findViewById(R.id.axOStextViewTA)

        val sphODLabeltextViewTA: TextView = findViewById(R.id.sphODLabeltextViewTA)
        val cylODLabeltextViewTA: TextView = findViewById(R.id.cylODLabeltextViewTA)
        val axODLabeltextViewTA: TextView = findViewById(R.id.axODLabeltextViewTA)

        val sphOSLabeltextViewTA: TextView = findViewById(R.id.sphOSLabeltextViewTA)
        val cylOSLabeltextViewTA: TextView = findViewById(R.id.cylOSLabeltextViewTA)
        val axOSLabeltextViewTA: TextView = findViewById(R.id.axOSLabeltextViewTA)

        val vistransformtextViewOD: TextView = findViewById(R.id.vistransformtextViewOD)
        val vistransformtextViewOS: TextView = findViewById(R.id.vistransformtextViewOS)
        val vistransformtextViewOU: TextView = findViewById(R.id.vistransformtextViewOU)

        val vistransformtextViewODcorr: TextView = findViewById(R.id.vistransformtextViewODcorr)
        val vistransformtextViewOScorr: TextView = findViewById(R.id.vistransformtextViewOScorr)
        val vistransformtextViewOUcorr: TextView = findViewById(R.id.vistransformtextViewOUcorr)

        val agetransformTextView: TextView = findViewById(R.id.agetransform_text_View)

        val textViewsphOD: TextView = findViewById(R.id.textViewsphOD)
        val textViewcylOD: TextView = findViewById(R.id.textViewcylOD)
      //val textViewaxOD: TextView = findViewById(R.id.textViewaxOD)

        val textViewsphOS: TextView = findViewById(R.id.textViewsphOS)
        val textViewcylOS: TextView = findViewById(R.id.textViewcylOS)
      //val textViewaxOS: TextView = findViewById(R.id.textViewaxOS)

        val comparesphtextView: TextView = findViewById(R.id.comparesphtextView)

        val midrtextViewTA: TextView = findViewById(R.id.midrtextViewTA)

        val osdiResultTextView: TextView = findViewById(R.id.osdi_result_text_view)

        val rabkinResultText = intent.getStringExtra("rabkinResultText")
        val rabkinScore = intent.getIntExtra("rabkinScore", 0)
        val rabkinResultTextView: TextView = findViewById(R.id.rabkin_result_text_view)

        rabkinResultTextView.text = rabkinResultText
        rabkinResultTextView.text = "$rabkinResultText (Score: $rabkinScore)"
        val rabkinResultFromIntent = intent.getParcelableExtra<RabkinResult>("rabkinResult")
        if (rabkinResultFromIntent != null) {
            rabkinResultTextView.text = "Результат Rabkin: ${rabkinResultFromIntent.resultText} (Баллы: ${rabkinResultFromIntent.score})"
        } else {
            // ... (обработка случая, когда rabkinResultFromIntent null)
        }

        val ishiharaResultText = intent.getStringExtra("ishiharaResultText")
        val ishiharaScore = intent.getIntExtra("ishiharaScore", 0)
        val ishiharaResultTextView: TextView = findViewById(R.id.ishihara_result_text_view)

        ishiharaResultTextView.text = ishiharaResultText
        ishiharaResultTextView.text = "$ishiharaResultText (Score: $ishiharaScore)"
        val ishiharaResultFromIntent = intent.getParcelableExtra<IshiharaResult>("ishiharaResult")
        if (ishiharaResultFromIntent != null) {
            ishiharaResultTextView.text = "Результат Ishihara: ${ishiharaResultFromIntent.resultText} (Баллы: ${ishiharaResultFromIntent.score})"
        } else {
            // ... (обработка случая, когда rabkinResultFromIntent null)
        }

        if (profile != null) {
            lifecycleScope.launch {
                val database = DatabaseInstance.getInstance(this@ThirdActivity)

                // Launch coroutines asynchronously
                val osdiResultsDeferred = async { database.patientProfileDao().getOsdiResultsForProfile(profile.id).first() }
                val rabkinResultsDeferred = async { database.patientProfileDao().getrabkinResultsForProfile(profile.id).first() }
                val ishiharaResultsDeferred = async { database.patientProfileDao().getishiharaResultsForProfile(profile.id).first() }

                // Await results
                val osdiResults = osdiResultsDeferred.await()
                val rabkinResults = rabkinResultsDeferred.await()
                val ishiharaResults = ishiharaResultsDeferred.await()

                // Update UI with results
                if (osdiResults.isNotEmpty()) {
                    val osdiResult = osdiResults[0]
                    osdiResultTextView.text = "Результат OSDI: ${osdiResult.resultText} (Баллы: ${osdiResult.score})"
                } else {
                    osdiResultTextView.text = "Результаты OSDI отсутствуют"
                }

                if (rabkinResults.isNotEmpty()) {
                    val rabkinResult = rabkinResults[0]
                    rabkinResultTextView.text = "Результат Rabkin: ${rabkinResult.resultText} (Баллы: ${rabkinResult.score})"
                } else {
                    rabkinResultTextView.text = "Результаты Rabkin отсутствуют"
                }
                if (ishiharaResults.isNotEmpty()) {
                    val ishiharaResult = ishiharaResults[0]
                    ishiharaResultTextView.text = "Результат Ishihara: ${ishiharaResult.resultText} (Баллы: ${ishiharaResult.score})"
                } else {
                    ishiharaResultTextView.text = "Результаты Ishihara отсутствуют"
                }
            }
        } else {
            // ... (обработка случая, когда профиль null)
        }


        if (profile != null) {
            // Display the data from the PatientProfile object
            lastNameTextViewTA.text = "Фамилия: ${profile.lastName}"
            firstNameTextViewTA.text = "Имя: ${profile.firstName}"
            middleNameTextViewTA.text = "Отчество: ${profile.middleName}"
            genderTextViewTA.text = "Пол: ${profile.gender}"

            dateTextViewTA.text = "Дата осмотра: ${profile.date}"

            visODtextViewTA.text = "Vis OD: ${profile.visOD}"
            visOStextViewTA.text = "Vis OS: ${profile.visOS}"
            visOUtextViewTA.text = "Vis OU: ${profile.visOU}"

            visODcorrtextViewTA.text = "Vis OD corr: ${profile.visODcorr}"
            visOScorrtextViewTA.text = "Vis OS corr: ${profile.visOScorr}"
            visOUcorrtextViewTA.text = "Vis OU corr: ${profile.visOUcorr}"

            sphODtextViewTA.text = "sph OD: ${profile.sphOD}"
            cylODtextViewTA.text = "cyl OD: ${profile.cylOD}"
            axODtextViewTA.text ="ax OD: ${profile.axOD}°"

            sphOStextViewTA.text = "sph OS: ${profile.sphOS}"
            cylOStextViewTA.text = "cyl OS: ${profile.cylOS}"
            axOStextViewTA.text = "ax OS: ${profile.axOS}°"

            sphODLabeltextViewTA.text = " ${profile.sphODLabel}"
            cylODLabeltextViewTA.text = " ${profile.cylODLabel}"
            axODLabeltextViewTA.text = " ${profile.axODLabel}"

            sphOSLabeltextViewTA.text = " ${profile.sphOSLabel}"
            cylOSLabeltextViewTA.text = " ${profile.cylOSLabel}"
            axOSLabeltextViewTA.text = " ${profile.axOSLabel}"

            midrtextViewTA.text = "Мидриатик: ${profile.midriaticAgent}"

            agetransformTextView.text = SecondActivity.ageTransform(profile.age, profile.gender == "Мужской")

            vistransformtextViewOD.text = SecondActivity.visTransformOD(profile.visOD, profile.age)
            vistransformtextViewOS.text = SecondActivity.visTransformOS(profile.visOS, profile.age)
            vistransformtextViewOU.text = SecondActivity.visTransformOU(profile.visOU, profile.age)

            vistransformtextViewODcorr.text = SecondActivity.visTransformOD(profile.visODcorr, profile.age)
            vistransformtextViewOScorr.text = SecondActivity.visTransformOS(profile.visOScorr, profile.age)
            vistransformtextViewOUcorr.text = SecondActivity.visTransformOU(profile.visOUcorr, profile.age)

            textViewsphOD.text = SecondActivity.sphCalculateOD(profile.sphOD, profile.age)
            textViewcylOD.text = SecondActivity.cylCalculateOD(profile.cylOD)
          //textViewaxOD.text = SecondActivity.axCalculateOD(profile.axOD, profile.age)

            textViewsphOS.text = SecondActivity.sphCalculateOS(profile.sphOS, profile.age)
            textViewcylOS.text = SecondActivity.cylCalculateOS(profile.cylOS)
          //textViewaxOS.text = SecondActivity.axCalculateOS(profile.axOS, profile.age)

            comparesphtextView.text = profile.comparesphResult

            val formattedAge = when {
                profile.age.rem(1.0) == 0.0 -> profile.age.toInt().toString() + getCorrectWordForm(profile.age)
                else -> "${profile.age} ${getCorrectWordForm(profile.age)}"
            }
            ageTextViewTA.text = "Возраст: $formattedAge"
            // Handle the case where the profile is null
            Log.e("ThirdActivity", "PatientProfile is null!")
            // You can also show an error message to the user here

            lifecycleScope.launch {
                val database = DatabaseInstance.getInstance(this@ThirdActivity)
                val osdiResults = database.patientProfileDao().getOsdiResultsForProfile(profile.id).first()
                if (osdiResults.isNotEmpty()) {
                    val osdiResult = osdiResults[0] // Берем первый результат (предполагаем, что он один)
                    osdiResultTextView.text = "Результат OSDI: ${osdiResult.resultText} (Баллы: ${osdiResult.score})"
                } else {
                    osdiResultTextView.text = "Результаты OSDI отсутствуют"
                }
            }

            lifecycleScope.launch {
                val database = DatabaseInstance.getInstance(this@ThirdActivity)
                val rabkinResults = database.patientProfileDao().getrabkinResultsForProfile(profile.id).first()
                if (rabkinResults.isNotEmpty()) {
                    val rabkinResult = rabkinResults[0] // Берем первый результат (предполагаем, что он один)
                    rabkinResultTextView.text = "Результат rabkin: ${rabkinResult.resultText} (Баллы: ${rabkinResult.score})"
                } else {
                    rabkinResultTextView.text = "Результаты rabkin отсутствуют"
                }
            }
            lifecycleScope.launch {
                val database = DatabaseInstance.getInstance(this@ThirdActivity)
                val ishiharaResults = database.patientProfileDao().getishiharaResultsForProfile(profile.id).first()
                if (ishiharaResults.isNotEmpty()) {
                    val ishiharaResult = ishiharaResults[0] // Берем первый результат (предполагаем, что он один)
                    ishiharaResultTextView.text = "Результат ishihara: ${ishiharaResult.resultText} (Баллы: ${ishiharaResult.score})"
                } else {
                    ishiharaResultTextView.text = "Результаты ishihara отсутствуют"
                }
            }
        }
    }

    private fun getCorrectWordForm(age: Double): String {
        val ageInt = age.toInt()
        val decimalPart = age.rem(1.0)
        return when {
            decimalPart != 0.0 -> " года"
            ageInt % 10 == 1 && ageInt != 11 -> " год"
            ageInt % 10 in 2..4 && ageInt % 100 !in 11..14 -> " года"
            else -> " лет"
        }
    }
}

