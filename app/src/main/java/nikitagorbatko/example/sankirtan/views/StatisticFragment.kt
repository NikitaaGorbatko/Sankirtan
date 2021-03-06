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
import nikitagorbatko.example.sankirtan.CalendarProvider
import nikitagorbatko.example.sankirtan.MessageBuilder
import nikitagorbatko.example.sankirtan.room.*
import java.util.*

@ExperimentalComposeUiApi
@ExperimentalUnitApi
@ExperimentalMaterialApi
@ExperimentalAnimationApi
@SuppressLint("UnrememberedMutableState")
@Composable
fun StatisticScreen(
    distributedItems: List<DistributedItem>,
    days: List<Day>,
    showSnackbar: (message: String, actionLabel: String) -> Unit,
    dao: BookDao,
    insertDayLambda: () -> Unit,
    liftStringLambda: (text: String) -> Unit,
    deleteDistributedItem: (distributedItem: DistributedItem, day: Day?) -> Unit,
    clickedDate: Int,
    onClickedDateChange: (Int) -> Unit,
    distributedDayList: List<DistributedItem>,
    onDistributedDayListChange: (list: List<DistributedItem>) -> Unit,
    donation_param: String,
) {
    //val focusRequest = remember { FocusRequester() }
    var donation by remember { mutableStateOf("0") }
    donation = donation_param
    var clickedDay by remember { mutableStateOf(0) }
    var monthNum by remember { mutableStateOf(CalendarProvider.monthNum) }
    var localDay: Day? = null

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
                        "?????? ???????? ?? ???????? ????????. ???????????????? ???????? ?? ???????????????? ?????????? ?? ?????????????? ???????????? \"+\".",
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
                        //.clickable {  }
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
                                    title = "???????????????????? ????????: $amount",
                                    body = "?????????????? ????????: $cost",
                                    onClick = {
                                        expanded = !expanded
                                    },
                                    expanded = expanded
                                )
                                Divider()
                                val isLastSet = distributedDayList.size < 2
                                distributedDayList.forEach {
                                    DistributedItemCard(
                                        it,
                                        deleteDistributedItem = { item ->
                                            deleteDistributedItem(
                                                item,
                                                if (isLastSet && localDay != null) {
                                                    //onDonationChange("0")
                                                    localDay
                                                } else {
                                                    null
                                                }
                                            )
                                        },
                                    )
                                }
                            }
                        } else {
                            FakeItem(
                                title = "???????????????????? ????????: $amount",
                                body = "?????????????? ????????: $cost",
                                onClick = {
                                    expanded = !expanded
                                },
                                expanded = expanded
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
                                        text = "???????? ${holder.stringDate()}",
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
                                            liftStringLambda(
                                                MessageBuilder.build(
                                                    distributedDayList,
                                                    holder,
                                                    totalCost,
                                                    donation.toInt(),
                                                    clickedDate
                                                )
                                            )
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
                                                showSnackbar("??????????????????", "OK")
                                            }
                                        } else {
                                            if (dao.insertDay(
                                                    dateHolder.intDate,
                                                    donation.toInt()
                                                ) > 0
                                            ) {
                                                showSnackbar("??????????????????", "OK")
                                            }
                                        }
                                        insertDayLambda()
                                        keyboardController?.hide()
                                    }),
                                label = { Text("??????????????????????????") }
                            )
                            OutlinedTextField(
                                modifier = Modifier
                                    .padding(24.dp, 0.dp, 24.dp, 8.dp)
                                    .fillMaxWidth(),
                                value = totalCost.toString(),
                                maxLines = 1,
                                enabled = false,
                                onValueChange = { },
                                label = { Text("?????????????? ????????") }
                            )
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
                                label = { Text("??????????????????") }
                            )
                        }
                    }
                }
            }
        }
    }
}


