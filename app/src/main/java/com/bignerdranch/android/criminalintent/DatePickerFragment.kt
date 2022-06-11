package com.bignerdranch.android.criminalintent

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import java.util.*

private const val ARG_DATE = "argument_date"

class DatePickerFragment : DialogFragment() {

    interface Callbacks {
        fun onDateSelected(date: Date)
    }

    companion object {
        fun newInstance(date: Date): DatePickerFragment =
            DatePickerFragment().apply {
                arguments = Bundle().apply { putSerializable(ARG_DATE, date) }
            }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val listener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            val resultDate = GregorianCalendar(year, month, dayOfMonth).time

            targetFragment?.let {
                (it as Callbacks).onDateSelected(resultDate)
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