package nikitagorbatko.example.sankirtan.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.ArrowBack
import androidx.compose.material.icons.twotone.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.unit.dp
import nikitagorbatko.example.sankirtan.CalendarProvider
import nikitagorbatko.example.sankirtan.R
import nikitagorbatko.example.sankirtan.room.DateHolder
import nikitagorbatko.example.sankirtan.room.DistributedItem

@Composable
fun MonthCard(
    year: Int,
    monthNum: Int,
    onArrowClick: (b: Boolean) -> Unit,
    clickedDay: Int,
    onItemClick: (date: Int, list: List<DistributedItem>) -> Unit,
    distributedItems: List<DistributedItem>,
) {
    val distributedItemsCopy = distributedItems.toMutableList()
    val months = stringArrayResource(R.array.months)
    CalendarProvider.setMonth(monthNum)
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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .paddingFrom(alignmentLine = FirstBaseline, 40.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = { onArrowClick(false) },
                    content = { Icon(Icons.TwoTone.ArrowBack, contentDescription = null) },
                )
                Box(Modifier.height(56.dp)) {
                    Text(
                        text = "$year $month",
                        style = MaterialTheme.typography.h6,
                    )
                }
                IconButton(
                    onClick = { onArrowClick(true) },
                    content = { Icon(Icons.TwoTone.ArrowForward, contentDescription = null) },
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Day("ПН", style = Style.BODY2)
                Day("ВТ", style = Style.BODY2)
                Day("СР", style = Style.BODY2)
                Day("ЧТ", style = Style.BODY2)
                Day("ПТ", style = Style.BODY2)
                Day("СБ", style = Style.BODY2)
                Day("ВС", style = Style.BODY2)
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                for (i in 1..7) {
                    if (counter < CalendarProvider.days) {
                        if (i >= CalendarProvider.firstDayOfFirstWeek) {
                            val books = selectBooks(counter, distributedItemsCopy)
                            Day(
                                num = "${counter++}",
                                onClick = { date, list ->
                                    onItemClick(date, list)
                                    //clickedDay = DateHolder(it).day
                                },
                                clicked = clickedDay == counter - 1,
                                clickable = true,
                                list = books
                            )
                        } else {
                            Day()
                        }
                    }
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                for (i in 1..7) {
                    val books = selectBooks(counter, distributedItemsCopy)
                    Day(
                        num = "${counter++}",
                        onClick = { date, list ->
                            onItemClick(date, list)
                            //clickedDay = DateHolder(it).day
                        },
                        clicked = clickedDay == counter - 1,
                        clickable = true,
                        list = books
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                for (i in 1..7) {
                    val books = selectBooks(counter, distributedItemsCopy)
                    Day(
                        num = "${counter++}",
                        onClick = { date, list ->
                            onItemClick(date, list)
                            //clickedDay = DateHolder(it).day
                        },
                        clicked = clickedDay == counter - 1,
                        clickable = true,
                        list = books
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                if (counter <= CalendarProvider.days) {
                    for (i in 1..7) {
                        if (counter <= CalendarProvider.days) {
                            val books = selectBooks(counter, distributedItemsCopy)
                            Day(
                                num = "${counter++}",
                                onClick = { date, list ->
                                    onItemClick(date, list)
                                    //clickedDay = DateHolder(it).day
                                },
                                clicked = clickedDay == counter - 1,
                                clickable = true,
                                list = books
                            )
                        } else {
                            Day()
                        }
                    }
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                if (counter <= CalendarProvider.days) {
                    for (i in 1..7) {
                        if (counter <= CalendarProvider.days) {
                            val books = selectBooks(counter, distributedItemsCopy)
                            Day(
                                num = "${counter++}",
                                onClick = { date, list ->
                                    onItemClick(date, list)
                                    //clickedDay = DateHolder(it).day
                                },
                                clicked = clickedDay == counter - 1,
                                clickable = true,
                                list = books
                            )
                        } else {
                            Day()
                        }
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                if (counter <= CalendarProvider.days) {
                    for (i in 1..7) {
                        if (counter <= CalendarProvider.days) {
                            val books = selectBooks(counter, distributedItemsCopy)
                            Day(
                                num = "${counter++}",
                                onClick = { date, list ->
                                    onItemClick(date, list)
                                    //clickedDay = DateHolder(it).day
                                },
                                clicked = clickedDay == counter - 1,
                                clickable = true,
                                list = books
                            )
                        } else {
                            Day()
                        }
                    }
                }
            }
        }
        counter = 1
    }
}

fun selectBooks(
    day: Int,
    distributedItemsCopy: MutableList<DistributedItem>
): List<DistributedItem> {
    val books = mutableListOf<DistributedItem>()
    if (distributedItemsCopy.isNotEmpty()) {
        distributedItemsCopy.forEach {
            if (it.date == DateHolder(
                    day,
                    CalendarProvider.monthNum,
                    CalendarProvider.year
                ).intDate
            ) {
                books += it
            }
        }
    }
    return books
}