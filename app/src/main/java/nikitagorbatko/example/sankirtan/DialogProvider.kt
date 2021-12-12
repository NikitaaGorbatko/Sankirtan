package nikitagorbatko.example.sankirtan

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun BuildDialog(mode: Mode, book: Book?, books: List<Book>, a: () -> Unit) {
    var bookName by remember { mutableStateOf(book?.name ?: "") }
    var cost by remember { mutableStateOf("") }
    val lastId = books.last().id

    AlertDialog(
        onDismissRequest = { a() },
        title = {
            Text(
                text = "Добавление новой книги",
                modifier = Modifier.padding(16.dp, 16.dp, 16.dp, 8.dp),
            )
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
                    //dao.insertBook(Book(lastId + 1, bookName, cost.toInt(),10))
                    a()
                }
            ) { Text("Добавить") }
        },
        dismissButton = {
            Button(onClick = { a() }) { Text("Отменить") }
        }
    )
}

enum class Mode {
    EDITION, CREATION
}

