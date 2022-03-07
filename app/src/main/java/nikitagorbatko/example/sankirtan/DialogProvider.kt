package nikitagorbatko.example.sankirtan

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.lang.Exception
import java.util.*


//Create book dialog
@Composable
fun CreateBookDialog(
    dao: BookDao,
    books: List<Book>,
    coroutineScope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
    close: () -> Unit
) {
    var bookName by remember { mutableStateOf("") }
    var bookCost by remember { mutableStateOf("") }

    Dialog(onDismissRequest = { close() }) {
        Column(modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colors.surface)
            .height(intrinsicSize = IntrinsicSize.Min)
            .width(IntrinsicSize.Max)
            .padding(24.dp, 0.dp, 0.dp, 0.dp)
        ) {

            OutlinedTextField(
                modifier = Modifier.padding(bottom = 8.dp, end = 24.dp),
                value = bookName,
                maxLines = 1,
                onValueChange = { bookName = it },
                label = { Text("Название") }
            )
            OutlinedTextField(
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.padding(bottom = 28.dp, end = 24.dp),
                value = bookCost,
                maxLines = 1,
                onValueChange = { if (it.length < 4) bookCost = it },
                label = { Text("Цена") }
            )
            Box(
                contentAlignment = Alignment.BottomEnd,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(end = 8.dp, bottom = 8.dp)
            ) {
                TextButton(
                    enabled = bookName.length > 3 && try { bookCost.toInt() > 0 } catch (_: Exception) { false },
                    onClick = {
                        if (dao.insertBook(bookName, bookCost.toInt()) > 0) {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar(
                                    "$bookName добавлена",
                                    "OK",
                                    SnackbarDuration.Short
                                )
                            }
                        }
                        close()
                    }
                ) { Text("ДОБАВИТЬ", style = MaterialTheme.typography.button) }
            }
        }
    }
}

//Edit book
@Composable
fun EditBookDialog(
    dao: BookDao,
    book: Book,
    coroutineScope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
    close: () -> Unit
) {
    var editedName by remember { mutableStateOf(book.name) }
    var editedCost by remember { mutableStateOf(book.cost.toString()) }

    Dialog(onDismissRequest = { close() }) {
        Column(modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colors.surface)
            .height(intrinsicSize = IntrinsicSize.Min)
            .width(IntrinsicSize.Max)
            .padding(24.dp, 0.dp, 0.dp, 0.dp)
        ) {
            Box(Modifier.height(56.dp)) {
                Text(
                    text = "Запись #${book.id}",
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
                label = { Text("Название") }
            )
            OutlinedTextField(
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.padding(bottom = 28.dp, end = 24.dp),
                value = editedCost,
                maxLines = 1,
                onValueChange = { if (it.length < 4) editedCost = it },
                label = { Text("Цена") }
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
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(
                                "${book.name} удалена",
                                "ОК",
                                SnackbarDuration.Short
                            )
                        }
                    }
                }) { Text("УДАЛИТЬ") }
                TextButton(
                    enabled = editedName.length > 3 && try { editedCost.toInt() > 0 } catch (_: Exception) { false },
                    onClick = {
                        if (dao.updateBook(book.id, editedName, editedCost.toInt()) == 1) {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar(
                                    "${book.name} изменена",
                                    "OK",
                                    SnackbarDuration.Short
                                )
                            }
                        }
                        close()
                    }
                ) { Text("СОХРАНИТЬ") }
            }
        }
    }
}

