package com.example.testapp13

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class FifthActivity : AppCompatActivity() {

    private lateinit var patientsListTextView: TextView
    private lateinit var profilesRecyclerView: RecyclerView
    private lateinit var searchEditText: EditText

    private var profilesList: MutableList<PatientProfile> = mutableListOf()
    private var currentGreenButtonId: Int = -1
    private var isNightMode = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isNightMode = intent.getBooleanExtra("isNightMode", true)
        setTheme(if (isNightMode) R.style.AppTheme_Night else R.style.AppTheme_Day)
        val backgroundColor = if (isNightMode) ContextCompat.getColor(this, R.color.black) else ContextCompat.getColor(this, R.color.white)

        setContentView(R.layout.activity_fifth)
        supportActionBar?.hide()
        window.decorView.setBackgroundColor(backgroundColor)

        searchEditText = findViewById(R.id.search_edit_text)
        patientsListTextView = findViewById(R.id.patients_list_text_View)
        profilesRecyclerView = findViewById(R.id.profiles_recycler_view)
        profilesRecyclerView.layoutManager = LinearLayoutManager(this)

        CoroutineScope(Dispatchers.Main).launch {
            loadProfiles().collect { profiles ->
                profilesList = profiles.toMutableList()
                profilesRecyclerView.adapter = PatientProfileAdapter(profilesList, this@FifthActivity, isNightMode)
                updateProfilesList(SortOption.DATE_DESC)
            }
        }

        setupSortButtons()
        setupSearchEditText()

        intent.getParcelableExtra<PatientProfile>("profile")?.let {
            profilesList.add(it)
            (profilesRecyclerView.adapter as? PatientProfileAdapter)?.updateProfiles(profilesList)
        }
    }

    private suspend fun loadProfiles(): Flow<List<PatientProfile>> {
        val database = DatabaseInstance.getInstance(this)
        val profilesFlow = database.patientProfileDao().getAllProfiles()
        Log.d("FifthActivity", "Number of profiles retrieved: ${profilesFlow.first().size}")
        return profilesFlow
    }

    private fun setupSortButtons() {
        val sortButtons = mapOf(
            R.id.sort_button_date_desc to SortOption.DATE_DESC,
            R.id.sort_button_date_asc to SortOption.DATE_ASC,
            R.id.sort_button_last_name to SortOption.LAST_NAME,
            R.id.sort_button_age_desc to SortOption.AGE_DESC,
            R.id.sort_button_age_asc to SortOption.AGE_ASC
        )

        sortButtons.keys.forEach { buttonId ->
            val button = findViewById<FrameLayout>(buttonId)
            button.setOnClickListener {
                if (currentGreenButtonId != -1) {
                    findViewById<FrameLayout>(currentGreenButtonId).setBackgroundResource(R.drawable.button4_red)
                }
                currentGreenButtonId = buttonId
                button.setBackgroundResource(R.drawable.button4_green)
                updateProfilesList(sortButtons[buttonId]!!)
            }
        }
    }

    private fun setupSearchEditText() {
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                filterProfiles(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun updateProfilesList(sortOption: SortOption) {
        when (sortOption) {
            SortOption.DATE_DESC -> profilesList.sortByDescending { it.examinationdate }
            SortOption.DATE_ASC -> profilesList.sortBy { it.examinationdate }
            SortOption.LAST_NAME -> profilesList.sortBy { it.lastName }
            SortOption.AGE_DESC -> profilesList.sortByDescending { it.age }
            SortOption.AGE_ASC -> profilesList.sortBy { it.age }
        }
        Log.d("FifthActivity", "Number of profiles in list: ${profilesList.size}")
        (profilesRecyclerView.adapter as? PatientProfileAdapter)?.updateProfiles(profilesList)
    }

    private fun filterProfiles(query: String) {
        val filteredProfiles = profilesList.filter { profile ->
            profile.lastName.contains(query, ignoreCase = true) ||
                    profile.firstName.contains(query, ignoreCase = true) ||
                    profile.middleName.contains(query, ignoreCase = true) ||
                    profile.age.toString().contains(query)
        }
        (profilesRecyclerView.adapter as? PatientProfileAdapter)?.updateProfiles(filteredProfiles)
    }

    private enum class SortOption {
        DATE_DESC, DATE_ASC, LAST_NAME, AGE_DESC, AGE_ASC
    }
}
