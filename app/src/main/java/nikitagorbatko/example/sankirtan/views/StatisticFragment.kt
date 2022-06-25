package nikitagorbatko.example.sankirtan.views

import android.annotation.SuppressLint
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Share
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import nikitagorbatko.example.sankirtan.CalendarProvider
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

@ExperimentalComposeUiApi
@ExperimentalUnitApi
@ExperimentalMaterialApi
@ExperimentalAnimationApi
@SuppressLint("UnrememberedMutableState")
@Composable
fun StatisticScreen(
    distributedItems: List<DistributedItem>,
    days: List<Day>,
    coroutineScope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
    dao: BookDao,
    insertDayLambda: () -> Unit,
    liftStringLambda: (text: String) -> Unit,
    clickedDate: Int,
    onClickedDateChange: (Int) -> Unit,
    distributedDayList: List<DistributedItem>,
    onDistributedDayListChange: (list: List<DistributedItem>) -> Unit
) {
    //var email by rememberSaveable(saver = stateSaver()) { mutableStateOf("") }

    StatisticContent(
        distributedItems = distributedItems,
        days = days,
        coroutineScope = coroutineScope,
        snackbarHostState = snackbarHostState,
        dao = dao,
        insertDayLambda = insertDayLambda,
        liftStringLambda = liftStringLambda,
        clickedDate,
        onClickedDateChange = { onClickedDateChange(it) },
        distributedDayList,
        onDistributedDayListChange = { onDistributedDayListChange(it) }
    )
}

