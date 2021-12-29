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
import java.lang.Exception


//Create book dialog
@Composable
fun CreateBookDialog(
    dao: BookDao,
    books: List<Book>,
    coroutineScope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
    closeLambda: () -> Unit
) {
    var bookName by remember { mutableStateOf("") }
    var bookCost by remember { mutableStateOf("") }

    Dialog(onDismissRequest = { closeLambda() }) {
        Column(modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colors.surface)
            .height(intrinsicSize = IntrinsicSize.Min)
            .width(IntrinsicSize.Max)
            .padding(24.dp, 0.dp, 0.dp, 0.dp)
        ) {
            Box(Modifier.height(56.dp)) {
                Text(
                    text = "Книга",
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier
                        .paddingFrom(alignmentLine = FirstBaseline, 40.dp)
                )
            }
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
                        closeLambda()
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
    closeLambda: (delete: Boolean) -> Unit
) {
    var editedName by remember { mutableStateOf(book.name) }
    var editedCost by remember { mutableStateOf(book.cost.toString()) }

    Dialog(onDismissRequest = { closeLambda(false) }) {
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
                        closeLambda(true)
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
                        closeLambda(false)
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
    closeLambda: () -> Unit
) {
    var amount by remember { mutableStateOf("0") }
    var currentBook by remember { mutableStateOf(books[0]) }
    var expanded by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = { closeLambda() }) {
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
            Row(Modifier
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
                        if (dao.insertItem(currentBook.name, currentBook.cost, amount.toInt()) > 0) {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar(
                                    "${currentBook.name} добавлена в портфель",
                                    "OK",
                                    SnackbarDuration.Short
                                )
                            }
                        }
                        closeLambda()
                    }
                ) { Text("ДОБАВИТЬ") }
            }
        }
    }
}



