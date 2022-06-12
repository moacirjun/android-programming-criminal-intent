package com.bignerdranch.android.criminalintent

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import java.util.*

private const val ARG_DATE = "argument_date"
private const val ARG_REQUEST_KEY = "argument_result_key"

class DatePickerFragment : DialogFragment() {

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
                val gregorianCalendar = GregorianCalendar.getInstance().apply {
                    set(GregorianCalendar.YEAR, year)
                    set(GregorianCalendar.MONTH, month)
                    set(GregorianCalendar.DAY_OF_MONTH, dayOfMonth)
                }
                val result = Bundle().apply {
                    putSerializable(SELECTED_DATE_KEY, gregorianCalendar.time)
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