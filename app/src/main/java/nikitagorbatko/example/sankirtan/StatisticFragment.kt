package nikitagorbatko.example.sankirtan

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import java.time.YearMonth
import java.util.*

val items = mutableMapOf<Date, DistributedItem>()

@RequiresApi(Build.VERSION_CODES.O)
//@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun StatisticScreen(distributedItems: List<DistributedItem>, coroutineScope: CoroutineScope) {
    var text by remember { mutableStateOf("") }
    Column {
        MonthCard(distributedItems, day = { text = it })
        Card(
            elevation = 4.dp,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.padding(16.dp).fillMaxWidth()
        ) {
            distributedItems.forEach { text += it.date.toString() }
            Text(text)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MonthCard(distributedItems: List<DistributedItem>, day: (String) -> Unit) {
    distributedItems.forEach { items[Date(it.date)] = it }

    val months = stringArrayResource(R.array.months)
    val month = months[CalendarProvider.monthNum]

    var counter = 1

    Card(
        elevation = 4.dp,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(Modifier.height(56.dp)) {
                Text(
                    text = "$month ${CalendarProvider.year} ${distributedItems.size}",
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.paddingFrom(alignmentLine = FirstBaseline, 40.dp)
                )
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                Day("ПН", style = Style.BODY2, clickable = false, make = { })
                Day("ВТ", style = Style.BODY2, clickable = false, make = { })
                Day("СР", style = Style.BODY2, clickable = false, make = { })
                Day("ЧТ", style = Style.BODY2, clickable = false, make = { })
                Day("ПТ", style = Style.BODY2, clickable = false, make = { })
                Day("СБ", style = Style.BODY2, clickable = false, make = { })
                Day("ВС", style = Style.BODY2, clickable = false, make = { })
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                for (i in 1 .. 7) {
                    if (counter < CalendarProvider.days) {
                        if (i == CalendarProvider.firstDayOfFirstWeek || i > CalendarProvider.firstDayOfFirstWeek) {
                            Day ("${counter++}", make = { day(it) })
                        } else {
                            Day("", clickable = false, make = { })
                        }
                    }
                }
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                for (i in 1 .. 7) {
                    Day("${counter++}", make = { day(it) })
                }
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                for (i in 1 .. 7) {
                    Day("${counter++}", make = { day(it) })
                }
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                if (counter <= CalendarProvider.days) {
                    for (i in 1 .. 7) {
                        if (counter > CalendarProvider.days) {
                            Day("", clickable = false, make = { })
                        } else {
                            Day("${counter++}", make = { day(it) })
                        }
                    }
                }
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                if (counter <= CalendarProvider.days) {
                    for (i in 1 .. 7) {
                        if (counter > CalendarProvider.days) {
                            Day("", clickable = false, make = { })
                        } else {
                            Day("${counter++}", make = { day(it) })
                        }
                    }
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                if (counter <= CalendarProvider.days) {
                    for (i in 1 .. 7) {
                        if (counter > CalendarProvider.days) {
                            Day("", clickable = false, make = { })
                            Day("${counter++}", make = { day(it) })
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
fun Day(
    num: String,
    isWeekend: Boolean = false,
    style: Style = Style.BODY1,
    clickable: Boolean = true,
    make: (day: String) -> Unit
) {
    var str = ""
    val modifier: Modifier = if (clickable) {
        Modifier
            .size(40.dp)
            .clip(CircleShape)
            .clickable {
                items.forEach { Log.d("TAG", it.key.toString()) }
                //Log.d("TAG", CalendarProvider.calendar.time.toString())
                make(str)
            }
    } else { Modifier.size(40.dp) }
    Box(contentAlignment = Alignment.Center, modifier = modifier) {
        Text(
            text = num,
            style = if (style == Style.BODY1) { MaterialTheme.typography.body1 } else { MaterialTheme.typography.body2 },
            textAlign = TextAlign.Center
        )
    }
}
