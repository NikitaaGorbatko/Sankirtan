package nikitagorbatko.example.sankirtan

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import nikitagorbatko.example.sankirtan.room.*

//Create book dialog
@Composable
fun CreateBookDialog(
    dao: BookDao,
    showSnackbar: (message: String, actionLabel: String) -> Unit,
    close: () -> Unit
) {
    var bookName by remember { mutableStateOf("") }
    var bookCost by remember { mutableStateOf("") }

    Dialog(onDismissRequest = { close() }) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colors.surface)
                .height(intrinsicSize = IntrinsicSize.Min)
                .width(IntrinsicSize.Max)
                .padding(24.dp, 0.dp, 0.dp, 0.dp)
        ) {
            Box(Modifier.height(56.dp)) {
                Text(
                    text = "?????????? ??????????",
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier
                        .paddingFrom(alignmentLine = FirstBaseline, 40.dp)
                )
            }
            //New_116
            OutlinedTextField(
                modifier = Modifier.padding(bottom = 8.dp, end = 24.dp),
                value = bookName,
                maxLines = 1,
                onValueChange = { bookName = it },
                label = { Text("????????????????") }
            )
            OutlinedTextField(
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.padding(bottom = 28.dp, end = 24.dp),
                value = bookCost,
                maxLines = 1,
                onValueChange = { if (it.length < 6) bookCost = it },
                label = { Text("????????") }
            )
            Box(
                contentAlignment = Alignment.BottomEnd,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(end = 8.dp, bottom = 8.dp)
            ) {
                TextButton(
                    enabled = bookName.length > 3 && try {
                        bookCost.toInt() > 0
                    } catch (_: Exception) {
                        false
                    },
                    onClick = {
                        if (dao.insertBook(bookName, bookCost.toInt()) > 0) {
                            showSnackbar("$bookName ??????????????????", "????")
                        }
                        close()
                    }
                ) { Text("????????????????", style = MaterialTheme.typography.button) }
            }
        }
    }
}

//Edit book
@Composable
fun EditBookDialog(
    dao: BookDao,
    book: Book,
    showSnackbar: (message: String, actionLabel: String) -> Unit,
    close: () -> Unit
) {
    var editedName by remember { mutableStateOf(book.name) }
    var editedCost by remember { mutableStateOf(book.cost.toString()) }

    Dialog(onDismissRequest = { close() }) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colors.surface)
                .height(intrinsicSize = IntrinsicSize.Min)
                .width(IntrinsicSize.Max)
                .padding(24.dp, 0.dp, 0.dp, 0.dp)
        ) {
            Box(Modifier.height(56.dp)) {
                Text(
                    text = "???????????? #${book.id}",
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier
                        .paddingFrom(alignmentLine = FirstBaseline, 40.dp)
                )
            }
            OutlinedTextField(
                modifier = Modifier.padding(bottom = 8.dp, end = 24.dp),
                value = editedName,
                maxLines = 1,
                onValueChange = { editedName = it },
                label = { Text("????????????????") }
            )
            OutlinedTextField(
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.padding(bottom = 28.dp, end = 24.dp),
                value = editedCost,
                maxLines = 1,
                onValueChange = { if (it.length < 6) editedCost = it },
                label = { Text("????????") }
            )
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(end = 8.dp, bottom = 8.dp)
            ) {
                TextButton(onClick = {
                    if (dao.deleteBook(book) == 1) {
                        close()
                        showSnackbar("${book.name} ??????????????", "????")
                    }
                }) { Text("??????????????") }
                TextButton(
                    enabled = editedName.length > 3 && try {
                        editedCost.toInt() > 0
                    } catch (_: Exception) {
                        false
                    },
                    onClick = {
                        if (dao.updateBook(book.id, editedName, editedCost.toInt()) == 1) {
                            showSnackbar("${book.name} ????????????????", "OK")
                        }
                        close()
                    }
                ) { Text("??????????????????") }
            }
        }
    }
}

