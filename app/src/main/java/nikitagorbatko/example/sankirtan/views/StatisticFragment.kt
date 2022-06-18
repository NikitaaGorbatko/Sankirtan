package nikitagorbatko.example.sankirtan.views

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.ArrowBack
import androidx.compose.material.icons.twotone.ArrowForward
import androidx.compose.material.icons.twotone.Share
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import nikitagorbatko.example.sankirtan.CalendarProvider
import nikitagorbatko.example.sankirtan.R
import nikitagorbatko.example.sankirtan.room.*
import java.util.*


//class StatisticScreenViewModel: ViewModel() {
//    private val _list = MutableLiveData(mutableListOf<DistributedItem>())
//    val list: LiveData<MutableList<DistributedItem>> = _list
//
//    fun onListChange(list_param: MutableList<DistributedItem>) {
//        _list.value = list_param
//    }
//}
//
//@ExperimentalAnimationApi
//@ExperimentalMaterialApi
//@ExperimentalUnitApi
//@Composable
//fun Statistic(
//    statisticScreenViewModel: StatisticScreenViewModel = viewModel(),
//    distributedItems: List<DistributedItem>,
//    days: List<Day>,
//    coroutineScope: CoroutineScope,
//    snackbarHostState: SnackbarHostState,
//    dao: BookDao,
//    insertDayLambda: () -> Unit,
//    liftStringLambda: (text: String) -> Unit
//) {
//    val list by statisticScreenViewModel.list.observeAsState()
//
//    StatisticScreen(
//        distributedItems = distributedItems,
//        days = days,
//        coroutineScope = coroutineScope,
//        snackbarHostState = snackbarHostState,
//        dao = dao,
//        insertDayLambda = insertDayLambda,
//        liftStringLambda = liftStringLambda
//    )
//}

