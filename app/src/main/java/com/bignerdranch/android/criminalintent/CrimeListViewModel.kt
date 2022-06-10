package com.bignerdranch.android.criminalintent

import androidx.lifecycle.ViewModel
import com.bignerdranch.android.criminalintent.database.CrimeRepository

class CrimeListViewModel : ViewModel() {

    private val crimeRepository = CrimeRepository.get()

    val crimesLiveData = crimeRepository.getCrimes()
}