@ExperimentalComposeUiApi
@Composable
fun CreateBriefcaseItemDialog(
    dao: BookDao,
    books: List<Book>,
    showSnackbar: (message: String, actionLabel: String) -> Unit,
    close: () -> Unit,
) {
    var amount by remember { mutableStateOf("0") }
    var currentBook by remember { mutableStateOf(books[0]) }
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf("") }

    var textFieldSize by remember { mutableStateOf(Size.Zero) }

    val interactionSource = remember {
        object : MutableInteractionSource {
            override val interactions = MutableSharedFlow<Interaction>(
                extraBufferCapacity = 16,
                onBufferOverflow = BufferOverflow.DROP_OLDEST,
            )

            override suspend fun emit(interaction: Interaction) {
                if (interaction is PressInteraction.Release) {
                    expanded = !expanded
                }

                interactions.emit(interaction)
            }

            override fun tryEmit(interaction: Interaction): Boolean {
                return interactions.tryEmit(interaction)
            }
        }
    }

    val icon = if (expanded) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown

    Dialog(onDismissRequest = { close() }) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colors.surface)
                .height(intrinsicSize = IntrinsicSize.Min)
                .width(IntrinsicSize.Max)
                //.padding(bottom = 16.dp, end = 24.dp),
                .padding(24.dp, 0.dp, 0.dp, 0.dp)
        ) {
            Box(Modifier.height(56.dp)) {
                Text(
                    "????????????????",
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier
                        .paddingFrom(alignmentLine = FirstBaseline, 40.dp)
                )
            }
            Column {
                OutlinedTextField(
                    value = selectedText,
                    onValueChange = { },
                    readOnly = true,
                    maxLines = 3,
                    //enabled = false,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 24.dp)
                        .onGloballyPositioned { coordinates ->
                            //This value is used to assign to the DropDown the same width
                            textFieldSize = coordinates.size.toSize()
                        },
                    label = { Text("??????????") },
                    trailingIcon = {
                        Icon(
                            imageVector = icon,
                            contentDescription = "contentDescription",
                            modifier = Modifier.clickable { expanded = !expanded }
                        )
                    },
                    interactionSource = interactionSource
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier
                        .height(350.dp)
                        .width(with(LocalDensity.current) { textFieldSize.width.toDp() })
                ) {
                    books.forEach { book ->
                        DropdownMenuItem(
                            onClick = {
                                selectedText = book.name
                                currentBook = book
                                expanded = false
                            }
                        ) {
                            Text(text = book.name)
                        }
                    }
                }
            }
            OutlinedTextField(
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.padding(end = 24.dp, top = 8.dp, bottom = 18.dp),
                value = amount,
                maxLines = 1,
                onValueChange = { amount = if (it.length < 4) it else amount },
                label = { Text("????????????????????") }
            )
            Box(
                contentAlignment = Alignment.BottomEnd,
                modifier = Modifier.fillMaxSize()
            ) {
                TextButton(
                    enabled = try {
                        amount.toInt() > 0
                    } catch (_: Exception) {
                        false
                    },
                    modifier = Modifier.padding(8.dp, 0.dp, 8.dp, 8.dp),
                    onClick = {
                        if (dao.insertBriefcaseItem(
                                currentBook.name,
                                currentBook.cost,
                                amount.toInt()
                            ) > 0
                        ) {
                            showSnackbar("${currentBook.name} ?????????????????? ?? ????????????????", "OK")
                        }
                        close()
                    }
                ) { Text("????????????????") }
            }
        }
    }
}

@ExperimentalComposeUiApi
@Composable
fun EditBriefcaseItemDialog(
    dao: BookDao,
    item: Item,
    showSnackbar: (message: String, actionLabel: String) -> Unit,
    close: () -> Unit
) {
    var amount by remember { mutableStateOf(item.amount.toString()) }
    var isError by remember { mutableStateOf(false) }
    var distributedAmount = amount

    Dialog(onDismissRequest = { close() }) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colors.surface)
                .height(intrinsicSize = if (isError) IntrinsicSize.Min else IntrinsicSize.Min)
                .width(IntrinsicSize.Max)
                .padding(24.dp, 0.dp, 0.dp, 0.dp)
        ) {
            Box(Modifier.height(56.dp)) {
                Text(
                    item.name,
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier
                        .paddingFrom(alignmentLine = FirstBaseline, 40.dp)
                )
            }
            OutlinedTextField(
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.padding(end = 24.dp),
                value = amount,
                maxLines = 1,
                onValueChange = {
                    try {
                        val value = if (it.isNotEmpty()) {
                            it.toInt()
                        } else 0
                        amount = if (it.length < 4) it else amount
                        distributedAmount = value.toString()
                        isError = if (it.isNotEmpty()) value > item.amount else false
                    } catch (ex: NumberFormatException) {
                    }
                },
                label = { Text("????????????????????") },
                isError = isError
            )
            if (isError) {
                Text(
                    text = "???????????????????????? ????????",
                    color = MaterialTheme.colors.error,
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(end = 8.dp, top = if (isError) 0.dp else 18.dp, bottom = 8.dp)
            ) {
                CalendarProvider.getInstance()
                val date = DateHolder(
                    CalendarProvider.day,
                    CalendarProvider.monthNum,
                    CalendarProvider.year
                ).intDate
                TextButton(
                    enabled = try {
                        amount.toInt() > 0 && amount.toInt() <= item.amount
                    } catch (_: Exception) {
                        false
                    },
                    modifier = Modifier.padding(8.dp, 0.dp, 0.dp, 0.dp),
                    onClick = {
                        val resultAmount = item.amount - distributedAmount.toInt()
                        if (resultAmount > 0) {
                            if (dao.updateBriefcaseItem(
                                    item.id,
                                    item.amount - distributedAmount.toInt()
                                ) > 0
                            ) {
                                dao.insertDistributedItem(
                                    item.name,
                                    item.cost,
                                    distributedAmount.toInt(),
                                    date
                                )
                                showSnackbar("?????????? ????????????????????????????", "OK")
                            }
                        } else {
                            if (dao.deleteBriefcaseItem(item) > 0) {
                                dao.insertDistributedItem(item.name, item.cost, item.amount, date)
                                showSnackbar("?????????? ????????????????????????????", "OK")
                            }
                        }
                        close()
                    }
                ) { Text("????????????????????????????") }
            }
        }
    }
}

