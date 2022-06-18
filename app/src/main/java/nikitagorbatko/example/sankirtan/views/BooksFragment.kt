package nikitagorbatko.example.sankirtan

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import nikitagorbatko.example.sankirtan.room.Book

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@ExperimentalUnitApi
@Composable
fun BooksScreen(books: List<Book>, onEditDialog: (book: Book) -> Unit) {
    LazyColumn(
        modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 60.dp),
        //contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        //verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(books) { book: Book -> BookCard(book, onEditDialog) }
    }
}

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@ExperimentalUnitApi
@Composable
fun BookCard(
    book: Book,
    onEditDialog: (book: Book) -> Unit = {}
) {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .clickable { onEditDialog(book) }
    ) {
        Text(
            book.name,
            maxLines = 1,
            fontSize = TextUnit(16f, TextUnitType.Sp),
            modifier = Modifier.padding(16.dp, 10.dp, 16.dp, 2.dp)
        )
        Text(
            book.cost.toString() + "₽",
            style = MaterialTheme.typography.body2,
            modifier = Modifier.padding(16.dp, 0.dp, 16.dp, 10.dp)
        )
    }
    Divider(modifier = Modifier.padding(start = 16.dp, end = 16.dp))
}

