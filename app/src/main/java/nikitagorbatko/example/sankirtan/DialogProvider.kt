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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

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
    var cost by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = { closeLambda() },
        title = {
            Row {
                Text(
                    text = "Добавление новой книги",
                    modifier = Modifier.padding(16.dp, 16.dp, 16.dp, 8.dp),
                )
            }
        },
        text = {
            Column() {
                OutlinedTextField(
                    modifier = Modifier.padding(16.dp, 16.dp, 16.dp, 8.dp),
                    value = bookName,
                    onValueChange = { bookName = it },
                    label = { Text("Название") }
                )
                OutlinedTextField(
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.padding(16.dp, 0.dp, 16.dp, 0.dp),
                    value = cost,
                    onValueChange = {
                        if ((!it.contains("-")) or (!it.contains("."))) cost = it
                                    },
                    label = { Text("Цена") }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (dao.insertBook(bookName, cost.toInt()) > 0) {
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
            ) { Text("Добавить") }
        },
        dismissButton = {
            Button(onClick = { closeLambda() }) { Text("Отменить") }
        }
    )
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

    AlertDialog(
        onDismissRequest = { closeLambda(false) },
        title = {
            Row(horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    text = "Изменение книги",
                    modifier = Modifier.padding(16.dp, 16.dp, 16.dp, 8.dp),
                )
                IconButton(
                    onClick = {
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
                    }
                ) { Icon(painterResource(R.drawable.ic_delete), "Delete") }
            }
        },
        text = {
            Column() {
                OutlinedTextField(
                    modifier = Modifier.padding(16.dp, 16.dp, 16.dp, 8.dp),
                    value = editedName,
                    onValueChange = { editedName = it },
                    label = { Text("Название") }
                )
                OutlinedTextField(
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.padding(16.dp, 0.dp, 16.dp, 0.dp),
                    value = editedCost,
                    onValueChange = { if ((it != "-") or (it != ".")) editedCost = it },
                    label = { Text("Цена") }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    //dao.updateBook(Book(book.id, book.name, book.))
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
            ) { Text("Изменить") }
        },
        dismissButton = {
            Button(onClick = { closeLambda(false) }) { Text("Отменить") }
        }
    )
}

@ExperimentalComposeUiApi
@Composable
fun AddBookDialog(
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
        Column (
            Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colors.surface)
        ) {
            Text("Положить в портфель",
                modifier = Modifier.padding(16.dp),
                fontSize = 20.sp,
                fontWeight = FontWeight.W600
            )
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(380.dp),
                //contentAlignment = Alignment.Center,
            ) {
                //Dropdown
                Row(
                    Modifier
                        .padding(24.dp)
                        .clickable { expanded = !expanded }
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    //verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(text = currentBook.name, fontSize = 18.sp, modifier = Modifier
                        .padding(end = 8.dp)
                        .fillMaxWidth(0.9f), maxLines = 2)
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
                            ) { Text(text = book.name) }
                        }
                    }
                }

            }
            OutlinedTextField(
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.padding(16.dp, 16.dp, 16.dp, 8.dp),
                value = amount,
                onValueChange = { amount = it },
                label = { Text("Количество") }
            )

            Button(
                onClick = {
                    //dao.updateBook(Book(book.id, book.name, book.))
                    if (dao.insertItem(currentBook.name, currentBook.cost, amount.toInt()) == 1L) {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(
                                "${currentBook.name} добавлена",
                                "OK",
                                SnackbarDuration.Short
                            )
                        }
                    }
                    closeLambda()
                }
            ) { Text("Изменить") }

        }
    }
}