@ExperimentalComposeUiApi
@Composable
fun DeleteBriefcaseItemDialog(
    dao: BookDao,
    item: Item,
    showSnackbar: (message: String, actionLabel: String) -> Unit,
    close: () -> Unit
) {
    Dialog(onDismissRequest = { close() }) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colors.surface)
                .height(intrinsicSize = IntrinsicSize.Min)
                .width(IntrinsicSize.Max)
                .padding(24.dp, 0.dp, 0.dp, 0.dp)
        ) {
            Box(Modifier.height(56.dp)) {
                Text(
                    "?????????????? ?????????????????",
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier
                        .paddingFrom(alignmentLine = FirstBaseline, 40.dp)
                )
            }
            Text(
                text = "???? ?????????????????????????? ???????????? ?????????????? ???????????????? ???? ????????????????? ?????????????????? ???? ???????????????? ???? ???????????????? ???????????? ????????.",
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(top = 0.dp, end = 24.dp, bottom = 16.dp)
            )
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(end = 8.dp, bottom = 8.dp)
            ) {
                TextButton(
                    modifier = Modifier.padding(8.dp, 0.dp, 0.dp, 0.dp),
                    onClick = {
                        if (dao.deleteBriefcaseItem(item) > 0) {
                            showSnackbar("${item.name} ???????????????? ????????????", "OK")
                        }
                        close()
                    }
                ) { Text("??????????????") }
            }
        }
    }
}

@ExperimentalComposeUiApi
@Composable
fun DeleteDistributedItemDialog(
    dao: BookDao,
    item: DistributedItem,
    showSnackbar: (message: String, actionLabel: String) -> Unit,
    close: (deleted: Boolean) -> Unit
) {
    Dialog(onDismissRequest = { close(false) }) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colors.surface)
                .height(intrinsicSize = IntrinsicSize.Min)
                .width(IntrinsicSize.Max)
                .padding(24.dp, 0.dp, 0.dp, 0.dp)
        ) {
            Box(Modifier.height(56.dp)) {
                Text(
                    "?????????????? ?????????????????",
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier
                        .paddingFrom(alignmentLine = FirstBaseline, 40.dp)
                )
            }
            Text(
                text = "???? ?????????????????????????? ???????????? ?????????????? ???????????????????????????????? ????????????????? ?????????? ???? ???????????????? ?? ???????????? \"????????????????\", ???????? ???????? ???????????????????????????? ???? ??????.",
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(top = 0.dp, end = 24.dp, bottom = 16.dp)
            )
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(end = 8.dp, bottom = 8.dp)
            ) {
                TextButton(
                    modifier = Modifier.padding(8.dp, 0.dp, 0.dp, 0.dp),
                    onClick = {
                        val deleted = dao.deleteDistributedItem(item) > 0
                        if (deleted) {
                            showSnackbar("${item.name} ???????????????????????????????? ???????????????? ????????????", "OK")
                        }
                        close(deleted)
                    }
                ) { Text("??????????????") }
            }
        }
    }
}

