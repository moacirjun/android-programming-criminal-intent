package com.bignerdranch.android.criminalintent

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import java.util.*

private const val ARG_DATE = "argument_date"
private const val ARG_REQUEST_KEY = "argument_result_key"

class DatePickerFragment : DialogFragment() {

    interface Callbacks {
        fun onDateSelected(date: Date)
    }

    companion object {
        const val SELECTED_DATE_KEY = "SELECTED_DATE"

        fun newInstance(date: Date, requestKey: String? = null): DatePickerFragment =
            DatePickerFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_DATE, date)
                    putString(ARG_REQUEST_KEY, requestKey)
                }
            }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val listener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            arguments?.getString(ARG_REQUEST_KEY)?.let { requestKey ->
                val resultDate = GregorianCalendar(year, month, dayOfMonth).time
                val result = Bundle().apply {
                    putSerializable(SELECTED_DATE_KEY, resultDate)
                }

                parentFragmentManager.setFragmentResult(requestKey, result)
            }
        }

        val date = arguments?.getSerializable(ARG_DATE) as Date
        val calendar = Calendar.getInstance().apply { time = date }
        val initialYear = calendar.get(Calendar.YEAR)
        val initialMonth = calendar.get(Calendar.MONTH)
        val initialDay = calendar.get(Calendar.DAY_OF_MONTH)

        return DatePickerDialog(
            requireContext(),
            listener,
            initialYear,
            initialMonth,
            initialDay
        )
    }
}