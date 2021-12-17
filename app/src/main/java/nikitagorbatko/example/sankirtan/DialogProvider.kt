package nikitagorbatko.example.sankirtan

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

//Create book dialog
@Composable
fun CreateBookDialog(dao: BookDao, books: List<Book>, closeLambda: () -> Unit) {
    var bookName by remember { mutableStateOf("") }
    var cost by remember { mutableStateOf("") }
    val lastId = books.last().id

    AlertDialog(
        onDismissRequest = { closeLambda() },
        title = {
            Row(horizontalArrangement = Arrangement.SpaceBetween) {
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
                    onValueChange = { if ((it != "-") or (it != ".")) cost = it },
                    label = { Text("Цена") }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    dao.insertBook(bookName, cost.toInt(), 10)
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
//@Composable
//fun EditBookDialog(dao: BookDao, book: Book, closeLambda: (delete: Boolean) -> Unit) {
//    var editedName by remember { mutableStateOf(book.name) }
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
//                        closeLambda(true)
//                        dao.deleteBook(book)
//                    }
//                ) { Icon(Icons.Filled.Delete, "Delete") }
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
//                    dao.insertBook(Book(bool + 1, editedName, editedCost.toInt(),0))
//                    closeLambda(false)
//                }
//            ) { Text("Добавить") }
//        },
//        dismissButton = {
//            Button(onClick = { closeLambda(false) }) { Text("Отменить") }
//        }
//    )
//}