@ExperimentalComposeUiApi
@Composable
fun CreateItemDialog(
    dao: BookDao,
    books: List<Book>,
    coroutineScope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
    close: () -> Unit
) {
    var amount by remember { mutableStateOf("0") }
    var currentBook by remember { mutableStateOf(books[0]) }
    var expanded by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = { close() }) {
        Column(modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colors.surface)
            .height(intrinsicSize = IntrinsicSize.Min)
            .width(IntrinsicSize.Max)
            .padding(24.dp, 0.dp, 0.dp, 0.dp)
        ) {
            Box(Modifier.height(56.dp)) {
                Text("Комплект",
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier
                        .paddingFrom(alignmentLine = FirstBaseline, 40.dp)
                )
            }
            Row(
                Modifier
                    .clickable { expanded = !expanded }
                    .padding(bottom = 16.dp, end = 24.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                //verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = currentBook.name,
                    style = MaterialTheme.typography.body1,
                    maxLines = 2,
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .fillMaxWidth(0.9f),
                    )
                Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = "")
                DropdownMenu(expanded = expanded, modifier = Modifier.height(250.dp), onDismissRequest = {
                    expanded = false
                }) {
                    books.forEach { book->
                        DropdownMenuItem(
                            onClick = {
                                expanded = false
                                currentBook = book
                            },
                        ) { Text(text = book.name, style = MaterialTheme.typography.body1) }
                    }
                }
            }
            OutlinedTextField(
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.padding(end = 24.dp, bottom = 0.dp),
                value = amount,
                maxLines = 1,
                onValueChange = { amount = if (it.length < 4) it else amount},
                label = { Text("Количество") }
            )
            Box(
                contentAlignment = Alignment.BottomEnd,
                modifier = Modifier.fillMaxSize()
            ) {
                TextButton(
                    enabled = try { amount.toInt() > 0 } catch (_: Exception) { false },
                    modifier = Modifier.padding(8.dp, 0.dp, 8.dp, 8.dp),
                    onClick = {
                        if (dao.insertItem(currentBook.name, currentBook.cost, amount.toInt()      ) > 0) {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar(
                                    "${currentBook.name} добавлена в портфель",
                                    "OK",
                                    SnackbarDuration.Short
                                )
                            }
                        }
                        close()
                    }
                ) { Text("ДОБАВИТЬ") }
            }
        }
    }
}


@ExperimentalComposeUiApi
@Composable
fun EditItemDialog(
    dao: BookDao,
    item: Item,
    coroutineScope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
    close: () -> Unit
) {
    var amount by remember { mutableStateOf(item.amount.toString()) }
    var isError by remember { mutableStateOf(false) }
    var distributedAmount = amount

    Dialog(onDismissRequest = { close() }) {
        Column(modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colors.surface)
            .height(intrinsicSize = if (isError) IntrinsicSize.Min else IntrinsicSize.Min )
            .width(IntrinsicSize.Max)
            .padding(24.dp, 0.dp, 0.dp, 0.dp)
        ) {
            Box(Modifier.height(56.dp)) {
                Text(item.name,
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier
                        .paddingFrom(alignmentLine = FirstBaseline, 40.dp)
                )
            }
//            Text(
//                text = item.name,
//                style = MaterialTheme.typography.body1,
//                maxLines = 2,
//                modifier = Modifier.padding(top = 0.dp, end = 24.dp, bottom = 16.dp)
//            )
            OutlinedTextField(
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.padding(end = 24.dp),
                value = amount,
                maxLines = 1,
                onValueChange = {
                    val value = if (it.isNotEmpty()) { it.toInt() } else 0
                    amount = if (it.length < 4) it else amount
                    distributedAmount = value.toString()
                    isError = if (it.isNotEmpty()) value > item.amount else false
                },
                label = { Text("Количество") },
                isError = isError
            )
            if (isError) {
                Text(
                    text = "Недостаточно книг",
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
                TextButton(
                    enabled = try { amount.toInt() > 0 && amount.toInt() <= item.amount } catch (_: Exception) { false },
                    modifier = Modifier.padding(8.dp, 0.dp, 0.dp, 0.dp),
                    onClick = {
                        val resultAmount = item.amount - distributedAmount.toInt()
                        if (resultAmount > 0) {
                            if (dao.updateItem(item.id, item.amount - distributedAmount.toInt()) > 0) {
                                dao.insertDistributedItem(item.name, item.cost, distributedAmount.toInt(), CalendarProvider.calendar.time.time)
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar(
                                        "Книги распространены",
                                        "OK",
                                        SnackbarDuration.Short
                                    )
                                }
                            }
                        } else {
                            if (dao.deleteItem(item) > 0) {
                                dao.insertDistributedItem(item.name, item.cost, item.amount, CalendarProvider.calendar.time.time)
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar(
                                        "Книги распространены",
                                        "OK",
                                        SnackbarDuration.Short
                                    )
                                }
                            }
                        }
                        close()
                    }
                ) { Text("РАСПРОСТРАНИТЬ") }
            }
        }
    }
}

@ExperimentalComposeUiApi
@Composable
fun DeleteItemDialog(
    dao: BookDao,
    item: Item,
    coroutineScope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
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
                    "Удалить комплект?",
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier
                        .paddingFrom(alignmentLine = FirstBaseline, 40.dp)
                )
            }
            Text(
                text = "Вы действиетльно хотите удалить комплект из портфеля? Изменение не повлияет на основной список книг.",
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
                        if (dao.deleteItem(item) > 0) {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar(
                                    "${item.name} комплект удален",
                                    "OK",
                                    SnackbarDuration.Short
                                )
                            }
                        }
                        close()
                    }
                ) { Text("УДАЛИТЬ") }
            }
        }
    }
}


