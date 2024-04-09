package com.example.testapp13

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PatientProfileAdapter(
    private var profiles: List<PatientProfile>,
    private val context: Context
) : RecyclerView.Adapter<PatientProfileAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.profile_item_text_view)
        val deleteButton: Button = view.findViewById(R.id.delete_button)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.profile_item, viewGroup, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        // Проверка на пустой список перед доступом к элементам
        if (profiles.isEmpty()) {
            viewHolder.textView.text = "Нет сохраненных профилей"
            viewHolder.deleteButton.visibility = View.GONE
        } else {
            val currentProfile = profiles[position]
            val nameText = "${currentProfile.lastName} ${currentProfile.firstName} ${currentProfile.middleName}"
            val genderText = if (currentProfile.gender == "Мужской") "муж." else "жен."
            val ageDateText = formatAgeAndDate(currentProfile.age, currentProfile.date)
            viewHolder.textView.text = "$nameText\n($genderText) $ageDateText"
            viewHolder.deleteButton.visibility = View.VISIBLE

            // Обработчики нажатий
            viewHolder.deleteButton.setOnClickListener {
                val profileToDelete = profiles[position]
                CoroutineScope(Dispatchers.Main).launch {
                    deleteProfile(profileToDelete)
                }
            }

            viewHolder.itemView.setOnClickListener {
                val profile = profiles[position]
                val intent = Intent(context, ThirdActivity::class.java)
                intent.putExtra("profile", profile)



                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount() = profiles.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateProfiles(newProfiles: List<PatientProfile>) {
        profiles = newProfiles
        notifyDataSetChanged()
    }

    private suspend fun deleteProfile(profile: PatientProfile) {
        profiles = profiles.filter { it.id != profile.id }
        notifyDataSetChanged()
        withContext(Dispatchers.Main) {
            notifyDataSetChanged()
        }
        val database = DatabaseInstance.getInstance(context)
        database.patientProfileDao().delete(profile)
    }

    private fun formatAgeAndDate(age: Double, date: String): String {
        val ageString = when {
            age.rem(1.0) == 0.0 -> age.toInt().toString() + getCorrectWordForm(age)
            else -> "$age ${getCorrectWordForm(age)}"
        }
        return "$ageString\nДата осмотра: $date"
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