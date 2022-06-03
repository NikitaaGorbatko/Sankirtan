package nikitagorbatko.example.sankirtan

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.res.stringArrayResource
import java.time.YearMonth
import java.util.*

object CalendarProvider {
    private var calendar = GregorianCalendar.getInstance()//createCalendar()
    var monthNum = calendar.get(GregorianCalendar.MONTH)
    val year = calendar.get(GregorianCalendar.YEAR)
    val day = calendar.get(GregorianCalendar.DAY_OF_MONTH)
    @RequiresApi(Build.VERSION_CODES.O)
    val days = YearMonth.of(year, monthNum + 1).lengthOfMonth()
    private val testCalendar = GregorianCalendar(year, monthNum, 1)
    private val dayOfWeek = testCalendar.get(GregorianCalendar.DAY_OF_WEEK)
    val firstDayOfFirstWeek = if (dayOfWeek == 1) 7 else dayOfWeek - 1

    fun setMonth(month: Int) {
        if (month in 0 .. 13) {
            calendar = GregorianCalendar.getInstance().also { it.set(year, monthNum, 1) }
            monthNum = month
        }
    }
}


//class CalendarProvider private constructor() {
//    private var INSTANCE: CalendarProvider? = null
//
//    private constructor()
//
//    fun getInstance(month: Int = -1): CalendarProvider {
//        if (INSTANCE == null) {
//            INSTANCE = CalendarProvider()
//        }
//    }
//
//}