package nikitagorbatko.example.sankirtan

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope


@ExperimentalUnitApi
@ExperimentalMaterialApi
@ExperimentalAnimationApi
@SuppressLint("UnrememberedMutableState")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun StatisticScreen(distributedItems: List<DistributedItem>, coroutineScope: CoroutineScope) {
    var list by mutableStateOf(listOf<DistributedItem>())
    LazyColumn {
        item {
            MonthCard(
                onItemClick = {
                    date: Int -> run {
                        list = listOf()
                        distributedItems.forEach { item ->
                            if (item.date == date) {
                                list = list.toMutableList().also { it.add(item) }
                            }
                        }
                    }
                }
            )
        }
        item {
            if (list.isEmpty()) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
                    Text(
                        "Нет книг в этот день.",
                        modifier = Modifier.padding(16.dp, 0.dp, 16.dp, 76.dp),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.body2,
                    )
                }
            } else {
                Card(
                    elevation = 4.dp,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .padding(16.dp, 0.dp, 16.dp, 76.dp)
                        .fillMaxWidth()
                ) {
                    Column {
                        list.forEach { DistributedItemCard(it) }
                    }
                }
            }
        }
    }
}

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@ExperimentalUnitApi
@Composable
private fun DistributedItemCard(
    book: DistributedItem,
    onEditDialog: (book: Book) -> Unit = {}
) {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            //.clickable { onEditDialog(book) }
    ) {
        Text(
            book.name,
            maxLines = 1,
            fontSize = TextUnit(16f, TextUnitType.Sp),
            modifier = Modifier.padding(16.dp, 10.dp, 16.dp, 2.dp)
        )
        Text(
            "${book.amount} по ${book.cost}р = ${book.amount * book.cost}р",
            style = MaterialTheme.typography.body2,
            modifier = Modifier.padding(16.dp, 0.dp, 16.dp, 10.dp)
        )
    }
    Divider()
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MonthCard(onItemClick: (date: Int) -> Unit) {
    val months = stringArrayResource(R.array.months)
    val month = months[CalendarProvider.monthNum]
    var counter = 1
    var clickedDay by remember { mutableStateOf(0) }

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
                    text = month,
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.paddingFrom(alignmentLine = FirstBaseline, 40.dp)
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
                for (i in 1 .. 7) {
                    if (counter < CalendarProvider.days) {
                        if (i >= CalendarProvider.firstDayOfFirstWeek) {
                            Day (
                                num = "${counter++}",
                                onClick = {
                                    onItemClick(it)
                                    clickedDay = Datee(it).day
                                },
                                clicked = clickedDay == counter - 1,
                                clickable = true
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
                for (i in 1 .. 7) {
                    Day (
                        num = "${counter++}",
                        onClick = {
                            onItemClick(it)
                            clickedDay = Datee(it).day
                        },
                        clicked = clickedDay == counter - 1,
                        clickable = true
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                for (i in 1 .. 7) {
                    Day (
                        num = "${counter++}",
                        onClick = {
                            onItemClick(it)
                            clickedDay = Datee(it).day
                        },
                        clicked = clickedDay == counter - 1,
                        clickable = true
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                if (counter <= CalendarProvider.days) {
                    for (i in 1 .. 7) {
                        if (counter <= CalendarProvider.days) {
                            Day (
                                num = "${counter++}",
                                onClick = {
                                    onItemClick(it)
                                    clickedDay = Datee(it).day
                                },
                                clickable = true
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
                    for (i in 1 .. 7) {
                        if (counter <= CalendarProvider.days) {
                            Day (
                                num = "${counter++}",
                                onClick = {
                                    onItemClick(it)
                                    clickedDay = Datee(it).day
                                },
                                clickable = true
                            )
                        } else {
                            Day()
                        }
                    }
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                if (counter <= CalendarProvider.days) {
                    for (i in 1 .. 7) {
                        if (counter <= CalendarProvider.days) {
                            Day (
                                num = "${counter++}",
                                onClick = {
                                    onItemClick(it)
                                    clickedDay = Datee(it).day
                                },
                                clickable = true
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

enum class Style { BODY1, BODY2 }

@Composable
fun Day(
    num: String = "",
    style: Style = Style.BODY1,
    clickable: Boolean = false,
    onClick: (date: Int) -> Unit = {},
    clicked: Boolean = false
) {
    val modifier: Modifier = if (clickable) {
        val date = Datee(num.toInt(), CalendarProvider.monthNum, CalendarProvider.year)
        Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(if (clicked) Color.LightGray else Color.Unspecified)
            .clickable { onClick(date.intDate) }
    } else { Modifier.size(40.dp) }
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        Text(
            text = num,
            style = if (style == Style.BODY1) { MaterialTheme.typography.body1 } else { MaterialTheme.typography.body2 },
            textAlign = TextAlign.Center
        )
    }
}
