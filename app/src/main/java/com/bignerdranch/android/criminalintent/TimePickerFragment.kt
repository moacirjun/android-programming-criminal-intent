package com.bignerdranch.android.criminalintent

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import java.util.*

private const val ARG_TIME = "argument_date"
private const val ARG_REQUEST_KEY = "argument_result_key"

class TimePickerFragment : DialogFragment() {
    companion object {
        const val RESULT_DATE_KEY = "SELECTED_DATE"

        fun newInstance(date: Date, requestKey: String? = null): TimePickerFragment =
            TimePickerFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_TIME, date)
                    putString(ARG_REQUEST_KEY, requestKey)
                }
            }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val listener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            arguments?.getString(ARG_REQUEST_KEY)?.let { requestKey ->
                val date = arguments?.getSerializable(ARG_TIME) as Date
                val resultCalendar = GregorianCalendar.getInstance().apply {
                    time = date
                    set(GregorianCalendar.HOUR_OF_DAY, hourOfDay)
                    set(GregorianCalendar.MINUTE, minute)
                }
                val result = Bundle().apply {
                    putSerializable(RESULT_DATE_KEY, resultCalendar.time)
                }

                parentFragmentManager.setFragmentResult(requestKey, result)
            }
        }

        val date = arguments?.getSerializable(ARG_TIME) as Date
        val calendar = Calendar.getInstance().apply { time = date }
        val initialHour = calendar.get(Calendar.HOUR_OF_DAY)
        val initialMinute = calendar.get(Calendar.MINUTE)

        return TimePickerDialog(
            requireContext(),
            listener,
            initialHour,
            initialMinute,
            true,
        )
    }
}