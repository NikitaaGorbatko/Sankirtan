package nikitagorbatko.example.sankirtan

import java.util.*
import kotlin.math.floor

object CalendarProvider {
    private var calendar = GregorianCalendar.getInstance()//createCalendar()
    private val month_const = calendar.get(GregorianCalendar.MONTH)
    var monthNum = month_const
        private set
    private val year_const = calendar.get(GregorianCalendar.YEAR)
    var year = year_const
        private set
    var day = calendar.get(GregorianCalendar.DAY_OF_MONTH)
        private set
    private val daysCalendar = GregorianCalendar(year, monthNum, 1)
    var days = daysCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        private set
    private var testCalendar = GregorianCalendar(year, monthNum, 1)
    private var dayOfWeek = testCalendar.get(GregorianCalendar.DAY_OF_WEEK)
    var firstDayOfFirstWeek = if (dayOfWeek == 1) 7 else dayOfWeek - 1
        private set

    fun setMonth(month_par: Int) {
        var swipedYear = year

        when (month_par) {
            in Int.MIN_VALUE..-13 -> {
                val addition = if (month_par % 12 == 0) 0 else 1
                val years = floor(month_par / -12 + 0.999).toInt() + addition
                swipedYear = year_const - years
                monthNum = 12 - ((month_par % 12) * -1)
                if (monthNum == 12) monthNum = 0
            }
            in -12..-1 -> {
                val years = 1
                swipedYear = year_const - years
                monthNum = 12 - month_par * -1
            }
            in 0..11 -> {
                monthNum = month_par
                swipedYear = year_const
            }
            in 12..Int.MAX_VALUE -> {
                val years = month_par / 12
                monthNum = month_par % 12
                swipedYear = year_const + years
            }
        }

        calendar.set(swipedYear, monthNum, 1)
        day = calendar.get(GregorianCalendar.DAY_OF_MONTH)
        year = swipedYear
        val daysCalendar = GregorianCalendar(swipedYear, monthNum, 1)
        days = daysCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        testCalendar = GregorianCalendar(swipedYear, monthNum, 1)
        dayOfWeek = testCalendar.get(GregorianCalendar.DAY_OF_WEEK)
        firstDayOfFirstWeek = if (dayOfWeek == 1) 7 else dayOfWeek - 1
    }

    fun getInstance() {
        calendar = GregorianCalendar.getInstance()
        monthNum = calendar.get(GregorianCalendar.MONTH)
        year = calendar.get(GregorianCalendar.YEAR)
        day = calendar.get(GregorianCalendar.DAY_OF_MONTH)
        val daysCalendar = GregorianCalendar(year, monthNum, 1)
        days = daysCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        testCalendar = GregorianCalendar(year, monthNum, 1)
        dayOfWeek = testCalendar.get(GregorianCalendar.DAY_OF_WEEK)
        firstDayOfFirstWeek = if (dayOfWeek == 1) 7 else dayOfWeek - 1
    }
}