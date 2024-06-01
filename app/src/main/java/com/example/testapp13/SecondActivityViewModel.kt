package com.example.testapp13

import androidx.lifecycle.ViewModel

class SecondActivityViewModel : ViewModel() {
    var birthDate: String = ""
        private set
    var lastName: String = ""
        private set
    var firstName: String = ""
        private set
    var middleName: String = ""
        private set
    var gender: String = "Мужской" // Значение по умолчанию
        private set
    var age: Double = 0.0
        private set
    var examinationdate: String = ""
        private set
    var visOD: Double = 0.0
        private set
    var visOS: Double = 0.0
        private set
    var visOU: Double = 0.0
        private set
    var visODcorr: Double = 0.0
        private set
    var visOScorr: Double = 0.0
        private set
    var visOUcorr: Double = 0.0
        private set
    var sphOD: Double = 0.0
        private set
    var cylOD: Double = 0.0
        private set
    var axOD: Double = 0.0
        private set
    var sphOS: Double = 0.0
        private set
    var cylOS: Double = 0.0
        private set
    var axOS: Double = 0.0
        private set
    var sphODLabel: String = ""
        private set
    var cylODLabel: String = ""
        private set
    var axODLabel: String = ""
        private set
    var sphOSLabel: String = ""
        private set
    var cylOSLabel: String = ""
        private set
    var axOSLabel: String = ""
        private set
    var comparesphResult: String = ""
        private set
    var midriaticAgent: String = ""
        private set
    var osdiResult: OsdiResult? = null
    var rabkinResult: RabkinResult? = null
    var ishiharaResult: IshiharaResult? = null

    fun updateBirthDate(newDate: String) {
        birthDate = newDate
    }

    fun updateLastName(newLastName: String) {
        lastName = newLastName
    }

    fun updateFirstName(newFirstName: String) {
        firstName = newFirstName
    }

    fun updateMiddleName(newMiddleName: String) {
        middleName = newMiddleName
    }

    fun updateGender(newGender: String) {
        gender = newGender
    }

    fun updateAge(newAge: Double) {
        age = newAge
    }

    fun updateExaminationDate(newDate: String) {
        examinationdate = newDate
    }

    fun updateVisOD(newVisOD: Double) {
        visOD = newVisOD
    }

    fun updateVisOS(newVisOS: Double) {
        visOS = newVisOS
    }

    fun updateVisOU(newVisOU: Double) {
        visOU = newVisOU
    }

    fun updateVisODcorr(newVisODcorr: Double) {
        visODcorr = newVisODcorr
    }

    fun updateVisOScorr(newVisOScorr: Double) {
        visOScorr = newVisOScorr
    }

    fun updateVisOUcorr(newVisOUcorr: Double) {
        visOUcorr = newVisOUcorr
    }

    fun updateSphOD(newSphOD: Double) {
        sphOD = newSphOD
    }

    fun updateCylOD(newCylOD: Double) {
        cylOD = newCylOD
    }

    fun updateAxOD(newAxOD: Double) {
        axOD = newAxOD
    }

    fun updateSphOS(newSphOS: Double) {
        sphOS = newSphOS
    }

    fun updateCylOS(newCylOS: Double) {
        cylOS = newCylOS
    }

    fun updateAxOS(newAxOS: Double) {
        axOS = newAxOS
    }

    fun updateSphODLabel(newSphODLabel: String) {
        sphODLabel = newSphODLabel
    }

    fun updateCylODLabel(newCylODLabel: String) {
        cylODLabel = newCylODLabel
    }

    fun updateAxODLabel(newAxODLabel: String) {
        axODLabel = newAxODLabel
    }

    fun updateSphOSLabel(newSphOSLabel: String) {
        sphOSLabel = newSphOSLabel
    }

    fun updateCylOSLabel(newCylOSLabel: String) {
        cylOSLabel = newCylOSLabel
    }

    fun updateAxOSLabel(newAxOSLabel: String) {
        axOSLabel = newAxOSLabel
    }

    fun updateComparesphResult(newComparesphResult: String) {
        comparesphResult = newComparesphResult
    }

    fun updateMidriaticAgent(newMidriaticAgent: String) {
        midriaticAgent = newMidriaticAgent
    }
}