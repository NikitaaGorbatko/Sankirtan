package nikitagorbatko.example.sankirtan

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import java.time.YearMonth
import java.util.*

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
    val monthNum = calendar.get(GregorianCalendar.MONTH)
    val year = calendar.get(GregorianCalendar.YEAR)
    val days = YearMonth.of(year, monthNum + 1).lengthOfMonth()
    val testCalendar = GregorianCalendar(year, monthNum, 1)
    val firstDayOfFirstWeek = testCalendar.get(GregorianCalendar.DAY_OF_WEEK)
    val months = stringArrayResource(R.array.months)
    val month = months[monthNum]

    var counter = 1

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
    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(40.dp).clip(CircleShape).clickable {  }) {
        Text(
            text = num,
            style = if (style == Style.BODY1) { MaterialTheme.typography.body1 } else { MaterialTheme.typography.body2 },
            textAlign = TextAlign.Center
        )
    }

}
