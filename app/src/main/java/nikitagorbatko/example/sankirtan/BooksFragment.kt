package nikitagorbatko.example.sankirtan

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@ExperimentalUnitApi
@Composable
fun BooksScreen(books: List<Book>, a: Boolean, onEditDialog: (book: Book) -> Unit) {
    LazyColumn(
        modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 60.dp),
        //contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        //verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        if (a) { items(books) { item: Book -> BookCard(item, onEditDialog) } }
    }
}

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@ExperimentalUnitApi
@Composable
private fun BookCard(book: Book, onClickDialog: (book: Book) -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),//.height(64.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            book.name,
            //color = MaterialTheme.colors.onPrimary,
            fontSize = TextUnit(16f, TextUnitType.Sp),
            modifier = Modifier
                .padding(16.dp, 10.dp, 16.dp, 2.dp)
        )
        Text(
            book.cost.toString() + "₽",
            color = MaterialTheme.colors.onPrimary,
            fontSize = TextUnit(14f, TextUnitType.Sp),
            modifier = Modifier.padding(16.dp, 0.dp, 16.dp, 10.dp)
        )
    }
    Divider()
}

