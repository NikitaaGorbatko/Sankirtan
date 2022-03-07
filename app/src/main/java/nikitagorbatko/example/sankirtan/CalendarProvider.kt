package nikitagorbatko.example.sankirtan

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.res.stringArrayResource
import java.time.YearMonth
import java.util.*

object CalendarProvider {
    private val calendar = GregorianCalendar.getInstance()
    val monthNum = calendar.get(GregorianCalendar.MONTH)
    val year = calendar.get(GregorianCalendar.YEAR)
    @RequiresApi(Build.VERSION_CODES.O)
    val days = YearMonth.of(year, monthNum + 1).lengthOfMonth()
    private val testCalendar = GregorianCalendar(year, monthNum, 1)
    val firstDayOfFirstWeek = testCalendar.get(GregorianCalendar.DAY_OF_WEEK)
}