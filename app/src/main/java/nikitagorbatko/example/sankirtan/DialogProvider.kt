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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import nikitagorbatko.example.sankirtan.ui.theme.SankirtanTheme



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
                onValueChange = { bookName = it },
                label = { Text("Название") }
            )
            OutlinedTextField(
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.padding(bottom = 28.dp, end = 24.dp),
                value = cost,
                onValueChange = {
                    if ((!it.contains("-")) or (!it.contains("."))) cost = it
                },
                label = { Text("Цена") }
            )
            Box(modifier = Modifier.fillMaxSize().padding(end = 8.dp, bottom = 8.dp), contentAlignment = Alignment.BottomEnd) {
                TextButton(
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

    AlertDialog(
        onDismissRequest = { closeLambda(false) },
        title = {
            Text(
                text = "Редактировать книгу",
                modifier = Modifier.padding(16.dp, 16.dp, 16.dp, 8.dp),
            )
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
            TextButton(
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
            ) { Text("СОХРАНИТЬ") }
        },
        dismissButton = {
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
            } }) { Text("УДАЛИТЬ") }
        }
    )
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
        Column (
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colors.surface)
                .height(intrinsicSize = IntrinsicSize.Min)
                .width(IntrinsicSize.Max)
        ) {
            Text("Набор книг",
                modifier = Modifier.padding(24.dp),
                style = MaterialTheme.typography.h6,
                //fontWeight = FontWeight.W600
            )
            Divider()
            //Dropdown
            Row(
                Modifier
                    .clickable { expanded = !expanded }
                    .padding(24.dp, 16.dp, 24.dp, 16.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                //verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = currentBook.name,
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier
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
            OutlinedTextField(
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.padding(24.dp, 0.dp, 24.dp, 36.dp),
                value = amount,
                onValueChange = { amount = if (it.length < 4) it else amount},
                label = { Text("Количество") }
            )
            Column (horizontalAlignment = Alignment.End, modifier = Modifier.width(IntrinsicSize.Max)) {
                Button(
                    modifier = Modifier.padding(8.dp, 0.dp, 8.dp, 8.dp),
                    onClick = {
                        //dao.updateBook(Book(book.id, book.name, book.))
                        val item = Item(1, currentBook.name, currentBook.cost, amount.toInt())
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

//@Composable
//fun EdiItemDialog(
//    dao: BookDao,
//    item: Item,
//    coroutineScope: CoroutineScope,
//    snackbarHostState: SnackbarHostState,
//    closeLambda: (delete: Boolean) -> Unit
//) {
//    var editedName by remember { mutableStateOf(item.name) }
//    var editedCost by remember { mutableStateOf(book.cost.toString()) }
//
//    AlertDialog(
//        onDismissRequest = { closeLambda(false) },
//        title = {
//            Row(horizontalArrangement = Arrangement.SpaceBetween) {
//                Text(
//                    text = "Изменение книги",
//                    modifier = Modifier.padding(16.dp, 16.dp, 16.dp, 8.dp),
//                )
//                IconButton(
//                    onClick = {
//                        if (dao.deleteBook(book) == 1) {
//                            closeLambda(true)
//                            coroutineScope.launch {
//                                snackbarHostState.showSnackbar(
//                                    "${book.name} удалена",
//                                    "ОК",
//                                    SnackbarDuration.Short
//                                )
//                            }
//                        }
//                    }
//                ) { Icon(painterResource(R.drawable.ic_delete), "Delete") }
//            }
//        },
//        text = {
//            Column() {
//                OutlinedTextField(
//                    modifier = Modifier.padding(16.dp, 16.dp, 16.dp, 8.dp),
//                    value = editedName,
//                    onValueChange = { editedName = it },
//                    label = { Text("Название") }
//                )
//                OutlinedTextField(
//                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
//                    modifier = Modifier.padding(16.dp, 0.dp, 16.dp, 0.dp),
//                    value = editedCost,
//                    onValueChange = { if ((it != "-") or (it != ".")) editedCost = it },
//                    label = { Text("Цена") }
//                )
//            }
//        },
//        confirmButton = {
//            Button(
//                onClick = {
//                    //dao.updateBook(Book(book.id, book.name, book.))
//                    if (dao.updateBook(book.id, editedName, editedCost.toInt()) == 1) {
//                        coroutineScope.launch {
//                            snackbarHostState.showSnackbar(
//                                "${book.name} изменена",
//                                "OK",
//                                SnackbarDuration.Short
//                            )
//                        }
//                    }
//                    closeLambda(false)
//                }
//            ) { Text("Изменить") }
//        },
//        dismissButton = {
//            Button(onClick = { closeLambda(false) }) { Text("Отменить") }
//        }
//    )
//}






