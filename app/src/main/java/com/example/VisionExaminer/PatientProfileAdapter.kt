package com.example.VisionExaminer

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PatientProfileAdapter(
    private var profiles: List<PatientProfile>,
    private val context: Context,
    var isNightMode: Boolean // Тема адаптера
) : RecyclerView.Adapter<PatientProfileAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.profile_item_text_view)
        val deleteButton: ImageButton = view.findViewById(R.id.delete_button)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.profile_item, viewGroup, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        if (profiles.isEmpty()) {
            viewHolder.textView.text = "Нет сохраненных профилей"
            viewHolder.deleteButton.visibility = View.GONE
        } else {
            val currentProfile = profiles[position]
            val nameText = "${currentProfile.lastName} ${currentProfile.firstName} ${currentProfile.middleName}"
            val genderText = if (currentProfile.gender == "Мужской") "муж." else "жен."
            val ageDateText = formatAgeAndDate(currentProfile.age, currentProfile.examinationdate)

            viewHolder.textView.text = "$nameText\n($genderText) $ageDateText\nДата рождения: ${currentProfile.birthDate}"
            viewHolder.deleteButton.visibility = View.VISIBLE

            // Логика двойного нажатия
            var clickCount = 0
            viewHolder.deleteButton.setOnClickListener {
                clickCount++
                if (clickCount == 1) {
                    // Первое нажатие: меняем фон
                    viewHolder.deleteButton.setBackgroundResource(R.drawable.purplebutton) // !!!
                } else if (clickCount == 2) {
                    clickCount = 0
                    // Второе нажатие: удаляем профиль и меняем фон обратно
                    val profileToDelete = profiles[position]
                    CoroutineScope(Dispatchers.Main).launch {
                        deleteProfile(profileToDelete)
                    }
                    viewHolder.deleteButton.setBackgroundResource(R.drawable.bluebutton) // !!!
                }
            }

            viewHolder.itemView.setOnClickListener {
                val profile = profiles[position]
                val intent = Intent(context, ThirdActivity::class.java)
                intent.putExtra("profile", profile)
                intent.putExtra("isNightMode", isNightMode) // Передайте isNightMode
                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount() = profiles.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateProfiles(newProfiles: List<PatientProfile>) {
        profiles = newProfiles
        //  notifyDataSetChanged()
        for (i in 0 until profiles.size) { // Проходим по всем элементам
            notifyItemChanged(i) // Обновляем конкретный элемент
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private suspend fun deleteProfile(profile: PatientProfile) {
        profiles = profiles.filter { it.id != profile.id }
        withContext(Dispatchers.Main) {
            notifyDataSetChanged()
        }
        val database = DatabaseInstance.getInstance(context)
        database.patientProfileDao().delete(profile)
    }

    private fun formatAgeAndDate(age: Double, examinationdate: String): String {
        val ageString = when {
            age.rem(1.0) == 0.0 -> age.toInt().toString() + getCorrectWordForm(age)
            else -> "$age ${getCorrectWordForm(age)}"
        }
        return "$ageString\nДата осмотра: $examinationdate"
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