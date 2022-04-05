package nikitagorbatko.example.sankirtan

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.res.stringArrayResource
import java.time.YearMonth
import java.util.*

object CalendarProvider {
    val calendar = GregorianCalendar.getInstance()//createCalendar()
    val monthNum = calendar.get(GregorianCalendar.MONTH)
    val year = calendar.get(GregorianCalendar.YEAR)
    val day = calendar.get(GregorianCalendar.DAY_OF_MONTH)
    @RequiresApi(Build.VERSION_CODES.O)
    val days = YearMonth.of(year, monthNum + 1).lengthOfMonth()
    private val testCalendar = GregorianCalendar(year, monthNum, 1)
    private val dayOfWeek = testCalendar.get(GregorianCalendar.DAY_OF_WEEK)
    val firstDayOfFirstWeek = if (dayOfWeek == 1) 7 else dayOfWeek - 1
}