package nikitagorbatko.example.sankirtan

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.YearMonth
import java.util.*
import kotlin.time.days

@RequiresApi(Build.VERSION_CODES.O)
//@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun StatisticScreen(coroutineScope: CoroutineScope) {
    MonthCard()
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MonthCard() {
    val calendar = GregorianCalendar.getInstance()
    val monthNum = calendar.get(GregorianCalendar.MONTH) + 1
    val year = calendar.get(GregorianCalendar.YEAR)
    val days = YearMonth.of(year, monthNum).lengthOfMonth()
    var day = calendar.get(GregorianCalendar.DAY_OF_MONTH)
    val dayOfWeek = calendar.get(GregorianCalendar.DAY_OF_WEEK) - 1

    val firstDayOfMonthDay = 8 - (day - dayOfWeek) % 7

    val months = stringArrayResource(R.array.months)
    val month = months[monthNum - 1]
    var counter = 1

    if (day > 7) {
        day -= dayOfWeek
        day %= 7
    }
    val firstDayOfFirstWeek = 7 - day + 1

    Card(
        elevation = 4.dp,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(Modifier.height(56.dp)) {
                Text(
                    text = "$month $year",
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.paddingFrom(alignmentLine = FirstBaseline, 40.dp)
                )
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                Day("ПН", style = Style.BODY2)
                Day("ВТ", style = Style.BODY2)
                Day("СР", style = Style.BODY2)
                Day("ЧТ", style = Style.BODY2)
                Day("ПТ", style = Style.BODY2)
                Day("СБ", style = Style.BODY2)
                Day("ВС", style = Style.BODY2)
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                for (i in 1 .. 7) {
                    if (counter++ < days) {
                        if (i == firstDayOfFirstWeek || i > firstDayOfFirstWeek) {
                            Day ("${counter++}")
                        } else {
                            Day("")
                        }
                    }
                }
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                for (i in 1 .. 7) {
                    Day("${counter++}")
                }
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                for (i in 1 .. 7) {
                    Day("${counter++}")
                }
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                if (counter <= days) {
                    for (i in 1 .. 7) {
                        if (counter > days) {
                            Day("")
                        } else {
                            Day("${counter++}")
                        }
                    }
                }
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                if (counter <= days) {
                    for (i in 1 .. 7) {
                        if (counter > days) {
                            Day("")
                        } else {
                            Day("${counter++}")
                        }
                    }
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                if (counter <= days) {
                    for (i in 1 .. 7) {
                        if (counter > days) {
                            Day("")
                        } else {
                            Day("${counter++}")
                        }
                    }
                }
            }
        }
    }
}

enum class Style {
    BODY1, BODY2
}

@Composable
fun Day(num: String, isWeekend: Boolean = false, style: Style = Style.BODY1) {
    Text(
        text = num,
        style = if (style == Style.BODY1) { MaterialTheme.typography.body1 } else { MaterialTheme.typography.body2 },
        modifier = Modifier.size(40.dp).clickable {
            //extract data from the database and present it to user
        },
        textAlign = TextAlign.Center
    )
}