@ExperimentalComposeUiApi
@ExperimentalUnitApi
@ExperimentalMaterialApi
@ExperimentalAnimationApi
@SuppressLint("UnrememberedMutableState")
@Composable
fun StatisticContent(
    distributedItems: List<DistributedItem>,
    days: List<Day>,
    coroutineScope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
    dao: BookDao,
    insertDayLambda: () -> Unit,
    liftStringLambda: (text: String) -> Unit,
    clickedDate: Int,
    onClickedDateChange: (Int) -> Unit,
    distributedDayList: List<DistributedItem>,
    onDistributedDayListChange: (list: List<DistributedItem>) -> Unit
) {
    var donation by remember { mutableStateOf("0") }
    var clickedDay by remember { mutableStateOf(0) }
    var monthNum by remember { mutableStateOf(CalendarProvider.monthNum) }
    var localDay: Day? = null

//    if (clickedDay != 0) {
//        list.forEach {
//            if (it.date = )
//        }
//    }

    LazyColumn {
        item {
            //CalendarProvider.getInstance()
            MonthCard(
                year = CalendarProvider.year,
                monthNum = monthNum,
                onArrowClick = {
                    onClickedDateChange(0)
                    onDistributedDayListChange(mutableListOf())
                    clickedDay = 0
                    if (it) {
                        CalendarProvider.setMonth(++monthNum)
                    } else {
                        CalendarProvider.setMonth(--monthNum)
                    }
                },
                clickedDay = clickedDay,
                onItemClick = { date, list_par ->
                    donation = "0"
                    onClickedDateChange(date)
                    localDay = null
                    onDistributedDayListChange(list_par)
                    clickedDay = DateHolder(date).day
                },
                distributedItems = distributedItems
            )
        }
        item {
            if (distributedDayList.isEmpty()) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
                    Text(
                        "Нет книг в этот день.\n",
                        modifier = Modifier.padding(16.dp, 0.dp, 16.dp, 76.dp),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.body2,
                    )
                }
            }
            AnimatedVisibility(
                visible = distributedDayList.isNotEmpty(),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                val holder = DateHolder(clickedDate)
                var totalCost = 0
                distributedDayList.forEach {
                    totalCost += it.amount * it.cost
                }
                Column {
                    var expanded by remember { mutableStateOf(false) }
                    Card(
                        elevation = 4.dp,
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .padding(16.dp, 0.dp, 16.dp, 16.dp)
                            .fillMaxWidth()
                            .clickable { expanded = !expanded }
                    ) {
                        var amount = 0
                        var cost = 0
                        distributedDayList.forEach {
                            amount += it.amount
                            cost += it.amount * it.cost
                        }
                        days.forEach {
                            if (it.day == clickedDate) {
                                donation = it.donation.toString()
                                localDay = it
                                return@forEach
                            }
                        }
                        if (expanded) {
                            Column {
                                FakeItem(
                                    title = "Количество книг: $amount",
                                    body = "Оптовая цена: $cost",
                                    onClick = {

                                    }
                                )
                                Divider()
                                distributedDayList.forEach {
                                    DistributedItemCard(it)
                                }
                            }
                        } else {
                            FakeItem(
                                title = "Количество книг: $amount",
                                body = "Оптовая цена: $cost",
                                onClick = {

                                }
                            )
                        }
                    }
                    Card(
                        elevation = 4.dp,
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .padding(16.dp, 0.dp, 16.dp, 76.dp)
                            .fillMaxWidth()
                    ) {
                        val keyboardController = LocalSoftwareKeyboardController.current
                        Column {
                            Box(Modifier.height(64.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Итог ${holder.stringDate()}",
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
                                            val stringBuilder =
                                                StringBuilder()//StringBuffer in other thread is preffered
                                            stringBuilder.append("${holder.stringDate()}\n\n")
                                            distributedDayList.forEach {
                                                if (it.date == clickedDate) {
                                                    stringBuilder
                                                        .append("${it.name}:\n${it.amount}шт по ${it.cost}руб = ${it.cost * it.amount}руб.\n\n")
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
                            OutlinedTextField(
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number,
                                    imeAction = ImeAction.Done
                                ),
                                modifier = Modifier
                                    .padding(24.dp, 0.dp, 24.dp, 8.dp)
                                    .fillMaxWidth(),
                                value = donation,
                                maxLines = 1,
                                onValueChange = {
                                    if (it.length < 10) donation = it
                                },
                                keyboardActions = KeyboardActions(
                                    onDone = {
                                        val dateHolder = DateHolder(
                                            clickedDay,
                                            CalendarProvider.monthNum,
                                            CalendarProvider.year
                                        )
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
                                        //}
                                        //pickedDay = 0
                                        keyboardController?.hide()
                                    }),
                                label = { Text("Пожертвование") }
                            )
                            OutlinedTextField(
                                //keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                                modifier = Modifier
                                    .padding(24.dp, 0.dp, 24.dp, 8.dp)
                                    .fillMaxWidth(),
                                value = totalCost.toString(),
                                maxLines = 1,
                                enabled = false,
                                onValueChange = { },
                                //keyboardActions = KeyboardActions(onDone = {}),
                                label = { Text("Оптовая цена") }
                            )
//                                Box(
//                                    Modifier.height(60.dp),
//                                    //contentAlignment = Alignment.Center
//                                ) {
//                                    Text(
//                                        text = "=",
//                                        style = TextStyle(fontSize = 25.sp),
//                                        modifier = Modifier
//                                            .paddingFrom(alignmentLine = FirstBaseline, 45.dp)
//                                    )
//                                }
                            OutlinedTextField(
                                modifier = Modifier
                                    .padding(24.dp, 0.dp, 24.dp, 28.dp)
                                    .fillMaxWidth(),
                                value = try {
                                    "${donation.toInt() - totalCost}"
                                } catch (_: Exception) {
                                    "${-totalCost}"
                                },
                                maxLines = 1,
                                enabled = false,
                                onValueChange = { },
                                label = { Text("Результат") }
                            )
//                            Box(
//                                contentAlignment = Alignment.BottomEnd,
//                                modifier = Modifier.fillMaxSize()
//                            ) {
//                                TextButton(
//                                    enabled = donation.isNotEmpty(),
//                                    modifier = Modifier.padding(8.dp, 0.dp, 8.dp, 8.dp),
//                                    onClick = {
//                                        val dateHolder = DateHolder(
//                                            clickedDay,
//                                            CalendarProvider.monthNum,
//                                            CalendarProvider.year
//                                        )
//                                        if (donation == "0") {
//                                            //Show snackbar or toast
//                                        } else {
//                                            if (localDay != null) {
//                                                if (dao.updateDay(
//                                                        localDay!!.id,
//                                                        donation.toInt()
//                                                    ) > 0
//                                                ) {
//                                                    coroutineScope.launch {
//                                                        snackbarHostState.showSnackbar(
//                                                            "Сохранено",
//                                                            "OK",
//                                                            SnackbarDuration.Short
//                                                        )
//                                                    }
//                                                }
//                                            } else {
//                                                if (dao.insertDay(
//                                                        dateHolder.intDate,
//                                                        donation.toInt()
//                                                    ) > 0
//                                                ) {
//                                                    coroutineScope.launch {
//                                                        snackbarHostState.showSnackbar(
//                                                            "Сохранено",
//                                                            "OK",
//                                                            SnackbarDuration.Short
//                                                        )
//                                                    }
//                                                }
//                                            }
//                                            insertDayLambda()
//                                        }
//                                        //pickedDay = 0
//                                    }
//                                ) { Text("СОХРАНИТЬ") }
//                            }
                        }
                    }
                }
            }
        }
    }
}


