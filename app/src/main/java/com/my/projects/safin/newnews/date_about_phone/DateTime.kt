package com.my.projects.safin.newnews.date_about_phone

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DateTime() {
    @RequiresApi(Build.VERSION_CODES.O)
    fun getCurrentDate(): String {
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.BASIC_ISO_DATE
        val edited =  current.format(formatter)
        return edited.toString()
    }
}