package org.fossify.commons.helpers

import saman.zamani.persiandate.PersianDate
import saman.zamani.persiandate.PersianDateFormat
import java.util.Calendar

object JalaliCalendarHelper {
    fun formatJalali(timestamp: Long, format: String): String {
        val pDate = PersianDate(timestamp)
        val jFormat = format
            .replace("yyyy", "Y")
            .replace("yy", "y")
            .replace("y", "Y")
            .replace("MMMM", "F")
            .replace("MMM", "F")
            .replace("MM", "m")
            .replace("M", "n")
            .replace("dd", "d")
            .replace("d", "j")
            .replace("HH", "H")
            .replace("hh", "g")
            .replace("mm", "i")
            .replace("ss", "s")
        
        val pdf = PersianDateFormat(jFormat)
        pdf.setNumberCharacter(PersianDateFormat.PersianDateNumberCharacter.FARSI)
        return pdf.format(pDate)
    }

    fun toPersianDigits(input: String): String {
        var result = input
        val persianDigits = arrayOf("۰", "۱", "۲", "۳", "۴", "۵", "۶", "۷", "۸", "۹")
        for (i in 0..9) {
            result = result.replace(i.toString(), persianDigits[i])
        }
        result = result.replace("AM", "ق.ظ")
            .replace("PM", "ب.ظ")
            .replace("am", "ق.ظ")
            .replace("pm", "ب.ظ")
        return result
    }

    fun isThisSolarYear(timestamp: Long): Boolean {
        val pDateThen = PersianDate(timestamp)
        val pDateNow = PersianDate()
        return pDateThen.shYear == pDateNow.shYear
    }
}