@ExperimentalUnitApi
@ExperimentalMaterialApi
@ExperimentalAnimationApi
@SuppressLint("UnrememberedMutableState")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun StatisticScreen(
    distributedItems: List<DistributedItem>,
    days: List<Day>,
    coroutineScope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
    dao: BookDao,
    insertDayLambda: () -> Unit,
    liftStringLambda: (text: String) -> Unit
) {
    var list by mutableStateOf(listOf<DistributedItem>())
    var donation by remember { mutableStateOf("0") }
    var totalCost = 0
    var savedDate = 0
    var localDay: Day? = null
    var pickedDay by remember { mutableStateOf(0) }
    var monthNum by remember { mutableStateOf(CalendarProvider.monthNum) }
    val stringBuilder = StringBuilder()//StringBuffer in other thread is preffered

    //distributedItems.forEach{ totalCost += it.amount * it.cost }
    LazyColumn {
        item {
            //CalendarProvider.getInstance()
            MonthCard(
                year = CalendarProvider.year,
                monthNum = monthNum,
                onArrowClick = {
                    list = mutableListOf()
                    pickedDay = 0
                    if (it) {
                        CalendarProvider.setMonth(++monthNum)
                    } else {
                        CalendarProvider.setMonth(--monthNum)
                    }
                },
                clickedDay = pickedDay,
                onItemClick = { date, list_par ->
                    donation = "0"
                    savedDate = date
                    localDay = null
                    list = list_par
                    pickedDay = DateHolder(date).day
                },
                list = distributedItems
            )
        }
        item {
            if (list.isEmpty()) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
                    Text(
                        "Нет книг в этот день.\nДобавьте их и распространите \nв секции \"Портфель\".",
                        modifier = Modifier.padding(16.dp, 0.dp, 16.dp, 76.dp),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.body2,
                    )
                }
            }
            AnimatedVisibility(
                visible = list.isNotEmpty(),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                stringBuilder.clear()
                val holder = DateHolder(savedDate)
                val day = if (holder.day < 10) "0${holder.day}" else holder.day
                val month = if (holder.month < 10) "0${holder.month}" else holder.month
                val year = holder.year
                stringBuilder.append("$day.$month.$year\n\n")

                Column {
                    var expanded by remember { mutableStateOf(false) }
                    //donation = ""
                    Divider(modifier = Modifier.padding(start = 16.dp, end = 16.dp))
                    Box(
//                        elevation = 4.dp,
//                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
//                            .padding(16.dp, 0.dp, 16.dp, 16.dp)
                            .fillMaxWidth()
                    ) {
                        Column {
                            totalCost = 0
                            days.forEach {
                                if (it.day == savedDate) {
                                    donation = it.donation.toString()
                                    localDay = it
                                    return@forEach
                                }
                            }
                            list.forEach {
                                totalCost += it.amount * it.cost
                                DistributedItemCard(it)
                                if (it.date == savedDate) {
                                    stringBuilder
                                        .append("${it.name}:\n${it.amount}шт по ${it.cost}руб = ${it.cost * it.amount}руб.\n\n")
                                }
                            }
                        }
                    }
                    Card(
                        elevation = 4.dp,
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .padding(16.dp, 0.dp, 16.dp, 76.dp)
                            .fillMaxWidth()
                    ) {
                        Column {
                            Box(Modifier.height(56.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Итог",
                                        style = MaterialTheme.typography.h6,
                                        modifier = Modifier
                                            .paddingFrom(alignmentLine = FirstBaseline, 40.dp)
                                            .padding(start = 24.dp)
                                    )
                                    IconButton(
                                        modifier = Modifier
                                            .paddingFrom(alignmentLine = FirstBaseline, 40.dp)
                                            .padding(end = 24.dp),
                                        onClick = {
                                            if (stringBuilder.isEmpty()) {
                                                list.forEach {
                                                    if (it.date == savedDate) {
                                                        totalCost += it.amount * it.cost
                                                        stringBuilder
                                                            .append("${it.name}:\n${it.amount}шт по ${it.cost}руб = ${it.cost * it.amount}руб.\n\n")
                                                    }
                                                }
                                            }
                                            stringBuilder.append(
                                                "\nОптовая цена: ${totalCost}руб\nСбор: ${donation}руб\nРезультат: ${
                                                    try {
                                                        "${donation.toInt() - totalCost}руб"
                                                    } catch (_: Exception) {
                                                        "${-totalCost}руб"
                                                    }
                                                }"
                                            )
                                            liftStringLambda(stringBuilder.toString())
                                            stringBuilder.clear()
                                        },
                                        content = {
                                            Icon(
                                                Icons.TwoTone.Share,
                                                contentDescription = null
                                            )
                                        },
                                    )
                                }
                            }
                            //Result calculator
                            Row {
                                OutlinedTextField(
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    modifier = Modifier
                                        .padding(24.dp, 0.dp, 8.dp, 28.dp)
                                        .width(100.dp),
                                    value = donation,
                                    maxLines = 1,
                                    onValueChange = {
                                        if (it.length < 7) donation = it
                                    },
                                    keyboardActions = KeyboardActions(onDone = { }),
                                    label = { Text("Сбор") }
                                )
                                OutlinedTextField(
                                    //keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                                    modifier = Modifier
                                        .padding(8.dp, 0.dp, 8.dp, 28.dp)
                                        .width(100.dp),
                                    value = totalCost.toString(),
                                    maxLines = 1,
                                    enabled = false,
                                    onValueChange = { },
                                    //keyboardActions = KeyboardActions(onDone = {}),
                                    label = { Text("Стоимость") }
                                )
                                Box(
                                    Modifier.height(60.dp),
                                    //contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "=",
                                        style = TextStyle(fontSize = 25.sp),
                                        modifier = Modifier
                                            .paddingFrom(alignmentLine = FirstBaseline, 45.dp)
                                    )
                                }
                                OutlinedTextField(
                                    modifier = Modifier
                                        .padding(8.dp, 0.dp, 0.dp, 28.dp)
                                        .width(100.dp),
                                    value = try {
                                        "${donation.toInt() - totalCost}"
                                    } catch (_: Exception) {
                                        "${-totalCost}"
                                    },
                                    maxLines = 1,
                                    enabled = false,
                                    onValueChange = { },
                                    //keyboardActions = KeyboardActions(onDone = {}),
                                    label = { Text("Результат") }
                                )
                            }
                            Box(
                                contentAlignment = Alignment.BottomEnd,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                TextButton(
                                    enabled = donation.isNotEmpty(),
                                    modifier = Modifier.padding(8.dp, 0.dp, 8.dp, 8.dp),
                                    onClick = {
                                        val dateHolder = DateHolder(
                                            pickedDay,
                                            CalendarProvider.monthNum,
                                            CalendarProvider.year
                                        )
                                        if (donation == "0") {
                                            //Show snackbar or toast
                                        } else {
                                            if (localDay != null) {
                                                if (dao.updateDay(
                                                        localDay!!.id,
                                                        donation.toInt()
                                                    ) > 0
                                                ) {
                                                    coroutineScope.launch {
                                                        snackbarHostState.showSnackbar(
                                                            "Сохранено",
                                                            "OK",
                                                            SnackbarDuration.Short
                                                        )
                                                    }
                                                }
                                            } else {
                                                if (dao.insertDay(
                                                        dateHolder.intDate,
                                                        donation.toInt()
                                                    ) > 0
                                                ) {
                                                    coroutineScope.launch {
                                                        snackbarHostState.showSnackbar(
                                                            "Сохранено",
                                                            "OK",
                                                            SnackbarDuration.Short
                                                        )
                                                    }
                                                }
                                            }
                                            insertDayLambda()
                                        }
                                        //pickedDay = 0
                                    }
                                ) { Text("СОХРАНИТЬ") }
                            }
                        }
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
private fun DistributedItemCard(book: DistributedItem) {
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
    Divider(modifier = Modifier.padding(start = 16.dp, end = 16.dp))
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MonthCard(
    year: Int,
    monthNum: Int,
    onArrowClick: (b: Boolean) -> Unit,
    clickedDay: Int,
    onItemClick: (date: Int, list: List<DistributedItem>) -> Unit,
    list: List<DistributedItem>,
) {
    val months = stringArrayResource(R.array.months)
    CalendarProvider.setMonth(monthNum)
    val month = months[CalendarProvider.monthNum]
    var counter = 1

    Card(
        elevation = 4.dp,
//        shape = RoundedCornerShape(16.dp),
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
                            val books = mutableListOf<DistributedItem>()
                            list.forEach {
                                if (it.date == DateHolder(
                                        counter,
                                        CalendarProvider.monthNum,
                                        CalendarProvider.year
                                    ).intDate
                                ) {
                                    books += it
                                }
                            }
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
                    val books = mutableListOf<DistributedItem>()
                    list.forEach {
                        if (it.date == DateHolder(
                                counter,
                                CalendarProvider.monthNum,
                                CalendarProvider.year
                            ).intDate
                        ) {
                            books += it
                        }
                    }
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
                    val books = mutableListOf<DistributedItem>()
                    list.forEach {
                        if (it.date == DateHolder(
                                counter,
                                CalendarProvider.monthNum,
                                CalendarProvider.year
                            ).intDate
                        ) {
                            books += it
                        }
                    }
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
                            val books = mutableListOf<DistributedItem>()
                            list.forEach {
                                if (it.date == DateHolder(
                                        counter,
                                        CalendarProvider.monthNum,
                                        CalendarProvider.year
                                    ).intDate
                                ) {
                                    books += it
                                }
                            }
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
                            val books = mutableListOf<DistributedItem>()
                            list.forEach {
                                if (it.date == DateHolder(
                                        counter,
                                        CalendarProvider.monthNum,
                                        CalendarProvider.year
                                    ).intDate
                                ) {
                                    books += it
                                }
                            }
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
                            val books = mutableListOf<DistributedItem>()
                            list.forEach {
                                if (it.date == DateHolder(
                                        counter,
                                        CalendarProvider.monthNum,
                                        CalendarProvider.year
                                    ).intDate
                                ) {
                                    books += it
                                }
                            }
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

enum class Style { BODY1, BODY2 }

@Composable
fun Day(
    num: String = "",
    style: Style = Style.BODY1,
    clickable: Boolean = false,
    onClick: (date: Int, list: List<DistributedItem>) -> Unit = { _, _ -> },
    list: List<DistributedItem> = listOf(),
    clicked: Boolean = false
) {
    val modifier: Modifier = if (clickable) {
        val date = DateHolder(num.toInt(), CalendarProvider.monthNum, CalendarProvider.year)
        Modifier
            .size(38.dp)
            .padding(1.dp)
            .clip(CircleShape)
            .background(if (clicked) Color.LightGray else Color.Unspecified)
            .clickable { onClick(date.intDate, list) }
            .border(
                if (list.isNotEmpty()) BorderStroke(1.dp, Color.Black) else BorderStroke(
                    0.dp,
                    Color.Unspecified
                ), shape = CircleShape
            )
    } else {
        Modifier
            .size(38.dp)
            .padding(1.dp)
    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        Text(
            text = num,
            style = if (style == Style.BODY1) {
                MaterialTheme.typography.body1
            } else {
                MaterialTheme.typography.body2
            },
            textAlign = TextAlign.Center
        )
    }
}

//@ExperimentalUnitApi
//@Preview
//@Composable
//fun Preview() {
//    MonthCard(
//        year = 2022,
//        monthNum = 6,
//        onArrowClick = {},
//        clickedDay = 2,
//        onItemClick = {}
//    )
//}



