package com.example.testapp13

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import android.util.Log

@Suppress("DEPRECATION")
class FifthActivity : AppCompatActivity() {

    private lateinit var patientslisttextView: TextView
    private lateinit var profilesRecyclerView: RecyclerView
    private lateinit var sortSpinner: Spinner
    private lateinit var searchEditText: EditText

    private var profilesList: MutableList<PatientProfile> = mutableListOf() // List of patient profiles

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fifth)

        val newProfile = intent.getParcelableExtra<PatientProfile>("profile")
        if (newProfile != null) {
            // Add the new profile to your profilesList
            profilesList.add(newProfile)
            // Update the adapter
            val adapter = profilesRecyclerView.adapter as? PatientProfileAdapter
            adapter?.updateProfiles(profilesList)
        }

        supportActionBar?.hide()

        searchEditText = findViewById(R.id.search_edit_text)
        patientslisttextView = findViewById(R.id.patients_list_text_View)
        profilesRecyclerView = findViewById(R.id.profiles_recycler_view) // Assuming the ID in your layout
        profilesRecyclerView.layoutManager = LinearLayoutManager(this) // Set a layout manager
        sortSpinner = findViewById(R.id.sort_spinner)

        // Load profiles from storage (replace with your actual loading logic)
        CoroutineScope(Dispatchers.Main).launch {
            loadProfiles().collect { profiles ->
                profilesList = profiles.toMutableList()

                // Create and set the adapter only after profiles are loaded
                val adapter = PatientProfileAdapter(profilesList, this@FifthActivity)
                profilesRecyclerView.adapter = adapter

                updateProfilesList(SortOption.DATE_DESC) // Initial sort
            }
        }

        // Set up initial list adapter
        updateProfilesList(SortOption.DATE_DESC)

        // Set up sort spinner
        val sortOptions = arrayOf(
            "Отобразить с самого давнего осмотра",
            "Отобразить с самого недавнего осмотра",
            "Отобразить по фамилии в алфавитном порядке",
            "Отобразить с наибольшего возраста",
            "Отобразить с наименьшего возраста"
        )
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, sortOptions)
        sortSpinner.adapter = adapter

        sortSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedOption = SortOption.entries[position]
                updateProfilesList(selectedOption)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                filterProfiles(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private suspend fun loadProfiles(): Flow<List<PatientProfile>> {
        val database = DatabaseInstance.getInstance(this)
        val profilesFlow = database.patientProfileDao().getAllProfiles()

        // Log the number of profiles retrieved once
        val profilesList = profilesFlow.first() // Get the first emitted list
        Log.d("FifthActivity", "Number of profiles retrieved: ${profilesList.size}")

        return profilesFlow
    }

    private fun updateProfilesList(sortOption: SortOption) {
        // Sort profiles based on selected option
        when (sortOption) {
            SortOption.DATE_DESC -> profilesList.sortByDescending { it.examinationdate }
            SortOption.DATE_ASC -> profilesList.sortBy { it.examinationdate }
            SortOption.LAST_NAME -> profilesList.sortBy { it.lastName }
            SortOption.AGE_DESC -> profilesList.sortByDescending { it.age }
            SortOption.AGE_ASC -> profilesList.sortBy { it.age }
        }
        Log.d("FifthActivity", "Number of profiles in list: ${profilesList.size}")
        // Update the adapter with the entire profilesList
        val adapter = profilesRecyclerView.adapter as? PatientProfileAdapter
        adapter?.updateProfiles(profilesList)
    }

    // Enum for sorting options
    private enum class SortOption {
        DATE_DESC, DATE_ASC, LAST_NAME, AGE_DESC, AGE_ASC
    }
    private fun filterProfiles(query: String) {
            val filteredProfiles = profilesList.filter { profile ->
                profile.lastName.contains(query, ignoreCase = true) ||
                        profile.firstName.contains(query, ignoreCase = true) ||
                        profile.middleName.contains(query, ignoreCase = true) ||
                        profile.age.toString().contains(query)
            }
            // Update the adapter with the filtered list
            val adapter = profilesRecyclerView.adapter as? PatientProfileAdapter
            adapter?.updateProfiles(filteredProfiles)
            // Handle the case where searchEditText is not initialized
            // You might log an error or show a message to the user.
    }
}

