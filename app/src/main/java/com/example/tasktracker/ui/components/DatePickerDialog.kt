package com.example.tasktracker.ui.components

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import java.util.*

fun showDateTimePicker(
    context: Context,
    onDateTimeSelected: (Long) -> Unit
) {
    val calendar = Calendar.getInstance()
    
    DatePickerDialog(
        context,
        { _, year, month, day ->
            TimePickerDialog(
                context,
                { _, hour, minute ->
                    calendar.set(year, month, day, hour, minute)
                    onDateTimeSelected(calendar.timeInMillis)
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
            ).show()
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    ).show()
} 