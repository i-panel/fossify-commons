package org.fossify.commons.extensions

import android.content.Context
import android.text.format.DateFormat
import android.text.format.DateUtils
import android.text.format.Time
import java.text.DecimalFormat
import java.util.Calendar
import java.util.Locale
import org.fossify.commons.helpers.JalaliCalendarHelper
import kotlin.math.log10
import kotlin.math.pow

fun Long.formatSize(): String {
    if (this <= 0) return "0 B"

    val units = arrayOf("B", "kB", "MB", "GB", "TB", "PB", "EB")
    val digitGroups = (log10(toDouble()) / log10(1000.0)).toInt()
    return "${DecimalFormat("#,##0.#").format(this / 1000.0.pow(digitGroups.toDouble()))} ${units[digitGroups]}"
}

fun Long.formatDate(context: Context, dateFormat: String? = null, timeFormat: String? = null): String {
    val useDateFormat = dateFormat ?: context.baseConfig.dateFormat
    val useTimeFormat = timeFormat ?: context.getTimeFormat()
    if (context.baseConfig.useSolarHijri) {
        val cal = Calendar.getInstance(Locale.ENGLISH)
        cal.timeInMillis = this
        val time = DateFormat.format(useTimeFormat, cal).toString()
        val persianTime = JalaliCalendarHelper.toPersianDigits(time)
        return "${JalaliCalendarHelper.formatJalali(this, useDateFormat)}, $persianTime"
    }
    val cal = Calendar.getInstance(Locale.ENGLISH)
    cal.timeInMillis = this
    return DateFormat.format("$useDateFormat, $useTimeFormat", cal).toString()
}

fun Long.formatTime(context: Context): String {
    val cal = Calendar.getInstance(Locale.ENGLISH)
    cal.timeInMillis = this
    val time = DateFormat.format(context.getTimeFormat(), cal).toString()
    return if (context.baseConfig.useSolarHijri) JalaliCalendarHelper.toPersianDigits(time) else time
}

fun Long.formatDateOrTime(
    context: Context,
    hideTimeOnOtherDays: Boolean,
    showCurrentYear: Boolean,
    hideTodaysDate: Boolean = true,
): String {
    val cal = Calendar.getInstance(Locale.ENGLISH)
    cal.timeInMillis = this

    return if (hideTodaysDate && DateUtils.isToday(this)) {
        this.formatTime(context)
    } else {
        var format = context.baseConfig.dateFormat
        val isSolar = context.baseConfig.useSolarHijri
        val isThisYear = if (isSolar) JalaliCalendarHelper.isThisSolarYear(this) else isThisYear()
        if (!showCurrentYear && isThisYear) {
            format = format.replace("y", "").trim().trim('-').trim('.').trim('/')
        }

        if (isSolar) {
            var formatted = JalaliCalendarHelper.formatJalali(this, format)
            if (!hideTimeOnOtherDays) {
                formatted += ", ${this.formatTime(context)}"
            }
            formatted
        } else {
            if (!hideTimeOnOtherDays) {
                format += ", ${context.getTimeFormat()}"
            }

            DateFormat.format(format, cal).toString()
        }
    }
}

fun Long.isThisYear(): Boolean {
    val time = Time()
    time.set(this)

    val thenYear = time.year
    time.set(System.currentTimeMillis())

    return (thenYear == time.year)
}

fun Long.toDayCode(format: String = "ddMMyy"): String {
    val cal = Calendar.getInstance(Locale.ENGLISH)
    cal.timeInMillis = this
    return DateFormat.format(format, cal).toString()
}
