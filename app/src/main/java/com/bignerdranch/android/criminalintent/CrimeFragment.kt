package com.bignerdranch.android.criminalintent

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import java.util.*

private const val ARG_CRIME_ID = "crime-id"
private const val DIALOG_DATE = "DialogDate"
private const val REQUEST_KEY = "DATE_RESULT_KEY"

class CrimeFragment : Fragment() {

    private lateinit var crime: Crime
    private lateinit var titleField: EditText
    private lateinit var dateButton: Button
    private lateinit var solvedCheckBox: CheckBox

    private val crimeDetailsViewModel: CrimeDetailsViewModel by lazy {
        ViewModelProvider(this).get(CrimeDetailsViewModel::class.java)
    }

    companion object {
        fun newInstance(crimeId: UUID): CrimeFragment {
            val args = Bundle().apply {
                putSerializable(ARG_CRIME_ID, crimeId)
            }
            return CrimeFragment().apply { arguments = args }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        crime = Crime()

        val crimeId = arguments?.getSerializable(ARG_CRIME_ID) as UUID
        crimeDetailsViewModel.loadCrime(crimeId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater
            .inflate(R.layout.fragment_crime, container, false)
            .also { view ->
                titleField = view.findViewById(R.id.crime_title) as EditText
                dateButton = view.findViewById(R.id.crime_date) as Button
                solvedCheckBox = view.findViewById(R.id.crime_solved) as CheckBox
                solvedCheckBox.setOnCheckedChangeListener { _, isChecked ->
                    crime.isSolved = isChecked
                }
            }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        crimeDetailsViewModel.crimeLiveData
            .observe(viewLifecycleOwner) { crime ->
                crime?.let {
                    this.crime = it
                    updateUI()
                }
            }
    }

    override fun onStart() {
        super.onStart()

        val titleWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //...
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                crime.title = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {
                //...
            }
        }

        titleField.addTextChangedListener(titleWatcher)

        dateButton.setOnClickListener {
            parentFragmentManager.also {
                it.setFragmentResultListener(REQUEST_KEY, viewLifecycleOwner) { _, bundle ->
                    crime.date =
                        bundle.getSerializable(DatePickerFragment.SELECTED_DATE_KEY) as Date
                    updateUI()
                }

                DatePickerFragment.newInstance(crime.date, REQUEST_KEY)
                    .show(it, DIALOG_DATE)
            }
        }
    }

    override fun onStop() {
        super.onStop()
        crimeDetailsViewModel.saveCrime(crime)
    }

    private fun updateUI() {
        titleField.setText(crime.title)
        dateButton.text = crime.date.toString()
        solvedCheckBox.apply {
            isChecked = crime.isSolved
            jumpDrawablesToCurrentState()
        }
    }
}