@ExperimentalUnitApi
@ExperimentalMaterialApi
@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@Composable
fun AddDistributedItemDialog(
    dao: BookDao,
    books: List<Book>,
    showSnackbar: (message: String, actionLabel: String) -> Unit,
    date: Int,
    close: () -> Unit
) {
    var amount by remember { mutableStateOf("0") }
    var currentBook by remember { mutableStateOf(books[0]) }
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf("") }

    var textFieldSize by remember { mutableStateOf(Size.Zero) }

    val interactionSource = remember {
        object : MutableInteractionSource {
            override val interactions = MutableSharedFlow<Interaction>(
                extraBufferCapacity = 16,
                onBufferOverflow = BufferOverflow.DROP_OLDEST,
            )

            override suspend fun emit(interaction: Interaction) {
                if (interaction is PressInteraction.Release) {
                    expanded = !expanded
                }

                interactions.emit(interaction)
            }

            override fun tryEmit(interaction: Interaction): Boolean {
                return interactions.tryEmit(interaction)
            }
        }
    }

    val icon = if (expanded) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown

    Dialog(onDismissRequest = { close() }) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colors.surface)
                .height(intrinsicSize = IntrinsicSize.Min)
                .width(IntrinsicSize.Max)
                //.padding(bottom = 16.dp, end = 24.dp),
                .padding(24.dp, 0.dp, 0.dp, 0.dp)
        ) {
            Box(Modifier.height(56.dp)) {
                Text(
                    "???????????????? ?? ${DateHolder(date).stringDate()}",
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier
                        .paddingFrom(alignmentLine = FirstBaseline, 40.dp)
                )
            }
            Column {
                OutlinedTextField(
                    value = selectedText,
                    onValueChange = { },
                    readOnly = true,
                    maxLines = 3,
                    //enabled = false,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 24.dp)
                        .onGloballyPositioned { coordinates ->
                            //This value is used to assign to the DropDown the same width
                            textFieldSize = coordinates.size.toSize()
                        },
                    label = { Text("??????????") },
                    trailingIcon = {
                        Icon(
                            imageVector = icon,
                            contentDescription = "contentDescription",
                            modifier = Modifier.clickable { expanded = !expanded }
                        )
                    },
                    interactionSource = interactionSource
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier
                        .height(350.dp)
                        .width(with(LocalDensity.current) { textFieldSize.width.toDp() })
                ) {
                    books.forEach { book ->
                        DropdownMenuItem(
                            onClick = {
                                selectedText = book.name
                                currentBook = book
                                expanded = false
                            }
                        ) {
                            Text(text = book.name)
                        }
                    }
                }
            }
            OutlinedTextField(
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.padding(end = 24.dp, top = 8.dp, bottom = 18.dp),
                value = amount,
                maxLines = 1,
                onValueChange = { amount = if (it.length < 4) it else amount },
                label = { Text("????????????????????") }
            )
            Box(
                contentAlignment = Alignment.BottomEnd,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {
                TextButton(
                    enabled = try {
                        amount.toInt() > 0 && selectedText.isNotEmpty() && selectedText != "??????????"
                    } catch (_: Exception) {
                        false
                    },
                    modifier = Modifier.padding(8.dp, 0.dp, 8.dp, 8.dp),
                    onClick = {
                        if (dao.insertDistributedItem(
                                currentBook.name,
                                currentBook.cost,
                                amount.toInt(),
                                DateHolder(date).intDate
                            ) > 0
                        ) {
                            showSnackbar("${currentBook.name} ????????????????????????????", "????")
//                            coroutineScope.launch {
//                                snackbarHostState.showSnackbar(
//                                    "${currentBook.name} ????????????????????????????",
//                                    "OK",
//                                    SnackbarDuration.Short
//                                )
//                            }
                        }
                        close()
                    }
                ) { Text("????????????????????????????") }
            }
        }
    }
}

@ExperimentalComposeUiApi
@Composable
fun InformThatDateUndefined(close: () -> Unit) {
    Dialog(onDismissRequest = { close() }) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colors.surface)
                .height(intrinsicSize = IntrinsicSize.Min)
                .width(IntrinsicSize.Max)
                .padding(24.dp, 0.dp, 0.dp, 0.dp)
        ) {
            Box(Modifier.height(56.dp)) {
                Text(
                    "????????????",
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier
                        .paddingFrom(alignmentLine = FirstBaseline, 40.dp)
                )
            }
            Text(
                text = "???????? ???? ??????????????. ????????????????????, ???????????????? ???????? ?? ?????????????????? ?? ?????????? ???????????????? ??????????.",
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(top = 0.dp, end = 24.dp, bottom = 16.dp)
            )
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(end = 8.dp, bottom = 8.dp)
            ) {
                TextButton(
                    modifier = Modifier.padding(8.dp, 0.dp, 0.dp, 0.dp),
                    onClick = {
                        close()
                    }
                ) { Text("????") }
            }
        }
    }
}


