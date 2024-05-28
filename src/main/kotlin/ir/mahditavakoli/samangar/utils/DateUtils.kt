package ir.mahditavakoli.samangar.utils

import com.github.eloyzone.jalalicalendar.DateConverter
import com.github.eloyzone.jalalicalendar.JalaliDateFormatter
import java.util.*

/*
        // Create an object of DateConverter, its the main class that converts calendars
        val dateConverter = DateConverter();

        // Convert Jalali date to Gregorian
        val localdate1 = dateConverter.jalaliToGregorian(1370, 11, 28);
        val localdate2 = dateConverter.jalaliToGregorian(1386, MonthPersian.ESFAND, 29);

        // Convert Gregorian date to Jalali
        val jalaliDate1 = dateConverter.gregorianToJalali(1992, 2, 17);
        val jalaliDate2 = dateConverter.gregorianToJalali(2019, 3, 20);

        // checking for leapyer of Jalali Date
        val leapyer1 = JalaliDate(1370, 11, 28).isLeapYear()
        val leapyer2 = dateConverter.gregorianToJalali(1992, 2, 17).isLeapYear();

        // Day of week
        val dayOfWeek1 = JalaliDate(1370, 11, 28).getDayOfWeek().getStringInPersian(); // Doshanbe
        val dayOfWeek2 = JalaliDate(1370, 11, 28).getDayOfWeek().getStringInEnglish(); // دوشنبه*/


fun getPersianCurrentDateYMD(): String {
    val calendar: Calendar = Calendar.getInstance()

    val year: Int = calendar.get(Calendar.YEAR)
    val month: Int = calendar.get(Calendar.MONTH) + 1 // January is 0, so we add 1
    val day: Int = calendar.get(Calendar.DAY_OF_MONTH)

    val dateConverter = DateConverter();


    return dateConverter.gregorianToJalali(year, month, day)
        .format(JalaliDateFormatter("yyyy/mm/dd", JalaliDateFormatter.FORMAT_IN_ENGLISH)).replace("/","-")
}


